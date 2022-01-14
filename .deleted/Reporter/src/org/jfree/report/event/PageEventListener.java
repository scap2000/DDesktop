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
 * PageEventListener.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.event;

import java.util.EventListener;

/**
 * The PageEventListener gets informed of PageEvents.
 * <p/>
 * This is an extracted interface of the original ReportEventListener. As page events are
 * only fired by some (page sensitive) report processors, there is no need to support page
 * events in the ReportEventListener interface.
 * <p/>
 * Functions that should be informed of page events should implement this interface.
 * <p/>
 * Information: The pageCanceled method is called, if a empty page was created and was
 * removed from the report afterwards.
 *
 * @author Thomas Morgner
 */
public interface PageEventListener extends EventListener
{

  /**
   * Receives notification that a new page is being started.
   *
   * @param event The event.
   */
  public void pageStarted (ReportEvent event);

  /**
   * Receives notification that a page is completed.
   *
   * @param event The event.
   */
  public void pageFinished (ReportEvent event);
}
