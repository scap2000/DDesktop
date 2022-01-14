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
 * DataSourceReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.DataTarget;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.ext.factory.base.ObjectDescription;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.xmlns.parser.ParseException;
import org.jfree.xmlns.parser.XmlReadHandler;

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
 * DataSourceReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class DataSourceReadHandler extends CompoundObjectReadHandler
{
  private DataSourceReadHandler childHandler;

  public DataSourceReadHandler()
  {
    super(null);
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing(final PropertyAttributes attrs)
      throws SAXException
  {
    final String typeName = attrs.getValue(getUri(), "type");
    if (typeName == null)
    {
      throw new ParseException("The datasource type must be specified",
          getRootHandler().getDocumentLocator());
    }

    final DataSourceCollector fc = (DataSourceCollector) getRootHandler()
        .getHelperObject(ReportDefinitionReadHandler.DATASOURCE_FACTORY_KEY);
    final ObjectDescription od = fc.getDataSourceDescription(typeName);
    if (od == null)
    {
      throw new ParseException("The specified DataSource type is not defined", getLocator());
    }
    setObjectDescription(od);
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final PropertyAttributes atts)
      throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }

    if ("datasource".equals(tagName))
    {
      childHandler = new DataSourceReadHandler();
      return childHandler;
    }
    return super.getHandlerForChild(uri, tagName, atts);
  }

  /**
   * Returns the object for this element or null, if this element does not
   * create an object.
   *
   * @return the object.
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject()
  {
    final DataSource ds = (DataSource) super.getObject();
    if (childHandler != null && ds instanceof DataTarget)
    {
      final DataTarget dt = (DataTarget) ds;
      dt.setDataSource((DataSource) childHandler.getObject());
    }
    return ds;
  }
}
