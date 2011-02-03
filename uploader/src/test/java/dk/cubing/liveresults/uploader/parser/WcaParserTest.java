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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author mohr
 *
 */
public class WcaParserTest {

	private Workbook workBook;
	private Workbook invalidWorkBook;
	private WcaParser parser;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		File resultsFile = new File(ClassLoader.getSystemResource("results.xls").getFile());
		FileInputStream fi = new FileInputStream(resultsFile);
		workBook = WorkbookFactory.create(fi);
		fi.close();
		
		resultsFile = new File(ClassLoader.getSystemResource("invalid.xls").getFile());
		fi = new FileInputStream(resultsFile);
		invalidWorkBook = WorkbookFactory.create(fi);
		fi.close();
		
		// get bean
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"uploaderContext-test.xml"}); 
		parser = (WcaParser) context.getBean("wcaParser");
	}

	/**
	 * Test method for {@link dk.cubing.liveresults.uploader.parser.WcaParser#isValidSpreadsheet(org.apache.poi.ss.usermodel.Workbook)}.
	 */
	@Test
	public void testIsValidSpreadsheet() {
		assertTrue(parser.isValidSpreadsheet(workBook));
		assertFalse(parser.isValidSpreadsheet(invalidWorkBook));
	}

}
