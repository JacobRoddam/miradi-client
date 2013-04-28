/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.TaxonomyAssociationSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;


public class TaxonomyAssociationImporter extends BaseObjectImporter
{
	public TaxonomyAssociationImporter(Xmpz2XmlImporter importerToUse, int parentTypeToUse)
	{
		super(importerToUse, new TaxonomyAssociationSchema());
		
		parentType = parentTypeToUse;
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);
		
		getImporter().setData(refToUse, TaxonomyAssociationSchema.TAG_PARENT_OBJECT_TYPE, parentType);
	}

	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(TaxonomyAssociationSchema.TAG_PARENT_OBJECT_TYPE))
			return true;
		
		if (tag.equals(TaxonomyAssociationSchema.TAG_TAXONOMY_ASSOCIATION_CODE))
			return true;
		
		return false;
	}

	@Override
	public ORef createBaseObject(final BaseObjectImporter importer,	Node baseObjectNode) throws Exception
	{
		ORef newTaxonomyAssociationRef = getProject().createObject(TaxonomyAssociationSchema.getObjectType());
		String taxonomyAssociationCode = getImporter().getAttributeValue(baseObjectNode, TAXONOMY_ASSOCIATION_CODE);
		getImporter().setData(newTaxonomyAssociationRef, TaxonomyAssociationSchema.TAG_TAXONOMY_ASSOCIATION_CODE, taxonomyAssociationCode);
		
		return newTaxonomyAssociationRef;
	}
	
	@Override
	public String createPoolElementName() throws Exception
	{
		return Xmpz2XmlExporter.getTaxonomyAssociationPoolName(parentType);
	}
	
	@Override
	protected String getPoolName() throws Exception 
	{
		return Xmpz2XmlExporter.getTaxonomyAssociationPoolName(parentType);
	}
	
	private int parentType;
}
