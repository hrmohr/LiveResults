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

public class Scramble3x3x3 extends Scramble {

	private String[] UandD, FandB, LandR;
	private int oldArray, previousArray, previousFace, currentArray,
			currentFace;
	private String formatedMove; 

	public Scramble3x3x3() {
		super();
		fillArrays();
	}
	
	public String getName() {
		return "3x3x3";
	}

	private void fillArrays() {
		UandD = new String[6];
		UandD[0] = "U";
		UandD[1] = "U'";
		UandD[2] = "U2";
		UandD[3] = "D";
		UandD[4] = "D'";
		UandD[5] = "D2";

		FandB = new String[6];
		FandB[0] = "F";
		FandB[1] = "F'";
		FandB[2] = "F2";
		FandB[3] = "B";
		FandB[4] = "B'";
		FandB[5] = "B2";

		LandR = new String[6];
		LandR[0] = "L";
		LandR[1] = "L'";
		LandR[2] = "L2";
		LandR[3] = "R";
		LandR[4] = "R'";
		LandR[5] = "R2";
	}

	private void generateMove() {
		currentArray = (int) (Math.abs(generator.nextInt()) % 3);
		currentFace = (int) (Math.abs(generator.nextInt()) % 6);

		String[] arrayChoice = null;
		switch (currentArray) {
		case 0:
			arrayChoice = UandD;
			break;
		case 1:
			arrayChoice = FandB;
			break;
		case 2:
			arrayChoice = LandR;
			break;
		}

		formatedMove = arrayChoice[currentFace];
	}

	public String generateScramble() {
		String scramble = "";

		// generate new move
		generateMove();

		// add first move to scramble
		scramble = formatedMove;

		// store current move so that we can generate a new one
		previousArray = currentArray;
		previousFace = currentFace;

		// generate new move
		generateMove();

		// while this move is the same face, generate a new one
		while (isSameFace(currentArray, currentFace, previousArray,
				previousFace)) {
			generateMove();
		}

		// add second move to scramble
		scramble = scramble + " " + formatedMove;

		// store previous and current move so that we can generate a new one
		oldArray = previousArray;
		previousArray = currentArray;
		previousFace = currentFace;

		// we have two moves of the scramble. Now we need to generate the rest.
		for (int i = 0; i < 23; i++) {
			// generate a new move
			generateMove();

			// If the first two moves are parallel
			if (isParallel(previousArray, oldArray)) {
				// loop until third move is not parallel
				while (isParallel(currentArray, previousArray)) {
					generateMove();
				}
			} else {
				// loop until the next move is not the same face as the previous
				while (isSameFace(currentArray, currentFace, previousArray,
						previousFace)) {
					generateMove();
				}
			}

			// add this move to scramble
			scramble = scramble + " " + formatedMove;

			// store previous and current move so that we can generate a new one
			oldArray = previousArray;
			previousArray = currentArray;
			previousFace = currentFace;
		}
		return scramble;
	} // end Generate Scramble

	private boolean isSameFace(int thisArray, int thisFace, int thatArray,
			int thatFace) {
		if ((thisArray == thatArray)) {
			if (((thisFace == 0 || thisFace == 1 || thisFace == 2) && (thatFace == 0
					|| thatFace == 1 || thatFace == 2))
					|| ((thisFace == 3 || thisFace == 4 || thisFace == 5) && (thatFace == 3
							|| thatFace == 4 || thatFace == 5))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	} // end isSameFace

	private boolean isParallel(int thisArray, int thatArray) {
		if (thisArray == thatArray) {
			return true;
		} else {
			return false;
		}
	} // end isParallel
} // end Scramble3x3x3
