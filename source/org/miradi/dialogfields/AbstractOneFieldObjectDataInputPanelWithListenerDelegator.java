/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.base.OneFieldObjectDataInputPanel;
import org.miradi.dialogs.base.RowSelectionListener;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

abstract public class AbstractOneFieldObjectDataInputPanelWithListenerDelegator extends OneFieldObjectDataInputPanel implements RowSelectionListener
{
	public AbstractOneFieldObjectDataInputPanelWithListenerDelegator(Project projectToUse, ORef orefToUse, String tagToUse,	ObjectDataInputField singleField, QuestionBasedEditorComponent editorToUse)
	{
		super(projectToUse, orefToUse, tagToUse, singleField);
		
		editor = editorToUse;
	}
	
	public void addRowSelectionListener(ListSelectionListener listener)
	{
		editor.getSafeRowSelectionHandler().addSelectionListener(listener);
	}

	public void removeRowSelectionListener(ListSelectionListener listener)
	{
		editor.getSafeRowSelectionHandler().removeSelectionListener(listener);
	}
	
	private QuestionBasedEditorComponent editor;
}
