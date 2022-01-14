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
 * WorkerHandle.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util;

/**
 * The worker handle is a control structure which allows control over the worker without
 * exposing the thread object.
 *
 * @author Thomas Morgner
 * @deprecated This class is used by the WorkerPool, which is not used anywhere anymore.
 */
public class WorkerHandle
{
  /**
   * The worker for this handle.
   */
  private final Worker worker;

  /**
   * Creates a new handle for the given worker.
   *
   * @param worker the worker.
   */
  public WorkerHandle (final Worker worker)
  {
    this.worker = worker;
  }

  /**
   * Finishes the worker.
   */
  public void finish ()
  {
    worker.finish();
  }
}
