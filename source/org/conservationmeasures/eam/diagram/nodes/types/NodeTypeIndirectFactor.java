/*
 * Copyright 2005, The Conservation Measures Partnership & Beneficent Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes.types;

import org.conservationmeasures.eam.main.EAM;




public class NodeTypeIndirectFactor extends NodeTypeFactor
{
	public boolean isIndirectFactor()
	{
		return true;
	}

	public String toString()
	{
		return EAM.text("Type|Indirect Factor");
	}
}
