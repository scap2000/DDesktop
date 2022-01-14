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
 * LegacyLayoutSupport.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.style.FontDefinition;

/**
 * Creation-Date: 08.04.2007, 15:49:03
 *
 * @author Thomas Morgner
 */
public class LegacyLayoutSupport implements LayoutSupport
{
  private OutputProcessorMetaData outputProcessorMetaData;

  public LegacyLayoutSupport(final OutputProcessorMetaData outputProcessorMetaData)
  {
    if (outputProcessorMetaData == null)
    {
      throw new NullPointerException();
    }
    this.outputProcessorMetaData = outputProcessorMetaData;
  }

  /**
   * Creates a size calculator for the current state of the output target.  The calculator is used to calculate the
   * string width and line height and later maybe more...
   *
   * @param font the font.
   * @return the size calculator.
   * @throws org.jfree.report.layout.SizeCalculatorException
   *          if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator(final FontDefinition font) throws SizeCalculatorException
  {
    return new DefaultSizeCalculator(font,
        outputProcessorMetaData.isFeatureSupported(OutputProcessorFeature.LEGACY_LINEHEIGHT_CALC));
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so that <code>mod(X,
   * horizontalAlignment) == 0</code> and <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable the
   * alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getVerticalAlignmentBorder()
  {
    return 0;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so that <code>mod(X,
   * horizontalAlignment) == 0</code> and <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable the
   * alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getHorizontalAlignmentBorder()
  {
    return 0;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so that <code>mod(X,
   * horizontalAlignment) == 0</code> and <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable the
   * alignment.
   * <p/>
   * Q&D Hack: Save some cycles of processor time by computing that thing only once.
   *
   * @return the vertical alignment grid boundry
   */
  public long getInternalVerticalAlignmentBorder()
  {
    return 0;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so that <code>mod(X,
   * horizontalAlignment) == 0</code> and <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable the
   * alignment.
   * <p/>
   * Q&D Hack: Save some cycles of processor time by computing that thing only once.
   *
   * @return the vertical alignment grid boundry
   */
  public long getInternalHorizontalAlignmentBorder()
  {
    return 0;
  }

  public boolean isImageResolutionMappingActive()
  {
    return outputProcessorMetaData.isFeatureSupported(OutputProcessorFeature.IMAGE_RESOLUTION_MAPPING);
  }
}
