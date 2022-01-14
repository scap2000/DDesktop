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
 * MasterDataRowChangeEvent.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.states.datarow;

/**
 * Creation-Date: 03.03.2006, 15:34:53
 *
 * @author Thomas Morgner
 */
public class MasterDataRowChangeEvent
{
  public static final int COLUMN_ADDED = 1;
  public static final int COLUMN_REMOVED = 2;
  public static final int COLUMN_UPDATED = 3;

  private int type;
  private String columnName;
  private Object columnValue;

  public MasterDataRowChangeEvent(final int type,
                                  final String columnName,
                                  final Object columnValue)
  {
    this.type = type;
    this.columnName = columnName;
    this.columnValue = columnValue;
  }

  public String getColumnName()
  {
    return columnName;
  }

  public Object getColumnValue()
  {
    return columnValue;
  }

  public int getType()
  {
    return type;
  }

  public void setColumnName(final String columnName)
  {
    this.columnName = columnName;
  }

  public void setColumnValue(final Object columnValue)
  {
    this.columnValue = columnValue;
  }
}
