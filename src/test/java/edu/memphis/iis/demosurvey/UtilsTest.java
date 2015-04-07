package edu.memphis.iis.demosurvey;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UtilsTest {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStringBlank() {
		assertTrue(Utils.isBlankString(null));
		assertTrue(Utils.isBlankString(""));
		assertTrue(Utils.isBlankString(" "));
		assertTrue(Utils.isBlankString("\t"));
		assertTrue(Utils.isBlankString("\r"));
		assertTrue(Utils.isBlankString("\n"));

		assertFalse(Utils.isBlankString("  a  "));
		assertFalse(Utils.isBlankString("a "));
		assertFalse(Utils.isBlankString(" a"));
		assertFalse(Utils.isBlankString("a"));
	}
}
