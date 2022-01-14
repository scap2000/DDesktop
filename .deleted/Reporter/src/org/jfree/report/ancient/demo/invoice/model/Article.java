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
 * Article.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.invoice.model;

public class Article
{
  // how is the article called?
  private String name;
  // how much does it cost
  private float price;
  // the article number
  private String articleNumber;
  // may contain a serial number or special notes
  private String articleDetails;

  public Article (final String articleNumber, final String name, final float price)
  {
    this(articleNumber, name, price, null);
  }

  public Article (final String articleNumber, final String name,
                  final float price, final String articleDetails)
  {
    this.articleNumber = articleNumber;
    this.name = name;
    this.price = price;
    this.articleDetails = articleDetails;
  }

  public String getArticleDetails ()
  {
    return articleDetails;
  }

  public String getArticleNumber ()
  {
    return articleNumber;
  }

  public String getName ()
  {
    return name;
  }

  public float getPrice ()
  {
    return price;
  }
}
