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
 * $Id: AboutActionPlugin.java 3320 2007-09-10 18:59:59Z tmorgner $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.base.actions;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import java.util.Locale;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.JFreeReportInfo;
import org.jfree.report.modules.gui.base.PreviewPane;
import org.jfree.report.modules.gui.base.SwingPreviewModule;
import org.jfree.report.modules.gui.commonswing.AbstractActionPlugin;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.report.modules.gui.commonswing.SwingUtil;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.about.AboutDialog;
import org.jfree.util.ResourceBundleSupport;

/**
 * Creation-Date: 16.11.2006, 16:34:55
 *
 * @author Thomas Morgner
 */
public class AboutActionPlugin extends AbstractActionPlugin
    implements ControlActionPlugin
{
  private ResourceBundleSupport resources;
  private AboutDialog aboutFrame;

  public AboutActionPlugin()
  {
  }

  public boolean initialize(final SwingGuiContext context)
  {
    super.initialize(context);
    resources = new ResourceBundleSupport(context.getLocale(),
        SwingPreviewModule.BUNDLE_NAME);
    return true;
  }

  protected String getConfigurationPrefix()
  {
    return "org.jfree.report.modules.gui.base.about."; //$NON-NLS-1$
  }

  /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
  public String getDisplayName()
  {
    return resources.getString("action.about.name"); //$NON-NLS-1$
  }

  /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
  public String getShortDescription()
  {
    return resources.getString("action.about.description"); //$NON-NLS-1$
  }

  /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getSmallIcon(locale, "action.about.small-icon"); //$NON-NLS-1$
  }

  /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getLargeIcon(locale, "action.about.icon"); //$NON-NLS-1$
  }

  /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey()
  {
    return null;
  }

  /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
  public Integer getMnemonicKey()
  {
    return resources.getMnemonic("action.about.mnemonic"); //$NON-NLS-1$
  }

  public boolean configure(final PreviewPane reportPane)
  {
    if (aboutFrame == null)
    {
      final String title = getDisplayName();
      // look where we have been added ...
      final Window w = SwingUtil.getWindowAncestor(reportPane);
      if (w instanceof Frame)
      {
        aboutFrame = new AboutDialog
                ((Frame) w, title, JFreeReportInfo.getInstance());
      }
      else if (w instanceof Dialog)
      {
        aboutFrame = new AboutDialog
                ((Dialog) w, title, JFreeReportInfo.getInstance());
      }
      else
      {
        aboutFrame = new AboutDialog
                (title, JFreeReportInfo.getInstance());
      }
      aboutFrame.pack();
      RefineryUtilities.centerFrameOnScreen(aboutFrame);
    }

    aboutFrame.setVisible(true);
    aboutFrame.requestFocus();
    return true;
  }

}
