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
package dk.cubing.liveresults.uploader.engine;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;

import dk.cubing.liveresults.model.Event;
import dk.cubing.liveresults.model.Result;
import dk.cubing.liveresults.webservice.wca.InvalidCountryException;
import dk.cubing.liveresults.webservice.wca.InvalidEventException;
import dk.cubing.liveresults.webservice.wca.WcaResults;
import org.apache.commons.jci.monitor.FilesystemAlterationMonitor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.security.BadCredentialsException;

import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.uploader.LoggerWrapper;
import dk.cubing.liveresults.uploader.LoggerWrapperFactory;
import dk.cubing.liveresults.uploader.configuration.Configuration;
import dk.cubing.liveresults.uploader.gui.MenuBar;
import dk.cubing.liveresults.uploader.gui.PreferencesPanel;
import dk.cubing.liveresults.uploader.gui.StatusPanel;
import dk.cubing.liveresults.uploader.parser.ExcelParser;
import dk.cubing.liveresults.uploader.parser.ResultsFileParser;
import dk.cubing.liveresults.uploader.parser.ResultsFileParserException;
import dk.cubing.liveresults.webservice.CompetitionNotFoundException;
import dk.cubing.liveresults.webservice.CompetitionSaveException;
import dk.cubing.liveresults.webservice.LiveResults;
import dk.cubing.liveresults.webservice.UnsupportedClientVersionException;

public class ResultsEngine implements Runnable {
	
	private static final LoggerWrapper log = LoggerWrapperFactory.getInstance().getLogger(ResultsEngine.class);
	
	public static ResourceBundle resources = ResourceBundle.getBundle("LiveResults");
	
	private final Configuration config;
	private final FilesystemAlterationMonitor fam;
	private ResultsFileChangeListener pListener = null;
	private final ResultsFileParser parser;
    private String endpoint;
    private String endpointWca;
	private LiveResults client;
    private WcaResults clientWca;
	private String clientVersion;
	private boolean isRunning = false;
	private boolean isShuttingDown = false;
	private JFrame frame = null;
	private StatusPanel statusPanel = null;
	
	public ResultsEngine() {
		config = new Configuration(this);
		fam = new FilesystemAlterationMonitor();
		parser = new ResultsFileParser();
        endpoint = getConfig().getWebserviceEndpoint();
        endpointWca = getConfig().getWcaWebserviceEndpoint();
	}
	
	/**
	 * @param parser
	 */
	public void setParser(ExcelParser parser) {
		this.parser.setParser(parser);
	}
	
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
	 * @return the config
	 */
	public Configuration getConfig() {
		return config;
	}
	
	/**
	 * @return the client
	 */
	public LiveResults getClient() {
		// lazy init and endpoint reloading
		if (client == null || !getConfig().getWebserviceEndpoint().equals(endpoint)) {
			try {
                endpoint = getConfig().getWebserviceEndpoint();
                JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
				factory.setServiceClass(LiveResults.class);
				factory.setAddress(endpoint);
				client = (LiveResults) factory.create();
			} catch (Exception e) {
				log.error("Could not create webservice client", e);
			}
		}
		return client;
	}

    /**
	 * @return the client
	 */
	public WcaResults getWcaClient() {
		// lazy init and endpoint reloading
		if (clientWca == null || !getConfig().getWcaWebserviceEndpoint().equals(endpointWca)) {
			try {
                endpointWca = getConfig().getWcaWebserviceEndpoint();
                JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
				factory.setServiceClass(WcaResults.class);
				factory.setAddress(endpointWca);
				clientWca = (WcaResults) factory.create();
			} catch (Exception e) {
				log.error("Could not create wca webservice client", e);
			}
		}
		return clientWca;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		// display swing gui?
		if (getConfig().doShowGUI()) {
			try {
				javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				    public void run() {
				    	createAndShowGUI();
				    }
				});
			} catch (Exception e) {
				log.error(e.getLocalizedMessage(), e);
			}
		}
		
		log.info("Starting ResultsEngine...");
		setProgress(2);
		if (isSupportedVersion()) {
			if (!getConfig().isConfigured()) {
				if (getConfig().doShowGUI()) {
					createAndShowPreferencesDialog();
				} else {
					log.warn("ResultsEngine are not configured!");
				}
			}
			while (!isRunning && !isShuttingDown) {
				if (getConfig().isConfigured()) {
					setProgress(4);
					pListener = new ResultsFileChangeListener(this);
					fam.addListener(getConfig().getResultsFile(), pListener);
					fam.start();
					isRunning = true;
					log.info("Engine started.");
					setProgress(6);
					uploadResults(getConfig().getResultsFile());
				} else {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						log.error(e.getLocalizedMessage(), e);
					}
				}
			}
		}
	}
	
	/**
	 * @param resultsFile
	 */
	public void uploadResults(File resultsFile) {
		setProgress(1);

        LiveResults client = getClient();
		Competition competition = null;
		
		// load competition
		try {
			log.info("Loading competition: {}", getConfig().getCompetitionId());
			competition = client.loadCompetition(
					getConfig().getCompetitionId(),
					getConfig().getPassword());
			setProgress(2);
		} catch (BadCredentialsException e) {
			log.error("Invalid CompetitionId and/or Password. CompetitionId: '{}', Password: '{}'", getConfig().getCompetitionId(), getConfig().getPassword());
		} catch (CompetitionNotFoundException e) {
			log.error("Could not load competition: {}", getConfig().getCompetitionId(), e);
		}
		
		// parse results spreadsheet
		if (competition != null) {
			try {
				log.info("Parsing result file: {}", resultsFile.getName());
				competition = parser.parse(competition, resultsFile.getAbsolutePath());
				setProgress(4);
			} catch (ResultsFileParserException e) {
				log.error("Selected spreadsheet does not appear to be based on the WCA template!", e);
			} catch (IllegalStateException e) {
				log.warn("Unexpected cell format.", e);
			}
		}

        // check for records
        if (competition != null) {
            log.info("Checking for records: {}", competition.getName());
            competition = checkForRecords(competition);
            setProgress(5);
        }
		
		// upload results
		if (competition != null) {
			try {
				log.info("Saving results: {}", competition.getName());
				client.saveCompetition(
						getConfig().getCompetitionId(),
						getConfig().getPassword(),
						competition);
				setProgress(6);
				log.info("Saved results.");
			} catch (BadCredentialsException e) {
				log.error("Invalid CompetitionId and/or Password. CompetitionId: '{}', Password: '{}'", getConfig().getCompetitionId(), getConfig().getPassword());
			} catch (CompetitionNotFoundException e) {
				log.error("Could not load competition: {}", getConfig().getCompetitionId(), e);
			} catch (CompetitionSaveException e) {
				log.error("Could not save competition: {}", getConfig().getCompetitionId(), e);
			}
		}
	}

    /**
	 * Restart engine (config has been changed)
	 */
	public void restart() {
		if (getConfig().isConfigured() && pListener != null) {
			setProgress(2);
			fam.removeListener(pListener);
			setProgress(4);
			fam.addListener(getConfig().getResultsFile(), pListener);
			log.info("Engine restarted.");
			setProgress(6);
			uploadResults(getConfig().getResultsFile());
		} else {
			log.debug("Restart was not possible at this moment.");
		}
	}
	
	/**
	 * Shutdown engine and file monitor if running
	 */
	public void shutdown() {
		setProgress(3);
		log.info("Shutting down ResultsEngine...");
		isShuttingDown = true;
		isRunning = false;
		try {
		    fam.stop();
		// workaround for https://issues.apache.org/jira/browse/JCI-62
		} catch (NullPointerException e) {}
		log.info("ResultsEngine shutdown completed.");
		setProgress(6);
	}

	/**
	 * @return
	 */
	public boolean isSupportedVersion() {
		boolean isSupported = false;
		log.info("Checking for updates...");
		try {
			isSupported = getClient().isSupportedVersion(getClientVersion());
		} catch (UnsupportedClientVersionException e) {
			log.error("There is a new version available. Please upgrade.", e);
		} catch (Exception e) {
			log.error("Could not init web client. This could be a connection problem. Endpoint: {}", getConfig().getWebserviceEndpoint());
		}
		if (isSupported) {
			log.info("No updates available.");
		}
		return isSupported;
	}

    /**
     * Checks entire competition for unmarked records.
     * @param competition
     * @return
     */
    private Competition checkForRecords(Competition competition) {
        try {
            WcaResults client = getWcaClient();
            for (Event event : competition.getEvents()) {
                log.debug("Checking '{}' for records", event.getName());
                String format = event.getFormat();
                List<Result> results = event.getResults();
                String country = null;

                // sort by single result
                Collections.sort(results, new Comparator<Result>() {
                    @Override
                    public int compare(Result r1, Result r2) {
                        return r1.getBest() - r2.getBest();
                    }
                });
                
                // check for single records
                String singleRecord = null;
                for (Result result : results) {
                    // only check "real" results
                    if (result.getBest() != Result.Penalty.DNF.getValue()
                            && result.getBest() != Result.Penalty.DNS.getValue()) {
                        // only need to check 1 result for each country
                        if (country != result.getCountry()) {
                            country = result.getCountry();
                            log.debug("Checking single result record for: {} {} in {}", new Object[]{result.getFirstname(), result.getSurname(), event.getName()});
                            singleRecord = client.getSingleRecordType(getEventId(event), country, result.getBest());
                            if (singleRecord != null) {
                                if (result.getRegionalSingleRecord() == null) {
                                    log.warn("Found a single {} for: {} {} in {}", new Object[]{singleRecord, result.getFirstname(), result.getSurname(), event.getName()});
                                } else if (result.getRegionalSingleRecord() != singleRecord) {
                                    log.warn("Not a single {} but a {} for: {} {} in {}", new Object[]{result.getRegionalSingleRecord(), singleRecord, result.getFirstname(), result.getSurname(), event.getName()});
                                }
                            } else if (result.getRegionalSingleRecord() != null) {
                                log.warn("Not a single record for: {} {} in {}", new Object[]{result.getFirstname(), result.getSurname(), event.getName()});
                            }
                        } else {
                            singleRecord = null;
                        }
                    }
                    result.setRegionalSingleRecord(singleRecord);
                }
                country = null;

                // check of average records
                if (Event.Format.AVERAGE.getValue().equals(format) || Event.Format.MEAN.getValue().equals(format)) {
                    // sort by average result
                    Collections.sort(results, new Comparator<Result>() {
                        @Override
                        public int compare(Result r1, Result r2) {
                            return r1.getAverage() - r2.getAverage();
                        }
                    });

                    String averageRecord = null;
                    for (Result result : results) {
                        // only check "real" results
                        if (result.getAverage() != Result.Penalty.DNF.getValue()
                                && result.getAverage() != Result.Penalty.DNS.getValue()) {
                            // only need to check 1 result for each country
                            if (country != result.getCountry()) {
                                country = result.getCountry();
                                log.debug("Checking average result record for: {} {} in {}", new Object[]{result.getFirstname(), result.getSurname(), event.getName()});
                                averageRecord = client.getAverageRecordType(getEventId(event), country, result.getAverage());
                                if (averageRecord != null) {
                                    if (result.getRegionalAverageRecord() == null) {
                                        log.warn("Found an average {} for: {} {} in {}", new Object[]{averageRecord, result.getFirstname(), result.getSurname(), event.getName()});
                                    } else if (result.getRegionalAverageRecord() != averageRecord) {
                                        log.warn("Not an average {} but a {} for: {} {} in {}", new Object[]{result.getRegionalAverageRecord(), averageRecord, result.getFirstname(), result.getSurname(), event.getName()});
                                    }
                                } else if (result.getRegionalAverageRecord() != null) {
                                    log.warn("Not an average record for: {} {} in {}", new Object[]{result.getFirstname(), result.getSurname(), event.getName()});
                                }
                            } else {
                                averageRecord = null;
                            }
                        }
                        result.setRegionalAverageRecord(averageRecord);
                    }

                // only Average and Mean formats can have average records
                } else {
                    for (Result result : results) {
                        if (result.getRegionalAverageRecord() != null) {
                            log.error("Not an average record for: {} {} in {}", new Object[]{result.getFirstname(), result.getSurname(), event.getName()});
                            result.setRegionalAverageRecord(null);
                        }
                    }
                }
            }
        } catch (InvalidEventException e) {
            log.error("Invalid event type", e);
        } catch (InvalidCountryException e) {
            log.error("Invalid country", e);
        } catch (Exception e) {
			log.error("Could not init wca web client. This could be a connection problem. Endpoint: {}", getConfig().getWcaWebserviceEndpoint());
		}
        return competition;
    }

    /**
     * Get WCA event id for this event.
     * @param event
     * @return
     */
    private String getEventId(Event event) {
        // TODO: implement this
        return event.getName();
    }

	
	/**
	 * Create Swing GUI
	 */
	public void createAndShowGUI() {
		frame = new JFrame("ResultsEngine");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setJMenuBar(new MenuBar(this));
		
		if (statusPanel == null) {
			statusPanel = new StatusPanel();
		}
		frame.getContentPane().add(statusPanel);

		frame.setSize(640, 480);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	/**
	 * Create preferences dialog
	 */
	public void createAndShowPreferencesDialog() {
		JDialog dialog = new JDialog(frame, "Preferences");
		dialog.add(new PreferencesPanel(dialog, this));
		dialog.setResizable(false);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	/**
	 * @param type
	 * @param message
	 */
	public void appendGuiMessage(final String type, final String message) {
		if (getConfig().doShowGUI() && statusPanel != null) {
	    	statusPanel.appendGuiMessage(type, message);
		}
	}
	
	/**
	 * @param type
	 * @param message
	 * @param throwable
	 */
	public void appendGuiMessage(final String type, final String message, final Throwable throwable) {
		if (getConfig().doShowGUI() && statusPanel != null) {
			if (throwable != null) {
		        StringWriter wr = new StringWriter();
			    throwable.printStackTrace(new PrintWriter(wr));
		    	statusPanel.appendGuiMessage(type, message, wr.toString());
			} else {
		    	statusPanel.appendGuiMessage(type, message);
			}
		}
	}
	
	/**
	 * @param type
	 * @param message
	 */
	public void showGuiAlert(final String type, final String message) {
	    if (getConfig().doShowGUI() && statusPanel != null) {
	    	statusPanel.showGuiAlert(type, message, message);
	    }
	}
	
	/**
	 * @param type
	 * @param message
	 * @param throwable
	 */
	public void showGuiAlert(final String type, final String message, final Throwable throwable) {
	    if (getConfig().doShowGUI() && statusPanel != null) {
	    	if (throwable != null) {
	    		StringWriter wr = new StringWriter();
			    throwable.printStackTrace(new PrintWriter(wr));
		    	statusPanel.showGuiAlert(type, message, wr.toString());
	    	} else {
	    		statusPanel.showGuiAlert(type, message);
	    	}
	    }
	}

	/**
	 * @param i
	 */
	private void setProgress(final int i) {
		if (getConfig().doShowGUI() && statusPanel != null) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
		          public void run() {
		        	  statusPanel.setProgress(i);
		          }
			});
		}
	}
}
