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
 * RenderableTextFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.text;

import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.style.StyleSheet;

/**
 * Problem: Text may span more than one chunk, and text may influence the break behaviour of the next chunk.
 * <p/>
 * Possible solution: TextFactory does not return the complete text. It returns the text up to the last whitespace
 * encountered and returns the text chunk only if either finishText has been called or some more text comes in. The ugly
 * sideffect: Text may result in more than one renderable text chunk returned.
 * <p/>
 * If we return lines (broken by an LineBreak-occurence) we can safe us a lot of trouble later.
 *
 * @author Thomas Morgner
 */
public interface RenderableTextFactory
{
  /**
   * The text is given as CodePoints.
   *
   * @param text
   * @return
   */
  public RenderNode[] createText(final int[] text,
                                 final int offset,
                                 final int length,
                                 final StyleSheet layoutContext);

  public RenderNode[] finishText();

  public void startText();
}
