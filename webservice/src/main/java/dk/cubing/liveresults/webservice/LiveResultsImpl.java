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
package dk.cubing.liveresults.webservice;

import javax.jws.WebService;

import org.springframework.security.BadCredentialsException;

import dk.cubing.liveresults.jms.Producer;
import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.service.CompetitionService;

@WebService(endpointInterface = "dk.cubing.liveresults.webservice.LiveResults") 
public class LiveResultsImpl extends GenericWebService implements LiveResults {
	
	private String clientVersion;
	private CompetitionService service;
	private Producer producer = null;

	/**
	 * @param clientVersion the clientVersion to set
	 */
	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}
	
	/**
	 * @return the clientVersion
	 */
	public String getClientVersion() {
		return clientVersion;
	}
	
	/**
	 * @param service the service to set
	 */
	public void setService(CompetitionService service) {
		this.service = service;
	}
	
	/**
	 * @return the service
	 */
	public CompetitionService getService() {
		return service;
	}

	/**
	 * @param producer the producer to set
	 */
	public void setProducer(Producer producer) {
		this.producer = producer;
	}

	/**
	 * @return the producer
	 */
	public Producer getProducer() {
		return producer;
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.webservice.LiveResults#isSupportedVersion(java.lang.String)
	 */
	public boolean isSupportedVersion(String clientVersion) throws UnsupportedClientVersionException {
		// check for supported client version
		if (!getClientVersion().equals(clientVersion)) {
			throw new UnsupportedClientVersionException("Unsupported client version. Expected: " + getClientVersion() + ", was: " + clientVersion);
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.webservice.LiveResults#loadCompetition(java.lang.String, java.lang.String)
	 */
	public Competition loadCompetition(String competitionId, String password) throws BadCredentialsException, CompetitionNotFoundException {
		authenticateUser(competitionId, password);
		
		Competition competition = null;
		try {
			competition = getService().find(competitionId);
		} catch (Exception e) {
			throw new CompetitionNotFoundException(e);
		}
		if (competition == null) {
			throw new CompetitionNotFoundException("Could not load competition with id: " + competitionId);
		}

		return competition;
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.webservice.LiveResults#saveCompetition(java.lang.String, java.lang.String, dk.cubing.liveresults.model.Competition)
	 */
	public void saveCompetition(String competitionId, String password, Competition competition) throws BadCredentialsException, CompetitionNotFoundException, CompetitionSaveException {
		authenticateUser(competitionId, password);

		try {
			// only existing competitions can be saved
			if (loadCompetition(competitionId, password) != null) {
				getService().update(competition);
                if (getProducer() != null) {
				    getProducer().send(competitionId);
                }
			}
		} catch (CompetitionNotFoundException e) {
			throw new CompetitionNotFoundException(e);
		} catch (Exception e) {
			throw new CompetitionSaveException(e);
		}
	}

}
