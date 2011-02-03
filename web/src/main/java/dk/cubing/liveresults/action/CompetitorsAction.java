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
package dk.cubing.liveresults.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.model.Result;
import dk.cubing.liveresults.service.CompetitionService;
import dk.cubing.liveresults.service.CompetitorService;
import dk.cubing.liveresults.service.ResultService;
import dk.cubing.liveresults.utilities.CountryUtil;

public class CompetitorsAction extends ActionSupport implements Preparable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(CompetitorsAction.class);
	
	private CompetitionService competitionService;
	private CompetitorService competitorService;
	private ResultService resultService;
	private String competitionId;
	private Competition competition;
	private CountryUtil countryUtil;
	private List<Result> records;

	/**
	 * Basic constructor
	 */
	public CompetitorsAction() {
		this.setCountryUtil(new CountryUtil());
	}
	
	/**
	 * @return the competitionService
	 */
	public CompetitionService getCompetitionService() {
		return competitionService;
	}

	/**
	 * @param competitionService the competitionService to set
	 */
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	/**
	 * @param competitorService the competitorService to set
	 */
	public void setCompetitorService(CompetitorService competitorService) {
		this.competitorService = competitorService;
	}

	/**
	 * @return the competitorService
	 */
	public CompetitorService getCompetitorService() {
		return competitorService;
	}

	/**
	 * @param resultService the resultService to set
	 */
	public void setResultService(ResultService resultService) {
		this.resultService = resultService;
	}

	/**
	 * @return the resultService
	 */
	public ResultService getResultService() {
		return resultService;
	}

	/**
	 * @param countryUtil the countryUtil to set
	 */
	public void setCountryUtil(CountryUtil countryUtil) {
		this.countryUtil = countryUtil;
	}

	/**
	 * @return the countryUtil
	 */
	public CountryUtil getCountryUtil() {
		return countryUtil;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(List<Result> records) {
		this.records = records;
	}

	/**
	 * @return the records
	 */
	public List<Result> getRecords() {
		return records;
	}

	/**
	 * @return the competitionId
	 */
	public String getCompetitionId() {
		return competitionId;
	}

	/**
	 * @param competitionId the competitionId to set
	 */
	public void setCompetitionId(String competitionId) {
		this.competitionId = competitionId;
	}

	/**
	 * @return the competition
	 */
	public Competition getCompetition() {
		return competition;
	}

	/**
	 * @param competition the competition to set
	 */
	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	
	/**
	 * @return
	 */
	public List<Integer> getRegisteredEventsCount() {
		if (getCompetition() != null) {
			return getCompetitorService().getRegisteredEventsCount(getCompetition().getCompetitionId());
		} else {
			return null;
		}
	}
	
	/**
	 * @return
	 */
	public int getNumberOfCountries() {
		if (getCompetition() != null) {
			return getCompetitorService().getNumberOfCountries(getCompetition().getCompetitionId());
		} else {
			return -1;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() {
		return Action.SUCCESS;
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		if (getCompetitionId() != null) {
			Competition competition = getCompetitionService().find(getCompetitionId());
			if (competition != null) {
				setCompetition(competition);
				log.debug("Loaded competition: {}", getCompetition().getName());
				setRecords(getResultService().getRecords(getCompetitionId()));
			}
		}
	}

}
