/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.properties;

import org.conservationmeasures.eam.dialogs.base.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.stress.StressListManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ManageStressesDialog extends ModelessDialogWithClose
{
	public ManageStressesDialog(MainWindow parent, StressListManagementPanel stressListManagementPanel)
	{
		super(parent, stressListManagementPanel, EAM.text("Manage Stresses"));
	}
}
