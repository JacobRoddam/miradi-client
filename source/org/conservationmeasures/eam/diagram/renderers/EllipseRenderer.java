/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Portions of this file are based on work with this copyright:
 * Copyright (c) 2001-2004, Gaudenz Alder
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of JGraph nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific
 *   prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class EllipseRenderer extends FactorRenderer
{
	public void fillShape(Graphics g, Rectangle rect, Color color)
	{
		Graphics2D g2 = (Graphics2D)g;
		Paint oldPaint = g2.getPaint();
		setPaint(g2, rect, color);
		g2.fillOval(rect.x, rect.y, rect.width, rect.height);
		g2.setPaint(oldPaint);
	}

	public void drawBorder(Graphics2D g2, Rectangle rect, Color color)
	{
		setPaint(g2, rect, color);
		g2.drawOval(rect.x, rect.y, rect.width, rect.height);
	}

	public static Point getPerimeterPoint(Point2D outsidePoint, Rectangle2D enclosingRect)
	{
		double x = enclosingRect.getX();
		double y = enclosingRect.getY();
		double a = (enclosingRect.getWidth() + 1) / 2;
		double b = (enclosingRect.getHeight() + 1) / 2;
		
		// x0,y0 - center of ellipse
		double x0 = x + a;
		double y0 = y + b;
	
		// x1, y1 - point
		double x1 = outsidePoint.getX();
		double y1 = outsidePoint.getY();
		
		// calculate straight line equation through point and ellipse center
		// y = d * x + h
		double dx = x1 - x0;
		double dy = y1 - y0;
	
		if (dx == 0)
		    return new Point((int)x0, (int)(y0 + b * dy / Math.abs(dy)));
	
		double d = dy / dx;
		double h = y0 - d * x0;
					
		// calculate intersection
		double e = a * a * d * d + b * b;
		double f = - 2 * x0 * e;
		double g = a * a * d * d * x0 * x0 + b * b * x0 * x0 - a * a * b * b;
		
		double det = Math.sqrt(f * f - 4 * e * g);
		
		// two solutions (perimeter points)
		double xout1 = (-f + det) / (2 * e);
		double xout2 = (-f - det) / (2 * e);
		double yout1 = d * xout1 + h;
		double yout2 = d * xout2 + h;
		
		double dist1 = Math.sqrt(Math.pow((xout1 - x1), 2)
								 + Math.pow((yout1 - y1), 2));
		double dist2 = Math.sqrt(Math.pow((xout2 - x1), 2)
								 + Math.pow((yout2 - y1), 2));
		
		// correct solution
		double xout, yout;
		
		if (dist1 < dist2) {
			xout = xout1;
			yout = yout1;
		} else {
			xout = xout2;
			yout = yout2;
		}
		
		return new Point((int)xout, (int)yout);
	}
}

