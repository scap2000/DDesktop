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
 * ExcelExportTask.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.xls;

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
import org.jfree.report.modules.gui.pdf.PdfExportPlugin;
import org.jfree.report.modules.output.table.base.FlowReportProcessor;
import org.jfree.report.modules.output.table.xls.FlowExcelOutputProcessor;
import org.jfree.report.util.i18n.Messages;
import org.jfree.util.Log;

/**
 * An export task implementation, which writes a given report into an Excel file.
 *
 * @author Thomas Morgner
 */
public class ExcelExportTask implements Runnable
{
  /**
   * Access to externalized strings 
   */
  private Messages messages;
  
  /**
   * The progress dialog that will be used to visualize the report progress.
   */
  private final ReportProgressDialog progressDialog;
  /**
   * The file name of the output file.
   */
  private final String fileName;
  /**
   * The report which should be exported.
   */
  private final JFreeReport report;
  private StatusListener statusListener;

  /**
   * Creates a new export task.
   *
   * @param dialog   the progress dialog that will monitor the report progress.
   * @param report   the report that should be exported.
   */
  public ExcelExportTask
          (final JFreeReport report,
           final ReportProgressDialog dialog,
           final SwingGuiContext swingGuiContext) throws ReportProcessingException
  {
    if (report == null)
    {
      throw new NullPointerException("ExcelExportTask(): Null report parameter not permitted"); //$NON-NLS-1$
    }
    this.fileName = report.getConfiguration().getConfigProperty
        ("org.jfree.report.modules.gui.xls.FileName"); //$NON-NLS-1$
    if (fileName == null)
    {
      throw new ReportProcessingException(messages.getErrorString("ExcelExportTask(): Filename is not defined")); //$NON-NLS-1$
    }
    this.progressDialog = dialog;
    this.report = report;
    if (swingGuiContext != null)
    {
      this.statusListener = swingGuiContext.getStatusListener();
      this.messages = new Messages
          (swingGuiContext.getLocale(), PdfExportPlugin.BASE_RESOURCE_CLASS);
    }
  }

  /**
   * Exports the report into an Excel file.
   */
  public void run()
  {
    OutputStream out = null;
    final File file = new File(fileName);
    try
    {
      final File directory = file.getParentFile();
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
      final FlowExcelOutputProcessor target = new FlowExcelOutputProcessor(report.getConfiguration(), out);
      final FlowReportProcessor reportProcessor = new FlowReportProcessor(report, target);
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
        statusListener.setStatus(StatusType.INFORMATION, messages.getString("ExcelExportTask.USER_TASK_FINISHED")); //$NON-NLS-1$
      }

    }
    catch (ReportInterruptedException re)
    {
      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.WARNING, messages.getString("ExcelExportTask.USER_TASK_ABORTED")); //$NON-NLS-1$
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
      Log.error("Excel export failed", re); //$NON-NLS-1$
      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.WARNING, messages.getString("ExcelExportTask.USER_TASK_FAILED")); //$NON-NLS-1$
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
        if (statusListener != null)
        {
          statusListener.setStatus(StatusType.WARNING, messages.getString("ExcelExportTask.USER_TASK_FAILED")); //$NON-NLS-1$
        }
        // if there is already another error, this exception is
        // just a minor obstactle. Something big crashed before ...
      }
    }
    if (progressDialog != null)
    {
      progressDialog.setVisible(false);
    }
  }
}
