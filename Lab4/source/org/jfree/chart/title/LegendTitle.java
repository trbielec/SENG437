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
 * ----------------
 * LegendTitle.java
 * ----------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: LegendTitle.java,v 1.25 2005/10/28 11:04:01 mungady Exp $
 *
 * Changes
 * -------
 * 25-Nov-2004 : First working version (DG);
 * 11-Jan-2005 : Removed deprecated code in preparation for 1.0.0 release (DG);
 * 08-Feb-2005 : Updated for changes in RectangleConstraint class (DG);
 * 11-Feb-2005 : Implemented PublicCloneable (DG);
 * 23-Feb-2005 : Replaced chart reference with LegendItemSource (DG);
 * 16-Mar-2005 : Added itemFont attribute (DG);
 * 17-Mar-2005 : Fixed missing fillShape setting (DG);
 * 20-Apr-2005 : Added new draw() method (DG);
 * 03-May-2005 : Modified equals() method to ignore sources (DG);
 * 13-May-2005 : Added settings for legend item label and graphic padding (DG);
 * 09-Jun-2005 : Fixed serialization bug (DG);
 * 
 */

package org.jfree.chart.title;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.block.ArrangeParams;
import org.jfree.chart.block.ArrangeResult;
import org.jfree.chart.block.Arrangement;
import org.jfree.chart.block.AttributedLabelBlock;
import org.jfree.chart.block.Block;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.block.CenterArrangement;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.block.ContentBlock;
import org.jfree.chart.block.FlowArrangement;
import org.jfree.chart.block.LabelBlock;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.event.TitleChangeEvent;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.PublicCloneable;

/**
 * A chart title that displays a legend for the data in the chart.
 * <P>
 * The title can be populated with legend items manually, or you can assign a
 * reference to the plot, in which case the legend items will be automatically
 * created to match the dataset(s).
 */
public class LegendTitle extends Title 
                         implements Cloneable, PublicCloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 2644010518533854633L;
    
    /** The default item font. */
    public static final Font DEFAULT_ITEM_FONT 
        = new Font("SansSerif", Font.PLAIN, 12);

    /** The sources for legend items. */
    private LegendItemSource[] sources;
    
    /** The edge for the legend item graphic relative to the text. */
    private RectangleEdge legendItemGraphicEdge;
    
    /** The anchor point for the legend item graphic. */
    private RectangleAnchor legendItemGraphicAnchor;
    
    /** The legend item graphic location. */
    private RectangleAnchor legendItemGraphicLocation;
    
    /** The padding to apply to all legend item graphics. */
    private RectangleInsets legendItemGraphicPadding;
    
    /** The padding to apply to all legend item text. */
    private RectangleInsets legendItemTextPadding;
    
    /** The item font. */
    private Font itemFont;
    
    /**
     * A container that holds and displays the legend items.
     */
    private BlockContainer items;
    
    private Arrangement hLayout;
    
    private Arrangement vLayout;
    
    /** 
     * An optional container for wrapping the legend items (allows for adding
     * a title or other text to the legend). 
     */
    private BlockContainer wrapper;

    /**
     * Constructs a new (empty) legend for the specified source.
     * 
     * @param source  the source.
     */
    public LegendTitle(LegendItemSource source) {
        this(source, new FlowArrangement(), new ColumnArrangement());
    }
    
    /**
     * Creates a new legend title with the specified arrangement.
     * 
     * @param source  the source.
     * @param hLayout  the horizontal item arrangement (<code>null</code> not
     *                 permitted).
     * @param vLayout  the vertical item arrangement (<code>null</code> not
     *                 permitted).
     */
    public LegendTitle(LegendItemSource source, 
                       Arrangement hLayout, Arrangement vLayout) {
        this.sources = new LegendItemSource[] {source};
        this.items = new BlockContainer(hLayout);
        this.hLayout = hLayout;
        this.vLayout = vLayout; 
        this.legendItemGraphicEdge = RectangleEdge.LEFT;
        this.legendItemGraphicAnchor = RectangleAnchor.CENTER;
        this.legendItemGraphicLocation = RectangleAnchor.CENTER;
        this.legendItemGraphicPadding = new RectangleInsets(2.0, 2.0, 2.0, 2.0);
        this.legendItemTextPadding = new RectangleInsets(2.0, 2.0, 2.0, 2.0);
        this.itemFont = DEFAULT_ITEM_FONT;
    }
    
    /**
     * Returns the legend item sources.
     * 
     * @return The sources.
     */
    public LegendItemSource[] getSources() {
        return this.sources;   
    }
    
    /**
     * Sets the legend item sources and sends a {@link TitleChangeEvent} to
     * all registered listeners.
     * 
     * @param sources  the sources (<code>null</code> not permitted).
     */
    public void setSources(LegendItemSource[] sources) {
        if (sources == null) {
            throw new IllegalArgumentException("Null 'sources' argument.");   
        }
        this.sources = sources;
        notifyListeners(new TitleChangeEvent(this));
    }

    /**
     * Returns the item font.
     * 
     * @return The font (never <code>null</code>).
     */
    public Font getItemFont() {
        return this.itemFont;   
    }
    
    /**
     * Sets the item font and sends a {@link TitleChangeEvent} to
     * all registered listeners.
     * 
     * @param font  the font (<code>null</code> not permitted).
     */
    public void setItemFont(Font font) {
        if (font == null) {
            throw new IllegalArgumentException("Null 'font' argument.");   
        }
        this.itemFont = font;
        notifyListeners(new TitleChangeEvent(this));
    }
    
    /**
     * Returns the location of the shape within each legend item. 
     * 
     * @return The location (never <code>null</code>).
     */
    public RectangleEdge getLegendItemGraphicEdge() {
        return this.legendItemGraphicEdge;
    }
    
    /**
     * Sets the location of the shape within each legend item.
     * 
     * @param edge  the edge (<code>null</code> not permitted).
     */
    public void setLegendItemGraphicEdge(RectangleEdge edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Null 'edge' argument.");
        }
        this.legendItemGraphicEdge = edge;
    }
    
    /**
     * Returns the legend item graphic anchor.
     * 
     * @return The graphic anchor (never <code>null</code>).
     */
    public RectangleAnchor getLegendItemGraphicAnchor() {
        return this.legendItemGraphicAnchor;
    }
    
    /**
     * Sets the anchor point used for the graphic in each legend item.
     * 
     * @param anchor  the anchor point (<code>null</code> not permitted).
     */
    public void setLegendItemGraphicAnchor(RectangleAnchor anchor) {
        if (anchor == null) {
            throw new IllegalArgumentException("Null 'anchor' point.");
        }
        this.legendItemGraphicAnchor = anchor;
    }
    
    /**
     * Returns the legend item graphic location.
     * 
     * @return The location (never <code>null</code>).
     */
    public RectangleAnchor getLegendItemGraphicLocation() {
        return this.legendItemGraphicLocation;
    }
    
    /**
     * Sets the legend item graphic location.
     * 
     * @param anchor  the anchor (<code>null</code> not permitted).
     */
    public void setLegendItemGraphicLocation(RectangleAnchor anchor) {
        this.legendItemGraphicLocation = anchor;
    }
    
    /**
     * Returns the padding that will be applied to each item graphic.
     * 
     * @return The padding (never <code>null</code>).
     */
    public RectangleInsets getLegendItemGraphicPadding() {
        return this.legendItemGraphicPadding;    
    }
    
    /**
     * Sets the padding that will be applied to each item graphic in the 
     * legend and sends a {@link TitleChangeEvent} to all registered listeners.
     * 
     * @param padding  the padding (<code>null</code> not permitted).
     */
    public void setLegendItemGraphicPadding(RectangleInsets padding) {
        if (padding == null) {
            throw new IllegalArgumentException("Null 'padding' argument.");   
        }
        this.legendItemGraphicPadding = padding;
        notifyListeners(new TitleChangeEvent(this));
    }
    
    /**
     * Returns the padding that will be applied to the text for each item.
     * 
     * @return The padding (never <code>null</code>).
     */
    public RectangleInsets getLegendItemTextPadding() {
        return this.legendItemTextPadding;    
    }
    
    /**
     * Sets the padding that will be applied to the text for each item in the 
     * legend and sends a {@link TitleChangeEvent} to all registered listeners.
     * 
     * @param padding  the padding (<code>null</code> not permitted).
     */
    public void setLegendItemTextPadding(RectangleInsets padding) {
        this.legendItemTextPadding = padding;
        notifyListeners(new TitleChangeEvent(this));
    }
    
    /**
     * Fetches the latest legend items.
     */
    protected void fetchLegendItems() {
        this.items.clear();
        RectangleEdge p = getPosition();
        if (RectangleEdge.isTopOrBottom(p)) {
            this.items.setArrangement(this.hLayout);   
        }
        else {
            this.items.setArrangement(this.vLayout);   
        }
        for (int s = 0; s < this.sources.length; s++) {
            LegendItemCollection legendItems = this.sources[s].getLegendItems();
            if (legendItems != null) {
                for (int i = 0; i < legendItems.getItemCount(); i++) {
                    LegendItem item = legendItems.get(i);
                    Block block = createLegendItemBlock(item);
                    this.items.add(block);
                }
            }
        }
    }
    
    /**
     * Creates a legend item block.
     * 
     * @param item  the legend item.
     * 
     * @return The block.
     */
    protected Block createLegendItemBlock(LegendItem item) {
        BlockContainer result = null;
        LegendGraphic lg = new LegendGraphic(
            item.getShape(), item.getFillPaint()
        );
        lg.setShapeFilled(item.isShapeFilled());
        lg.setLine(item.getLine());
        lg.setLineStroke(item.getLineStroke());
        lg.setLinePaint(item.getLinePaint());
        lg.setLineVisible(item.isLineVisible());
        lg.setShapeVisible(item.isShapeVisible());
        lg.setShapeOutlineVisible(item.isShapeOutlineVisible());
        lg.setOutlinePaint(item.getOutlinePaint());
        lg.setOutlineStroke(item.getOutlineStroke());
        lg.setPadding(this.legendItemGraphicPadding);
        
        BlockContainer legendItem = new BlockContainer(new BorderArrangement());
        lg.setShapeAnchor(this.legendItemGraphicAnchor);
        lg.setShapeLocation(getLegendItemGraphicLocation());
        legendItem.add(lg, this.legendItemGraphicEdge);
        ContentBlock labelBlock = null;
        if (item.getAttributedLabel() != null) {
            labelBlock = new AttributedLabelBlock(item.getAttributedLabel(), 
                    this.itemFont);
        }
        else {
            labelBlock = new LabelBlock(item.getLabel(), this.itemFont);
        }
        labelBlock.setPadding(this.legendItemTextPadding);
        legendItem.add(labelBlock);
        
        result = new BlockContainer(new CenterArrangement());
        result.add(legendItem);
        
        return result;
    }
    
    /**
     * Returns the container that holds the legend items.
     * 
     * @return The container for the legend items.
     */
    public BlockContainer getItemContainer() {
        return this.items;
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
        
        fetchLegendItems();
        return super.arrange(g2, constraint, params);
        
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
        BlockContainer container = this.wrapper;
        if (container == null) {
            container = this.items;
        }
        return container.arrange(g2, new RectangleConstraint(fixedWidth, 
                fixedHeight), params);
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
        
        double h = getDefaultHeight();
        BlockContainer container = this.wrapper;
        if (container == null) {
            container = this.items;
        }
        ArrangeResult naturalSize = container.arrange(g2, 
                new RectangleConstraint(fixedWidth, null), params);
        if (h < 0.0) {
            h = naturalSize.getHeight();   
        }
        else {
            h = trimToContentHeight(h);
        }
        // TODO: evaluate logging
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
        
        double w = getDefaultWidth();
        BlockContainer container = this.wrapper;
        if (container == null) {
            container = this.items;
        }
        ArrangeResult naturalSize = container.arrange(g2, 
                new RectangleConstraint(null, fixedHeight), params);
        if (w < 0.0) {
            w = naturalSize.getWidth();   
        }
        else {
            w = trimToContentWidth(w);
        }
        // TODO: evaluate logging
        ArrangeResult result = params.getRecyclableResult();
        if (result != null) {
            result.setSize(w, fixedHeight);
        }
        else {
            result = new ArrangeResult(w, fixedHeight, null);
        }
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
    protected ArrangeResult arrangeNN(Graphics2D g2, ArrangeParams params) {
        double w = getDefaultWidth();
        double h = getDefaultHeight();
        BlockContainer container = this.wrapper;
        if (container == null) {
            container = this.items;
        }
        ArrangeResult naturalSize = container.arrange(g2, 
                RectangleConstraint.NONE, params);
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
        // TODO:  evaluate logging
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
     * Draws the title on a Java 2D graphics device (such as the screen or a
     * printer).
     *
     * @param g2  the graphics device.
     * @param area  the available area for the title.
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
     * @return An {@link org.jfree.chart.block.EntityBlockResult} or 
     *         <code>null</code>.
     */
    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        area = (Rectangle2D) area.clone();
        Paint backgroundPaint = getBackgroundPaint();
        if (backgroundPaint != null) {
            g2.setPaint(backgroundPaint);
            g2.fill(area);
        }
        Rectangle2D target = (Rectangle2D) area.clone();
        target = trimMargin(target);
        getBorder().draw(g2, target);
        getBorder().getInsets().trim(target);
        Paint interiorBackgroundPaint = getInteriorBackgroundPaint();
        if (interiorBackgroundPaint != null) {
            g2.setPaint(interiorBackgroundPaint);
            g2.fill(target);
        }
        BlockContainer container = this.wrapper;
        if (container == null) {
            container = this.items; 
        }
        target = trimPadding(target);
        return container.draw(g2, target, params);   
    }

    /**
     * Sets the wrapper container for the legend.
     * 
     * @param wrapper  the wrapper container.
     */
    public void setWrapper(BlockContainer wrapper) {
        this.wrapper = wrapper;
    }
    
    /**
     * Tests this title for equality with an arbitrary object.
     * 
     * @param obj  the object (<code>null</code> permitted).
     * 
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;   
        }
        if (!(obj instanceof LegendTitle)) {
            return false;   
        }
        if (!super.equals(obj)) {
            return false;   
        }
        LegendTitle that = (LegendTitle) obj;
        if (this.legendItemGraphicEdge != that.legendItemGraphicEdge) {
            return false;   
        }
        if (this.legendItemGraphicAnchor != that.legendItemGraphicAnchor) {
            return false;   
        }
        if (this.legendItemGraphicLocation != that.legendItemGraphicLocation) {
            return false;   
        }
        if (!this.itemFont.equals(that.itemFont)) {
            return false;   
        }
        if (!this.hLayout.equals(that.hLayout)) {
            return false;   
        }
        if (!this.vLayout.equals(that.vLayout)) {
            return false;   
        }
        return true;
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
    }

}