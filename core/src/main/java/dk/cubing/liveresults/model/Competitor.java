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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@NamedQueries({
	@NamedQuery(
			name = "numberOfCountries", 
			query = "SELECT COUNT(DISTINCT country) FROM dk.cubing.liveresults.model.Competitor WHERE competitionId = :competitionId"
	)
})
@Table(name = "wca_competitors")
public class Competitor {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(length = 10)
	private String wcaId;
	private String firstname;
	private String surname;
	@Column(length = 2)
	private String country;
	
	@Column(length = 1)
	@Transient
	private String gender;
	
	@Temporal(value = TemporalType.DATE)
	@Transient
	private Date birthday;
	
	@OneToOne(cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN) 
	@JoinColumn(name = "registeredEventsId", nullable = false)
	private RegisteredEvents registeredEvents = new RegisteredEvents();
	
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
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
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
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param registeredEvents the registeredEvents to set
	 */
	public void setRegisteredEvents(RegisteredEvents registeredEvents) {
		this.registeredEvents = registeredEvents;
	}

	/**
	 * @return the registeredEvents
	 */
	public RegisteredEvents getRegisteredEvents() {
		return registeredEvents;
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
		result = prime
				* result
				+ ((registeredEvents == null) ? 0 : registeredEvents.hashCode());
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
		Competitor other = (Competitor) obj;
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
		if (registeredEvents == null) {
			if (other.registeredEvents != null)
				return false;
		} else if (!registeredEvents.equals(other.registeredEvents))
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
