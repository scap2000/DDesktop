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
 * RendererElement.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import org.jfree.report.layout.model.context.NodeLayoutProperties;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.VerticalTextAlign;
import org.jfree.report.util.InstanceID;
import org.jfree.report.util.geom.StrictBounds;

/**
 * Creation-Date: 03.04.2007, 13:16:06
 *
 * @author Thomas Morgner
 */
public abstract class RenderNode implements Cloneable
{
  public static final int HORIZONTAL_AXIS = 0;
  public static final int VERTICAL_AXIS = 1;

  public static final int CACHE_CLEAN = 0;
  public static final int CACHE_DIRTY = 1;
  public static final int CACHE_DEEP_DIRTY = 2;

  private long changeTracker;
  private RenderBox parent;
  private RenderNode next;
  private RenderNode prev;
  private boolean frozen;
  private boolean hibernated;

  private long minimumChunkWidth;
  private long maximumBoxWidth;
  private NodeLayoutProperties nodeLayoutProperties;

  private long computedX;
  private long computedWidth;

  private long cachedAge;
  private long cachedParentWidth;

  private long cachedX;
  private long cachedY;
  private long cachedWidth;
  private long cachedHeight;

  private long x;
  private long y;
  private long width;
  private long height;

  private int cacheState;
  private boolean finished;

  protected RenderNode(final StyleSheet styleSheet)
  {
    this.nodeLayoutProperties = new NodeLayoutProperties(styleSheet);
    this.cacheState = CACHE_DEEP_DIRTY;
  }

  public boolean isSizeSpecifiesBorderBox()
  {
    return true;
  }

  public long getComputedWidth()
  {
    return computedWidth;
  }

  /**
   * Defines the computed width. The computed-width is a static-property and is always specified as border-box size.
   *
   * @param computedWidth
   */
  public void setComputedWidth(final long computedWidth)
  {
    if (computedWidth < 0)
    {
      throw new IllegalArgumentException();
    }
//    if (computedWidth == 0)
//    {
//      Log.debug ("HJERE");
//    }
    this.computedWidth = computedWidth;
  }

  protected void setMinorAxis(final int axis)
  {
    this.nodeLayoutProperties.setMinorAxis(axis);
  }

  public int getMinorAxis()
  {
    return this.nodeLayoutProperties.getMinorAxis();
  }

  protected void setMajorAxis(final int axis)
  {
    this.nodeLayoutProperties.setMajorAxis(axis);
  }

  public int getMajorAxis()
  {
    return this.nodeLayoutProperties.getMajorAxis();
  }

  public final NodeLayoutProperties getNodeLayoutProperties()
  {
    return nodeLayoutProperties;
  }

  public final long getX()
  {
    return x;
  }

  public final void setX(final long x)
  {
    this.x = x;
    //this.updateChangeTracker();
  }

  public final long getY()
  {
    return y;
  }

  public final void shift(final long amount)
  {
    this.y += amount;
    this.updateCacheState(CACHE_DEEP_DIRTY);
  }

  public final void setY(final long y)
  {
    this.y = y;
    this.updateCacheState(CACHE_DEEP_DIRTY);
  }

  protected final void updateCacheState(final int state)
  {
    switch (state)
    {
//      case CACHE_CLEAN:
//        this.cacheState = CACHE_CLEAN;
//        break;
      case CACHE_DIRTY:
        if (cacheState == CACHE_CLEAN)
        {
          this.cacheState = CACHE_DIRTY;
          if (parent != null)
          {
            parent.updateCacheState(CACHE_DIRTY);
          }
        }
        // if cache-state either dirty or deep-dirty, no need to update.
        break;
      case CACHE_DEEP_DIRTY:
        if (cacheState == CACHE_CLEAN)
        {
          this.cacheState = CACHE_DEEP_DIRTY;
          if (parent != null)
          {
            parent.updateCacheState(CACHE_DIRTY);
          }
        }
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  public final long getWidth()
  {
    return width;
  }

  public final void setWidth(final long width)
  {
    this.width = width;
    //this.updateChangeTracker();
  }

  public final long getHeight()
  {
    return height;
  }

  public final void setHeight(final long height)
  {
    this.height = height;
    this.updateCacheState(CACHE_DIRTY);
    //this.updateChangeTracker();
  }

  public final StyleSheet getStyleSheet()
  {
    return nodeLayoutProperties.getStyleSheet();
  }

  public InstanceID getInstanceId()
  {
    return nodeLayoutProperties.getInstanceId();
  }

  protected void updateChangeTracker()
  {
    changeTracker += 1;
    cacheState = CACHE_DIRTY;
    if (parent != null)
    {
      parent.updateChangeTracker();
    }
  }

  public final long getChangeTracker()
  {
    return changeTracker;
  }

  public final RenderBox getParent()
  {
    return parent;
  }

  protected final void setParent(final RenderBox parent)
  {
    if (parent != null && prev == parent)
    {
      throw new IllegalStateException("Assertation failed.");
    }
    // Object oldParent = this.parent;
    this.parent = parent;
  }

  public RenderNode getVisiblePrev()
  {
    RenderNode node = prev;
    while (node != null)
    {
      if (node.isIgnorableForRendering() == false)
      {
        return node;
      }
      node = node.getPrev();
    }
    return null;
  }

  public final RenderNode getPrev()
  {
    return prev;
  }

  protected final void setPrev(final RenderNode prev)
  {
    this.prev = prev;
    if (prev != null && prev == parent)
    {
      throw new IllegalStateException();
    }
  }

  public RenderNode getVisibleNext()
  {
    RenderNode node = next;
    while (node != null)
    {
      if (node.isIgnorableForRendering() == false)
      {
        return node;
      }
      node = node.getNext();
    }
    return null;
  }


  public final RenderNode getNext()
  {
    return next;
  }

  protected final void setNext(final RenderNode next)
  {
    this.next = next;
  }

  public LogicalPageBox getLogicalPage()
  {
    RenderNode parent = this;
    while (parent != null)
    {
      if (parent instanceof LogicalPageBox)
      {
        return (LogicalPageBox) parent;
      }

      parent = parent.getParent();
    }
    return null;
  }

  /**
   * Clones this node. Be aware that cloning can get you into deep trouble, as the relations this node has may no longer
   * be valid.
   *
   * @return
   */
  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Clone failed for some reason.");
    }
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the original node. The derived node will no
   * longer have any parent, silbling, child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode derive(final boolean deep)
  {
    final RenderNode node = (RenderNode) clone();
    node.parent = null;
    node.next = null;
    node.prev = null;
    node.hibernated = false;
    return node;
  }

  /**
   * Derives an hibernation copy. The resulting object should get stripped of all unnecessary caching information and
   * all objects, which will be regenerated when the layouting restarts. Size does matter here.
   *
   * @return
   */
  public RenderNode hibernate()
  {
    final RenderNode node = (RenderNode) clone();
    node.parent = null;
    node.next = null;
    node.prev = null;
    node.hibernated = true;
    return node;
  }

  public RenderNode deriveFrozen(final boolean deep)
  {
    final RenderNode node = (RenderNode) clone();
    node.parent = null;
    node.next = null;
    node.prev = null;
    node.frozen = true;
    node.hibernated = false;
    return node;
  }

  public boolean isFrozen()
  {
    return frozen;
  }

  public boolean isHibernated()
  {
    return hibernated;
  }

  protected void setHibernated(final boolean hibernated)
  {
    this.hibernated = hibernated;
  }

  public RenderNode findNodeById(final Object instanceId)
  {
    if (instanceId == getInstanceId())
    {
      return this;
    }
    return null;
  }

  public boolean isOpen()
  {
    return false;
  }

  public boolean isEmpty()
  {
    return false;
  }

  public RenderBox getParentBlockContext()
  {
    if (parent == null)
    {
      return null;
    }
    if (parent instanceof BlockRenderBox)
    {
      return parent;
    }
    if (parent instanceof CanvasRenderBox)
    {
      return parent;
    }
    return parent.getParentBlockContext();
  }


  public boolean isDiscardable()
  {
    return false;
  }

  /**
   * If that method returns true, the element will not be used for rendering. For the purpose of computing sizes or
   * performing the layouting (in the validate() step), this element will treated as if it is not there.
   * <p/>
   * If the element reports itself as non-empty, however, it will affect the margin computation.
   *
   * @return
   */
  public boolean isIgnorableForRendering()
  {
    return isEmpty();
  }

  public void freeze()
  {
    frozen = true;
  }

  public long getMaximumBoxWidth()
  {
    return maximumBoxWidth;
  }

  public void setMaximumBoxWidth(final long maximumBoxWidth)
  {
    this.maximumBoxWidth = maximumBoxWidth;
  }

  public long getMinimumChunkWidth()
  {
    return minimumChunkWidth;
  }

  public void setMinimumChunkWidth(final long minimumChunkWidth)
  {
    this.minimumChunkWidth = minimumChunkWidth;
  }

  public long getComputedX()
  {
    return computedX;
  }

  public void setComputedX(final long computedX)
  {
    this.computedX = computedX;
  }

  public long getEffectiveMarginTop()
  {
    return 0;
  }

  public long getEffectiveMarginBottom()
  {
    return 0;
  }

  public VerticalTextAlign getVerticalTextAlignment()
  {
    return nodeLayoutProperties.getVerticalTextAlign();
  }
//
//  /**
//   * The sticky-Marker contains the original Y of this node.
//   * @return
//   */
//  public long getStickyMarker()
//  {
//    return stickyMarker;
//  }
//
//  public void setStickyMarker(final long stickyMarker)
//  {
//    this.stickyMarker = stickyMarker;
//  }

  public String getName()
  {
    return null;
  }

  public boolean isBreakAfter()
  {
    return false;
  }

  public final long getCachedAge()
  {
    return cachedAge;
  }

  public final void setCachedAge(final long cachedAge)
  {
    this.cachedAge = cachedAge;
  }

  public final long getCachedParentWidth()
  {
    return cachedParentWidth;
  }

  public final void setCachedParentWidth(final long cachedParentWidth)
  {
    this.cachedParentWidth = cachedParentWidth;
  }

  public final long getCachedX()
  {
    return cachedX;
  }

  public final void setCachedX(final long cachedX)
  {
    this.cachedX = cachedX;
  }

  public final long getCachedY()
  {
    return cachedY;
  }

  public final void setCachedY(final long cachedY)
  {
    this.cachedY = cachedY;
  }

  public final void shiftCached(final long amount)
  {
    this.cachedY += amount;
  }

  public final long getCachedWidth()
  {
    return cachedWidth;
  }

  public final void setCachedWidth(final long cachedWidth)
  {
    this.cachedWidth = cachedWidth;
  }

  public final long getCachedHeight()
  {
    return cachedHeight;
  }

  public final void setCachedHeight(final long cachedHeight)
  {
    this.cachedHeight = cachedHeight;
  }

  public void apply()
  {
    this.x = this.cachedX;
    this.y = this.cachedY;
    this.width = this.cachedWidth;
    this.height = this.cachedHeight;
    this.cachedAge = this.changeTracker;
    this.cacheState = CACHE_CLEAN;
    if (this.parent == null)
    {
      this.cachedParentWidth = 0;
    }
    else
    {
      this.cachedParentWidth = this.parent.getWidth();
    }
  }

  public final boolean isCacheValid()
  {
    if (cachedAge != changeTracker)
    {
      return false;
    }

    if (this.parent == null)
    {
      if (this.cachedParentWidth != 0)
      {
//        cachedAge = -1;
        return false;
      }
    }
    else
    {
      if (this.cachedParentWidth != this.parent.getWidth())
      {
//        cachedAge = -1;
        return false;
      }
    }
    return true;
  }

  /**
   * Checks, whether this node can be removed. This flag is used by iterative streaming output targets to mark nodes
   * that have been fully processed.
   *
   * @return
   */
  public boolean isFinished()
  {
    return finished;
  }

  public void setFinished(final boolean finished)
  {
    if (this.finished == true && finished == false)
    {
      throw new IllegalStateException();
    }
    this.finished = finished;
  }

  public int getCacheState()
  {
    return cacheState;
  }

  public void markCacheClean()
  {
    if (cachedY != y)
    {
      throw new IllegalStateException();
    }
    cacheState = CACHE_CLEAN;
  }

  public Object getStateKey()
  {
    return null;
  }

  public final boolean isNodeVisible(final StrictBounds drawArea)
  {
    final long drawAreaX0 = drawArea.getX();
    final long drawAreaY0 = drawArea.getY();
    final long drawAreaX1 = drawAreaX0 + drawArea.getWidth();
    final long drawAreaY1 = drawAreaY0 + drawArea.getHeight();

    final long x2 = x + width;
    final long y2 = y + height;

    if (width == 0)
    {
      if (x2 < drawAreaX0)
      {
        return false;
      }
      if (x > drawAreaX1)
      {
        return false;
      }
    }
    else
    {
      if (x2 <= drawAreaX0)
      {
        return false;
      }
      if (x >= drawAreaX1)
      {
        return false;
      }
    }
    if (height == 0)
    {
      if (y2 < drawAreaY0)
      {
        return false;
      }
      if (y > drawAreaY1)
      {
        return false;
      }
    }
    else
    {
      if (y2 <= drawAreaY0)
      {
        return false;
      }
      if (y >= drawAreaY1)
      {
        return false;
      }
    }
    return true;
  }

  public final boolean isNodeVisibleInParent()
  {
    final RenderBox parent = getParent();
    if (parent == null)
    {
      // Nodes without a parent are always visible.
      return true;
    }

    final long drawAreaX0 = parent.getX();
    final long drawAreaY0 = parent.getY();
    final long drawAreaX1 = drawAreaX0 + parent.getWidth();
    final long drawAreaY1 = drawAreaY0 + parent.getHeight();

    final long x2 = x + width;
    final long y2 = y + height;

    if (width == 0)
    {
      if (x2 < drawAreaX0)
      {
        return false;
      }
      if (x > drawAreaX1)
      {
        return false;
      }
    }
    else
    {
      if (x2 <= drawAreaX0)
      {
        return false;
      }
      if (x >= drawAreaX1)
      {
        return false;
      }
    }
    if (height == 0)
    {
      if (y2 < drawAreaY0)
      {
        return false;
      }
      if (y > drawAreaY1)
      {
        return false;
      }
    }
    else
    {
      if (y2 <= drawAreaY0)
      {
        return false;
      }
      if (y >= drawAreaY1)
      {
        return false;
      }
    }
    return true;
  }

}
