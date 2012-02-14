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
 * BlockPanel.java
 * ---------------
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: BlockPanel.java,v 1.3 2005/11/16 17:06:54 mungady Exp $
 *
 * Changes
 * -------
 * 29-Apr-2005 : Version 1 (DG);
 * 
 */

package org.jfree.chart.block;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.ui.Size2D;

/**
 * A Swing GUI component for displaying a {@link Block} object.
 */
public class BlockPanel extends JPanel implements Serializable {

    /** The block that is displayed in the panel. */
    private Block block;

    private ArrangeParams arrangeParams;
    
    /**
     * Constructs a new panel.
     *
     * @param block  the block.
     * @param arrangeParams  the layout parameters (<code>null</code> not 
     *                       permitted).
     */
    public BlockPanel(Block block, ArrangeParams arrangeParams) {
        
        if (arrangeParams == null) {
            throw new IllegalArgumentException(
                    "Null 'arrangeParams' argument.");
        }
        this.block = block;
        this.arrangeParams = arrangeParams;

        BufferedImage image = new BufferedImage(10, 10, 
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        ArrangeResult ar = getBlock().arrange(g2, RectangleConstraint.NONE, 
            this.arrangeParams);
        List messages = ar.getMessages();
        if (messages != null) {
            Iterator iterator = messages.iterator();
            while (iterator.hasNext()) {
                Message m = (Message) iterator.next();
                System.out.println(m.toString());
            }
        }
        Size2D size = ar.getSize();
        setPreferredSize(new Dimension((int) size.width, (int) size.height));
    }

    /**
     * Returns the block contained in the panel.
     *
     * @return The block (possibly <code>null</code>).
     */
    public Block getBlock() {
        return this.block;
    }

    /**
     * Sets the block that is displayed in the panel.
     *
     * @param block  the block (<code>null</code> permitted).
     */
    public void setBlock(Block block) {
        this.block = block;
        repaint();
    }

    /**
     * Paints the component by drawing the chart to fill the entire component,
     * but allowing for the insets (which will be non-zero if a border has been
     * set for this component).  To increase performance (at the expense of
     * memory), an off-screen buffer image can be used.
     *
     * @param g  the graphics device for drawing on.
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (getBlock() == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        Map hints = new HashMap();
        hints.put(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.addRenderingHints(hints);
        Insets insets = getInsets();

        AffineTransform saved = g2.getTransform();
        g2.translate(insets.left, insets.top);
        
        ArrangeResult ar = getBlock().arrange(g2, RectangleConstraint.NONE, 
                this.arrangeParams);
        Size2D s = ar.getSize();
        getBlock().draw(g2, new Rectangle2D.Double(0.0, 0.0, s.getWidth(), 
                s.getHeight()));
        g2.setTransform(saved);

    }

}
