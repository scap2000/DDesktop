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
 * PlainTextReportUtil.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.plaintext;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.plaintext.driver.TextFilePrinterDriver;
import org.jfree.util.Configuration;

/**
 * An utility class to write an report into a plain text file. If you need more control over the writing process, you
 * will have to implement your own write method.
 *
 * @author Thomas Morgner
 */
public final class PlainTextReportUtil
{
  /**
   * Default Constructor.
   */
  private PlainTextReportUtil()
  {
  }

  /**
   * Saves a report to plain text format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException               if there was an IOerror while processing the report.
   */
  public static void createTextFile(final JFreeReport report,
                                    final String filename,
                                    final float charsPerInch,
                                    final float linesPerInch)
      throws IOException, ReportProcessingException
  {
    OutputStream fout = null;
    try
    {
      fout = new BufferedOutputStream(new FileOutputStream(filename));

      final TextFilePrinterDriver pc = new TextFilePrinterDriver(fout, charsPerInch, linesPerInch);
      final String lineSeparator = report.getReportConfiguration().getConfigProperty("line.separator", "\n");
      pc.setEndOfLine(lineSeparator.toCharArray());
      pc.setEndOfPage(lineSeparator.toCharArray());

      final PageableTextOutputProcessor outputProcessor = new PageableTextOutputProcessor(pc, report.getConfiguration());
      final PageableReportProcessor proc = new PageableReportProcessor(report, outputProcessor);
      proc.processReport();
      proc.close();
      fout.close();
      fout = null;
    }
    finally
    {
      if (fout != null)
      {
        try
        {
          fout.close();
        }
        catch (Exception e)
        {
          // ignore
        }
      }
    }
  }

  public static void createTextFile(final JFreeReport report, final String filename)
      throws IOException, ReportProcessingException
  {
    createTextFile(report, filename, 10, 6);
  }

  public static void createPlainText(final JFreeReport report,
                                     final String filename)
      throws IOException, ReportProcessingException
  {
    createPlainText(report, filename, 10, 6);
  }

  /**
   * Saves a report to plain text format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException               if there was an IOerror while processing the report.
   */
  public static void createPlainText(final JFreeReport report,
                                     final String filename,
                                     final float charsPerInch,
                                     final float linesPerInch)
      throws IOException, ReportProcessingException
  {
    OutputStream fout = null;
    try
    {
      fout = new BufferedOutputStream(new FileOutputStream(filename));
      createPlainText(report, fout, charsPerInch, linesPerInch, null);
      fout.close();
      fout = null;
    }
    finally
    {
      if (fout != null)
      {
        try
        {
          fout.close();
        }
        catch (Exception e)
        {
          // ignore
        }
      }
    }
  }

  public static void createPlainText(final JFreeReport report,
                                     final OutputStream outputStream) throws ReportProcessingException
  {
    createPlainText(report, outputStream, 10, 6, null);
  }

  public static void createPlainText(final JFreeReport report,
                                     final OutputStream outputStream,
                                     final float charsPerInch,
                                     final float linesPerInch) throws ReportProcessingException
  {
    createPlainText(report, outputStream, charsPerInch, linesPerInch, null);
  }

  public static void createPlainText(final JFreeReport report,
                                     final OutputStream outputStream,
                                     final float charsPerInch,
                                     final float linesPerInch,
                                     final String encoding) throws ReportProcessingException
  {
    final TextFilePrinterDriver pc = new TextFilePrinterDriver(outputStream, charsPerInch, linesPerInch);
    final PageableTextOutputProcessor outputProcessor = new PageableTextOutputProcessor(pc, report.getConfiguration());
    outputProcessor.setEncoding(encoding);

    final PageableReportProcessor proc = new PageableReportProcessor(report, outputProcessor);
    proc.processReport();
    proc.close();
  }

  public static byte[] getInitSequence(final Configuration report)
      throws UnsupportedEncodingException
  {
    final String encoding = report.getConfigProperty
        ("org.jfree.report.modules.output.pageable.plaintext.RawEncoding", "Raw");
    final String sequence = report.getConfigProperty
        ("org.jfree.report.modules.output.pageable.plaintext.RawInitSequence");
    if (sequence == null)
    {
      return null;
    }
    if ("Raw".equalsIgnoreCase(encoding))
    {
      final char[] rawChars = sequence.toCharArray();
      final byte[] rawBytes = new byte[rawChars.length];
      for (int i = 0; i < rawBytes.length; i++)
      {
        rawBytes[i] = (byte) rawChars[i];
      }
      return rawBytes;
    }
    else
    {
      return sequence.getBytes(encoding);
    }
  }

}
