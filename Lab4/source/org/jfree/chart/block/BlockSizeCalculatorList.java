package org.jfree.chart.block;

/**
 * An interface to get access to height and width calculators for blocks.
 */
public interface BlockSizeCalculatorList {
    
    public BlockHeightCalculator findHeightCalculator(Block block);

}
