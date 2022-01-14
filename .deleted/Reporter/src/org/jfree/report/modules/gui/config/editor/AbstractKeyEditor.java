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
 * AbstractKeyEditor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.config.editor;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.report.modules.gui.config.ConfigGUIModule;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.util.ImageUtils;
import org.jfree.util.Configuration;
import org.jfree.util.ResourceBundleSupport;

/**
 * This key editor class is the base class for all key editor components. It provides
 * common services usable for most key editor implementation.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractKeyEditor extends JComponent implements KeyEditor
{
  /**
   * A constant for the "validInput" property name.
   */
  public static final String VALID_INPUT_PROPERTY = "validInput"; //$NON-NLS-1$

  /**
   * The error icon used for the key editors.
   */
  private static Icon errorIcon;
  /**
   * The empty icon used for the key editors.
   */
  private static Icon emptyIcon;

  /**
   * The report configuration that provides the values for this editor.
   */
  private final HierarchicalConfiguration config;
  /**
   * The config description entry that provides the definition for this key.
   */
  private final ConfigDescriptionEntry entry;
  /**
   * A flag indicating whether the input is valid.
   */
  private boolean validInput;
  /**
   * A label that holds the error indicator icons.
   */
  private final JLabel stateLabel;
  /**
   * the resource bundle instance used to translate the text.
   */
  private final ResourceBundleSupport resources;

  /**
   * Creates a new key editor for the given report configuration and key entry.
   *
   * @param config the report configuration that supplies the value for the editor
   * @param entry  the entry description provides the meta data for the edited key.
   */
  protected AbstractKeyEditor (final HierarchicalConfiguration config,
                            final ConfigDescriptionEntry entry)
  {
    this.resources = new ResourceBundleSupport(ConfigGUIModule.BUNDLE_NAME);
    this.setLayout(new BorderLayout());
    this.config = config;
    this.entry = entry;
    stateLabel = new JLabel(getEmptyIcon());
  }

  /**
   * Returns the empty icon for this an all derived editors.
   *
   * @return the empty icon.
   */
  protected Icon getEmptyIcon ()
  {
    if (emptyIcon == null)
    {
      final Icon errorIcon = getErrorIcon();
      final int width = errorIcon.getIconWidth();
      final int height = errorIcon.getIconHeight();
      emptyIcon = ImageUtils.createTransparentIcon(width, height);
    }
    return emptyIcon;
  }

  /**
   * Returns the error icon for this an all derived editors.
   *
   * @return the error icon.
   */
  protected Icon getErrorIcon ()
  {
    if (errorIcon == null)
    {
      errorIcon = resources.getIcon("default-editor.error-icon"); //$NON-NLS-1$
    }
    return errorIcon;
  }

  /**
   * Defines the content pane for this editor.
   *
   * @param contentPane the new content pane
   */
  protected void setContentPane (final JPanel contentPane)
  {
    removeAll();
    add(contentPane, BorderLayout.CENTER);
    add(stateLabel, BorderLayout.EAST);
  }

  /**
   * Returns the report configuration instance used for this editor.
   *
   * @return the report configuration instance of this editor.
   */
  public Configuration getConfig ()
  {
    return config;
  }

  /**
   * Returns the config description entry of this editor.
   *
   * @return the config description entry.
   */
  public ConfigDescriptionEntry getEntry ()
  {
    return entry;
  }

  /**
   * Loads the value from the configuration.
   *
   * @return the value of the edited key from the configuration.
   */
  protected String loadValue ()
  {
    return config.getConfigProperty(entry.getKeyName());
  }

  /**
   * Stores the value to the configuration.
   *
   * @param o the new value for the key of the editor.
   */
  protected void storeValue (final String o)
  {
    config.setConfigProperty(entry.getKeyName(), o);
  }

  /**
   * Removes the value from the configuration; the configuration will fall back to the
   * default value from the global configuration.
   * <p/>
   * Deleting the value triggers the <code>isDefined</code> property.
   */
  protected void deleteValue ()
  {
    config.setConfigProperty(entry.getKeyName(), null);
  }

  /**
   * Returns true, if the component validated the entered values, false otherwise.
   *
   * @return true, if the input is valid, false otherwise.
   */
  public boolean isValidInput ()
  {
    return validInput;
  }

  /**
   * Defines, whether the input is valid. This should be called after the value of the
   * component changed.
   *
   * @param validInput true, if the input should be considered valid, false otherwise.
   */
  protected void setValidInput (final boolean validInput)
  {
    if (this.validInput != validInput)
    {
      final boolean oldValue = this.validInput;
      this.validInput = validInput;
      firePropertyChange(VALID_INPUT_PROPERTY, oldValue, validInput);
      if (this.validInput == false)
      {
        stateLabel.setIcon(getErrorIcon());
      }
      else
      {
        stateLabel.setIcon(getEmptyIcon());
      }
    }
  }

  /**
   * Checks, whether the local key has a defined value in the local report configuration.
   *
   * @return true, if the key is defined, false otherwise.
   *
   * @see org.jfree.report.modules.gui.config.editor.KeyEditor#isDefined()
   */
  public boolean isDefined ()
  {
    return config.isLocallyDefined(entry.getKeyName());
  }

  /**
   * Returns the editor component; this implementation returns the "this" reference.
   *
   * @return a reference to this object.
   *
   * @see org.jfree.report.modules.gui.config.editor.KeyEditor#getComponent()
   */
  public JComponent getComponent ()
  {
    return this;
  }
}
