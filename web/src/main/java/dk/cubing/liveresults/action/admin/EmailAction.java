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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.annotation.Secured;

import au.com.bytecode.opencsv.CSVReader;

import com.opensymphony.xwork2.Action;

import dk.cubing.liveresults.action.FrontendAction;
import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.service.CompetitionService;

@Secured( { "ROLE_USER" })
public class EmailAction extends FrontendAction {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(EmailAction.class);
	
	private CompetitionService competitionService;
	
	private File csv;
	private String csvContentType;
	private String csvFileName;
	
	private List<Competition> competitions;
	private String competitionId;
	private Competition competition;
	
	private List<String> acceptedCompetitiors;
	private List<String> pendingCompetitiors;
	private boolean sendToAccepted = true;
	private boolean sendToPending = false;
	private String subject;
	private String body;
	
	public EmailAction() {
		this.acceptedCompetitiors = new ArrayList<String>();
		this.pendingCompetitiors = new ArrayList<String>();
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
	 * @return the csv
	 */
	public File getCsv() {
		return csv;
	}

	/**
	 * @param csv the csv to set
	 */
	public void setCsv(File csv) {
		this.csv = csv;
	}

	/**
	 * @return the csvContentType
	 */
	public String getCsvContentType() {
		return csvContentType;
	}

	/**
	 * @param csvContentType the csvContentType to set
	 */
	public void setCsvContentType(String csvContentType) {
		this.csvContentType = csvContentType;
	}

	/**
	 * @return the csvFileName
	 */
	public String getCsvFileName() {
		return csvFileName;
	}

	/**
	 * @param csvFileName the csvFileName to set
	 */
	public void setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
	}

	/**
	 * @param competitions the competitions to set
	 */
	public void setCompetitions(List<Competition> competitions) {
		this.competitions = competitions;
	}

	/**
	 * @return the competitions
	 */
	@Secured( { "ROLE_ADMIN" })
	public List<Competition> getCompetitions() {
		return competitions;
	}

	/**
	 * @param competitionId the competitionId to set
	 */
	public void setCompetitionId(String competitionId) {
		this.competitionId = competitionId;
	}

	/**
	 * @return the competitionId
	 */
	public String getCompetitionId() {
		return competitionId;
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
	
	/**
	 * @return the acceptedCompetitiors
	 */
	public List<String> getAcceptedCompetitiors() {
		return acceptedCompetitiors;
	}

	/**
	 * @param acceptedCompetitiors the acceptedCompetitiors to set
	 */
	public void setAcceptedCompetitiors(List<String> acceptedCompetitiors) {
		this.acceptedCompetitiors = acceptedCompetitiors;
	}

	/**
	 * @return the pendingCompetitiors
	 */
	public List<String> getPendingCompetitiors() {
		return pendingCompetitiors;
	}

	/**
	 * @param pendingCompetitiors the pendingCompetitiors to set
	 */
	public void setPendingCompetitiors(List<String> pendingCompetitiors) {
		this.pendingCompetitiors = pendingCompetitiors;
	}
	
	/**
	 * @return the sendToAccepted
	 */
	public boolean isSendToAccepted() {
		return sendToAccepted;
	}

	/**
	 * @param sendToAccepted the sendToAccepted to set
	 */
	public void setSendToAccepted(boolean sendToAccepted) {
		this.sendToAccepted = sendToAccepted;
	}

	/**
	 * @return the sendToPending
	 */
	public boolean isSendToPending() {
		return sendToPending;
	}

	/**
	 * @param sendToPending the sendToPending to set
	 */
	public void setSendToPending(boolean sendToPending) {
		this.sendToPending = sendToPending;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return
	 */
	public String parseCsv() {
		if (csv != null && competitionId != null) {
			Competition competition = getCompetitionService().find(competitionId);
			if (competition == null) {
				log.error("Could not load competition: {}", competitionId);
				return Action.ERROR;
			} else {
				setCompetition(competition);
				log.debug("Loaded competition: {}", competition.getName());
			}
			
			List<String> acceptedCompetitiors = new ArrayList<String>();
			List<String> pendingCompetitiors = new ArrayList<String>();
			
			// parse csv file
			try {
				CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csv), "ISO-8859-1"), ',');
				List<String[]> csvLines = reader.readAll();
				csvLines.remove(0); // first line contains column definition
				for (String[] line : csvLines) {
					String email = line[line.length-3];
					log.info("Email: {}", email);
					if ("a".equals(line[0])) { // accepted competitors
						acceptedCompetitiors.add(email);
					} else { // pending competitors
						pendingCompetitiors.add(email);
					}
				}
				setAcceptedCompetitiors(acceptedCompetitiors);
				setPendingCompetitiors(pendingCompetitiors);
				setSendToAccepted(true);
				setSendToPending(false);
                setBody("");
                setSubject("");
				return Action.SUCCESS;
			} catch (Exception e) {
				log.error(e.getLocalizedMessage(), e);
			}
		}
		return Action.INPUT;
	}
	
	/**
	 * @return
	 */
	public String sendEmail() {
		if (getCompetition() != null) {
			try {
				SimpleEmail email = new SimpleEmail();
				email.setCharset(SimpleEmail.ISO_8859_1);
				email.setHostName(getText("email.smtp.server"));
				if (!getText("email.username").isEmpty() && !getText("email.password").isEmpty()) {
					email.setAuthentication(getText("email.username"), getText("email.password"));
				}
				email.setSSL("true".equals(getText("email.ssl")));
				email.setSubject(getSubject());
				email.setMsg(getBody());
				email.setFrom(getCompetition().getOrganiserEmail(), getCompetition().getOrganiser());
				email.addBcc(getCompetition().getWcaDelegateEmail(), getCompetition().getWcaDelegate());
				if (isSendToAccepted()) {
					for (String toAddress : getAcceptedCompetitiors()) {
						email.addBcc(toAddress);
					}
				}
				if (isSendToPending()) {
					for (String toAddress : getPendingCompetitiors()) {
						email.addBcc(toAddress);
					}
				}
				email.send();
				return Action.SUCCESS;
			} catch (Exception e) {
				log.error("Could not send email upon competition creation!", e);
				return Action.ERROR;
			}
		} else {
			log.error("Could not load competition!");
			return Action.ERROR;
		}
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
		return Action.SUCCESS;
	}
}
