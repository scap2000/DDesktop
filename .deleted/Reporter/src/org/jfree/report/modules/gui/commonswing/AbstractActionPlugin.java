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
 * $Id: AbstractActionPlugin.java 3207 2007-08-17 19:02:34Z dkincade $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.commonswing;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.jfree.report.modules.gui.common.IconTheme;
import org.jfree.util.ExtendedConfiguration;
import org.jfree.util.ExtendedConfigurationWrapper;
import org.jfree.util.ResourceBundleSupport;

/**
 * The AbstractExportPlugin provides a basic implementation of the ExportPlugin
 * interface.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractActionPlugin implements ActionPlugin
{
  public static final String ENABLED_PROPERTY = "enabled"; //$NON-NLS-1$
  private PropertyChangeSupport propertyChangeSupport;

  /**
   * Localised resources.
   */
  private ResourceBundleSupport baseResources;
  private IconTheme iconTheme;

  /**
   * The base resource class.
   */
  private SwingGuiContext context;
  private ExtendedConfiguration configuration;
  private boolean enabled;

  protected AbstractActionPlugin()
  {
    enabled = true;
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public boolean isEnabled()
  {
    return enabled;
  }

  public void setEnabled(final boolean enabled)
  {
    final boolean oldEnabled = this.enabled;
    this.enabled = enabled;
    propertyChangeSupport.firePropertyChange(ENABLED_PROPERTY, oldEnabled, enabled);
  }

  public boolean initialize(final SwingGuiContext context)
  {
    this.context = context;
    this.baseResources = new ResourceBundleSupport
        (context.getLocale(), SwingCommonModule.BUNDLE_NAME);
    this.iconTheme = context.getIconTheme();
    this.configuration = new ExtendedConfigurationWrapper
        (context.getConfiguration());
    return true;
  }

  public ResourceBundleSupport getBaseResources()
  {
    return baseResources;
  }

  protected PropertyChangeSupport getPropertyChangeSupport()
  {
    return propertyChangeSupport;
  }

  public SwingGuiContext getContext()
  {
    return context;
  }

  public ExtendedConfiguration getConfig()
  {
    return configuration;
  }

  /**
   * Returns true if the action is separated, and false otherwise. A separated
   * action starts a new action group and will be spearated from previous
   * actions on the menu and toolbar.
   *
   * @return true, if the action should be separated from previous actions,
   *         false otherwise.
   */
  public boolean isSeparated()
  {
    return getConfig().getBoolProperty(getConfigurationPrefix() + "separated"); //$NON-NLS-1$
  }

  /**
   * Returns true if the action should be added to the toolbar, and false
   * otherwise.
   *
   * @return true, if the plugin should be added to the toolbar, false
   *         otherwise.
   */
  public boolean isAddToToolbar()
  {
    return getConfig().getBoolProperty(getConfigurationPrefix() + "add-to-toolbar"); //$NON-NLS-1$
  }

  /**
   * Returns true if the action should be added to the menu, and false
   * otherwise.
   *
   * @return A boolean.
   */
  public boolean isAddToMenu()
  {
    final String name = getConfigurationPrefix() + "add-to-menu"; //$NON-NLS-1$
    return getConfig().getBoolProperty(name);
  }

  /**
   * Creates a progress dialog, and tries to assign a parent based on the given
   * preview proxy.
   *
   * @return the progress dialog.
   */
  protected ReportProgressDialog createProgressDialog()
  {
    final Window proxy = context.getWindow();
    if (proxy instanceof Frame)
    {
      return new ReportProgressDialog((Frame) proxy);
    }
    else if (proxy instanceof Dialog)
    {
      return new ReportProgressDialog((Dialog) proxy);
    }
    else
    {
      return new ReportProgressDialog();
    }
  }

  public void addPropertyChangeListener(final PropertyChangeListener l)
  {
    propertyChangeSupport.addPropertyChangeListener(l);
  }

  public void addPropertyChangeListener(final String property,
                                        final PropertyChangeListener l)
  {
    propertyChangeSupport.addPropertyChangeListener(property, l);
  }

  public void removePropertyChangeListener(final PropertyChangeListener l)
  {
    propertyChangeSupport.removePropertyChangeListener(l);
  }

  public IconTheme getIconTheme()
  {
    return iconTheme;
  }

  protected abstract String getConfigurationPrefix();


  /**
   * A sort key used to enforce a certain order within the actions.
   *
   * @return
   */
  public int getMenuOrder()
  {
    return getConfig().getIntProperty(getConfigurationPrefix() + "menu-order", 0); //$NON-NLS-1$
  }

  public int getToolbarOrder()
  {
    return getConfig().getIntProperty(getConfigurationPrefix() + "toolbar-order", 0); //$NON-NLS-1$
  }

  public String getRole()
  {
    return getConfig().getConfigProperty(getConfigurationPrefix() + "role"); //$NON-NLS-1$
  }

  public int getRolePreference()
  {
    return getConfig().getIntProperty(getConfigurationPrefix() + "role-preference", 0); //$NON-NLS-1$
  }
}
