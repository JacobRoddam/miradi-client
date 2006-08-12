/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;

public class ThreatRatingPanel extends Box
{
	public ThreatRatingPanel(ThreatMatrixView viewToUse)
	{
		super(BoxLayout.X_AXIS);
		view = viewToUse;
		framework = view.getThreatRatingFramework();
		
		dropdowns = new HashMap();
		
		ratingSummaryLabel = new UiLabel();
		ratingSummaryLabel.setVerticalAlignment(JLabel.CENTER);
		ratingSummaryLabel.setHorizontalAlignment(JLabel.CENTER);
		
		createSummaryPanel();

		Box criteria = createCriteriaSection();
		
		add(criteria);
		add(ratingSummaryPanel);
	}
	
	public Dimension getMaximumSize()
	{
		return super.getPreferredSize();
	}

	public void setBundle(ThreatRatingBundle bundleToUse)
	{
		bundle = bundleToUse;
		updateDropdownsFromBundle();
		updateSummary();
	}

	private void createSummaryPanel()
	{
		int lineWidth = 1;
		ratingSummaryPanel = new JPanel();
		ratingSummaryPanel.setLayout(new BorderLayout());
		ratingSummaryPanel.add(ratingSummaryLabel, BorderLayout.CENTER);
		ratingSummaryPanel.setBorder(new LineBorder(Color.BLACK, lineWidth));
	}

	private Box createCriteriaSection()
	{
		int lineWidth = 1;
		Box criteria = Box.createVerticalBox();

		ThreatRatingCriterion[] criterionItems = framework.getCriteria();
		for(int i = 0; i < criterionItems.length; ++i)
		{
			ThreatRatingCriterion criterion = criterionItems[i];

			CriterionLabel criterionLabel = new CriterionLabel(criterion.getLabel());
			criterionLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			criterionLabel.setBorder(new EmptyBorder(2, 2, 2, 2));
			criterionLabel.setFont(criterionLabel.getFont().deriveFont(Font.BOLD));

			UiComboBox dropdown = createRatingDropdown(framework.getValueOptions());
			dropdown.addActionListener(new ValueListener(this, criterion));
			dropdowns.put(criterion, dropdown);

			JPanel panel = new JPanel(new BorderLayout());
			panel.setBorder(new LineBorder(Color.BLACK, lineWidth));
			panel.add(criterionLabel, BorderLayout.BEFORE_FIRST_LINE);
			panel.add(dropdown, BorderLayout.CENTER);

			criteria.add(panel);
		}
		return criteria;
	}
	
	static class CriterionLabel extends UiLabel
	{
		public CriterionLabel(String text)
		{
			super(text);
		}

		public void paint(Graphics g)
		{
			super.paint(g);
			
			Graphics2D g2 = (Graphics2D)g;
			drawCommentTriangle(g2, getBounds(), Color.BLACK);
		}

		// FIXME: Code copied directly from RectangleRenderer!
		private void drawCommentTriangle(Graphics2D g2, Rectangle rect, Color color)
		{
			int triangleInset = 15;
			Polygon triangle = new Polygon();
			triangle.addPoint(getWidth() - triangleInset, 0);
			triangle.addPoint(getWidth(), 0);
			triangle.addPoint(getWidth(), triangleInset);
			triangle.addPoint(getWidth() - triangleInset, 0);
			setPaint(g2, rect, Color.CYAN);
			g2.fill(triangle);
			setPaint(g2, rect, color);
			g2.drawPolygon(triangle);
		}

		// FIXME: Code copied directly from RectangleRenderer!
		//Windows 2000 Quirk, this needs to be set or the graphic isn't filled in
		public static void setPaint(Graphics2D g2, Rectangle rect, Color color) 
		{
			g2.setPaint(new GradientPaint(rect.x, rect.y, color,
					rect.width, rect.height, color, false));
		}

	}
	
	private void updateDropdownsFromBundle()
	{
		Iterator iter = dropdowns.keySet().iterator();
		while(iter.hasNext())
		{
			ThreatRatingCriterion criterion = (ThreatRatingCriterion)iter.next();
			UiComboBoxWithSaneActionFiring dropdown = (UiComboBoxWithSaneActionFiring)dropdowns.get(criterion);
			
			if(bundle == null)
			{
				dropdown.setSelectedItemWithoutFiring(dropdown.getItemAt(0));
			}
			else
			{
				BaseId valueId = bundle.getValueId(criterion.getId());
				if(!valueId.isInvalid())
				{
					ThreatRatingValueOption option = framework.getValueOption(valueId);
					dropdown.setSelectedItemWithoutFiring(option);
				}
			}
		}
	}

	private void updateSummary()
	{
		ThreatRatingValueOption value;
		
		if(bundle == null)
			value = framework.findValueOptionByNumericValue(0);
		else
			value = framework.getBundleValue(bundle);
		
		if(value == null)
		{
			EAM.logError("ThreatRatingPanel.updateSummary: value was null!");
			return;
		}
		
		ratingSummaryLabel.setText(value.getLabel());
		ratingSummaryLabel.setBackground(value.getColor());
		ratingSummaryPanel.setBackground(value.getColor());
	}

	private UiComboBox createRatingDropdown(ThreatRatingValueOption[] options)
	{
		UiComboBox dropDown = ThreatRatingPanel.createThreatDropDown(options);
		return dropDown;
	}
	
	public static UiComboBox createThreatDropDown(ThreatRatingValueOption[] options)
	{
		UiComboBox dropDown = new UiComboBoxWithSaneActionFiring();
		dropDown.setRenderer(new ThreatRenderer());
		
		for(int i = 0; i < options.length; ++i)
		{
			dropDown.addItem(options[i]);
		}
		return dropDown;
	}
	
	public void valueWasChanged(ThreatRatingCriterion criterion, ThreatRatingValueOption value)
	{
		if(bundle == null)
			return;
		bundle.setValueId(criterion.getId(), value.getId());
		updateSummary();
		try
		{
			view.setBundleValue(criterion, value);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error setting value"));
		}
	}
	
	static class ValueListener implements ActionListener
	{
		public ValueListener(ThreatRatingPanel panelToUse, ThreatRatingCriterion criterionToUse)
		{
			panel = panelToUse;
			criterion = criterionToUse;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			EAM.logWarning("ThreatRatingPanel.ValueListener.actionPerformed: " + event.getActionCommand());
			UiComboBox source = (UiComboBox)event.getSource();
			ThreatRatingValueOption selected = (ThreatRatingValueOption)source.getSelectedItem();
			panel.valueWasChanged(criterion, selected);
		}
		
		ThreatRatingPanel panel;
		ThreatRatingCriterion criterion;
	}
	
	ThreatMatrixView view;
	ThreatRatingFramework framework;
	ThreatRatingBundle bundle;
	UiLabel ratingSummaryLabel;
	JPanel ratingSummaryPanel;
	Map dropdowns;
}