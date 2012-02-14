package org.jfree.data;

import static org.junit.Assert.assertEquals;

import java.security.InvalidParameterException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;


public class CalculateRowTotal {
	@Test
	public void nullDataObjectPassed() {
		boolean testPassed = false;
		
		try {
			DataUtilities.calculateRowTotal(null, 1);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}
	
	@Test
	public void invalidDataPassed() {
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
	
	@Test
	public void columnIsLastColumnInTable() {
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
	
	@Test
	public void columnIsFirstInTable() {
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
	
	@Test
	public void columnIsNegativeOne() {
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
	
	@Test
	public void columnIsOneMoreThanMax() {
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
	
	@Test
	public void columnIsCentralInTable() {
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
}
