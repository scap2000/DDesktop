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
 * ReportProgressBar.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.commonswing;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.jfree.report.event.ReportProgressEvent;
import org.jfree.report.event.ReportProgressListener;

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
 * ReportProgressBar.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class ReportProgressBar extends JProgressBar implements ReportProgressListener
{
  private class ScreenUpdateRunnable implements Runnable
  {
    /**
     * This is the event upon which we will update the report progress information
     */
    private ReportProgressEvent reportProgressEvent;

    /**
     * Constructor for the screen updatable thread
     * @param reportProgressEvent the event information used to update the UI
     */
    protected ScreenUpdateRunnable()
    {
    }

    public ReportProgressEvent getReportProgressEvent()
    {
      return reportProgressEvent;
    }

    public void setReportProgressEvent(final ReportProgressEvent reportProgressEvent)
    {
      this.reportProgressEvent = reportProgressEvent;
    }

    /**
     * Performs the process of actually updaing the UI
     */
    public synchronized void run()
    {
      if (reportProgressEvent == null)
      {
        return;
      }

      setValue((int) computePercentageComplete(reportProgressEvent));
      reportProgressEvent = null;
    }

    public boolean update(final ReportProgressEvent event)
    {
      final boolean retval = (reportProgressEvent == null);
      this.reportProgressEvent = event;
      return retval;
    }
  }

  /**
   * Indicates if this process is only for pagination
   */
  private boolean onlyPagination;
  private final ScreenUpdateRunnable runnable;

  /**
   * Creates a horizontal progress bar that displays a border but no progress string. The
   * initial and minimum values are 0, and the maximum is 100.
   *
   * @see #setOrientation
   * @see #setBorderPainted
   * @see #setStringPainted
   * @see #setString
   * @see #setIndeterminate
   */
  public ReportProgressBar()
  {
    super(JProgressBar.HORIZONTAL, 0, 100);
    this.runnable = new ScreenUpdateRunnable();
  }

  public boolean isOnlyPagination()
  {
    return onlyPagination;
  }

  public void setOnlyPagination(final boolean onlyPagination)
  {
    this.onlyPagination = onlyPagination;
  }

  public void reportProcessingStarted(final ReportProgressEvent event)
  {
    synchronized(runnable)
    {
      if (runnable.update(event))
      {
        if (SwingUtilities.isEventDispatchThread())
        {
          runnable.run();
        }
        else
        {
          SwingUtilities.invokeLater(runnable);
        }
      }
    }
  }

  public void reportProcessingUpdate(final ReportProgressEvent event)
  {
    synchronized(runnable)
    {
      if (runnable.update(event))
      {
        if (SwingUtilities.isEventDispatchThread())
        {
          runnable.run();
        }
        else
        {
          SwingUtilities.invokeLater(runnable);
        }
      }
    }
  }

  public void reportProcessingFinished(final ReportProgressEvent event)
  {
    synchronized(runnable)
    {
      if (runnable.update(event))
      {
        if (SwingUtilities.isEventDispatchThread())
        {
          runnable.run();
        }
        else
        {
          SwingUtilities.invokeLater(runnable);
        }
      }
    }
  }

  /**
   * Computes the percentage complete (on a scale from 0.0 to 100.0) based on the
   * information found in the report progress event.
   * @param event the data used to calculate the percentage complete
   * @return the calculated percentage complete 
   */
  protected double computePercentageComplete(final ReportProgressEvent event)
  {
    final double maximumLevel;
    final double level;
    if (isOnlyPagination())
    {
      maximumLevel = event.getMaximumLevel();
      level = event.getLevel();
    }
    else
    {
      maximumLevel = event.getMaximumLevel() + 1;
      if (event.getActivity() == ReportProgressEvent.GENERATING_CONTENT)
      {
        level = event.getLevel() + 1;
      }
      else
      {
        level = event.getLevel();
      }
    }
    final double levelPercentage = level / maximumLevel;
    final double levelSizePercentage = 1.0 / maximumLevel;
    final double subPercentage = levelSizePercentage * (event.getRow() / (double) event.getMaximumRow());
    final double percentage = 100.0 * (levelPercentage + subPercentage);
    return Math.max(0.0, Math.min(100.0, percentage));
  }
}
