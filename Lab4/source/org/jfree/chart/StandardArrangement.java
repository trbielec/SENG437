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
 * StandardArrangement.java
 * ------------------------
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: StandardArrangement.java,v 1.3 2005/11/16 10:05:59 mungady Exp $
 *
 * Changes
 * -------
 * 25-May-2005 : Version 1 (DG); 
 *
 */

package org.jfree.chart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.block.ArrangeParams;
import org.jfree.chart.block.ArrangeResult;
import org.jfree.chart.block.Arrangement;
import org.jfree.chart.block.Block;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.data.Range;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.Size2D;
import org.jfree.ui.VerticalAlignment;

/**
 * A chart arrangement that follows the traditional JFreeChart approach of 
 * title(s) first, then the plot uses whatever space remains.
 */
public class StandardArrangement implements Arrangement, Serializable {
    
    /** The chart that the arrangement belongs to. */
    private JFreeChart chart;
    
    /**
     * Creates a new layout manager.
     * 
     * @param chart  the chart that the arrangement is working on.
     */
    public StandardArrangement(JFreeChart chart) {
        this.chart = chart;   
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
        // no action required
    }
    
    /**
     * Arranges the blocks within the specified container, subject to the given
     * constraint.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param constraint  the constraint.
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
                throw new RuntimeException("Not implemented.");  
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
                return arrangeFF(container, g2, constraint.getWidth(), 
                        constraint.getHeight(), params);  
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
     * Arranges the items within a container.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed width.
     * @param fixedHeight  the fixed height.
     * @param params  optional parameters.
     * 
     * @return The container size after the arrangement.
     */
    protected ArrangeResult arrangeFF(BlockContainer container, 
                                      Graphics2D g2, 
                                      double fixedWidth,
                                      double fixedHeight,
                                      ArrangeParams params) {
        
        List messages = null;
        if (params.isLogEnabled()) {
            messages = new java.util.LinkedList();
        }
        Rectangle2D remaining = new Rectangle2D.Double(0.0, 0.0, fixedWidth, 
                fixedHeight);
        
        // arrange the main title
        TextTitle title = this.chart.getTitle();
        if (title != null) {
            ArrangeResult r = arrangeTitle(title, remaining, g2, params);
            if (messages != null) {
                if (r.getMessages() != null) {
                    messages.addAll(r.getMessages());
                }
            }
        }
        
        // arrange the subtitles, if there are any
        Iterator iterator = this.chart.getSubtitles().iterator();
        while (iterator.hasNext()) {
            Title subtitle = (Title) iterator.next();
            ArrangeResult r = arrangeTitle(subtitle, remaining, g2, params);
            if (messages != null) {
                if (r.getMessages() != null) {
                    messages.addAll(r.getMessages());
                }
            }
        }
        
        // arrange the plot within the remaining space
        Plot plot = this.chart.getPlot();
        RectangleConstraint plotConstraint = new RectangleConstraint(
            remaining.getWidth(), remaining.getHeight()
        );
        /* ArrangeResult r = */ plot.arrange(g2, plotConstraint, params);
        plot.setBounds(remaining);
        return new ArrangeResult(fixedWidth, fixedHeight, null);
    }
    
    /**
     * A utility method that arranges a title within the specified area, then
     * updates the area to represent the remaining space (if any).
     * 
     * @param title  the title.
     * @param remaining  the remaining area (updated by this method).
     * @param g2  the graphics device.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    private ArrangeResult arrangeTitle(Title title, Rectangle2D remaining, 
                                       Graphics2D g2, ArrangeParams params) {
        
        if (title == null) {
            throw new IllegalArgumentException("Null 'title' argument.");   
        }
        if (remaining == null) {
            throw new IllegalArgumentException("Null 'remaining' argument.");   
        }

        Rectangle2D titleArea = new Rectangle2D.Double();
        RectangleEdge position = title.getPosition();
        double ww = remaining.getWidth();
        double hh = remaining.getHeight();
        RectangleConstraint constraint = new RectangleConstraint(
            ww, new Range(0.0, ww), LengthConstraintType.RANGE,
            hh, new Range(0.0, hh), LengthConstraintType.RANGE
        );
        
        if (position == RectangleEdge.TOP) {
            RectangleConstraint topConstraint = new RectangleConstraint(
                ww, new Range(0.0, hh)
            );
            ArrangeResult r = title.arrange(g2, topConstraint, params);
            Size2D size = r.getSize();
            titleArea = createAlignedRectangle2D(size, remaining, 
                    title.getHorizontalAlignment(), VerticalAlignment.TOP);
            title.setBounds(titleArea);
            remaining.setRect(
                remaining.getX(), 
                Math.min(remaining.getY() + size.height, remaining.getMaxY()),
                remaining.getWidth(), 
                Math.max(remaining.getHeight() - size.height, 0)
            );
        }
        else if (position == RectangleEdge.BOTTOM) {
            ArrangeResult r = title.arrange(g2, constraint, params);
            Size2D size = r.getSize();
            titleArea = createAlignedRectangle2D(size, remaining, 
                    title.getHorizontalAlignment(), VerticalAlignment.BOTTOM);
            title.setBounds(titleArea);
            remaining.setRect(
                remaining.getX(), remaining.getY(), 
                remaining.getWidth(), remaining.getHeight() - size.height
            );
        }
        else if (position == RectangleEdge.RIGHT) {
            ArrangeResult r = title.arrange(g2, constraint, params);
            Size2D size = r.getSize();
            titleArea = createAlignedRectangle2D(size, remaining, 
                    HorizontalAlignment.RIGHT, title.getVerticalAlignment());
            title.setBounds(titleArea);
            remaining.setRect(
                remaining.getX(), remaining.getY(), 
                remaining.getWidth() - size.width, remaining.getHeight()
            );
        }

        else if (position == RectangleEdge.LEFT) {
            ArrangeResult r = title.arrange(g2, constraint, params);
            Size2D size = r.getSize();
            titleArea = createAlignedRectangle2D(size, remaining, 
                    HorizontalAlignment.LEFT, title.getVerticalAlignment());
            title.setBounds(titleArea);
            remaining.setRect(
                remaining.getX() + size.width, remaining.getY(),
                remaining.getWidth() - size.width, remaining.getHeight()
            );
        }
        else {
            throw new RuntimeException("Unrecognised title position.");
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            // TODO: fill in the correct values
            result.setSize(0.0, 0.0);
            result.setMessages(null);
        }
        else {
            result = new ArrangeResult(0.0, 0.0, null);
        }
        return result;
        
    }

    /**
     * Clears any cached layout information retained by the arrangement.
     */
    public void clear() {
        // nothing to clear
    }

    /**
     * Creates a rectangle that is aligned to the frame.
     * 
     * @param dimensions
     * @param frame
     * @param hAlign
     * @param vAlign
     * 
     * @return A rectangle.
     */
    private Rectangle2D createAlignedRectangle2D(Size2D dimensions, 
            Rectangle2D frame, HorizontalAlignment hAlign, 
            VerticalAlignment vAlign) {
        double x = Double.NaN;
        double y = Double.NaN;
        if (hAlign == HorizontalAlignment.LEFT) {
            x = frame.getX();   
        }
        else if (hAlign == HorizontalAlignment.CENTER) {
            x = frame.getCenterX() - (dimensions.width / 2.0);   
        }
        else if (hAlign == HorizontalAlignment.RIGHT) {
            x = frame.getMaxX() - dimensions.width;   
        }
        if (vAlign == VerticalAlignment.TOP) {
            y = frame.getY();   
        }
        else if (vAlign == VerticalAlignment.CENTER) {
            y = frame.getCenterY() - (dimensions.height / 2.0);   
        }
        else if (vAlign == VerticalAlignment.BOTTOM) {
            y = frame.getMaxY() - dimensions.height;   
        }
        
        return new Rectangle2D.Double(
            x, y, dimensions.width, dimensions.height
        );
    }

}
