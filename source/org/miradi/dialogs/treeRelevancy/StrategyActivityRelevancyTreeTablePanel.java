/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.dialogs.treeRelevancy;

import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionTreeCreateRelevancyActivity;
import org.miradi.actions.ActionTreeCreateRelevancyMonitoringActivity;
import org.miradi.dialogs.base.EditableBaseObjectTable;
import org.miradi.dialogs.base.SingleBooleanColumnEditableModel;
import org.miradi.dialogs.treetables.TreeTableWithStateSaving;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.objects.StrategyActivityRelevancyInterface;

public class StrategyActivityRelevancyTreeTablePanel extends AbstractEditableTreeTablePanel
{
	public static StrategyActivityRelevancyTreeTablePanel createTreeTablePanel(MainWindow mainWindowToUse, BaseObject baseObject, StrategyActivityRelevancyTreeTableModel treeTableModel, StrategyActivityRelevancyTreeTable treeTable) throws Exception
	{
		return new StrategyActivityRelevancyTreeTablePanel(mainWindowToUse, treeTableModel, treeTable, baseObject);
	}
	
	private StrategyActivityRelevancyTreeTablePanel(MainWindow mainWindowToUse, StrategyActivityRelevancyTreeTableModel modelToUse, TreeTableWithStateSaving treeTable, BaseObject baseObject) throws Exception
	{
		super(mainWindowToUse, modelToUse, treeTable, baseObject, getButtonActionClasses());
	}
	
	@Override
	protected SingleBooleanColumnEditableModel createEditableTableModel()
	{
		return new StrategyActivityRelevancyTableModel(getProject(), getTree(), (StrategyActivityRelevancyInterface) getBaseObjectForPanel());
	}
	
	@Override
	protected EditableBaseObjectTable createEditableTable()
	{
		return new StrategyActivityRelevancyTable(getMainWindow(), getEditableSingleBooleanColumnTableModel());
	}
	
	private static Class[] getButtonActionClasses()
	{
		return new Class[] {
				ActionTreeCreateRelevancyActivity.class,
				ActionTreeCreateRelevancyMonitoringActivity.class,
				ActionDeletePlanningViewTreeNode.class,
		};
	}
}