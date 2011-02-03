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
 * 
 * Icons by http://www.famfamfam.com/lab/icons/silk/
 */
package dk.cubing.liveresults.uploader.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class CellIconRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	private final ImageIcon infoIcon;
	private final ImageIcon warnIcon;
	private final ImageIcon errorIcon;
	
	public CellIconRenderer() {
		super();
	    infoIcon = new ImageIcon(getClass().getResource("/icons/tick.png"));
	    warnIcon = new ImageIcon(getClass().getResource("/icons/error.png"));
	    errorIcon = new ImageIcon(getClass().getResource("/icons/exclamation.png"));
	    setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		String type = (String) value;
		if ("info".equals(type)) {
			setIcon(infoIcon);
		} else if ("warn".equals(type)) {
			setIcon(warnIcon);
		} else if ("error".equals(type)) {
			setIcon(errorIcon);
		} else {
			setText(type);
		}
	    return this;
	}

}
