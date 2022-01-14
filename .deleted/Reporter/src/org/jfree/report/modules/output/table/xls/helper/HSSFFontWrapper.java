/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * HSSFFontWrapper.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.xls.helper;

import java.awt.Color;

import org.apache.poi.hssf.usermodel.HSSFFont;

/**
 * The HSSFFontWrapper is used to store excel style font informations.
 *
 * @author Heiko Evermann
 */
public final class HSSFFontWrapper
{
  /**
   * scale between Excel and awt. With this value it looks fine.
   */
  public static final int FONT_FACTOR = 20;

  /**
   * the font name.
   */
  private final String fontName;

  /**
   * the excel color index.
   */
  private final short colorIndex;

  /**
   * the font size.
   */
  private final int fontHeight;

  /**
   * the font's bold flag.
   */
  private final boolean bold;

  /**
   * the font's italic flag.
   */
  private final boolean italic;

  /**
   * the font's underline flag.
   */
  private final boolean underline;

  /**
   * the font's strikethrough flag.
   */
  private final boolean strikethrough;

  /**
   * the cached hashcode.
   */
  private int hashCode;

  /**
   * Creates a new HSSFFontWrapper for the given font and color.
   *
   * @param font  the wrapped font.
   * @param color the foreground color.
   */
  public HSSFFontWrapper (final String fontName,
                          final short fontSize,
                          final boolean bold,
                          final boolean italic,
                          final boolean underline,
                          final boolean strikethrough,
                          final Color color)
  {
    if (fontName == null)
    {
      throw new NullPointerException("FontDefinition is null");
    }
    if (color == null)
    {
      throw new NullPointerException("Color is null");
    }

    if ("SansSerif".equalsIgnoreCase(fontName))
    {
      this.fontName = "Arial";
    }
    else if ("Monosspace".equalsIgnoreCase(fontName))
    {
      this.fontName = "Courier New";
    }
    else if ("Serif".equalsIgnoreCase(fontName))
    {
      this.fontName = "Times New Roman";
    }
    else
    {
      this.fontName = fontName;
    }

    this.colorIndex = ExcelColorSupport.getNearestColor(color);
    this.fontHeight = fontSize;
    this.bold = bold;
    this.italic = italic;
    this.underline = underline;
    this.strikethrough = strikethrough;
  }

  /**
   * Creates a HSSFFontWrapper for the excel font.
   *
   * @param font the font.
   */
  public HSSFFontWrapper(final HSSFFont font)
  {
    if (font == null)
    {
      throw new NullPointerException("Font is null");
    }
    fontName = font.getFontName();
    colorIndex = font.getColor();
    fontHeight = font.getFontHeightInPoints();
    italic = font.getItalic();
    bold = (font.getBoldweight() == HSSFFont.BOLDWEIGHT_BOLD);
    underline = (font.getUnderline() != HSSFFont.U_NONE);
    strikethrough = font.getStrikeout();
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param o the compared object.
   * @return true, if the font wrapper contains the same font definition, false
   *         otherwise.
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof HSSFFontWrapper))
    {
      return false;
    }

    final HSSFFontWrapper wrapper = (HSSFFontWrapper) o;

    if (bold != wrapper.bold)
    {
      return false;
    }
    if (underline != wrapper.strikethrough)
    {
      return false;
    }
    if (strikethrough != wrapper.strikethrough)
    {
      return false;
    }
    if (colorIndex != wrapper.colorIndex)
    {
      return false;
    }
    if (fontHeight != wrapper.fontHeight)
    {
      return false;
    }
    if (italic != wrapper.italic)
    {
      return false;
    }
    if (!fontName.equals(wrapper.fontName))
    {
      return false;
    }

    return true;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the benefit of
   * hashtables such as those provided by <code>java.util.Hashtable</code>.
   *
   * @return the hash code.
   */
  public int hashCode ()
  {
    if (hashCode == 0)
    {
      int result = fontName.hashCode();
      result = 29 * result + colorIndex;
      result = 29 * result + fontHeight;
      result = 29 * result + (bold ? 1 : 0);
      result = 29 * result + (italic ? 1 : 0);
      hashCode = result;
    }
    return hashCode;
  }

  public boolean isBold ()
  {
    return bold;
  }

  public short getColorIndex ()
  {
    return colorIndex;
  }

  public int getFontHeight ()
  {
    return fontHeight;
  }

  public String getFontName ()
  {
    return fontName;
  }

  public int getHashCode ()
  {
    return hashCode;
  }

  public boolean isItalic ()
  {
    return italic;
  }

  public boolean isStrikethrough ()
  {
    return strikethrough;
  }

  public boolean isUnderline ()
  {
    return underline;
  }
}
