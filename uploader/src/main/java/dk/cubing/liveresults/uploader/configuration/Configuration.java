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
package dk.cubing.liveresults.uploader.configuration;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import dk.cubing.liveresults.uploader.LoggerWrapper;
import dk.cubing.liveresults.uploader.LoggerWrapperFactory;
import dk.cubing.liveresults.uploader.engine.ResultsEngine;

public class Configuration {

	private static final LoggerWrapper log = LoggerWrapperFactory.getInstance().getLogger(Configuration.class);
	
	private final ResultsEngine engine;
	private PropertiesConfiguration config;
	
	/**
	 * @param engine
	 */
	public Configuration(ResultsEngine engine) {
		this.engine = engine;
		try {
			File configFile = new File(new File(".").getAbsolutePath() + "/conf/config.properties");
			if (!configFile.getCanonicalFile().exists()) {
				configFile = new File(ClassLoader.getSystemResource("config.properties").getFile());
			}
			log.debug("Config file: {}", configFile.getCanonicalFile());
			config = new PropertiesConfiguration(configFile.getCanonicalFile());
			config.setReloadingStrategy(new ResultsConfigReloadingStrategy(engine));
		} catch (Exception e) {
			log.error("Could not load configuration file", e);
			engine.shutdown();
		}
	}
	
	/**
	 * @param key
	 * @return
	 */
	private String getConfigurationValue(String key) {
		String result = config.getString(key);
		if (result == null) {
			log.error("Could not load value for key: {}", key);
		}
		return result;
	}
	
	/**
	 * @return
	 */
	public String getCompetitionId() {
		return getConfigurationValue("competition.id");
	}
	
	/**
	 * @param competitionId
	 */
	public void setCompetitionId(String competitionId) {
		config.setProperty("competition.id", competitionId);
	}
	
	/**
	 * @return
	 */
	public String getPassword() {
		return getConfigurationValue("password");
	}
	
	/**
	 * @param password
	 */
	public void setPassword(String password) {
		config.setProperty("password", password);
	}
	
	/**
	 * @return
	 */
	public String getResultsFilename() {
		return getConfigurationValue("results.filename");
	}
	
	/**
	 * @return
	 */
	public File getResultsFile() {
		File resultsFile = new File(getResultsFilename());
		if (resultsFile.isFile()) {
			return resultsFile;
		} else {
			log.error("Could not find results file: {}", resultsFile.getAbsolutePath());
			return null;
		}
		
	}
	
	/**
	 * @param filename
	 */
	public void setResultsFilename(String filename) {
		config.setProperty("results.filename", filename);
	}
	
	/**
	 * @return
	 */
	public String getWebserviceEndpoint() {
		return getConfigurationValue("webservice.endpoint");
	}
	
	/**
	 * @param endpoint
	 */
	public void setWebserviceEndpoint(String endpoint) {
		config.setProperty("webservice.endpoint", endpoint);
	}

    /**
	 * @return
	 */
	public String getWcaWebserviceEndpoint() {
		return getConfigurationValue("webservice.wca.endpoint");
	}

	/**
	 * @param endpoint
	 */
	public void setWcaWebserviceEndpoint(String endpoint) {
		config.setProperty("webservice.wca.endpoint", endpoint);
	}

	/**
	 * @return
	 */
	public boolean doShowGUI() {
		return config.getBoolean("show.gui");
	}
	
	/**
	 * @param showGui
	 */
	public void setShowGUI(boolean showGui) {
		config.setProperty("show.gui", showGui);
	}
	
	/**
	 * Save the current values to selected properties file
	 */
	public void save() {
		try {
			config.save();
			engine.restart();
		} catch (ConfigurationException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * @return
	 */
	public boolean isConfigured() {
		return (!"".equals(getCompetitionId()) && 
				!"".equals(getPassword()) && 
				!"".equals(getResultsFilename()) && getResultsFile() != null);
	}
}
