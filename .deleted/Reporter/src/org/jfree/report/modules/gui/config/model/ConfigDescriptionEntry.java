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
 * ConfigDescriptionEntry.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.config.model;

/**
 * A config description entry provides a declaration of a single report configuration key
 * and speicifes rules for the values of that key.
 *
 * @author Thomas Morgner
 */
public abstract class ConfigDescriptionEntry
{
  /**
   * A description of the given key.
   */
  private String description;
  /**
   * The fully qualified name of the key.
   */
  private String keyName;
  /**
   * a flag defining whether this is a boot time key.
   */
  private boolean global;
  /**
   * a flag defining whether this is a hidden key.
   */
  private boolean hidden;

  /**
   * Creates a new config description entry with the given name.
   *
   * @param keyName the name of the entry.
   */
  protected ConfigDescriptionEntry (final String keyName)
  {
    if (keyName == null)
    {
      throw new NullPointerException();
    }
    this.keyName = keyName;
  }

  /**
   * Returns the full key name of the configuration description.
   *
   * @return the key name.
   */
  public String getKeyName ()
  {
    return keyName;
  }

  /**
   * Returns the descrption of the configuration entry.
   *
   * @return the key description.
   */
  public String getDescription ()
  {
    return description;
  }

  /**
   * Defines the descrption of the configuration entry.
   *
   * @param description the key description.
   */
  public void setDescription (final String description)
  {
    this.description = description;
  }

  /**
   * Returns, whether the key is a global key. Global keys are read from the global report
   * configuration and specifying them in the report local configuration is useless.
   *
   * @return true, if the key is global, false otherwise.
   */
  public boolean isGlobal ()
  {
    return global;
  }

  /**
   * Defines, whether the key is a global key. Global keys are read from the global report
   * configuration and specifying them in the report local configuration is useless.
   *
   * @param global set to true, if the key is global, false otherwise.
   */
  public void setGlobal (final boolean global)
  {
    this.global = global;
  }

  /**
   * Returns, whether the key is hidden. Hidden keys will not be visible in the
   * configuration editor.
   *
   * @return true, if the key is hidden, false otherwise
   */
  public boolean isHidden ()
  {
    return hidden;
  }

  /**
   * Defines, whether the key is hidden. Hidden keys will not be visible in the
   * configuration editor.
   *
   * @param hidden set to true, if the key is hidden, false otherwise
   */
  public void setHidden (final boolean hidden)
  {
    this.hidden = hidden;
  }

  /**
   * Checks, whether the given object is equal to this config description entry. The
   * object will be equal, if it is also an config description entry with the same name as
   * this entry.
   *
   * @param o the other object.
   * @return true, if the config entry is equal to the given object, false otherwise.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof ConfigDescriptionEntry))
    {
      return false;
    }

    final ConfigDescriptionEntry configDescriptionEntry = (ConfigDescriptionEntry) o;

    if (!keyName.equals(configDescriptionEntry.keyName))
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
    return keyName.hashCode();
  }
}
