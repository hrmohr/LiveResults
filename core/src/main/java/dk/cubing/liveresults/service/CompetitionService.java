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
package dk.cubing.liveresults.service;

import java.util.List;

import org.springframework.security.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import dk.cubing.liveresults.dao.CompetitionDAO;
import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.model.Event;

@Transactional
public class CompetitionService implements GenericService<Competition, String> {
	
	private CompetitionDAO dao;

	/**
	 * @param dao the dao to set
	 */
	public void setDao(CompetitionDAO dao) {
		this.dao = dao;
	}

	/**
	 * @return the dao
	 */
	public CompetitionDAO getDao() {
		return dao;
	}
	
	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.service.GenericService#create(java.lang.Object)
	 */
	@Secured( { "ROLE_USER" })
	@Override
	public Competition create(Competition entity) {
		getDao().save(entity);
		return entity;
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.service.GenericService#delete(java.lang.Object)
	 */
	@Secured( { "ROLE_USER" })
	@Override
	public void delete(Competition entity) {
		getDao().delete(entity);
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.service.GenericService#find(java.io.Serializable)
	 */
	@Override
	public Competition find(String id) {
		return getDao().getById(new String(id));
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.service.GenericService#list(int, int)
	 */
	@Override
	public List<Competition> list(int page, int size) {
		return getDao().list(page, size);
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.service.GenericService#update(java.lang.Object)
	 */
	@Secured( { "ROLE_USER" })
	@Override
	public Competition update(Competition entity) {
		getDao().update(entity);
		return entity;
	}
	
	/**
	 * @param competitionId
	 * @return
	 */
	public Event findLiveEvent(String competitionId) {
		return getDao().findLiveEvent(competitionId);
	}

}
