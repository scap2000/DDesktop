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
 * ReportResource.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.base;

import org.jfree.resourceloader.CompoundResource;
import org.jfree.resourceloader.DependencyCollector;
import org.jfree.resourceloader.ResourceException;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: Dec 17, 2006, 5:08:21 PM
 *
 * @author Thomas Morgner
 */
public class ReportResource extends CompoundResource
{
  private boolean cloneable;

  public ReportResource(final ResourceKey source,
                        final DependencyCollector dependencies, final Object product)
  {
    super(source, dependencies, product);
    if (product instanceof Cloneable)
    {
      cloneable = true;
    }

  }

  public long getVersion(final ResourceKey key)
  {
    if (cloneable)
    {
      return super.getVersion(key);
    }
    return -1;
  }

  public Object getResource() throws ResourceException
  {
    try
    {
      final Object resource = super.getResource();
      if (cloneable)
      {
        return ObjectUtilities.clone(resource);
      }
      return resource;
    }
    catch (CloneNotSupportedException e)
    {

      throw new ResourceException("Unable to retrieve the resource.", e);
    }
  }
}
