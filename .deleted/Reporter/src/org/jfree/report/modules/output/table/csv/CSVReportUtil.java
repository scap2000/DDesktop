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
 * CSVReportUtil.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.csv;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.fonts.encoding.EncodingRegistry;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.table.base.StreamReportProcessor;

/**
 * Utility class to provide an easy to use default implementation of CSV destTable exports.
 *
 * @author Thomas Morgner
 */
public final class CSVReportUtil
{
  /**
   * DefaultConstructor.
   */
  private CSVReportUtil()
  {
  }

  /**
   * Saves a report to CSV format.
   *
   * @param report the report.
   * @throws ReportProcessingException if the report processing failed.
   */
  public static void createCSV(final JFreeReport report,
                               final OutputStream outputStream,
                               final String encoding)
      throws ReportProcessingException, IOException
  {
    final StreamCSVOutputProcessor target = new StreamCSVOutputProcessor(report.getConfiguration(), outputStream);
    if (encoding != null)
    {
      target.setEncoding(encoding);
    }

    final StreamReportProcessor reportProcessor = new StreamReportProcessor(report, target);
    reportProcessor.processReport();
    reportProcessor.close();
    outputStream.flush();
  }

  /**
   * Saves a report to CSV format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException               if there was an IOerror while processing the report.
   */
  public static void createCSV(final JFreeReport report, final String filename)
      throws ReportProcessingException, IOException
  {
    createCSV(report, filename, EncodingRegistry.getPlatformDefaultEncoding());
  }


  /**
   * Saves a report to CSV format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws org.jfree.report.ReportProcessingException
   *                             if the report processing failed.
   * @throws java.io.IOException if there was an IOerror while processing the report.
   */
  public static void createCSV(final JFreeReport report,
                               final String filename,
                               final String encoding)
      throws ReportProcessingException, IOException
  {
    final OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    try
    {
      createCSV(report, fout, encoding);
    }
    finally
    {
      fout.close();
    }
  }


}
