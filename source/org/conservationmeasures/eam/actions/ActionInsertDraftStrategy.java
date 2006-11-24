/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.DraftStrategyIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertDraftStrategy extends LocationAction
{
	public ActionInsertDraftStrategy(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new DraftStrategyIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Draft Strategy");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a draft Strategy");
	}


}
