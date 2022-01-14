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
 * CardType.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.cards;

/**
 * An enumeration of card types.
 *
 * @author Thomas Morgner.
 */
public final class CardType
{
  /**
   * An 'account' card.
   */
  public static final CardType ACCOUNT = new CardType("Account");

  /**
   * An 'admin' card.
   */
  public static final CardType ADMIN = new CardType("Admin");

  /**
   * A 'user' card.
   */
  public static final CardType USER = new CardType("User");

  /**
   * A 'prepaid' card.
   */
  public static final CardType PREPAID = new CardType("Prepaid");

  /**
   * A 'free' card.
   */
  public static final CardType FREE = new CardType("Free");

  /**
   * A 'empty' card.
   */
  public static final CardType EMPTY = new CardType("Empty");

  /**
   * The type name.
   */
  private final String myName;

  /**
   * Creates a new card type.
   * <p/>
   * This constructor is private to prevent new types being constructed - only the
   * predefined types are valid.
   *
   * @param name the type name.
   */
  private CardType (final String name)
  {
    myName = name;
  }

  /**
   * Returns the type name.
   *
   * @return The type name.
   */
  public String getTypeName ()
  {
    return myName;
  }

  /**
   * Returns a string representing the type.
   *
   * @return A string.
   */
  public String toString ()
  {
    return myName;
  }
}
