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
 * StartSequenceElement.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.process.layoutrules;

import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.model.context.StaticBoxLayoutProperties;

/**
 * Represents the opening of an inline element and represents the respective border. There is no break after that
 * element.
 *
 * @author Thomas Morgner
 */
public class StartSequenceElement implements InlineSequenceElement
{
  public static final InlineSequenceElement INSTANCE = new StartSequenceElement();

  private StartSequenceElement()
  {
  }

  /**
   * The width of the element. This is the minimum width of the element.
   *
   * @return
   */
  public long getMinimumWidth(final RenderNode node)
  {
    final InlineRenderBox box = (InlineRenderBox) node;
    final StaticBoxLayoutProperties blp = box.getStaticBoxLayoutProperties();
    final BoxDefinition bdef = box.getBoxDefinition();
    return blp.getBorderLeft() + bdef.getPaddingLeft() + blp.getMarginLeft();
  }

  /**
   * The extra-space width for an element. Some elements can expand to fill some more space (justified text is a good
   * example, adding some space between the letters of each word to reduce the inner-word spacing).
   *
   * @return
   */
  public long getMaximumWidth(final RenderNode node)
  {
    return getMinimumWidth(node);
  }


  public boolean isPreserveWhitespace(final RenderNode node)
  {
    final InlineRenderBox box = (InlineRenderBox) node;
    return box.getStaticBoxLayoutProperties().isPreserveSpace();
  }
}
