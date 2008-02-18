/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.actions;

import org.miradi.icons.MethodIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionShareMethod extends ObjectsAction
{
	public ActionShareMethod(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new MethodIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Share Method");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Share an existing Method into this Indicator");
	}
}
