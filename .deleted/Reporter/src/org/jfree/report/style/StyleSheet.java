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
 * StyleSheet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.style;

import org.jfree.report.util.InstanceID;

/**
 * Creation-Date: 26.06.2006, 11:26:59
 *
 * @author Thomas Morgner
 */
public interface StyleSheet
{
  public boolean getBooleanStyleProperty (final StyleKey key);
  public boolean getBooleanStyleProperty (final StyleKey key, final boolean defaultValue);

  /**
   * @deprecated use the FontSpecification instead (or use the single properties).
   */
  public FontDefinition getFontDefinitionProperty ();
  public int getIntStyleProperty (final StyleKey key, final int def);
  public double getDoubleStyleProperty (final StyleKey key, final double def);
  public Object getStyleProperty (final StyleKey key);
  public Object getStyleProperty (final StyleKey key, final Object defaultValue);

  public InstanceID getId();
  public Object[] toArray (StyleKey[] definedKeys);
  public long getChangeTracker();
}
