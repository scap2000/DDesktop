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
 * FileModelPrinter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.jfree.report.layout.model.FinishedRenderNode;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableText;

/**
 * Creation-Date: Jan 9, 2007, 2:22:59 PM
 *
 * @author Thomas Morgner
 */
public class FileModelPrinter
{
  private static final boolean PRINT_LINEBOX_CONTENTS = false;

  private FileModelPrinter()
  {
  }

  public static void print(final RenderBox box)
  {
    try
    {
      final File file = File.createTempFile("model-dump-", ".txt");
      final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
      printBox(writer, box, 0);
      writer.close();
    }
    catch(Exception e)
    {
      throw new Error();
    }
  }

  public static void printBox(final Writer writer, final RenderBox box, final int level)
      throws Exception
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
    writer.write(b.toString());
    writer.write("\n");

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- boxDefinition=");
    b.append(box.getBoxDefinition());
    writer.write(b.toString());
    writer.write("\n");

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(box.getNodeLayoutProperties());
    writer.write(b.toString());
    writer.write("\n");

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- staticBoxLayoutProperties=");
    b.append(box.getStaticBoxLayoutProperties());
    writer.write(b.toString());
    writer.write("\n");

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
      writer.write(b.toString());
      writer.write("\n");
    }

    if (box.isOpen())
    {
      b = new StringBuffer();
      for (int i = 0; i < level; i++)
      {
        b.append("   ");
      }
      b.append("- WARNING: THIS BOX IS STILL OPEN");
      writer.write(b.toString());
      writer.write("\n");
    }

    if (box.isFinished())
    {
      b = new StringBuffer();
      for (int i = 0; i < level; i++)
      {
        b.append("   ");
      }
      b.append("- INFO: THIS BOX IS FINISHED");
      writer.write(b.toString());
      writer.write("\n");
    }
    if (box.isCommited())
    {
      b = new StringBuffer();
      for (int i = 0; i < level; i++)
      {
        b.append("   ");
      }
      b.append("- INFO: THIS BOX IS COMMITED");
      writer.write(b.toString());
      writer.write("\n");
    }

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    writer.write(b.toString());
    writer.write("\n");

    if (box instanceof ParagraphRenderBox)
    {
      if (PRINT_LINEBOX_CONTENTS)
      {
        final ParagraphRenderBox paraBox = (ParagraphRenderBox) box;
        if (paraBox.isComplexParagraph())
        {
          writer.write("----------------  START PARAGRAPH LINEBOX CONTAINER -------------------------------------");
          writer.write("\n");
          printBox(writer, paraBox.getLineboxContainer(), level + 1);
          writer.write ("---------------- FINISH PARAGRAPH LINEBOX CONTAINER -------------------------------------");
          writer.write("\n");
        }
      }
    }

    if (box instanceof LogicalPageBox)
    {
      final LogicalPageBox lbox = (LogicalPageBox) box;
      printBox(writer, lbox.getHeaderArea(), level + 1);
      printBox(writer, lbox.getWatermarkArea(), level + 1);
    }
    printChilds(writer, box, level);
    if (box instanceof LogicalPageBox)
    {
      final LogicalPageBox lbox = (LogicalPageBox) box;
      printBox(writer, lbox.getFooterArea(), level + 1);
    }
  }

  private static void printChilds(final Writer writer, final RenderBox box, final int level)
      throws Exception
  {
    RenderNode childs = box.getFirstChild();
    while (childs != null)
    {
      if (childs instanceof RenderBox)
      {
        printBox(writer, (RenderBox) childs, level + 1);
      }
      else if (childs instanceof RenderableText)
      {
        printText(writer, (RenderableText) childs, level + 1);
      }
      else
      {
        printNode(writer, childs, level + 1);
      }
      childs = childs.getNext();
    }
  }

  private static void printNode(final Writer writer, final RenderNode node, final int level) throws IOException
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
    writer.write(b.toString());
    writer.write("\n");


    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(node.getNodeLayoutProperties());
    writer.write(b.toString());
    writer.write("\n");
  }

  private static void printText(final Writer writer, final RenderableText text, final int level) throws IOException
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
    writer.write(b.toString());
    writer.write("\n");

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(text.getNodeLayoutProperties());
    writer.write(b.toString());
    writer.write("\n");
  }

}
