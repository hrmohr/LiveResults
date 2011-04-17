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
package dk.cubing.liveresults.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "wca_registered_events")
public class RegisteredEvents implements Serializable {

	@Id
	@GeneratedValue
	private int id;
	
	private boolean signedUpFor2x2 = false;
	private boolean signedUpFor3x3 = false;
	private boolean signedUpFor4x4 = false;
	private boolean signedUpFor5x5 = false;
	private boolean signedUpFor6x6 = false;
	private boolean signedUpFor7x7 = false;
	private boolean signedUpForFm = false;
	private boolean signedUpForOh = false;
	private boolean signedUpForBf = false;
	private boolean signedUpForBf4 = false;
	private boolean signedUpForBf5 = false;
	private boolean signedUpForFeet = false;
	private boolean signedUpForClk = false;
	private boolean signedUpForMgc = false;
	private boolean signedUpForMmgc = false;
	private boolean signedUpForMinx = false;
	private boolean signedUpForSq1 = false;
	private boolean signedUpForPyr = false;
	private boolean signedUpForMbf = false;
	private boolean signedUpFor333ni = false;
	private boolean signedUpFor333sbf = false;
	private boolean signedUpFor333r3 = false;
	private boolean signedUpFor333ts = false;
	private boolean signedUpFor333bts = false;
	private boolean signedUpFor222bf = false;
	private boolean signedUpFor333si = false;
	private boolean signedUpForRainb = false;
	private boolean signedUpForSnake = false;
	private boolean signedUpForSkewb = false;
	private boolean signedUpForMirbl = false;
	private boolean signedUpFor222oh = false;
	private boolean signedUpForMagico = false;
	private boolean signedUpFor360 = false;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the signedUpFor2x2
	 */
	public boolean isSignedUpFor2x2() {
		return signedUpFor2x2;
	}
	/**
	 * @param signedUpFor2x2 the signedUpFor2x2 to set
	 */
	public void setSignedUpFor2x2(boolean signedUpFor2x2) {
		this.signedUpFor2x2 = signedUpFor2x2;
	}
	/**
	 * @return the signedUpFor3x3
	 */
	public boolean isSignedUpFor3x3() {
		return signedUpFor3x3;
	}
	/**
	 * @param signedUpFor3x3 the signedUpFor3x3 to set
	 */
	public void setSignedUpFor3x3(boolean signedUpFor3x3) {
		this.signedUpFor3x3 = signedUpFor3x3;
	}
	/**
	 * @return the signedUpFor4x4
	 */
	public boolean isSignedUpFor4x4() {
		return signedUpFor4x4;
	}
	/**
	 * @param signedUpFor4x4 the signedUpFor4x4 to set
	 */
	public void setSignedUpFor4x4(boolean signedUpFor4x4) {
		this.signedUpFor4x4 = signedUpFor4x4;
	}
	/**
	 * @return the signedUpFor5x5
	 */
	public boolean isSignedUpFor5x5() {
		return signedUpFor5x5;
	}
	/**
	 * @param signedUpFor5x5 the signedUpFor5x5 to set
	 */
	public void setSignedUpFor5x5(boolean signedUpFor5x5) {
		this.signedUpFor5x5 = signedUpFor5x5;
	}
	/**
	 * @return the signedUpFor6x6
	 */
	public boolean isSignedUpFor6x6() {
		return signedUpFor6x6;
	}
	/**
	 * @param signedUpFor6x6 the signedUpFor6x6 to set
	 */
	public void setSignedUpFor6x6(boolean signedUpFor6x6) {
		this.signedUpFor6x6 = signedUpFor6x6;
	}
	/**
	 * @return the signedUpFor7x7
	 */
	public boolean isSignedUpFor7x7() {
		return signedUpFor7x7;
	}
	/**
	 * @param signedUpFor7x7 the signedUpFor7x7 to set
	 */
	public void setSignedUpFor7x7(boolean signedUpFor7x7) {
		this.signedUpFor7x7 = signedUpFor7x7;
	}
	/**
	 * @return the signedUpForFm
	 */
	public boolean isSignedUpForFm() {
		return signedUpForFm;
	}
	/**
	 * @param signedUpForFm the signedUpForFm to set
	 */
	public void setSignedUpForFm(boolean signedUpForFm) {
		this.signedUpForFm = signedUpForFm;
	}
	/**
	 * @return the signedUpForOh
	 */
	public boolean isSignedUpForOh() {
		return signedUpForOh;
	}
	/**
	 * @param signedUpForOh the signedUpForOh to set
	 */
	public void setSignedUpForOh(boolean signedUpForOh) {
		this.signedUpForOh = signedUpForOh;
	}
	/**
	 * @return the signedUpForBf
	 */
	public boolean isSignedUpForBf() {
		return signedUpForBf;
	}
	/**
	 * @param signedUpForBf the signedUpForBf to set
	 */
	public void setSignedUpForBf(boolean signedUpForBf) {
		this.signedUpForBf = signedUpForBf;
	}
	/**
	 * @return the signedUpForBf4
	 */
	public boolean isSignedUpForBf4() {
		return signedUpForBf4;
	}
	/**
	 * @param signedUpForBf4 the signedUpForBf4 to set
	 */
	public void setSignedUpForBf4(boolean signedUpForBf4) {
		this.signedUpForBf4 = signedUpForBf4;
	}
	/**
	 * @return the signedUpForBf5
	 */
	public boolean isSignedUpForBf5() {
		return signedUpForBf5;
	}
	/**
	 * @param signedUpForBf5 the signedUpForBf5 to set
	 */
	public void setSignedUpForBf5(boolean signedUpForBf5) {
		this.signedUpForBf5 = signedUpForBf5;
	}
	/**
	 * @return the signedUpForFeet
	 */
	public boolean isSignedUpForFeet() {
		return signedUpForFeet;
	}
	/**
	 * @param signedUpForFeet the signedUpForFeet to set
	 */
	public void setSignedUpForFeet(boolean signedUpForFeet) {
		this.signedUpForFeet = signedUpForFeet;
	}
	/**
	 * @return the signedUpForClk
	 */
	public boolean isSignedUpForClk() {
		return signedUpForClk;
	}
	/**
	 * @param signedUpForClk the signedUpForClk to set
	 */
	public void setSignedUpForClk(boolean signedUpForClk) {
		this.signedUpForClk = signedUpForClk;
	}
	/**
	 * @return the signedUpForMgc
	 */
	public boolean isSignedUpForMgc() {
		return signedUpForMgc;
	}
	/**
	 * @param signedUpForMgc the signedUpForMgc to set
	 */
	public void setSignedUpForMgc(boolean signedUpForMgc) {
		this.signedUpForMgc = signedUpForMgc;
	}
	/**
	 * @return the signedUpForMmgc
	 */
	public boolean isSignedUpForMmgc() {
		return signedUpForMmgc;
	}
	/**
	 * @param signedUpForMmgc the signedUpForMmgc to set
	 */
	public void setSignedUpForMmgc(boolean signedUpForMmgc) {
		this.signedUpForMmgc = signedUpForMmgc;
	}
	/**
	 * @return the signedUpForMinx
	 */
	public boolean isSignedUpForMinx() {
		return signedUpForMinx;
	}
	/**
	 * @param signedUpForMinx the signedUpForMinx to set
	 */
	public void setSignedUpForMinx(boolean signedUpForMinx) {
		this.signedUpForMinx = signedUpForMinx;
	}
	/**
	 * @return the signedUpForSq1
	 */
	public boolean isSignedUpForSq1() {
		return signedUpForSq1;
	}
	/**
	 * @param signedUpForSq1 the signedUpForSq1 to set
	 */
	public void setSignedUpForSq1(boolean signedUpForSq1) {
		this.signedUpForSq1 = signedUpForSq1;
	}
	/**
	 * @return the signedUpForPyr
	 */
	public boolean isSignedUpForPyr() {
		return signedUpForPyr;
	}
	/**
	 * @param signedUpForPyr the signedUpForPyr to set
	 */
	public void setSignedUpForPyr(boolean signedUpForPyr) {
		this.signedUpForPyr = signedUpForPyr;
	}
	/**
	 * @return the signedUpForMbf
	 */
	public boolean isSignedUpForMbf() {
		return signedUpForMbf;
	}
	/**
	 * @param signedUpForMbf the signedUpForMbf to set
	 */
	public void setSignedUpForMbf(boolean signedUpForMbf) {
		this.signedUpForMbf = signedUpForMbf;
	}
	
	/**
	 * @return the signedUpFor333ni
	 */
	public boolean isSignedUpFor333ni() {
		return signedUpFor333ni;
	}
	/**
	 * @param signedUpFor333ni the signedUpFor333ni to set
	 */
	public void setSignedUpFor333ni(boolean signedUpFor333ni) {
		this.signedUpFor333ni = signedUpFor333ni;
	}
	/**
	 * @return the signedUpFor333sbf
	 */
	public boolean isSignedUpFor333sbf() {
		return signedUpFor333sbf;
	}
	/**
	 * @param signedUpFor333sbf the signedUpFor333sbf to set
	 */
	public void setSignedUpFor333sbf(boolean signedUpFor333sbf) {
		this.signedUpFor333sbf = signedUpFor333sbf;
	}
	/**
	 * @return the signedUpFor333r3
	 */
	public boolean isSignedUpFor333r3() {
		return signedUpFor333r3;
	}
	/**
	 * @param signedUpFor333r3 the signedUpFor333r3 to set
	 */
	public void setSignedUpFor333r3(boolean signedUpFor333r3) {
		this.signedUpFor333r3 = signedUpFor333r3;
	}
	/**
	 * @return the signedUpFor333ts
	 */
	public boolean isSignedUpFor333ts() {
		return signedUpFor333ts;
	}
	/**
	 * @param signedUpFor333ts the signedUpFor333ts to set
	 */
	public void setSignedUpFor333ts(boolean signedUpFor333ts) {
		this.signedUpFor333ts = signedUpFor333ts;
	}
	/**
	 * @return the signedUpFor333bts
	 */
	public boolean isSignedUpFor333bts() {
		return signedUpFor333bts;
	}
	/**
	 * @param signedUpFor333bts the signedUpFor333bts to set
	 */
	public void setSignedUpFor333bts(boolean signedUpFor333bts) {
		this.signedUpFor333bts = signedUpFor333bts;
	}
	/**
	 * @return the signedUpFor222bf
	 */
	public boolean isSignedUpFor222bf() {
		return signedUpFor222bf;
	}
	/**
	 * @param signedUpFor222bf the signedUpFor222bf to set
	 */
	public void setSignedUpFor222bf(boolean signedUpFor222bf) {
		this.signedUpFor222bf = signedUpFor222bf;
	}
	/**
	 * @return the signedUpFor333si
	 */
	public boolean isSignedUpFor333si() {
		return signedUpFor333si;
	}
	/**
	 * @param signedUpFor333si the signedUpFor333si to set
	 */
	public void setSignedUpFor333si(boolean signedUpFor333si) {
		this.signedUpFor333si = signedUpFor333si;
	}
	/**
	 * @return the signedUpForRainb
	 */
	public boolean isSignedUpForRainb() {
		return signedUpForRainb;
	}
	/**
	 * @param signedUpForRainb the signedUpForRainb to set
	 */
	public void setSignedUpForRainb(boolean signedUpForRainb) {
		this.signedUpForRainb = signedUpForRainb;
	}
	/**
	 * @return the signedUpForSnake
	 */
	public boolean isSignedUpForSnake() {
		return signedUpForSnake;
	}
	/**
	 * @param signedUpForSnake the signedUpForSnake to set
	 */
	public void setSignedUpForSnake(boolean signedUpForSnake) {
		this.signedUpForSnake = signedUpForSnake;
	}
	/**
	 * @return the signedUpForSkewb
	 */
	public boolean isSignedUpForSkewb() {
		return signedUpForSkewb;
	}
	/**
	 * @param signedUpForSkewb the signedUpForSkewb to set
	 */
	public void setSignedUpForSkewb(boolean signedUpForSkewb) {
		this.signedUpForSkewb = signedUpForSkewb;
	}
	/**
	 * @return the signedUpForMirbl
	 */
	public boolean isSignedUpForMirbl() {
		return signedUpForMirbl;
	}
	/**
	 * @param signedUpForMirbl the signedUpForMirbl to set
	 */
	public void setSignedUpForMirbl(boolean signedUpForMirbl) {
		this.signedUpForMirbl = signedUpForMirbl;
	}
	/**
	 * @return the signedUpFor222oh
	 */
	public boolean isSignedUpFor222oh() {
		return signedUpFor222oh;
	}
	/**
	 * @param signedUpFor222oh the signedUpFor222oh to set
	 */
	public void setSignedUpFor222oh(boolean signedUpFor222oh) {
		this.signedUpFor222oh = signedUpFor222oh;
	}
	/**
	 * @return the signedUpForMagico
	 */
	public boolean isSignedUpForMagico() {
		return signedUpForMagico;
	}
	/**
	 * @param signedUpForMagico the signedUpForMagico to set
	 */
	public void setSignedUpForMagico(boolean signedUpForMagico) {
		this.signedUpForMagico = signedUpForMagico;
	}
	/**
	 * @return the signedUpFor360
	 */
	public boolean isSignedUpFor360() {
		return signedUpFor360;
	}
	/**
	 * @param signedUpFor360 the signedUpFor360 to set
	 */
	public void setSignedUpFor360(boolean signedUpFor360) {
		this.signedUpFor360 = signedUpFor360;
	}
	
	/**
	 * @return
	 */
	@Transient
	public boolean hasSignedUp() {
		return 
			signedUpFor2x2 ||
			signedUpFor3x3 ||
			signedUpFor4x4 ||
			signedUpFor5x5 ||
			signedUpFor6x6 ||
			signedUpFor7x7 ||
			signedUpForFm ||
			signedUpForOh ||
			signedUpForBf ||
			signedUpForBf4 ||
			signedUpForBf5 ||
			signedUpForFeet ||
			signedUpForClk ||
			signedUpForMgc ||
			signedUpForMmgc ||
			signedUpForMinx ||
			signedUpForSq1 ||
			signedUpForPyr ||
			signedUpForMbf ||
			signedUpFor333ni ||
			signedUpFor333sbf ||
			signedUpFor333r3 ||
			signedUpFor333ts ||
			signedUpFor333bts ||
			signedUpFor222bf ||
			signedUpFor333si ||
			signedUpForRainb ||
			signedUpForSnake ||
			signedUpForSkewb ||
			signedUpForMirbl ||
			signedUpFor222oh ||
			signedUpForMagico ||
			signedUpFor360;
	}
	
	/**
	 * @return
	 */
	@Transient
	public int getNumberOfEvents() {
		int result = 0;
		if (signedUpFor2x2) result++;
		if (signedUpFor3x3) result++;
		if (signedUpFor4x4) result++;
		if (signedUpFor5x5) result++;
		if (signedUpFor6x6) result++;
		if (signedUpFor7x7) result++;
		if (signedUpForFm) result++;
		if (signedUpForOh) result++;
		if (signedUpForBf) result++;
		if (signedUpForBf4) result++;
		if (signedUpForBf5) result++;
		if (signedUpForFeet) result++;
		if (signedUpForClk) result++;
		if (signedUpForMgc) result++;
		if (signedUpForMmgc) result++;
		if (signedUpForMinx) result++;
		if (signedUpForSq1) result++;
		if (signedUpForPyr) result++;
		if (signedUpForMbf) result++;
		if (signedUpFor333ni) result++;
		if (signedUpFor333sbf) result++;
		if (signedUpFor333r3) result++;
		if (signedUpFor333ts) result++;
		if (signedUpFor333bts) result++;
		if (signedUpFor222bf) result++;
		if (signedUpFor333si) result++;
		if (signedUpForRainb) result++;
		if (signedUpForSnake) result++;
		if (signedUpForSkewb) result++;
		if (signedUpForMirbl) result++;
		if (signedUpFor222oh) result++;
		if (signedUpForMagico) result++;
		if (signedUpFor360) result++;
		return result;
	}
	
	/**
	 * @return
	 */
	@Transient
	public List<Boolean> getSignupList() {
		List<Boolean> row = new ArrayList<Boolean>();
		row.add(isSignedUpFor2x2());
		row.add(isSignedUpFor3x3());
		row.add(isSignedUpFor4x4());
		row.add(isSignedUpFor5x5());
		row.add(isSignedUpFor6x6());
		row.add(isSignedUpFor7x7());
		row.add(isSignedUpForFm());
		row.add(isSignedUpForOh());
		row.add(isSignedUpForBf());
		row.add(isSignedUpForBf4());
		row.add(isSignedUpForBf5());
		row.add(isSignedUpForFeet());
		row.add(isSignedUpForClk());
		row.add(isSignedUpForMgc());
		row.add(isSignedUpForMmgc());
		row.add(isSignedUpForMinx());
		row.add(isSignedUpForSq1());
		row.add(isSignedUpForPyr());
		row.add(isSignedUpForMbf());
		row.add(isSignedUpFor333ni());
		row.add(isSignedUpFor333sbf());
		row.add(isSignedUpFor333r3());
		row.add(isSignedUpFor333ts());
		row.add(isSignedUpFor333bts());
		row.add(isSignedUpFor222bf());
		row.add(isSignedUpFor333si());
		row.add(isSignedUpForRainb());
		row.add(isSignedUpForSnake());
		row.add(isSignedUpForSkewb());
		row.add(isSignedUpForMirbl());
		row.add(isSignedUpFor222oh());
		row.add(isSignedUpForMagico());
		row.add(isSignedUpFor360());
		return row;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (signedUpFor2x2 ? 1231 : 1237);
		result = prime * result + (signedUpFor3x3 ? 1231 : 1237);
		result = prime * result + (signedUpFor4x4 ? 1231 : 1237);
		result = prime * result + (signedUpFor5x5 ? 1231 : 1237);
		result = prime * result + (signedUpFor6x6 ? 1231 : 1237);
		result = prime * result + (signedUpFor7x7 ? 1231 : 1237);
		result = prime * result + (signedUpForBf ? 1231 : 1237);
		result = prime * result + (signedUpForBf4 ? 1231 : 1237);
		result = prime * result + (signedUpForBf5 ? 1231 : 1237);
		result = prime * result + (signedUpForClk ? 1231 : 1237);
		result = prime * result + (signedUpForFeet ? 1231 : 1237);
		result = prime * result + (signedUpForFm ? 1231 : 1237);
		result = prime * result + (signedUpForMbf ? 1231 : 1237);
		result = prime * result + (signedUpForMgc ? 1231 : 1237);
		result = prime * result + (signedUpForMinx ? 1231 : 1237);
		result = prime * result + (signedUpForMmgc ? 1231 : 1237);
		result = prime * result + (signedUpForOh ? 1231 : 1237);
		result = prime * result + (signedUpForPyr ? 1231 : 1237);
		result = prime * result + (signedUpForSq1 ? 1231 : 1237);
		result = prime * result + (signedUpFor333ni ? 1231 : 1237);
		result = prime * result + (signedUpFor333sbf ? 1231 : 1237);
		result = prime * result + (signedUpFor333r3 ? 1231 : 1237);
		result = prime * result + (signedUpFor333ts ? 1231 : 1237);
		result = prime * result + (signedUpFor333bts ? 1231 : 1237);
		result = prime * result + (signedUpFor222bf ? 1231 : 1237);
		result = prime * result + (signedUpFor333si ? 1231 : 1237);
		result = prime * result + (signedUpForRainb ? 1231 : 1237);
		result = prime * result + (signedUpForSnake ? 1231 : 1237);
		result = prime * result + (signedUpForSkewb ? 1231 : 1237);
		result = prime * result + (signedUpForMirbl ? 1231 : 1237);
		result = prime * result + (signedUpFor222oh ? 1231 : 1237);
		result = prime * result + (signedUpForMagico ? 1231 : 1237);
		result = prime * result + (signedUpFor360 ? 1231 : 1237);
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegisteredEvents other = (RegisteredEvents) obj;
		if (signedUpFor2x2 != other.signedUpFor2x2)
			return false;
		if (signedUpFor3x3 != other.signedUpFor3x3)
			return false;
		if (signedUpFor4x4 != other.signedUpFor4x4)
			return false;
		if (signedUpFor5x5 != other.signedUpFor5x5)
			return false;
		if (signedUpFor6x6 != other.signedUpFor6x6)
			return false;
		if (signedUpFor7x7 != other.signedUpFor7x7)
			return false;
		if (signedUpForBf != other.signedUpForBf)
			return false;
		if (signedUpForBf4 != other.signedUpForBf4)
			return false;
		if (signedUpForBf5 != other.signedUpForBf5)
			return false;
		if (signedUpForClk != other.signedUpForClk)
			return false;
		if (signedUpForFeet != other.signedUpForFeet)
			return false;
		if (signedUpForFm != other.signedUpForFm)
			return false;
		if (signedUpForMbf != other.signedUpForMbf)
			return false;
		if (signedUpForMgc != other.signedUpForMgc)
			return false;
		if (signedUpForMinx != other.signedUpForMinx)
			return false;
		if (signedUpForMmgc != other.signedUpForMmgc)
			return false;
		if (signedUpForOh != other.signedUpForOh)
			return false;
		if (signedUpForPyr != other.signedUpForPyr)
			return false;
		if (signedUpForSq1 != other.signedUpForSq1)
			return false;
		if (signedUpFor333ni != other.signedUpFor333ni)
			return false;
		if (signedUpFor333sbf != other.signedUpFor333sbf)
			return false;
		if (signedUpFor333r3 != other.signedUpFor333r3)
			return false;
		if (signedUpFor333ts != other.signedUpFor333ts)
			return false;
		if (signedUpFor333bts != other.signedUpFor333bts)
			return false;
		if (signedUpFor222bf != other.signedUpFor222bf)
			return false;
		if (signedUpFor333si != other.signedUpFor333si)
			return false;
		if (signedUpForRainb != other.signedUpForRainb)
			return false;
		if (signedUpForSnake != other.signedUpForSnake)
			return false;
		if (signedUpForSkewb != other.signedUpForSkewb)
			return false;
		if (signedUpForMirbl != other.signedUpForMirbl)
			return false;
		if (signedUpFor222oh != other.signedUpFor222oh)
			return false;
		if (signedUpForMagico != other.signedUpForMagico)
			return false;
		if (signedUpFor360 != other.signedUpFor360)
			return false;
		return true;
	}	
}
