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
 * ParagraphPoolBox.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 03.04.2007, 13:38:24
 *
 * @author Thomas Morgner
 */
public class ParagraphPoolBox extends InlineRenderBox
{
  private long lineHeight;

  public ParagraphPoolBox(final StyleSheet style, final Object stateKey)
  {
    super(style, BoxDefinition.EMPTY, stateKey);
    lineHeight = StrictGeomUtility.toInternalValue(style.getDoubleStyleProperty(TextStyleKeys.LINEHEIGHT, 0));
  }

  public long getLineHeight()
  {
    return lineHeight;
  }

//  public void close()
//  {
//    if (isOpen() == false)
//    {
//      return;
//    }
//
//    final RenderBox parent = getParent();
//    super.close();
//    if (parent != null)
//    {
//      parent.close();
//    }
//  }
//
  public void trim()
  {
    // remove leading and trailing spacer ...
    RenderNode node = getFirstChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering() == false)
      {
        break;
      }
      remove(node);
      node = getFirstChild();
    }

    node = getLastChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering() == false)
      {
        break;
      }
      remove(node);
      node = getLastChild();
    }
  }
}
