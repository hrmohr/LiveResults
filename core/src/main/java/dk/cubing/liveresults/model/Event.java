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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@NamedQueries(value = {
		@NamedQuery(
				name = "findLiveEvent",
				query = "SELECT e FROM dk.cubing.liveresults.model.Competition c JOIN c.events e WHERE c.competitionId = :competitionId AND e.live = 1"
		)
})
@Table(name = "wca_events")
public class Event {
	
	public static enum Format {
		AVERAGE ("a"),
		MEAN ("m"),
		BEST_OF_1 ("1"),
		BEST_OF_2 ("2"),
		BEST_OF_3 ("3");
		
		private String value;
		private Format(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
	
	public static enum TimeFormat {
		SECONDS ("s"),
		MINUTES ("m"),
		MULTI_BLD ("b"),
		NUMBER ("n");
		
		private String value;
		private TimeFormat(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable=false, length = 1)
	private String format;

	@Column(nullable=false, length = 1)
	private String timeFormat;
	
	@OneToMany(cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN) 
	@JoinColumn(name = "eventId", nullable = false)
	@OrderBy("average ASC, best ASC")
	private List<Result> results;
	
	@Column(nullable = false)
	private boolean live = false;
	
	public Event() {
		this.results = new CopyOnWriteArrayList<Result>();
	}
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return
	 */
	public String getFormattedName() {
		if (isLive()) {
			return "Live: " + getName();
		} else {
			return getName();
		}
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the timeFormat
	 */
	public String getTimeFormat() {
		return timeFormat;
	}

	/**
	 * @param timeFormat the timeFormat to set
	 */
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<Result> results) {
		for (Result o : getResults()) {
			if (!results.contains(o)) {
				this.results.remove(o);
			} else {
				int i = results.indexOf(o);
				if (i != -1) {
					Result n = results.get(i);
					o.setResult1(n.getResult1());
					o.setResult2(n.getResult2());
					o.setResult3(n.getResult3());
					o.setResult4(n.getResult4());
					o.setResult5(n.getResult5());
					o.setAverage(n.getAverage());
					o.setBest(n.getBest());
					o.setWorst(n.getWorst());
					o.setRegionalAverageRecord(n.getRegionalAverageRecord());
					o.setRegionalSingleRecord(n.getRegionalSingleRecord());
				}
			}
		}
		for (Result n : results) {
			if (!getResults().contains(n)) {
				this.results.add(n);
			}
		}
	}

	/**
	 * @return the results
	 */
	public List<Result> getResults() {
		return results;
	}

	/**
	 * @param live the live to set
	 */
	public void setLive(boolean live) {
		this.live = live;
	}

	/**
	 * @return the live
	 */
	public boolean isLive() {
		return live;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + (live ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((timeFormat == null) ? 0 : timeFormat.hashCode());
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
		Event other = (Event) obj;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (live != other.live)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (timeFormat == null) {
			if (other.timeFormat != null)
				return false;
		} else if (!timeFormat.equals(other.timeFormat))
			return false;
		return true;
	}

}
