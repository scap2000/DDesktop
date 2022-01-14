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
package org.pentaho.reportdesigner.crm.report.commands.debug;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * User: Martin
 * Date: 20.02.2006
 * Time: 19:30:01
 */
public class DebugComponentTreeCommand extends AbstractCommand
{

    public DebugComponentTreeCommand()
    {
        super("DebugComponentTreeCommand");
        getTemplatePresentation().setText("DebugComponentTreeCommand");//NON-NLS
    }


    public void update(@NotNull CommandEvent event)
    {
        event.getPresentation().setEnabled(true);
    }


    public void execute(@NotNull CommandEvent event)
    {
        final ReportDialog reportDialog = (ReportDialog) event.getPresentation().getCommandApplicationRoot();

        final JLayeredPane jLayeredPane = reportDialog.getRootPane().getLayeredPane();

        final JComponent mouseComponent = new JComponent()
        {

            protected void paintComponent(@NotNull Graphics g)
            {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 255, 30));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };


        mouseComponent.setOpaque(false);

        mouseComponent.setBounds(0, 0, reportDialog.getWidth(), reportDialog.getHeight());
        jLayeredPane.add(mouseComponent);
        jLayeredPane.setLayer(mouseComponent, JLayeredPane.POPUP_LAYER.intValue());

        final JLabel highlightLabel = new JLabel();
        highlightLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        jLayeredPane.add(highlightLabel);
        jLayeredPane.setLayer(highlightLabel, JLayeredPane.POPUP_LAYER.intValue() + 1);

        jLayeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "turnOff");//NON-NLS
        jLayeredPane.getActionMap().put("turnOff", new AbstractAction()//NON-NLS
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                jLayeredPane.remove(mouseComponent);
                jLayeredPane.remove(highlightLabel);
                jLayeredPane.getInputMap().remove(KeyStroke.getKeyStroke("ESCAPE"));//NON-NLS
                jLayeredPane.getActionMap().remove("turnOff");//NON-NLS

                reportDialog.getRootPane().revalidate();
                reportDialog.getRootPane().repaint();
            }
        });

        mouseComponent.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseMoved(@NotNull MouseEvent e)
            {
                Point p = new Point(e.getX(), e.getY());
                SwingUtilities.convertPointToScreen(p, mouseComponent);
                SwingUtilities.convertPointFromScreen(p, reportDialog.getRootPane());

                Component deepestComponentAt = getDeepestComponent(e, mouseComponent, highlightLabel, reportDialog);
                Rectangle rectangle = SwingUtilities.convertRectangle(deepestComponentAt.getParent(), deepestComponentAt.getBounds(), reportDialog.getRootPane());
                highlightLabel.setBounds(rectangle);
            }
        });

        mouseComponent.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                Component deepestComponent = getDeepestComponent(e, mouseComponent, highlightLabel, reportDialog);

                DefaultMutableTreeNode node = new DefaultMutableTreeNode(deepestComponent);
                DefaultMutableTreeNode child = node;

                while ((deepestComponent = deepestComponent.getParent()) != null)
                {
                    DefaultMutableTreeNode n = new DefaultMutableTreeNode(deepestComponent);
                    n.add(node);
                    node = n;
                }

                JTree tree = new JTree(node);
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) child.getParent();
                tree.expandPath(new TreePath(parent.getPath()));
                JScrollPane pane = new JScrollPane(tree);
                pane.setPreferredSize(new Dimension(600, 400));
                JOptionPane.showMessageDialog(reportDialog, pane);
            }
        });
    }


    @NotNull
    private Component getDeepestComponent(@NotNull MouseEvent e, @NotNull JComponent mouseComponent, @NotNull JLabel highlightLabel, @NotNull ReportDialog reportDialog)
    {
        mouseComponent.setVisible(false);
        highlightLabel.setVisible(false);
        Component deepestComponentAt = SwingUtilities.getDeepestComponentAt(reportDialog.getRootPane(), e.getX(), e.getY());
        highlightLabel.setVisible(true);
        mouseComponent.setVisible(true);
        return deepestComponentAt;
    }


}
