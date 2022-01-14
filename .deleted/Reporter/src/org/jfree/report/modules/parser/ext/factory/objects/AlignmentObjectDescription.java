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
 * AlignmentObjectDescription.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.objects;

import org.jfree.report.ElementAlignment;
import org.jfree.report.modules.parser.ext.factory.base.AbstractObjectDescription;
import org.jfree.report.modules.parser.ext.factory.base.ObjectFactoryException;

/**
 * An object-description for an {@link ElementAlignment} object.
 *
 * @author Thomas Morgner
 */
public class AlignmentObjectDescription extends AbstractObjectDescription
{
  /**
   * Creates a new object description.
   */
  public AlignmentObjectDescription ()
  {
    super(ElementAlignment.class);
    setParameterDefinition("value", String.class);
  }

  /**
   * Creates an {@link ElementAlignment} object based on this description.
   *
   * @return The object.
   */
  public Object createObject ()
  {
    final String o = (String) getParameter("value");
    if (o == null)
    {
      return null;
    }
    if ("left".equalsIgnoreCase(o))
    {
      return ElementAlignment.LEFT;
    }
    if ("right".equalsIgnoreCase(o))
    {
      return ElementAlignment.RIGHT;
    }
    if ("center".equalsIgnoreCase(o))
    {
      return ElementAlignment.CENTER;
    }
    if ("top".equalsIgnoreCase(o))
    {
      return ElementAlignment.TOP;
    }
    if ("middle".equalsIgnoreCase(o))
    {
      return ElementAlignment.MIDDLE;
    }
    if ("bottom".equalsIgnoreCase(o))
    {
      return ElementAlignment.BOTTOM;
    }
    return null;
  }

  /**
   * Sets the parameters in the object description to match the specified object.
   *
   * @param o the object (an {@link ElementAlignment} instance).
   * @throws ObjectFactoryException if the object is not recognised.
   */
  public void setParameterFromObject (final Object o)
          throws ObjectFactoryException
  {
    if (o.equals(ElementAlignment.BOTTOM))
    {
      setParameter("value", "bottom");
    }
    else if (o.equals(ElementAlignment.MIDDLE))
    {
      setParameter("value", "middle");
    }
    else if (o.equals(ElementAlignment.TOP))
    {
      setParameter("value", "top");
    }
    else if (o.equals(ElementAlignment.CENTER))
    {
      setParameter("value", "center");
    }
    else if (o.equals(ElementAlignment.RIGHT))
    {
      setParameter("value", "right");
    }
    else if (o.equals(ElementAlignment.LEFT))
    {
      setParameter("value", "left");
    }
    else
    {
      throw new ObjectFactoryException("Invalid value specified for ElementAlignment");
    }
  }

}
