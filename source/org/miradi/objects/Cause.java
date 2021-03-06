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
package org.miradi.objects;

import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ThreatClassificationQuestion;
import org.miradi.schemas.CauseSchema;
import org.miradi.utils.CommandVector;

public class Cause extends Factor
{
	public Cause(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static CauseSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static CauseSchema createSchema(ObjectManager objectManager)
	{
		return (CauseSchema) objectManager.getSchemas().get(ObjectType.CAUSE);
	}

	@Override
	public boolean isCause()
	{
		return true;
	}
	
	@Override
	public boolean isContributingFactor()
	{
		return !isDirectThreat();
	}
		
	@Override
	public boolean isDirectThreat()
	{
		return getBooleanData(TAG_IS_DIRECT_THREAT);
	}

	@Override
	public boolean canHaveObjectives()
	{
		return true;
	}
	
	@Override
	public String getTypeName()
	{
		if(isDirectThreat())
			return OBJECT_NAME_THREAT;

		return OBJECT_NAME_CONTRIBUTING_FACTOR;
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_TAXONOMY_CODE_VALUE))
		{
			String code = getData(TAG_TAXONOMY_CODE);
			ThreatClassificationQuestion question = new ThreatClassificationQuestion();
			ChoiceItem choice = question.findChoiceByCode(code);
			return choice.getLabel();
		}
		
		return super.getPseudoData(fieldTag);
	}

	@Override
	protected CommandVector createCommandsToDereferenceObject() throws Exception
	{
		CommandVector commandsToDereferences = super.createCommandsToDereferenceObject();
		commandsToDereferences.addAll(buildCommandsToDeleteRelatedAbstractThreatData());

		return commandsToDereferences;
	}

	private CommandVector buildCommandsToDeleteRelatedAbstractThreatData()
	{
		CommandVector commands = new CommandVector();

		ORefList abstractThreatRatingRefs = AbstractThreatRatingData.findThreatRatingDataRefsForThreat(getProject(), getRef());
		for (ORef abstractThreatRatingRef : abstractThreatRatingRefs)
		{
			commands.add(new CommandDeleteObject(abstractThreatRatingRef));
		}

		return commands;
	}

	public CommandVector getCommandsToRemoveFromThreatReductionResults()
	{
		CommandVector clearDirectThreatsFromThreatReductionResults = new CommandVector();

		EAMObjectPool pool = getProject().getPool(ObjectType.THREAT_REDUCTION_RESULT);
		ORefList orefList = pool.getORefList();
		for (int i = 0; i < orefList.size(); ++i)
		{
			ThreatReductionResult threatReductionResult = (ThreatReductionResult) getProject().findObject(orefList.get(i));
			ORef directThreatRef = ORef.createFromString(threatReductionResult.getRelatedDirectThreatRefAsString());
			if (! directThreatRef.equals(this.getRef()))
				continue;

			CommandSetObjectData setDirectThreat = new CommandSetObjectData(threatReductionResult.getRef(), ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, ORef.INVALID.toString());
			clearDirectThreatsFromThreatReductionResults.add(setDirectThreat);
		}

		return clearDirectThreatsFromThreatReductionResults;
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == CauseSchema.getObjectType();
	}
	
	public static boolean is(BaseObject object)
	{
		return is(object.getType());
	}
	
	public static boolean isDirectThreat(BaseObject object)
	{
		if(!is(object))
			return false;
		
		Cause cause = (Cause)object;
		return cause.isDirectThreat();
	}

	public static Cause find(ObjectManager objectManager, ORef causeRef)
	{
		return (Cause) objectManager.findObject(causeRef);
	}
	
	public static Cause find(Project project, ORef causeRef)
	{
		return find(project.getObjectManager(), causeRef);
	}
	
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	public static final String TAG_IS_DIRECT_THREAT = "IsDirectThreat";
	
	public static final String OBJECT_NAME_THREAT = "DirectThreat";
	public static final String OBJECT_NAME_CONTRIBUTING_FACTOR = "ContributingFactor";
}
