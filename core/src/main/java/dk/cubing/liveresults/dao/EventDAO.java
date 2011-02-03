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
package dk.cubing.liveresults.dao;

import java.util.List;

import javax.persistence.Query;

import dk.cubing.liveresults.model.Event;
import dk.cubing.liveresults.model.Result;

public class EventDAO extends GenericDAO<Event, Integer> {

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.dao.GenericDAO#list(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Event> list(int page, int size) {
		Query query = getEntityManager().createQuery("FROM dk.cubing.liveresults.model.Event ORDER BY live DESC");
        query.setFirstResult(Math.max(0, page - 1) * size);
        if (size > 0) {
        	query.setMaxResults(size);
        }
        return query.getResultList();
	}
	
	/**
	 * @param event
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Result> getResults(Event event, int page, int size) {
		return getResults(event, page, size, null);
	}
	
	/**
	 * @param event
	 * @param page
	 * @param size
	 * @param country
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Result> getResults(Event event, int page, int size, String country) {
		Query query;
		if (Event.Format.AVERAGE.getValue().equals(event.getFormat()) || 
				Event.Format.MEAN.getValue().equals(event.getFormat())) {
			query = getEntityManager().createNamedQuery((country != null)? "findAverageResultsByEventAndCountry" : "findAverageResultsByEvent");
		} else {
			query = getEntityManager().createNamedQuery((country != null)? "findBestResultsByEventAndCountry" : "findBestResultsByEvent");
		}
		query.setParameter("eventId", event.getId());
		if (country != null) {
			query.setParameter("country", country);
		}
        query.setFirstResult(Math.max(0, page - 1) * size);
        if (size > 0) {
        	query.setMaxResults(size);
        }
        return query.getResultList();
	}
	
	/**
	 * @param event
	 * @return
	 */
	public List<Result> getWinners(Event event) {
		return getWinners(event, null);
	}
	
	/**
	 * @param event
	 * @param country
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Result> getWinners(Event event, String country) {
		Query query;
		if (Event.Format.AVERAGE.getValue().equals(event.getFormat()) || 
				Event.Format.MEAN.getValue().equals(event.getFormat())) {
			query = getEntityManager().createNamedQuery((country != null)? "findAverageWinnersByEventAndCountry" : "findAverageWinnersByEvent");
		} else {
			query = getEntityManager().createNamedQuery((country != null)? "findBestWinnersByEventAndCountry" : "findBestWinnersByEvent");
		}
		query.setParameter("eventId", event.getId());
		if (country != null) {
			query.setParameter("country", country);
		}
        query.setFirstResult(0);
        query.setMaxResults(3);
        return query.getResultList();
	}

}
