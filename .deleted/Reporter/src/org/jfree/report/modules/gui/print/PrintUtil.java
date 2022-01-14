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
 * PrintUtil.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.print;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportProgressListener;
import org.jfree.report.modules.output.pageable.graphics.PrintReportProcessor;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * Creation-Date: 05.09.2005, 18:36:03
 *
 * @author Thomas Morgner
 */
public class PrintUtil
{
  public static final String PRINTER_JOB_NAME_KEY =
      "org.jfree.report.modules.output.pageable.graphics.print.JobName"; //$NON-NLS-1$
  public static final String NUMBER_COPIES_KEY =
      "org.jfree.report.modules.output.pageable.graphics.print.NumberOfCopies"; //$NON-NLS-1$

  private PrintUtil()
  {
  }

  public static void printDirectly(final JFreeReport report)
      throws PrinterException, ReportProcessingException
  {
    print(report, null);
  }

  public static void printDirectly(final JFreeReport report, final ReportProgressListener progressListener)
      throws PrinterException, ReportProcessingException
  {
    final ModifiableConfiguration reportConfiguration = report.getReportConfiguration();
    final String jobName = reportConfiguration.getConfigProperty
        (PRINTER_JOB_NAME_KEY, report.getName());

    final PrinterJob printerJob = PrinterJob.getPrinterJob();
    printerJob.setJobName(jobName);

    final PrintReportProcessor reportPane = new PrintReportProcessor(report);
    if (progressListener != null)
    {
      reportPane.addReportProgressListener(progressListener);
    }
    printerJob.setPageable(reportPane);
    try
    {
      printerJob.setCopies(getNumberOfCopies(reportConfiguration));
      printerJob.print();
    }
    finally
    {
      reportPane.close();
      if (progressListener != null)
      {
        reportPane.removeReportProgressListener(progressListener);
      }
    }
  }

  public static boolean print(final JFreeReport report)
      throws PrinterException, ReportProcessingException
  {
    return print(report, null);
  }

  public static boolean print(final JFreeReport report, final ReportProgressListener progressListener)
      throws PrinterException, ReportProcessingException
  {
    final ModifiableConfiguration reportConfiguration = report.getReportConfiguration();
    final String jobName = reportConfiguration.getConfigProperty(PRINTER_JOB_NAME_KEY, report.getName());

    final PrinterJob printerJob = PrinterJob.getPrinterJob();
    printerJob.setJobName(jobName);

    final PrintReportProcessor reportPane = new PrintReportProcessor(report);
    if (progressListener != null)
    {
      reportPane.addReportProgressListener(progressListener);
    }

    try
    {
      printerJob.setPageable(reportPane);
      printerJob.setCopies(getNumberOfCopies(reportConfiguration));
      if (printerJob.printDialog())
      {
        printerJob.print();
        return true;
      }
      return false;
    }
    finally
    {
      reportPane.close();
      if (progressListener != null)
      {
        reportPane.removeReportProgressListener(progressListener);
      }
    }
  }

  public static int getNumberOfCopies(final Configuration configuration)
  {
    try
    {
      return Math.max(1, Integer.parseInt
          (configuration.getConfigProperty(NUMBER_COPIES_KEY, "1"))); //$NON-NLS-1$
    }
    catch (Exception e)
    {
      Log.warn("PrintUtil: Number of copies declared for the report is invalid"); //$NON-NLS-1$
      return 1;
    }
  }
}
