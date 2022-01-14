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
 * DynamicWrapperStyleSheet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.style;

import org.jfree.report.style.AbstractStyleSheet;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.InstanceID;

/**
 * An element, that does not have the dynamic flag set to true, is limited to its minimum size and cannot
 * grow any larger than that. This stylesheet simply enforces this policy by redefining the maximum size so that the
 * maximum size is equal to the minimum size.
 *
 * @author Thomas Morgner
 */
public class NonDynamicHeightWrapperStyleSheet extends AbstractStyleSheet
{
  private StyleSheet parent;

  public NonDynamicHeightWrapperStyleSheet(final StyleSheet parent)
  {
    this.parent = parent;
  }

  public StyleSheet getParent()
  {
    return parent;
  }

  public Object getStyleProperty(final StyleKey key, final Object defaultValue)
  {
    if (ElementStyleKeys.MAXIMUMSIZE.equals(key))
    {
      return parent.getStyleProperty(ElementStyleKeys.MINIMUMSIZE, defaultValue);
    }
    if (ElementStyleKeys.MAX_WIDTH.equals(key))
    {
      return parent.getStyleProperty(ElementStyleKeys.MIN_WIDTH, defaultValue);
    }
    if (ElementStyleKeys.MAX_HEIGHT.equals(key))
    {
      return parent.getStyleProperty(ElementStyleKeys.MIN_HEIGHT, defaultValue);
    }
    return parent.getStyleProperty(key, defaultValue);
  }

  public Object[] toArray(final StyleKey[] keys)
  {
    final Object[] objects = (Object[]) parent.toArray(keys).clone();
    objects[ElementStyleKeys.MAXIMUMSIZE.getIdentifier()] = null;
    objects[ElementStyleKeys.MAX_WIDTH.getIdentifier()] = objects[ElementStyleKeys.MIN_WIDTH.getIdentifier()];
    objects[ElementStyleKeys.MAX_HEIGHT.getIdentifier()] = objects[ElementStyleKeys.MIN_HEIGHT.getIdentifier()];
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
