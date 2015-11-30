/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogfields.editors;

import org.miradi.dialogfields.WhenPlannedEditorField;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneRowPanel;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.project.ProjectCalendar;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

abstract public class DateUnitPanel extends OneRowPanel
{
	public DateUnitPanel(ProjectCalendar projectCalendar, DateUnit dateUnit, String panelTitle)
	{
		dateUnitChooser = createDateUnitChooser(projectCalendar, dateUnit);
		dateUnitChooser.addItemListener(new ChangeHandler());
		add(new PanelTitleLabel(panelTitle));
		add(dateUnitChooser);
	}

	public DateUnit getDateUnit()
	{
		return dateUnitChooser.getDateUnit();
	}

	public void setDateUnit(DateUnit dateUnitToUse)
	{
		dateUnitChooser.setSelectedDateUnit(dateUnitToUse);
	}

	public void addActionListener(WhenPlannedEditorField.WhenPlannedEditorChangeHandler editorFieldChangeHandlerToUse)
	{
		editorFieldChangeHandler = editorFieldChangeHandlerToUse;
	}

	abstract protected DateUnitComboBox createDateUnitChooser(ProjectCalendar projectCalendar,	DateUnit dateUnit);

	private class ChangeHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getStateChange() == ItemEvent.SELECTED && editorFieldChangeHandler != null)
				editorFieldChangeHandler.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "DateUnitPanel"));
		}
	}

	private WhenPlannedEditorField.WhenPlannedEditorChangeHandler editorFieldChangeHandler;
	private DateUnitComboBox dateUnitChooser;
}
