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

import dk.cubing.liveresults.model.Competition;

@WebService
public interface LiveResults {

	/**
	 * @param clientVersion
	 * @return
	 * @throws UnsupportedClientVersionException
	 */
	public boolean isSupportedVersion(String clientVersion) throws UnsupportedClientVersionException;
	
	/**
	 * @param competitionId
	 * @param password
	 * @return
	 * @throws BadCredentialsException
	 * @throws CompetitionNotFoundException
	 */
	public Competition loadCompetition(String competitionId, String password) throws BadCredentialsException, CompetitionNotFoundException;
	
	/**
	 * @param competitionId
	 * @param password
	 * @param competition
	 * @throws BadCredentialsException
	 * @throws CompetitionNotFoundException
	 * @throws CompetitionSaveException
	 */
	public void saveCompetition(String competitionId, String password, Competition competition) throws BadCredentialsException, CompetitionNotFoundException, CompetitionSaveException;

}
