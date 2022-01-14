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
 * $Id: ActionPlugin.java 3180 2007-08-15 15:19:27Z tmorgner $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.commonswing;

import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * The Action-Plugin interface defines the common properties for the Control and Export-Plugins. Implementing
 * just this interface will not make sense, you have to use either the Control- or ExportActionPlugin interface
 * instead.
 *
 * @author Thomas Morgner
 */
public interface ActionPlugin
{
  /**
   * Returns the display name for the export action.
   *
   * @return The display name.
   */
  public String getDisplayName ();

  /**
   * Returns the short description for the export action.
   *
   * @return The short description.
   */
  public String getShortDescription ();

  /**
   * Returns the small icon for the export action.
   *
   * @return The icon.
   */
  public Icon getSmallIcon ();

  /**
   * Returns the large icon for the export action.
   *
   * @return The icon.
   */
  public Icon getLargeIcon ();

  /**
   * Returns the accelerator key for the export action.
   *
   * @return The accelerator key.
   */
  public KeyStroke getAcceleratorKey ();

  /**
   * Returns the mnemonic key code.
   *
   * @return The code.
   */
  public Integer getMnemonicKey ();

  /**
   * Returns true if the action is separated, and false otherwise.
   *
   * @return A boolean.
   */
  public boolean isSeparated ();

  /**
   * Returns true if the action should be added to the toolbar, and false otherwise.
   *
   * @return A boolean.
   */
  public boolean isAddToToolbar ();

  /**
   * Returns true if the action should be added to the menu, and false otherwise.
   *
   * @return A boolean.
   */
  public boolean isAddToMenu ();

  public boolean isEnabled();

  public void addPropertyChangeListener (PropertyChangeListener l);

  public void addPropertyChangeListener (String property, PropertyChangeListener l);

  public void removePropertyChangeListener (PropertyChangeListener l);

  /**
   * A sort key used to enforce a certain order within the actions.
   *
   * @return
   */
  public int getMenuOrder ();

  public int getToolbarOrder ();

  public String getRole();

  public int getRolePreference ();

  public boolean initialize(final SwingGuiContext context);
}
