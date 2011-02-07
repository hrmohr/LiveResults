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
package dk.cubing.liveresults.utilities;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dk.cubing.liveresults.model.Event;
import dk.cubing.liveresults.model.Result;

public class ResultTimeFormat {
	
	private final DecimalFormat secondsFormat;
	private final SimpleDateFormat minutesFormat;
	private final DecimalFormat numberFormat;
	private final Calendar cal;

	public ResultTimeFormat() {
		secondsFormat = new DecimalFormat("#0.00");
		minutesFormat = new SimpleDateFormat("mm:ss.S");
		numberFormat = new DecimalFormat("0");
		cal = Calendar.getInstance();
	}

	/**
	 * @param result
	 * @param timeFormat
	 * @return
	 */
	public String format(int result, String timeFormat) {
		if (result > 0) {
			if (Event.TimeFormat.NUMBER.getValue().equals(timeFormat)) {
				return formatNumber(result);
			} else if (Event.TimeFormat.SECONDS.getValue().equals(timeFormat)) {
				return format(result / 100F);
			} else if (Event.TimeFormat.MINUTES.getValue().equals(timeFormat)) {
				Date date = formatDoubleToDate(result); // FIXME: refactor this too as working on dates does not work
				return format(date);
			} else if (Event.TimeFormat.MULTI_BLD.getValue().equals(timeFormat)) {
				return formatMultiBLD(result);
			}
			return null;
		} else {
			switch (result) {
			case -2:
				return Result.Penalty.DNS.toString();
			case -1:
			default:
				return Result.Penalty.DNF.toString();
			}
		}
	}
	
	/**
	 * @param result
	 * @return
	 */
	public String format(double result) {
		if (result < 60) {
			return secondsFormat.format(result);
		} else {
			Date date = formatDoubleToDate(result * 100);
			return format(date);
		}
	}
	
	/**
	 * @param result
	 * @return
	 */
	public String format(Date result) {
		String resultStr = minutesFormat.format(result);
		if (resultStr.startsWith("00:")) {
			resultStr = resultStr.substring(3);
		}
		if (resultStr.startsWith("0")) {
			resultStr = resultStr.substring(1);
		}
		resultStr += "00";
		resultStr = resultStr.substring(0, resultStr.indexOf(".")+3);
		return resultStr;
	}

	/**
	 * @param result
	 * @return
	 */
	public String formatNumber(double result) {
		if (result > 0) {
			return numberFormat.format(result);
		} else {
			switch (new Double(result).intValue()) {
			case -2:
				return Result.Penalty.DNS.toString();
			case -1:
			default:
				return Result.Penalty.DNF.toString();
			}
		}
	}
	
	/**
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public int formatDateToInt(Date date) throws ParseException {
		int result = 0;
		cal.setTime(minutesFormat.parse(minutesFormat.format(date)));
		result = (cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND)) * 100 + cal.get(Calendar.MILLISECOND) / 10;
		return result;
	}
	
	/**
	 * @param result
	 * @return
	 */
	public Date formatDoubleToDate(double result) {
		int seconds = (int)Math.floor(result / 100);
		int milis = (int)(result - seconds * 100);
		String milisStr = "0" + milis;
		String dateStr = seconds * 10 + milisStr.substring(milisStr.length()-2); 
		return new Date(new Long(dateStr));
	}
	
	/**
	 * @param rank
	 * @return
	 */
	public String formatRank(int rank) {
		switch (rank) {
		case 1:
			return "1st place in the";
		case 2:
			return "2nd place in the"; 
		case 3:
			return "3rd place in the";
		default:
			return Integer.toString(rank);
		}
	}
	
	/**
	 * @param rank
	 * @param isDanishOpen
	 * @return
	 */
	public String formatDanishRank(int rank, boolean isDanishOpen) {
		switch (rank) {
		case 1:
			return isDanishOpen ? "Dansk Mester i" : "1. plads i";
		case 2:
			return "2. plads i"; 
		case 3:
			return "3. plads i";
		default:
			return Integer.toString(rank);
		}
	}
	
	/**
	 * @param result
	 * @return
	 */
	public String formatMultiBLD(int result) {
		if (result > 0) {
			String resultStr = Integer.toString(result);
			int failed = Integer.parseInt(resultStr.substring(resultStr.length() - 2));
			int time = Integer.parseInt(Integer.toString(result).substring(2, 7));
			int solved = 99 - Integer.parseInt(Integer.toString(result).substring(0, 2)) + failed;
            int tried = solved + failed;
			int minutes = (int)Math.floor(time / 60);
			int seconds = time - (minutes * 60);
			String timeStr = "";
            if (minutes > 0) {
                if (minutes == 60) {
                    timeStr = "1:00:00";
                } else {
                	String secondsStr = "00" + seconds;
                    timeStr = minutes + ":" + secondsStr.substring(secondsStr.length()-2);
                }
            } else {
                timeStr = "" + seconds;
            }
			return solved + "/" + tried + " " + timeStr;
		} else {
			switch (result) {
			case -2:
				return Result.Penalty.DNS.toString();
			case -1:
			default:
				return Result.Penalty.DNF.toString();
			}
		}
	}
}
