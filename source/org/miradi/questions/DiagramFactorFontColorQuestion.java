/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.questions;

import java.awt.Color;


public class DiagramFactorFontColorQuestion extends StaticChoiceQuestion
{
	public DiagramFactorFontColorQuestion()
	{
		super(getColorChoices());
	}
	
	static ChoiceItem[] getColorChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Black (Default)", Color.BLACK),
			new ChoiceItem(DARK_GRAY_HEX, "Dark Gray", DARK_GRAY_FROM_HEX),
			new ChoiceItem(LIGHT_GRAY_HEX, "Light Gray", LIGHT_GRAY_FROM_HEX),
			new ChoiceItem(BROWN_HEX, "Brown", BROWN_FROM_HEX),
			new ChoiceItem(TAN_HEX, "Tan", TAN_FROM_HEX),
			new ChoiceItem(WHITE_HEX, "White", WHITE_FROM_HEX),
			new ChoiceItem(RED_HEX, "Red", RED_FROM_HEX),
			new ChoiceItem(PINK_HEX, "Pink", PINK_FROM_HEX),
			new ChoiceItem(ORANGE_HEX, "Orange", ORANGE_FROM_HEX),
			new ChoiceItem(YELLOW_HEX, "Yellow", YELLOW_FROM_HEX),
			new ChoiceItem(DARK_GREEN_HEX, "Dark Green", DARK_GREEN_FROM_HEX),
			new ChoiceItem(LIGHT_GREEN_HEX, "Light Green", LIGHT_GREEN_FROM_HEX),
			new ChoiceItem(DARK_BLUE_HEX, "Dark Blue", DARK_BLUE_FROM_HEX),
			new ChoiceItem(LIGHT_BLUE_HEX, "Light Blue", LIGHT_BLUE_FROM_HEX),
		};
	}
	
	public static final String DARK_GRAY_HEX = "#4E4848";
	public static final String LIGHT_GRAY_HEX = "#6D7B8D";
	public static final String BROWN_HEX = "#C85A17";
	public static final String TAN_HEX = "#EDE275";
	public static final String WHITE_HEX = "#FFFFFF";
	public static final String RED_HEX = "#FF0000";
	public static final String PINK_HEX = "#FF00FF";
	public static final String ORANGE_HEX = "#FF8040";
	public static final String YELLOW_HEX = "#FFFF00";
	public static final String DARK_GREEN_HEX = "#254117";
	public static final String LIGHT_GREEN_HEX = "#5FFB17";
	public static final String DARK_BLUE_HEX = "#0000CC";
	public static final String LIGHT_BLUE_HEX = "#00CCFF";
	
	public static final Color DARK_GRAY_FROM_HEX = Color.decode(DARK_GRAY_HEX);
	public static final Color LIGHT_GRAY_FROM_HEX = Color.decode(LIGHT_GRAY_HEX);
	public static final Color BROWN_FROM_HEX = Color.decode(BROWN_HEX);
	public static final Color TAN_FROM_HEX = Color.decode(TAN_HEX);
	public static final Color WHITE_FROM_HEX = Color.decode(WHITE_HEX);
	public static final Color RED_FROM_HEX = Color.decode(RED_HEX);
	public static final Color PINK_FROM_HEX = Color.decode(PINK_HEX);
	public static final Color ORANGE_FROM_HEX = Color.decode(ORANGE_HEX);
	public static final Color YELLOW_FROM_HEX = Color.decode(YELLOW_HEX);
	public static final Color DARK_GREEN_FROM_HEX = Color.decode(DARK_GREEN_HEX);
	public static final Color LIGHT_GREEN_FROM_HEX = Color.decode(LIGHT_GREEN_HEX);
	public static final Color DARK_BLUE_FROM_HEX = Color.decode(DARK_BLUE_HEX);
	public static final Color LIGHT_BLUE_FROM_HEX = Color.decode(LIGHT_BLUE_HEX);	
}
