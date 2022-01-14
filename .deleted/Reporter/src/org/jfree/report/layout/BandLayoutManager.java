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
 * BandLayoutManager.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

import org.jfree.report.Band;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.style.StyleKey;
import org.jfree.report.util.geom.StrictDimension;

/**
 * An interface that defines the methods to be supported by a band layout manager.
 * <p/>
 * See the AWT LayoutManager for the idea :)
 *
 * @author Thomas Morgner
 * @see org.jfree.report.layout.StaticLayoutManager
 * @deprecated This layout manager is no longer used.
 */
public interface BandLayoutManager
{
  /**
   * The LayoutManager styleKey. All bands must define their LayoutManager by using this
   * key when using the PageableReportProcessor.
   */
  public static final StyleKey LAYOUTMANAGER =
          StyleKey.getStyleKey("layoutmanager", BandLayoutManager.class, false, false);

  /**
   * Calculates the preferred layout size for a band.
   *
   * @param b             the band.
   * @param containerDims the bounds of the surrounding container.
   * @param maxUsableSize the maximum size that can be granted by the surrounding container.
   * @param support       the layout support used to compute sizes.
   * @return the preferred size.
   */
  public StrictDimension preferredLayoutSize (Band b,
                                              StrictDimension containerDims,
                                              StrictDimension maxUsableSize,
                                              LayoutSupport support,
                                              final ExpressionRuntime runtime);

  /**
   * Calculates the minimum layout size for a band.
   *
   * @param b             the band.
   * @param containerDims the bounds of the surrounding container.
   * @param maxUsableSize
   * @param support       the layout support used to compute sizes.
   * @return the minimum size.
   */
  public StrictDimension minimumLayoutSize (Band b,
                                            StrictDimension containerDims,
                                            StrictDimension maxUsableSize,
                                            LayoutSupport support,
                                            final ExpressionRuntime runtime);

  /**
   * Performs the layout of a band.
   *
   * @param b       the band.
   * @param support the layout support used to compute sizes.
   */
  public void doLayout (Band b,
                        LayoutSupport support,
                        final ExpressionRuntime runtime);
}
