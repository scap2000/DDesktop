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
 * PDFReportUtil.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.util.Log;

/**
 * Utility class to provide an easy to use default implementation of PDF exports.
 *
 * @author Thomas Morgner
 * @author Cedric Pronzato
 */
public final class PdfReportUtil
{
  /**
   * DefaultConstructor.
   */
  private PdfReportUtil()
  {
  }

  /**
   * Saves a report to PDF format.
   *
   * @param report   the report.
   * @param fileName target file.
   * @return true if the report has been successfully exported, false otherwise.
   */
  public static boolean createPDF(final JFreeReport report,
                                  final File fileName)
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(fileName));
      createPDF(report, out);
      out.close();
      out = null;
      return true;
    }
    catch (Exception e)
    {
      Log.error("Writing PDF failed.", e);
      return false;
    }
    finally
    {
      try
      {
        if (out != null)
        {
          out.close();
        }
      }
      catch (Exception e)
      {
        Log.error("Saving PDF failed.", e);
      }
    }
  }

  /**
   * Saves a report to PDF format.
   *
   * @param report the report.
   * @param out    target output stream.
   * @return true if the report has been successfully exported, false otherwise.
   */
  public static boolean createPDF(final JFreeReport report,
                                  final OutputStream out)
  {
    PageableReportProcessor proc = null;
    try
    {

      final PdfOutputProcessor outputProcessor = new PdfOutputProcessor(report.getConfiguration(), out);
      proc = new PageableReportProcessor(report, outputProcessor);
      proc.processReport();
      return true;
    }
    catch (Exception e)
    {
      Log.error("Writing PDF failed.", e);
      return false;
    }
    finally
    {
      if (proc != null)
      {
        proc.close();
      }
    }
  }

  /**
   * Concates and saves a list of reports to PDF format.
   *
   * @param report   the report.
   * @param fileName target file name.
   * @return true if the report has been successfully exported, false otherwise.
   */
  public static boolean createPDF(final JFreeReport report,
                                  final String fileName)
  {
    return createPDF(report, new File(fileName));
  }

  /**
   * Saves a report to PDF format.
   *
   * @param reports  the report list.
   * @param fileName target file.
   * @return true if the report has been successfully exported, false otherwise.
   * @deprecated use SubReports instead.
   */
  public static boolean concatPDF(final JFreeReport[] reports,
                                  final File fileName)
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(fileName));
      concatPDF(reports, out);
      out.close();
      out = null;
      return true;
    }
    catch (Exception e)
    {
      Log.error("Writing PDF failed.", e);
      return false;
    }
    finally
    {
      try
      {
        if (out != null)
        {
          out.close();
        }
      }
      catch (Exception e)
      {
        Log.error("Saving PDF failed.", e);
      }
    }
  }

  /**
   * Concates and saves a list of reports to PDF format.
   *
   * @param reports  the report list.
   * @param fileName target file name.
   * @return true if the report has been successfully exported, false otherwise.
   * @deprecated use SubReports instead.
   */
  public static boolean concatPDF(final JFreeReport[] reports,
                                  final String fileName)
  {
    return concatPDF(reports, new File(fileName));
  }

  /**
   * Concates and saves a list of reports to PDF format.
   *
   * @param reports the report list.
   * @param out     target output stream.
   * @return true if the report has been successfully exported, false otherwise.
   * @deprecated use SubReports instead.
   */
  public static boolean concatPDF(final JFreeReport[] reports, final OutputStream out)
  {
    try
    {
      final ArrayList masterBookmarkList = new ArrayList();
      Document document = null;
      PdfCopy writer = null;
      int pageOffset = 0;
      //int f = 0;
      for (int index = 0; index < reports.length; index++)
      {
        final File tempFile = File.createTempFile("concat", "pdf");
        final JFreeReport report = reports[index];
        createPDF(report, tempFile);

        final FileInputStream inputStream = new FileInputStream(tempFile);
        final PdfReader reader = new PdfReader(inputStream);
        reader.consolidateNamedDestinations();

        // we retrieve the total number of pages
        final int n = reader.getNumberOfPages();
        final List bookmarks = SimpleBookmark.getBookmark(reader);
        if (bookmarks != null)
        {
          if (pageOffset != 0)
          {
            SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
          }
          masterBookmarkList.addAll(bookmarks);
        }
        pageOffset += n;


        if (index == 0)
        {
          // step 1: creation of a document-object
          document = new Document(reader.getPageSizeWithRotation(1));
          // step 2: we create a writer that listens to the document
          writer = new PdfCopy(document, out);
          // step 3: we open the document
          document.open();
        }

        // step 4: we add content
        for (int i = 1; i <= n; i += 1)
        {
          final PdfImportedPage page = writer.getImportedPage(reader, i);
          writer.addPage(page);

        }
        writer.freeReader(reader);
        tempFile.delete();
      }

      if (!masterBookmarkList.isEmpty())
      {
        writer.setOutlines(masterBookmarkList);
      }
      // step 5: we close the document
      document.close();


      return true;
    }
    catch (Exception e)
    {
      Log.error("Writing PDF failed.", e);
      return false;
    }

  }
}
