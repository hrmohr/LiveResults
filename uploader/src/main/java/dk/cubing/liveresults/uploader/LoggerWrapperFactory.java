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
package dk.cubing.liveresults.uploader;

import dk.cubing.liveresults.uploader.engine.ResultsEngine;

public class LoggerWrapperFactory {

	private static LoggerWrapperFactory instance = null;
	private ResultsEngine engine = null;
	
	private LoggerWrapperFactory() {
	}
	
	/**
	 * @return
	 */
	public static LoggerWrapperFactory getInstance() {
		if (instance == null) {
			instance = new LoggerWrapperFactory();
		}
		return instance;
	}
	
	/**
	 * @param engine the engine to set
	 */
	public void setEngine(ResultsEngine engine) {
		this.engine = engine;
	}

	/**
	 * @return the engine
	 */
	public ResultsEngine getEngine() {
		return engine;
	}

	/**
	 * @param clazz
	 * @return
	 */
	public LoggerWrapper getLogger(Class<?> clazz) {
		return new LoggerWrapper(this, clazz);
	}
}
