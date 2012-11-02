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
package org.miradi.views.diagram.doers;

import java.awt.datatransfer.Transferable;

import org.miradi.diagram.DiagramModel;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.main.EAM;
import org.miradi.main.TransferableMiradiList;
import org.miradi.views.diagram.DiagramClipboard;
import org.miradi.views.diagram.LocationDoer;

abstract public class AbstractPasteDoer extends LocationDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (! isInDiagram())
			return false;
		
		try
		{
			Transferable contents = getDiagramClipboardContents();
			if(contents == null)
				return false;
			
			return contents.isDataFlavorSupported(TransferableMiradiList.miradiListDataFlavor);
		}
		catch (IllegalStateException e)
		{
			EAM.logException(e);
			return false;
		}
	}
	
	protected TransferableMiradiList getTransferableMiradiList() throws Exception
	{
		Transferable contents = getDiagramClipboardContents();
		if(!contents.isDataFlavorSupported(TransferableMiradiList.miradiListDataFlavor))
			return null;

		return (TransferableMiradiList)contents.getTransferData(TransferableMiradiList.miradiListDataFlavor);
	}

	private Transferable getDiagramClipboardContents()
	{
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		
		return clipboard.getContents();
	}
	
	protected DiagramModel getDiagramModel()
	{
		return getDiagramView().getDiagramModel();
	}
	
	protected DiagramPanel getDiagramPanel()
	{
		return getDiagramView().getDiagramPanel();
	}
	
	protected final String CANCEL_BUTTON = EAM.text("Button|Cancel");
}
