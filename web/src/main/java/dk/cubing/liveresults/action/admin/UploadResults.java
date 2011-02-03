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
package dk.cubing.liveresults.action.admin;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.annotation.Secured;

import com.opensymphony.xwork2.Action;

import dk.cubing.liveresults.action.FrontendAction;
import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.service.CompetitionService;
import dk.cubing.liveresults.uploader.parser.ExcelParser;
import dk.cubing.liveresults.uploader.parser.ResultsFileParser;
import dk.cubing.liveresults.uploader.parser.ResultsFileParserException;

@Secured( { "ROLE_USER" })
public class UploadResults extends FrontendAction {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(UploadResults.class);
	
	private CompetitionService competitionService;
	private final ResultsFileParser parser;
	
	private File xls;
	private String xlsContentType;
	private String xlsFileName;

	private List<Competition> competitions;
	private String competitionId;
	private Competition competition;
	
	public UploadResults() {
		super();
		parser = new ResultsFileParser();
	}

	/**
	 * @param competitionService the competitionService to set
	 */
	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	/**
	 * @return the competitionService
	 */
	public CompetitionService getCompetitionService() {
		return competitionService;
	}

	/**
	 * @param parser
	 */
	public void setParser(ExcelParser parser) {
		this.parser.setParser(parser);
	}

	/**
	 * @return the xls
	 */
	public File getXls() {
		return xls;
	}

	/**
	 * @param xls the xls to set
	 */
	public void setXls(File xls) {
		this.xls = xls;
	}

	/**
	 * @return the xlsContentType
	 */
	public String getXlsContentType() {
		return xlsContentType;
	}

	/**
	 * @param xlsContentType the xlsContentType to set
	 */
	public void setXlsContentType(String xlsContentType) {
		this.xlsContentType = xlsContentType;
	}

	/**
	 * @return the xlsFileName
	 */
	public String getXlsFileName() {
		return xlsFileName;
	}

	/**
	 * @param xlsFileName the xlsFileName to set
	 */
	public void setXlsFileName(String xlsFileName) {
		this.xlsFileName = xlsFileName;
	}

	/**
	 * @return the competitions
	 */
	@Secured( { "ROLE_ADMIN" })
	public List<Competition> getCompetitions() {
		return competitions;
	}

	/**
	 * @param competitions the competitions to set
	 */
	public void setCompetitions(List<Competition> competitions) {
		this.competitions = competitions;
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
	 * @param competition the competition to set
	 */
	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	/**
	 * @return the competition
	 */
	public Competition getCompetition() {
		return competition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() throws Exception {
		return Action.SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.action.FrontendAction#list()
	 */
	@Override
	public String list() {
		setCompetitions(getCompetitionService().list(page, size));
		setCompetition(null);
		return Action.SUCCESS;
	}

	/**
	 * @return
	 */
	public String parseResults() {
		if (xls != null && competitionId != null) {
			Competition competition = null;
			
			// load competition
			competition = getCompetitionService().find(competitionId);
			if (competition == null) {
				log.error("Could not load competition: {}", competitionId);
				return Action.ERROR;
			}
			
			// parse results
			try {
				log.debug("Parsing result file: {}", xls.getName());
				competition = parser.parse(competition, xls.getAbsolutePath());
			} catch (ResultsFileParserException e) {
				log.error("Selected spreadsheet does not appear to be based on the WCA template!", e);
				return Action.ERROR;
			} catch (IllegalStateException e) {
				log.warn("Unexpected cell format.", e);
				return Action.ERROR;
			}
			
			// save competition
			if (competition != null) {
				try {
					getCompetitionService().update(competition);
					setCompetition(competition);
				} catch (Exception e) {
					log.error("Could not save competition!", e);
					return Action.ERROR;
				}
				return Action.SUCCESS;
			}
		}
		return Action.INPUT;
	}
	
}
