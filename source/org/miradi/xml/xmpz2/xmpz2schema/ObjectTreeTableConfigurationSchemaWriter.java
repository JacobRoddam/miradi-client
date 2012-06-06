/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2.xmpz2schema;

import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.schemas.BaseObjectSchema;

public class ObjectTreeTableConfigurationSchemaWriter extends BaseObjectSchemaWriter
{
	public ObjectTreeTableConfigurationSchemaWriter(Xmpz2XmlSchemaCreator creatorToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(creatorToUse, baseObjectSchemaToUse);
	}

	//FIXME this should be replaced with a size of the list of omitted tags.  
	//Which will remove the shouldImitField method, and replace it with getTagsToOmit
	@Override
	protected int getFieldCount()
	{
		final int TWO_OMMITTED_FIELD = 2;
		return super.getFieldCount() - TWO_OMMITTED_FIELD;
	}

	@Override
	protected boolean shouldOmitField(String tag)
	{
		if (tag.equals(ObjectTreeTableConfiguration.TAG_COL_CONFIGURATION))
			return true;
		
		if (tag.equals(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION))
			return true;
		
		return super.shouldOmitField(tag);
	}
}
