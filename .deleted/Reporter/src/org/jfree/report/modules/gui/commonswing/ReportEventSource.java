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
 * ReportEventSource.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.commonswing;

import java.beans.PropertyChangeListener;

import org.jfree.report.JFreeReport;

/**
 * Creation-Date: 15.08.2007, 16:20:24
 *
 * @author Thomas Morgner
 */
public interface ReportEventSource
{
  public int getPageNumber();
  public int getNumberOfPages();
  public boolean isPaginated();
  public boolean isPaginating();
  public JFreeReport getReportJob();

  public void addPropertyChangeListener(final PropertyChangeListener propertyChangeListener);
  public void addPropertyChangeListener(final String property, final PropertyChangeListener propertyChangeListener);

  public void removePropertyChangeListener(final PropertyChangeListener propertyChangeListener);
  public void removePropertyChangeListener(final String property, final PropertyChangeListener propertyChangeListener);
}
