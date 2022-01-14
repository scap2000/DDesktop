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
 * ImageElement.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import org.jfree.report.style.ElementStyleKeys;

/**
 * Used to draw images. References to the Images must be given as ImageContainer. If you
 * use the <code>ImageElementFactory</code> implementations, the necessary wrapping is
 * done for you, if needed.
 * <p/>
 *
 * @author Thomas Morgner
 */
public class ImageElement extends Element
{
  /**
   * A string for the content type.
   */
  public static final String CONTENT_TYPE = "image/generic";

  /**
   * Constructs a image element.
   */
  public ImageElement ()
  {
  }

  /**
   * Returns the content type, in this case 'image/generic'.
   *
   * @return the content type.
   */
  public String getContentType ()
  {
    return ImageElement.CONTENT_TYPE;
  }

  /**
   * Returns true if the image should be scaled, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isScale ()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleKeys.SCALE);
  }

  /**
   * Sets a flag that controls whether the image should be scaled to fit the element
   * bounds.
   *
   * @param scale the flag.
   */
  public void setScale (final boolean scale)
  {
    getStyle().setBooleanStyleProperty(ElementStyleKeys.SCALE, scale);
  }

  /**
   * Returns true if the image's aspect ratio should be preserved, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isKeepAspectRatio ()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleKeys.KEEP_ASPECT_RATIO);
  }

  /**
   * Sets a flag that controls whether the shape's aspect ratio should be preserved.
   *
   * @param kar the flag.
   */
  public void setKeepAspectRatio (final boolean kar)
  {
    getStyle().setBooleanStyleProperty(ElementStyleKeys.KEEP_ASPECT_RATIO, kar);
  }
}
