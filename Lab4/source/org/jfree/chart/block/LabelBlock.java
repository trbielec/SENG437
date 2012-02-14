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
 * ---------------
 * LabelBlock.java
 * ---------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: LabelBlock.java,v 1.12 2005/10/28 12:59:56 mungady Exp $
 *
 * Changes:
 * --------
 * 22-Oct-2004 : Version 1 (DG);
 * 19-Apr-2005 : Added optional tooltip and URL text items,
 *               draw() method now returns entities if 
 *               requested (DG);
 * 13-May-2005 : Added methods to set the font (DG);
 * 
 */

package org.jfree.chart.block;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.data.Range;
import org.jfree.text.TextBlock;
import org.jfree.text.TextBlockAnchor;
import org.jfree.text.TextUtilities;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.Size2D;

/**
 * A block containing a label.
 */
public class LabelBlock extends AbstractContentBlock {
    
    /** 
     * The text for the label - retained in case the label needs 
     * regenerating (for example, to change the font). 
     */
    private String text;
    
    /** The label. */
    private TextBlock label;
    
    /** The font. */
    private Font font;
    
    /** The tool tip text (can be <code>null</code>). */
    private String toolTipText;
    
    /** The URL text (can be <code>null</code>). */
    private String urlText;
    
    /**
     * Creates a new label block.
     * 
     * @param label  the label (<code>null</code> not permitted).
     */
    public LabelBlock(String label) {
        this(label, new Font("SansSerif", Font.PLAIN, 10));
    }
    
    /**
     * Creates a new label block.
     * 
     * @param text  the text for the label (<code>null</code> not permitted).
     * @param font  the font (<code>null</code> not permitted).
     */
    public LabelBlock(String text, Font font) {        
        this.text = text;
        this.label = TextUtilities.createTextBlock(text, font, Color.black);
        this.label.setLineAlignment(HorizontalAlignment.LEFT);
        this.font = font;
        this.toolTipText = null;
        this.urlText = null;
    }
    
    /**
     * Returns the font.
     *
     * @return The font.
     */
    public Font getFont() {
        return this.font;    
    }
    
    /**
     * Sets the font and regenerates the label.
     *
     * @param font  the font.
     */
    public void setFont(Font font) {
        this.font = font;
        this.label = TextUtilities.createTextBlock(this.text, font, 
                Color.black);
        this.label.setLineAlignment(HorizontalAlignment.LEFT);
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
     * Sets the tool tip text.
     * 
     * @param text  the text (<code>null</code> permitted).
     */
    public void setToolTipText(String text) {
        this.toolTipText = text;   
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
     * Sets the URL text.
     * 
     * @param text  the text (<code>null</code> permitted).
     */
    public void setURLText(String text) {
        this.urlText = text;   
    }
    
    /**
     * Arranges the contents of the block, to fit within the given constraints.
     * 
     * @param g2  the graphics device.
     * @param constraint  the constraint (<code>null</code> not permitted).
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The result of the arrangement, including the block size.
     */
    public ArrangeResult arrange(Graphics2D g2, RectangleConstraint constraint,
                                 ArrangeParams params) {        
        ArrangeResult result;
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
            else {
                throw new RuntimeException("Unrecognised constraint.");
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
            else {
                throw new RuntimeException("Unrecognised constraint.");
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
            else {
                throw new RuntimeException("Unrecognised constraint.");
            }
        }
        else {
            throw new RuntimeException("Unrecognised constraints.");
        }
        
        // here we scale the content size back up to the overall block size...
        result.setSize(calculateTotalWidth(result.getWidth()),
                calculateTotalHeight(result.getHeight()));
        return result;

    }
    
    /**
     * Arranges the content to fit the given constraints.
     * 
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed width.
     * @param fixedHeight  the fixed height.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result (including *content* size).
     */
    protected ArrangeResult arrangeFF(Graphics2D g2, double fixedWidth, 
            double fixedHeight, ArrangeParams params) {
        
        List messages = null;
        if (params.isLogEnabled()) {
            messages = new java.util.LinkedList();
        }
        
        g2.setFont(this.font);
        Size2D s = this.label.calculateDimensions(g2);
        
        if (messages != null) {
            if (s.width > fixedWidth) {
                messages.add(new Message(this, "Text is too wide to fit."));
            }
            if (s.height > fixedHeight) {
                messages.add(new Message(this, "Text is too tall to fit."));                
            }
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(fixedWidth, fixedHeight);
            result.setMessages(messages);
        }
        else {
            result = new ArrangeResult(fixedWidth, fixedHeight, messages);
        }
        return result;
    }
    
    /**
     * Arranges the content to fit the given constraints.
     * 
     * @param g2  the graphics device.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result (including *content* size).
     */
    protected ArrangeResult arrangeFR(Graphics2D g2, double fixedWidth, 
            Range heightRange, ArrangeParams params) {
        ArrangeResult result = arrangeFN(g2, fixedWidth, params);
        if (heightRange.contains(result.getHeight())) {
            return result;
        }
        else {
            return arrangeFF(g2, fixedWidth, 
                    heightRange.constrain(result.getHeight()), params);
        }
    }

    /**
     * Arranges the content to fit the given constraints.
     * 
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed width.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result (including *content* size).
     */
    protected ArrangeResult arrangeFN(Graphics2D g2, double fixedWidth, 
            ArrangeParams params) {
        List messages = null;
        if (params.isLogEnabled()) {
            messages = new java.util.LinkedList();
        }
        
        g2.setFont(this.font);
        Size2D s = this.label.calculateDimensions(g2);
        
        if (messages != null) {
            if (s.width > fixedWidth) {
                messages.add(new Message(this, "Text is too wide to fit."));
            }
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(fixedWidth, s.getHeight());
            result.setMessages(messages);
        }
        else {
            result = new ArrangeResult(fixedWidth, s.getHeight(), messages);
        }
        return result;
    }

    /**
     * Arranges the content to fit the given constraints.
     * 
     * @param g2  the graphics device.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result (including *content* size).
     */
    protected ArrangeResult arrangeRF(Graphics2D g2, Range widthRange,
            double fixedHeight, ArrangeParams params) {
        ArrangeResult result = arrangeNF(g2, fixedHeight, params);
        if (widthRange.contains(result.getWidth())) {
            return result;
        }
        else {
            return arrangeFF(g2, widthRange.constrain(result.getWidth()), 
                    fixedHeight, params);
        }
    }

    /**
     * Arranges the content to fit the given constraints.
     * 
     * @param g2  the graphics device.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result (including *content* size).
     */
    protected ArrangeResult arrangeRR(Graphics2D g2, Range widthRange, 
            Range heightRange, ArrangeParams params) {
        ArrangeResult result = arrangeNN(g2, params);
        if (widthRange.contains(result.getWidth()) 
                && heightRange.contains(result.getHeight())) {
            return result;
        }
        else {
            return arrangeFF(g2, widthRange.constrain(result.getWidth()), 
                    heightRange.constrain(result.getHeight()), params);
        }
    }

    /**
     * Arranges the content to fit the given constraints.
     * 
     * @param g2  the graphics device.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result (including *content* size).
     */
    protected ArrangeResult arrangeRN(Graphics2D g2, Range widthRange, 
            ArrangeParams params) {
        ArrangeResult result = arrangeNN(g2, params);
        if (widthRange.contains(result.getWidth())) {
            return result;
        }
        else {
            return arrangeFF(g2, widthRange.constrain(result.getWidth()), 
                    result.getHeight(), params);
        }
    }

    /**
     * Arranges the content to fit the given constraints.
     * 
     * @param g2  the graphics device.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result (including *content* size).
     */
    protected ArrangeResult arrangeNF(Graphics2D g2, double fixedHeight, 
            ArrangeParams params) {
        List messages = null;
        if (params.isLogEnabled()) {
            messages = new java.util.LinkedList();
        }
        
        g2.setFont(this.font);
        Size2D s = this.label.calculateDimensions(g2);
        
        if (messages != null) {
            if (s.height > fixedHeight) {
                messages.add(new Message(this, "Text is too tall to fit."));
            }
        }
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(s.getWidth(), fixedHeight);
            result.setMessages(messages);
        }
        else {
            result = new ArrangeResult(s.getWidth(), fixedHeight, messages);
        }
        return result;
    }

    /**
     * Arranges the content to fit the given constraints.
     * 
     * @param g2  the graphics device.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result (including *content* size).
     */
    protected ArrangeResult arrangeNR(Graphics2D g2, 
            Range heightRange, ArrangeParams params) {
        ArrangeResult result = arrangeNN(g2, params);
        if (heightRange.contains(result.getHeight())) {
            return result;
        }
        else {
            return arrangeFF(g2, result.getWidth(), 
                    heightRange.constrain(result.getHeight()), params);
        }
    }

    /**
     * Arranges the content to fit the given constraints.
     * 
     * @param g2  the graphics device.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The layout result (including *content* size).
     */
    protected ArrangeResult arrangeNN(Graphics2D g2, ArrangeParams params) {
        g2.setFont(this.font);
        Size2D s = this.label.calculateDimensions(g2);
        
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(s.getWidth(), s.getHeight());
        }
        else {
            result = new ArrangeResult(s.getWidth(), s.getHeight(), null);
        }
        return result;
    }

    /**
     * Draws the block.
     * 
     * @param g2  the graphics device.
     * @param area  the area.
     */
    public void draw(Graphics2D g2, Rectangle2D area) {
        draw(g2, area, null);
    }
    
    /**
     * Draws the block within the specified area.
     * 
     * @param g2  the graphics device.
     * @param area  the area.
     * @param params  ignored (<code>null</code> permitted).
     * 
     * @return Always <code>null</code>.
     */
    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        area = (Rectangle2D) area.clone();
        area = trimMargin(area);
        drawBorder(g2, area);
        area = trimBorder(area);
        area = trimPadding(area);
        
        // check if we need to collect chart entities from the container
        EntityBlockParams ebp = null;
        StandardEntityCollection sec = null;
        Shape entityArea = null;
        if (params instanceof EntityBlockParams) {
            ebp = (EntityBlockParams) params;
            if (ebp.getGenerateEntities()) {
                sec = new StandardEntityCollection();
                // TODO:  this transformation doesn't work always.  Fix!
                entityArea = g2.getTransform().createTransformedShape(area);
            }
        }
        g2.setPaint(Color.black);
        g2.setFont(this.font);
        this.label.draw(
            g2, (float) area.getX(), (float) area.getY(), 
            TextBlockAnchor.TOP_LEFT
        );
        BlockResult result = null;
        if (ebp != null && sec != null) {
            if (this.toolTipText != null || this.urlText != null) {
                ChartEntity entity = new ChartEntity(
                    entityArea, this.toolTipText, this.urlText
                );   
                sec.add(entity);
                result = new BlockResult();
                result.setEntityCollection(sec);
            }
        }
        return result;
    }

}
