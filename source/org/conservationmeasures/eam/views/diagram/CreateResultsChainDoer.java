/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.ResultsChainCreatorHelper;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateResultsChainDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (! isDiagramView())
			return false;
		
		Factor[] selectedFactors = getDiagramView().getDiagramPanel().getOnlySelectedFactors();
		if (selectedFactors.length == 0)
			return false;
		
		if (! areAllStrategies(selectedFactors))
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;

		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ResultsChainCreatorHelper creatorHelper = new ResultsChainCreatorHelper(getProject(), getDiagramView().getDiagramPanel());
			creatorHelper.createResultsChain();
		}
		catch (Exception e) 
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}	
	
	private boolean areAllStrategies(BaseObject[] selectedObjects)
	{
		for (int i = 0; i < selectedObjects.length; i++)
		{
			ORef ref = selectedObjects[i].getRef();
			if (ref.getObjectType() != ObjectType.STRATEGY)
				return false;
		}
		
		return true;
	}
}
