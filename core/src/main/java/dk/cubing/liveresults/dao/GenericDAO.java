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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class GenericDAO<T, ID extends Serializable> {

	private Class<T> persistentClass;

	protected EntityManager em;

	/**
	 * get class of instance
	 */
	@SuppressWarnings("unchecked")
	public GenericDAO() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * @return
	 */
	private Class<T> getPersistentClass() {
		return this.persistentClass;
	}

	/**
	 * @param em
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/**
	 * @return
	 */
	public EntityManager getEntityManager() {
		return em;
	}

	/**
	 * @param entity
	 */
	public void save(T entity) {
		getEntityManager().persist(entity);
	}
	
	/**
	 * @param entity
	 */
	public void update(T entity) {
		getEntityManager().merge(entity);
	}

	/**
	 * @param entity
	 */
	public void delete(T entity) {
		getEntityManager().remove(entity);
	}

	/**
	 * @param id
	 * @return
	 */
	public T getById(ID id) {
		T entity = (T) getEntityManager().find(getPersistentClass(), id);
		return entity;
	}
	
	/**
	 * @param page
	 * @param size
	 * @return
	 */
	public abstract List<T> list(int page, int size);

}
