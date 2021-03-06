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

package org.miradi.xml.generic;

import org.miradi.objects.ProjectMetadata;

public class ProjectSummaryPlanningSchemaElement extends ObjectSchemaElement
{
	public ProjectSummaryPlanningSchemaElement()
	{
		super(XmlConstants.PROJECT_SUMMARY_PLANNING);
		
		createOptionalDateField(ProjectMetadata.TAG_START_DATE);
		createOptionalDateField(ProjectMetadata.TAG_EXPECTED_END_DATE);
		createOptionalDateField(ProjectMetadata.TAG_WORKPLAN_START_DATE);
		createOptionalDateField(ProjectMetadata.TAG_WORKPLAN_END_DATE);
		createOptionalFiscalYearStartField(ProjectMetadata.TAG_FISCAL_YEAR_START);
		createOptionalNumericField(ProjectMetadata.TAG_FULL_TIME_EMPLOYEE_DAYS_PER_YEAR);
		createOptionalTextField(ProjectMetadata.TAG_PLANNING_COMMENTS);
		
		createOptionalTextField(ProjectMetadata.TAG_CURRENCY_TYPE);
		createOptionalTextField(ProjectMetadata.TAG_CURRENCY_SYMBOL);
		createOptionalNumericField(ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES);
		createOptionalNumericField(ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING);
		createOptionalNumericField(ProjectMetadata.TAG_BUDGET_SECURED_PERCENT);
		createOptionalTextField(ProjectMetadata.TAG_KEY_FUNDING_SOURCES);
		createOptionalTextField(ProjectMetadata.TAG_FINANCIAL_COMMENTS);
	}
}
