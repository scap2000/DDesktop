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
 * NumberFieldElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.TextElement;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.NumberFormatFilter;
import org.jfree.report.filter.templates.NumberFieldTemplate;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.FontDefinition;

/**
 * The number format factory can be used to create numeric text elements. These text
 * elements have special abilities to format numeric values.
 * <p/>
 * Once the desired properties are set, the factory can be reused to create similiar text
 * elements.
 *
 * @author Thomas Morgner
 */
public class NumberFieldElementFactory extends TextFieldElementFactory
{
  /**
   * The default format pattern that mimics the Integer.toString() result.
   */
  private static final String DECIMALFORMAT_DEFAULT_PATTERN =
    "#,###.###################################################" +
    "#########################################################" +
    "#########################################################" +
    "#########################################################" +
    "#########################################################" +
    "#########################################################" +
    "####";

  /**
   * The number format instance used to format numeric values in the text element.
   */
  private NumberFormat format;
  /** The cell format for the excel export. */
  private String excelCellFormat;

  /**
   * Creates a new number field element factory.
   */
  public NumberFieldElementFactory ()
  {
  }

  /**
   * Returns the excel export cell format.
   *
   * @return the excel cell format.
   */
  public String getExcelCellFormat ()
  {
    return excelCellFormat;
  }

  /**
   * Defines a special cell format that should be used when exporting the report
   * into Excel workbooks.
   *
   * @param excelCellFormat the excel cell format
   */
  public void setExcelCellFormat (final String excelCellFormat)
  {
    this.excelCellFormat = excelCellFormat;
  }

  /**
   * Returns the number format used for all generated text elements. The number format is
   * shared among all generated elements.
   *
   * @return the number format used in this factory.
   */
  public NumberFormat getFormat ()
  {
    return format;
  }

  /**
   * Defines the number format used for all generated text elements. The number format is
   * shared among all generated elements.
   *
   * @param format the number format used in this factory.
   */
  public void setFormat (final NumberFormat format)
  {
    this.format = format;
  }

  /**
   * Returns the format string of the used number format. This method will return null, if
   * the current number format is no instance of DecimalFormat.
   *
   * @return the formatstring of the number format instance.
   */
  public String getFormatString ()
  {
    if (getFormat() instanceof DecimalFormat)
    {
      final DecimalFormat decFormat = (DecimalFormat) getFormat();
      return decFormat.toPattern();
    }
    return null;
  }

  /**
   * Defines the format string of the used number format. This method will replace the
   * number format instance of this factory.
   *
   * @param formatString the formatstring of the number format instance.
   */
  public void setFormatString (final String formatString)
  {
    if (formatString == null)
    {
      format = null;
    }
    else
    {
      if (formatString.length() == 0)
      {
        // this is a workaround for a bug in JDK 1.5
        setFormat(new DecimalFormat(DECIMALFORMAT_DEFAULT_PATTERN));
      }
      else
      {
        setFormat(new DecimalFormat(formatString));
      }
    }
  }

  /**
   * Creates the number text element based on the defined settings. Undefined properties
   * will not be set in the generated element.
   *
   * @return the generated numberic text element
   *
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final DataSource ds;
    if (format instanceof DecimalFormat)
    {
      final NumberFieldTemplate template = new NumberFieldTemplate();
      template.setDecimalFormat((DecimalFormat) format);
      if (getFormula() != null)
      {
        template.setFormula(getFormula());
      }
      else
      {
        template.setField(getFieldname());
      }
      if (getNullString() != null)
      {
        template.setNullValue(getNullString());
      }
      ds = template;
    }
    else
    {
      final NumberFormatFilter dataSource = new NumberFormatFilter();
      if (format != null)
      {
        dataSource.setFormatter(format);
      }
      final DataRowDataSource dds = new DataRowDataSource();
      if (getFormula() != null)
      {
        dds.setFormula(getFormula());
      }
      else
      {
        dds.setDataSourceColumnName(getFieldname());
      }

      dataSource.setDataSource(dds);
      if (getNullString() != null)
      {
        dataSource.setNullValue(getNullString());
      }
      ds = dataSource;
    }

    final TextElement element = new TextElement();
    applyElementName(element);
    element.setDataSource(ds);

    applyStyle(element.getStyle());
    element.getStyle().setStyleProperty(ElementStyleKeys.EXCEL_DATA_FORMAT_STRING, getExcelCellFormat());
    return element;
  }


  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name       the name of the new element
   * @param bounds     the bounds of the new element
   * @param paint      the text color of this text element
   * @param alignment  the horizontal text alignment.
   * @param font       the font for this element
   * @param nullString the text used when the value of this element is null
   * @param field      the field in the datamodel to retrieve values from
   * @param format     the NumberFormat used in this number element
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException     if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createNumberElement (final String name,
                                                 final Rectangle2D bounds,
                                                 final Color paint,
                                                 final ElementAlignment alignment,
                                                 final FontDefinition font,
                                                 final String nullString,
                                                 final NumberFormat format,
                                                 final String field)
  {
    return createNumberElement(name, bounds, paint, alignment,
            ElementAlignment.TOP,
            font, nullString,
            format, field);
  }

  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name       the name of the new element.
   * @param bounds     the bounds of the new element.
   * @param color      the text color of this text element.
   * @param alignment  the horizontal text alignment.
   * @param valign     the vertical alignment.
   * @param font       the font for this element.
   * @param nullString the text used when the value of this element is null.
   * @param field      the field in the datamodel to retrieve values from.
   * @param format     the NumberFormat used in this number element.
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException     if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createNumberElement (final String name,
                                                 final Rectangle2D bounds,
                                                 final Color color,
                                                 final ElementAlignment alignment,
                                                 final ElementAlignment valign,
                                                 final FontDefinition font,
                                                 final String nullString,
                                                 final NumberFormat format,
                                                 final String field)
  {

    final NumberFieldElementFactory factory = new NumberFieldElementFactory();
    factory.setX(new Float(bounds.getX()));
    factory.setY(new Float(bounds.getY()));
    factory.setMinimumWidth(new Float(bounds.getWidth()));
    factory.setMinimumHeight(new Float(bounds.getHeight()));
    factory.setName(name);
    factory.setColor(color);
    factory.setHorizontalAlignment(alignment);
    factory.setVerticalAlignment(valign);

    if (font != null)
    {
      factory.setFontName(font.getFontName());
      factory.setFontSize(new Integer(font.getFontSize()));
      factory.setBold(ElementFactory.getBooleanValue(font.isBold()));
      factory.setItalic(ElementFactory.getBooleanValue(font.isItalic()));
      factory.setEncoding(font.getFontEncoding(null));
      factory.setUnderline(ElementFactory.getBooleanValue(font.isUnderline()));
      factory.setStrikethrough(ElementFactory.getBooleanValue(font.isStrikeThrough()));
      factory.setEmbedFont(ElementFactory.getBooleanValue(font.isEmbeddedFont()));
    }
    factory.setNullString(nullString);
    factory.setFormat(format);
    factory.setFieldname(field);
    return (TextElement) factory.createElement();
  }

  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name       the name of the new element
   * @param bounds     the bounds of the new element
   * @param paint      the text color of this text element
   * @param alignment  the horizontal text alignment.
   * @param font       the font for this element
   * @param nullString the text used when the value of this element is null
   * @param field      the fieldname in the datamodel to retrieve values from
   * @param format     the DecimalFormatString used in this text field
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException     if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createNumberElement (final String name,
                                                 final Rectangle2D bounds,
                                                 final Color paint,
                                                 final ElementAlignment alignment,
                                                 final FontDefinition font,
                                                 final String nullString,
                                                 final String format,
                                                 final String field)
  {
    return createNumberElement(name, bounds, paint, alignment,
            null,
            font, nullString,
            format, field);
  }

  /**
   * Creates a new TextElement containing a numeric filter structure.
   *
   * @param name       the name of the new element.
   * @param bounds     the bounds of the new element.
   * @param color      the text color of the element.
   * @param alignment  the horizontal text alignment.
   * @param valign     the vertical alignment.
   * @param font       the font for this element.
   * @param nullString t he text used when the value of this element is null.
   * @param field      the fieldname in the datamodel to retrieve values from.
   * @param format     the DecimalFormatString used in this text field.
   * @return a report element for displaying <code>Number</code> objects.
   *
   * @throws NullPointerException     if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createNumberElement (final String name,
                                                 final Rectangle2D bounds,
                                                 final Color color,
                                                 final ElementAlignment alignment,
                                                 final ElementAlignment valign,
                                                 final FontDefinition font,
                                                 final String nullString,
                                                 final String format,
                                                 final String field)
  {

    final NumberFieldElementFactory factory = new NumberFieldElementFactory();
    factory.setX(new Float(bounds.getX()));
    factory.setY(new Float(bounds.getY()));
    factory.setMinimumWidth(new Float(bounds.getWidth()));
    factory.setMinimumHeight(new Float(bounds.getHeight()));
    factory.setName(name);
    factory.setColor(color);
    factory.setHorizontalAlignment(alignment);
    factory.setVerticalAlignment(valign);

    if (font != null)
    {
      factory.setFontName(font.getFontName());
      factory.setFontSize(new Integer(font.getFontSize()));
      factory.setBold(ElementFactory.getBooleanValue(font.isBold()));
      factory.setItalic(ElementFactory.getBooleanValue(font.isItalic()));
      factory.setEncoding(font.getFontEncoding(null));
      factory.setUnderline(ElementFactory.getBooleanValue(font.isUnderline()));
      factory.setStrikethrough(ElementFactory.getBooleanValue(font.isStrikeThrough()));
      factory.setEmbedFont(ElementFactory.getBooleanValue(font.isEmbeddedFont()));
    }
    factory.setNullString(nullString);
    factory.setFormatString(format);
    factory.setFieldname(field);
    return (TextElement) factory.createElement();
  }

}
