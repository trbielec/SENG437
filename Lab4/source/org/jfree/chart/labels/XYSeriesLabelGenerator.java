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
 * ---------------------------
 * XYSeriesLabelGenerator.java
 * ---------------------------
 * (C) Copyright 2004, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: XYSeriesLabelGenerator.java,v 1.3 2005/06/28 08:58:01 mungady Exp $
 *
 * Changes
 * -------
 * 16-Nov-2004 : Version 1 (DG);
 *
 */

package org.jfree.chart.labels;

import java.awt.Font;
import java.awt.Paint;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import org.jfree.data.xy.XYDataset;

/**
 * A generator that creates labels for the series in an {@link XYDataset}.
 */
public interface XYSeriesLabelGenerator {

    /**
     * Generates a label for the specified series.  This label will be
     * used for the chart legend.
     * 
     * @param dataset  the dataset.
     * @param series  the series index.
     * 
     * @return A series label.
     */
    public String generateLabel(XYDataset dataset, int series);
    
    /**
     * Generates an attributed label for the specified series, or 
     * <code>null</code> if no attributed label is available (in which case,
     * the string returned by {@link #generateLabel(XYDataset, int)} will 
     * provide the fallback).  Only certain attributes are recognised by the 
     * code that ultimately displays the labels: 
     * <ul>
     * <li>{@link TextAttribute#FONT}: will set the font;</li>
     * <li>{@link TextAttribute#POSTURE}: a value of 
     *     {@link TextAttribute#POSTURE_OBLIQUE} will add {@link Font#ITALIC} to
     *     the current font;</li>
     * <li>{@link TextAttribute#WEIGHT}: a value of 
     *     {@link TextAttribute#WEIGHT_BOLD} will add {@link Font#BOLD} to the 
     *     current font;</li>
     * <li>{@link TextAttribute#FOREGROUND}: this will set the {@link Paint} 
     *     for the current</li>
     * <li>{@link TextAttribute#SUPERSCRIPT}: the values 
     *     {@link TextAttribute#SUPERSCRIPT_SUB} and 
     *     {@link TextAttribute#SUPERSCRIPT_SUPER} are recognised.</li> 
     * </ul>
     * 
     * @param dataset  the dataset.
     * @param series  the series index.
     * 
     * @return An attributed label (possibly <code>null</code>).
     */
    public AttributedString generateAttributedLabel(XYDataset dataset, 
                                                    int series);

}