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
 * StreamRTFOutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.rtf;

import java.io.InputStream;
import java.io.OutputStream;

import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.DisplayAllFlowSelector;
import org.jfree.report.layout.output.FlowSelector;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.modules.output.table.base.AbstractTableOutputProcessor;
import org.jfree.report.modules.output.table.base.TableContentProducer;
import org.jfree.report.modules.output.table.rtf.helper.RTFOutputProcessorMetaData;
import org.jfree.report.modules.output.table.rtf.helper.RTFPrinter;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 09.05.2007, 14:36:28
 *
 * @author Thomas Morgner
 */
public class StreamRTFOutputProcessor extends AbstractTableOutputProcessor
{
  private OutputProcessorMetaData metaData;
  private FlowSelector flowSelector;
  private RTFPrinter printer;

  public StreamRTFOutputProcessor(final Configuration config,
                                  final OutputStream outputStream)
  {
    this.metaData = new RTFOutputProcessorMetaData(config, RTFOutputProcessorMetaData.PAGINATION_NONE);
    this.flowSelector = new DisplayAllFlowSelector();

    this.printer = new RTFPrinter();
    this.printer.init(config, outputStream);
  }

  public InputStream getTemplateInputStream()
  {
    return printer.getTemplateInputStream();
  }

  public void setTemplateInputStream(final InputStream templateInputStream)
  {
    printer.setTemplateInputStream(templateInputStream);
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  protected FlowSelector getFlowSelector()
  {
    return flowSelector;
  }

  protected void processTableContent(final LogicalPageKey logicalPageKey,
                                     final LogicalPageBox logicalPage,
                                     final TableContentProducer contentProducer) throws ContentProcessingException
  {
    printer.print(logicalPageKey, logicalPage, contentProducer, metaData, false);
  }

  protected void updateTableContent(final LogicalPageKey logicalPageKey,
                                    final LogicalPageBox logicalPageBox,
                                    final TableContentProducer tableContentProducer,
                                    final boolean performOutput) throws ContentProcessingException
  {
    printer.print(logicalPageKey, logicalPageBox, tableContentProducer, metaData, true);
  }

  protected void processingContentFinished()
  {
    if (isContentGeneratable() == false)
    {
      return;
    }

    this.printer.close();
  }

}
