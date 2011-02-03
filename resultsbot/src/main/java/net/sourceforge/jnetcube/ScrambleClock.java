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
 * 
 * Based on Javascript written by Jaap Scherphuis. (jaapsch a t yahoo d o t com)
 */
package net.sourceforge.jnetcube;

public class ScrambleClock extends Scramble {
	
	private int[] posit = {0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0};
	private int[][] moves = {
			{1,1,1,1,1,1,0,0,0,  -1,0,-1,0,0,0,0,0,0},
			{0,1,1,0,1,1,0,1,1,  -1,0,0,0,0,0,-1,0,0},
			{0,0,0,1,1,1,1,1,1,  0,0,0,0,0,0,-1,0,-1},
			{1,1,0,1,1,0,1,1,0,  0,0,-1,0,0,0,0,0,-1},

			{0,0,0,0,0,0,1,0,1,  0,0,0,-1,-1,-1,-1,-1,-1},
			{1,0,0,0,0,0,1,0,0,  0,-1,-1,0,-1,-1,0,-1,-1},
			{1,0,1,0,0,0,0,0,0,  -1,-1,-1,-1,-1,-1,0,0,0},
			{0,0,1,0,0,0,0,0,1,  -1,-1,0,-1,-1,0,-1,-1,0},

			{0,1,1,1,1,1,1,1,1,  -1,0,0,0,0,0,-1,0,-1},
			{1,1,0,1,1,1,1,1,1,  0,0,-1,0,0,0,-1,0,-1},
			{1,1,1,1,1,1,1,1,0,  -1,0,-1,0,0,0,0,0,-1},
			{1,1,1,1,1,1,0,1,1,  -1,0,-1,0,0,0,-1,0,0},

			{1,1,1,1,1,1,1,1,1,  -1,0,-1,0,0,0,-1,0,-1},
			{1,0,1,0,0,0,1,0,1,  -1,-1,-1,-1,-1,-1,-1,-1,-1}
	};
	private int[] seq = new int[14];
	
	public ScrambleClock() {
		super();
	}

	public String getName() {
		return "Clock";
	}
		
	private String adjustPin() {
		int direction = (int) (Math.abs(generator.nextInt()) % 3);
		return (direction==0) ? "U" : "d";
	}
	
	public String generateScramble() {
		String scramble = "";
		
		for (int i=0; i<14; i++){
			seq[i] = (int) (Math.abs(generator.nextInt()) % 12) - 5;
		}
		
		for (int i=0; i<14; i++){
			for (int j=0; j<18; j++){
				posit[j]+=seq[i]*moves[i][j];
			}
		}
		
		
		for (int j=0; j<18; j++){
			posit[j]%=12;
			while (posit[j]<=0) posit[j]+=12;
		}
		
		scramble += "UU  u="+seq[0]+CrLf;
		scramble += "dd  d="+seq[4]+CrLf;
		scramble += CrLf;
		scramble += "dU  u="+seq[1]+CrLf;
		scramble += "dU  d="+seq[5]+CrLf;
		scramble += CrLf;
		scramble += "dd  u="+seq[2]+CrLf;
		scramble += "UU  d="+seq[6]+CrLf;
		scramble += CrLf;
		scramble += "Ud  u="+seq[3]+CrLf;
		scramble += "Ud  d="+seq[7]+CrLf;
		scramble += CrLf;
		scramble += "dU  u="+seq[8]+CrLf;
		scramble += "UU\n";
		scramble += CrLf;
		scramble += "Ud  u="+seq[9]+CrLf;
		scramble += "UU\n";
		scramble += CrLf;
		scramble += "UU  u="+seq[10]+CrLf;
		scramble += "Ud\n";
		scramble += CrLf;
		scramble += "UU  u="+seq[11]+CrLf;
		scramble += "dU\n";
		scramble += CrLf;
		scramble += "UU  u="+seq[12]+CrLf;
		scramble += "UU\n";
		scramble += CrLf;
		scramble += "dd  d="+seq[13]+CrLf;
		scramble += "dd\n";
		scramble += CrLf;
		
		scramble += adjustPin();
		scramble += adjustPin();
		scramble += CrLf;
		scramble += adjustPin();
		scramble += adjustPin();
		
		return scramble;
	}

}
