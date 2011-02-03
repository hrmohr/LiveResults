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

public class ScrambleSquare1 extends Scramble {
	
	private int seqlen = 40;
	private int[] seq = new int[seqlen];
	private int[] posit = new int[24];
	
	public ScrambleSquare1() {
		super();
	}

	public String getName() {
		return "Square-1";
	}
	
	private void fillArrays() {
		seq = new int[seqlen];
		posit[0]=0;
		posit[1]=0;
		posit[2]=1;
		posit[3]=2;
		posit[4]=2;
		posit[5]=3;
		posit[6]=4;
		posit[7]=4;
		posit[8]=5;
		posit[9]=6;
		posit[10]=6;
		posit[11]=7;
		posit[12]=8;
		posit[13]=9;
		posit[14]=9;
		posit[15]=10;
		posit[16]=11;
		posit[17]=11;
		posit[18]=12;
		posit[19]=13;
		posit[20]=13;
		posit[21]=14;
		posit[22]=15;
		posit[23]=15;
	}
	
	private void scramble() {
		fillArrays();
		
		int ls=-1;
        int f=0;
        int j;
        for(int i=0; i<seqlen; i++){
            do{
                if(ls==0){
                    j=(int) (Math.abs(generator.nextInt()) % 22)-11;
                    if(j>=0) j++;
                }else if(ls==1){
                    j=(int) (Math.abs(generator.nextInt()) % 12)-11;
                }else if(ls==2){
                    j=0;
                }else{
                    j=(int) (Math.abs(generator.nextInt()) % 23)-11;
                }
                // if past second twist, restrict bottom layer
            }while( (f>1 && j>=-6 && j<0) || domove(j) );
            if(j>0) ls=1;
            else if(j<0) ls=2;
            else { ls=0; f++; }
            seq[i]=j;
        }
	}
	
	private boolean domove(int f){
		int c;
	    int[] t = new int[15];
	    //do move f
	    if( f==0 ){
	        for(int i=0; i<6; i++){
	            c=posit[i+12];
	            posit[i+12]=posit[i+6];
	            posit[i+6]=c;
	        }
	    }else if(f>0){
	        f=12-f;
	        if( posit[f]==posit[f-1] ) return true;
	        if( f<6 && posit[f+6]==posit[f+5] ) return true;
	        if( f>6 && posit[f-6]==posit[f-7] ) return true;
	        if( f==6 && posit[0]==posit[11] ) return true;
	        t= new int[15];
	        for(int i=0;i<12;i++) t[i]=posit[i];
	        c=f;
	        for(int i=0;i<12;i++){
	            posit[i]=t[c];
	            if(c==11)c=0; else c++;
	        }
	    }else if(f<0){
	        f=-f;
	        if( posit[f+12]==posit[f+11] ) return true;
	        if( f<6 && posit[f+18]==posit[f+17] ) return true;
	        if( f>6 && posit[f+6]==posit[f+5] ) return true;
	        if( f==6 && posit[12]==posit[23] ) return true;
	        t= new int[15];
	        for(int i=0;i<12;i++) t[i]=posit[i+12];
	        c=f;
	        for(int i=0;i<12;i++){
	            posit[i+12]=t[c];
	            if(c==11)c=0; else c++;
	        }
	    }
	    return false;
	}

			
	public String generateScramble() {
		
		scramble();
		
		String s = "";
		int k = 0;
		int l = -1;
		
		for(int i=0; i<seq.length; i++){
	        k=seq[i];
	        if(k==0){
	            if(l==-1) s+="(0,0)  ";
	            if(l==1) s+="0)  ";
	            if(l==2) s+=")  ";
	            l=0;
	        }else if(k>0){
	            s+= "(" + (k>6?k-12:k)+",";
	            l=1;
	        }else if(k<0){
	            if(l<=0) s+="(0,";
	            s+=(k<=-6?k+12:k);
	            l=2;
	        }
	    }
	    if(l==1) s+="0";
	    if(l!=0) s+=")";
	    //if(l==0) s+="(0,0)";
	    
		return s;
	}

}
