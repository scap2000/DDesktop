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
 * ComponentFieldReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.ComponentFieldElementFactory;
import org.jfree.report.elementfactory.ElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xmlns.parser.ParseException;

import org.xml.sax.SAXException;

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
 * ComponentFieldReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class ComponentFieldReadHandler extends AbstractElementReadHandler
{
  private ComponentFieldElementFactory elementFactory;

  public ComponentFieldReadHandler ()
  {
    this.elementFactory = new ComponentFieldElementFactory();
  }

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes atts)
          throws SAXException
  {
    super.startParsing(atts);

    final String fieldName = atts.getValue(getUri(), "fieldname");
    if (fieldName != null)
    {
      elementFactory.setFieldname(fieldName);
    }
    else
    {
      final String formula = atts.getValue(getUri(), "formula");
      if (formula == null)
      {
        throw new ParseException
            ("Either 'fieldname' or 'formula' attribute must be given.", getLocator());
      }
      elementFactory.setFormula(formula);
    }
  }

  protected ElementFactory getElementFactory ()
  {
    return elementFactory;
  }
}
