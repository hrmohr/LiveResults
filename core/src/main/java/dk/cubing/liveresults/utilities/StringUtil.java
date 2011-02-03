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

public class StringUtil {

	/**
	 * @param s
	 * @return
	 */
	public static String ucfirst(String s) {
		String result = null;
		if (s != null) {
			s = s.trim();
			if (s.length() > 1) {
				result = s.substring(0, 1).toUpperCase().concat(s.substring(1).toLowerCase());
			} else {
				result = s.toUpperCase();
			}
		}
		return result;
	}
	
	/**
	 * @param words
	 * @return
	 */
	public static String ucwords(String words) {
		String result = null;
		if (words != null) {
			StringBuilder sb = new StringBuilder();
			for (String word : words.split(" ")) {
				sb.append(ucfirst(word));
				sb.append(" ");
			}
			result = sb.toString().trim();
		}
		return result;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public static String parseFirstname(String name) {
		String result = null;
		if (name != null) {
			if (name.lastIndexOf(' ') != -1) {
				result = name.substring(0, name.lastIndexOf(' '));
			} else {
				result = name;
			}
		}
		return result;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public static String parseSurname(String name) {
		String result = null;
		if (name != null) {
			if (name.lastIndexOf(' ') != -1) {
				result = name.substring(name.lastIndexOf(' ')+1);
			} else {
				result = name;
			}
		}
		return result;
	}
}
