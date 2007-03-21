/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public class CopyTextAction extends AbstractAction
{
	public CopyTextAction(JTextComponent fieldToUse)
	{
		field = fieldToUse;
	}

	public void actionPerformed(ActionEvent e)
	{
		field.copy();
	}
	
	JTextComponent field;
}
