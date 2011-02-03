/**
 * Copyright (C) 2010 Mads Mohr Christensen, <hr.mohr@gmail.com>
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
package dk.cubing.liveresults.xalan;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.gnehzr.cct.scrambles.Scramble;
import net.gnehzr.cct.scrambles.ScramblePlugin;
import net.gnehzr.cct.scrambles.ScrambleVariation;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.puzzletimer.scramblers.Scrambler;
import com.puzzletimer.scramblers.ScramblerProvider;

public class ScramblesExt {
	
	private String FO_NS = "http://www.w3.org/1999/XSL/Format";
	
	private ScramblerProvider scramblerProvider = new ScramblerProvider();
	private static Map<String, String> variantToProviderMap = new HashMap<String, String>();
	static {
		variantToProviderMap.put("2x2", "2x2x2-CUBE-RANDOM");
		variantToProviderMap.put("3x3", "RUBIKS-CUBE-RANDOM");
		variantToProviderMap.put("4x4", "4x4x4-CUBE-RANDOM");
		variantToProviderMap.put("5x5", "5x5x5-CUBE-RANDOM");
		variantToProviderMap.put("6x6", "6x6x6-CUBE-RANDOM");
		variantToProviderMap.put("7x7", "7x7x7-CUBE-RANDOM");
		variantToProviderMap.put("fm", "RUBIKS-CUBE-RANDOM");
		variantToProviderMap.put("oh", "RUBIKS-CUBE-RANDOM");
		variantToProviderMap.put("bf", "RUBIKS-CUBE-RANDOM");
		variantToProviderMap.put("bf4", "4x4x4-CUBE-RANDOM");
		variantToProviderMap.put("bf5", "5x5x5-CUBE-RANDOM");
		variantToProviderMap.put("feet", "RUBIKS-CUBE-RANDOM");
		variantToProviderMap.put("clk", "RUBIKS-CLOCK-RANDOM");
		variantToProviderMap.put("minx", "MEGAMINX-RANDOM");
		variantToProviderMap.put("sq1", "SQUARE-1-RANDOM");
		variantToProviderMap.put("pyr", "PYRAMINX-RANDOM");
		variantToProviderMap.put("mbf", "RUBIKS-CUBE-RANDOM");
	}
	
	public DocumentFragment generate(String variantParam, int unitSizeParam, String numberOfScramblesParam, String sameScrambleParam) throws SAXException {
		try {
			String variant = variantParam;
			int unitSize = unitSizeParam;
			int numberOfScrambles = Integer.parseInt(numberOfScramblesParam);
			boolean sameScramble = "true".equals(sameScrambleParam);
			
			Document doc = createDocument();
			
			Element table = doc.createElementNS(FO_NS, "table");
			table.setAttribute("table-layout", "fixed");
			table.setAttribute("width", "100%");
			addBorder(table);
			Element tableColumn1 = doc.createElementNS(FO_NS, "table-column");
			tableColumn1.setAttribute("column-width", "0.5cm");
			Element tableColumn2 = doc.createElementNS(FO_NS, "table-column");
			tableColumn2.setAttribute("column-width", "proportional-column-width(1)");
			Element tableColumn3 = doc.createElementNS(FO_NS, "table-column");
			tableColumn3.setAttribute("column-width", "10cm");
			table.appendChild(tableColumn1);
			table.appendChild(tableColumn2);
			table.appendChild(tableColumn3);
			
			Element tableBody = doc.createElementNS(FO_NS, "table-body");
			
			ScrambleVariation sv = ScramblePlugin.getBestMatchVariation(variant);
			Scrambler scrambler = scramblerProvider.get(variantToProviderMap.get(variant));
			String scrambleMoves = null;
			for (int i=1; i<=numberOfScrambles; i++) {
				
				String[] sequence = null;
				if (scrambleMoves == null) {
					sequence = scrambler.getNextScramble().getSequence();
					StringBuilder sb = new StringBuilder();
					for (String move : sequence) {
						sb.append(move);
						sb.append(" ");
					}
					scrambleMoves = sb.toString().trim();
				}
				
				Scramble scramble = sv.getScramblePlugin().newScramble(variant, scrambleMoves, sv.getScramblePlugin().getDefaultGeneratorGroup(sv), sv.getScramblePlugin().getEnabledPuzzleAttributes());
				if (sequence != null) {
					scramble.setSequence(sequence);
				}
				BufferedImage image = sv.getScramblePlugin().getScrambleImage(scramble, 5, unitSize, sv.getScramblePlugin().getColorScheme(false));
				File temp = File.createTempFile("rcp", ".png");
				temp.deleteOnExit();
				ImageIO.write(image, "png", temp);
				
				Element tableRow = doc.createElementNS(FO_NS, "table-row");
				
				Element tableCell1 = doc.createElementNS(FO_NS, "table-cell");
				tableCell1.setAttribute("display-align", "center");
				addBorder(tableCell1);
				Element scrambleNumber = doc.createElementNS(FO_NS, "block");
				scrambleNumber.setAttribute("text-align", "center");
				scrambleNumber.appendChild(doc.createTextNode(Integer.toString(i) + "."));
				tableCell1.appendChild(scrambleNumber);
				tableRow.appendChild(tableCell1);
				
				Element tableCell2 = doc.createElementNS(FO_NS, "table-cell");
				addBorder(tableCell2);
				tableCell2.setAttribute("display-align", "center");
				Element scrambleString = doc.createElementNS(FO_NS, "block");
				scrambleString.appendChild(doc.createTextNode(scrambleMoves));
				tableCell2.appendChild(scrambleString);
				tableRow.appendChild(tableCell2);
				
				Element tableCell3 = doc.createElementNS(FO_NS, "table-cell");
				addBorder(tableCell3);
				Element cellBlock = doc.createElementNS(FO_NS, "block");
				cellBlock.setAttribute("text-align", "end");
				Element scrambleImage = doc.createElementNS(FO_NS, "external-graphic");
				scrambleImage.setAttribute("src", "url('"+temp.getAbsolutePath()+"')");
				cellBlock.appendChild(scrambleImage);
				tableCell3.appendChild(cellBlock);
				tableRow.appendChild(tableCell3);
				
				tableBody.appendChild(tableRow);
				
				if (!sameScramble) scrambleMoves = null;
			}
			
			table.appendChild(tableBody);
			Element root = doc.getDocumentElement();
			root.appendChild(table);
			
			DocumentFragment frag = doc.createDocumentFragment();
	        frag.appendChild(doc.importNode(doc.getFirstChild(), true));
			return frag;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SAXException(e);
		}
	}

	private void addBorder(Element element) {
		element.setAttribute("border-style", "solid");
		element.setAttribute("border-width", "1pt");
		element.setAttribute("border-color", "#000000");
		element.setAttribute("border-collapse", "collapse");
	}

	private Document createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation domImpl = db.getDOMImplementation();
        return domImpl.createDocument(FO_NS, "block", null);
	}
}
