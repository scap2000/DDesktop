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
 * VerticalTextAlignValueConverter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util.beans;

import org.jfree.report.style.VerticalTextAlign;

/**
 * Creation-Date: 06.09.2007, 14:00:42
 *
 * @author Thomas Morgner
 */
public class VerticalTextAlignValueConverter implements ValueConverter
{
  public VerticalTextAlignValueConverter()
  {
  }

  public String toAttributeValue(final Object o) throws BeanException
  {
    if (o instanceof VerticalTextAlign)
    {
      return String.valueOf(o);
    }
    throw new BeanException("Invalid value encountered for VerticalTextAlign");
  }

  public Object toPropertyValue(final String o) throws BeanException
  {
    if ("use-script".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.USE_SCRIPT;
    }
    if ("text-bottom".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.TEXT_BOTTOM;
    }
    if ("bottom".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.BOTTOM;
    }
    if ("text-top".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.TEXT_TOP;
    }
    if ("top".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.TOP;
    }
    if ("central".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.CENTRAL;
    }
    if ("middle".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.MIDDLE;
    }

    if ("sub".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.SUB;
    }
    if ("super".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.SUPER;
    }
    if ("baseline".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.BASELINE;
    }
    throw new BeanException("Invalid value encountered for VerticalTextAlign");
  }
}
