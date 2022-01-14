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
 * SpacerRenderNode.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import org.jfree.report.layout.style.SimpleStyleSheet;

/**
 * A spacer reserves space for whitespaces found in the text. When encountered
 * at the beginning or end of lines, it gets removed.
 * <p/>
 * Spacers are always considered discardable, so when encountered alone, they
 * will get pruned.
 *
 * @author Thomas Morgner
 */
public class SpacerRenderNode extends RenderNode
{
  private boolean empty;
  private boolean preserve;

  public SpacerRenderNode()
  {
    this(0,0,false);
  }

  public SpacerRenderNode(final long width,
                          final long height,
                          final boolean preserve)
  {
    super(SimpleStyleSheet.EMPTY_STYLE);
    this.preserve = preserve;
    setMaximumBoxWidth(width);
    setMinimumChunkWidth(0);
    empty = width == 0 && height == 0;

    // Major axis: All child boxes are placed from left-to-right
    setMajorAxis(HORIZONTAL_AXIS);
    // Minor: The childs might be aligned on their position (shifted up or down)
    setMinorAxis(VERTICAL_AXIS);

  }

  public boolean isEmpty()
  {
    return empty;
  }

  public boolean isDiscardable()
  {
    return preserve == false;
  }

  /**
   * If that method returns true, the element will not be used for rendering.
   * For the purpose of computing sizes or performing the layouting (in the
   * validate() step), this element will treated as if it is not there.
   * <p/>
   * If the element reports itself as non-empty, however, it will affect the
   * margin computation.
   *
   * @return
   */
  public boolean isIgnorableForRendering()
  {
    return true;
  }

}
