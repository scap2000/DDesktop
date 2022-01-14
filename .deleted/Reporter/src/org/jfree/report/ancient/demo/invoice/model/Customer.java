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
 * Customer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.invoice.model;

public class Customer
{
  private String firstName;
  private String lastName;
  private String street;
  private String postalCode;
  private String town;
  private String country;
  private String salutation;

  public Customer (final String firstName, final String lastName,
                   final String salutation, final String street,
                   final String postalCode, final String town,
                   final String country)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    this.salutation = salutation;
    this.street = street;
    this.postalCode = postalCode;
    this.town = town;
    this.country = country;
  }

  public String getCountry ()
  {
    return country;
  }

  public String getFirstName ()
  {
    return firstName;
  }

  public String getLastName ()
  {
    return lastName;
  }

  public String getPostalCode ()
  {
    return postalCode;
  }

  public String getStreet ()
  {
    return street;
  }

  public String getTown ()
  {
    return town;
  }

  public String getSalutation ()
  {
    return salutation;
  }
}
