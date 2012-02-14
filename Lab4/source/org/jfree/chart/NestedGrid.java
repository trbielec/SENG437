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
 * NestedGrid.java
 * ---------------
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: NestedGrid.java,v 1.5 2005/10/28 10:17:43 mungady Exp $
 *
 * Changes
 * -------
 * 11-May-2005 : Version 1 (DG);
 * 
 */

package org.jfree.chart;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jfree.chart.block.AbstractContentBlock;
import org.jfree.chart.block.ArrangeParams;
import org.jfree.chart.block.ArrangeResult;
import org.jfree.chart.block.Block;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.Size2D;

/**
 * A structural element used by the {@link NestedGridArrangement} class to 
 * layout blocks in a grid structure.
 */
public class NestedGrid extends AbstractContentBlock {

    /** The elements in the grid. */
    private Block[][] blocks;
    
    private double[] rowHeights;
    
    private double[] columnWidths;
    
    /**
     * Creates a new empty grid.
     */
    public NestedGrid() {
        this.blocks = new Block[0][0];
        this.rowHeights = new double[0];
        this.columnWidths = new double[0];
    }

    /**
     * Returns the number of columns in the grid.
     * 
     * @return The number of columns.
     */
    public int getColumnCount() {
        return (this.blocks.length == 0 ? 0 : this.blocks[0].length);
    }

    /**
     * Returns the number of rows in the grid.
     * 
     * @return The number of rows.
     */
    public int getRowCount() {
        return this.blocks.length;
    }
    
    public double getRowHeight(int row) {
        return this.rowHeights[row];   
    }
    
    public void setRowHeight(int row, double height) {
        this.rowHeights[row] = height;   
    }
    
    public double getColumnWidth(int column) {
        return this.columnWidths[column];   
    }
    
    public void setColumnWidth(int column, double width) {
        this.columnWidths[column] = width;   
    }
    
    public Block get(int row, int column) {
        return this.blocks[row][column];    
    }
    
    public void put(int row, int column, Block block) {
        put(new NestedGridKey(row, column), block);    
    }
    
    /**
     * Adds an element to the nested grid at a specified location.
     * 
     * @param key  the key (<code>null</code> not permitted).
     * @param block  the element.
     */
    public void put(NestedGridKey key, Block block) {
        if (key == null) {
            throw new IllegalArgumentException("Null 'key' argument.");   
        }
        int row = key.getRow();
        int column = key.getColumn();
        ensureCapacity(row + 1, column + 1);
        if (key.getSubLevelKey() == null) {
            this.blocks[row][column] = block;
        }
        else {
            // three cases:
            if (this.blocks[row][column] == null) {
                this.blocks[row][column] = new NestedGrid();
            }
            if (!(this.blocks[row][column] instanceof NestedGrid)) {
                throw new RuntimeException("Found an element, not a nested grid");
            }
            NestedGrid subgrid = (NestedGrid) this.blocks[row][column];
            subgrid.put(key.getSubLevelKey(), block);
        }
    }
    
    /**
     * Arranges the blocks within the specified container, subject to the given
     * constraint.
     * 
     * @param g2  the graphics device.
     * @param constraint  the constraint.
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The container size after the arrangement.
     */
    public ArrangeResult arrange(Graphics2D g2, RectangleConstraint constraint, 
                                 ArrangeParams params) {
        
        this.rowHeights = new double[getRowCount()];
        this.columnWidths = new double[getColumnCount()];
        RectangleConstraint contentConstraint = toContentConstraint(constraint);
        ArrangeResult result = null;
        LengthConstraintType w = contentConstraint.getWidthConstraintType();     
        LengthConstraintType h = contentConstraint.getHeightConstraintType();
        if (w == LengthConstraintType.NONE) {
            if (h == LengthConstraintType.NONE) {
                result = arrangeNN(g2, params);  
            }
            else if (h == LengthConstraintType.FIXED) {
                throw new RuntimeException("Not implemented.");  
            }
            else if (h == LengthConstraintType.RANGE) {
                throw new RuntimeException("Not implemented.");  
            }
        }
        else if (w == LengthConstraintType.FIXED) {
            if (h == LengthConstraintType.NONE) {
                throw new RuntimeException("Not implemented.");   
            }
            else if (h == LengthConstraintType.FIXED) {
                result = arrangeFF(g2, contentConstraint.getWidth(), 
                        contentConstraint.getHeight(), params);  
            }
            else if (h == LengthConstraintType.RANGE) {
                throw new RuntimeException("Not implemented.");  
            }
        }
        else if (w == LengthConstraintType.RANGE) {
            if (h == LengthConstraintType.NONE) {
                throw new RuntimeException("Not implemented.");  
            }
            else if (h == LengthConstraintType.FIXED) {
                throw new RuntimeException("Not implemented.");  
            }
            else if (h == LengthConstraintType.RANGE) {
                throw new RuntimeException("Not implemented.");  
            }
        }
        Size2D contentSize = result.getSize();
        result.setSize(new Size2D(calculateTotalWidth(contentSize.getWidth()), 
                calculateTotalHeight(contentSize.getHeight())));
        return result;
    }
    
    /**
     * Arranges the items within a container.
     * 
     * @param g2  the graphics device.
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The container size after the arrangement.
     */
    protected ArrangeResult arrangeNN(Graphics2D g2, ArrangeParams params) {
        int columns = getColumnCount();
        int rows = getRowCount();
        for (int c = 0; c < columns; c++) {
            for (int r = 0; r < rows; r++) {
                Block block = get(r, c);
                if (block != null) {
                    ArrangeResult ar = block.arrange(g2, 
                            RectangleConstraint.NONE, params);
                    Size2D size = ar.getSize();
                    setRowHeight(r, Math.max(getRowHeight(r), 
                            size.getHeight()));
                    setColumnWidth(c, Math.max(getColumnWidth(c), 
                            size.getWidth()));
                }
            }
        }

        // go back through the grid and fix the size of all the blocks
        // ...and collect messages if logging
        List messages = null;
        if (params.isLogEnabled()) {
            messages = new java.util.ArrayList();
        }
        double x = 0.0;
        for (int c = 0; c < columns; c++) {
            double width = getColumnWidth(c);
            double y = 0.0;
            for (int r = 0; r < rows; r++) {
                double height = getRowHeight(r);
                Block block = get(r, c);
                if (block != null) {
                    RectangleConstraint constraint = new RectangleConstraint(
                            width, height);
                    ArrangeResult ar = block.arrange(g2, constraint, params);
                    if (params.isLogEnabled()) {
                        List m = ar.getMessages();
                        if (m != null) {
                            messages.addAll(m);
                        }
                    }
                    block.setBounds(new Rectangle2D.Double(x, y, width, 
                            height));
                }
                y = y + height;
            }
            x = x + width;
        }
        
        double width = 0;
        double height = 0;
        for (int c = 0; c < columns; c++) {
            width = width + getColumnWidth(c);   
        }
        for (int r = 0; r < rows; r++) {
            height = height + getRowHeight(r);   
        }
        ArrangeResult result = params.getRecyclableResult();
        if (result == null) {
            result = new ArrangeResult(new Size2D(width, height), messages);
        }
        else {
            result.setSize(new Size2D(width, height));
            result.setMessages(messages);
        }
        return result;
    }

    /**
     * Arranges the blocks in the grid with fixed constraints for both the
     * width and the height.  First, the row heights and column widths are 
     * calculated without constraints.  Next, all heights and widths are
     * adjusted by the same proportion to achieve the desired fixed height.
     * 
     * @param g2  the graphics device.
     * @param fixedWidth  the fixed width.
     * @param fixedHeight  the fixed height.
     * @param params  layout parameters (<code>null</code> not permitted).
     * 
     * @return The container size after the arrangement.
     */
    protected ArrangeResult arrangeFF(Graphics2D g2, 
                                      double fixedWidth, double fixedHeight, 
                                      ArrangeParams params) {
        // TODO: we should probably switch of messages for the first arrange
        ArrangeResult ar = arrangeNN(g2, params);
        Size2D unconstrainedSize = ar.getSize();
        double wRatio = fixedWidth / unconstrainedSize.getWidth();
        double hRatio = fixedHeight / unconstrainedSize.getHeight();
        for (int r = 0; r < getRowCount(); r++) {
            setRowHeight(r, getRowHeight(r) * hRatio);   
        }
        for (int c = 0; c < getColumnCount(); c++) {
            setColumnWidth(c, getColumnWidth(c) * wRatio);               
        }
        // go back through the grid and fix the size of all the blocks
        double x = 0.0;
        for (int c = 0; c < getColumnCount(); c++) {
            double width = getColumnWidth(c);
            double y = 0.0;
            for (int r = 0; r < getRowCount(); r++) {
                double height = getRowHeight(r);
                Block block = get(r, c);
                if (block != null) {
                    RectangleConstraint cc = new RectangleConstraint(width, 
                            height);
                    // TODO: don't we need an arrange here?
                    block.setBounds(new Rectangle2D.Double(x, y, width, 
                            height));
                }
                y = y + height;
            }
            x = x + width;
        }
        // TODO: the null should be the list of messages
        return new ArrangeResult(new Size2D(fixedWidth, fixedHeight), null);
    }

    /**
     * Draws the grid within the specified area.
     * 
     * @param g2  the graphics device (<code>null</code> not permitted).
     * @param area  the area (<code>null</code> not permitted).
     */
    public void draw(Graphics2D g2, Rectangle2D area) {
        // clone the area to make sure we don't update the original
        area = (Rectangle2D) area.clone();
        area = trimMargin(area);
        drawBorder(g2, area);
        area = trimBorder(area);
        area = trimPadding(area);
        AffineTransform saved = g2.getTransform();
        g2.translate(area.getX(), area.getY());
        
        int rows = getRowCount();
        int columns = getColumnCount();
        for (int c = 0; c < columns; c++) {
            for (int r= 0; r < rows; r++) {
                Block b = get(r, c);
                if (b != null) {
                    b.draw(g2, b.getBounds());
                }
            }
        }
        g2.setTransform(saved);
    }
    
    /**
     * Draws the block within the specified area.  Refer to the documentation 
     * for the implementing class for information about the <code>params</code>
     * and return value supported.
     * 
     * @param g2  the graphics device.
     * @param area  the area.
     * @param params  optional parameters (<code>null</code> permitted).
     * 
     * @return An optional return value (possibly <code>null</code>).
     */
    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        draw(g2, area);
        return null;
    }
    
    /**
     * Ensure that the grid contains sufficient capacity for the specified
     * number of rows and columns.
     * 
     * @param rows  the row count.
     * @param columns  the column count.
     */
    private void ensureCapacity(int rows, int columns) {
        ensureRows(rows);
        ensureColumns(columns);
    }
    
    /**
     * Ensure that the grid contains sufficient capacity for the specified 
     * number of rows.
     * 
     * @param rows  the row count.
     */
    private void ensureRows(int rows) {
        int existingRows = this.blocks.length;
        if (rows > existingRows) {
            int existingColumns = 0;
            if (existingRows > 0) {
                existingColumns = this.blocks[0].length;   
            }
            Block[][] old = this.blocks;
            this.blocks = new Block[rows][existingColumns];
            for (int r = 0; r < existingRows; r++) {
                this.blocks[r] = old[r];
            }
            
            // extend rowHeights
            double[] oldRowHeights = this.rowHeights;
            this.rowHeights = new double[rows];
            for (int r = 0; r < oldRowHeights.length; r++) {
                this.rowHeights[r] = oldRowHeights[r];   
            }
        }
    }
    
    /**
     * Expands the grid structure, if necessary, to ensure that it can hold
     * at least the specified number of columns.
     * 
     * @param columns  the required number of columns.
     */
    private void ensureColumns(int columns) {
        // will only work if there is at least one row
        int rowCount = this.blocks.length;
        if (rowCount == 0) {
            throw new RuntimeException("No rows in grid, so column count cannot be determined.");    
        }
        int existingCols = this.blocks[0].length;
        if (columns > existingCols) {
            for (int r = 0; r < rowCount; r++) {
                Block[] newRow = new Block[columns];
                System.arraycopy(this.blocks[r], 0, newRow, 0, existingCols);
                this.blocks[r] = newRow;
            }
            // extend columnWidths
            double[] oldColumnWidths = this.columnWidths;
            this.columnWidths = new double[columns];
            for (int c = 0; c < oldColumnWidths.length; c++) {
                this.columnWidths[c] = oldColumnWidths[c];   
            }
        }

    }
    
    /**
     * Puts an element at the specified side of the element indicated by the
     * key.  This may require an expansion of the grid.
     * 
     * @param key
     * @param edge
     * @param block
     */
    public void put(NestedGridKey key, RectangleEdge edge, Block block) {
       int row = key.getRow();
       int column = key.getColumn();
       if (key.getSubLevelKey() == null) {
           if (edge == RectangleEdge.LEFT) {
               if (column == 0) {
                   insertColumnZero();
               }
               else {
                   column--;
               }
           }
           else if (edge == RectangleEdge.RIGHT) {
               column++;
           }
           else if (edge == RectangleEdge.TOP) {
               if (row == 0) {
                   insertRowZero();
               }
               else {
                   row--;
               }
           }
           else if (edge == RectangleEdge.BOTTOM) {
               row++;
           }
           put(row, column, block);
       }
       else {
           Block e = this.blocks[row][column];
           if (!(e instanceof NestedGrid)) {
               throw new RuntimeException("Can't descend into that element.");
           }
           NestedGrid subgrid = (NestedGrid) e;
           subgrid.put(key.getSubLevelKey(), edge, block);
       }
    }
    
    private void insertRowZero() {
        int rows = this.blocks.length;
        int columns = this.blocks[0].length;
        Block[] newRow = new Block[columns];
        Block[][] newBlocks = new Block[rows + 1][];
        newBlocks[0] = newRow;
        for (int r = 0; r < rows; r++) {
            newBlocks[r + 1] = this.blocks[r];   
        }
        this.blocks = newBlocks;
    }

    private void insertColumnZero() {
        int rows = this.blocks.length;
        int columns = this.blocks[0].length;
        for (int r = 0; r < rows; r++) {
            Block[] newRow = new Block[columns + 1];
            System.arraycopy(this.blocks[r], 0, newRow, 1, this.blocks[r].length);
            this.blocks[r] = newRow;
        }
    }
    
    public void resizeAll(double x, double y, double width, double height, Graphics2D g2) {
        int columnCount = getColumnCount();
        int rowCount = getRowCount();
        for (int c = 0; c < columnCount; c++) {
            setColumnWidth(c, width);
            for (int r = 0; r < rowCount; r++) {
                Object element = get(r, c);
                if (element instanceof NestedGrid) {
                    NestedGrid subgrid = (NestedGrid) element;
                    subgrid.resizeAll(c * width, r * height, width / subgrid.getColumnCount(), height / subgrid.getRowCount(), g2);
                }
                else if (element instanceof Block) {
                    Block b = (Block) element;
                    b.arrange(g2, new RectangleConstraint(width, height), null);
                    b.setBounds(new Rectangle2D.Double(x + c * width, y + r * height, width, height));
                }
            }
        }
        for (int r = 0; r < rowCount; r++) {
            setRowHeight(r, height);   
        }
    }
    /**
     * Sets the margin (use {@link RectangleInsets#ZERO_INSETS} for no 
     * padding).
     * 
     * @param margin  the margin (<code>null</code> not permitted).
     */
    public void setMargin(RectangleInsets margin) {
        if (margin == null) {
            throw new IllegalArgumentException("Null 'margin' argument.");   
        }
        super.setMargin(margin);
    }


}
