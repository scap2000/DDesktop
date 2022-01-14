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
 * HtmlDirExportTask.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.html;

import java.io.File;

import org.jfree.io.IOUtils;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportInterruptedException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.common.StatusListener;
import org.jfree.report.modules.gui.commonswing.ReportProgressDialog;
import org.jfree.report.modules.gui.commonswing.StatusType;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.report.modules.output.table.base.FlowReportProcessor;
import org.jfree.report.modules.output.table.html.AllItemsHtmlPrinter;
import org.jfree.report.modules.output.table.html.FileSystemURLRewriter;
import org.jfree.report.modules.output.table.html.FlowHtmlOutputProcessor;
import org.jfree.report.modules.output.table.html.HtmlOutputProcessor;
import org.jfree.report.modules.output.table.html.HtmlPrinter;
import org.jfree.report.modules.output.table.html.PageableHtmlOutputProcessor;
import org.jfree.report.modules.output.table.html.StreamHtmlOutputProcessor;
import org.jfree.report.util.i18n.Messages;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.DefaultNameGenerator;
import org.jfree.repository.file.FileRepository;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * An export task implementation that exports the report into a HTML directory structure.
 *
 * @author Thomas Morgner
 */
public class HtmlDirExportTask implements Runnable
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
   * The name of the data directory (relative to the target file).
   */
  private File dataDirectory;
  /**
   * The report that should be exported.
   */
  private final JFreeReport report;
  private StatusListener statusListener;
  private String exportMethod;
  private File targetDirectory;
  private String suffix;
  private String filename;

  /**
   * Creates a new html export task.
   *
   * @param progressDialog the progress monitor component (may be null).
   * @param report        the report that should be exported.
   */
  public HtmlDirExportTask(final JFreeReport report,
                           final ReportProgressDialog progressDialog,
                           final SwingGuiContext swingGuiContext) throws ReportProcessingException
  {
    if (report == null)
    {
      throw new NullPointerException(messages.getErrorString("HtmlDirExportTask.ERROR_0001_REPORT_IS_NULL")); //$NON-NLS-1$
    }

    this.progressDialog = progressDialog;
    this.report = report;
    
    final Configuration config = report.getConfiguration();
    final String dataDirectoryName = config.getConfigProperty
        ("org.jfree.report.modules.gui.html.file.DataDirectory"); //$NON-NLS-1$
    final String targetFileName = config.getConfigProperty
        ("org.jfree.report.modules.gui.html.file.TargetFileName"); //$NON-NLS-1$
    exportMethod = config.getConfigProperty
        ("org.jfree.report.modules.gui.html.file.ExportMethod"); //$NON-NLS-1$

    if (swingGuiContext != null)
    {
      this.statusListener = swingGuiContext.getStatusListener();
      this.messages = new Messages
          (swingGuiContext.getLocale(), HtmlDirExportPlugin.BASE_RESOURCE_CLASS);
    }
    final File targetFile = new File(targetFileName);
    targetDirectory = targetFile.getParentFile();

    final File tempDataDir = new File(dataDirectoryName);
    if (tempDataDir.isAbsolute())
    {
      dataDirectory = tempDataDir;
    }
    else
    {
      dataDirectory = new File(targetDirectory, dataDirectoryName);
    }
    if (dataDirectory.exists() && dataDirectory.isDirectory() == false)
    {
      dataDirectory = dataDirectory.getParentFile();
      if (dataDirectory.isDirectory() == false)
      {
        if (messages == null)
        {
          messages = new Messages(HtmlDirExportPlugin.BASE_RESOURCE_CLASS);
        }
        throw new ReportProcessingException(messages.getErrorString("HtmlDirExportTask.ERROR_0002_INVALID_DATA_DIRECTORY", dataDirectory.getAbsolutePath())); //$NON-NLS-1$
      }
    }
    else if (dataDirectory.exists() == false)
    {
      dataDirectory.mkdirs();
    }

    suffix = getSuffix(targetFileName);
    filename = IOUtils.getInstance().stripFileExtension(targetFile.getName());

    if (targetFile.exists())
    {
      // lets try to delete it ..
      if (targetFile.delete() == false)
      {
        if (messages == null)
        {
          messages = new Messages(HtmlDirExportPlugin.BASE_RESOURCE_CLASS);
        }
        throw new ReportProcessingException(messages.getErrorString("HtmlDirExportTask.ERROR_0003_TARGET_FILE_EXISTS", targetFile.getAbsolutePath())); //$NON-NLS-1$
      }
    }
  }

  private String getSuffix(final String filename)
  {
    final String suffix = IOUtils.getInstance().getFileExtension(filename);
    if (suffix.length() == 0)
    {
      return ""; //$NON-NLS-1$
    }
    return suffix.substring(1);
  }


  /**
   * Exports the report into a Html Directory Structure.
   */
  public void run()
  {
    try
    {

      final FileRepository targetRepository = new FileRepository(targetDirectory);
      final ContentLocation targetRoot = targetRepository.getRoot();

      final FileRepository dataRepository = new FileRepository(dataDirectory);
      final ContentLocation dataRoot = dataRepository.getRoot();

      final HtmlOutputProcessor outputProcessor = createOutputProcessor();
      final HtmlPrinter printer = new AllItemsHtmlPrinter(report.getResourceManager());
      printer.setContentWriter(targetRoot,
          new DefaultNameGenerator(targetRoot, filename, suffix));
      printer.setDataWriter(dataRoot, new DefaultNameGenerator(dataRoot, "content")); //$NON-NLS-1$
      printer.setUrlRewriter(new FileSystemURLRewriter());
      outputProcessor.setPrinter(printer);
      final FlowReportProcessor sp = new FlowReportProcessor(report, outputProcessor);
      if (progressDialog != null)
      {
        progressDialog.setModal(false);
        progressDialog.setVisible(true);
        sp.addReportProgressListener(progressDialog);
      }
      sp.processReport();

      if (progressDialog != null)
      {
        sp.removeReportProgressListener(progressDialog);
      }

      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.INFORMATION, messages.getString("HtmlDirExportTask.USER_TASK_FINISHED")); //$NON-NLS-1$
      }
    }
    catch (ReportInterruptedException re)
    {
      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.INFORMATION, messages.getString("HtmlDirExportTask.USER_TASK_ABORTED")); //$NON-NLS-1$
      }
    }
    catch (Exception re)
    {
      Log.error("Exporting failed .", re); //$NON-NLS-1$
      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.ERROR, messages.getString("HtmlDirExportTask.USER_TASK_ERROR")); //$NON-NLS-1$
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
