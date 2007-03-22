/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Task;

public class TaskPool extends EAMNormalObjectPool
{
	public TaskPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.TASK);
	}
	
	public void put(Task task)
	{
		put(task.getId(), task);
	}
	
	public Task find(BaseId id)
	{
		return (Task)getRawObject(id);
	}

	EAMObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new Task(actualId, (CreateTaskParameter)extraInfo);
	}

	public EAMObject[] getAllTasks()
	{
		BaseId[] allIds = getIds();
		Task[] allTasks = new Task[allIds.length];
		for (int i = 0; i < allTasks.length; i++)
			allTasks[i] = find(allIds[i]);
			
		return allTasks;
	}

}
