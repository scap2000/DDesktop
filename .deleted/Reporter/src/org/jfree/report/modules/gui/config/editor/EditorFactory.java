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
 * EditorFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.config.editor;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.modules.Module;
import org.jfree.report.modules.gui.config.ConfigGUIModule;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.util.i18n.Messages;

/**
 * The editor factory is used to create a module editor for an given module. Implementors
 * may add their own, more specialized editor components to the factory.
 *
 * @author Thomas Morgner
 */
public final class EditorFactory
{
  /**
   * The singleton instance of the factory.
   */
  private static EditorFactory factory;
  /**
   * A collection containing all defined modules and their priorities.
   */
  private final HashMap priorities;
  
  /**
   * Externalized String Access
   */
  private static final Messages messages = new Messages(ConfigGUIModule.BUNDLE_NAME);

  /**
   * Creates a new editor factory, which has the default module editor registered at
   * lowest priority.
   */
  private EditorFactory ()
  {
    priorities = new HashMap();
    registerModuleEditor(new DefaultModuleEditor(), -1);
  }

  /**
   * Returns the singleton instance of this factory or creates a new one if no already
   * done.
   *
   * @return the editor factory instance.
   */
  public static synchronized EditorFactory getInstance ()
  {
    if (factory == null)
    {
      factory = new EditorFactory();
    }
    return factory;
  }

  /**
   * Registers a module editor with this factory. The editor will be registered at the
   * given priority.
   *
   * @param editor   the editor that should be registered.
   * @param priority the priority.
   */
  public void registerModuleEditor (final ModuleEditor editor, final int priority)
  {
    if (editor == null)
    {
      throw new NullPointerException(messages.getErrorString("EditorFactory.ERROR_0001_EDITOR_IS_NULL")); //$NON-NLS-1$
    }
    priorities.put(editor, new Integer(priority));
  }

  /**
   * Returns the module editor that will be most suitable for editing the given module.
   *
   * @param module   the module that should be edited.
   * @param config   the configuration which will supply the values for the edited keys.
   * @param keyNames the configuration entries which should be edited within the module.
   * @return the module editor for the given module or null, if no editor is suitable for
   *         the given module.
   */
  public ModuleEditor getModule
          (final Module module, final HierarchicalConfiguration config,
           final ConfigDescriptionEntry[] keyNames)
  {
    if (module == null)
    {
      throw new NullPointerException(messages.getErrorString("EditorFactory.ERROR_0002_MODULE_IS_NULL")); //$NON-NLS-1$
    }
    if (config == null)
    {
      throw new NullPointerException(messages.getErrorString("EditorFactory.ERROR_0003_CONFIG_IS_NULL")); //$NON-NLS-1$
    }
    if (keyNames == null)
    {
      throw new NullPointerException(messages.getErrorString("EditorFactory.ERROR_0004_KEYNAMES_IS_NULL")); //$NON-NLS-1$
    }
    final Iterator keys = priorities.keySet().iterator();
    ModuleEditor currentEditor = null;
    int currentEditorPriority = Integer.MIN_VALUE;

    while (keys.hasNext())
    {
      final ModuleEditor ed = (ModuleEditor) keys.next();
      if (ed.canHandle(module))
      {
        final Integer prio = (Integer) priorities.get(ed);
        if (prio.intValue() > currentEditorPriority)
        {
          currentEditorPriority = prio.intValue();
          currentEditor = ed;
        }
      }
    }
    if (currentEditor != null)
    {
      return currentEditor.createInstance(module, config, keyNames);
    }
    return null;
  }

}
