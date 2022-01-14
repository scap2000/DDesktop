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
 * DrawableElement.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

/**
 * The element that returns a drawable object. Drawable elements always scale without obeying to an aspect ratio. It is
 * the resposibility of the drawable implementation to implement scaling if needed.
 *
 * @author Thomas Morgner
 */
public class DrawableElement extends Element
{
  /**
   * The content type for the element.
   */
  public static final String CONTENT_TYPE = "drawable/generic";

  /**
   * Default constructor.
   */
  public DrawableElement()
  {
  }

  /**
   * Defines the content-type for this element.
   *
   * @return the content-type as string.
   * @deprecated we no longer use the content-type.
   */
  public String getContentType()
  {
    return DrawableElement.CONTENT_TYPE;
  }
}
