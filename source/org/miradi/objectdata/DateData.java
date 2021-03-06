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
package org.miradi.objectdata;

import org.martus.util.MultiCalendar;
import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.InvalidDateException;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;
import org.w3c.dom.Node;

public class DateData extends ObjectData
{
	public DateData(String tagToUse)
	{
		super(tagToUse);
		date = null;
	}

	@Override
	public boolean isDateData()
	{
		return true;
	}

	@Override
	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
		{
			date = null;
			return;
		}
		
		try
		{
			date = MultiCalendar.createFromIsoDateString(newValue);
		}
		catch (Exception e)
		{
			throw new InvalidDateException(e);
		}
	}

	@Override
	public String get()
	{
		if(date == null)
			return "";
		return date.toIsoDateString();
	}
	
	public MultiCalendar getDate()
	{
		return date;
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof DateData))
			return false;
		
		DateData other = (DateData)rawOther;
		return date.equals(other.date);
	}

	@Override
	public int hashCode()
	{
		return date.hashCode();
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writeDateData(baseObjectSchema, fieldSchema, get());
	}
	
	@Override
	public void readAsXmpz2XmlData(Xmpz2XmlImporter importer, Node node, ORef destinationRefToUse, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		importer.importStringField(node, baseObjectSchema.getXmpz2ElementName(), destinationRefToUse, fieldSchema.getTag());
	}
	
	@Override
	public String createXmpz2SchemaElementString(Xmpz2XmlSchemaCreator creator, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		return creator.createDateSchemaElement(baseObjectSchema, fieldSchema);
	}
	
	private MultiCalendar date;
}
