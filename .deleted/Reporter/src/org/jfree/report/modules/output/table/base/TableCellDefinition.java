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
 * TableCellDefinition.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.base;

import java.awt.Color;
import java.awt.geom.Line2D;

import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.model.Border;
import org.jfree.report.layout.model.BorderCorner;
import org.jfree.report.layout.model.BorderEdge;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.model.context.StaticBoxLayoutProperties;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 02.05.2007, 16:10:32
 *
 * @author Thomas Morgner
 */
public class TableCellDefinition implements Cloneable
{
  private BorderEdge top;
  private BorderEdge left;
  private BorderEdge bottom;
  private BorderEdge right;

  private BorderCorner topLeft;
  private BorderCorner topRight;
  private BorderCorner bottomLeft;
  private BorderCorner bottomRight;

  private long x;
  private long y;
  private long width;
  private long height;

  private Color backgroundColor;
  private ElementAlignment verticalAlignment;

  private String name;
  private String anchor;
  private Line2D lineHint;
  private String mergeHistory;

  public TableCellDefinition(final RenderBox node, final long shift)
  {
    final StaticBoxLayoutProperties sblp = node.getStaticBoxLayoutProperties();
    final BoxDefinition boxDefinition = node.getBoxDefinition();
    final Border border = boxDefinition.getBorder();
    this.bottom = resolveBorder(border.getBottom(), sblp.getBorderBottom());
    this.left = resolveBorder(border.getLeft(), sblp.getBorderLeft());
    this.right = resolveBorder(border.getRight(), sblp.getBorderRight());

    this.bottomRight = border.getBottomRight();
    this.bottomLeft = border.getBottomLeft();

    final long shiftedY = node.getY() + shift;
    if (shiftedY < 0)
    {
      this.topLeft = BorderCorner.EMPTY;
      this.topRight = BorderCorner.EMPTY;
      this.top = BorderEdge.EMPTY;
      this.y = 0;
      // Shifted-Y is negative, so this just corrects the height ..
      this.height = node.getHeight() + shiftedY;
    }
    else
    {
      this.topLeft = border.getTopLeft();
      this.topRight = border.getTopRight();
      this.top = resolveBorder(border.getTop(), sblp.getBorderTop());
      this.y = shiftedY;
      this.height = node.getHeight();
    }
    this.width = node.getWidth();
    this.x = node.getX();
    this.verticalAlignment = node.getNodeLayoutProperties().getVerticalAlignment();

    this.name = node.getName();

    final StyleSheet styleSheet = node.getStyleSheet();
    this.anchor = (String) styleSheet.getStyleProperty(ElementStyleKeys.ANCHOR_NAME);
    this.backgroundColor = (Color) styleSheet.getStyleProperty(ElementStyleKeys.BACKGROUND_COLOR);
  }

  private BorderEdge resolveBorder(final BorderEdge border, final long borderWidth)
  {
    if (borderWidth <= 0)
    {
      return BorderEdge.EMPTY;
    }
    if (border.getWidth() == borderWidth)
    {
      return border;
    }
    return new BorderEdge(border.getBorderStyle(), border.getColor(), borderWidth);
  }

  public String getMergeHistory()
  {
    return mergeHistory;
  }

  public Line2D getLineHint()
  {
    return lineHint;
  }

  public void setLineHint(final Line2D lineHint)
  {
    this.lineHint = lineHint;
  }

  protected void setVerticalAlignment(final ElementAlignment verticalAlignment)
  {
    this.verticalAlignment = verticalAlignment;
  }

  protected void setAnchor(final String anchor)
  {
    this.anchor = anchor;
  }

  public String getAnchor()
  {
    return anchor;
  }

  public String getName()
  {
    return name;
  }

  public BorderEdge getTop()
  {
    return top;
  }

  public void setTop(final BorderEdge top)
  {
    if (top == null)
    {
      throw new NullPointerException();
    }
    this.top = top;
  }

  public BorderEdge getLeft()
  {
    return left;
  }

  public void setLeft(final BorderEdge left)
  {
    if (left == null)
    {
      throw new NullPointerException();
    }
    this.left = left;
  }

  public BorderEdge getBottom()
  {
    return bottom;
  }

  public void setBottom(final BorderEdge edge)
  {
    if (edge == null)
    {
      throw new NullPointerException();
    }
    this.bottom = edge;
  }

  public BorderEdge getRight()
  {
    return right;
  }

  public void setRight(final BorderEdge edge)
  {
    if (edge == null)
    {
      throw new NullPointerException();
    }
    this.right = edge;
  }

  public void setTopLeft(final BorderCorner topLeft)
  {
    if (topLeft == null)
    {
      throw new NullPointerException();
    }
    this.topLeft = topLeft;
  }

  public void setTopRight(final BorderCorner topRight)
  {
    if (topRight == null)
    {
      throw new NullPointerException();
    }
    this.topRight = topRight;
  }

  public void setBottomLeft(final BorderCorner bottomLeft)
  {
    if (bottomLeft == null)
    {
      throw new NullPointerException();
    }
    this.bottomLeft = bottomLeft;
  }

  public void setBottomRight(final BorderCorner bottomRight)
  {
    if (bottomRight == null)
    {
      throw new NullPointerException();
    }
    this.bottomRight = bottomRight;
  }

  public BorderCorner getTopLeft()
  {
    return topLeft;
  }

  public BorderCorner getTopRight()
  {
    return topRight;
  }

  public BorderCorner getBottomLeft()
  {
    return bottomLeft;
  }

  public BorderCorner getBottomRight()
  {
    return bottomRight;
  }

  protected long getX()
  {
    return x;
  }

  protected long getY()
  {
    return y;
  }

  protected long getWidth()
  {
    return width;
  }

  protected long getHeight()
  {
    return height;
  }

  public Color getBackgroundColor()
  {
    return backgroundColor;
  }

  public void setBackgroundColor(final Color backgroundColor)
  {
    this.backgroundColor = backgroundColor;
  }

  /**
     * Needed during the layout computation. May be invalid afterwards, so do not rely on the validity of this value
     * outside the destTable-layout computation process.
     *
     * @return
     */
  public StrictBounds getBounds()
  {
    return new StrictBounds(x, y, width, height);
  }

  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Clone not supported? I'm confused!");
    }
  }

  protected boolean isSameBounds (final StrictBounds bounds)
  {
    return (bounds.getX() == x && bounds.getY() == y && bounds.getWidth() == width && bounds.getHeight() == height);
  }

  public TableCellDefinition normalize(final StrictBounds bounds)
  {

    final long orgX2 = getX() + getWidth();
    final long orgY2 = getY() + getHeight();
    if ((bounds.getX() > orgX2 || bounds.getY() > orgY2))
    {
      // this cell is out of bounds ..
      return null;
    }


    final TableCellDefinition bg = (TableCellDefinition) clone();
    if (isSameBounds(bounds))
    {
      return bg;
    }

    bg.y = bounds.getY();
    bg.x = bounds.getX();
    bg.width = bounds.getWidth();
    bg.height = bounds.getHeight();
    final boolean leftBorderMatch = (getX() == bounds.getX());
    final boolean topBorderMatch = (getY() == bounds.getY());

//    if (bounds.getX() < getX() || bounds.getY() < getY() ||
//        (bounds.getX() + bounds.getWidth()) > (getX() + getWidth()) ||
//        (bounds.getY() + bounds.getHeight()) > (getY() + getHeight()))
//    {
//      bg.backgroundColor = null;
//    }
    if (topBorderMatch == false || leftBorderMatch == false)
    {
      bg.anchor = null;
    }

    if (topBorderMatch == false)
    {
      bg.setTop(BorderEdge.EMPTY);
    }
    if (leftBorderMatch == false)
    {
      bg.setLeft(BorderEdge.EMPTY);
    }
    final boolean rightBorderMatch = (bg.getX() + bg.getWidth()) == orgX2;
    final boolean bottomBorderMatch = (bg.getY() + bg.getHeight()) == orgY2;
    if (rightBorderMatch == false)
    {
      bg.setRight(BorderEdge.EMPTY);
    }
    if (bottomBorderMatch == false)
    {
      bg.setBottom(BorderEdge.EMPTY);
    }

    if (topBorderMatch == false || leftBorderMatch == false)
    {
      // top-left corner is not ok.
      bg.setTopLeft(BorderCorner.EMPTY);
    }
    if (topBorderMatch == false || rightBorderMatch == false)
    {
      bg.setTopRight(BorderCorner.EMPTY);
    }

    if (bottomBorderMatch == false || leftBorderMatch == false)
    {
      // bottom-left corner is not ok.
      bg.setBottomLeft(BorderCorner.EMPTY);
    }
    if (bottomBorderMatch == false || rightBorderMatch == false)
    {
      // bottom-left corner is not ok.
      bg.setBottomRight(BorderCorner.EMPTY);
    }
    return bg;
  }

  public TableCellDefinition merge(final TableCellDefinition background, final StrictBounds cellBounds)
  {
    // merge the borders: CellBounds are most likely totally wrong.
    // we correct that here.
    final TableCellDefinition merged = background.normalize(cellBounds);
    if (merged == null)
    {
      return null;
    }

    // If the given cell does not fully overlap this cell or
    // the this cell does not fully overlap the given cell
    // then we preserve the other's color nothing ...
    final Color orgColor = getBackgroundColor();
    final boolean thisIsALine = (getHeight() == 0 || getWidth() == 0);
    final boolean otherIsALine = (background.getHeight() == 0 || background.getWidth() == 0);
    if (otherIsALine == false && thisIsALine == false)
    {
      // create the unified color for both backgrounds ..
      if (orgColor == null)
      {
        merged.setBackgroundColor(background.getBackgroundColor());
      }
      else if (background.getBackgroundColor() != null)
      {
        final Color color = addColor(orgColor, background.getBackgroundColor());
        merged.setBackgroundColor(color);
      }
      else
      {
        merged.setBackgroundColor(orgColor);
        // do nothing ..
      }
    }
    else if (thisIsALine == false)
    {
      merged.setBackgroundColor(orgColor);
      merged.setName(merged.getName());
      if (merged.getMergeHistory() == null)
      {
        merged.mergeHistory = merged.getName() + " < " + getName();
      }
      else
      {
        merged.mergeHistory += " < " + getName();
      }
    }
    else
    {
      // do nothing ..
    }

    final boolean topEdgeMatch = getY() == cellBounds.getY();
    final boolean leftEdgeMatch = getX() == cellBounds.getX();
    final boolean bottomEdgeMatch = (getY() + getHeight()) == (cellBounds.getY() + cellBounds.getHeight());
    final boolean rightEdgeMatch = (getX() + getWidth()) == (cellBounds.getX() + cellBounds.getWidth());

    // copy the borders of the other background to the merged instance
    if (BorderEdge.EMPTY.equals(merged.getTop()))
    {
      // the thing does not have a border ..
      if (topEdgeMatch)
      {
        merged.setTop(getTop());
      }
    }
    if (BorderEdge.EMPTY.equals(merged.getLeft()))
    {
      // the thing does not have a border ..
      if (leftEdgeMatch)
      {
        merged.setLeft(getLeft());
      }
    }
    if (BorderEdge.EMPTY.equals(merged.getBottom()))
    {
      // the thing does not have a border ..
      if (bottomEdgeMatch)
      {
        merged.setBottom(getBottom());
      }
    }
    if (BorderEdge.EMPTY.equals(merged.getRight()))
    {
      // the thing does not have a border ..
      if (rightEdgeMatch)
      {
        merged.setRight(getRight());
      }
    }
    if (BorderCorner.EMPTY.equals(merged.getTopLeft()))
    {
      if (topEdgeMatch && leftEdgeMatch)
      {
        merged.setTopLeft(getTopLeft());
      }
    }
    if (BorderCorner.EMPTY.equals(merged.getTopRight()))
    {
      if (topEdgeMatch && rightEdgeMatch)
      {
        merged.setTopRight(getTopRight());
      }
    }
    if (BorderCorner.EMPTY.equals(merged.getBottomLeft()))
    {
      if (bottomEdgeMatch && leftEdgeMatch)
      {
        merged.setBottomLeft(getBottomLeft());
      }
    }
    if (BorderCorner.EMPTY.equals(merged.getBottomRight()))
    {
      if (bottomEdgeMatch && rightEdgeMatch)
      {
        merged.setBottomRight(getBottomRight());
      }
    }
    return merged;
  }

  private void setName(final String name)
  {
    this.name = name;
  }


  /**
   * Adds two colors, the result is the mixed color of the base color and the paint color.
   *
   * @param base  the base color
   * @param paint the overlay color
   * @return the merged colors.
   */
  private static Color addColor(final Color base, final Color paint)
  {
    if (paint.getAlpha() == 255)
    {
      return paint;
    }
    if (paint.getAlpha() == 0)
    {
      return base;
    }

    final double baseAlpha = (base.getAlpha() / 255.0);
    final double paintAlpha = (paint.getAlpha() / 255.0);
    final double effectiveAlpha = 1.0 - baseAlpha * paintAlpha;

    final double deltaAlpha = 1.0 - effectiveAlpha;
    final int red = (int)
        (base.getRed() * deltaAlpha + paint.getRed() * effectiveAlpha);
    final int green = (int)
        (base.getGreen() * deltaAlpha + paint.getGreen() * effectiveAlpha);
    final int blue = (int)
        (base.getBlue() * deltaAlpha + paint.getBlue() * effectiveAlpha);
    return new Color(red, green, blue, (float) (effectiveAlpha * 255.0));
  }

  public ElementAlignment getVerticalAlignment()
  {
    return verticalAlignment;
  }

  public String toString()
  {
    return "TableCellDefinition{" +
        "name='" + name + '\'' +
        ", top=" + top +
        ", left=" + left +
        ", bottom=" + bottom +
        ", right=" + right +
        ", topLeft=" + topLeft +
        ", topRight=" + topRight +
        ", bottomLeft=" + bottomLeft +
        ", bottomRight=" + bottomRight +
        ", x=" + x +
        ", y=" + y +
        ", width=" + width +
        ", height=" + height +
        ", backgroundColor=" + backgroundColor +
        ", verticalAlignment=" + verticalAlignment +
        '}';
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final TableCellDefinition that = (TableCellDefinition) o;

    if (height != that.height)
    {
      return false;
    }
    if (width != that.width)
    {
      return false;
    }
    if (x != that.x)
    {
      return false;
    }
    if (y != that.y)
    {
      return false;
    }
    if (ObjectUtilities.equal(backgroundColor, that.backgroundColor) == false)
    {
      return false;
    }
    if (!bottom.equals(that.bottom))
    {
      return false;
    }
    if (!bottomLeft.equals(that.bottomLeft))
    {
      return false;
    }
    if (!bottomRight.equals(that.bottomRight))
    {
      return false;
    }
    if (!left.equals(that.left))
    {
      return false;
    }
    if (!right.equals(that.right))
    {
      return false;
    }
    if (!top.equals(that.top))
    {
      return false;
    }
    if (!topLeft.equals(that.topLeft))
    {
      return false;
    }
    if (!topRight.equals(that.topRight))
    {
      return false;
    }
    if (!verticalAlignment.equals(that.verticalAlignment))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = top.hashCode();
    result = 29 * result + left.hashCode();
    result = 29 * result + bottom.hashCode();
    result = 29 * result + right.hashCode();
    result = 29 * result + topLeft.hashCode();
    result = 29 * result + topRight.hashCode();
    result = 29 * result + bottomLeft.hashCode();
    result = 29 * result + bottomRight.hashCode();
    result = 29 * result + (int) (x ^ (x >>> 32));
    result = 29 * result + (int) (y ^ (y >>> 32));
    result = 29 * result + (int) (width ^ (width >>> 32));
    result = 29 * result + (int) (height ^ (height >>> 32));
    if (backgroundColor != null)
    {
      result = 29 * result + backgroundColor.hashCode();
    }
    result = 29 * result + verticalAlignment.hashCode();
    return result;
  }

  public void setWidth(final long width)
  {
    this.width = width;
  }


}
