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
 * VerticalLineTemplate.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter.templates;

import java.awt.geom.Line2D;

import org.jfree.report.function.ExpressionRuntime;

/**
 * Defines a horizontal line template. The line always has the width of 100 points. This
 * implementation is used to cover the common use of the line shape element. Use the
 * scaling feature of the shape element to adjust the size of the line.
 *
 * @author Thomas Morgner
 */
public class VerticalLineTemplate extends AbstractTemplate
{
  /**
   * Default Constructor.
   */
  public VerticalLineTemplate ()
  {
  }

  /**
   * Returns the template value, a vertical line.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return a vertical line with a height of 100.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    return new Line2D.Float(0, 0, 0, 100);
  }
}
