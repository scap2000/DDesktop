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
 * ReportStateKey.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.states;

/**
 * The process-state key is a unique functional identifier of report-states. However, it does not
 * get updated with changes to the report-state, so it always represents the process-state at the
 * beginning of the report-state processing. This key can be used to see whether there was some progress
 * and to uniquely identify report-states, but for everything else, this class is not suitable. 
 *
 * @author Thomas Morgner
 */
public class ReportStateKey
{
  private ReportStateKey parent;
  private int cursor;
  private int stateCode;
  private int groupLevel;
  private int subreport;
  private Integer hashCode;

  public ReportStateKey(final ReportStateKey parent,
                        final int cursor,
                        final int stateCode,
                        final int groupLevel,
                        final int subreport)
  {
    this.parent = parent;
    this.cursor = cursor;
    this.stateCode = stateCode;
    this.groupLevel = groupLevel;
    this.subreport = subreport;
  }

  public ReportStateKey getParent()
  {
    return parent;
  }

  public int getCursor()
  {
    return cursor;
  }

  public int getStateCode()
  {
    return stateCode;
  }

  public int getGroupLevel()
  {
    return groupLevel;
  }

  public int getSubreport()
  {
    return subreport;
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

    final ReportStateKey that = (ReportStateKey) o;

    if (cursor != that.cursor)
    {
      return false;
    }
    if (groupLevel != that.groupLevel)
    {
      return false;
    }
    if (stateCode != that.stateCode)
    {
      return false;
    }
    if (subreport != that.subreport)
    {
      return false;
    }
    if (parent != null ? !parent.equals(that.parent) : that.parent != null)
    {
      return false;
    }
    return true;
  }

  public int hashCode()
  {
    if (hashCode == null)
    {
      int result = (parent != null ? parent.hashCode() : 0);
      result = 29 * result + cursor;
      result = 29 * result + stateCode;
      result = 29 * result + groupLevel;
      result = 29 * result + subreport;
      hashCode = new Integer(result);
      return result;
    }
    return hashCode.intValue();
  }


  public String toString()
  {
    return "ReportStateKey{" +
        "parent=" + parent +
        ", cursor=" + cursor +
        ", stateCode=" + stateCode +
        ", groupLevel=" + groupLevel +
        ", subreport=" + subreport +
        '}';
  }
}
