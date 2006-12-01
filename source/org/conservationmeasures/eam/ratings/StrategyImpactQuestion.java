/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

import org.conservationmeasures.eam.dialogfields.ChoiceItem;
import org.conservationmeasures.eam.dialogfields.ChoiceQuestion;

public class StrategyImpactQuestion extends ChoiceQuestion
{
	public StrategyImpactQuestion(String tag)
	{
		super(tag, "Impact on Key Factors", getImpactChoices());
	}
	
	static ChoiceItem[] getImpactChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "None", COLOR_1_OF_4),
			new ChoiceItem("2", "Low", COLOR_2_OF_4),
			new ChoiceItem("3", "Medium", COLOR_3_OF_4),
			new ChoiceItem("4", "High", COLOR_4_OF_4),
		};
	}

}
