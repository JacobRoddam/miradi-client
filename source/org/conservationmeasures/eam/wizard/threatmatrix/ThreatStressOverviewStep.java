/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.threatmatrix;

import org.conservationmeasures.eam.actions.views.ActionViewThreatMatrix;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.menu.ProcessSteps;
import org.conservationmeasures.eam.wizard.ThreatRatingWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class ThreatStressOverviewStep extends ThreatRatingWizardStep
{
	public ThreatStressOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1C;
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewThreatMatrix.class;
	}
	
	public String getSubHeading()
	{
		return EAM.text("Stress-Based Threat Rating");
	}
}
