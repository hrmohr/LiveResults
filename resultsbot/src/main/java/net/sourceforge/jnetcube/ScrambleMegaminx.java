/*
 * Copyright (C) 2008 Mads Mohr Christensen
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * hr.mohr@gmail.com
 * Mads Mohr Christensen 2008
 */
package net.sourceforge.jnetcube;

public class ScrambleMegaminx extends Scramble{

	public ScrambleMegaminx() {
		super();
	}
	
	public String getName() {
		return "Megaminx";
	}
	
	private String addSuffix(String face) {
		int direction = (int) (Math.abs(generator.nextInt()) % 3);
		if (direction==0) {
			return face + "-- ";
		} else {
			return face + "++ ";
		}
	}
	
	private String adjustU(String scramble) {
		if (scramble.trim().endsWith("++")) {
			return "U'";
		} else {
			return "U";
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jnetcube.Scramble#generateScramble()
	 */
	public String generateScramble(){
		String scramble = "";
		for (int i=0; i<7; i++) {
			for (int j=0; j<14; j++) {
				if (j % 2 == 0) {
					scramble += addSuffix("R");
				} else {
					scramble += addSuffix("D");
				}
			}
			scramble += adjustU(scramble) + CrLf;
		}
		return scramble;
	} // end Generate Scramble

} // end ScrambleMegaminx