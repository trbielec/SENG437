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
 * AbstractContentBlock.java
 * -------------------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: AbstractContentBlock.java,v 1.2 2005/07/19 14:27:02 mungady Exp $
 *
 * Changes:
 * --------
 * 22-Oct-2004 : Version 1 (DG);
 * 02-Feb-2005 : Added accessor methods for margin (DG);
 * 04-Feb-2005 : Added equals() method and implemented Serializable (DG);
 * 03-May-2005 : Added null argument checks (DG);
 * 06-May-2005 : Added convenience methods for setting margin, border and 
 *               padding (DG);
 * 20-Jun-2005 : Renamed AbstractBlock --> AbstractContentBlock (DG);
 * 
 */

package org.jfree.chart.block;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jfree.data.Range;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.Size2D;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;

/**
 * A convenience class for creating new classes that implement 
 * the {@link ContentBlock} interface.
 */
public abstract class AbstractContentBlock 
                implements ContentBlock, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 7689852412141274563L;
    
    /** The id for the block. */
    private String id;
    
    /** The margin around the outside of the block. */
    private RectangleInsets margin;
    
    /** The border for the block. */
    private BlockBorder border;

    /** The padding between the block content and the border. */
    private RectangleInsets padding;
    
    /** 
     * The default width of the block (may be overridden if there are 
     * constraints in sizing).
     */
    private double defaultWidth;
    
    /** 
     * The default height of the block (may be overridden if there are 
     * constraints in sizing).
     */
    private double defaultHeight;
    
    /**
     * The current bounds for the block (position of the block in Java2D space).
     */
    private transient Rectangle2D bounds;
    
    /** The background paint (<code>null</code> permitted). */
    private transient Paint backgroundPaint;
    
    /** 
     * The background paint for the area inside the border (<code>null</code>
     * permitted).
     */
    private transient Paint interiorBackgroundPaint;

    /**
     * Creates a new block.
     */
    protected AbstractContentBlock() {
        this.id = null;
        this.defaultWidth = -1.0;  // not specified
        this.defaultHeight = -1.0;  // not specified
        this.bounds = new Rectangle2D.Float();
        this.margin = RectangleInsets.ZERO_INSETS;
        this.border = BlockBorder.NONE; 
        this.padding = RectangleInsets.ZERO_INSETS;
        this.backgroundPaint = null;
        this.interiorBackgroundPaint = null;
    }
    
    /**
     * Returns the id.
     * 
     * @return The id (possibly <code>null</code>).
     */
    public String getID() {
        return this.id;   
    }
    
    /**
     * Sets the id for the block.
     * 
     * @param id  the id (<code>null</code> permitted).
     */
    public void setID(String id) {
        this.id = id;   
    }
    
    /**
     * Returns the default width of the block, if this is known in advance.
     * The actual width of the block may be overridden if layout constraints
     * make this necessary.  
     * 
     * @return The default width.
     */
    public double getDefaultWidth() {
        return this.defaultWidth;
    }
    
    /**
     * Sets the default width of the block, if this is known in advance.
     * 
     * @param width  the width (in Java2D units)
     */
    public void setDefaultWidth(double width) {
        this.defaultWidth = width;
    }
    
    /**
     * Returns the default height of the block, if this is known in advance.
     * The actual height of the block may be overridden if layout constraints
     * make this necessary.  
     * 
     * @return The default height.
     */
    public double getDefaultHeight() {
        return this.defaultHeight;
    }
    
    /**
     * Sets the default width of the block, if this is known in advance.
     * 
     * @param height  the height (in Java2D units)
     */
    public void setDefaultHeight(double height) {
        this.defaultHeight = height;
    }
    
    /**
     * Returns the margin.
     * 
     * @return The margin (never <code>null</code>).
     */
    public RectangleInsets getMargin() {
        return this.margin;
    }

    /**
     * Sets the margin (use {@link RectangleInsets#ZERO_INSETS} for no 
     * padding).
     * 
     * @param margin  the margin (<code>null</code> not permitted).
     */
    public void setMargin(RectangleInsets margin) {
        if (margin == null) {
            throw new IllegalArgumentException("Null 'margin' argument.");   
        }
        this.margin = margin;
    }

    /**
     * Sets the margin using the specified values in Java2D units.
     * 
     * @param top  the top margin.
     * @param left  the left margin.
     * @param bottom  the bottom margin.
     * @param right  the right margin.
     */
    public void setMargin(double top, double left, double bottom, 
                          double right) {
        setMargin(new RectangleInsets(top, left, bottom, right));
    }

    /**
     * Returns the border.
     * 
     * @return The border (never <code>null</code>).
     */
    public BlockBorder getBorder() {
        return this.border;
    }
    
    /**
     * Sets the border for the block (use {@link BlockBorder#NONE} for
     * no border).
     * 
     * @param border  the border (<code>null</code> not permitted).
     */
    public void setBorder(BlockBorder border) {
        if (border == null) {
            throw new IllegalArgumentException("Null 'border' argument.");   
        }
        this.border = border;
    }
    
    /**
     * Sets a black border with the specified line widths.
     * 
     * @param top  the top border line width.
     * @param left  the left border line width.
     * @param bottom  the bottom border line width.
     * @param right  the right border line width.
     */
    public void setBorder(double top, double left, double bottom, 
                          double right) {
        setBorder(new BlockBorder(top, left, bottom, right));
    }
    
    /**
     * Returns the padding.
     * 
     * @return The padding (never <code>null</code>).
     */
    public RectangleInsets getPadding() {
        return this.padding;
    }
    
    /**
     * Sets the padding (use {@link RectangleInsets#ZERO_INSETS} for no 
     * padding).
     * 
     * @param padding  the padding (<code>null</code> not permitted).
     */
    public void setPadding(RectangleInsets padding) {
        if (padding == null) {
            throw new IllegalArgumentException("Null 'padding' argument.");   
        }
        this.padding = padding;
    }
    
    /**
     * Sets the padding.
     * 
     * @param top  the top padding.
     * @param left  the left padding.
     * @param bottom  the bottom padding.
     * @param right  the right padding.
     */
    public void setPadding(double top, double left, double bottom, 
                           double right) {
        setPadding(new RectangleInsets(top, left, bottom, right));
    }
    
    /**
     * Returns the background paint for the block.  This is used to fill the
     * entire background area for the block.  To fill only the area inside the
     * block's border, use {@link #setInteriorBackgroundPaint(Paint)}.  If
     * the paint is <code>null</code>, the block will appear to be transparent.
     * 
     * @return The background paint (possibly <code>null</code>).
     */
    public Paint getBackgroundPaint() {
        return this.backgroundPaint;   
    }
    
    /**
     * Sets the background paint for the block.
     * 
     * @param paint  the paint (<code>null</code> permitted).
     */
    public void setBackgroundPaint(Paint paint) {
        this.backgroundPaint = paint;
    }
    
    /**
     * Returns the interior background paint for the block.  This is used to 
     * fill the background area inside the block's border (which we call the
     * interior).  To fill the entire background area (that is, including the
     * margin area), use {@link #setBackgroundPaint(Paint)}.  If the paint is 
     * <code>null</code>, the interior of the block will appear to be 
     * transparent.
     * 
     * @return The background paint (possibly <code>null</code>).
     */
    public Paint getInteriorBackgroundPaint() {
        return this.interiorBackgroundPaint;   
    }
    
    /**
     * Sets the interior background paint for the block.
     * 
     * @param paint  the paint (<code>null</code> permitted).
     */
    public void setInteriorBackgroundPaint(Paint paint) {
        this.interiorBackgroundPaint = paint;   
    }
    
    public double getContentXOffset() {
        return this.margin.getLeft() + this.border.getInsets().getLeft() + this.padding.getLeft();    
    }
    
    public double getContentYOffset() {
        return this.margin.getTop() + this.border.getInsets().getTop() + this.padding.getTop();   
    }
    
    /**
     * Arranges the contents of the block, with no constraints, and returns 
     * the block size.
     * 
     * @param g2  the graphics device.
     * @param params  optional parameters.
     * 
     * @return The size of the block following the arrangement.
     * 
     * @deprecated use the other arrange method.
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
     * @return The layout result.
     */
    public abstract ArrangeResult arrange(Graphics2D g2, 
            RectangleConstraint constraint, ArrangeParams params);

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
     * @param bounds  the bounds (<code>null</code> not permitted).
     */
    public void setBounds(Rectangle2D bounds) {
        if (bounds == null) {
            throw new IllegalArgumentException("Null 'bounds' argument.");
        }
        this.bounds = bounds;
    }
    
    /**
     * Calculate the width available for content after subtracting 
     * the margin, border and padding space from the specified fixed 
     * width.
     * 
     * @param fixedWidth  the fixed (total) width.
     * 
     * @return The available space.
     */
    public double trimToContentWidth(double fixedWidth) {
        double result = this.margin.trimWidth(fixedWidth);
        result = this.border.getInsets().trimWidth(result);
        result = this.padding.trimWidth(result);
        return result;
    }

    /**
     * Calculate the height available for content after subtracting 
     * the margin, border and padding space from the specified fixed 
     * height.
     * 
     * @param fixedHeight  the fixed (total) height.
     * 
     * @return The available space.
     */
    public double trimToContentHeight(double fixedHeight) {
        double result = this.margin.trimHeight(fixedHeight);
        result = this.border.getInsets().trimHeight(result);
        result = this.padding.trimHeight(result);
        return result;
    }
    
    /**
     * Returns a constraint for the content of this block that will result in
     * the bounds of the block matching the specified constraint.
     * 
     * @param c  the outer constraint (<code>null</code> not permitted).
     * 
     * @return The content constraint.
     */
    public RectangleConstraint toContentConstraint(RectangleConstraint c) {
        if (c == null) {
            throw new IllegalArgumentException("Null 'c' argument.");
        }
        if (c.equals(RectangleConstraint.NONE)) {
            return c;
        }
        double w = c.getWidth();
        Range wr = c.getWidthRange();
        double h = c.getHeight();
        Range hr = c.getHeightRange();
        double ww = trimToContentWidth(w);
        double hh = trimToContentHeight(h);
        Range wwr = trimToContentWidth(wr);
        Range hhr = trimToContentHeight(hr);
        return new RectangleConstraint(
            ww, wwr, c.getWidthConstraintType(), 
            hh, hhr, c.getHeightConstraintType()
        );
    }

    public Range trimToContentWidth(Range r) {
        if (r == null) {
            return null;   
        }
        double lowerBound = 0.0;
        double upperBound = Double.POSITIVE_INFINITY;
        if (r.getLowerBound() > 0.0) {
            lowerBound = trimToContentWidth(r.getLowerBound());   
        }
        if (r.getUpperBound() < Double.POSITIVE_INFINITY) {
            upperBound = trimToContentWidth(r.getUpperBound());
        }
        return new Range(lowerBound, upperBound);
    }
    
    public Range trimToContentHeight(Range r) {
        if (r == null) {
            return null;   
        }
        double lowerBound = 0.0;
        double upperBound = Double.POSITIVE_INFINITY;
        if (r.getLowerBound() > 0.0) {
            lowerBound = trimToContentHeight(r.getLowerBound());   
        }
        if (r.getUpperBound() < Double.POSITIVE_INFINITY) {
            upperBound = trimToContentHeight(r.getUpperBound());
        }
        return new Range(lowerBound, Math.max(lowerBound, upperBound));
    }
    
    /**
     * Adds the margin, border and padding to the specified content width.
     * 
     * @param contentWidth  the content width.
     * 
     * @return The adjusted width.
     */
    public double calculateTotalWidth(double contentWidth) {
        double result = contentWidth;
        result = this.padding.extendWidth(result);
        result = this.border.getInsets().extendWidth(result);
        result = this.margin.extendWidth(result);
        return result;
    }

    /**
     * Adds the margin, border and padding to the specified content height.
     * 
     * @param contentHeight  the content height.
     * 
     * @return The adjusted height.
     */
    public double calculateTotalHeight(double contentHeight) {
        double result = contentHeight;
        result = this.padding.extendHeight(result);
        result = this.border.getInsets().extendHeight(result);
        result = this.margin.extendHeight(result);
        return result;
    }

    /**
     * Reduces the specified area by the amount of space consumed 
     * by the margin.
     * 
     * @param area  the area (<code>null</code> not permitted).
     * 
     * @return The trimmed area.
     */
    public Rectangle2D trimMargin(Rectangle2D area) {
        // defer argument checking...
        this.margin.trim(area);
        return area;
    }
    
    /**
     * Reduces the specified area by the amount of space consumed 
     * by the border.
     * 
     * @param area  the area (<code>null</code> not permitted).
     * 
     * @return The trimmed area.
     */
    public Rectangle2D trimBorder(Rectangle2D area) {
        // defer argument checking...
        this.border.getInsets().trim(area);
        return area;
    }

    /**
     * Reduces the specified area by the amount of space consumed 
     * by the padding.
     * 
     * @param area  the area (<code>null</code> not permitted).
     * 
     * @return The trimmed area.
     */
    public Rectangle2D trimPadding(Rectangle2D area) {
        // defer argument checking...
        this.padding.trim(area);
        return area;
    }

    /**
     * Draws the border around the perimeter of the specified area.
     * 
     * @param g2  the graphics device.
     * @param area  the area.
     */
    protected void drawBorder(Graphics2D g2, Rectangle2D area) {
        this.border.draw(g2, area);
    }
    
    /**
     * Tests this block for equality with an arbitrary object.
     * 
     * @param obj  the object (<code>null</code> permitted).
     * 
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;   
        }
        if (!(obj instanceof AbstractContentBlock)) {
            return false;   
        }
        AbstractContentBlock that = (AbstractContentBlock) obj;
        if (!ObjectUtilities.equal(this.id, that.id)) {
            return false;   
        }
        if (!this.border.equals(that.border)) {
            return false;   
        }
        if (!this.bounds.equals(that.bounds)) {
            return false;   
        }
        if (!this.margin.equals(that.margin)) {
            return false;   
        }
        if (!this.padding.equals(that.padding)) {
            return false;   
        }
        if (this.defaultHeight != that.defaultHeight) {
            return false;   
        }
        if (this.defaultWidth != that.defaultWidth) {
            return false;   
        }
        if (!PaintUtilities.equal(this.backgroundPaint, that.backgroundPaint)) {
            return false;   
        }
        if (!PaintUtilities.equal(this.interiorBackgroundPaint, 
                that.interiorBackgroundPaint)) {
            return false;   
        }
        return true;
    }
    
    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writeShape(this.bounds, stream);
        SerialUtilities.writePaint(this.backgroundPaint, stream);
        SerialUtilities.writePaint(this.interiorBackgroundPaint, stream);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream) 
        throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.bounds = (Rectangle2D) SerialUtilities.readShape(stream);
        this.backgroundPaint = SerialUtilities.readPaint(stream);
        this.interiorBackgroundPaint = SerialUtilities.readPaint(stream);
    }

}
