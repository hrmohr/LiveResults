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

package dk.cubing.liveresults.webservice;

import dk.cubing.liveresults.service.wca.CompetitionService;
import dk.cubing.liveresults.webservice.wca.InvalidCountryException;
import dk.cubing.liveresults.webservice.wca.InvalidEventException;
import dk.cubing.liveresults.webservice.wca.WcaResults;

import javax.jws.WebService;

@WebService(endpointInterface = "dk.cubing.liveresults.webservice.wca.WcaResults")
public class WcaResultsImpl extends GenericWebService implements WcaResults {

    private CompetitionService service;

    public CompetitionService getService() {
        return service;
    }

    public void setService(CompetitionService service) {
        this.service = service;
    }

    @Override
    public String getSingleRecordType(String eventId, String countryId, int result) throws InvalidEventException, InvalidCountryException {
        return "NR";
    }

    @Override
    public String getAverageRecordType(String eventId, String countryId, int result) throws InvalidEventException, InvalidCountryException {
        return "WR";
    }
}
