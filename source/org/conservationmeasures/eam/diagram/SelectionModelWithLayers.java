/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Vector;

import org.jgraph.graph.DefaultGraphSelectionModel;

public class SelectionModelWithLayers extends DefaultGraphSelectionModel
{
	public SelectionModelWithLayers(DiagramComponent diagramToUse)
	{
		super(diagramToUse);
		diagram = diagramToUse;
	}
	
	private boolean isSelectable(Object cellObject)
	{
		return true;
	}

	public void addSelectionCell(Object cell)
	{
		if(isSelectable(cell))
			super.addSelectionCell(cell);
	}

	public void addSelectionCells(Object[] cells)
	{
		Vector selectableCells = new Vector();
		for(int i=0; i < cells.length; ++i)
		{
			if(isSelectable(cells[i]))
				selectableCells.add(cells[i]);
		}
		super.addSelectionCells(selectableCells.toArray());
		selectionWasChanged();
	}

	public void setSelectionCell(Object cell)
	{
		if(isSelectable(cell))
			super.setSelectionCell(cell);
		selectionWasChanged();
	}

	public void setSelectionCells(Object[] cells)
	{
		Vector selectableCells = new Vector();
		for(int i=0; i < cells.length; ++i)
		{
			if(isSelectable(cells[i]))
				selectableCells.add(cells[i]);
		}
		super.setSelectionCells(selectableCells.toArray());
		selectionWasChanged();
	}
	
	void selectionWasChanged()
	{
		diagram.selectionWasChanged();
	}
	
	DiagramComponent diagram;
}