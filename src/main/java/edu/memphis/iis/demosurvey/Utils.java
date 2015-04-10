package edu.memphis.iis.demosurvey;

/**
 * Simple utility class for our demo project. Note that a real-world
 * project would use things like Apache Commons for these functions
 */
public class Utils {
	/**
	 * Return true if the given string is null, empty (""), or only
	 * whitespace
	 * @param s The string to evalulate
	 * @return True if string is blank (false otherwise)
	 */
	public static boolean isBlankString(String s) {
		return s == null || s.length() < 1 || s.trim().length() < 1;
	}

	/**
	 * If isBlankString(s) is true then return def else return s
	 * @param s the string to check
	 * @param def the default string to return
	 * @return the chosen string
	 */
	public static String defStr(String s, String def) {
		return isBlankString(s) ? def : s;
	}

	/**
	 * Override that uses that def=""
	 * @param s the string to to check
	 * @return the result of defStr(s, "")
	 */
	public static String defStr(String s) {
		return defStr(s, "");
	}
}
