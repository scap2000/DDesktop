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
 * FreeCard.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.cards;

import java.util.Date;

/**
 * A card.
 *
 * @author Thomas Morgner.
 */
public class FreeCard extends Card
{
  /**
   * The expiry date.
   */
  private final Date expires;

  /**
   * The card number.
   */
  private final String cardNr;

  /**
   * Creates a new 'free' card.
   *
   * @param cardNr  the card number.
   * @param expires the expiry date.
   */
  public FreeCard (final String cardNr, final Date expires)
  {
    this.cardNr = cardNr;
    this.expires = expires;
  }

  /**
   * Returns the expiry date.
   *
   * @return The expiry date.
   */
  public Date getExpires ()
  {
    return expires;
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

  /**
   * Returns the card type.
   *
   * @return The card type.
   */
  public CardType getType ()
  {
    return CardType.FREE;
  }
}
