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
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

public class StatusPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private InfoDataTable table;
	private JProgressBar progressBar;
	
	public StatusPanel() {
        super(new BorderLayout());
        
        table = new InfoDataTable(new InfoDataTableModel(), 620);
        table.setPreferredColumnWidths(new double[]{0.05, 0.2, 0.55, 0});
        table.getColumnModel().getColumn(0).setCellRenderer(new CellIconRenderer());
        table.setRowHeight(20);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Point p = e.getPoint();
                    int row = table.rowAtPoint(p);
        		    JOptionPane.showMessageDialog(
        		    		null, 
        		    		(String)table.getModel().getValueAt(row, 3), 
        		    		(String)table.getModel().getValueAt(row, 1), 
        		    		JOptionPane.PLAIN_MESSAGE
        		    );
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.PAGE_START);

        progressBar = new JProgressBar(0, 6);
        progressBar.setValue(0);
        add(progressBar, BorderLayout.PAGE_END);
	}
	
	/**
	 * @param type
	 * @param info
	 */
	public void appendGuiMessage(String type, String info) {
		appendGuiMessage(type, info, info);
	}
	
	/**
	 * @param type
	 * @param info
	 * @param longInfo
	 */
	public void appendGuiMessage(String type, String info, String longInfo) {
		InfoDataTableModel model = (InfoDataTableModel) table.getModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.insertRow(0, new Object[]{type, sdf.format(new Date()), info, longInfo});
	}

    /**
	 * @param type
	 * @param info
	 */
    public void showGuiAlert(String type, String info) {
        appendGuiMessage(type, info);
        if ("warn".equals(type)) {
			JOptionPane.showMessageDialog(null, info, "Warning", JOptionPane.WARNING_MESSAGE);
		} else if ("error".equals(type)) {
			JOptionPane.showMessageDialog(null, info, "Error", JOptionPane.ERROR_MESSAGE);
		} else {
		    JOptionPane.showMessageDialog(null, info, "Info", JOptionPane.PLAIN_MESSAGE);
		}
    }
    
    /**
     * @param type
     * @param info
     * @param longInfo
     */
    public void showGuiAlert(String type, String info, String longInfo) {
        appendGuiMessage(type, info, longInfo);
        if ("warn".equals(type)) {
			JOptionPane.showMessageDialog(null, info, "Warning", JOptionPane.WARNING_MESSAGE);
		} else if ("error".equals(type)) {
			JOptionPane.showMessageDialog(null, info, "Error", JOptionPane.ERROR_MESSAGE);
		} else {
		    JOptionPane.showMessageDialog(null, info, "Info", JOptionPane.PLAIN_MESSAGE);
		}
    }
    
	/**
	 * @param i
	 */
	public void setProgress(int i) {
		progressBar.setValue(i);
	}
}
