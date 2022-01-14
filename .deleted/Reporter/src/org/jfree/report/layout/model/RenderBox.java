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
 * RendererContainer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.model.context.StaticBoxLayoutProperties;
import org.jfree.report.layout.text.ExtendedBaselineInfo;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.util.Log;

/**
 * Creation-Date: 03.04.2007, 13:17:47
 *
 * @author Thomas Morgner
 */
public abstract class RenderBox extends RenderNode
{
  public static final int NO_MANUAL_BREAK = 0;
  public static final int DIRECT_MANUAL_BREAK = 1;
  public static final int INDIRECT_MANUAL_BREAK = 2;

  public static final boolean LOG_PRUNE = true;

  private long contentAreaX1;
  private long contentAreaX2;
  private BoxDefinition boxDefinition;
  private StaticBoxLayoutProperties staticBoxLayoutProperties;
  private RenderNode firstChild;
  private RenderNode lastChild;
  private boolean open;
  private ExtendedBaselineInfo baselineInfo;
  private String name;
  private int manualBreakIndicator;
  private long staticBoxPropertiesAge;
  private Object stateKey;
  private RenderBox textEllipseBox;

  private RenderNode prunedChild;
  private boolean deepFinished;

  protected RenderBox(final StyleSheet styleSheet,
                      final BoxDefinition boxDefinition,
                      final Object stateKey)
  {
    super(styleSheet);
    if (boxDefinition == null)
    {
      throw new NullPointerException();
    }

    this.boxDefinition = boxDefinition;
    this.open = true;
    this.staticBoxLayoutProperties = new StaticBoxLayoutProperties();
    this.staticBoxPropertiesAge = -1;
    this.staticBoxLayoutProperties.setBreakAfter
        (getStyleSheet().getBooleanStyleProperty(BandStyleKeys.PAGEBREAK_AFTER));
    this.stateKey = stateKey;
  }

  public boolean isSizeSpecifiesBorderBox()
  {
    return boxDefinition.isSizeSpecifiesBorderBox();
  }

  public RenderBox getTextEllipseBox()
  {
    return textEllipseBox;
  }

  public void setTextEllipseBox(final RenderBox textEllipseBox)
  {
    this.textEllipseBox = textEllipseBox;
  }

  public Object getStateKey()
  {
    return stateKey;
  }

  public int getManualBreakIndicator()
  {
    return manualBreakIndicator;
  }

  public void setManualBreakIndicator(final int manualBreakIndicator)
  {
    this.manualBreakIndicator = manualBreakIndicator;
  }

  public BoxDefinition getBoxDefinition()
  {
    return boxDefinition;
  }

  public RenderNode getFirstChild()
  {
    return firstChild;
  }

  protected void setFirstChild(final RenderNode firstChild)
  {
    this.firstChild = firstChild;
  }

  public RenderNode getLastChild()
  {
    return lastChild;
  }

  protected void setLastChild(final RenderNode lastChild)
  {
    this.lastChild = lastChild;
  }

  public void addGeneratedChild(final RenderNode child)
  {
    if (child == null)
    {
      throw new NullPointerException
          ("Child to be added must not be null.");
    }

    if (isHibernated())
    {
      throw new IllegalStateException
          ("Check your state management, you've messed with an hibernated element.");
    }

    if (lastChild != null)
    {
      lastChild.setNext(child);
    }

    child.setPrev(lastChild);
    child.setNext(null);
    lastChild = child;

    if (firstChild == null)
    {
      firstChild = child;
    }

    child.setParent(this);

    if (isFrozen())
    {
      child.freeze();
    }
    child.updateChangeTracker();
  }

  public void addChild(final RenderNode child)
  {
    if (child == null)
    {
      throw new NullPointerException
          ("Child to be added must not be null.");
    }

    if (isOpen() == false)
    {
      throw new IllegalStateException
          ("Adding content to an already closed element: " + this);
    }

    if (isHibernated())
    {
      throw new IllegalStateException
          ("Check your state management. You tried to modify a hibernated element.");
    }

    if (this instanceof BlockRenderBox)
    {
      if (child instanceof InlineRenderBox)
      {
        throw new IllegalStateException();
      }
    }
    if (lastChild != null)
    {
      lastChild.setNext(child);
    }

    child.setPrev(lastChild);
    child.setNext(null);
    lastChild = child;

    if (firstChild == null)
    {
      firstChild = child;
    }

    child.setParent(this);
    if (isFrozen())
    {
      child.freeze();
    }

    child.updateChangeTracker();
  }

  /**
   * Inserts the given target after the specified node. If the node is null, the target is inserted as first node.
   *
   * @param node
   * @param target
   */
  protected void insertAfter(final RenderNode node, final RenderNode target)
  {
    if (node == null)
    {
      // ok, insert as new first element.
      final RenderNode firstChild = getFirstChild();
      if (firstChild == null)
      {
        setLastChild(target);
        setFirstChild(target);
        target.setParent(this);
        target.setPrev(null);
        target.setNext(null);
        target.updateChangeTracker();
        return;
      }

      // we have a first-child.
      firstChild.setPrev(target);
      setFirstChild(target);
      target.setParent(this);
      target.setPrev(null);
      target.setNext(firstChild);
      target.updateChangeTracker();
      return;
    }

    if (node.getParent() != this)
    {
      throw new IllegalStateException("You made a big boo");
    }

    final RenderNode next = node.getNext();
    node.setNext(target);
    target.setPrev(node);
    target.setParent(this);
    target.setNext(next);
    if (next != null)
    {
      next.setPrev(target);
    }
    else
    {
      setLastChild(target);
    }
    target.updateChangeTracker();
  }

  /**
   * Inserts the given target directly before the the specified node. If the node is null, the element is inserted at
   * the last position.
   *
   * @param node
   * @param target
   */
  protected void insertBefore(final RenderNode node, final RenderNode target)
  {
    if (node == null)
    {
      final RenderNode lastChild = getLastChild();
      if (lastChild == null)
      {
        target.setParent(this);
        target.setPrev(null);
        target.setNext(null);
        setFirstChild(target);
        setLastChild(target);
        target.updateChangeTracker();
        return;
      }

      setLastChild(target);
      target.setParent(this);
      target.setPrev(lastChild);
      target.setNext(null);
      lastChild.setNext(target);
      target.updateChangeTracker();
      return;
    }

    if (node.getParent() != this)
    {
      throw new IllegalStateException("You made a big boo");
    }

    final RenderNode prev = node.getPrev();
    node.setPrev(target);
    target.setNext(node);
    target.setParent(this);
    target.setPrev(prev);
    if (prev != null)
    {
      prev.setNext(target);
    }
    else
    {
      setFirstChild(target);
    }
    target.updateChangeTracker();
  }

  public void replaceChild(final RenderNode old, final RenderNode replacement)
  {
    if (old.getParent() != this)
    {
      throw new IllegalArgumentException("None of my childs.");
    }
    if (old == replacement)
    {
      // nothing to do ...
      return;
    }

    if (old == firstChild)
    {
      firstChild = replacement;
    }
    if (old == lastChild)
    {
      lastChild = replacement;
    }

    final RenderNode prev = old.getPrev();
    final RenderNode next = old.getNext();
    replacement.setPrev(prev);
    replacement.setNext(next);

    if (prev != null)
    {
      prev.setNext(replacement);
    }
    if (next != null)
    {
      next.setPrev(replacement);
    }

    replacement.setParent(this);

    old.setNext(null);
    old.setPrev(null);
    old.setParent(null);
    old.updateChangeTracker();
    replacement.updateChangeTracker();
  }


  public void replaceChilds(final RenderNode old,
                            final RenderNode[] replacement)
  {
    if (old.getParent() != this)
    {
      throw new IllegalArgumentException("None of my childs.");
    }

    if (replacement.length == 0)
    {
      throw new IndexOutOfBoundsException("Array is empty ..");
    }

    if (old == replacement[0])
    {
      if (replacement.length == 1)
      {
        // nothing to do ...
        return;
      }
//      throw new IllegalArgumentException
//          ("Thou shall not use the replace method to insert new elements!");
    }

    old.setParent(null);

    final RenderNode oldPrev = old.getPrev();
    final RenderNode oldNext = old.getNext();

    // first, connect all replacements ...
    RenderNode first = null;
    RenderNode last = null;

    for (int i = 0; i < replacement.length; i++)
    {
      if (last == null)
      {
        last = replacement[i];
        if (last != null)
        {
          first = last;
          first.setParent(this);
        }
        continue;
      }


      final RenderNode node = replacement[i];

      last.setNext(node);
      node.setPrev(last);
      node.setParent(this);
      last = node;
    }

    if (first == null)
    {
      throw new IndexOutOfBoundsException("Array is empty (NullValues stripped)..");
    }

    if (old == firstChild)
    {
      firstChild = first;
    }

    if (old == lastChild)
    {
      lastChild = last;
    }

    // Something inbetween ...
    first.setPrev(oldPrev);
    last.setNext(oldNext);

    if (oldPrev != null)
    {
      oldPrev.setNext(first);
    }
    if (oldNext != null)
    {
      oldNext.setPrev(last);
    }

    old.updateChangeTracker();

    for (int i = 0; i < replacement.length; i++)
    {
      final RenderNode renderNode = replacement[i];
      renderNode.updateChangeTracker();
    }
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the original node. The derived node will no
   * longer have any parent, silbling, child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode derive(final boolean deepDerive)
  {
    final RenderBox box = (RenderBox) super.derive(deepDerive);

    if (deepDerive)
    {
      RenderNode node = firstChild;
      RenderNode currentNode = null;
      while (node != null)
      {
        final RenderNode previous = currentNode;

        currentNode = node.derive(true);
        currentNode.setParent(box);
        if (previous == null)
        {
          box.firstChild = currentNode;
          currentNode.setPrev(null);
        }
        else
        {
          previous.setNext(currentNode);
          currentNode.setPrev(previous);
        }
        node = node.getNext();
      }

      box.lastChild = currentNode;
      if (lastChild != null)
      {
        box.lastChild.setNext(null);
      }
    }
    else
    {
      box.firstChild = null;
      box.lastChild = null;
    }
    return box;
  }


  /**
   * Derive creates a disconnected node that shares all the properties of the original node. The derived node will no
   * longer have any parent, silbling, child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode hibernate()
  {
    final RenderBox box = (RenderBox) super.hibernate();

    RenderNode node = firstChild;
    RenderNode currentNode = null;
    while (node != null)
    {
      final RenderNode previous = currentNode;

      currentNode = node.hibernate();
      currentNode.setParent(box);
      if (previous == null)
      {
        box.firstChild = currentNode;
        currentNode.setPrev(null);
      }
      else
      {
        previous.setNext(currentNode);
        currentNode.setPrev(previous);
      }
      node = node.getNext();
    }

    box.lastChild = currentNode;
    if (lastChild != null)
    {
      box.lastChild.setNext(null);
    }
    return box;
  }


  /**
   * Derive creates a disconnected node that shares all the properties of the original node. The derived node will no
   * longer have any parent, silbling, child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode deriveFrozen(final boolean deepDerive)
  {
    final RenderBox box = (RenderBox) super.deriveFrozen(deepDerive);
    if (deepDerive)
    {
      RenderNode node = firstChild;
      RenderNode currentNode = null;
      while (node != null)
      {
        final RenderNode previous = currentNode;

        currentNode = node.deriveFrozen(true);
        currentNode.setParent(box);
        if (previous == null)
        {
          box.firstChild = currentNode;
          currentNode.setPrev(null);
        }
        else
        {
          previous.setNext(currentNode);
          currentNode.setPrev(previous);
        }
        node = node.getNext();
      }

      box.lastChild = currentNode;
      if (lastChild != null)
      {
        box.lastChild.setNext(null);
      }
    }
    else
    {
      box.firstChild = null;
      box.lastChild = null;
    }
    return box;
  }

  public void addChilds(final RenderNode[] nodes)
  {
    for (int i = 0; i < nodes.length; i++)
    {
      addChild(nodes[i]);
    }
  }

  public void addGeneratedChilds(final RenderNode[] nodes)
  {
    for (int i = 0; i < nodes.length; i++)
    {
      addGeneratedChild(nodes[i]);
    }
  }

  public RenderNode findNodeById(final Object instanceId)
  {
    if (instanceId == getInstanceId())
    {
      return this;
    }

    RenderNode child = getLastChild();
    while (child != null)
    {
      final RenderNode nodeById = child.findNodeById(instanceId);
      if (nodeById != null)
      {
        return nodeById;
      }
      child = child.getPrev();
    }
    return null;
  }

  public boolean isAppendable()
  {
    return isOpen();
  }

  public RenderBox getInsertationPoint()
  {
    if (isAppendable() == false)
    {
      throw new IllegalStateException("Already closed");
    }

    final RenderNode lastChild = getLastChild();
    if (lastChild instanceof RenderBox)
    {
      final RenderBox lcBox = (RenderBox) lastChild;
      if (lcBox.isAppendable())
      {
        return lcBox.getInsertationPoint();
      }
    }
    return this;
  }

  /**
   * Removes all children.
   */
  public void clear()
  {
    RenderNode child = getFirstChild();
    while (child != null)
    {
      final RenderNode nextChild = child.getNext();
      if (child != getFirstChild())
      {
        child.getPrev().setNext(null);
      }
      child.setPrev(null);
      child.setParent(null);
      child = nextChild;
    }
    setFirstChild(null);
    setLastChild(null);
    updateChangeTracker();
  }

  public RenderNode getVisibleFirst()
  {
    RenderNode firstChild = getFirstChild();
    while (firstChild != null)
    {
      if (firstChild.isIgnorableForRendering() == false)
      {
        return firstChild;
      }
      firstChild = firstChild.getNext();
    }
    return null;
  }

  public RenderNode getVisibleLast()
  {
    RenderNode lastChild = getLastChild();
    while (lastChild != null)
    {
      if (lastChild.isIgnorableForRendering() == false)
      {
        return lastChild;
      }
      lastChild = lastChild.getPrev();
    }
    return null;
  }

  private RenderNode getFirstNonEmpty()
  {
    RenderNode firstChild = getFirstChild();
    while (firstChild != null)
    {
      if (firstChild.isEmpty() == false)
      {
        return firstChild;
      }
      firstChild = firstChild.getNext();
    }
    return null;
  }

  public boolean isEmpty()
  {
    if (getBoxDefinition().isEmpty() == false)
    {
      return false;
    }

    final RenderNode node = getFirstNonEmpty();
    if (node != null)
    {
      return false;
    }
    // Ok, the childs were not able to tell us some truth ..
    // lets try something else.
    return true;
  }

  public boolean isDiscardable()
  {
    if (getBoxDefinition().isEmpty() == false)
    {
      return false;
    }

    RenderNode node = getFirstChild();
    while (node != null)
    {
      if (node.isDiscardable() == false)
      {
        return false;
      }
      node = node.getNext();
    }
    return true;
  }

  public void close()
  {
    if (isOpen() == false)
    {
      throw new IllegalStateException("Double close..");
    }

    if (isHibernated())
    {
      throw new IllegalStateException
          ("Check your state management. You tried to mess with an hibernated element.");
    }

    this.open = false;

    RenderNode lastChild = getLastChild();
    while (lastChild != null)
    {
      if (lastChild.isDiscardable())
      {
        if (LOG_PRUNE)
        {
          Log.debug("Pruning: " + lastChild);
        }
        remove(lastChild);
        if (prunedChild == null)
        {
          prunedChild = lastChild;
        }
        else
        {
          lastChild.setPrev(prunedChild);
          prunedChild = lastChild;          
        }
        
        lastChild = getLastChild();
      }
      else
      {
        break;
      }
    }
  }

  public void remove(final RenderNode child)
  {
    final RenderBox parent = child.getParent();
    if (parent != this)
    {
      throw new IllegalArgumentException("None of my childs");
    }

    child.setParent(null);

    final RenderNode prev = child.getPrev();
    final RenderNode next = child.getNext();

    if (prev != null)
    {
      prev.setNext(next);
    }

    if (next != null)
    {
      next.setPrev(prev);
    }

    if (firstChild == child)
    {
      firstChild = next;
    }
    if (lastChild == child)
    {
      lastChild = prev;
    }
    child.updateChangeTracker();
    this.updateChangeTracker();
  }

  public boolean isOpen()
  {
    return open;
  }

  public void freeze()
  {
    if (isFrozen())
    {
      return;
    }

    super.freeze();
    RenderNode node = getFirstChild();
    while (node != null)
    {
      node.freeze();
      node = node.getNext();
    }
  }

  /**
   * Performs a simple split. This box will be altered to form the left/top side of the split, and a derived empty box
   * will be returned, which makes up the right/bottom side.
   * <p/>
   * A split will only happen on inline-boxes during the line-break-step. In the ordinary layouting, splitting is not
   * necesary.
   *
   * @param axis
   * @return
   */
  public RenderBox split(final int axis)
  {
    final RenderBox otherBox = (RenderBox) derive(false);
    final BoxDefinition[] boxDefinitions = boxDefinition.split(axis);
    boxDefinition = boxDefinitions[0];
    otherBox.boxDefinition = boxDefinitions[1];
    return otherBox;
  }


  public long getContentAreaX1()
  {
    return contentAreaX1;
  }

  public void setContentAreaX1(final long contentAreaX1)
  {
    this.contentAreaX1 = contentAreaX1;
  }

  public long getContentAreaX2()
  {
    return contentAreaX2;
  }

  public void setContentAreaX2(final long contentAreaX2)
  {
    this.contentAreaX2 = contentAreaX2;
  }

  public StaticBoxLayoutProperties getStaticBoxLayoutProperties()
  {
    return staticBoxLayoutProperties;
  }

  public ExtendedBaselineInfo getBaselineInfo()
  {
    return baselineInfo;
  }

  public void setBaselineInfo(final ExtendedBaselineInfo baselineInfo)
  {
    this.baselineInfo = baselineInfo;
  }

  public String getName()
  {
    return name;
  }

  public void setName(final String name)
  {
    this.name = name;
  }

  public boolean isBreakAfter()
  {
    return staticBoxLayoutProperties.isBreakAfter();
  }

  public long getStaticBoxPropertiesAge()
  {
    return staticBoxPropertiesAge;
  }

  public void setStaticBoxPropertiesAge(final long staticBoxPropertiesAge)
  {
    if (staticBoxLayoutProperties.getNominalBaselineInfo() == null)
    {
      throw new IllegalStateException
          ("Assertation: Cannot declare static-properties finished without a nominal baseline info");
    }
    this.staticBoxPropertiesAge = staticBoxPropertiesAge;
  }

  public String toString()
  {
    return getClass().getName() + '{' +
        "name='" + name + '\'' +
        ", x='" + getX() + '\'' +
        ", y='" + getY() + '\'' +
        ", width='" + getWidth() + '\'' +
        ", height='" + getHeight() + '\'' +
        ", hexId='" + System.identityHashCode(this) + '\'' +
        ", finished='" + isFinished() + '\'' +
        ", commited='" + isCommited() + '\'' +
        '}';
  }

  public void markCacheClean()
  {
    super.markCacheClean();
    this.staticBoxPropertiesAge = getChangeTracker();
  }
  
  private boolean markedSeen;
  private boolean markedOpen;

  private boolean appliedSeen;
  private boolean appliedOpen;

  public void commitApplyMark()
  {
    appliedOpen = markedOpen;
    appliedSeen = markedSeen;
  }

  public boolean isAppliedOpen()
  {
    return appliedOpen;
  }

  public boolean isAppliedSeen()
  {
    return appliedSeen;
  }

  public boolean isMarkedOpen()
  {
    return markedOpen;
  }

  public void setMarkedOpen(final boolean markedOpen)
  {
    this.markedOpen = markedOpen;
  }

  public boolean isMarkedSeen()
  {
    return markedSeen;
  }

  public void setMarkedSeen(final boolean markedSeen)
  {
    this.markedSeen = markedSeen;
  }

  public boolean isCommited()
  {
    return appliedOpen == false && appliedSeen == true;
  }

  public void reopenAfterRollback()
  {
    this.open = appliedOpen;
    
    while (this.prunedChild != null)
    {
      final RenderNode child = this.prunedChild;
      this.prunedChild = child.getPrev();
      addChild(child);
    }
    prunedChild = null;
  }

  public boolean isDeepFinished()
  {
    return deepFinished;
  }

  public void setDeepFinished(final boolean deepFinished)
  {
    this.deepFinished = deepFinished;
  }


  public final boolean isBoxVisible(final StrictBounds drawArea)
  {
    if (isNodeVisible(drawArea) == false)
    {
      return false;
    }

    final RenderBox parent = getParent();
    if (parent == null)
    {
      return true;
    }

    final StyleSheet styleSheet = getStyleSheet();
    if (styleSheet.getStyleProperty(ElementStyleKeys.ANCHOR_NAME) != null)
    {
      return true;
    }

    if (Boolean.FALSE.equals(styleSheet.getStyleProperty(ElementStyleKeys.OVERFLOW_X)))
    {
      final long parentX1 = parent.getX();
      final long parentX2 = parentX1 + parent.getWidth();

      if (getWidth() == 0)
      {
        // could be a line ..
        return true;
      }

      final long boxX1 = getX();
      final long boxX2 = boxX1 + getWidth();

      if (boxX2 <= parentX1)
      {
        return false;
      }
      if (boxX1 >= parentX2)
      {
        return false;
      }
    }

    if (Boolean.FALSE.equals(styleSheet.getStyleProperty(ElementStyleKeys.OVERFLOW_Y)))
    {
      // Compute whether the box is at least partially contained in the parent's bounding box.
      final long parentY1 = parent.getY();
      final long parentY2 = parentY1 + parent.getHeight();

      if (getHeight() == 0)
      {
        // could be a line ..
        return true;
      }

      final long boxY1 = getY();
      final long boxY2 = boxY1 + getHeight();

      if (boxY2 <= parentY1)
      {
        return false;
      }
      if (boxY1 >= parentY2)
      {
        return false;
      }
    }
    return true;
  }
}
