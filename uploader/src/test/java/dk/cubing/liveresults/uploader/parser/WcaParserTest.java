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
package dk.cubing.liveresults.uploader.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dk.cubing.liveresults.model.Competition;

/**
 * @author mohr
 *
 */
public class WcaParserTest {

	private WcaParser parser;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// get bean
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"uploaderContext-test.xml"}); 
		parser = (WcaParser) context.getBean("wcaParser");
	}

	/**
	 * Test method for {@link dk.cubing.liveresults.uploader.parser.WcaParser#isValidSpreadsheet(org.apache.poi.ss.usermodel.Workbook)}.
	 */
	@Test
	public void testIsValidSpreadsheet() throws Exception {
		assertTrue(parser.isValidSpreadsheet(new File(ClassLoader.getSystemResource("results.xls").getFile())));
		assertFalse(parser.isValidSpreadsheet(new File(ClassLoader.getSystemResource("invalid.xls").getFile())));
		assertTrue(parser.isValidSpreadsheet(new File(ClassLoader.getSystemResource("unofficial.xls").getFile())));
	}
	
	/**
	 * Test method for the unofficial event "3 in row"
	 */
	@Test
	public void testParse3inRow() throws Exception {
		Competition competition = new Competition();
		competition.setName("Unofficial Open 2011");
		Competition parsed = parser.parse(competition, ClassLoader.getSystemResource("unofficial.xls").getFile());
		
		assertNotNull(parsed);
		
		assertNotNull(parsed.getCompetitors());
		assertEquals(1, parsed.getCompetitors().size());
		
		assertNotNull(parsed.getEvents());
		assertEquals(2, parsed.getEvents().size());
	}

}
