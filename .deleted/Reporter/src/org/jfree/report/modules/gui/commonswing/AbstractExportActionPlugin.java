/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2000-2007, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * $Id: AbstractExportActionPlugin.java 3710 2007-11-06 21:03:01Z tmorgner $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.commonswing;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.lang.reflect.Constructor;

import org.jfree.report.JFreeReport;
import org.jfree.report.util.i18n.Messages;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 02.12.2006, 14:21:07
 *
 * @author Thomas Morgner
 */
public abstract class AbstractExportActionPlugin extends AbstractActionPlugin
  implements ExportActionPlugin
{
  private class ReportJobListener implements PropertyChangeListener
  {
    protected ReportJobListener()
    {
    }

    public void propertyChange(final PropertyChangeEvent evt)
    {
      setEnabled(eventSource.getReportJob() != null);
    }
  }

  private ReportEventSource eventSource;
  
  private Messages messages;

  protected AbstractExportActionPlugin()
  {
    messages = new Messages(SwingCommonModule.BUNDLE_NAME);
  }

  public boolean initialize(final SwingGuiContext context)
  {
    if (super.initialize(context) == false)
    {
      return false;
    }
    messages = new Messages(context.getLocale(), SwingCommonModule.BUNDLE_NAME);
    eventSource = context.getEventSource();
    eventSource.addPropertyChangeListener("reportJob", new ReportJobListener()); //$NON-NLS-1$
    setEnabled(eventSource.getReportJob() != null);
    return true;
  }

  /**
   * Creates a progress dialog, and tries to assign a parent based on the given
   * preview proxy.
   *
   * @return the progress dialog.
   */
  protected ExportDialog createExportDialog(final String className)
      throws InstantiationException
  {
    final Window proxy = getContext().getWindow();
    if (proxy instanceof Frame)
    {
      final ClassLoader classLoader = ObjectUtilities.getClassLoader(AbstractActionPlugin.class);
      try
      {
        final Class aClass = classLoader.loadClass(className);
        final Constructor constructor = aClass.getConstructor(new Class[]{Frame.class});
        return (ExportDialog) constructor.newInstance(new Object[]{proxy});
      }
      catch (Exception e)
      {
        Log.error(messages.getErrorString("AbstractExportActionPlugin.ERROR_0001_FAILED_EXPORT_DIALOG_CREATION", className)); //$NON-NLS-1$
      }
    }
    else if (proxy instanceof Dialog)
    {
      final ClassLoader classLoader = ObjectUtilities.getClassLoader(AbstractActionPlugin.class);
      try
      {
        final Class aClass = classLoader.loadClass(className);
        final Constructor constructor = aClass.getConstructor(new Class[]{Dialog.class});
        return (ExportDialog) constructor.newInstance(new Object[]{proxy});
      }
      catch (Exception e)
      {
        Log.error(messages.getErrorString("AbstractExportActionPlugin.ERROR_0002_FAILED_EXPORT_DIALOG_CREATION", className), e); //$NON-NLS-1$
      }
    }

    final Object fallBack = ObjectUtilities.loadAndInstantiate
        (className, AbstractActionPlugin.class, ExportDialog.class);
    if (fallBack != null)
    {
      return (ExportDialog) fallBack;
    }

    Log.error (messages.getErrorString("AbstractExportActionPlugin.ERROR_0003_FAILED_EXPORT_DIALOG_CREATION", className)); //$NON-NLS-1$
    throw new InstantiationException(messages.getErrorString("AbstractExportActionPlugin.ERROR_0004_FAILED_EXPORT_DIALOG_CREATION")); //$NON-NLS-1$
  }


  /**
   * Exports a report.
   *
   * @param job the report.
   * @return A boolean.
   */
  public boolean performShowExportDialog(final JFreeReport job, final String configKey)
  {
    try
    {
      final Configuration configuration = job.getConfiguration();
      final String dialogClassName = configuration.getConfigProperty(configKey);
      final ExportDialog dialog = createExportDialog(dialogClassName);

      return dialog.performQueryForExport(job, getContext());
    }
    catch (InstantiationException e)
    {
      Log.error (messages.getErrorString("AbstractExportActionPlugin.ERROR_0005_UNABLE_TO_CONFIGURE")); //$NON-NLS-1$
      getContext().getStatusListener().setStatus(StatusType.ERROR, messages.getString("AbstractExportActionPlugin.ERROR_0005_UNABLE_TO_CONFIGURE")); //$NON-NLS-1$
      return false;
    }
  }

  protected boolean isProgressDialogEnabled(final JFreeReport report,
                                            final String configKey)
  {
    return getConfig().getBoolProperty(configKey);
  }

}
