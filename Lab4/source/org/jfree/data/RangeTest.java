package org.jfree.data;

import java.security.InvalidParameterException;

import junit.framework.TestCase;

import org.junit.Test;



public class RangeTest extends TestCase {

	@Test
	public void testNomalRange() {
		Range range1 = new Range(0, 5);
		double lower = range1.getLowerBound();
		double upper = range1.getUpperBound();
		assertTrue(lower == 0);
		assertTrue(upper == 5);
	}

	@Test
	public void testrangeEqualValues() {
		Range range1 = new Range(5, 5);
		double lower = range1.getLowerBound();
		double upper = range1.getUpperBound();
		assertTrue(lower == 5);
		assertTrue(upper == 5);
	}

	@Test
	public void testrangeOppositeValues() {
		boolean testPassed = false;
		Range range1;
		try {
			range1 = new Range(5, 0);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}

	@Test
	public void testrangeOneOverlap() {
		boolean testPassed = false;
		Range range1;
		try {
			range1 = new Range(5, 4);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}

	@Test
	public void testcombineFirstRangeNullSecondNot() {
		boolean testPassed = false;
		Range range1 = null;
		Range range2 = new Range(1.0, 2.0);
		Range range3;
		try {
			range3 = Range.combine(range1, range2);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testcombineSecondRangeNullFirstNot() {
		boolean testPassed = false;
		Range range1 = new Range(1.0, 2.0);
		Range range2 = null;
		Range range3;
		try {
			range3 = Range.combine(range1, range2);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testcombineBothRangesNull() {
		boolean testPassed = false;
		Range range1 = null;
		Range range2 = null;
		Range range3;
		try {
			range3 = Range.combine(range1, range2);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testrangeOneEqualsRangeTwo() {
		Range range1 = new Range(1.0, 2.0);
		Range range2 = new Range(1.0, 2.0);
		Range expected = new Range(1.0, 2.0);
		Range actual = Range.combine(range1, range2);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testcombineOverlappingRanges() {
		Range range1 = new Range(1.0, 3.0);
		Range range2 = new Range(2.0, 4.0);
		Range expected = new Range(1.0, 4.0);
		Range actual = Range.combine(range2, range1);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testcombineGapBetweenRanges() {
		Range range1 = new Range(1.0, 2.0);
		Range range2 = new Range(3.0, 4.0);
		Range expected = new Range(1.0, 4.0);
		Range actual = Range.combine(range2, range1);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testcombineNoOverlapNoIntegerGap() {
		Range range1 = new Range(1.0, 2.0);
		Range range2 = new Range(2.1, 4.0);
		Range expected = new Range(1.0, 4.0);
		Range actual = Range.combine(range2, range1);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testcombineOneNegativeRangeOnePositive() {
		Range range1 = new Range(-2.0, -1.0);
		Range range2 = new Range(1.0, 2.0);
		Range expected = new Range(-2.0, 2.0);
		Range actual = Range.combine(range2, range1);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testconstrainTenLessThanLowerBound() {
		Range range1 = new Range(20.0, 100.0);
		boolean testPassed = false;
		try {
			double actual = range1.constrain(10.0);
		} catch (Exception e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testconstrainOneLessThanLowerBound() {
		Range range1 = new Range(20.0, 100.0);
		boolean testPassed = false;
		try {
			double actual = range1.constrain(19.0);
		} catch (Exception e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testconstrainOneMoreThanUpperBound() {
		Range range1 = new Range(20.0, 100.0);
		boolean testPassed = false;
		try {
			double actual = range1.constrain(101.0);
		} catch (Exception e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void constrainTenMoreThanUpperBound() {
		Range range1 = new Range(20.0, 100.0);
		boolean testPassed = false;
		try {
			double actual = range1.constrain(110.0);
		} catch (Exception e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testconstrainUpperBound() {
		Range range1 = new Range(20.0, 100.0);
		double actual = range1.constrain(100.0);
		assertEquals(100.0, actual, 0);
	}

	@Test
	public void testconstrainLowerBound() {
		Range range1 = new Range(20.0, 100.0);
		double actual = range1.constrain(20.0);
		assertEquals(20.0, actual, 0);
	}

	@Test
	public void testconstrainCentralValue() {
		Range range1 = new Range(0.0, 100.0);
		double actual = range1.constrain(50.0);
		assertEquals(50.0, actual, 0);
	}

	@Test
	public void testcontainsOneLessThanLowerBound() {
		Range range1 = new Range(1.0, 10.0);
		boolean testPassed = range1.contains(9.0);
		assertEquals(true, testPassed);
	}

	@Test
	public void testcontainsLowerBound() {
		Range range1 = new Range(1.0, 10.0);
		boolean testPassed = range1.contains(1.0);
		assertEquals(true, testPassed);
	}

	@Test
	public void testcontainsCenterOfRange() {
		Range range1 = new Range(1.0, 9.0);
		boolean testPassed = range1.contains(5.0);
		assertEquals(true, testPassed);
	}

	@Test
	public void testcontainsUpperBound() {
		Range range1 = new Range(1.0, 10.0);
		boolean testPassed = range1.contains(10.0);
		assertEquals(true, testPassed);
	}

	@Test
	public void testcontainsOneOverUpperBound() {
		Range range1 = new Range(1.0, 10.0);
		boolean testPassed = !range1.contains(11.0);
		assertEquals(true, testPassed);
	}


	@Test
	public void testexpandToIncludeTenLessThanLowerBound() {
		Range expected = new Range(80, 100.0);
		Range actual = Range.expandToInclude(expected, 70.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testexpandToIncludeOneLessThanLowerBound() {
		Range expected = new Range(80, 100.0);
		Range actual = Range.expandToInclude(expected, 79.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testexpandToIncludeLowerBound() {
		Range expected = new Range(1.0, 100.0);
		Range actual = Range.expandToInclude(expected, 1.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testexpandToIncludeCentralValue() {
		Range expected = new Range(0.0, 100.0);
		Range actual = Range.expandToInclude(expected, 50.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testexpandToIncludeUpperBound() {
		Range expected = new Range(0.0, 100.0);
		Range actual = Range.expandToInclude(expected, 100.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testexpandToIncludeOneOverUpperBound() {
		Range range1 = new Range(0.0, 100.0);
		Range expected = new Range(0.0, 101.0);
		Range actual = Range.expandToInclude(range1, 101.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testexpandToIncludeTenOverUpperBound() {
		Range range1 = new Range(0.0, 100.0);
		Range expected = new Range(0.0, 110.0);
		Range actual = Range.expandToInclude(range1, 110.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testexpandToIncludeNullRange() {
		boolean testPassed = false;
		Range range1 = null;
		try {
			Range actual = Range.expandToInclude(range1, 1);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}


	@Test
	public void testshiftNegShiftLeft() {
		Range range1 = new Range(-10.0, -5);
		Range actual = Range.shift(range1, -1);
		Range expected = new Range(-11.0, -6);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftZeroShiftRightLegal() {
		Range range1 = new Range(0.0, 5.0);
		Range actual = Range.shift(range1, 1);
		Range expected = new Range(1.0, 6.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftNegShiftRightLegal() {
		Range range1 = new Range(-10.0, -5);
		Range actual = Range.shift(range1, 1);
		Range expected = new Range(-9.0, -4);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftNegShiftRightIllegal() {
		Range range1 = new Range(-10.0, -5);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, 7.0);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftNegShiftForUpperEqualsOne() {
		Range range1 = new Range(-10.0, -5);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, 6.0);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftNegShiftForUpperEqualsZero() {
		Range range1 = new Range(-10.0, -5.0);
		Range actual = Range.shift(range1, 5.0);
		Range expected = new Range(-5.0, 0.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftPosShiftRight() {
		Range range1 = new Range(5.0, 10.0);
		Range actual = Range.shift(range1, 1);
		Range expected = new Range(6.0, 11);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftPosShiftLeftLegal() {
		Range range1 = new Range(5.0, 10.0);
		Range actual = Range.shift(range1, -1);
		Range expected = new Range(4.0, 9.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftPosShiftLeftIllegal() {
		Range range1 = new Range(5.0, 10.0);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, -7.0);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftPosShiftForLowerEqualMinusOne() {
		Range range1 = new Range(5.0, 10.0);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, -6.0);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftPosShiftForLowerEqualZero() {
		Range range1 = new Range(5.0, 10.0);
		Range actual = Range.shift(range1, -5.0);
		Range expected = new Range(0.0, 5.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftCenterShiftLeftLegal() {
		Range range1 = new Range(-5.0, 5.0);
		Range actual = Range.shift(range1, -1.0);
		Range expected = new Range(-6.0, 4.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftCenterShiftRightLegal() {
		Range range1 = new Range(-5.0, 5.0);
		Range actual = Range.shift(range1, 1.0);
		Range expected = new Range(-4.0, 6.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftCenterShiftLeftIllegal() {
		Range range1 = new Range(-5.0, 5.0);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, -6.0);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftCenterShiftRightIllegal() {
		Range range1 = new Range(-5.0, 5.0);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, 6.0);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftCenterNoShift() {
		Range expected = new Range(-5.0, 5.0);
		Range actual = Range.shift(expected, 0.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftZeroCrossingNegShiftLeft() {
		Range range1 = new Range(-10.0, -5);
		Range actual = Range.shift(range1, -1, true);
		Range expected = new Range(-11.0, -6);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftZeroCrossingNegShiftRightLegal() {
		Range range1 = new Range(-10.0, -5);
		Range actual = Range.shift(range1, 1, true);
		Range expected = new Range(-9.0, -4);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftZeroCrossingNegShiftRightIllegal() {
		Range range1 = new Range(-10.0, -5);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, 7.0, true);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftZeroCrossingNegShiftForUpperEqualsOne() {
		Range range1 = new Range(-10.0, -5);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, 6.0, true);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftZeroCrossingNegShiftForUpperEqualsZero() {
		Range range1 = new Range(-10.0, -5.0);
		Range actual = Range.shift(range1, 5.0, true);
		Range expected = new Range(-5.0, 0.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void shiftZeroCrossingPosShiftRight() {
		Range range1 = new Range(5.0, 10.0);
		Range actual = Range.shift(range1, 1, true);
		Range expected = new Range(6.0, 11);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftZeroCrossingPosShiftLeftLegal() {
		Range range1 = new Range(5.0, 10.0);
		Range actual = Range.shift(range1, -1, true);
		Range expected = new Range(4.0, 9.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftZeroCrossingPosShiftLeftIllegal() {
		Range range1 = new Range(5.0, 10.0);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, -7.0, true);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftZeroCrossingPosShiftForLowerEqualMinusOne() {
		Range range1 = new Range(5.0, 10.0);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, -6.0, true);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftZeroCrossingPosShiftForLowerEqualZero() {
		Range range1 = new Range(5.0, 10.0);
		Range actual = Range.shift(range1, -5.0, true);
		Range expected = new Range(0.0, 5.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftZeroCrossingCenterShiftLeftLegal() {
		Range range1 = new Range(-5.0, 5.0);
		Range actual = Range.shift(range1, -1.0, true);
		Range expected = new Range(-6.0, 4.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftZeroCrossingCenterShiftRightLegal() {
		Range range1 = new Range(-5.0, 5.0);
		Range actual = Range.shift(range1, 1.0, true);
		Range expected = new Range(-4.0, 6.0);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testshiftZeroCrossingCenterShiftLeftIllegal() {
		Range range1 = new Range(-5.0, 5.0);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, -6.0, true);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftZeroCrossingCenterShiftRightIllegal() {
		Range range1 = new Range(-5.0, 5.0);
		boolean testPassed = false;
		Range actual;
		try {
			actual = Range.shift(range1, 6.0, true);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertEquals("Method should throw exception.", true, testPassed);
		}
	}

	@Test
	public void testshiftZeroCrossingCenterNoShift() {
		Range expected = new Range(-5.0, 5.0);
		Range actual = Range.shift(expected, 0.0, true);
		boolean testPassed = false;
		testPassed = actual.equals(expected);
		assertEquals(true, testPassed);
	}

	@Test
	public void testequalsNullPassed() {
		Range range1 = null;
		Range range2 = new Range(0, 5);
		assertTrue(!range2.equals(range1));
	}

	@Test
	public void testequalsNonInstanceOfRange() {
		String str = new String("test");
		Range range1 = new Range(0, 5);
		assertTrue(!range1.equals(str));
	}

	@Test
	public void testequalsNotEquivalentRange() {
		Range range1 = new Range(0, 4);
		Range range2 = new Range(1, 5);
		assertTrue(!range1.equals(range2));
	}

	@Test
	public void testequalsUpperEquivalentLowerNot() {
		Range range1 = new Range(0, 5);
		Range range2 = new Range(1, 5);
		assertTrue(!range1.equals(range2));
	}

	@Test
	public void testequalsLowerEquivalentUpperNot() {
		Range range1 = new Range(0, 4);
		Range range2 = new Range(0, 5);
		assertTrue(!range1.equals(range2));
	}

	@Test
	public void testexpandNullPassed() {
		boolean testPassed = false;
		Range range1 = null;
		try {
			Range.expand(range1, 0.25, 0.5);
		} catch (InvalidParameterException e) {
			testPassed = true;
		} finally {
			assertTrue(testPassed);
		}
	}

	@Test
	public void testexpandNormalRangeZeroUpperLower() {
		Range actual = new Range(2, 6);
		Range expected = new Range(2, 6);
		Range.expand(actual, 0, 0);
		assertTrue(actual.equals(expected));
	}

	@Test
	public void testexpandNormalRangeZeroLowerPointFiveUpper() {
		Range actual = new Range(2, 6);
		Range expected = new Range(2, 8);
		Range.expand(actual, 0, 0.5);
		assertTrue(actual.equals(expected));
	}

	@Test
	public void testexpandNormalRangeZeroUpperPointFiveLower() {
		Range actual = new Range(2, 6);
		Range expected = new Range(0, 6);
		Range.expand(actual, 0.5, 0);
		assertTrue(actual.equals(expected));
	}

	@Test
	public void testexpandNormalRangeUpperLowerAboveZero() {
		Range actual = new Range(2, 6);
		Range expected = new Range(1, 8);
		Range.expand(actual, 0.25, 0.5);
		assertTrue(actual.equals(expected));
	}

	@Test
	public void testexpandZeroRangeUpperLowerAboveZero() {
		Range actual = new Range(0, 0);
		Range expected = new Range(0, 0);
		Range.expand(actual, 0.25, 0.5);
		assertTrue(actual.equals(expected));
	}

	@Test
	public void testgetCentralValueZeroRange() {
		Range range1 = new Range(0, 0);
		assertTrue(range1.getCentralValue() == 0);
	}

	@Test
	public void testgetCentralValueUpperLowerOneApart() {
		Range range1 = new Range(0, 1);
		assertTrue(range1.getCentralValue() == 0.5);
	}

	@Test
	public void testgetCentralValueUpperLowerTwoApart() {
		Range range1 = new Range(0, 2);
		assertTrue(range1.getCentralValue() == 1);
	}

	@Test
	public void testgetCentralValueNegToPosRange() {
		Range range1 = new Range(-2, 2);
		assertTrue(range1.getCentralValue() == 0);
	}

	@Test
	public void testgetLengthZeroRange() {
		Range range1 = new Range(0, 0);
		assertTrue(range1.getLength() == 0);
	}

	@Test
	public void testgetLengthNegToPosRange() {
		Range range1 = new Range(-1, 2);
		assertTrue(range1.getLength() == 3);
	}

	@Test
	public void testgetLengthPosToPosRange() {
		Range range1 = new Range(1, 3);
		assertTrue(range1.getLength() == 2);
	}

	@Test
	public void testgetLowerBoundZeroRange() {
		Range range1 = new Range(0, 0);
		assertTrue(range1.getLowerBound() == 0);
	}

	@Test
	public void testgetLowerBoundNonZeroRange() {
		Range range1 = new Range(1, 5);
		assertTrue(range1.getLowerBound() == 1);
	}

	@Test
	public void testgetUpperBoundZeroRange() {
		Range range1 = new Range(0, 0);
		assertTrue(range1.getUpperBound() == 0);
	}

	@Test
	public void testgetUpperBoundNonZeroRange() {
		Range range1 = new Range(1, 5);
		assertTrue(range1.getUpperBound() == 5);
	}

	@Test
	public void testhashCodeReturnValueIsInt() {
		boolean testPassed = true;
		Range range1 = new Range(1, 5);
		int i;
		try {
			i = range1.hashCode();
		} catch (InvalidParameterException e) {
			testPassed = false;
		} finally {
			assertTrue(testPassed);
		}
	}

	@Test
	public void testintersectsOverlapping() {
		Range range1 = new Range(1, 5);
		boolean testResult = range1.intersects(3, 7);
		assertTrue(testResult);
	}

	@Test
	public void testintersectsNotOverlapping() {
		Range range1 = new Range(1, 5);
		boolean testResult = range1.intersects(7, 9);
		assertTrue(!testResult);
	}

	@Test
	public void testintersectsEqual() {
		Range range1 = new Range(1, 5);
		boolean testResult = range1.intersects(1, 5);
		assertTrue(testResult);
	}

	@Test
	public void testintersectsOneApart() {
		Range range1 = new Range(1, 5);
		boolean testResult = range1.intersects(6, 7);
		assertTrue(!testResult);
	}

	@Test
	public void testintersectsOverlapByOne() {
		Range range1 = new Range(1, 5);
		boolean testResult = range1.intersects(4, 7);
		assertTrue(testResult);
	}

	@Test
	public void testintersectsLowerGreaterThanUpper() {
		Range range1 = new Range(1, 5);
		boolean testResult = false;
		try {
			testResult = range1.intersects(7, 6);
		} catch (InvalidParameterException e) {
			testResult = true;
		} finally {
			assertTrue(testResult);
		}
	}

	@Test
	public void testtoStringZeroRange() {
		Range range1 = new Range(0, 0);
		String expected = new String("Range[0,0]");
		assertTrue(range1.toString() == expected);
	}

	@Test
	public void testtoStringPositiveRange() {
		Range range1 = new Range(1, 5);
		String expected = new String("Range[1,5]");
		assertTrue(range1.toString() == expected);
	}
}
