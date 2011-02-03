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
package dk.cubing.liveresults.uploader.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dk.cubing.liveresults.uploader.configuration.Configuration;
import dk.cubing.liveresults.uploader.engine.ResultsEngine;

public class PreferencesPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JDialog dialog;
	private ResultsEngine engine;
	
	private JButton selectButton;
	private JButton applyButton;
	private JFileChooser fc;
	
	private JLabel competitionIdLabel;
	private JLabel passwordLabel;
	private JLabel resultsFilenameLabel;
	
	private JTextField competitionIdField;
	private JPasswordField passwordField;
	private JTextField resultsFilenameField;
	
	public PreferencesPanel(JDialog dialog, ResultsEngine engine) {
		super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        this.dialog = dialog;
        this.engine = engine;
		Configuration config = engine.getConfig();
        
        fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new ExcelFileFilter());
        File f = new File(config.getResultsFilename());
        if (f.exists()) {
        	fc.setCurrentDirectory(f);
        }
        
        competitionIdLabel = new JLabel("Competition ID");
    	passwordLabel = new JLabel("Password");
    	resultsFilenameLabel = new JLabel("Path to spreadsheet");
        
        competitionIdField = new JTextField();
        competitionIdField.setText(config.getCompetitionId());
        
        passwordField = new JPasswordField();
        passwordField.setText(config.getPassword());
        
        resultsFilenameField = new JTextField();
        resultsFilenameField.setText(config.getResultsFilename());
        resultsFilenameField.setEditable(false);
        
        selectButton = new JButton("Select spreadsheet");
        selectButton.addActionListener(this);
        
        applyButton = new JButton("Save");
        applyButton.addActionListener(this);

        competitionIdLabel.setLabelFor(competitionIdField);
        passwordLabel.setLabelFor(passwordField);
        resultsFilenameLabel.setLabelFor(resultsFilenameField);
        
        JPanel labelPane = new JPanel(new GridLayout(0,1));
        labelPane.add(competitionIdLabel);
        labelPane.add(passwordLabel);
        labelPane.add(resultsFilenameLabel);
        labelPane.add(new JLabel());
        
        JPanel fieldPane = new JPanel(new GridLayout(0,1));
        fieldPane.add(competitionIdField);
        fieldPane.add(passwordField);
        fieldPane.add(resultsFilenameField);
        fieldPane.add(selectButton);
        
        JPanel controlsPane = new JPanel(new GridLayout(0,1));
        controlsPane.add(applyButton);
        
        add(labelPane, BorderLayout.CENTER);
        add(fieldPane, BorderLayout.LINE_END);
        add(controlsPane, BorderLayout.PAGE_END);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == selectButton) {
			int returnVal = fc.showOpenDialog(this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            resultsFilenameField.setText(file.getAbsolutePath());
	        }
		} else if (source == applyButton) {
			Configuration config = engine.getConfig();
			config.setCompetitionId(competitionIdField.getText());
			config.setPassword(new String(passwordField.getPassword()));
			config.setResultsFilename(resultsFilenameField.getText());
			dialog.setVisible(false);
			config.save();
		}
	}
	
}
