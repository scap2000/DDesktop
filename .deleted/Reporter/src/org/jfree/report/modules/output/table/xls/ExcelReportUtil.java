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
 * ExcelReportUtil.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.xls;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.table.base.FlowReportProcessor;

/**
 * Utility class to provide an easy to use default implementation of excel exports.
 *
 * @author Thomas Morgner
 */
public final class ExcelReportUtil
{
  /**
   * DefaultConstructor.
   */
  private ExcelReportUtil ()
  {
  }

  /**
   * Saves a report to Excel format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException               if there was an IOerror while processing the
   *                                   report.
   */
  public static void createXLS (final JFreeReport report, final String filename)
          throws IOException, ReportProcessingException
  {
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    try
    {
      final FlowExcelOutputProcessor target = new FlowExcelOutputProcessor(report.getConfiguration(), fout);
      final FlowReportProcessor reportProcessor = new FlowReportProcessor(report, target);
      reportProcessor.processReport();
      reportProcessor.close();
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
        catch(Exception e)
        {
          // ignore
        }
      }
    }
  }


  /**
   * Saves a report to Excel format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException               if there was an IOerror while processing the
   *                                   report.
   */
  public static void createXLS (final JFreeReport report,
                                final String filename,
                                final boolean strict)
          throws IOException, ReportProcessingException
  {
    report.getReportConfiguration().setConfigProperty
        ("org.jfree.report.modules.output.table.base.StrictLayout", String.valueOf(strict));

    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    try
    {
      final FlowExcelOutputProcessor target = new FlowExcelOutputProcessor(report.getConfiguration(), fout);
      final FlowReportProcessor reportProcessor = new FlowReportProcessor(report, target);
      reportProcessor.processReport();
      reportProcessor.close();
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
        catch(Exception e)
        {
          // ignore
        }
      }
    }
  }

  public static void createXLS(final JFreeReport report, final OutputStream outputStream)
      throws ReportProcessingException
  {
    final FlowExcelOutputProcessor target = new FlowExcelOutputProcessor(report.getConfiguration(), outputStream);
    final FlowReportProcessor reportProcessor = new FlowReportProcessor(report, target);
    reportProcessor.processReport();
    reportProcessor.close();
  }
}
