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
 * ColumnArrangement.java
 * ----------------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: ColumnArrangement.java,v 1.16 2005/11/16 17:06:54 mungady Exp $
 *
 * Changes:
 * --------
 * 22-Oct-2004 : Version 1 (DG);
 * 04-Feb-2005 : Added equals() and implemented Serializable (DG);
 * 
 */

package org.jfree.chart.block;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.Range;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.Size2D;
import org.jfree.ui.VerticalAlignment;

/**
 * Arranges blocks in a column layout.  This class is immutable.
 */
public class ColumnArrangement implements Arrangement, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -5315388482898581555L;
    
    /** The horizontal alignment of blocks. */
    private HorizontalAlignment horizontalAlignment;
    
    /** The vertical alignment of blocks within each row. */
    private VerticalAlignment verticalAlignment;
    
    /** The horizontal gap between columns. */
    private double horizontalGap;
    
    /** The vertical gap between items in a column. */
    private double verticalGap;
    
    /**
     * Creates a new instance.
     */
    public ColumnArrangement() {   
    }
    
    /**
     * Creates a new instance.
     * 
     * @param hAlign  the horizontal alignment (currently ignored).
     * @param vAlign  the vertical alignment (currently ignored).
     * @param hGap  the horizontal gap.
     * @param vGap  the vertical gap.
     */
    public ColumnArrangement(HorizontalAlignment hAlign, 
                             VerticalAlignment vAlign,
                             double hGap, double vGap) {        
        this.horizontalAlignment = hAlign;
        this.verticalAlignment = vAlign;
        this.horizontalGap = hGap;
        this.verticalGap = vGap;
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
        // since the flow layout is relatively straightforward, no information
        // needs to be recorded here
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
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The size of the container after arrangement of the contents.
     */
    public ArrangeResult arrange(BlockContainer container, Graphics2D g2,
                                 RectangleConstraint constraint, 
                                 ArrangeParams params) {
        
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
                return arrangeFF(container, g2, constraint, params); 
            }
            else if (h == LengthConstraintType.RANGE) {
                throw new RuntimeException("Not implemented.");  
            }
        }
        else if (w == LengthConstraintType.RANGE) {
            if (h == LengthConstraintType.NONE) {
                throw new RuntimeException("Not implemented.");  
            }
            else if (h == LengthConstraintType.FIXED) {
                return arrangeRF(container, g2, constraint, params);  
            }
            else if (h == LengthConstraintType.RANGE) {
                return arrangeRR(container, g2, constraint, params);  
            }
        }
        return new ArrangeResult(new Size2D(), null);  // TODO: complete this
        
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
     * @param params  optional parameters.
     * 
     * @return The container size after the arrangement.
     */
    protected ArrangeResult arrangeFF(BlockContainer container, Graphics2D g2,
                                      RectangleConstraint constraint, 
                                      ArrangeParams params) {
        // TODO: implement properly
        return arrangeNF(container, g2, constraint, params);
    }
    
    /**
     * Arranges the blocks in the container with a fixed width constraint.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed width.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeFN(BlockContainer container, Graphics2D g2,
                                      double fixedWidth, 
                                      ArrangeParams params) {
        
        List messages = null;
        if (params.isLogEnabled()) {
            messages = new java.util.LinkedList();
        }
        
        List blocks = container.getBlocks();
        int blockCount = blocks.size();
        double totalHeight = 0.0;
        
        // there is no limit on the height, so all the blocks will stack on
        // top of each other.  The width of each block can be anything up to
        // the fixedWidth
        RectangleConstraint constraint = new RectangleConstraint(
                new Range(0.0, fixedWidth), null);
        for (int i = 0; i < blockCount; i++) {
            Block b = (Block) blocks.get(i);
            ArrangeResult r = b.arrange(g2, constraint, params);
            b.setBounds(new Rectangle2D.Double(0.0, totalHeight, r.getWidth(), 
                    r.getHeight()));
            totalHeight += r.getHeight() + this.verticalGap;
            if (messages != null) {
                if (r.getMessages() != null) {
                    messages.addAll(r.getMessages());
                }
            }
        }
        if (blockCount > 0) {
            totalHeight = totalHeight - this.verticalGap;
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(fixedWidth, totalHeight);
            result.setMessages(messages);
        }
        else {
            result = new ArrangeResult(fixedWidth, totalHeight, messages);
        }
        return result;
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
     * @param params optional parameters.
     * 
     * @return The container size after the arrangement.
     */
    protected ArrangeResult arrangeNF(BlockContainer container, Graphics2D g2,
                                      RectangleConstraint constraint, 
                                      ArrangeParams params) {
    
        List blocks = container.getBlocks();
        
        double height = constraint.getHeight();
        if (height <= 0.0) {
            height = Double.POSITIVE_INFINITY;
        }
        
        double x = 0.0;
        double y = 0.0;
        double maxWidth = 0.0;
        List itemsInColumn = new ArrayList();
        for (int i = 0; i < blocks.size(); i++) {
            Block block = (Block) blocks.get(i);
            ArrangeResult r = block.arrange(g2, RectangleConstraint.NONE, 
                    params);
            if (y + r.getHeight() <= height) {
                itemsInColumn.add(block);
                block.setBounds(
                    new Rectangle2D.Double(x, y, r.getWidth(), r.getHeight())
                );
                y = y + r.getHeight() + this.verticalGap;
                maxWidth = Math.max(maxWidth, r.getWidth());
            }
            else {
                if (itemsInColumn.isEmpty()) {
                    // place in this column (truncated) anyway
                    block.setBounds(new Rectangle2D.Double(x, y, r.getWidth(), 
                            Math.min(r.getHeight(), height - y)));
                    y = 0.0;
                    x = x + r.getWidth() + this.horizontalGap;
                }
                else {
                    // start new column
                    itemsInColumn.clear();
                    x = x + maxWidth + this.horizontalGap;
                    y = 0.0;
                    maxWidth = r.getWidth();
                    block.setBounds(new Rectangle2D.Double(x, y, r.getWidth(), 
                            Math.min(r.getHeight(), height)));
                    y = r.getHeight() + this.verticalGap;
                    itemsInColumn.add(block);
                }
            }
        }
        return new ArrangeResult(x + maxWidth, constraint.getHeight(), null);  
    }

    /**
     * Arranges the blocks in the container to fit the given width and height
     * ranges.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param constraint  the constraint.
     * @param params  optional parameters.
     * 
     * @return Results from the arrangement, including the size.
     */
    protected ArrangeResult arrangeRR(BlockContainer container, Graphics2D g2,
                                      RectangleConstraint constraint, 
                                      ArrangeParams params) {

        // first arrange without constraints, and see if this fits within
        // the required ranges...
        ArrangeResult r1 = arrangeNN(container, g2, params);
        Size2D s1 = r1.getSize();
        if (constraint.getHeightRange().contains(s1.height)) {
            return r1;  // TODO: we didn't check the width yet
        }
        else {
            RectangleConstraint c = constraint.toFixedHeight(
                constraint.getHeightRange().getUpperBound()
            );
            return arrangeRF(container, g2, c, params);
        }
    }
    
    /**
     * Arranges the blocks in the container using a fixed height and a
     * range for the width.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param constraint  the constraint.
     * @param params  optional parameters.
     * 
     * @return The size of the container after arrangement.
     */ 
    protected ArrangeResult arrangeRF(BlockContainer container, Graphics2D g2,
                                      RectangleConstraint constraint, 
                                      ArrangeParams params) {

        ArrangeResult r = arrangeNF(container, g2, constraint, params);
        Size2D s = r.getSize();
        if (constraint.getWidthRange().contains(s.width)) {
            return r;   
        }
        else {
            RectangleConstraint c = constraint.toFixedWidth(
                constraint.getWidthRange().constrain(s.getWidth())
            );
            return arrangeFF(container, g2, c, params);
        }
    }

    /**
     * Arranges the blocks without any constraints.  This puts all blocks
     * into a single column.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param params  optional parameters.
     * 
     * @return The size after the arrangement.
     */
    protected ArrangeResult arrangeNN(BlockContainer container, Graphics2D g2, 
                                      ArrangeParams params) {
        double y = 0.0;
        double height = 0.0;
        double maxWidth = 0.0;
        List blocks = container.getBlocks();
        int blockCount = blocks.size();
        if (blockCount > 0) {
            Size2D[] sizes = new Size2D[blocks.size()];
            for (int i = 0; i < blocks.size(); i++) {
                Block block = (Block) blocks.get(i);
                sizes[i] = block.arrange(g2, params);
                height = height + sizes[i].getHeight();
                maxWidth = Math.max(sizes[i].width, maxWidth);
                block.setBounds(
                    new Rectangle2D.Double(
                        0.0, y, sizes[i].width, sizes[i].height
                    )
                );
                y = y + sizes[i].height + this.verticalGap;
            }
            if (blockCount > 1) {
                height = height + this.verticalGap * (blockCount - 1);   
            }
            if (this.horizontalAlignment != HorizontalAlignment.LEFT) {
                for (int i = 0; i < blocks.size(); i++) {
                    Block b = (Block) blocks.get(i);
                    if (this.horizontalAlignment 
                            == HorizontalAlignment.CENTER) {
                        //TODO: shift block right by half
                    }
                    else if (this.horizontalAlignment 
                            == HorizontalAlignment.RIGHT) {
                        //TODO: shift block over to right
                    }
                }            
            }
        }
        return new ArrangeResult(maxWidth, height, null);
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
        if (!(obj instanceof ColumnArrangement)) {
            return false;   
        }
        ColumnArrangement that = (ColumnArrangement) obj;
        if (this.horizontalAlignment != that.horizontalAlignment) {
            return false;
        }
        if (this.verticalAlignment != that.verticalAlignment) {
            return false;
        }
        if (this.horizontalGap != that.horizontalGap) {
            return false;   
        }
        if (this.verticalGap != that.verticalGap) {
            return false;   
        }
        return true;
    }
    

}
