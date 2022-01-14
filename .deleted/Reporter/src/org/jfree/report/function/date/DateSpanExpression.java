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
 * DateSpanExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function.date;

import java.util.Date;

import org.jfree.report.function.AbstractExpression;

/**
 * Computes the difference date between the start and the end date.
 *
 * @author Thomas Morgner
 */
public class DateSpanExpression extends AbstractExpression
{
  /**
   * The field that contains the start-date.
   */
  private String startDateField;
  /**
   * The field that contains the end-date.
   */
  private String endDateField;

  /**
   * Default Constructor.
   */
  public DateSpanExpression()
  {
  }

  /**
   * Returns the name of the field that contains the start-date.
   *
   * @return the start-date fieldname
   */
  public String getStartDateField()
  {
    return startDateField;
  }

  /**
   * Defines the name of the field that contains the start-date.
   *
   * @param startDateField the start-date fieldname
   */
  public void setStartDateField(final String startDateField)
  {
    this.startDateField = startDateField;
  }

  /**
   * Returns the name of the field that contains the end-date.
   *
   * @return the end-date fieldname
   */
  public String getEndDateField()
  {
    return endDateField;
  }

  /**
   * Defines the name of the field that contains the end-date.
   *
   * @param endDateField the start-date fieldname
   */
  public void setEndDateField(final String endDateField)
  {
    this.endDateField = endDateField;
  }

  /**
   * Computes the difference between the start and the end date. The start-field and end-field must contain either Date
   * objects or Number objects. If the fields contain number objects, the number will be interpreted as milliseconds
   * since 01-Jan-1970.
   *
   * @return the difference between start and end or null, if the difference could not be computed.
   */
  public Object getValue()
  {
    if (startDateField == null)
    {
      return null;
    }
    if (endDateField == null)
    {
      return null;
    }

    final Object startRaw = getDataRow().get(startDateField);
    final long startTime;
    if (startRaw instanceof Date)
    {
      final Date start = (Date) startRaw;
      startTime = start.getTime();
    }
    else if (startRaw instanceof Number)
    {
      final Number start = (Number) startRaw;
      startTime = start.longValue();
    }
    else
    {
      return null;
    }

    final Object endRaw = getDataRow().get(startDateField);
    final long endTime;
    if (endRaw instanceof Date)
    {
      final Date end = (Date) endRaw;
      endTime = end.getTime();
    }
    else if (endRaw instanceof Number)
    {
      final Number end = (Number) endRaw;
      endTime = end.longValue();
    }
    else
    {
      return null;
    }
    return new Date(endTime - startTime);
  }
}
