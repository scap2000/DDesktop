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
package org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.connection.MetaDataService;
import org.pentaho.reportdesigner.lib.client.components.docking.IconCreator;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 12.02.2006
 * Time: 15:28:49
 */
public class DatabaseSchemaTree extends JTree
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(DatabaseSchemaTree.class.getName());

    @NotNull
    private DefaultTreeModel defaultTreeModel;


    public DatabaseSchemaTree(@NotNull final MetaDataService metaDataService)
    {
        RootNode rootNode = new RootNode();
        DatabaseNode databaseNode = new DatabaseNode(metaDataService.getCatalog());
        rootNode.add(databaseNode);

        defaultTreeModel = new DefaultTreeModel(rootNode);
        defaultTreeModel.setAsksAllowsChildren(true);

        setShowsRootHandles(true);
        setRootVisible(false);

        setModel(defaultTreeModel);

        addTreeWillExpandListener(new TreeWillExpandListener()
        {
            public void treeWillExpand(@NotNull TreeExpansionEvent event)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
                if (node.getChildCount() == 0)
                {
                    fetchNode(node, metaDataService);
                }
            }


            public void treeWillCollapse(@NotNull TreeExpansionEvent event)
            {
            }
        });

        setCellRenderer(new DefaultTreeCellRenderer()
        {
            @NotNull
            public Component getTreeCellRendererComponent(@NotNull JTree tree, @Nullable Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
            {
                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                try
                {
                    if (value instanceof DatabaseNode)
                    {
                        DatabaseNode node = (DatabaseNode) value;
                        String catalog = node.getCatalog();
                        if (catalog == null)
                        {
                            catalog = TranslationManager.getInstance().getTranslation("R", "SchemaTree.DatabaseNode.Title");
                        }
                        label.setText(catalog);
                        if (node.getState() > 0)
                        {
                            ImageIcon overlayImageIcon = IconCreator.createOverlayImageIcon(IconLoader.getInstance().getDataSetsIcon(), IconLoader.getInstance().getSanduhrs()[(node.getState() * 2) % 14]);
                            label.setIcon(overlayImageIcon);
                        }
                        else
                        {
                            label.setIcon(IconLoader.getInstance().getDataSetsIcon());
                        }
                    }
                    else if (value instanceof SchemaNode)
                    {
                        SchemaNode schemaNode = (SchemaNode) value;
                        label.setText(schemaNode.getSchema());
                        if (schemaNode.getState() > 0)
                        {
                            ImageIcon overlayImageIcon = IconCreator.createOverlayImageIcon(IconLoader.getInstance().getDatabaseSchemaIcon(), IconLoader.getInstance().getSanduhrs()[(schemaNode.getState() * 2) % 14]);
                            label.setIcon(overlayImageIcon);
                        }
                        else
                        {
                            label.setIcon(IconLoader.getInstance().getDatabaseSchemaIcon());
                        }
                    }
                    else if (value instanceof TableNode)
                    {
                        TableNode tableNode = (TableNode) value;
                        label.setText(tableNode.getTable());
                        if (tableNode.getState() > 0)
                        {
                            ImageIcon overlayImageIcon = IconCreator.createOverlayImageIcon(IconLoader.getInstance().getDatabaseTableIcon(), IconLoader.getInstance().getSanduhrs()[(tableNode.getState() * 2) % 14]);
                            label.setIcon(overlayImageIcon);
                        }
                        else
                        {
                            label.setIcon(IconLoader.getInstance().getDatabaseTableIcon());
                        }
                    }
                    else if (value instanceof ColumnNode)
                    {
                        ColumnNode columnNode = (ColumnNode) value;
                        label.setText(columnNode.getColumnInfo().getColumnName() + " (" + columnNode.getColumnInfo().getColumnClass().getSimpleName() + ")");
                        if (columnNode.getState() > 0)
                        {
                            ImageIcon overlayImageIcon = IconCreator.createOverlayImageIcon(IconLoader.getInstance().getDatabaseColumnIcon(), IconLoader.getInstance().getSanduhrs()[(columnNode.getState() * 2) % 14]);
                            label.setIcon(overlayImageIcon);
                        }
                        else
                        {
                            label.setIcon(IconLoader.getInstance().getDatabaseColumnIcon());
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

        initDragAndDrop();
    }


    private void fetchNode(@NotNull final DefaultMutableTreeNode node, @NotNull final MetaDataService metaDataService)
    {
        Thread t = new Thread()
        {
            private transient boolean running;


            public void run()
            {
                running = true;
                Thread stateUpdateThread = new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            while (running)
                            {
                                try
                                {
                                    Thread.sleep(50);
                                    if (!running)
                                    {
                                        return;
                                    }
                                }
                                catch (InterruptedException e)
                                {
                                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "DatabaseSchemaTree.run ", e);
                                }
                                if (node instanceof ProgressNode)
                                {
                                    try
                                    {
                                        EventQueue.invokeAndWait(new Runnable()
                                        {
                                            public void run()
                                            {
                                                ProgressNode progressNode = (ProgressNode) node;
                                                progressNode.setState(progressNode.getState() + 1);
                                                Rectangle pathBounds = getPathBounds(new TreePath(node.getPath()));
                                                if (pathBounds != null)
                                                {
                                                    repaint(pathBounds);
                                                }
                                            }
                                        });
                                    }
                                    catch (InterruptedException e)
                                    {
                                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "DatabaseSchemaTree.run ", e);
                                    }
                                    catch (InvocationTargetException e)
                                    {
                                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "DatabaseSchemaTree.run ", e);
                                    }
                                }
                            }
                        }
                        finally
                        {
                            try
                            {
                                EventQueue.invokeAndWait(new Runnable()
                                {
                                    public void run()
                                    {
                                        ProgressNode progressNode = (ProgressNode) node;
                                        progressNode.setState(0);
                                        Rectangle pathBounds = getPathBounds(new TreePath(node.getPath()));
                                        if (pathBounds != null)
                                        {
                                            repaint(pathBounds);
                                        }
                                    }
                                });
                            }
                            catch (InterruptedException e)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "DatabaseSchemaTree.run ", e);
                            }
                            catch (InvocationTargetException e)
                            {
                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "DatabaseSchemaTree.run ", e);
                            }
                        }
                    }
                };
                stateUpdateThread.setDaemon(true);
                stateUpdateThread.setPriority(Thread.NORM_PRIORITY - 1);
                stateUpdateThread.start();


                if (node instanceof DatabaseNode)
                {
                    final String[] schemaNames = metaDataService.getSchemaNames();

                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            for (String schemaName : schemaNames)
                            {
                                SchemaNode newNode = new SchemaNode(schemaName);
                                defaultTreeModel.insertNodeInto(newNode, node, node.getChildCount());
                            }

                            //workaround for mysql
                            if (schemaNames.length == 0)
                            {
                                SchemaNode newNode = new SchemaNode("");
                                defaultTreeModel.insertNodeInto(newNode, node, node.getChildCount());
                            }
                        }
                    });

                }
                else if (node instanceof SchemaNode)
                {
                    final SchemaNode schemaNode = (SchemaNode) node;
                    final String[] tableNames = metaDataService.getTableNames(schemaNode.getSchema());
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            for (String tableName : tableNames)
                            {
                                TableNode newNode = new TableNode(schemaNode.getSchema(), tableName);
                                defaultTreeModel.insertNodeInto(newNode, node, node.getChildCount());
                            }
                        }
                    });

                }
                else if (node instanceof TableNode)
                {
                    final TableNode tableNode = (TableNode) node;
                    final ColumnInfo[] columnInfos = metaDataService.getColumnInfos(tableNode.getSchema(), tableNode.getTable());
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            for (ColumnInfo columnInfo : columnInfos)
                            {
                                ColumnNode newNode = new ColumnNode(tableNode.getSchema(), tableNode.getTable(), columnInfo);
                                defaultTreeModel.insertNodeInto(newNode, node, node.getChildCount());
                            }
                        }
                    });

                }

                running = false;
            }
        };
        t.setDaemon(true);
        t.setPriority(Thread.NORM_PRIORITY - 1);
        t.start();
    }


    private void initDragAndDrop()
    {
        setTransferHandler(new TransferHandler()
        {
            @NotNull
            protected Transferable createTransferable(@NotNull JComponent c)
            {
                return new Transferable()
                {
                    @NotNull
                    public DataFlavor[] getTransferDataFlavors()
                    {
                        return new DataFlavor[]{DataFlavor.stringFlavor};
                    }


                    public boolean isDataFlavorSupported(@NotNull DataFlavor flavor)
                    {
                        //noinspection ObjectEquality
                        return DataFlavor.stringFlavor == flavor;
                    }


                    @Nullable
                    public Object getTransferData(@NotNull DataFlavor flavor) throws UnsupportedFlavorException
                    {
                        //noinspection ObjectEquality
                        if (DataFlavor.stringFlavor == flavor)
                        {
                            TreePath[] selectionPaths = getSelectionPaths();
                            if (selectionPaths != null && selectionPaths.length == 1)
                            {
                                TreePath tp = selectionPaths[0];
                                if (tp.getLastPathComponent() instanceof TableNode)
                                {
                                    TableNode tableNode = (TableNode) tp.getLastPathComponent();
                                    return tableNode.getTable();
                                }
                                else if (tp.getLastPathComponent() instanceof ColumnNode)
                                {
                                    ColumnNode columnNode = (ColumnNode) tp.getLastPathComponent();
                                    return columnNode.getTable() + "." + columnNode.getColumnInfo().getColumnName();
                                }
                            }
                            else if (selectionPaths != null)
                            {
                                StringBuilder sb = new StringBuilder(32);
                                boolean first = true;
                                for (TreePath treePath : selectionPaths)
                                {
                                    if (treePath.getLastPathComponent() instanceof ColumnNode)
                                    {
                                        ColumnNode columnNode = (ColumnNode) treePath.getLastPathComponent();
                                        if (!first)
                                        {
                                            sb.append(", ");
                                        }
                                        sb.append(columnNode.getTable()).append(".").append(columnNode.getColumnInfo().getColumnName());
                                        first = false;
                                    }
                                }
                                return sb.toString();
                            }

                            return null;
                        }
                        else
                        {
                            throw new UnsupportedFlavorException(flavor);
                        }
                    }
                };
            }


            public int getSourceActions(@NotNull JComponent c)
            {
                TreePath[] paths = getSelectionPaths();
                if (paths != null && paths.length == 1)
                {
                    if (paths[0].getLastPathComponent() instanceof RootNode)
                    {
                        return TransferHandler.NONE;
                    }
                    else if (paths[0].getLastPathComponent() instanceof DatabaseNode)
                    {
                        return TransferHandler.NONE;
                    }
                    else if (paths[0].getLastPathComponent() instanceof SchemaNode)
                    {
                        return TransferHandler.NONE;
                    }
                    else
                    {
                        return TransferHandler.COPY;
                    }
                }
                else if (paths != null)
                {
                    for (TreePath treePath : paths)
                    {
                        if (!(treePath.getLastPathComponent() instanceof ColumnNode))
                        {
                            return TransferHandler.NONE;
                        }
                    }
                    return TransferHandler.COPY;
                }
                return TransferHandler.NONE;
            }
        });
        setDragEnabled(true);
    }


    public static class ProgressNode extends DefaultMutableTreeNode
    {
        private int state;


        public int getState()
        {
            return state;
        }


        public void setState(int state)
        {
            this.state = state;
        }
    }

    public static class RootNode extends ProgressNode
    {
        public RootNode()
        {
        }


        public boolean getAllowsChildren()
        {
            return true;
        }
    }

    public static class DatabaseNode extends ProgressNode
    {
        @NotNull
        private String catalog;


        public DatabaseNode(@NotNull String catalog)
        {
            this.catalog = catalog;
        }


        @NotNull
        public String getCatalog()
        {
            return catalog;
        }


        public boolean getAllowsChildren()
        {
            return true;
        }
    }

    public static class SchemaNode extends ProgressNode
    {
        @NotNull
        private String schema;


        public SchemaNode(@NotNull String schema)
        {
            this.schema = schema;
        }


        @NotNull
        public String getSchema()
        {
            return schema;
        }


        public boolean getAllowsChildren()
        {
            return true;
        }


        @NotNull
        public String toString()
        {
            return "SchemaNode{" +
                   "schema='" + schema + "'" +
                   "}";
        }
    }

    public static class TableNode extends ProgressNode
    {
        @NotNull
        private String schema;
        @NotNull
        private String table;


        public TableNode(@NotNull String schema, @NotNull String table)
        {
            this.schema = schema;
            this.table = table;
        }


        @NotNull
        public String getSchema()
        {
            return schema;
        }


        @NotNull
        public String getTable()
        {
            return table;
        }


        public boolean getAllowsChildren()
        {
            return true;
        }


        @NotNull
        public String toString()
        {
            return "TableNode{" +
                   "schema='" + schema + "'" +
                   ", table='" + table + "'" +
                   "}";
        }
    }

    public static class ColumnNode extends ProgressNode
    {
        @NotNull
        private String schema;
        @NotNull
        private String table;
        @NotNull
        private ColumnInfo columnInfo;


        public ColumnNode(@NotNull String schema, @NotNull String table, @NotNull ColumnInfo columnInfo)
        {
            this.schema = schema;
            this.table = table;

            this.columnInfo = columnInfo;
        }


        @NotNull
        public String getSchema()
        {
            return schema;
        }


        @NotNull
        public String getTable()
        {
            return table;
        }


        @NotNull
        public ColumnInfo getColumnInfo()
        {
            return columnInfo;
        }


        public boolean getAllowsChildren()
        {
            return false;
        }


        @NotNull
        public String toString()
        {
            return "ColumnNode{" +
                   "schema='" + schema + "'" +
                   ", table='" + table + "'" +
                   ", columnInfo=" + columnInfo +
                   "}";
        }
    }


}
