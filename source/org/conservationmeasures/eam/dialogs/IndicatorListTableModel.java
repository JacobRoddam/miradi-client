/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;

public class IndicatorListTableModel extends ObjectListTableModel
{
	public IndicatorListTableModel(Project projectToUse, FactorId nodeId)
	{
		super(projectToUse, ObjectType.TARGET, nodeId, Factor.TAG_INDICATOR_IDS, ObjectType.INDICATOR, Indicator.TAG_LABEL);
	}
}
