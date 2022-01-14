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
 * PrintingPlugin.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.print;

import java.util.Locale;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.gui.commonswing.AbstractExportActionPlugin;
import org.jfree.report.modules.gui.commonswing.ReportProgressDialog;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ResourceBundleSupport;

/**
 * An export plugin for the <code>java.awt.print</code> API.
 * <p/>
 *
 * @author Thomas Morgner
 */
public class PrintingPlugin extends AbstractExportActionPlugin
{
  /**
   * Localised resources.
   */
  private final ResourceBundleSupport resources;

  /**
   * The base resource class.
   */
  public static final String BASE_RESOURCE_CLASS =
          "org.jfree.report.modules.gui.print.messages.messages"; //$NON-NLS-1$
  public static final String PROGRESS_DIALOG_ENABLE_KEY =
          "org.jfree.report.modules.gui.print.ProgressDialogEnabled"; //$NON-NLS-1$

  /**
   * DefaultConstructor.
   */
  public PrintingPlugin ()
  {
    resources = new ResourceBundleSupport(BASE_RESOURCE_CLASS);
  }
  public boolean initialize(final SwingGuiContext context)
  {
    if (super.initialize(context) == false)
    {
      return false;
    }
    if (JFreeReportBoot.getInstance().isModuleAvailable(AWTPrintingGUIModule.class.getName()) == false)
    {
      return false;
    }
    return true;
  }


  /**
   * Returns the resourcebundle used to translate strings.
   *
   * @return the resourcebundle.
   */
  protected ResourceBundleSupport getResources ()
  {
    return resources;
  }

  /**
   * Creates the progress dialog that monitors the export process.
   *
   * @return the progress monitor dialog.
   */
  protected ReportProgressDialog createProgressDialog ()
  {
    final ReportProgressDialog progressDialog = super.createProgressDialog();
    progressDialog.setDefaultCloseOperation(ReportProgressDialog.DO_NOTHING_ON_CLOSE);
    progressDialog.setMessage(resources.getString("printing-export.progressdialog.message")); //$NON-NLS-1$
    progressDialog.pack();
    RefineryUtilities.positionFrameRandomly(progressDialog);
    return progressDialog;
  }

  protected String getConfigurationPrefix()
  {
    return "org.jfree.report.modules.gui.print.print."; //$NON-NLS-1$
  }

  /**
   * Exports a report.
   *
   * @param report the report.
   * @return true, if the export was successfull, false otherwise.
   */
  public boolean performExport (final JFreeReport report)
  {
    // need to connect to the report pane to receive state updates ...
    final ReportProgressDialog progressDialog;
    if ("true".equals(report.getReportConfiguration().getConfigProperty(PROGRESS_DIALOG_ENABLE_KEY, "false"))) //$NON-NLS-1$ //$NON-NLS-2$
    {
      progressDialog = createProgressDialog();
      if (report.getName() == null)
      {
        progressDialog.setTitle(getResources().getString("ProgressDialog.EMPTY_TITLE"));
      }
      else
      {
        progressDialog.setTitle(getResources().formatMessage("ProgressDialog.TITLE", report.getName()));
      }
    }
    else
    {
      progressDialog = null;
    }

    final PrintExportTask task = new PrintExportTask(report, progressDialog, getContext());
    final Thread worker = new Thread(task);
    worker.start();
    return true;
  }

  /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
  public String getDisplayName ()
  {
    return (resources.getString("action.print.name")); //$NON-NLS-1$
  }

  /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
  public String getShortDescription ()
  {
    return (resources.getString("action.print.description")); //$NON-NLS-1$
  }

  /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon ()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getSmallIcon(locale, "action.print.small-icon"); //$NON-NLS-1$
  }

  /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon ()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getLargeIcon(locale, "action.print.icon"); //$NON-NLS-1$
  }

  /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey ()
  {
    return (resources.getKeyStroke("action.print.accelerator")); //$NON-NLS-1$
  }

  /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
  public Integer getMnemonicKey ()
  {
    return (resources.getMnemonic("action.print.mnemonic")); //$NON-NLS-1$
  }

}
