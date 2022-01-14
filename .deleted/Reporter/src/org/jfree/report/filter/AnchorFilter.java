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
 * AnchorFilter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.filter;

import org.jfree.report.Anchor;
import org.jfree.report.function.ExpressionRuntime;

/**
 * The AnchorFilter converts arbitary objects into Anchors.
 *
 * @author Thomas Morgner
 * @see Anchor
 * @deprecated The anchor filter is deprecated now. Use the stylekey "anchor" instead.
 */
public class AnchorFilter implements DataFilter
{
  /**
   * The data source from where to get the values for the anchor.
   */
  private DataSource dataSource;

  /**
   * DefaultConstructor.
   */
  public AnchorFilter()
  {
  }

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    final AnchorFilter af = (AnchorFilter) super.clone();
    if (dataSource == null)
    {
      af.dataSource = null;
    }
    else
    {
      af.dataSource = (DataSource) dataSource.clone();
    }
    return af;
  }

  /**
   * Returns the current value for the data source.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return the value.
   */
  public Object getValue(final ExpressionRuntime runtime)
  {
    if (dataSource == null)
    {
      return null;
    }
    final Object value = dataSource.getValue(runtime);
    if (value == null)
    {
      return null;
    }
    if (value instanceof Anchor)
    {
      return value;
    }
    return new Anchor(String.valueOf(value));
  }

  /**
   * Returns the assigned DataSource for this Target.
   *
   * @return The datasource.
   */
  public DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Assigns a DataSource for this Target.
   *
   * @param ds The data source.
   */
  public void setDataSource(final DataSource ds)
  {
    this.dataSource = ds;
  }
}
