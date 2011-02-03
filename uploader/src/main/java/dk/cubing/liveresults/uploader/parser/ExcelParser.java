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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import dk.cubing.liveresults.model.Competition;
import dk.cubing.liveresults.model.Competitor;
import dk.cubing.liveresults.model.Event;
import dk.cubing.liveresults.uploader.LoggerWrapper;
import dk.cubing.liveresults.uploader.LoggerWrapperFactory;

public abstract class ExcelParser {
	
	private static final LoggerWrapper log = LoggerWrapperFactory.getInstance().getLogger(ExcelParser.class);
	
	/**
	 * @param competition
	 * @param filename
	 * @return
	 * @throws ResultsFileParserException
	 * @throws IllegalStateException
	 */
	public Competition parse(Competition competition, String filename) throws ResultsFileParserException, IllegalStateException {
		try {
			// load excel work book
			FileInputStream fi = new FileInputStream(filename);
			Workbook workBook = WorkbookFactory.create(fi);
			fi.close();
			
			// validate spreadsheet format
			if (isValidSpreadsheet(workBook)) {
			
				// parse competitors
				List<Competitor> competitors = parseCompetitors(workBook);
				if (!competitors.isEmpty()) {
					competition.setCompetitors(competitors);
				}
				
				// parse events
				List<Event> events = parseEvents(workBook);
				if (!events.isEmpty()) {
					competition.setEvents(events);
				}
				
				return competition;
			} else {
				throw new ResultsFileParserException("Results file are not based on the WCA template!");
			}
			
		} catch (FileNotFoundException e) {
			log.error("Results file not found: {}", filename);
		} catch (InvalidFormatException e) {
			log.error("Results file has an invalid format.", e);
		} catch (IOException e) {
			log.error("Error reading results file.", e);
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}

		return null;
	}
	
	/**
	 * @param workBook
	 * @return
	 * @throws IllegalStateException
	 */
	protected abstract boolean isValidSpreadsheet(Workbook workBook) throws IllegalStateException;

	/**
	 * @param workBook
	 * @return
	 * @throws IllegalStateException
	 */
	protected abstract List<Competitor> parseCompetitors(Workbook workBook) throws IllegalStateException;
	
	/**
	 * @param workBook
	 * @return
	 * @throws IllegalStateException
	 */
	protected abstract List<Event> parseEvents(Workbook workBook) throws IllegalStateException;
	
}