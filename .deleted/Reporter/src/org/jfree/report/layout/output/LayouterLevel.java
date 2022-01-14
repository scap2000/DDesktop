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
 * LayouterLevel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import org.jfree.report.ReportDefinition;

/**
 * Creation-Date: Jan 22, 2007, 12:20:04 PM
 *
 * @author Thomas Morgner
 */
public class LayouterLevel
{
  private ReportDefinition reportDefinition;
  private int groupIndex;
  private LayoutExpressionRuntime runtime;

  public LayouterLevel(final ReportDefinition reportDefinition,
                       final int groupIndex,
                       final LayoutExpressionRuntime runtime)
  {
    this.reportDefinition = reportDefinition;
    this.groupIndex = groupIndex;
    this.runtime = runtime;
  }

  public ReportDefinition getReportDefinition()
  {
    return reportDefinition;
  }

  public int getGroupIndex()
  {
    return groupIndex;
  }

  public LayoutExpressionRuntime getRuntime()
  {
    return runtime;
  }
}
