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
 * HtmlZipExportTask.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.html;

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
import org.jfree.report.modules.output.table.base.FlowReportProcessor;
import org.jfree.report.modules.output.table.html.AllItemsHtmlPrinter;
import org.jfree.report.modules.output.table.html.FlowHtmlOutputProcessor;
import org.jfree.report.modules.output.table.html.HtmlOutputProcessor;
import org.jfree.report.modules.output.table.html.HtmlPrinter;
import org.jfree.report.modules.output.table.html.PageableHtmlOutputProcessor;
import org.jfree.report.modules.output.table.html.SingleRepositoryURLRewriter;
import org.jfree.report.modules.output.table.html.StreamHtmlOutputProcessor;
import org.jfree.report.util.i18n.Messages;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.DefaultNameGenerator;
import org.jfree.repository.RepositoryUtilities;
import org.jfree.repository.zipwriter.ZipRepository;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * An export task implementation that exports the report into a ZIPped Html directory
 * structure.
 *
 * @author Thomas Morgner
 */
public class HtmlZipExportTask implements Runnable
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
   * The report that should be exported.
   */
  private final JFreeReport report;
  private StatusListener statusListener;

  private String exportMethod;
  private String dataDirectory;
  private File targetFile;

  /**
   * Creates a new html export task.
   *
   * @param dialog        the progress monitor component.
   * @param report        the report that should be exported.
   */
  public HtmlZipExportTask (final JFreeReport report,
                            final ReportProgressDialog dialog,
                            final SwingGuiContext swingGuiContext) throws ReportProcessingException
  {
    if (report == null)
    {
      throw new NullPointerException(messages.getErrorString("HtmlZipExportTask.ERROR_0001_REPORT_IS_NULL")); //$NON-NLS-1$
    }

    final Configuration config = report.getConfiguration();
    dataDirectory = config.getConfigProperty
        ("org.jfree.report.modules.gui.html.zip.DataDirectory"); //$NON-NLS-1$
    final String targetFileName = config.getConfigProperty
        ("org.jfree.report.modules.gui.html.zip.TargetFileName"); //$NON-NLS-1$
    exportMethod = config.getConfigProperty
        ("org.jfree.report.modules.gui.html.zip.ExportMethod"); //$NON-NLS-1$

    this.progressDialog = dialog;
    this.report = report;
    if (swingGuiContext != null)
    {
      this.statusListener = swingGuiContext.getStatusListener();
      this.messages = new Messages
          (swingGuiContext.getLocale(), HtmlStreamExportPlugin.BASE_RESOURCE_CLASS);
    }

    targetFile = new File(targetFileName);

    if (targetFile.exists())
    {
      // lets try to delete it ..
      if (targetFile.delete() == false)
      {
        if (messages == null)
        {
          messages = new Messages(HtmlDirExportPlugin.BASE_RESOURCE_CLASS);
        }
        throw new ReportProcessingException(messages.getErrorString("HtmlZipExportTask.ERROR_0002_TARGET_FILE_EXISTS")); //$NON-NLS-1$
      }
    }
  }

  /**
   * Exports the report into a Html Directory Structure.
   */
  public void run()
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(new FileOutputStream(targetFile));

      final ZipRepository zipRepository = new ZipRepository(out);
      final ContentLocation root = zipRepository.getRoot();
      final ContentLocation data = RepositoryUtilities.createLocation
          (zipRepository, RepositoryUtilities.split(dataDirectory, "/")); //$NON-NLS-1$

      final HtmlOutputProcessor outputProcessor = createOutputProcessor();
      final HtmlPrinter printer = new AllItemsHtmlPrinter(report.getResourceManager());
      printer.setContentWriter(root, new DefaultNameGenerator(root, "report.html")); //$NON-NLS-1$
      printer.setDataWriter(data, new DefaultNameGenerator(data, "content")); //$NON-NLS-1$
      printer.setUrlRewriter(new SingleRepositoryURLRewriter());
      outputProcessor.setPrinter(printer);

      final FlowReportProcessor sp = new FlowReportProcessor(report, outputProcessor);
      if (progressDialog != null)
      {
        progressDialog.setModal(false);
        progressDialog.setVisible(true);
        sp.addReportProgressListener(progressDialog);
      }

      sp.processReport();

      zipRepository.close();
      out.close();
      out = null;

      if (progressDialog != null)
      {
        sp.removeReportProgressListener(progressDialog);
      }

      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.INFORMATION, messages.getString("HtmlZipExportTask.USER_TASK_FINISHED")); //$NON-NLS-1$
      }
    }
    catch (ReportInterruptedException re)
    {
      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.INFORMATION, messages.getString("HtmlZipExportTask.USER_TASK_ABORTED")); //$NON-NLS-1$
      }
      try
      {
        out.close();
        out = null;
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
        statusListener.setStatus(StatusType.ERROR, messages.getString("HtmlZipExportTask.USER_TASK_FAILED")); //$NON-NLS-1$
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
          statusListener.setStatus(StatusType.ERROR, messages.getString("HtmlZipExportTask.USER_TASK_FAILED")); //$NON-NLS-1$
        }
      }
    }
    if (progressDialog != null)
    {
      progressDialog.setVisible(false);
    }
  }


  protected HtmlOutputProcessor createOutputProcessor()
  {
    if ("pageable".equals(exportMethod)) //$NON-NLS-1$
    {
      return new PageableHtmlOutputProcessor(report.getConfiguration());
    }
    else if ("flow".equals(exportMethod)) //$NON-NLS-1$
    {
      return new FlowHtmlOutputProcessor(report.getConfiguration());
    }
    else
    {
      return new StreamHtmlOutputProcessor(report.getConfiguration());
    }
  }
}
