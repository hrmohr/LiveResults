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

import dk.cubing.liveresults.model.Result;

public class ResultDAO extends GenericDAO<Result, Integer> {

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.dao.GenericDAO#list(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Result> list(int page, int size) {
		Query query = getEntityManager().createQuery("FROM dk.cubing.liveresults.model.Result ORDER BY eventId ASC, average ASC, best ASC");
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
	@SuppressWarnings("unchecked")
	public List<Result> findRecords(String competitionId) {
		Query query = getEntityManager().createNamedQuery("findRecords");
		query.setParameter("competitionId", competitionId);
        return query.getResultList();
	}
}
