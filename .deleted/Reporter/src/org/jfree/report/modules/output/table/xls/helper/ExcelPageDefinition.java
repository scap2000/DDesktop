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
 * ExcelPageDefinition.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.xls.helper;

/**
 * A read only page format mapping definiton to map a page format to an predefined excel
 * constant.
 *
 * @author Thomas Morgner
 */
public final class ExcelPageDefinition
{
  /**
   * The excel internal page format code referring to that page size.
   */
  private final short pageFormatCode;
  /**
   * The width of the page format.
   */
  private final int width;
  /**
   * The height of the page format.
   */
  private final int height;

  /**
   * Defines a new excel page format mapping.
   *
   * @param pageFormatCode the excel internal page format code.
   * @param width          the width of the page.
   * @param height         the height of the page.
   */
  public ExcelPageDefinition (final short pageFormatCode, final int width,
                              final int height)
  {
    this.pageFormatCode = pageFormatCode;
    this.width = width;
    this.height = height;
  }

  /**
   * Return the excel page format code that describes that page size.
   *
   * @return the page format code as defined in the Excel File format.
   */
  public short getPageFormatCode ()
  {
    return pageFormatCode;
  }

  /**
   * Returns the defined page width for that page definition.
   *
   * @return the page width;
   */
  public int getWidth ()
  {
    return width;
  }

  /**
   * Returns the defined page height for that page definition.
   *
   * @return the page height;
   */
  public int getHeight ()
  {
    return height;
  }
}
