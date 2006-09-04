package org.conservationmeasures.eam.views.umbrella;
import java.awt.Dimension;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetIndicator;
import org.conservationmeasures.eam.commands.CommandSetNodeName;
import org.conservationmeasures.eam.commands.CommandSetNodeObjectives;
import org.conservationmeasures.eam.commands.CommandSetNodePriority;
import org.conservationmeasures.eam.commands.CommandSetNodeSize;
import org.conservationmeasures.eam.commands.CommandSetTargetGoal;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.GoalIds;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.ids.ObjectiveIds;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestUndoRedo extends EAMTestCase 
{

	public TestUndoRedo(String name) 
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}
	
	public void testBasics() throws Exception
	{
		String target1Text = "Target 1 Text";
		project.executeCommand(new CommandBeginTransaction());
		ModelNodeId insertedId = insertFactor(project);
		project.executeCommand(new CommandSetNodeName(insertedId, target1Text));
		project.executeCommand(new CommandEndTransaction());
		assertEquals("Should have 1 node now.", 1, project.getDiagramModel().getNodeCount());
		
		project.getDiagramModel().getNodeById(insertedId);
		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals("Should have 0 nodes now.", 0, project.getDiagramModel().getNodeCount());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();

		Vector inserted = project.getDiagramModel().getAllNodes();
		
		assertEquals("Should have 1 node again after redo.", 1, project.getDiagramModel().getNodeCount());
		assertEquals("wrong number of nodes after redo?", 1, inserted.size());
		DiagramNode node = (DiagramNode)inserted.get(0);
		assertTrue(project.getDiagramModel().isNodeInProject(node));
		assertEquals("Incorrect label?", target1Text, node.getLabel());
		
		undo.doIt();
		assertEquals("Should have 0 nodes again.", 0, project.getDiagramModel().getNodeCount());
	}

	public void testUndoRedoPriority() throws Exception
	{
		BaseId insertedId = insertFactor(project);

		// Deprecated command--just make sure it doesn't crash
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetNodePriority(insertedId, null));
		project.executeCommand(new CommandEndTransaction());

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();

		undo.doIt();
	}
	
	public void testUndoRedoIndication() throws Exception
	{
		BaseId target1Indicator = new BaseId(2);
		ModelNodeId insertedId = insertFactor(project);

		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetIndicator(insertedId, target1Indicator));
		project.executeCommand(new CommandEndTransaction());

		assertEquals(target1Indicator, project.getDiagramModel().getNodeById(insertedId).getIndicatorId());

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals(BaseId.INVALID, project.getDiagramModel().getNodeById(insertedId).getIndicatorId());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertEquals(target1Indicator, project.getDiagramModel().getNodeById(insertedId).getIndicatorId());

		undo.doIt();
		assertEquals("Should have no indicator again", BaseId.INVALID, project.getDiagramModel().getNodeById(insertedId).getIndicatorId());
	}
	
	public void testUndoRedoObjective() throws Exception
	{
		BaseId objectiveId = project.createObject(ObjectType.OBJECTIVE);
		
		ObjectiveIds target1Objectives = new ObjectiveIds();
		target1Objectives.addId(objectiveId);
		
		ModelNodeId insertedId = insertFactor(project);

		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetNodeObjectives(insertedId, target1Objectives));
		project.executeCommand(new CommandEndTransaction());

		assertTrue(project.getDiagramModel().getNodeById(insertedId).getObjectives().hasAnnotation());
		assertEquals(1, project.getDiagramModel().getNodeById(insertedId).getObjectives().size());
		assertEquals(objectiveId, project.getDiagramModel().getNodeById(insertedId).getObjectives().getId(0));

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertFalse(project.getDiagramModel().getNodeById(insertedId).getObjectives().hasAnnotation());
		assertEquals("Should now have (No) Ojectives", 0, project.getDiagramModel().getNodeById(insertedId).getObjectives().size());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertTrue(project.getDiagramModel().getNodeById(insertedId).getObjectives().hasAnnotation());
		assertEquals(1, project.getDiagramModel().getNodeById(insertedId).getObjectives().size());
		assertEquals(objectiveId, project.getDiagramModel().getNodeById(insertedId).getObjectives().getId(0));

		undo.doIt();
		assertFalse(project.getDiagramModel().getNodeById(insertedId).getObjectives().hasAnnotation());
		assertEquals(0, project.getDiagramModel().getNodeById(insertedId).getObjectives().size());
	}

	public void testUndoRedoGoals() throws Exception
	{
		BaseId goalId = project.getGoalPool().getIds()[1];
		
		GoalIds target1Goals = new GoalIds();
		target1Goals.addId(goalId);
		
		ModelNodeId insertedId = insertTarget(project);

		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetTargetGoal(insertedId, target1Goals));
		project.executeCommand(new CommandEndTransaction());

		assertTrue(project.getDiagramModel().getNodeById(insertedId).getGoals().hasAnnotation());
		assertEquals(1, project.getDiagramModel().getNodeById(insertedId).getGoals().size());
		assertEquals(goalId, project.getDiagramModel().getNodeById(insertedId).getGoals().getId(0));

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertFalse(project.getDiagramModel().getNodeById(insertedId).getGoals().hasAnnotation());
		assertEquals("Should now have (No) Goals", 0, project.getDiagramModel().getNodeById(insertedId).getGoals().size());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertTrue(project.getDiagramModel().getNodeById(insertedId).getGoals().hasAnnotation());
		assertEquals(1, project.getDiagramModel().getNodeById(insertedId).getGoals().size());
		assertEquals(goalId, project.getDiagramModel().getNodeById(insertedId).getGoals().getId(0));

		undo.doIt();
		assertFalse(project.getDiagramModel().getNodeById(insertedId).getGoals().hasAnnotation());
		assertEquals(0, project.getDiagramModel().getNodeById(insertedId).getGoals().size());
	}
	
	public void testUndoRedoNodeSize() throws Exception
	{
		BaseId insertedId = insertFactor(project);
		DiagramNode node = project.getDiagramModel().getNodeById(insertedId);
		Dimension originalSize = node.getSize();

		Dimension newSize1 = new Dimension(5,10);
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetNodeSize(insertedId, newSize1, originalSize));
		project.executeCommand(new CommandEndTransaction());

		assertEquals(newSize1, node.getSize());

		Dimension newSize2 = new Dimension(20,30);
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetNodeSize(insertedId, newSize2, newSize1));
		project.executeCommand(new CommandEndTransaction());
		assertEquals(newSize2, node.getSize());

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals(newSize1, node.getSize());

		undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals(originalSize, node.getSize());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertEquals(newSize1, node.getSize());

		redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertEquals(newSize2, node.getSize());
	}
	
	private ModelNodeId insertFactor(Project p) throws CommandFailedException 
	{
		CommandInsertNode insert = new CommandInsertNode( DiagramNode.TYPE_FACTOR);
		p.executeCommand(insert);
		ModelNodeId insertedId = insert.getId();
		return insertedId;
	}

	private ModelNodeId insertTarget(Project p) throws CommandFailedException 
	{
		CommandInsertNode insert = new CommandInsertNode( DiagramNode.TYPE_TARGET);
		p.executeCommand(insert);
		ModelNodeId insertedId = insert.getId();
		return insertedId;
	}

	ProjectForTesting project;
}
