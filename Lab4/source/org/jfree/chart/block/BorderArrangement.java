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
 * BorderArrangement.java
 * ----------------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: BorderArrangement.java,v 1.17 2005/11/16 17:06:54 mungady Exp $
 *
 * Changes:
 * --------
 * 22-Oct-2004 : Version 1 (DG);
 * 08-Feb-2005 : Updated for changes in RectangleConstraint (DG);
 * 24-Feb-2005 : Improved arrangeRR() method (DG);
 * 03-May-2005 : Implemented Serializable and added equals() method (DG);
 * 13-May-2005 : Fixed bugs in the arrange() method (DG);
 * 
 */

package org.jfree.chart.block;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.jfree.data.Range;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.Size2D;

/**
 * An arrangement manager that lays out blocks in a similar way to
 * Swing's BorderLayout class.
 */
public class BorderArrangement implements Arrangement, Serializable {
    
    /** For serialization. */
    private static final long serialVersionUID = 506071142274883745L;
    
    /** The block (if any) at the center of the layout. */
    private Block centerBlock;

    /** The block (if any) at the top of the layout. */
    private Block topBlock;
    
    /** The block (if any) at the bottom of the layout. */
    private Block bottomBlock;
    
    /** The block (if any) at the left of the layout. */
    private Block leftBlock;
    
    /** The block (if any) at the right of the layout. */
    private Block rightBlock;
    
    /**
     * Creates a new instance.
     */
    public BorderArrangement() {
    }
    
    /**
     * Adds a block to the arrangement manager at the specified edge.
     * 
     * @param block  the block (<code>null</code> permitted).
     * @param key  the edge (an instance of {@link RectangleEdge}) or 
     *             <code>null</code> for the center block.
     */
    public void add(Block block, Object key) {
        
        if (key == null) {
            this.centerBlock = block;
        }
        else {
            RectangleEdge edge = (RectangleEdge) key;
            if (edge == RectangleEdge.TOP) {
                this.topBlock = block;
            }
            else if (edge == RectangleEdge.BOTTOM) {
                this.bottomBlock = block;
            }
            else if (edge == RectangleEdge.LEFT) {
                this.leftBlock = block;
            }
            else if (edge == RectangleEdge.RIGHT) {
                this.rightBlock = block;
            }
        }
    }
    
    /**
     * Arranges the items in the specified container, subject to the given 
     * constraint.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param constraint  the constraint.
     * @param params  arrangement parameters (<code>null</code> not permitted).
     * 
     * @return The block size.
     */
    public ArrangeResult arrange(BlockContainer container, 
                                 Graphics2D g2, 
                                 RectangleConstraint constraint,
                                 ArrangeParams params) {
        RectangleConstraint contentConstraint 
            = container.toContentConstraint(constraint);
        ArrangeResult result = null;
        LengthConstraintType w = contentConstraint.getWidthConstraintType();
        LengthConstraintType h = contentConstraint.getHeightConstraintType();
        if (w == LengthConstraintType.NONE) {
            if (h == LengthConstraintType.NONE) {
                result = arrangeNN(container, g2, params);  
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
                result = arrangeFN(container, g2, constraint.getWidth(), params);  
            }
            else if (h == LengthConstraintType.FIXED) {
                result = arrangeFF(
                    container, g2, constraint.getWidth(), 
                    constraint.getHeight(), params
                );  
            }
            else if (h == LengthConstraintType.RANGE) {
                result = arrangeFR(container, g2, constraint, params);  
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
                result = arrangeRR(
                    container, g2, constraint.getWidthRange(),
                    constraint.getHeightRange(), params
                );  
            }
        }
        Size2D contentSize = result.getSize();
        return new ArrangeResult(container.calculateTotalWidth(contentSize.getWidth()),
                container.calculateTotalHeight(contentSize.getHeight()), null);
    }
    
    /**
     * Performs an arrangement without constraints.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param params  optional parameters.
     * 
     * @return The container size after the arrangement.
     */
    protected ArrangeResult arrangeNN(BlockContainer container, Graphics2D g2, 
                                      ArrangeParams params) {
        double[] w = new double[5];
        double[] h = new double[5];
        if (this.topBlock != null) {
            Size2D size = this.topBlock.arrange(g2, params);
            w[0] = size.width;
            h[0] = size.height;
        }
        if (this.bottomBlock != null) {
            Size2D size = this.bottomBlock.arrange(g2, params);
            w[1] = size.width;
            h[1] = size.height;
        }
        if (this.leftBlock != null) {
            Size2D size = this.leftBlock.arrange(g2, params);
            w[2] = size.width;
            h[2] = size.height;
       }
        if (this.rightBlock != null) {
            Size2D size = this.rightBlock.arrange(g2, params);
            w[3] = size.width;
            h[3] = size.height;
        }
        
        h[2] = Math.max(h[2], h[3]);
        h[3] = h[2];
        
        if (this.centerBlock != null) {
            Size2D size = this.centerBlock.arrange(g2, params);
            w[4] = size.width;
            h[4] = size.height;
        }
        double width = Math.max(w[0], Math.max(w[1], w[2] + w[4] + w[3]));
        double centerHeight = Math.max(h[2], Math.max(h[3], h[4]));
        double height = h[0] + h[1] + centerHeight;
        if (this.topBlock != null) {
            this.topBlock.setBounds(
                new Rectangle2D.Double(0.0, 0.0, width, h[0])
            );
        }
        if (this.bottomBlock != null) {
            this.bottomBlock.setBounds(
                new Rectangle2D.Double(0.0, height - h[1], width, h[1])
            );
        }
        if (this.leftBlock != null) {
            this.leftBlock.setBounds(
                new Rectangle2D.Double(0.0, h[0], w[2], centerHeight)
            );
        }
        if (this.rightBlock != null) {
            this.rightBlock.setBounds(
                new Rectangle2D.Double(width - w[3], h[0], w[3], centerHeight)
            );
        }
        
        if (this.centerBlock != null) {
            this.centerBlock.setBounds(
                new Rectangle2D.Double(
                    w[2], h[0], width - w[2] - w[3], centerHeight
                )
            );
        }
        return new ArrangeResult(width, height, null);
    }

    /**
     * Performs an arrangement with a fixed width and a range for the height.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param constraint  the constraint.
     * @param params  optional parameters.
     * 
     * @return The container size after the arrangement.
     */
    protected ArrangeResult arrangeFR(BlockContainer container, Graphics2D g2,
                                      RectangleConstraint constraint, 
                                      ArrangeParams params) {
        ArrangeResult r1 = arrangeFN(container, g2, constraint.getWidth(), 
                params);
        Size2D size1 = r1.getSize();
        if (constraint.getHeightRange().contains(size1.getHeight())) {
            return r1;   
        }
        else {
            double h = constraint.getHeightRange().constrain(size1.getHeight());
            RectangleConstraint c2 = constraint.toFixedHeight(h);
            return arrange(container, g2, c2, null);   
        }
    }
    
    /** 
     * Arranges the container width a fixed width and no constraint on the 
     * height.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param width  the fixed width.
     * @param params  arrangement parameters (<code>null</code> not permitted).
     * 
     * @return The container size after arranging the contents.
     */
    protected ArrangeResult arrangeFN(BlockContainer container, Graphics2D g2,
                                      double width, ArrangeParams params) {
        
        if (params == null) {
            throw new IllegalArgumentException("Null 'params' argument.");
        }
        double[] w = new double[5];
        double[] h = new double[5];
        RectangleConstraint c1 = new RectangleConstraint(
            width, null, LengthConstraintType.FIXED,
            0.0, null, LengthConstraintType.NONE
        );
        if (this.topBlock != null) {
            ArrangeResult r = this.topBlock.arrange(g2, c1, params);
            Size2D size = r.getSize();
            w[0] = size.width;
            h[0] = size.height;
        }
        if (this.bottomBlock != null) {
            ArrangeResult r = this.bottomBlock.arrange(g2, c1, params);
            Size2D size = r.getSize();
            w[1] = size.width;
            h[1] = size.height;
        }
        RectangleConstraint c2 = new RectangleConstraint(
            0.0, new Range(0.0, width), LengthConstraintType.RANGE,
            0.0, null, LengthConstraintType.NONE
        );
        if (this.leftBlock != null) {
            ArrangeResult r = this.leftBlock.arrange(g2, c2, params);
            Size2D size = r.getSize();
            w[2] = size.width;
            h[2] = size.height;
        }
        if (this.rightBlock != null) {
            double maxW = Math.max(width - w[2], 0.0);
            RectangleConstraint c3 = new RectangleConstraint(
                0.0, new Range(Math.min(w[2], maxW), maxW), 
                LengthConstraintType.RANGE,
                0.0, null, LengthConstraintType.NONE
            );    
            ArrangeResult r = this.rightBlock.arrange(g2, c3, params);
            Size2D size = r.getSize();
            w[3] = size.width;
            h[3] = size.height;
        }
        
        h[2] = Math.max(h[2], h[3]);
        h[3] = h[2];
        
        if (this.centerBlock != null) {
            RectangleConstraint c4 = new RectangleConstraint(
                width - w[2] - w[3], null, LengthConstraintType.FIXED,
                0.0, null, LengthConstraintType.NONE
            );    
            ArrangeResult r = this.centerBlock.arrange(g2, c4, params);
            Size2D size = r.getSize();
            w[4] = size.width;
            h[4] = size.height;
        }
        double height = h[0] + h[1] + Math.max(h[2], Math.max(h[3], h[4]));
        return arrange(container, g2, new RectangleConstraint(width, height), params);
    }

    /**
     * Performs an arrangement with range constraints on both the vertical 
     * and horizontal sides.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param widthRange  the allowable range for the container width.
     * @param heightRange  the allowable range for the container height.
     * @param params  optional parameters.
     * 
     * @return The container size.
     */
    protected ArrangeResult arrangeRR(BlockContainer container, Graphics2D g2, 
                                      Range widthRange, Range heightRange, 
                                      ArrangeParams params) {
        double[] w = new double[5];
        double[] h = new double[5];
        if (this.topBlock != null) {
            RectangleConstraint c1 = new RectangleConstraint(
                widthRange, heightRange
            );
            ArrangeResult r = this.topBlock.arrange(g2, c1, null);
            Size2D size = r.getSize();
            w[0] = size.width;
            h[0] = size.height;
        }
        if (this.bottomBlock != null) {
            Range heightRange2 = Range.shift(heightRange, -h[0], false);
            RectangleConstraint c2 = new RectangleConstraint(
                widthRange, heightRange2
            );  
            ArrangeResult r = this.bottomBlock.arrange(g2, c2, null);
            Size2D size = r.getSize();
            w[1] = size.width;
            h[1] = size.height;
        }
        Range heightRange3 = Range.shift(heightRange, -(h[0] + h[1]));
        if (this.leftBlock != null) {
            RectangleConstraint c3 = new RectangleConstraint(
                widthRange, heightRange3
            );
            ArrangeResult r = this.leftBlock.arrange(g2, c3, null);
            Size2D size = r.getSize();
            w[2] = size.width;
            h[2] = size.height;
        }
        Range widthRange2 = Range.shift(widthRange, -w[2], false);
        if (this.rightBlock != null) {
            RectangleConstraint c4 = new RectangleConstraint(
                widthRange2, heightRange3
            );
            ArrangeResult r = this.rightBlock.arrange(g2, c4, null);
            Size2D size = r.getSize();
            w[3] = size.width;
            h[3] = size.height;
        }
        
        h[2] = Math.max(h[2], h[3]);
        h[3] = h[2];
        Range widthRange3 = Range.shift(widthRange, -(w[2] + w[3]), false);
        if (this.centerBlock != null) {
            RectangleConstraint c5 = new RectangleConstraint(
                widthRange3, heightRange3
            );
            // TODO:  the width and height ranges should be reduced by the 
            // height required for the top and bottom, and the width required
            // by the left and right 
            ArrangeResult r = this.centerBlock.arrange(g2, c5, null);
            Size2D size = r.getSize();
            w[4] = size.width;
            h[4] = size.height;
        }
        double width = Math.max(w[0], Math.max(w[1], w[2] + w[4] + w[3]));
        double height = h[0] + h[1] + Math.max(h[2], Math.max(h[3], h[4]));
        if (this.topBlock != null) {
            this.topBlock.setBounds(
                new Rectangle2D.Double(0.0, 0.0, width, h[0])
            );
        }
        if (this.bottomBlock != null) {
            this.bottomBlock.setBounds(
                new Rectangle2D.Double(0.0, height - h[1], width, h[1])
            );
        }
        if (this.leftBlock != null) {
            this.leftBlock.setBounds(
                new Rectangle2D.Double(0.0, h[0], w[2], h[2])
            );
        }
        if (this.rightBlock != null) {
            this.rightBlock.setBounds(
                new Rectangle2D.Double(width - w[3], h[0], w[3], h[3])
            );
        }
        
        if (this.centerBlock != null) {
            this.centerBlock.setBounds(
                new Rectangle2D.Double(
                    w[2], h[0], width - w[2] - w[3], height - h[0] - h[1]
                )
            );
        }
        return new ArrangeResult(new Size2D(width, height), null);
    }

    /**
     * Arranges the items within a container to achieve the fixed width and 
     * height.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param width  the fixed width.
     * @param height  the fixed height.
     * @param params  optional parameters.
     * 
     * @return The container size after the arrangement.
     */
    protected ArrangeResult arrangeFF(BlockContainer container, Graphics2D g2,
                                      double width, double height, 
                                      ArrangeParams params) {
        double[] w = new double[5];
        double[] h = new double[5];
        w[0] = width;
        if (this.topBlock != null) {
            RectangleConstraint c1 = new RectangleConstraint(
                width, new Range(0.0, height)
            );
            ArrangeResult r = this.topBlock.arrange(g2, c1, params);
            Size2D size = r.getSize();
            h[0] = size.height;
        }
        w[1] = w[0];
        if (this.bottomBlock != null) {
            RectangleConstraint c2 = new RectangleConstraint(
                w[0], new Range(0.0, height - h[0])
            );
            ArrangeResult r = this.bottomBlock.arrange(g2, c2, params);
            Size2D size = r.getSize();
            h[1] = size.height;
        }
        h[2] = height - h[1] - h[0];
        if (this.leftBlock != null) {
            RectangleConstraint c3 = new RectangleConstraint(
                new Range(0.0, width), h[2]
            );
            ArrangeResult r = this.leftBlock.arrange(g2, c3, params);
            Size2D size = r.getSize();
            w[2] = size.width;            
        }
        h[3] = h[2];
        if (this.rightBlock != null) {
            RectangleConstraint c4 = new RectangleConstraint(
                new Range(0.0, width - w[2]), h[2]
            );
            ArrangeResult r = this.rightBlock.arrange(g2, c4, params);
            Size2D size = r.getSize();
            w[3] = size.width;            
        }
        h[4] = h[2];
        w[4] = width - w[3] - w[2];
        RectangleConstraint c5 = new RectangleConstraint(w[4], h[4]);
        if (this.centerBlock != null) {
            this.centerBlock.arrange(g2, c5, params);   
        }
       
        if (this.topBlock != null) {
            this.topBlock.setBounds(
                new Rectangle2D.Double(0.0, 0.0, w[0], h[0])
            );
        }
        if (this.bottomBlock != null) {
            this.bottomBlock.setBounds(
                new Rectangle2D.Double(0.0, h[0] + h[2], w[1], h[1])
            );
        }
        if (this.leftBlock != null) {
            this.leftBlock.setBounds(
                new Rectangle2D.Double(0.0, h[0], w[2], h[2])
            );
        }
        if (this.rightBlock != null) {
            this.rightBlock.setBounds(
                new Rectangle2D.Double(w[2] + w[4], h[0], w[3], h[3])
            );
        }
        if (this.centerBlock != null) {
            this.centerBlock.setBounds(
                new Rectangle2D.Double(w[2], h[0], w[4], h[4])
            );
        }
        return new ArrangeResult(new Size2D(width, height), null);
    }
    
    /**
     * Clears the layout.
     */
    public void clear() {
        this.centerBlock = null;
        this.topBlock = null;
        this.bottomBlock = null;
        this.leftBlock = null;
        this.rightBlock = null;
    }
    
    /**
     * Tests this <code>BorderArrangement</code> for equality with an 
     * arbitrary object.  This method returns <code>true</code> if and only if:
     * <ul>
     * <li><code>obj</code> is not <code>null</code>;      
     * <li><code>obj</code> is an instance of <code>BorderArrangement</code>;
     * </ul>
     * 
     * @param obj  the object (<code>null</code> permitted).
     * 
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;   
        }
        if (!(obj instanceof BorderArrangement)) {
            return false;   
        }
        return true;
    }
}
