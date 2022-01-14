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
 * PageableTextOutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.plaintext;

import org.jfree.fonts.encoding.EncodingRegistry;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PageGrid;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.output.PhysicalPageKey;
import org.jfree.report.modules.output.pageable.base.AbstractPageableOutputProcessor;
import org.jfree.report.modules.output.pageable.base.AllPageFlowSelector;
import org.jfree.report.modules.output.pageable.base.PageFlowSelector;
import org.jfree.report.modules.output.pageable.plaintext.driver.PrinterDriver;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 13.05.2007, 13:06:24
 *
 * @author Thomas Morgner
 */
public class PageableTextOutputProcessor extends AbstractPageableOutputProcessor
{
  private TextDocumentWriter writer;
  private OutputProcessorMetaData metaData;
  private PrinterDriver driver;
  private PageFlowSelector flowSelector;
  private String encoding;

  public PageableTextOutputProcessor(final PrinterDriver driver,
                                     final Configuration configuration)
  {
    this.driver = driver;
    this.metaData = new TextOutputProcessorMetaData
        (configuration, driver.getLinesPerInch(), driver.getCharactersPerInch());
    this.flowSelector = new AllPageFlowSelector();
  }

  public String getEncoding()
  {
    return encoding;
  }

  public void setEncoding(final String encoding)
  {
    this.encoding = encoding;
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public PageFlowSelector getFlowSelector()
  {
    return flowSelector;
  }

  public void setFlowSelector(final PageFlowSelector flowSelector)
  {
    if (flowSelector == null)
    {
      throw new NullPointerException();
    }

    this.flowSelector = flowSelector;
  }

  protected void processingContentFinished()
  {
    if (writer != null)
    {
      writer.close();
    }
  }

  protected void processPhysicalPage(final PageGrid pageGrid,
                                     final LogicalPageBox logicalPage,
                                     final int row,
                                     final int col,
                                     final PhysicalPageKey pageKey)
      throws ContentProcessingException
  {
    try
    {
      if (writer == null)
      {
        if (encoding == null)
        {
          final String encoding = metaData.getConfiguration().getConfigProperty
              ("org.jfree.report.modules.output.pageable.plaintext.Encoding",
                  EncodingRegistry.getPlatformDefaultEncoding());

          writer = new TextDocumentWriter(metaData, driver, encoding);
        }
        else
        {
          writer = new TextDocumentWriter(metaData, driver, this.encoding);
        }

        writer.open();

        final byte[] sequence = PlainTextReportUtil.getInitSequence(metaData.getConfiguration());
        if (sequence != null)
        {
          driver.printRaw(sequence);
        }

      }
      writer.processPhysicalPage(pageGrid,  logicalPage, row, col, pageKey);
    }
    catch(Exception e)
    {
      throw new ContentProcessingException("Failed to generate the PlainText document", e);
    }
  }

  protected void processLogicalPage(final LogicalPageKey key,
                                    final LogicalPageBox logicalPage)
      throws ContentProcessingException
  {
    try
    {
      if (writer == null)
      {
        if (encoding == null)
        {
          final String encoding = metaData.getConfiguration().getConfigProperty
              ("org.jfree.report.modules.output.pageable.plaintext.Encoding",
                  EncodingRegistry.getPlatformDefaultEncoding());

          writer = new TextDocumentWriter(metaData, driver, encoding);
        }
        else
        {
          writer = new TextDocumentWriter(metaData, driver, this.encoding);
        }
        writer.open();
      }
      writer.processLogicalPage(key, logicalPage);
    }
    catch(Exception e)
    {
      throw new ContentProcessingException("Failed to generate the PlainText document", e);
    }
  }
}
