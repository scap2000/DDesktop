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
 * ImmutableStyleSheet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.style;

import java.io.Serializable;

import org.jfree.report.util.IntegerCache;

/**
 * Creation-Date: 26.06.2006, 11:25:02
 *
 * @author Thomas Morgner
 */
public class ImmutableStyleSheet extends AbstractStyleSheet implements Serializable
{
  private ElementStyleSheet styleSheet;

  public ImmutableStyleSheet(final ElementStyleSheet styleSheet)
  {
    if (styleSheet == null)
    {
      throw new NullPointerException();
    }
    this.styleSheet = styleSheet;
  }

  public boolean getBooleanStyleProperty(final StyleKey key)
  {
    return styleSheet.getBooleanStyleProperty(key);
  }

  public boolean getBooleanStyleProperty(final StyleKey key,
                                         final boolean defaultValue)
  {
    return styleSheet.getBooleanStyleProperty(key, defaultValue);
  }

  public FontDefinition getFontDefinitionProperty()
  {
    return styleSheet.getFontDefinitionProperty();
  }

  /**
   * Returns an integer style.
   *
   * @param key the style key.
   * @param def the default value.
   * @return the style value.
   */
  public int getIntStyleProperty(final StyleKey key, final int def)
  {
    final Number i = (Number) getStyleProperty(key, IntegerCache.getInteger(def));
    return i.intValue();
  }

  /**
   * Returns an double style.
   *
   * @param key the style key.
   * @param def the default value.
   * @return the style value.
   */
  public double getDoubleStyleProperty(final StyleKey key, final double def)
  {
    final Number i = (Number) getStyleProperty(key, new Double (def));
    return i.intValue();
  }

  public Object getStyleProperty(final StyleKey key)
  {
    return styleSheet.getStyleProperty(key);
  }

  public Object getStyleProperty(final StyleKey key, final Object defaultValue)
  {
    return styleSheet.getStyleProperty(key, defaultValue);
  }
}
