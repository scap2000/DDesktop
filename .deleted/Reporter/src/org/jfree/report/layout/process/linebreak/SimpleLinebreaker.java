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
 * SimpleLinebreaker.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process.linebreak;

import java.util.Arrays;

import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;

/**
 * This implementation is used in the simple mode. The pool-box is used as is - none of the nodes get derived,
 * but we keep track of the calls in case we encounter a manual break later. In that case, the SimpleLinebreaker
 * converts itself into a FullLinebreaker.
 *
 * @author Thomas Morgner
 */
public class SimpleLinebreaker implements ParagraphLinebreaker
{
//  private class Call
//  {
//    private Call next;
//    private Object parameter;
//    private int method;
//
//    protected Call(final int method, final Object parameter, final Call next)
//    {
//      this.next = next;
//      this.method = method;
//      this.parameter = parameter;
//    }
//
//    public Call getNext()
//    {
//      return next;
//    }
//
//    public Object getParameter()
//    {
//      return parameter;
//    }
//
//    public int getMethod()
//    {
//      return method;
//    }
//  }

  private static final int MTH_START_BLOCK = 0;
  private static final int MTH_FINISH_BLOCK = 1;
  private static final int MTH_START_INLINE = 2;
  private static final int MTH_FINISH_INLINE = 3;
  private static final int MTH_ADD_NODE = 4;

  private int[] methods;
  private Object[] parameters;
  private int counter;

  private ParagraphRenderBox paragraphRenderBox;
  private Object suspendItem;
  private boolean breakRequested;

  public SimpleLinebreaker(final ParagraphRenderBox paragraphRenderBox)
  {
    this.paragraphRenderBox = paragraphRenderBox;
    this.methods = new int[paragraphRenderBox.getPoolSize()];
    this.parameters = new Object[paragraphRenderBox.getPoolSize()];
  }

  private void add(final int method, final Object parameter)
  {
    if (methods.length == counter)
    {
      // Grow the arrays ..
      final int nextSize = Math.max (10, (methods.length * 2));
      final int[] newMethods = new int[nextSize];
      System.arraycopy(methods, 0, newMethods, 0, methods.length);

      final Object[] newParameters = new Object[nextSize];
      System.arraycopy(parameters, 0, newParameters, 0, parameters.length);

      this.methods = newMethods;
      this.parameters = newParameters;
    }

    methods[counter] = method;
    parameters[counter] = parameter;
    counter += 1;
  }

  public boolean isWritable()
  {
    return true;
  }

  public FullLinebreaker startComplexLayout()
  {
    final FullLinebreaker fullBreaker = new FullLinebreaker(paragraphRenderBox);
    for (int i = 0; i < counter; i++)
    {
      final int method = methods[i];
      final Object parameter = parameters[i];
      switch(method)
      {
        case MTH_START_BLOCK:
        {
          fullBreaker.startBlockBox((RenderBox) parameter);
          break;
        }
        case MTH_FINISH_BLOCK:
        {
          fullBreaker.finishBlockBox((RenderBox) parameter);
          break;
        }
        case MTH_START_INLINE:
        {
          fullBreaker.startInlineBox((InlineRenderBox) parameter);
          break;
        }
        case MTH_FINISH_INLINE:
        {
          fullBreaker.finishInlineBox((InlineRenderBox) parameter);
          break;
        }
        case MTH_ADD_NODE:
        {
          fullBreaker.addNode((RenderNode) parameter);
          break;
        }
        default:
        {
          throw new IllegalStateException();
        }
      }
    }

    paragraphRenderBox.setPoolSize(counter);
    // replay ..
    return fullBreaker;
  }

  public void startBlockBox(final RenderBox child)
  {
    if (suspendItem != null)
    {
      suspendItem = child.getInstanceId();
    }

    add(MTH_START_BLOCK, child);
  }

  public void finishBlockBox(final RenderBox box)
  {
    if (suspendItem == box.getInstanceId())
    {
      suspendItem = null;
    }

    add(MTH_FINISH_BLOCK, box);
  }

  public ParagraphLinebreaker startParagraphBox(final ParagraphRenderBox box)
  {
    throw new UnsupportedOperationException();
  }

  public void finishParagraphBox(final ParagraphRenderBox box)
  {
    throw new UnsupportedOperationException();
  }

  public boolean isSuspended()
  {
    return suspendItem != null;
  }

  public void finish()
  {
    paragraphRenderBox.setPoolSize(counter);
    paragraphRenderBox.setLineBoxAge(paragraphRenderBox.getPool().getChangeTracker());
    counter = 0;
    Arrays.fill(parameters, null);
  }

  public void startInlineBox(final InlineRenderBox box)
  {
    add(MTH_START_INLINE, box);
  }

  public void finishInlineBox(final InlineRenderBox box)
  {
    add(MTH_FINISH_INLINE, box);
  }

  public boolean isBreakRequested()
  {
    return breakRequested;
  }

  public void addNode(final RenderNode node)
  {
    add(MTH_ADD_NODE, node);
  }

  public void setBreakRequested(final boolean breakRequested)
  {
    this.breakRequested = breakRequested;
  }
}
