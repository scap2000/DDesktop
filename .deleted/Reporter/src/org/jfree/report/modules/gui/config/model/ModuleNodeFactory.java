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
 * ModuleNodeFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.config.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.modules.Module;
import org.jfree.base.modules.PackageManager;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.JFreeReportCoreModule;
import org.jfree.report.modules.gui.config.ConfigGUIModule;
import org.jfree.report.util.i18n.Messages;
import org.jfree.util.Log;

import org.xml.sax.SAXException;

/**
 * The module node factory is used to build the lists of modules and their assigned keys
 * for the ConfigTreeModel.
 *
 * @author Thomas Morgner
 */
public class ModuleNodeFactory
{
  /**
   * Sorts the given modules by their class package names.
   *
   * @author Thomas Morgner
   */
  private static class ModuleSorter implements Comparator, Serializable
  {
    /**
     * DefaultConstructor.
     */
    protected ModuleSorter ()
    {
    }

    /**
     * Compares its two arguments for order.  Returns a negative integer, zero, or a
     * positive integer as the first argument is less than, equal to, or greater than the
     * second.<p>
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is
     *         less than, equal to, or greater than the second.
     *
     * @throws ClassCastException if the arguments' types prevent them from being compared
     *                            by this Comparator.
     */
    public int compare (final Object o1, final Object o2)
    {
      final String name1;
      final String name2;

      if (o1.getClass().getPackage() == null || o2.getClass().getPackage() == null)
      {
        name1 = getPackage(o1.getClass());
        name2 = getPackage(o2.getClass());
      }
      else
      {
        name1 = o1.getClass().getPackage().getName();
        name2 = o2.getClass().getPackage().getName();
      }
      return name1.compareTo(name2);
    }
  }

  /**
   * Provides access to externalized strings
   */
  private static final Messages messages = new Messages(ConfigGUIModule.BUNDLE_NAME);
  
  /**
   * All known modules as known at construction time.
   */
  private final Module[] activeModules;
  /**
   * A list of global module nodes.
   */
  private final ArrayList globalNodes;
  /**
   * A list of local module nodes.
   */
  private final ArrayList localNodes;
  /**
   * A hashtable of all defined config description entries.
   */
  private final HashMap configEntryLookup;

  /**
   * Create a new and uninitialized module node factory.
   */
  private ModuleNodeFactory ()
  {
    JFreeReportBoot.getInstance().start();
    final PackageManager pm = JFreeReportBoot.getInstance().getPackageManager();
    activeModules = pm.getAllModules();
    Arrays.sort(activeModules, new ModuleSorter());
    globalNodes = new ArrayList();
    localNodes = new ArrayList();
    configEntryLookup = new HashMap();

  }

  /**
   * Creates a new module node factory and initializes the factory from the given input
   * stream. The stream will be used to build a ConfigDescription model and should contain
   * suitable XML content.
   *
   * @param in the input stream from where to read the model content.
   * @throws IOException if an error occured while reading the stream.
   */
  public ModuleNodeFactory (final InputStream in)
          throws IOException
  {
    this();
    final ConfigDescriptionModel model = new ConfigDescriptionModel();
    try
    {
      model.load(in);
    }
    catch (SAXException saxException)
    {
      final String error = messages.getErrorString("ModuleNodeFactory.ERROR_0001_PARSE_FAILURE", saxException.getMessage()); //$NON-NLS-1$
      Log.error(error, saxException);
      throw new IOException(error);
    }
    catch (ParserConfigurationException pE)
    {
      final String error = messages.getErrorString("ModuleNodeFactory.ERROR_0002_PARSER_CONFIG_ERROR", pE.getMessage()); //$NON-NLS-1$
      Log.error(error, pE);
      throw new IOException(error);
    }

    final ConfigDescriptionEntry[] entries = model.toArray();
    for (int i = 0; i < entries.length; i++)
    {
      //Log.debug ("Entry: " + entries[i].getKeyName() + " registered");
      configEntryLookup.put(entries[i].getKeyName(), entries[i]);
    }
  }

  /**
   * (Re)Initializes the factory from the given report configuration. This will assign all
   * keys frmo the report configuration to the model and assignes the definition from the
   * configuration description if possible.
   *
   * @param config the report configuration that contains the keys.
   * @throws ConfigTreeModelException if an error occurs.
   */
  public void init (final HierarchicalConfiguration config)
          throws ConfigTreeModelException
  {
    globalNodes.clear();
    localNodes.clear();

    //Iterator enum = config.findPropertyKeys("");
    final Iterator keys = configEntryLookup.keySet().iterator();
    while (keys.hasNext())
    {
      final String key = (String) keys.next();
      processKey(key, config);
    }
  }

  /**
   * Processes a single report configuration key and tries to find a definition for that
   * key.
   *
   * @param key    the name of the report configuration key
   * @param config the report configuration used to build the model
   * @throws ConfigTreeModelException if an error occurs
   */
  private void processKey (final String key, final HierarchicalConfiguration config)
          throws ConfigTreeModelException
  {
    ConfigDescriptionEntry cde = (ConfigDescriptionEntry) configEntryLookup.get(key);

    final Module mod = lookupModule(key);
    //Log.debug ("ActiveModule: " + mod.getClass() + " for key " + key);
    if (cde == null)
    {
      // check whether the system properties define such an key.
      // if they do, then we can assume, that it is just a sys-prop
      // and we ignore the key.
      //
      // if this is no system property, then this is a new entry, we'll
      // assume that it is a local text key.
      //
      // Security restrictions are handled as if the key is not defined
      // in the system properties. It is safer to add too much than to add
      // less properties ...
      try
      {
        if (System.getProperties().containsKey(key))
        {
          Log.debug("Ignored key from the system properties: " + key); //$NON-NLS-1$
          return;
        }
        else
        {
          Log.debug("Undefined key added on the fly: " + key); //$NON-NLS-1$
          cde = new TextConfigDescriptionEntry(key);
        }
      }
      catch (SecurityException se)
      {
        Log.debug("Unsafe key-definition due to security restrictions: " + key); //$NON-NLS-1$
        cde = new TextConfigDescriptionEntry(key);
      }
    }

    // We ignore hidden keys.
    if (cde.isHidden())
    {
      return;
    }

    if (cde.isGlobal() == false)
    {
      ConfigTreeModuleNode node = lookupNode(mod, localNodes);
      if (node == null)
      {
        node = new ConfigTreeModuleNode(mod, config);
        localNodes.add(node);
      }
      node.addAssignedKey(cde);
    }

    // The global configuration provides defaults for the local
    // settings...
    ConfigTreeModuleNode node = lookupNode(mod, globalNodes);
    if (node == null)
    {
      node = new ConfigTreeModuleNode(mod, config);
      globalNodes.add(node);
    }
    node.addAssignedKey(cde);
  }

  /**
   * Tries to find a module node for the given module in the given list.
   *
   * @param key      the module that is searched.
   * @param nodeList the list with all known modules.
   * @return the node containing the given module, or null if not found.
   */
  private ConfigTreeModuleNode lookupNode (final Module key, final ArrayList nodeList)
  {
    for (int i = 0; i < nodeList.size(); i++)
    {
      final ConfigTreeModuleNode node = (ConfigTreeModuleNode) nodeList.get(i);
      if (key == node.getModule())
      {
        return node;
      }
    }
    return null;
  }

  /**
   * Returns the name of the package for the given class. This is a workaround for the
   * classloader behaviour of JDK1.2.2 where no package objects are created.
   *
   * @param c the class for which we search the package.
   * @return the name of the package, never null.
   */
  public static String getPackage (final Class c)
  {
    final String className = c.getName();
    final int idx = className.lastIndexOf('.');
    if (idx <= 0)
    {
      // the default package
      return ""; //$NON-NLS-1$
    }
    else
    {
      return className.substring(0, idx);
    }
  }

  /**
   * Looks up the module for the given key. If no module is responsible for the key, then
   * it will be assigned to the core module.
   * <p/>
   * If the core is not defined, then a ConfigTreeModelException is thrown. The core is
   * the base for all modules, and is always defined in a sane environment.
   *
   * @param key the name of the configuration key
   * @return the module that most likely defines that key
   *
   * @throws ConfigTreeModelException if the core module is not available.
   */
  private Module lookupModule (final String key)
          throws ConfigTreeModelException
  {
    Module fallback = null;
    for (int i = 0; i < activeModules.length; i++)
    {
      if (activeModules[i].getClass().equals(JFreeReportCoreModule.class))
      {
        fallback = activeModules[i];
      }
      else
      {
        final String modPackage = getPackage(activeModules[i].getClass());
        // Log.debug ("Module package: " + modPackage + " for " + activeModules[i].getClass());
        if (key.startsWith(modPackage))
        {
          return activeModules[i];
        }
      }
    }
    if (fallback == null)
    {
      final String error = messages.getErrorString("ModuleNodeFactory.ERROR_0003_UNREGISTERED_CORE_MODULE"); //$NON-NLS-1$
      Log.error(error);
      throw new ConfigTreeModelException(error);
    }
    return fallback;
  }

  /**
   * Returns all global nodes. You have to initialize the factory before using this
   * method.
   *
   * @return the list of all global nodes.
   */
  public ArrayList getGlobalNodes ()
  {
    return globalNodes;
  }

  /**
   * Returns all local nodes. You have to initialize the factory before using this
   * method.
   *
   * @return the list of all global nodes.
   */
  public ArrayList getLocalNodes ()
  {
    return localNodes;
  }

  /**
   * Returns the entry for the given key or null, if the key has no metadata.
   *
   * @param key the name of the key
   * @return the entry or null if not found.
   */
  public ConfigDescriptionEntry getEntryForKey (final String key)
  {
    return (ConfigDescriptionEntry) configEntryLookup.get(key);
  }
}
