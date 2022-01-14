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
 * YieldReportListener.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import org.jfree.report.event.ReportProgressEvent;
import org.jfree.report.event.ReportProgressListener;

/**
 * A report listener that calls Thread.yield() on each generated event. Although this slows down the report processing a
 * bit, this also makes the application a lot more responsive as the report-thread does no longer block the CPU all the
 * time.
 *
 * @author Thomas Morgner
 */
public class YieldReportListener implements ReportProgressListener
{
  private int rate;

  private transient int lastCall;

  private transient int lastPage;

  public YieldReportListener()
  {
    rate = 50;
  }

  public YieldReportListener(final int rate)
  {
    this.rate = rate;
  }

  public int getRate()
  {
    return rate;
  }

  public void setRate(final int rate)
  {
    this.rate = rate;
  }

  public void reportProcessingStarted(final ReportProgressEvent event)
  {

  }

  public void reportProcessingFinished(final ReportProgressEvent event)
  {

  }

  public void reportProcessingUpdate(final ReportProgressEvent event)
  {
    final int currentRow = event.getRow();
    final int thisCall = currentRow % rate;
    final int page = event.getPage();

    if (page != lastPage)
    {
      Thread.yield();
    }
    else if (thisCall != lastCall)
    {
      Thread.yield();
    }
    lastCall = thisCall;
    lastPage = page;
  }
}
