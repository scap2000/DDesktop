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
 * CSVDataExportPlugin.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.csv;

import java.util.Locale;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.gui.commonswing.AbstractExportActionPlugin;
import org.jfree.report.modules.gui.commonswing.ReportProgressDialog;
import org.jfree.report.modules.gui.commonswing.StatusType;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;
import org.jfree.util.ResourceBundleSupport;

/**
 * Encapsulates the CSVDataExportDialog into a separate plugin.
 *
 * @author Thomas Morgner
 */
public class CSVDataExportPlugin extends AbstractExportActionPlugin
{
  public static final String BASE_RESOURCE_CLASS =
          "org.jfree.report.modules.gui.csv.messages.messages"; //$NON-NLS-1$

  /**
   * Localised resources.
   */
  private final ResourceBundleSupport resources;

  /**
   * DefaultConstructor.
   */
  public CSVDataExportPlugin()
  {
    resources = new ResourceBundleSupport(BASE_RESOURCE_CLASS);
  }

  public boolean initialize(final SwingGuiContext context)
  {
    if (super.initialize(context) == false)
    {
      return false;
    }
    if (JFreeReportBoot.getInstance().isModuleAvailable(CSVExportGUIModule.class.getName()) == false)
    {
      return false;
    }
    return true;
  }

  protected String getConfigurationPrefix()
  {
    return "org.jfree.report.modules.gui.csv.export.data."; //$NON-NLS-1$
  }

  /**
   * Creates the report progress dialog used to monitor the export.
   *
   * @return the created dialog.
   */
  protected ReportProgressDialog createProgressDialog()
  {
    final ReportProgressDialog progressDialog = super.createProgressDialog();
    progressDialog.setDefaultCloseOperation(ReportProgressDialog.DO_NOTHING_ON_CLOSE);
    progressDialog.setMessage(resources.getString("cvs-export.progressdialog.message")); //$NON-NLS-1$
    progressDialog.pack();
    RefineryUtilities.positionFrameRandomly(progressDialog);
    return progressDialog;
  }

  /**
   * Shows this dialog and (if the dialog is confirmed) saves the complete report into an comma separated values file.
   *
   * @param report the report being processed.
   * @return true or false.
   */
  public boolean performExport(final JFreeReport report)
  {
    if (report == null)
    {
      throw new NullPointerException();
    }

    final boolean result = performShowExportDialog
        (report, "org.jfree.report.modules.gui.csv.data.Dialog"); //$NON-NLS-1$
    if (result == false)
    {
      // user canceled the dialog ...
      return false;
    }

    final ReportProgressDialog progressDialog;
    if (isProgressDialogEnabled(report, "org.jfree.report.modules.gui.csv.data.ProgressDialogEnabled"))
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

    try
    {
      final Runnable task = new CSVDataExportTask(report, progressDialog, getContext());
      final Thread worker = new Thread(task);
      worker.start();
      return true;
    }
    catch (Exception e)
    {
      Log.error("Failure while preparing the CSV export", e); //$NON-NLS-1$
      getContext().getStatusListener().setStatus(StatusType.ERROR, getResources().getString("CVSExportPlugin.ERROR_0001_FAILED")); //$NON-NLS-1$
      return false;
    }
  }

  /**
   * Returns the resourcebundle to be used to translate strings into localized content.
   *
   * @return the resourcebundle for the localisation.
   */
  protected ResourceBundleSupport getResources()
  {
    return resources;
  }

  /**
   * Returns the display name for the CSV dialog.
   *
   * @return The name.
   */
  public String getDisplayName()
  {
    return resources.getString("action.export-to-csv.data.name"); //$NON-NLS-1$
  }

  /**
   * Returns a short description for the CSV dialog.
   *
   * @return The description.
   */
  public String getShortDescription()
  {
    return resources.getString("action.export-to-csv.data.description"); //$NON-NLS-1$
  }

  /**
   * Returns the small icon for the dialog.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getSmallIcon(locale, "action.export-to-csv.data.small-icon"); //$NON-NLS-1$
  }

  /**
   * Returns the large icon for the dialog.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getLargeIcon(locale, "action.export-to-csv.data.icon"); //$NON-NLS-1$
  }

  /**
   * Returns the accelerator key for the action associated with the dialog.
   *
   * @return The key stroke.
   */
  public KeyStroke getAcceleratorKey()
  {
    return resources.getKeyStroke("action.export-to-csv.data.accelerator"); //$NON-NLS-1$
  }

  /**
   * Returns the mnemonic key code for the action associated with the dialog.
   *
   * @return The key code.
   */
  public Integer getMnemonicKey()
  {
    return resources.getMnemonic("action.export-to-csv.data.mnemonic"); //$NON-NLS-1$
  }
}
