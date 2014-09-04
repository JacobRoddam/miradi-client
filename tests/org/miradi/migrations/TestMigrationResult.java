/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations;

import org.miradi.main.MiradiTestCase;

public class TestMigrationResult extends MiradiTestCase
{
	public TestMigrationResult(String name)
	{
		super(name);
	}
	
	public void testDataLoss()
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();
		assertFalse("Should not be data loss?", migrationResult.didLoseData());
		
		MigrationResult dataLossResult = migrationResult.createDataLoss("");
		assertTrue("Should be data loss?", dataLossResult.didLoseData());
		
		migrationResult.merge(dataLossResult);
		assertTrue("Should be data loss?", migrationResult.didLoseData());
		
		assertEquals("incorrect data loss messages count?", 1, migrationResult.dataLossCount());
		
		migrationResult.addDataLoss("Some randome message");
		assertEquals("incorrect data loss messages count?", 2, migrationResult.dataLossCount());
	}
}
