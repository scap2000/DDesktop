package org.jfree.report.layout.process;

import org.jfree.report.layout.model.ParagraphRenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.process.layoutrules.InlineNodeSequenceElement;
import org.jfree.report.layout.process.layoutrules.InlineSequenceElement;
import org.jfree.report.layout.process.layoutrules.ReplacedContentSequenceElement;
import org.jfree.report.layout.process.layoutrules.SequenceList;
import org.jfree.report.layout.process.layoutrules.TextSequenceElement;

/**
 * Creation-Date: 12.07.2007, 14:47:54
 *
 * @author Thomas Morgner
 */
public final class MinorAxisParagraphBreakState
{
  private Object suspendItem;
  private SequenceList primarySequence;
  private ParagraphRenderBox paragraph;
  private boolean containsContent;

  public MinorAxisParagraphBreakState()
  {
    this.primarySequence = new SequenceList();
  }

  public void init(final ParagraphRenderBox paragraph)
  {
    if (paragraph == null)
    {
      throw new NullPointerException();
    }
    this.paragraph = paragraph;
    this.primarySequence.clear();
    this.suspendItem = null;
    this.containsContent = false;
  }

  public void deinit()
  {
    this.paragraph = null;
    this.primarySequence.clear();
    this.suspendItem = null;
    this.containsContent = false;
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

  public void add(final InlineSequenceElement element, final RenderNode node)
  {
    primarySequence.add(element, node);
    if (element instanceof TextSequenceElement ||
        element instanceof InlineNodeSequenceElement ||
        element instanceof ReplacedContentSequenceElement)
    {
      containsContent = true;
    }
  }

  public boolean isContainsContent()
  {
    return containsContent;
  }

  public boolean isSuspended()
  {
    return suspendItem != null;
  }

  public SequenceList getSequence ()
  {
    return primarySequence;
  }

  public void clear()
  {
    primarySequence.clear();
    containsContent = false;
  }
}
