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
 * ConfigTreeRootNode.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.config.model;

import javax.swing.tree.TreeNode;

/**
 * The root node contains the local and the global node and is the main entry point into
 * the tree.
 *
 * @author Thomas Morgner
 */
public class ConfigTreeRootNode extends AbstractConfigTreeNode
{
  /**
   * Creates a new root node with the given name.
   *
   * @param name the name of the node.
   */
  public ConfigTreeRootNode (final String name)
  {
    super(name);
  }

  /**
   * Returns the parent <code>TreeNode</code> of the receiver.
   *
   * @return always null, as the root node never has a parent.
   */
  public TreeNode getParent ()
  {
    return null;
  }

}
