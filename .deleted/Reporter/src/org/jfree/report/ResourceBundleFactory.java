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
 * ResourceBundleFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report;

import java.io.Serializable;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A resource bundle factory defines the locale for a report and is used to create resourcebundles.
 *
 * @author Thomas Morgner
 */
public interface ResourceBundleFactory extends Serializable
{
  /**
   * A constant containing the configuration key to specify a resource-bundle name for the report.
   * <p/>
   * This property has the value "org.jfree.report.ResourceBundle".
   */
  public static final String DEFAULT_RESOURCE_BUNDLE_CONFIG_KEY = "org.jfree.report.ResourceBundle";

  /**
   * Creates a resource bundle for the given key. How that key is interpreted depends on the used concrete
   * implementation of this interface.
   *
   * @param key the key that identifies the resource bundle
   * @return the created resource bundle
   * @throws java.util.MissingResourceException
   *          if no resource bundle for the specified base name can be found
   */
  public ResourceBundle getResourceBundle(String key);

  /**
   * Returns the locale that will be used to create the resource bundle. This locale is also used to initialize the
   * java.text.Format instances used by the report.
   *
   * @return the locale.
   */
  public Locale getLocale();
}
