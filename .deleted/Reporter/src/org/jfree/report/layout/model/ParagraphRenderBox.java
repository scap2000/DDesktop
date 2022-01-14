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
 * ParagraphRenderBox.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.style.ParagraphPoolboxStyleSheet;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.InstanceID;

/**
 * Creation-Date: 03.04.2007, 13:38:00
 *
 * @author Thomas Morgner
 */
public class ParagraphRenderBox extends BlockRenderBox
{
  private static class LineBoxRenderBox extends BlockRenderBox
  {
    protected LineBoxRenderBox(final StyleSheet styleSheet, final Object stateKey)
    {
      super(styleSheet, BoxDefinition.EMPTY, stateKey);
    }
  }

  private ParagraphPoolBox pool;
  private LineBoxRenderBox lineboxContainer;
  private ElementAlignment textAlignment;
  private ElementAlignment lastLineAlignment;
  private long lineBoxAge;
  private long minorLayoutAge;
  private int poolSize;
  private Object rawValue;

  public ParagraphRenderBox(final StyleSheet styleSheet,
                            final BoxDefinition boxDefinition,
                            final Object stateKey)
  {
    super(styleSheet, boxDefinition, stateKey);

    pool = new ParagraphPoolBox(new ParagraphPoolboxStyleSheet(styleSheet), stateKey);
    pool.setParent(this);

//    // yet another helper box. Level 2
//    lineboxContainer = new LineBoxRenderBox(styleSheet);
//    lineboxContainer.setParent(this);

    // level 3 means: Add all lineboxes to the paragraph
    // This gets auto-generated ..
    this.textAlignment = (ElementAlignment)
        styleSheet.getStyleProperty(ElementStyleKeys.ALIGNMENT, ElementAlignment.LEFT);
    this.lastLineAlignment = textAlignment;
  }
//
//  public void appyStyle(LayoutContext context, OutputProcessorMetaData metaData)
//  {
//    super.appyStyle(context, metaData);
//    CSSValue alignVal = context.getValue(TextStyleKeys.TEXT_ALIGN);
//    CSSValue alignLastVal = context.getValue(TextStyleKeys.TEXT_ALIGN_LAST);
//    this.textAlignment = createAlignment(alignVal);
//    if (textAlignment == TextAlign.JUSTIFY)
//    {
//      this.lastLineAlignment = createAlignment(alignLastVal);
//    }
//    else
//    {
//      this.lastLineAlignment = textAlignment;
//    }
//
//    pool.appyStyle(context, metaData);
//    lineboxContainer.appyStyle(context, metaData);
//  }

  /**
   * Derive creates a disconnected node that shares all the properties of the original node. The derived node will no
   * longer have any parent, silbling, child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode derive(final boolean deepDerive)
  {
    final ParagraphRenderBox box = (ParagraphRenderBox) super.derive(deepDerive);
    box.pool = (ParagraphPoolBox) pool.derive(deepDerive);
    box.pool.setParent(box);

    if (lineboxContainer != null)
    {
      box.lineboxContainer = (LineBoxRenderBox) lineboxContainer.derive(deepDerive);
      box.lineboxContainer.setParent(box);
    }
    if (!deepDerive)
    {
      box.lineBoxAge = 0;
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
    final ParagraphRenderBox box = (ParagraphRenderBox) super.derive(false);
    box.setHibernated(true);
    box.pool = (ParagraphPoolBox) pool.hibernate();
    box.pool.setParent(box);
    if (lineboxContainer != null)
    {
      box.lineboxContainer = (LineBoxRenderBox) lineboxContainer.hibernate();
    }
    box.lineBoxAge = 0;
    return box;
  }

  public final void addChild(final RenderNode child)
  {
    pool.addChild(child);
  }

  protected void addDirectly(final RenderNode child)
  {
    if (child instanceof ParagraphPoolBox)
    {
      final ParagraphPoolBox poolBox = (ParagraphPoolBox) child;
      poolBox.trim();
    }
    super.addGeneratedChild(child);
  }

  /**
   * Removes all children.
   */
  public final void clear()
  {
    pool.clear();
    if (lineboxContainer != null)
    {
      lineboxContainer.clear();
    }
    super.clear();
    lineBoxAge = 0;
  }

  public final void clearLayout()
  {
    super.clear();
    minorLayoutAge = 0;
    //lineBoxAge = 0;
  }

  public RenderBox getInsertationPoint()
  {
    return pool.getInsertationPoint();
  }

  public boolean isAppendable()
  {
    return pool.isAppendable();
  }

  public boolean isEmpty()
  {
    return pool.isEmpty();
  }

  public boolean isDiscardable()
  {
    return pool.isDiscardable();
  }

  public ElementAlignment getLastLineAlignment()
  {
    return lastLineAlignment;
  }

  public ElementAlignment getTextAlignment()
  {
    return textAlignment;
  }

  public RenderBox getLineboxContainer()
  {
    return lineboxContainer;
  }

  public boolean isComplexParagraph()
  {
    return lineboxContainer != null;
  }

  public RenderBox createLineboxContainer()
  {
    if (lineboxContainer == null)
    {
      this.lineboxContainer = new LineBoxRenderBox(pool.getStyleSheet(), getStateKey());
      this.lineboxContainer.setParent(this);
    }
    return lineboxContainer;
  }

  public ParagraphPoolBox getPool()
  {
    return pool;
  }

  public long getLineBoxAge()
  {
    return lineBoxAge;
  }

  public void setLineBoxAge(final long lineBoxAge)
  {
    this.lineBoxAge = lineBoxAge;
  }

  public long getMinorLayoutAge()
  {
    return minorLayoutAge;
  }

  public void setMinorLayoutAge(final long minorLayoutAge)
  {
    this.minorLayoutAge = minorLayoutAge;
  }

  /**
   * The public-id for the paragraph is the pool-box.
   *
   * @return
   */
  public InstanceID getInstanceId()
  {
    return pool.getInstanceId();
  }

  public int getPoolSize()
  {
    return poolSize;
  }

  public void setPoolSize(final int poolSize)
  {
    this.poolSize = poolSize;
  }

  public void setRawValue(final Object rawValue)
  {
    this.rawValue = rawValue;
  }

  public Object getRawValue()
  {
    return rawValue;
  }

  public void close()
  {
    pool.close();
    super.close();
  }
}
