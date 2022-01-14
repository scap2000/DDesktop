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
 * NonDynamicReplacedContentStyleSheet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.style;

import java.awt.geom.Point2D;

import org.jfree.report.style.AbstractStyleSheet;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.InstanceID;
import org.jfree.ui.FloatDimension;

/**
 * A replaced content element that is contained in a 'canvas' box (which is the default for all non-inline replaced
 * content elements) must have a minimum width and height of 100% so that it fills the whole box.
 *
 * @author Thomas Morgner
 */
public class NonDynamicReplacedContentStyleSheet extends AbstractStyleSheet
{
  private static final Float SIZE = new Float(-100);
  private static final Float POS = new Float(0);
  private StyleSheet parent;

  public NonDynamicReplacedContentStyleSheet(final StyleSheet parent)
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
      return new FloatDimension(-100, -100);
    }
    if (ElementStyleKeys.MAX_WIDTH.equals(key))
    {
      return SIZE;
    }
    if (ElementStyleKeys.MAX_HEIGHT.equals(key))
    {
      return SIZE;
    }
    if (ElementStyleKeys.MINIMUMSIZE.equals(key))
    {
      return new FloatDimension(-100, -100);
    }
    if (ElementStyleKeys.MIN_WIDTH.equals(key))
    {
      return SIZE;
    }
    if (ElementStyleKeys.MIN_HEIGHT.equals(key))
    {
      return SIZE;
    }
    if (ElementStyleKeys.POS_X.equals(key))
    {
      return POS;
    }
    if (ElementStyleKeys.POS_Y.equals(key))
    {
      return POS;
    }
    if (ElementStyleKeys.ABSOLUTE_POS.equals(key))
    {
      return new Point2D.Float (0,0);
    }

    return parent.getStyleProperty(key, defaultValue);
  }

  public Object[] toArray(final StyleKey[] keys)
  {
    final Object[] objects = (Object[]) parent.toArray(keys).clone();
    objects[ElementStyleKeys.MIN_WIDTH.getIdentifier()] = SIZE;
    objects[ElementStyleKeys.MIN_HEIGHT.getIdentifier()] = SIZE;
    objects[ElementStyleKeys.MAX_WIDTH.getIdentifier()] = SIZE;
    objects[ElementStyleKeys.MAX_HEIGHT.getIdentifier()] = SIZE;
    objects[ElementStyleKeys.MAXIMUMSIZE.getIdentifier()] = null;
    objects[ElementStyleKeys.MINIMUMSIZE.getIdentifier()] = null;
    objects[ElementStyleKeys.POS_X.getIdentifier()] = POS;
    objects[ElementStyleKeys.POS_Y.getIdentifier()] = POS;
    objects[ElementStyleKeys.ABSOLUTE_POS.getIdentifier()] = null;
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
