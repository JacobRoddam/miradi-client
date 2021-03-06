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
package org.miradi.dialogfields;

import java.awt.Component;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.miradi.dialogs.fieldComponents.PanelTable;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.utils.IgnoreCaseStringComparator;
import org.miradi.utils.SingleColumnReadonlyGenericDefaultTableModel;
import org.miradi.utils.XmlUtilities2;

public class ObjectReadonlyObjectListField extends ObjectDataInputField
{
	public ObjectReadonlyObjectListField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, String uniqueIdentifier)
	{
		super(mainWindowToUse.getProject(), refToUse, tagToUse);
		
		model = new SingleColumnReadonlyGenericDefaultTableModel();
		table = new TableWithHtmlRenderer(mainWindowToUse, model);
		setDefaultFieldBorder();
	}
	
	@Override
	public String getText()
	{
		return null;
	}

	@Override
	public void setText(String newValue)
	{
		try
		{
			ORefList refList = new ORefList(newValue);
			Vector<String> names = createSortedBaseObjectFullNameList(refList);
			setModelValues(names);
			
			table.resizeTable();
			final int ARBITRARY_REASONABLE_WIDTH = 300;
			table.setColumnWidth(0, ARBITRARY_REASONABLE_WIDTH);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void setModelValues(Vector<String> names)
	{
		model.setRowCount(names.size());
		for(int row = 0; row < names.size(); ++row)
		{
			model.setValueAt(names.get(row), row, 0);
		}
	}

	protected Vector<String> createSortedBaseObjectFullNameList(ORefList refList)
	{
		Vector<String> names = new Vector<String>();
		for (int index = 0; index < refList.size(); ++index)
		{
			ORef ref = refList.get(index);
			//TODO these invalid refs (orphaned DF)should get auto-repaired during project open at some point
			if (ref.isInvalid())
				continue;
			
			BaseObject object = getProject().findObject(ref); 
			if(object == null)
			{
				EAM.logError("Ignored a missing object while in ObjectReadonlyObjectListField.setText(). Ref = " + ref);
			}
			else
			{
				names.add(object.getFullName());
			}
		}
		
		Collections.sort(names, new IgnoreCaseStringComparator());
		
		return names;
	}
	
	@Override
	public boolean allowEdits()
	{
		return false;
	}

	@Override
	public JComponent getComponent()
	{
		return table;
	}
	
	private class TableWithHtmlRenderer extends PanelTable
	{
		public TableWithHtmlRenderer(MainWindow mainWindowToUse, TableModel model)
		{
			super(mainWindowToUse, model);
			
			setForeground(EAM.READONLY_FOREGROUND_COLOR);
			setBackground(EAM.READONLY_BACKGROUND_COLOR);
		}
		
		@Override
		public TableCellRenderer getCellRenderer(int row, int column)
		{
			return new HtmlTableCellRenderer();
		}
	}
	
	private class HtmlTableCellRenderer extends DefaultTableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(JTable tableToUse, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			String valueAsString = value.toString();
			valueAsString = XmlUtilities2.convertXmlTextToHtml(valueAsString);
			
			return super.getTableCellRendererComponent(tableToUse, valueAsString, isSelected, hasFocus, row, column);
		}
	}
	
	private SingleColumnReadonlyGenericDefaultTableModel model;
	private PanelTable table;
}
