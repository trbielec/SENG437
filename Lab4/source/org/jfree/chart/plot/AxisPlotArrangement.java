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
 * ------------------------
 * AxisPlotArrangement.java
 * ------------------------
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: AxisPlotArrangement.java,v 1.3 2005/11/16 16:49:20 mungady Exp $
 *
 * Changes
 * -------
 * 25-May-2005 : Version 1 (DG);
 * 
 */

package org.jfree.chart.plot;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.block.ArrangeParams;
import org.jfree.chart.block.ArrangeResult;
import org.jfree.chart.block.Arrangement;
import org.jfree.chart.block.Block;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.data.Range;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.Size2D;

/**
 * A layout class for a plot that has axes.  This arranges the axes around the
 * four sides of the plot in a similar way to JFreeChart 1.0.0.
 */
public class AxisPlotArrangement implements Arrangement, Serializable {
        
    private Block centerBlock;
    
    /** Temporary storage for the axes at the left of the plot. */
    private List leftBlocks;
    
    /** Temporary storage for the axes at the right of the plot. */
    private List rightBlocks;
    
    /** Temporary storage for the axes at the top of the plot. */
    private List topBlocks;
    
    /** Temporary storage for the axes at the bottom of the plot. */
    private List bottomBlocks;
    
    /** 
     * Coordinates defining the remaining space for the plot after the axes 
     * have been allocated space - this is where the data is plotted.
     */
    private double x0, x1, y0, y1;
    
    /**
     * Creates a new layout manager.
     */
    public AxisPlotArrangement() {
        this.leftBlocks = new ArrayList();
        this.rightBlocks = new ArrayList();
        this.topBlocks = new ArrayList();
        this.bottomBlocks = new ArrayList();
    }
    
    /**
     * Returns the x-coordinate that marks the line between the left axes 
     * and the data area.
     * 
     * @return The x-coordinate.
     */
    public double getX0() {
        return this.x0;
    }
    
    /**
     * Returns the x-coordinate that marks the line between the right axes 
     * and the data area.
     * 
     * @return The x-coordinate.
     */
    public double getX1() {
        return this.x1;   
    }
    
    /**
     * Returns the y-coordinate that marks the line between the top axes 
     * and the data area.
     * 
     * @return The y-coordinate.
     */
    public double getY0() {
        return this.y0;   
    }
    
    /**
     * Returns the y-coordinate that marks the line between the bottom axes 
     * and the data area.
     * 
     * @return The y-coordinate.
     */
    public double getY1() {
        return this.y1;   
    }
    
    /**
     * Adds a block and a key which can be used to determine the position of 
     * the block in the arrangement.  This method is called by the container 
     * (you don't need to call this method directly) and gives the arrangement
     * an opportunity to record the details if they are required.
     * 
     * @param block  the block.
     * @param key  the key (<code>null</code> permitted).
     */
    public void add(Block block, Object key) {
        if (key instanceof RectangleEdge) {
            RectangleEdge edge = (RectangleEdge) key;
            if (edge == RectangleEdge.LEFT) {
                this.leftBlocks.add(0, block);   
            }
            else if (edge == RectangleEdge.RIGHT) {
                this.rightBlocks.add(0, block);   
            }
            else if (edge == RectangleEdge.TOP) {
                this.topBlocks.add(0, block);   
            }
            else if (edge == RectangleEdge.BOTTOM) {
                this.bottomBlocks.add(0, block);   
            }
        }
        else {
            this.centerBlock = block;   
        }
    }
    
    /**
     * Arranges the blocks within the specified container, subject to the given
     * constraint.
     * 
     * @param container  the container.
     * @param constraint  the constraint.
     * @param g2  the graphics device.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The container size after the arrangement.
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
                throw new RuntimeException("Not implemented.");  
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
                throw new RuntimeException("Not implemented.");  
            }
            else if (h == LengthConstraintType.RANGE) {
                throw new RuntimeException("Not implemented.");  
            }
        }
        return new ArrangeResult(new Size2D(), null);  // TODO: complete this
        
    }
    
    /**
     * Arranges the components in the specified container and returns the
     * resulting size.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param params  optional parameters.
     * 
     * @return The result (includes the size).
     */
    protected ArrangeResult arrangeNN(BlockContainer container, Graphics2D g2,
                                      ArrangeParams params) {
        // work out the unconstrained plot size
        // this determines the height of left and right blocks,
        // and the width of top and bottom blocks
        ArrangeResult ar1 = this.centerBlock.arrange(
            g2, RectangleConstraint.NONE, params
        );
        // TODO: need to take account of axes if they are present
        return ar1;
    }
    
    /**
     * Arranges the items within a container.
     * 
     * @param container  the container.
     * @param constraint  the constraint.
     * @param g2  the graphics device.
     * @param params  optional parameters.
     * 
     * @return The container size after the arrangement.
     */
    protected ArrangeResult arrangeFF(BlockContainer container, Graphics2D g2,
                                      RectangleConstraint constraint, 
                                      ArrangeParams params) {
  
        RectangleInsets margin = container.getMargin();
        BlockBorder border = container.getBorder();
        RectangleInsets padding = container.getPadding();
        
        // TODO : we can probably write a convenience method to do this 
        // calculation...
        this.x0 = 0.0; //margin.getLeft() + border.getInsets().getLeft() + padding.getLeft();
        this.x1 = constraint.getWidth() - margin.getLeft() - margin.getRight() - border.getInsets().getLeft() - border.getInsets().getRight() - padding.getLeft() - padding.getRight();
        this.y0 = 0.0; //margin.getTop() + border.getInsets().getTop() + padding.getTop();
        this.y1 = constraint.getHeight() - margin.getTop() - margin.getBottom() - border.getInsets().getTop() - border.getInsets().getBottom() - padding.getTop() - padding.getBottom();
        
        RectangleConstraint c1 = new RectangleConstraint(
            new Range(0.0, this.x1), this.y1
        );
        
        // do the left blocks
        for (int i = 0; i < this.leftBlocks.size(); i++) {
            Block b = (Block) this.leftBlocks.get(i);
            ArrangeResult r = b.arrange(g2, c1, null);
            Size2D size = r.getSize();
            b.setBounds(
                new Rectangle2D.Double(
                    this.x0, this.y0, size.getWidth(), size.getHeight()
                )
            );
            this.x0 = this.x0 + size.getWidth();
        }
        
        // do the right blocks
        for (int i = 0; i < this.rightBlocks.size(); i++) {
            Block b = (Block) this.rightBlocks.get(i);
            ArrangeResult r = b.arrange(g2, c1, null);
            Size2D size = r.getSize();
            b.setBounds(
                new Rectangle2D.Double(
                    this.x1 - size.getWidth(), this.y0, 
                    size.getWidth(), size.getHeight()
                )
            );
            this.x1 = this.x1 - size.getWidth();
        }
        
        RectangleConstraint c2 = new RectangleConstraint(
            this.x1 - this.x0, new Range(0.0, this.y1 - this.y0)
        );
 
        // do the top blocks
        for (int i = 0; i < this.topBlocks.size(); i++) {
            Block b = (Block) this.topBlocks.get(i);
            ArrangeResult r = b.arrange(g2, c2, null);
            Size2D size = r.getSize();
            b.setBounds(
                new Rectangle2D.Double(
                    this.x0, this.y0, size.getWidth(), size.getHeight()
                )
            );
            this.y0 = this.y0 + size.getHeight();
        }
        
        // do the bottom blocks
        for (int i = 0; i < this.bottomBlocks.size(); i++) {
            Block b = (Block) this.bottomBlocks.get(i);
            ArrangeResult r = b.arrange(g2, c2, null);
            Size2D size = r.getSize();
            b.setBounds(
                new Rectangle2D.Double(
                    this.x0, this.y1 - size.getHeight(), 
                    size.getWidth(), size.getHeight()
                )
            );
            this.y1 = this.y1 - size.getHeight();
        }
        
        // resize widths and/or heights
        for (int i = 0; i < this.leftBlocks.size(); i++) {
            Block b = (Block) this.leftBlocks.get(i);
            Rectangle2D bounds = b.getBounds();
            bounds.setRect(
                bounds.getX(), this.y0, bounds.getWidth(), this.y1 - this.y0
            );
            b.setBounds(bounds);
        }
        
        for (int i = 0; i < this.rightBlocks.size(); i++) {
            Block b = (Block) this.rightBlocks.get(i);
            Rectangle2D bounds = b.getBounds();
            bounds.setRect(
                bounds.getX(), this.y0, bounds.getWidth(), this.y1 - this.y0
            );
            b.setBounds(bounds);
        }
        this.centerBlock.setBounds(new Rectangle2D.Double(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0));
        return new ArrangeResult(new Size2D(constraint.getWidth(), constraint.getHeight()), null);
    }
    
    /**
     * Clears any cached layout information retained by the arrangement.
     */
    public void clear() {
        // nothing to clear
    }
    
    /**
     * Tests this <code>AxisPlotArrangement</code> for equality with an
     * arbitrary object.
     * 
     * @param obj  the object (<code>null</code> permitted).
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;   
        }
        if (!(obj instanceof AxisPlotArrangement)) {
            return false;   
        }
        return true;
    }

}
