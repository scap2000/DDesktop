package org.jfree.report.layout.process;

import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.process.valign.BoxAlignContext;
import org.jfree.util.FastStack;

/**
 * Creation-Date: 12.07.2007, 14:16:07
 *
 * @author Thomas Morgner
 */
public final class MajorAxisParagraphBreakState
{
  private BoxAlignContext currentLine;

  private Object suspendItem;
  private FastStack contexts;
  private ParagraphRenderBox paragraph;

  /**
   */
  public MajorAxisParagraphBreakState()
  {
    this.contexts = new FastStack();
  }

  public void init(final ParagraphRenderBox paragraph)
  {
    if (paragraph == null)
    {
      throw new NullPointerException();
    }
    this.paragraph = paragraph;
    this.contexts.clear();
    this.suspendItem = null;
  }

  public void deinit()
  {
    this.paragraph = null;
    this.suspendItem = null;
    this.contexts.clear();
  }

  public boolean isActive()
  {
    return paragraph != null;
  }

  public ParagraphRenderBox getParagraph()
  {
    return paragraph;
  }

  public Object getSuspendItem()
  {
    return suspendItem;
  }

  public void setSuspendItem(final Object suspendItem)
  {
    this.suspendItem = suspendItem;
  }

  public boolean isSuspended()
  {
    return suspendItem != null;
  }

  public BoxAlignContext getCurrentLine()
  {
    return currentLine;
  }

  public void openContext(final BoxAlignContext context)
  {
    if (currentLine != null)
    {
      currentLine.addChild(context);
    }
    contexts.push(context);
    currentLine = context;
  }

  public BoxAlignContext closeContext()
  {
    final BoxAlignContext context = (BoxAlignContext) contexts.pop();
    if (contexts.isEmpty())
    {
      currentLine = null;
    }
    else
    {
      currentLine = (BoxAlignContext) contexts.peek();
    }
    return context;
  }
}
