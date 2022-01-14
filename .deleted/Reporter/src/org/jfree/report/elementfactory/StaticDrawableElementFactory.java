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
 * StaticDrawableElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.elementfactory;

import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.ui.Drawable;

/**
 * Creates an element that displays a predefined (static) drawable object.
 *
 * @author Thomas Morgner
 */
public class StaticDrawableElementFactory extends ElementFactory
{
  /** The drawable that should be displayed. */
  private Drawable content;

  /**
   * DefaultConstructor.
   */
  public StaticDrawableElementFactory ()
  {
  }

  /**
   * Returns the field name from where to read the content of the element.
   *
   * @return the field name.
   */
  public Drawable getContent ()
  {
    return content;
  }

  /**
     * Defines the field name from where to read the content of the element. The field name
     * is the name of a datarow destColumn.
     *
     * @param content the field name.
     */
  public void setContent (final Drawable content)
  {
    this.content = content;
  }

  /**
   * Creates a new drawable field element based on the defined properties.
   *
   * @return the generated elements
   *
   * @throws IllegalStateException if the field name is not set.
   * @see ElementFactory#createElement()
   */
  public Element createElement ()
  {
    if (getContent() == null)
    {
      throw new IllegalStateException("Fieldname is not set.");
    }

    final DrawableElement element = new DrawableElement();
    applyElementName(element);
    applyStyle(element.getStyle());

    final StaticDataSource dataSource = new StaticDataSource(content);
    element.setDataSource(dataSource);

    return element;
  }

}
