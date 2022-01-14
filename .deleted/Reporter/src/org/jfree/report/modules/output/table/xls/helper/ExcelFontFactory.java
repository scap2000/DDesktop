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
 * ExcelFontFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.xls.helper;

import java.awt.Color;

import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This class keeps track of all fonts that we have used so far in our Excel file.
 * <p/>
 * Excel fonts should never be created directly, as excel does not like the idea of having
 * too many font definitions.
 *
 * @author Heiko Evermann
 */
public class ExcelFontFactory
{
  /**
   * The list of fonts that we have used so far.
   */
  private HashMap fonts;

  /**
   * The workbook that is used to create the font.
   */
  private final HSSFWorkbook workbook;

  /**
   * Constructor for ExcelFontFactory.
   *
   * @param workbook the workbook.
   */
  public ExcelFontFactory (final HSSFWorkbook workbook)
  {
    this.fonts = new HashMap();
    this.workbook = workbook;

    // read the fonts from the workbook ...
    // Funny one: Please note that the layout will be broken if the first
    // font is not 'Arial 10'.
    final short numberOfFonts = this.workbook.getNumberOfFonts();
    for (int i = 0; i < numberOfFonts; i++)
    {
      final HSSFFont font = workbook.getFontAt((short) i);
      this.fonts.put(new HSSFFontWrapper(font), font);
    }

    // add the default font
    // this MUST be the first one, that is created.
    // oh, I hate Excel ...
    final HSSFFontWrapper wrapper = new HSSFFontWrapper
        ("Arial", (short) 10, false, false, false, false, Color.black);
    getExcelFont(wrapper);
  }

  /**
   * Creates a HSSFFont. The created font is cached and reused later, if a similiar font
   * is requested.
   *
   * @param wrapper the font information that should be used to produce the excel font
   * @return the created or a cached HSSFFont instance
   */
  public HSSFFont getExcelFont (final HSSFFontWrapper wrapper)
  {
    if (fonts.containsKey(wrapper))
    {
      return (HSSFFont) fonts.get(wrapper);
    }

    // ok, we need a new one ...
    final HSSFFont excelFont = createFont(wrapper);
    fonts.put(wrapper, excelFont);
    return excelFont;
  }

  /**
   * Returns the excel font stored in this wrapper.
   *
   * @param wrapper the font wrapper that holds all font information from
   * the repagination.
   * @return the created font.
   */
  private HSSFFont createFont (final HSSFFontWrapper wrapper)
  {
    final HSSFFont font = workbook.createFont();
    if (wrapper.isBold())
    {
      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    }
    else
    {
      font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    }
    font.setColor(wrapper.getColorIndex());
    font.setFontName(wrapper.getFontName());
    font.setFontHeightInPoints((short) wrapper.getFontHeight());
    font.setItalic(wrapper.isItalic());
    font.setStrikeout(wrapper.isStrikethrough());
    if (wrapper.isUnderline())
    {
      font.setUnderline(HSSFFont.U_SINGLE);
    }
    else
    {
      font.setUnderline(HSSFFont.U_NONE);
    }
    return font;
  }

}
