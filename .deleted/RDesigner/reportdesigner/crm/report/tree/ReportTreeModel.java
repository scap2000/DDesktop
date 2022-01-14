/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * User: Martin
 * Date: 25.10.2005
 * Time: 10:43:08
 */
public class ReportTreeModel implements TreeModel, PropertyChangeListener
{
    @NotNull
    protected EventListenerList listenerList = new EventListenerList();

    @NotNull
    private Report report;


    public ReportTreeModel(@NotNull Report report)
    {
        this.report = report;

    }


    @NotNull
    public Report getRoot()
    {
        return report;
    }


    @Nullable
    public ReportElement getChild(@NotNull Object parent, int index)
    {
        if (parent instanceof ReportElement)
        {
            ReportElement reportElement = (ReportElement) parent;
            return reportElement.getChildren().get(index);
        }
        return null;
    }


    public int getChildCount(@NotNull Object parent)
    {
        if (parent instanceof ReportElement)
        {
            ReportElement reportElement = (ReportElement) parent;
            return reportElement.getChildren().size();
        }

        return 0;
    }


    public boolean isLeaf(@NotNull Object node)
    {
        if (node instanceof ReportElement)
        {
            ReportElement reportElement = (ReportElement) node;
            return reportElement.getChildren().isEmpty();
        }
        return false;
    }


    public void valueForPathChanged(@NotNull TreePath path, @NotNull Object newValue)
    {
    }


    public void propertyChange(@NotNull PropertyChangeEvent evt)
    {
    }


    @SuppressWarnings({"ObjectEquality"})
    public void nodeChanged(@Nullable ReportElement node)
    {
        if (listenerList != null && node != null)
        {
            ReportElement parent = node.getParent();

            if (parent != null)
            {
                int anIndex = parent.getChildren().indexOf(node);
                if (anIndex != -1)
                {
                    int[] cIndexs = new int[1];

                    cIndexs[0] = anIndex;
                    nodesChanged(parent, cIndexs);
                }
            }
            else if (node == getRoot())
            {
                nodesChanged(node, null);
            }
        }
    }


    @SuppressWarnings({"ObjectEquality"})
    public void nodesChanged(@Nullable ReportElement node, @Nullable int[] childIndices)
    {
        if (node != null)
        {
            if (childIndices != null)
            {
                int cCount = childIndices.length;

                if (cCount > 0)
                {
                    Object[] cChildren = new Object[cCount];

                    for (int counter = 0; counter < cCount; counter++)
                    {
                        cChildren[counter] = node.getChildren().get(childIndices[counter]);
                    }
                    fireTreeNodesChanged(this, getPathToRoot(node), childIndices, cChildren);
                }
            }
            else if (node == getRoot())
            {
                fireTreeNodesChanged(this, getPathToRoot(node), null, null);
            }
        }
    }


    @Nullable
    public ReportElement[] getPathToRoot(@Nullable ReportElement aNode)
    {
        return getPathToRoot(aNode, 0);
    }


    @Nullable
    @SuppressWarnings({"ObjectEquality"})
    protected ReportElement[] getPathToRoot(@Nullable ReportElement aNode, int depth)
    {
        ReportElement[] retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if (aNode == null)
        {
            if (depth == 0)
            {
                return null;
            }
            else
            {
                retNodes = new ReportElement[depth];
            }
        }
        else
        {
            depth++;
            if (aNode == getRoot())
            {
                retNodes = new ReportElement[depth];
            }
            else
            {
                retNodes = getPathToRoot(aNode.getParent(), depth);
            }
            //noinspection ConstantConditions
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }


    public int getIndexOfChild(@Nullable Object parent, @Nullable Object child)
    {
        if (parent instanceof ReportElement)
        {
            ReportElement reportElement = (ReportElement) parent;
            //noinspection SuspiciousMethodCalls
            return reportElement.getChildren().indexOf(child);
        }

        return -1;
    }


    public void addTreeModelListener(@NotNull TreeModelListener l)
    {
        listenerList.add(TreeModelListener.class, l);
    }


    public void removeTreeModelListener(@NotNull TreeModelListener l)
    {
        listenerList.remove(TreeModelListener.class, l);
    }


    @NotNull
    public TreeModelListener[] getTreeModelListeners()
    {
        return listenerList.getListeners(TreeModelListener.class);
    }


    protected void fireTreeNodesChanged(@NotNull Object source,
                                        @Nullable Object[] path,
                                        @Nullable int[] childIndices,
                                        @Nullable Object[] children)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            //noinspection ObjectEquality
            if (listeners[i] == TreeModelListener.class)
            {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }


    protected void fireTreeNodesInserted(@NotNull Object source,
                                         @Nullable Object[] path,
                                         @Nullable int[] childIndices,
                                         @Nullable Object[] children)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            //noinspection ObjectEquality
            if (listeners[i] == TreeModelListener.class)
            {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }


    protected void fireTreeNodesRemoved(@NotNull Object source,
                                        @Nullable Object[] path,
                                        @Nullable int[] childIndices,
                                        @Nullable Object[] children)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            //noinspection ObjectEquality
            if (listeners[i] == TreeModelListener.class)
            {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }


    protected void fireTreeStructureChanged(@NotNull Object source,
                                            @Nullable Object[] path,
                                            @Nullable int[] childIndices,
                                            @Nullable Object[] children)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            //noinspection ObjectEquality
            if (listeners[i] == TreeModelListener.class)
            {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }


    protected void fireTreeStructureChanged(@NotNull Object source, @Nullable TreePath path)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            //noinspection ObjectEquality
            if (listeners[i] == TreeModelListener.class)
            {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path);
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }
}
