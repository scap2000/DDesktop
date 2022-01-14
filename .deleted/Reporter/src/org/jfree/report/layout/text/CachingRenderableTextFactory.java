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
 * CachingRenderableTextFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.text;

import java.util.ArrayList;

import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;
import org.jfree.util.ObjectUtilities;

/**
 * This one does not work as expected.
 *
 * @author Thomas Morgner
 */
public class CachingRenderableTextFactory implements RenderableTextFactory
{
  public static class CacheEntry
  {
    private Object[] styleInformation;
    private RenderNode[] text;

    public CacheEntry(final Object[] styleKeys, final RenderNode[] text)
    {
      this.styleInformation = styleKeys;
      this.text = text;
    }

    public RenderNode[] getText()
    {
      final RenderNode[] textcopy = (RenderNode[]) text.clone();
      for (int i = 0; i < textcopy.length; i++)
      {
        textcopy[i] = textcopy[i].derive(true);
      }
      return textcopy;
    }

    public boolean isSameStyle(final StyleSheet styleSheet, final StyleKey[] styleKeys)
    {
      for (int i = 0; i < styleKeys.length; i++)
      {
        final StyleKey key = styleKeys[i];
        final Object value = styleInformation[key.getIdentifier()];
        if (value != CacheStyleSheet.UNDEFINED)
        {
          final Object styleProperty = styleSheet.getStyleProperty(key, null);
          if (ObjectUtilities.equal(styleProperty, value) == false)
          {
            return false;
          }
        }
      }
      return true;
    }
  }

  private RenderableTextFactory otherTextFactory;
  private int keyCount;
  private ArrayList values;
  private CacheStyleSheet cacheStyleSheet;

  public CachingRenderableTextFactory(final RenderableTextFactory otherTextFactory,
                                      final int keyCount)
  {
    this.otherTextFactory = otherTextFactory;
    this.keyCount = keyCount;
    this.values = new ArrayList();
  }

  public ArrayList getValues()
  {
    return values;
  }


  public void startText()
  {
    otherTextFactory.startText();
    values.clear();
  }

  /**
   * The text is given as CodePoints.
   *
   * @param text
   * @return
   */
  public RenderNode[] createText(final int[] text, final int offset, final int length, final StyleSheet layoutContext)
  {
    if (cacheStyleSheet == null)
    {
      cacheStyleSheet = new CacheStyleSheet(layoutContext, keyCount);
    }
    else
    {
      if (layoutContext != cacheStyleSheet.getParent())
      {
        throw new IllegalStateException();
      }
    }
    final RenderNode[] nodes = otherTextFactory.createText(text, offset, length, cacheStyleSheet);
    for (int i = 0; i < nodes.length; i++)
    {
      values.add(nodes[i].derive(true));
    }
    return nodes;
  }

  public RenderNode[] finishText()
  {
    final RenderNode[] nodes = otherTextFactory.finishText();
    for (int i = 0; i < nodes.length; i++)
    {
      values.add(nodes[i].derive(true));
    }
    return nodes;
  }

  public CacheEntry getCacheEntry()
  {
    final RenderNode[] objects = (RenderNode[]) values.toArray(new RenderNode[values.size()]);
    return new CacheEntry(cacheStyleSheet.getEntries(), objects);
  }

  public void clear()
  {
    values.clear();
    if (cacheStyleSheet != null)
    {
      cacheStyleSheet.lock();
    }
    cacheStyleSheet = null;
  }
}
