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
 * PrintExportTask.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.print;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.common.StatusListener;
import org.jfree.report.modules.gui.commonswing.ReportProgressDialog;
import org.jfree.report.modules.gui.commonswing.StatusType;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.report.modules.gui.pdf.PdfExportPlugin;
import org.jfree.report.util.i18n.Messages;
import org.jfree.util.Log;

/**
 * An export task implementation that prints a report using the AWT printing API.
 *
 * @author Thomas Morgner
 */
public class PrintExportTask implements Runnable
{
  private Messages messages;
  private JFreeReport job;
  private ReportProgressDialog progressListener;
  private StatusListener statusListener;

  public PrintExportTask(final JFreeReport job,
                         final ReportProgressDialog progressListener,
                         final SwingGuiContext swingGuiContext)
  {
    this.job = job;
    this.progressListener = progressListener;
    if (swingGuiContext != null)
    {
      this.statusListener = swingGuiContext.getStatusListener();
      this.messages = new Messages
          (swingGuiContext.getLocale(), PdfExportPlugin.BASE_RESOURCE_CLASS);
    }
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used to
   * create a thread, starting the thread causes the object's <code>run</code>
   * method to be called in that separately executing thread.
   * <p/>
   * The general contract of the method <code>run</code> is that it may take any
   * action whatsoever.
   *
   * @see Thread#run()
   */
  public void run()
  {
    try
    {
      if (progressListener != null)
      {
        progressListener.setVisible(true);
      }
      PrintUtil.print(job, progressListener);
    }
    catch (Exception e)
    {
      if (statusListener != null)
      {
        statusListener.setStatus(StatusType.ERROR, messages.getString("PrintExportTask.USER_EXPORT_FAILED")); //$NON-NLS-1$
      }
      Log.error ("Failed"); //$NON-NLS-1$
    }
    finally
    {
      if (progressListener != null)
      {
        progressListener.setVisible(false);
      }
    }
  }

}

