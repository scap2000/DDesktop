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
 * ItemColumnQuotientExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;



/**
 * A report function that calculates the quotient of two fields (columns) from the current
 * row.
 * <p/>
 * This function expects its input values to be java.lang.Number instances.
 * <p/>
 * The function undestands two parameters. The <code>dividend</code> parameter is required
 * and denotes the name of an ItemBand-field which is used as dividend. The
 * <code>divisor</code> parameter is required and denotes the name of an ItemBand-field
 * which is uses as divisor.
 * <p/>
 *
 * @author Heiko Evermann
 * @deprecated Use PercentageExpression instead, it's name is much clearer
 */
public class ItemColumnQuotientExpression extends PercentageExpression
{
  /**
   * Constructs a new function. <P> Initially the function has no name...be sure to assign
   * one before using the function.
   */
  public ItemColumnQuotientExpression ()
  {
  }
}
