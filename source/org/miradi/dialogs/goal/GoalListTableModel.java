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
package org.miradi.dialogs.goal;

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Goal;
import org.miradi.project.Project;

public class GoalListTableModel extends ObjectListTableModel
{
	public GoalListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef, AbstractTarget.TAG_GOAL_IDS, ObjectType.GOAL, getColumnTags());
	}

	private static String[] getColumnTags()
	{
		return new String[] {
			Goal.TAG_SHORT_LABEL,
			Goal.TAG_LABEL,
			Goal.TAG_FULL_TEXT,
		};
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}
	
	private static final String UNIQUE_MODEL_IDENTIFIER = "GoalListTableModel";
}
