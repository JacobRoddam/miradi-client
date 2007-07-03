/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.main.ProcessSteps;
import org.conservationmeasures.eam.wizard.DiagramWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class DiagramWizardIdentifyIndirectThreatStep extends DiagramWizardStep
{

	public DiagramWizardIdentifyIndirectThreatStep(WizardPanel panelToUse)
	{
		super(panelToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1D;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpDiagramWizardIdentifyIndirectThreatStep.class;
	}
	
}
