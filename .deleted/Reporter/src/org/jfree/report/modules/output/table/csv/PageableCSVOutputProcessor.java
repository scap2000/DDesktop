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
 * PageableCSVOutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.csv;

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
import org.jfree.report.modules.output.table.csv.helper.CSVOutputProcessorMetaData;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.NameGenerator;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 09.05.2007, 14:36:28
 *
 * @author Thomas Morgner
 */
public class PageableCSVOutputProcessor extends AbstractTableOutputProcessor
    implements PageableOutputProcessor
{
  private List physicalPages;
  private OutputProcessorMetaData metaData;
  private CSVPrinter printer;
  private FlowSelector flowSelector;

  public PageableCSVOutputProcessor(final Configuration configuration)
  {
    this.physicalPages = new ArrayList();
    this.metaData = new CSVOutputProcessorMetaData(configuration, CSVOutputProcessorMetaData.PAGINATION_FULL);
    this.flowSelector = new DisplayAllFlowSelector();

    this.printer = new CSVPrinter();
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
    printer.print(logicalPage, contentProducer, metaData, false);
  }

  protected void processingContentFinished()
  {
    printer.close();
  }

  public ContentLocation getContentLocation()
  {
    return printer.getContentLocation();
  }

  public void setContentLocation(final ContentLocation contentLocation)
  {
    printer.setContentLocation(contentLocation);
  }

  public NameGenerator getContentNameGenerator()
  {
    return printer.getContentNameGenerator();
  }

  public void setContentNameGenerator(final NameGenerator contentNameGenerator)
  {
    printer.setContentNameGenerator(contentNameGenerator);
  }

}
