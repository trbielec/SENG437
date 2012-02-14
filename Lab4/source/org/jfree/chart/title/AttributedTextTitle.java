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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, 
 * USA.  
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * ------------------------
 * AttributedTextTitle.java
 * ------------------------
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: AttributedTextTitle.java,v 1.6 2005/12/13 14:58:37 mungady Exp $
 *
 * Changes
 * -------
 * 21-Jun-2005 : Version 1, based on TextTitle.java (DG);
 * 
 */

package org.jfree.chart.title;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.LinkedList;
import java.util.List;

import org.jfree.chart.block.ACIUtilities;
import org.jfree.chart.block.ArrangeParams;
import org.jfree.chart.block.ArrangeResult;
import org.jfree.chart.block.BlockResult;
import org.jfree.chart.block.EntityBlockParams;
import org.jfree.chart.block.Message;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.event.TitleChangeEvent;
import org.jfree.text.TextBlock;
import org.jfree.text.TextBlockAnchor;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.Size2D;
import org.jfree.ui.VerticalAlignment;

/**
 * A chart title that displays an attributed text string.
 */
public class AttributedTextTitle extends Title 
                                 implements Serializable, Cloneable {

    /** For serialization. */
    private static final long serialVersionUID = 8372008692127477443L;
    
    /** The default font. */
    public static final Font DEFAULT_FONT 
        = new Font("SansSerif", Font.BOLD, 12);

    /** The default text color. */
    public static final Paint DEFAULT_PAINT = Color.black;

    /** The default font to use when none is specified in the attributes. */
    private Font defaultFont;
    
    /** The default paint to use when none is specified in the attributes. */
    private Paint defaultPaint;
    
    /** The title content. */
    private AttributedCharacterIterator content;

    private HorizontalAlignment lineAlignment = HorizontalAlignment.CENTER;
    
    /** The content. */
    private TextBlock textBlock;
    
    /**
     * Creates a new title, using default attributes where necessary.
     */
    public AttributedTextTitle() {
        this(new AttributedString("").getIterator());
    }

    /**
     * Creates a new title, using default attributes where necessary.
     *
     * @param content  the title content (<code>null</code> not permitted).
     */
    public AttributedTextTitle(AttributedCharacterIterator content) {
        this(content, DEFAULT_FONT, DEFAULT_PAINT, Title.DEFAULT_POSITION,
                Title.DEFAULT_HORIZONTAL_ALIGNMENT,
                Title.DEFAULT_VERTICAL_ALIGNMENT, Title.DEFAULT_PADDING);
    }

    /**
     * Creates a new title.
     * 
     * @param content  the title content.
     * @param defaultFont  the default font.
     * @param defaultPaint  the default paint.
     */
    public AttributedTextTitle(AttributedCharacterIterator content, 
            Font defaultFont, Paint defaultPaint) {
        this(content, defaultFont, defaultPaint, Title.DEFAULT_POSITION, 
                Title.DEFAULT_HORIZONTAL_ALIGNMENT, 
                Title.DEFAULT_VERTICAL_ALIGNMENT, Title.DEFAULT_PADDING);
    }
    
    /**
     * Creates a new title.
     *
     * @param content  the content for the title (<code>null</code> not 
     *                 permitted).
     * @param position  the title position (<code>null</code> not permitted).
     * @param horizontalAlignment  the horizontal alignment (<code>null</code> 
     *                             not permitted).
     * @param verticalAlignment  the vertical alignment (<code>null</code> not 
     *                           permitted).
     * @param padding  the space to leave around the outside of the title.
     */
    public AttributedTextTitle(AttributedCharacterIterator content, 
                               Font defaultFont, Paint defaultPaint, 
                               RectangleEdge position,
                               HorizontalAlignment horizontalAlignment, 
                               VerticalAlignment verticalAlignment,
                               RectangleInsets padding) {

        super(position, horizontalAlignment, verticalAlignment, padding);
        
        if (content == null) {
            throw new NullPointerException("Null 'content' argument.");
        }

        this.content = content;
        this.defaultFont = defaultFont;
        this.defaultPaint = defaultPaint;
        
        this.textBlock = null;  // this is set up each time by the arrange() 
                                // method
        this.lineAlignment = horizontalAlignment;
    }
    
    /**
     * Returns the default font.
     * 
     * @return The default font.
     */
    public Font getDefaultFont() {
        return this.defaultFont;
    }
    
    /**
     * Sets the default font.
     * 
     * @param font  the default font.
     */
    public void setDefaultFont(Font font) {
        this.defaultFont = font;    
    }
    
    /**
     * Returns the default paint.
     * 
     * @return The default paint.
     */
    public Paint getDefaultPaint() {
        return this.defaultPaint;
    }
    
    /**
     * Sets the default paint.
     * 
     * @param paint  the default paint.
     */
    public void setDefaultPaint(Paint paint) {
        this.defaultPaint = paint;   
    }
    
    /**
     * Returns the line alignment.
     * 
     * @return The line alignment.
     */
    public HorizontalAlignment getLineAlignment() {
        return this.lineAlignment;   
    }
    
    /**
     * Sets the line alignment.
     * 
     * @param alignment  the line alignment.
     */
    public void setLineAlignment(HorizontalAlignment alignment) {
        this.lineAlignment = alignment;   
    }

    /**
     * Returns the title content.
     *
     * @return The content (never <code>null</code>).
     */
    public AttributedCharacterIterator getContent() {
        return this.content;
    }

    /**
     * Sets the title content to the specified value and sends a 
     * {@link TitleChangeEvent} to all registered listeners.
     *
     * @param content  the content (<code>null</code> not permitted).
     */
    public void setContent(AttributedCharacterIterator content) {
        if (content == null) {
            throw new NullPointerException("Null 'content' argument.");
        }
        this.content = content;
        notifyListeners(new TitleChangeEvent(this));
    }

    /**
     * Calculates the size of the title content (excludes margin, border and 
     * padding) if there is no constraint.  This is either the natural size
     * of the text, or the block size if this has been specified manually.
     * 
     * @param g2  the graphics device.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeNN(Graphics2D g2, ArrangeParams params) {
        double w = getDefaultWidth();
        double h = getDefaultHeight();
        this.textBlock = ACIUtilities.createTextBlock(this.content, 
                this.defaultFont, this.defaultPaint, this.lineAlignment);
        Size2D naturalSize = this.textBlock.calculateDimensions(g2);
        
        // transpose the dimensions if the title is positioned at the left
        // or the right, because the text is rotated in that case
        RectangleEdge position = getPosition();
        if (position == RectangleEdge.LEFT || position == RectangleEdge.RIGHT) {
            naturalSize = new Size2D(naturalSize.height, naturalSize.width);
        }

        if (w < 0.0) {
            w = naturalSize.getWidth();   
        }
        else {
            w = trimToContentWidth(w);
        }
        if (h < 0.0) {
            h = naturalSize.getHeight();   
        }
        else {
            h = trimToContentHeight(h);   
        }
        // TODO: log if the text block doesn't fit the calculated dimensions
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(w, h);
        }
        else {
            result = new ArrangeResult(w, h, null);
        }
        return result;
    }
 
    /**
     * Arranges the block with no width constraint and a fixed height, 
     * returning the size of the content.
     * 
     * @param g2  the graphics device.
     * @param fixedHeight  the fixed height.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeNF(Graphics2D g2, double fixedHeight, 
            ArrangeParams params) {
        
        ArrangeResult result = arrangeNN(g2, params);
        
        // TODO log if the resulting height is taller than the fixedHeight
        result.setHeight(fixedHeight);
        return result;
    }
    
    /**
     * Arranges the block with a fixed width and no height constraint, 
     * returning the size of the content.
     * 
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed width.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeFN(Graphics2D g2, double fixedWidth, 
            ArrangeParams params) {
        
        this.textBlock = ACIUtilities.createTextBlock(this.content, 
                this.defaultFont, this.defaultPaint, this.lineAlignment);
        Size2D naturalSize = this.textBlock.calculateDimensions(g2);
        
        // transpose the dimensions if the title is positioned at the left
        // or the right, because the text is rotated in that case
        RectangleEdge position = getPosition();
        if (position == RectangleEdge.LEFT || position == RectangleEdge.RIGHT) {
            naturalSize = new Size2D(naturalSize.height, naturalSize.width);
        }
        
        // TODO: log if the title is too wide
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(fixedWidth, naturalSize.getHeight());
        }
        else {
            result = new ArrangeResult(fixedWidth, naturalSize.getHeight(), null);
        }
        return result;
    }
        
    /**
     * Arranges the title with a fixed width and height.
     * 
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed (content) width.
     * @param fixedHeight  the fixed (content) height.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeFF(Graphics2D g2, double fixedWidth, 
                               double fixedHeight, ArrangeParams params) {
        
        List messages = null;
        if (params.isLogEnabled()) {
            messages = new LinkedList();
        }
        this.textBlock = ACIUtilities.createTextBlock(this.content, 
                this.defaultFont, this.defaultPaint, this.lineAlignment);
        
        if (messages != null) {
            
            // check if the text block will fit the constraints
            Size2D size = this.textBlock.calculateDimensions(g2);
            // transpose the dimensions if the title is positioned at the left
            // or the right, because the text is rotated in that case
            RectangleEdge position = getPosition();
            if (position == RectangleEdge.LEFT 
                    || position == RectangleEdge.RIGHT) {
                size = new Size2D(size.height, size.width);
            }

            if (size.width > fixedWidth) {
                messages.add(new Message(this, 
                        "Text too wide to fit without clipping."));
            }
            if (size.height > fixedHeight) {
                messages.add(new Message(this, 
                        "Text too tall to fit without clipping."));
            }
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(fixedWidth, fixedHeight);
            if (messages != null) {
                result.getMessages().addAll(messages);
            }
        }
        else {
            result = new ArrangeResult(fixedWidth, fixedHeight, messages);
        }
        return result;   
    }

    /**
     * Draws the title on a Java 2D graphics device (such as the screen or a 
     * printer).
     *
     * @param g2  the graphics device.
     * @param area  the area allocated for the title.
     */
    public void draw(Graphics2D g2, Rectangle2D area) {
        draw(g2, area, null);
    }
    
    /**
     * Draws the block within the specified area.
     * 
     * @param g2  the graphics device.
     * @param area  the area.
     * @param params  if this is an instance of {@link EntityBlockParams} it
     *                is used to determine whether or not an 
     *                {@link EntityCollection} is returned by this method.
     * 
     * @return An {@link EntityCollection} containing a chart entity for the
     *         title, or <code>null</code>.
     */
    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        Shape savedClip = g2.getClip();
        g2.clip(area);
        if (this.content == null) {
            g2.setClip(savedClip);
            return null;   
        }
        area = (Rectangle2D) area.clone();
        Paint backgroundPaint = getBackgroundPaint();
        if (backgroundPaint != null) {
            g2.setPaint(backgroundPaint);
            g2.fill(area);
        }
        area = trimMargin(area);
        drawBorder(g2, area);
        ChartEntity entity = null;
        if (params instanceof EntityBlockParams) {
            EntityBlockParams p = (EntityBlockParams) params;
            if (p.getGenerateEntities()) {
                entity = new ChartEntity(area, getToolTipText(), getURLText());    
            }
        }
        area = trimBorder(area);
        Paint interiorBackgroundPaint = getInteriorBackgroundPaint();
        if (interiorBackgroundPaint != null) {
            g2.setPaint(interiorBackgroundPaint);
            g2.fill(area);
        }
        area = trimPadding(area);
        RectangleEdge position = getPosition();
        if (position == RectangleEdge.TOP || position == RectangleEdge.BOTTOM) {
            drawHorizontal(g2, area);
        }
        else if (position == RectangleEdge.LEFT 
                 || position == RectangleEdge.RIGHT) {
            drawVertical(g2, area);
        }
        BlockResult result = new BlockResult();
        if (entity != null) {
            StandardEntityCollection sec = new StandardEntityCollection();
            sec.add(entity);
            result.setEntityCollection(sec);
        }
        g2.setClip(savedClip);
        return result;
    }

    /**
     * Draws a the title horizontally within the specified area.  This method 
     * will be called from the {@link #draw(Graphics2D, Rectangle2D) draw} 
     * method.
     * 
     * @param g2  the graphics device.
     * @param area  the area for the title.
     */
    protected void drawHorizontal(Graphics2D g2, Rectangle2D area) {
        Rectangle2D titleArea = (Rectangle2D) area.clone();
        TextBlockAnchor anchor = null;
        float x = 0.0f;
        HorizontalAlignment horizontalAlignment = getHorizontalAlignment();
        if (horizontalAlignment == HorizontalAlignment.LEFT) {
            x = (float) titleArea.getX();
            anchor = TextBlockAnchor.TOP_LEFT;
        }
        else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
            x = (float) titleArea.getMaxX();
            anchor = TextBlockAnchor.TOP_RIGHT;
        }
        else if (horizontalAlignment == HorizontalAlignment.CENTER) {
            x = (float) titleArea.getCenterX();
            anchor = TextBlockAnchor.TOP_CENTER;
        }
        float y = 0.0f;
        RectangleEdge position = getPosition();
        if (position == RectangleEdge.TOP) {
            y = (float) titleArea.getY();
        }
        else if (position == RectangleEdge.BOTTOM) {
            y = (float) titleArea.getMaxY();
            if (horizontalAlignment == HorizontalAlignment.LEFT) {
                anchor = TextBlockAnchor.BOTTOM_LEFT;
            }
            else if (horizontalAlignment == HorizontalAlignment.CENTER) {
                anchor = TextBlockAnchor.BOTTOM_CENTER;
            }
            else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
                anchor = TextBlockAnchor.BOTTOM_RIGHT;
            }
        }
        this.textBlock.draw(g2, x, y, anchor);
    }
    
    /**
     * Draws a the title vertically within the specified area.  This method 
     * will be called from the {@link #draw(Graphics2D, Rectangle2D) draw} 
     * method.
     * 
     * @param g2  the graphics device.
     * @param area  the area for the title.
     */
    protected void drawVertical(Graphics2D g2, Rectangle2D area) {
        Rectangle2D titleArea = (Rectangle2D) area.clone();
        TextBlockAnchor anchor = null;
        float y = 0.0f;
        VerticalAlignment verticalAlignment = getVerticalAlignment();
        if (verticalAlignment == VerticalAlignment.TOP) {
            y = (float) titleArea.getY();
            anchor = TextBlockAnchor.TOP_RIGHT;
        }
        else if (verticalAlignment == VerticalAlignment.BOTTOM) {
            y = (float) titleArea.getMaxY();
            anchor = TextBlockAnchor.TOP_LEFT;
        }
        else if (verticalAlignment == VerticalAlignment.CENTER) {
            y = (float) titleArea.getCenterY();
            anchor = TextBlockAnchor.TOP_CENTER;
        }
        float x = 0.0f;
        RectangleEdge position = getPosition();
        if (position == RectangleEdge.LEFT) {
            x = (float) titleArea.getX();
        }
        else if (position == RectangleEdge.RIGHT) {
            x = (float) titleArea.getMaxX();
            if (verticalAlignment == VerticalAlignment.TOP) {
                anchor = TextBlockAnchor.BOTTOM_RIGHT;
            }
            else if (verticalAlignment == VerticalAlignment.CENTER) {
                anchor = TextBlockAnchor.BOTTOM_CENTER;
            }
            else if (verticalAlignment == VerticalAlignment.BOTTOM) {
                anchor = TextBlockAnchor.BOTTOM_LEFT;
            }
        }
        this.textBlock.draw(g2, x, y, anchor, x, y, -Math.PI / 2.0);
    }

    /**
     * Tests this title for equality with another object.
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return <code>true</code> or <code>false</code>.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AttributedTextTitle)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        AttributedTextTitle that = (AttributedTextTitle) obj;
        if (!ACIUtilities.equal(this.content, that.content)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code.
     * 
     * @return A hash code.
     */
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (this.content != null 
                ? this.content.hashCode() : 0);
        return result;
    }

    /**
     * Returns a clone of this object.
     * 
     * @return A clone.
     * 
     * @throws CloneNotSupportedException never.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}

