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


package org.conservationmeasures.eam.diagram.nodes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.martus.util.xml.XmlUtilities;


public abstract class MultilineNodeRenderer extends JComponent implements CellViewRenderer
{
	public MultilineNodeRenderer()
	{
		label = new JLabel();
	}

	abstract Dimension getInsetDimension();
	abstract void fillShape(Graphics g);
	abstract void drawBorder(Graphics2D g2);
	
	//Windows 2000 Quirk, this needs to be set or the graphic isn't filled in
	public void setPaint(Graphics2D g2, Color color) 
	{
		g2.setPaint(new GradientPaint(0, 0, color,
				getWidth(), getHeight(), color, false));
	}

	public void paint(Graphics g1)
	{
		Graphics2D g2 = (Graphics2D) g1;
		if (super.isOpaque())
		{
			g2.setColor(super.getBackground());
			if (gradientColor != null && !preview)
			{
				setOpaque(false);
				g2.setPaint(new GradientPaint(0, 0, getBackground(),
						getWidth(), getHeight(), gradientColor, true));
			}
			fillShape(g2);
		}
		int xInset = getInsetDimension().width;
		int yInset = getInsetDimension().height;
		label.setBorder(new EmptyBorder(yInset, xInset, yInset, xInset));
		label.setSize(getSize());
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.TOP);
		label.paint(g2);
		
		if (bordercolor != null)
		{
			Color color = bordercolor;
			Stroke stroke = new BasicStroke(borderWidth);
			g2.setColor(color);
			g2.setStroke(stroke);
			drawBorder(g2);
		}
		if (selected)
		{
			Color color = graph.getHighlightColor();
			Stroke stroke = GraphConstants.SELECTION_STROKE;
			g2.setColor(color);
			g2.setStroke(stroke);
			drawBorder(g2);
		}
	}
	
	public Component getRendererComponent(JGraph graphToUse, CellView view,
			boolean sel, boolean focus, boolean previewMode)
	{
		String text = view.getCell().toString();
		String formattedLabel = HTML_BEFORE_TEXT + XmlUtilities.getXmlEncoded(text) + HTML_AFTER_TEXT;
		label.setText(formattedLabel);
		
		graph = graphToUse;
		selected = sel;
		preview = previewMode;
		installAttributes(view.getAllAttributes());
		return this;
	}
	
	protected void installAttributes(Map attributes)
	{
		setOpaque(GraphConstants.isOpaque(attributes));
		Color foreground = GraphConstants.getForeground(attributes);
		setForeground((foreground != null) ? foreground : graph.getForeground());
		Color background = GraphConstants.getBackground(attributes);
		setBackground((background != null) ? background : graph.getBackground());
		Font font = GraphConstants.getFont(attributes);
		label.setFont((font != null) ? font : graph.getFont());
		Border border = GraphConstants.getBorder(attributes);
		bordercolor = GraphConstants.getBorderColor(attributes);
		if (border != null)
			setBorder(border);
		else if (bordercolor != null)
		{
			borderWidth = Math.max(1, Math.round(GraphConstants
					.getLineWidth(attributes)));
			setBorder(BorderFactory.createLineBorder(bordercolor, borderWidth));
		}
		gradientColor = GraphConstants.getGradientColor(attributes);
	
	}

	public static final String HTML_AFTER_TEXT = "</font></div></html>";
	public static final String HTML_BEFORE_TEXT = "<html><div align='center'><font size='4'>";

	JGraph graph;
	JLabel label;
	int borderWidth;
	Color bordercolor;
	Color gradientColor;
	boolean selected;
	boolean preview;
}
