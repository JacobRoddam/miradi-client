/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;


import javax.swing.table.TableModel;

import org.conservationmeasures.eam.main.AppPreferences;

public class PlanningViewBudgetTotalsTable extends PlanningViewAbstractRightAlignedTable
{
	public PlanningViewBudgetTotalsTable(TableModel model)
	{
		super(model);
		setBackground(AppPreferences.BUDGET_TOTAL_TABLE_BACKGROUND);
	}

	int getPreferredScrollableViewportWidth()
	{
		return getSavedColumnWidth(0);
	}
	
	protected int getColumnWidth(int column)
	{
		return 125;
	}	
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningViewBudgetTotalsTable";
}