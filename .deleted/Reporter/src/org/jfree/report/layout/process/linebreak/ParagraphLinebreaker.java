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
 * ParagraphLinebreaker.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process.linebreak;

import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;

/**
 * Creation-Date: 25.04.2007, 13:42:05
 *
 * @author Thomas Morgner
 */
public interface ParagraphLinebreaker
{
  public boolean isWritable();

  public FullLinebreaker startComplexLayout();

  public void startBlockBox(final RenderBox child);
  public void finishBlockBox(final RenderBox box);

  public ParagraphLinebreaker startParagraphBox(final ParagraphRenderBox box);
  public void finishParagraphBox(final ParagraphRenderBox box);

//  public Object getSuspendItem();
  public boolean isSuspended();

  public void finish();

  public void startInlineBox(final InlineRenderBox box);
  public void finishInlineBox(final InlineRenderBox box);

  public boolean isBreakRequested();

  public void addNode(final RenderNode node);

  public void setBreakRequested(final boolean breakRequested);
}
