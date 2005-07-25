/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.CommandInsertGoal;
import org.conservationmeasures.eam.commands.CommandInsertThreat;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestCommandLinkNodes extends EAMTestCase
{
	public TestCommandLinkNodes(String name)
	{
		super(name);
	}

	public void testInsertConnection() throws Exception
	{
		Project project = new Project();
		DiagramModel model = project.getDiagramModel();

		CommandInsertThreat insertThreat = new CommandInsertThreat();
		insertThreat.execute(project);
		int threatId = insertThreat.getId();
		Node threat = model.getNodeById(threatId);
		CommandInsertGoal insertGoal = new CommandInsertGoal();
		insertGoal.execute(project);
		int goalId = insertGoal.getId();
		Node goal = model.getNodeById(goalId);
		
		CommandLinkNodes command = new CommandLinkNodes(threatId, goalId);
		command.execute(project);
		Linkage linkage = model.getLinkageById(command.getLinkageId());

		assertEquals("not from threat?", threat, linkage.getFromNode());
		assertEquals("not to goal?", goal, linkage.getToNode());
	}
}
