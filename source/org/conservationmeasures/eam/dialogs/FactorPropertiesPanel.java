/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.icons.ContributingFactorIcon;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DialogGridPanel;
import org.conservationmeasures.eam.utils.UiTextFieldWithLengthLimit;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;

public class FactorPropertiesPanel extends DisposablePanel
{
	public FactorPropertiesPanel(MainWindow parent,DiagramComponent diagramToUse)
	{
		mainWindow = parent;
		diagram = diagramToUse;
	}
	
	public void dispose()
	{
		detailsTab.dispose();
		if(indicatorsTab != null)
			indicatorsTab.dispose();
		if(goalsTab != null)
			goalsTab.dispose();
		if(objectivesTab != null)
			objectivesTab.dispose();
		super.dispose();
	}
	
	public void selectTab(int tabIdentifier)
	{
		switch(tabIdentifier)
		{
			case TAB_OBJECTIVES:
				tabs.setSelectedComponent(objectivesTab);
				break;
			case TAB_INDICATORS:
				tabs.setSelectedComponent(indicatorsTab);
				break;
			case TAB_GOALS:
				tabs.setSelectedComponent(goalsTab);
				break;
			default:
				tabs.setSelectedComponent(detailsTab);
				break;
		}
	}

	public void setCurrentDiagramFactor(DiagramComponent diagram, DiagramFactor diagramFactor)
	{
		this.setLayout(new BorderLayout());
		this.removeAll();
		try
		{
			currentDiagramFactor = diagramFactor;
			this.add(createLabelBar(currentDiagramFactor),
					BorderLayout.BEFORE_FIRST_LINE);
			this.add(createTabbedPane(currentDiagramFactor), BorderLayout.CENTER);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error reading activity information");
		}
	}

	public DiagramFactor getCurrentDiagramFactor()
	{
		return currentDiagramFactor;
	}

	public FactorId getCurrentFactorId()
	{
		return getCurrentDiagramFactor().getWrappedId();
	}

	private Component createLabelBar(DiagramFactor diagramFactor)
	{
		createTextField(diagramFactor.getLabel(), MAX_LABEL_LENGTH);

		DialogGridPanel grid = new DialogGridPanel();
		grid.add(new UiLabel(EAM.text("Label|Label")));
		grid.add(textField);

		grid.add(new UiLabel(EAM.text("Label|Type")));
		
		//TODO: factor labes should be centralized in a common properties file or class
		if(diagramFactor.isDirectThreat())
				grid.add(new UiLabel(EAM.text("Direct Threat"), new DirectThreatIcon(), UiLabel.LEADING));
		if (diagramFactor.isContributingFactor())
				grid.add(new UiLabel(EAM.text("Contributing Factor"), new ContributingFactorIcon(), UiLabel.LEADING));
		if (diagramFactor.isStrategy()) 
				grid.add(new UiLabel(EAM.text("Strategy"), new StrategyIcon(), UiLabel.LEADING));
		if (diagramFactor.isTarget())
				grid.add(new UiLabel(EAM.text("Target"), new TargetIcon(), UiLabel.LEADING));

		grid.add(new UiLabel());
		return grid;
	}

	private Component createTabbedPane(DiagramFactor diagramFactor) throws Exception
	{
		tabs = new JTabbedPane();
		detailsTab = new FactorDetailsTab(getProject(), diagramFactor);
		tabs.add(detailsTab, detailsTab.getPanelDescription());
		
		indicatorsTab = new IndicatorListManagementPanel(getProject(), getCurrentDiagramFactor().getWrappedId(), mainWindow.getActions());
		tabs.add(indicatorsTab, indicatorsTab.getPanelDescription());
		
		if(diagramFactor.canHaveObjectives())
		{
			objectivesTab = new ObjectiveListManagementPanel(getProject(), getCurrentDiagramFactor().getWrappedId(), mainWindow.getActions());
			tabs.add(objectivesTab, objectivesTab.getPanelDescription());
		}
		
		if(diagramFactor.canHaveGoal())
		{
			goalsTab = new GoalListManagementPanel(getProject(), getCurrentDiagramFactor().getWrappedId(), mainWindow.getActions());
			tabs.add(goalsTab, goalsTab.getPanelDescription());
		}
		
		if(diagramFactor.isStrategy())
			tabs.add(createTasksGrid(diagramFactor), EAM.text("Tab|Actions"));
		
		return tabs;
	}
	
	class FactorDetailsTab extends ModelessDialogPanel
	{
		public FactorDetailsTab(Project projectToUse, DiagramFactor diagramFactor)
		{
			realPanel = new FactorDetailsPanel(projectToUse, diagramFactor);
			add(new JScrollPane(realPanel));
		}

		public void dispose()
		{
			realPanel.dispose();
			super.dispose();
		}

		public EAMObject getObject()
		{
			return realPanel.getObject();
		}

		public String getPanelDescription()
		{
			return realPanel.getPanelDescription();
		}
		
		FactorDetailsPanel realPanel;
	}

	private Component createTasksGrid(DiagramFactor diagramFactor) throws Exception
	{
		return new ActivityListManagementPanel(getProject(), getCurrentFactorId(), mainWindow.getActions());
	}

	private Component createTextField(String initialText, int maxLength)
	{
		textField = new UiTextFieldWithLengthLimit(maxLength);
		textField.addFocusListener(new LabelFocusHandler());
		textField.requestFocus(true);

		textField.setText(initialText);
		textField.selectAll();

		JPanel component = new JPanel(new BorderLayout());
		component.add(textField, BorderLayout.LINE_START);
		return component;
	}

	class LabelFocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent event)
		{
		}

		public void focusLost(FocusEvent event)
		{
			String newText = getText();
			if(newText.equals(getCurrentDiagramFactor().getLabel()))
				return;
			try
			{
				CommandSetObjectData cmd = FactorCommandHelper
						.createSetLabelCommand(getCurrentFactorId(), newText);
				getProject().executeCommand(cmd);
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}
	}



	private Project getProject()
	{
		return getDiagram().getProject();
	}

	private DiagramComponent getDiagram()
	{
		return diagram;
	}

	private String getText()
	{
		return textField.getText();
	}



	static final int MAX_LABEL_LENGTH = 40;
	public static final int TAB_DETAILS = 0;
	public static final int TAB_INDICATORS = 1;
	public static final int TAB_OBJECTIVES = 2;
	public static final int TAB_GOALS = 3;

	JTabbedPane tabs;
	ModelessDialogPanel detailsTab;
	ObjectiveListManagementPanel objectivesTab;
	IndicatorListManagementPanel indicatorsTab;
	GoalListManagementPanel goalsTab;
	MainWindow mainWindow;
	DiagramComponent diagram;
	DiagramFactor currentDiagramFactor;
	UiTextField textField;
	boolean ignoreObjectiveChanges;
	

}
