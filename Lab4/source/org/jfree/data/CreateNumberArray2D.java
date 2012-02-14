package org.jfree.data;

import static org.junit.Assert.assertEquals;

import java.security.InvalidParameterException;

import org.junit.Test;


public class CreateNumberArray2D {
	
	@Test
	public void nullDataObjectPassed() {
		boolean testPassed = false;
		
		try {
			DataUtilities.createNumberArray2D(null);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}
	
	@Test
	public void oneByOneArrayPassed() {
		double [][] input = {{2.5}};
		Number [][] expected = {{2.5}};
		Number [][] actual = DataUtilities.createNumberArray2D(input);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}
	
	@Test
	public void oneByTenElementArrayPassed() {
		double [][] input = {{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5}};
		Number [][] expected = {{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5}};
		Number [][] actual = DataUtilities.createNumberArray2D(input);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}
	
	@Test
	public void tenByOneElementArrayPassed() {
		double [][] input = {
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
			};
		Number [][] expected = {
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
				{2.5},
			};
		Number [][] actual = DataUtilities.createNumberArray2D(input);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}
	
	@Test
	public void tenByTenElementArrayPassed() {
		double [][] input = {
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5}
				};
		Number [][] expected = {
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5},
				{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5}
				};
		Number [][] actual = DataUtilities.createNumberArray2D(input);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}
}
