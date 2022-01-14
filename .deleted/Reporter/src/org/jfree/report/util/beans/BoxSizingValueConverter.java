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
 * BoxSizingValueConverter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util.beans;

import org.jfree.report.style.BoxSizing;

/**
 * Creation-Date: 06.09.2007, 14:00:42
 *
 * @author Thomas Morgner
 */
public class BoxSizingValueConverter implements ValueConverter
{
  public BoxSizingValueConverter()
  {
  }

  public String toAttributeValue(final Object o) throws BeanException
  {
    if (o instanceof BoxSizing)
    {
      return String.valueOf(o);
    }
    else
    {
      throw new BeanException("Invalid value specified for BoxSizing");
    }
  }

  public Object toPropertyValue(final String s) throws BeanException
  {
    if (BoxSizing.BORDER_BOX.toString().equals(s))
    {
      return BoxSizing.BORDER_BOX;
    }
    if (BoxSizing.CONTENT_BOX.toString().equals(s))
    {
      return BoxSizing.CONTENT_BOX;
    }
    throw new BeanException("Invalid value specified for BoxSizing");
  }
}
