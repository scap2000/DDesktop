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
 * DefaultResourceBundleFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A default implementation of the ResourceBundleFactory, that creates resource bundles using the specified locale.
 * <p/>
 * If not defined otherwise, this implementation uses <code>Locale.getDefault()</code> as Locale.
 *
 * @author Thomas Morgner
 */
public class DefaultResourceBundleFactory implements ResourceBundleFactory
{
  /**
   * The locale used by this factory.
   */
  private Locale locale;

  /**
   * Creates a new DefaultResourceBundleFactory using the system's default locale as factory locale.
   */
  public DefaultResourceBundleFactory()
  {
    this(Locale.getDefault());
  }

  /**
   * Creates a new DefaultResourceBundleFactory using the specified locale as factory locale.
   *
   * @param locale the Locale instance that should be used when creating ResourceBundles.
   * @throws NullPointerException if the given Locale is null.
   */
  public DefaultResourceBundleFactory(final Locale locale)
  {
    if (locale == null)
    {
      throw new NullPointerException("Locale must not be null");
    }
    this.locale = locale;
  }

  /**
   * Returns the locale that will be used to create the resource bundle.
   *
   * @return the locale.
   */
  public Locale getLocale()
  {
    return locale;
  }

  /**
   * Redefines the locale. The locale given must not be null.
   *
   * @param locale the new locale (never null).
   * @throws NullPointerException if the given locale is null.
   */
  public void setLocale(final Locale locale)
  {
    if (locale == null)
    {
      throw new NullPointerException("Locale must not be null");
    }
    this.locale = locale;
  }

  /**
   * Creates a resource bundle named by the given key and using the factory's defined locale.
   *
   * @param key the name of the resourcebundle, never null.
   * @return the created resource bundle
   * @throws NullPointerException if <code>key</code> is <code>null</code>
   * @throws java.util.MissingResourceException
   *                              if no resource bundle for the specified base name can be found
   * @see ResourceBundle#getBundle(String,Locale)
   */
  public ResourceBundle getResourceBundle(final String key)
  {
    return ResourceBundle.getBundle(key, locale);
  }
}
