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
 * TableRowStyleRule.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html.helper;

import java.awt.Color;

import org.jfree.report.layout.model.BorderEdge;

/**
 * A CSS rule for a destTable-row. Unlike in the old days, this one is now generated on the fly while the report is
 * generated.
 *
 * @author Thomas Morgner
 */
public class TableRowStyleRule implements HtmlStyleRule
{
  private BorderEdge top;
  private BorderEdge bottom;
  private Color backgroundColor;
  private long height;

  public TableRowStyleRule()
  {
  }

  public BorderEdge getTop()
  {
    return top;
  }

  public void setTop(final BorderEdge top)
  {
    this.top = top;
  }

  public BorderEdge getBottom()
  {
    return bottom;
  }

  public void setBottom(final BorderEdge bottom)
  {
    this.bottom = bottom;
  }

  public Color getBackgroundColor()
  {
    return backgroundColor;
  }

  public void setBackgroundColor(final Color backgroundColor)
  {
    this.backgroundColor = backgroundColor;
  }

  public long getHeight()
  {
    return height;
  }

  public void setHeight(final long height)
  {
    this.height = height;
  }

  public String getCSSText(final boolean inline)
  {
    return null;
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

    final TableRowStyleRule that = (TableRowStyleRule) o;

    if (height != that.height)
    {
      return false;
    }
    if (backgroundColor != null ? !backgroundColor.equals(that.backgroundColor) : that.backgroundColor != null)
    {
      return false;
    }
    if (bottom != null ? !bottom.equals(that.bottom) : that.bottom != null)
    {
      return false;
    }
    if (top != null ? !top.equals(that.top) : that.top != null)
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = (top != null ? top.hashCode() : 0);
    result = 29 * result + (bottom != null ? bottom.hashCode() : 0);
    result = 29 * result + (backgroundColor != null ? backgroundColor.hashCode() : 0);
    result = 29 * result + (int) (height ^ (height >>> 32));
    return result;
  }
}
