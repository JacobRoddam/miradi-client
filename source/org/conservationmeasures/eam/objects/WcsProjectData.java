/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.BooleanData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class WcsProjectData extends BaseObject
{
	public WcsProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public WcsProjectData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
	}
	
	public WcsProjectData(int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(new BaseId(idAsInt), jsonObject);
	}
	
	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.WCS_PROJECT_DATA;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	void clear()
	{
		super.clear();
		
		organizationalFocus = new StringData();
		organizationalLevel = new StringData();
		SwotCompleted = new BooleanData(TAG_SWOT_COMPLETED);
		SwotUrl = new StringData();
		StepCompleted = new BooleanData(TAG_STEP_COMPLETED);
		StepUrl = new StringData();

		addField(TAG_ORGANIZATIONAL_FOCUS, organizationalFocus);
		addField(TAG_ORGANIZATIONAL_LEVEL, organizationalLevel);
		addField(TAG_SWOT_COMPLETED, SwotCompleted);
		addField(TAG_SWOT_URL, SwotUrl);
		addField(TAG_STEP_COMPLETED, StepCompleted);
		addField(TAG_STEP_URL, StepUrl);
	}
	
	public static final String OBJECT_NAME = "WcsProjectData";
	
	public static final String TAG_ORGANIZATIONAL_FOCUS = "OrganizationalFocus";
	public static final String TAG_ORGANIZATIONAL_LEVEL = "OrganizationalLevel";
	public static final String TAG_SWOT_COMPLETED = "SwotCompleted"; 
	public static final String TAG_SWOT_URL = "SwotUrl";
	public static final String TAG_STEP_COMPLETED = "StepCompleted";
	public static final String TAG_STEP_URL = "StepUrl";
	
	private StringData organizationalFocus;
	private StringData organizationalLevel;
	private BooleanData SwotCompleted;
	private StringData SwotUrl;
	private BooleanData StepCompleted;
	private StringData StepUrl;
}
