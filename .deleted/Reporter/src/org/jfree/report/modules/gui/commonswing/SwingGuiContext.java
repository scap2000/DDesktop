/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2000-2007, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * $Id: SwingGuiContext.java 3180 2007-08-15 15:19:27Z tmorgner $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.commonswing;

import java.awt.Window;

import org.jfree.report.modules.gui.common.GuiContext;
import org.jfree.report.modules.gui.common.StatusListener;

/**
 * Extends the common GUI-Context by a way to get access to the calling window. This is a neccessary evil to support
 * modal dialogs. Try to use this handle to tamper with the calling dialog in any other way, and you will suffer weird
 * and unhappy consequences.
 *
 * @author Thomas Morgner
 */
public interface SwingGuiContext extends GuiContext
{
  /**
   * Returns the calling window.
   *
   * @return the calling window, or null, if there is none.
   */
  public Window getWindow();

  public StatusListener getStatusListener();

  public ReportEventSource getEventSource();
}
