package org.jfree.data;

import static org.junit.Assert.assertEquals;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;


public class GetCumulativePercentages {
	
	@Test
	public void dataHasOneRow(){
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
	
	@Test
	public void dataHasFourRows(){
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
	
	@Test
	public void firstThreeRowsAreZero(){
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
	
	@Test
	public void lastThreeRowsAreZero(){
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
	
	@Test
	public void allRowsAreZero(){
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
