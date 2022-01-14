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
 * URLObjectDescription.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.base;

import java.net.URL;

import org.jfree.io.IOUtils;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.parser.AbstractXmlResourceFactory;

/**
 * An object-description for a <code>URL</code> object.
 *
 * @author Thomas Morgner
 */
public class URLObjectDescription extends AbstractObjectDescription
{

  /**
   * Creates a new object description.
   */
  public URLObjectDescription()
  {
    super(URL.class);
    setParameterDefinition("value", String.class);
  }

  /**
   * Creates an object based on this description.
   *
   * @return The object.
   */
  public Object createObject()
  {
    final String o = (String) getParameter("value");
    final String baseURL = getConfig().getConfigProperty(AbstractXmlResourceFactory.CONTENTBASE_KEY);

    try
    {
      if (baseURL != null)
      {
        try
        {
          final URL bURL = new URL(baseURL);
          return new URL(bURL, o);
        }
        catch (Exception e)
        {
          Log.warn("BaseURL is invalid: " + baseURL);
        }
      }
      return new URL(o);
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * Sets the parameters of this description object to match the supplied
   * object.
   *
   * @param o the object (should be an instance of <code>URL</code>).
   * @throws ObjectFactoryException if the object is not an instance of
   *                                <code>URL</code>.
   */
  public void setParameterFromObject(final Object o)
      throws ObjectFactoryException
  {
    if (!(o instanceof URL))
    {
      throw new ObjectFactoryException("Is no instance of java.net.URL");
    }

    final URL comp = (URL) o;
    final String baseURL = getConfig().getConfigProperty(AbstractXmlResourceFactory.CONTENTBASE_KEY);
    try
    {
      final URL bURL = new URL(baseURL);
      if (ObjectUtilities.equal(bURL, comp))
      {
        setParameter("value", null);
      }
      else
      {
        setParameter("value", IOUtils.getInstance().createRelativeURL(comp, bURL));
      }
    }
    catch (Exception e)
    {
      Log.warn("BaseURL is invalid: ", e);
      setParameter("value", comp.toExternalForm());
    }
  }

}
