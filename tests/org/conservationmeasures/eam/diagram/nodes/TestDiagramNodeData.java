/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramNodeData extends EAMTestCase 
{

	public TestDiagramNodeData(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		NodeType nodeAType = DiagramNode.TYPE_STRESS;
		ConceptualModelFactor cmFactor = new ConceptualModelFactor(nodeAType);
		DiagramNode nodeA = DiagramNode.wrapConceptualModelObject(cmFactor);
		String nodeAText = "Node A";
		Point location = new Point(5,22);
		int id = 2;
		nodeA.setText(nodeAText);
		nodeA.setLocation(location);
		cmFactor.setId(id);
		NodeDataMap nodeAData = nodeA.createNodeDataMap();
		
		assertEquals("Text incorrect", nodeAText, nodeAData.getString(DiagramNode.TAG_VISIBLE_LABEL));
		assertEquals("location incorrect", location, nodeAData.getPoint(DiagramNode.TAG_LOCATION));
		assertEquals("id incorrect", id, nodeAData.getInt(DiagramNode.TAG_ID));
	}
	
}
