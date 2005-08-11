/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandDeleteLinkage;
import org.conservationmeasures.eam.commands.CommandDeleteNode;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionDelete extends ProjectAction
{
	public ActionDelete(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/delete.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Delete");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		preformDelete();
	}

	public void preformDelete() throws CommandFailedException 
	{
		EAMGraphCell[] selectedRelatedCells = getSelectedAndRelatedCells();
		
		for(int i=0; i < selectedRelatedCells.length; ++i)
		{
			EAMGraphCell cell = selectedRelatedCells[i];
			if(cell.isLinkage())
				deleteLinkage((Linkage)cell);	
		}

		for(int i=0; i < selectedRelatedCells.length; ++i)
		{
			EAMGraphCell cell = selectedRelatedCells[i];
			if(cell.isNode())
				deleteNode((Node)cell);
		}
	}

	
	private void deleteLinkage(Linkage linkageToDelete) throws CommandFailedException
	{
		BaseProject project = getProject();
		int id = linkageToDelete.getId();
		CommandDeleteLinkage command = new CommandDeleteLinkage(id);
		project.executeCommand(command);
	}

	private void deleteNode(Node nodeToDelete) throws CommandFailedException
	{
		BaseProject project = getProject();
		int id = nodeToDelete.getId();

		Point location = nodeToDelete.getLocation();
		CommandDiagramMove moveToZeroZero = new CommandDiagramMove(-location.x, -location.y, new int[] {id});
		CommandSetNodeText clearText = new CommandSetNodeText(id, "");
		CommandDeleteNode command = new CommandDeleteNode(id);
		
		project.executeCommand(moveToZeroZero);
		project.executeCommand(clearText);
		project.executeCommand(command);
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete the selection");
	}

	public boolean shouldBeEnabled()
	{
		if(!getProject().isOpen())
			return false;

		// TODO: Note that changing the selection DOESN'T currently 
		// call this method
		EAMGraphCell[] selected = getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

}

