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
 * --------------
 * TextTitle.java
 * --------------
 * (C) Copyright 2000-2005, by David Berry and Contributors.
 *
 * Original Author:  David Berry;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *                   Nicolas Brodu;
 *
 * $Id: TextTitle.java,v 1.21 2005/12/13 14:58:37 mungady Exp $
 *
 * Changes (from 18-Sep-2001)
 * --------------------------
 * 18-Sep-2001 : Added standard header (DG);
 * 07-Nov-2001 : Separated the JCommon Class Library classes, JFreeChart now 
 *               requires jcommon.jar (DG);
 * 09-Jan-2002 : Updated Javadoc comments (DG);
 * 07-Feb-2002 : Changed Insets --> Spacer in AbstractTitle.java (DG);
 * 06-Mar-2002 : Updated import statements (DG);
 * 25-Jun-2002 : Removed redundant imports (DG);
 * 18-Sep-2002 : Fixed errors reported by Checkstyle (DG);
 * 28-Oct-2002 : Small modifications while changing JFreeChart class (DG);
 * 13-Mar-2003 : Changed width used for relative spacing to fix bug 703050 (DG);
 * 26-Mar-2003 : Implemented Serializable (DG);
 * 15-Jul-2003 : Fixed null pointer exception (DG);
 * 11-Sep-2003 : Implemented Cloneable (NB)
 * 22-Sep-2003 : Added checks for null values and throw nullpointer 
 *               exceptions (TM); 
 *               Background paint was not serialized.
 * 07-Oct-2003 : Added fix for exception caused by empty string in title (DG);
 * 29-Oct-2003 : Added workaround for text alignment in PDF output (DG);
 * 03-Feb-2004 : Fixed bug in getPreferredWidth() method (DG);
 * 17-Feb-2004 : Added clone() method and fixed bug in equals() method (DG);
 * 01-Apr-2004 : Changed java.awt.geom.Dimension2D to org.jfree.ui.Size2D 
 *               because of JDK bug 4976448 which persists on JDK 1.3.1.  Also
 *               fixed bug in getPreferredHeight() method (DG);
 * 29-Apr-2004 : Fixed bug in getPreferredWidth() method - see bug id 
 *               944173 (DG);
 * 11-Jan-2005 : Removed deprecated code in preparation for the 1.0.0 
 *               release (DG);
 * 08-Feb-2005 : Updated for changes in RectangleConstraint class (DG);
 * 11-Feb-2005 : Implemented PublicCloneable (DG);
 * 20-Apr-2005 : Added support for tooltips (DG);
 * 26-Apr-2005 : Removed LOGGER (DG);
 * 06-Jun-2005 : Modified equals() to handle GradientPaint (DG);
 * 
 */

package org.jfree.chart.title;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import org.jfree.chart.block.ArrangeParams;
import org.jfree.chart.block.ArrangeResult;
import org.jfree.chart.block.BlockResult;
import org.jfree.chart.block.EntityBlockParams;
import org.jfree.chart.block.Message;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.event.TitleChangeEvent;
import org.jfree.data.Range;
import org.jfree.io.SerialUtilities;
import org.jfree.text.G2TextMeasurer;
import org.jfree.text.TextBlock;
import org.jfree.text.TextBlockAnchor;
import org.jfree.text.TextUtilities;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.Size2D;
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;

/**
 * A chart title that displays a text string with automatic wrapping as 
 * required.
 */
public class TextTitle extends Title 
                       implements Serializable, Cloneable, PublicCloneable {

    /** For serialization. */
    private static final long serialVersionUID = 8372008692127477443L;
    
    /** The default font. */
    public static final Font DEFAULT_FONT 
        = new Font("SansSerif", Font.BOLD, 12);

    /** The default text color. */
    public static final Paint DEFAULT_TEXT_PAINT = Color.black;

    /** The title text. */
    private String text;

    /** The font used to display the title. */
    private Font font;

    /** The paint used to display the title text. */
    private transient Paint paint;

    /** The content. */
    private TextBlock content;
    
    /**
     * Creates a new title, using default attributes where necessary.
     */
    public TextTitle() {
        this("");
    }

    /**
     * Creates a new title, using default attributes where necessary.
     *
     * @param text  the title text (<code>null</code> not permitted).
     */
    public TextTitle(String text) {
        this(text, TextTitle.DEFAULT_FONT, TextTitle.DEFAULT_TEXT_PAINT,
                Title.DEFAULT_POSITION, Title.DEFAULT_HORIZONTAL_ALIGNMENT,
                Title.DEFAULT_VERTICAL_ALIGNMENT, Title.DEFAULT_PADDING);
    }

    /**
     * Creates a new title, using default attributes where necessary.
     *
     * @param text  the title text (<code>null</code> not permitted).
     * @param font  the title font (<code>null</code> not permitted).
     */
    public TextTitle(String text, Font font) {
        this(text, font, TextTitle.DEFAULT_TEXT_PAINT, Title.DEFAULT_POSITION,
                Title.DEFAULT_HORIZONTAL_ALIGNMENT, 
                Title.DEFAULT_VERTICAL_ALIGNMENT, Title.DEFAULT_PADDING);
    }

    /**
     * Creates a new title.
     *
     * @param text  the text for the title (<code>null</code> not permitted).
     * @param font  the title font (<code>null</code> not permitted).
     * @param paint  the title paint (<code>null</code> not permitted).
     * @param position  the title position (<code>null</code> not permitted).
     * @param horizontalAlignment  the horizontal alignment (<code>null</code> 
     *                             not permitted).
     * @param verticalAlignment  the vertical alignment (<code>null</code> not 
     *                           permitted).
     * @param padding  the space to leave around the outside of the title.
     */
    public TextTitle(String text, Font font, Paint paint, 
                     RectangleEdge position,
                     HorizontalAlignment horizontalAlignment, 
                     VerticalAlignment verticalAlignment, 
                     RectangleInsets padding) {

        super(position, horizontalAlignment, verticalAlignment, padding);
        
        if (text == null) {
            throw new NullPointerException("Null 'text' argument.");
        }
        if (font == null) {
            throw new NullPointerException("Null 'font' argument.");
        }
        if (paint == null) {
            throw new NullPointerException("Null 'paint' argument.");
        }
        this.text = text;
        this.font = font;
        this.paint = paint;
        this.content = null;
        
    }

    /**
     * Returns the title text.
     *
     * @return The text (never <code>null</code>).
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets the title to the specified text and sends a 
     * {@link TitleChangeEvent} to all registered listeners.
     *
     * @param text  the text (<code>null</code> not permitted).
     */
    public void setText(String text) {
        if (text == null) {
            throw new NullPointerException("Null 'text' argument.");
        }
        if (!this.text.equals(text)) {
            this.text = text;
            notifyListeners(new TitleChangeEvent(this));
        }
    }

    /**
     * Returns the font used to display the title string.
     *
     * @return The font (never <code>null</code>).
     */
    public Font getFont() {
        return this.font;
    }

    /**
     * Sets the font used to display the title string.  Registered listeners 
     * are notified that the title has been modified.
     *
     * @param font  the new font (<code>null</code> not permitted).
     */
    public void setFont(Font font) {
        if (font == null) {
            throw new IllegalArgumentException("Null 'font' argument.");
        }
        if (!this.font.equals(font)) {
            this.font = font;
            notifyListeners(new TitleChangeEvent(this));
        }
    }

    /**
     * Returns the paint used to display the title string.
     *
     * @return The paint (never <code>null</code>).
     */
    public Paint getPaint() {
        return this.paint;
    }

    /**
     * Sets the paint used to display the title string.  Registered listeners 
     * are notified that the title has been modified.
     *
     * @param paint  the new paint (<code>null</code> not permitted).
     */
    public void setPaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        if (!this.paint.equals(paint)) {
            this.paint = paint;
            notifyListeners(new TitleChangeEvent(this));
        }
    }

    /**
     * Returns the size of the title content (excludes margin, border and 
     * padding) if there is no constraint.  This is either the natural size
     * of the text, or the block size if this has been specified manually.
     * 
     * @param g2  the graphics device.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeNN(Graphics2D g2, ArrangeParams params) {
        List messages = null;
        boolean logEnabled = params.isLogEnabled();
        if (logEnabled) {
            messages = new java.util.LinkedList();
        }
        double w = getDefaultWidth();
        double h = getDefaultHeight();
        g2.setFont(this.font);
        this.content = TextUtilities.createTextBlock(this.text, this.font, 
                this.paint, Float.MAX_VALUE, new G2TextMeasurer(g2));
        Size2D naturalSize = this.content.calculateDimensions(g2);

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
        
        if (messages != null) {
            if (w < naturalSize.getWidth()) {
                messages.add(new Message(this, 
                        "Prespecified width is too small."));
            }
            if (h < naturalSize.getHeight()) {
                messages.add(new Message(this, 
                        "Prespecified height is too small."));
            }
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result == null) {
            result = new ArrangeResult(w, h, messages);
        }
        else {
            result.setSize(w, h);
            result.setMessages(messages);
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
        
        // TODO: probably should switch off logging before the next call
        // then switch it back to whatever it was
        ArrangeResult r = arrangeNN(g2, params);
        // TODO: evaluate logging requirements
        r.setHeight(fixedHeight);
        return r;
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
        
        float maxWidth = (float) fixedWidth;
        g2.setFont(this.font);
        this.content = TextUtilities.createTextBlock(this.text, this.font, 
                this.paint, maxWidth, new G2TextMeasurer(g2));
        Size2D s = this.content.calculateDimensions(g2);

        // transpose the dimensions if the title is positioned at the left
        // or the right, because the text is rotated in that case
        RectangleEdge position = getPosition();
        if (position == RectangleEdge.LEFT || position == RectangleEdge.RIGHT) {
            s = new Size2D(s.height, s.width);
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(fixedWidth, s.getHeight());
        }
        else {
            result = new ArrangeResult(fixedWidth, s.getHeight(), null);
        }
        return result;
    }
    
    /**
     * Arranges the block with a fixed width and range constraint for the 
     * height, returning the size of the content.
     * 
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed width.
     * @param heightRange  the height range.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeFR(Graphics2D g2, double fixedWidth, 
            Range heightRange, ArrangeParams params) {
        
        float maxWidth = (float) fixedWidth;
        g2.setFont(this.font);
        this.content = TextUtilities.createTextBlock(this.text, this.font, 
                this.paint, maxWidth, new G2TextMeasurer(g2));
        Size2D s = this.content.calculateDimensions(g2);
        
        // transpose the dimensions if the title is positioned at the left
        // or the right, because the text is rotated in that case
        RectangleEdge position = getPosition();
        if (position == RectangleEdge.LEFT || position == RectangleEdge.RIGHT) {
            s = new Size2D(s.height, s.width);
        }

        if (heightRange.contains(s.getHeight())) {
            ArrangeResult result = params.getRecyclableResult();
            if (result != null) {
                result.setSize(s.getWidth(), s.getHeight());
            }
            else {
                result = new ArrangeResult(s.getWidth(), s.getHeight(), null);
            }
            return result;   
        }
        else {
            // TODO: review the logging requirement here
            return arrangeFF(g2, fixedWidth, 
                    heightRange.constrain(s.getHeight()), params);
        }
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
        boolean logEnabled = params.isLogEnabled();
        if (logEnabled) {
            messages = new java.util.LinkedList();
        }
        g2.setFont(this.font);
        this.content = TextUtilities.createTextBlock(this.text, this.font, 
                this.paint, (float) fixedWidth, new G2TextMeasurer(g2));
        Size2D contentSize = this.content.calculateDimensions(g2);
        
        // transpose the dimensions if the title is positioned at the left
        // or the right, because the text is rotated in that case
        RectangleEdge position = getPosition();
        if (position == RectangleEdge.LEFT || position == RectangleEdge.RIGHT) {
            contentSize = new Size2D(contentSize.height, contentSize.width);
        }
        
        if (messages != null) {
            if (fixedHeight < contentSize.getHeight()) {
                messages.add(new Message(this, "Title is too tall."));
            }
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result == null) {
            result = new ArrangeResult(fixedWidth, fixedHeight, messages);
        }
        else {
            result.setSize(fixedWidth, fixedHeight);
            result.setMessages(messages);
        }
        return result;
    }
 
    /**
     * Arranges the block with a range constraint on the width and no height 
     * constraint, returning the size of the content.
     * 
     * @param g2  the graphics device.
     * @param widthRange  the width range.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeRN(Graphics2D g2, Range widthRange, 
            ArrangeParams params) {
        
        float maxWidth = (float) widthRange.getUpperBound();
        g2.setFont(this.font);
        this.content = TextUtilities.createTextBlock(this.text, this.font, 
                this.paint, maxWidth, new G2TextMeasurer(g2));
        Size2D contentSize = this.content.calculateDimensions(g2);

        // transpose the dimensions if the title is positioned at the left
        // or the right, because the text is rotated in that case
        RectangleEdge position = getPosition();
        if (position == RectangleEdge.LEFT || position == RectangleEdge.RIGHT) {
            contentSize = new Size2D(contentSize.height, contentSize.width);
        }
        
        if (widthRange.contains(contentSize.getWidth())) {
            ArrangeResult result = params.getRecyclableResult();
            if (result != null) {
                result.setSize(contentSize.getWidth(), contentSize.getHeight());
            }
            else {
                result = new ArrangeResult(contentSize.getWidth(), 
                        contentSize.getHeight(), null);
            }
            return result;   
        }
        else {
            // TODO: review the logging requirement here
            return arrangeFN(g2, widthRange.constrain(contentSize.getWidth()), 
                    params);    
        }
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
        Shape savedClip = g2.getClip();
        g2.clip(area);
        Paint backgroundPaint = getBackgroundPaint();
        if (backgroundPaint != null) {
            g2.setPaint(backgroundPaint);
            g2.fill(area);
        }
        area = trimMargin(area);
        drawBorder(g2, area);
        if (this.text.equals("")) {
            g2.setClip(savedClip);
            return null;
        }
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
        g2.setFont(this.font);
        g2.setPaint(this.paint);
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
        this.content.draw(g2, x, y, anchor);
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
        g2.setFont(this.font);
        g2.setPaint(this.paint);
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
        this.content.draw(g2, x, y, anchor, x, y, -Math.PI / 2.0);
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
        if (!(obj instanceof TextTitle)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        TextTitle that = (TextTitle) obj;
        if (!ObjectUtilities.equal(this.text, that.text)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.font, that.font)) {
            return false;
        }
        if (!PaintUtilities.equal(this.paint, that.paint)) {
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
        result = 29 * result + (this.font != null ? this.font.hashCode() : 0);
        result = 29 * result + (this.paint != null ? this.paint.hashCode() : 0);
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
    
    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.paint, stream);
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
        this.paint = SerialUtilities.readPaint(stream);
    }

}

