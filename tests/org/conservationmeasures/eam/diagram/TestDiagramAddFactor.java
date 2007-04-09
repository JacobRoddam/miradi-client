/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactor;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.jgraph.graph.GraphConstants;

public class TestDiagramAddFactor extends EAMTestCase
{
	public TestDiagramAddFactor(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
	}
	
	public void tearDown() throws Exception
	{
		project.close();
	}
	
	public void testBadInsert() throws Exception
	{
		Command insertCommand = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()));
		try
		{
			EAM.setLogToString();
			insertCommand.execute(project);
			fail("should have thrown");
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
	}

	public void testInsertTarget() throws Exception
	{
		project.createNodeAndAddToDiagram(ObjectType.TARGET);
		DiagramModel model = project.getDiagramModel();
		FactorCell insertedNode = (FactorCell)model.getAllDiagramFactors().get(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", insertedNode.getLabel());
		DiagramFactorId id = insertedNode.getDiagramFactorId();
		FactorCell foundNode = model.getFactorCellById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a target?", foundNode.isTarget());
		assertEquals(Factor.TYPE_TARGET, foundNode.getUnderlyingFactorType());
	}

	public void testInsertFactor() throws Exception
	{
		project.createNodeAndAddToDiagram(ObjectType.CAUSE);
		DiagramModel model = project.getDiagramModel();
		FactorCell insertedNode = (FactorCell)model.getAllDiagramFactors().get(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", insertedNode.getLabel());
		DiagramFactorId id = insertedNode.getDiagramFactorId();
		FactorCell foundNode = model.getFactorCellById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a contributing factor?", foundNode.isContributingFactor());
		assertEquals(Factor.TYPE_CAUSE, foundNode.getUnderlyingFactorType());
	}

	public void testInsertIntervention() throws Exception
	{
		project.createNodeAndAddToDiagram(ObjectType.STRATEGY);
		DiagramModel model = project.getDiagramModel();
		FactorCell insertedNode = (FactorCell)model.getAllDiagramFactors().get(0);
		Rectangle2D bounds = GraphConstants.getBounds(insertedNode.getAttributes());
		assertEquals("wrong x?", 0, (int)bounds.getX());
		assertEquals("wrong y?", 0, (int)bounds.getY());
		assertContains("wrong text?", "", insertedNode.getLabel());
		DiagramFactorId id = insertedNode.getDiagramFactorId();
		FactorCell foundNode = model.getFactorCellById(id);
		assertEquals("can't find node?", insertedNode, foundNode);
		assertTrue("not a strategy?", foundNode.isStrategy());
		assertEquals(Factor.TYPE_STRATEGY, foundNode.getUnderlyingFactorType());
	}
	
	ProjectForTesting project;
}
