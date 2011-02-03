/**
 * Copyright (C) 2009 Mads Mohr Christensen, <hr.mohr@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package dk.cubing.liveresults.uploader.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import dk.cubing.liveresults.uploader.engine.ResultsEngine;

public class MenuBar extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private ResultsEngine engine;
	
	private JMenu fileMenu;
	private JMenu helpMenu;
	
	private JMenuItem preferencesItem;
	private JMenuItem quitItem;
	private JMenuItem aboutItem;

	public MenuBar(ResultsEngine engine) {
		super();
		this.engine = engine;

		fileMenu = new JMenu("File");
		preferencesItem = new JMenuItem("Preferences...");
		preferencesItem.addActionListener(this);
		fileMenu.add(preferencesItem);
		
		quitItem = new JMenuItem("Quit Live Results");
		quitItem.addActionListener(this);
		fileMenu.add(quitItem);
		add(fileMenu);
		
		helpMenu = new JMenu("Help");
		aboutItem = new JMenuItem("About Live Results");
		aboutItem.addActionListener(this);
		helpMenu.add(aboutItem);
		add(helpMenu);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == quitItem) {
			System.exit(0);
		} else if (source == aboutItem) {
		    JOptionPane.showMessageDialog(null, "\u00A9 2009-2010 Mads Mohr Christensen\nhttp://live.speedcubing.dk/", "About Live Results", JOptionPane.INFORMATION_MESSAGE);
		} else if (source == preferencesItem) {
			engine.createAndShowPreferencesDialog();
		}
	}
	
}
