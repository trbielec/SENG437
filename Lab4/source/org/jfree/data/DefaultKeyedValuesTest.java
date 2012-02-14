package org.jfree.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jfree.util.SortOrder;
import org.junit.Test;


public class DefaultKeyedValuesTest {
	//tests for method 1
	@Test
	public void testConstructor() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		assertTrue(testDKV.getItemCount() == 0);
	}
	
	//tests for method 2
	@Test
	public void testGetItemCount_DKVHasZeroEntries() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		int expected = 0;
		int actual = testDKV.getItemCount();
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetItemCount_DKVHasOneEntry() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue(new Integer(0), 1.0);
		int expected = 1;
		int actual = testDKV.getItemCount();
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetItemCount_DKVHasFiveEntries() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		int expected = 5;
		int actual = testDKV.getItemCount();
		
		assertTrue(actual == expected);
	}
	
	//tests for method 3
	@Test
	public void testGetValue_indexIsZero() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		double expected = 0;
		double actual = (Double) testDKV.getValue(0);
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetValue_indexIsHighestInRange() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		double expected = 4;
		double actual = (Double) testDKV.getValue(4);
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetValue_indexIsOutOfBounds() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		boolean testPassed = false;
		try {
			testDKV.getValue(5);
		} catch(IndexOutOfBoundsException e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	//tests for method 4
	@Test
	public void testGetKey_indexIsZero() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		int expected = 0;
		int actual = (Integer) testDKV.getKey(0);
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetKey_indexIsHighestInRange() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		int expected = 4;
		int actual = (Integer) testDKV.getKey(4);
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetKey_indexIsOutOfBounds() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		boolean testPassed = false;
		try {
			testDKV.getKey(5);
		} catch(IndexOutOfBoundsException e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	//tests for method 5
	@Test
	public void testGetIndex_keyIsValid() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 1; i < 6; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		int expected = 2;
		int actual = (Integer) testDKV.getIndex(new Integer(3));
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetIndex_keyIsFoundInZerothElement() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 1; i < 6; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		int expected = 0;
		int actual = (Integer) testDKV.getIndex(new Integer(1));
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetIndex_keyIsFoundInLastElement() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 1; i < 6; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		int expected = 4;
		int actual = (Integer) testDKV.getIndex(new Integer(5));
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetIndex_keyNotInTable() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 1; i < 6; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		int expected = -1;
		int actual = (Integer) testDKV.getIndex(new Integer(12));
		
		assertTrue(actual == expected);
	}
	
	//tests for method 6
	@Test
	public void testGetKeys_emptyDKVUsed() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		
		int expected = 0;
		int actual = testDKV.getKeys().size();
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetKeys_oneElementDKVUsed() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue(new Integer(0), 1.0);
		
		int expected = 1;
		int actual = testDKV.getKeys().size();
		
		assertTrue(actual == expected);
		
		assertEquals(0, testDKV.getKeys().get(0));
	}
	
	@Test
	public void testGetKeys_fiveElementDKVUsed() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		int expected = 5;
		int actual = testDKV.getKeys().size();
		
		assertTrue(actual == expected);
		
		for(int i = 0; i < 5; i++) {
			assertEquals(i, testDKV.getKeys().get(i));
		}
	}
	
	//tests for method 7
	@Test
	public void testGetValue_validKeyNullValue() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		testDKV.addValue(new Integer(5), null);
			
		Double expected = null;
		Double actual = (Double) testDKV.getValue(new Integer(5));
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetValue_validKeyNonNullValue() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		double expected = 4.0;
		double actual = (Double) testDKV.getValue(new Integer(4));
		
		assertTrue(actual == expected);
	}
	
	@Test
	public void testGetValue_invalidKey() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
			
		boolean testPassed = false;
		
		try {
			testDKV.getValue(new Integer(5));
		} catch (UnknownKeyException e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	//tests for method 8
	@Test
	public void testAddValue_ValueAndKeyNotInTable() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.addValue((Integer)5, 10.0);
		
		double expected = 10.0;
		double actual = (Double) testDKV.getValue((Integer)5);
		
		assertTrue(expected == actual);
	}
	
	@Test
	public void testAddValue_NullKeyPassed() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		boolean testPassed = false;
		
		try {
			testDKV.addValue(null, 10.0);
		} catch (Exception e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	@Test
	public void testAddValue_ValueNotInTableKeyInTable() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.addValue((Integer)4, 10.0);
		
		double expected = 10.0;
		double actual = (Double) testDKV.getValue((Integer)4);
		
		assertTrue(expected == actual);
	}
	
	//tests for method 9
	@Test
	public void testAddValueOverloaded_ValueAndKeyNotInTable() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.addValue((Integer)5, (Number)10.0);
		
		Number expected = 10.0;
		Number actual = (Number) testDKV.getValue((Integer)5);
		
		assertTrue(expected.equals(actual));
	}
	
	@Test
	public void testAddValueOverloaded_NullKeyPassed() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		boolean testPassed = false;
		
		try {
			testDKV.addValue(null, (Number)10.0);
		} catch (Exception e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	@Test
	public void testAddValueOverloaded_ValueNotInTableKeyInTable() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.addValue((Integer)4, (Number)10.0);
		
		Double expected = 10.0;
		Double actual = (Double) testDKV.getValue((Integer)4);
		
		assertTrue(expected.equals(actual));
	}
	
	//tests for method 10
	@Test
	public void testSetValue_ValueAndKeyNotInTable() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.setValue((Integer)5, 10.0);
		
		double expected = 10.0;
		double actual = (Double) testDKV.getValue((Integer)5);
		
		assertTrue(expected == actual);
	}
	
	@Test
	public void testSetValue_NullKeyPassed() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		boolean testPassed = false;
		
		try {
			testDKV.setValue(null, 10.0);
		} catch (Exception e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	@Test
	public void testSetValue_ValueNotInTableKeyInTable() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.setValue((Integer)4, 10.0);
		
		double expected = 10.0;
		double actual = (Double) testDKV.getValue((Integer)4);
		
		assertTrue(expected == actual);
	}
	
	//tests for method 11
	@Test
	public void testSetValueOverloaded_ValueAndKeyNotInTable() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.setValue((Integer)5, (Number)10.0);
		
		Number expected = 10.0;
		Number actual = (Number) testDKV.getValue((Integer)5);
		
		assertTrue(expected.equals(actual));
	}
	
	@Test
	public void testSetValueOverloaded_NullKeyPassed() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		boolean testPassed = false;
		
		try {
			testDKV.setValue(null, (Number)10.0);
		} catch (Exception e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	@Test
	public void testSetValueOverloaded_ValueNotInTableKeyInTable() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.setValue((Integer)4, (Number)10.0);
		
		Double expected = 10.0;
		Double actual = (Double) testDKV.getValue((Integer)4);
		
		assertTrue(expected.equals(actual));
	}
	
	//tests for method 12
	@Test
	public void testRemoveValue_indexEqualsZero() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.removeValue(0);
		
		assertEquals(testDKV.getValue(0), 1.0);
	}
	
	@Test
	public void testRemoveValue_indexIsHighestIndex() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		boolean testPassed = false;
		
		try {
			testDKV.removeValue(4);
			testDKV.getValue(4);
		} catch (IndexOutOfBoundsException e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	@Test
	public void testRemoveValue_middleIndex() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.removeValue(2);
		
		assertEquals(testDKV.getValue(2), 3.0);
	}
	
	@Test
	public void testRemoveValue_indexOutOfBounds() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		boolean testPassed = false;
		
		try {
			testDKV.removeValue(5);
		} catch (IndexOutOfBoundsException e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	//tests for method 13
	@Test
	public void testRemoveValueOverloaded_indexEqualsZero() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.removeValue(new Integer(0));
		
		assertEquals(testDKV.getValue(0), 1.0);
	}
	
	@Test
	public void testRemoveValueOverloaded_indexIsHighestIndex() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		boolean testPassed = false;
		
		try {
			testDKV.removeValue(new Integer(4));
			testDKV.getValue(4);
		} catch (IndexOutOfBoundsException e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	@Test
	public void testRemoveValueOverloaded_middleIndex() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		testDKV.removeValue(new Integer(2));
		
		assertEquals(testDKV.getValue(2), 3.0);
	}
	
	@Test
	public void testRemoveValueOverloaded_indexOutOfBounds() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}
		
		DefaultKeyedValues test2 = null;
		try {
			test2 = (DefaultKeyedValues)testDKV.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		testDKV.removeValue(new Integer(5));
		
		test2.equals(testDKV); // make sure method did nothing to DKV
	}
	
	//tests for method 14
	@Test 
	public void testSortByKeys_nullOrder() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		
		boolean testPassed = false;
		
		try {
			testDKV.sortByKeys(null);
		} catch(IllegalArgumentException e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	@Test 
	public void testSortByKeys_emptySet() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		
		boolean testPassed = true;
		
		try {
			testDKV.sortByKeys(SortOrder.ASCENDING);
		} catch(Exception e) {
			testPassed = false;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	@Test 
	public void testSortByKeys_oneElement() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue((Integer) 1, 1);
		DefaultKeyedValues expected = (DefaultKeyedValues) testDKV.clone();
		testDKV.sortByKeys(SortOrder.ASCENDING);
		assertTrue(testDKV.equals(expected));
	}
	
	@Test 
	public void testSortByKeys_fiveElementsAscending() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue((Integer) 1, 1);
		testDKV.addValue((Integer) 3, 3);
		testDKV.addValue((Integer) 4, 4);
		testDKV.addValue((Integer) 2, 2);
		testDKV.addValue((Integer) 5, 5);
		
		DefaultKeyedValues expected = new DefaultKeyedValues();
		
		for(int i = 1; i <= 5; i++) {
			expected.addValue(new Integer(i), i);
		}
		
		testDKV.sortByKeys(SortOrder.ASCENDING);
		assertTrue(testDKV.equals(expected));
	}
	
	@Test 
	public void testSortByKeys_fiveElementsDescending() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue((Integer) 1, 1);
		testDKV.addValue((Integer) 3, 3);
		testDKV.addValue((Integer) 4, 4);
		testDKV.addValue((Integer) 2, 2);
		testDKV.addValue((Integer) 5, 5);
		
		DefaultKeyedValues expected = new DefaultKeyedValues();
		
		for(int i = 1; i <= 5; i++) {
			expected.addValue(new Integer(6-i), 6-i);
		}
		
		testDKV.sortByKeys(SortOrder.DESCENDING);
		assertTrue(testDKV.equals(expected));
	}
	
	//tests for method 15
	@Test 
	public void testSortByValues_nullOrder() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		
		boolean testPassed = false;
		
		try {
			testDKV.sortByValues(null);
		} catch(IllegalArgumentException e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	@Test 
	public void testSortByValues_emptySet() {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		
		boolean testPassed = true;
		
		try {
			testDKV.sortByValues(SortOrder.ASCENDING);
		} catch(Exception e) {
			testPassed = false;
		} finally {
			assertTrue(testPassed);
		}
	}
	
	@Test 
	public void testSortByValues_oneElement() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue((Integer) 1, 1);
		DefaultKeyedValues expected = (DefaultKeyedValues) testDKV.clone();
		testDKV.sortByValues(SortOrder.ASCENDING);
		assertTrue(testDKV.equals(expected));
	}
	
	@Test 
	public void testSortByValues_fiveElementsAscending() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue((Integer) 1, 1);
		testDKV.addValue((Integer) 3, 3);
		testDKV.addValue((Integer) 4, 4);
		testDKV.addValue((Integer) 2, 2);
		testDKV.addValue((Integer) 5, 5);
		
		DefaultKeyedValues expected = new DefaultKeyedValues();
		
		for(int i = 1; i <= 5; i++) {
			expected.addValue(new Integer(i), i);
		}
		
		testDKV.sortByValues(SortOrder.ASCENDING);
		assertTrue(testDKV.equals(expected));
	}
	
	@Test 
	public void testSortByValues_fiveElementsDescending() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue((Integer) 1, 1);
		testDKV.addValue((Integer) 3, 3);
		testDKV.addValue((Integer) 4, 4);
		testDKV.addValue((Integer) 2, 2);
		testDKV.addValue((Integer) 5, 5);
		
		DefaultKeyedValues expected = new DefaultKeyedValues();
		
		for(int i = 1; i <= 5; i++) {
			expected.addValue(new Integer(6-i), 6-i);
		}
		
		testDKV.sortByValues(SortOrder.DESCENDING);
		assertTrue(testDKV.equals(expected));
	}
	
	@Test 
	public void testSortByValues_oneNullValueAmidFive() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue((Integer) 1, 1);
		testDKV.addValue((Integer) 3, 3);
		testDKV.addValue((Integer) 4, 4);
		testDKV.addValue((Integer) 2, 2);
		testDKV.addValue((Integer) 5, null);
		
		DefaultKeyedValues expected = new DefaultKeyedValues();
		
		for(int i = 1; i < 5; i++) {
			expected.addValue(new Integer(i), i);
		}
		
		expected.addValue((Integer)5, null);
		
		testDKV.sortByValues(SortOrder.ASCENDING);
		assertTrue(testDKV.equals(expected));
	}
	
	@Test 
	public void testSortByValues_twoNullValuesAmidFiveAscending() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue((Integer) 1, 1);
		testDKV.addValue((Integer) 3, null);
		testDKV.addValue((Integer) 4, 4);
		testDKV.addValue((Integer) 2, 2);
		testDKV.addValue((Integer) 5, null);
		
		DefaultKeyedValues expected = new DefaultKeyedValues();
		
		expected.setValue((Integer)1, 1);
		expected.setValue((Integer)2, 2);
		expected.setValue((Integer)4, 4);
		expected.setValue((Integer)3, null);
		expected.addValue((Integer)5, null);
		
		testDKV.sortByValues(SortOrder.ASCENDING);
		assertTrue(testDKV.equals(expected));
	}
	
	@Test 
	public void testSortByValues_twoNullValuesAmidFiveDescending() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue((Integer) 1, 1);
		testDKV.addValue((Integer) 3, null);
		testDKV.addValue((Integer) 4, 4);
		testDKV.addValue((Integer) 2, 2);
		testDKV.addValue((Integer) 5, null);
		
		DefaultKeyedValues expected = new DefaultKeyedValues();
		
		expected.setValue((Integer)4, 4);
		expected.setValue((Integer)2, 2);
		expected.setValue((Integer)1, 1);
		expected.setValue((Integer)3, null);
		expected.addValue((Integer)5, null);
		
		testDKV.sortByValues(SortOrder.DESCENDING);
		assertTrue(testDKV.equals(expected));
	}
	
	@Test 
	public void testSortByValues_allNullValues() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		testDKV.addValue((Integer) 1, null);
		testDKV.addValue((Integer) 3, null);
		testDKV.addValue((Integer) 4, null);
		testDKV.addValue((Integer) 2, null);
		testDKV.addValue((Integer) 5, null);
		
		DefaultKeyedValues expected = (DefaultKeyedValues) testDKV.clone();
		
		testDKV.sortByValues(SortOrder.DESCENDING);
		assertTrue(testDKV.equals(expected));
	}
	
	//tests for method 16
	@Test
	public void testEquals_twoEqualObjects() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV1 = new DefaultKeyedValues();
		testDKV1.addValue((Integer) 1, 1);
		testDKV1.addValue((Integer) 3, 3);
		testDKV1.addValue((Integer) 4, 4);
		testDKV1.addValue((Integer) 2, 2);
		testDKV1.addValue((Integer) 5, 5);
		
		DefaultKeyedValues testDKV2 = (DefaultKeyedValues) testDKV1.clone();
		
		assertTrue(testDKV1.equals(testDKV2));
	}
	
	@Test
	public void testEquals_oneValueDifferent() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV1 = new DefaultKeyedValues();
		testDKV1.addValue((Integer) 1, 1);
		testDKV1.addValue((Integer) 3, 3);
		testDKV1.addValue((Integer) 4, 4);
		testDKV1.addValue((Integer) 2, 2);
		testDKV1.addValue((Integer) 5, 5);
		
		DefaultKeyedValues testDKV2 = (DefaultKeyedValues) testDKV1.clone();
		
		testDKV2.setValue((Integer) 3, 4);
		
		assertFalse(testDKV1.equals(testDKV2));
	}
	
	@Test
	public void testEquals_oneKeyDifferent() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV1 = new DefaultKeyedValues();
		testDKV1.addValue((Integer) 1, 1);
		testDKV1.addValue((Integer) 3, 3);
		testDKV1.addValue((Integer) 4, 4);
		testDKV1.addValue((Integer) 2, 2);
		testDKV1.addValue((Integer) 5, 5);
		
		DefaultKeyedValues testDKV2 = (DefaultKeyedValues) testDKV1.clone();
		
		testDKV1.addValue((Integer)6, 6);
		testDKV2.addValue((Integer)7, 6);
		
		assertFalse(testDKV1.equals(testDKV2));
	}
	
	@Test
	public void testEquals_noSimilarValues() {
		DefaultKeyedValues testDKV1 = new DefaultKeyedValues();
		testDKV1.addValue((Integer) 1, 1);
		testDKV1.addValue((Integer) 3, 3);
		testDKV1.addValue((Integer) 4, 4);
		testDKV1.addValue((Integer) 2, 2);
		testDKV1.addValue((Integer) 5, 5);
		
		DefaultKeyedValues testDKV2 = new DefaultKeyedValues();
		
		testDKV1.addValue((Integer) 1, 2);
		testDKV1.addValue((Integer) 3, 4);
		testDKV1.addValue((Integer) 4, 5);
		testDKV1.addValue((Integer) 2, 3);
		testDKV1.addValue((Integer) 5, 6);
		
		assertFalse(testDKV1.equals(testDKV2));
	}
	
	@Test
	public void testEquals_noSimilarKeys() {
		DefaultKeyedValues testDKV1 = new DefaultKeyedValues();
		testDKV1.addValue((Integer) 1, 1);
		testDKV1.addValue((Integer) 3, 3);
		testDKV1.addValue((Integer) 4, 4);
		testDKV1.addValue((Integer) 2, 2);
		testDKV1.addValue((Integer) 5, 5);
		
		DefaultKeyedValues testDKV2 = new DefaultKeyedValues();
		
		testDKV1.addValue((Integer) 2, 1);
		testDKV1.addValue((Integer) 4, 3);
		testDKV1.addValue((Integer) 5, 4);
		testDKV1.addValue((Integer) 3, 2);
		testDKV1.addValue((Integer) 6, 5);
		
		assertFalse(testDKV1.equals(testDKV2));
	}
	
	@Test
	public void testEquals_noSimilarities() {
		DefaultKeyedValues testDKV1 = new DefaultKeyedValues();
		testDKV1.addValue((Integer) 1, 1);
		testDKV1.addValue((Integer) 3, 3);
		testDKV1.addValue((Integer) 4, 4);
		testDKV1.addValue((Integer) 2, 2);
		testDKV1.addValue((Integer) 5, 5);
		
		DefaultKeyedValues testDKV2 = new DefaultKeyedValues();
		
		testDKV1.addValue((Integer) 2, 0);
		testDKV1.addValue((Integer) 4, 2);
		testDKV1.addValue((Integer) 5, 3);
		testDKV1.addValue((Integer) 3, 1);
		testDKV1.addValue((Integer) 6, 4);
		
		assertFalse(testDKV1.equals(testDKV2));
	}
	
	//tests for method 17 (no way to test hashcode method without knowing implementation of hashing function
	@Test
	public void testClone_noValues() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		DefaultKeyedValues test2 = (DefaultKeyedValues)testDKV.clone();
		
		assertTrue(testDKV.equals(test2));
	}
	
	@Test
	public void testClone_fiveValues() throws CloneNotSupportedException {
		DefaultKeyedValues testDKV = new DefaultKeyedValues();
		for(int i = 0; i < 5; i++) {
			testDKV.addValue(new Integer(i), i);
		}	
		DefaultKeyedValues test2 = (DefaultKeyedValues)testDKV.clone();
		
		assertTrue(testDKV.equals(test2));
	}
}
