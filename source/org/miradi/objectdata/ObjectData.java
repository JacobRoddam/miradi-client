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
package org.miradi.objectdata;

import java.util.HashSet;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;
import org.w3c.dom.Node;


abstract public class ObjectData
{
	public ObjectData(String tagToUse)
	{
		this(tagToUse, new HashSet<String>());
	}
	
	public ObjectData(String tagToUse, HashSet<String> dependencyTagsToUse)
	{
		tag = tagToUse;
		dependencyTags = dependencyTagsToUse;
	}
	
	abstract public void set(String newValue) throws Exception;
	abstract public String get();
	@Override
	abstract public boolean equals(Object rawOther);
	@Override
	abstract public int hashCode();
	
	@Override
	final public String toString()
	{
		return get();
	}
	
	final public boolean isEmpty()
	{
		return toString().length() == 0;
	}
	
	public boolean isCurrentValue(String text)
	{
		return get().equals(text);
	}

	public boolean isPseudoField()
	{
		return false;
	}
	
	public boolean isUserText()
	{
		return false;
	}
	
	public boolean isSingleLineUserText()
	{
		return false;
	}
	
	public boolean isUserTextWithHtmlFormatting()
	{
		return false;
	}
	
	public boolean isExpandingUserText()
	{
		return false;
	}
	
	public boolean isMultiLineUserText()
	{
		return false;
	}
	
	public boolean isBaseIdData()
	{
		return false;
	}

	public boolean isRefData()
	{
		return false;
	}
	
	public boolean isIdListData()
	{
		return false;
	}
	
	public boolean isRefListData()
	{
		return false;
	}
	
	public boolean isChoiceItemData()
	{
		return false;
	}
	
	public boolean isCodeListData()
	{
		return false;
	}
	
	public boolean isCodeToCodeMapData()
	{
		return false;
	}
	
	public boolean isCodeToUserStringMapData()
	{
		return false;
	}
	
	public boolean isCodeToChoiceMapData()
	{
		return false;
	}
	
	public boolean isCodeToCodeListMapData()
	{
		return false;
	}
	
	public ChoiceQuestion getChoiceQuestion()
	{
		return null;
	}
	
	public ORef getRef()
	{
		throw new RuntimeException("Called getRawRef on " + getClass().getSimpleName());
	}
	
	public ORefList getRefList()
	{
		return new ORefList();
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public HashSet<String> getDependencyTags()
	{
		return dependencyTags;
	}
	
	public void setNavigationField(final boolean isNavigationFieldToUse)
	{
		isNavigationField = isNavigationFieldToUse;
	}
	
	public boolean isNavigationField()
	{
		return isNavigationField;
	}
	
	public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		throw new RuntimeException("This method should not be called since the field is only used by TableSettings, which is not exported");
	}
	
	public void readAsXmpz2XmlData(Xmpz2XmlImporter importer, Node node, ORef destinationRefToUse, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		throw new RuntimeException("This method should be overriden and must call back into the importer. Type name = " + baseObjectSchema.getObjectName() + ". Tag = " + fieldSchema.getTag() + ". Class needing to override = " +  getClass().getSimpleName());
	}
	
	public void writeAsXmpz2SchemaElement(Xmpz2XmlSchemaCreator creator, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		
	}
	
	private HashSet<String> dependencyTags;
	private String tag;
	private boolean isNavigationField;
}
