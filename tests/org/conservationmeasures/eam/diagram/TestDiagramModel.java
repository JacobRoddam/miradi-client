/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Set;

import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramModel extends EAMTestCase
{
	public TestDiagramModel(String name)
	{
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void testIsNode()
	{
		DiagramModel model = new DiagramModel();
		Node threat = model.createThreatNode();
		Node goal = model.createGoalNode();
		Linkage link = model.createLinkage(threat, goal);
		assertTrue("threat isn't a node?", model.isNode(threat));
		assertTrue("goal isn't a node?", model.isNode(goal));
		assertFalse("linkage is a node?", model.isNode(link));
	}
	
	public void testHasLinkage()
	{
		DiagramModel model = new DiagramModel();
		Node threat = model.createThreatNode();
		Node goal = model.createGoalNode();
		assertFalse("already linked?", model.hasLinkage(threat, goal));
		model.createLinkage(threat, goal);
		assertTrue("not linked?", model.hasLinkage(threat, goal));
		assertTrue("reverse link not detected?", model.hasLinkage(goal, threat));
	}
	
	public void testGetLinkages()
	{
		DiagramModel model = new DiagramModel();
		Node threat1 = model.createThreatNode();
		Node threat2 = model.createThreatNode();
		Node goal = model.createGoalNode();
		Linkage linkage1 = model.createLinkage(threat1, goal);
		Linkage linkage2 = model.createLinkage(threat2, goal);
		Set found = model.getLinkages(goal);
		assertEquals("Didn't see both links?", 2, found.size());
		assertTrue("missed first?", found.contains(linkage1));
		assertTrue("missed second?", found.contains(linkage2));
	}
}
