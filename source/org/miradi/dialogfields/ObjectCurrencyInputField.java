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
package org.miradi.dialogfields;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.DoubleUtilities;

public class ObjectCurrencyInputField extends ObjectFloatingPointRestrictedInputField
{
	public ObjectCurrencyInputField(MainWindow mainWindowToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int columnsToUse) throws Exception
	{
		super(mainWindowToUse, objectTypeToUse, objectIdToUse, tagToUse, columnsToUse);
	}
	
	@Override
	public void setText(String newValue)
	{
		try
		{
			if (newValue.length() > 0)
			{
				double valueToFormat = DoubleUtilities.toDoubleFromHumanFormat(newValue);
				newValue = getProject().getCurrencyFormatterWithoutCommas().format(valueToFormat);
			}
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
		
		super.setText(newValue);
	}
}
