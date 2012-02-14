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
 * ----------------------
 * CenterArrangement.java
 * ----------------------
 * (C) Copyright 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: CenterArrangement.java,v 1.5 2005/07/19 14:27:02 mungady Exp $
 *
 * Changes:
 * --------
 * 08-Mar-2005 : Version 1 (DG);
 * 
 */

package org.jfree.chart.block;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;

import org.jfree.data.Range;
import org.jfree.ui.Size2D;

/**
 * Arranges a single block in the center of its container.  If the container
 * has more than one block, the first block is positioned in the center and the 
 * rest are ignored.  This class is immutable.
 */
public class CenterArrangement implements Arrangement, Serializable {
    
    /** For serialization. */
    private static final long serialVersionUID = -353308149220382047L; 
    
    /**
     * Creates a new instance.
     */
    public CenterArrangement() {   
    }
     
    /**
     * Adds a block to be managed by this instance.  This method is usually 
     * called by the {@link BlockContainer}, you shouldn't need to call it 
     * directly.
     * 
     * @param block  the block.
     * @param key  a key that controls the position of the block.
     */
    public void add(Block block, Object key) {
        // since the center layout is relatively straightforward, 
        // no information needs to be recorded here
    }
    
    /**
     * Calculates and sets the bounds of all the items in the specified 
     * container, subject to the given constraint.  The <code>Graphics2D</code>
     * can be used by some items (particularly items containing text) to 
     * calculate sizing parameters.
     * 
     * @param container  the container whose items are being arranged.
     * @param g2  the graphics device.
     * @param constraint  the size constraint.
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    public ArrangeResult arrange(BlockContainer container, Graphics2D g2,
                                 RectangleConstraint constraint, 
                                 ArrangeParams params) {
        
        // TODO: this method is returning the size of the block in the 
        // container - that is, the *content* size.  Is this consistent with
        // the size returned elsewhere?  Maybe we use the *total* size 
        // elsewhere...
        LengthConstraintType w = constraint.getWidthConstraintType();
        LengthConstraintType h = constraint.getHeightConstraintType();
        if (w == LengthConstraintType.NONE) {
            if (h == LengthConstraintType.NONE) {
                return arrangeNN(container, g2, params);  
            }
            else if (h == LengthConstraintType.FIXED) {
                throw new RuntimeException("Not implemented.");  
            }
            else if (h == LengthConstraintType.RANGE) {
                throw new RuntimeException("Not implemented.");  
            }
        }
        else if (w == LengthConstraintType.FIXED) {
            if (h == LengthConstraintType.NONE) {
                return arrangeFN(container, g2, constraint.getWidth(), params);  
            }
            else if (h == LengthConstraintType.FIXED) {
                throw new RuntimeException("Not implemented.");  
            }
            else if (h == LengthConstraintType.RANGE) {
                throw new RuntimeException("Not implemented.");  
            }
        }
        else if (w == LengthConstraintType.RANGE) {
            if (h == LengthConstraintType.NONE) {
                return arrangeRN(container, g2, constraint.getWidthRange(), 
                        params);  
            }
            else if (h == LengthConstraintType.FIXED) {
                return arrangeRF(container, g2, constraint.getWidthRange(), 
                        constraint.getHeight(), params);  
            }
            else if (h == LengthConstraintType.RANGE) {
                return arrangeRR(container, g2, constraint, params);   
            }
        }
        throw new IllegalArgumentException("Unknown LengthConstraintType.");
        
    }

    /**
     * Arranges the first block in the container with a fixed width and no 
     * height constraint.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed width.
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeFN(BlockContainer container, Graphics2D g2,
                                      double fixedWidth,ArrangeParams params) {
        
        List messages = null;
        boolean logging = params.isLogEnabled();
        if (logging) {
            messages = new java.util.LinkedList();
        }
        
        List blocks = container.getBlocks();
        Block b = (Block) blocks.get(0);
        
        ArrangeResult r = b.arrange(g2, RectangleConstraint.NONE, params);
        double w = r.getWidth();
        double h = r.getHeight();
        if (messages != null) {
            if (r.getMessages() != null) {
                messages.addAll(r.getMessages());
            }
        }
        
        Rectangle2D bounds = new Rectangle2D.Double((fixedWidth - w) / 2.0, 
                0.0, w, h);
        b.setBounds(bounds);

        if (fixedWidth < w) {
            messages.add(new Message(container, "Content too wide to fit."));
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(fixedWidth, h);
            result.setMessages(messages);
        }
        else {
            result = new ArrangeResult(w, h, messages);
        }
        return new ArrangeResult(fixedWidth, h, messages);
    }
    
    /**
     * Arranges the blocks in the container with a fixed with and a range
     * constraint on the height.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed width.
     * @param heightRange  the permitted height range.
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The size following the arrangement.
     */
    protected ArrangeResult arrangeFR(BlockContainer container, Graphics2D g2,
                                      double fixedWidth, Range heightRange, 
                                      ArrangeParams params) {

        // TODO: evaluate logging requirement here
        ArrangeResult r = arrangeFN(container, g2, fixedWidth, params);
        if (heightRange.contains(r.getHeight())) {
            return r;   
        }
        else {
            double fixedHeight = heightRange.constrain(r.getHeight());
            return arrangeFF(container, g2, fixedWidth, fixedHeight, params);
        }
    }

    /**
     * Arranges the blocks in the container with the overall height and width
     * specified as fixed constraints.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed width.
     * @parma fixedHeight  the fixed height.
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeFF(BlockContainer container, Graphics2D g2,
                                      double fixedWidth, double fixedHeight, 
                                      ArrangeParams params) {

        List messages = null;
        boolean logging = params.isLogEnabled();
        if (logging) {
            messages = new java.util.LinkedList();
        }
        
        List blocks = container.getBlocks();
        Block b = (Block) blocks.get(0);
        
        ArrangeResult r = b.arrange(g2, RectangleConstraint.NONE, params);
        double w = r.getWidth();
        double h = r.getHeight();
        if (messages != null) {  // with no constraint, there probably won't
            if (r.getMessages() != null) {  // be any messages
                messages.addAll(r.getMessages());
            }
        }
        
        Rectangle2D bounds = new Rectangle2D.Double(0.0, 0.0, fixedWidth, 
                fixedHeight);
        b.setBounds(bounds);

        if (fixedWidth < w) {
            messages.add(new Message(container, "Content too wide to fit."));
        }
        if (fixedHeight < h) {
            messages.add(new Message(container, "Content too tall to fit."));
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(fixedWidth, fixedHeight);
            result.setMessages(messages);
        }
        else {
            result = new ArrangeResult(fixedWidth, fixedHeight, messages);
        }
        return result;
    }

    /**
     * Arranges the blocks with the overall width and height to fit within 
     * specified ranges.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param constraint  the constraint.
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeRR(BlockContainer container, Graphics2D g2,
                                      RectangleConstraint constraint, 
                                      ArrangeParams params) {

        // first arrange without constraints, and see if this fits within
        // the required ranges...
        // TODO: might need to turn off logging here
        ArrangeResult r1 = arrangeNN(container, g2, params);
        Size2D s1 = r1.getSize();
        if (constraint.getWidthRange().contains(s1.width)) {
            return r1;  // TODO: we didn't check the height yet
        }
        else {
            RectangleConstraint c = constraint.toFixedWidth(
                constraint.getWidthRange().getUpperBound()
            );
            return arrangeFR(container, g2, c.getWidth(), c.getHeightRange(), 
                    params);
        }
    }
    
    /**
     * Arranges the blocks in the container with a range constraint on the
     * width and a fixed height.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param widthRange  the width range.
     * @param fixedHeight  the fixed height.
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The size following the arrangement.
     */
    protected ArrangeResult arrangeRF(BlockContainer container, Graphics2D g2,
                                      Range widthRange, double fixedHeight, 
                                      ArrangeParams params) {

        ArrangeResult r = arrangeNF(container, g2, fixedHeight, params);
        if (widthRange.contains(r.getWidth())) {
            return r;   
        }
        else {
            double fixedWidth = widthRange.constrain(r.getWidth());
            return arrangeFF(container, g2, fixedWidth, fixedHeight, params);
        }
    }

    /**
     * Arranges the block with a range constraint on the width, and no 
     * constraint on the height.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param widthRange  the width range.
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeRN(BlockContainer container, Graphics2D g2,
                                      Range widthRange, ArrangeParams params) {
        
        // first arrange without constraints, then see if the width fits
        // within the required range...if not, call arrangeFN() at max width
        ArrangeResult r1 = arrangeNN(container, g2, params);
        if (widthRange.contains(r1.getWidth())) {
            return r1;   
        }
        else {
            double fixedWidth = widthRange.constrain(r1.getWidth());
            return arrangeFN(container, g2, fixedWidth, params);
        }
    }
    
    /**
     * Arranges the blocks without any constraints.  This puts all blocks
     * into a single row.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The size after the arrangement.
     */
    protected ArrangeResult arrangeNN(BlockContainer container, Graphics2D g2, 
                                      ArrangeParams params) {
        List blocks = container.getBlocks();
        Block b = (Block) blocks.get(0);
        // TODO: we are returning the content size here, not the total size
        // for the container - is this correct?
        return b.arrange(g2, RectangleConstraint.NONE, params); 
    }
    
    /**
     * Arranges the blocks with no width constraint and a fixed height 
     * constraint.  This puts all blocks into a single row.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param fixedHeight  the fixed height.
     * @param params  layout parameters (<code>null</code> permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeNF(BlockContainer container, Graphics2D g2,
                                      double fixedHeight, 
                                      ArrangeParams params) {
        // TODO: for now we are ignoring the height constraint
        return arrangeNN(container, g2, params);
    }
    
    /**
     * Clears any cached information.
     */
    public void clear() {
        // no action required.
    }
    
    /**
     * Tests this instance for equality with an arbitrary object.
     * 
     * @param obj  the object (<code>null</code> permitted).
     * 
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;   
        }
        if (!(obj instanceof CenterArrangement)) {
            return false;   
        }
        return true;
    }
    
}
