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
 * FinishedRenderNode.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import org.jfree.report.layout.style.SimpleStyleSheet;

/**
 * A box replacement. It has a predefined width and height and does not change
 * those. It is a placeholder for all already printed content.
 * <p/>
 * If you see this node inside an inline box, you can be sure you've shot
 * yourself in the foot.
 *
 * @author Thomas Morgner
 */
public class FinishedRenderNode extends RenderNode
{
  private long layoutedWidth;
  private long layoutedHeight;
  private long marginsTop;
  private long marginsBottom;
  private boolean breakAfter;

  public FinishedRenderNode(final long layoutedWidth,
                            final long layoutedHeight,
                            final long marginsTop,
                            final long marginsBottom,
                            final boolean breakAfter)
  {
    super(SimpleStyleSheet.EMPTY_STYLE);
    if (layoutedWidth <= 0)
    {
      throw new IllegalStateException("Layouted Width is less than zero: " + layoutedWidth);
    }
    if (layoutedHeight < 0)
    {
      throw new IllegalStateException("Layouted Height is less than zero: " + layoutedHeight);
    }

    this.breakAfter = breakAfter;
    this.layoutedWidth = layoutedWidth;
    this.layoutedHeight = layoutedHeight;
    this.marginsBottom = marginsBottom;
    this.marginsTop = marginsTop;
    setFinished(true);
  }

  public boolean isBreakAfter()
  {
    return breakAfter;
  }

  public long getLayoutedWidth()
  {
    return layoutedWidth;
  }

  public long getLayoutedHeight()
  {
    return layoutedHeight;
  }

  public long getMarginsTop()
  {
    return marginsTop;
  }

  public long getMarginsBottom()
  {
    return marginsBottom;
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
    // Finished rows affect the margins ..
    return false;
  }

  public void updateParent (final RenderBox parent)
  {
    super.setParent(parent);
  }


}
