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
 * NonPaddingWrapperStyleSheet.java
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
public class NonPaddingWrapperStyleSheet extends AbstractStyleSheet
{
  private StyleSheet parent;
  private static final Float ZERO = new Float(0);

  public NonPaddingWrapperStyleSheet()
  {
  }

  public void setParent(final StyleSheet parent)
  {
    this.parent = parent;
  }

  public StyleSheet getParent()
  {
    return parent;
  }

  public Object getStyleProperty(final StyleKey key, final Object defaultValue)
  {
    if (ElementStyleKeys.PADDING_TOP.equals(key))
    {
      return ZERO;
    }
    if (ElementStyleKeys.PADDING_LEFT.equals(key))
    {
      return ZERO;
    }
    if (ElementStyleKeys.PADDING_BOTTOM.equals(key))
    {
      return ZERO;
    }
    if (ElementStyleKeys.PADDING_RIGHT.equals(key))
    {
      return ZERO;
    }
    return parent.getStyleProperty(key, defaultValue);
  }

  public Object[] toArray(final StyleKey[] keys)
  {
    final Object[] objects = (Object[]) parent.toArray(keys).clone();
    objects[ElementStyleKeys.PADDING_TOP.getIdentifier()] = ZERO;
    objects[ElementStyleKeys.PADDING_LEFT.getIdentifier()] = ZERO;
    objects[ElementStyleKeys.PADDING_BOTTOM.getIdentifier()] = ZERO;
    objects[ElementStyleKeys.PADDING_RIGHT.getIdentifier()] = ZERO;
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
