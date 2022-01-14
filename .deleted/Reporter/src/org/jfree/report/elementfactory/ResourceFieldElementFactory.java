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
 * ResourceFieldElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.elementfactory;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.jfree.report.Element;
import org.jfree.report.ElementAlignment;
import org.jfree.report.TextElement;
import org.jfree.report.filter.templates.ResourceFieldTemplate;
import org.jfree.report.style.FontDefinition;

/**
 * A factory to define ResourceFieldElements. ResourceField translate their content using
 * a ResourceBundle instance.
 *
 * @author Thomas Morgner
 */
public class ResourceFieldElementFactory extends TextFieldElementFactory
{
  /**
   * The resource base from which to read the translations.
   */
  private String resourceBase;

  /**
   * Default Constructor.
   */
  public ResourceFieldElementFactory ()
  {
  }

  /**
   * Returns the base name of the resource bundle used to translate the content later.
   *
   * @return the resource bundle name of the element.
   */
  public String getResourceBase ()
  {
    return resourceBase;
  }

  /**
   * Defines the base name of the resource bundle used to translate the content later.
   *
   * @param resourceBase the resource bundle name of the element.
   */
  public void setResourceBase (final String resourceBase)
  {
    this.resourceBase = resourceBase;
  }

  /**
   * Creates the resource field element based on the set properties.
   *
   * @return the generated element.
   *
   * @throws IllegalStateException if the fieldname is not defined.
   * @see org.jfree.report.elementfactory.ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getFieldname() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final ResourceFieldTemplate template = new ResourceFieldTemplate();
    template.setResourceIdentifier(getResourceBase());
    if (getFormula() != null)
    {
      template.setFormula(getFormula());
    }
    else
    {
      template.setField(getFieldname());
    }
    template.setNullValue(getNullString());

    final TextElement element = new TextElement();
    applyElementName(element);
    element.setDataSource(template);

    applyStyle(element.getStyle());
    return element;
  }

  /**
   * creates a ResourceElement. ResourceElements resolve their value using a
   * <code>java.util.ResourceBundle</code>.
   *
   * @param name         the name of the element (null allowed)
   * @param bounds       the element's bounds
   * @param color        the text color of the element
   * @param alignment    the element's horizontal text alignment
   * @param valignment   the element's vertical text alignment
   * @param font         the elements font
   * @param nullValue    the text used when the value of this element is null
   * @param field        the field in the datamodel to retrieve values from
   * @param resourceBase the classname/basename of the assigned resource bundle
   * @return the created ResourceElement
   */
  public static TextElement createResourceElement (final String name,
                                                   final Rectangle2D bounds,
                                                   final Color color,
                                                   final ElementAlignment alignment,
                                                   final ElementAlignment valignment,
                                                   final FontDefinition font,
                                                   final String nullValue,
                                                   final String resourceBase,
                                                   final String field)
  {
    final ResourceFieldElementFactory factory = new ResourceFieldElementFactory();
    factory.setX(new Float(bounds.getX()));
    factory.setY(new Float(bounds.getY()));
    factory.setMinimumWidth(new Float(bounds.getWidth()));
    factory.setMinimumHeight(new Float(bounds.getHeight()));
    factory.setName(name);
    factory.setColor(color);
    factory.setHorizontalAlignment(alignment);
    factory.setVerticalAlignment(valignment);

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
    factory.setNullString(nullValue);
    factory.setResourceBase(resourceBase);
    factory.setFieldname(field);
    return (TextElement) factory.createElement();
  }


}
