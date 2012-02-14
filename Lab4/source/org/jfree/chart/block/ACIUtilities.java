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
 * -----------------
 * ACIUtilities.java
 * -----------------
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: ACIUtilities.java,v 1.5 2005/11/16 11:03:58 mungady Exp $
 *
 * Changes
 * -------
 * 14-Jun-2005 : Version 1 (DG);
 * 
 */

package org.jfree.chart.block;

import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.util.Map;

import org.jfree.text.TextBlock;
import org.jfree.text.TextFragment;
import org.jfree.text.TextLine;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.TextAnchor;

/**
 * Some utility methods for working with text via the 
 * {@link AttributedCharacterIterator} interface.
 */
public class ACIUtilities {

    /** The scale factor for deriving a superscript font. */
    private static double superscriptScaleFactor = 0.666;
    
    /** The scale factor for deriving a subscript font. */
    private static double subscriptScaleFactor = 0.666;
    
    /** The superscript offset. */
    private static float superscriptOffset = 0.25f;
    
    /** The subscript offset. */
    private static float subscriptOffset = 0.20f;
    
    /**
     * Private constructor prevents object creation.
     */
    private ACIUtilities() {
    }

    /**
     * Returns the scale factor for deriving a superscript font.
     * 
     * @return The scale factor for deriving a superscript font.
     * 
     * @see #createTextLine(AttributedCharacterIterator, Font, Paint)
     */
    public static double getSuperscriptScaleFactor() {
        return superscriptScaleFactor;
    }
    
    /**
     * Sets the scale factor for deriving a superscript font.
     * 
     * @param scale  the scale factor for deriving a superscript font.
     * 
     * @see #createTextLine(AttributedCharacterIterator, Font, Paint)
     */
    public static void setSuperscriptScaleFactor(double scale) {
        superscriptScaleFactor = scale;
    }
    
    /**
     * Returns the scale factor for deriving a subscript font.
     * 
     * @return The scale factor for deriving a subscript font.
     * 
     * @see #createTextLine(AttributedCharacterIterator, Font, Paint)
     */
    public static double getSubscriptScaleFactor() {
        return subscriptScaleFactor;
    }
    
    /**
     * Sets the scale factor for deriving a subscript font.
     * 
     * @param scale  the scale factor for deriving a subscript font.
     * 
     * @see #createTextLine(AttributedCharacterIterator, Font, Paint)
     */
    public static void setSubscriptScaleFactor(double scale) {
        subscriptScaleFactor = scale;
    }
    
    /**
     * Returns the superscript offset.  The default value is 0.25f.
     * 
     * @return The superscript offset.
     * 
     * @see #createTextLine(AttributedCharacterIterator, Font, Paint)
     */
    public static float getSuperscriptOffset() {
        return superscriptOffset;
    }
    
    /**
     * Sets the superscript offset as a fraction of the font ascent.
     * 
     * @param offset  the offset.
     * 
     * @see #createTextLine(AttributedCharacterIterator, Font, Paint)
     */
    public static void setSuperscriptOffset(float offset) {
        superscriptOffset = offset;
    }
    
    /**
     * Returns the subscript offset.  The default value is 0.20f.
     * 
     * @return The subscript offset.
     * 
     * @see #createTextLine(AttributedCharacterIterator, Font, Paint)
     */
    public static float getSubscriptOffset() {
        return subscriptOffset;
    }
    
    /**
     * Sets the subscript offset as a fraction of the font ascent.
     * 
     * @param offset  the offset.
     * 
     * @see #createTextLine(AttributedCharacterIterator, Font, Paint)
     */
    public static void setSubscriptOffset(float offset) {
        subscriptOffset = offset;
    }
        
    /**
     * Returns the bounds for the specified text.
     * 
     * @param text  the text (<code>null</code> permitted).
     * @param g2  the graphics context (not <code>null</code>).
     * 
     * @return The text bounds (<code>null</code> if the <code>text</code> 
     *         argument is <code>null</code>).
     */
    public static Rectangle2D getTextBounds(AttributedCharacterIterator text,
                                            Graphics2D g2) { 
        TextLayout tl = new TextLayout(text, g2.getFontRenderContext());
        return tl.getBounds();
    }
    
    /**
     * Draws a string such that the specified anchor point is aligned to the 
     * given (x, y) location.
     *
     * @param text  the text.
     * @param g2  the graphics device.
     * @param x  the x coordinate (Java 2D).
     * @param y  the y coordinate (Java 2D).
     * @param anchor  the anchor location.
     * 
     * @return The text bounds (adjusted for the text position).
     */
    public static Rectangle2D drawAlignedString(
            AttributedCharacterIterator text, Graphics2D g2, float x, float y,
            TextAnchor anchor) {

        final Rectangle2D textBounds = new Rectangle2D.Double();
        final float[] adjust = deriveTextBoundsAnchorOffsets(
            g2, text, anchor, textBounds
        );
        // adjust text bounds to match string position
        textBounds.setRect(
            x + adjust[0], y + adjust[1] + adjust[2], 
            textBounds.getWidth(), textBounds.getHeight()
        );
        g2.drawString(text, x + adjust[0], y + adjust[1]);
        return textBounds;

    }

    /**
     * A utility method that calculates the anchor offsets for a string.  
     * Normally, the (x, y) coordinate for drawing text is a point on the 
     * baseline at the left of the text string.  If you add these offsets to 
     * (x, y) and draw the string, then the anchor point should coincide with 
     * the (x, y) point.
     *
     * @param g2  the graphics device (not <code>null</code>).
     * @param text  the text.
     * @param anchor  the anchor point.
     * @param textBounds  the text bounds (if not <code>null</code>, this 
     *                    object will be updated by this method to match the 
     *                    string bounds).
     * 
     * @return  The offsets.
     */
    private static float[] deriveTextBoundsAnchorOffsets(final Graphics2D g2, 
            AttributedCharacterIterator text, final TextAnchor anchor,
            final Rectangle2D textBounds) {

        final float[] result = new float[3];
        TextLayout tl = new TextLayout(text, g2.getFontRenderContext());
        Rectangle2D bounds = tl.getBounds();
        final float ascent = tl.getAscent();
        result[2] = - ascent;
        final float halfAscent = ascent / 2.0f;
        final float descent = tl.getDescent();
        final float leading = tl.getLeading();
        float xAdj = 0.0f;
        float yAdj = 0.0f;

        if (anchor == TextAnchor.TOP_CENTER
                || anchor == TextAnchor.CENTER
                || anchor == TextAnchor.BOTTOM_CENTER
                || anchor == TextAnchor.BASELINE_CENTER
                || anchor == TextAnchor.HALF_ASCENT_CENTER) {
                    
            xAdj = (float) -bounds.getWidth() / 2.0f;
            
        }
        else if (anchor == TextAnchor.TOP_RIGHT
                || anchor == TextAnchor.CENTER_RIGHT
                || anchor == TextAnchor.BOTTOM_RIGHT
                || anchor == TextAnchor.BASELINE_RIGHT
                || anchor == TextAnchor.HALF_ASCENT_RIGHT) {
                    
            xAdj = (float) -bounds.getWidth();
            
        }

        if (anchor == TextAnchor.TOP_LEFT
                || anchor == TextAnchor.TOP_CENTER
                || anchor == TextAnchor.TOP_RIGHT) {
                    
            yAdj = -descent - leading + (float) bounds.getHeight();
            
        }
        else if (anchor == TextAnchor.HALF_ASCENT_LEFT
                || anchor == TextAnchor.HALF_ASCENT_CENTER
                || anchor == TextAnchor.HALF_ASCENT_RIGHT) {
                    
            yAdj = halfAscent;
            
        }
        else if (anchor == TextAnchor.CENTER_LEFT
                || anchor == TextAnchor.CENTER
                || anchor == TextAnchor.CENTER_RIGHT) {
                    
            yAdj = -descent - leading + (float) (bounds.getHeight() / 2.0);
            
        }
        else if (anchor == TextAnchor.BASELINE_LEFT
                || anchor == TextAnchor.BASELINE_CENTER
                || anchor == TextAnchor.BASELINE_RIGHT) {
                    
            yAdj = 0.0f;
            
        }
        else if (anchor == TextAnchor.BOTTOM_LEFT
                || anchor == TextAnchor.BOTTOM_CENTER
                || anchor == TextAnchor.BOTTOM_RIGHT) {
                    
            yAdj = -tl.getDescent() - tl.getLeading();
            
        }
        if (textBounds != null) {
            textBounds.setRect(bounds);   
        }
        result[0] = xAdj;
        result[1] = yAdj;
        return result;

    }

    /**
     * A utility method for drawing rotated text.
     * <P>
     * A common rotation is -Math.PI/2 which draws text 'vertically' (with the 
     * top of the characters on the left).
     *
     * @param text  the text.
     * @param g2  the graphics device.
     * @param angle  the angle of the (clockwise) rotation (in radians).
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     */
    public static void drawRotatedString(AttributedCharacterIterator text,
                                         Graphics2D g2,
                                         double angle,
                                         float x, float y) {
        drawRotatedString(text, g2, x, y, angle, x, y);
    }

    /**
     * A utility method for drawing rotated text.
     * <P>
     * A common rotation is -Math.PI/2 which draws text 'vertically' (with the 
     * top of the characters on the left).
     *
     * @param text  the text.
     * @param g2  the graphics device.
     * @param textX  the x-coordinate for the text (before rotation).
     * @param textY  the y-coordinate for the text (before rotation).
     * @param angle  the angle of the (clockwise) rotation (in radians).
     * @param rotateX  the point about which the text is rotated.
     * @param rotateY  the point about which the text is rotated.
     */
    public static void drawRotatedString(AttributedCharacterIterator text,
                                         Graphics2D g2,
                                         float textX,
                                         float textY,
                                         double angle,
                                         float rotateX,
                                         float rotateY) {

        if ((text == null) || (text.equals(""))) {
            return;
        }

        final AffineTransform saved = g2.getTransform();

        // apply the rotation...
        final AffineTransform rotate = AffineTransform.getRotateInstance(
            angle, rotateX, rotateY
        );
        g2.transform(rotate);
          
        final TextLayout tl = new TextLayout(
            text, g2.getFontRenderContext()
        );
        tl.draw(g2, textX, textY);

        g2.setTransform(saved);

    }

    /**
     * Draws a string that is aligned by one anchor point and rotated about 
     * another anchor point.
     *
     * @param text  the text.
     * @param g2  the graphics device.
     * @param x  the x-coordinate for positioning the text.
     * @param y  the y-coordinate for positioning the text.
     * @param textAnchor  the text anchor.
     * @param angle  the rotation angle.
     * @param rotationX  the x-coordinate for the rotation anchor point.
     * @param rotationY  the y-coordinate for the rotation anchor point.
     */
    public static void drawRotatedString(AttributedCharacterIterator text,
                                         Graphics2D g2,
                                         float x,
                                         float y,
                                         TextAnchor textAnchor,
                                         double angle,
                                         float rotationX, float rotationY) {

        if (text == null || text.equals("")) {
            return;
        }
        final float[] textAdj = deriveTextBoundsAnchorOffsets(
            g2, text, textAnchor
        );
        drawRotatedString(
            text, g2, x + textAdj[0], y + textAdj[1],
            angle, rotationX, rotationY
        );

    }

    /**
     * Draws a string that is aligned by one anchor point and rotated about 
     * another anchor point.
     *
     * @param text  the text.
     * @param g2  the graphics device.
     * @param x  the x-coordinate for positioning the text.
     * @param y  the y-coordinate for positioning the text.
     * @param textAnchor  the text anchor.
     * @param angle  the rotation angle (in radians).
     * @param rotationAnchor  the rotation anchor.
     */
    public static void drawRotatedString(AttributedCharacterIterator text,
                                         Graphics2D g2,
                                         float x,
                                         float y,
                                         TextAnchor textAnchor,
                                         double angle,
                                         TextAnchor rotationAnchor) {

        if (text == null || text.equals("")) {
            return;
        }
        final float[] textAdj = deriveTextBoundsAnchorOffsets(
            g2, text, textAnchor
        );
        final float[] rotateAdj = deriveRotationAnchorOffsets(
            g2, text, rotationAnchor
        );
        drawRotatedString(
            text, g2, x + textAdj[0], y + textAdj[1],
            angle, x + textAdj[0] + rotateAdj[0], y + textAdj[1] + rotateAdj[1]
        );

    }

    /**
     * Returns a shape that represents the bounds of the string after the 
     * specified rotation has been applied.
     * 
     * @param text  the text (<code>null</code> permitted).
     * @param g2  the graphics device.
     * @param x  the x coordinate for the anchor point.
     * @param y  the y coordinate for the anchor point.
     * @param textAnchor  the text anchor.
     * @param angle  the angle.
     * @param rotationAnchor  the rotation anchor.
     * 
     * @return The bounds (possibly <code>null</code>).
     */
    public static Shape calculateRotatedStringBounds(
            AttributedCharacterIterator text, 
            final Graphics2D g2, final float x, final float y,
            final TextAnchor textAnchor, final double angle,
            final TextAnchor rotationAnchor) {

        if (text == null || text.equals("")) {
            return null;
        }
        final float[] textAdj = deriveTextBoundsAnchorOffsets(
            g2, text, textAnchor
        );
        final float[] rotateAdj = deriveRotationAnchorOffsets(
            g2, text, rotationAnchor
        );
        final Shape result = calculateRotatedStringBounds(
            text, g2, x + textAdj[0], y + textAdj[1],
            angle, x + textAdj[0] + rotateAdj[0], y + textAdj[1] + rotateAdj[1]
        );
        return result;
            
    }
    
    /**
     * A utility method that calculates the anchor offsets for a string.  
     * Normally, the (x, y) coordinate for drawing text is a point on the 
     * baseline at the left of the text string.  If you add these offsets to 
     * (x, y) and draw the string, then the anchor point should coincide with 
     * the (x, y) point.
     *
     * @param g2  the graphics device (not <code>null</code>).
     * @param text  the text.
     * @param anchor  the anchor point.
     *
     * @return  The offsets.
     */
    private static float[] deriveTextBoundsAnchorOffsets(Graphics2D g2, 
            AttributedCharacterIterator text, final TextAnchor anchor) {

        final float[] result = new float[2];
        TextLayout tl = new TextLayout(text, g2.getFontRenderContext());
        Rectangle2D bounds = tl.getBounds();
        final float ascent = tl.getAscent();
        final float halfAscent = ascent / 2.0f;
        final float descent = tl.getDescent();
        final float leading = tl.getLeading();
        float xAdj = 0.0f;
        float yAdj = 0.0f;

        if (anchor == TextAnchor.TOP_CENTER
                || anchor == TextAnchor.CENTER
                || anchor == TextAnchor.BOTTOM_CENTER
                || anchor == TextAnchor.BASELINE_CENTER
                || anchor == TextAnchor.HALF_ASCENT_CENTER) {
                    
            xAdj = (float) -bounds.getWidth() / 2.0f;
            
        }
        else if (anchor == TextAnchor.TOP_RIGHT
                || anchor == TextAnchor.CENTER_RIGHT
                || anchor == TextAnchor.BOTTOM_RIGHT
                || anchor == TextAnchor.BASELINE_RIGHT
                || anchor == TextAnchor.HALF_ASCENT_RIGHT) {
                    
            xAdj = (float) -bounds.getWidth();
            
        }

        if (anchor == TextAnchor.TOP_LEFT
                || anchor == TextAnchor.TOP_CENTER
                || anchor == TextAnchor.TOP_RIGHT) {
                    
            yAdj = -descent - leading + (float) bounds.getHeight();
            
        }
        else if (anchor == TextAnchor.HALF_ASCENT_LEFT
                || anchor == TextAnchor.HALF_ASCENT_CENTER
                || anchor == TextAnchor.HALF_ASCENT_RIGHT) {
                    
            yAdj = halfAscent;
            
        }
        else if (anchor == TextAnchor.CENTER_LEFT
                || anchor == TextAnchor.CENTER
                || anchor == TextAnchor.CENTER_RIGHT) {
                    
            yAdj = -descent - leading + (float) (bounds.getHeight() / 2.0);
            
        }
        else if (anchor == TextAnchor.BASELINE_LEFT
                || anchor == TextAnchor.BASELINE_CENTER
                || anchor == TextAnchor.BASELINE_RIGHT) {
                    
            yAdj = 0.0f;
            
        }
        else if (anchor == TextAnchor.BOTTOM_LEFT
                || anchor == TextAnchor.BOTTOM_CENTER
                || anchor == TextAnchor.BOTTOM_RIGHT) {
                    
            yAdj = -tl.getDescent() - tl.getLeading();
            
        }
        result[0] = xAdj;
        result[1] = yAdj;
        return result;

    }

    /**
     * A utility method that calculates the rotation anchor offsets for a 
     * string.  These offsets are relative to the text starting coordinate 
     * (BASELINE_LEFT).
     *
     * @param g2  the graphics device.
     * @param text  the text.
     * @param anchor  the anchor point.
     *
     * @return  The offsets.
     */
    private static float[] deriveRotationAnchorOffsets(final Graphics2D g2, 
            AttributedCharacterIterator text, final TextAnchor anchor) {

        final float[] result = new float[2];
        TextLayout tl = new TextLayout(text, g2.getFontRenderContext());
        Rectangle2D bounds = tl.getBounds();
        final float ascent = tl.getAscent();
        final float halfAscent = ascent / 2.0f;
        final float descent = tl.getDescent();
        final float leading = tl.getLeading();
        float xAdj = 0.0f;
        float yAdj = 0.0f;

        if (anchor == TextAnchor.TOP_LEFT
                || anchor == TextAnchor.CENTER_LEFT
                || anchor == TextAnchor.BOTTOM_LEFT
                || anchor == TextAnchor.BASELINE_LEFT
                || anchor == TextAnchor.HALF_ASCENT_LEFT) {

            xAdj = 0.0f;

        }
        else if (anchor == TextAnchor.TOP_CENTER
                || anchor == TextAnchor.CENTER
                || anchor == TextAnchor.BOTTOM_CENTER
                || anchor == TextAnchor.BASELINE_CENTER
                || anchor == TextAnchor.HALF_ASCENT_CENTER) {
                    
            xAdj = (float) bounds.getWidth() / 2.0f;
           
        }
        else if (anchor == TextAnchor.TOP_RIGHT
                || anchor == TextAnchor.CENTER_RIGHT
                || anchor == TextAnchor.BOTTOM_RIGHT
                || anchor == TextAnchor.BASELINE_RIGHT
                || anchor == TextAnchor.HALF_ASCENT_RIGHT) {
                    
            xAdj = (float) bounds.getWidth();
            
        }

        if (anchor == TextAnchor.TOP_LEFT
                || anchor == TextAnchor.TOP_CENTER
                || anchor == TextAnchor.TOP_RIGHT) {
                    
            yAdj = descent + leading - (float) bounds.getHeight();
            
        }
        else if (anchor == TextAnchor.CENTER_LEFT
                || anchor == TextAnchor.CENTER
                || anchor == TextAnchor.CENTER_RIGHT) {
                    
            yAdj = descent + leading - (float) (bounds.getHeight() / 2.0);
            
        }
        else if (anchor == TextAnchor.HALF_ASCENT_LEFT
                || anchor == TextAnchor.HALF_ASCENT_CENTER
                || anchor == TextAnchor.HALF_ASCENT_RIGHT) {
                    
            yAdj = -halfAscent;
            
        }
        else if (anchor == TextAnchor.BASELINE_LEFT
                || anchor == TextAnchor.BASELINE_CENTER
                || anchor == TextAnchor.BASELINE_RIGHT) {
                    
            yAdj = 0.0f;
            
        }
        else if (anchor == TextAnchor.BOTTOM_LEFT
                || anchor == TextAnchor.BOTTOM_CENTER
                || anchor == TextAnchor.BOTTOM_RIGHT) {
                    
            yAdj = tl.getDescent() + tl.getLeading();
            
        }
        result[0] = xAdj;
        result[1] = yAdj;
        return result;

    }

    /**
     * Returns a shape that represents the bounds of the string after the 
     * specified rotation has been applied.
     * 
     * @param text  the text (<code>null</code> permitted).
     * @param g2  the graphics device.
     * @param textX  the x coordinate for the text.
     * @param textY  the y coordinate for the text.
     * @param angle  the angle.
     * @param rotateX  the x coordinate for the rotation point.
     * @param rotateY  the y coordinate for the rotation point.
     * 
     * @return The bounds (<code>null</code> if <code>text</code> is 
     *         </code>null</code> or has zero length).
     */
    public static Shape calculateRotatedStringBounds(
            AttributedCharacterIterator text, Graphics2D g2, float textX,
            float textY, double angle, float rotateX, float rotateY) {

        if ((text == null) || (text.equals(""))) {
            return null;
        }
        TextLayout tl = new TextLayout(text, g2.getFontRenderContext());
        Rectangle2D bounds = tl.getBounds();
        AffineTransform translate = AffineTransform.getTranslateInstance(
            textX, textY);
        Shape translatedBounds = translate.createTransformedShape(bounds);
        AffineTransform rotate = AffineTransform.getRotateInstance(angle, 
                rotateX, rotateY);
        Shape result = rotate.createTransformedShape(translatedBounds);
        return result;

    }
    
    /**
     * Creates a text line from an {@link AttributedCharacterIterator}.  The
     * attributes recognised are TextAttribute.FONT, paint, superscript and
     * subscript.
     * 
     * @param aci  the text.
     * 
     * @return A text line.
     */
    public static TextLine createTextLine(AttributedCharacterIterator aci, 
            Font defaultFont, Paint defaultPaint) {
        TextLine result = new TextLine();
        int current = 0;
        char ch = aci.first();
        while (ch != CharacterIterator.DONE) {
            int next = aci.getRunLimit();
            Map atts = aci.getAttributes();
            StringBuffer sb = new StringBuffer();
            while (current < next) {
                sb.append(aci.current());
                aci.next();
                current++;
            }
            Font font = (Font) atts.get(TextAttribute.FONT);
            if (font == null) {
                font = defaultFont;   
            }
            Paint paint = (Paint) atts.get(TextAttribute.FOREGROUND);
            if (paint == null) {
                paint = defaultPaint;   
            }
            Integer superscript = (Integer) atts.get(TextAttribute.SUPERSCRIPT);
            if (superscript == null) {
                superscript = new Integer(0);
            }
            Float weight = (Float) atts.get(TextAttribute.WEIGHT);
            if (TextAttribute.WEIGHT_BOLD.equals(weight)) {
                font = font.deriveFont(Font.BOLD | font.getStyle()); 
            }
            Float posture = (Float) atts.get(TextAttribute.POSTURE);
            if (TextAttribute.POSTURE_OBLIQUE.equals(posture)) {
                font = font.deriveFont(Font.ITALIC | font.getStyle());   
            }
            LineMetrics lm = font.getLineMetrics(sb.toString(), 
                    new FontRenderContext(new AffineTransform(), true, true));
            float ascent = lm.getAscent();
            float offset = -(ascent * superscript.floatValue());
            if (superscript.intValue() > 0) {
                font = font.deriveFont(AffineTransform.getScaleInstance(
                        superscriptScaleFactor, superscriptScaleFactor));   
                offset = offset * superscriptOffset;
            }
            else if (superscript.intValue() < 0) {
                font = font.deriveFont(AffineTransform.getScaleInstance(
                        subscriptScaleFactor, subscriptScaleFactor));   
                offset = offset * subscriptOffset;                
            }
            result.addFragment(new TextFragment(sb.toString(), font, paint, 
                    offset));
            ch = aci.setIndex(next);
        }
        return result;
    }
    
    /**
     * Creates a {@link TextBlock}.
     * 
     * @param aci  the source of attributed text.
     * @param defaultFont  the default font.
     * @param defaultPaint  the default paint.
     * @param lineAlignment  the line alignment.
     * 
     * @return The text block.
     */
    public static TextBlock createTextBlock(AttributedCharacterIterator aci, 
            Font defaultFont, Paint defaultPaint, 
            HorizontalAlignment lineAlignment) {
        TextBlock result = new TextBlock();
        result.setLineAlignment(lineAlignment);
        int start = 0;
        int end = 0;
        char c = aci.first();
        while (c != CharacterIterator.DONE) {
            if (c == '\n') {
                end = aci.getIndex();
                if (end > start) {
                    TextLine line = createTextLine(new AttributedString(
                            (AttributedCharacterIterator) aci.clone(), start, 
                            end).getIterator(), defaultFont, defaultPaint);
                    result.addLine(line);
                    start = end + 1;
                }
            }
            c = aci.next();   
        }
        end = aci.getIndex();
        if (end > start) {
            TextLine line = createTextLine(new AttributedString(aci, start, 
                    end).getIterator(), defaultFont, defaultPaint);
            result.addLine(line);
            start = end + 1;
        }
        return result;
    }

    /**
     * Returns <code>true</code> if two {@link AttributedCharacterIterator} instances are the
     * same or both <code>null</code>.
     * 
     * @param i1  character iterator 1 (<code>null</code> permitted).
     * @param i2  character iterator 2 (<code>null</code> permitted).
     * 
     * @return A boolean.
     */
    public static boolean equal(AttributedCharacterIterator i1, AttributedCharacterIterator i2) {
        
        // handle cases where either or both arguments are null
        if (i1 == null) {
            return (i2 == null);   
        }
        if (i2 == null) {
            return false;   
        }
        
        // check that both iterators cover the same length of text
        int length1 = i1.getEndIndex() - i1.getBeginIndex();
        int length2 = i2.getEndIndex() - i2.getBeginIndex();
        if (length1 != length2) {
            return false;
        }
        
        char c1 = i1.first();
        char c2 = i2.first();
        if (c1 != c2) {
            return false;
        }
        boolean done = false;
        while (!done) {
            int limit1 = i1.getRunLimit();
            int limit2 = i2.getRunLimit();
            if (limit1 != limit2) {
                return false;
            }
            // check attributes and values are the same
            Map m1 = i1.getAttributes();
            Map m2 = i2.getAttributes();
            if (!m1.equals(m2)) {
                return false;
            }
            if (c1 == AttributedCharacterIterator.DONE 
                || c2 == AttributedCharacterIterator.DONE) {
                done = true;
            }
            c1 = i1.setIndex(limit1);
            c2 = i2.setIndex(limit2);
        }
        return (c1 == c2);
    }
}
