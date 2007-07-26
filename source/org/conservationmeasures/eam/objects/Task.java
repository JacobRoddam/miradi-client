/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.views.budget.BudgetTotalsCalculator;

public class Task extends BaseObject
{
	public Task(ObjectManager objectManager, BaseId idToUse) throws Exception
	{
		super(objectManager, new TaskId(idToUse.asInt()));
		clear();
	}
	
	public Task(BaseId idToUse) throws Exception
	{
		super(new TaskId(idToUse.asInt()));
		clear();
	}
	
	public Task(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new TaskId(idAsInt), json);
	}
	
	
	public Task(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new TaskId(idAsInt), json);
	}
	
	
	public Vector getDeleteSelfAndSubtasksCommands(Project project) throws Exception
	{
		Vector deleteIds = new Vector();
		deleteIds.add(new CommandSetObjectData(getType(), getId(), Task.TAG_SUBTASK_IDS, ""));
		int subTaskCount = getSubtaskCount();
		for (int index = 0; index < subTaskCount; index++)
		{
			BaseId subTaskId = getSubtaskId(index);
			Task  subTask = (Task)project.findObject(ObjectType.TASK, subTaskId);
			Vector returnedDeleteCommands = subTask.getDeleteSelfAndSubtasksCommands(project);
			deleteIds.addAll(returnedDeleteCommands);
			
		}
		
		deleteIds.addAll(Arrays.asList(createCommandsToClear()));
		deleteIds.add(new CommandDeleteObject(getType(), getId()));
		
		return deleteIds;
	}
	
	public Command[] createCommandToFixupRefLists(HashMap oldToNewRefMap) throws Exception
	{	
		Command commandToFixSubTaskRefs = fixUpRefs(oldToNewRefMap, Task.getObjectType(), TAG_SUBTASK_IDS);	
		return new Command[] {commandToFixSubTaskRefs};
	}

	public int getType()
	{
		return getObjectType();
	}

	public static int getObjectType()
	{
		return ObjectType.TASK;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		switch(type)
		{
			case ObjectType.TASK: 
				return true;
			case ObjectType.ASSIGNMENT: 
				return true;
			default:
				return false;
		}
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	

	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.TASK: 
				list.addAll(new ORefList(objectType, getSubtaskIdList()));
				break;
			case ObjectType.ASSIGNMENT: 
				list.addAll(new ORefList(objectType, getAssignmentIdList()));
				break;
		}
		return list;
	}
	
	//NOTE: this is not testing if this is a Task object...
	//but if it is a user level task as opposed to a method or an activity
	public boolean isTask()
	{
		if (getOwnerRef() == null)
			return false;
		
		return getOwnerRef().getObjectType() == ObjectType.TASK;
	}

	public boolean isActivity()
	{
		if (getOwnerRef() == null)
			return false;
		
		return Factor.isFactor(getOwnerRef().getObjectType());
	}

	public boolean isMethod()
	{
		if (getOwnerRef() == null)
			return false;
		
		return getOwnerRef().getObjectType() == ObjectType.INDICATOR;
	}

	public void addSubtaskId(BaseId subtaskId)
	{
		subtaskIds.add(subtaskId);
	}
	
	public int getSubtaskCount()
	{
		return subtaskIds.size();
	}
	
	public BaseId getSubtaskId(int index)
	{
		return subtaskIds.get(index);
	}
	
	public IdList getSubtaskIdList()
	{
		return subtaskIds.getIdList().createClone();
	}
	
	public IdList getAssignmentIdList()
	{
		return assignmentIds.getIdList().createClone();
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_STRATEGY_LABEL))
			return getLabelOfTaskParent();
		
		if(fieldTag.equals(PSEUDO_TAG_INDICATOR_LABEL))
			return getLabelOfTaskParent();
		
		if (fieldTag.equals(PSEUDO_TAG_SUBTASK_TOTAL))
			return getSubtaskTotalCost();
		
		if (fieldTag.equals(PSEUDO_TAG_TASK_TOTAL))
			return getTaskTotalCost();
		
		if (fieldTag.equals(PSEUDO_TAG_TASK_COST))
			return getTaskCost();
		
		return super.getPseudoData(fieldTag);
	}



	private String getTaskCost()
	{
		try
		{
			BudgetTotalsCalculator calculator = new BudgetTotalsCalculator(objectManager.getProject());
			double cost = calculator.getTaskCost((TaskId)getId());
			return formateResults(cost);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "0";
		}
	}

	private String getSubtaskTotalCost()
	{
		try
		{
			BudgetTotalsCalculator calculator = new BudgetTotalsCalculator(objectManager.getProject());
			double subtaskTotalCost = calculator.getTotalTasksCost(getSubtaskIdList());
			return formateResults(subtaskTotalCost);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "0";
		}
	}

	private String getTaskTotalCost()
	{		
		try
		{
			BudgetTotalsCalculator calculator = new BudgetTotalsCalculator(objectManager.getProject());
			double totalTaskCost = calculator.getTotalTaskCost((TaskId)getId());
			return formateResults(totalTaskCost);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "0";
		}
	}

	private String formateResults(double cost)
	{
		DecimalFormat formater = objectManager.getProject().getCurrencyFormatter();
		return formater.format(cost);
	}


	private String getLabelOfTaskParent()
	{

		BaseObject parent = objectManager.findObject(getOwnerRef());
		if(parent == null)
		{
			EAM.logDebug("Parent of task " + getId() + " not found: " + getOwnerRef());
			return "(none)";
		}
		return parent.getData(BaseObject.TAG_LABEL);
	}

	
	public void clear()
	{
		super.clear();
		subtaskIds = new IdListData();
		assignmentIds = new IdListData();
		strategyLabel = new PseudoStringData(PSEUDO_TAG_STRATEGY_LABEL);
		indicatorLabel = new PseudoStringData(PSEUDO_TAG_INDICATOR_LABEL);
		subtaskTotal = new PseudoStringData(PSEUDO_TAG_SUBTASK_TOTAL);
		taskTotal = new PseudoStringData(PSEUDO_TAG_TASK_TOTAL);
		taskCost = new PseudoStringData(PSEUDO_TAG_TASK_COST);
		
		addField(TAG_SUBTASK_IDS, subtaskIds);
		addField(TAG_ASSIGNMENT_IDS, assignmentIds);
		addField(PSEUDO_TAG_STRATEGY_LABEL, strategyLabel);
		addField(PSEUDO_TAG_INDICATOR_LABEL, indicatorLabel);
		addField(PSEUDO_TAG_SUBTASK_TOTAL, subtaskTotal);
		addField(PSEUDO_TAG_TASK_TOTAL, taskTotal);
		addField(PSEUDO_TAG_TASK_COST, taskCost);
	}


	public final static String TAG_SUBTASK_IDS = "SubtaskIds";
	public final static String TAG_ASSIGNMENT_IDS = "AssignmentIds";
	public final static String PSEUDO_TAG_STRATEGY_LABEL = "StrategyLabel";
	public final static String PSEUDO_TAG_INDICATOR_LABEL = "IndicatorLabel";
	public final static String PSEUDO_TAG_SUBTASK_TOTAL = "SubtaskTotal";
	public final static String PSEUDO_TAG_TASK_TOTAL = "TaskTotal";
	public final static String PSEUDO_TAG_TASK_COST = "TaskCost";
	
	IdListData subtaskIds;
	IdListData assignmentIds;
	PseudoStringData strategyLabel;
	PseudoStringData indicatorLabel;
	PseudoStringData subtaskTotal;
	PseudoStringData taskTotal;
	PseudoStringData taskCost;
}
