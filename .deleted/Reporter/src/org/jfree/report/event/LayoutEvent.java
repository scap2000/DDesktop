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
 * LayoutEvent.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.event;

import org.jfree.report.Band;
import org.jfree.report.states.ReportState;

/**
 * The LayoutEvent describes the current report state and the current band, which had been
 * laid out for printing.
 *
 * @author Thomas Morgner
 * @deprecated The engine does no longer generate layout events.
 */
public class LayoutEvent extends ReportEvent
{
  /**
   * The type constant to mark a LayoutEvent.
   */
  public static final int LAYOUT_COMPLETE_EVENT = 0x20000;
  /**
   * The type constant to mark a Output-Complete event.
   * @deprecated This event type is no longer used.
   */
  public static final int OUTPUT_COMPLETE_EVENT = 0x40000;

  /**
   * the current band.
   */
  private Band layoutedBand;

  /**
   * Creates a new LayoutEvent.
   *
   * @param state the current report state.
   * @param band  the layouted band.
   * @param type  the event type.
   */
  public LayoutEvent (final ReportState state, final Band band, final int type)
  {
    super(state, type);

    if (band == null)
    {
      throw new NullPointerException();
    }
    this.layoutedBand = band;
  }

  /**
   * Gets the layouted band. This band will be printed next.
   *
   * @return the layouted band.
   */
  public Band getLayoutedBand ()
  {
    return layoutedBand;
  }
}
