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
 * VerticalLayout.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.config;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

/**
 * A simple layout manager, which aligns all components in a vertical flow layout.
 *
 * @author Thomas Morgner
 */
public class VerticalLayout implements LayoutManager
{
  /**
   * Defines, whether to use the parents size or whether to compute the size from the
   * parent's childs during the layouting.
   */
  private final boolean useSizeFromParent;

  /**
   * DefaultConstructor.
   */
  public VerticalLayout ()
  {
    this(true);
  }

  /**
   * Creates a new vertical layout. If useParent is set to true, the parents size will be
   * used when performing the layouting, else only the parents childs are used to compute
   * the layout.
   *
   * @param useParent defines, whether the parent's size is used.
   */
  public VerticalLayout (final boolean useParent)
  {
    this.useSizeFromParent = useParent;
  }

  /**
   * Adds the specified component with the specified name to the layout.
   *
   * @param name the component name
   * @param comp the component to be added
   */
  public void addLayoutComponent (final String name, final Component comp)
  {
  }

  /**
   * Removes the specified component from the layout.
   *
   * @param comp the component to be removed
   */
  public void removeLayoutComponent (final Component comp)
  {
  }

  /**
   * Calculates the preferred size dimensions for the specified panel given the components
   * in the specified parent container.
   *
   * @param parent the component to be laid out
   * @return the preferred layout size
   *
   * @see #minimumLayoutSize
   */
  public Dimension preferredLayoutSize (final Container parent)
  {
    synchronized (parent.getTreeLock())
    {
      final Insets ins = parent.getInsets();
      final Component[] comps = parent.getComponents();
      int height = 0;
      int width = 0;
      for (int i = 0; i < comps.length; i++)
      {
        if (comps[i].isVisible() == false)
        {
          continue;
        }
        final Dimension pref = comps[i].getPreferredSize();
        height += pref.height;
        if (pref.width > width)
        {
          width = pref.width;
        }
      }
//      Log.debug ("PreferredSize in VLayout: " + new Dimension(width + ins.left + ins.right,
//          height + ins.top + ins.bottom));
      return new Dimension(width + ins.left + ins.right,
              height + ins.top + ins.bottom);
    }
  }

  /**
   * Calculates the minimum size dimensions for the specified panel given the components
   * in the specified parent container.
   *
   * @param parent the component to be laid out
   * @return the minimul layoutsize
   *
   * @see #preferredLayoutSize
   */
  public Dimension minimumLayoutSize (final Container parent)
  {
    synchronized (parent.getTreeLock())
    {
      final Insets ins = parent.getInsets();
      final Component[] comps = parent.getComponents();
      int height = 0;
      int width = 0;
      for (int i = 0; i < comps.length; i++)
      {
        if (comps[i].isVisible() == false)
        {
          continue;
        }
        final Dimension min = comps[i].getMinimumSize();
        height += min.height;
        if (min.width > width)
        {
          width = min.width;
        }
      }
      return new Dimension(width + ins.left + ins.right,
              height + ins.top + ins.bottom);
    }
  }

  /**
   * Returns, whether the parent's defined size is used during the layouting, or whether
   * the childs are used to compute the size.
   *
   * @return true, if the parent's size is used, false otherwise.
   */
  public boolean isUseSizeFromParent ()
  {
    return useSizeFromParent;
  }

  /**
   * Lays out the container in the specified panel.
   *
   * @param parent the component which needs to be laid out
   */
  public void layoutContainer (final Container parent)
  {
    synchronized (parent.getTreeLock())
    {
      final Insets ins = parent.getInsets();
      final int insHorizontal = ins.left + ins.right;

      final int width;
      if (isUseSizeFromParent())
      {
        final Rectangle bounds = parent.getBounds();
        width = bounds.width - insHorizontal;
      }
      else
      {
        width = preferredLayoutSize(parent).width - insHorizontal;
      }
      final Component[] comps = parent.getComponents();

      //final int x = bounds.x + ins.left;
      int y = /*bounds.y + */ ins.top;

      for (int i = 0; i < comps.length; i++)
      {
        final Component c = comps[i];
        if (c.isVisible() == false)
        {
          continue;
        }
        final Dimension dim = c.getPreferredSize();
        c.setBounds(0, y, width, dim.height);
        y += dim.height;
      }
    }
  }
}
