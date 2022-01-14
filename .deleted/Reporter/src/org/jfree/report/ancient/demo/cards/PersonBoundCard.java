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
 * PersonBoundCard.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.cards;

/**
 * A card that is bound to a person's identity.
 *
 * @author Thomas Morgner.
 */
public abstract class PersonBoundCard extends Card
{
  /**
   * The person's first name.
   */
  private String firstName;

  /**
   * The person's last name.
   */
  private String lastName;

  /**
   * The card number.
   */
  private String cardNr;

  /**
   * Creates a new card.
   *
   * @param firstName the first name.
   * @param lastName  the last name.
   * @param cardNr    the card number.
   */
  public PersonBoundCard (final String firstName, final String lastName,
                          final String cardNr)
  {
    if (firstName == null)
    {
      throw new NullPointerException("FirstName");
    }
    if (lastName == null)
    {
      throw new NullPointerException("LastName");
    }
    if (cardNr == null)
    {
      throw new NullPointerException("CardNr");
    }

    this.firstName = firstName;
    this.lastName = lastName;
    this.cardNr = cardNr;
  }

  /**
   * Returns the first name.
   *
   * @return The first name.
   */
  public String getFirstName ()
  {
    return firstName;
  }

  /**
   * Returns the last name.
   *
   * @return The last name.
   */
  public String getLastName ()
  {
    return lastName;
  }

  /**
   * Returns the card number.
   *
   * @return The card number.
   */
  public String getCardNr ()
  {
    return cardNr;
  }

}
