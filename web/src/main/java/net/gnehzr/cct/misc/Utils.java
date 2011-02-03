package net.gnehzr.cct.misc;

import java.awt.Color;

public class Utils {
	
	public static String join(Object[] arr, String sep) {
		StringBuilder s = new StringBuilder();
		for(Object o : arr)
			s.append(sep + o.toString());
		return s.substring(sep.length());
	}
	
	public static Color stringToColor(String s, boolean nullIfInvalid) {
		try {
			return new Color(Integer.parseInt(s, 16));
		} catch(Exception e) {
			return nullIfInvalid ? null : Color.WHITE;
		}
	}

	private Utils() {}

}
