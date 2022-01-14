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
 * ImageElementFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.elementfactory;

import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.ElementStyleSheet;

/**
 * An ElementFactory that can be used to create ImageElements. This is the base class for
 * all image element factories.
 *
 * @author Thomas Morgner
 */
public abstract class ImageElementFactory extends ElementFactory
{
  /**
   * The image element scaling property.
   */
  private Boolean scale;
  /**
   * The Keep-Aspect-Ratio property for the generated image element.
   */
  private Boolean keepAspectRatio;

  /**
   * DefaultConstructor.
   */
  protected ImageElementFactory ()
  {
  }

  /**
   * Returns, whether the image content should be scaled to fit the complete image element
   * bounds.
   *
   * @return the scale flag of the image element.
   */
  public Boolean getScale ()
  {
    return scale;
  }

  /**
   * Defines, whether the image content should be scaled to fit the complete image element
   * bounds.
   *
   * @param scale the scale flag of the image element.
   */
  public void setScale (final Boolean scale)
  {
    this.scale = scale;
  }

  /**
   * Returns whether the generated image element should preserve the original aspect ratio
   * of the image content during scaling. This property has no effect if the image content
   * is not scaled.
   *
   * @return the keep aspect ratio flag.
   */
  public Boolean getKeepAspectRatio ()
  {
    return keepAspectRatio;
  }

  /**
   * Defines whether the generated image element should preserve the original aspect ratio
   * of the image content during scaling. This property has no effect if the image content
   * is not scaled.
   *
   * @param keepAspectRatio whether to keep the aspect ratio of the image content during
   *                        the scaling.
   */
  public void setKeepAspectRatio (final Boolean keepAspectRatio)
  {
    this.keepAspectRatio = keepAspectRatio;
  }

  /**
   * Applies the style definition to the elements stylesheet.
   *
   * @param style the element stylesheet which should receive the style definition.
   */
  protected void applyStyle (final ElementStyleSheet style)
  {
    super.applyStyle(style);
    style.setStyleProperty(ElementStyleKeys.KEEP_ASPECT_RATIO, getKeepAspectRatio());
    style.setStyleProperty(ElementStyleKeys.SCALE, getScale());
  }
}
