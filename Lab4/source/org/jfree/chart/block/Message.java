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
 * ------------
 * Message.java
 * ------------
 * (C) Copyright 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: Message.java,v 1.2 2005/10/28 10:12:37 mungady Exp $
 *
 * Changes:
 * --------
 * 20-Jul-2005 : Version 1 (DG);
 * 
 */

package org.jfree.chart.block;

/**
 * A warning or error message from the layout process.
 */
public class Message {

    private ContentBlock source;
    
    private String message;
    
    public Message(ContentBlock source, String message) {
        this.source = source;
        this.message = message;
    }
    
    public ContentBlock getSource() {
        return this.source;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public String toString() {
        return "[" + this.source.toString() + ": " + this.message + "]";
    }
    
}
