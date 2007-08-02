/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableMiradiList;

public class Paste extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (! isDiagramView())
			return false;
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		if(contents == null)
			return false;

		return contents.isDataFlavorSupported(TransferableMiradiList.miradiListDataFlavor);
	}

	public void doIt() throws CommandFailedException
	{
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		getProject().executeCommand(new CommandBeginTransaction());
		try 
		{	
			Transferable contents = clipboard.getContents(null);
			if(!contents.isDataFlavorSupported(TransferableMiradiList.miradiListDataFlavor))
				return;
			
			TransferableMiradiList list = (TransferableMiradiList)contents.getTransferData(TransferableMiradiList.miradiListDataFlavor);
			DiagramPaster diagramPaster = new DiagramPaster(getDiagramView().getDiagramModel(), list);
			if (! diagramPaster.canPaste())
			{
				EAM.notifyDialog(EAM.text("Contributing Factors and Direct Threats cannot be pasted into a Results Chain; " +
											"Intermediate Results and Threat Reduction Results cannot be pasted into a Conceptual Model."));
				return;
			}

			paste(diagramPaster);
			clipboard.incrementPasteCount();
			possiblyNotitfyUserIfDataWasLost(diagramPaster);
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		} 
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void possiblyNotitfyUserIfDataWasLost(DiagramPaster diagramPaster) throws Exception
	{
		if (!diagramPaster.wasAnyDataLost())
			return;
		
		EAM.notifyDialog(EAM.text("Some of the data could not be moved to this project because " +
								  "it refers to other data that only exists in the old project"));
	}

	protected void paste(DiagramPaster diagramPaster) throws Exception
	{
		diagramPaster.pasteFactorsAndLinks(getLocation());
	}
}
