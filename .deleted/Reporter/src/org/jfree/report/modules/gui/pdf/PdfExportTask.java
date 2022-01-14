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
 * PDFExportTask.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.pdf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.common.StatusListener;
import org.jfree.report.modules.gui.commonswing.ReportProgressDialog;
import org.jfree.report.modules.gui.commonswing.StatusType;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.report.modules.gui.html.HtmlDirExportPlugin;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.pdf.PdfOutputProcessor;
import org.jfree.report.util.i18n.Messages;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * An export task implementation which writes a given report into a PDF file.
 *
 * @author Thomas Morgner
 */
public class PdfExportTask implements Runnable
{
  /**
   * Provides access to externalized strings 
   */
  private Messages messages;
  
  private JFreeReport job;
  private ReportProgressDialog progressListener;
  private StatusListener statusListener;
  private File targetFile;

  /**
   * Creates a new PDF export task.
   */
  public PdfExportTask (final JFreeReport job,
                        final ReportProgressDialog progressListener,
                        final SwingGuiContext swingGuiContext) throws ReportProcessingException
  {
    if (job == null)
    {
      throw new NullPointerException();
    }

    this.job = job;
    if (swingGuiContext != null)
    {
      this.statusListener = swingGuiContext.getStatusListener();
      this.messages = new Messages
          (swingGuiContext.getLocale(), PdfExportPlugin.BASE_RESOURCE_CLASS);
    }
    this.progressListener = progressListener;
    final Configuration config = job.getConfiguration();
    final String targetFileName = config.getConfigProperty("org.jfree.report.modules.gui.pdf.TargetFileName"); //$NON-NLS-1$
    if (targetFileName == null)
    {
      throw new NullPointerException("TargetFileName must be set in the configuration.");
    }

    targetFile = new File(targetFileName);
    if (targetFile.exists())
    {
      if (targetFile.delete() == false)
      {
        if (messages == null)
        {
          messages = new Messages(HtmlDirExportPlugin.BASE_RESOURCE_CLASS);
        }
        throw new ReportProcessingException(messages.getErrorString("PdfExportTask.ERROR_0001_TARGET_EXISTS")); //$NON-NLS-1$
      }
    }
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread causes
   * the object's <code>run</code> method to be called in that separately executing thread.
   * <p/>
   * The general contract of the method <code>run</code> is that it may take any action whatsoever.
   *
   * @see Thread#run()
   */
  public void run()
  {
    PageableReportProcessor proc = null;
    OutputStream fout = null;
    try
    {
      fout = new BufferedOutputStream (new FileOutputStream(targetFile));
      final PdfOutputProcessor outputProcessor = new PdfOutputProcessor(job.getConfiguration(), fout);
      proc = new PageableReportProcessor(job, outputProcessor);
      if (progressListener != null)
      {
        proc.addReportProgressListener(progressListener);
        progressListener.setVisible(true);
      }
      proc.processReport();
      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.INFORMATION, messages.getString("PdfExportTask.USER_EXPORT_COMPLETE")); //$NON-NLS-1$
      }
    }
    catch (Exception e)
    {
      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.ERROR, messages.getString("PdfExportTask.USER_EXPORT_FAILED")); //$NON-NLS-1$
      }
      Log.error ("Failed"); //$NON-NLS-1$
    }
    finally
    {
      if (proc != null)
      {
        if (progressListener != null)
        {
          proc.removeReportProgressListener(progressListener);
        }
        proc.close();
      }
      if (fout != null)
      {
        try
        {
          fout.close();
        }
        catch (IOException e)
        {
          // We tried our best ...
        }
      }

      if (progressListener != null)
      {
        progressListener.setVisible(false);
      }

    }
  }
}
