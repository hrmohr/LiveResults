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

import java.util.HashMap;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountryUtil {
	
	static private final Logger log = LoggerFactory.getLogger(CountryUtil.class);
	
	private HashMap<String, String> countriesCode = new HashMap<String, String>();
	private HashMap<String, String> countriesName = new HashMap<String, String>();


	public CountryUtil() {
		Locale.setDefault(Locale.ENGLISH);
		for (String country : Locale.getISOCountries()) {
			Locale locale = new Locale("", country);
			countriesCode.put(country, locale.getDisplayCountry());
			countriesName.put(locale.getDisplayCountry().toLowerCase(), country);
		}
	}
	
	/**
	 * @param code
	 * @return
	 */
	public String getCountryByCode(String code) {
		String country = countriesCode.get((code != null) ? code.toUpperCase() : null);
		if (country == null) {
			log.warn("Could not look up country by code: {}", code);
		}
		return country;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public String getCountryCodeByName(String name) {
		String country = countriesName.get((name != null) ? name.toLowerCase() : null);
		if (country == null) {
			log.warn("Could not look up country code by name: {}", name);
		}
		return country;
	}
}
