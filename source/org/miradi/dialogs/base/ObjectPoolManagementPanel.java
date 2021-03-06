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

import javax.swing.Icon;

import org.miradi.main.MainWindow;
import org.miradi.rtf.RtfManagementExporter;
import org.miradi.rtf.RtfWriter;

abstract public class ObjectPoolManagementPanel extends ObjectManagementPanel
{
	public ObjectPoolManagementPanel(MainWindow mainWindowToUse, ObjectCollectionPanel tablePanelToUse, AbstractObjectDataInputPanel propertiesPanelToUse) throws Exception
	{
		super(mainWindowToUse, tablePanelToUse, propertiesPanelToUse);
	}
	
	@Override
	public boolean isRtfExportable()
	{
		return true;
	}
		
	@Override
	public void exportRtf(RtfWriter writer) throws Exception
	{
		new RtfManagementExporter(getProject()).writeManagement(getTableExporter(), writer);
	}
			
	@Override
	abstract public Icon getIcon();	
}
