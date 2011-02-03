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
package dk.cubing.liveresults.resultsbot.engine;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import net.sourceforge.jnetcube.Scramble;
import net.sourceforge.jnetcube.Scramble2x2x2;
import net.sourceforge.jnetcube.Scramble3x3x3;
import net.sourceforge.jnetcube.Scramble4x4x4;
import net.sourceforge.jnetcube.Scramble5x5x5;
import net.sourceforge.jnetcube.Scramble6x6x6;
import net.sourceforge.jnetcube.Scramble7x7x7;
import net.sourceforge.jnetcube.ScrambleClock;
import net.sourceforge.jnetcube.ScrambleMegaminx;
import net.sourceforge.jnetcube.ScramblePyraminx;
import net.sourceforge.jnetcube.ScrambleSquare1;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.cubing.liveresults.jms.Consumer;
import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.model.Event;
import dk.cubing.liveresults.model.Result;
import dk.cubing.liveresults.service.CompetitionService;
import dk.cubing.liveresults.service.EventService;
import dk.cubing.liveresults.utilities.ResultTimeFormat;

public class ResultsBot extends PircBot implements Runnable, MessageListener {
	
	private static final Logger log = LoggerFactory.getLogger(ResultsBot.class);
	
	private String name;
	private String server;
	private String channel;
	private String password;
	
	private Consumer consumer;
	private CompetitionService competitionService;
	private EventService eventService;

	private SimpleDateFormat sdf;
	private ResultTimeFormat timeFormat;
	
	private Map<String, Scramble> scramblers = new HashMap<String, Scramble>();
	
	/**
	 * @param name
	 * @param server
	 * @param channel
	 */
	public ResultsBot(String name, String server, String channel, String password) {
		this.name = name;
		this.server = server;
		this.channel = channel;
		this.password = password;
		
		setName(this.name);
		setLogin(getName());
		
		sdf = new SimpleDateFormat("HH:mm.ss");
		timeFormat = new ResultTimeFormat();
		
		scramblers.put("222", new Scramble2x2x2());
		scramblers.put("333", new Scramble3x3x3());
		scramblers.put("444", new Scramble4x4x4());
		scramblers.put("555", new Scramble5x5x5());
		scramblers.put("666", new Scramble6x6x6());
		scramblers.put("777", new Scramble7x7x7());
		scramblers.put("py", new ScramblePyraminx());
		scramblers.put("mega", new ScrambleMegaminx());
		scramblers.put("sq1", new ScrambleSquare1());
		scramblers.put("clk", new ScrambleClock());
	}

	/**
	 * @return the consumer
	 */
	public Consumer getConsumer() {
		return consumer;
	}

	/**
	 * @param consumer the consumer to set
	 */
	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
		this.consumer.setMessageListener(this);
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

	public void run() {
		log.info("Starting ResultsBot...");
		setVerbose(true);
		try {
			connect(server);
		} catch (NickAlreadyInUseException e) {
			log.error("Please choose a different name.", e);
		} catch (IOException e) {
			log.error("Network error!", e);
		} catch (IrcException e) {
			log.error("Irc error!", e);
		}
		if (isConnected()) {
			joinChannel(channel);
		}
		log.info("Engine started.");
	}
	
	/**
	 * Basic cleanup method 
	 */
	public void shutdown() {
		log.info("Shutting down ResultsBot...");
		if (getConsumer() != null) {
			getConsumer().shutdown();
		}
		if (isConnected()) {
			partChannel(channel, "See ya!");
			disconnect();
		}
		log.info("ResultsBot shutdown completed.");
	}
	
	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onDisconnect()
	 */
	@Override
	protected void onDisconnect() {
		super.onDisconnect();
		while (!isConnected()) {
			try {
				reconnect();
			} catch (Exception e) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					log.error("Can't wait?!", e1);
					System.exit(0);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onKick(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
		super.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason);
		if (recipientNick.equalsIgnoreCase(getNick())) {
		    joinChannel(channel);
		}
	}

	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
            TextMessage txtMessage = (TextMessage)message;
            try {
            	String competitionId = txtMessage.getText();
            	if (competitionId != null) {
            		Competition competition = getCompetitionService().find(competitionId);
            		Event event = getCompetitionService().findLiveEvent(competitionId);
            		if (competition != null && event != null) {
            			String topic = "Results for " + competition.getName() + " was updated at " + sdf.format(new Date());
            			log.debug("debug: {}", topic);
		            	setTopic(channel, topic);
            			sendResultsByEvent(event);
            		} else {
            			log.error("Could not load live event: {}", competitionId);
            		}
            	} else {
            		log.error("Did not receive a valid competition id!");
            	}
			} catch (JMSException e) {
				log.error("JMS Error!", e);
			}
		}
	}

	/**
	 * @param event
	 */
	private void sendResultsByEvent(Event event) {
		sendMessage(channel, "Current top 3 in the " + event.getName() + " event:");
		int rank = 1;
		for (Result result : getEventService().getWinners(event)) {
			String msg = (rank++) + ". " + result.getFirstname() + " " + result.getSurname() + " ";
			if (Event.Format.AVERAGE.getValue().equals(event.getFormat()) || Event.Format.MEAN.getValue().equals(event.getFormat())) {
				msg += timeFormat.format(result.getAverage(), event.getTimeFormat());
			} else {
				msg += timeFormat.format(result.getBest(), event.getTimeFormat());
			}
			log.debug("Msg: {}", msg);
			sendMessage(channel, msg);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		super.onMessage(channel, sender, login, hostname, message);
		if (message.equalsIgnoreCase(".help")) {
			sendMessage(channel, "Commands:");
			sendMessage(channel, ".help");
			sendMessage(channel, ".list, shows a list of competitions.");
			sendMessage(channel, ".top, shows the top-3 for the latest live event.");
			sendMessage(channel, ".scrambles, lists supported scrambles.");
		} else if (message.equalsIgnoreCase(".list")) {
			for (Competition competition : getCompetitionService().list(1, 3)) {
				String info = competition.getName();
				sendMessage(channel, info);	
			}
		} else if (message.equalsIgnoreCase(".top")) {
			for (Competition competition : getCompetitionService().list(1, 1)) {
				sendMessage(channel, "Results for: " + competition.getName());
				Event event = getCompetitionService().findLiveEvent(competition.getCompetitionId());
				sendResultsByEvent(event);
			}
		} else if (message.equalsIgnoreCase(".scrambles")) {
			sendMessage(channel, "Supported scramblers:");
			for (String key : scramblers.keySet()) {
				Scramble scramble = scramblers.get(key);
				sendMessage(channel, "." + key + ", " + scramble.getName());
			}
		} else if (message.startsWith(".")) {
			for (String key : scramblers.keySet()) {
				if (message.equalsIgnoreCase("." + key)) {
					Scramble scramble = scramblers.get(key);
					sendMessage(channel, scramble.getName() + ": " + scramble.generateScramble());
					break;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.jibble.pircbot.PircBot#onPrivateMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onPrivateMessage(String sender, String login, String hostname, String message) {
		super.onPrivateMessage(sender, login, hostname, message);
		StringTokenizer tok = new StringTokenizer(message, " ");
		if (tok.hasMoreTokens()) {
			String cmd = tok.nextToken();
			String password = "";
			if (tok.hasMoreTokens()) {
				password = tok.nextToken();
			}
			if (this.password.equals(password)) {
				if (cmd.equalsIgnoreCase(".op")) {
					op(channel, sender);
				} else if (cmd.equalsIgnoreCase(".rejoin")) {
					partChannel(channel, "brb");
					joinChannel(channel);
				} else if (cmd.equalsIgnoreCase(".quit")) {
					shutdown();
				}
			}
		}
	}
}
