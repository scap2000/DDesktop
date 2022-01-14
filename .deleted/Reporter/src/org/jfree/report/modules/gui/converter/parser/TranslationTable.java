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
 * TranslationTable.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.converter.parser;

import java.util.Properties;

/**
 * A simple attribute translator. The translator searches a set of properties for a
 * specific value and replaces the attribute value with the updated value.
 * <p/>
 * A translation will be valid for a given context, which is build acording to the rules
 * specified in the contextmap.properties file.
 *
 * @author Thomas Morgner
 */
public class TranslationTable
{
  /**
   * The translation map contains all keys and their values.
   */
  private final Properties translationMap;
  /**
   * the current context, where the map will be valid.
   */
  private final String context;

  /**
     * Creates a new translation destTable which will contain the given translations and will be
     * valid within the given context.
     *
     * @param translations the translations
     * @param context the translation context.
     */
  public TranslationTable (final Properties translations, final String context)
  {
    this.translationMap = translations;
    this.context = context;
  }

  /**
   * Translates the value of the given attribute into a new value. If no translation for
   * the original value is known, then the value is returned unchanged.
   *
   * @param localName the name of the attribute
   * @param orgValue  the untranslated value as read from the xml file
   * @return the translated value if defined, else the untranslated value.
   */
  public String translateAttribute (final String localName, final String orgValue)
  {
    if (orgValue == null)
    {
      return null;
    }
    if (localName == null)
    {
      throw new NullPointerException();
    }
    if (localName.length() == 0)
    {
      throw new IllegalArgumentException();
    }
    final StringBuffer key = new StringBuffer();
    key.append(context);
    key.append('@');
    key.append(localName);
    key.append('@');
    key.append(orgValue);
    return translationMap.getProperty(key.toString(), orgValue);
  }
}
