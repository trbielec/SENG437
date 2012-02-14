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
 * CategoryPlotWrapper.java
 * ------------------------
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: CategoryPlotWrapper.java,v 1.5 2005/11/16 16:49:20 mungady Exp $
 *
 * Changes
 * -------
 * 15-Jun-2005 : Version 1 (DG);
 *
 */

package org.jfree.chart.plot;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.block.ArrangeParams;
import org.jfree.chart.block.ArrangeResult;
import org.jfree.chart.block.Block;
import org.jfree.chart.block.BlockHeightCalculator;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.ui.Size2D;

/**
 * This wrapper presents an {@link CategoryPlot} as a {@link Block} that only 
 * draws the plot and not the axes.
 */
public class CategoryPlotWrapper implements Block {

    /** The "wrapped" plot. */
    private CategoryPlot plot;
    
    /** The current bounds. */
    private Rectangle2D bounds;
    
    private Paint backgroundPaint;
    
    /**
     * Creates a new wrapper for the given plot.
     * 
     * @param plot  the plot.
     */
    public CategoryPlotWrapper(CategoryPlot plot) {
        this.plot = plot;
        this.bounds = new Rectangle2D.Double();
    }
    
    /**
     * Returns an ID for the block.
     * 
     * @return An ID.
     */
    public String getID() {
        return plot.getID();       
    }
    
    /**
     * Sets the ID for the block.
     * 
     * @param id  the ID.
     */
    public void setID(String id) {
        plot.setID(id);
    }
    
    /**
     * Returns the current bounds of the block.
     * 
     * @return The bounds.
     */
    public Rectangle2D getBounds() {
        return this.bounds;   
    }
    
    /**
     * Sets the bounds of the block.
     * 
     * @param bounds  the bounds.
     */
    public void setBounds(Rectangle2D bounds) {
        this.bounds.setRect(bounds);
    }
    
    public void setBackgroundPaint(Paint paint) {
        this.backgroundPaint = paint;   
    }
    
    /**
     * Arranges the contents of the block, with no constraints, and returns
     * the block size.  In general, the size returned by this method is the
     * preferred size for the block.
     * 
     * @param g2  the graphics device.
     * 
     * @return The size of the block following the arrangement.
     */
    public Size2D arrange(Graphics2D g2, ArrangeParams params) {
        return arrange(g2, RectangleConstraint.NONE, params).getSize();  
    }
    
    /**
     * Arranges the contents of the block, within the given constraints, and 
     * returns the block size.
     * 
     * @param g2  the graphics device.
     * @param constraint  the constraint (<code>null</code> not permitted).
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The result of the arrangement, including the size.
     */
    public ArrangeResult arrange(Graphics2D g2, RectangleConstraint constraint, 
                                 ArrangeParams params) {

        RectangleConstraint cc = this.plot.toContentConstraint(constraint);
        ArrangeResult result = null;
        LengthConstraintType w = cc.getWidthConstraintType();     
        LengthConstraintType h = cc.getHeightConstraintType();
        if (w == LengthConstraintType.NONE) {
            if (h == LengthConstraintType.NONE) {
                result = arrangeNN(g2, params);  
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
                throw new RuntimeException("Not implemented.");   
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
        Size2D contentSize = result.getSize();
        result.setSize(
            new Size2D(
                this.plot.calculateTotalWidth(contentSize.getWidth()), 
                this.plot.calculateTotalHeight(contentSize.getHeight())
            )
        );
        return result;
        
    }

    /**
     * Arranges the plot with no constraints.
     * 
     * @param g2  the graphics device.
     * @param params  optional parameters.
     * 
     * @return The result of the arrangement.
     */
    protected ArrangeResult arrangeNN(Graphics2D g2, ArrangeParams params) {
        // is there a set width?  return that;
        double width = this.plot.getDefaultWidth();
        if (width < 0.0) {
            // is there a width calculator?  use that;
            // otherwise width = 300;
            width = 300.0;
        }
        // is there a set height?  return that;
        // is there a height calculator?  use that;
        // otherwise height = 200;        
        BlockHeightCalculator bhc = null;
        double height = this.plot.getDefaultHeight();
        if (height < 0.0 && params != null) {
            bhc = params.findHeightCalculator(this.plot);
            if (bhc != null) {
                height = bhc.calculateHeight(this);
            }
            else { 
                height = 200.0;   
            }
        }
        return new ArrangeResult(new Size2D(width, height), null);    
    }

    /**
     * Draws the block within the specified area.  Refer to the documentation 
     * for the implementing class for information about the <code>params</code>
     * and return value supported.
     * 
     * @param g2  the graphics device.
     * @param area  the area.
     * @param params  optional parameters (<code>null</code> permitted).
     * 
     * @return An optional return value (possibly <code>null</code>).
     */
    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        Paint oldBackgroundPaint = this.plot.getBackgroundPaint();
        plot.setBackgroundPaint(this.backgroundPaint, false);
        this.plot.drawDataArea(g2, area);
        plot.setBackgroundPaint(oldBackgroundPaint, false);
        return null;
    }

    public void draw(Graphics2D g2, Rectangle2D area) {
        draw(g2, area, null);
    }
}
