/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * ------------------
 * DataUtilities.java
 * ------------------
 * (C) Copyright 2003-2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: DataUtilities.java,v 1.2 2005/05/17 12:24:00 mungady Exp $
 *
 * Changes
 * -------
 * 05-Mar-2003 : Version 1 (DG);
 * 03-Mar-2005 : Moved createNumberArray() and createNumberArray2D() methods
 *               from the DatasetUtilities class (DG);
 * 17-May-2005 : Added calculateColumnTotal() and calculateRowTotal() 
 *               methods (DG);
 * 
 */

package org.jfree.data;

import org.jfree.data.general.DatasetUtilities;

/**
 * Utility methods for use with some of the data classes (but not the datasets, 
 * see {@link DatasetUtilities}).
 */
public abstract class DataUtilities {

    /**
     * Returns the sum of the values in one column of the supplied data
     * table. With invalid input, a total of zero will be returned.
     * 
     * @param data  the table of values (<code>null</code> not permitted).
     * @param column  the column index (zero-based).
     * 
     * @return The sum of the values in the specified column.
     */
    public static double calculateColumnTotal(Values2D data, int column) {
        double total = 0.0;
        int rowCount = data.getRowCount();
        for (int r = 0; r < rowCount; r++) {
            Number n = data.getValue(r, column);
            if (n != null) {
                total += n.doubleValue();   
            }
        }
        return total;
    }
    
    /**
     * Returns the sum of the values in one row of the supplied data
     * table. With invalid input, a total of zero will be returned.
     * 
     * @param data  the table of values (<code>null</code> not permitted).
     * @param row  the row index (zero-based).
     * 
     * @return The total of the values in the specified row.
     */
    public static double calculateRowTotal(Values2D data, int row) {
        double total = 0.0;
        int columnCount = data.getColumnCount();
        for (int c = 0; c < columnCount; c++) {
            Number n = data.getValue(row, c);
            if (n != null) {
                total += n.doubleValue();   
            }
        }
        return total;
    }
    
    /**
     * Constructs an array of <code>Number</code> objects from an array of 
     * <code>double</code> primitives.
     *
     * @param data  An array of <code>double</code> primitives (<code>null</code> not permitted).
     *
     * @return An array of <code>Number</code> objects.
     */
    public static Number[] createNumberArray(double[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Null 'data' argument.");   
        }
        Number[] result = new Number[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = new Double(data[i]);
        }
        return result;
    }

    /**
     * Constructs an array of arrays of <code>Number</code> objects from a 
     * corresponding structure containing <code>double</code> primitives.
     *
     * @param data  An array of <code>double</code> primitives (<code>null</code> not permitted).
     *
     * @return An array of <code>Number</code> objects.
     */
    public static Number[][] createNumberArray2D(double[][] data) {
        if (data == null) {
            throw new IllegalArgumentException("Null 'data' argument.");   
        }
        int l1 = data.length;
        Number[][] result = new Number[l1][];
        for (int i = 0; i < l1; i++) {
            result[i] = createNumberArray(data[i]);
        }
        return result;
    }

    /**
     * Returns a {@link KeyedValues} instance that contains the cumulative 
     * percentage values for the data in another {@link KeyedValues} instance.
     * The cumulative percentage is each value's cumulative sum's portion of the sum of all
     * the values. <br>
     * eg: <br>
     * Input:<br>
     * <br>
     * Key&nbsp;&nbsp;Value<br>
     *  0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5<br>
     *  1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9<br>
     *  2&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2<br>
     *  <br>
     * Returns:<br>
     * <br>
     * Key&nbsp;&nbsp;Value<br>
     *  0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;0.3125 (5 / 16)<br>
     *  1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;0.875 ((5 + 9) / 16)<br>
     *  2&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.0 ((5 + 9 + 2) / 16)<br>
     * <p>
     * The percentages are values between 0.0 and 1.0 (where 1.0 = 100%).
     *
     * @param data  the data (<code>null</code> not permitted).
     *
     * @return The cumulative percentages.
     */
    public static KeyedValues getCumulativePercentages(KeyedValues data) {
        if (data == null) {
            throw new IllegalArgumentException("Null 'data' argument.");   
        }
        DefaultKeyedValues result = new DefaultKeyedValues();
        double total = 0.0;
        for (int i = 0; i < data.getItemCount(); i++) {
            Number v = data.getValue(i);
            if (v != null) {
                total = total + v.doubleValue();
            }
        }
        double runningTotal = 0.0;
        for (int i = 0; i < data.getItemCount(); i++) {
            Number v = data.getValue(i);
            if (v != null) {
                runningTotal = runningTotal + v.doubleValue() * 0.9d;
            }
            result.addValue(data.getKey(i), new Double(runningTotal / total));
        }
        return result;
    }

}
