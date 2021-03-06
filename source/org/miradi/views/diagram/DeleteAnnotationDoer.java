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
package org.miradi.views.diagram;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.utils.CommandVector;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.diagram.doers.HideStressBubbleDoer;

import java.text.ParseException;
import java.util.Arrays;

public abstract class DeleteAnnotationDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		return (getSingleSelected(getAnnotationType()) != null);
	}

	@Override
	protected void doIt() throws Exception
	{
		if(!isAvailable())
			return;
	
		String[] buttons = {EAM.text("Delete"), EAM.text("Retain"), };
		String[] dialogText = getDialogText();
		if(!EAM.confirmDialog(EAM.text("Delete"), dialogText, buttons))
			return;
	
		try
		{
			String tag = getAnnotationIdListTag();
			BaseObject annotationToDelete = getObjects()[0];
			BaseObject selectedFactor = getParent(annotationToDelete);

			deleteAnnotationViaCommands(getProject(), selectedFactor, annotationToDelete, tag);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}	
	}

	protected BaseObject getParent(BaseObject annotationToDelete)
	{
		return annotationToDelete.getOwner();
	}
	
	protected BaseObject getReferrerParent(BaseObject annotationToDelete)
	{
		ORefList referrerRefs = annotationToDelete.findAllObjectsThatReferToUs();
		if (referrerRefs.isEmpty())
			return null;
			
		return BaseObject.find(getProject(), referrerRefs.get(0));
	}

	private void deleteAnnotationViaCommands(Project project, BaseObject owner, BaseObject annotationToDelete, String annotationIdListTag) throws Exception
	{
		Command[] commands = buildCommandsToDeleteAnnotation(project, owner, annotationIdListTag, annotationToDelete);
		getProject().executeCommands(commands);
	}
	
	public static Command[] buildCommandsToDeleteAnnotation(Project project, BaseObject owner, String annotationIdListTag, BaseObject annotationToDelete) throws CommandFailedException, ParseException, Exception
	{
		CommandVector commands = new CommandVector();
		commands.add(buildCommandToRemoveAnnotationFromObject(owner, annotationIdListTag, annotationToDelete.getRef()));
		commands.addAll(buildCommandsToDeleteReferredObjects(project, owner, annotationIdListTag, annotationToDelete));
		commands.addAll(buildCommandsToDeleteReferringObjects(project, owner, annotationIdListTag, annotationToDelete));		
		commands.addAll(annotationToDelete.createCommandsToDeleteChildrenAndObject());
		
		return commands.toArray(new Command[0]);
	}

	private static CommandVector buildCommandsToDeleteReferredObjects(Project project, BaseObject owner, String annotationIdListTag,	BaseObject annotationToDelete) throws Exception
	{
		CommandVector commands = new CommandVector();
		if (KeyEcologicalAttribute.is(annotationToDelete.getType()))
		{
			commands.addAll(buildCommandsToDeleteKEAIndicators(project, (KeyEcologicalAttribute) annotationToDelete));
		}
		
		return commands;
	}
	
	private static CommandVector buildCommandsToDeleteReferringObjects(Project project, BaseObject owner, String annotationIdListTag, BaseObject annotationToDelete) throws Exception
	{
		CommandVector commands = new CommandVector();
		if (Stress.is(annotationToDelete.getType()))
			commands.addAll(createCommandsToDeleteDiagramFactors(project, annotationToDelete));
		
		return commands;
	}

	private static CommandVector createCommandsToDeleteDiagramFactors(Project project, BaseObject annotationToDelete) throws Exception
	{
		CommandVector commandsToHide = new CommandVector();
		ORefList diagramFactorRefs = annotationToDelete.findObjectsThatReferToUs(DiagramFactorSchema.getObjectType());
		for (int index = 0; index < diagramFactorRefs.size(); ++index)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(index));
			ORefList conceptualModelRefs = diagramFactor.findObjectsThatReferToUs(ConceptualModelDiagramSchema.getObjectType());
			for (int diagramRefIndex = 0; diagramRefIndex < conceptualModelRefs.size(); ++diagramRefIndex)
			{
				ConceptualModelDiagram conceptualModel = ConceptualModelDiagram.find(project, conceptualModelRefs.get(diagramRefIndex));
				commandsToHide.addAll(HideStressBubbleDoer.createCommandsToHideDiagramFactorForNonSelectedFactors(conceptualModel, diagramFactor));
			}
		}
		
		return commandsToHide;
	}
	
	private static CommandSetObjectData buildCommandToRemoveAnnotationFromObject(BaseObject owner, String annotationIdListTag, ORef refToRemove) throws ParseException
	{
		ObjectData objectData = owner.getField(annotationIdListTag);
		if (objectData.isIdListData())
			return CommandSetObjectData.createRemoveIdCommand(owner, annotationIdListTag, refToRemove.getObjectId());
		
		return CommandSetObjectData.createRemoveORefCommand(owner, annotationIdListTag, refToRemove);
	}

	public Factor getSelectedFactor()
	{
		BaseObject selected = getView().getSelectedObject();
		if(selected == null)
			return null;
		
		if(! Factor.isFactor(selected.getType()))
			return null;
		
		return (Factor)selected;
	}

	private static CommandVector buildCommandsToDeleteKEAIndicators(Project project, KeyEcologicalAttribute kea) throws Exception
	{
		CommandVector commands = new CommandVector();
		IdList indicatorList = kea.getIndicatorIds();
		for (int i  = 0; i < indicatorList.size(); i++)
		{
			BaseObject thisAnnotation = project.findObject(ObjectType.INDICATOR,  indicatorList.get(i));
			Command[] deleteCommands = DeleteIndicator.buildCommandsToDeleteAnnotation(project, kea, KeyEcologicalAttribute.TAG_INDICATOR_IDS, thisAnnotation);
			commands.addAll(Arrays.asList(deleteCommands));
		}

		return commands;
	}	

	abstract public String[] getDialogText();
	abstract public String getAnnotationIdListTag();
	abstract public int getAnnotationType();
}
