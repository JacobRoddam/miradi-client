package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionRenameResultsChain extends MainWindowAction
{
	public ActionRenameResultsChain(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Rename Results Chain");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Rename this Results Chain");
	}

}
