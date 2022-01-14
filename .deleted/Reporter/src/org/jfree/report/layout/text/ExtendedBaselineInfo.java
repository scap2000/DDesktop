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
 * ExtendedBaselineInfo.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.text;

/**
 * Creation-Date: 04.04.2007, 14:47:05
 *
 * @author Thomas Morgner
 */
public interface ExtendedBaselineInfo
{
  public static final int BASELINE_COUNT = 10;

  public static final int BEFORE_EDGE = 0;
  public static final int TEXT_BEFORE_EDGE = 1;
  public static final int HANGING = 2;
  public static final int CENTRAL = 3;
  public static final int MIDDLE = 4;
  public static final int MATHEMATICAL = 5;
  public static final int ALPHABETHIC = 6;
  public static final int IDEOGRAPHIC = 7;
  public static final int TEXT_AFTER_EDGE = 8;
  public static final int AFTER_EDGE = 9;

  public int getDominantBaseline();

  public long[] getBaselines();
  public long getBaseline (int baseline);

  public long getUnderlinePosition();
  public long getStrikethroughPosition();
}
