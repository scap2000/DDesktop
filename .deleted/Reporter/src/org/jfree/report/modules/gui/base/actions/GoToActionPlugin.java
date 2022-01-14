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
 * $Id: GoToActionPlugin.java 3603 2007-10-30 11:38:30Z tmorgner $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.base.actions;

import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.jfree.report.modules.gui.base.PreviewPane;
import org.jfree.report.modules.gui.base.SwingPreviewModule;
import org.jfree.report.modules.gui.base.internal.NumericInputDialog;
import org.jfree.report.modules.gui.commonswing.AbstractActionPlugin;
import org.jfree.report.modules.gui.commonswing.SwingGuiContext;
import org.jfree.report.util.i18n.Messages;
import org.jfree.util.Log;
import org.jfree.util.ResourceBundleSupport;

/**
 * Creation-Date: 16.11.2006, 16:34:55
 *
 * @author Thomas Morgner
 */
public class GoToActionPlugin extends AbstractActionPlugin implements ControlActionPlugin
{
  private ResourceBundleSupport resources;

  private Messages messages;

  public GoToActionPlugin()
  {
  }

  public boolean initialize(final SwingGuiContext context)
  {
    super.initialize(context);
    resources = new ResourceBundleSupport(context.getLocale(), SwingPreviewModule.BUNDLE_NAME);
    messages = new Messages(context.getLocale(), SwingPreviewModule.BUNDLE_NAME);
    context.getEventSource().addPropertyChangeListener(new PaginatedUpdateListener(this));
    setEnabled(context.getEventSource().isPaginated());
    return true;
  }

  protected String getConfigurationPrefix()
  {
    return "org.jfree.report.modules.gui.base.go-to."; //$NON-NLS-1$
  }

  /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
  public String getDisplayName()
  {
    return resources.getString("action.gotopage.name"); //$NON-NLS-1$
  }

  /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
  public String getShortDescription()
  {
    return resources.getString("action.gotopage.description"); //$NON-NLS-1$
  }

  /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getSmallIcon(locale, "action.gotopage.small-icon"); //$NON-NLS-1$
  }

  /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon()
  {
    final Locale locale = getContext().getLocale();
    return getIconTheme().getLargeIcon(locale, "action.gotopage.icon"); //$NON-NLS-1$
  }

  /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey()
  {
    return resources.getKeyStroke("action.gotopage.accelerator"); //$NON-NLS-1$
  }

  /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
  public Integer getMnemonicKey()
  {
    return resources.getMnemonic("action.gotopage.mnemonic"); //$NON-NLS-1$
  }

  public boolean configure(final PreviewPane reportPane)
  {
    final Integer result = NumericInputDialog.showInputDialog(getContext().getWindow(),
        JOptionPane.QUESTION_MESSAGE,
        resources.getString("dialog.gotopage.title"), //$NON-NLS-1$
        resources.getString("dialog.gotopage.message"), //$NON-NLS-1$
        1, reportPane.getNumberOfPages(), reportPane.getPageNumber(), true);
    if (result == null)
    {
      return false;
    }
    try
    {
      final int page = result.intValue();
      if (page > 0 && page <= reportPane.getNumberOfPages())
      {
        reportPane.setPageNumber(page);
      }
    }
    catch (Exception ex)
    {
      if (messages != null)
      {
        Log.info(messages.getString("GoToActionPlugin.INFO_EXCEPTION_SWALLOWED")); //$NON-NLS-1$
      }
    }
    return false;
  }

}
