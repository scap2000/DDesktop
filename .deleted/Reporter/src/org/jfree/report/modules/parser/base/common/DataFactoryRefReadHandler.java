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
 * DataFactoryRefReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.base.common;

import org.jfree.report.DataFactory;
import org.jfree.report.modules.parser.base.DataFactoryReadHandler;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceException;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.parser.ParseException;

import org.xml.sax.SAXException;

/**
 * Creation-Date: 27.06.2006, 12:09:08
 *
 * @author Thomas Morgner
 */
public class DataFactoryRefReadHandler extends AbstractPropertyXmlReadHandler
  implements DataFactoryReadHandler
{
  private DataFactory dataFactory;

  public DataFactoryRefReadHandler()
  {
  }

  protected void startParsing(final PropertyAttributes attrs)
          throws SAXException
  {
    final String href = attrs.getValue(getUri(), "href");
    // we have a HREF given, ...
    if (href != null)
    {
      // load ..

      final ResourceKey key = getRootHandler().getSource();
      final ResourceManager manager = getRootHandler().getResourceManager();
      try
      {
        final ResourceKey derivedKey = manager.deriveKey(key, href);
        final Resource resource = manager.create(derivedKey, null, DataFactory.class);
        getRootHandler().getDependencyCollector().add(resource);
        dataFactory = (DataFactory) resource.getResource();
      }
      catch (ResourceKeyCreationException e)
      {
        throw new ParseException("Unable to deriveForAdvance key for " + key + " and " + href, e);
      }
      catch (ResourceCreationException e)
      {
        throw new ParseException("Unable to parse resource for " + key + " and " + href, e);
      }
      catch (ResourceLoadingException e)
      {
        throw new ParseException("Unable to load resource data for " + key + " and " + href, e);
      }
      catch (ResourceException e)
      {
        throw new ParseException("Unable to parse resource for " + key + " and " + href, e);
      }
      return;
    }

    final String dfType = attrs.getValue(getUri(), "type");
    if (dfType != null)
    {
      final Object o = ObjectUtilities.loadAndInstantiate
              (dfType, DataFactory.class);
      if (o instanceof DataFactory == false)
      {
        throw new SAXException("'type' did not point to a usable " +
                "DataFactory implementation.");
      }
      dataFactory = (DataFactory) o;
    }
  }

  public DataFactory getDataFactory()
  {
    return dataFactory;
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
    return dataFactory;
  }
}
