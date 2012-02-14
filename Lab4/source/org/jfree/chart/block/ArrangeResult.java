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
 * ------------------
 * ArrangeResult.java
 * ------------------
 * (C) Copyright 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: ArrangeResult.java,v 1.3 2005/07/19 14:27:01 mungady Exp $
 *
 * Changes:
 * --------
 * 17-Jun-2005 : Version 1 (DG);
 * 12-Jun-2005 : Added a list for messages (DG);
 * 
 */

package org.jfree.chart.block;

import java.util.List;

import org.jfree.ui.Size2D;

/**
 * Represents the result of a call to one of the arrange() methods in the
 * {@link Block} class.
 */
public class ArrangeResult {

    /** A list of warnings/errors from the layout process. */
    private List messages; 
    
    /** The size of the block. */
    private Size2D size;
    
    /**
     * Creates a new default instance.
     */
    public ArrangeResult() {
        this(null, null);    
    }
    
    public ArrangeResult(double width, double height, List messages) {
        this.size = new Size2D(width, height);
        this.messages = messages;
    }
    
    /**
     * Creates a new result.
     * 
     * @param size  the size.
     */
    public ArrangeResult(Size2D size, List messages) {
        this.size = size;
        this.messages = messages;
    }

    public double getWidth() {
        return this.size.getWidth();
    }
    
    public void setWidth(double width) {
        this.size = new Size2D(width, this.size.getHeight());
    }
    
    public double getHeight() {
        return this.size.getHeight();
    }
    
    public void setHeight(double height) {
        this.size = new Size2D(this.size.getWidth(), height);
    }
    
    /**
     * Returns the block size.
     * 
     * @return The block size.
     */
    public Size2D getSize() {
        return this.size;   
    }
    
    /**
     * Sets the block size.
     * 
     * @param size  the size.
     */
    public void setSize(Size2D size) {
        this.size = size;   
    }
    
    public void setSize(double width, double height) {
        this.size = new Size2D(width, height);
    }
    
    /**
     * Returns the message list.  This contains errors and/or warnings from
     * the layout process.
     * 
     * @return The message list (possibly <code>null</code>).
     */
    public List getMessages() {
        return this.messages;
    }
    
    /**
     * Sets the message list.
     * 
     * @param messages  the message list (<code>null</code> permitted)..
     */
    public void setMessages(List messages) {
        this.messages = messages;
    }
    
    public String toString() {
        return "ArrangeResult[w=" + this.getWidth() + ",h=" + this.getHeight() 
            + "]";
    }
}