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
 * ImageUtils.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util;

import java.awt.image.BufferedImage;

import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Provides utility methods for image creation and manipluation.
 *
 * @author Thomas Morgner
 */
public final class ImageUtils
{
  /**
   * DefaultConstructor.
   */
  private ImageUtils ()
  {
  }

  /**
   * Creates a transparent image.  These can be used for aligning menu items.
   *
   * @param width  the width.
   * @param height the height.
   * @return the created transparent image.
   */
  public static BufferedImage createTransparentImage (final int width, final int height)
  {
    final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    if (height == 0)
    {
      return img;
    }

    final int[] data = img.getRGB(0, 0, width, 1, null, 0, width);
    Arrays.fill(data, 0x00000000);
    for (int i = 0; i < height; i++)
    {
      img.setRGB(0, i, width, 1, data, 0, width);
    }
    return img;
  }

  /**
   * Creates a transparent icon. The Icon can be used for aligning menu items.
   *
   * @param width  the width of the new icon
   * @param height the height of the new icon
   * @return the created transparent icon.
   */
  public static Icon createTransparentIcon (final int width, final int height)
  {
    return new ImageIcon(createTransparentImage(width, height));
  }
}
