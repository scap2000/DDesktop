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
 * DefaultReportController.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.gui.base;

import java.awt.BorderLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;

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
 * DefaultReportController.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class DefaultReportController extends JPanel implements ReportController
{
  private class EnabledUpdateHandler implements PropertyChangeListener
  {
    protected EnabledUpdateHandler ()
    {
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the property
     *            that has changed.
     */

    public void propertyChange (final PropertyChangeEvent evt)
    {
      if (PreviewPane.PAGINATING_PROPERTY.equals(evt.getPropertyName()) == false)
      {
        return;
      }
      DefaultReportController.this.setEnabled(Boolean.FALSE.equals(evt.getNewValue()));
    }
  }

  private static final JMenu[] EMPTY_MENU = new JMenu[0];
  private EnabledUpdateHandler enabledUpdateHandler;


  /**
   * Creates a new <code>JPanel</code> with a double buffer and a flow layout.
   */
  public DefaultReportController ()
  {
    enabledUpdateHandler = new EnabledUpdateHandler();
  }

  /**
   * Returns the graphical representation of the controler. This component will be added
   * between the menu bar and the toolbar.
   * <p/>
   * Changes to this property are not detected automaticly, you have to call
   * "refreshControler" whenever you want to display a completly new control panel.
   *
   * @return the controler component.
   */
  public JComponent getControlPanel ()
  {
    return this;
  }

  /**
   * The default implementation has no menus.
   *
   * @return an empty array.
   */
  public JMenu[] getMenus ()
  {
    return EMPTY_MENU;
  }

  /**
   * Returns the location for the report controler, one of BorderLayout.NORTH,
   * BorderLayout.SOUTH, BorderLayout.EAST or BorderLayout.WEST.
   *
   * @return the location;
   */
  public String getControllerLocation ()
  {
    return BorderLayout.NORTH;
  }

  /**
   * Defines, whether the controler component is placed between the report pane and the
   * toolbar.
   *
   * @return true, if this is a inne component.
   */
  public boolean isInnerComponent ()
  {
    return false;
  }

  public void initialize(final PreviewPane pane)
  {
    pane.addPropertyChangeListener(PreviewPane.PAGINATING_PROPERTY, enabledUpdateHandler);
    setEnabled(pane.isPaginating() == false);
  }
}
