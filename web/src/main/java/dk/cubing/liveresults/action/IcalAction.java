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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.SimpleHostInfo;
import net.fortuna.ical4j.util.UidGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;

import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.service.CompetitionService;

public class IcalAction extends FrontendAction {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(IcalAction.class);
	
	private CompetitionService competitionService;
	private ByteArrayOutputStream out;
	
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

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.action.FrontendAction#list()
	 */
	@Override
	public String list() {
		
		// generate calendar
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		for (Competition competition : getCompetitionService().list(page, size)) {
			log.debug("iCal event for: {}", competition.getName());
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(competition.getEndDate());
            cal.add(java.util.Calendar.DATE, 1);
			VEvent event = new VEvent(
					new Date(competition.getStartDate()),
					new Date(cal.getTime()),
					competition.getName()
			);
			UidGenerator ug = new UidGenerator(new SimpleHostInfo("live.speedcubing.dk"), "uidGen");
			event.getProperties().add(ug.generateUid());
			calendar.getComponents().add(event);
		}
		
		// generate output
		CalendarOutputter outputter = new CalendarOutputter();
		try {
			log.debug("Generate calendar stream");
			out = new ByteArrayOutputStream();
			outputter.output(calendar, out);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return Action.ERROR;
		}
		return Action.SUCCESS;
	}

	/**
	 * @return
	 */
	public InputStream getInputStream() {
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	/**
	 * @return
	 */
	public int getContentLength() {
		return out.size();
	}
}
