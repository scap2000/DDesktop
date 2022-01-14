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
 * Patient.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.form;

import java.util.ArrayList;

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
 * Patient.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class Patient
{
  private String name;
  private String address;
  private String town;
  private String ssn;
  private String insurance;
  private String symptoms;
  private String allergy;
  private String level;
  private ArrayList treatments;

  public Patient ()
  {
    treatments = new ArrayList();
  }

  public Patient (final String name, final String address, final String town,
                  final String ssn, final String insurance, final String symptoms)
  {
    treatments = new ArrayList();
    this.name = name;
    this.address = address;
    this.town = town;
    this.ssn = ssn;
    this.insurance = insurance;
    this.symptoms = symptoms;
  }

  public String getAddress ()
  {
    return address;
  }

  public void setAddress (final String address)
  {
    this.address = address;
  }

  public String getInsurance ()
  {
    return insurance;
  }

  public void setInsurance (final String insurance)
  {
    this.insurance = insurance;
  }

  public String getName ()
  {
    return name;
  }

  public void setName (final String name)
  {
    this.name = name;
  }

  public String getSsn ()
  {
    return ssn;
  }

  public void setSsn (final String ssn)
  {
    this.ssn = ssn;
  }

  public String getSymptoms ()
  {
    return symptoms;
  }

  public void setSymptoms (final String symptoms)
  {
    this.symptoms = symptoms;
  }

  public String getTown ()
  {
    return town;
  }

  public void setTown (final String town)
  {
    this.town = town;
  }

  public int getTreatmentCount ()
  {
    return treatments.size();
  }

  public Treatment getTreatment (final int i)
  {
    return (Treatment) treatments.get(i);
  }

  public void addTreament (final Treatment t)
  {
    treatments.add(t);
  }

  public void removeTreatment (final Treatment t)
  {
    treatments.remove(t);
  }

  public String getAllergy ()
  {
    return allergy;
  }

  public void setAllergy (final String allergy)
  {
    this.allergy = allergy;
  }

  public String getLevel ()
  {
    return level;
  }

  public void setLevel (final String level)
  {
    this.level = level;
  }
}
