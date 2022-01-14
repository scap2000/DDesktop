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
 * $Id: DefaultActionFactory.java 3710 2007-11-06 21:03:01Z tmorgner $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.commonswing;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.report.util.i18n.Messages;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 16.11.2006, 16:28:03
 *
 * @author Thomas Morgner
 */
public class DefaultActionFactory implements ActionFactory
{
  private static final ActionPlugin[] EMPTY_ACTIONS = new ActionPlugin[0];
  private static final String PREFIX =
      "org.jfree.report.modules.gui.swing.actions."; //$NON-NLS-1$
  
  private static final Messages MESSAGES = new Messages(SwingCommonModule.BUNDLE_NAME);

  public DefaultActionFactory()
  {
  }

  public ActionPlugin[] getActions(final SwingGuiContext context, final String category)
  {
    final Configuration configuration = context.getConfiguration();
    final String prefix = PREFIX + category;
    final Iterator keys = configuration.findPropertyKeys(prefix);
    if (keys.hasNext() == false)
    {
      Log.debug(MESSAGES.getString("DefaultActionFactory.DEBUG_NO_ACTIONS", category)); //$NON-NLS-1$
      return EMPTY_ACTIONS;
    }

    final HashMap plugins = new HashMap();
    while (keys.hasNext())
    {
      final String key = (String) keys.next();
      final String base = key.substring(prefix.length());
      if (isPluginKey(base) == false)
      {
        // Maybe an invalid key or a key for a sub-category ..
        continue;
      }

      final String clazz = configuration.getConfigProperty(key);
      final Object maybeActionPlugin = ObjectUtilities.loadAndInstantiate
          (clazz, DefaultActionFactory.class, ActionPlugin.class);
      if (maybeActionPlugin == null)
      {
        Log.debug(MESSAGES.getString("DefaultActionFactory.DEBUG_NOT_ACTION_PLUGIN", category, clazz)); //$NON-NLS-1$
        continue;
      }

      final ActionPlugin plugin = (ActionPlugin) maybeActionPlugin;
      plugin.initialize(context);
      final String role = plugin.getRole();
      if (role == null)
      {
        plugins.put(plugin, plugin);
      }
      else
      {
        final ActionPlugin otherPlugin = (ActionPlugin) plugins.get(role);
        if (otherPlugin != null)
        {
          if (plugin.getRolePreference() > otherPlugin.getRolePreference())
          {
            plugins.put(role, plugin);
          }
          else
          {
            Log.debug(MESSAGES.getString("DefaultActionFactory.DEBUG_PLUGIN_OVERRIDE", category, clazz, otherPlugin.getClass().getName())); //$NON-NLS-1$
          }
        }
        else
        {
          plugins.put(role, plugin);
        }
      }
    }

    Log.debug(MESSAGES.getString("DefaultActionFactory.DEBUG_RETURNING_PLUGINS", String.valueOf(plugins.size()), category)); //$NON-NLS-1$ //$NON-NLS-2$

    return (ActionPlugin[]) plugins.values().toArray
        (new ActionPlugin[plugins.size()]);
  }

  private boolean isPluginKey(final String base)
  {
    if (base.length() < 1)
    {
      return false;
    }
    if (base.indexOf('.', 1) > 0)
    {
      return false;
    }
    return true;
  }
}
