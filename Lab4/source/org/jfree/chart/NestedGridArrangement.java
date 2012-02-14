package org.jfree.chart;

import java.awt.Graphics2D;

import org.jfree.chart.block.ArrangeParams;
import org.jfree.chart.block.ArrangeResult;
import org.jfree.chart.block.Arrangement;
import org.jfree.chart.block.Block;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.ui.Size2D;

/**
 * A layout manager that uses a nested grid structure to position chart
 * elements.
 */
public class NestedGridArrangement implements Arrangement {
    
    /** The grid structure. */
    private NestedGrid grid;
    
    /**
     * Creates a new nested grid arrangement.
     */
    public NestedGridArrangement() {
        this.grid = new NestedGrid();
    }
    
    /**
     * Adds a block and a key which can be used to determine the position of 
     * the block in the arrangement.  This method is called by the container 
     * (you don't need to call this method directly) and gives the arrangement
     * an opportunity to record the details if they are required.
     * 
     * @param block  the block.
     * @param key  the key (<code>null</code> permitted).
     */
    public void add(Block block, Object key) {
        // the key should specify where in the grid the block is to be
        // added...needs the ability to drill down into a nested grid
        if (key instanceof NestedGridKey) {
            this.grid.put((NestedGridKey) key, block);
        }
    }
    
    /**
     * Arranges the blocks within the specified container, subject to the given
     * constraint.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param constraint  the constraint.
     * @param params  the layout parameters (<code>null</code> not permitted).
     * 
     * @return The container size after the arrangement.
     */
    public ArrangeResult arrange(BlockContainer container, Graphics2D g2,
                                 RectangleConstraint constraint, 
                                 ArrangeParams params) {
        
        // what is the strategy for arranging the grid?
        // we must observe the incoming constraint
        // in a lot of cases we are expecting fixed dimensions to be supplied
        // by the Charter application.
        // we need to return log messages for potential layout problems...
        LengthConstraintType w = constraint.getWidthConstraintType();
        LengthConstraintType h = constraint.getHeightConstraintType();
        if (w == LengthConstraintType.NONE) {
            if (h == LengthConstraintType.NONE) {
                return arrangeNN(container, g2, params);  
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
                return arrangeFF(container, g2, constraint, params);  
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
        return new ArrangeResult(new Size2D(), null);  // TODO: complete this
    }
    
    /**
     * Arranges the items within a container.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param params  optional parameters.
     * 
     * @return The container size after the arrangement.
     */
    protected ArrangeResult arrangeNN(BlockContainer container, Graphics2D g2, 
                                      ArrangeParams params) {
        double columns = this.grid.getColumnCount();
        double rows = this.grid.getRowCount();
        for (int c = 0; c < columns; c++) {
            for (int r = 0; r < rows; r++) {
                Object element = this.grid.get(r, c);
                if (element instanceof NestedGrid) {
                    
                }
            }
        }
        return null;
    }

    /**
     * Arranges the items within a container.
     * 
     * @param container  the container.
     * @param g2  the graphics device.
     * @param constraint  the constraint.
     * @param params  optional parameters.
     * 
     * @return The container size after the arrangement.
     */
    protected ArrangeResult arrangeFF(BlockContainer container, Graphics2D g2, 
                                      RectangleConstraint constraint, 
                                      ArrangeParams params) {
        double displayW = constraint.getWidth();
        double displayH = constraint.getHeight();
        double columns = this.grid.getColumnCount();
        double rows = this.grid.getRowCount();
        this.grid.resizeAll(0.0, 0.0, displayW / columns, displayH / rows, g2);
        return new ArrangeResult(displayW, displayH, null);
    }

    /**
     * Clears any cached layout information retained by the arrangement.
     */
    public void clear() {
        this.grid = new NestedGrid();  
    }

}
