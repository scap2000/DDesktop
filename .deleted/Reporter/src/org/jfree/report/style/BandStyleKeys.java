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
 * BandStyleKeys.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.style;

/**
 * A band style sheet. Defines some base StyleKeys for all Bands.
 *
 * @author Thomas Morgner
 */
public class BandStyleKeys
{
  /**
   * A key for the band's 'page break before' flag.
   */
  public static final StyleKey PAGEBREAK_BEFORE = StyleKey.getStyleKey("pagebreak-before", Boolean.class, false, false);

  /**
   * A key for the band's 'page break after' flag.
   */
  public static final StyleKey PAGEBREAK_AFTER = StyleKey.getStyleKey("pagebreak-after", Boolean.class, false, false);

  /**
   * A key for the band's 'display on first page' flag.
   */
  public static final StyleKey DISPLAY_ON_FIRSTPAGE = StyleKey.getStyleKey("display-on-firstpage", Boolean.class, false, false);

  /**
   * A key for the band's 'display on last page' flag.
   */
  public static final StyleKey DISPLAY_ON_LASTPAGE = StyleKey.getStyleKey("display-on-lastpage", Boolean.class, false, false);

  /**
   * A key for the band's 'repeat header' flag.
   */
  public static final StyleKey REPEAT_HEADER = StyleKey.getStyleKey("repeat-header", Boolean.class, false, false);

  /**
   * A key for the band's 'print on bottom' flag.
   */
  public static final StyleKey FIXED_POSITION = StyleKey.getStyleKey("fixed-position", Float.class, false, false);

  public static final StyleKey BOOKMARK = StyleKey.getStyleKey("bookmark", String.class, false, false);

  public static final StyleKey STICKY = StyleKey.getStyleKey("sticky", Boolean.class, false, false);

  public static final StyleKey LAYOUT = StyleKey.getStyleKey("layout", String.class, false, false);

  /**
   * An internal carrier key that is used to store the computed sheetname for a given band.
   */
  public static final StyleKey COMPUTED_SHEETNAME = StyleKey.getStyleKey("computed-sheetname", String.class, true, false);

  /**
   * Creates a new band style-sheet.
   */
  private BandStyleKeys ()
  {
  }
}
