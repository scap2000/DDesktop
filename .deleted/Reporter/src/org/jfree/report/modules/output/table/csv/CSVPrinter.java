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
 * CSVPrinter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.csv;

import java.awt.Shape;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.jfree.fonts.encoding.EncodingRegistry;
import org.jfree.report.Anchor;
import org.jfree.report.ImageContainer;
import org.jfree.report.InvalidReportStateException;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.modules.output.table.base.DefaultTextExtractor;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.base.TableContentProducer;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.util.CSVQuoter;
import org.jfree.repository.ContentCreationException;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentItem;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.NameGenerator;

/**
 * Creation-Date: 09.05.2007, 14:52:05
 *
 * @author Thomas Morgner
 */
public class CSVPrinter
{
  private ContentLocation contentLocation;
  private NameGenerator contentNameGenerator;
  private String encoding;
  private ContentItem documentContentItem;
  private PrintWriter writer;

  public CSVPrinter()
  {
    encoding = EncodingRegistry.getPlatformDefaultEncoding();
  }

  public String getEncoding()
  {
    return encoding;
  }

  public void setEncoding(final String encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException();
    }
    this.encoding = encoding;
  }

  public ContentLocation getContentLocation()
  {
    return contentLocation;
  }

  public void setContentLocation(final ContentLocation contentLocation)
  {
    this.contentLocation = contentLocation;
  }

  public NameGenerator getContentNameGenerator()
  {
    return contentNameGenerator;
  }

  public void setContentNameGenerator(final NameGenerator contentNameGenerator)
  {
    this.contentNameGenerator = contentNameGenerator;
  }

  public void print(final LogicalPageBox logicalPage,
                    final TableContentProducer contentProducer,
                    final OutputProcessorMetaData metaData,
                    final boolean incremental)
      throws ContentProcessingException
  {
    try
    {
      final String separator =
          metaData.getConfiguration().getConfigProperty
              (CSVTableModule.CONFIGURATION_PREFIX + '.' + CSVTableModule.SEPARATOR_KEY,
                  CSVTableModule.SEPARATOR_DEFAULT);

      if (separator.length() == 0)
      {
        throw new IllegalArgumentException("CSV separate cannot be an empty string.");
      }

      if (documentContentItem == null)
      {
        documentContentItem = contentLocation.createItem
            (contentNameGenerator.generateName("content", "text/csv"));
        final OutputStream out = documentContentItem.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(out, encoding));
      }


      final CSVQuoter quoter = new CSVQuoter(separator.charAt(0));
      final SheetLayout sheetLayout = contentProducer.getSheetLayout();
      final int columnCount = contentProducer.getColumnCount();
      final int lastColumn = columnCount - 1;

      final int startRow = contentProducer.getFinishedRows();
      final int finishRow = contentProducer.getFilledRows();

      for (int row = startRow; row < finishRow; row++)
      {
        for (short col = 0; col < columnCount; col++)
        {
          final RenderBox content = contentProducer.getContent(row, col);
          if (content == null)
          {
            writer.print(quoter.getSeparator());
            continue;
          }

          if (content.isCommited() == false)
          {
            throw new InvalidReportStateException("Uncommited content encountered");
          }

          final TableRectangle rectangle = sheetLayout.getTableBounds
              (content.getX(), content.getY() + contentProducer.getContentOffset(row, col),
                  content.getWidth(), content.getHeight(), null);
          if (rectangle.isOrigin(col, row) == false)
          {
            // A spanned cell ..
            writer.print(quoter.getSeparator());
            continue;
          }

          final DefaultTextExtractor textExtractor = new DefaultTextExtractor(metaData);
          final Object o = textExtractor.compute(content);
          if (o instanceof Shape == false &&
              o instanceof ImageContainer == false &&
              o instanceof Anchor == false)
          {
            writer.print(quoter.doQuoting(String.valueOf(o)));
          }
          if(col < lastColumn)
          {
            writer.print(quoter.getSeparator());
          }
          content.setFinished(true);
        }

        writer.println();

      }
      if (incremental == false)
      {
        // cleanup ..
        writer.flush();
        writer.close();

        writer = null;
        documentContentItem = null;
      }
    }
    catch(IOException e)
    {
      writer = null;
      documentContentItem = null;

      throw new ContentProcessingException("Failed to write content", e);
    }
    catch (ContentCreationException e)
    {
      writer = null;
      documentContentItem = null;

      throw new ContentProcessingException("Failed to write content", e);
    }
    catch (ContentIOException e)
    {
      writer = null;
      documentContentItem = null;

      throw new ContentProcessingException("Failed to write content", e);
    }


  }

  public void close()
  {

  }
}
