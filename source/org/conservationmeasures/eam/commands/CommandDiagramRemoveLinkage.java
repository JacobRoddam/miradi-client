/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class CommandDiagramRemoveLinkage extends Command
{
	public CommandDiagramRemoveLinkage(BaseId idToDelete)
	{
		id = idToDelete;
		wasFrom = new ModelNodeId(BaseId.INVALID.asInt());
		wasTo = new ModelNodeId(BaseId.INVALID.asInt());
	}
	
	public ModelNodeId getWasFromId()
	{
		return wasFrom;
	}
	
	public ModelNodeId getWasToId()
	{
		return wasTo;
	}

	public String toString()
	{
		return getCommandName() + ":" + getId() + "," + getWasFromId() + "," + getWasToId();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			BaseId idToDelete = id;
			DiagramLinkage linkageToDelete = model.getLinkageById(idToDelete);
			wasFrom = linkageToDelete.getFromNode().getWrappedId();
			wasTo = linkageToDelete.getToNode().getWrappedId();
			deleteLinkage(target, linkageToDelete.getDiagramLinkageId());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void undo(Project target) throws CommandFailedException
	{
		try
		{
			CommandDiagramAddLinkage.createLinkage(target, getId(), getWasFromId(), getWasToId());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public BaseId getId()
	{
		return id;
	}

	public static void deleteLinkage(Project target, DiagramLinkageId idToDelete) throws Exception, IOException, ParseException
	{
		target.removeLinkageFromDiagram(idToDelete);
		target.deleteObject(ObjectType.MODEL_LINKAGE, idToDelete);
	}
	

	public static final String COMMAND_NAME = "DiagramRemoveLinkage";

	BaseId id;
	ModelNodeId wasFrom;
	ModelNodeId wasTo;
}
