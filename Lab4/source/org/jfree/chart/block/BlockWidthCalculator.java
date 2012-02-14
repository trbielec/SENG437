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
 * -------------------------
 * BlockWidthCalculator.java
 * -------------------------
 * (C) Copyright 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: BlockWidthCalculator.java,v 1.1 2005/06/27 11:05:30 mungady Exp $
 *
 * Changes:
 * --------
 * 17-Jun-2005 : Version 1 (DG);
 * 
 */

package org.jfree.chart.block;

/**
 * A block width calculator is used to determine the unconstrained height
 * of a block during layout.
 */
public interface BlockWidthCalculator {
    
    /** 
     * Returns <code>true</code> if the calculator can provide a width for
     * the given block.
     */
    public boolean canHandleBlock(Block block);

    /**
     * Returns the calculated width for the specified block.
     * 
     * @param block  the block.
     * 
     * @return The width (a negative value indicates that no valid width 
     *         could be calculated).
     */
    public double calculateWidth(Block block);
    
}
