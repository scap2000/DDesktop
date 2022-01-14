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
 * GraphicsOutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.graphics.internal;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PageGrid;
import org.jfree.report.layout.model.PhysicalPageBox;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.output.PhysicalPageKey;
import org.jfree.report.modules.output.pageable.base.AbstractPageableOutputProcessor;
import org.jfree.report.modules.output.pageable.base.PageFlowSelector;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 02.01.2006, 19:55:14
 *
 * @author Thomas Morgner
 */
public class GraphicsOutputProcessor extends AbstractPageableOutputProcessor
{
  private OutputProcessorMetaData metaData;
  private GraphicsContentInterceptor interceptor;

  public GraphicsOutputProcessor(final Configuration configuration)
  {
    final DefaultFontStorage fontStorage = new DefaultFontStorage(new AWTFontRegistry());
    metaData = new GraphicsOutputProcessorMetaData(configuration, fontStorage);
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public GraphicsContentInterceptor getInterceptor()
  {
    return interceptor;
  }

  public void setInterceptor(final GraphicsContentInterceptor interceptor)
  {
    this.interceptor = interceptor;
  }


  protected final PageFlowSelector getFlowSelector()
  {
    return getInterceptor();
  }

  protected void processPhysicalPage(final PageGrid pageGrid,
                                     final LogicalPageBox logicalPage,
                                     final int row,
                                     final int col,
                                     final PhysicalPageKey pageKey)
  {
    final PhysicalPageBox page = pageGrid.getPage(row, col);
    if (page != null)
    {
      final LogicalPageDrawable drawable = new LogicalPageDrawable(logicalPage, metaData);
      final PhysicalPageDrawable pageDrawable = new PhysicalPageDrawable(drawable, page);
      interceptor.processPhysicalPage(pageKey, pageDrawable);
    }
  }

  protected void processLogicalPage (final LogicalPageKey key, final LogicalPageBox logicalPage)
  {
    final LogicalPageDrawable drawable = new LogicalPageDrawable(logicalPage, metaData);
    interceptor.processLogicalPage(key, drawable);
  }
}
