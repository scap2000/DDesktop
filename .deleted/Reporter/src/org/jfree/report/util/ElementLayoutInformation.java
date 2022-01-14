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
 * ElementLayoutInformation.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.util;

import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictDimension;
import org.jfree.report.util.geom.StrictPoint;

/**
 * A small carrier class to encapsulate the common layout parameters. This information is
 * a utility class, don't expect to find one bound to an element.
 *
 * @author Thomas Morgner
 * @deprecated Is no longer needed. Remove it.
 */
public class ElementLayoutInformation
{
  /**
   * The absolute position of the element.
   */
  private StrictPoint absolutePosition;

  /**
   * The current minimum size for the element.
   */
  private StrictDimension minimumSize;

  /**
   * The current maximum size for the element.
   */
  private StrictDimension maximumSize;

  /**
   * The current preferred size for the element.
   */
  private StrictDimension preferredSize;

  /**
     * Creates a new instance.
     * <p/>
     * The position will be <code>rect.minX, rect.minY</code> and all dimensions are set to
     * <code>rect.width, rect.height</code>.
     *
     * @param rect the rectangle that will be the base for this ElementLayoutInformation.
     * @throws java.lang.NullPointerException if the given rectangle is null.
     */
  public ElementLayoutInformation (final StrictBounds rect)
  {
    if (rect == null)
    {
      throw new NullPointerException();
    }
    absolutePosition = new StrictPoint(rect.getX(), rect.getY());
    final StrictDimension fdim = new StrictDimension(rect.getWidth(), rect.getHeight());
    maximumSize = fdim;
    minimumSize = fdim;
    preferredSize = fdim;
  }

  /**
   * Creates a new instance.
   * <p/>
   * The preferred size will be undefined (<code>null</code>).
   *
   * @param absolutePosition the absolute position for the element.
   * @param minimumSize      the minimum size for the element.
   * @param maximumSize      the maximum size for the element.
   * @throws java.lang.NullPointerException if one of the parameters is <code>null</code>.
   */
  public ElementLayoutInformation (final StrictPoint absolutePosition,
                                   final StrictDimension minimumSize,
                                   final StrictDimension maximumSize)
  {
    this(absolutePosition, minimumSize, maximumSize, null);
  }

  /**
   * Creates a new instance.
   * <p/>
   * If the preferred size is <code>null</code>, then it is left undefined.
   *
   * @param absolutePosition the absolute position for the element
   * @param minimumSize      the minimum size for the element
   * @param maximumSize      the maximum size for the element
   * @param preferredSize    the preferred size or <code>null</code> if not known.
   * @throws java.lang.NullPointerException if the position or max/min size is
   *                                        <code>null</code>.
   */
  public ElementLayoutInformation (final StrictPoint absolutePosition,
                                   final StrictDimension minimumSize,
                                   final StrictDimension maximumSize,
                                   final StrictDimension preferredSize)
  {
    if (absolutePosition == null)
    {
      throw new NullPointerException();
    }
    if (minimumSize == null)
    {
      throw new NullPointerException();
    }
    if (maximumSize == null)
    {
      throw new NullPointerException();
    }
    this.absolutePosition = (StrictPoint) absolutePosition.clone();
    // the minsize cannot be greater than the max size ...
    this.minimumSize = unionMin(maximumSize, minimumSize);
    this.maximumSize = (StrictDimension) maximumSize.clone();
    if (preferredSize != null)
    {
      this.preferredSize = (StrictDimension) preferredSize.clone();
    }
  }

  /**
   * Gets the absolute positon defined in this LayoutInformation.
   *
   * @return a clone of the absolute position.
   */
  public StrictPoint getAbsolutePosition ()
  {
    return (StrictPoint) absolutePosition.clone();
  }

  /**
   * Gets the minimum size defined in this LayoutInformation.
   *
   * @return a clone of the minimum size.
   */
  public StrictDimension getMinimumSize ()
  {
    return (StrictDimension) minimumSize.clone();
  }

  /**
   * Gets the maximum size defined in this LayoutInformation.
   *
   * @return a clone of the maximum size.
   */
  public StrictDimension getMaximumSize ()
  {
    return (StrictDimension) maximumSize.clone();
  }

  /**
   * Gets the preferred size defined in this LayoutInformation.
   *
   * @return a clone of the preferred size.
   */
  public StrictDimension getPreferredSize ()
  {
    if (preferredSize == null)
    {
      return null;
    }
    return (StrictDimension) preferredSize.clone();
  }

  /**
   * Create a minimum dimension of the given 2 dimension objects. If pref is not given,
   * the max parameter is returned unchanged.
   * <p/>
   * This is used to limit the element bounds to the preferred size or the maximum size
   * (in case the user misconfigured anything).
   *
   * @param max  the maximum size as retrieved from the element.
   * @param pref the preferred size.
   * @return the minimum dimension.
   */
  public static StrictDimension unionMin (final StrictDimension max,
                                          final StrictDimension pref)
  {
    if (pref == null)
    {
      return max;
    }
    return new StrictDimension(Math.min(pref.getWidth(), max.getWidth()),
            Math.min(pref.getHeight(), max.getHeight()));
  }

  /**
   * Returns a string representing the object (useful for debugging).
   *
   * @return A string.
   */
  public String toString ()
  {
    final StringBuffer b = new StringBuffer();
    b.append("ElementLayoutInformation: \n");
    b.append("    AbsolutePos: ");
    b.append(absolutePosition);
    b.append("\n    MinSize: ");
    b.append(minimumSize);
    b.append("\n    PrefSize: ");
    b.append(preferredSize);
    b.append("\n    MaxSize: ");
    b.append(maximumSize);
    b.append('\n');
    return b.toString();
  }
}
