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
 * StackedLayoutManager.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.layout;

import org.jfree.report.Band;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.util.geom.StrictDimension;

/**
 *
 * @deprecated This layout manager is no longer used.
 */
public class StackedLayoutManager implements BandLayoutManager
{
  public StackedLayoutManager ()
  {
  }

  /**
   * Performs the layout of a band.
   *
   * @param b       the band.
   * @param support the layout support used to compute sizes.
   */
  public void doLayout (final Band b,
                        final LayoutSupport support,
                        final ExpressionRuntime runtime)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Calculates the minimum layout size for a band.
   *
   * @param b             the band.
   * @param containerDims the bounds of the surrounding container.
   * @param maxUsableSize
   * @param support       the layout support used to compute sizes.
   * @return the minimum size.
   */
  public StrictDimension minimumLayoutSize (final Band b,
                                            final StrictDimension containerDims,
                                            final StrictDimension maxUsableSize,
                                            final LayoutSupport support,
                                            final ExpressionRuntime runtime)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Calculates the preferred layout size for a band.
   *
   * @param b             the band.
   * @param containerDims the bounds of the surrounding container.
   * @param maxUsableSize
   * @param support       the layout support used to compute sizes.
   * @return the preferred size.
   */
  public StrictDimension preferredLayoutSize (final Band b,
                                              final StrictDimension containerDims,
                                              final StrictDimension maxUsableSize,
                                              final LayoutSupport support,
                                              final ExpressionRuntime runtime)
  {
    throw new UnsupportedOperationException();
  }
}
