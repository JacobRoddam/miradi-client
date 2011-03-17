/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.PlanningTaskNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeBaseObjectNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeErrorNode;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Desire;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.FundingSource;
import org.miradi.objects.Goal;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

abstract public class AbstractTreeRebuilder
{
	public AbstractTreeRebuilder(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse)
	{
		project = projectToUse;
		rowColumnProvider = rowColumnProviderToUse;
	}
	
	public void rebuildTree(AbstractPlanningTreeNode rootNode) throws Exception
	{
		CodeList rows = getRowColumnProvider().getRowCodesToShow();
		rebuildTree(rootNode, null, rows);
		deleteUnclesAndTheirChildren(rootNode);
		removeUnwantedLayersAndPromoteChildren(rootNode, rows);
	}

	private void rebuildTree(AbstractPlanningTreeNode parentNode, DiagramObject diagram, CodeList rows)
	{
		try
		{
			parentNode.clearChildren();
			ORef parentRef = parentNode.getObjectReference();
			if(DiagramObject.isDiagramObject(parentRef))
				diagram = DiagramObject.findDiagramObject(getProject(), parentRef);

			ORefList candidateChildRefs = getChildRefs(parentNode.getObjectReference(), diagram);
			candidateChildRefs.addAll(parentNode.getObject().getResourceAssignmentRefs());
			candidateChildRefs.addAll(parentNode.getObject().getExpenseAssignmentRefs());
			ORefList childRefs = getListWithoutChildrenThatWouldCauseRecursion(parentNode, candidateChildRefs);
			createAndAddChildren(parentNode, childRefs);

			for(int index = 0; index < parentNode.getChildCount(); ++index)
			{
				AbstractPlanningTreeNode childNode = (AbstractPlanningTreeNode) parentNode.getChild(index);
				rebuildTree(childNode, diagram, rows);
			}
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	private ORefList getListWithoutChildrenThatWouldCauseRecursion(AbstractPlanningTreeNode parentNode, ORefList candidateChildRefs) throws Exception
	{
		TreeTableNode node = parentNode;
		ORefList hierarchySoFar = new ORefList();
		while(node != null)
		{
			hierarchySoFar.add(node.getObjectReference());
			node = node.getParentNode();
		}
	
		ORefList remainingChildren = new ORefList();
		for(int i = 0; i < candidateChildRefs.size(); ++i)
		{
			ORef childRef = candidateChildRefs.get(i);
			if(!hierarchySoFar.contains(childRef))
				remainingChildren.add(childRef);
		}
		return remainingChildren;
	}

	protected boolean shouldTargetsBeAtSameLevelAsDiagrams() throws Exception
	{
		return getRowColumnProvider().shouldPutTargetsAtTopLevelOfTree();
	}
	
	public static ORefList findRelevantGoals(Project projectToUse, ORef strategyRef) throws Exception
	{
		return Desire.findRelevantDesires(projectToUse, strategyRef, Goal.getObjectType());
	}

	public static ORefList findRelevantObjectives(Project projectToUse, ORef strategyRef) throws Exception
	{
		return Desire.findRelevantDesires(projectToUse, strategyRef, Objective.getObjectType());
	}
	
	private void createAndAddChildren(AbstractPlanningTreeNode parent, ORefList childRefsToAdd) throws Exception
	{
		for(int index = 0; index < childRefsToAdd.size(); ++index)
		{
			ORef childRef = childRefsToAdd.get(index);
			createAndAddChild(parent, childRef);
		}
	}

	private void createAndAddChild(AbstractPlanningTreeNode parent, ORef childRefToAdd) throws Exception
	{
		AbstractPlanningTreeNode childNode = createChildNode(parent, childRefToAdd);
		parent.addChild(childNode);
	}

	private AbstractPlanningTreeNode createChildNode(AbstractPlanningTreeNode parentNode, ORef refToAdd) throws Exception
	{
		int[] supportedTypes = new int[] {
			ConceptualModelDiagram.getObjectType(),
			ResultsChainDiagram.getObjectType(),
			
			Strategy.getObjectType(),
			Target.getObjectType(),
			HumanWelfareTarget.getObjectType(),
			Cause.getObjectType(),
			IntermediateResult.getObjectType(),
			ThreatReductionResult.getObjectType(),
			
			SubTarget.getObjectType(),
			Goal.getObjectType(),
			Objective.getObjectType(),
			Indicator.getObjectType(),
			Task.getObjectType(),
			
			Measurement.getObjectType(),
			ResourceAssignment.getObjectType(),
			ExpenseAssignment.getObjectType(),
		};
		
		try
		{
			if(Task.is(refToAdd))
				return new PlanningTaskNode(getProject(), parentNode.getContextRef(), parentNode, refToAdd);
			
			int type = refToAdd.getObjectType();
			for(int i = 0; i < supportedTypes.length; ++i)
			{
				if(type == supportedTypes[i])
					return new PlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			}
			throw new Exception("Attempted to create node of unknown type: " + refToAdd);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new PlanningTreeErrorNode(getProject(), parentNode, refToAdd);
		}
	}
	
	private void deleteUnclesAndTheirChildren(AbstractPlanningTreeNode rootNode)
	{
		Vector<AbstractPlanningTreeNode> childrenToKeep = new Vector<AbstractPlanningTreeNode>();
		for(AbstractPlanningTreeNode childNode : rootNode.getRawChildrenByReference())
		{
			boolean keepThisChild = true;
			for(AbstractPlanningTreeNode otherChildNode : rootNode.getRawChildrenByReference())
			{
				if(childNode.equals(otherChildNode))
				{
					continue;
				}
				if(otherChildNode.getRawChildrenByReference().contains(childNode))
				{
					keepThisChild = false;
					break;
				}
			}
			
			if(keepThisChild)
			{
				childrenToKeep.add(childNode);
				deleteUnclesAndTheirChildren(childNode);
			}
		}
		
		rootNode.setRawChildren(childrenToKeep);
	}

	private void removeUnwantedLayersAndPromoteChildren(AbstractPlanningTreeNode node, CodeList objectTypesToShow)
	{
		if(node.isAnyChildAllocated())
			node.setAllocated();
		
		Vector<AbstractPlanningTreeNode> newChildren = new Vector<AbstractPlanningTreeNode>();
		for(int i = 0; i < node.getChildCount(); ++i)
		{
			AbstractPlanningTreeNode child = (AbstractPlanningTreeNode) node.getChild(i);
			removeUnwantedLayersAndPromoteChildren(child, objectTypesToShow);
			
			boolean isChildVisible = objectTypesToShow.contains(child.getObjectTypeName());
			if(isChildVisible)
			{
				mergeChildIntoList(newChildren, child);
			}
			else
			{
				addChildrenOfNodeToList(newChildren, child);
			}
		}

		possiblySortChildren(node, newChildren);
		
		node.setRawChildren(newChildren);
	}
	
	private void mergeChildIntoList(Vector<AbstractPlanningTreeNode> destination, AbstractPlanningTreeNode newChild)
	{
		AbstractPlanningTreeNode existingNode = findNodeWithRef(destination, newChild.getObjectReference());
		if(existingNode == null)
		{
			if (isChildOfAnyNodeInList(destination, newChild))
				return;

			destination.add(newChild);
			return;
		}
		
		destination = existingNode.getRawChildrenByReference();
		addChildrenOfNodeToList(destination, newChild);

		existingNode.addProportionShares(newChild);

		possiblySortChildren(existingNode, destination);
	}

	protected void possiblySortChildren(AbstractPlanningTreeNode existingNode, Vector<AbstractPlanningTreeNode> destination)
	{
		if(shouldSortChildren(existingNode))
			Collections.sort(destination, createNodeSorter());
	}
	
	private boolean isChildOfAnyNodeInList(Vector<AbstractPlanningTreeNode> destination, AbstractPlanningTreeNode newChild)
	{
		for(AbstractPlanningTreeNode parentNode : destination)
		{
			Vector<AbstractPlanningTreeNode> children = parentNode.getRawChildrenByReference();
			AbstractPlanningTreeNode foundMatchingChild = findNodeWithRef(children, newChild.getObjectReference());
			if (foundMatchingChild != null)
				return true;
		}
		
		return false;
	}
	
	private AbstractPlanningTreeNode findNodeWithRef(Vector<AbstractPlanningTreeNode> list, ORef ref)
	{
		for(AbstractPlanningTreeNode node : list)
		{
			if(ref.equals(node.getObjectReference()))
				return node;
		}
		
		return null;
	}
	
	private void addChildrenOfNodeToList(Vector<AbstractPlanningTreeNode> destination, AbstractPlanningTreeNode otherNode)
	{
		for(AbstractPlanningTreeNode newChild : otherNode.getRawChildrenByReference())
		{
			mergeChildIntoList(destination, newChild);
		}
	}

	private boolean shouldSortChildren(AbstractPlanningTreeNode parentNode)
	{
		ORef parentRef = parentNode.getObjectReference();
		if(Task.is(parentRef))
			return false;
		
		if(Strategy.is(parentRef))
			return false;
		
		if(Indicator.is(parentRef))
			return false;
		
		return true;
	}

	private NodeSorter createNodeSorter()
	{
		return new NodeSorter();
	}

	private class NodeSorter implements Comparator<AbstractPlanningTreeNode>
	{
		public int compare(AbstractPlanningTreeNode nodeA, AbstractPlanningTreeNode nodeB)
		{

			int typeSortLocationA = getTypeSortLocation(nodeA.getType());
			int typeSortLocationB = getTypeSortLocation(nodeB.getType());
			int diff = typeSortLocationA - typeSortLocationB;
			if(diff != 0)
				return diff;

			ORef refA = nodeA.getObjectReference();
			ORef refB = nodeB.getObjectReference();
			if(refA.isValid() && refB.isInvalid())
				return -1;
			
			if(refA.isInvalid() && refB.isValid())
				return 1;
			
			String labelA = nodeA.toString();
			String labelB = nodeB.toString();
			return labelA.compareToIgnoreCase(labelB);
		}
		
		private int getTypeSortLocation(int type)
		{
			int[] sortOrder = getNodeSortOrder();
			for(int index = 0; index < sortOrder.length; ++index)
			{
				if(type == sortOrder[index])
				{
					return index;
				}
			}
			
			EAM.logError("NodeSorter unknown type: " + type);
			return sortOrder.length;
		}
	}
	
	private int[] getNodeSortOrder()
	{
		return new int[] {
			Target.getObjectType(),
			HumanWelfareTarget.getObjectType(),
			ResultsChainDiagram.getObjectType(),
			ConceptualModelDiagram.getObjectType(),
			Goal.getObjectType(),
			SubTarget.getObjectType(),
			Cause.getObjectType(),
			ThreatReductionResult.getObjectType(),
			IntermediateResult.getObjectType(),
			Objective.getObjectType(),
			Strategy.getObjectType(),
			Indicator.getObjectType(),
			ProjectResource.getObjectType(),
			AccountingCode.getObjectType(),
			FundingSource.getObjectType(),
			BudgetCategoryOne.getObjectType(),
			BudgetCategoryTwo.getObjectType(),
			Task.getObjectType(),
			Measurement.getObjectType(),
			ResourceAssignment.getObjectType(),
			ExpenseAssignment.getObjectType(),
		};
	}
	
	protected PlanningTreeRowColumnProvider getRowColumnProvider()
	{
		return rowColumnProvider;
	}

	protected Project getProject()
	{
		return project;
	}
	
	abstract protected ORefList getChildRefs(ORef parentRef, DiagramObject diagram) throws Exception;

	private Project project;
	private PlanningTreeRowColumnProvider rowColumnProvider;
}