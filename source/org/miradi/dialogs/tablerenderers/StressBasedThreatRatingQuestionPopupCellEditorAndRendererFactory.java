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

package org.miradi.dialogs.tablerenderers;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.miradi.dialogs.threatrating.properties.ThreatStressRatingTable;
import org.miradi.dialogs.threatrating.properties.ThreatStressRatingTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.StressBasedThreatRatingQuestionPopupEditorComponent;

public class StressBasedThreatRatingQuestionPopupCellEditorAndRendererFactory extends AbstractCellEditor implements TableCellEditor, TableCellRenderer
{
	public StressBasedThreatRatingQuestionPopupCellEditorAndRendererFactory(Project projectToUse, ChoiceQuestion questionToUse) throws Exception 
	{
	    super();
	    
	    questionEditor = new StressBasedThreatRatingQuestionPopupEditorComponent(projectToUse, questionToUse);
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		updateEditor(table, value);
		
		return questionEditor;
	}

	public Object getCellEditorValue()
	{
		return questionEditor.getText();
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		updateEditor(table, value);

		return questionEditor;
	}
	
	private void updateEditor(JTable table, Object value)
	{
		ChoiceItem choiceItem = (ChoiceItem) value;
		questionEditor.setText(choiceItem.getCode());
		
		ThreatStressRatingTableModel model = ((ThreatStressRatingTable) table).getThreatStressRatingTableModel();
		ORef threatRef = model.getThreatRef();
		ORef targetRef = model.getTargetRef();
		questionEditor.setThreatRef(threatRef);
		questionEditor.setTargetRef(targetRef);
	}

	private StressBasedThreatRatingQuestionPopupEditorComponent questionEditor;
}