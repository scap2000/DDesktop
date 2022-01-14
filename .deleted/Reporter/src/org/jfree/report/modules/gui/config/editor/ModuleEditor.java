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
 * ModuleEditor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.config.editor;

import javax.swing.JComponent;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.modules.Module;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;

/**
 * The module editor is used to provide a customizable editor component for a JfreeReport
 * module.
 * <p/>
 * At the moment, there is only one common module editor known, which provides an
 * on-the-fly editor for all defined properties of the module. Specialized editors may be
 * added in the future.
 *
 * @author Thomas Morgner
 */
public interface ModuleEditor
{
  /**
   * Creates a new instance of the module editor. This instance will be used to edit the
   * specific module.
   * <p/>
   * Editors are free to ignore the list of keys given as builder hints.
   *
   * @param module   the module that should be edited.
   * @param config   the report configuration used to fill the values of the editors.
   * @param keyNames the list of keynames this module editor should handle.
   * @return the created new editor instance.
   */
  public ModuleEditor createInstance
          (Module module, HierarchicalConfiguration config, ConfigDescriptionEntry[] keyNames);

  /**
   * Checks, whether this module editor can handle the given module.
   *
   * @param module the module to be edited.
   * @return true, if this editor may be used to edit the module, false otherwise.
   */
  public boolean canHandle (Module module);

  /**
   * Returns the editor component of the module. Calling this method is only valid on
   * instances created with createInstance.
   *
   * @return the editor component for the GUI.
   */
  public JComponent getComponent ();

  /**
   * Resets all keys to the values from the report configuration.
   */
  public void reset ();

  /**
   * Stores all values for the editor's keys into the report configuration.
   */
  public void store ();
}
