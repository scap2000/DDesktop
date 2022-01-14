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
 * TranslationTableFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.converter.parser;

import java.io.InputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.jfree.report.modules.gui.converter.ConverterGUIModule;
import org.jfree.report.util.CSVTokenizer;
import org.jfree.report.util.i18n.Messages;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * The translation destTable factory is responsible for building the contexts and reading the
 * translations.
 *
 * @author Thomas Morgner
 */
public final class TranslationTableFactory
{
  private static final Messages messages = new Messages(ConverterGUIModule.BUNDLE_NAME);
  
  /**
   * The context rule defines a transition from one context state to an other. How the
   * mapping is done is defined in the contextmap.properties file.
   */
  public static class ContextRule
  {
    /**
     * The name of the context.
     */
    private String name;
    /**
     * The mapped name of the context. If null, the no mapping is done.
     */
    private String mapTo;

    /**
     * Creates a new rule for the given name and mapping. The mapping is optional.
     *
     * @param name  the name of the context
     * @param mapTo the name of the target context or null if no mapping is done.
     */
    public ContextRule (final String name, final String mapTo)
    {
      if (name == null)
      {
        throw new NullPointerException(messages.getErrorString("ContextRule.ERROR_0001_NAME_IS_NULL")); //$NON-NLS-1$
      }
      this.name = name;
      this.mapTo = mapTo;
    }

    /**
     * Checks, whether a mapping is defined for this rule.
     *
     * @return true, if a mapping is defined, false otherwise.
     */
    public boolean isMappingDefined ()
    {
      return mapTo != null;
    }

    /**
     * Returns the name of this context rule.
     *
     * @return the context rule name.
     */
    public String getName ()
    {
      return name;
    }

    /**
     * Returns the mapping for this rule or null, if no mapping is defined.
     *
     * @return the mapping.
     */
    public String getMapTo ()
    {
      return mapTo;
    }

    /**
     * Checks, wether this context rule is equal to an other object. It is equal, if the
     * other object is also an context rule which has the same name.
     *
     * @param o the other object
     * @return true, if the other object is equal, false otherwise.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (final Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (!(o instanceof ContextRule))
      {
        return false;
      }

      final ContextRule contextRule = (ContextRule) o;

      if (!name.equals(contextRule.name))
      {
        return false;
      }

      return true;
    }

    /**
     * Computes an hashcode for this object.
     *
     * @return the hashcode.
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
      return name.hashCode();
    }
  }

  /**
     * The singleton instance of the translation destTable factory.
     */
  private static TranslationTableFactory singleton;

  /**
   * Returns the singleton instance of this factory.
   *
   * @return the factory.
   */
  public static synchronized TranslationTableFactory getInstance ()
  {
    if (singleton == null)
    {
      singleton = new TranslationTableFactory();
    }
    return singleton;
  }

  /**
   * A collection of all known contexts.
   */
  private final HashMap contexts;
  /**
   * A collection of all known translations.
   */
  private final Properties translations;

  /**
   * Creates a new translation factory and loads the required property files.
   */
  private TranslationTableFactory ()
  {
    contexts = new HashMap();
    translations = new Properties();
    loadContextMap();
    loadTranslationSpecs();
  }

  private static final String TRANSLATION_PROPERTIES_FILENAME = "translations.properties"; //$NON-NLS-1$
  /**
   * Loads the translation specifications. This method will ignore all errors.
   */
  private void loadTranslationSpecs ()
  {
    final InputStream in = ObjectUtilities.getResourceRelativeAsStream
            (TRANSLATION_PROPERTIES_FILENAME,  TranslationTableFactory.class);
    if (in == null)
    {
      Log.warn(messages.getErrorString("TranslationTableFactory.ERROR_0001_UNABLE_TO_LOCATE_RESOURCE", TRANSLATION_PROPERTIES_FILENAME)); //$NON-NLS-1$
      return;
    }
    try
    {
      try
      {
        translations.load(in);
      }
      finally
      {
        in.close();
      }
    }
    catch (Exception e)
    {
      Log.warn(messages.getErrorString("TranslationTableFactory.ERROR_0002_UNABLE_TO_LOAD_TRANSLATION_SET")); //$NON-NLS-1$
    }
  }

  private static final String CONTEXTMAP_PROPERTIES_FILENAME = "contextmap.properties"; //$NON-NLS-1$
  /**
   * Loads the context mapping specifications. This method will ignore all errors.
   */
  private void loadContextMap ()
  {
    final InputStream in = ObjectUtilities.getResourceRelativeAsStream
            (CONTEXTMAP_PROPERTIES_FILENAME, TranslationTableFactory.class);
    if (in == null)
    {
      Log.warn(messages.getErrorString("TranslationTableFactory.ERROR_0003_UNABLE_TO_LOCATE_RESOURCE", CONTEXTMAP_PROPERTIES_FILENAME)); //$NON-NLS-1$
      return;
    }
    try
    {

      final Properties contextProperties = new Properties();
      try
      {
        contextProperties.load(in);
      }
      finally
      {
        in.close();
      }

      final String initialContext = contextProperties.getProperty("%init"); //$NON-NLS-1$
      if (initialContext == null)
      {
        Log.debug(messages.getErrorString("TranslationTableFactory.ERROR_0004_INITIAL_CONTEXT_NULL")); //$NON-NLS-1$
        return;
      }
      final CSVTokenizer tokenizer = new CSVTokenizer(initialContext, CSVTokenizer.SEPARATOR_COMMA);
      while (tokenizer.hasMoreTokens())
      {
        final String context = tokenizer.nextToken().trim();
        buildContext(context, contextProperties);
      }

      // now validate the mappings
      final Iterator keys = contexts.keySet().iterator();
      while (keys.hasNext())
      {
        final Object key = keys.next();
        final ContextRule rule = (ContextRule) contexts.get(key);
        if (rule.isMappingDefined())
        {
          final String mapTo = rule.getMapTo();
          if (contexts.get(mapTo) == null)
          {
            throw new IllegalStateException(messages.getErrorString("TranslationTableFactory.ERROR_0005_NO_CHILD_MAPPING", String.valueOf(key))); //$NON-NLS-1$ //$NON-NLS-2$
          }
        }
      }
    }
    catch (Exception e)
    {
      Log.error(messages.getErrorString("TranslationTableFactory.ERROR_0006_FAILED_TO_LOAD_CONTEXT_MAP"), e); //$NON-NLS-1$
    }
  }

  /**
   * Builds a context rule for the specified base name and stores the rule in the map of
   * available mappings.
   *
   * @param base       the base name
   * @param contextMap the context map that contains all known mappings.
   */
  private void buildContext (final String base, final Properties contextMap)
  {
    final String equals = contextMap.getProperty(base + ".equal"); //$NON-NLS-1$
    if (equals != null)
    {
      final CSVTokenizer tokenizer = new CSVTokenizer(equals, CSVTokenizer.SEPARATOR_COMMA);
      while (tokenizer.hasMoreTokens())
      {
        final String context = tokenizer.nextToken().trim();
        final String fqContext = base + '.' + context;
        final ContextRule rule = new ContextRule(fqContext, base);
        contexts.put(fqContext, rule);
      }
    }

    // if a mapping is defined, there is no way to define childs for
    // an context node.
    final String mapTo = contextMap.getProperty(base + ".mapto"); //$NON-NLS-1$
    if (mapTo != null)
    {
      final ContextRule rule = new ContextRule(base, mapTo);
      contexts.put(base, rule);
      return;
    }

    final String childs = contextMap.getProperty(base + ".child"); //$NON-NLS-1$
    if (childs == null)
    {
      throw new IllegalStateException(messages.getErrorString("TranslationTableFactory.ERROR_0007_NO_PROPERTY_CHILDREN", base)); //$NON-NLS-1$
    }

    final CSVTokenizer tokenizer = new CSVTokenizer(childs, CSVTokenizer.SEPARATOR_COMMA);
    while (tokenizer.hasMoreTokens())
    {
      final String context = tokenizer.nextToken().trim();
      final String fqContext = base + '.' + context;
      buildContext(fqContext, contextMap);
    }

    final ContextRule rule = new ContextRule(base, null);
    contexts.put(base, rule);
  }

  /**
   * Builds a context based on the given last context and the new context name. If no last
   * context is given, the context is considered to be an initial context.
   *
   * @param lastContext the (possibly null) context rule of the last node.
   * @param context     the new context name segment.
   * @return the new context rule, or null, if there is no such context defined.
   */
  public ContextRule buildContext (final ContextRule lastContext, final String context)
  {
    if (lastContext == null)
    {
      return (ContextRule) contexts.get(context);
    }

    if (lastContext.isMappingDefined())
    {
      throw new IllegalArgumentException(messages.getErrorString("TranslationTableFactory.ERROR_0008_UNRESOLVED_MAPPING")); //$NON-NLS-1$
    }

    ContextRule nextContext = (ContextRule)
            contexts.get(lastContext.getName() + '.' + context);
    if (nextContext == null)
    {
      Log.debug(messages.getErrorString("TranslationTableFactory.ERROR_0009_UNDEFINED_MAPPING", lastContext.getName(), context)); //$NON-NLS-1$
      return null;
    }

    while (nextContext.isMappingDefined())
    {
      nextContext = (ContextRule) contexts.get(nextContext.getMapTo());
    }
    return nextContext;
  }

  /**
     * Creates a mapping for the given rule.
     *
     * @param rule the rule that should be looked up
     * @return a translation destTable for the given rule.
     *
     * @throws NullPointerException if the rule is null.
     */
  public TranslationTable getTranslationTable (final ContextRule rule)
  {
    return new TranslationTable(translations, rule.getName());
  }

//  public static void main (String [] args)
//  {
//    TranslationTableFactory factory = TranslationTableFactory.getInstance();

//    ArrayList cons = new ArrayList();
//    Enumeration enum = factory.contexts.keys();
//    while (enum.hasMoreElements())
//    {
//      String context = enum.nextElement().toString();
//      cons.add(context);
//    }
//    Collections.sort(cons);
//    for (int i = 0; i < cons.size(); i++)
//    {
//      System.out.println(cons.get(i));
//    }

//    ContextRule rule = factory.buildContext(null, "report-definition");
//    rule = factory.buildContext(rule, "report-description");
//    rule = factory.buildContext(rule, "report-header");
//    rule = factory.buildContext(rule, "band");
//    rule = factory.buildContext(rule, "band");
//    rule = factory.buildContext(rule, "element");
//    rule = factory.buildContext(rule, "style");
//    Log.debug (rule.getName());
//  }
}
