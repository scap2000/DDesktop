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
 * StaticDrawableURLElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.elementfactory;

import java.net.URL;

import org.jfree.report.DrawableElement;
import org.jfree.report.Element;
import org.jfree.report.filter.templates.DrawableURLElementTemplate;

/**
 * A factory to create an drawable element that loads its drawable from an static URL. The
 * content string may contain a relative URL if an valid base URL is given.
 *
 * @author Thomas Morgner
 */
public class StaticDrawableURLElementFactory extends ElementFactory
{
  /**
   * The content URL (may be a relative URL).
   */
  private String content;
  /**
   * The base URL for relative content URLs.
   */
  private URL baseURL;

  /**
   * DefaultConstructor.
   */
  public StaticDrawableURLElementFactory ()
  {
  }

  /**
   * Returns the content part of the URL. This string may contain a relative URL, if the
   * base URL is defined.
   *
   * @return the content part of the URL.
   */
  public String getContent ()
  {
    return content;
  }

  /**
   * Defines the content part of the URL. This string may contain a relative URL, if the
   * base URL is defined.
   *
   * @param content the content part of the URL.
   */
  public void setContent (final String content)
  {
    this.content = content;
  }

  /**
   * Returns the BaseURL. The base URL is used to build the complete URL if the content
   * url is relative.
   *
   * @return the base URL.
   */
  public URL getBaseURL ()
  {
    return baseURL;
  }

  /**
   * Defines the base URL.
   *
   * @param baseURL the base URL.
   */
  public void setBaseURL (final URL baseURL)
  {
    this.baseURL = baseURL;
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

    final DrawableURLElementTemplate fieldTemplate = new DrawableURLElementTemplate();
    fieldTemplate.setContent(getContent());
    fieldTemplate.setBaseURL(getBaseURL());
    element.setDataSource(fieldTemplate);

    return element;
  }

}
