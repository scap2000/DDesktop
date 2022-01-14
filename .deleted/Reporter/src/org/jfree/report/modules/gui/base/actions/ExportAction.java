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
 * $Id: ExportAction.java 3296 2007-09-07 15:43:06Z tmorgner $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.base.actions;

import java.awt.event.ActionEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.PreviewPane;
import org.jfree.report.modules.gui.commonswing.ExportActionPlugin;
import org.jfree.report.modules.gui.commonswing.SwingCommonModule;
import org.jfree.ui.action.ActionDowngrade;
import org.jfree.util.Log;

/**
 * Creation-Date: 16.11.2006, 17:52:48
 *
 * @author Thomas Morgner
 */
public class ExportAction extends AbstractAction
{
  private class EnableChangeListener implements PropertyChangeListener
  {
    protected EnableChangeListener()
    {
    }

    public void propertyChange(final PropertyChangeEvent evt)
    {
      setEnabled(actionPlugin.isEnabled());
    }
  }

  private ExportActionPlugin actionPlugin;
  private PreviewPane previewPane;

  /**
   * Defines an <code>Action</code> object with a default description string and
   * default icon.
   */
  public ExportAction(final ExportActionPlugin actionPlugin,
                      final PreviewPane previewPane)
  {
    if (actionPlugin == null)
    {
      throw new NullPointerException();
    }
    if (previewPane == null)
    {
      throw new NullPointerException();
    }

    this.actionPlugin = actionPlugin;
    this.previewPane = previewPane;
    putValue(Action.NAME, actionPlugin.getDisplayName());
    putValue(Action.SHORT_DESCRIPTION, actionPlugin.getShortDescription());
    putValue(ActionDowngrade.ACCELERATOR_KEY, actionPlugin.getAcceleratorKey());
    putValue(ActionDowngrade.MNEMONIC_KEY, actionPlugin.getMnemonicKey());
    putValue(Action.SMALL_ICON, actionPlugin.getSmallIcon());
    putValue(SwingCommonModule.LARGE_ICON_PROPERTY, actionPlugin.getLargeIcon());
    this.actionPlugin.addPropertyChangeListener("enabled", new EnableChangeListener()); //$NON-NLS-1$

    setEnabled(actionPlugin.isEnabled());
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(final ActionEvent e)
  {
    if (actionPlugin.isEnabled() == false)
    {
      return;
    }

    final JFreeReport reportJob = previewPane.getReportJob();
    if (reportJob == null)
    {
      return;
    }

    try
    {
      actionPlugin.performExport((JFreeReport) reportJob.clone());
    }
    catch (CloneNotSupportedException e1)
    {
      Log.error ("Internal error: Failed to clone the report."); //$NON-NLS-1$
    }
  }
}
