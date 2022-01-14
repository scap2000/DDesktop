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
 * EmptyDataSource.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter;

import org.jfree.report.function.ExpressionRuntime;

/**
 * A data source that always returns null.
 *
 * @author Thomas Morgner
 */
public final class EmptyDataSource implements DataSource
{
  /**
   * Default-Constructor.
   */
  public EmptyDataSource()
  {
  }

  /**
   * Returns the value for the data source (always null in this case).
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return always null.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    return null;
  }

  /**
   * Clones the data source.
   *
   * @return a clone.
   * @noinspection CloneDoesntCallSuperClone
   */
  public Object clone ()
  {
    return this;
  }

}
