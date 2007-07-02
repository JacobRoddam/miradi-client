/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.views.map.wizard;

import org.conservationmeasures.eam.views.map.MapView;
import org.conservationmeasures.eam.wizard.SplitWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class MapOverviewStep extends SplitWizardStep
{
	public MapOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, MapView.getViewName());
	}
}
