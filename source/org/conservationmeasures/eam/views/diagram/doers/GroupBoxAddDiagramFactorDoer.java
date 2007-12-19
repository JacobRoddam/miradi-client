/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;

public class GroupBoxAddDiagramFactorDoer extends AbstractGroupBoxDoer
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
		
		if (!isAtLeastOneGroupBoxSelected())
			return false;
		
		return true;
	}
	
	protected void getCommandsToUpdateGroupBoxChildren() throws Exception
	{
		ORefList nonGroupBoxDiagramFactorRefs = extractNonGroupBoxDiagramFactors();
		DiagramFactor groupBoxDiagramFactor = getGroupBox();
		ORefList groupBoxChildrenRefs = groupBoxDiagramFactor.getGroupBoxChildrenRefs();
		
		ORefList diagramFactorRefsToAdd = ORefList.subtract(nonGroupBoxDiagramFactorRefs, groupBoxChildrenRefs);
		CommandSetObjectData appendCommand = CommandSetObjectData.createAppendORefListCommand(groupBoxDiagramFactor, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, diagramFactorRefsToAdd);
		getProject().executeCommand(appendCommand);
	}
}
