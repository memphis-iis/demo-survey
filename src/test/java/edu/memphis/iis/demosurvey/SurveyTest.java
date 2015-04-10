package edu.memphis.iis.demosurvey;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SurveyTest {
	protected Survey survey;

	@Before
	public void setUp() throws Exception {
		survey = new Survey();
	}

	@After
	public void tearDown() throws Exception {
		survey = null;
	}

	@Test
	public void testDefaults() {
		assertEquals(null, survey.getParticipantCode());
		assertEquals(null, survey.getFavoriteDogBreed());
		assertEquals(false, survey.isCatLover());
		assertEquals(0, survey.getFavoriteNumber());

		assertFalse(survey.isValid());
	}

	@Test
	public void testBadParticipantCode() {
		survey.setParticipantCode("");
		assertFalse(survey.isValid());
	}

	@Test
	public void testBasicOps() {
		survey.setParticipantCode("01");
		survey.setFavoriteDogBreed("Bassett Hound");
		survey.setCatLover(true); //Lie for testing
		survey.setFavoriteNumber(42);

		assertEquals("01", survey.getParticipantCode());
		assertEquals("Bassett Hound", survey.getFavoriteDogBreed());
		assertEquals(true, survey.isCatLover());
		assertEquals(42, survey.getFavoriteNumber());

		assertTrue(survey.isValid());
	}
}
