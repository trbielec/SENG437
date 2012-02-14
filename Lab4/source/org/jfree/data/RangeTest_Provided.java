package org.jfree.data;

import java.io.Serializable;

import junit.framework.TestCase;

public strictfp class RangeTest_Provided extends TestCase
{
	public void setUp()
	{
		testRange = new Range(1, 2);
	}

	public void tearDown()
	{
		testRange = null;
	}

	public void testIntersectsOne()
	{
		assertTrue(testRange.intersects(-1, 1.5));
	}

	public void testIntersectsTwo()
	{
		assertFalse(testRange.intersects(-1, 1));
	}

	public void testIntersectsThree()
	{
		assertFalse(testRange.intersects(1.1, 0));
	}

	public void testIntersectsFour()
	{
		assertFalse(testRange.intersects(1.2, 1.1));
	}

	public void testIntersectsFive()
	{
		assertTrue(testRange.intersects(1.5, 1.5));
	}

	private Range testRange;
}