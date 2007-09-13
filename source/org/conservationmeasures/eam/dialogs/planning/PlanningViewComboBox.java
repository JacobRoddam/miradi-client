/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.utils.UiComboBoxWithSaneActionFiring;

abstract public class PlanningViewComboBox extends UiComboBoxWithSaneActionFiring implements RowColumnProvider
{
	public PlanningViewComboBox(Project projectToUse, ChoiceItem[] choices) throws Exception
	{
		super(choices);
		
		project = projectToUse;
		addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event)
	{		
		try
		{
			saveStateIfNeeded();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: " + e.getMessage());
		}
	}

	private void saveStateIfNeeded() throws Exception
	{
		if (!needsSave())
			return;

		project.executeCommand(new CommandBeginTransaction());
		try
		{
			ChoiceItem selectedItem = (ChoiceItem) getSelectedItem();
			saveSelectedItem(getChoiceTag(), selectedItem.getCode());
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	private void saveSelectedItem(String tag, String newValue) throws Exception
	{	
		ViewData viewData = getProject().getCurrentViewData();
		String existingValue = viewData.getData(tag);
		if (existingValue.equals(newValue))
			return;

		CommandSetObjectData setComboItem = new CommandSetObjectData(viewData.getRef(), tag, newValue);
		getProject().executeCommand(setComboItem);
	}

	
	protected Project getProject()
	{
		return project;
	}

	abstract public String getChoiceTag();
	abstract boolean needsSave() throws Exception;
	
	private Project project;
	
}
