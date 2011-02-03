/*
 * JNetCube
 * Copyright (C) 2007 Chris Hunt
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 *
 */
package net.sourceforge.jnetcube;

public class ScramblePyraminx extends Scramble{
	private String[] U, L, R, B;
	private int previousArray,currentArray,currentFace;
	private String formatedMove;

	public ScramblePyraminx(){
		super();
		fillArrays();
	}
	
	public String getName() {
		return "Pyraminx";
	}

	private void fillArrays(){
		U = new String[2];
		U[0] = "U";
		U[1] = "U'";
		
		L = new String[2];
		L[0] = "L";
		L[1] = "L'";

		R = new String[2];
		R[0] = "R";
		R[1] = "R'";

		B = new String[2];
		B[0] = "B";
		B[1] = "B'";
	}

	private void generateCoreMove(){
		currentArray = (int) (Math.abs(generator.nextInt()) % 4);
		currentFace = (int) (Math.abs(generator.nextInt()) % 2);

		String[] arrayChoice = null;
		switch (currentArray){
			case 0: arrayChoice = U;break;
			case 1: arrayChoice = L;break;
			case 2: arrayChoice = R;break;
			case 3: arrayChoice = B;break;
		}

		formatedMove = arrayChoice[currentFace];		
	}

	public String generateScramble(){
		String scramble = "";
		
		//generate new move
		generateCoreMove();

		//add first move to scramble
		scramble = formatedMove;
		
		//store current move so that we can generate a new one
		previousArray = currentArray;
		//we have one move of the scramble. Now we need to generate the rest.
		for (int i = 0; i<19; i++){
			//generate new move
			generateCoreMove();

			//while this move is the same face, or parallel, generate a new one
			while(previousArray == currentArray){
				generateCoreMove();
			}
		
			//add second move to scramble
			scramble = scramble + " " + formatedMove;

			//store previous and current move so that we can generate a new one
			previousArray = currentArray;		
		}

		int tipChoice = (int) (Math.abs(generator.nextInt()) % 3);

		switch (tipChoice){
			case 0: scramble = scramble + " " + "u";break;
			case 1: scramble = scramble + " " + "u'";break;
			case 2: break;
		}

		tipChoice = (int) (Math.abs(generator.nextInt()) % 3);

		switch (tipChoice){
			case 0: scramble = scramble + " " + "l";break;
			case 1: scramble = scramble + " " + "l'";break;
			case 2: break;
		}

		tipChoice = (int) (Math.abs(generator.nextInt()) % 3);

		switch (tipChoice){
			case 0: scramble = scramble + " " + "r";break;
			case 1: scramble = scramble + " " + "r'";break;
			case 2: break;
		}

		tipChoice = (int) (Math.abs(generator.nextInt()) % 3);

		switch (tipChoice){
			case 0: scramble = scramble + " " + "b";break;
			case 1: scramble = scramble + " " + "b'";break;
			case 2: break;
		}
		return scramble;
	} // end Generate Scramble		
} // end ScramblePyraminx