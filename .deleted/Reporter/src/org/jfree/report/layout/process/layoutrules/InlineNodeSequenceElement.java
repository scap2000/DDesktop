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
 * InlineNodeSequenceElement.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process.layoutrules;

import org.jfree.report.layout.model.RenderNode;

/**
 * Anthing that is not text. This could be an image or an inline-block element.
 * For now, we assume that these beasts are not breakable at the end of the
 * line (outer linebreaks).
 *
 * @author Thomas Morgner
 */
public class InlineNodeSequenceElement  implements InlineSequenceElement
{
  public static final InlineSequenceElement INSTANCE = new InlineNodeSequenceElement();

  protected InlineNodeSequenceElement()
  {
  }

  /**
   * The width of the element. This is the minimum width of the element.
   *
   * @return
   */
  public long getMinimumWidth(final RenderNode node)
  {
    return node.getMinimumChunkWidth();
  }

  /**
   * The extra-space width for an element. Some elements can expand to fill some
   * more space (justified text is a good example, adding some space between the
   * letters of each word to reduce the inner-word spacing).
   *
   * @return
   */
  public long getMaximumWidth(final RenderNode node)
  {
    return node.getMaximumBoxWidth();
  }

  public boolean isPreserveWhitespace(final RenderNode node)
  {
    return false;
  }
}
