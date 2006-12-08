/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramConstants;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.objects.FactorLink;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.martus.swing.Utilities;

public class ArrowLineRenderer extends EdgeRenderer
{

	public Component getRendererComponent(JGraph graphToUse, CellView cellView, 
					boolean sel, boolean hasFocus, boolean previewMode)
	{
		ArrowLineRenderer renderer = 
			(ArrowLineRenderer)super.getRendererComponent(graphToUse, cellView, sel, hasFocus, previewMode);
		
		cell = (LinkCell)cellView.getCell();
		if(sel)
		{
			renderer.lineWidth = 4;
		}

		DiagramComponent diagram = (DiagramComponent)graphToUse;
		isVisible = diagram.areLinkagesVisible();
		stressText = cell.getFactorLink().getStressLabel();

		if (sel) 
		{
			Graphics2D g2 = (Graphics2D) diagram.getGraphics();
			paintSelectionBorder(g2);
		}
		
		return renderer;
	}

	protected void paintSelectionBorder(Graphics2D g2) 
	{
		g2.setStroke(GraphConstants.SELECTION_STROKE);
		g2.setColor(highlightColor);
		Dimension d = getSize();
		g2.drawRect(0, 0, d.width - 1, d.height - 1);
	}
	
	private LinkCell getLinkCell()
	{
		return cell;
	}
	
	public void paint(Graphics g)
	{
		if(!isVisible)
			return;
		
		super.paint(g);
		drawStress(g);
		
	}
	
	
	
	protected Shape createShape()
	{
		Shape shape = super.createShape();
		// To prevent drawing the line body, uncomment the following
		//view.lineShape = null;
		return shape;
	}

	protected Shape createLineEnd(int size, int style, Point2D src, Point2D dst)
	{
		if(style != ARROW_JUST_LINE)
			return super.createLineEnd(size, style, src, dst);
		
		if (src == null || dst == null)
			return null;
		int d = (int) Math.max(1, dst.distance(src));
		int ax = (int) -(size * (dst.getX() - src.getX()) / d);
		int ay = (int) -(size * (dst.getY() - src.getY()) / d);
		Polygon poly = new Polygon();
		poly.addPoint((int) dst.getX(), (int) dst.getY());
		dst.setLocation(dst.getX() + ax, dst.getY() + ay);
		poly.addPoint((int) dst.getX(), (int) dst.getY());
		return poly;
	}

	public Rectangle2D getPaintBounds(EdgeView viewToUse) 
	{
		Rectangle2D graphBounds = super.getPaintBounds(viewToUse);

		LinkCell thisCell = (LinkCell)viewToUse.getCell();
		FactorLink factorLink = thisCell.getFactorLink();
		String text = factorLink.getStressLabel();
		if (text == null || text.length()==0)
			return graphBounds;
		
		Rectangle2D union = calculateNewBoundsForStress(graphBounds, text);
		return union;
	}

	
	private Rectangle2D calculateNewBoundsForStress(Rectangle2D graphBounds, String text)
	{
		Rectangle textBounds = calcalateCenteredAndCushioned(graphBounds, text);
		Rectangle2D union = graphBounds.createUnion(textBounds);
		return union;
	}

	
	private Rectangle calcalateCenteredAndCushioned(Rectangle2D graphBounds, String text)
	{
		Graphics2D g2 = (Graphics2D)fontGraphics;
		
		TextLayout textLayout = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
		Rectangle textBounds = textLayout.getBounds().getBounds();
		
		textBounds.width = textBounds.width + 2*CUSHION;
		textBounds.height = textBounds.height +2*CUSHION;
		
		Point upperLeftToDrawText = Utilities.center(textBounds.getSize(), graphBounds.getBounds().getBounds());
		textBounds.setLocation(upperLeftToDrawText);
		return textBounds;
	}


	

	private void drawStress(Graphics g)
	{
		if(!getLinkCell().getTo().isTarget())
			return;
		
		if(stressText == null || stressText.length() < 1)
			return;
		
		Graphics2D g2 = (Graphics2D)g;
		Rectangle rectangle = calcalateCenteredAndCushioned(getBounds(), stressText);

		int arc = 5;

		g2.setColor(DiagramConstants.COLOR_STRESS);
		g2.fillRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, arc, arc);
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, arc, arc);
		g2.drawString(stressText, rectangle.x+CUSHION, rectangle.y + rectangle.height-CUSHION);
	}
	
	private static final int CUSHION = 5;
	public static final int ARROW_JUST_LINE = 23253;
	
	LinkCell cell;
	boolean isVisible;
	String stressText;
}
