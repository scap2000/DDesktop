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
 * ClassConfigDescriptionEntry.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.config.model;

/**
 * A config description entry that describes class name configurations. The specified
 * class in the configuration is forced to be a subclass of the specified base class.
 *
 * @author Thomas Morgner
 */
public class ClassConfigDescriptionEntry extends ConfigDescriptionEntry
{
  /**
   * The base class for the configuration value.
   */
  private Class baseClass;

  /**
   * Creates a new config description entry.
   *
   * @param keyName the full name of the key.
   */
  public ClassConfigDescriptionEntry (final String keyName)
  {
    super(keyName);
    baseClass = Object.class;
  }

  /**
   * Returns the base class used to verify the configuration values.
   *
   * @return the base class or Object.class if not specified otherwise.
   */
  public Class getBaseClass ()
  {
    return baseClass;
  }

  /**
   * Defines the base class for this configuration entry.
   *
   * @param baseClass the base class, never null.
   */
  public void setBaseClass (final Class baseClass)
  {
    if (baseClass == null)
    {
      throw new NullPointerException();
    }
    this.baseClass = baseClass;
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

    final ClassConfigDescriptionEntry that = (ClassConfigDescriptionEntry) o;
    if (baseClass != null ? !baseClass.equals(that.baseClass) : that.baseClass != null)
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = super.hashCode();
    result = 29 * result + (baseClass != null ? baseClass.hashCode() : 0);
    return result;
  }
}
