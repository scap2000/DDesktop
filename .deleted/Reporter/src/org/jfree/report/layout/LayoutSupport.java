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
 * LayoutSupport.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.layout;

import org.jfree.report.style.FontDefinition;

/**
 * The LayoutSupport contains all methods required to estaminate sizes for the content-creation.
 *
 * @author Thomas Morgner
 * @deprecated This one should be removed. It is no longer needed by the system and may lead to invalid results.
 *             Functions should not make assumptions about the size of the report elements.
 */
public interface LayoutSupport
{

  /**
   * Creates a size calculator for the current state of the output target.  The calculator is used to calculate the
   * string width and line height and later maybe more...
   *
   * @param font the font.
   * @return the size calculator.
   * @throws SizeCalculatorException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator(FontDefinition font)
      throws SizeCalculatorException;

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so that <code>mod(X,
   * horizontalAlignment) == 0</code> and <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable the
   * alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getVerticalAlignmentBorder();

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so that <code>mod(X,
   * horizontalAlignment) == 0</code> and <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable the
   * alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getHorizontalAlignmentBorder();

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so that <code>mod(X,
   * horizontalAlignment) == 0</code> and <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable the
   * alignment.
   * <p/>
   * Q&D Hack: Save some cycles of processor time by computing that thing only once.
   *
   * @return the vertical alignment grid boundry
   */
  public long getInternalVerticalAlignmentBorder();

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so that <code>mod(X,
   * horizontalAlignment) == 0</code> and <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable the
   * alignment.
   * <p/>
   * Q&D Hack: Save some cycles of processor time by computing that thing only once.
   *
   * @return the vertical alignment grid boundry
   */
  public long getInternalHorizontalAlignmentBorder();

  /**
   * Checks, if the layouter uses the System's native resolution to compute the size of images. This fixes problems with
   * the HTML output, which assumes that images are rendered with a resolution of 96dpi.
   *
   * @return true, if the image-resolution mapping is active, false otherwise.
   */
  public boolean isImageResolutionMappingActive();
}
