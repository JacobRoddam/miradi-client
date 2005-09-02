/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionRedo extends MainWindowAction
{
	public ActionRedo(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/redo.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Redo");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Redo last undone action");
	}

}
