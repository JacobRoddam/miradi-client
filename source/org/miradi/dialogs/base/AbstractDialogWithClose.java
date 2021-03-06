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
package org.miradi.dialogs.base;

import java.awt.Component;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JDialog;

import org.miradi.dialogs.fieldComponents.MiradiUIButton;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

abstract public class AbstractDialogWithClose extends DialogWithDisposablePanelAndMainWindowUpdating
{
	public AbstractDialogWithClose(JDialog owner, MainWindow mainWindow)
	{
		super(owner, mainWindow);
		
		setButtons();
	}
	
	protected AbstractDialogWithClose(MainWindow parent)
	{
		super(parent);
		
		setButtons();
	}
	
	protected AbstractDialogWithClose(MainWindow parent, DisposablePanel panel)
	{
		super(parent, panel);
		
		setButtons();
	}

	private void setButtons()
	{
		setButtons(getButtonBarComponents());
	}
	
	protected Vector<Component> getButtonBarComponents()
	{
		MiradiUIButton closeButton = new PanelButton(EAM.text("Button|Close"));
		setSimpleCloseButton(closeButton);
		
		Vector<Component> components = new Vector<Component>(); 
		components.add(Box.createHorizontalGlue());
		components.add(closeButton);
		
		return components;
	}
}
