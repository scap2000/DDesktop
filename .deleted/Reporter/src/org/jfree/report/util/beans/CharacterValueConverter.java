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
 * CharacterValueConverter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.util.beans;

/**
 * A class that handles the conversion of {@link Character} attributes to and from their
 * {@link String} representation.
 *
 * @author Thomas Morgner
 */
public class CharacterValueConverter implements ValueConverter
{

  /**
   * Creates a new value converter.
   */
  public CharacterValueConverter ()
  {
    super();
  }

  /**
   * Converts the attribute to a string.
   *
   * @param o the attribute ({@link Character} expected).
   * @return A string representing the {@link Character} value.
   */
  public String toAttributeValue (final Object o)
  {
    if (o instanceof Character)
    {
      return o.toString();
    }
    throw new ClassCastException("Give me a real type.");
  }

  /**
   * Converts a string to a {@link Character}.
   *
   * @param s the string.
   * @return a {@link Character}.
   */
  public Object toPropertyValue (final String s)
  {
    if (s.length() == 0)
    {
      throw new RuntimeException("Ugly, no char set!");
    }
    return new Character(s.charAt(0));
  }
}
