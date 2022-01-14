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
 * QueryPhysicalPageInterceptor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.graphics.internal;

import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.PhysicalPageKey;
import org.jfree.report.modules.output.pageable.graphics.PageDrawable;

/**
 * Creation-Date: 10.11.2006, 20:41:29
 *
 * @author Thomas Morgner
 */
public class QueryPhysicalPageInterceptor implements GraphicsContentInterceptor
{
  private PageDrawable drawable;
  private PhysicalPageKey pageKey;

  public QueryPhysicalPageInterceptor(final PhysicalPageKey pageKey)
  {
    this.pageKey = pageKey;
  }

  public boolean isLogicalPageAccepted(final LogicalPageKey key)
  {
    return false;
  }

  public void processLogicalPage(final LogicalPageKey key, final PageDrawable page)
  {
  }

  public boolean isPhysicalPageAccepted(final PhysicalPageKey key)
  {
    return pageKey.equals(key);
  }

  public void processPhysicalPage(final PhysicalPageKey key, final PageDrawable page)
  {
    this.drawable = page;
  }

  public boolean isMoreContentNeeded()
  {
    return drawable == null;
  }

  public PageDrawable getDrawable()
  {
    return drawable;
  }
}
