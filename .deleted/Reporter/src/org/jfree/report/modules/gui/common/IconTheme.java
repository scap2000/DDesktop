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
 * $Id: IconTheme.java 2813 2007-05-20 16:15:56Z taqua $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.common;

import java.util.Locale;

import javax.swing.Icon;

import org.jfree.util.Configuration;

/**
 * An Icon-Theme is an extension point to replace the icons that are used by
 * JFreeReport. Icons provided by the theme must be available in two flavours:
 * Large (24x24) and small (16x16).
 *
 * @author Thomas Morgner
 */
public interface IconTheme
{
  public void initialize(Configuration configuration);

  public Icon getSmallIcon(Locale locale, String id);

  public Icon getLargeIcon(Locale locale, String id);
}
