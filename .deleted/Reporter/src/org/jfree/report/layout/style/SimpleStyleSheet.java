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
 * SimpleStyleSheet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.style;

import org.jfree.report.style.AbstractStyleSheet;
import org.jfree.report.style.BandDefaultStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.InstanceID;

/**
 * A simple, read-only stylesheet.
 *
 * @author Thomas Morgner
 */
public final class SimpleStyleSheet extends AbstractStyleSheet
{
  public static final StyleSheet EMPTY_STYLE =
      new SimpleStyleSheet(BandDefaultStyleSheet.getBandDefaultStyle(),
                           StyleKey.getDefinedStyleKeys());

  private Object[] properties;
  private InstanceID parentId;

  public SimpleStyleSheet(final StyleSheet parent, final StyleKey[] definedStyleKeys)
  {
    if (parent == null)
    {
      throw new NullPointerException();
    }
    if (definedStyleKeys == null)
    {
      throw new NullPointerException();
    }
    this.parentId = parent.getId();
    this.properties = parent.toArray(definedStyleKeys);
  }

  /**
   * Returns the value of a style.  If the style is not found in this style-sheet, the code looks in the parent
   * style-sheets.  If the style is not found in any of the parent style-sheets, then the default value (possibly
   * <code>null</code>) is returned.
   *
   * @param key          the style key.
   * @param defaultValue the default value (<code>null</code> permitted).
   * @return the value.
   */
  public Object getStyleProperty(final StyleKey key, final Object defaultValue)
  {
    final int identifier = key.getIdentifier();
    if (properties.length > identifier)
    {
      final Object property = properties[identifier];
      if (property != null)
      {
        return property;
      }
    }
    return defaultValue;
  }

  public InstanceID getParentId()
  {
    return parentId;
  }

}
