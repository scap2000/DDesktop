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
 * InlineSequenceElement.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process.layoutrules;

import org.jfree.report.layout.model.RenderNode;

/**
 * A sequence element. Usually all elements get their maximum width. There are
 * only a few special cases, where the minimum width needs to be considered:
 *
 * * The element is an inline-block and there is not enough space to print he
 *   complete element. The element is guaranteed to always get its minimum width.
 *
 * @author Thomas Morgner
 */
public interface InlineSequenceElement
{
  /**
   * The minimum width of the element. This is the minimum width of the element.
   *
   * @return
   */
  public long getMinimumWidth(final RenderNode node);

  /**
   * The maximum width an element wants to take. This returns the preferred
   * size; even if offered more space, an element would not consume more than
   * that.
   *
   * @return
   */
  public long getMaximumWidth(final RenderNode node);

  public boolean isPreserveWhitespace(final RenderNode node);
}
