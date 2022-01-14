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
 * SubOrganizationRecord.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.sportscouncil;

public class SubOrganizationRecord extends Record
{
  private String name;
  private String email;
  private int maleGenderCount;
  private int femaleGenderCount;

  public SubOrganizationRecord (final String orgID,
                                final String name, final String email,
                                final int maleGenderCount,
                                final int femaleGenderCount)
  {
    super("org", orgID);
    this.name = name;
    this.email = email;
    this.maleGenderCount = maleGenderCount;
    this.femaleGenderCount = femaleGenderCount;
  }

  public String getEmail ()
  {
    return email;
  }

  public int getFemaleGenderCount ()
  {
    return femaleGenderCount;
  }

  public int getMaleGenderCount ()
  {
    return maleGenderCount;
  }

  public String getName ()
  {
    return name;
  }
}
