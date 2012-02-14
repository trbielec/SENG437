package org.jfree.chart;

/**
 * A key that specifies a location within a {@link NestedGrid}.
 */
public class NestedGridKey {

    /** The row index. */
    private int row;
    
    /** The column index. */
    private int column;
    
    /** 
     * The key for the next level (or <code>null</code> to indicate a location
     * at this level.
     */
    private NestedGridKey subLevelKey;

    /**
     * Creates a key for a cell at the current grid level.
     * 
     * @param row  the row index.
     * @param column  the column index.
     */
    public NestedGridKey(int row, int column) {
        this(row, column, null);   
    }
    
    /**
     * Creates a new key.
     * 
     * @param row  the row index.
     * @param column  the column index.
     * @param subLevelKey  the sub-level key (<code>null</code> permitted).
     */
    public NestedGridKey(int row, int column, NestedGridKey subLevelKey) {
        if (row < 0) {
            throw new IllegalArgumentException("Negative 'row' argument.");   
        }
        if (column < 0) {
            throw new IllegalArgumentException("Negative 'column' argument.");   
        }
        this.row = row;
        this.column = column;
        this.subLevelKey = subLevelKey;
    }
    
    /**
     * Returns the row index.
     * 
     * @return The row index.
     */
    public int getRow() {
        return this.row;   
    }
    
    /**
     * Returns the column index.
     * 
     * @return The column index.
     */
    public int getColumn() {
        return this.column;   
    }
    
    /**
     * Returns the sub-level key.
     * 
     * @return The sub-level key (possibly <code>null</code>).
     */
    public NestedGridKey getSubLevelKey() {
        return this.subLevelKey;   
    }
    
}
