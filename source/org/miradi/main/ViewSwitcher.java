/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JComboBox;

import org.miradi.actions.Actions;
import org.miradi.actions.views.ActionViewDiagram;
import org.miradi.actions.views.ActionViewImages;
import org.miradi.actions.views.ActionViewMap;
import org.miradi.actions.views.ActionViewPlanning;
import org.miradi.actions.views.ActionViewReports;
import org.miradi.actions.views.ActionViewSchedule;
import org.miradi.actions.views.ActionViewSummary;
import org.miradi.actions.views.ActionViewTargetViability;
import org.miradi.actions.views.ActionViewThreatMatrix;
import org.miradi.actions.views.ActionViewWorkPlan;
import org.miradi.dialogs.fieldComponents.PanelComboBox;

public class ViewSwitcher extends PanelComboBox<Object>
{
	static public ViewSwitcher create(Actions actions, Class defaultActionClass)
	{
		Object[] views = getViewSwitchActions(actions);
		ViewSwitcher switcher = new ViewSwitcher(views);
		Action defaultAction = actions.get(defaultActionClass);
		switcher.addActionListener(new ActionHandler(switcher, defaultAction));
		return switcher;
	}

	public static Action[] getViewSwitchActions(Actions actions)
	{
		Action[] views = new Action[] {
			actions.get(ActionViewSummary.class),
			actions.get(ActionViewDiagram.class), 
			actions.get(ActionViewTargetViability.class),
			actions.get(ActionViewThreatMatrix.class),
			actions.get(ActionViewPlanning.class),
			actions.get(ActionViewWorkPlan.class),
			actions.get(ActionViewReports.class),
		};
		Vector<Action> viewVector = new Vector<>(Arrays.asList(views));
		
		if(Miradi.isDemoMode())
		{
			viewVector.add(actions.get(ActionViewMap.class));
			viewVector.add(actions.get(ActionViewSchedule.class));
			viewVector.add(actions.get(ActionViewImages.class));
		}
		return viewVector.toArray(new Action[0]);
	}
	
	private ViewSwitcher(Object[] choices)
	{
		super(choices);
		setToolTipText(EAM.text("TT|Switch to a different view"));
		setMaximumRowCount(choices.length);
	}
	
	@Override
	public Dimension getMaximumSize()
	{
		return getPreferredSize();
	}

	static class ActionHandler implements ActionListener
	{
		public ActionHandler(ViewSwitcher switcherToControl, Action defaultAction)
		{
			switcher = switcherToControl;
			defaultValue = defaultAction;
			selectDefaultAction();
		}
		
		public void actionPerformed(ActionEvent event)
		{
			JComboBox comboBox = (JComboBox)event.getSource();
			Action action = (Action)comboBox.getSelectedItem();
			action.actionPerformed(null);
			selectDefaultAction();
		}

		private void selectDefaultAction()
		{
			switcher.setSelectedItem(defaultValue);
		}
		
		ViewSwitcher switcher;
		Action defaultValue;
	}
}