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
 * HtmlColors.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.output.table.html.util;

import java.awt.Color;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Creation-Date: 20.12.2005, 10:48:22
 *
 * @author Thomas Morgner
 */
public class HtmlColors
{
  public static final Color BLACK = new Color(0x000000, false);
  public static final Color GREEN = new Color(0x008000, false);
  public static final Color SILVER = new Color(0xC0C0C0, false);
  public static final Color LIME = new Color(0x00FF00, false);
  public static final Color GRAY = new Color(0x808080, false);
  public static final Color OLIVE = new Color(0x808000, false);
  public static final Color WHITE = new Color(0xFFFFFF, false);
  public static final Color YELLOW = new Color(0xFFFF00, false);
  public static final Color MAROON = new Color(0x800000, false);
  public static final Color NAVY = new Color(0x000080, false);
  public static final Color RED = new Color(0xFF0000, false);
  public static final Color BLUE = new Color(0x0000FF, false);
  public static final Color PURPLE = new Color(0x800080, false);
  public static final Color TEAL = new Color(0x008080, false);
  public static final Color FUCHSIA = new Color(0xFF00FF, false);
  public static final Color AQUA = new Color(0x00FFFF, false);

  private HtmlColors() {}

  /**
   * Creates the color string for the given AWT color. If the color is one of
   * the predefined HTML colors, then the logical name is returned. For all
   * other colors, the RGB-Tripple is returned.
   *
   * @param color the AWTColor that should be translated.
   * @return the translated html color definition
   */
  public static String getColorString(final Color color)
  {
    try
    {
      final Field[] fields = HtmlColors.class.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        final Field f = fields[i];
        if (Modifier.isPublic(f.getModifiers())
                && Modifier.isFinal(f.getModifiers())
                && Modifier.isStatic(f.getModifiers()))
        {
          final String name = f.getName();
          final Object oColor = f.get(null);
          if (oColor instanceof Color)
          {
            if (color.equals(oColor))
            {
              return name;
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      //
    }

    // no defined constant color, so this must be a user defined color
    final String colorText = Integer.toHexString(color.getRGB() & 0x00ffffff);
    final StringBuffer retval = new StringBuffer(7);
    retval.append('#');

    final int fillUp = 6 - colorText.length();
    for (int i = 0; i < fillUp; i++)
    {
      retval.append('0');
    }

    retval.append(colorText);
    return retval.toString();
  }

}
