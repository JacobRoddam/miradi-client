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
package org.miradi.project;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.CausePool;

public class TestCausePool extends TestFactorPool
{
	public TestCausePool(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		pool = project.getCausePool();
	}
	
	@Override
	public void testBasics() throws Exception
	{
		super.testBasics();
		assertEquals("wrong direct threat count?", 0, pool.getDirectThreats().length);
	}
	
	@Override
	public int getObjectType()
	{
		return ObjectType.CAUSE;
	}
	
	public void testDirectThreats() throws Exception
	{
		project.createThreat();
		assertEquals("wrong direct threat count?", 1, pool.getDirectThreats().length);
	}
	
	CausePool pool;
}
