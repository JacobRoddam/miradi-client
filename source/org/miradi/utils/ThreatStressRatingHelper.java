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
package org.miradi.utils;

import java.util.Vector;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Cause;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;

public class ThreatStressRatingHelper
{
	public ThreatStressRatingHelper(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public ORefList getRelatedThreatStressRatingRefs(FactorLink threatLink) throws Exception
	{
		Vector<ThreatStressRating> threatStressRatings = getRelatedThreatStressRatings(threatLink);
		return new ORefList(threatStressRatings.toArray(new ThreatStressRating[0]));
	}
	
	private Vector<ThreatStressRating> getRelatedThreatStressRatings(FactorLink threatLink) throws Exception
	{
		if (threatLink.isThreatTargetLink())
			return getRelatedThreatStressRatings(threatLink.getUpstreamThreatRef(), threatLink.getDownstreamTargetRef());
		
		return new Vector<ThreatStressRating>();
	}
	
	public Vector<ThreatStressRating> getRelatedThreatStressRatings(ORef threatRef, ORef targetRef)
	{
		threatRef.ensureType(Cause.getObjectType());
		targetRef.ensureType(Target.getObjectType());
		
		Target target = Target.find(getProject(), targetRef);
		ORefList stressRefs = target.getStressRefs();
		Cause threat = Cause.find(getProject(), threatRef);
		ORefList threatStressRatingReferrerRefs = threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		Vector<ThreatStressRating> threatStressRatings = new Vector();
		for (int index = 0; index < threatStressRatingReferrerRefs.size(); ++index)
		{
			ThreatStressRating threatStressRating = ThreatStressRating.find(getProject(), threatStressRatingReferrerRefs.get(index));
			if (stressRefs.contains(threatStressRating.getStressRef()))
				threatStressRatings.add(threatStressRating);
		}
		
		return threatStressRatings;
	}

	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
