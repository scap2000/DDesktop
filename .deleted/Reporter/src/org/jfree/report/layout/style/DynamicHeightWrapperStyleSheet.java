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
 * DynamicHeightWrapperStyleSheet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.style;

import org.jfree.report.style.AbstractStyleSheet;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.InstanceID;
import org.jfree.ui.FloatDimension;

/**
 * An element, that has the dynamic flag set to true, must restrict its maximum width to the minimum width (unless
 * it explicitly defines the maximum-width). This strange behavior is a legacy of our old sloopy layouting definition,
 * where the minimum-size provided the defaults for all other sizes.
 *
 * @author Thomas Morgner
 */
public class DynamicHeightWrapperStyleSheet extends AbstractStyleSheet
{
  private StyleSheet parent;
  private Float minWidth;

  public DynamicHeightWrapperStyleSheet(final StyleSheet parent)
  {
    this.parent = parent;
    this.minWidth = (Float) parent.getStyleProperty(ElementStyleKeys.MIN_WIDTH);
  }

  public StyleSheet getParent()
  {
    return parent;
  }

  public Object getStyleProperty(final StyleKey key, final Object defaultValue)
  {
    if (ElementStyleKeys.MAXIMUMSIZE.equals(key))
    {
      final Float maxHeight = (Float) parent.getStyleProperty(ElementStyleKeys.MAX_HEIGHT);
      return new FloatDimension(minWidth.floatValue(), maxHeight.floatValue());
    }
    if (ElementStyleKeys.MAX_WIDTH.equals(key))
    {
      return parent.getStyleProperty(ElementStyleKeys.MIN_WIDTH, defaultValue);
    }
    return parent.getStyleProperty(key, defaultValue);
  }

  public Object[] toArray(final StyleKey[] keys)
  {
    final Object[] objects = (Object[]) parent.toArray(keys).clone();
    objects[ElementStyleKeys.MAXIMUMSIZE.getIdentifier()] = null;
    objects[ElementStyleKeys.MAX_WIDTH.getIdentifier()] = objects[ElementStyleKeys.MIN_WIDTH.getIdentifier()];
    return objects;
  }

  public InstanceID getId()
  {
    return parent.getId();
  }

  public long getChangeTracker()
  {
    return parent.getChangeTracker();
  }
}
