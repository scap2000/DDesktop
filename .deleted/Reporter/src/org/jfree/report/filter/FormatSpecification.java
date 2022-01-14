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
 * FormatSpecification.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter;

/**
 * Todo: Document me!
 *
 * @author : Thomas Morgner
 */
public class FormatSpecification
{
  public static final int TYPE_UNDEFINED = 0;
  public static final int TYPE_DATE_FORMAT = 1;
  public static final int TYPE_DECIMAL_FORMAT = 2;
  public static final int TYPE_MESSAGE_FORMAT = 3;
  public static final int TYPE_CHOICE_FORMAT = 4;

  private int type;
  private String formatString;

  public FormatSpecification()
  {
  }

  public void redefine (int type, String formatString)
  {
    this.type = type;
    this.formatString = formatString;
  }

  public int getType()
  {
    return type;
  }

  public String getFormatString()
  {
    return formatString;
  }
}
