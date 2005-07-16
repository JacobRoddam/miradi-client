/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.main.ConnectionPropertiesDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;

public class ActionInsertConnection extends MainWindowAction
{
	public ActionInsertConnection(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Connection...");
	}

	public void actionPerformed(ActionEvent event)
	{
		ConnectionPropertiesDialog dialog = new ConnectionPropertiesDialog(getMainWindow());
		dialog.show();
		if(!dialog.getResult())
			return;
		
		DiagramModel model = getMainWindow().getProject().getDiagramModel();
		int fromIndex = model.getNodeId(dialog.getFrom());
		int toIndex = model.getNodeId(dialog.getTo());
		
		if(fromIndex == toIndex)
		{
			String[] body = {EAM.text("Can't link a node to itself"), };
			getMainWindow().okDialog(EAM.text("Can't Create Link"), body);
			return;
		}
		
		if(model.hasLinkage(dialog.getFrom(), dialog.getTo()))
		{
			String[] body = {EAM.text("Those nodes are already linked"), };
			getMainWindow().okDialog(EAM.text("Can't Create Link"), body);
			return;
		}
		
		CommandLinkNodes command = new CommandLinkNodes(fromIndex, toIndex);
		getMainWindow().getProject().executeCommand(command);
	}

}
