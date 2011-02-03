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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@NamedNativeQueries(value = {
	@NamedNativeQuery(
			name = "findAverageResultsByEvent",
			query = "SELECT * FROM wca_results WHERE eventId = :eventId ORDER BY SIGN(average) DESC, average ASC, best ASC, surname ASC",
			resultClass = dk.cubing.liveresults.model.Result.class
	),
	@NamedNativeQuery(
			name = "findBestResultsByEvent",
			query = "SELECT * FROM wca_results WHERE eventId = :eventId ORDER BY SIGN(best) DESC, best ASC, surname ASC",
			resultClass = dk.cubing.liveresults.model.Result.class
	),
	@NamedNativeQuery(
			name = "findAverageResultsByEventAndCountry",
			query = "SELECT * FROM wca_results WHERE eventId = :eventId AND country = :country ORDER BY SIGN(average) DESC, average ASC, best ASC, surname ASC",
			resultClass = dk.cubing.liveresults.model.Result.class
	),
	@NamedNativeQuery(
			name = "findBestResultsByEventAndCountry",
			query = "SELECT * FROM wca_results WHERE eventId = :eventId AND country = :country ORDER BY SIGN(best) DESC, best ASC, surname ASC",
			resultClass = dk.cubing.liveresults.model.Result.class
	),
	@NamedNativeQuery(
			name = "findAverageWinnersByEvent",
			query = "SELECT * FROM wca_results WHERE eventId = :eventId AND average > 0 ORDER BY average ASC, best ASC, surname ASC",
			resultClass = dk.cubing.liveresults.model.Result.class
	),
	@NamedNativeQuery(
			name = "findBestWinnersByEvent",
			query = "SELECT * FROM wca_results WHERE eventId = :eventId AND best > 0 ORDER BY best ASC, surname ASC",
			resultClass = dk.cubing.liveresults.model.Result.class
	),
	@NamedNativeQuery(
			name = "findAverageWinnersByEventAndCountry",
			query = "SELECT * FROM wca_results WHERE eventId = :eventId AND country = :country AND average > 0 ORDER BY average ASC, best ASC, surname ASC",
			resultClass = dk.cubing.liveresults.model.Result.class
	),
	@NamedNativeQuery(
			name = "findBestWinnersByEventAndCountry",
			query = "SELECT * FROM wca_results WHERE eventId = :eventId AND country = :country AND best > 0 ORDER BY best ASC, surname ASC",
			resultClass = dk.cubing.liveresults.model.Result.class
	),
	@NamedNativeQuery(
			name = "findRecords",
			query = "SELECT r.* FROM wca_events e, wca_results r WHERE e.competitionId = :competitionId AND e.id=r.eventId AND (r.regionalAverageRecord IS NOT NULL OR r.regionalSingleRecord IS NOT NULL) ORDER BY e.name DESC, r.surname ASC",
			resultClass = dk.cubing.liveresults.model.Result.class
	)
})
@Table(name = "wca_results")
public class Result {
	
	public static enum Penalty {
		DNF (-1),
		DNS (-2);
		
		private int value;
		private Penalty(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	
	public static enum Record {
		NR ("NR"),
		WR ("WR"),
		CR ("CR"),
		AFR ("AfR"),
		ASR ("AsR"),
		AUR ("AuR"),
		ER ("ER"),
		NAR ("NAR"),
		SAR ("SAR");
		
		private String value;
		private Record(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column(insertable=false, updatable=false)
	private int eventId;
	
	@Column(length = 10)
	private String wcaId;

	@Column(nullable = false)
	private String firstname;

	@Column(nullable = false)
	private String surname;

	@Column(nullable = false, length = 2)
	private String country;
	
	private int result1; 	
	private int result2; 	
	private int result3; 	
	private int result4; 	
	private int result5;
	private int best;
	private int worst;
	private int average;

	@Column(length = 3)
	private String regionalSingleRecord;

	@Column(length = 3)
	private String regionalAverageRecord;
	
	@Transient
	private transient int rank;
	
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
	 * @param eventId the eventId to set
	 */
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the eventId
	 */
	public int getEventId() {
		return eventId;
	}

	/**
	 * @return the wcaId
	 */
	public String getWcaId() {
		return wcaId;
	}

	/**
	 * @param wcaId the wcaId to set
	 */
	public void setWcaId(String wcaId) {
		this.wcaId = wcaId;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the result1
	 */
	public int getResult1() {
		return result1;
	}

	/**
	 * @param result1 the result1 to set
	 */
	public void setResult1(int result1) {
		this.result1 = result1;
	}

	/**
	 * @return the result2
	 */
	public int getResult2() {
		return result2;
	}

	/**
	 * @param result2 the result2 to set
	 */
	public void setResult2(int result2) {
		this.result2 = result2;
	}

	/**
	 * @return the result3
	 */
	public int getResult3() {
		return result3;
	}

	/**
	 * @param result3 the result3 to set
	 */
	public void setResult3(int result3) {
		this.result3 = result3;
	}

	/**
	 * @return the result4
	 */
	public int getResult4() {
		return result4;
	}

	/**
	 * @param result4 the result4 to set
	 */
	public void setResult4(int result4) {
		this.result4 = result4;
	}

	/**
	 * @return the result5
	 */
	public int getResult5() {
		return result5;
	}

	/**
	 * @param result5 the result5 to set
	 */
	public void setResult5(int result5) {
		this.result5 = result5;
	}

	/**
	 * @return the best
	 */
	public int getBest() {
		return best;
	}

	/**
	 * @param best the best to set
	 */
	public void setBest(int best) {
		this.best = best;
	}

	/**
	 * @param worst the worst to set
	 */
	public void setWorst(int worst) {
		this.worst = worst;
	}

	/**
	 * @return the worst
	 */
	public int getWorst() {
		return worst;
	}

	/**
	 * @return the average
	 */
	public int getAverage() {
		return average;
	}

	/**
	 * @param average the average to set
	 */
	public void setAverage(int average) {
		this.average = average;
	}

	/**
	 * @return the regionalSingleRecord
	 */
	public String getRegionalSingleRecord() {
		return regionalSingleRecord;
	}

	/**
	 * @param regionalSingleRecord the regionalSingleRecord to set
	 */
	public void setRegionalSingleRecord(String regionalSingleRecord) {
		this.regionalSingleRecord = regionalSingleRecord;
	}

	/**
	 * @return the regionalAverageRecord
	 */
	public String getRegionalAverageRecord() {
		return regionalAverageRecord;
	}

	/**
	 * @param regionalAverageRecord the regionalAverageRecord to set
	 */
	public void setRegionalAverageRecord(String regionalAverageRecord) {
		this.regionalAverageRecord = regionalAverageRecord;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result + ((wcaId == null) ? 0 : wcaId.hashCode());
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
		Result other = (Result) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		if (wcaId == null) {
			if (other.wcaId != null)
				return false;
		} else if (!wcaId.equals(other.wcaId))
			return false;
		return true;
	}
	
}
