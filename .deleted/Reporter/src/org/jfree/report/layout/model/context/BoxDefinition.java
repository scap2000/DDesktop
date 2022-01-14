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
 * BoxDefinition.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model.context;

import org.jfree.report.layout.model.Border;
import org.jfree.report.layout.model.RenderLength;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * A box definition. The paddings or maximum height/width cannot be percentages or AUTO.
 *
 * @author Thomas Morgner
 */
public final class BoxDefinition
{
  public static final BoxDefinition EMPTY = new BoxDefinition().lock();
  public static final RenderLength DEFAULT_PREFERRED_WIDTH =
      new RenderLength(StrictGeomUtility.toInternalValue(100), true);

  private Boolean empty;
  private long paddingTop;
  private long paddingLeft;
  private long paddingBottom;
  private long paddingRight;
  private Border border;
  private RenderLength preferredHeight;
  private RenderLength preferredWidth;
  private RenderLength minimumHeight;
  private RenderLength minimumWidth;
  private RenderLength marginTop;
  private RenderLength marginBottom;
  private RenderLength marginLeft;
  private RenderLength marginRight;
  private RenderLength maximumHeight;
  private RenderLength maximumWidth;
  private RenderLength positionX;
  private RenderLength positionY;
  private RenderLength fixedPosition;
  private boolean locked;
  private boolean sizeSpecifiesBorderBox;

  public BoxDefinition()
  {
    border = Border.EMPTY_BORDER;
    preferredWidth = RenderLength.AUTO;
    preferredHeight = RenderLength.AUTO;
    minimumHeight = RenderLength.EMPTY;
    minimumWidth = RenderLength.EMPTY;
    marginTop = RenderLength.EMPTY;
    marginLeft = RenderLength.EMPTY;
    marginBottom = RenderLength.EMPTY;
    marginRight = RenderLength.EMPTY;
    maximumWidth = RenderLength.AUTO;
    maximumHeight = RenderLength.AUTO;
    positionX = RenderLength.AUTO;
    positionY = RenderLength.AUTO;
    fixedPosition = RenderLength.AUTO;
    sizeSpecifiesBorderBox = true;
  }

  public void setSizeSpecifiesBorderBox(final boolean sizeSpecifiesBorderBox)
  {
    this.sizeSpecifiesBorderBox = sizeSpecifiesBorderBox;
  }

  public boolean isSizeSpecifiesBorderBox()
  {
    return sizeSpecifiesBorderBox;
  }

  public BoxDefinition lock()
  {
    locked = true;
    return this;
  }

  public BoxDefinition derive ()
  {
    final BoxDefinition retval = new BoxDefinition();
    retval.border = border;
    retval.preferredWidth = preferredWidth;
    retval.preferredHeight = preferredHeight;
    retval.minimumHeight = minimumHeight;
    retval.minimumWidth = minimumWidth;
    retval.marginTop = marginTop;
    retval.marginLeft = marginLeft;
    retval.marginBottom = marginBottom;
    retval.marginRight = marginRight;
    retval.maximumWidth = maximumWidth;
    retval.maximumHeight = maximumHeight;
    retval.positionX = positionX;
    retval.positionY = positionY;
    retval.fixedPosition = fixedPosition;
    retval.locked = locked;
    return retval;
  }

  public RenderLength getFixedPosition()
  {
    return fixedPosition;
  }

  public void setFixedPosition(final RenderLength fixedPosition)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }

    if (fixedPosition == null)
    {
      throw new NullPointerException();
    }

    this.fixedPosition = fixedPosition;
  }
//
//  public RenderLength getPositionX()
//  {
//    return positionX;
//  }
//
//  public void setPositionX(final RenderLength positionX)
//  {
//    if (locked)
//    {
//      throw new IllegalStateException();
//    }
//
//    if (positionX == null)
//    {
//      throw new NullPointerException();
//    }
//
//    this.positionX = positionX;
//  }
//
//  public RenderLength getPositionY()
//  {
//    return positionY;
//  }
//
//  public void setPositionY(final RenderLength positionY)
//  {
//    if (locked)
//    {
//      throw new IllegalStateException();
//    }
//
//    if (positionY == null)
//    {
//      throw new NullPointerException();
//    }
//
//    this.positionY = positionY;
//  }

  public Border getBorder()
  {
    return border;
  }

  public void setBorder(final Border border)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }

    if (border == null)
    {
      throw new NullPointerException();
    }

    this.border = border;
  }

  public long getPaddingTop()
  {
    return paddingTop;
  }

  public void setPaddingTop(final long paddingTop)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }

    this.paddingTop = paddingTop;
  }

  public long getPaddingLeft()
  {
    return paddingLeft;
  }

  public void setPaddingLeft(final long paddingLeft)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }

    this.paddingLeft = paddingLeft;
  }

  public long getPaddingBottom()
  {
    return paddingBottom;
  }

  public void setPaddingBottom(final long paddingBottom)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }

    this.paddingBottom = paddingBottom;
  }

  public long getPaddingRight()
  {
    return paddingRight;
  }

  public void setPaddingRight(final long paddingRight)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    this.paddingRight = paddingRight;
  }

  public RenderLength getPreferredHeight()
  {
    return preferredHeight;
  }

  public void setPreferredHeight(final RenderLength preferredHeight)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (preferredHeight == null)
    {
      throw new NullPointerException();
    }

    this.preferredHeight = preferredHeight;
  }

  public RenderLength getPreferredWidth()
  {
    return preferredWidth;
  }

  public void setPreferredWidth(final RenderLength preferredWidth)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (preferredWidth == null)
    {
      throw new NullPointerException();
    }

    this.preferredWidth = preferredWidth;
  }

  public RenderLength getMinimumHeight()
  {
    return minimumHeight;
  }

  public void setMinimumHeight(final RenderLength minimumHeight)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (minimumHeight == null)
    {
      throw new NullPointerException();
    }
    this.minimumHeight = minimumHeight;
  }

  public RenderLength getMinimumWidth()
  {
    return minimumWidth;
  }

  public void setMinimumWidth(final RenderLength minimumWidth)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (minimumWidth == null)
    {
      throw new NullPointerException();
    }
    this.minimumWidth = minimumWidth;
  }

  public RenderLength getMaximumHeight()
  {
    return maximumHeight;
  }

  public void setMaximumHeight(final RenderLength maximumHeight)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (maximumHeight == null)
    {
      throw new NullPointerException();
    }

    this.maximumHeight = maximumHeight;
  }

  public RenderLength getMaximumWidth()
  {
    return maximumWidth;
  }

  public void setMaximumWidth(final RenderLength maximumWidth)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (maximumWidth == null)
    {
      throw new NullPointerException();
    }

    this.maximumWidth = maximumWidth;
  }

  public RenderLength getMarginTop()
  {
    return marginTop;
  }

  public void setMarginTop(final RenderLength marginTop)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (marginTop == null)
    {
      throw new NullPointerException();
    }

    this.marginTop = marginTop;
  }

  public RenderLength getMarginBottom()
  {
    return marginBottom;
  }

  public void setMarginBottom(final RenderLength marginBottom)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (marginBottom == null)
    {
      throw new NullPointerException();
    }

    this.marginBottom = marginBottom;
  }

  public RenderLength getMarginLeft()
  {
    return marginLeft;
  }

  public void setMarginLeft(final RenderLength marginLeft)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (marginLeft == null)
    {
      throw new NullPointerException();
    }
    this.marginLeft = marginLeft;
  }

  public RenderLength getMarginRight()
  {
    return marginRight;
  }

  public void setMarginRight(final RenderLength marginRight)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }
    if (marginRight == null)
    {
      throw new NullPointerException();
    }
    this.marginRight = marginRight;
  }

  public boolean isEmpty()
  {
    if (empty != null)
    {
      return empty.booleanValue();
    }

    if (paddingTop != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (paddingLeft != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (paddingBottom != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (paddingRight != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }

    if (border.isEmpty())
    {
      empty = Boolean.FALSE;
      return false;
    }

    empty = Boolean.TRUE;
    return true;
  }


  /**
   * Split the box definition for the given major axis. A horizontal axis will perform vertical splits (resulting in a
   * left and right box definition) and a given vertical axis will split the box into a top and bottom box.
   *
   * @param axis
   * @return
   */
  public BoxDefinition[] split(final int axis)
  {
    if (axis == RenderNode.HORIZONTAL_AXIS)
    {
      return splitVertically();
    }
    return splitHorizontally();
  }

  private BoxDefinition[] splitVertically()
  {
    final Border[] borders = border.splitVertically(null);
    final BoxDefinition first = new BoxDefinition();
    first.marginTop = marginTop;
    first.marginLeft = marginLeft;
    first.marginBottom = marginBottom;
    first.marginRight = RenderLength.EMPTY;
    first.paddingBottom = paddingBottom;
    first.paddingTop = paddingTop;
    first.paddingLeft = paddingLeft;
    first.paddingRight = 0;
    first.border = borders[0];
    first.preferredHeight = preferredHeight;
    first.preferredWidth = preferredWidth;
    first.minimumHeight = minimumHeight;
    first.minimumWidth = minimumWidth;
    first.maximumHeight = maximumHeight;
    first.maximumWidth = maximumWidth;
    first.positionX = RenderLength.AUTO;
    first.positionY = positionY;

    final BoxDefinition second = new BoxDefinition();
    second.marginTop = marginTop;
    second.marginLeft = RenderLength.EMPTY;
    second.marginBottom = marginBottom;
    second.marginRight = marginRight;
    second.paddingBottom = paddingBottom;
    second.paddingTop = paddingTop;
    second.paddingLeft = 0;
    second.paddingRight = paddingRight;
    second.border = borders[1];
    second.preferredHeight = preferredHeight;
    second.preferredWidth = preferredWidth;
    second.minimumHeight = minimumHeight;
    second.minimumWidth = minimumWidth;
    second.maximumHeight = maximumHeight;
    second.maximumWidth = maximumWidth;
    second.positionX = RenderLength.AUTO;
    second.positionY = positionY;

    final BoxDefinition[] boxes = new BoxDefinition[2];
    boxes[0] = first;
    boxes[1] = second;
    return boxes;
  }

  private BoxDefinition[] splitHorizontally()
  {
    final Border[] borders = border.splitHorizontally(null);

    final BoxDefinition first = new BoxDefinition();
    first.marginTop = marginTop;
    first.marginLeft = marginLeft;
    first.marginBottom = RenderLength.EMPTY;
    first.marginRight = marginRight;
    first.paddingBottom = 0;
    first.paddingTop = paddingTop;
    first.paddingLeft = paddingLeft;
    first.paddingRight = paddingRight;
    first.border = borders[0];
    first.preferredHeight = preferredHeight;
    first.preferredWidth = preferredWidth;
    first.minimumHeight = minimumHeight;
    first.minimumWidth = minimumWidth;
    first.maximumHeight = maximumHeight;
    first.maximumWidth = maximumWidth;
    first.positionX = positionX;
    first.positionY = RenderLength.AUTO;

    final BoxDefinition second = new BoxDefinition();
    second.marginTop = RenderLength.EMPTY;
    second.marginLeft = marginLeft;
    second.marginBottom = marginBottom;
    second.marginRight = marginRight;
    second.paddingBottom = paddingBottom;
    second.paddingTop = 0;
    second.paddingLeft = paddingLeft;
    second.paddingRight = paddingRight;
    second.border = borders[1];
    second.preferredHeight = preferredHeight;
    second.preferredWidth = preferredWidth;
    second.minimumHeight = minimumHeight;
    second.minimumWidth = minimumWidth;
    second.maximumHeight = maximumHeight;
    second.maximumWidth = maximumWidth;
    second.positionX = positionX;
    second.positionY = RenderLength.AUTO;

    final BoxDefinition[] boxes = new BoxDefinition[2];
    boxes[0] = first;
    boxes[1] = second;
    return boxes;
  }


  public String toString()
  {
    return "BoxDefinition{" +
        "paddingTop=" + paddingTop +
        ", paddingLeft=" + paddingLeft +
        ", paddingBottom=" + paddingBottom +
        ", paddingRight=" + paddingRight +
        ", border=" + border +
        ", preferredHeight=" + preferredHeight +
        ", preferredWidth=" + preferredWidth +
        ", minimumHeight=" + minimumHeight +
        ", minimumWidth=" + minimumWidth +
        ", marginTop=" + marginTop +
        ", marginBottom=" + marginBottom +
        ", marginLeft=" + marginLeft +
        ", marginRight=" + marginRight +
        ", maximumHeight=" + maximumHeight +
        ", maximumWidth=" + maximumWidth +
        ", positionX=" + positionX +
        ", positionY=" + positionY +
        '}';
  }
}
