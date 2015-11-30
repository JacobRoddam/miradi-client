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

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.questions.AbstractDateUnitTypeQuestion;
import org.miradi.utils.FillerLabel;

public class DayDateUnitStartAndEndCard extends DateUnitStartAndEndCard
{
	public DayDateUnitStartAndEndCard(StartEndDateUnitProvider startEndDateUnitProvider)
	{
		startDayPanel = new DayPanel(startEndDateUnitProvider.getStartDateUnit(), getStartText());
		endDayPanel = new DayPanel(startEndDateUnitProvider.getEndDateUnit(), getEndText());

		add(new PanelTitleLabel(EAM.text("Day Selection: ")));
		add(new FillerLabel());
		add(startDayPanel);
		add(endDayPanel);
	}
	
	@Override
	public void dispose()
	{
		disposePanel(startDayPanel);
		startDayPanel = null;
		
		disposePanel(endDayPanel);
		endDayPanel = null;
		
		super.dispose();
	}
	
	@Override
	protected DateUnit getEndDate()
	{
		return endDayPanel.getDateUnit();
	}

	@Override
	protected DateUnit getStartDate()
	{
		return startDayPanel.getDateUnit();
	}
	
	@Override
	public String getPanelDescription()
	{
		return AbstractDateUnitTypeQuestion.DAY_CODE;
	}

	@Override
	public void setStartEndDateUnitProvider(StartEndDateUnitProvider startEndDateUnitProviderToUse)
	{
		startDayPanel.setDateUnit(startEndDateUnitProviderToUse.getStartDateUnit());
		endDayPanel.setDateUnit(startEndDateUnitProviderToUse.getEndDateUnit());
	}

	private DayPanel startDayPanel;
	private DayPanel endDayPanel;
}
