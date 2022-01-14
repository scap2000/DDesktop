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
 * HSSFCellStyleProducer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.xls.helper;

import java.awt.Color;

import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.model.BorderEdge;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.modules.output.table.base.TableCellDefinition;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.style.TextWrap;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.Log;

/**
 * The cellstyle producer converts the JFreeReport content into excel cell
 * styles. This class is able to use the POI 2.0 features to build data cells.
 *
 * @author Thomas Morgner
 */
public class HSSFCellStyleProducer
{
  private static class HSSFCellStyleKey
  {
    /**
     * The cell background color.
     */
    private short color;

    /**
     * The top border's size.
     */
    private short borderStrokeTop;

    /**
     * The bottom border's size.
     */
    private short borderStrokeBottom;

    /**
     * The left border's size.
     */
    private short borderStrokeLeft;

    /**
     * The right border's size.
     */
    private short borderStrokeRight;

    /**
     * The top border's color.
     */
    private short colorTop;

    /**
     * The left border's color.
     */
    private short colorLeft;

    /**
     * The bottom border's color.
     */
    private short colorBottom;

    /**
     * The right border's color.
     */
    private short colorRight;

    /**
     * A flag indicating whether to enable excels word wrapping.
     */
    private boolean wrapText;

    /**
     * the horizontal text alignment.
     */
    private short horizontalAlignment;

    /**
     * the vertical text alignment.
     */
    private short verticalAlignment;

    /**
     * the font definition for the cell.
     */
    private short font;

    /**
     * the data style.
     */
    private short dataStyle;


    /**
     * @param background can be null
     * @param content can be null
     */
    protected HSSFCellStyleKey(final TableCellDefinition background,
                            final RenderBox content,
                            final HSSFDataFormat dataFormat,
                            final ExcelFontFactory fontFactory)
    {
      if (background != null)
      {
        if (background.getBackgroundColor() != null)
        {
          this.color = ExcelColorSupport.getNearestColor(background.getBackgroundColor());
        }
        final BorderEdge bottom = background.getBottom();
        this.colorBottom = ExcelColorSupport.getNearestColor(bottom.getColor());
        this.borderStrokeBottom = HSSFCellStyleProducer.translateStroke(bottom.getBorderStyle(), bottom.getWidth());

        final BorderEdge left = background.getLeft();
        this.colorLeft = ExcelColorSupport.getNearestColor(left.getColor());
        this.borderStrokeLeft = HSSFCellStyleProducer.translateStroke(left.getBorderStyle(), left.getWidth());

        final BorderEdge top = background.getLeft();
        this.colorTop = ExcelColorSupport.getNearestColor(top.getColor());
        this.borderStrokeTop = HSSFCellStyleProducer.translateStroke(top.getBorderStyle(), top.getWidth());

        final BorderEdge right = background.getRight();
        this.colorRight = ExcelColorSupport.getNearestColor(right.getColor());
        this.borderStrokeRight = HSSFCellStyleProducer.translateStroke(right.getBorderStyle(), right.getWidth());
      }

      if (content != null)
      {
        final StyleSheet styleSheet = content.getStyleSheet();
        final Color textColor = (Color) styleSheet.getStyleProperty(ElementStyleKeys.PAINT);
        final String fontName = (String) styleSheet.getStyleProperty(TextStyleKeys.FONT);
        final short fontSize = (short) styleSheet.getIntStyleProperty(TextStyleKeys.FONTSIZE, 0);
        final boolean bold = styleSheet.getBooleanStyleProperty(TextStyleKeys.BOLD);
        final boolean italic = styleSheet.getBooleanStyleProperty(TextStyleKeys.ITALIC);
        final boolean underline = styleSheet.getBooleanStyleProperty(TextStyleKeys.UNDERLINED);
        final boolean strikethrough = styleSheet.getBooleanStyleProperty(TextStyleKeys.STRIKETHROUGH);
        final HSSFFontWrapper wrapper = new HSSFFontWrapper
                (fontName, fontSize, bold, italic, underline, strikethrough, textColor);
        final HSSFFont excelFont = fontFactory.getExcelFont(wrapper);
        this.font = excelFont.getIndex();

        final ElementAlignment horizontal =
                (ElementAlignment) styleSheet.getStyleProperty(ElementStyleKeys.ALIGNMENT);
        this.horizontalAlignment = HSSFCellStyleProducer.convertAlignment(horizontal);
        final ElementAlignment vertical =
                (ElementAlignment) styleSheet.getStyleProperty(ElementStyleKeys.VALIGNMENT);
        this.verticalAlignment = HSSFCellStyleProducer.convertAlignment(vertical);
        final String dataStyle =
                (String) styleSheet.getStyleProperty(ElementStyleKeys.EXCEL_DATA_FORMAT_STRING);
        if (dataStyle != null)
        {
          this.dataStyle = dataFormat.getFormat(dataStyle);
        }
        this.wrapText = isWrapText(styleSheet);
      }
    }

    private boolean isWrapText (final StyleSheet styleSheet)
    {
      final Object excelWrap = styleSheet.getStyleProperty(ElementStyleKeys.EXCEL_WRAP_TEXT);
      if (excelWrap != null)
      {
        return Boolean.TRUE.equals(excelWrap);
      }
      return TextWrap.WRAP.equals(styleSheet.getStyleProperty(TextStyleKeys.TEXT_WRAP, TextWrap.WRAP));
    }

    protected HSSFCellStyleKey(final HSSFCellStyle style)
    {

      this.color = style.getFillForegroundColor();
      this.colorTop = style.getTopBorderColor();
      this.colorLeft = style.getLeftBorderColor();
      this.colorBottom = style.getBottomBorderColor();
      this.colorRight = style.getRightBorderColor();
      this.borderStrokeTop = style.getBorderTop();
      this.borderStrokeLeft = style.getBorderLeft();
      this.borderStrokeBottom = style.getBorderBottom();
      this.borderStrokeRight = style.getBorderRight();

      this.dataStyle = style.getDataFormat();
      this.font = style.getFontIndex();
      this.horizontalAlignment = style.getAlignment();
      this.verticalAlignment = style.getVerticalAlignment();
      this.wrapText = style.getWrapText();
    }

    public boolean equals(final Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      final HSSFCellStyleKey that = (HSSFCellStyleKey) o;

      if (borderStrokeBottom != that.borderStrokeBottom)
      {
        return false;
      }
      if (borderStrokeLeft != that.borderStrokeLeft)
      {
        return false;
      }
      if (borderStrokeRight != that.borderStrokeRight)
      {
        return false;
      }
      if (borderStrokeTop != that.borderStrokeTop)
      {
        return false;
      }
      if (color != that.color)
      {
        return false;
      }
      if (colorBottom != that.colorBottom)
      {
        return false;
      }
      if (colorLeft != that.colorLeft)
      {
        return false;
      }
      if (colorRight != that.colorRight)
      {
        return false;
      }
      if (colorTop != that.colorTop)
      {
        return false;
      }
      if (dataStyle != that.dataStyle)
      {
        return false;
      }
      if (font != that.font)
      {
        return false;
      }
      if (horizontalAlignment != that.horizontalAlignment)
      {
        return false;
      }
      if (verticalAlignment != that.verticalAlignment)
      {
        return false;
      }
      if (wrapText != that.wrapText)
      {
        return false;
      }

      return true;
    }

    public int hashCode()
    {
      int result = (int) color;
      result = 29 * result + (int) borderStrokeTop;
      result = 29 * result + (int) borderStrokeBottom;
      result = 29 * result + (int) borderStrokeLeft;
      result = 29 * result + (int) borderStrokeRight;
      result = 29 * result + (int) colorTop;
      result = 29 * result + (int) colorLeft;
      result = 29 * result + (int) colorBottom;
      result = 29 * result + (int) colorRight;
      result = 29 * result + (wrapText ? 1 : 0);
      result = 29 * result + (int) horizontalAlignment;
      result = 29 * result + (int) verticalAlignment;
      result = 29 * result + (int) font;
      result = 29 * result + (int) dataStyle;
      return result;
    }

    public short getColor()
    {
      return color;
    }

    public short getBorderStrokeTop()
    {
      return borderStrokeTop;
    }

    public short getBorderStrokeBottom()
    {
      return borderStrokeBottom;
    }

    public short getBorderStrokeLeft()
    {
      return borderStrokeLeft;
    }

    public short getBorderStrokeRight()
    {
      return borderStrokeRight;
    }

    public short getColorTop()
    {
      return colorTop;
    }

    public short getColorLeft()
    {
      return colorLeft;
    }

    public short getColorBottom()
    {
      return colorBottom;
    }

    public short getColorRight()
    {
      return colorRight;
    }

    public boolean isWrapText()
    {
      return wrapText;
    }

    public short getHorizontalAlignment()
    {
      return horizontalAlignment;
    }

    public short getVerticalAlignment()
    {
      return verticalAlignment;
    }

    public short getFont()
    {
      return font;
    }

    public short getDataStyle()
    {
      return dataStyle;
    }
  }

  /**
   * The workbook wide singleton instance of an empty cell.
   */
  private HSSFCellStyle emptyCellStyle;

  /**
   * the font factory is used to create excel fonts.
   */
  private ExcelFontFactory fontFactory;

  /**
   * The workbook, which creates all cells and styles.
   */
  private HSSFWorkbook workbook;

  /**
   * The data format is used to create format strings.
   */
  private HSSFDataFormat dataFormat;

  /**
   * White background. This is the default background if not specified
   * otherwise.
   */
  private static final short WHITE_INDEX = (new HSSFColor.WHITE()).getIndex();

  /**
   * The cache for all generated styles.
   */
  private HashMap styleCache;

  private boolean warningDone;
  private boolean hardLimit;

  /**
   * The class does the dirty work of creating the HSSF-objects.
   *
   * @param workbook the workbook for which the styles should be created.
   */
  public HSSFCellStyleProducer(final HSSFWorkbook workbook,
                               final boolean hardLimit)
  {
    if (workbook == null)
    {
      throw new NullPointerException();
    }
    this.styleCache = new HashMap();
    this.workbook = workbook;
    this.fontFactory = new ExcelFontFactory(workbook);
    this.dataFormat = workbook.createDataFormat();
    this.hardLimit = hardLimit;

    // Read in the styles ...
    final short predefinedStyles = workbook.getNumCellStyles();
    for (short i = 0; i < predefinedStyles; i++)
    {
      final HSSFCellStyle cellStyleAt = workbook.getCellStyleAt(i);
      this.styleCache.put(new HSSFCellStyleKey(cellStyleAt), cellStyleAt);
    }
  }

  /**
   * Gets the default style, which is used for empty cells.
   *
   * @return the default style for empty cells.
   */
  public HSSFCellStyle getEmptyCellStyle()
  {
    if (emptyCellStyle == null)
    {
      emptyCellStyle = workbook.createCellStyle();
      emptyCellStyle.setFillForegroundColor(HSSFCellStyleProducer.WHITE_INDEX);
      emptyCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }
    return emptyCellStyle;
  }

  /**
     * Creates a HSSFCellStyle based on the given ExcelDataCellStyle. If a
     * similiar cell style was previously generated, then reuse that cached
     * result.
     *
     * @param element never null
     * @param bg the background style for the destTable cell.
     * @return the generated or cached HSSFCellStyle.
     */
  public HSSFCellStyle createCellStyle(final RenderBox element,
                                       final TableCellDefinition bg)
  {
    // check, whether that style is already created
    final HSSFCellStyleKey styleKey =
            new HSSFCellStyleKey(bg, element, dataFormat, fontFactory);
    if (styleCache.containsKey(styleKey))
    {
      return (HSSFCellStyle) styleCache.get(styleKey);
    }

    if ((styleCache.size()) > 4000)
    {
      if (warningDone == false)
      {
        Log.warn("HSSFCellStyleProducer has reached the limit of 4000 created styles.");
        warningDone = true;
      }
      if (hardLimit)
      {
        Log.warn("HSSFCellStyleProducer will not create more styles. New cells will not have any style.");
        return null;
      }
    }
    final HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
    if (element != null)
    {
      hssfCellStyle.setAlignment(styleKey.getHorizontalAlignment());
      hssfCellStyle.setVerticalAlignment(styleKey.getVerticalAlignment());
      hssfCellStyle.setFont(workbook.getFontAt(styleKey.getFont()));
      hssfCellStyle.setWrapText(styleKey.isWrapText());
      hssfCellStyle.setDataFormat(styleKey.getDataStyle());
    }
    if (bg != null)
    {
      if (BorderStyle.NONE.equals(bg.getBottom().getBorderStyle()) == false)
      {
        hssfCellStyle.setBorderBottom(styleKey.getBorderStrokeBottom());
        hssfCellStyle.setBottomBorderColor(styleKey.getColorBottom());
      }
      if (BorderStyle.NONE.equals(bg.getTop().getBorderStyle()) == false)
      {
        hssfCellStyle.setBorderTop(styleKey.getBorderStrokeTop());
        hssfCellStyle.setTopBorderColor(styleKey.getColorTop());
      }
      if (BorderStyle.NONE.equals(bg.getLeft().getBorderStyle()) == false)
      {
        hssfCellStyle.setBorderLeft(styleKey.getBorderStrokeLeft());
        hssfCellStyle.setLeftBorderColor(styleKey.getColorLeft());
      }
      if (BorderStyle.NONE.equals(bg.getRight().getBorderStyle()) == false)
      {
        hssfCellStyle.setBorderRight(styleKey.getBorderStrokeRight());
        hssfCellStyle.setRightBorderColor(styleKey.getColorRight());
      }
      if (bg.getBackgroundColor() != null)
      {
        hssfCellStyle.setFillForegroundColor(styleKey.getColor());
        hssfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      }
    }
    

    styleCache.put(styleKey, hssfCellStyle);
    return hssfCellStyle;
  }

  /**
   * Converts the given element alignment into one of the
   * HSSFCellStyle-constants.
   *
   * @param e the JFreeReport element alignment.
   * @return the HSSFCellStyle-Alignment.
   * @throws IllegalArgumentException if an Unknown JFreeReport alignment is
   *                                  given.
   */
  private static short convertAlignment(final ElementAlignment e)
  {
    if (e == ElementAlignment.LEFT)
    {
      return HSSFCellStyle.ALIGN_LEFT;
    }
    if (e == ElementAlignment.RIGHT)
    {
      return HSSFCellStyle.ALIGN_RIGHT;
    }
    if (e == ElementAlignment.CENTER)
    {
      return HSSFCellStyle.ALIGN_CENTER;
    }
    if (e == ElementAlignment.TOP)
    {
      return HSSFCellStyle.VERTICAL_TOP;
    }
    if (e == ElementAlignment.BOTTOM)
    {
      return HSSFCellStyle.VERTICAL_BOTTOM;
    }
    if (e == ElementAlignment.MIDDLE)
    {
      return HSSFCellStyle.VERTICAL_CENTER;
    }

    throw new IllegalArgumentException("Invalid alignment");
  }

  /**
   * Tries to translate the given stroke width into one of the predefined excel
   * border styles.
   *
   * @param widthRaw the AWT-Stroke-Width.
   * @return the translated excel border width.
   */
  private static short translateStroke(final BorderStyle borderStyle, final long widthRaw)
  {
    final double width = StrictGeomUtility.toExternalValue(widthRaw);
    if (BorderStyle.NONE.equals(borderStyle))
    {
      return HSSFCellStyle.BORDER_NONE;
    }
    if (BorderStyle.DASHED.equals(borderStyle))
    {
      if (width <= 1.5)
      {
        return HSSFCellStyle.BORDER_DASHED;
      }
      else
      {
        return HSSFCellStyle.BORDER_MEDIUM_DASHED;
      }
    }
    if (BorderStyle.DOT_DOT_DASH.equals(borderStyle))
    {
      if (width <= 1.5)
      {
        return HSSFCellStyle.BORDER_DASH_DOT_DOT;
      }
      else
      {
        return HSSFCellStyle.BORDER_MEDIUM_DASH_DOT_DOT;
      }
    }
    if (BorderStyle.DOT_DASH.equals(borderStyle))
    {
      if (width <= 1.5)
      {
        return HSSFCellStyle.BORDER_DASH_DOT;
      }
      else
      {
        return HSSFCellStyle.BORDER_MEDIUM_DASH_DOT;
      }
    }
    if (BorderStyle.DOTTED.equals(borderStyle))
    {
      return HSSFCellStyle.BORDER_DOTTED;
    }
    if (BorderStyle.DOUBLE.equals(borderStyle))
    {
      return HSSFCellStyle.BORDER_DOUBLE;
    }

    if (width == 0)
    {
      return HSSFCellStyle.BORDER_NONE;
    }
    else if (width <= 0.5)
    {
      return HSSFCellStyle.BORDER_HAIR;
    }
    else if (width <= 1)
    {
      return HSSFCellStyle.BORDER_THIN;
    }
    else if (width <= 1.5)
    {
      return HSSFCellStyle.BORDER_MEDIUM;
    }
//    else if (width <= 2)
//    {
//      return HSSFCellStyle.BORDER_DOUBLE;
//    }
    else
    {
      return HSSFCellStyle.BORDER_THICK;
    }
  }

  public ExcelFontFactory getFontFactory()
  {
    return fontFactory;
  }
}
