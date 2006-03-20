/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.utils.HtmlBuilder;

public class ThreatRatingWizardSetValueText extends HtmlBuilder
{
	public ThreatRatingWizardSetValueText(String[] optionLabels)
	{
		
		text = font("Arial", table(
			tableRow(
				tableCell(
					heading("Rate the Scope of the Selected Threat") +
					indent(paragraph("Using the rating criteria for the scope on the right, " +
							"decide what the " + anchorTag("DefineScope", "scope") + 
							"of the threat is on the target.") +
							paragraph(bold("What is the scope of the threat on the target?")) +
					dropDown("value", optionLabels) +
					newline() +
					indent(table(
						tableRow(
							tableCell(button("Back", "&lt; Previous")) +
							tableCell("&nbsp;") +
							tableCell(button("Next", "Next &gt;")) 
							)
						)) + 
					newline()
					
					)
				) + 
				tableCell(
					paragraph(bold("Scope - ") + "Most commonly defined spatially as the proportion " +
							"of the overall area of a project site or target occurence likely " +
							"to be affected by a threat under current circumstances (i.e. given " +
							"the continuation of the existing situation) over the next ten years.") +
					indent(paragraph(bold("Very High: ") + "The threat is likely to be " +
							"very widespread or pervasive in its scope, and affect the " +
							"conservation target throughout the target's occurences at the site.") +
							paragraph(bold("High: ") + "The threat is likely to be widespread " +
									"in its scope, and affect the conservation target at many " +
									"of its locations at the site.") +
							paragraph(bold("Medium: ") + "The threat is likely to be localized " +
									"in its scope, and affect the conservation target at some " +
									"of its locations at the site.") +
							paragraph(bold("Low: ") + "The threat is likely to be very localized " +
									"in its scope, and affect the conservation target at a " +
									"limited portion of the target's location at the site.")
					)
				)
			)) 

		);
	}
	
	public String getText()
	{
		return text;
	}
	
	String text;
}
