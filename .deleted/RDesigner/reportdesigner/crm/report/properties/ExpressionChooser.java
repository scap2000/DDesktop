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
package org.pentaho.reportdesigner.crm.report.properties;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.function.Expression;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 10.01.2006
 * Time: 14:50:30
 */
public class ExpressionChooser extends JPanel
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ExpressionChooser.class.getName());

    @NotNull
    private static final String ROOT = "root";

    @NotNull
    private JTree expressionsTree;


    public ExpressionChooser(@NotNull final ReportDialog reportDialog, @NotNull final Report report, @NotNull final ReportElementSelectionModel reportElementSelectionModel)
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(ROOT);

        Set<Class<?>> classes = ExpressionRegistry.getInstance().getJFreeReportExpressionToWrapperClassesMap().keySet();
        ArrayList<Class<?>> classesList = new ArrayList<Class<?>>(classes);
        Collections.sort(classesList, new Comparator<Class<?>>()
        {
            public int compare(@NotNull Class<?> o1, @NotNull Class<?> o2)
            {
                String functionName1 = ExpressionRegistry.getInstance().getLocalizedExpressionName(o1);
                String functionName2 = ExpressionRegistry.getInstance().getLocalizedExpressionName(o2);
                return functionName1.compareTo(functionName2);
            }
        });

        buildTree(root, classesList);

        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(root);
        expressionsTree = new JTree(defaultTreeModel);

        expressionsTree.setShowsRootHandles(true);

        expressionsTree.expandRow(0);

        expressionsTree.setCellRenderer(new DefaultTreeCellRenderer()
        {
            @NotNull
            public Component getTreeCellRendererComponent(@NotNull JTree tree, @Nullable Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
            {
                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                try
                {
                    DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) value;
                    if (defaultMutableTreeNode != null)
                    {
                        if (ROOT.equals(defaultMutableTreeNode.getUserObject()))
                        {
                            label.setText(TranslationManager.getInstance().getTranslation("R", "ExpressionChooser.AvailableFunctionsLabel"));
                            label.setIcon(null);
                        }
                        else if (defaultMutableTreeNode.getUserObject() instanceof Class)
                        {
                            Class<?> jFreeClass = (Class<?>) defaultMutableTreeNode.getUserObject();
                            label.setText(ExpressionRegistry.getInstance().getLocalizedExpressionName(jFreeClass));
                            label.setIcon(null);
                            String text = TranslationManager.getInstance().getTranslation("R", "Expressions." + jFreeClass.getSimpleName() + ".Description");
                            label.setToolTipText("<html>" + text + "</html>");//NON-NLS
                        }
                        else
                        {
                            label.setIcon(null);
                        }
                    }
                }
                catch (Throwable e)
                {
                    UncaughtExcpetionsModel.getInstance().addException(e);
                }

                return label;
            }
        });


        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, fill:default:grow, 0dlu", "0dlu, fill:10dlu:grow, 4dlu, pref, 4dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();

        setLayout(formLayout);

        add(new JScrollPane(expressionsTree), cc.xy(2, 2));

        final JButton addButton = new JButton(TranslationManager.getInstance().getTranslation("R", "ExpressionChooser.AddFunctionToReportButton"));
        add(addButton, cc.xy(2, 4, "center, center"));


        ToolTipManager.sharedInstance().registerComponent(expressionsTree);

        addButton.setEnabled(false);

        expressionsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        expressionsTree.addTreeSelectionListener(new TreeSelectionListener()
        {
            public void valueChanged(@NotNull TreeSelectionEvent e)
            {
                TreePath selectionPath = expressionsTree.getSelectionPath();
                boolean b = selectionPath != null &&
                            selectionPath.getLastPathComponent() instanceof DefaultMutableTreeNode &&
                            ((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject() instanceof Class;
                addButton.setEnabled(b);
            }
        });

        addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                try
                {
                    reportDialog.getUndo().startTransaction(UndoConstants.ADD_FUNCTION);
                    TreePath selectionPath = expressionsTree.getSelectionPath();
                    Class<?> expressionInfo = (Class<?>) ((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject();
                    Expression jFreeExpression = (Expression) expressionInfo.newInstance();
                    jFreeExpression.setName("");
                    ReportFunctionElement reportElement = ExpressionRegistry.getInstance().createWrapperInstance(jFreeExpression);
                    report.getReportFunctions().addChild(reportElement);
                    reportElementSelectionModel.setSelection(Arrays.asList(reportElement));
                    reportDialog.getUndo().endTransaction();
                }
                catch (Exception e1)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ExpressionChooser.actionPerformed ", e1);
                    UncaughtExcpetionsModel.getInstance().addException(e1);
                }
            }
        });
    }


    private void buildTree(@NotNull DefaultMutableTreeNode root, @NotNull ArrayList<Class<?>> classesList)
    {
        HashMap<String, DefaultMutableTreeNode> groups = new HashMap<String, DefaultMutableTreeNode>();
        groups.put("", root);

        for (Class<?> aClass : classesList)
        {
            String path = ExpressionRegistry.getInstance().getTreePath(aClass);
            if (groups.get(path) == null)
            {
                String text = TranslationManager.getInstance().getTranslation("R", "TreeGroup." + path);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(text, true);
                boolean inserted = false;
                for (int i = 0; i < root.getChildCount(); i++)
                {
                    if (text.compareToIgnoreCase(root.getChildAt(i).toString()) < 0)
                    {
                        root.insert(node, i);
                        inserted = true;
                        break;
                    }
                }
                if (!inserted)
                {
                    root.add(node);
                }
                groups.put(path, node);
            }
        }

        for (Class<?> aClass : classesList)
        {
            String path = ExpressionRegistry.getInstance().getTreePath(aClass);
            DefaultMutableTreeNode parent = groups.get(path);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(aClass, false);
            parent.add(node);
        }
    }

}
