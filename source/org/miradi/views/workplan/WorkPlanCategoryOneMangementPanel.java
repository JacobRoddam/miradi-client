/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.views.workplan;

import javax.swing.Icon;

import org.miradi.dialogs.planning.CategoryOneCoreRowColumnProvider;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.CategoryOneTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.CategoryOneTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.ExportablePlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.icons.CategoryOneIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class WorkPlanCategoryOneMangementPanel extends PlanningTreeManagementPanel
{
	public WorkPlanCategoryOneMangementPanel(MainWindow mainWindowToUse,
			PlanningTreeTablePanel planningTreeTablePanel,
			PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel)
			throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanel, planningTreePropertiesPanel);
	}

	@Override
	protected PlanningTreeTablePanel createPlanningTreeTablePanel(String uniqueTreeTableModelIdentifier, RowColumnProvider rowColumnProvider) throws Exception
	{
		PlanningTreeTableModel model = CategoryOneTreeTableModel.createOperationalPlanCategoryOneTreeTableModel(getProject(), rowColumnProvider.getColumnListToShow());
		return ExportablePlanningTreeTablePanel.createPlanningTreeTablePanelWithoutButtons(getMainWindow(), rowColumnProvider, model);
	}

	public static PlanningTreeManagementPanel createPanel(MainWindow mainWindowToUse) throws Exception
	{
		RowColumnProvider rowColumnProvider = new CategoryOneCoreRowColumnProvider();
		PlanningTreeTableModel treeTableModel = CategoryOneTreeTableModel.createOperationalPlanCategoryOneTreeTableModel(mainWindowToUse.getProject(), rowColumnProvider.getColumnListToShow());
		PlanningTreeTablePanel treeTablePanel = CategoryOneTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse, treeTableModel, rowColumnProvider);
		PlanningTreeMultiPropertiesPanel propertiesPanel = new PlanningTreeMultiPropertiesPanel(mainWindowToUse, ORef.INVALID);
		
		return new WorkPlanCategoryOneMangementPanel(mainWindowToUse, treeTablePanel, propertiesPanel);
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Category One");
	}	
	
	@Override
	public Icon getIcon()
	{
		return new CategoryOneIcon();
	}
}