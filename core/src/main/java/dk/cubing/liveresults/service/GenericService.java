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

import java.io.Serializable;
import java.util.List;

public interface GenericService<T, ID extends Serializable> {

    /**
     * @param entity
     * @return
     */
    T create(T entity);

    /**
     * @param entity
     */
    void delete(T entity);
    
    /**
     * @param entity
     * @return
     */
    T update(T entity);

    /**
     * @param page
     * @param size
     * @return
     */
    List<T> list(int page, int size);
    
    /**
     * @param id
     * @return
     */
    T find(ID id);

}
