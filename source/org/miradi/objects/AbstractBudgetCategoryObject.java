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

package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.project.ObjectManager;
import org.miradi.schemas.BaseObjectSchema;

abstract public class AbstractBudgetCategoryObject extends BaseObject
{
	public AbstractBudgetCategoryObject(ObjectManager objectManagerToUse, BaseId idToUse, final BaseObjectSchema schemaToUse)
	{
		super(objectManagerToUse, idToUse, schemaToUse);
	}

	@Override
	public String getFullName()
	{
		return toFullNameWithCode(getData(TAG_CODE));
	}
	
	public static final String TAG_CODE = "Code";
	public static final String TAG_COMMENTS = "Comments";
}
