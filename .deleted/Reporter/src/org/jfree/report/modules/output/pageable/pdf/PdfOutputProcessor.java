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
 * PdfOutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.pdf;

import java.io.OutputStream;

import org.jfree.fonts.encoding.EncodingRegistry;
import org.jfree.fonts.itext.ITextFontStorage;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.PageGrid;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.output.PhysicalPageKey;
import org.jfree.report.modules.output.pageable.base.AbstractPageableOutputProcessor;
import org.jfree.report.modules.output.pageable.base.AllPageFlowSelector;
import org.jfree.report.modules.output.pageable.base.PageFlowSelector;
import org.jfree.report.modules.output.pageable.pdf.internal.PdfDocumentWriter;
import org.jfree.report.modules.output.pageable.pdf.internal.PdfOutputProcessorMetaData;
import org.jfree.report.modules.output.support.itext.BaseFontModule;
import org.jfree.util.Configuration;

/**
 * A streaming target, which produces a PDF document.
 *
 * @author Thomas Morgner
 */
public class PdfOutputProcessor extends AbstractPageableOutputProcessor
{
  private PdfOutputProcessorMetaData metaData;
  private PageFlowSelector flowSelector;
  private OutputStream outputStream;
  private PdfDocumentWriter writer;

  public PdfOutputProcessor(final Configuration configuration,
                            final OutputStream outputStream)
  {
    if (configuration == null)
    {
      throw new NullPointerException("Configuration must not be null");
    }
    if (outputStream == null)
    {
      throw new NullPointerException("OutputStream must not be null");
    }

    this.outputStream = outputStream;
    this.flowSelector = new AllPageFlowSelector();

    // for the sake of simplicity, we use the AWT font registry for now.
    // This is less accurate than using the iText fonts, but completing
    // the TrueType registry or implementing an iText registry is too expensive
    // for now.
    final String encoding = configuration.getConfigProperty
        ("org.jfree.report.modules.output.pageable.pdf.Encoding", EncodingRegistry.getPlatformDefaultEncoding());
    final ITextFontStorage fontStorage = new ITextFontStorage(BaseFontModule.getFontRegistry(), encoding);

    metaData = new PdfOutputProcessorMetaData(configuration, fontStorage);

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
        writer = new PdfDocumentWriter(metaData, outputStream);
        writer.open();
      }
      writer.processPhysicalPage(pageGrid,  logicalPage, row, col, pageKey);
    }
    catch(Exception e)
    {
      throw new ContentProcessingException("Failed to generate PDF document", e);
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
        writer = new PdfDocumentWriter(metaData, outputStream);
        writer.open();
      }
      writer.processLogicalPage(key, logicalPage);
    }
    catch(Exception e)
    {
      throw new ContentProcessingException("Failed to generate PDF document", e);
    }
  }

}
