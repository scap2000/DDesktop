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
 * RenderLength.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 09.07.2006, 21:03:12
 *
 * @author Thomas Morgner
 */
public class RenderLength
{
  public static final RenderLength AUTO = new RenderLength(Long.MIN_VALUE, false);
  public static final RenderLength EMPTY = new RenderLength(0, false);

  private long value;
  private boolean percentage;

  public RenderLength(final long value,
                      final boolean percentage)
  {
    this.value = value;
    this.percentage = percentage;
  }

  public long getValue()
  {
    return value;
  }

  public boolean isPercentage()
  {
    return percentage;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final RenderLength that = (RenderLength) o;

    if (percentage != that.percentage)
    {
      return false;
    }
    if (value != that.value)
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = (int) (value ^ (value >>> 32));
    result = 29 * result + (percentage ? 1 : 0);
    return result;
  }

  public long resolve (final long parent)
  {
    if (isPercentage())
    {
      return StrictGeomUtility.multiply(value, parent) / 100;
    }
    else if (value == Long.MIN_VALUE)
    {
      return 0;
    }
    else
    {
      return value;
    }
  }

  public long resolve(final long parent, final long auto)
  {
    if (isPercentage())
    {
      return StrictGeomUtility.multiply(value, parent) / 100;
    }
    else if (value == Long.MIN_VALUE)
    {
      return auto;
    }
    else
    {
      return value;
    }
  }


  public static long resolveLength(final long parent, final double rawvalue)
  {
    final long value = StrictGeomUtility.toInternalValue(rawvalue);
    if (value == Long.MIN_VALUE)
    {
      return 0;
    }
    if (value  < 0)
    {
      return -StrictGeomUtility.multiply(value, parent) / 100;
    }
    else
    {
      return value;
    }
  }

  public static RenderLength createFromRaw (final double rawValue)
  {
    if (rawValue < 0)
    {
      return new RenderLength(StrictGeomUtility.toInternalValue(-rawValue), true);
    }
    if (rawValue == 0)
    {
      return EMPTY;
    }
    return new RenderLength(StrictGeomUtility.toInternalValue(rawValue), false);
  }

  public RenderLength resolveToRenderLength (final long parent)
  {
    if (isPercentage())
    {
      if (parent <= 0)
      {
        // An unresolvable parent ...
        return RenderLength.AUTO;
      }
      // This may resolve to zero - which is valid
      final long value = StrictGeomUtility.multiply(this.value, parent) / 100;
      return new RenderLength(value, false);
    }
    else if (value <= 0)
    {
      return RenderLength.AUTO;
    }
    else
    {
      return new RenderLength(value, false);
    }
  }

  public String toString()
  {
    if (value == Long.MIN_VALUE)
    {
      return "RenderLength{value=AUTO}";
    }
    if (isPercentage())
    {
      return "RenderLength{" +
            "value=" + StrictGeomUtility.toExternalValue(value) +
            "% }";
    }
    else
    {
      return "RenderLength{" +
            "value=" + value +
            "micro-pt }";
    }
  }
}
