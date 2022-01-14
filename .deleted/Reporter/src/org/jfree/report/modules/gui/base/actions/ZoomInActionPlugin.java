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
 * $Id: ZoomInActionPlugin.java 3696 2007-11-03 15:48:41Z tmorgner $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.base.actions;

import java.util.Locale;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.jfree.report.modules.gui.base.PreviewPane;
import org.jfree.report.modules.gui.base.SwingPreviewModule;
import org.jfree.report.modules.gui.base.internal.PreviewPaneUtilities;
import org.jfree.report.modules.gui.commonswing.AbstractActionPlugin;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.util.ResourceBundleSupport;

/**
 * Creation-Date: 16.11.2006, 18:52:30
 *
 * @author Thomas Morgner
 */
public class ZoomInActionPlugin extends AbstractActionPlugin
    implements ControlActionPlugin
{
  private ResourceBundleSupport resources;

  public ZoomInActionPlugin()
  {
  }

  public boolean initialize(final SwingGuiContext context)
  {
    super.initialize(context);
    resources = new ResourceBundleSupport(context.getLocale(),
        SwingPreviewModule.BUNDLE_NAME);
    context.getEventSource().addPropertyChangeListener(new PaginatedUpdateListener(this));
    setEnabled(context.getEventSource().isPaginated());
    return true;
  }

  protected String getConfigurationPrefix()
  {
    return "org.jfree.report.modules.gui.base.zoom-in."; //$NON-NLS-1$
  }

  /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
  public String getDisplayName()
  {
    return resources.getString("action.zoomIn.name"); //$NON-NLS-1$
  }

  /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
  public String getShortDescription()
  {
    return resources.getString("action.zoomIn.description"); //$NON-NLS-1$
  }

  /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getSmallIcon(locale, "action.zoomIn.small-icon"); //$NON-NLS-1$
  }

  /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getLargeIcon(locale, "action.zoomIn.icon"); //$NON-NLS-1$
  }

  /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey()
  {
    final String charAccelerator = resources.getString("action.zoomIn.char-accelerator");
    if (charAccelerator.length() > 0)
    {
      return KeyStroke.getKeyStroke(charAccelerator.charAt(0));
    }
    return resources.getOptionalKeyStroke("action.zoomIn.accelerator"); //$NON-NLS-1$
  }

  /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
  public Integer getMnemonicKey()
  {
    return resources.getOptionalMnemonic("action.zoomIn.mnemonic"); //$NON-NLS-1$
  }

  public boolean configure(final PreviewPane reportPane)
  {
    final double nextZoomIn = PreviewPaneUtilities.getNextZoomIn
        (reportPane.getZoom(), reportPane.getZoomFactors());
    if (nextZoomIn == 0)
    {
      return false;
    }
    reportPane.setZoom(nextZoomIn);
    return true;
  }
}
