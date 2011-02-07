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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.servlet.ServletContextURIResolver;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.annotation.Secured;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.opensymphony.xwork2.Action;

import dk.cubing.liveresults.action.FrontendAction;
import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.model.Event;
import dk.cubing.liveresults.service.CompetitionService;
import dk.cubing.liveresults.service.EventService;
import dk.cubing.liveresults.utilities.ResultTimeFormat;

@Secured( { "ROLE_USER" })
public class DiplomasAction extends FrontendAction {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(DiplomasAction.class);
	
	private CompetitionService competitionService;
	private EventService eventService;
	private DOMImplementation docImplementation;
	private ServletContextURIResolver uriResolver;
	private FopFactory fopFactory;
	private TransformerFactory tFactory;
	private Map<String, String> templates = new LinkedHashMap<String, String>();
	private String template;
	private String competitionId;
	private int eventId;
	private ByteArrayOutputStream out;
	private List<Competition> competitions;
	private ResultTimeFormat resultTimeFormat;
	private boolean danishDiplomas = false;
	
	public DiplomasAction() {
		super();
		try {
			// define templates
			templates.put("/xsl/dsf.xsl", "Danish Speedcubing Association");
			templates.put("/xsl/dsf_danish.xsl", "Dansk Speedcubing Forening");
			templates.put("/xsl/nca.xsl", "Norwegian Cube Association");
			templates.put("/xsl/svekub.xsl", "SveKub");
			
			docImplementation = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
			fopFactory = FopFactory.newInstance();
			tFactory = TransformerFactory.newInstance();
			resultTimeFormat = new ResultTimeFormat();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
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
	 * @param eventService the eventService to set
	 */
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	/**
	 * @return the eventService
	 */
	public EventService getEventService() {
		return eventService;
	}

	/**
	 * @param templates the templates to set
	 */
	public void setTemplates(Map<String, String> templates) {
		this.templates = templates;
	}

	/**
	 * @return the templates
	 */
	public Map<String, String> getTemplates() {
		return templates;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
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
	 * @param eventId the eventId to set
	 */
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the eventId
	 */
	public int getEventId() {
		return eventId;
	}

	/**
	 * @param competitionId
	 * @return
	 */
	public List<Event> getEvents(String competitionId) {
		List<Event> events = new ArrayList<Event>();
		if (competitionId != null) {
			Competition competition = getCompetitionService().find(competitionId);
			if (competition != null) {
				for (Event event : competition.getEvents()) {
					if (event.getName().toLowerCase().contains("final")) {
						events.add(event);
					}
				}
			}
		}
		return events;
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
	 * @param danishDiplomas the danishDiplomas to set
	 */
	public void setDanishDiplomas(boolean danishDiplomas) {
		this.danishDiplomas = danishDiplomas;
	}

	/**
	 * @return the danishDiplomas
	 */
	public boolean isDanishDiplomas() {
		return danishDiplomas;
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
	
	/**
	 * @return
	 */
	public String getContentDisposition() {
		return "attachment; filename=" + getCompetitionId() + ".pdf";
	}

	/**
	 * @param competitionId 
	 * @param eventId 
	 * @return
	 */
	private DOMSource generateXML(Competition competition) {
		Document doc = docImplementation.createDocument(null, "diplomas", null);
		Element rootElement = doc.getDocumentElement();
		rootElement.setAttribute("competitionId", competitionId);
		
		// competition data
		Element competitionName = doc.createElement("competitionName");
		competitionName.appendChild(doc.createTextNode(competition.getName()));
		rootElement.appendChild(competitionName);
		boolean isDanishTemplate = "/xsl/dsf_danish.xsl".equals(getTemplate());
		boolean isDanishOpen = competition.getCompetitionId().startsWith("DanishOpen");
		
		Element organiser = doc.createElement("organiser");
		organiser.appendChild(doc.createTextNode(competition.getOrganiser()));
		rootElement.appendChild(organiser);
		
		Element wcaDelegate = doc.createElement("wcaDelegate");
		wcaDelegate.appendChild(doc.createTextNode(competition.getWcaDelegate()));
		rootElement.appendChild(wcaDelegate);
		
		log.info("Template: {}", getTemplate());
		
		// selected event
		if (getEventId() != -1) {
			Event event = getEventService().find(getEventId());
			if (event != null) {
				generateEventXML(doc, rootElement, event, isDanishTemplate, isDanishOpen);
			}
			
		// all events
		} else {
			for (Event event : competition.getEvents()) {
				if (event.getName().toLowerCase().contains("final")) {
					generateEventXML(doc, rootElement, event, isDanishTemplate, isDanishOpen);
				}
			}
		}
		if (doc.hasChildNodes()) {
			return new DOMSource(doc);
		} else {
			return null;
		}
	}

	/**
	 * @param doc
	 * @param rootElement
	 * @param event
	 * @param isDanishTemplate
	 * @param isDanishOpen
	 */
	private void generateEventXML(Document doc, Element rootElement, Event event, boolean isDanishTemplate, boolean isDanishOpen) {
		int rank = 1;
		for (dk.cubing.liveresults.model.Result result : getEventService().getWinners(event, isDanishDiplomas() ? "DK" : null)) {
			Element diploma = doc.createElement("diploma");
			
			// event data
			Element eventName = doc.createElement("eventName");
			eventName.appendChild(doc.createTextNode(formatEventName(event.getName())));
			diploma.appendChild(eventName);
			
			boolean isTeamEvent = event.getName().toLowerCase().contains("team");
			Element eventTeam = doc.createElement("teamEvent");
			eventTeam.appendChild(doc.createTextNode(isTeamEvent ? "TRUE" : "FALSE"));
			diploma.appendChild(eventTeam);
			
			if (isTeamEvent) {
				String firstnames[] = result.getFirstname().split(" / ");
				String surnames[] = result.getSurname().split(" / ");
				String fullName1 = firstnames[0] + " " + surnames[0];
				String fullName2 = firstnames[1] + " " + surnames[1];
				Element competitor1Name = doc.createElement("competitor1Name");
				Element competitor2Name = doc.createElement("competitor2Name");
				competitor1Name.appendChild(doc.createTextNode(fullName1));
				competitor2Name.appendChild(doc.createTextNode(fullName2));
				diploma.appendChild(competitor1Name);
				diploma.appendChild(competitor2Name);
			} else {
				Element competitorName = doc.createElement("competitorName");
				competitorName.appendChild(doc.createTextNode(result.getFirstname() + " " + result.getSurname()));
				diploma.appendChild(competitorName);
			}
			
			Element rankNode = doc.createElement("rank");
			rankNode.appendChild(doc.createTextNode(isDanishTemplate ? resultTimeFormat.formatDanishRank(rank, isDanishOpen) : resultTimeFormat.formatRank(rank)));
			diploma.appendChild(rankNode);
			
			Element resultNode = doc.createElement("result");
			// format result
			String resultStr = "";
			if (Event.Format.AVERAGE.getValue().equals(event.getFormat()) || Event.Format.MEAN.getValue().equals(event.getFormat())) {
				resultStr = resultTimeFormat.format(result.getAverage(), event.getTimeFormat());
				if (Event.Format.AVERAGE.getValue().equals(event.getFormat())) {
					resultStr += isDanishTemplate ? " gennemsnit af 5" : " average of 5";
				} else if (Event.Format.MEAN.getValue().equals(event.getFormat())) {
					resultStr += isDanishTemplate ? " gennemsnit af 3" : " mean of 3";
				}
			} else {
				resultStr = resultTimeFormat.format(result.getBest(), event.getTimeFormat());
			}
			resultNode.appendChild(doc.createTextNode(resultStr));
			diploma.appendChild(resultNode);
			
			rootElement.appendChild(diploma);
			rank++;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() throws Exception {
		if (getCompetitionId() != null) {
			Competition competition = getCompetitionService().find(competitionId);
			if (competition != null) {
				try {
					out = new ByteArrayOutputStream();
					uriResolver = new ServletContextURIResolver(ServletActionContext.getServletContext());
					fopFactory.setURIResolver(uriResolver);
					tFactory.setURIResolver(uriResolver);
					
				    //Setup FOP
				    Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
			
				    //Setup Transformer
				    Source xsltSrc = uriResolver.resolve("servlet-context:" + getTemplate(), null);
				    Transformer transformer = tFactory.newTransformer(xsltSrc);
				    transformer.setURIResolver(uriResolver);
			
				    //Make sure the XSL transformation's result is piped through to FOP
				    Result res = new SAXResult(fop.getDefaultHandler());
			
				    //Setup input
				    Source xmlSrc = generateXML(competition);
				    
				    //Start the transformation and rendering process
				    if (xmlSrc != null) {
					    transformer.transform(xmlSrc, res);
					    return Action.SUCCESS;
					    
					// argh, error!
				    } else {
				    	return Action.ERROR;
				    }
				} catch (Exception e) {
					log.error(e.getLocalizedMessage(), e);
				}
			} else {
				log.error("Could not find competition: {}", competitionId);
		    	return Action.ERROR;
			}
		}
		return Action.INPUT;
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.action.FrontendAction#list()
	 */
	@Override
	public String list() {
		setCompetitions(getCompetitionService().list(page, size));
		setEventId(-1);
		setDanishDiplomas(false);
		return Action.SUCCESS;
	}

	/**
	 * @param eventName
	 * @return
	 */
	private String formatEventName(String eventName) {
		if (eventName.lastIndexOf("-") != -1) {
			return eventName.substring(0, eventName.lastIndexOf("-")).trim();
		} else {
			return eventName;
		}
	}
}
