/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.cells.FactorLinkDataMap;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objectdata.BaseIdData;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class DiagramFactorLink extends EAMBaseObject
{
	public DiagramFactorLink(BaseId idToUse, CreateDiagramFactorLinkParameter extraInfo) throws Exception
	{
		super(new DiagramFactorLinkId(idToUse.asInt()));
		clear();
		
		underlyingObjectId.setId(extraInfo.getFactorLinkId());
		fromId.setId(extraInfo.getFromFactorId());
		toId.setId(extraInfo.getToFactorId());
	}
	
	public DiagramFactorLink(int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(new DiagramFactorLinkId(idToUse), json);
		underlyingObjectId.setId(json.getId(TAG_WRAPPED_ID));
		fromId.setId(json.getId(TAG_FROM_DIAGRAM_FACTOR_ID));
		toId.setId(json.getId(TAG_TO_DIAGRAM_FACTOR_ID));
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		json.putId(TAG_WRAPPED_ID, underlyingObjectId.getId());
		json.putId(TAG_FROM_DIAGRAM_FACTOR_ID, fromId.getId());
		json.putId(TAG_TO_DIAGRAM_FACTOR_ID, toId.getId());
		return json;
	}

	public int getType()
	{
		return ObjectType.DIAGRAM_LINK;
	}

	public DiagramFactorLinkId getDiagramLinkageId()
	{
		return (DiagramFactorLinkId)getId(); 
	}
	
	public FactorLinkId getWrappedId()
	{
		return new FactorLinkId(underlyingObjectId.getId().asInt());
	}
	
	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_WRAPPED_ID))
			return underlyingObjectId.get();
		if(fieldTag.equals(TAG_FROM_DIAGRAM_FACTOR_ID))
			return fromId.get();
		if(fieldTag.equals(TAG_TO_DIAGRAM_FACTOR_ID))
			return toId.get();
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_WRAPPED_ID))
			underlyingObjectId.set(dataValue);
		else if(fieldTag.equals(TAG_FROM_DIAGRAM_FACTOR_ID))
			fromId.set(dataValue);
		else if(fieldTag.equals(TAG_TO_DIAGRAM_FACTOR_ID))
			toId.set(dataValue);
		else super.setData(fieldTag, dataValue);
	}

	public FactorLinkDataMap createLinkageDataMap() throws Exception
	{
		FactorLinkDataMap dataMap = new FactorLinkDataMap();
		dataMap.setId(getDiagramLinkageId());
		dataMap.setFromId(new DiagramFactorId(fromId.getId().asInt()));
		dataMap.setToId(new DiagramFactorId(toId.getId().asInt()));
		return dataMap;
	}

	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateDiagramFactorLinkParameter(
				new FactorLinkId(underlyingObjectId.getId().asInt()), 
				new DiagramFactorId(fromId.getId().asInt()), 
				new DiagramFactorId(toId.getId().asInt()));
	}

	void clear()
	{
		super.clear();
		underlyingObjectId = new BaseIdData();
		fromId = new BaseIdData();
		toId = new BaseIdData();
	}
	
	public static final String TAG_WRAPPED_ID = "WrappedLinkId";
	public static final String TAG_FROM_DIAGRAM_FACTOR_ID = "FromDiagramFactorId";
	public static final String TAG_TO_DIAGRAM_FACTOR_ID = "ToDiagramFactorId";
	
	private BaseIdData underlyingObjectId;
	private BaseIdData fromId;
	private BaseIdData toId;
}
