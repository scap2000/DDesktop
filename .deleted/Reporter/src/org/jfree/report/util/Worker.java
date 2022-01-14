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
 * Worker.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util;

/**
 * A simple worker implementation. The worker executes a assigned workload and then sleeps
 * until another workload is set or the worker is killed.
 *
 * @author Thomas Morgner
 */
public final class Worker extends Thread
{
  /**
   * the worker's task.
   */
  private Runnable workload;

  /**
   * a flag whether the worker should exit after the processing.
   */
  private volatile boolean finish;

  /**
   * the time in milliseconds beween 2 checks for exit or work requests.
   */
  private final int sleeptime;
  /**
   * The worker pool, to which this worker is assigned. May be null.
   */
  private WorkerPool workerPool;

  /**
   * Creates a new worker.
   *
   * @param sleeptime the time this worker sleeps until he checks for new work.
   */
  public Worker (final int sleeptime)
  {
    this.sleeptime = sleeptime;
    this.setDaemon(true);
    start();
  }

  /**
   * Creates a new worker with an default idle timeout of 2 minutes.
   */
  public Worker ()
  {
    this(120000);
  }

  /**
   * Set the next workload for this worker.
   *
   * @param r the next workload for the worker.
   * @throws IllegalStateException if the worker is not idle.
   */
  public void setWorkload (final Runnable r)
  {
    if (workload != null)
    {
      throw new IllegalStateException("This worker is not idle.");
    }
    //Log.debug("Workload set...");
    synchronized (this)
    {
      workload = r;
      //Log.debug("Workload assigned: Notified " + getName());
      this.notifyAll();
    }
  }

  /**
   * Returns the workload object.
   *
   * @return the runnable executed by this worker thread.
   */
  public synchronized Runnable getWorkload()
  {
    return workload;
  }

  /**
   * Kills the worker after he completed his work. Awakens the worker if he's sleeping, so
   * that the worker dies without delay.
   */
  public void finish ()
  {
    finish = true;
    // we are evil ..
    try
    {
      this.interrupt();
    }
    catch (SecurityException se)
    {
      // ignored
    }
    if (workerPool != null)
    {
      workerPool.workerFinished(this);
    }
  }

  /**
   * Checks, whether this worker has some work to do.
   *
   * @return true, if this worker has no more work and is currently sleeping.
   */
  public boolean isAvailable ()
  {
    return (workload == null);
  }

  /**
   * If a workload is set, process it. After the workload is processed, this worker starts
   * to sleep until a new workload is set for the worker or the worker got the finish()
   * request.
   */
  public synchronized void run ()
  {
    while (!finish)
    {
      if (workload != null)
      {
        try
        {
          workload.run();
        }
        catch (Exception e)
        {
          org.jfree.util.Log.error("Worker caught exception on run: ", e);
        }
        workload = null;
        if (workerPool != null)
        {
          workerPool.workerAvailable(this);
        }
      }

      synchronized (this)
      {
        try
        {
          // remove lock
          this.wait(sleeptime);
        }
        catch (InterruptedException ie)
        {
          // ignored
        }
      }
    }
  }

  /**
   * Checks whether this worker has received the signal to finish and die.
   *
   * @return true, if the worker should finish the work and end the thread.
   */
  public boolean isFinish ()
  {
    return finish;
  }

  /**
   * Returns the worker's assigned pool.
   *
   * @return the worker pool (or null, if the worker is not assigned to a pool).
   */
  public WorkerPool getWorkerPool ()
  {
    return workerPool;
  }

  /**
   * Defines the worker's assigned pool.
   *
   * @param workerPool the worker pool (or null, if the worker is not assigned to a
   *                   pool).
   */
  public void setWorkerPool (final WorkerPool workerPool)
  {
    this.workerPool = workerPool;
  }
}
