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
 * ModelPrinter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

import org.jfree.report.layout.model.FinishedRenderNode;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.util.Log;

/**
 * Creation-Date: Jan 9, 2007, 2:22:59 PM
 *
 * @author Thomas Morgner
 */
public class ModelPrinter
{
  private static final boolean PRINT_LINEBOX_CONTENTS = false;

  private ModelPrinter()
  {
  }

  public static void printParents (RenderNode node)
  {
    while (node != null)
    {
      final StringBuffer b = new StringBuffer();
      b.append(node.getClass().getName());
      b.append('[');
      //b.append(Integer.toHexString(System.identityHashCode(node)));
      b.append(']');
      Log.debug (b);
      node = node.getParent();
    }
  }

  public static void print(final RenderBox box)
  {
    printBox(box, 0);
  }

  public static void printBox(final RenderBox box, final int level)
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append(box.getClass().getName());
    b.append('[');
    //b.append(Integer.toHexString(System.identityHashCode(box)));
    b.append(';');
    b.append(box.getName());
    b.append(']');
    b.append("={stateKey=");
    b.append(box.getStateKey());
    b.append(", x=");
    b.append(box.getX());
    b.append(", y=");
    b.append(box.getY());
    b.append(", width=");
    b.append(box.getWidth());
    b.append(", height=");
    b.append(box.getHeight());
    b.append(", computed-x=");
    b.append(box.getComputedX());
    b.append(", computed-width=");
    b.append(box.getComputedWidth());
    b.append(", cached-x=");
    b.append(box.getCachedX());
    b.append(", cached-y=");
    b.append(box.getCachedY());
    b.append(", cached-width=");
    b.append(box.getCachedWidth());
    b.append(", cached-height=");
    b.append(box.getCachedHeight());
    b.append('}');
    Log.debug(b.toString());

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- boxDefinition=");
    b.append(box.getBoxDefinition());
    Log.debug(b.toString());
    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(box.getNodeLayoutProperties());
    Log.debug(b.toString());
    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- staticBoxLayoutProperties=");
    b.append(box.getStaticBoxLayoutProperties());
    Log.debug(b.toString());

    if (box instanceof LogicalPageBox)
    {
      final LogicalPageBox pageBox = (LogicalPageBox) box;
      b = new StringBuffer();
      for (int i = 0; i < level; i++)
      {
        b.append("   ");
      }
      b.append("- PageBox={PageOffset=");
      b.append(pageBox.getPageOffset());
      b.append(", PageHeight=");
      b.append(pageBox.getPageHeight());
      b.append(", PageEnd=");
      b.append(pageBox.getPageEnd());
      b.append('}');
      Log.debug(b.toString());
    }

    if (box.isOpen())
    {
      b = new StringBuffer();
      for (int i = 0; i < level; i++)
      {
        b.append("   ");
      }
      b.append("- WARNING: THIS BOX IS STILL OPEN");
      Log.debug(b.toString());
    }

    if (box.isFinished())
    {
      b = new StringBuffer();
      for (int i = 0; i < level; i++)
      {
        b.append("   ");
      }
      b.append("- INFO: THIS BOX IS FINISHED");
      Log.debug(b.toString());
    }
    if (box.isCommited())
    {
      b = new StringBuffer();
      for (int i = 0; i < level; i++)
      {
        b.append("   ");
      }
      b.append("- INFO: THIS BOX IS COMMITED");
      Log.debug(b.toString());
    }

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    Log.debug(b.toString());

    if (box instanceof ParagraphRenderBox)
    {
      if (PRINT_LINEBOX_CONTENTS)
      {
        final ParagraphRenderBox paraBox = (ParagraphRenderBox) box;
        if (paraBox.isComplexParagraph())
        {
          Log.debug ("----------------  START PARAGRAPH LINEBOX CONTAINER -------------------------------------");
          printBox(paraBox.getLineboxContainer(), level + 1);
          Log.debug ("---------------- FINISH PARAGRAPH LINEBOX CONTAINER -------------------------------------");
        }
      }
    }

    if (box instanceof LogicalPageBox)
    {
      final LogicalPageBox lbox = (LogicalPageBox) box;
      printBox(lbox.getHeaderArea(), level + 1);
      printBox(lbox.getWatermarkArea(), level + 1);
    }
    printChilds(box, level);
    if (box instanceof LogicalPageBox)
    {
      final LogicalPageBox lbox = (LogicalPageBox) box;
      printBox(lbox.getFooterArea(), level + 1);
    }
  }

  private static void printChilds(final RenderBox box, final int level)
  {
    RenderNode childs = box.getFirstChild();
    while (childs != null)
    {
      if (childs instanceof RenderBox)
      {
        printBox((RenderBox) childs, level + 1);
      }
      else if (childs instanceof RenderableText)
      {
        printText((RenderableText) childs, level + 1);
      }
      else
      {
        printNode(childs, level + 1);
      }
      childs = childs.getNext();
    }
  }

  private static void printNode(final RenderNode node, final int level)
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append(node.getClass().getName());
    b.append('[');
    //b.append(Integer.toHexString(System.identityHashCode(node)));
    b.append(']');
    b.append("={x=");
    b.append(node.getX());
    b.append(", y=");
    b.append(node.getY());
    b.append(", width=");
    b.append(node.getWidth());
    b.append(", height=");
    b.append(node.getHeight());
    b.append(", computed-x=");
    b.append(node.getComputedX());
    b.append(", computed-width=");
    b.append(node.getComputedWidth());

    if (node instanceof FinishedRenderNode)
    {
      final FinishedRenderNode fn = (FinishedRenderNode) node;
      b.append(", layouted-width=");
      b.append(fn.getLayoutedWidth());
      b.append(", layouted-height=");
      b.append(fn.getLayoutedHeight());
    }
    b.append('}');
    Log.debug(b.toString());


    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(node.getNodeLayoutProperties());
    Log.debug(b.toString());
  }

  private static void printText(final RenderableText text, final int level)
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("Text");
    b.append('[');
    //b.append(Integer.toHexString(System.identityHashCode(text)));
    b.append(']');
    b.append("={x=");
    b.append(text.getX());
    b.append(", y=");
    b.append(text.getY());
    b.append(", width=");
    b.append(text.getWidth());
    b.append(", height=");
    b.append(text.getHeight());
    b.append(", computed-x=");
    b.append(text.getComputedX());
    b.append(", computed-width=");
    b.append(text.getComputedWidth());
    b.append(", text='");
    b.append(text.getRawText());
    b.append("'}");
    Log.debug(b.toString());

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(text.getNodeLayoutProperties());
    Log.debug(b.toString());
  }

}
