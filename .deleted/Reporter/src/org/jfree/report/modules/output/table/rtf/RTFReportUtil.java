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
 * RTFReportUtil.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.rtf;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.table.base.StreamReportProcessor;

/**
 * Utility class to provide an easy to use default implementation of RTF exports.
 *
 * @author Thomas Morgner
 */
public final class RTFReportUtil
{
  /**
   * Default Constructor.
   */
  private RTFReportUtil ()
  {
  }

  /**
   * Saves a report to rich-text format (RTF).
   *
   * @param report   the report.
   * @param filename target file name.
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException               if there was an IOerror while processing the
   *                                   report.
   */
  public static void createRTF (final JFreeReport report, final String filename)
          throws IOException, ReportProcessingException
  {
    OutputStream fout = new BufferedOutputStream(new FileOutputStream(filename));
    try
    {
      createRTF(report, fout);
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

  public static void createRTF(final JFreeReport report, final OutputStream outputStream)
      throws ReportProcessingException
  {
    final StreamRTFOutputProcessor target = new StreamRTFOutputProcessor(report.getConfiguration(), outputStream);
    final StreamReportProcessor proc = new StreamReportProcessor(report, target);
    proc.processReport();
    proc.close();
  }
}
