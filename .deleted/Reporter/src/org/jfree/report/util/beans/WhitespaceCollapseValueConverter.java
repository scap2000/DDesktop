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
 * WhitespaceCollapseValueConverter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util.beans;

import org.jfree.report.style.WhitespaceCollapse;

/**
 * Creation-Date: 06.09.2007, 14:00:42
 *
 * @author Thomas Morgner
 */
public class WhitespaceCollapseValueConverter implements ValueConverter
{
  public WhitespaceCollapseValueConverter()
  {
  }

  public String toAttributeValue(final Object o) throws BeanException
  {
    if (o instanceof WhitespaceCollapse)
    {
      return String.valueOf(o);
    }
    else
    {
      throw new BeanException("Invalid value specified for WhitespaceCollapse");
    }
  }

  public Object toPropertyValue(final String o) throws BeanException
  {
    if ("discard".equalsIgnoreCase(o))
    {
      return WhitespaceCollapse.DISCARD;
    }
    if ("collapse".equalsIgnoreCase(o))
    {
      return WhitespaceCollapse.COLLAPSE;
    }
    if ("preserve".equalsIgnoreCase(o))
    {
      return WhitespaceCollapse.PRESERVE;
    }
    if ("preserve-breaks".equalsIgnoreCase(o))
    {
      return WhitespaceCollapse.PRESERVE_BREAKS;
    }
    return null;
  }
}
