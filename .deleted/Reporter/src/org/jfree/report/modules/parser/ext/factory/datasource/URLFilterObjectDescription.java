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
 * URLFilterObjectDescription.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.datasource;

import java.net.URL;

import org.jfree.report.filter.URLFilter;
import org.jfree.report.modules.parser.ext.factory.base.BeanObjectDescription;
import org.jfree.util.Log;
import org.jfree.xmlns.parser.AbstractXmlResourceFactory;

/**
 * An ObjectDescription for the URLFilterClass. This class uses either an given or an
 * preconfigured base url to construct the URL.
 *
 * @author Thomas Morgner
 */
public class URLFilterObjectDescription extends BeanObjectDescription
{
  /**
   * Creates a new object description.
   *
   * @param className the class.
   */
  public URLFilterObjectDescription (final Class className)
  {
    super(className);
    if (URLFilter.class.isAssignableFrom(className) == false)
    {
      throw new IllegalArgumentException("Given class is no instance of URLFilter.");
    }
  }

  /**
   * Creates an object based on this description.
   *
   * @return The object.
   */
  public Object createObject ()
  {
    final URLFilter t = (URLFilter) super.createObject();
    if (t.getBaseURL() == null)
    {
      final String baseURL = getConfig().getConfigProperty(AbstractXmlResourceFactory.CONTENTBASE_KEY);
      try
      {
        final URL bURL = new URL(baseURL);
        t.setBaseURL(bURL);
      }
      catch (Exception e)
      {
        Log.warn("BaseURL is invalid: ", e);
      }
    }
    return t;
  }
}
