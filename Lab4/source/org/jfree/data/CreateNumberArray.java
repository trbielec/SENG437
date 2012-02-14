package org.jfree.data;

import static org.junit.Assert.assertEquals;

import java.security.InvalidParameterException;

import org.junit.Test;


public class CreateNumberArray {
	
	@Test
	public void nullDataObjectPassed() {
		boolean testPassed = false;
		
		try {
			DataUtilities.createNumberArray(null);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}
	
	@Test
	public void oneElementArrayPassed() {
		double [] input = {2.5};
		Number [] expected = {2.5};
		Number [] actual = DataUtilities.createNumberArray(input);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}
	
	@Test
	public void tenElementArrayPassed() {
		double [] input = {2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5};
		Number [] expected = {2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5};
		Number [] actual = DataUtilities.createNumberArray(input);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}
}
