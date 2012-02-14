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
 * ArrangeParams.java
 * ------------------
 * (C) Copyright 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: ArrangeParams.java,v 1.4 2005/10/28 10:12:37 mungady Exp $
 *
 * Changes:
 * --------
 * 17-Jun-2005 : Version 1 (DG);
 * 
 */

package org.jfree.chart.block;

import java.util.List;

/**
 * A class for holding parameters to pass to the arrange() method in the 
 * Block class.
 */
public class ArrangeParams implements BlockSizeCalculatorList {

    /** A result object to reuse. */
    private ArrangeResult recyclableResult;
    
    /**
     * A flag that controls whether or not log output should be appended to 
     * the results object.
     */
    private boolean log;
    
    /** The width calculators, if any. */
    private List widthCalculators;
    
    /** The height calculators, if any. */
    private List heightCalculators;
    
    /**
     * Creates a new instance with no logging.
     */
    public ArrangeParams() {
        this(false);
    }
    
    /**
     * Creates a new default instance.
     */
    public ArrangeParams(boolean log) {
        this.recyclableResult = null;
        this.log = log;
        this.widthCalculators = new java.util.ArrayList();
        this.heightCalculators = new java.util.ArrayList();
    }
    
    /**
     * Returns a result object that can be reused.
     * 
     * @return A result (possibly <code>null</code>).
     */
    public ArrangeResult getRecyclableResult() {
        if (this.recyclableResult != null) {
            this.recyclableResult.setSize(Double.NaN, Double.NaN);
            this.recyclableResult.setMessages(null);
        }
        return this.recyclableResult;   
    }
    
    /**
     * Sets the result.
     * 
     * @param result  the result (<code>null</code> permitted).
     */
    public void setRecyclableResult(ArrangeResult result) {
        this.recyclableResult = result;   
    }
    
    /**
     * Adds a width calculator.
     * 
     * @param calculator  the calculator.
     */
    public void addWidthCalculator(BlockWidthCalculator calculator) {
        this.widthCalculators.add(calculator);
    }
    
    /**
     * Finds a width calculator for the given block.
     * 
     * @return The calculator, or <code>null</code> if none is found.
     */
    public BlockWidthCalculator findWidthCalculator(Block block) {
        BlockWidthCalculator result = null;
        for (int i = 0; i < this.widthCalculators.size(); i++) {
            BlockWidthCalculator bwc 
                = (BlockWidthCalculator) this.widthCalculators.get(i);
            if (bwc.canHandleBlock(block)) {
                result = bwc;
                break;
            }
        }
        return result;
    }
    
    /**
     * Adds a height calculator.
     * 
     * @param calculator  the calculator.
     */
    public void addHeightCalculator(BlockHeightCalculator calculator) {
        this.heightCalculators.add(calculator);
    }    
    
    /**
     * Finds a height calculator for the given block.
     * 
     * @return The calculator, or <code>null</code> if none is found.
     */
    public BlockHeightCalculator findHeightCalculator(Block block) {
        BlockHeightCalculator result = null;
        for (int i = 0; i < this.heightCalculators.size(); i++) {
            BlockHeightCalculator bhc 
                = (BlockHeightCalculator) this.heightCalculators.get(i);
            if (bhc.canHandleBlock(block)) {
                result = bhc;
                break;
            }
        }
        return result;
    }
    
    /**
     * Return the log flag status.
     * 
     * @return A boolean.
     */
    public boolean isLogEnabled() {
        return log;   
    }
    
}
