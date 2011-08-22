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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.cubing.liveresults.model.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import dk.cubing.liveresults.uploader.LoggerWrapper;
import dk.cubing.liveresults.uploader.LoggerWrapperFactory;
import dk.cubing.liveresults.utilities.CountryUtil;
import dk.cubing.liveresults.utilities.ResultTimeFormat;
import dk.cubing.liveresults.utilities.StringUtil;

public class WcaParser extends ExcelParser {
	
	private static final LoggerWrapper log = LoggerWrapperFactory.getInstance().getLogger(WcaParser.class);
	
	private final String SHEET_TYPE_REGISTRATION = "Registration";
	private final String SHEET_TYPE_DUMMY = "empty";
	private String version = "";
	private String[] basicRegistrationColumns = {"#", "Name", "Country", "WCA id", "Gender\n(f/m)", "Date-of-birth"};
	private String[] basicResultColumns = {"Position", "Name", "Country", "WCA id"};
	
	private final List<String> eventNames = new ArrayList<String>();
	private final SimpleDateFormat birthdayFormat = new SimpleDateFormat("dd-MM-yyyy"); 
	private final Pattern eventFormatPattern = Pattern.compile("((average|mean|best) of ([1-5]))");
	private final Pattern timeFormatPattern = Pattern.compile("(minutes|seconds|number)");
	private final Pattern wcaIdPattern = Pattern.compile("([0-9]{4}[A-Z]{4}[0-9]{2})");
	private final ResultTimeFormat resultTimeFormat = new ResultTimeFormat();
	private FormulaEvaluator evaluator = null;
	private final CountryUtil countryUtil = new CountryUtil();

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @return
	 */
	private CountryUtil getCountryUtil() {
		return countryUtil;
	}
	
	/**
	 * @param sheet
	 * @throws IllegalStateException
	 */
	private void parseEventNames(Sheet sheet) throws IllegalStateException {
		if (eventNames.isEmpty()) {  
			Row row = sheet.getRow(2);
			if (row != null) {
				for (int i=7; i<row.getLastCellNum(); i++) {
					Cell cell = row.getCell(i);
					if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
						String eventName = StringUtil.ucfirst(cell.getStringCellValue());
						log.debug("Event name: {}", eventName);
						eventNames.add(eventName);
					}
				}
			}
		}
	}
	
	/**
	 * @param index
	 * @return
	 */
	private String getEventName(int index) {
		return eventNames.get(index);
	}
	
	/**
	 * @return
	 */
	private int getLastEventNum() {
		return eventNames.size();
	}
	
	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.uploader.parser.ExcelParser#isValidSpreadsheet(org.apache.poi.ss.usermodel.Workbook)
	 */
	@Override
	protected boolean isValidSpreadsheet(Workbook workBook) throws IllegalStateException {
		boolean isValid = false;
		
		// get version information from registration sheet
		Sheet regSheet = workBook.getSheet(SHEET_TYPE_REGISTRATION);
		log.debug("Validating: {}", regSheet.getSheetName());
		isValid = isValidRegistrationSheet(regSheet);
		
		// continue validating result sheets
		if (isValid) {
			for (int i=0; i<workBook.getNumberOfSheets(); i++) {
				Sheet sheet = workBook.getSheetAt(i);
				if (sheet != null && !SHEET_TYPE_REGISTRATION.equals(sheet.getSheetName()) && !SHEET_TYPE_DUMMY.equals(sheet.getSheetName())) {
					log.debug("Validating: {}", sheet.getSheetName());
					isValid = isValidResultSheet(sheet);
					if (!isValid) {
						log.warn("[{}] Invalid result sheet", sheet.getSheetName());
						break;
					}
				}
			}
		} else {
			log.warn("[{}] Invalid registration sheet", regSheet.getSheetName());
		}
		
		return isValid;
	}
	
	/**
	 * @param resultsFile
	 * @return
	 * @throws IllegalStateException
	 */
	protected boolean isValidSpreadsheet(File resultsFile) throws IllegalStateException {
		try {
			FileInputStream fi = new FileInputStream(resultsFile);
			Workbook workBook = WorkbookFactory.create(fi);
			fi.close();
			return isValidSpreadsheet(workBook);
		} catch (FileNotFoundException e) {
			log.error("Results file not found: {}", resultsFile.getAbsolutePath());
		} catch (InvalidFormatException e) {
			log.error("Results file has an invalid format.", e);
		} catch (IOException e) {
			log.error("Error reading results file.", e);
		} catch (IllegalStateException e) {
			log.error("Unexpected cell format.", e);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return false;
	}
	
	/**
	 * @param sheet
	 * @return
	 * @throws IllegalStateException
	 */
	private boolean isValidRegistrationSheet(Sheet sheet) throws IllegalStateException {
		boolean isValid = false;
		if (sheet != null && SHEET_TYPE_REGISTRATION.equals(sheet.getSheetName())) {
			Row versionRow = sheet.getRow(1);
			if (versionRow != null) {
				Cell versionCell = versionRow.getCell(0);
				if (versionCell != null && versionCell.getCellType() == Cell.CELL_TYPE_STRING) {
					isValid = version.equals(versionCell.getStringCellValue());
				}
			}
			if (isValid) {
				// check for basic registration columns
				Row row = sheet.getRow(2);
				if (row != null) {
					for (int i=0; i<6; i++) {
						Cell cell = row.getCell(i);
						if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
							isValid = basicRegistrationColumns[i].equals(cell.getStringCellValue());
							if (!isValid) break;
						}
					}
				}
			}
			// check for position formula
			if (isValid) {
				Row row = sheet.getRow(3);
				if (row != null) {
					Cell cell = row.getCell(0);
					isValid = (cell != null && cell.getCellType() == Cell.CELL_TYPE_FORMULA);
				}
			}
		}
		return isValid;
	}
	
	/**
	 * @param sheet
	 * @return
	 * @throws IllegalStateException
	 */
	private boolean isValidResultSheet(Sheet sheet) throws IllegalStateException {
		boolean isValid = false;
		if (sheet != null && !SHEET_TYPE_REGISTRATION.equals(sheet.getSheetName())) {
			// check for basic result columns
			Row row = sheet.getRow(3);
			if (row != null) {
				for (int i=0; i<4; i++) {
					Cell cell = row.getCell(i);
					if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
						isValid = basicResultColumns[i].equals(cell.getStringCellValue());
						if (!isValid) break;
					}
				}
			}
			// check for position formula
			if (isValid) {
				row = sheet.getRow(4);
				if (row != null) {
					Cell cell = row.getCell(0);
					isValid = (cell != null && cell.getCellType() == Cell.CELL_TYPE_FORMULA);
				}
			}
		}
		return isValid;
	}

    @Override
    protected Competition parseCompetitionDetails(Workbook workBook, Competition competition) throws IllegalStateException {
        if (competition.getName() == null) { // only used when automatic upload are disabled
            Sheet sheet = workBook.getSheet(SHEET_TYPE_REGISTRATION);
            if (isValidRegistrationSheet(sheet)) {
                Row row = sheet.getRow(0);
                if (row != null) {
                    Cell cell = row.getCell(0);
                    if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        competition.setName(cell.getStringCellValue());
                    }
                }
            }
        }
        return competition;
    }

    /* (non-Javadoc)
      * @see dk.cubing.liveresults.uploader.parser.ExcelParser#parseCompetitors(org.apache.poi.ss.usermodel.Workbook)
      */
	@Override
	protected List<Competitor> parseCompetitors(Workbook workBook) throws IllegalStateException {
		List<Competitor> competitors = new CopyOnWriteArrayList<Competitor>();
		Sheet sheet = workBook.getSheet(SHEET_TYPE_REGISTRATION);
		if (isValidRegistrationSheet(sheet)) {
			parseEventNames(sheet);
			Row firstRow = sheet.getRow(3); // first row with competitor data
			if (firstRow != null) {
				Cell cell = firstRow.getCell(1); // first cell with competitor data
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) { // only parse sheet with content
					log.debug("Parsing: {}", sheet.getSheetName());
					for (Row row : sheet) {
						if (row.getRowNum() > 2) {
							Competitor competitor = parseCompetitorRow(row);
							if (competitor != null) {
								if (competitor.getRegisteredEvents().hasSignedUp()) {
									competitors.add(competitor);
								} else {
									log.warn("[{}] No events registered for: {}", sheet.getSheetName(), competitor.getFirstname());
								}
							}
						}
					}
				}
			}
		}
		return competitors;
	}
	
	/**
	 * @param row
	 * @return
	 * @throws IllegalStateException
	 */
	private Competitor parseCompetitorRow(Row row) throws IllegalStateException {
		Competitor competitor = new Competitor();
		// parse competitor data
		for (int i=1; i<6; i++) {
			Cell cell = row.getCell(i);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				switch (i) {
				// parse name
				case 1:
					String name = cell.getStringCellValue();
					if (name != null) { 
						name = StringUtil.ucwords(name);
						if (name.lastIndexOf(' ') != -1) {
							competitor.setFirstname(StringUtil.parseFirstname(name));
							competitor.setSurname(StringUtil.parseSurname(name));
							log.debug("Found competitor: {}", name);
						} else {
							log.error("[{}] Missing firstname and/or surname for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
						}
					}
					break;
					
				// parse iso country code
				case 2:
					String country = cell.getStringCellValue();
					if (country != null) {
						String countryCode = null;
						if (country.length() > 2) {
							countryCode = getCountryUtil().getCountryCodeByName(country);
						} else {
							countryCode = getCountryUtil().getCountryByCode(country);
						}
						if (countryCode != null) { 
							competitor.setCountry(countryCode);
							log.debug("Country: {} - {}", countryCode, country);
						} else {
							log.error("[{}] Missing country information for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
						}
					}
					break;
					
				// parse wca id
				case 3:
					String wcaId = cell.getStringCellValue();
					if (wcaId != null) {
						wcaId = wcaId.trim();
						if (wcaId.length() == 10) {
							Matcher m = wcaIdPattern.matcher(wcaId);
							if (m.find()) {
								competitor.setWcaId(wcaId); 
								log.debug("WCA Id: {}", competitor.getWcaId());
							} else {
								log.warn("[{}] Invalid wcaId format: {}", row.getSheet().getSheetName(), wcaId);
							}
						} else {
							log.warn("[{}] Entered WCA id has wrong length. Expected: 10, Was: {}. Row: {}", new Object[]{row.getSheet().getSheetName(), wcaId.length(), row.getRowNum()+1});
						}
					}
					break;
					
				// parse gender
				case 4:
					String gender = cell.getStringCellValue();
					if (gender != null) {
						gender = gender.toLowerCase();
						if ("f".equals(gender) || "m".equals(gender)) {
							competitor.setGender(gender);
							log.debug("Gender: {}", ("f".equals(gender)? "Female" : "Male"));
						} else {
							log.warn("[{}] Invalid gender: {}", row.getSheet().getSheetName(), gender);
						}
					} else {
						log.warn("[{}] Missing gender information for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
					}
					break;
					
				// parse birthday
				case 5:
					Date birthday = cell.getDateCellValue();
					if (birthday != null) {
						try {
							competitor.setBirthday(birthday);
							log.debug("Birthday: {}", birthdayFormat.format(birthday));
						} catch (Exception e) {
							log.warn("[{}] Invalid birthday format: {}", row.getSheet().getSheetName(), birthday);
						}
					} else {
						log.warn("[{}] Missing birthday information for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
					}
					break;
				}
			} else {
                switch (i) {
                    case 1:
                        log.error("[{}] Missing firstname and/or surname for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
                        break;
                    case 2:
                        log.error("[{}] Missing country information for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
                        break;
                    case 3:
                        // WCA ID are optional
                        break;
                    case 4:
                        log.warn("[{}] Missing gender information for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
                        break;
                    case 5:
                        log.warn("[{}] Missing birthday information for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
                        break;
                }
            }
		}
		
		// parse registered events
		if (competitor.getFirstname() != null && competitor.getSurname() != null) {
			RegisteredEvents registeredEvents = new RegisteredEvents();
			for (int i=0; i<getLastEventNum(); i++) {
				Cell cell = row.getCell(i+7);
				boolean registered = false;
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					log.debug("Registered for: {}", getEventName(i));
					registered = true;
				} else {
					log.debug("Not registered for: {}", getEventName(i));
					registered = false;
				}
				try {
					Method method = registeredEvents.getClass().getMethod("setSignedUpFor" + getEventName(i), boolean.class);
					method.invoke(registeredEvents, registered);
				} catch (Exception e) {
					log.error("[{}] " + e.getLocalizedMessage(), row.getSheet().getSheetName(), e);
				}
			}
			competitor.setRegisteredEvents(registeredEvents);
			return competitor;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see dk.cubing.liveresults.uploader.parser.ExcelParser#parseEvents(org.apache.poi.ss.usermodel.Workbook)
	 */
	@Override
	protected List<Event> parseEvents(Workbook workBook) throws IllegalStateException {
		evaluator = workBook.getCreationHelper().createFormulaEvaluator();
		List<Event> events = new CopyOnWriteArrayList<Event>();
		for (int i=0; i<workBook.getNumberOfSheets(); i++) {
			Sheet sheet = workBook.getSheetAt(i);
			if (isValidResultSheet(sheet)) {
				log.debug("Parsing: {}", sheet.getSheetName());
				Row firstRow = sheet.getRow(4); // first row with event results
				if (firstRow != null) {
					Cell cell = firstRow.getCell(1); // first cell with event results
					if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) { // only parse sheet with content
						Event event = parseEventDetails(sheet);
						event.setLive(workBook.getActiveSheetIndex() == i);
						List<Result> results = new CopyOnWriteArrayList<Result>();
						for (int j=4; j<sheet.getLastRowNum(); j++) {
							Row row = sheet.getRow(j);
							if (row != null) {
								Result result = parseResultRow(row, event);
								if (result.getFirstname() != null && result.getSurname() != null) {
									results.add(result);
								}
							}
						}
						if (!results.isEmpty()) {
							event.setResults(results);
							events.add(event);
						}
					}
				}
			}
		}
		return events;
	}

	/**
	 * @param sheet
	 * @return
	 * @throws IllegalStateException
	 */
	private Event parseEventDetails(Sheet sheet) throws IllegalStateException {
		Event event = new Event();
		for (int i=0; i<3; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				Cell cell = row.getCell(0);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					switch (i) {
					// event name
					case 0:
						String eventName = cell.getStringCellValue();
						if (eventName != null) {
							log.debug("Results for: {}", eventName);
							event.setName(eventName);
						}
						break;
						
					// event format
					case 1:
						String eventFormat = cell.getStringCellValue();
						if (eventFormat != null) {
							Matcher m = eventFormatPattern.matcher(eventFormat);
							if (m.find()) {
								int numberOfAttempts = Integer.parseInt(m.group(3));
								if ("best".equals(m.group(2))) {
									eventFormat = Integer.toString(numberOfAttempts);
								} else {
									eventFormat = m.group(2);
								}
								log.debug("Event format: {}", m.group(1));
								event.setFormat(eventFormat.substring(0, 1));
							}
						}
						break;
						
					// time format
					case 2:
						String timeFormat = cell.getStringCellValue();
						if (timeFormat != null) {
							if (event.getName() != null && event.getName().toLowerCase().contains("multi")) { // multi bld has a special format
								log.debug("Time format: {}", Event.TimeFormat.MULTI_BLD.toString());
								event.setTimeFormat(Event.TimeFormat.MULTI_BLD.getValue());
							} else {
								Matcher m = timeFormatPattern.matcher(timeFormat);
								if (m.find()) {
									log.debug("Time format: {}", timeFormat);
									event.setTimeFormat(m.group().substring(0, 1));
								}
							}
						}
						break;
					}
				}
			}
		}
		return event;
	}

	/**
	 * @param row
	 * @param event
	 * @return
	 * @throws IllegalStateException
	 */
	private Result parseResultRow(Row row, Event event) throws IllegalStateException {
		Result result = new Result();
		
		// competitor data for teams
		if (event.getName().toLowerCase().contains("team")) {
			parseTeamCompetitorData(row, result);
		// normal competitors
		} else {
			parseCompetitorData(row, result);
		}
		
		// only parse results for competitors with a name / country
		if (result.getFirstname() != null && result.getSurname() != null && result.getCountry() != null) {
		
			// handle special multiple blindfolded sheet
			if (event.getName().toLowerCase().contains("multi")) {
				// best of 1
				if (Event.Format.BEST_OF_1.getValue().equals(event.getFormat())) {
					result.setResult1(parseResultCell(row, 5));
					result.setBest(result.getResult1());
					result.setRegionalSingleRecord(parseRecordCell(row, 4));
					
				// best of 2
				} else if (Event.Format.BEST_OF_2.getValue().equals(event.getFormat())) {
					result.setResult1(parseResultCell(row, 4));
					result.setResult2(parseResultCell(row, 8));
					result.setBest(parseResultCell(row, 9));
					result.setRegionalSingleRecord(parseRecordCell(row, 10));
					
				// unsupported format
				} else {
					log.warn("[{}] Unsupported format: {}", row.getSheet().getSheetName(), event.getFormat());
				}
				
			// handle special team events
			} else if (event.getName().toLowerCase().contains("team")) {
				// best of 1
				if (Event.Format.BEST_OF_1.getValue().equals(event.getFormat())) {
					result.setResult1(parseResultCell(row, 3+1));
					result.setBest(result.getResult1());
					result.setRegionalSingleRecord(parseRecordCell(row, 3+2));
					
				// best of 2
				} else if (Event.Format.BEST_OF_2.getValue().equals(event.getFormat())) {
					result.setResult1(parseResultCell(row, 3+1));
					result.setResult2(parseResultCell(row, 3+2));
					result.setBest(parseResultCell(row, 3+3));
					result.setRegionalSingleRecord(parseRecordCell(row, 3+4));
					
				// best of 3
				} else if (Event.Format.BEST_OF_3.getValue().equals(event.getFormat())) {
					result.setResult1(parseResultCell(row, 3+1));
					result.setResult2(parseResultCell(row, 3+2));
					result.setResult3(parseResultCell(row, 3+3));
					result.setBest(parseResultCell(row, 3+4));
					result.setRegionalSingleRecord(parseRecordCell(row, 3+5));
					
				// unsupported format
				} else {
					log.warn("[{}] Unsupported format: {}", row.getSheet().getSheetName(), event.getFormat());
				}
		
			// average of 5
			} else if (Event.Format.AVERAGE.getValue().equals(event.getFormat())) {
				result.setResult1(parseResultCell(row, 1));
				result.setResult2(parseResultCell(row, 2));
				result.setResult3(parseResultCell(row, 3));
				result.setResult4(parseResultCell(row, 4));
				result.setResult5(parseResultCell(row, 5));
				result.setBest(parseResultCell(row, 6));
				result.setRegionalSingleRecord(parseRecordCell(row, 7));
				result.setWorst(parseResultCell(row, 8));
				result.setAverage(parseResultCell(row, 9));
				result.setRegionalAverageRecord(parseRecordCell(row, 10));
			
			// best of 1
			} else if (Event.Format.BEST_OF_1.getValue().equals(event.getFormat())) {
				result.setResult1(parseResultCell(row, 1));
				result.setBest(result.getResult1());
				result.setRegionalSingleRecord(parseRecordCell(row, 2));
			
			// best of 2
			} else if (Event.Format.BEST_OF_2.getValue().equals(event.getFormat())) {
				result.setResult1(parseResultCell(row, 1));
				result.setResult2(parseResultCell(row, 2));
				result.setBest(parseResultCell(row, 3));
				result.setRegionalSingleRecord(parseRecordCell(row, 4));
	
			// best of 3
			} else if (Event.Format.BEST_OF_3.getValue().equals(event.getFormat())) {
				result.setResult1(parseResultCell(row, 1));
				result.setResult2(parseResultCell(row, 2));
				result.setResult3(parseResultCell(row, 3));
				result.setBest(parseResultCell(row, 4));
				result.setRegionalSingleRecord(parseRecordCell(row, 5));
			
			// mean of 3
			} else if (Event.Format.MEAN.getValue().equals(event.getFormat())) {
				result.setResult1(parseResultCell(row, 1));
				result.setResult2(parseResultCell(row, 2));
				result.setResult3(parseResultCell(row, 3));
				result.setBest(parseResultCell(row, 4));
				result.setRegionalSingleRecord(parseRecordCell(row, 5));
				result.setAverage(parseResultCell(row, 6));
				result.setRegionalAverageRecord(parseRecordCell(row, 7));
				
			// unsupported format
			} else {
				log.error("[{}] Unsupported format: {}", row.getSheet().getSheetName(), event.getFormat());
			}
		} else {
			log.debug("[{}] Skipping results for competitor with no name and/or country: Row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
		}
		return result;
	}

	/**
	 * @param row
	 * @param i
	 * @return
	 * @throws IllegalStateException
	 */
	private int parseResultCell(Row row, int i) throws IllegalStateException {
		int result = 0;
		Cell cell = row.getCell(3 + i);
		if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
			String cellStr = null;
			
			switch (cell.getCellType()) {
			// result values
			case Cell.CELL_TYPE_NUMERIC:
				// seconds
				if ("0.00".equals(cell.getCellStyle().getDataFormatString())) {
					result = new Double(cell.getNumericCellValue()*100).intValue();
				
				// minutes
				} else if ("M:SS.00".equalsIgnoreCase(cell.getCellStyle().getDataFormatString())) {
					try {
						result = resultTimeFormat.formatDateToInt(cell.getDateCellValue());
					} catch (ParseException e) {
						log.error("[{}] " + e.getLocalizedMessage(), e);
					}
				
				// number
				} else if ("0".equals(cell.getCellStyle().getDataFormatString())) {
					result = new Double(cell.getNumericCellValue()).intValue();
					
				// unsupported
				} else {
					log.warn("[{}] Unsupported cell format: {}. Row/Column ({}, {})", new Object[]{row.getSheet().getSheetName(), cell.getCellStyle().getDataFormatString(), cell.getRowIndex(), cell.getColumnIndex()});
				}
				break;
				
			// Penalties
			case Cell.CELL_TYPE_STRING:
				cellStr = cell.getStringCellValue();
				if (cellStr != null) {
					if (cellStr.equalsIgnoreCase(Result.Penalty.DNF.toString())) {
						result = Result.Penalty.DNF.getValue();
					} else if (cellStr.equalsIgnoreCase(Result.Penalty.DNS.toString())) {
						result = Result.Penalty.DNS.getValue();
					}
				}
				break;
				
			// best / worst
			case Cell.CELL_TYPE_FORMULA:
				CellValue cellValue = evaluator.evaluate(cell);
				switch (cellValue.getCellType()) {
				// calculated value
				case Cell.CELL_TYPE_NUMERIC:
					// seconds
					if ("0.00".equals(cell.getCellStyle().getDataFormatString())) {
						result = new Double(cellValue.getNumberValue()*100).intValue();
					
					// minutes
					} else if ("M:SS.00".equalsIgnoreCase(cell.getCellStyle().getDataFormatString())) {
						try {
							result = resultTimeFormat.formatDateToInt(cell.getDateCellValue());
						} catch (ParseException e) {
							log.error("[{}] " + e.getLocalizedMessage(), e);
						}
					
					// number
					} else if ("0".equals(cell.getCellStyle().getDataFormatString()) ||
							"GENERAL".equals(cell.getCellStyle().getDataFormatString())) {
						result = new Double(cell.getNumericCellValue()).intValue();
						
					// unsupported
					} else {
						log.warn("[{}] Unsupported cell format: {}. Row/Column ({}, {})", new Object[]{row.getSheet().getSheetName(), cell.getCellStyle().getDataFormatString(), cell.getRowIndex(), cell.getColumnIndex()});
					}
					break;
					
				// Penalties
				case Cell.CELL_TYPE_STRING:
					cellStr = cellValue.getStringValue();
					if (cellStr != null) {
						if (cellStr.equalsIgnoreCase(Result.Penalty.DNF.toString())) {
							result = Result.Penalty.DNF.getValue();
						} else if (cellStr.equalsIgnoreCase(Result.Penalty.DNS.toString())) {
							result = Result.Penalty.DNS.getValue();
						}
					}
					break;
				}
				break;
			}
		}
		return result;
	}
	
	/**
	 * @param row
	 * @param i
	 * @return
	 * @throws IllegalStateException
	 */
	private String parseRecordCell(Row row, int i) throws IllegalStateException {
		String result = null;
		Cell cell = row.getCell(3 + i);
		if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
			String cellStr = cell.getStringCellValue();
			if (cellStr != null) {
				if (cellStr.equalsIgnoreCase(Result.Record.NR.toString())) {
					result = Result.Record.NR.getValue();
				} else if (cellStr.equalsIgnoreCase(Result.Record.CR.toString())) {
					result = Result.Record.CR.getValue();
				} else if (cellStr.equalsIgnoreCase(Result.Record.ER.toString())) {
					result = Result.Record.ER.getValue();
				} else if (cellStr.equalsIgnoreCase(Result.Record.WR.toString())) {
					result = Result.Record.WR.getValue();
				} else if (cellStr.equalsIgnoreCase(Result.Record.NAR.toString())) {
					result = Result.Record.NAR.getValue();
				} else if (cellStr.equalsIgnoreCase(Result.Record.SAR.toString())) {
					result = Result.Record.SAR.getValue();
				} else if (cellStr.equalsIgnoreCase(Result.Record.AUR.toString())) {
					result = Result.Record.AUR.getValue();
				} else if (cellStr.equalsIgnoreCase(Result.Record.AUR.toString())) {
					result = Result.Record.AUR.getValue();
				} else if (cellStr.equalsIgnoreCase(Result.Record.AUR.toString())) {
					result = Result.Record.AUR.getValue();
				}
			}
		}
		return result;
	}		

	/**
	 * @param row
	 * @param result
	 * @throws IllegalStateException
	 */
	private void parseCompetitorData(Row row, Result result) throws IllegalStateException {
		for (int i=1; i<4; i++) {
			Cell cell = row.getCell(i);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				switch (i) {
				// parse name
				case 1:
					String name = cell.getStringCellValue();
					if (name != null) { 
						name = StringUtil.ucwords(name);
						if (name.lastIndexOf(' ') != -1) {
							result.setFirstname(StringUtil.parseFirstname(name));
							result.setSurname(StringUtil.parseSurname(name));
							log.debug("Found result for : {}", name);
						} else {
							log.error("[{}] Missing firstname and/or surname for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
						}
					}
					break;
					
				// parse iso country code
				case 2:
					String country = cell.getStringCellValue();
					if (country != null) {
						String countryCode = null;
						if (country.length() > 2) {
							countryCode = getCountryUtil().getCountryCodeByName(country);
						} else {
							countryCode = getCountryUtil().getCountryByCode(country);
						}
						if (countryCode != null) { 
							result.setCountry(countryCode);
							log.debug("Country: {} - {}", countryCode, country);
						} else {
							log.error("[{}] Missing country information for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
						}
					}
					break;
					
				// parse wca id
				case 3:
					String wcaId = cell.getStringCellValue();
					if (wcaId != null) {
						wcaId = wcaId.trim();
						if (wcaId.length() == 10) {
							Matcher m = wcaIdPattern.matcher(wcaId);
							if (m.find()) {
								result.setWcaId(wcaId); 
								log.debug("WCA Id: {}", result.getWcaId());
							} else {
								log.warn("[{}] Invalid wcaId format: {}", row.getSheet().getSheetName(), wcaId);
							}
						} else {
							log.warn("[{}] Entered WCA id has wrong length. Expected: 10, Was: {}. Row: {}", new Object[]{row.getSheet().getSheetName(), wcaId.length(), row.getRowNum()+1});
						}
					}
					break;
				}
			} else {
                switch (i) {
                    case 1:
                        if (result.getCountry() != null) {
                            log.error("[{}] Missing firstname and/or surname for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
                        }
                        break;
                    case 2:
                        if (result.getFirstname() != null) {
                            log.error("[{}] Missing country information for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
                        }
                        break;
                    case 3:
                        // WCA ID are optional
                        break;
                }
            }
		}
	}
	
	/**
	 * @param row
	 * @param result
	 * @throws IllegalStateException
	 */
	private void parseTeamCompetitorData(Row row, Result result) throws IllegalStateException {
		Cell nameCell1 = row.getCell(1);
		Cell nameCell2 = row.getCell(1+3);
		if ((nameCell1 != null && nameCell1.getCellType() != Cell.CELL_TYPE_BLANK) && (nameCell2 != null && nameCell2.getCellType() != Cell.CELL_TYPE_BLANK)) {
			String name1 = nameCell1.getStringCellValue();
			String name2 = nameCell2.getStringCellValue();
			if (name1 != null && name2 != null) {
				name1 = StringUtil.ucwords(name1);
				name2 = StringUtil.ucwords(name2);
				if (name1.lastIndexOf(' ') != -1 && name2.lastIndexOf(' ') != -1) {
					result.setFirstname(StringUtil.parseFirstname(name1) + " / " + StringUtil.parseFirstname(name2));
					result.setSurname(StringUtil.parseSurname(name1) + " / " + StringUtil.parseSurname(name2));
					log.debug("Found result for : {} / {}", name1, name2);
				} else {
					log.error("[{}] Missing firstname and/or surname for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
				}
			}
		}
		
		Cell countryCell1 = row.getCell(2);
		Cell countryCell2 = row.getCell(2+3);
		if ((countryCell1 != null && countryCell1.getCellType() != Cell.CELL_TYPE_BLANK) && (countryCell2 != null && countryCell2.getCellType() != Cell.CELL_TYPE_BLANK)) {
			String country1 = countryCell1.getStringCellValue();
			String country2 = countryCell2.getStringCellValue();
			if (country1 != null && country2 != null) {
				String countryCode1 = null;
				String countryCode2 = null;
				if (country1.length() > 2 && country2.length() > 2) {
					countryCode1 = getCountryUtil().getCountryCodeByName(country1);
					countryCode2 = getCountryUtil().getCountryCodeByName(country2);
				} else {
					countryCode1 = getCountryUtil().getCountryByCode(country1);
					countryCode2 = getCountryUtil().getCountryByCode(country2);
				}
				if (countryCode1 != null && countryCode2 != null) { 
					result.setCountry(countryCode1); //TODO: for now we only support 1 country
					log.debug("Country: {} - {} / {} - {}", new Object[]{countryCode1, country1, countryCode1, country1});
				} else {
					log.error("[{}] Missing country information for row: {}", row.getSheet().getSheetName(), row.getRowNum()+1);
				}
			}
		}
		
		Cell wcaIdCell1 = row.getCell(3);
		Cell wcaIdCell2 = row.getCell(3+3);
		if ((wcaIdCell1 != null && wcaIdCell1.getCellType() != Cell.CELL_TYPE_BLANK) && (wcaIdCell2 != null && wcaIdCell2.getCellType() != Cell.CELL_TYPE_BLANK)) {
			String wcaId1 = wcaIdCell1.getStringCellValue();
			String wcaId2 = wcaIdCell2.getStringCellValue();
			if (wcaId1 != null && wcaId2 != null) {
				wcaId1 = wcaId1.trim();
				wcaId2 = wcaId2.trim();
				if (wcaId1.length() == 10 && wcaId2.length() == 10) {
					Matcher m1 = wcaIdPattern.matcher(wcaId1);
					Matcher m2 = wcaIdPattern.matcher(wcaId2);
					if (m1.find() && m2.find()) {
						result.setWcaId(wcaId1); // FIXME: for now only 1 wcaId are supported 
						log.debug("WCA Id: {} / {}", wcaId1, wcaId2);
					} else {
						log.warn("[{}] Invalid wcaId format: {} / {}", new Object[]{row.getSheet().getSheetName(), wcaId1, wcaId2});
					}
				} else {
					log.warn("[{}] Entered WCA id has wrong length. Expected: 10, Was: {} / {}. Row: {}", new Object[]{row.getSheet().getSheetName(), wcaId1.length(), wcaId2.length(), row.getRowNum()+1});
				}
			}
		}
	}

}
