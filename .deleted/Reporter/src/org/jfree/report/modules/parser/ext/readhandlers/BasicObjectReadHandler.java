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
 * BasicObjectReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.common.PropertyStringReadHandler;
import org.jfree.report.modules.parser.ext.factory.base.ClassFactory;
import org.jfree.report.modules.parser.ext.factory.base.ObjectDescription;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.parser.ParseException;
import org.jfree.xmlns.parser.RootXmlReadHandler;

import org.xml.sax.Attributes;
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
 * BasicObjectReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class BasicObjectReadHandler extends AbstractPropertyXmlReadHandler
{
  private ObjectDescription objectDescription;
  private PropertyStringReadHandler stringReadHandler;
  private ClassFactory classFactory;

  /**
   * @param objectDescription may be null.
   * @param commentHintPath   never null.
   */
  public BasicObjectReadHandler (final ObjectDescription objectDescription)
  {
    this.stringReadHandler = new PropertyStringReadHandler();
    this.objectDescription = objectDescription;
  }

  /**
   * Initialises the handler.
   *
   * @param rootHandler the root handler.
   * @param tagName     the tag name.
   */
  public void init(final RootXmlReadHandler rootHandler,
                   final String uri,
                   final String tagName)
  {
    super.init(rootHandler, uri, tagName);
    this.classFactory = (ClassFactory)
            getRootHandler().getHelperObject(ReportDefinitionReadHandler.CLASS_FACTORY_KEY);
  }

  protected ObjectDescription getObjectDescription ()
  {
    return objectDescription;
  }

  protected void setObjectDescription (final ObjectDescription objectDescription)
  {
    this.objectDescription = objectDescription;
  }

  protected ClassFactory getClassFactory ()
  {
    return classFactory;
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException
  {
    handleStartParsing(attrs);
    getRootHandler().delegate(stringReadHandler, getUri(), getTagName(), attrs);
  }

  protected void handleStartParsing (final Attributes attrs)
      throws ParseException
  {
    final String name = attrs.getValue(getUri(), "name");
    if (name == null)
    {
      throw new ParseException ("Required attribute 'name' is missing.",
          getRootHandler().getDocumentLocator());
    }


    final String attrClass = attrs.getValue(getUri(), "class");
    if (attrClass != null)
    {
      try
      {
        final ClassLoader loader = ObjectUtilities.getClassLoader(BasicObjectReadHandler.class);
        final Class clazz = loader.loadClass(attrClass);
        objectDescription = ObjectFactoryUtility.findDescription
            (classFactory, clazz, getLocator());
      }
      catch (ClassNotFoundException e)
      {
        throw new ParseException
                ("Value for given 'class' attribute is invalid",
                        getRootHandler().getDocumentLocator());
      }
    }
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing ()
          throws SAXException
  {
    final String value = stringReadHandler.getResult();
    objectDescription.setParameter("value", value);
    super.doneParsing();
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
  {
    objectDescription.configure(getRootHandler().getParserConfiguration());
    return objectDescription.createObject();
  }
}
