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
 * ---------------
 * EmptyBlock.java
 * ---------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: EmptyBlock.java,v 1.6 2005/07/19 14:27:02 mungady Exp $
 *
 * Changes:
 * --------
 * 22-Oct-2004 : Version 1 (DG);
 * 04-Feb-2005 : Now cloneable and serializable (DG);
 * 20-Apr-2005 : Added new draw() method (DG);
 * 
 */

package org.jfree.chart.block;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.jfree.util.PublicCloneable;

/**
 * An empty block with a fixed size.
 */
public class EmptyBlock extends AbstractContentBlock 
                        implements Block, Cloneable, PublicCloneable,
                                   Serializable {
    
    /** For serialization. */
    private static final long serialVersionUID = -4083197869412648579L;
    
    /**
     * Creates a new block with the specified width and height.
     * 
     * @param width  the block width.
     * @param height  the block height.
     */
    public EmptyBlock(double width, double height) {
        setDefaultWidth(width);
        setDefaultHeight(height);
    }

    /**
     * Arranges the contents of the block, within the given constraints, and 
     * returns the block size.
     * 
     * @param g2  the graphics device.
     * @param constraint  the constraint (<code>null</code> not permitted).
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    public ArrangeResult arrange(Graphics2D g2, RectangleConstraint constraint, 
            ArrangeParams params) {
        
        // there isn't any content to arrange, so we just need to return the
        // size for the given constraint
        ArrangeResult result = params.getRecyclableResult();
        double w = constraint.calculateConstrainedWidth(getDefaultWidth());
        double h = constraint.calculateConstrainedHeight(getDefaultHeight());
        if (result != null) {
            result.setSize(w, h);
        }
        else {
            result = new ArrangeResult(w, h, null);
        }
        return result;
        
    }
    
    /**
     * Draws the block.  Since the block is empty, this method does nothing.
     * 
     * @param g2  the graphics device.
     * @param area  the area.
     */
    public void draw(Graphics2D g2, Rectangle2D area) {
        // do nothing, we're empty
    }
    
    /**
     * Draws the block within the specified area.  Since the block is empty, 
     * this method does nothing.
     * 
     * @param g2  the graphics device.
     * @param area  the area.
     * @param params  ignored (<code>null</code> permitted).
     * 
     * @return Always <code>null</code>.
     */
    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        return null;
    }

    /**
     * Returns a clone of the block.
     * 
     * @return A clone.
     * 
     * @throws CloneNotSupportedException if there is a problem cloning.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();   
    }

}
