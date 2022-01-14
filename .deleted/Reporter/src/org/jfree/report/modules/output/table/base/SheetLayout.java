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
 * SheetLayout.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.base;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import java.util.ArrayList;

import org.jfree.report.layout.model.Border;
import org.jfree.report.layout.model.BorderCorner;
import org.jfree.report.layout.model.BorderEdge;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.process.ProcessUtility;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.InstanceID;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.util.Log;

/**
 * The sheet layout is used to build the background map and to collect the minX- and minY-cell-borders.
 */
public class SheetLayout
{
  /**
   * How backgrounds for cells get computed
   * --------------------------------------
   *
   * JFreeReport handles 4 background types:
   *
   * Bands
   * -----
   * Bands are no real backgrounds, as they do not influence the output,
   * but they will be used to simplify the computation.
   *
   * Rectangles
   * ----------
   * Define the cell background (fill = true) and all 4 borders of a cell
   * (draw == true).
   *
   * Horizontal & Vertical Lines
   * ----------------
   * These lines define the Top or Left border or a cell. Bottom and right
   * borders get mapped into the Top/Left borders of the next cells.
   *
   * Joining
   * -------
   * When a background element is added, JFreeReport first checks, whether
   * a background has been already defined for that cell position. If not,
   * the given background is used as is.
   *
   * If there is a background defined, a merge operation starts. JFreeReport
   * will try to join both background definitions.
   * (This is done while adding the TableCellBackground to the SheetLayout)
   *
   * New Elements overwrite old elements. That means if there are two
   * conflicting borders or backgrounds at a given position, any old border
   * or background will be replaced as soon as a more current value appears.
   *
   * Lines, which make up the bottom most or right most borders, are held in
   * a zero-width column or zero-height row. These columns are always there,
   * if there is at least one background reaching to right or bottom of the report.
   * A flag indicates, whether these cells are significant. (for validity this
   * flag should mirror the result of an test "All Cells in these Row/Column are
   * empty".
   *
   * These lines are not mapped into bottom cell lines, as the resulting
   * merge would not be predictable and would depend on the order of the
   * split operations. A predictable merge implementation would be by far
   * more complex than this 'hack'.
   *
   * To create a consistent behaviour, rectangle-borders behave like four lines;
   * therefore the bottom and right border of the rectangle will be mapped into
   * top or left border cells.
   */

  /**
   * An internal flag indicating that the upper or left bounds should be used.
   */
  private static final boolean UPPER_BOUNDS = true;
  private static final boolean LOWER_BOUNDS = false;

  private static class CellReference
  {
    private long x;
    private long y;
    private long width;
    private long height;
    private InstanceID contentID;
    private String node;

    public CellReference(final RenderNode node, final long shift)
    {
      this.node = node.toString();
      this.width = node.getWidth();
      this.y = node.getY() + shift;
      this.x = node.getX();
      this.height = node.getHeight();
      this.contentID = node.getInstanceId();
    }

    public long getX()
    {
      return x;
    }

    public long getY()
    {
      return y;
    }

    public long getWidth()
    {
      return width;
    }

    public long getHeight()
    {
      return height;
    }

    public InstanceID getContentID()
    {
      return contentID;
    }


    public String toString()
    {
      return "CellReference{" +
          "x=" + x +
          ", y=" + y +
          ", width=" + width +
          ", height=" + height +
          ", contentID=" + contentID +
          ", node='" + node + '\'' +
          '}';
    }
  }

  /**
   * A flag, defining whether to use strict layout mode.
   */
  private final boolean strict;

  /**
   * The XBounds, all vertical cell boundaries (as CoordinateMappings).
   */
  private final TableCutList xBounds;

  /**
   * The YBounds, all vertical cell boundaries (as CoordinateMappings).
   */
  private final TableCutList yBounds;

  /**
   * Is a list of lists, contains the merged backgrounds ...
   */
  private GenericObjectTable backend;
  /**
   * Contains the references to the original data passed into this layouter.
   */
  private GenericObjectTable objectIdTable;

  /**
   * The right border of the grid. This is needed when not being in the strict mode.
   */
  private long xMaxBounds;
  private long yMaxBounds;
  private transient StrictBounds workBounds;
  private boolean ellipseAsRectangle;
  private boolean verboseCellMarker;

  /**
     * Creates a new TableGrid-object. If strict mode is enabled, all cell bounds are used to create the destTable grid,
     * resulting in a more complex layout.
     *
     * @param strict the strict mode for the layout.
     */
  public SheetLayout(final boolean strict,
                     final boolean ellipseAsRectangle,
                     final boolean verboseCellMarker)
  {
    this.ellipseAsRectangle = ellipseAsRectangle;
    this.xBounds = new TableCutList(50);
    this.yBounds = new TableCutList(200);
    this.strict = strict;
    this.xMaxBounds = 0;
    this.yMaxBounds = 0;
    this.backend = new GenericObjectTable(200, 5);
    this.objectIdTable = new GenericObjectTable(200, 5);
    this.verboseCellMarker = verboseCellMarker;
    this.ensureXMapping(0, false);
    this.ensureYMapping(0, false);
  }

  private TableCellDefinition createBackground(final RenderBox box, final long shift)
  {
    final TableCellDefinition legacyDefinition = computeLegacyBackground(box, shift);
    if (legacyDefinition != null)
    {
      return legacyDefinition;
    }

    if (box.getBoxDefinition().getBorder().isEmpty() == false)
    {
      return new TableCellDefinition(box, shift);
    }

    final StyleSheet styleSheet = box.getStyleSheet();
    if (styleSheet.getStyleProperty(ElementStyleKeys.BACKGROUND_COLOR) != null)
    {
      return new TableCellDefinition(box, shift);
    }

    if (styleSheet.getStyleProperty(ElementStyleKeys.ANCHOR_NAME) != null)
    {
      return new TableCellDefinition(box, shift);
    }
    return null;
  }


  private TableCellDefinition computeLegacyBackground(final RenderBox box, final long shift)
  {

    // For legacy reasons: A single ReplacedContent in a canvas means, we may have a old-style border and
    // background definition.
    if (box instanceof CanvasRenderBox == false)
    {
      return null;
    }

    final RenderNode firstChild = box.getFirstChild();
    if (firstChild != box.getLastChild())
    {
      return null;
    }

    if (firstChild instanceof RenderableReplacedContent == false)
    {
      return null;
    }

    final StyleSheet styleSheet = box.getStyleSheet();
    final RenderableReplacedContent rpc = (RenderableReplacedContent) firstChild;
    final Object rawObject = rpc.getRawObject();

    final boolean draw = styleSheet.getBooleanStyleProperty(ElementStyleKeys.DRAW_SHAPE);
    if (rawObject instanceof Line2D && draw)
    {
      final TableCellDefinition tableCellDefinition = new TableCellDefinition(box, shift);
      tableCellDefinition.setLineHint((Line2D) rawObject);
      return tableCellDefinition;
    }

    if (rawObject instanceof Rectangle2D ||
        (ellipseAsRectangle && rawObject instanceof Ellipse2D))
    {
      final TableCellDefinition tableCellDefinition = new TableCellDefinition(box, shift);
      if (draw)
      {
        // the beast has a border ..
        final BorderEdge edge = ProcessUtility.produceBorderEdge(box.getStyleSheet());
        if (edge != null)
        {
          tableCellDefinition.setTop(edge);
          tableCellDefinition.setLeft(edge);
          tableCellDefinition.setBottom(edge);
          tableCellDefinition.setRight(edge);
        }
      }
      if (styleSheet.getBooleanStyleProperty(ElementStyleKeys.FILL_SHAPE))
      {
        tableCellDefinition.setBackgroundColor((Color) styleSheet.getStyleProperty(ElementStyleKeys.PAINT));
      }
      return tableCellDefinition;
    }

    if (rawObject instanceof RoundRectangle2D)
    {
      final TableCellDefinition tableCellDefinition = new TableCellDefinition(box, shift);
      if (draw)
      {
        // the beast has a border ..
        final BorderEdge edge = ProcessUtility.produceBorderEdge(box.getStyleSheet());
        if (edge != null)
        {
          tableCellDefinition.setTop(edge);
          tableCellDefinition.setLeft(edge);
          tableCellDefinition.setBottom(edge);
          tableCellDefinition.setRight(edge);
        }
      }
      if (styleSheet.getBooleanStyleProperty(ElementStyleKeys.FILL_SHAPE))
      {
        tableCellDefinition.setBackgroundColor((Color) styleSheet.getStyleProperty(ElementStyleKeys.PAINT));
      }
      final RoundRectangle2D rr = (RoundRectangle2D) rawObject;
      final long arcHeight = StrictGeomUtility.toInternalValue(rr.getArcHeight());
      final long arcWidth = StrictGeomUtility.toInternalValue(rr.getArcWidth());
      if (arcHeight > 0 && arcWidth > 0)
      {
        final BorderCorner bc = new BorderCorner(arcWidth, arcHeight);
        tableCellDefinition.setTopLeft(bc);
        tableCellDefinition.setBottomLeft(bc);
        tableCellDefinition.setTopRight(bc);
        tableCellDefinition.setBottomRight(bc);
      }
      return tableCellDefinition;
    }

    return null;
  }


  /**
   * Adds the bounds of the given TableCellData to the grid. The bounds given must be the same as the bounds of the
   * element, or the layouting might produce surprising results.
   * <p/>
   * This method will do nothing, if the element has a width and height of zero and does not define any anchors.
   *
   * @param element the position that should be added to the grid (might be null).
   * @param shift   the vertical shift which adjusts the visual position of the content.
   * @throws NullPointerException if the bounds are null
   */
  public void add(final RenderBox element, final long shift)
  {
    final long shiftedY = element.getY() + shift;
    final long elementY;
    if (shiftedY < 0)
    {
      if ((shiftedY + element.getHeight()) < 0)
      {
        // The box will not be visible at all. (Should not happen in a sane environment ..)
        Log.debug("THIS BOX WILL BE INVISIBLE: " + element);
        return;
      }

      elementY = 0;
    }
    else
    {
      elementY = shiftedY;
    }

    final TableCellDefinition background = createBackground(element, shift);
    if (addLine(element, background, shift, elementY, shiftedY))
    {
      return;
    }

    final long elementX = element.getX();
    final long elementRightX = (element.getWidth() + elementX);
    final long elementBottomY = element.getHeight() + shiftedY;

    // collect the bounds and add them to the xBounds and yBounds collection
    // if necessary...
    ensureXMapping(elementX, false);
    ensureYMapping(elementY, false);

    // an end cut is auxilary, if it is not a background and the layout is not strict
    final boolean aux = (background == null) && (isStrict() == false);
    ensureXMapping(elementRightX, aux);
    ensureYMapping(elementBottomY, aux);

    // update the collected maximums
    if (xMaxBounds < elementRightX)
    {
      xMaxBounds = elementRightX;
    }
    if (yMaxBounds < elementBottomY)
    {
      yMaxBounds = elementBottomY;
    }

    // now add the new element to the table ...
    // the +1 makes sure, that we include the right and bottom element borders in the set
    final int lowerXIndex = xBounds.findKeyPosition(elementX, SheetLayout.LOWER_BOUNDS);
    final int upperXIndex = xBounds.findKeyPosition(elementRightX, SheetLayout.UPPER_BOUNDS);

    final int lowerYIndex = yBounds.findKeyPosition(elementY, SheetLayout.LOWER_BOUNDS);
    final int upperYIndex = yBounds.findKeyPosition(elementBottomY, SheetLayout.UPPER_BOUNDS);


    if (background != null)
    {
      // this does nothing for yLength == 1 && xLength == 1
      // in that case, the whole thing did not define an area but a
      // vertical or horizontal line.
      processAreaBackground(lowerXIndex, lowerYIndex, upperXIndex, upperYIndex, background);
    }

    if (ProcessUtility.isContent(element, false, ellipseAsRectangle, false) == false)
    {
      return;
    }


    final boolean hasVerticalPaddings;
    final BoxDefinition sblp = element.getBoxDefinition();
    if (sblp.getPaddingTop() != 0 || sblp.getPaddingBottom() != 0)
    {
      final long coordinate = elementBottomY - sblp.getPaddingBottom();
      if (coordinate > 0)
      {
        ensureYMapping(coordinate, false);
        if (shiftedY >= 0)
        {
          ensureYMapping(elementY + sblp.getPaddingTop(), false);
        }
        hasVerticalPaddings = true;
      }
      else
      {
        hasVerticalPaddings = false;
      }
    }
    else
    {
      hasVerticalPaddings = false;
    }

    final boolean hasHorizontalPaddings;
    if (sblp.getPaddingLeft() != 0 || sblp.getPaddingRight() != 0)
    {
      // check if the element is a page-spanning element. No top-paddings apply in that case..
      ensureXMapping(elementX + sblp.getPaddingLeft(), false);
      ensureXMapping(elementRightX - sblp.getPaddingRight(), false);
      hasHorizontalPaddings = true;
    }
    else
    {
      hasHorizontalPaddings = false;
    }

    if (hasHorizontalPaddings == false && hasVerticalPaddings == false)
    {
      // now, elements can be both content and background.
      // mark cells as occupied ..
      if (isCellAreaOccupied(lowerXIndex, lowerYIndex, upperXIndex, upperYIndex, element.getName()) == false)
      {
        final Object cellReference;
        if (verboseCellMarker)
        {
          cellReference = new SheetLayout.CellReference(element, shift);
        }
        else
        {
          cellReference = element.getInstanceId();
        }

        final int maxX = Math.max(lowerXIndex + 1, upperXIndex);
        final int maxY = Math.max(lowerYIndex + 1, upperYIndex);
        for (int y = lowerYIndex; y < maxY; y++)
        {
          // get the index of the current row in the backend-table ...
          final TableCutList.CutEntry currentRowValue = yBounds.getValueAt(y);
          final int currentRowIndex = currentRowValue.getPosition();

          // for every row we iterate over all columns ...
          // but we do not touch the last column ..
          for (int x = lowerXIndex; x < maxX; x++)
          {
            // again get the column index for the backend table ...
            final TableCutList.CutEntry currentColumnValue = xBounds.getValueAt(x);
            final int currentColumnIndex = currentColumnValue.getPosition();

            objectIdTable.setObject(currentRowIndex, currentColumnIndex, cellReference);
          }
        }
      }
    }
    else
    {
      final int lowerXPadIndex = xBounds.findKeyPosition(elementX + sblp.getPaddingLeft(), SheetLayout.LOWER_BOUNDS);
      final int upperXPadIndex = xBounds.findKeyPosition(elementRightX + sblp.getPaddingRight(), SheetLayout.UPPER_BOUNDS);

      final int lowerYPadIndex = yBounds.findKeyPosition(elementY + sblp.getPaddingTop(), SheetLayout.LOWER_BOUNDS);
      final int upperYPadIndex = yBounds.findKeyPosition(elementBottomY - sblp.getPaddingBottom(), SheetLayout.UPPER_BOUNDS);

      if (isCellAreaOccupied(lowerXPadIndex, lowerYPadIndex, upperXPadIndex, upperYPadIndex, element.getName()) == false)
      {
        final Object cellReference;
        if (verboseCellMarker)
        {
          cellReference = new SheetLayout.CellReference(element, shift);
        }
        else
        {
          cellReference = element.getInstanceId();
        }

        final int maxX = Math.max(lowerXPadIndex + 1, upperXPadIndex);
        final int maxY = Math.max(lowerYPadIndex + 1, upperYPadIndex);
        for (int y = lowerYPadIndex; y < maxY; y++)
        {
          // get the index of the current row in the backend-table ...
          final TableCutList.CutEntry currentRowValue = yBounds.getValueAt(y);
          final int currentRowIndex = currentRowValue.getPosition();

          // for every row we iterate over all columns ...
          // but we do not touch the last column ..
          for (int x = lowerXPadIndex; x < maxX; x++)
          {
            // again get the column index for the backend table ...
            final TableCutList.CutEntry currentColumnValue = xBounds.getValueAt(x);
            final int currentColumnIndex = currentColumnValue.getPosition();

            objectIdTable.setObject(currentRowIndex, currentColumnIndex, cellReference);
          }
        }
      }
    }
  }

  private boolean addLine(final RenderBox element,
                          TableCellDefinition background,
                          final long shift,
                          final long elementY,
                          final long shiftedY)
  {
    // This method handles several special cases. If the element is a non-area box with borderss,
    // it mapps the borders into a equivalent line-definition.
    final long width = element.getWidth();
    final long height = element.getHeight();
    if (width == 0 && height == 0)
    {
      if (background != null)
      {
        background.setLineHint(null);
        if (background.getAnchor() != null)
        {
          // Elements that define anchors are an exception. We add it ..
          return false;
        }
      }
      // this element will be invisible. We do not add it ..
      return true;
    }

    // line definitions are treated like boxes that span from zero to the position of the
    // line.
    boolean lineVertical = false;
    long linePosition = 0;
    BorderEdge significantEdge = BorderEdge.EMPTY;

    final Border border = element.getBoxDefinition().getBorder();
    if (width == 0)
    {
      if (BorderEdge.EMPTY.equals(border.getLeft()) == false)
      {
        significantEdge = border.getLeft();
        lineVertical = true;
        linePosition = element.getX();
      }
      else if (BorderEdge.EMPTY.equals(border.getRight()) == false)
      {
        significantEdge = border.getRight();
        lineVertical = true;
        linePosition = element.getX() + element.getWidth();
      }
    }

    boolean lineHorizontal = false;
    if (height == 0)
    {
      if (BorderEdge.EMPTY.equals(border.getTop()) == false)
      {
        significantEdge = border.getTop();
        lineHorizontal = true;
        linePosition = shiftedY;
      }
      else if (BorderEdge.EMPTY.equals(border.getBottom()) == false)
      {
        significantEdge = border.getBottom();
        lineHorizontal = true;
        linePosition = shiftedY + element.getHeight();
      }
    }
    if (background != null)
    {
      final Line2D lineHint = background.getLineHint();
      if (lineHint != null)
      {
        final BorderEdge edge = ProcessUtility.produceBorderEdge(element.getStyleSheet());
        if (BorderEdge.EMPTY.equals(edge) == false)
        {
          if (height > 0 && lineHint.getX1() == lineHint.getX2())
          {
            lineVertical = true;
            if (lineHint.getX1() == 0)
            {
              linePosition = element.getX();
            }
            else
            {
              linePosition = element.getX() + element.getWidth();
            }
          }
          if (width > 0 && lineHint.getY1() == lineHint.getY2())
          {
            lineHorizontal = true;
            if (lineHint.getY1() == 0)
            {
              linePosition = shiftedY;
            }
            else
            {
              linePosition = shiftedY + element.getHeight();
            }
          }
          significantEdge = edge;
        }
      }
    }

    if ((lineHorizontal && lineVertical) ||
        (lineHorizontal == false && lineVertical == false) ||
        linePosition < 0 ||
        BorderEdge.EMPTY.equals(significantEdge))
    {
      // an invalid definition. it will be ignored ...
      if (background != null && background.getLineHint() != null)
      {
        // this is a line, not a content element, and the line itself is invalid. Ignore it
        background.setLineHint(null);
        if (background.getAnchor() != null)
        {
          // Elements that define anchors are an exception. We add it ..
          return false;
        }
        return true;
      }
      return false;
    }

    if (background == null)
    {
      background = new TableCellDefinition(element, shift);
    }

    final long elementX = element.getX();
    final long elementRightX = (element.getWidth() + elementX);
    final long elementBottomY = element.getHeight() + shiftedY;

    final int lowerXIndex;
    final int upperXIndex;
    final int lowerYIndex;
    final int upperYIndex;

    // Beginn the mapping ..
    if (lineHorizontal)
    {
      ensureXMapping(elementX, false);
      ensureXMapping(elementRightX, false);
      lowerXIndex = xBounds.findKeyPosition(elementX, SheetLayout.LOWER_BOUNDS);
      upperXIndex = xBounds.findKeyPosition(elementRightX, SheetLayout.UPPER_BOUNDS);

      if (linePosition == 0)
      {
        ensureYMapping(elementY, false);
        lowerYIndex = yBounds.findKeyPosition(shiftedY, SheetLayout.LOWER_BOUNDS);
        upperYIndex = yBounds.findKeyPosition(elementBottomY + 1, SheetLayout.UPPER_BOUNDS);
        background.setTop(significantEdge);
      }
      else
      {
        ensureYMapping(elementBottomY, false);
        lowerYIndex = yBounds.findKeyPosition(shiftedY - 1, SheetLayout.LOWER_BOUNDS);
        upperYIndex = yBounds.findKeyPosition(elementBottomY, SheetLayout.UPPER_BOUNDS);
        background.setBottom(significantEdge);
      }
    }
    else // if (lineVertical)
    {
      ensureYMapping(elementY, false);
      ensureYMapping(elementBottomY, false);
      lowerYIndex = yBounds.findKeyPosition(shiftedY, SheetLayout.LOWER_BOUNDS);
      upperYIndex = yBounds.findKeyPosition(elementBottomY, SheetLayout.UPPER_BOUNDS);

      if (linePosition == 0)
      {
        ensureXMapping(elementX, false);
        lowerXIndex = xBounds.findKeyPosition(elementX, SheetLayout.LOWER_BOUNDS);
        upperXIndex = xBounds.findKeyPosition(elementX + 1, SheetLayout.UPPER_BOUNDS);
        background.setLeft(significantEdge);
      }
      else
      {
        ensureXMapping(elementRightX, false);
        lowerXIndex = xBounds.findKeyPosition(elementX - 1, SheetLayout.LOWER_BOUNDS);
        upperXIndex = xBounds.findKeyPosition(elementX, SheetLayout.UPPER_BOUNDS);
        background.setRight(significantEdge);
      }
    }

    // update the collected maximums
    if (xMaxBounds < elementRightX)
    {
      xMaxBounds = elementRightX;
    }
    if (yMaxBounds < elementBottomY)
    {
      yMaxBounds = elementBottomY;
    }

    processAreaBackground(lowerXIndex, lowerYIndex, upperXIndex, upperYIndex, background);
    background.setLineHint(null);
    return true;
  }

  private boolean isCellAreaOccupied(final int lowerXIndex, final int lowerYIndex,
                                     final int upperXIndex, final int upperYIndex,
                                     final String newContent)
  {
    if (lowerXIndex == upperXIndex || lowerYIndex == upperYIndex)
    {
      // not an area object, and therefore not valid ..
      return false;
    }

    final int maxX = Math.max(lowerXIndex + 1, upperXIndex);
    final int maxY = Math.max(lowerYIndex + 1, upperYIndex);
    for (int y = lowerYIndex; y < maxY; y++)
    {
      // get the index of the current row in the backend-table ...
      final TableCutList.CutEntry currentRowValue = yBounds.getValueAt(y);
      final int currentRowIndex = currentRowValue.getPosition();

      // for every row we iterate over all columns ...
      // but we do not touch the last column ..
      for (int x = lowerXIndex; x < maxX; x++)
      {
        // again get the column index for the backend table ...
        final TableCutList.CutEntry currentColumnValue = xBounds.getValueAt(x);
        final int currentColumnIndex = currentColumnValue.getPosition();

        final Object o = objectIdTable.getObject(currentRowIndex, currentColumnIndex);
        if (o != null)
        {
          Log.warn("Overlapping elements detected. Cell at (" +
              currentColumnIndex + ", " + currentRowIndex + ") is occupied by " + o +
              " but content from element " + newContent + " tried to use the same space.");
          return true;
        }
      }
    }
    return false;
  }

  private void processAreaBackground(final int lowerXIndex, final int lowerYIndex,
                                     final int upperXIndex, final int upperYIndex,
                                     final TableCellDefinition background)
  {
//    final boolean hasRightBorders = (BorderEdge.EMPTY.equals(background.getRight()) == false);
//    final boolean hasBottomBorders = (BorderEdge.EMPTY.equals(background.getBottom()) == false);

    final int maxX = Math.max(lowerXIndex + 1, upperXIndex);
    final int maxY = Math.max(lowerYIndex + 1, upperYIndex);
    for (int y = lowerYIndex; y < maxY; y++)
    {
      // get the index of the current row in the backend-table ...
      final TableCutList.CutEntry currentRowValue = yBounds.getValueAt(y);
      final int currentRowIndex = currentRowValue.getPosition();

      // for every row we iterate over all columns ...
      // but we do not touch the last column ..
      for (int x = lowerXIndex; x < maxX; x++)
      {
        // again get the column index for the backend table ...
        final TableCutList.CutEntry currentColumnValue = xBounds.getValueAt(x);
        final int currentColumnIndex = currentColumnValue.getPosition();

        workBounds = computeCellBounds(workBounds, currentColumnValue.getCoordinate(), currentRowValue.getCoordinate());
        performMergeCellBackground(currentRowIndex, currentColumnIndex, background, workBounds);
      }
    }

//    if (hasRightBorders && (xBounds.getKeyAt(upperXIndex) == xMaxBounds))
//    {
//      lastColumnCutIsSignificant = true;
//    }
//    if (hasBottomBorders && (yBounds.getKeyAt(upperYIndex) == yMaxBounds))
//    {
//      lastRowCutIsSignificant = true;
//    }
//
  }

  /**
     * This method computes the cell bounds for a cell on a given gid position. If the retval parameter is non-null, the
     * computed cell bounds will be copied into the given object to avoid unnecessary object creation.
     *
     * @param retval the bounds, to which the computed result should be copied, or null, if a new object should be
     * returned.
     * @param xVal the minX coordinates within the grid
     * @param yVal the minY coordinates within the grid
     * @return the computed cell bounds.
     */
  private StrictBounds computeCellBounds(final StrictBounds retval,
                                         final long xVal, final long yVal)
  {
    final long x2Val = xBounds.findKey(xVal + 1, SheetLayout.UPPER_BOUNDS);
    final long y2Val = yBounds.findKey(yVal + 1, SheetLayout.UPPER_BOUNDS);
    if (retval == null)
    {
      return new StrictBounds(xVal, yVal, x2Val - xVal, y2Val - yVal);
    }
    retval.setRect(xVal, yVal, x2Val - xVal, y2Val - yVal);
    return retval;
  }


  private void performMergeCellBackground(final int currentRowIndex,
                                          final int currentColumnIndex,
                                          final TableCellDefinition background,
                                          final StrictBounds bounds)
  {
    // get the old background ... we will merge this one with the new ..
    final TableCellDefinition oldBackground =
        (TableCellDefinition) backend.getObject(currentRowIndex,
            currentColumnIndex);
    final TableCellDefinition newBackground;
    if (oldBackground == null)
    {
      // split the element ..
      newBackground = background.normalize(bounds);
    }
    else
    {
      newBackground = oldBackground.merge(background, bounds);
    }
    backend.setObject(currentRowIndex, currentColumnIndex, newBackground);
  }

  private void ensureXMapping(final long coordinate, final boolean aux)
  {
    final TableCutList.CutEntry cut = xBounds.get(coordinate);
    if (cut == null)
    {
      final int result = xBounds.size();
      xBounds.put(coordinate, new TableCutList.CutEntry(coordinate, result, aux));

      // backend copy ...
      final int oldColumn = getPreviousColumnPosition(coordinate);
      if (coordinate < xMaxBounds)
      {
        columnInserted(coordinate, oldColumn, result);
      }
    }
    else if (cut.isAuxilary() && aux == false)
    {
      cut.makePermanent();
    }
  }

  /**
     * Splits the background destColumn into two new columns.
     *
     * @param coordinate
     * @param oldColumn
     * @param newColumn
     */
  protected void columnInserted(final long coordinate, final int oldColumn,
                                final int newColumn)
  {
//    Log.debug("Inserting new column on position " + coordinate +
//            " (Col: " + oldColumn + " -> " + newColumn);
//
    // now copy all entries from old column to new column
    backend.copyColumn(oldColumn, newColumn);
    objectIdTable.copyColumn(oldColumn, newColumn);

    // handle the backgrounds ..
    StrictBounds rightBounds = null;
    final TableCutList.CutEntry[] entries = yBounds.getRawEntries();
    final int size = yBounds.size();
    for (int i = 0; i < size; i++)
    {
      final TableCutList.CutEntry bcut = entries[i];

      final int position = bcut.getPosition();
      final TableCellDefinition originalBackground = (TableCellDefinition) backend.getObject(position, newColumn);
      if (originalBackground == null)
      {
        continue;
      }

      // a column has been inserted. We have to check, whether the background has
      // borders defined, which might be invalid now.
      rightBounds = computeCellBounds(rightBounds, coordinate, bcut.getCoordinate());

      // the bounds of the old background have to be adjusted too ..
      final StrictBounds leftBounds = originalBackground.getBounds();
      final long parentNewWidth = rightBounds.getX() - leftBounds.getX();
      leftBounds.setRect(leftBounds.getX(), leftBounds.getY(), Math.max(0, parentNewWidth), leftBounds.getHeight());
      // the original cell was split into two new cells ...
      // the new right border is no longer filled ...
      // a border was found, but is invalid now.

      final TableCellDefinition rightBackground = originalBackground.normalize(rightBounds);
      final TableCellDefinition leftBackground = originalBackground.normalize(leftBounds);

      backend.setObject(position, oldColumn, leftBackground);
      backend.setObject(position, newColumn, rightBackground);
    }
  }

  private int getPreviousColumnPosition(final long coordinate)
  {
    final TableCutList.CutEntry entry = xBounds.getPrevious(coordinate);
    if (entry == null)
    {
      return -1;
    }
    return entry.getPosition();
  }

  protected void rowInserted(final long coordinate,
                             final int oldRow,
                             final int newRow)
  {
    if (oldRow < 0)
    {
      throw new IndexOutOfBoundsException("OldRow cannot be negative: " + coordinate);
    }
    if (newRow < 0)
    {
      throw new IndexOutOfBoundsException("NewRow cannot be negative: " + coordinate);
    }
//    Log.debug("Inserting new row on position " + coordinate +
//            " (Row: " + oldRow + " -> " + newRow);

    // now copy all entries from old column to new column
    backend.copyRow(oldRow, newRow);
    objectIdTable.copyRow(oldRow, newRow);

    // handle the backgrounds ..
    StrictBounds cellBounds = null;

    final TableCutList.CutEntry[] entries = xBounds.getRawEntries();
    final int xEntrySize = xBounds.size();
    for (int i = 0; i < xEntrySize; i++)
    {
      final TableCutList.CutEntry bcut = entries[i];

      final int position = bcut.getPosition();
      final TableCellDefinition originalBackground = (TableCellDefinition) backend.getObject(newRow, position);
      if (originalBackground == null)
      {
        continue;
      }

      // a row has been inserted. We have to check, whether the background has
      // borders defined, which might be invalid now.
      cellBounds = computeCellBounds(cellBounds, bcut.getCoordinate(), coordinate);

      // the bounds of the old background have to be adjusted too ..
      final StrictBounds bounds = originalBackground.getBounds();
      final long parentNewHeight = cellBounds.getY() - bounds.getY();
      bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth(), Math.max(0, parentNewHeight));
      // due to the merging it is possible, that the bottom border
      // is invalid now.
      // the Top-Border of the original background is not touched ...

      final TableCellDefinition bottomBackground = originalBackground.normalize(cellBounds);
      final TableCellDefinition topBackground = originalBackground.normalize(bounds);
      backend.setObject(oldRow, position, topBackground);
      backend.setObject(newRow, position, bottomBackground);
    }
  }

  private int getPreviousRowPosition(final long coordinate)
  {
    final TableCutList.CutEntry entry = yBounds.getPrevious(coordinate);
    if (entry == null)
    {
      return -1;
    }
//    Log.debug ("GetPreviousRow: " + entry.getCoordinate() + " (prev to " + coordinate + ")");
    if (entry.getCoordinate() >= coordinate)
    {
      throw new IllegalStateException
          ("GetPrevious returned an invalid result:" + entry.getCoordinate() + " (prev to " + coordinate + ')');
    }
    return entry.getPosition();
  }

  private void ensureYMapping(final long coordinate, final boolean aux)
  {
    final TableCutList.CutEntry cut = yBounds.get(coordinate);
    if (cut == null)
    {
      final int result = yBounds.size();
      yBounds.put(coordinate, new TableCutList.CutEntry(coordinate, result, aux));

      final int oldRow = getPreviousRowPosition(coordinate);
      if (coordinate < yMaxBounds)
      {
        // oh, an insert operation. Make sure that everyone updates its state.
        rowInserted(coordinate, oldRow, result);
      }
    }
    else if (cut.isAuxilary() && aux == false)
    {
      cut.makePermanent();
    }
  }

  /**
   * Gets the strict mode flag.
   *
   * @return true, if strict mode is enabled, false otherwise.
   */
  public boolean isStrict()
  {
    return strict;
  }

  protected GenericObjectTable getLayoutBackend()
  {
    return backend;
  }

  public boolean isEmpty()
  {
    return ((backend.getColumnCount() == 0) &&
        (backend.getRowCount() == 0) &&
        xMaxBounds == 0 &&
        yMaxBounds == 0);
  }

  /**
     * Returns the position of the given element within the destTable. The TableRectangle contains row and cell indices, no
     * layout coordinates.
     *
     * @param x the element bounds for which the destTable bounds should be found.
     * @param y the element bounds for which the destTable bounds should be found.
     * @param width the element bounds for which the destTable bounds should be found.
     * @param height the element bounds for which the destTable bounds should be found.
     * @param rect the returned rectangle or null, if a new instance should be created
     * @return the filled destTable rectangle.
     */
  public TableRectangle getTableBounds(final long x, final long y, final long width, final long height,
                                       TableRectangle rect)
  {
    if (rect == null)
    {
      rect = new TableRectangle();
    }
    final int x1 = xBounds.findKeyPosition(x, SheetLayout.LOWER_BOUNDS);
    final int y1 = yBounds.findKeyPosition(y, SheetLayout.LOWER_BOUNDS);
    final int x2 = xBounds.findKeyPosition(x + width, SheetLayout.UPPER_BOUNDS);
    final int y2 = yBounds.findKeyPosition(y + height, SheetLayout.UPPER_BOUNDS);
    rect.setRect(x1, y1, x2, y2);
    return rect;
  }

  /**
     * Returns the position of the given element within the destTable. The TableRectangle contains row and cell indices, no
     * layout coordinates.
     *
     * @param bounds the element bounds for which the destTable bounds should be found.
     * @param rect the returned rectangle or null, if a new instance should be created
     * @return the filled destTable rectangle.
     */
  public TableRectangle getTableBounds(final StrictBounds bounds,
                                       TableRectangle rect)
  {
    if (rect == null)
    {
      rect = new TableRectangle();
    }
    final int x1 = xBounds.findKeyPosition(bounds.getX(), SheetLayout.LOWER_BOUNDS);
    final int y1 = yBounds.findKeyPosition(bounds.getY(), SheetLayout.LOWER_BOUNDS);
    final int x2 = xBounds.findKeyPosition(bounds.getX() + bounds.getWidth(), SheetLayout.UPPER_BOUNDS);
    final int y2 = yBounds.findKeyPosition(bounds.getY() + bounds.getHeight(), SheetLayout.UPPER_BOUNDS);
    rect.setRect(x1, y1, x2, y2);
    return rect;
  }

  protected int mapColumn(final int xCutIndex)
  {
    final TableCutList.CutEntry boundsCut = xBounds.getValueAt(xCutIndex);
    if (boundsCut == null)
    {
      throw new IllegalStateException("There is no column at " + xCutIndex);
    }
    return boundsCut.getPosition();
  }

  protected int mapRow(final int yCutIndex)
  {
    final TableCutList.CutEntry boundsCut = yBounds.getValueAt(yCutIndex);
    if (boundsCut == null)
    {
      throw new IllegalStateException("There is no row at " + yCutIndex);
    }
    return boundsCut.getPosition();
  }

  /**
   * A Callback method to inform the sheet layout, that the current page is complete, and no more content will be
   * added.
   */
  public void pageCompleted()
  {
    removeAuxilaryBounds();
    clearObjectIdTable();
  }

  protected void removeAuxilaryBounds()
  {
    ensureXMapping(this.xMaxBounds, false);
    ensureYMapping(this.yMaxBounds, false);
    // Log.debug("Size: " + getRowCount() + ", " + getColumnCount());

    final ArrayList removedCuts = new ArrayList();
    final TableCutList.CutEntry[] xEntries = (TableCutList.CutEntry[]) xBounds.getRawEntries().clone();
    final int xEntrySize = xBounds.size();
    for (int i = xEntrySize - 1; i >= 0; i--)
    {
      final TableCutList.CutEntry cut = xEntries[i];
      if (cut.isAuxilary())
      {
        xBounds.remove(cut.getCoordinate());
        removedCuts.add(cut);
      }
    }

    // now join the cuts with their left neighbour ..
    for (int i = 0; i < removedCuts.size(); i++)
    {
      // the col-cut that will be removed/merged/whatever ...
      final TableCutList.CutEntry removedCut = (TableCutList.CutEntry) removedCuts.get(i);
      final int removedColPosition = removedCut.getPosition();
      // the col cut marking the cell that will receive the merged content.
      final TableCutList.CutEntry prevCut = xBounds.getPrevious(removedCut.getCoordinate());
      final int previousColPosition = prevCut.getPosition();

      for (int row = 0; row < getRowCount(); row++)
      {
        final int mappedRow = mapRow(row);
        final TableCellDefinition leftBg = (TableCellDefinition) backend.getObject(mappedRow, previousColPosition);
        final TableCellDefinition rightBg = (TableCellDefinition) backend.getObject(mappedRow, removedColPosition);
        if (leftBg == null && rightBg == null)
        {
          continue;
        }
        if (leftBg == null)
        {
          final long x = prevCut.getCoordinate();
          final long y = getYPosition(row);
          final StrictBounds bounds = computeCellBounds(null, x, y);
          final TableCellDefinition unionBg = rightBg.normalize(bounds);
          backend.setObject(mappedRow, previousColPosition, unionBg);
          backend.setObject(mappedRow, removedColPosition, null);
        }
        else if (rightBg == null)
        {
          final long x = prevCut.getCoordinate();
          final long y = getYPosition(row);
          final StrictBounds bounds = computeCellBounds(null, x, y);
          final TableCellDefinition unionBg = leftBg.normalize(bounds);
          backend.setObject(mappedRow, previousColPosition, unionBg);
          backend.setObject(mappedRow, removedColPosition, null);
        }
        else
        {
          // now join ..
          final StrictBounds leftBounds = leftBg.getBounds();
          final StrictBounds newBounds = rightBg.getBounds().createUnion(leftBounds);
          final TableCellDefinition unionBg = leftBg.normalize(newBounds);
          if (unionBg != null)
          {
            unionBg.setRight(rightBg.getRight());
          }

          backend.setObject(mappedRow, previousColPosition, unionBg);
          backend.setObject(mappedRow, removedColPosition, null);
        }
      }
    }
    removedCuts.clear();

    final TableCutList.CutEntry[] yEntries = (TableCutList.CutEntry[]) yBounds.getRawEntries().clone();
    final int ySize = yBounds.size();
    for (int i = 0; i < ySize; i++)
    {
      final TableCutList.CutEntry cut = yEntries[i];
      if (cut.isAuxilary())
      {
        removedCuts.add(cut);
      }
    }

    yBounds.removeAll(removedCuts);

    // now join the cuts with their top neighbour ..
    for (int i = 0; i < removedCuts.size(); i++)
    {
      // the row-cut that will be removed/merged/whatever ...
      final TableCutList.CutEntry removedCut = (TableCutList.CutEntry) removedCuts.get(i);
      final int rowPosition = removedCut.getPosition();
      // the row cut marking the cell that will receive the merged content.
      final TableCutList.CutEntry prevCut = yBounds.getPrevious(removedCut.getCoordinate());
      final int previousRowPosition = prevCut.getPosition();

      for (int col = 0; col < getColumnCount(); col++)
      {
        final int mappedColumn = mapColumn(col);
        final TableCellDefinition topBg =
            (TableCellDefinition) backend.getObject(previousRowPosition, mappedColumn);
        final TableCellDefinition bottomBg =
            (TableCellDefinition) backend.getObject(rowPosition, mappedColumn);
        if (topBg == null && bottomBg == null)
        {
          // do nothing ...
        }
        else if (topBg == null)
        {
          // the cut has been removed already, so that the coordinate given in the boundsCut is
          // now invalid. It would point to a different location now.
          // however, the x-positions are still valid.

          final long x = getXPosition(col);
          final long y = prevCut.getCoordinate();
          final StrictBounds bounds = computeCellBounds(null, x, y);
          final TableCellDefinition unionBg = bottomBg.normalize(bounds);
          backend.setObject(previousRowPosition, mappedColumn, unionBg);
          backend.setObject(rowPosition, mappedColumn, null);
        }
        else if (bottomBg == null)
        {
          final long x = getXPosition(col);
          final long y = prevCut.getCoordinate();
          final StrictBounds bounds = computeCellBounds(null, x, y);
          final TableCellDefinition unionBg = topBg.normalize(bounds);
          backend.setObject(previousRowPosition, mappedColumn, unionBg);
          backend.setObject(rowPosition, mappedColumn, null);
        }
        else
        {
          // now join ..
          final StrictBounds topBounds = topBg.getBounds();
          final StrictBounds newBounds = bottomBg.getBounds().createUnion(topBounds);
          final TableCellDefinition unionBg = topBg.normalize(newBounds);
          if (unionBg != null)
          {
            unionBg.setBottom(bottomBg.getBottom());
          }
          backend.setObject(previousRowPosition, mappedColumn, unionBg);
          backend.setObject(rowPosition, mappedColumn, null);
        }
      }
    }
  }

  protected void clearObjectIdTable()
  {
    objectIdTable.clear();
    objectIdTable.ensureCapacity(backend.getRowCount(), backend.getColumnCount());
  }

  /**
     * Returns the element at grid-position (minX,minY). This returns the cell background for a certain cell, or null, if there
     * is no background at that cell.
     *
     * @param row the row of the requested element
     * @param column the destColumn starting with zero.
     * @return the element at the specified position.
     */
  public TableCellDefinition getBackgroundAt(final int row, final int column)
  {
    final int mappedRow = mapRow(row);
    final int mappedColumn = mapColumn(column);
    return (TableCellDefinition) backend.getObject(mappedRow, mappedColumn);
  }

  /**
   * Computes the height of the given row.
   *
   * @param row the row, for which the height should be computed.
   * @return the height of the row.
   * @throws IndexOutOfBoundsException if the row is invalid.
   */
  public long getRowHeight(final int row)
  {
    final int rowCount = yBounds.size();
    if (row >= rowCount)
    {
      throw new IndexOutOfBoundsException
          ("Row " + row + " is invalid. Max valid row is " + (rowCount - 1));
    }

    final long bottomBorder;
    if ((row + 1) < rowCount)
    {
      bottomBorder = yBounds.getKeyAt(row + 1);
    }
    else
    {
      bottomBorder = yMaxBounds;
    }

    return bottomBorder - yBounds.getKeyAt(row);
  }

  public long getMaxHeight()
  {
    return yMaxBounds;
  }

  public long getMaxWidth()
  {
    return xMaxBounds;
  }

  public long getCellWidth(final int startCell)
  {
    return getCellWidth(startCell, startCell + 1);
  }

  /**
   * Computes the height of the given row.
   *
   * @param startCell the first cell in the range
   * @param endCell   the last cell included in the cell range
   * @return the height of the row.
   * @throws IndexOutOfBoundsException if the row is invalid.
   */
  public long getCellWidth(final int startCell, final int endCell)
  {
    if (startCell < 0)
    {
      throw new IndexOutOfBoundsException("Start-Cell must not be negative");
    }
    if (endCell < 0)
    {
      throw new IndexOutOfBoundsException("End-Cell must not be negative");
    }
    if (endCell < startCell)
    {
      throw new IndexOutOfBoundsException("End-Cell must not smaller than end-cell");
    }

    final long rightBorder;
    if (endCell >= xBounds.size())
    {
      rightBorder = xMaxBounds;
    }
    else
    {
      rightBorder = xBounds.getKeyAt(endCell);
    }
    return rightBorder - xBounds.getKeyAt(startCell);
  }

  public long getRowHeight (final int startRow, final int endRow)
  {
    if (startRow < 0)
    {
      throw new IndexOutOfBoundsException("Start-Cell must not be negative");
    }
    if (endRow < 0)
    {
      throw new IndexOutOfBoundsException("End-Cell must not be negative");
    }
    if (endRow < startRow)
    {
      throw new IndexOutOfBoundsException("End-Cell must not smaller than end-cell");
    }
    
    final long bottomBorder;
    if (endRow >= yBounds.size())
    {
      bottomBorder = yMaxBounds;
    }
    else
    {
      bottomBorder = yBounds.getKeyAt(endRow);
    }
    return bottomBorder - yBounds.getKeyAt(startRow);
  }
  /**
   * The current number of columns. Of course, this value begins to be reliable, once the number of columns is known
   * (that is at the end of the layouting process).
   *
   * @return the number columns.
   */
  public int getColumnCount()
  {
    return Math.max(xBounds.size() - 1, 0);
  }

  /**
   * The current number of rows. Of course, this value begins to be reliable, once the number of rows is known (that is
   * at the end of the layouting process).
   *
   * @return the number columns.
   */
  public int getRowCount()
  {
    return Math.max(yBounds.size() - 1, 0);
  }

  public long getXPosition(final int col)
  {
    return xBounds.getKeyAt(col);
  }

  public long getYPosition(final int row)
  {
    return yBounds.getKeyAt(row);
  }

  public TableCellDefinition getBackgroundAt(final int x, final int y, final int columnSpan, final int rowSpan)
  {
    if (rowSpan == 1 && columnSpan == 1)
    {
      return getBackgroundAt(x, y);
    }

    // make a large-scale merge ...
    final StrictBounds bounds = new StrictBounds();
    // First merge all cells on each row. This is a merge in horizontal direction, much like we did on end-Sheet.
    // This way we get a list of 1-col-cells per row, that can then be merged vertically. 
    final TableCellDefinition[] rowBackgrounds = new TableCellDefinition[rowSpan];
    for (int row = 0; row < rowSpan; row += 1)
    {
      for (int col = 0; col < columnSpan; col += 1)
      {
        final TableCellDefinition rightBackground = getBackgroundAt(y + row, x + col);
        if (rightBackground == null)
        {
          continue;
        }
        if (rowBackgrounds[row] == null)
        {
          rowBackgrounds[row] = rightBackground;
        }
        else
        {
          // merge.
          final TableCellDefinition leftBackground = rowBackgrounds[row];
          bounds.setRect(leftBackground.getX(), leftBackground.getY(),
              rightBackground.getX() + rightBackground.getWidth() - leftBackground.getX(), leftBackground.getHeight());
          rowBackgrounds[row] = leftBackground.merge(rightBackground, bounds);
        }

      }
    }

    TableCellDefinition topBackground = rowBackgrounds[0];
    for (int i = 1; i < rowBackgrounds.length; i++)
    {
      final TableCellDefinition bottomBackground = rowBackgrounds[i];
      if (bottomBackground == null)
      {
        continue;
      }
      if (topBackground == null)
      {
        topBackground = bottomBackground;
        continue;
      }
      bounds.setRect(topBackground.getX(), topBackground.getY(),
          topBackground.getWidth(), bottomBackground.getY() + bottomBackground.getHeight() - topBackground.getY());
      topBackground = topBackground.merge(bottomBackground, bounds);
    }
    // normalize before returning ..
    bounds.setRect(getXPosition(x), getYPosition(y), getCellWidth(x, x + columnSpan), getRowHeight(y, y + rowSpan));
    return topBackground.normalize(bounds);
  }
}
