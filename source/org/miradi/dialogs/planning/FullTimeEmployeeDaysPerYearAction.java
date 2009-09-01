/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTextField;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.planning.propertiesPanel.AssignmentDateUnitsTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeMultiTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningUpperMultiTable;
import org.miradi.dialogs.planning.upperPanel.PlanningUpperTableModelInterface;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;

public class FullTimeEmployeeDaysPerYearAction extends AbstractAction
{
	public FullTimeEmployeeDaysPerYearAction(PlanningUpperMultiTable planningUpperMultiTable)
	{
		super(EAM.text("Percent of FTE..."));
		
		mainWindow = planningUpperMultiTable.getMainWindow();
		multiTable = planningUpperMultiTable;
	}

	public void actionPerformed(ActionEvent e)
	{
		Vector<Component> buttons = new Vector();
		PanelButton insertButton = new PanelButton(EAM.text("Insert"));
		insertButton.addActionListener(new InsertHandler());
		buttons.add(insertButton);
		PanelButton cancelButton = new PanelButton(EAM.text("Cancel"));
		buttons.add(cancelButton);
		
		dialog = new ModalDialogWithClose(getMainWindow(), EAM.text("Edit..."), buttons);
		dialog.setSimpleCloseButton(cancelButton);
		dialog.add(createEditPanel(), BorderLayout.BEFORE_FIRST_LINE);
		Utilities.centerDlg(dialog);
		dialog.setVisible(true);
	}
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}

	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public JPanel createEditPanel()
	{
		TwoColumnPanel panel = new TwoColumnPanel();
		String fullTimeEmployeeDaysPerYear = getFullTimeEmployeeDaysPerYearAsString();
		PanelTextField daysPerYearField = new PanelTextField(fullTimeEmployeeDaysPerYear);
		daysPerYearField.setEditable(false);
		panel.add(new PanelTitleLabel(EAM.text("FTE Days Per Year:")));
		panel.add(daysPerYearField);

		percentField = new PanelTextField(10);
		percentField.setDocument(new NumericDocument());

		panel.add(new PanelTitleLabel(EAM.text("Percent:")));
		panel.add(percentField);
		
		return panel;
	}

	private String getFullTimeEmployeeDaysPerYearAsString()
	{
		return AssignmentDateUnitsTableModel.getRawFullTimeEmployeeDaysPerYear(getProject());
	}

	public class NumericDocument extends PlainDocument 
	{
	     public NumericDocument() 
	     {
	          super();
	     }
	   
	     public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException 
	     {
	    	 if (str == null)
	    		 return;
	    	 	    	 
	    	 //TODO filter to be 0 <= x <= 
	    	 super.insertString(offset, str, attr);
	     }
	}
	
	class InsertHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String doubleAsString = percentField.getText();
			double parsedDouble = Double.parseDouble(doubleAsString);
			int selectedColumn = multiTable.getSelectedColumn();
			int modelColumn = multiTable.convertColumnIndexToModel(selectedColumn);
			
			int selectedRow = multiTable.getSelectedRow();
			PlanningTreeMultiTableModel castedModel = multiTable.getCastedModel();
			int columnWithinMultiTable = castedModel.findColumnWithinSubTable(modelColumn);
			PlanningUpperTableModelInterface model = castedModel.getCastedModel(columnWithinMultiTable);
			model.updateFullTimeEmployeeDaysPerYearPercent(selectedRow, columnWithinMultiTable, parsedDouble);
			
			dialog.dispose();
			dialog.setVisible(false);
		}
	}
	
	private PlanningUpperMultiTable multiTable;
	private MainWindow mainWindow;
	private PanelTextField percentField;
	private ModalDialogWithClose dialog;
}
