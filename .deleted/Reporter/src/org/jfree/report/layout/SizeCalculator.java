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
 * SizeCalculator.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

/**
 * The interface for an class that is able to calculate the width of a given string, and
 * the height of a line of text.  The calculations rely on state information (e.g. font
 * size, graphics device, etc) maintained by the calculator.
 * <p/>
 * Every {@link org.jfree.report.layout.LayoutSupport} can create an instance of a class
 * that implements this interface, via the {@link org.jfree.report.layout.LayoutSupport#createTextSizeCalculator}
 * method.
 *
 * @author Thomas Morgner
 */
public interface SizeCalculator
{
  /**
   * @deprecated This config-key should not be declared here. 
   */
  public static final String USE_MAX_CHAR_SIZE = "org.jfree.report.layout.fontrenderer.UseMaxCharBounds";

  /**
   * @deprecated This config-key is no longer used.
   */
  public static final String CLIP_TEXT = "org.jfree.report.layout.fontrenderer.ClipText";

  /**
   * Calculates the width of a <code>String<code> in the current <code>Graphics</code>
   * context.
   *
   * @param text         the text.
   * @param lineStartPos the start position of the substring to be measured.
   * @param endPos       the position of the last character to be measured.
   * @return the width of the string in Java2D units.
   */
  public float getStringWidth (String text, int lineStartPos, int endPos);

  /**
   * Returns the line height.  This includes the font's ascent, descent and leading.
   *
   * @return the line height.
   */
  public float getLineHeight ();

}
