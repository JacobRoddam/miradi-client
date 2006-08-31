/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Rectangle;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetNodeSize;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramCluster;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ConceptualModelCluster;

public class NodeMoveHandler
{
	public NodeMoveHandler(Project projectToUse)
	{
		project = projectToUse;
	}

	public void nodesWereMovedOrResized(int deltaX, int deltaY, BaseId[] ids) throws CommandFailedException
	{
		DiagramModel model = getProject().getDiagramModel();
		model.nodesWereMoved(ids);

		Vector commandsToRecord = new Vector();
		Vector commandsToExecute = new Vector();
		Vector movedNodes = new Vector();
		for(int i = 0 ; i < ids.length; ++i)
		{
			try 
			{
				DiagramNode node = model.getNodeById(ids[i]);
				if(node.getParent() != null)
					commandsToExecute.addAll(buildDetachFromClusterCommand(node));
				if(node.getParent() == null)
					commandsToExecute.addAll(buildAttachToClusterCommand(node));
				
				if(node.hasMoved())
					movedNodes.add(node);
				
				if(node.sizeHasChanged())
					commandsToRecord.add(buildResizeCommand(node));
			} 
			catch (Exception e) 
			{
				EAM.logException(e);
			}
		}
		
		if(movedNodes.size() > 0)
		{
			BaseId[] idsActuallyMoved = new BaseId[movedNodes.size()];
			for(int i = 0; i < movedNodes.size(); ++i)
			{
				DiagramNode node = (DiagramNode)movedNodes.get(i);
				idsActuallyMoved[i] = node.getId();
			}
			
			commandsToRecord.add(new CommandDiagramMove(deltaX, deltaY, idsActuallyMoved));
		}
		
		if(commandsToRecord.size() > 0 || commandsToExecute.size() > 0)
		{
			getProject().recordCommand(new CommandBeginTransaction());
			for(int i=0; i < commandsToRecord.size(); ++i)
				getProject().recordCommand((Command)commandsToRecord.get(i));
			for(int i=0; i < commandsToExecute.size(); ++i)
				getProject().executeCommand((Command)commandsToExecute.get(i));
			getProject().recordCommand(new CommandEndTransaction());
		}
		
		/*
		 * NOTE: The following chunk of code works around a weird bug deep in jgraph
		 * If you click on a cluster, then click on a member, then drag the member out,
		 * part of jgraph still thinks the cluster has a member that is selected.
		 * So when you drag the node back in, it doesn't become a member because jgraph 
		 * won't return the cluster, because it thinks the cluster has something selected.
		 * The workaround is to re-select what is selected, so the cached values inside 
		 * jgraph get reset to their proper values.
		 */
		MainWindow mainWindow = EAM.mainWindow;
		if(mainWindow != null)
		{
			DiagramComponent diagram = mainWindow.getDiagramComponent();
			diagram.setSelectionCells(diagram.getSelectionCells());
		}

	}
	
	private List buildAttachToClusterCommand(DiagramNode node) throws Exception
	{
		Vector result = new Vector();
		if(node.isCluster())
			return result;
		
		DiagramCluster cluster = getFirstClusterThatContains(node.getRectangle());
		if(cluster == null)
			return result;
		
		// FIXME: It looks wrong to mix commands with a non-command call like addNodeToCluster()
		getProject().addNodeToCluster(cluster, node);
		CommandSetObjectData cmd = CommandSetObjectData.createAppendIdCommand(cluster.getUnderlyingObject(), 
				ConceptualModelCluster.TAG_MEMBER_IDS, node.getId());
		result.add(cmd);
		return result;
	}
	
	private List buildDetachFromClusterCommand(DiagramNode node) throws ParseException
	{
		Vector result = new Vector();
		DiagramCluster cluster = (DiagramCluster)node.getParent();
		if(cluster.getRectangle().contains(node.getRectangle()))
			return result;
		
		// FIXME: It looks wrong to mix commands with a non-command call like removeNodeFromCluster()
		getProject().removeNodeFromCluster(cluster, node);
		CommandSetObjectData cmd = CommandSetObjectData.createRemoveIdCommand(cluster.getUnderlyingObject(), 
				ConceptualModelCluster.TAG_MEMBER_IDS, node.getId());
		result.add(cmd);
		return result;
	}
	
	private DiagramCluster getFirstClusterThatContains(Rectangle candidateRect) throws Exception
	{
		DiagramModel model = getProject().getDiagramModel();
		BaseId[] allNodeIds = getProject().getNodePool().getIds();
		for(int i = 0; i < allNodeIds.length; ++i)
		{
			DiagramNode possibleCluster = model.getNodeById(allNodeIds[i]);
			if(!possibleCluster.isCluster())
				continue;
			
			if(possibleCluster.getRectangle().contains(candidateRect))
				return (DiagramCluster)possibleCluster;
		}
		
		return null;
	}
	
	private Command buildResizeCommand(DiagramNode node)
	{
		return new CommandSetNodeSize(node.getId(), node.getSize(), node.getPreviousSize());
	}
	

	Project getProject()
	{
		return project;
	}
	
	Project project;
}
