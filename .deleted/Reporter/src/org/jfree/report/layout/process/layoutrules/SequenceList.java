package org.jfree.report.layout.process.layoutrules;

import java.util.ArrayList;

import org.jfree.report.layout.model.RenderNode;

/**
 * Creation-Date: 17.07.2007, 11:23:20
 *
 * @author Thomas Morgner
 */
public class SequenceList
{
  private ArrayList nodeList;
  private ArrayList sequenceElementList;

  public SequenceList()
  {
    this(50);
  }

  public SequenceList(final int initialSize)
  {
    this.nodeList = new ArrayList(initialSize);
    this.sequenceElementList = new ArrayList(initialSize);
  }

  public RenderNode getNode(final int index)
  {
    return (RenderNode) nodeList.get(index);
  }

  public InlineSequenceElement getSequenceElement (final int index)
  {
    return (InlineSequenceElement) sequenceElementList.get(index);
  }

  public void clear()
  {
    this.nodeList.clear();
    this.sequenceElementList.clear();
  }


  public void add(final InlineSequenceElement element, final RenderNode node)
  {
    if (element == null || node == null)
    {
      throw new NullPointerException();
    }

    this.sequenceElementList.add(element);
    this.nodeList.add(node);
  }

  public int size()
  {
    return nodeList.size();
  }

  public InlineSequenceElement[] getSequenceElements(final InlineSequenceElement[] target)
  {
    return (InlineSequenceElement[]) sequenceElementList.toArray(target);
  }

  public RenderNode[] getNodes(final RenderNode[] target)
  {
    return (RenderNode[]) nodeList.toArray(target);
  }
}
