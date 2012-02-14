package org.jfree.data;

import java.security.InvalidParameterException;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class DataUtilitiesTest extends TestCase {


	public void testCalculateColumnTotal_nullDataObjectPassed() {
		boolean testPassed = false;
		
		try {
			DataUtilities.calculateColumnTotal(null, 1);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}
	

	public void testCalculateColumnTotal_invalidDataPassed() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(2));
				one(values).getValue(0, 0); 
			    will(returnValue(true)); 
			    one(values).getValue(1, 0); 
			    will(returnValue(false));
			}
		});
		double result = DataUtilities.calculateColumnTotal(values, 0);
		assertEquals(0, result, 0.000000001d);
	}
	

	public void testCalculateColumnTotal_columnIsLastColumnInTable() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(3));
				one(values).getValue(0, 2); 
			    will(returnValue(2.4)); 
			    one(values).getValue(1, 2); 
			    will(returnValue(1.6));
			    one(values).getValue(2, 2); 
			    will(returnValue(3.4));
			}
		});
		double result = DataUtilities.calculateColumnTotal(values, 2);
		assertEquals(7.4, result, 0.000000001d);
	}
	

	public void testCalculateColumnTotal_columnIsFirstInTable() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(3));
				one(values).getValue(0, 0); 
			    will(returnValue(1.4)); 
			    one(values).getValue(1, 0); 
			    will(returnValue(1.6));
			    one(values).getValue(2, 0); 
			    will(returnValue(3.4));
			}
		});
		double result = DataUtilities.calculateColumnTotal(values, 0);
		assertEquals(6.4, result, 0.000000001d);
	}
	

	public void testCalculateColumnTotal_columnIsNegativeOne() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(3));
			}
		});
		double result = DataUtilities.calculateColumnTotal(values, -1);
		assertEquals(0, result, 0.000000001d);
	}
	

	public void testCalculateColumnTotal_columnIsOneMoreThanMax() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(3));
			}
		});
		double result = DataUtilities.calculateColumnTotal(values, 3);
		assertEquals(0, result, 0.000000001d);
	}
	

	public void testCalculateColumnTotal_columnIsCentralInTable() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(3));
				one(values).getValue(0, 1); 
			    will(returnValue(1.4)); 
			    one(values).getValue(1, 1); 
			    will(returnValue(1.6));
			    one(values).getValue(2, 1); 
			    will(returnValue(3.4));
			}
		});
		double result = DataUtilities.calculateColumnTotal(values, 1);
		assertEquals(6.4, result, 0.000000001d);
	}
	

	public void testCalculateRowTotal_nullDataObjectPassed() {
		boolean testPassed = false;
		
		try {
			DataUtilities.calculateRowTotal(null, 1);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}
	

	public void testCalculateRowTotal_invalidDataPassed() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getColumnCount();
				will(returnValue(2));
				one(values).getValue(0, 0); 
			    will(returnValue(true)); 
			    one(values).getValue(0, 1); 
			    will(returnValue(false));
			}
		});
		double result = DataUtilities.calculateRowTotal(values, 0);
		assertEquals(0, result, 0.000000001d);
	}
	

	public void testCalculateRowTotal_columnIsLastColumnInTable() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getColumnCount();
				will(returnValue(3));
				one(values).getValue(2, 0); 
			    will(returnValue(2.4)); 
			    one(values).getValue(2, 1); 
			    will(returnValue(1.6));
			    one(values).getValue(2, 2); 
			    will(returnValue(3.4));
			}
		});
		double result = DataUtilities.calculateRowTotal(values, 2);
		assertEquals(7.4, result, 0.000000001d);
	}
	

	public void testCalculateRowTotal_columnIsFirstInTable() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getColumnCount();
				will(returnValue(3));
				one(values).getValue(0, 0); 
			    will(returnValue(1.4)); 
			    one(values).getValue(0, 1); 
			    will(returnValue(1.6));
			    one(values).getValue(0, 2); 
			    will(returnValue(3.4));
			}
		});
		double result = DataUtilities.calculateRowTotal(values, 0);
		assertEquals(6.4, result, 0.000000001d);
	}
	

	public void testCalculateRowTotal_columnIsNegativeOne() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getColumnCount();
				will(returnValue(3));
			}
		});
		double result = DataUtilities.calculateRowTotal(values, -1);
		assertEquals(0, result, 0.000000001d);
	}
	

	public void testCalculateRowTotal_columnIsOneMoreThanMax() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getColumnCount();
				will(returnValue(3));
			}
		});
		double result = DataUtilities.calculateRowTotal(values, 3);
		assertEquals(0, result, 0.000000001d);
	}
	

	public void testCalculateRowTotal_columnIsCentralInTable() {
		Mockery mockingContext = new Mockery();
		final Values2D values = mockingContext.mock(Values2D.class);
		mockingContext.checking(new Expectations() {
			{
				one(values).getColumnCount();
				will(returnValue(3));
				one(values).getValue(1, 0); 
			    will(returnValue(1.4)); 
			    one(values).getValue(1, 1); 
			    will(returnValue(1.6));
			    one(values).getValue(1, 2); 
			    will(returnValue(3.4));
			}
		});
		double result = DataUtilities.calculateRowTotal(values, 1);
		assertEquals(6.4, result, 0.000000001d);
	}
	

	public void testcreateNumberArray_nullDataObjectPassed() {
		boolean testPassed = false;
		
		try {
			DataUtilities.createNumberArray(null);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}
	

	public void testcreateNumberArray_oneElementArrayPassed() {
		double [] input = {2.5};
		Number [] expected = {2.5};
		Number [] actual = DataUtilities.createNumberArray(input);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}
	

	public void testcreateNumberArray_tenElementArrayPassed() {
		double [] input = {2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5};
		Number [] expected = {2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5};
		Number [] actual = DataUtilities.createNumberArray(input);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}
	

	public void testcreateNumberArray2D_nullDataObjectPassed() {
		boolean testPassed = false;
		
		try {
			DataUtilities.createNumberArray2D(null);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}
	

	public void testcreateNumberArray2D_oneByOneArrayPassed() {
		double [][] input = {{2.5}};
		Number [][] expected = {{2.5}};
		Number [][] actual = DataUtilities.createNumberArray2D(input);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}
	

	public void testcreateNumberArray2D_oneByTenElementArrayPassed() {
		double [][] input = {{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5}};
		Number [][] expected = {{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5}};
		Number [][] actual = DataUtilities.createNumberArray2D(input);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}
	

	public void testcreateNumberArray2D_tenByOneElementArrayPassed() {
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
	

	public void testcreateNumberArray2D_tenByTenElementArrayPassed() {
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
	

	public void testGetCumulativePercentages_dataHasOneRow(){
		Mockery mockingContext = new Mockery();
		final KeyedValues values = mockingContext.mock(KeyedValues.class);
		mockingContext.checking(new Expectations() {
			{
				allowing(values).getItemCount();
				will(returnValue(1));
				allowing(values).getValue(0);
				will(returnValue(2));
				allowing(values).getKey(0);
				will(returnValue(1));
			}
		});
		
		KeyedValues result = DataUtilities.getCumulativePercentages(values);
		assertEquals(1.0, result.getValue(0));
	}
	

	public void testGetCumulativePercentages_dataHasFourRows(){
		Mockery mockingContext = new Mockery();
		final KeyedValues values = mockingContext.mock(KeyedValues.class);
		mockingContext.checking(new Expectations() {
			{
				allowing(values).getItemCount();
				will(returnValue(4));
				allowing(values).getValue(0);
				will(returnValue(2));
				allowing(values).getValue(1);
				will(returnValue(2));
				allowing(values).getValue(2);
				will(returnValue(2));
				allowing(values).getValue(3);
				will(returnValue(2));
				allowing(values).getKey(0);
				will(returnValue(0));
				allowing(values).getKey(1);
				will(returnValue(1));
				allowing(values).getKey(2);
				will(returnValue(2));
				allowing(values).getKey(3);
				will(returnValue(3));
			}
		});
		
		KeyedValues result = DataUtilities.getCumulativePercentages(values);
		assertEquals(0.25, result.getValue(0));
		assertEquals(0.5, result.getValue(1));
		assertEquals(0.75, result.getValue(2));
		assertEquals(1.0, result.getValue(3));
	}
	

	public void testGetCumulativePercentages_firstThreeRowsAreZero(){
		Mockery mockingContext = new Mockery();
		final KeyedValues values = mockingContext.mock(KeyedValues.class);
		mockingContext.checking(new Expectations() {
			{
				allowing(values).getItemCount();
				will(returnValue(4));
				allowing(values).getValue(0);
				will(returnValue(0));
				allowing(values).getValue(1);
				will(returnValue(0));
				allowing(values).getValue(2);
				will(returnValue(0));
				allowing(values).getValue(3);
				will(returnValue(2));
				allowing(values).getKey(0);
				will(returnValue(0));
				allowing(values).getKey(1);
				will(returnValue(1));
				allowing(values).getKey(2);
				will(returnValue(2));
				allowing(values).getKey(3);
				will(returnValue(3));
			}
		});
		
		KeyedValues result = DataUtilities.getCumulativePercentages(values);
		assertEquals(0.0, result.getValue(0));
		assertEquals(0.0, result.getValue(1));
		assertEquals(0.0, result.getValue(2));
		assertEquals(1.0, result.getValue(3));
	}
	

	public void testGetCumulativePercentages_lastThreeRowsAreZero(){
		Mockery mockingContext = new Mockery();
		final KeyedValues values = mockingContext.mock(KeyedValues.class);
		mockingContext.checking(new Expectations() {
			{
				allowing(values).getItemCount();
				will(returnValue(4));
				allowing(values).getValue(0);
				will(returnValue(2));
				allowing(values).getValue(1);
				will(returnValue(0));
				allowing(values).getValue(2);
				will(returnValue(0));
				allowing(values).getValue(3);
				will(returnValue(0));
				allowing(values).getKey(0);
				will(returnValue(0));
				allowing(values).getKey(1);
				will(returnValue(1));
				allowing(values).getKey(2);
				will(returnValue(2));
				allowing(values).getKey(3);
				will(returnValue(3));
			}
		});
		
		KeyedValues result = DataUtilities.getCumulativePercentages(values);
		assertEquals(1.0, result.getValue(0));
		assertEquals(1.0, result.getValue(1));
		assertEquals(1.0, result.getValue(2));
		assertEquals(1.0, result.getValue(3));
	}
	

	public void testGetCumulativePercentages_allRowsAreZero(){
		Mockery mockingContext = new Mockery();
		final KeyedValues values = mockingContext.mock(KeyedValues.class);
		mockingContext.checking(new Expectations() {
			{
				allowing(values).getItemCount();
				will(returnValue(4));
				allowing(values).getValue(0);
				will(returnValue(0));
				allowing(values).getValue(1);
				will(returnValue(0));
				allowing(values).getValue(2);
				will(returnValue(0));
				allowing(values).getValue(3);
				will(returnValue(0));
				allowing(values).getKey(0);
				will(returnValue(0));
				allowing(values).getKey(1);
				will(returnValue(1));
				allowing(values).getKey(2);
				will(returnValue(2));
				allowing(values).getKey(3);
				will(returnValue(3));
			}
		});
		
		KeyedValues result = DataUtilities.getCumulativePercentages(values);
		assertEquals(0.0 / 0.0, result.getValue(0));
		assertEquals(0.0 / 0.0, result.getValue(1));
		assertEquals(0.0 / 0.0, result.getValue(2));
		assertEquals(0.0 / 0.0, result.getValue(3));
	}
	
}
