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
}
