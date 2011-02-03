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

import java.util.Date;
import java.util.List;

import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.annotation.Secured;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetailsManager;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;

import dk.cubing.liveresults.action.FrontendAction;
import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.service.CompetitionService;
import dk.cubing.liveresults.utilities.CountryUtil;

@Secured( { "ROLE_ADMIN" })
public class CompetitionAction extends FrontendAction implements Preparable {

private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(CompetitionAction.class);
	
	private CompetitionService competitionService;
	private UserDetailsManager userDetailsManager;
	private PasswordEncoder passwordEncoder;
	private List<Competition> competitions;
	private String competitionId;
	private String password;
	private boolean enabled = true;
	private Competition competition;
	private CountryUtil countryUtil;
	
	/**
	 * Basic constructor
	 */
	public CompetitionAction() {
		super();
		this.setCountryUtil(new CountryUtil());
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
	private CompetitionService getCompetitionService() {
		return competitionService;
	}

	/**
	 * @param userDetailsManager the userDetailsManager to set
	 */
	public void setUserDetailsManager(UserDetailsManager userDetailsManager) {
		this.userDetailsManager = userDetailsManager;
	}

	/**
	 * @return the userDetailsManager
	 */
	public UserDetailsManager getUserDetailsManager() {
		return userDetailsManager;
	}

	/**
	 * @param passwordEncoder the passwordEncoder to set
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * @return the passwordEncoder
	 */
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	/**
	 * @return the competitions
	 */
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
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
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
		return Action.INPUT;
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
			log.debug("New competition");
			competition = new Competition();
			competition.setStartDate(new Date());
			competition.setEndDate(new Date());
		}
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		Competition competition = getCompetition();
		if (competition.getCountry().length() != 2) {
			competition.setCountry(getCountryUtil().getCountryCodeByName(competition.getCountry()));
		} else if (getCountryUtil().getCountryByCode(competition.getCountry()) != null) {
			competition.setCountry(competition.getCountry().toUpperCase());
		}
		if ("".equals(competition.getWebsite())) {
			competition.setWebsite(null);
		}
		User user = new User(
				competition.getCompetitionId(), 
				getPasswordEncoder().encodePassword(getPassword(), null), 
				isEnabled(), true, true, true, 
				new GrantedAuthority[]{new GrantedAuthorityImpl("ROLE_USER")}
		);
		if (getUserDetailsManager().userExists(competition.getCompetitionId())) {
			log.info("Updating user: {}", competition.getCompetitionId());
			getUserDetailsManager().updateUser(user);
			getCompetitionService().update(competition);
		} else {
			log.info("Creating user: {}", competition.getCompetitionId());
			getUserDetailsManager().createUser(user);
			getCompetitionService().create(competition);
			// send mail to organiser.
			if (competition.getOrganiserEmail() != null && !competition.getOrganiserEmail().isEmpty()) {
				log.info("Sending login information to new user: {}", competition.getCompetitionId());
				try {
					SimpleEmail email = new SimpleEmail();
					email.setCharset(SimpleEmail.ISO_8859_1);
					email.setHostName(getText("email.smtp.server"));
					if (!getText("email.username").isEmpty() && !getText("email.password").isEmpty()) {
						email.setAuthentication(getText("email.username"), getText("email.password"));
					}
					email.setSSL("true".equals(getText("email.ssl")));
					email.setSubject(getText("competitions.email.subject", new String[]{competition.getName()}));
					email.setMsg(getText("competitions.email.message", new String[]{
							competition.getCompetitionId(), 
							getPassword()
					}));
					email.setFrom(getText("competitions.email.senderEmail"), getText("competitions.email.sender"));
					email.addBcc(getText("competitions.email.senderEmail"), getText("competitions.email.sender"));
					email.addTo(competition.getOrganiserEmail(), competition.getOrganiser());
					email.send();
				} catch (Exception e) {
					log.error("Could not send email upon competition creation!", e);
				}
			}
		}
		return Action.SUCCESS;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		if (getUserDetailsManager().userExists(getCompetition().getCompetitionId())) {
			log.info("Deleting user: {}", getCompetition().getCompetitionId());
			getUserDetailsManager().deleteUser(getCompetition().getCompetitionId());
			getCompetitionService().delete(getCompetition());
		}
		return Action.SUCCESS;
	}
	
}
