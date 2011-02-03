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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import dk.cubing.liveresults.model.Event;
import dk.cubing.liveresults.model.Result;

public class ResultTimeFormatTest {
	
	private ResultTimeFormat resultTimeFormat;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		resultTimeFormat = new ResultTimeFormat();
	}

	/**
	 * Test method for {@link dk.cubing.liveresults.utilities.ResultTimeFormat#format(int, String)}.
	 */
	@Test
	public void testFormatInt() {
		// number format
		assertEquals(Result.Penalty.DNF.toString(), resultTimeFormat.format(Result.Penalty.DNF.getValue(), Event.TimeFormat.NUMBER.getValue()));
		assertEquals(Result.Penalty.DNF.toString(), resultTimeFormat.format(0, Event.TimeFormat.NUMBER.getValue()));
		assertEquals(Result.Penalty.DNF.toString(), resultTimeFormat.format(Integer.MIN_VALUE, Event.TimeFormat.NUMBER.getValue()));
		assertEquals(Result.Penalty.DNS.toString(), resultTimeFormat.format(Result.Penalty.DNS.getValue(), Event.TimeFormat.NUMBER.getValue()));
		assertEquals(Integer.toString(Integer.MAX_VALUE), resultTimeFormat.format(Integer.MAX_VALUE, Event.TimeFormat.NUMBER.getValue()));
		assertEquals("42", resultTimeFormat.format(42, Event.TimeFormat.NUMBER.getValue()));
		
		// seconds format
		assertEquals(Result.Penalty.DNF.toString(), resultTimeFormat.format(Result.Penalty.DNF.getValue(), Event.TimeFormat.SECONDS.getValue()));
		assertEquals(Result.Penalty.DNS.toString(), resultTimeFormat.format(Result.Penalty.DNS.getValue(), Event.TimeFormat.SECONDS.getValue()));
		assertEquals("7.08", resultTimeFormat.format(708, Event.TimeFormat.SECONDS.getValue()));
		
		// minutes format
		assertEquals(Result.Penalty.DNF.toString(), resultTimeFormat.format(Result.Penalty.DNF.getValue(), Event.TimeFormat.MINUTES.getValue()));
		assertEquals(Result.Penalty.DNS.toString(), resultTimeFormat.format(Result.Penalty.DNS.getValue(), Event.TimeFormat.MINUTES.getValue()));
		assertEquals("1:43.36", resultTimeFormat.format(10336, Event.TimeFormat.MINUTES.getValue()));
		
		// multi bld format
		assertEquals(Result.Penalty.DNF.toString(), resultTimeFormat.format(Result.Penalty.DNF.getValue(), Event.TimeFormat.MULTI_BLD.getValue()));
		assertEquals(Result.Penalty.DNS.toString(), resultTimeFormat.format(Result.Penalty.DNS.getValue(), Event.TimeFormat.MULTI_BLD.getValue()));
		assertEquals("3/4 25:37", resultTimeFormat.format(970153701, Event.TimeFormat.MULTI_BLD.getValue()));
	}
	
	/**
	 * Test method for {@link dk.cubing.liveresults.utilities.ResultTimeFormat#format(double)}.
	 */
	@Test
	public void testFormatDouble() {
		// seconds format
		assertEquals("7.08", resultTimeFormat.format(7.08));
		
		// minutes format
		//assertEquals("1:01.08", resultTimeFormat.format(61.08));
		//FIXME: this test breaks the formatter
	}

	/**
	 * Test method for {@link dk.cubing.liveresults.utilities.ResultTimeFormat#format(java.util.Date)}.
	 */
	@Test
	public void testFormatDate() {
		// TODO: add more test cases
	}

	/**
	 * Test method for {@link dk.cubing.liveresults.utilities.ResultTimeFormat#formatNumber(double)}.
	 */
	@Test
	public void testFormatNumber() {
		assertEquals(Result.Penalty.DNF.toString(), resultTimeFormat.formatNumber(Result.Penalty.DNF.getValue()));
		assertEquals(Result.Penalty.DNF.toString(), resultTimeFormat.formatNumber(0));
		assertEquals(Result.Penalty.DNF.toString(), resultTimeFormat.formatNumber(Integer.MIN_VALUE));
		assertEquals(Result.Penalty.DNS.toString(), resultTimeFormat.formatNumber(Result.Penalty.DNS.getValue()));
		assertEquals(Integer.toString(Integer.MAX_VALUE), resultTimeFormat.formatNumber(Integer.MAX_VALUE));
	}

	/**
	 * Test method for {@link dk.cubing.liveresults.utilities.ResultTimeFormat#formatDateToInt(java.util.Date)}.
	 */
	@Test
	public void testFormatDateToInt() {
		try {
			assertNotNull(resultTimeFormat.formatDateToInt(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} 
		// TODO: add more test cases
	}
	
	/**
	 * Test method for {@link dk.cubing.liveresults.utilities.ResultTimeFormat#formatRank(int)}.
	 */
	@Test
	public void testFormatRank() {
		assertEquals("0", resultTimeFormat.formatRank(0));
		assertEquals("4", resultTimeFormat.formatRank(4));
		assertEquals("1st place in the", resultTimeFormat.formatRank(1));
		assertEquals("2nd place in the", resultTimeFormat.formatRank(2));
		assertEquals("3rd place in the", resultTimeFormat.formatRank(3));
	}
	
	/**
	 * Test method for {@link dk.cubing.liveresults.utilities.ResultTimeFormat#formatRank(int)}.
	 */
	@Test
	public void testFormatDanishRank() {
		assertEquals("0", resultTimeFormat.formatDanishRank(0, false));
		assertEquals("4", resultTimeFormat.formatDanishRank(4, false));
		assertEquals("1. plads i", resultTimeFormat.formatDanishRank(1, false));
		assertEquals("Dansk Mester i", resultTimeFormat.formatDanishRank(1, true));
		assertEquals("2. plads i", resultTimeFormat.formatDanishRank(2, false));
		assertEquals("3. plads i", resultTimeFormat.formatDanishRank(3, false));
	}
}
