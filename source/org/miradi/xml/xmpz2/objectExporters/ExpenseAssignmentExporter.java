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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;


public class ExpenseAssignmentExporter extends AbstractPlanningObjectExporter
{
	public ExpenseAssignmentExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, ExpenseAssignmentSchema.getObjectType());
	}
	@Override
	protected String getDateUnitElementName()
	{
		return EXPENSES_DATE_UNIT;
	}
	
	@Override
	protected String getDayElementName()
	{
		return EXPENSES_DAY;
	}
	
	@Override
	protected String getMonthElementName()
	{
		return EXPENSES_MONTH;
	}
	
	@Override
	protected String getQuarterElementName()
	{
		return EXPENSES_QUARTER;
	}
	
	@Override
	protected String getYearElementName()
	{
		return EXPENSES_YEAR;
	}
	
	@Override
	protected String getFullProjectTimespanElementName()
	{
		return EXPENSES_FULL_PROJECT_TIMESPAN;
	}
	
	@Override
	protected String getQuantityElementName()
	{
		return EXPENSE;
	}

	@Override
	protected String getDateUnitsElementName()
	{
		return DATE_UNITS_EXPENSE;
	}
	
	@Override
	protected String getPoolName()
	{
		return EXPENSE_ASSIGNMENT;
	}
}
