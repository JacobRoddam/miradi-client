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

import javax.swing.JScrollBar;

import org.miradi.dialogs.base.EditableBaseObjectTable;
import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.dialogs.base.SingleBooleanColumnEditableModel;
import org.miradi.dialogs.treetables.AbstractTreeTablePanel;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.dialogs.treetables.TreeTableWithStateSaving;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.utils.TableWithColumnWidthAndSequenceSaver;

abstract public class AbstractEditableTreeTablePanel extends AbstractTreeTablePanel
{
	public AbstractEditableTreeTablePanel(MainWindow mainWindowToUse, GenericTreeTableModel modelToUse, TreeTableWithStateSaving treeTable, BaseObject baseObject) throws Exception
	{
		this(mainWindowToUse, modelToUse, treeTable, baseObject, new Class[0]);
	}
	
	public AbstractEditableTreeTablePanel(MainWindow mainWindowToUse, GenericTreeTableModel modelToUse, TreeTableWithStateSaving treeTable, BaseObject baseObjectToUse, Class[] buttonActionClasses) throws Exception
	{
		super(mainWindowToUse, treeTable, buttonActionClasses);

		baseObjectForPanel = baseObjectToUse;
		JScrollBar masterVerticalScrollBar = new MasterVerticalScrollBar(treeTableScrollPane);
		treeTableScrollPane.addMouseWheelListener(new MouseWheelHandler(masterVerticalScrollBar));

		editableTableModel = createEditableTableModel();
		editableTable = createEditableTable();
		mainTableScrollPane = integrateTable(masterVerticalScrollBar, editableTable);

		getScrollController().addScrollBar(masterVerticalScrollBar);
		createTreeAndTablePanel();
		rebuildEntireTreeTable();
	}
	
	@Override
	protected void rebuildEntireTreeTable() throws Exception
	{
		mainTableScrollPane.showVerticalScrollBar();
		ORef selectedRef = ORef.INVALID;
		BaseObject[] selected = tree.getSelectedObjects();
		if(selected.length == 1)
			selectedRef = selected[0].getRef();
		ORefList refHierarchy = getTree().findHierarchyForRef(selectedRef);
		int selectedRow = getTree().getSelectionModel().getMinSelectionIndex();
		
		getTree().rebuildTableCompletely();
		getEditableSingleBooleanColumnTableModel().fireTableStructureChanged();
		
		// NOTE: The following rebuild the tree but don't touch the columns
		getTreeTableModel().rebuildEntireTree();
		restoreTreeExpansionState();
		updateRightSideTablePanels();

		getTree().selectObjectAfterSwingClearsItDueToTreeStructureChange(refHierarchy, selectedRow);
	}

	protected SingleBooleanColumnEditableModel getEditableSingleBooleanColumnTableModel()
	{
		return editableTableModel;
	}

	private void updateRightSideTablePanels() throws Exception
	{
		validate();
		repaint();
	}
	
	@Override
	public void handleCommandEventImmediately(CommandExecutedEvent event)
	{
		super.handleCommandEventImmediately(event);
		if(event.isSetDataCommand())
			editableTable.repaint();
	}
	
	@Override
	protected EditableObjectTableModel getMainModel()
	{
		return editableTableModel;
	}
	
	protected BaseObject getBaseObjectForPanel()
	{
		return baseObjectForPanel;
	}
	
	@Override
	protected TableWithColumnWidthAndSequenceSaver getMainTable()
	{
		return editableTable; 
	}
	
	abstract protected SingleBooleanColumnEditableModel createEditableTableModel();
	
	abstract protected EditableBaseObjectTable createEditableTable();
	
	private SingleBooleanColumnEditableModel editableTableModel;
	private EditableBaseObjectTable editableTable;
	private BaseObject baseObjectForPanel;
}
