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

import dk.cubing.liveresults.dao.ResultDAO;
import dk.cubing.liveresults.model.Result;

@Transactional
public class ResultService implements GenericService<Result, Integer> {
	
	private ResultDAO dao;

	/**
	 * @param dao the dao to set
	 */
	public void setDao(ResultDAO dao) {
		this.dao = dao;
	}

	/**
	 * @return the dao
	 */
	public ResultDAO getDao() {
		return dao;
	}
	
	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.service.GenericService#create(java.lang.Object)
	 */
	@Secured( { "ROLE_USER" })
	@Override
	public Result create(Result entity) {
		getDao().save(entity);
		return entity;
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.service.GenericService#delete(java.lang.Object)
	 */
	@Secured( { "ROLE_USER" })
	@Override
	public void delete(Result entity) {
		getDao().delete(entity);
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.service.GenericService#find(java.io.Serializable)
	 */
	@Override
	public Result find(Integer id) {
		return getDao().getById(new Integer(id));
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.service.GenericService#list(int, int)
	 */
	@Override
	public List<Result> list(int page, int size) {
		return getDao().list(page, size);
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.service.GenericService#update(java.lang.Object)
	 */
	@Secured( { "ROLE_USER" })
	@Override
	public Result update(Result entity) {
		getDao().update(entity);
		return entity;
	}
	
	/**
	 * @param competitionId
	 * @return
	 */
	public List<Result> getRecords(String competitionId) {
		return getDao().findRecords(competitionId);
	}
}
