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

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class InfoDataTable extends JTable {
	
	private static final long serialVersionUID = 1L;
	private int width = 0;

	/**
	 * @param model
	 */
	public InfoDataTable(InfoDataTableModel model, int width) {
		super(model);
		setWidth(width);
	}

	/**
	 * @param percentages
	 */
	public void setPreferredColumnWidths(double[] percentages) {
		Dimension tableDim = new Dimension(getWidth(), 0); 
		double total = 0; 
		for(int i = 0; i < getColumnModel().getColumnCount(); i++) { 
			total += percentages[i]; 
		}
		for(int i = 0; i < getColumnModel().getColumnCount(); i++) { 
			TableColumn column = getColumnModel().getColumn(i);
			if (percentages[i] == 0) {
				column.setMaxWidth(0);
			} else {
				column.setPreferredWidth((int) (tableDim.width * (percentages[i] / total)));
			}
		} 
	}
	
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
}
