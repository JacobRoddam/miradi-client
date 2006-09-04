/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestInsertConnection extends EAMTestCase
{
	public TestInsertConnection(String name)
	{
		super(name);
	}
	
	public void testIsAvailable() throws Exception
	{
		OpenableProject project = new OpenableProject(getName());
		try
		{
			InsertConnection doer = new InsertConnection();
			doer.setProject(project);

			assertFalse("enabled when no project open?", doer.isAvailable());
			project.setIsOpen(true);
			assertFalse("Enabled when no nodes in the system?", doer.isAvailable());
			project.insertNodeAtId(DiagramNode.TYPE_TARGET, BaseId.INVALID);
			assertFalse("Enabled when only 1 node?", doer.isAvailable());
			project.insertNodeAtId(DiagramNode.TYPE_FACTOR, BaseId.INVALID);
			assertTrue("not enabled when 2 nodes?", doer.isAvailable());
		}
		finally
		{
			project.close();
		}
		
	}

}

class OpenableProject extends ProjectForTesting
{
	public OpenableProject(String name) throws Exception
	{
		super(name);
	}

	public boolean isOpen()
	{
		return isOpenFlag;
	}
	
	public void setIsOpen(boolean newValue)
	{
		isOpenFlag = newValue;
	}
	
	boolean isOpenFlag;
}
