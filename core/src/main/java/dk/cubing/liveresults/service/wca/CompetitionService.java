/*
 * Copyright (C) 2011 Mads Mohr Christensen, <hr.mohr@gmail.com>
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

package dk.cubing.liveresults.service.wca;

import dk.cubing.liveresults.dao.wca.CompetitionDAO;
import dk.cubing.liveresults.model.wca.Competition;
import dk.cubing.liveresults.service.GenericService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional("transactionManagerWca")
public class CompetitionService implements GenericService<Competition, String> {

    private CompetitionDAO dao;

    public CompetitionDAO getDao() {
        return dao;
    }

    public void setDao(CompetitionDAO dao) {
        this.dao = dao;
    }

    @Override
    public Competition create(Competition entity) {
        getDao().save(entity);
        return entity;
    }

    @Override
    public void delete(Competition entity) {
        getDao().delete(entity);
    }

    @Override
    public Competition update(Competition entity) {
        getDao().update(entity);
        return entity;
    }

    @Override
    public List<Competition> list(int page, int size) {
        return getDao().list(page, size);
    }

    @Override
    public Competition find(String id) {
        return getDao().getById(new String(id));
    }
}
