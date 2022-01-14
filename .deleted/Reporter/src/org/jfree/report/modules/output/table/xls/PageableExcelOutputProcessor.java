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
 * PageableExcelOutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.xls;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.DisplayAllFlowSelector;
import org.jfree.report.layout.output.FlowSelector;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.output.PhysicalPageKey;
import org.jfree.report.modules.output.pageable.base.PageableOutputProcessor;
import org.jfree.report.modules.output.table.base.AbstractTableOutputProcessor;
import org.jfree.report.modules.output.table.base.TableContentProducer;
import org.jfree.report.modules.output.table.xls.helper.ExcelOutputProcessorMetaData;
import org.jfree.report.modules.output.table.xls.helper.ExcelPrinter;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 09.05.2007, 14:36:28
 *
 * @author Thomas Morgner
 */
public class PageableExcelOutputProcessor extends AbstractTableOutputProcessor
    implements PageableOutputProcessor
{
  private List physicalPages;
  private OutputProcessorMetaData metaData;
  private ExcelPrinter printer;
  private FlowSelector flowSelector;

  public PageableExcelOutputProcessor(final Configuration configuration,
                                      final OutputStream outputStream)
  {
    this.physicalPages = new ArrayList();
    this.metaData = new ExcelOutputProcessorMetaData(configuration, ExcelOutputProcessorMetaData.PAGINATION_FULL);
    this.flowSelector = new DisplayAllFlowSelector();

    this.printer = new ExcelPrinter();
    this.printer.init(configuration, metaData, outputStream);
  }

  public InputStream getTemplateInputStream()
  {
    return printer.getTemplateInputStream();
  }

  public void setTemplateInputStream(final InputStream templateInputStream)
  {
    printer.setTemplateInputStream(templateInputStream);
  }


  protected void processingPagesFinished()
  {
    super.processingPagesFinished();
    physicalPages = Collections.unmodifiableList(physicalPages);
  }

  public int getPhysicalPageCount()
  {
    return physicalPages.size();
  }

  public PhysicalPageKey getPhysicalPage(final int page)
  {
    if (isPaginationFinished() == false)
    {
      throw new IllegalStateException();
    }

    return (PhysicalPageKey) physicalPages.get(page);
  }

  protected LogicalPageKey createLogicalPage(final int width,
                                             final int height)
  {
    final LogicalPageKey key = super.createLogicalPage(width, height);
    for (int h = 0; h < key.getHeight(); h++)
    {
      for (int w = 0; w < key.getWidth(); w++)
      {
        physicalPages.add(key.getPage(w, h));
      }
    }
    return key;
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public FlowSelector getFlowSelector()
  {
    return flowSelector;
  }

  public void setFlowSelector(final FlowSelector flowSelector)
  {
    this.flowSelector = flowSelector;
  }

  protected void processTableContent(final LogicalPageKey logicalPageKey,
                                     final LogicalPageBox logicalPage,
                                     final TableContentProducer contentProducer)
       throws ContentProcessingException
  {
    printer.print(logicalPageKey, logicalPage, contentProducer, false);
  }

  protected void processingContentFinished()
  {
    printer.close();
  }
}
