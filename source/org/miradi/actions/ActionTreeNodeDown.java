/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.DownArrowIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionTreeNodeDown extends ObjectsAction
{
	public ActionTreeNodeDown(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new DownArrowIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Tree|Move Item Down");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Move the selected item down");
	}
}
