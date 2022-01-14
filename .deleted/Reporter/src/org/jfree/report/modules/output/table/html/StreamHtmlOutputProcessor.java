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
 * StreamHtmlOutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html;

import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.DisplayAllFlowSelector;
import org.jfree.report.layout.output.FlowSelector;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.modules.output.table.base.AbstractTableOutputProcessor;
import org.jfree.report.modules.output.table.base.TableContentProducer;
import org.jfree.report.modules.output.table.html.helper.HtmlOutputProcessorMetaData;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 04.05.2007, 16:36:40
 *
 * @author Thomas Morgner
 */
public class StreamHtmlOutputProcessor extends AbstractTableOutputProcessor
    implements HtmlOutputProcessor
{
  private HtmlPrinter printer;
  private OutputProcessorMetaData metaData;
  private FlowSelector flowSelector;

  public StreamHtmlOutputProcessor(final Configuration configuration)
  {
    this.metaData = new HtmlOutputProcessorMetaData
        (configuration, HtmlOutputProcessorMetaData.PAGINATION_NONE);
    this.flowSelector = new DisplayAllFlowSelector();
  }

  protected FlowSelector getFlowSelector()
  {
    return flowSelector;
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public HtmlPrinter getPrinter()
  {
    return printer;
  }

  public void setPrinter(final HtmlPrinter printer)
  {
    this.printer = printer;
  }

  protected void processTableContent(final LogicalPageKey logicalPageKey,
                                     final LogicalPageBox logicalPage,
                                     final TableContentProducer contentProducer)
       throws ContentProcessingException
  {
    if (printer == null)
    {
      return;
    }

    printer.print(logicalPageKey, logicalPage, contentProducer, metaData, false);
  }


  protected void updateTableContent(final LogicalPageKey logicalPageKey,
                                    final LogicalPageBox logicalPageBox,
                                    final TableContentProducer tableContentProducer,
                                    final boolean performOutput) throws ContentProcessingException
  {
    if (printer == null)
    {
      return;
    }

    printer.print(logicalPageKey, logicalPageBox, tableContentProducer, metaData, true);
  }
}
