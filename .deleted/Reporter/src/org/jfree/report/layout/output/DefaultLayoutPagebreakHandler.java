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
 * DefaultLayoutPagebreakHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import org.jfree.report.states.ReportState;

/**
 * Creation-Date: 22.04.2007, 13:47:41
 *
 * @author Thomas Morgner
 */
public class DefaultLayoutPagebreakHandler implements LayoutPagebreakHandler, Cloneable
{
  private ReportState reportState;

  public DefaultLayoutPagebreakHandler(final ReportState reportState)
  {
    this.reportState = reportState;
  }

  public DefaultLayoutPagebreakHandler()
  {
  }

  public ReportState getReportState()
  {
    return reportState;
  }

  public void setReportState(final ReportState reportState)
  {
    this.reportState = reportState;
  }

  public void pageFinished()
  {
    if (reportState == null)
    {
      throw new IllegalStateException
          ("A Report-State must be given. If you dont have a report state, then you're doing something wrong.");
    }
    reportState.firePageFinishedEvent();
  }

  public void pageStarted()
  {
    if (reportState == null)
    {
      throw new IllegalStateException
          ("A Report-State must be given. If you dont have a report state, then you're doing something wrong.");
    }
    reportState.setCurrentPage(reportState.getCurrentPage() + 1);
    reportState.firePageStartedEvent(reportState.getEventCode());
  }

  public Object clone () throws CloneNotSupportedException
  {
    return super.clone();
  }
}
