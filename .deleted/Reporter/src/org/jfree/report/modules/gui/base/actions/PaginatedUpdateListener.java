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
 * PaginatedUpdateListener.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.base.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jfree.report.modules.gui.base.PreviewPane;
import org.jfree.report.modules.gui.commonswing.AbstractActionPlugin;

/**
 * Creation-Date: 15.08.2007, 16:12:34
 *
 * @author Thomas Morgner
 */
public class PaginatedUpdateListener implements PropertyChangeListener
{
  private AbstractActionPlugin actionPlugin;

  public PaginatedUpdateListener(final AbstractActionPlugin actionPlugin)
  {
    if (actionPlugin == null)
    {
      throw new NullPointerException();
    }
    this.actionPlugin = actionPlugin;
  }

  public void propertyChange(final PropertyChangeEvent evt)
  {
    if (PreviewPane.PAGINATED_PROPERTY.equals(evt.getPropertyName()) == false)
    {
      return;
    }

    actionPlugin.setEnabled(Boolean.TRUE.equals(evt.getNewValue()));
  }
}
