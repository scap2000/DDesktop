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
 * $Id: ZoomAction.java 3296 2007-09-07 15:43:06Z tmorgner $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.base.actions;

import java.awt.event.ActionEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.text.NumberFormat;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.jfree.report.modules.gui.base.PreviewPane;
import org.jfree.report.modules.gui.commonswing.SwingCommonModule;
import org.jfree.report.util.ImageUtils;
import org.jfree.ui.action.ActionDowngrade;

/**
 * Creation-Date: 16.11.2006, 18:51:18
 *
 * @author Thomas Morgner
 */
public class ZoomAction extends AbstractAction
{
  private class PaginatedListener implements PropertyChangeListener
  {
    protected PaginatedListener()
    {
    }

    public void propertyChange(final PropertyChangeEvent evt)
    {
      setEnabled(previewPane.isPaginated());
    }
  }
  private double zoom;
  private PreviewPane previewPane;

  /**
   * Defines an <code>Action</code> object with a default description string and
   * default icon.
   */
  public ZoomAction(final double zoom, final PreviewPane previewPane)
  {
    this.zoom = zoom;
    this.previewPane = previewPane;

    this.putValue(Action.NAME, NumberFormat.getPercentInstance
        (previewPane.getLocale()).format(zoom));
    this.putValue(ActionDowngrade.SMALL_ICON,
            ImageUtils.createTransparentIcon(16, 16));
    this.putValue(SwingCommonModule.LARGE_ICON_PROPERTY, ImageUtils.createTransparentIcon(24, 24));

    this.previewPane.addPropertyChangeListener(PreviewPane.PAGINATED_PROPERTY, new PaginatedListener());
    setEnabled(previewPane.getReportJob() != null);
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(final ActionEvent e)
  {
    previewPane.setZoom(zoom);
  }
}
