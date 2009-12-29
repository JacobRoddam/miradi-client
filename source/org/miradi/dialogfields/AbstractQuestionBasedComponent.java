/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
package org.miradi.dialogfields;


import java.util.Set;

import javax.swing.JToggleButton;
import javax.swing.event.ListSelectionListener;

import org.martus.util.xml.XmlUtilities;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

abstract public class AbstractQuestionBasedComponent extends AbstractDataValueListComponent
{
	public AbstractQuestionBasedComponent(ChoiceQuestion questionToUse, int columnCount, ListSelectionListener listener)
	{
		super(questionToUse, columnCount, listener);
		
		codesToDisable = new CodeList();
	}
	
	protected CodeList getSelectedCodes()
	{
		CodeList codes = new CodeList();
		Set<ChoiceItem> choices =  choiceItemToToggleButtonMap.keySet();
		for(ChoiceItem choiceItem : choices)
		{	
			JToggleButton toggleButton = choiceItemToToggleButtonMap.get(choiceItem);
			if (toggleButton.isSelected())
			{
				codes.add(choiceItem.getCode());
			}
		}
		
		return codes;
	}
	
	protected void updateToggleButtonSelections(CodeList codes)
	{
		enableSkipNotification();
		try
		{
			for (int index = 0; index<choiceItems.length; ++index)
			{
				toggleButtons[index].setSelected(false);
				ChoiceItem choiceItem = choiceItems[index];
				boolean isChecked  = codes.contains(choiceItem.getCode());
				toggleButtons[index].setSelected(isChecked);
			}
			
			setSameToolTipForAllToggleButtons();
		}
		finally
		{
			disableSkipNotification();
		}
	}

	protected String setSameToolTipForAllToggleButtons()
	{
		String partialToolTip = ""; 
		int selectionCount = 0;
		for (int index = 0; (index < choiceItems.length && selectionCount <= MAX_ITEMS_COUNT_IN_TOOLTIP); ++index)
		{
			ChoiceItem choiceItem = choiceItems[index];
			if (toggleButtons[index].isSelected() )
			{
				partialToolTip += XmlUtilities.getXmlEncoded(choiceItem.getLabel()) + "<BR>";
				++selectionCount;
			}
		}
		
		String moreText = "";
		if (getSelectedCodes().size() > (MAX_ITEMS_COUNT_IN_TOOLTIP  + 1))
			moreText = "...more";
		
		String toolTip = "<HTML>" + partialToolTip + moreText + "</HTML>";
		for (int index = 0; index < choiceItems.length; ++index)
		{
			toggleButtons[index].setToolTipText(toolTip);
		}
		
		return toolTip;
	}
	
	public void setEnabled(boolean isValidObject)
	{
		super.setEnabled(isValidObject);
		disableCheckBoxes();
	}

	private void disableCheckBoxes()
	{
		for (int index = 0; index<toggleButtons.length; ++index)
		{
			ChoiceItem choiceItem = choiceItems[index];
			if (codesToDisable.contains(choiceItem.getCode()))
				toggleButtons[index].setEnabled(false);
		}
	}

	public void setDisabledCodes(CodeList codesToDiableToUse)
	{
		codesToDisable = codesToDiableToUse;
	}
	
	@Override
	public String getText()
	{
		throw new RuntimeException(EAM.text("Unexpected call to getText"));
	}

	@Override
	public void setText(String codesToUse)
	{
		throw new RuntimeException(EAM.text("Unexpected call to setText"));
	}
	
	private CodeList codesToDisable;
	private static final int MAX_ITEMS_COUNT_IN_TOOLTIP = 5;
}
