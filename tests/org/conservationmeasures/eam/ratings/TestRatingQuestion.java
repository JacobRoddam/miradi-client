/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

import org.conservationmeasures.eam.dialogfields.ChoiceItem;
import org.conservationmeasures.eam.dialogfields.ChoiceQuestion;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestRatingQuestion extends EAMTestCase
{
	public TestRatingQuestion(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		ChoiceItem[] choices = {
			new ChoiceItem("", "None", Color.BLACK),
			new ChoiceItem("1", "Low", Color.GREEN),
		};
		ChoiceQuestion question = new ChoiceQuestion("Feasibility", "Feasibility and Cost", choices);
		assertEquals("Feasibility", question.getTag());
		assertEquals("Feasibility and Cost", question.getLabel());
		assertEquals(2, question.getChoices().length);
	}
}
