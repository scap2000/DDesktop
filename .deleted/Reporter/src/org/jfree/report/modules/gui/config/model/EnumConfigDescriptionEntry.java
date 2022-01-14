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
 * EnumConfigDescriptionEntry.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.config.model;

import java.util.Arrays;

/**
 * The enumeration config description entry represents an configuration key, where users
 * may select a valid value from a predefined list of elements. Such an key will not allow
 * free-form text.
 *
 * @author Thomas Morgner
 */
public class EnumConfigDescriptionEntry extends ConfigDescriptionEntry
{
  /**
   * The list of available options in this entry.
   */
  private String[] options;
  private static final String[] EMPTY_STRINGS = new String[0];

  /**
   * Creates a new enumeration description entry for the given configuration key.
   *
   * @param keyName the keyname of this entry.
   */
  public EnumConfigDescriptionEntry (final String keyName)
  {
    super(keyName);
    this.options = EMPTY_STRINGS;
  }

  /**
   * Returns all options from this entry as array.
   *
   * @return the options as array.
   */
  public String[] getOptions ()
  {
    return (String[]) options.clone();
  }

  /**
   * Defines all options for this entry.
   *
   * @param options the selectable values for this entry.
   */
  public void setOptions (final String[] options)
  {
    this.options = (String[]) options.clone();
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }

    final EnumConfigDescriptionEntry that = (EnumConfigDescriptionEntry) o;

    if (!Arrays.equals(options, that.options))
    {
      return false;
    }

    return true;
  }


  public int hashCode()
  {
    int result = super.hashCode();
    result = 31 * result + options.hashCode();
    return result;
  }
}
