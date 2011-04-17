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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import dk.cubing.liveresults.utilities.StringUtil;

@Entity
@Table(name = "wca_competitions")
public class Competition implements Serializable {

	@Id
	private String competitionId;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false, length = 2)
	private String country;
	
	private String venue;
	private String website;
	
	private String organiser;
	private String wcaDelegate;
	
	private String organiserEmail;
	private String wcaDelegateEmail;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	@OrderBy
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@OneToMany(cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN) 
	@JoinColumn(name = "competitionId", nullable = false)
	@OrderBy("country ASC, surname ASC, firstname ASC")
	private List<Competitor> competitors;
	
	@OneToMany(cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN) 
	@JoinColumn(name = "competitionId", nullable = false)
	@OrderBy("live DESC, name ASC")
	private List<Event> events;
	
	public Competition() {
		competitors = new CopyOnWriteArrayList<Competitor>();
		events = new CopyOnWriteArrayList<Event>();
	}
	
	/**
	 * @return the competitionId
	 */
	public String getCompetitionId() {
		return competitionId;
	}

	/**
	 * @param competitionId the competitionId to set
	 */
	public void setCompetitionId(String competitionId) {
		this.competitionId = competitionId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
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
	 * @return the venue
	 */
	public String getVenue() {
		return venue;
	}

	/**
	 * @param venue the venue to set
	 */
	public void setVenue(String venue) {
		this.venue = venue;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the organiser
	 */
	public String getOrganiser() {
		return organiser;
	}

	/**
	 * @param organiser the organiser to set
	 */
	public void setOrganiser(String organiser) {
		this.organiser = organiser;
	}

	/**
	 * @return the wcaDelegate
	 */
	public String getWcaDelegate() {
		return wcaDelegate;
	}

	/**
	 * @param wcaDelegate the wcaDelegate to set
	 */
	public void setWcaDelegate(String wcaDelegate) {
		this.wcaDelegate = wcaDelegate;
	}

	/**
	 * @param organiserEmail the organiserEmail to set
	 */
	public void setOrganiserEmail(String organiserEmail) {
		this.organiserEmail = organiserEmail;
	}

	/**
	 * @return the organiserEmail
	 */
	public String getOrganiserEmail() {
		return organiserEmail;
	}

	/**
	 * @param wcaDelegateEmail the wcaDelegateEmail to set
	 */
	public void setWcaDelegateEmail(String wcaDelegateEmail) {
		this.wcaDelegateEmail = wcaDelegateEmail;
	}

	/**
	 * @return the wcaDelegateEmail
	 */
	public String getWcaDelegateEmail() {
		return wcaDelegateEmail;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @param competitors the competitors to set
	 */
	public void setCompetitors(List<Competitor> competitors) {
		for (Competitor o : getCompetitors()) {
			if (!competitors.contains(o)) {
				this.competitors.remove(o);
			}
		}
		for (Competitor n : competitors) {
			if (!getCompetitors().contains(n)) {
				this.competitors.add(n);
			}
		}
	}

	/**
	 * @return the competitors
	 */
	public List<Competitor> getCompetitors() {
		return competitors;
	}
	
	/**
	 * @param event
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<Competitor> getCompetitorsByEvent(Event event)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		List<Competitor> competitorsByEvent = new CopyOnWriteArrayList<Competitor>();
		for (Competitor competitor : getCompetitors()) {
			Class<?> c = competitor.getRegisteredEvents().getClass();
			Method m = c.getDeclaredMethod("isSignedUpFor" + StringUtil.ucfirst(event.getName()));
			if ((Boolean) m.invoke(competitor.getRegisteredEvents())) {
				competitorsByEvent.add(competitor);
			}
		}
		return competitorsByEvent;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(List<Event> events) {
		boolean hasLive = false;
		for (Event o : getEvents()) {
			if (!events.contains(o)) {
				this.events.remove(o);
			} else {
				int i = events.indexOf(o);
				if (i != -1) {
					Event n = events.get(i);
					o.setName(n.getName());
					o.setFormat(n.getFormat());
					o.setTimeFormat(n.getTimeFormat());
					o.setResults(n.getResults());
					o.setLive(n.isLive());
					if (!hasLive) {
						hasLive = n.isLive();
					}
				}
			}
		}
		for (Event n : events) {
			if (!getEvents().contains(n)) {
				this.events.add(n);
				if (!hasLive) {
					hasLive = n.isLive();
				}
			}
		}	
		// if no events are marked as live then select the first event
		if (!hasLive && getEvents().size() > 0) {
			getEvents().get(0).setLive(true);
		}
	}

	/**
	 * @return the events
	 */
	public List<Event> getEvents() {
		return events;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((competitionId == null) ? 0 : competitionId.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((organiser == null) ? 0 : organiser.hashCode());
		result = prime * result
				+ ((organiserEmail == null) ? 0 : organiserEmail.hashCode());
		result = prime * result
				+ ((wcaDelegateEmail == null) ? 0 : wcaDelegateEmail.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((venue == null) ? 0 : venue.hashCode());
		result = prime * result
				+ ((wcaDelegate == null) ? 0 : wcaDelegate.hashCode());
		result = prime * result + ((website == null) ? 0 : website.hashCode());
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
		Competition other = (Competition) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (competitionId == null) {
			if (other.competitionId != null)
				return false;
		} else if (!competitionId.equals(other.competitionId))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (organiser == null) {
			if (other.organiser != null)
				return false;
		} else if (!organiser.equals(other.organiser))
			return false;
		if (organiserEmail == null) {
			if (other.organiserEmail != null)
				return false;
		} else if (!organiserEmail.equals(other.organiserEmail))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (venue == null) {
			if (other.venue != null)
				return false;
		} else if (!venue.equals(other.venue))
			return false;
		if (wcaDelegate == null) {
			if (other.wcaDelegate != null)
				return false;
		} else if (!wcaDelegate.equals(other.wcaDelegate))
			return false;
		if (wcaDelegateEmail == null) {
			if (other.wcaDelegateEmail != null)
				return false;
		} else if (!wcaDelegateEmail.equals(other.wcaDelegateEmail))
			return false;
		if (website == null) {
			if (other.website != null)
				return false;
		} else if (!website.equals(other.website))
			return false;
		return true;
	}

}
