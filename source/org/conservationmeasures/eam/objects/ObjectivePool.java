/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.EAMObjectPool;

public class ObjectivePool extends EAMObjectPool
{
	public void put(Objective objective)
	{
		put(objective.getId(), objective);
	}
	
	public Objective find(BaseId id)
	{
		return (Objective)getRawObject(id);
	}

}
