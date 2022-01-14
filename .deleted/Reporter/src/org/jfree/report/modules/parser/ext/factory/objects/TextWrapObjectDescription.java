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
 * TextWrapObjectDescription.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.objects;

import org.jfree.report.modules.parser.ext.factory.base.AbstractObjectDescription;
import org.jfree.report.modules.parser.ext.factory.base.ObjectFactoryException;
import org.jfree.report.style.TextWrap;

/**
 * An object-description for an {@link org.jfree.report.ElementAlignment} object.
 *
 * @author Thomas Morgner
 */
public class TextWrapObjectDescription extends AbstractObjectDescription
{
  /**
   * Creates a new object description.
   */
  public TextWrapObjectDescription ()
  {
    super(TextWrap.class);
    setParameterDefinition("value", String.class);
  }

  /**
   * Creates an {@link org.jfree.report.ElementAlignment} object based on this description.
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
    if ("wrap".equalsIgnoreCase(o))
    {
      return TextWrap.WRAP;
    }
    if ("none".equalsIgnoreCase(o))
    {
      return TextWrap.NONE;
    }
    return null;
  }

  /**
   * Sets the parameters in the object description to match the specified object.
   *
   * @param o the object (an {@link org.jfree.report.ElementAlignment} instance).
   * @throws org.jfree.report.modules.parser.ext.factory.base.ObjectFactoryException if the object is not recognised.
   */
  public void setParameterFromObject (final Object o)
          throws ObjectFactoryException
  {
    if (o.equals(TextWrap.WRAP))
    {
      setParameter("value", "wrap");
    }
    else if (o.equals(TextWrap.NONE))
    {
      setParameter("value", "none");
    }
    else
    {
      throw new ObjectFactoryException("Invalid value specified for ElementAlignment");
    }
  }

}
