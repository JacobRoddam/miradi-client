/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

import java.awt.event.FocusEvent;

import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class ObjectExpandingMultilineInputField extends ObjectMultilineInputField
{
	public ObjectExpandingMultilineInputField(MainWindow mainWindow, ORef refToUse, String tagToUse, int columnsToUse) throws Exception
	{
		super(mainWindow, refToUse, tagToUse, 1, columnsToUse);
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		super.focusGained(e);
		
		if (!wasFocusLostTemporarily())
			selectAll();
	}
}
