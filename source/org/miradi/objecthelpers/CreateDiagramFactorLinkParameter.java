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
package org.miradi.objecthelpers;

import java.util.HashMap;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorLinkId;
import org.miradi.objects.DiagramFactor;

public class CreateDiagramFactorLinkParameter extends CreateObjectParameter
{
	public CreateDiagramFactorLinkParameter(ORef fromRefToUse, ORef toRefToUse)
	{
		this(BaseId.INVALID, fromRefToUse.getObjectId(), toRefToUse.getObjectId());
	}
		
	public CreateDiagramFactorLinkParameter(ORef factorLinkRef, ORef fromRefToUse, ORef toRefToUse)
	{
		this(factorLinkRef.getObjectId(), fromRefToUse.getObjectId(), toRefToUse.getObjectId());
	}
	
	public CreateDiagramFactorLinkParameter(BaseId fromIdToUse, BaseId toIdToUse)
	{
		this(BaseId.INVALID, fromIdToUse, toIdToUse);
	}
	
	public CreateDiagramFactorLinkParameter(BaseId factorLinkIdToUse, BaseId fromIdToUse, BaseId toIdToUse)
	{
		factorLinkId = factorLinkIdToUse;
		fromId = fromIdToUse;
		toId = toIdToUse;
	}
	
	public BaseId getFactorLinkId()
	{
		return factorLinkId;
	}
	
	public BaseId getFromFactorId()
	{
		return fromId;
	}
	
	public BaseId getToFactorId()
	{
		return toId;
	}
	
	public ORef getFromDiagramFactorRef()
	{
		return new ORef(DiagramFactor.getObjectType(), getFromFactorId());
	}
	
	public ORef getToDiagramFactorRef()
	{
		return new ORef(DiagramFactor.getObjectType(), getToFactorId());
	}
	
	@Override
	public String getFormatedDataString()
	{
		HashMap<String, Comparable> dataPairs = new HashMap<String, Comparable>();
		dataPairs.put(FactorLinkId.class.getSimpleName(), factorLinkId);
		dataPairs.put("FactorFromId", fromId);
		dataPairs.put("FactorToID", toId);
		return formatDataString(dataPairs);
	}
	
	private BaseId factorLinkId;
	private BaseId fromId;
	private BaseId toId;
}
