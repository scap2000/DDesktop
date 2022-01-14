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
 * PatientTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.form;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

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
 * PatientTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class PatientTableModel extends AbstractTableModel
{
  private static Class[] COLUMN_TYPES =
          {
            Patient.class,
            String.class, String.class, String.class,
            String.class, String.class, String.class,
            String.class, String.class,
            Date.class, String.class, String.class, String.class
          };

  private static String[] COLUMN_NAMES =
          {
            "patient",
            "patient.name", "patient.address", "patient.town",
            "patient.ssn", "patient.insurance", "patient.symptoms",
            "patient.allergy", "patient.level",
            "treatment.date", "treatment.description", "treatment.medication",
            "treatment.success"
          };

  private ArrayList patients;
  private int totalSize;

  private transient Patient[] patientPerRow;
  private transient Treatment[] treatmentPerRow;

  public PatientTableModel ()
  {
    patients = new ArrayList();
  }

  public void addPatient (final Patient patient)
  {
    patients.add(patient);
    invalidateCaches();
    fireTableDataChanged();
  }

  public void removePatient (final Patient patient)
  {
    patients.remove(patient);
    invalidateCaches();
    fireTableDataChanged();
  }

  public Patient getPatient (final int patient)
  {
    return (Patient) patients.get(patient);
  }

  public void invalidateCaches ()
  {
    int size = 0;
    for (int i = 0; i < patients.size(); i++)
    {
      final Patient p = getPatient(i);
      size += p.getTreatmentCount();
    }
    this.totalSize = size;
    this.patientPerRow = null;
    this.treatmentPerRow = null;
  }

  /**
   * Returns the number of columns in the model. A <code>JTable</code> uses this method to
   * determine how many columns it should create and display by default.
   *
   * @return the number of columns in the model
   *
   * @see #getRowCount
   */
  public int getColumnCount ()
  {
    return COLUMN_NAMES.length;
  }

  /**
   * Returns the number of rows in the model. A <code>JTable</code> uses this method to
   * determine how many rows it should display.  This method should be quick, as it is
   * called frequently during rendering.
   *
   * @return the number of rows in the model
   *
   * @see #getColumnCount
   */
  public int getRowCount ()
  {
    return totalSize;
  }

  private void fillCache ()
  {
    if (treatmentPerRow != null && patientPerRow != null)
    {
      // nothing to do...
      return;
    }
    // ensure that we have enough space ...
    this.treatmentPerRow = new Treatment[totalSize];
    this.patientPerRow = new Patient[totalSize];


    int currentRow = 0;
    final int patientSize = patients.size();
    for (int i = 0; i < patientSize; i++)
    {
      final Patient pat = (Patient) patients.get(i);
      final int treatmentCount = pat.getTreatmentCount();
      for (int tc = 0; tc < treatmentCount; tc++)
      {
        patientPerRow[currentRow] = pat;
        treatmentPerRow[currentRow] = pat.getTreatment(tc);
        currentRow += 1;
      }
    }
  }

  /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the destColumn being queried
     * @return the Object.class
     */
  public Class getColumnClass (final int columnIndex)
  {
    return COLUMN_TYPES[columnIndex];
  }

  /**
     * Returns a default name for the destColumn using spreadsheet conventions: A, B, C, ... Z,
     * AA, AB, etc.  If <code>destColumn</code> cannot be found, returns an empty string.
     *
     * @param column the destColumn being queried
     * @return a string containing the default name of <code>destColumn</code>
     */
  public String getColumnName (final int column)
  {
    return COLUMN_NAMES[column];
  }

  /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex the row whose value is to be queried
     * @param columnIndex the destColumn whose value is to be queried
     * @return the value Object at the specified cell
     */
  public Object getValueAt (final int rowIndex, final int columnIndex)
  {
    fillCache();
    final Patient pat = patientPerRow[rowIndex];
    final Treatment trm = treatmentPerRow[rowIndex];

    switch (columnIndex)
    {
      case 0:
        return pat;
      case 1:
        return pat.getName();
      case 2:
        return pat.getAddress();
      case 3:
        return pat.getTown();
      case 4:
        return pat.getSsn();
      case 5:
        return pat.getInsurance();
      case 6:
        return pat.getSymptoms();
      case 7:
        return pat.getAllergy();
      case 8:
        return pat.getLevel();

      case 9:
        return trm.getDate();
      case 10:
        return trm.getDescription();
      case 11:
        return trm.getMedication();
      case 12:
        return trm.getSuccess();
    }

    throw new IndexOutOfBoundsException();
  }
}
