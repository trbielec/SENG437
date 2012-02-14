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
 * -----------------
 * ACITextTitle.java
 * -----------------
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: ACITextTitle.java,v 1.3 2005/07/19 14:22:57 mungady Exp $
 *
 * Changes
 * -------
 * 15-Jun-2005 : Version 1 (DG);
 * 
 */

package org.jfree.chart.title;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;

import org.jfree.chart.block.ArrangeParams;
import org.jfree.chart.block.ArrangeResult;
import org.jfree.chart.block.BlockResult;
import org.jfree.chart.block.CharterAttributedCharacterIterator;
import org.jfree.chart.block.EntityBlockParams;
import org.jfree.chart.block.Message;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.event.TitleChangeEvent;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.ObjectUtilities;

/**
 * A chart title that displays a title based on an 
 * {@link AttributedCharacterIterator}.
 */
public class ACITextTitle extends Title 
                          implements Serializable, Cloneable {

    // TODO: this class could do with a better name
    
    /** For serialization. */
    //private static final long serialVersionUID = ???L;

    /** The title text. */
    private AttributedCharacterIterator text;

    /** The tool tip text (can be <code>null</code>). */
    private String toolTipText;
    
    /** The URL text (can be <code>null</code>). */
    private String urlText;
    
    /** The content. */
    private TextLayout content;
    
    /**
     * Creates a new title, using default attributes where necessary.
     */
    public ACITextTitle() {
        this((AttributedCharacterIterator) new AttributedString(""));
    }

    /**
     * Creates a new title, using default attributes where necessary.
     *
     * @param text  the title text (<code>null</code> not permitted).
     */
    public ACITextTitle(AttributedCharacterIterator text) {
        this(
            text,
            Title.DEFAULT_POSITION,
            Title.DEFAULT_HORIZONTAL_ALIGNMENT,
            Title.DEFAULT_VERTICAL_ALIGNMENT,
            Title.DEFAULT_PADDING
        );
    }

    /**
     * Creates a new title.
     *
     * @param text  the text for the title (<code>null</code> not permitted).
     * @param position  the title position (<code>null</code> not permitted).
     * @param horizontalAlignment  the horizontal alignment (<code>null</code> 
     *                             not permitted).
     * @param verticalAlignment  the vertical alignment (<code>null</code> not 
     *                           permitted).
     * @param padding  the space to leave around the outside of the title.
     */
    public ACITextTitle(AttributedCharacterIterator text, 
                        RectangleEdge position,
                        HorizontalAlignment horizontalAlignment, 
                        VerticalAlignment verticalAlignment,
                        RectangleInsets padding) {

        super(position, horizontalAlignment, verticalAlignment, padding);
        
        if (text == null) {
            throw new NullPointerException("Null 'text' argument.");
        }
        this.text = text;
        this.content = null;
        this.toolTipText = null;
        this.urlText = null;
        
    }

    /**
     * Returns the title text.
     *
     * @return The text (never <code>null</code>).
     */
    public AttributedCharacterIterator getText() {
        return this.text;
    }

    /**
     * Sets the title to the specified text and sends a 
     * {@link TitleChangeEvent} to all registered listeners.
     *
     * @param text  the text (<code>null</code> not permitted).
     */
    public void setText(AttributedCharacterIterator text) {
        if (text == null) {
            throw new NullPointerException("Null 'text' argument.");
        }
        this.text = text;
        notifyListeners(new TitleChangeEvent(this));
    }

    /**
     * Returns the tool tip text.
     *
     * @return The tool tip text (possibly <code>null</code>).
     */
    public String getToolTipText() {
        return this.toolTipText;
    }

    /**
     * Sets the tool tip text to the specified text and sends a 
     * {@link TitleChangeEvent} to all registered listeners.
     *
     * @param text  the text (<code>null</code> permitted).
     */
    public void setToolTipText(String text) {
        this.toolTipText = text;
        notifyListeners(new TitleChangeEvent(this));
    }

    /**
     * Returns the URL text.
     *
     * @return The URL text (possibly <code>null</code>).
     */
    public String getURLText() {
        return this.toolTipText;
    }

    /**
     * Sets the URL text to the specified text and sends a 
     * {@link TitleChangeEvent} to all registered listeners.
     *
     * @param text  the text (<code>null</code> permitted).
     */
    public void setURLText(String text) {
        this.urlText = text;
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
        CharterAttributedCharacterIterator it 
            = new CharterAttributedCharacterIterator(this.text, 
                    g2.getFontRenderContext());
        this.content = new TextLayout(it, g2.getFontRenderContext());
        Rectangle2D naturalSize = this.content.getBounds();
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
        
        List messages = null;
        boolean logging = params.isLogEnabled();
        if (logging) {
            messages = new java.util.LinkedList();
        }
        
        //TODO: if the fixed height is too small, this should generate a
        // warning
        ArrangeResult r = arrangeNN(g2, params);
        double w = r.getWidth();
        double h = r.getHeight();
        if (messages != null) {
            if (fixedHeight < h) {
                messages.add(new Message(this, 
                        "Title taller than fixed height."));    
            }
        }
        return new ArrangeResult(w, fixedHeight, messages);
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
        
        // TODO: no wrapping occurs here to meet the fixed width constraint.
        // we should return some kind of warning if the text is too wide
        CharterAttributedCharacterIterator it 
            = new CharterAttributedCharacterIterator(this.text, 
                    g2.getFontRenderContext());
        this.content = new TextLayout(it, g2.getFontRenderContext());
        double h = getDefaultHeight();
        if (h < 0.0) {
            Rectangle2D naturalSize = this.content.getBounds();
            h = naturalSize.getHeight();   
        }
        else {
            h = trimToContentHeight(h);   
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(fixedWidth, h);
        }
        else {
            result = new ArrangeResult(fixedWidth, h, null);
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
        //TODO: if the content doesn't fit the given dimensions, we need to 
        // (a) truncate the text and (b) return a warning.
        CharterAttributedCharacterIterator it 
            = new CharterAttributedCharacterIterator(this.text, 
                    g2.getFontRenderContext());
        this.content = new TextLayout(it, g2.getFontRenderContext());
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(fixedWidth, fixedHeight);
        }
        else {
            result = new ArrangeResult(fixedWidth, fixedHeight, null);
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
        if (this.content == null) {
            return null;   
        }
        area = (Rectangle2D) area.clone();
        Paint backgroundPaint = getBackgroundPaint();
        if (backgroundPaint != null) {
            g2.setPaint(backgroundPaint);
            g2.fill(area);
        }
        area = trimMargin(area);
        Rectangle2D interior = (Rectangle2D) area.clone();
        if (this.text.equals("")) {
            return null;
        }
        ChartEntity entity = null;
        if (params instanceof EntityBlockParams) {
            EntityBlockParams p = (EntityBlockParams) params;
            if (p.getGenerateEntities()) {
                entity = new ChartEntity(area, this.toolTipText, this.urlText);    
            }
        }
        interior = trimBorder(interior);
        Paint interiorBackgroundPaint = getInteriorBackgroundPaint();
        if (interiorBackgroundPaint != null) {
            g2.setPaint(interiorBackgroundPaint);
            g2.fill(interior);
        }
        drawBorder(g2, area);
        interior = trimPadding(interior);
        RectangleEdge position = getPosition();
        if (position == RectangleEdge.TOP || position == RectangleEdge.BOTTOM) {
            drawHorizontal(g2, interior);
        }
        else if (position == RectangleEdge.LEFT 
                 || position == RectangleEdge.RIGHT) {
            drawVertical(g2, interior);
        }
        BlockResult result = new BlockResult();
        if (entity != null) {
            StandardEntityCollection sec = new StandardEntityCollection();
            sec.add(entity);
            result.setEntityCollection(sec);
        }
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
        float x = 0.0f;
        HorizontalAlignment horizontalAlignment = getHorizontalAlignment();
        if (horizontalAlignment == HorizontalAlignment.LEFT) {
            x = (float) titleArea.getX();
        }
        else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
            x = (float) titleArea.getMaxX();
        }
        else if (horizontalAlignment == HorizontalAlignment.CENTER) {
            x = (float) titleArea.getCenterX() 
                - this.content.getAdvance() / 2.0f;
        }
        float y = (float) titleArea.getMaxY() - this.content.getDescent() 
                - this.content.getLeading();
        this.content.draw(g2, x, y);
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
        float y = 0.0f;
        VerticalAlignment verticalAlignment = getVerticalAlignment();
        if (verticalAlignment == VerticalAlignment.TOP) {
            y = (float) titleArea.getY();
        }
        else if (verticalAlignment == VerticalAlignment.BOTTOM) {
            y = (float) titleArea.getMaxY();
        }
        else if (verticalAlignment == VerticalAlignment.CENTER) {
            y = (float) titleArea.getCenterY();
        }
        float x = 0.0f;
        RectangleEdge position = getPosition();
        if (position == RectangleEdge.LEFT) {
            x = (float) titleArea.getX();
        }
        else if (position == RectangleEdge.RIGHT) {
            x = (float) titleArea.getMaxX();
        }
        // TODO: fix this
        this.content.draw(g2, x, y);
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
        if (!(obj instanceof ACITextTitle)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        ACITextTitle that = (ACITextTitle) obj;
        if (!ObjectUtilities.equal(this.text, that.text)) {
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
        result = 29 * result + (this.text != null ? this.text.hashCode() : 0);
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

