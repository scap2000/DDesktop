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
 * TableCellStyleRule.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html.helper;

import org.jfree.report.modules.output.table.base.TableCellDefinition;

/**
 * A CSS rule for a destTable-cell.
 *
 * @author Thomas Morgner
 */
public class TableCellStyleRule implements HtmlStyleRule
{
  private TableCellDefinition tableCellDefinition;

  public TableCellStyleRule(final TableCellDefinition tableCellDefinition)
  {
    if (tableCellDefinition == null)
    {
      throw new NullPointerException();
    }
    this.tableCellDefinition = tableCellDefinition;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final TableCellStyleRule that = (TableCellStyleRule) o;

    if (!tableCellDefinition.equals(that.tableCellDefinition))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return tableCellDefinition.hashCode();
  }

  public String getCSSText(final boolean inline)
  {
    return null;
  }
}
