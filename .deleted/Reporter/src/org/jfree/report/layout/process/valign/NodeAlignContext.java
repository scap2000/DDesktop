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
 * NodeAlignContext.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process.valign;

import org.jfree.report.layout.model.RenderNode;

/**
 * A generic align context for images and other nodes. (Renderable-Content
 * should have been aligned by the parent.
 *
 * @author Thomas Morgner
 */
public class NodeAlignContext extends AlignContext
{
  private long shift;

  public NodeAlignContext(final RenderNode node)
  {
    super(node);
  }

  public long getBaselineDistance(final int baseline)
  {
    return 0;
  }

  public void shift(final long delta)
  {
    this.shift += delta;
  }

  public long getAfterEdge()
  {
    return shift;
  }

  public long getBeforeEdge()
  {
    return shift;
  }
}
