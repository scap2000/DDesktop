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
 * CSVDataReportUtil.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.csv;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;

/**
 * Utility class to provide an easy to use default implementation of CSV destTable exports.
 *
 * @author Thomas Morgner
 */
public final class CSVDataReportUtil
{
  /**
   * DefaultConstructor.
   */
  private CSVDataReportUtil ()
  {
  }

  /**
   * Saves a report to CSV format.
   *
   * @param report   the report.
   * @param writer   the writer
   * @throws org.jfree.report.ReportProcessingException if the report processing failed.
   */
  public static void createCSV (final JFreeReport report, final Writer writer)
          throws ReportProcessingException
  {
    final CSVProcessor pr = new CSVProcessor(report);
    pr.setWriter(writer);
    pr.processReport();
  }

  /**
   * Saves a report to CSV format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws org.jfree.report.ReportProcessingException if the report processing failed.
   * @throws java.io.IOException               if there was an IOerror while processing the
   *                                   report.
   */
  public static void createCSV (final JFreeReport report, final String filename)
          throws ReportProcessingException, IOException
  {
    final CSVProcessor pr = new CSVProcessor(report);

    final Writer fout = new BufferedWriter(new FileWriter(filename));
    pr.setWriter(fout);
    pr.processReport();
    fout.close();
  }

  /**
   * Saves a report to CSV format.
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws org.jfree.report.ReportProcessingException if the report processing failed.
   * @throws java.io.IOException               if there was an IOerror while processing the
   *                                   report.
   */
  public static void createCSV (final JFreeReport report,
                                final String filename,
                                final String encoding)
          throws ReportProcessingException, IOException
  {
    final CSVProcessor pr = new CSVProcessor(report);
    final FileOutputStream outstr = new FileOutputStream(filename);
    final Writer fout = new BufferedWriter(new OutputStreamWriter(outstr, encoding));
    pr.setWriter(fout);
    pr.processReport();
    fout.close();
  }


}
