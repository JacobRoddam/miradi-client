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

package org.miradi.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.miradi.main.EAM;
import org.miradi.project.Project;

public class ProjectNameRestrictedDocument extends PlainDocument
{
	@Override
	public void insertString(int offset, String value, AttributeSet as) throws BadLocationException
	{
		if (offset >= Project.MAX_PROJECT_FILENAME_LENGTH)
			return;
		
		String newValue = removeIllegalCharacters(value);
		super.insertString(offset, newValue, as);
	}

	private String removeIllegalCharacters(String value) throws BadLocationException
	{
		StringBuffer newValue = new StringBuffer();
		for (int index = 0; index < value.length(); ++index)
		{
			char character = value.charAt(index);
			if (isValidCharacter(character))
			{
				newValue.append(character);
			}
		}
		
		return newValue.toString();	
	}

	private boolean isValidCharacter(char character)
	{
		return EAM.isValidCharacter(character);
	}
}