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
 * TextFieldElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.TextElement;
import org.jfree.report.filter.templates.StringFieldTemplate;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;

/**
 * A factory to define text fields. Text fields read their content from the dataRow and
 * try to print it as plain text (using toString() if required).
 *
 * @author Thomas Morgner
 */
public class TextFieldElementFactory extends TextElementFactory
{
  /**
   * The fieldname of the datarow from where to read the content.
   */
  private String fieldname;

  /**
   * The nullstring of the text element if the value in the datasource was null.
   */
  private String nullString;

  /**
   * The value-formula that computes the value for this element.
   */
  private String formula;

  /**
   * DefaultConstructor.
   */
  public TextFieldElementFactory ()
  {
  }

  /**
   * Returns the field name from where to read the content of the element.
   *
   * @return the field name.
   */
  public String getFieldname ()
  {
    return fieldname;
  }

  /**
     * Defines the field name from where to read the content of the element. The field name
     * is the name of a datarow destColumn.
     *
     * @param fieldname the field name.
     */
  public void setFieldname (final String fieldname)
  {
    this.fieldname = fieldname;
  }

  /**
   * Returns the formula that should be used to compute the value of the field.
   * The formula must be valid according to the OpenFormula
   * specifications.
   *
   * @return the formula as string.
   */
  public String getFormula()
  {
    return formula;
  }

  /**
   * Assigns a formula to the element to compute the value for this element.
   * If a formula is defined, it will override the 'field' property.
   *
   * @param formula the formula as a string.
   */
  public void setFormula(final String formula)
  {
    this.formula = formula;
  }

  /**
   * Returns the null string for the text element. The null string is used when no content
   * is found for that element.
   *
   * @return the null string.
   */
  public String getNullString ()
  {
    return nullString;
  }

  /**
   * Defines the null string for the text element. The null string is used when no content
   * is found for that element. The nullstring itself can be null.
   *
   * @param nullString the null string.
   */
  public void setNullString (final String nullString)
  {
    this.nullString = nullString;
  }

  /**
   * Creates the text field element.
   *
   * @return the generated text field element
   *
   * @throws IllegalStateException if the fieldname is null.
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final StringFieldTemplate template = new StringFieldTemplate();
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

    final TextElement element = new TextElement();
    applyElementName(element);
    element.setDataSource(template);
    final ElementStyleSheet style = element.getStyle();
    applyStyle(style);
    return element;
  }

  /**
   * Creates a new TextElement without any additional filtering.
   *
   * @param name       the name of the new element
   * @param bounds     the bounds of the new element
   * @param paint      the text color of this text element
   * @param alignment  the horizontal text alignment.
   * @param font       the font for this element
   * @param nullString the text used when the value of this element is null
   * @param field      the field in the datamodel to retrieve values from
   * @return a report element for displaying <code>String</code> objects.
   *
   * @throws NullPointerException     if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createStringElement (final String name,
                                                 final Rectangle2D bounds,
                                                 final Color paint,
                                                 final ElementAlignment alignment,
                                                 final FontDefinition font,
                                                 final String nullString,
                                                 final String field)
  {
    return createStringElement(name, bounds, paint, alignment,
            ElementAlignment.TOP, font, nullString, field);
  }

  /**
   * Creates a new TextElement without any additional filtering.
   *
   * @param name       the name of the new element
   * @param bounds     the bounds of the new element
   * @param paint      the text color of this text element
   * @param alignment  the horizontal text alignment.
   * @param valign     the vertical alignment.
   * @param font       the font for this element
   * @param nullString the text used when the value of this element is null
   * @param field      the field in the datamodel to retrieve values from
   * @return a report element for displaying <code>String</code> objects.
   *
   * @throws NullPointerException     if bounds, name or function are null
   * @throws IllegalArgumentException if the given alignment is invalid
   */
  public static TextElement createStringElement (final String name,
                                                 final Rectangle2D bounds,
                                                 final Color paint,
                                                 final ElementAlignment alignment,
                                                 final ElementAlignment valign,
                                                 final FontDefinition font,
                                                 final String nullString,
                                                 final String field)
  {
    final TextFieldElementFactory factory = new TextFieldElementFactory();
    factory.setX(new Float(bounds.getX()));
    factory.setY(new Float(bounds.getY()));
    factory.setMinimumWidth(new Float(bounds.getWidth()));
    factory.setMinimumHeight(new Float(bounds.getHeight()));
    factory.setName(name);
    factory.setColor(paint);
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
    factory.setFieldname(field);
    factory.setNullString(nullString);
    return (TextElement) factory.createElement();
  }


}
