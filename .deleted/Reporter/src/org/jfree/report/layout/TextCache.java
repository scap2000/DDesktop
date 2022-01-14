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
 * TextCache.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

import java.util.HashMap;

import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.InstanceID;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 26.04.2007, 20:23:40
 *
 * @author Thomas Morgner
 */
public class TextCache
{
  public static class Result
  {
    private RenderNode[] text;
    private RenderNode[] finish;
    private StyleSheet styleSheet;

    public Result(final RenderNode[] text, final RenderNode[] finish,
                  final StyleSheet styleSheet)
    {
      this.styleSheet = styleSheet;
      this.text = (RenderNode[]) text.clone();
      this.finish = (RenderNode[]) finish.clone();
    }

    public StyleSheet getStyleSheet()
    {
      return styleSheet;
    }

    public RenderNode[] getText()
    {
      final RenderNode[] nodes = (RenderNode[]) text.clone();
      for (int i = 0; i < nodes.length; i++)
      {
        final RenderNode node = nodes[i];
        nodes[i] = node.derive(true);
      }
      return nodes;
    }

    public RenderNode[] getFinish()
    {
      final RenderNode[] nodes = (RenderNode[]) finish.clone();
      for (int i = 0; i < nodes.length; i++)
      {
        final RenderNode node = nodes[i];
        nodes[i] = node.derive(true);
      }
      return nodes;
    }
  }

  private static class InternalResult extends Result
  {
    private long changeTracker;
    private String originalText;

    protected InternalResult(final RenderNode[] text, final RenderNode[] finish,
                             final StyleSheet styleSheet,
                             final long changeTracker, final String originalText)
    {
      super(text, finish, styleSheet);
      this.changeTracker = changeTracker;
      this.originalText = originalText;
    }

    public boolean isValid(final long changeTracker, final String text)
    {
      if (changeTracker != this.changeTracker)
      {
        return false;
      }
      return ObjectUtilities.equal(text, originalText);
    }
  }

  private HashMap cache;

  public TextCache()
  {
    cache = new HashMap();
  }

  public void store(final InstanceID instanceID, final long changeTracker, final String originalText,
                    final StyleSheet styleSheet, final RenderNode[] text, final RenderNode[] finish)
  {
    cache.put (instanceID, new InternalResult(text, finish, styleSheet, changeTracker, originalText));
  }

  public Result get (final InstanceID instanceID, final long changeTracker, final String originalText)
  {
    final InternalResult o = (InternalResult) cache.get(instanceID);
    if (o == null)
    {
      return null;
    }
    if (o.isValid(changeTracker, originalText) == false)
    {
      cache.remove(instanceID);
      return null;
    }
    return o;
  }

}
