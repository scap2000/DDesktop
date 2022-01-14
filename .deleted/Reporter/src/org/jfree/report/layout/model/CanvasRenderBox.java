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
 * CanvasRenderBox.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.style.StyleSheet;

/**
 * A renderbox that behaves like a huge canvas. Elements can be painted freely on this box.
 *
 * @author Thomas Morgner
 */
public class CanvasRenderBox extends RenderBox
{
  public CanvasRenderBox(final StyleSheet styleSheet,
                         final BoxDefinition boxDefinition,
                         final Object stateKey)
  {
    super(styleSheet, boxDefinition, stateKey);
  }
}
