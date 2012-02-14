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
 * -------------------
 * BlockContainer.java
 * -------------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: BlockContainer.java,v 1.16 2005/11/16 17:06:54 mungady Exp $
 *
 * Changes:
 * --------
 * 22-Oct-2004 : Version 1 (DG);
 * 02-Feb-2005 : Added isEmpty() method (DG);
 * 04-Feb-2005 : Added equals(), clone() and implemented Serializable (DG);
 * 08-Feb-2005 : Updated for changes in RectangleConstraint (DG);
 * 20-Apr-2005 : Added new draw() method (DG);
 * 
 */

package org.jfree.chart.block;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.data.Range;
import org.jfree.ui.Size2D;
import org.jfree.util.PublicCloneable;

/**
 * A container for a collection of {@link Block} objects.  The container uses 
 * an {@link Arrangement} object to handle the position of each block.
 */
public class BlockContainer extends AbstractContentBlock 
                            implements Block, 
                                       Cloneable, PublicCloneable,
                                       Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 8199508075695195293L;
    
    /** The blocks within the container. */
    private List blocks;
    
    /** The object responsible for laying out the blocks. */
    private Arrangement arrangement;
    
    /**
     * Creates a new instance with default settings.
     */
    public BlockContainer() {
        this(new BorderArrangement());
    }
    
    /**
     * Creates a new instance with the specified arrangement.
     * 
     * @param arrangement  the arrangement manager (<code>null</code> not 
     *                     permitted).
     */
    public BlockContainer(Arrangement arrangement) {
        if (arrangement == null) {
            throw new IllegalArgumentException("Null 'arrangement' argument.");
        }
        this.arrangement = arrangement;
        this.blocks = new ArrayList();
    }    

    /**
     * Returns the arrangement (layout) manager for the container.
     * 
     * @return The arrangement manager (never <code>null</code>).
     */
    public Arrangement getArrangement() {
        return this.arrangement;    
    }
    
    /**
     * Sets the arrangement (layout) manager.
     * 
     * @param arrangement  the arrangement (<code>null</code> not permitted).
     */
    public void setArrangement(Arrangement arrangement) {
        if (arrangement == null) {
            throw new IllegalArgumentException("Null 'arrangement' argument.");
        }
        this.arrangement = arrangement;   
    }
    
    /**
     * Returns <code>true</code> if there are no blocks in the container, and
     * <code>false</code> otherwise.
     * 
     * @return A boolean.
     */
    public boolean isEmpty() {
        return this.blocks.isEmpty();   
    }
    
    /**
     * Returns an unmodifiable list of the {@link Block} objects managed by 
     * this arrangement.
     * 
     * @return A list of blocks.
     */
    public List getBlocks() {
        return Collections.unmodifiableList(this.blocks);
    }
    
    /**
     * Adds a block to the container.
     * 
     * @param block  the block (<code>null</code> permitted).
     */
    public void add(Block block) {
        add(block, null);
    }
    
    /**
     * Adds a block to the container.
     * 
     * @param block  the block (<code>null</code> permitted).
     * @param key  the key (<code>null</code> permitted).
     */
    public void add(Block block, Object key) {
        this.blocks.add(block);
        this.arrangement.add(block, key);
    }
    
    /**
     * Clears all the blocks from the container.
     */
    public void clear() {
        this.blocks.clear();
        this.arrangement.clear();
    }
    
    /**
     * Arranges the contents of the block, within the given constraints, and 
     * returns the block size.
     * 
     * @param g2  the graphics device.
     * @param constraint  the constraint (<code>null</code> not permitted).
     * @param params  optional parameters.
     * 
     * @return The block size (in Java2D units, never <code>null</code>).
     */
    public ArrangeResult arrangeOld(Graphics2D g2, 
            RectangleConstraint constraint, ArrangeParams params) {
        // the incoming constraint is for the overall size of the container
        // but the arrangement needs to constrain the contained blocks within
        // the *content* area only
        RectangleConstraint cc = toContentConstraint(constraint);
        return this.arrangement.arrange(this, g2, cc, params);
    }

    /**
     * Arranges the contents of the block, within the given constraints, and 
     * returns the block size.
     * 
     * @param g2  the graphics device.
     * @param constraint  the constraint (<code>null</code> not permitted).
     * @param params  arrangement parameters (<code>null</code> not permitted).
     * 
     * @return The block size (in Java2D units, never <code>null</code>).
     */
    public ArrangeResult arrange(Graphics2D g2, RectangleConstraint constraint, 
                                 ArrangeParams params) {
        RectangleConstraint cc = toContentConstraint(constraint);
        LengthConstraintType w = cc.getWidthConstraintType();
        LengthConstraintType h = cc.getHeightConstraintType();
        Size2D contentSize = null;
        if (w == LengthConstraintType.NONE) {
            if (h == LengthConstraintType.NONE) {
                contentSize = arrangeNN(g2, params);  
            }
            else if (h == LengthConstraintType.RANGE) {
                throw new RuntimeException("Not yet implemented."); 
            }
            else if (h == LengthConstraintType.FIXED) {
                contentSize = arrangeNF(g2, cc.getHeight(), params);                 
            }            
        }
        else if (w == LengthConstraintType.RANGE) {
            if (h == LengthConstraintType.NONE) {
                throw new RuntimeException("Not yet implemented."); 
            }
            else if (h == LengthConstraintType.RANGE) {
                // TODO: make this something specific
                contentSize = arrangeRR(g2, cc.getWidthRange(), 
                        cc.getHeightRange(), params);  
            }
            else if (h == LengthConstraintType.FIXED) {
                contentSize = arrangeRF(g2, cc.getWidthRange(), cc.getHeight(),
                        params);
            }
        }
        else if (w == LengthConstraintType.FIXED) {
            if (h == LengthConstraintType.NONE) {
                contentSize = arrangeFN(g2, cc.getWidth(), params);  
            }
            else if (h == LengthConstraintType.RANGE) {
                contentSize = arrangeFR(g2, cc.getWidth(), cc.getHeightRange(), 
                        params);  
            }
            else if (h == LengthConstraintType.FIXED) {
                contentSize = arrangeFF(g2, cc.getWidth(), cc.getHeight(), 
                        params);  
            }
        }
        ArrangeResult result = params.getRecyclableResult();
        if (result == null) {
            result = new ArrangeResult();   
        }
        result.setSize(new Size2D(calculateTotalWidth(contentSize.getWidth()),
                calculateTotalHeight(contentSize.getHeight())));
        return result;
    }
    
    /**
     * Returns the size of the title content (excludes margin, border and 
     * padding) if there is no constraint.  This is either the natural size
     * of the text, or the block size if this has been specified manually.
     * 
     * @param g2  the graphics device.
     * 
     * @return The content size.
     */
    protected Size2D arrangeNN(Graphics2D g2, ArrangeParams params) {
        double w = getDefaultWidth();
        double h = getDefaultHeight();
        Size2D naturalSize = this.arrangement.arrange(this, g2, 
                RectangleConstraint.NONE, params).getSize();
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
        return new Size2D(w, h);
    }

    /**
     * Returns the size of the title content (excludes margin, border and 
     * padding) if there is no constraint.  This is either the natural size
     * of the text, or the block size if this has been specified manually.
     * 
     * @param g2  the graphics device.
     * 
     * @return The content size.
     */
    protected Size2D arrangeRR(Graphics2D g2, Range widthRange, 
            Range heightRange, ArrangeParams params) {
        Size2D naturalSize = arrangeNN(g2, params);
        if (!widthRange.contains(naturalSize.getWidth())) {
            return arrangeFR(g2, widthRange.getUpperBound(), heightRange, 
                    params);
        }
        else if (!heightRange.contains(naturalSize.getHeight())) {
            return arrangeRF(g2, widthRange, heightRange.getUpperBound(), 
                    params);
        }
        return naturalSize;
    }

    /**
     * Arranges the container with the given constraints (a fixed width and a 
     * range of heights) and returns the calculated size.
     * 
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed width.
     * @param heightRange  the height range.
     * @param params  the params.
     * 
     * @return The container size.
     */
    protected Size2D arrangeFR(Graphics2D g2, double fixedWidth, 
            Range heightRange, ArrangeParams params) {
        ArrangeResult r = this.arrangement.arrange(this, g2, 
                new RectangleConstraint(fixedWidth, heightRange), params);
        return r.getSize();
    }
    
    protected Size2D arrangeFN(Graphics2D g2, double fixedWidth, 
            ArrangeParams params) {
        return this.arrangement.arrange(this, g2, 
                new RectangleConstraint(fixedWidth, null), params).getSize();   
    }
        
    protected Size2D arrangeNF(Graphics2D g2, double fixedHeight, 
            ArrangeParams params) {
        return this.arrangement.arrange(this, g2, new RectangleConstraint(null,
                fixedHeight), params).getSize();   
    }

    protected Size2D arrangeRF(Graphics2D g2, Range widthRange, 
            double fixedHeight, ArrangeParams params) {
        ArrangeResult r = this.arrangement.arrange(this, g2, 
                new RectangleConstraint(widthRange, fixedHeight), params);
        return r.getSize();
    }

    /**
     * Arranges the block with a fixed width and height.
     * 
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed (content) width.
     * @param fixedHeight  the fixed (content) height.
     * 
     * @return The content size.
     */
    protected Size2D arrangeFF(Graphics2D g2, double fixedWidth, 
                               double fixedHeight, ArrangeParams params) {
        this.arrangement.arrange(this, g2, new RectangleConstraint(fixedWidth, 
                fixedHeight), params);
        return new Size2D(fixedWidth, fixedHeight);   
    }

    /**
     * Draws the container and all the blocks within it.
     * 
     * @param g2  the graphics device.
     * @param area  the area.
     */
    public void draw(Graphics2D g2, Rectangle2D area) {
        arrange(g2, new RectangleConstraint(area.getWidth(), area.getHeight()), 
                new ArrangeParams());
        draw(g2, area, null);
    }
    
    /**
     * Draws the block within the specified area.
     * 
     * @param g2  the graphics device.
     * @param area  the area.
     * @param params  passed on to blocks within the container 
     *                (<code>null</code> permitted).
     * 
     * @return An instance of {@link EntityBlockResult}, or <code>null</code>.
     */
    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        // check if we need to collect chart entities from the container
        EntityBlockParams ebp = null;
        StandardEntityCollection sec = null;
        if (params instanceof EntityBlockParams) {
            ebp = (EntityBlockParams) params;
            if (ebp.getGenerateEntities()) {
                sec = new StandardEntityCollection();   
            }
        }
        Rectangle2D contentArea = (Rectangle2D) area.clone();
        contentArea = trimMargin(contentArea);
        drawBorder(g2, contentArea);
        contentArea = trimBorder(contentArea);
        contentArea = trimPadding(contentArea);
        AffineTransform saved = g2.getTransform();
        g2.translate(contentArea.getX(), contentArea.getY());
        Iterator iterator = this.blocks.iterator();
        while (iterator.hasNext()) {
            Block block = (Block) iterator.next();
            Object r = block.draw(g2, block.getBounds(), params);
            if (sec != null) {
                if (r instanceof EntityBlockResult) {
                    EntityBlockResult ebr = (EntityBlockResult) r;
                    EntityCollection ec = ebr.getEntityCollection();
                    sec.addAll(ec);
                }
            }
        }
        g2.setTransform(saved);
        BlockResult result = null;
        if (sec != null) {
            result = new BlockResult();
            result.setEntityCollection(sec);
        }
        return result;
    }

    /**
     * Tests this container for equality with an arbitrary object.
     * 
     * @param obj  the object (<code>null</code> permitted).
     * 
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;   
        }
        if (!(obj instanceof BlockContainer)) {
            return false;   
        }
        if (!super.equals(obj)) {
            return false;   
        }
        BlockContainer that = (BlockContainer) obj;
        if (!this.arrangement.equals(that.arrangement)) {
            return false;   
        }
        if (!this.blocks.equals(that.blocks)) {
            return false;   
        }
        return true;
    }
    
    /**
     * Returns a clone of the container.
     * 
     * @return A clone.
     * 
     * @throws CloneNotSupportedException if there is a problem cloning.
     */
    public Object clone() throws CloneNotSupportedException {
        Object clone = (BlockContainer) super.clone();
        // TODO : complete this
        return clone;
    }
    
}
