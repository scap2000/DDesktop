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
package org.pentaho.reportdesigner.lib.client.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

/**
 * User: Martin
 * Date: 25.12.2004
 * Time: 11:15:41
 */
public class CooltipManager
{
    @NotNull
    private BufferedImage image;
    @NotNull
    private TipWindow window;

    @NotNull
    private ImagePanel imagePanel = new ImagePanel();
    @NotNull
    private static final Integer TOOLTIP_LAYER = new Integer(290);

    @NotNull
    private CellRendererPane cellRendererPane;

    private boolean windowEnabled;


    public CooltipManager()
    {
        cellRendererPane = new CellRendererPane();
        windowEnabled = true;
    }


    public boolean isWindowEnabled()
    {
        return windowEnabled;
    }


    public void setWindowEnabled(boolean windowEnabled)
    {
        this.windowEnabled = windowEnabled;
    }


    public void registerComponent(@NotNull final JComponent component)
    {
        component.add(cellRendererPane);

        component.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseMoved(@NotNull MouseEvent e)
            {
                processCooltip(e);
            }


            public void mouseDragged(@NotNull MouseEvent e)
            {
                processCooltip(e);
            }
        });

        component.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                processCooltip(e);
            }


            public void mousePressed(@NotNull MouseEvent e)
            {
                processCooltip(e);
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                processCooltip(e);
            }


            public void mouseEntered(@NotNull MouseEvent e)
            {
                processCooltip(e);
            }


            public void mouseExited(@NotNull MouseEvent e)
            {
                processCooltip(e);
                removeImagePanel();
                getTipWindow((JComponent) e.getComponent()).setVisible(false);
            }
        });

        component.addKeyListener(new KeyAdapter()
        {
            public void keyTyped(@NotNull KeyEvent e)
            {
                removeImagePanel();
                if (getTipWindow(component).isVisible())
                {
                    getTipWindow(component).setVisible(false);
                }
            }


            public void keyPressed(@NotNull KeyEvent e)
            {
                removeImagePanel();
                if (getTipWindow(component).isVisible())
                {
                    getTipWindow(component).setVisible(false);
                }
            }


            public void keyReleased(@NotNull KeyEvent e)
            {
                removeImagePanel();
                if (getTipWindow(component).isVisible())
                {
                    getTipWindow(component).setVisible(false);
                }
            }
        });

        if (component instanceof JList)
        {
            JList list = (JList) component;
            list.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(@NotNull ListSelectionEvent e)
                {
                    removeImagePanel();
                    if (getTipWindow(component).isVisible())
                    {
                        getTipWindow(component).setVisible(false);
                    }
                }
            });
        }
        else if (component instanceof JTree)
        {
            JTree tree = (JTree) component;
            tree.addTreeSelectionListener(new TreeSelectionListener()
            {
                public void valueChanged(@NotNull TreeSelectionEvent e)
                {
                    removeImagePanel();
                    if (getTipWindow(component).isVisible())
                    {
                        getTipWindow(component).setVisible(false);
                    }
                }
            });
        }

        AdjustmentListener al = new AdjustmentListener()
        {
            public void adjustmentValueChanged(@NotNull AdjustmentEvent e)
            {
                removeImagePanel();
                if (getTipWindow(component).isVisible())
                {
                    getTipWindow(component).setVisible(false);
                }
            }
        };
        Container parent = component;
        while ((parent = parent.getParent()) != null)
        {
            if (parent instanceof JScrollPane)
            {
                JScrollPane parentScrollPane = (JScrollPane) parent;
                parentScrollPane.getVerticalScrollBar().addAdjustmentListener(al);
                parentScrollPane.getHorizontalScrollBar().addAdjustmentListener(al);
            }
        }
    }


    @NotNull
    private JComponent lastComponent;
    private int lastX;
    private int lastY;


    private void processCooltip(@Nullable MouseEvent e)
    {
        JComponent source;
        if (e != null)
        {
            lastComponent = (JComponent) e.getComponent();
            source = lastComponent;
            lastX = e.getX();
            lastY = e.getY();
        }
        else
        {
            if (lastComponent == null)
            {
                return;
            }
            else
            {
                source = lastComponent;
            }
        }

        //search the topmost rootpane
        JRootPane rootPane = null;
        Component comp = source;
        while ((comp = comp.getParent()) != null)
        {
            if (comp instanceof JRootPane)
            {
                rootPane = (JRootPane) comp;
            }
        }

        removeImagePanel();

        int x = lastX;
        int y = lastY;


        Rectangle pathRect = getDesiredVisibleRect(source, x, y);

        if (pathRect == null)
        {
            if (getTipWindow(source).isVisible())
            {
                getTipWindow(source).setVisible(false);
            }
            return;
        }

        Component cellRenderer = null;

        if (useCellRenderer(source))
        {
            cellRenderer = getCellRenderer(source, x, y);
            if (cellRenderer != null)
            {
                Dimension ps = cellRenderer.getPreferredSize();
                pathRect.width = ps.width;
            }
        }

        Rectangle visibleTreeRect = source.getVisibleRect();
        convertRectangleToScreen(visibleTreeRect, source);

        Rectangle pathRectOnScreen = new Rectangle(pathRect);
        convertRectangleToScreen(pathRectOnScreen, source);

        int missingWidth = (pathRectOnScreen.x + pathRectOnScreen.width) - (visibleTreeRect.x + visibleTreeRect.width);
        Rectangle missingRectOnScreen = new Rectangle();

        if (missingWidth > 0)
        {
            missingRectOnScreen.x = (pathRectOnScreen.x + pathRectOnScreen.width) - missingWidth;
            missingRectOnScreen.y = pathRectOnScreen.y;
            missingRectOnScreen.width = missingWidth;
            missingRectOnScreen.height = pathRectOnScreen.height;
        }
        else
        {
            removeImagePanel();
            if (getTipWindow(source).isVisible())
            {
                getTipWindow(source).setVisible(false);
            }
            return;
        }

        Rectangle layeredPaneBoundsOnScreen;


        if (rootPane != null)
        {
            layeredPaneBoundsOnScreen = rootPane.getLayeredPane().getBounds();
            convertRectangleToScreen(layeredPaneBoundsOnScreen, rootPane.getLayeredPane());

            if (image == null || image.getWidth() != missingRectOnScreen.width || image.getHeight() != missingRectOnScreen.height)
            {
                image = new BufferedImage(missingRectOnScreen.width, missingRectOnScreen.height, BufferedImage.TYPE_INT_ARGB);
            }


            Graphics g = image.getGraphics();

            g.fillRect(-1, 0, missingRectOnScreen.width, missingRectOnScreen.height - 1);

            if (useCellRenderer(source))
            {
                if (cellRenderer != null)
                {
                    Dimension preferredSize = cellRenderer.getPreferredSize();
                    cellRendererPane.paintComponent(g, cellRenderer, source, -(preferredSize.width - missingWidth), 0,
                                                    pathRect.width, missingRectOnScreen.height, true);
                }

            }
            else
            {
                g.translate(-(pathRect.x + pathRect.width - missingRectOnScreen.width), -pathRect.y);
                g.setClip(pathRect.x + pathRect.width - missingRectOnScreen.width, pathRect.y, missingRectOnScreen.width, missingRectOnScreen.height);
                source.paint(g);
                g.translate(pathRect.x + pathRect.width - missingRectOnScreen.width, pathRect.y);
            }

            g.setColor(Color.BLACK);
            g.drawRect(-1, 0, missingRectOnScreen.width, missingRectOnScreen.height - 1);
            //Display whole missing stuff on layered pane
            imagePanel.setImage(image);
            rootPane.getLayeredPane().add(imagePanel, CooltipManager.TOOLTIP_LAYER, 0);
            Point p = missingRectOnScreen.getLocation();
            SwingUtilities.convertPointFromScreen(p, rootPane.getLayeredPane());
            imagePanel.setBounds(p.x, p.y, missingRectOnScreen.width, missingRectOnScreen.height);

            //rootPane.getLayeredPane().repaint();//SLOOOOOOOOOOW!!!!

            //Display stuff not fitting on layeredPane
            int widthAlreadyOnLayeredPane = (layeredPaneBoundsOnScreen.x + layeredPaneBoundsOnScreen.width) - missingRectOnScreen.x;

            if (widthAlreadyOnLayeredPane < missingRectOnScreen.width)
            {
                //still needs a window
                getTipWindow(source).setImage(image.getSubimage(widthAlreadyOnLayeredPane, 0, image.getWidth() - widthAlreadyOnLayeredPane, image.getHeight()));
                getTipWindow(source).setLocation(missingRectOnScreen.x + widthAlreadyOnLayeredPane, missingRectOnScreen.y);
                getTipWindow(source).setSize(image.getWidth() - widthAlreadyOnLayeredPane, image.getHeight());

                boolean windowTemporaryDisabled = false;

                if (!SwingUtilities.getWindowAncestor(source).isActive())
                {
                    windowTemporaryDisabled = true;
                }
                else
                {
                    Window[] windows = SwingUtilities.getWindowAncestor(source).getOwnedWindows();
                    for (Window window1 : windows)
                    {
                        String name = window1.getClass().getName();
                        if ("javax.swing.Popup$HeavyWeightWindow".equals(name))//NON-NLS
                        {
                            if (window1.isShowing())
                            {
                                windowTemporaryDisabled = true;
                            }
                        }
                    }
                }

                if (!getTipWindow(source).isVisible() && windowEnabled && !windowTemporaryDisabled)
                {
                    getTipWindow(source).setVisible(true);
                }
            }
            else
            {
                //no window required
                if (getTipWindow(source).isVisible())
                {
                    getTipWindow(source).setVisible(false);
                }
            }
        }
        else
        {
            //TODO display as popup-->not really used yet as JFrame, JDialog and JWindow have rootpanes. We can probably remove this whole branch.
        }

        getTipWindow(source).repaint();
    }


    @Nullable
    private Component getCellRenderer(@NotNull JComponent source, int x, int y)
    {
        if (source instanceof JTree)
        {
            JTree tree = (JTree) source;
            TreePath treePath = tree.getPathForLocation(x, y);
            int row = tree.getRowForPath(treePath);
            if (treePath != null)
            {

                Component cellRenderer = tree.getCellRenderer().getTreeCellRendererComponent(tree,
                                                                                             treePath.getLastPathComponent(),
                                                                                             tree.isRowSelected(row),
                                                                                             tree.isExpanded(treePath),
                                                                                             tree.getModel().isLeaf(treePath.getLastPathComponent()),
                                                                                             row,
                                                                                             tree.isFocusOwner() && tree.isRowSelected(row));

                return cellRenderer;
            }

        }
        else if (source instanceof JList)
        {
            JList list = (JList) source;
            Point location = new Point(x, y);
            int index = list.locationToIndex(location);
            Rectangle bounds = list.getCellBounds(index, index);
            if (!bounds.contains(location))
            {
                return null;
            }

            Component cellRenderer = list.getCellRenderer().getListCellRendererComponent(list,
                                                                                         list.getModel().getElementAt(index),
                                                                                         index,
                                                                                         list.getSelectedIndex() == index,
                                                                                         list.isFocusOwner() && list.getSelectedIndex() == index);

            return cellRenderer;
        }
        else if (source instanceof JTable)
        {
            JTable table = (JTable) source;
            Point point = new Point(x, y);
            int row = table.rowAtPoint(point);
            int column = table.columnAtPoint(point);
            if (row == -1 || column == -1)
            {
                return null;
            }
            Component cellRenderer = table.getCellRenderer(row, column).getTableCellRendererComponent(table,
                                                                                                      table.getValueAt(row, column),
                                                                                                      table.isCellSelected(row, column),
                                                                                                      table.isFocusOwner(),
                                                                                                      row,
                                                                                                      column);
            return cellRenderer;
        }

        return null;

    }


    private boolean useCellRenderer(@NotNull JComponent source)
    {
        return (source instanceof JTree) || (source instanceof JList);
    }


    @Nullable
    private Rectangle getDesiredVisibleRect(@NotNull JComponent source, int x, int y)
    {
        if (source instanceof JTree)
        {
            JTree tree = (JTree) source;
            TreePath treePath = tree.getPathForLocation(x, y);
            Rectangle pathRect = tree.getPathBounds(treePath);
            return pathRect;
        }
        else if (source instanceof JList)
        {
            JList list = (JList) source;
            Point location = new Point(x, y);
            int index = list.locationToIndex(location);
            Rectangle bounds = list.getCellBounds(index, index);
            if (bounds == null || !bounds.contains(location))
            {
                return null;
            }
            return bounds;
        }
        else if (source instanceof JTable)
        {
            JTable table = (JTable) source;
            Point point = new Point(x, y);
            int row = table.rowAtPoint(point);
            int column = table.columnAtPoint(point);
            if (row == -1 || column == -1)
            {
                return null;
            }
            Rectangle bounds = table.getCellRect(row, column, false);
            return bounds;
        }

        return null;
    }


    private void convertRectangleToScreen(@NotNull Rectangle rect, @NotNull Component c)
    {
        Point p = rect.getLocation();
        SwingUtilities.convertPointToScreen(p, c);
        rect.setLocation(p);
    }


    private void removeImagePanel()
    {
        Container container = imagePanel.getParent();
        if (container != null)
        {
            Rectangle bounds = imagePanel.getBounds();
            container.remove(imagePanel);
            container.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }


    @NotNull
    protected TipWindow getTipWindow(@NotNull JComponent source)
    {
        if (this.window == null)
        {
            Window w = SwingUtilities.getWindowAncestor(source);
            this.window = new TipWindow(w);
        }
        return this.window;
    }


    private static class ImagePanel extends JPanel
    {
        @NotNull
        private Image image;


        private ImagePanel()
        {
            super();
        }


        public void setImage(@NotNull Image image)
        {
            this.image = image;
        }


        protected void paintComponent(@NotNull Graphics g)
        {
            if (image != null)
            {
                g.drawImage(image, 0, 0, null);
            }
        }
    }

    private static class TipWindow extends JWindow
    {
        @Nullable
        private BufferedImage bi;


        private TipWindow(@NotNull Window owner)
        {
            super(owner);
            getContentPane().setLayout(null);
        }


        public void setImage(@Nullable BufferedImage bi)
        {
            this.bi = bi;
            if (bi != null)
            {
                setSize(bi.getWidth(), bi.getHeight());
            }
        }


        public void paint(@NotNull Graphics g)
        {
            if (bi != null)
            {
                g.drawImage(bi, 0, 0, null);
            }
        }
    }

}


