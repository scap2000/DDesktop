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
 * PageSetupPlugin.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.print;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Locale;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.PageDefinition;
import org.jfree.report.SimplePageDefinition;
import org.jfree.report.modules.gui.base.PreviewPane;
import org.jfree.report.modules.gui.base.actions.ControlActionPlugin;
import org.jfree.report.modules.gui.commonswing.AbstractActionPlugin;
import org.jfree.report.modules.gui.commonswing.ReportEventSource;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.util.ResourceBundleSupport;

/**
 * An export control plugin that handles the setup of page format objects for the report.
 *
 * @author Thomas Morgner
 */
public class PageSetupPlugin extends AbstractActionPlugin implements ControlActionPlugin
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


  /**
   * Localised resources.
   */
  private ResourceBundleSupport resources;
  private ReportEventSource eventSource;

  /**
   * Default Constructor.
   */
  public PageSetupPlugin ()
  {
    resources = new ResourceBundleSupport(PrintingPlugin.BASE_RESOURCE_CLASS);
  }

  public boolean initialize(final SwingGuiContext context)
  {
    if (super.initialize(context) == false)
    {
      return false;
    }
    eventSource = context.getEventSource();
    eventSource.addPropertyChangeListener("reportJob", new ReportJobListener()); //$NON-NLS-1$
    setEnabled(eventSource.getReportJob() != null);

    if (JFreeReportBoot.getInstance().isModuleAvailable(AWTPrintingGUIModule.class.getName()) == false)
    {
      return false;
    }
    return true;
  }


  /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
  public String getDisplayName ()
  {
    return (resources.getString("action.page-setup.name")); //$NON-NLS-1$
  }

  /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
  public String getShortDescription ()
  {
    return (resources.getString("action.page-setup.description")); //$NON-NLS-1$
  }

  /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon ()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getSmallIcon(locale, "action.page-setup.small-icon"); //$NON-NLS-1$
  }

  /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon ()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getLargeIcon(locale, "action.page-setup.icon"); //$NON-NLS-1$
  }

  /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey ()
  {
    return null;
  }

  /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
  public Integer getMnemonicKey ()
  {
    return resources.getMnemonic("action.page-setup.mnemonic"); //$NON-NLS-1$
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

  protected String getConfigurationPrefix()
  {
    return "org.jfree.report.modules.gui.print.page-setup."; //$NON-NLS-1$
  }

  public boolean configure(final PreviewPane pane)
  {
    final JFreeReport report = pane.getReportJob();

    final PrinterJob pj = PrinterJob.getPrinterJob();
    final PageFormat original = report.getPageDefinition().getPageFormat(0);
    final PageFormat pf = pj.validatePage(pj.pageDialog(original));
    if (PageFormatFactory.isEqual(pf, original))
    {
      return false;
    }

    final PageDefinition pageDefinition = report.getPageDefinition();
    if (pageDefinition instanceof SimplePageDefinition)
    {
      final SimplePageDefinition spd = (SimplePageDefinition) pageDefinition;
      report.setPageDefinition(new SimplePageDefinition
          (pf, spd.getPageCountHorizontal(), spd.getPageCountVertical()));
    }
    else
    {
      report.setPageDefinition(new SimplePageDefinition(pf));
    }
    pane.setReportJob(report);
    return true;
  }
}
