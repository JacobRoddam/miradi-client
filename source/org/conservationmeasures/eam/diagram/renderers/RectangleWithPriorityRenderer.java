/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

public class RectangleWithPriorityRenderer extends RectangleRenderer
{
	public void fillShape(Graphics g, Rectangle rect, Color color)
	{
		super.fillShape(g, rect, color);
		
		Graphics2D g2 = (Graphics2D)g;
		Paint oldPaint = g2.getPaint();
		drawPriority(g, rect, g2);
		g2.setPaint(oldPaint);
	}
	
	void drawPriority(Graphics g, Rectangle rect, Graphics2D g2) 
	{
		if(priority == null || priority.isNotUsed() || priority.isNone())
			return;

		Rectangle smallRect = new Rectangle();
		smallRect.x = rect.x;
		smallRect.y = rect.y;
		smallRect.width = PRIORITY_WIDTH;
		smallRect.height = rect.height;
		setPaint(g2, smallRect, priority.getColor());
		g.fillRect(smallRect.x, smallRect.y, smallRect.width, smallRect.height);
	}

	private static final int PRIORITY_WIDTH = 6;
}
