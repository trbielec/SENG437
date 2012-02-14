package org.jfree.chart.block;

import org.jfree.ui.RectangleInsets;

/**
 * A block that has content surrounded by padding, a border and a margin.
 */
public interface ContentBlock extends Block {

    /**
     * Returns the default width of the block, if this is known in advance.
     * The actual width of the block may be overridden if layout constraints
     * make this necessary.  
     * 
     * @return The default width.
     */
    public double getDefaultWidth();
    
    /**
     * Sets the default width of the block, if this is known in advance.
     * 
     * @param width  the width (in Java2D units)
     */
    public void setDefaultWidth(double width);
    
    /**
     * Returns the default height of the block, if this is known in advance.
     * The actual height of the block may be overridden if layout constraints
     * make this necessary.  
     * 
     * @return The default height.
     */
    public double getDefaultHeight();
    
    /**
     * Sets the default width of the block, if this is known in advance.
     * 
     * @param height  the height (in Java2D units)
     */
    public void setDefaultHeight(double height);

    /**
     * Returns the margin.
     * 
     * @return The margin (never <code>null</code>).
     */
    public RectangleInsets getMargin();

    /**
     * Sets the margin (use {@link RectangleInsets#ZERO_INSETS} for no 
     * padding).
     * 
     * @param margin  the margin (<code>null</code> not permitted).
     */
    public void setMargin(RectangleInsets margin);

    /**
     * Sets the margin using the specified values in Java2D units.
     * 
     * @param top  the top margin.
     * @param left  the left margin.
     * @param bottom  the bottom margin.
     * @param right  the right margin.
     */
    public void setMargin(double top, double left, double bottom, double right);

    /**
     * Returns the border.
     * 
     * @return The border (never <code>null</code>).
     */
    public BlockBorder getBorder();
    
    /**
     * Sets the border for the block (use {@link BlockBorder#NONE} for
     * no border).
     * 
     * @param border  the border (<code>null</code> not permitted).
     */
    public void setBorder(BlockBorder border);
    
    /**
     * Sets a black border with the specified line widths.
     * 
     * @param top  the top border line width.
     * @param left  the left border line width.
     * @param bottom  the bottom border line width.
     * @param right  the right border line width.
     */
    public void setBorder(double top, double left, double bottom, double right);
    
    /**
     * Returns the padding.
     * 
     * @return The padding (never <code>null</code>).
     */
    public RectangleInsets getPadding();
    
    /**
     * Sets the padding (use {@link RectangleInsets#ZERO_INSETS} for no 
     * padding).
     * 
     * @param padding  the padding (<code>null</code> not permitted).
     */
    public void setPadding(RectangleInsets padding);

    /**
     * Sets the padding.
     * 
     * @param top  the top padding.
     * @param left  the left padding.
     * @param bottom  the bottom padding.
     * @param right  the right padding.
     */
    public void setPadding(double top, double left, double bottom, 
                           double right);

}
