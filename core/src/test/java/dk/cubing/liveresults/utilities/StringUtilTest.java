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
/**
 * 
 */
package dk.cubing.liveresults.utilities;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilTest {

	/**
	 * Test method for {@link dk.cubing.liveresults.utilities.StringUtil#ucfirst(java.lang.String)}.
	 */
	@Test
	public void testUcfirst() {
		assertNull(StringUtil.ucfirst(null));
		assertEquals("", StringUtil.ucfirst(""));
		assertEquals("Mads", StringUtil.ucfirst("mads"));
		assertEquals("Mads mohr christensen", StringUtil.ucfirst("mads mohr christensen"));
		assertEquals("Mads mohr christensen", StringUtil.ucfirst("MADS MOHR CHRISTENSEN"));
		assertEquals("Mads mohr christensen", StringUtil.ucfirst("mAds mOhr cHristensen"));
	}

	/**
	 * Test method for {@link dk.cubing.liveresults.utilities.StringUtil#ucwords(java.lang.String)}.
	 */
	@Test
	public void testUcwords() {
		assertNull(StringUtil.ucwords(null));
		assertEquals("", StringUtil.ucwords(""));
		assertEquals("Mads", StringUtil.ucwords("mads"));
		assertEquals("Mads Mohr Christensen", StringUtil.ucwords("mads mohr christensen"));
		assertEquals("Mads Mohr Christensen", StringUtil.ucwords("MADS MOHR CHRISTENSEN"));
		assertEquals("Mads Mohr Christensen", StringUtil.ucwords("mAds mOhr cHristensen"));
	}

	/**
	 * Test method for {@link dk.cubing.liveresults.utilities.StringUtil#parseFirstname(java.lang.String)}.
	 */
	@Test
	public void testParseFirstname() {
		assertNull(StringUtil.parseFirstname(null));
		assertEquals("", StringUtil.parseFirstname(""));
		assertEquals("Mads", StringUtil.parseFirstname("Mads"));
		assertEquals("Mads Mohr", StringUtil.parseFirstname("Mads Mohr Christensen"));
	}

	/**
	 * Test method for {@link dk.cubing.liveresults.utilities.StringUtil#parseSurname(java.lang.String)}.
	 */
	@Test
	public void testParseSurname() {
		assertNull(StringUtil.parseSurname(null));
		assertEquals("", StringUtil.parseSurname(""));
		assertEquals("Mads", StringUtil.parseSurname("Mads"));
		assertEquals("Christensen", StringUtil.parseSurname("Mads Mohr Christensen"));
	}

}
