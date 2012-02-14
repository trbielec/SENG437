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
 * ----------
 * Title.java
 * ----------
 * (C) Copyright 2000-2005, by David Berry and Contributors.
 *
 * Original Author:  David Berry;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *                   Nicolas Brodu;
 *
 * $Id: Title.java,v 1.14 2005/10/28 11:04:01 mungady Exp $
 *
 * Changes (from 21-Aug-2001)
 * --------------------------
 * 21-Aug-2001 : Added standard header (DG);
 * 18-Sep-2001 : Updated header (DG);
 * 14-Nov-2001 : Package com.jrefinery.common.ui.* changed to 
 *               com.jrefinery.ui.* (DG);
 * 07-Feb-2002 : Changed blank space around title from Insets --> Spacer, to 
 *               allow for relative or absolute spacing (DG);
 * 25-Jun-2002 : Removed unnecessary imports (DG);
 * 01-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 14-Oct-2002 : Changed the event listener storage structure (DG);
 * 11-Sep-2003 : Took care of listeners while cloning (NB);
 * 22-Sep-2003 : Spacer cannot be null. Added nullpointer checks for this (TM);
 * 08-Jan-2003 : Renamed AbstractTitle --> Title and moved to separate 
 *               package (DG);
 * 26-Oct-2004 : Refactored to implement Block interface, and removed redundant 
 *               constants (DG);
 * 11-Jan-2005 : Removed deprecated code in preparation for the 1.0.0 
 *               release (DG);
 * 02-Feb-2005 : Changed Spacer --> RectangleInsets for padding (DG);
 * 03-May-2005 : Fixed problem in equals() method (DG);
 * 
 */

package org.jfree.chart.title;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.event.EventListenerList;

import org.jfree.chart.block.AbstractContentBlock;
import org.jfree.chart.block.ArrangeParams;
import org.jfree.chart.block.ArrangeResult;
import org.jfree.chart.block.Block;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.event.TitleChangeEvent;
import org.jfree.chart.event.TitleChangeListener;
import org.jfree.data.Range;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.ObjectUtilities;

/**
 * The base class for all chart titles.  A chart can have multiple titles, 
 * appearing at the top, bottom, left or right of the chart.
 * <P>
 * Concrete implementations of this class will render text and images, and 
 * hence do the actual work of drawing titles.
 *
 * @author David Berry
 */
public abstract class Title extends AbstractContentBlock 
                            implements Block, Cloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -6675162505277817221L;
    
    /** The default title position. */
    public static final RectangleEdge DEFAULT_POSITION = RectangleEdge.TOP;

    /** The default horizontal alignment. */
    public static final HorizontalAlignment 
        DEFAULT_HORIZONTAL_ALIGNMENT = HorizontalAlignment.CENTER;

    /** The default vertical alignment. */
    public static final VerticalAlignment 
        DEFAULT_VERTICAL_ALIGNMENT = VerticalAlignment.CENTER;

    /** Default title padding. */
    public static final RectangleInsets DEFAULT_PADDING = new RectangleInsets(
        1, 1, 1, 1
    );

    /** The title position. */
    private RectangleEdge position;

    /** The horizontal alignment of the title content. */
    private HorizontalAlignment horizontalAlignment;

    /** The vertical alignment of the title content. */
    private VerticalAlignment verticalAlignment;

    /** The tool tip text (can be <code>null</code>). */
    private String toolTipText;
    
    /** The URL text (can be <code>null</code>). */
    private String urlText;
    
    /** Storage for registered change listeners. */
    private transient EventListenerList listenerList;

    /** 
     * A flag that can be used to temporarily disable the listener mechanism. 
     */
    private boolean notify;

    /**
     * Creates a new title, using default attributes where necessary.
     */
    protected Title() {
        this(
            Title.DEFAULT_POSITION,
            Title.DEFAULT_HORIZONTAL_ALIGNMENT,
            Title.DEFAULT_VERTICAL_ALIGNMENT,
            Title.DEFAULT_PADDING
        );
    }

    /**
     * Creates a new title, using default attributes where necessary.
     *
     * @param position  the position of the title (<code>null</code> not 
     *                  permitted).
     * @param horizontalAlignment  the horizontal alignment of the title 
     *                             (<code>null</code> not permitted).
     * @param verticalAlignment  the vertical alignment of the title 
     *                           (<code>null</code> not permitted).
     */
    protected Title(RectangleEdge position, 
                    HorizontalAlignment horizontalAlignment, 
                    VerticalAlignment verticalAlignment) {

        this(
            position, horizontalAlignment, verticalAlignment,
            Title.DEFAULT_PADDING
        );

    }

    /**
     * Creates a new title.
     *
     * @param position  the position of the title (<code>null</code> not 
     *                  permitted).
     * @param horizontalAlignment  the horizontal alignment of the title (LEFT,
     *                             CENTER or RIGHT, <code>null</code> not 
     *                             permitted).
     * @param verticalAlignment  the vertical alignment of the title (TOP, 
     *                           MIDDLE or BOTTOM, <code>null</code> not 
     *                           permitted).
     * @param padding  the amount of space to leave around the outside of the 
     *                 title (<code>null</code> not permitted).
     */
    protected Title(RectangleEdge position,
                    HorizontalAlignment horizontalAlignment, 
                    VerticalAlignment verticalAlignment,
                    RectangleInsets padding) {

        // check arguments...
        if (position == null) {
            throw new IllegalArgumentException("Null 'position' argument.");
        }
        if (horizontalAlignment == null) {
            throw new IllegalArgumentException(
                "Null 'horizontalAlignment' argument."
            );
        }

        if (verticalAlignment == null) {
            throw new IllegalArgumentException(
                "Null 'verticalAlignment' argument."
            );
        }
        if (padding == null) {
            throw new IllegalArgumentException("Null 'spacer' argument.");
        }

        this.position = position;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        setPadding(padding);
        this.toolTipText = null;
        this.urlText = null;
        this.listenerList = new EventListenerList();
        this.notify = true;

    }

    /**
     * Returns the position of the title.
     *
     * @return The title position (never <code>null</code>).
     */
    public RectangleEdge getPosition() {
        return this.position;
    }

    /**
     * Sets the position for the title and sends a {@link TitleChangeEvent} to 
     * all registered listeners.
     *
     * @param position  the position (<code>null</code> not permitted).
     */
    public void setPosition(RectangleEdge position) {
        if (position == null) {
            throw new IllegalArgumentException("Null 'position' argument.");
        }
        if (this.position != position) {
            this.position = position;
            notifyListeners(new TitleChangeEvent(this));
        }
    }

    /**
     * Sets the background paint for the title and sends a 
     * {@link TitleChangeEvent} to all registered listeners.
     * 
     * @param paint  the paint (<code>null</code> permitted).
     */
    public void setBackgroundPaint(Paint paint) {
        super.setBackgroundPaint(paint);
        notifyListeners(new TitleChangeEvent(this));
    }
    
    /**
     * Sets the interior background paint for the title and sends a 
     * {@link TitleChangeEvent} to all registered listeners.
     * 
     * @param paint  the paint (<code>null</code> permitted).
     */
    public void setInteriorBackgroundPaint(Paint paint) {
        super.setInteriorBackgroundPaint(paint);
        notifyListeners(new TitleChangeEvent(this));
    }
    
    /**
     * Returns the horizontal alignment of the title.
     *
     * @return The horizontal alignment (never <code>null</code>).
     */
    public HorizontalAlignment getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    /**
     * Sets the horizontal alignment for the title and sends a 
     * {@link TitleChangeEvent} to all registered listeners.
     *
     * @param alignment  the horizontal alignment (<code>null</code> not 
     *                   permitted).
     */
    public void setHorizontalAlignment(HorizontalAlignment alignment) {
        if (alignment == null) {
            throw new IllegalArgumentException("Null 'alignment' argument.");
        }
        if (this.horizontalAlignment != alignment) {
            this.horizontalAlignment = alignment;
            notifyListeners(new TitleChangeEvent(this));
        }
    }

    /**
     * Returns the vertical alignment of the title.
     *
     * @return The vertical alignment (never <code>null</code>).
     */
    public VerticalAlignment getVerticalAlignment() {
        return this.verticalAlignment;
    }

    /**
     * Sets the vertical alignment for the title, and notifies any registered
     * listeners of the change.
     *
     * @param alignment  the new vertical alignment (TOP, MIDDLE or BOTTOM, 
     *                   <code>null</code> not permitted).
     */
    public void setVerticalAlignment(VerticalAlignment alignment) {
        if (alignment == null) {
            throw new IllegalArgumentException("Null 'alignment' argument.");
        }
        if (this.verticalAlignment != alignment) {
            this.verticalAlignment = alignment;
            notifyListeners(new TitleChangeEvent(this));
        }
    }

    /**
     * Returns the flag that indicates whether or not the notification 
     * mechanism is enabled.
     *
     * @return The flag.
     */
    public boolean getNotify() {
        return this.notify;
    }

    /**
     * Sets the flag that indicates whether or not the notification mechanism
     * is enabled.  There are certain situations (such as cloning) where you
     * want to turn notification off temporarily.
     *
     * @param flag  the new value of the flag.
     */
    public void setNotify(boolean flag) {
        this.notify = flag;
        if (flag) {
            notifyListeners(new TitleChangeEvent(this));   
        }
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
        return this.urlText;
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
     * Arranges the contents of the block, within the given constraints, and 
     * returns the block size.
     * 
     * @param g2  the graphics device.
     * @param constraint  the constraint (<code>null</code> not permitted).
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    public ArrangeResult arrange(Graphics2D g2, RectangleConstraint constraint, 
                                 ArrangeParams params) {
        
        // TODO: review how the result is used/created.
        ArrangeResult result = params.getRecyclableResult();
        RectangleConstraint cc = toContentConstraint(constraint);
        LengthConstraintType w = cc.getWidthConstraintType();
        LengthConstraintType h = cc.getHeightConstraintType();
        if (w == LengthConstraintType.NONE) {
            if (h == LengthConstraintType.NONE) {
                result = arrangeNN(g2, params);  
            }
            else if (h == LengthConstraintType.RANGE) {
                throw new RuntimeException("Not yet implemented."); 
            }
            else if (h == LengthConstraintType.FIXED) {
                result = arrangeNF(g2, cc.getHeight(), params);                 
            }            
        }
        else if (w == LengthConstraintType.RANGE) {
            if (h == LengthConstraintType.NONE) {
                result = arrangeRN(g2, cc.getWidthRange(), params); 
            }
            else if (h == LengthConstraintType.RANGE) {
                result = arrangeRR(g2, cc.getWidthRange(), cc.getHeightRange(), 
                        params); 
            }
            else if (h == LengthConstraintType.FIXED) {
                result = arrangeRF(g2, cc.getWidthRange(), cc.getHeight(), 
                        params);                 
            }
        }
        else if (w == LengthConstraintType.FIXED) {
            if (h == LengthConstraintType.NONE) {
                result = arrangeFN(g2, cc.getWidth(), params); 
            }
            else if (h == LengthConstraintType.RANGE) {
                result = arrangeFR(g2, cc.getWidth(), cc.getHeightRange(), 
                        params); 
            }
            else if (h == LengthConstraintType.FIXED) {
                result = arrangeFF(g2, cc.getWidth(), cc.getHeight(), params);
            }
        }
        
        // here we scale the content size back up to the overall block size...
        result.setSize(calculateTotalWidth(result.getWidth()),
                calculateTotalHeight(result.getHeight()));
        return result;
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
    protected abstract ArrangeResult arrangeNN(Graphics2D g2, 
            ArrangeParams params);

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
    protected abstract ArrangeResult arrangeNF(Graphics2D g2, 
            double fixedHeight, ArrangeParams params);
    
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
        // TODO: review logging requirements
        ArrangeResult r = arrangeNN(g2, params);
        if (widthRange.contains(r.getWidth())) {
            return r;   
        }
        else {
            return arrangeFF(g2, widthRange.constrain(r.getWidth()), 
                    r.getHeight(), params);
        }
    }

    /**
     * Arranges the block with a range constraint on the width and height, 
     * returning the size of the content.
     * 
     * @param g2  the graphics device.
     * @param widthRange  the width range.
     * @param heightRange  the height range.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeRR(Graphics2D g2, Range widthRange, 
            Range heightRange, ArrangeParams params) {
        
        // TODO: evaluate logging requirements
        ArrangeResult r = arrangeNN(g2, params);
        
        if (widthRange.contains(r.getWidth()) 
                && heightRange.contains(r.getHeight())) {
            return r;   
        }
        else {
            return arrangeFF(g2, widthRange.constrain(r.getWidth()), 
                    heightRange.constrain(r.getHeight()), params);
        }
    }
    
    /**
     * Arranges the content of the title with a range constraint for the title
     * width and a fixed height, and returns the size of the title content.
     * 
     * @param g2  the graphics device.
     * @param widthRange  the width range.
     * @param fixedHeight  the fixed height.
     * @param params  the layout parameters (<code>null</code> permitted).
     * 
     * @return The layout result.
     */
    protected ArrangeResult arrangeRF(Graphics2D g2, Range widthRange, 
            double fixedHeight, ArrangeParams params) {
      
        // TODO: review logging requirements
        ArrangeResult r = arrangeNF(g2, fixedHeight, params);
        if (widthRange.contains(r.getWidth())) {
            return r;   
        }
        else {
            return arrangeFF(g2, widthRange.constrain(r.getWidth()), 
                    fixedHeight, params);
        }
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
    protected abstract ArrangeResult arrangeFN(Graphics2D g2, 
            double fixedWidth, ArrangeParams params);
    
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
        
        // TODO: review logging requirements
        ArrangeResult r = arrangeFN(g2, fixedWidth, params);
        if (heightRange.contains(r.getHeight())) {
            return r;   
        }
        else {
            return arrangeFF(g2, fixedWidth, 
                    heightRange.constrain(r.getHeight()), params);
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
    protected abstract ArrangeResult arrangeFF(Graphics2D g2, 
            double fixedWidth, double fixedHeight, ArrangeParams params);

    /**
     * Draws the title on a Java 2D graphics device (such as the screen or a 
     * printer).
     *
     * @param g2  the graphics device.
     * @param area  the area allocated for the title (subclasses should not
     *              draw outside this area).
     */
    public abstract void draw(Graphics2D g2, Rectangle2D area);

    /**
     * Returns a clone of the title.
     * <P>
     * One situation when this is useful is when editing the title properties -
     * you can edit a clone, and then it is easier to cancel the changes if
     * necessary.
     *
     * @return A clone of the title.
     *
     * @throws CloneNotSupportedException not thrown by this class, but it may 
     *         be thrown by subclasses.
     */
    public Object clone() throws CloneNotSupportedException {

        Title duplicate = (Title) super.clone();
        duplicate.listenerList = new EventListenerList();
        // RectangleInsets is immutable => same reference in clone OK
        return duplicate;
    }

    /**
     * Registers an object for notification of changes to the title.
     *
     * @param listener  the object that is being registered.
     */
    public void addChangeListener(TitleChangeListener listener) {
        this.listenerList.add(TitleChangeListener.class, listener);
    }

    /**
     * Unregisters an object for notification of changes to the chart title.
     *
     * @param listener  the object that is being unregistered.
     */
    public void removeChangeListener(TitleChangeListener listener) {
        this.listenerList.remove(TitleChangeListener.class, listener);
    }

    /**
     * Notifies all registered listeners that the chart title has changed in 
     * some way.
     *
     * @param event  an object that contains information about the change to 
     *               the title.
     */
    protected void notifyListeners(TitleChangeEvent event) {
        if (this.notify) {
            Object[] listeners = this.listenerList.getListenerList();
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == TitleChangeListener.class) {
                    ((TitleChangeListener) listeners[i + 1]).titleChanged(
                        event
                    );
                }
            }
        }
    }

    /**
     * Tests an object for equality with this title.
     *
     * @param obj  the object (<code>null</code> not permitted).
     *
     * @return <code>true</code> or <code>false</code>.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Title)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;   
        }
        Title that = (Title) obj;
        if (this.position != that.position) {
            return false;
        }
        if (this.horizontalAlignment != that.horizontalAlignment) {
            return false;
        }
        if (this.verticalAlignment != that.verticalAlignment) {
            return false;
        }
        if (this.notify != that.notify) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hashcode for the title.
     * 
     * @return The hashcode.
     */
    public int hashCode() {
        int result = 193;
        result = 37 * result + ObjectUtilities.hashCode(this.position);    
        result = 37 * result 
            + ObjectUtilities.hashCode(this.horizontalAlignment);    
        result = 37 * result + ObjectUtilities.hashCode(this.verticalAlignment);
        return result;
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
        this.listenerList = new EventListenerList();
    }

}
