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
 * PageItemCountFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;

/**
 * An ItemCount function, that is reset to zero on every new page.
 *
 * @author Thomas Morgner
 */
public class PageItemCountFunction extends ItemCountFunction implements PageEventListener
{
  /**
   * Default Constructor.
   */
  public PageItemCountFunction()
  {
  }

  /**
   * Handles the pageStartedEvent.
   *
   * @param event the report event.
   */
  public void pageStarted(final ReportEvent event)
  {
  }

  /**
   * Handles the pageFinishedEvent. This method is emtpy and only here as implementation side effect.
   *
   * @param event the report event.
   */
  public void pageFinished(final ReportEvent event)
  {
    setCount(0);
  }
}
