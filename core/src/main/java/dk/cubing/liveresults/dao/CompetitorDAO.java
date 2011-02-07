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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import dk.cubing.liveresults.model.Competitor;

public class CompetitorDAO extends GenericDAO<Competitor, Integer> {
	
	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.dao.GenericDAO#list(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Competitor> list(int page, int size) {
        Query query = getEntityManager().createQuery("FROM dk.cubing.liveresults.model.Competitor");
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
	public int getNumberOfCountries(String competitionId) {
        Query query = getEntityManager().createNamedQuery("numberOfCountries");
        query.setParameter("competitionId", competitionId);
        try {
        	return ((Long) query.getSingleResult()).intValue();
        } catch (NoResultException e) {
        	return 0;
        }
	}
	
	/**
	 * @param competitionId
	 * @return
	 */
	public List<Integer> getRegisteredEventsCount(String competitionId) {
		// if this are to use namedquery then you would need to write a result set mapping
		String sql = 
			 "SELECT SUM(e.signedUpFor2x2), SUM(e.signedUpFor3x3), SUM(e.signedUpFor4x4), SUM(e.signedUpFor5x5), SUM(e.signedUpFor6x6), "
			 + "SUM(e.signedUpFor7x7), SUM(e.signedUpForFm), SUM(e.signedUpForOh), SUM(e.signedUpForBf), SUM(e.signedUpForBf4), SUM(e.signedUpForBf5), "
			 + "SUM(e.signedUpForFeet), SUM(e.signedUpForClk), SUM(e.signedUpForMgc), SUM(e.signedUpForMmgc), SUM(e.signedUpForMinx), SUM(e.signedUpForSq1), "
			 + "SUM(e.signedUpForPyr), SUM(e.signedUpForMbf), SUM(e.signedUpFor333ni), SUM(e.signedUpFor333sbf), SUM(e.signedUpFor333r3), SUM(e.signedUpFor333ts), "
			 + "SUM(e.signedUpFor333bts), SUM(e.signedUpFor222bf), SUM(e.signedUpFor333si), SUM(e.signedUpForRainb), SUM(e.signedUpForSnake), SUM(e.signedUpForSkewb), "
			 + "SUM(e.signedUpForMirbl), SUM(e.signedUpFor222oh), SUM(e.signedUpForMagico), SUM(e.signedUpFor360) "
			 + "FROM wca_competitors c, wca_registered_events e "
			 + "WHERE c.competitionId = :competitionId "
			 + "AND c.registeredEventsId = e.id";
		Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter("competitionId", competitionId);
        try {
	        Object[] temp = (Object[])query.getSingleResult();
	        List<Integer> result = new ArrayList<Integer>();
	        for (int i=0; i<temp.length; i++) {
	        	result.add(((BigDecimal)temp[i]).intValue());
	        }
	        return result;
        } catch (NoResultException e) {
        	return Collections.emptyList();
        }
	}
	
}
