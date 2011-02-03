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

import javax.persistence.NoResultException;
import javax.persistence.Query;

import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.model.Event;

public class CompetitionDAO extends GenericDAO<Competition, String> {

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.dao.GenericDAO#list(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Competition> list(int page, int size) {
		Query query = getEntityManager().createQuery("FROM dk.cubing.liveresults.model.Competition ORDER BY startDate DESC");
        query.setFirstResult(Math.max(0, page - 1) * size);
        if (size > 0) {
        	query.setMaxResults(size);
        }
        return query.getResultList();
	}
	
	/**
	 * @param competitionId
	 * @return
	 */
	public Event findLiveEvent(String competitionId) {
		Query query = getEntityManager().createNamedQuery("findLiveEvent");
		query.setParameter("competitionId", competitionId);
    	query.setMaxResults(1);
    	try {
    		return (Event)query.getSingleResult();
    	} catch (NoResultException e) {
    		return null;
    	}
	}

}
