package org.jfree.chart.block;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Creates out of a AttributedCharacterIterator with subscripts and superscripts
 * a new AttributedCharacterIterator where the {@link TextAttribute#SUPERSCRIPT}
 * is translated into a {@link TextAttribute#FONT}that has been appropriately
 * shrunk in size and shifted up/down for a super/subscript. <br>
 * <br>
 * It also works with multiple fonts in the character iterator, e.g.: <br>
 * <br>
 * String s = "attributed string of a length long enough to wrap a few times.";
 * <br>
 * as = new AttributedString(s); <br>
 * as.addAttribute(TextAttribute.FONT, new Font("Verdana", Font.PLAIN, 72));
 * <br>
 * as.addAttribute(TextAttribute.FONT, new Font("Times New Roman", Font.PLAIN,
 * 72), 10, 16); <br>
 * as.addAttribute(TextAttribute.SUPERSCRIPT,TextAttribute.SUPERSCRIPT_SUB, 3,
 * 5); <br>
 * as.addAttribute(TextAttribute.SUPERSCRIPT,TextAttribute.SUPERSCRIPT_SUPER,
 * 12, 14); <br>
 * <br>
 * <br>
 * Note that it is apparently important not to create a font per character but
 * reusing them as much as possible, as creating fonts is a time consuming
 * operation ( <a href="http://mindprod.com/jgloss/font.html">Java Glossary:
 * Font </a>).
 * 
 * @author hum
 *  
 */
public class CharterAttributedCharacterIterator implements
        AttributedCharacterIterator {

    /**
     * Fraction of line metrics ascent by which font is shifted down. There is a
     * separate value for Sun's VM version 1.4.x, as it handles this value
     * differently (it appears to double this offset, so we halve it).
     */
    public static final double SUPER_OFFSET = (System.getProperty(
            "java.version").startsWith("1.4.") && System.getProperty(
            "java.vendor").equals("Sun Microsystems Inc.")) ? 0.25 : 0.5;

    /**
     * Fraction of line metrics ascent by which font is shifted up. There is a
     * separate value for Sun's VM version 1.4.x, as it handles this value
     * differently (it appears to double this offset, so we halve it).
     */
    public static final double SUB_OFFSET = (System.getProperty("java.version")
            .startsWith("1.4.") && System.getProperty("java.vendor").equals(
            "Sun Microsystems Inc.")) ? 0.2 : 0.4;

    /** Scaling factor for super and subscript text */
    public static final double SUBSUPER_SCALE_FACTOR = 0.71;

    protected FontRenderContext frc;

    protected String simpleString;
    protected Set allAttributes;
    protected ArrayList mapList;
    protected AttributedCharacterIterator aci = null;

    /* For supporting multiple fonts in AttributedCharacterIterator. */
    /** Maps a font to its superscript font */
    protected Map superFont = new HashMap();
    /** Maps a font to its subscript font */
    protected Map subFont = new HashMap();

    public CharterAttributedCharacterIterator(AttributedCharacterIterator aci,
            FontRenderContext frc) {
        this.aci = aci;
        this.frc = frc;
        buildAttributeTables();
        convertSuperSubScriptToAffineTransform();
    }

    private void convertSuperSubScriptToAffineTransform() {
        char c = aci.first();
        for (int i = 0; c != CharacterIterator.DONE; c = aci.next(), i++) {
            Map attrs = getAttributes();
            Integer superAttr = (Integer) attrs.get(TextAttribute.SUPERSCRIPT);
            if (superAttr != null) {
                Font aciFont = (Font) aci.getAttribute(TextAttribute.FONT);
                final LineMetrics lineMetrics = aciFont.getLineMetrics(aci, i,
                        i + 1, frc);
                if (superAttr.equals(TextAttribute.SUPERSCRIPT_SUB)) {
                    Font aciSubFont = subFont.containsKey(aciFont) ? (Font) subFont
                            .get(aciFont)
                            : createSubFont(aciFont, lineMetrics);

                    attrs.remove(TextAttribute.SUPERSCRIPT);
                    attrs.put(TextAttribute.FONT, aciSubFont);
                } else if (superAttr.equals(TextAttribute.SUPERSCRIPT_SUPER)) {
                    Font aciSuperFont = superFont.containsKey(aciFont) ? (Font) superFont
                            .get(aciFont)
                            : createSuperFont(aciFont, lineMetrics);

                    attrs.remove(TextAttribute.SUPERSCRIPT);
                    attrs.put(TextAttribute.FONT, aciSuperFont);
                }
            }
        }
    }

    private Font createSubFont(Font aciFont, final LineMetrics lineMetrics) {
        Map subFontAttrs = aciFont.getAttributes();
        AffineTransform subFontAT = AffineTransform.getScaleInstance(
                SUBSUPER_SCALE_FACTOR, SUBSUPER_SCALE_FACTOR);
        subFontAT.translate(0, lineMetrics.getAscent() * SUB_OFFSET);
        subFontAttrs.put(TextAttribute.TRANSFORM, subFontAT);
        Font aciSubFont = new Font(subFontAttrs);
        subFont.put(aciFont, aciSubFont);
        return aciSubFont;
    }

    private Font createSuperFont(Font aciFont, final LineMetrics lineMetrics) {
        Map subFontAttrs = aciFont.getAttributes();
        AffineTransform subFontAT = AffineTransform.getScaleInstance(
                SUBSUPER_SCALE_FACTOR, SUBSUPER_SCALE_FACTOR);
        subFontAT.translate(0, -lineMetrics.getAscent() * SUPER_OFFSET);
        subFontAttrs.put(TextAttribute.TRANSFORM, subFontAT);
        Font aciSubFont = new Font(subFontAttrs);
        superFont.put(aciFont, aciSubFont);
        return aciSubFont;
    }

    private void buildAttributeTables() {
        allAttributes = aci.getAllAttributeKeys();
        int length = aci.getEndIndex() - aci.getBeginIndex();
        mapList = new ArrayList(length);
        char c = aci.first();
        char chars[] = new char[length];
        for (int i = 0; i < length; ++i, c = aci.next()) {
            chars[i] = c;
            mapList.add(new HashMap(aci.getAttributes()));

        }
        simpleString = new String(chars);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.AttributedCharacterIterator#getRunLimit()
     */
    public int getRunLimit() {
        return aci.getRunLimit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.AttributedCharacterIterator#getRunStart()
     */
    public int getRunStart() {
        return aci.getRunStart();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.AttributedCharacterIterator#getRunLimit(java.text.AttributedCharacterIterator.Attribute)
     */
    public int getRunLimit(Attribute attribute) {
        // TODO ???
        return aci.getRunLimit(attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.AttributedCharacterIterator#getRunStart(java.text.AttributedCharacterIterator.Attribute)
     */
    public int getRunStart(Attribute attribute) {
        // TODO ???
        return aci.getRunStart(attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.AttributedCharacterIterator#getAttributes()
     */
    public Map getAttributes() {
        return (Map) mapList.get(aci.getIndex());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.AttributedCharacterIterator#getAllAttributeKeys()
     */
    public Set getAllAttributeKeys() {
        return allAttributes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.AttributedCharacterIterator#getRunLimit(java.util.Set)
     */
    public int getRunLimit(Set attributes) {
        // TODO ???
        return aci.getRunLimit(attributes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.AttributedCharacterIterator#getRunStart(java.util.Set)
     */
    public int getRunStart(Set attributes) {
        // TODO ???
        return aci.getRunStart(attributes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.AttributedCharacterIterator#getAttribute(java.text.AttributedCharacterIterator.Attribute)
     */
    public Object getAttribute(Attribute attribute) {
        return getAttributes().get(attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.CharacterIterator#current()
     */
    public char current() {
        return aci.current();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.CharacterIterator#first()
     */
    public char first() {
        return aci.first();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.CharacterIterator#last()
     */
    public char last() {
        return aci.last();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.CharacterIterator#next()
     */
    public char next() {
        return aci.next();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.CharacterIterator#previous()
     */
    public char previous() {
        return aci.previous();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.CharacterIterator#getBeginIndex()
     */
    public int getBeginIndex() {
        return aci.getBeginIndex();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.CharacterIterator#getEndIndex()
     */
    public int getEndIndex() {
        return aci.getEndIndex();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.CharacterIterator#getIndex()
     */
    public int getIndex() {
        return aci.getIndex();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.text.CharacterIterator#setIndex(int)
     */
    public char setIndex(int position) {
        return aci.setIndex(position);
    }

    /**
     * Create a copy of this iterator
     */
    public Object clone() {
        AttributedCharacterIterator cloneACI = new CharterAttributedCharacterIterator(
                this, frc);
        return cloneACI;
    }

}