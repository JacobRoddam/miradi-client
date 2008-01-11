/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.actions.ActionDeleteResultsChain;
import org.conservationmeasures.eam.actions.ActionDiagramProperties;
import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;

public class ResultsChainPageList extends DiagramPageList
{
	public ResultsChainPageList(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, new ObjectPoolTableModel(mainWindowToUse.getProject(), ObjectType.RESULTS_CHAIN_DIAGRAM, getTags()));
	}
	
	private static String[] getTags()
	{
		return new String[] {ResultsChainDiagram.TAG_LABEL};
	}

	public boolean isConceptualModelPageList()
	{
		return false;
	}

	public boolean isResultsChainPageList()
	{
		return true;
	}

	public int getManagedDiagramType()
	{
		return ObjectType.RESULTS_CHAIN_DIAGRAM;
	}

	public Class[] getPopUpMenuActions()
	{
		return  new Class[] {
				ActionDeleteResultsChain.class,
				ActionDiagramProperties.class,
		};
	}
}
