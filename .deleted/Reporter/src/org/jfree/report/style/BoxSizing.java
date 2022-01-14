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
 * BoxSizing.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.style;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.jfree.report.util.ObjectStreamResolveException;

/**
 * Creation-Date: 30.10.2005, 19:37:35
 *
 * @author Thomas Morgner
 */
public class BoxSizing implements Serializable
{
  public static final BoxSizing CONTENT_BOX = new BoxSizing("content-box");
  public static final BoxSizing BORDER_BOX = new BoxSizing("border-box");
  private String type;

  private BoxSizing(final String type)
  {
    this.type = type;
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

    final BoxSizing that = (BoxSizing) o;

    if (type != null ? !type.equals(that.type) : that.type != null)
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return (type != null ? type.hashCode() : 0);
  }

  public String toString()
  {
    return type;
  }

  /**
   * Replaces the automatically generated instance with one of the enumeration instances.
   *
   * @return the resolved element
   *
   * @throws java.io.ObjectStreamException if the element could not be resolved.
   * @noinspection UNUSED_SYMBOL
   */
  private Object readResolve ()
          throws ObjectStreamException
  {
    if (this.type.equals(BoxSizing.CONTENT_BOX.type))
    {
      return BoxSizing.CONTENT_BOX;
    }
    if (this.type.equals(BoxSizing.BORDER_BOX.type))
    {
      return BoxSizing.BORDER_BOX;
    }
    // unknown element alignment...
    throw new ObjectStreamResolveException();
  }

}
