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

public class Scramble4x4x4 extends Scramble {
	private String[][] UandD, FandB, LandR;
	private int deadArray, oldArray, oldGroup, previousArray, previousGroup, previousFace, currentArray, currentGroup, currentFace;
	private String formatedMove;
	
	public Scramble4x4x4(){
		super();
		fillArrays();
	}
	
	public String getName() {
		return "4x4x4";
	}

	private void fillArrays(){
		UandD = new String[3][4];
		UandD[0][0] = "U";
		UandD[0][1] = "Uw";
		UandD[0][2] = "Dw'";
		UandD[0][3] = "D'";
		UandD[1][0] = "U'";
		UandD[1][1] = "Uw'";
		UandD[1][2] = "Dw";
		UandD[1][3] = "D";
		UandD[2][0] = "U2";
		UandD[2][1] = "Uw2";
		UandD[2][2] = "Dw2";
		UandD[2][3] = "D2";

		FandB = new String[3][4];
		FandB[0][0] = "F";
		FandB[0][1] = "Fw";
		FandB[0][2] = "Bw";
		FandB[0][3] = "B'";
		FandB[1][0] = "F'";
		FandB[1][1] = "Fw'";
		FandB[1][2] = "Bw";
		FandB[1][3] = "B";
		FandB[2][0] = "F2";
		FandB[2][1] = "Fw2";
		FandB[2][2] = "Bw2";
		FandB[2][3] = "B2";

		LandR = new String[3][4];
		LandR[0][0] = "L";
		LandR[0][1] = "Lw";
		LandR[0][2] = "Rw'";
		LandR[0][3] = "R'";
		LandR[1][0] = "L'";
		LandR[1][1] = "Lw'";
		LandR[1][2] = "Rw";
		LandR[1][3] = "R";
		LandR[2][0] = "L2";
		LandR[2][1] = "Lw2";
		LandR[2][2] = "Rw2";
		LandR[2][3] = "R2";
	} // end fillArrays

	private void generateMove(){
		currentArray = (int) (Math.abs(generator.nextInt()) % 3);
		currentGroup = (int) (Math.abs(generator.nextInt()) % 3);
		currentFace = (int) (Math.abs(generator.nextInt()) % 4);

		String[][] arrayChoice = null;
		switch (currentArray){
			case 0: arrayChoice = UandD;break;
			case 1: arrayChoice = FandB;break;
			case 2: arrayChoice = LandR;break;
		}

		formatedMove = arrayChoice[currentGroup][currentFace];		
	}	

	public String generateScramble(){
		String scramble = "";
		
		//generate new move
		generateMove();

		//add first move to scramble
		scramble = formatedMove;
		
		//store current move so that we can generate a new one
		previousArray = currentArray;
		previousGroup = currentGroup;
		previousFace = currentFace;

		//generate new move
		generateMove();

		//while this move is the same face, generate a new one
		while(isSameFace(currentArray,currentFace,previousArray,previousFace)){
			generateMove();
		}
		
		//add second move to scramble
		scramble = scramble + " " + formatedMove;

		//store previous and current move so that we can generate a new one
		oldArray = previousArray;
		oldGroup = previousGroup;
		previousArray = currentArray;
		previousGroup = currentGroup;
		previousFace = currentFace;
		
		//generate a new move
		generateMove();

		//If the first two moves are parallel
		if (isParallel(previousArray, oldArray)){
			//If the first two moves are in the same direction
			if (movesSameDirection(previousArray, previousGroup, oldArray, oldGroup)){
				//loop until the next move is an intersecting face
				while(isParallel(currentArray,previousArray)){
					generateMove();
				}
			} else {
				//loop until the next move is not in the same group as either of the first two moves AND is not the same face
				while((movesSameDirection(currentArray,currentGroup,previousArray,previousGroup) || movesSameDirection(currentArray,currentGroup,oldArray,oldGroup)) || isSameFace(currentArray,currentFace,previousArray,previousFace)){
					generateMove();
				}
			}
		} else {
			//loop until the next move is not the same face as the previous
			while(isSameFace(currentArray,currentFace,previousArray,previousFace)){
				generateMove();
			}
		}

		//add third move to scramble
		scramble = scramble + " " + formatedMove;
		
		//store previous and current move so that we can generate a new one
		deadArray = oldArray;
		oldArray = previousArray;
		oldGroup = previousGroup;
		previousArray = currentArray;
		previousGroup = currentGroup;
		previousFace = currentFace;

		//we have three moves of the scramble. Now we need to generate the rest.
		for (int i = 0; i<37; i++){
			generateMove();
			
			//if three moves before this one are all parellel, then make this move on an intersecting face
			if (isParallel(previousArray,oldArray) && isParallel(previousArray, deadArray)){
				while (isParallel(currentArray, previousArray)){
					generateMove();
				}
			} else {
				//If the first two moves are parallel
				if (isParallel(previousArray, oldArray)){
					//If the first two moves are in the same direction
					if (movesSameDirection(previousArray, previousGroup, oldArray, oldGroup)){
						//loop until the next move is an intersecting face
						while(isParallel(currentArray,previousArray)){
							generateMove();
						}
					} else {
						//loop until the next move is not in the same group as either of the first two moves AND is not the same face
						while(movesSameDirection(currentArray,currentGroup,previousArray,previousGroup) || movesSameDirection(currentArray,currentGroup,oldArray,oldGroup) || isSameFace(currentArray,currentFace,previousArray,previousFace)){
							generateMove();
						}
					}
				} else {
					//loop until the next move is not the same face as the previous
					while(isSameFace(currentArray,currentFace,previousArray,previousFace)){
						generateMove();
					}
				}
			}

			//add this move to scramble
			scramble = scramble + " " + formatedMove;
		
			//store previous and current move so that we can generate a new one
			deadArray = oldArray;
			oldArray = previousArray;
			oldGroup = previousGroup;
			previousArray = currentArray;
			previousGroup = currentGroup;
			previousFace = currentFace;
		}
		return scramble;
	} // end Generate Scramble

	private boolean isSameFace(int thisArray, int thisFace, int thatArray, int thatFace){
		if ((thisArray == thatArray) && (thisFace == thatFace)){
			return true;
		} else {
			return false;
		}
	} // end isSameFace

	private boolean isParallel(int thisArray, int thatArray){
		if (thisArray == thatArray){
			return true;
		} else {
			return false;
		}
	} // end isParallel

	private boolean movesSameDirection(int thisArray, int thisGroup, int thatArray, int thatGroup){
		if (((thisArray == thatArray) && (thisGroup == thatGroup)) || ((thisArray == thatArray) && (thisGroup == 2)) || ((thisArray == thatArray) && (thatGroup == 2))){
			return true;
		} else {
			return false;
		}
	} // end movesSameDirection
} // end Scramble4x4x4