/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import info.clearthought.layout.TableLayout;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.conservationmeasures.eam.dialogs.EAMDialog;
import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.Utilities;

public class ComingAttractionsPanel implements HyperlinkHandler
{
	void showOkDialog()
	{
		String title = EAM.text("Title|Coming Attractions");
		EAMDialog dlg = new EAMDialog(EAM.mainWindow, title);
		dlg.setModal(true);
		
		String body = loadComingAttractonsHtml();
		if (body == null)
			return;
		HtmlViewer bodyComponent =  new HtmlViewer(body, this);
		bodyComponent.setFont(Font.getFont("Arial"));
		bodyComponent.setSize(new Dimension(750, Short.MAX_VALUE));
		
		JButton close = new JButton(new CloseAction(dlg));

		// Seems like there should be an easier way to enforce a fixed width
		// and allow an HTML text component to become as high as necessary.
		// I couldn't find any sane way using any built-in LayoutManager,
		// nor with any jhlabs LayoutManager. 2006-11-18 kbs
		double[] rowSizes = {bodyComponent.getWidth()};
		double[] columnSizes = {TableLayout.PREFERRED, TableLayout.PREFERRED};
		double gridSizes[][] = { rowSizes, columnSizes };
		JPanel panel = new JPanel(new TableLayout(gridSizes));
		String columnZeroRowZero = "0, 0";
		panel.add(bodyComponent, columnZeroRowZero);
		String columnZeroRowOneCenteredCentered = "0, 1, c, c";
		panel.add(close, columnZeroRowOneCenteredCentered);
		
		panel.setBorder(new EmptyBorder(18,18,18,18));
		panel.setBackground(bodyComponent.getBackground());
		
		dlg.getContentPane().add(new JScrollPane(panel));
		dlg.pack();
		dlg.setLocation(Utilities.center(dlg.getSize(), Utilities.getViewableRectangle()));
		
		dlg.getRootPane().setDefaultButton(close);
		close.requestFocus(true);
		dlg.setVisible(true);
		
	}

	private String loadComingAttractonsHtml()
	{
		try
		{
			return EAM.loadResourceFile(getClass(), COMMING_ATTRACTONS_HTML);
		}
		catch (Exception e)
		{
			EAM.errorDialog("ERROR: Feature file not found: " + COMMING_ATTRACTONS_HTML );
			return null;
		}
	}

	static class CloseAction extends AbstractAction
	{
		public CloseAction(JDialog dialogToClose)
		{
			super("CLOSE");
			dlg = dialogToClose;
		}
		
		public void actionPerformed(ActionEvent arg0)
		{
			dlg.dispose();
		}
		
		JDialog dlg;
	}

	public void buttonPressed(String buttonName)
	{
	}

	public JPopupMenu getRightClickMenu(String url)
	{
		return null;
	}

	public void linkClicked(String linkDescription)
	{	
	}

	public void valueChanged(String widget, String newValue)
	{
	}
	
	private static final String COMMING_ATTRACTONS_HTML = "ComingAttractions.html";

}
