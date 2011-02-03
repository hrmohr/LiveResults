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
package dk.cubing.liveresults.uploader.parser;

import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.uploader.LoggerWrapper;
import dk.cubing.liveresults.uploader.LoggerWrapperFactory;

public class ResultsFileParser {
	
	private static final LoggerWrapper log = LoggerWrapperFactory.getInstance().getLogger(ResultsFileParser.class);

	private ExcelParser parser;
	
	/**
	 * @return the parser
	 */
	public ExcelParser getParser() {
		return parser;
	}

	/**
	 * @param parser the parser to set
	 */
	public void setParser(ExcelParser parser) {
		this.parser = parser;
	}

	/**
	 * @param competition
	 * @param filename
	 * @return
	 * @throws ResultsFileParserException
	 * @throws IllegalStateException
	 */
	public Competition parse(Competition competition, String filename) throws ResultsFileParserException, IllegalStateException {
		try {
			ExcelParser parser = getParser(); 
			if (parser != null) {
				return parser.parse(competition, filename);
			} else {
				log.error("No parser is set!");
				System.exit(0);
			}
		} catch (ResultsFileParserException e) {
			throw e;
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			throw new ResultsFileParserException(e);
		}
		return null;
	}
}
