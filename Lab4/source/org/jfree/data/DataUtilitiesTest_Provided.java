package org.jfree.data;

import junit.framework.TestCase;

public class DataUtilitiesTest_Provided extends TestCase 
{
	public void setUp()
	{
		testValues = new DefaultKeyedValues2D();
	}
	
	public void testLoopNotExecutedColumnTotal()
	{
		assertEquals(0.0, DataUtilities.calculateColumnTotal(new DefaultKeyedValues2D(), 0), 0.0000001d);
	}
	
	public void testLoopExecutedOnceWithNullColumnTotal()
	{
		testValues.addValue(null, new Integer(0), new Integer(0));
		
		assertEquals(0.0, DataUtilities.calculateColumnTotal(testValues, 0), 0.0000001d);
	}
	
	public void testLoopExecutedMultipleTimesColumnTotal()
	{
		testValues.addValue(new Integer(1), new Integer(0), new Integer(0));
		testValues.addValue(new Integer(4), new Integer(1), new Integer(0));
		
		assertEquals(5.0, DataUtilities.calculateColumnTotal(testValues, 0), 0.0000001d);
	}
	
	public void testCalculateRowTotalZeroDataValidRow() {
		DefaultKeyedValues2D testVar = new DefaultKeyedValues2D();
		testVar.addValue(0, 0, 0);
		testVar.addValue(0, 0, 1);
		testVar.addValue(0, 0, 2);
		assertEquals(DataUtilities.calculateRowTotal(testVar, 0), 0.0);
	}

	private DefaultKeyedValues2D testValues;
}
