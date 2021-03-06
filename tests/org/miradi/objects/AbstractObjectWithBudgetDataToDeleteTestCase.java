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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.CommandVector;

abstract public class AbstractObjectWithBudgetDataToDeleteTestCase extends ObjectTestCase
{
	public AbstractObjectWithBudgetDataToDeleteTestCase(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		getProject().disableLeaderEnsurer();
	}

	public void testDeleteChildren() throws Exception
	{
		verifyChildren(BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS);
		verifyChildren(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
	}

	private void verifyChildren(String tag) throws Exception
	{
		BaseObject baseObject = createObjectWithChildrenToDelete();
		ORefList assignmentRefs = baseObject.getSafeRefListData(tag);
		assertTrue("should have atleast one assignment?", assignmentRefs.size() > 0);

		CommandVector commandToDeleteChildren = baseObject.createCommandsToDeleteChildren();
		getProject().executeCommands(commandToDeleteChildren);
		assertEquals("should not have any children?", 0, baseObject.getSafeRefListData(tag).size());

		verifyChildrenNoLongerExist(assignmentRefs);
	}

	private void verifyChildrenNoLongerExist(ORefList refsToVerifyExistance)
	{
		for (int index = 0; index < refsToVerifyExistance.size(); ++index)
		{
			BaseObject foundChild = BaseObject.find(getProject(), refsToVerifyExistance.get(index));
			assertEquals("child object was not deleted?", null, foundChild);
		}
	}

	private BaseObject createObjectWithChildrenToDelete() throws Exception
	{
		BaseObject parentObject = createParentObject();
		getProject().addExpenseWithValue(parentObject);
		getProject().addResourceAssignment(parentObject);

		return parentObject;
	}

	protected BaseObject createParentObject() throws Exception
	{
		ORef ref = getProject().createObject(getType());
		return BaseObject.find(getProject(), ref);
	}

	abstract protected int getType();
}
