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
 * HtmlFileExportPlugin.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.html;

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
 * Encapsulates the HtmlExportDialog into a separate plugin.
 *
 * @author Thomas Morgner
 */
public class HtmlDirExportPlugin extends AbstractExportActionPlugin
{
  /**
   * Localised resources.
   */
  private final ResourceBundleSupport resources;

  /**
   * The base resource class.
   */
  public static final String BASE_RESOURCE_CLASS =
          "org.jfree.report.modules.gui.html.messages.messages"; //$NON-NLS-1$

  /**
   * DefaultConstructor.
   */
  public HtmlDirExportPlugin ()
  {
    resources = new ResourceBundleSupport(HtmlDirExportPlugin.BASE_RESOURCE_CLASS);
  }

  public boolean initialize(final SwingGuiContext context)
  {
    if (super.initialize(context) == false)
    {
      return false;
    }
    if (JFreeReportBoot.getInstance().isModuleAvailable(HtmlExportGUIModule.class.getName()) == false)
    {
      return false;
    }
    return true;
  }

  protected String getConfigurationPrefix()
  {
    return "org.jfree.report.modules.gui.html.export.file."; //$NON-NLS-1$
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
    progressDialog.setMessage(resources.getString("html-export.progressdialog.message")); //$NON-NLS-1$
    progressDialog.pack();
    RefineryUtilities.positionFrameRandomly(progressDialog);
    return progressDialog;
  }

  /**
   * Shows this dialog and (if the dialog is confirmed) saves the complete report into an
   * Excel file.
   *
   * @param report the report being processed.
   * @return true or false.
   */
  public boolean performExport (final JFreeReport report)
  {
    final boolean result = performShowExportDialog
        (report, "org.jfree.report.modules.gui.html.file.Dialog"); //$NON-NLS-1$
    if (result == false)
    {
      // user canceled the dialog ...
      return false;
    }

    final ReportProgressDialog progressDialog;
    if (isProgressDialogEnabled(report, "org.jfree.report.modules.gui.html.file.ProgressDialogEnabled"))
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
      final HtmlDirExportTask task = new HtmlDirExportTask(report, progressDialog, getContext());
      final Thread worker = new Thread(task);
      worker.start();
      return true;
    }
    catch (Exception e)
    {
      Log.error("Failure while preparing the HTML export", e); //$NON-NLS-1$
      getContext().getStatusListener().setStatus(StatusType.ERROR, resources.getString("HtmlDirExportPlugin.USER_FAILED")); //$NON-NLS-1$
      return false;
    }
  }

  /**
   * Returns the action display name.
   *
   * @return The display name.
   */
  public String getDisplayName ()
  {
    return resources.getString("action.export-to-html.file.name"); //$NON-NLS-1$
  }

  /**
   * Returns the short description for the action.
   *
   * @return The short description.
   */
  public String getShortDescription ()
  {
    return resources.getString("action.export-to-html.file.description"); //$NON-NLS-1$
  }

  /**
   * Returns the small icon for the action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon ()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getSmallIcon(locale, "action.export-to-html.file.small-icon"); //$NON-NLS-1$
  }

  /**
   * Returns the large icon for the action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon ()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getLargeIcon(locale, "action.export-to-html.file.icon"); //$NON-NLS-1$
  }

  /**
   * Returns the accelerator key for the action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey ()
  {
    return resources.getOptionalKeyStroke("action.export-to-html.file.accelerator"); //$NON-NLS-1$
  }

  /**
   * Returns the mnemonic key code for the action.
   *
   * @return The key code.
   */
  public Integer getMnemonicKey ()
  {
    return resources.getMnemonic("action.export-to-html.file.mnemonic"); //$NON-NLS-1$
  }

  /**
   * Returns the resourcebundle to be used to translate strings into localized content.
   *
   * @return the resourcebundle for the localisation.
   */
  protected ResourceBundleSupport getResources ()
  {
    return resources;
  }
}
