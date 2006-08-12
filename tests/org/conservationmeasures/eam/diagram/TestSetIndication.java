/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetIndicator;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestSetIndication extends EAMTestCase 
{
	public TestSetIndication(String name)
	{
		super(name);
	}

	public void testSetIndicator() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		CommandInsertNode insertCommand = new CommandInsertNode(DiagramNode.TYPE_TARGET);
		insertCommand.execute(project);
		BaseId id = insertCommand.getId();

		BaseId indicator3 = new BaseId(3);
		Command setIndicatorCommand = new CommandSetIndicator(id, indicator3);
		setIndicatorCommand.execute(project);

		DiagramNode found = model.getNodeById(id);
		BaseId foundindicator = found.getIndicatorId();
		assertEquals("wrong priority?", indicator3, foundindicator);
		
		project.close();
	}

}
