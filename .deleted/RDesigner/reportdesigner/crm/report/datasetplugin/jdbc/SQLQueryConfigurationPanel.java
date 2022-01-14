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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.components.ProgressListener;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.connection.JDBCConnection;
import org.pentaho.reportdesigner.crm.report.connection.MetaDataService;
import org.pentaho.reportdesigner.crm.report.datasetplugin.composer.JDBCAnalyzer;
import org.pentaho.reportdesigner.crm.report.datasetplugin.composer.JDBCColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.composer.JDBCGraph;
import org.pentaho.reportdesigner.crm.report.datasetplugin.composer.JDBCVertex;
import org.pentaho.reportdesigner.crm.report.datasetplugin.composer.NoAffectedTablesException;
import org.pentaho.reportdesigner.crm.report.datasetplugin.composer.NoSuitableRootException;
import org.pentaho.reportdesigner.crm.report.datasetplugin.composer.QueryComposer;
import org.pentaho.reportdesigner.crm.report.datasetplugin.composer.QueryComposerColumn;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.components.ProgressDialog;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.CooltipManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;
import org.pentaho.reportdesigner.lib.client.util.TableUtils;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 13.02.2006
 * Time: 07:47:56
 */
public class SQLQueryConfigurationPanel extends JPanel
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(SQLQueryConfigurationPanel.class.getName());

    private static final int MAX_QUERY_HISTORY_SIZE = 30;
    private static final int DEFAULT_MAX_ROWS = 100;


    @NotNull
    public static final String OK = "ok";

    @NotNull
    private PropertyChangeSupport propertyChangeSupport;

    @NotNull
    private ArrayList<ColumnInfo> columnInfos;
    @NotNull
    private ArrayList<ColumnInfo> availableColumnInfos;

    @NotNull
    private ArrayList<String> oldQueries = new ArrayList<String>();

    @NotNull
    @NonNls
    private JTextArea sqlQueryTextArea;
    @NotNull
    private JTextField maxResultsTextField;

    @NotNull
    private JScrollPane schemaTreeScrollPane;
    @NotNull
    private DatabaseSchemaTree databaseSchemaTree;

    private boolean ok;
    @NotNull
    private Component parent;
    @NotNull
    private WorkspaceSettings workspaceSettings;
    @Nullable
    private MetaDataService metaDataService;

    @NotNull
    private JButton testQueryButton;
    @NotNull
    private JButton sqlHistoryButton;

    //caching
    @NotNull
    private JDBCGraph graph;


    public SQLQueryConfigurationPanel(@NotNull Component parent, @NotNull final WorkspaceSettings workspaceSettings)
    {
        //noinspection ConstantConditions
        if (parent == null)
        {
            throw new IllegalArgumentException("parent must not be null");
        }
        //noinspection ConstantConditions
        if (workspaceSettings == null)
        {
            throw new IllegalArgumentException("workspaceSettings must not be null");
        }

        this.parent = parent;
        this.workspaceSettings = workspaceSettings;

        setLayout(new BorderLayout());

        propertyChangeSupport = new PropertyChangeSupport(this);

        ok = false;

        @NonNls
        CellConstraints cc = new CellConstraints();

        schemaTreeScrollPane = new JScrollPane();

        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, fill:default:grow, 4dlu, fill:default, 4dlu, fill:default, 4dlu, fill:default, 0dlu",
                                               "0dlu, " +
                                               "fill:10dlu:grow, " +
                                               "4dlu, " +
                                               "pref, " +
                                               "0dlu");

        JPanel bottomPanel = new JPanel(formLayout);

        sqlQueryTextArea = new JTextArea();
        sqlQueryTextArea.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(@NotNull DocumentEvent e)
            {
                sqlQueryTextArea.requestFocusInWindow();
            }


            public void removeUpdate(@NotNull DocumentEvent e)
            {
                sqlQueryTextArea.requestFocusInWindow();
            }


            public void changedUpdate(@NotNull DocumentEvent e)
            {
                sqlQueryTextArea.requestFocusInWindow();
            }
        });
        UndoHelper.installUndoSupport(sqlQueryTextArea);
        TextComponentHelper.installDefaultPopupMenu(sqlQueryTextArea);
        /*
        "SELECT * FROM customer JOIN\n" +
        "shopping_cart ON customer.customer_id=shopping_cart.customer_id JOIN\n" +
        "item ON shopping_cart.item_id=item.item_id"
        */

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setTopComponent(schemaTreeScrollPane);

        splitPane.setDividerLocation(200);
        bottomPanel.add(splitPane, cc.xyw(2, 2, 7, "fill, fill"));

        JScrollPane scrollPane = new JScrollPane(sqlQueryTextArea);
        scrollPane.setPreferredSize(new Dimension(10, 10));
        splitPane.setBottomComponent(scrollPane);

        JLabel maxResultsLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "WizardPageDatabaseConnection.MaxResultsLabel"));
        maxResultsTextField = new JTextField(String.valueOf(DEFAULT_MAX_ROWS), 5);
        bottomPanel.add(maxResultsLabel, cc.xy(2, 4, "right, center"));
        bottomPanel.add(maxResultsTextField, cc.xy(4, 4));

        testQueryButton = ComponentFactory.createButton("R", "WizardPageDatabaseConnection.PreviewButton");
        bottomPanel.add(testQueryButton, cc.xy(6, 4));

        testQueryButton.setEnabled(false);

        testQueryButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                try
                {
                    final String query = sqlQueryTextArea.getText();
                    final int maxRows = Integer.parseInt(maxResultsTextField.getText());

                    final DefaultListModel defaultListModel = new DefaultListModel();
                    columnInfos.clear();

                    final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(SQLQueryConfigurationPanel.this, TranslationManager.getInstance().getTranslation("R", "SQLQueryConfigurationPanel.ExecutingQueryDialog.Title"), "");

                    Thread t = new Thread(new Runnable()
                    {
                        public void run()
                        {
                            final ColumnInfo[] queryMetaData;
                            final ReportDataTableModel reportDataTableModel;
                            try
                            {
                                MetaDataService mds = metaDataService;
                                if (mds != null)
                                {
                                    ArrayList<ArrayList<Object>> queryData = mds.getQueryData(maxRows, query);
                                    queryMetaData = mds.getQueryMetaData(query);
                                    reportDataTableModel = new ReportDataTableModel(queryMetaData, queryData);

                                    EventQueue.invokeLater(new Runnable()
                                    {
                                        public void run()
                                        {
                                            for (ColumnInfo columnInfo : queryMetaData)
                                            {
                                                defaultListModel.addElement(columnInfo);
                                                columnInfos.add(columnInfo);
                                            }

                                            progressDialog.dispose();

                                            showPreviewDialog(reportDataTableModel);

                                            availableColumnInfos.clear();
                                            availableColumnInfos.addAll(columnInfos);

                                            ok = true;
                                            propertyChangeSupport.firePropertyChange(OK, Boolean.FALSE, Boolean.TRUE);
                                        }
                                    });
                                }
                            }
                            catch (final Throwable e1)
                            {
                                EventQueue.invokeLater(new Runnable()
                                {
                                    public void run()
                                    {
                                        progressDialog.dispose();
                                        ok = false;
                                        propertyChangeSupport.firePropertyChange(OK, Boolean.TRUE, Boolean.FALSE);

                                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "WizardPageDatabaseConnection.actionPerformed ", e1);
                                        Throwable t = e1;
                                        if (e1.getCause() != null)
                                        {
                                            t = e1.getCause();
                                        }
                                        JOptionPane.showMessageDialog(SQLQueryConfigurationPanel.this,
                                                                      TranslationManager.getInstance().getTranslation("R", "Database.CouldNotFetchData") + " (" + t.getMessage() + ")",
                                                                      TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                                                      JOptionPane.ERROR_MESSAGE);
                                    }
                                });
                            }
                        }
                    });

                    t.setDaemon(true);
                    t.start();

                    if (t.isAlive())
                    {
                        progressDialog.setVisible(true);
                    }
                    else
                    {
                        progressDialog.dispose();
                    }
                }
                catch (Exception e1)
                {
                    ok = false;
                    propertyChangeSupport.firePropertyChange(OK, Boolean.TRUE, Boolean.FALSE);

                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "WizardPageDatabaseConnection.actionPerformed ", e1);
                    JOptionPane.showMessageDialog(SQLQueryConfigurationPanel.this,
                                                  TranslationManager.getInstance().getTranslation("R", "Database.CouldNotFetchData") + " " + e1.getMessage(),
                                                  TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                }

            }
        });


        ArrayList<String> list = workspaceSettings.getList("SQLQueryConfigurationPanel.SQLHistory");
        oldQueries = new ArrayList<String>(list);

        sqlHistoryButton = new JButton(TranslationManager.getInstance().getTranslation("R", "SQLQueryConfigurationPanel.SQLHistoryButton"));
        bottomPanel.add(sqlHistoryButton, cc.xy(8, 4));
        sqlHistoryButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                final CenterPanelDialog historyDialog = CenterPanelDialog.createDialog(sqlHistoryButton, TranslationManager.getInstance().getTranslation("R", "SQLQueryConfigurationPanel.SQLHistoryDialog.Title"), true);

                JPanel centerPanel = new JPanel(new BorderLayout());
                ArrayList<String> queries = new ArrayList<String>(oldQueries);
                Collections.reverse(queries);
                final JList list = new JList(queries.toArray());

                final JTextArea queryArea = new JTextArea();
                queryArea.setEditable(false);

                list.addListSelectionListener(new ListSelectionListener()
                {
                    public void valueChanged(@NotNull ListSelectionEvent e)
                    {
                        if (!e.getValueIsAdjusting())
                        {
                            Object selectedValue = list.getSelectedValue();
                            if (selectedValue != null)
                            {
                                queryArea.setText(selectedValue.toString());
                            }
                        }
                    }
                });

                JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
                JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));
                historyDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

                okButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        try
                        {
                            Object selectedValue = list.getSelectedValue();
                            if (selectedValue != null)
                            {
                                if (sqlQueryTextArea.getDocument().getLength() > 0)
                                {
                                    sqlQueryTextArea.getDocument().insertString(sqlQueryTextArea.getDocument().getLength(), "\n", null);
                                }
                                sqlQueryTextArea.getDocument().insertString(sqlQueryTextArea.getDocument().getLength(), selectedValue.toString(), null);
                            }

                            workspaceSettings.storeDialogBounds(historyDialog, "SQLHistoryDialog");
                            historyDialog.dispose();
                        }
                        catch (BadLocationException e1)
                        {
                            UncaughtExcpetionsModel.getInstance().addException(e1);
                        }
                    }
                });

                cancelButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(@NotNull ActionEvent e)
                    {
                        workspaceSettings.storeDialogBounds(historyDialog, "SQLHistoryDialog");
                        historyDialog.dispose();
                    }
                });

                JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JScrollPane(list), new JScrollPane(queryArea));
                centerPanel.add(splitPane, BorderLayout.CENTER);
                splitPane.setDividerLocation(150);
                centerPanel.setPreferredSize(new Dimension(100, 100));

                historyDialog.setCenterPanel(centerPanel);

                if (!workspaceSettings.restoreDialogBounds(historyDialog, "SQLHistoryDialog"))
                {
                    historyDialog.pack();
                    GUIUtils.ensureMinimumDialogSize(historyDialog, 400, 400);
                }

                WindowUtils.setLocationRelativeTo(historyDialog, SQLQueryConfigurationPanel.this);
                historyDialog.setVisible(true);
            }
        });

        DocumentListener modificationListener = new DocumentListener()
        {
            public void insertUpdate(@NotNull DocumentEvent e)
            {
                ok = false;
                propertyChangeSupport.firePropertyChange(OK, Boolean.TRUE, Boolean.FALSE);
            }


            public void removeUpdate(@NotNull DocumentEvent e)
            {
                ok = false;
                propertyChangeSupport.firePropertyChange(OK, Boolean.TRUE, Boolean.FALSE);
            }


            public void changedUpdate(@NotNull DocumentEvent e)
            {
                ok = false;
                propertyChangeSupport.firePropertyChange(OK, Boolean.TRUE, Boolean.FALSE);
            }
        };

        sqlQueryTextArea.getDocument().addDocumentListener(modificationListener);
        maxResultsTextField.getDocument().addDocumentListener(modificationListener);

        columnInfos = new ArrayList<ColumnInfo>();
        availableColumnInfos = new ArrayList<ColumnInfo>();

        add(bottomPanel, BorderLayout.CENTER);
    }


    @NotNull
    public ArrayList<ColumnInfo> getAvailableColumnInfos()
    {
        return availableColumnInfos;
    }


    @NotNull
    public ArrayList<ColumnInfo> getColumnInfos()
    {
        return columnInfos;
    }


    @NotNull
    public String getSQLQuery()
    {
        return sqlQueryTextArea.getText();
    }


    public void setSQLQuery(@NotNull String query)
    {
        sqlQueryTextArea.setText(query);
    }


    public int getMaxPreviewRows()
    {
        try
        {
            return Integer.parseInt(maxResultsTextField.getText());
        }
        catch (NumberFormatException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "SQLQueryConfigurationPanel.getMaxPreviewRows ", e);
            return DEFAULT_MAX_ROWS;
        }
    }


    public void setMaxPreviewRows(int maxPreviewRows)
    {
        maxResultsTextField.setText(String.valueOf(maxPreviewRows));
    }


    public boolean isOk()
    {
        return ok;
    }


    private void showPreviewDialog(@NotNull ReportDataTableModel reportDataTableModel)
    {
        final CenterPanelDialog centerPanelDialog = CenterPanelDialog.createDialog(parent, TranslationManager.getInstance().getTranslation("R", "WizardPageSQLQuery.PreviewDialog.Title"), true);

        final JPanel centerPanel = new JPanel(new BorderLayout());
        JTable table = new JTable(reportDataTableModel);
        JTable rowHeaderTable = new JTable(new RowNumberTableModel(reportDataTableModel.getRowCount()));
        rowHeaderTable.setEnabled(false);
        rowHeaderTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer()
        {
            @NotNull
            public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                JLabel tableCellRendererComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                tableCellRendererComponent.setHorizontalAlignment(JLabel.TRAILING);
                tableCellRendererComponent.setBackground(centerPanel.getBackground());
                return tableCellRendererComponent;
            }
        });
        rowHeaderTable.setGridColor(centerPanel.getBackground().darker());
        rowHeaderTable.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, centerPanel.getBackground().darker()));

        JViewport viewport = new JViewport();
        viewport.setPreferredSize(new Dimension(40, 100));
        viewport.setView(rowHeaderTable);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableUtils.resizeAllColumns(table, 200);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeader(viewport);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanelDialog.setCenterPanel(centerPanel);
        JButton closeButton = ComponentFactory.createButton("R", "WizardPageSQLQuery.Close");
        closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                workspaceSettings.storeDialogBounds(centerPanelDialog, "WizardPageSQLQuery.PreviewDialog");
                centerPanelDialog.dispose();
            }
        });
        centerPanelDialog.setButtons(closeButton, closeButton, CenterPanelDialog.ButtonAlignment.CENTER, closeButton);

        if (!workspaceSettings.restoreDialogBounds(centerPanelDialog, "WizardPageSQLQuery.PreviewDialog"))
        {
            centerPanelDialog.pack();
        }

        if (!oldQueries.contains(sqlQueryTextArea.getText()))
        {
            oldQueries.add(sqlQueryTextArea.getText());
            while (oldQueries.size() > MAX_QUERY_HISTORY_SIZE)
            {
                oldQueries.remove(0);
            }
            workspaceSettings.put("SQLQueryConfigurationPanel.SQLHistory", oldQueries);
        }


        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);
    }


    public void setMetaDataService(@Nullable final MetaDataService metaDataService)
    {
        if (this.metaDataService != null)
        {
            this.metaDataService.dispose();
        }
        this.metaDataService = metaDataService;
        testQueryButton.setEnabled(metaDataService != null);

        if (metaDataService != null)
        {
            databaseSchemaTree = new DatabaseSchemaTree(metaDataService);

            databaseSchemaTree.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(@NotNull MouseEvent e)
                {
                    showPopup(e);
                }


                public void mousePressed(@NotNull MouseEvent e)
                {
                    showPopup(e);
                }


                public void mouseReleased(@NotNull MouseEvent e)
                {
                    showPopup(e);
                }


                private void showPopup(@NotNull MouseEvent e)
                {
                    if (e.isPopupTrigger())
                    {
                        TreePath[] selectionPaths = databaseSchemaTree.getSelectionPaths();
                        if (selectionPaths != null && selectionPaths.length == 1)
                        {
                            if (selectionPaths[0].getLastPathComponent() instanceof DatabaseSchemaTree.TableNode)
                            {
                                final DatabaseSchemaTree.TableNode tableNode = (DatabaseSchemaTree.TableNode) selectionPaths[0].getLastPathComponent();
                                JPopupMenu popupMenu = new JPopupMenu();
                                JMenuItem menuItem = new JMenuItem(TranslationManager.getInstance().getTranslation("R", "WizardPageSQLQuery.InsertSelect"));
                                menuItem.addActionListener(new ActionListener()
                                {
                                    public void actionPerformed(@NotNull ActionEvent e)
                                    {
                                        if (tableNode.getChildCount() == 0)
                                        {
                                            StringBuilder sb = new StringBuilder(32);
                                            sb.append("SELECT * FROM ").append(tableNode.getTable());//NON-NLS
                                            sqlQueryTextArea.insert(sb.toString(), sqlQueryTextArea.getDocument().getLength());
                                        }
                                        else
                                        {
                                            StringBuilder sb = new StringBuilder(32);
                                            sb.append("SELECT ");//NON-NLS
                                            for (int i = 0; i < tableNode.getChildCount(); i++)
                                            {
                                                DatabaseSchemaTree.ColumnNode columnNode = (DatabaseSchemaTree.ColumnNode) tableNode.getChildAt(i);
                                                sb.append(columnNode.getColumnInfo().getColumnName());
                                                if (i < tableNode.getChildCount() - 1)
                                                {
                                                    sb.append(", ");
                                                }
                                            }
                                            sb.append("\nFROM ").append(tableNode.getTable());//NON-NLS
                                            sqlQueryTextArea.insert(sb.toString(), sqlQueryTextArea.getDocument().getLength());
                                        }
                                    }
                                });
                                popupMenu.add(menuItem);
                                popupMenu.show(databaseSchemaTree, e.getX(), e.getY());
                            }
                            else if (selectionPaths[0].getLastPathComponent() instanceof DatabaseSchemaTree.SchemaNode)
                            {
                                //show query configurator
                            }
                        }
                        else if (selectionPaths != null && selectionPaths.length > 1)
                        {
                            //get all columns from same schema
                            String firstSchema = null;
                            final ArrayList<DatabaseSchemaTree.ColumnNode> columnNodes = new ArrayList<DatabaseSchemaTree.ColumnNode>();
                            for (TreePath treePath : selectionPaths)
                            {
                                if (treePath.getLastPathComponent() instanceof DatabaseSchemaTree.ColumnNode)
                                {
                                    DatabaseSchemaTree.ColumnNode columnNode = (DatabaseSchemaTree.ColumnNode) treePath.getLastPathComponent();
                                    if (firstSchema == null)
                                    {
                                        firstSchema = columnNode.getSchema();
                                        columnNodes.add(columnNode);
                                    }
                                    else if (firstSchema.equals(columnNode.getSchema()))
                                    {
                                        columnNodes.add(columnNode);
                                    }
                                }
                            }
                            if (!columnNodes.isEmpty() && (metaDataService instanceof JDBCConnection))
                            {
                                final JDBCConnection jdbcConnection = (JDBCConnection) metaDataService;
                                JPopupMenu popupMenu = new JPopupMenu();
                                JMenuItem menuItem = new JMenuItem(TranslationManager.getInstance().getTranslation("R", "WizardPageSQLQuery.InsertSelect"));
                                menuItem.addActionListener(new ActionListener()
                                {
                                    public void actionPerformed(@NotNull ActionEvent e)
                                    {
                                        createAutomaticQuery(jdbcConnection, columnNodes);
                                    }
                                });
                                popupMenu.add(menuItem);
                                popupMenu.show(databaseSchemaTree, e.getX(), e.getY());
                            }
                        }
                    }
                }
            });
            schemaTreeScrollPane.setViewportView(databaseSchemaTree);

            CooltipManager cooltipManager = new CooltipManager();
            cooltipManager.registerComponent(databaseSchemaTree);
        }
        else
        {
            schemaTreeScrollPane.setViewportView(new JPanel());
        }
    }


    private void createAutomaticQuery(@NotNull final JDBCConnection jdbcConnection, @NotNull final ArrayList<DatabaseSchemaTree.ColumnNode> columnNodes)
    {
        if (graph == null)
        {
            final ProgressDialog progressDialog = ProgressDialog.createProgressDialog(this, TranslationManager.getInstance().getTranslation("R", "SQLQueryConfigurationPanel.AnalyzeDatabase"), "");
            Thread analyzerThread = new Thread()
            {
                public void run()
                {
                    try
                    {
                        graph = JDBCAnalyzer.buildGraph(jdbcConnection.getCatalog(), columnNodes.get(0).getSchema(), jdbcConnection.getConnection(), new ProgressListener()
                        {
                            public void taskStarted(@NotNull final String task)
                            {
                                EventQueue.invokeLater(new Runnable()
                                {
                                    public void run()
                                    {
                                        progressDialog.setTask(TranslationManager.getInstance().getTranslation("R", "SQLQueryConfigurationPanel.AnalyzeTable", task));
                                    }
                                });
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "SQLQueryConfigurationPanel.run ", e);
                        UncaughtExcpetionsModel.getInstance().addException(e);//TODO show useful message
                    }

                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            progressDialog.dispose();
                        }
                    });
                }
            };

            analyzerThread.setDaemon(true);
            analyzerThread.start();

            if (analyzerThread.isAlive())
            {
                progressDialog.setVisible(true);
            }
            else
            {
                progressDialog.dispose();
            }
        }

        ArrayList<QueryComposerColumn> queryComposerColumns = new ArrayList<QueryComposerColumn>();

        for (DatabaseSchemaTree.ColumnNode columnNode : columnNodes)
        {
            JDBCVertex table = graph.getVertexForTable(columnNode.getTable());
            if (table != null)
            {
                @Nullable
                JDBCColumnInfo gueltigVon = table.getTableInfo().getColumnInfos().get(columnNode.getColumnInfo().getColumnName());
                if (gueltigVon != null)
                {
                    queryComposerColumns.add(new QueryComposerColumn(gueltigVon, true, null, null, null, null));
                }
            }
        }
        String query = null;
        try
        {
            query = QueryComposer.getQuery(graph, queryComposerColumns);
        }
        catch (NoAffectedTablesException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "SQLQueryConfigurationPanel.createAutomaticQuery ", e);
            JOptionPane.showMessageDialog(this,
                                          TranslationManager.getInstance().getTranslation("R", "Database.CouldNotComposeQuery") + " (" + e.getMessage() + ")",
                                          TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                          JOptionPane.ERROR_MESSAGE);
        }
        catch (NoSuitableRootException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "SQLQueryConfigurationPanel.createAutomaticQuery ", e);
            JOptionPane.showMessageDialog(this,
                                          TranslationManager.getInstance().getTranslation("R", "Database.CouldNotComposeQuery") + " (" + e.getMessage() + ")",
                                          TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                          JOptionPane.ERROR_MESSAGE);
        }
        sqlQueryTextArea.setText(query);
    }


    public void dispose()
    {
        if (metaDataService != null)
        {
            metaDataService.dispose();
        }
    }


    public void addPropertyChangeListener2(@NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }


    public void addPropertyChangeListener2(@NotNull String propertyName, @NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }


    public void removePropertyChangeListener2(@NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }


    public void removePropertyChangeListener2(@NotNull String propertyName, @NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }


}
