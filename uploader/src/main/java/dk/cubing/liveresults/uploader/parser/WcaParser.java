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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import dk.cubing.liveresults.model.Competitor;
import dk.cubing.liveresults.model.Event;
import dk.cubing.liveresults.model.RegisteredEvents;
import dk.cubing.liveresults.model.Result;
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
						log.info("Invalid result sheet: {}", sheet.getSheetName());
						break;
					}
				}
			}
		} else {
			log.info("Invalid registration sheet");
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
							log.error("[{}] Missing firstname and/or surname for row: {}", row.getSheet().getSheetName(), row.getRowNum());
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
							log.error("[{}] Missing country information for row: {}", row.getSheet().getSheetName(), row.getRowNum());
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
							log.warn("[{}] Entered WCA id has wrong length. Expected: 10, Was: {}. Row: {}", new Object[]{row.getSheet().getSheetName(), wcaId.length(), row.getRowNum()});
						}
					}
					break;
					
				// parse gender (currently not used)
				case 4:
					String gender = cell.getStringCellValue();
					if (gender != null) {
						log.debug("Gender: {}", ("f".equalsIgnoreCase(gender)? "Female" : "Male"));
					} else {
						log.warn("[{}] Missing gender information for row: {}", row.getSheet().getSheetName(), row.getRowNum());
					}
					break;
					
				// parse birthday (currently not used)
				case 5:
					Date birthday = cell.getDateCellValue();
					if (birthday != null) {
						try {
							log.debug("Birthday: {}", birthdayFormat.format(birthday));
						} catch (Exception e) {
							log.warn("[{}] Invalid birthday format: {}", row.getSheet().getSheetName(), birthday);
						}
					} else {
						log.warn("[{}] Missing birthday information for row: {}", row.getSheet().getSheetName(), row.getRowNum());
					}
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
		
		// competitor data
		parseCompetitorData(row, result);
		
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
			log.debug("Skipping results for competitor with no name and/or country: Row: {}", row.getRowNum());
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
							log.error("[{}] Missing firstname and/or surname for row: {}", row.getSheet().getSheetName(), row.getRowNum());
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
							log.error("[{}] Missing country information for row: {}", row.getSheet().getSheetName(), row.getRowNum());
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
							log.warn("[{}] Entered WCA id has wrong length. Expected: 10, Was: {}. Row: {}", new Object[]{row.getSheet().getSheetName(), wcaId.length(), row.getRowNum()});
						}
					}
					break;
				}
			}
		}
	}

}
