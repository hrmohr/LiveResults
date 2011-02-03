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

import org.apache.struts2.json.annotations.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;

import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.model.Event;
import dk.cubing.liveresults.model.Result;
import dk.cubing.liveresults.service.CompetitionService;
import dk.cubing.liveresults.service.EventService;
import dk.cubing.liveresults.service.ResultService;
import dk.cubing.liveresults.utilities.CountryUtil;
import dk.cubing.liveresults.utilities.ResultTimeFormat;

public class CompetitionAction extends FrontendAction implements Preparable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(CompetitionAction.class);
	
	private CompetitionService competitionService;
	private EventService eventService;
	private ResultService resultService;
	private List<Competition> competitions;
	private String competitionId;
	private Competition competition;
	private int eventId;
	private Event event;
	private List<Result> results;
	private List<Result> records;
	private CountryUtil countryUtil;
	private ResultTimeFormat resultTimeFormat;
	
	/**
	 * Basic constructor
	 */
	public CompetitionAction() {
		super();
		this.setCountryUtil(new CountryUtil());
		this.setResultTimeFormat(new ResultTimeFormat());
	}

	/**
	 * @param service the service to set
	 */
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	/**
	 * @return the service
	 */
	@JSON(serialize = false)
	private CompetitionService getCompetitionService() {
		return competitionService;
	}
	
	/**
	 * @param eventService the eventService to set
	 */
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	/**
	 * @return the eventService
	 */
	@JSON(serialize = false)
	public EventService getEventService() {
		return eventService;
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
	@JSON(serialize = false)
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
	@JSON(serialize = false)
	public CountryUtil getCountryUtil() {
		return countryUtil;
	}

	/**
	 * @param resultTimeFormat the resultTimeFormat to set
	 */
	public void setResultTimeFormat(ResultTimeFormat resultTimeFormat) {
		this.resultTimeFormat = resultTimeFormat;
	}

	/**
	 * @return the resultTimeFormat
	 */
	public ResultTimeFormat getResultTimeFormat() {
		return resultTimeFormat;
	}

	/**
	 * @return
	 */
	@JSON(serialize = false)
	public List<Competition> getCompetitions() {
		return competitions;
	}
	
	/**
	 * @return the competitionId
	 */
	@JSON(serialize = false)
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
	@JSON(serialize = false)
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
	 * @param eventId the eventId to set
	 */
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the eventId
	 */
	@JSON(serialize = false)
	public int getEventId() {
		return eventId;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(Event event) {
		this.event = event;
	}

	/**
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}
	
	/**
	 * @param results the results to set
	 */
	public void setResults(List<Result> results) {
		this.results = results;
	}
	
	/**
	 * @return
	 */
	public List<Result> getResults() {
		boolean average = (getEvent().getFormat().equals(Event.Format.AVERAGE.getValue()) || 
				getEvent().getFormat().equals(Event.Format.MEAN.getValue()));
		for (int i=0; i<results.size(); i++) {
			Result result = results.get(i);
			if (result.getCountry().length() == 2) {
				result.setCountry(getCountryUtil().getCountryByCode(result.getCountry()));
			}
			if (i > 0 && average) {
				if (result.getAverage() == results.get(i-1).getAverage()) {
					if (result.getBest() == results.get(i-1).getBest()) {
						result.setRank(results.get(i-1).getRank());
					}
				}
			} else if (i > 0 && !average) {
				if (result.getBest() == results.get(i-1).getBest()) {
					result.setRank(results.get(i-1).getRank());
				}
			}
			if (result.getRank() == 0) result.setRank(i + 1);
		}
		return results;
	}
	
	/**
	 * @param records the records to set
	 */
	public void setRecords(List<Result> records) {
		this.records = records;
	}
	
	/**
	 * @return
	 */
	public List<Result> getRecords() {
		return records;
	}
	
	/**
	 * @return
	 */
	public int getPages() {
		double pages = Math.ceil(new Double(getEvent().getResults().size()) / new Double(getSize()));
		return new Double(pages).intValue();
	}
	
	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.action.FrontendAction#list()
	 */
	@Override
	public String list() {
		competitions = getCompetitionService().list(page, size);
		return Action.SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() throws Exception {
		loadEvent(getEventId());
		return Action.SUCCESS;
	}
	
	/**
	 * @return
	 */
	public String setScoreboardMode() {
		setSize(12);
		loadEvent(getEventId());
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
				log.debug("Loaded competition: {}", competition.getName());
			}
		} else {
			loadEvent(getEventId());
		}
	}
	
	/**
	 * @param eventId
	 */
	public void loadEvent(int eventId) {
		if (eventId > 0) {
			Event event = getEventService().find(eventId);
			if (event != null) {
				setEvent(event);
				setEventId(event.getId());
				log.debug("Loaded event: {}", event.getName());
				setResults(getEventService().getResults(event, page, 0));
				if (getCompetitionId() != null) {
					setRecords(getResultService().getRecords(getCompetitionId()));
				}
			}
		} else {
			if (getCompetitionId() != null) {
				Event event = getCompetitionService().findLiveEvent(getCompetitionId());
				if (event != null) {
					setEvent(event);
					setEventId(event.getId());
					log.debug("Loaded live event: {}", event.getName());
					setResults(getEventService().getResults(event, page, size));	
					setRecords(getResultService().getRecords(getCompetitionId()));
				}			
			}
		}
	}
	
	/**
	 * @param eventId
	 * @return
	 */
	public Event getEventById(int eventId) {
		return getEventService().find(eventId);
	}

}
