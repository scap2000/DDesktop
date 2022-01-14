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
 * CSVTableExportTask.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.csv;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.common.StatusListener;
import org.jfree.report.modules.gui.commonswing.ReportProgressDialog;
import org.jfree.report.modules.gui.commonswing.StatusType;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.report.modules.output.table.base.StreamReportProcessor;
import org.jfree.report.modules.output.table.csv.StreamCSVOutputProcessor;
import org.jfree.report.util.i18n.Messages;
import org.jfree.util.Log;

/**
 * An export task implementation that writes an report into a CSV file, and uses the destTable
 * target to create layouted content.
 *
 * @author Thomas Morgner
 */
public class CSVTableExportTask implements Runnable
{
  /**
   * Provides access to externalized strings
   */
  private Messages messages;
  /**
   * The progress dialog that monitors the export process.
   */
  private final ReportProgressDialog progressDialog;
  /**
   * The name of the output file.
   */
  private final String fileName;

  /**
   * The report that should be exported.
   */
  private final JFreeReport report;
  private StatusListener statusListener;
  private String encoding;

  /**
   * Creates a new CSV export task.
   *
   * @param swingGuiContext the GUI Context
   * @param dialog   the progress monitor
   * @param report   the report that should be exported.
   */
  public CSVTableExportTask
          (final JFreeReport report,
           final ReportProgressDialog dialog,
           final SwingGuiContext swingGuiContext) throws ReportProcessingException
  {
    if (report == null)
    {
      throw new NullPointerException(messages.getErrorString("CSVTableExportTask.ERROR_0001_REPORT_IS_NULL")); //$NON-NLS-1$
    }

    final String filename = report.getConfiguration().getConfigProperty
        ("org.jfree.report.modules.gui.csv.FileName"); //$NON-NLS-1$
    if (filename == null)
    {
      throw new ReportProcessingException(messages.getErrorString("CSVTableExportTask.ERROR_0002_INVALID_FILANAME")); //$NON-NLS-1$
    }

    this.encoding = report.getConfiguration().getConfigProperty
        (CSVDataExportDialog.CSV_OUTPUT_ENCODING, CSVDataExportDialog.CSV_OUTPUT_ENCODING_DEFAULT);

    this.progressDialog = dialog;
    this.report = report;
    this.fileName = filename;
    if (swingGuiContext != null)
    {
      this.statusListener = swingGuiContext.getStatusListener();
      this.messages = new Messages
          (swingGuiContext.getLocale(), CSVDataExportPlugin.BASE_RESOURCE_CLASS);
    }
  }

  /**
   * Exports the report into a CSV file.
   */
  public void run()
  {
    OutputStream out = null;
    final File file = new File(fileName);
    try
    {
      final File directory = file.getAbsoluteFile().getParentFile();
      if (directory != null)
      {
        if (directory.exists() == false)
        {
          if (directory.mkdirs() == false)
          {
            Log.warn("Can't create directories. Hoping and praying now.."); //$NON-NLS-1$
          }
        }
      }
      out = new BufferedOutputStream(new FileOutputStream(file));
      final StreamCSVOutputProcessor target = new StreamCSVOutputProcessor(report.getConfiguration(), out);
      target.setEncoding(encoding);

      final StreamReportProcessor reportProcessor = new StreamReportProcessor(report, target);
      if (progressDialog != null)
      {
        progressDialog.setModal(false);
        progressDialog.setVisible(true);
        reportProcessor.addReportProgressListener(progressDialog);
      }
      reportProcessor.processReport();
      out.close();
      out = null;
      if (progressDialog != null)
      {
        reportProcessor.removeReportProgressListener(progressDialog);
      }

      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.INFORMATION, messages.getString("CSVTableExportTask.USER_TASK_COMPLETE")); //$NON-NLS-1$
      }
    }
    catch (ReportInterruptedException re)
    {
      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.INFORMATION, messages.getString("CSVTableExportTask.USER_TASK_ABORTED")); //$NON-NLS-1$
      }

      try
      {
        out.close();
        out = null;
        if (file.delete() == false)
        {
          Log.warn(new Log.SimpleMessage("Unable to delete incomplete export:", file)); //$NON-NLS-1$
        }
      }
      catch (SecurityException se)
      {
        // ignore me
      }
      catch (IOException ioe)
      {
        // ignore me...
      }
    }
    catch (Exception re)
    {
      Log.error("Exporting failed .", re); //$NON-NLS-1$
      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.ERROR, messages.getString("CSVTableExportTask.USER_TASK_FAILED")); //$NON-NLS-1$
      }
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
        Log.error("Unable to close the output stream.", e); //$NON-NLS-1$
        // if there is already another error, this exception is
        // just a minor obstactle. Something big crashed before ...
      }

      if (progressDialog != null)
      {
        progressDialog.setVisible(false);
      }
    }
  }
}
