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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.SubTargetSchema;
import org.miradi.schemas.TargetSchema;

public class SubTarget extends BaseObject
{
	public SubTarget(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static SubTargetSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static SubTargetSchema createSchema(ObjectManager objectManager)
	{
		return (SubTargetSchema) objectManager.getSchemas().get(ObjectType.SUB_TARGET);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			TargetSchema.getObjectType(),
			HumanWelfareTargetSchema.getObjectType(),
			};
	}
	
	public boolean canHaveIndicators()
	{
		return false;
	}

	@Override
	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}

	public String getComment()
	{
		return getStringData(TAG_COMMENTS);
	}

	@Override
	public String toString()
	{
		return getFullName();
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == SubTargetSchema.getObjectType();
	}
	
	public static boolean is(BaseObject object)
	{
		return is(object.getType());
	}
	
	public static SubTarget find(ObjectManager objectManager, ORef subTargetRef)
	{
		return (SubTarget) objectManager.findObject(subTargetRef);
	}
	
	public static SubTarget find(Project project, ORef subTargetRef)
	{
		return find(project.getObjectManager(), subTargetRef);
	}
	
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_DETAIL = "Detail";
}
