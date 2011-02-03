package net.gnehzr.cct.scrambles;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import net.gnehzr.cct.misc.Utils;
import net.gnehzr.cct.scrambles.Scramble.InvalidScrambleException;

public class ScramblePlugin {
	
	private static Map<String, String> scrambleMap = new HashMap<String, String>();
	static {
		scrambleMap.put("2x2", "CubeScramble");
		scrambleMap.put("3x3", "CubeScramble");
		scrambleMap.put("4x4", "CubeScramble");
		scrambleMap.put("5x5", "CubeScramble");
		scrambleMap.put("6x6", "CubeScramble");
		scrambleMap.put("7x7", "CubeScramble");
		scrambleMap.put("oh", "CubeScramble");
		scrambleMap.put("feet", "CubeScramble");
		scrambleMap.put("bf", "CubeScramble");
		scrambleMap.put("mbf", "CubeScramble");
		scrambleMap.put("fm", "CubeScramble");
		scrambleMap.put("bf4", "CubeScramble");
		scrambleMap.put("bf5", "CubeScramble");
		scrambleMap.put("clk", "ClockScramble");
		scrambleMap.put("minx", "MegaminxScramble");
		scrambleMap.put("sq1", "SquareOneScramble");
		scrambleMap.put("pyr", "PyraminxScramble");
	}
	
	private Scramble scramble;
	
	public ScramblePlugin(String variationName) {
		if ("CubeScramble".equals(scrambleMap.get(variationName))) {
			if ("2x2".equals(variationName)) {
				this.scramble = new CubeScramble("2x2x2", 25, "UD");
			} else if ("4x4".equals(variationName) || "bf4".equals(variationName)) {
				this.scramble = new CubeScramble("4x4x4", 40, "UD");
			} else if ("5x5".equals(variationName) || "bf5".equals(variationName)) {
				this.scramble = new CubeScramble("5x5x5", 60, "UD");
			} else if ("6x6".equals(variationName)) {
				this.scramble = new CubeScramble("6x6x6", 80, "UD");
			} else if ("7x7".equals(variationName)) {
				this.scramble = new CubeScramble("7x7x7", 100, "UD");
			} else {
				this.scramble = new CubeScramble("3x3x3", 25, "UD");
			}
		} else if ("ClockScramble".equals(scrambleMap.get(variationName))) {
			this.scramble = new ClockScramble("Clock", 10, "");
		} else if ("MegaminxScramble".equals(scrambleMap.get(variationName))) {
			this.scramble = new MegaminxScramble("Pochmann Megaminx", 70, "");
		} else if ("SquareOneScramble".equals(scrambleMap.get(variationName))) {
			this.scramble = new SquareOneScramble("Square-1", 40, "");
		} else if ("PyraminxScramble".equals(scrambleMap.get(variationName))) {
			this.scramble = new PyraminxScramble("Pyraminx", 25, "");
		}
	}

	public static ScrambleVariation getBestMatchVariation(String variant) {
		return new ScrambleVariation(new ScramblePlugin(variant), variant);
	}
	
	public Color[] getColorScheme(boolean defaults) {
		String[][] faceNamesColors = scramble.getFaceNamesColors();
		Color[] scheme = new Color[faceNamesColors[1].length];
		for (int i=0; i<faceNamesColors[1].length; i++) {
			scheme[i] = Utils.stringToColor(faceNamesColors[1][i], true);
		}
		return scheme;
	}

	public BufferedImage getScrambleImage(Scramble scramble, int gap, int unitSize, Color[] colorScheme) {
		if (scramble instanceof CubeScramble) {
			return ((CubeScramble) scramble).getScrambleImage(gap, unitSize, colorScheme);
		} else if (scramble instanceof PyraminxScramble) {
			return ((PyraminxScramble) scramble).getScrambleImage(gap, unitSize, colorScheme);
		} else if (scramble instanceof MegaminxScramble) {
			return ((MegaminxScramble) scramble).getScrambleImage(gap, unitSize, colorScheme);
		} else if (scramble instanceof SquareOneScramble) {
			return ((SquareOneScramble) scramble).getScrambleImage(gap, unitSize, colorScheme);
		} else if (scramble instanceof ClockScramble) {
			return ((ClockScramble) scramble).getScrambleImage(gap, unitSize, colorScheme);
		} else {
			return null;
		}
	}

	public String getDefaultGeneratorGroup(ScrambleVariation scrambleVariation) {
		if (scrambleVariation.getScramblePlugin().scramble instanceof CubeScramble) {
			return "UD";
		} else {
			return "";
		}
	}

	public String[] getEnabledPuzzleAttributes() {
		String[] attributes = new String[0];
		return attributes;
	}
	
	public int getLength() {
		return scramble.getLength();
	}

	public Scramble newScramble(String variationName, String scramble, String defaultGeneratorGroup, String[] enabledPuzzleAttributes) throws InvalidScrambleException {
		if ("CubeScramble".equals(scrambleMap.get(variationName))) {
			if ("2x2".equals(variationName)) {
				return new CubeScramble("2x2x2", scramble, defaultGeneratorGroup, enabledPuzzleAttributes);
			} else if ("4x4".equals(variationName) || "bf4".equals(variationName)) {
				return new CubeScramble("4x4x4", scramble, defaultGeneratorGroup, enabledPuzzleAttributes);
			} else if ("5x5".equals(variationName) || "bf5".equals(variationName)) {
				return new CubeScramble("5x5x5", scramble, defaultGeneratorGroup, enabledPuzzleAttributes);
			} else if ("6x6".equals(variationName)) {
				return new CubeScramble("6x6x6", scramble, defaultGeneratorGroup, enabledPuzzleAttributes);
			} else if ("7x7".equals(variationName)) {
				return new CubeScramble("7x7x7", scramble, defaultGeneratorGroup, enabledPuzzleAttributes);
			} else {
				return new CubeScramble("3x3x3", scramble, defaultGeneratorGroup, enabledPuzzleAttributes);
			}
		} else if ("ClockScramble".equals(scrambleMap.get(variationName))) {
			return new ClockScramble("Clock", scramble, defaultGeneratorGroup, enabledPuzzleAttributes);
		} else if ("MegaminxScramble".equals(scrambleMap.get(variationName))) {
			return new MegaminxScramble("Pochmann Megaminx", scramble, defaultGeneratorGroup, enabledPuzzleAttributes);
		} else if ("SquareOneScramble".equals(scrambleMap.get(variationName))) {
			return new SquareOneScramble("Square-1", scramble, defaultGeneratorGroup, enabledPuzzleAttributes);
		} else if ("PyraminxScramble".equals(scrambleMap.get(variationName))) {
			return new PyraminxScramble("Pyraminx", scramble, defaultGeneratorGroup, enabledPuzzleAttributes);
		}
		return null;
	}
}
