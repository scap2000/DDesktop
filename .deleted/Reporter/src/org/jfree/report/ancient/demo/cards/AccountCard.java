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
 * AccountCard.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.cards;

/**
 * A user account card.
 *
 * @author Thomas Morgner.
 */
public class AccountCard extends PersonBoundCard
{
  /**
   * The login id.
   */
  private String login;

  /**
   * The password.
   */
  private String password;

  /**
   * Creates a new user account card.
   *
   * @param firstName the first name.
   * @param lastName  the last name.
   * @param cardNr    the card number.
   * @param login     the login id.
   * @param password  the password.
   */
  public AccountCard (final String firstName, final String lastName, final String cardNr,
                      final String login, final String password)
  {
    super(firstName, lastName, cardNr);
    if (login == null)
    {
      throw new NullPointerException();
    }
    if (password == null)
    {
      throw new NullPointerException();
    }

    this.login = login;
    this.password = password;
  }

  /**
   * Returns the login id.
   *
   * @return The login id.
   */
  public String getLogin ()
  {
    return login;
  }

  /**
   * Returns the password.
   *
   * @return The password.
   */
  public String getPassword ()
  {
    return password;
  }

  /**
   * Returns the account type (<code>CardType.ACCOUNT</code>).
   *
   * @return The account type.
   */
  public CardType getType ()
  {
    return CardType.ACCOUNT;
  }
}
