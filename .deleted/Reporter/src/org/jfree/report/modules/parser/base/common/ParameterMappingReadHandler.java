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
 * ParameterMappingReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.base.common;

import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xmlns.parser.ParseException;

import org.xml.sax.SAXException;

/**
 * Creation-Date: Jan 9, 2007, 6:01:15 PM
 *
 * @author Thomas Morgner
 */
public class ParameterMappingReadHandler extends AbstractPropertyXmlReadHandler
{
  private String name;
  private String alias;

  public ParameterMappingReadHandler()
  {
  }

  protected void startParsing(final PropertyAttributes attrs)
      throws SAXException
  {
    name = attrs.getValue(getUri(), "name");
    if (name == null)
    {
      throw new ParseException
          ("Required attribute 'name' is missing.", getLocator());
    }
    alias = attrs.getValue(getUri(), "alias");
    if (alias == null)
    {
      alias = name;
    }
  }

  public String getName()
  {
    return name;
  }

  public String getAlias()
  {
    return alias;
  }

  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   * @throws SAXException if there is a parsing error.
   */
  public Object getObject() throws SAXException
  {
    return getName();
  }
}
