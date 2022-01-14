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
package org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.core.admin.datasources.DataSourceInfo;
import org.pentaho.core.admin.datasources.StandaloneSimpleJNDIDatasourceAdmin;
import org.pentaho.core.session.StandaloneSession;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.ColumnInfoTableModel;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataFetchException;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.TableModelDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.crm.report.util.FileRelativator;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.util.logging.ILogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.xpath.XPathExpressionException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin Date: 31.01.2006 Time: 10:30:43
 */
public class MultiDataSetReportElement extends TableModelDataSetReportElement
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(MultiDataSetReportElement.class.getName());

    public enum ConnectionType
    {
        @NotNull JNDI,
        @NotNull MQL,
        @NotNull XQuery
    }

    @NotNull
    private ConnectionType connectionType;

    @NotNull
    private ArrayList<JNDISource> jndiSources;
    @Nullable
    private String xQueryDataFile;

    @Nullable
    private JNDISource selectedJNDIDataSource;

    private boolean useMondrianCubeDefinition;
    @NotNull
    private String mondrianCubeDefinitionFile;
    @Nullable
    private String xmiDefinitionFile;
    @NotNull
    private ArrayList<Query> queries;

    @NotNull
    private ArrayList<ColumnInfo> columnInfos;

    @Nullable
    private TableModel cachedTableModel;
    @NotNull
    private JTable infoTable;


    public MultiDataSetReportElement()
    {
        connectionType = ConnectionType.JNDI;
        jndiSources = new ArrayList<JNDISource>();

        StandaloneSession session = new StandaloneSession("Datasource-Session"); //$NON-NLS-1$
        session.setLoggingLevel(ILogger.ERROR);
        StandaloneSimpleJNDIDatasourceAdmin dataSourceAdmin = new StandaloneSimpleJNDIDatasourceAdmin(ReportWizard.simpleJNDIPath, session);
        Map dsMap = dataSourceAdmin.listDataSources();
        for (Object o : dsMap.keySet())
        {
            String key = (String) o;
            DataSourceInfo dsi = (DataSourceInfo) dsMap.get(key);

            JNDISource jndiSource = new JNDISource();
            jndiSource.setJndiName(dsi.getName());
            jndiSource.setDriverClass(dsi.getDriver());
            jndiSource.setConnectionString(dsi.getUrl());
            jndiSource.setUsername(dsi.getUserId());
            jndiSource.setPassword(dsi.getPassword());

            jndiSources.add(jndiSource);
        }

        if (jndiSources.isEmpty())
        {
            jndiSources.add(new JNDISource("SampleData", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/sampledata", "pentaho_user", "password"));// NON-NLS
            jndiSources.add(new JNDISource("Quartz", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/quartz", "pentaho_user", "password"));// NON-NLS
            jndiSources.add(new JNDISource("Hibernate", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/hibernate", "hibuser", "password"));// NON-NLS
            jndiSources.add(new JNDISource("Shark", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/shark", "sa", ""));// NON-NLS
        }

        xQueryDataFile = "";
        mondrianCubeDefinitionFile = "";

        selectedJNDIDataSource = null;
        useMondrianCubeDefinition = false;
        queries = new ArrayList<Query>();
        queries.add(new Query(ReportDialogConstants.DEFAULT_DATA_FACTORY, ""));
        columnInfos = new ArrayList<ColumnInfo>();

        infoTable = new JTable(0, 0);
    }


    @NotNull
    public String getQueryName()
    {
        if (!queries.isEmpty())
        {
            return queries.get(0).getQueryName();
        }
        return "";
    }


    @NotNull
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }


    public void setConnectionType(@NotNull final ConnectionType connectionType)
    {
        // noinspection ConstantConditions
        if (connectionType == null)
        {
            throw new IllegalArgumentException("connectionType must not be null");
        }

        final ConnectionType oldConnectionType = this.connectionType;
        this.connectionType = connectionType;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.CONNECTION_TYPE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setConnectionType(oldConnectionType);
                }


                public void redo()
                {
                    setConnectionType(connectionType);
                }
            });
            undo.endTransaction();
        }

        cachedTableModel = null;

        firePropertyChange(PropertyKeys.CONNECTION_TYPE, oldConnectionType, connectionType);
    }


    @NotNull
    public ArrayList<JNDISource> getJndiSources()
    {
        return new ArrayList<JNDISource>(jndiSources);
    }


    public void setJndiSources(@NotNull final ArrayList<JNDISource> jndiSources)
    {
        // noinspection ConstantConditions
        if (jndiSources == null)
        {
            throw new IllegalArgumentException("jndiSources must not be null");
        }

        final ArrayList<JNDISource> oldJndiSources = this.jndiSources;
        this.jndiSources = jndiSources;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.JNDI_SOURCES);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setJndiSources(oldJndiSources);
                }


                public void redo()
                {
                    setJndiSources(jndiSources);
                }
            });
            undo.endTransaction();
        }

        cachedTableModel = null;

        ArrayList<String> sources = new ArrayList<String>();
        for (JNDISource jndiSource : jndiSources)
        {
            sources.add(jndiSource.getJndiName() + "|" + jndiSource.getDriverClass() + "|" + jndiSource.getConnectionString() + "|" + jndiSource.getUsername() + "|" + jndiSource.getPassword());
        }

        WorkspaceSettings.getInstance().put("JNDISources", sources);

        firePropertyChange(PropertyKeys.JNDI_SOURCES, oldJndiSources, jndiSources);
    }


    @NotNull
    public String getMondrianCubeDefinitionFile()
    {
        return mondrianCubeDefinitionFile;
    }


    public void setMondrianCubeDefinitionFile(@NotNull final String mondrianCubeDefinitionFile)
    {
        // noinspection ConstantConditions
        if (mondrianCubeDefinitionFile == null)
        {
            throw new IllegalArgumentException("mondrianCubeDefinitionFile must not be null");
        }

        final String oldMondrianCubeDefinitionFile = this.mondrianCubeDefinitionFile;
        this.mondrianCubeDefinitionFile = mondrianCubeDefinitionFile;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.MONDRIAN_CUBE_DEFINITION_FILE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setMondrianCubeDefinitionFile(oldMondrianCubeDefinitionFile);
                }


                public void redo()
                {
                    setMondrianCubeDefinitionFile(mondrianCubeDefinitionFile);
                }
            });
            undo.endTransaction();
        }

        cachedTableModel = null;

        firePropertyChange(PropertyKeys.MONDRIAN_CUBE_DEFINITION_FILE, oldMondrianCubeDefinitionFile, mondrianCubeDefinitionFile);
    }


    @NotNull
    public ArrayList<Query> getQueries()
    {
        ArrayList<Query> copyQueries = new ArrayList<Query>();
        for (Query query : queries)
        {
            copyQueries.add(new Query(query.getQueryName(), query.getQuery()));
        }
        return copyQueries;
    }


    public void setQueries(@NotNull final ArrayList<Query> queries)
    {
        // noinspection ConstantConditions
        if (queries == null)
        {
            throw new IllegalArgumentException("queries must not be null");
        }
        if (queries.size() < 1)
        {
            throw new IllegalArgumentException("queries must contain at least one query (mainquery)");
        }

        final ArrayList<Query> oldQueries = this.queries;
        this.queries = queries;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.QUERIES);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setQueries(oldQueries);
                }


                public void redo()
                {
                    setQueries(queries);
                }
            });
            undo.endTransaction();
        }

        cachedTableModel = null;

        firePropertyChange(PropertyKeys.QUERIES, oldQueries, queries);
    }


    @Nullable
    public JNDISource getSelectedJNDIDataSource()
    {
        return selectedJNDIDataSource;
    }


    public void setSelectedJNDIDataSource(@Nullable final JNDISource selectedJNDIDataSource)
    {
        final JNDISource oldSelectedJNJndiSource = this.selectedJNDIDataSource;
        this.selectedJNDIDataSource = selectedJNDIDataSource;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.SELECTED_JNDI_DATA_SOURCE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setSelectedJNDIDataSource(oldSelectedJNJndiSource);
                }


                public void redo()
                {
                    setSelectedJNDIDataSource(selectedJNDIDataSource);
                }
            });
            undo.endTransaction();
        }

        cachedTableModel = null;

        firePropertyChange(PropertyKeys.SELECTED_JNDI_DATA_SOURCE, oldSelectedJNJndiSource, selectedJNDIDataSource);
    }


    public boolean isUseMondrianCubeDefinition()
    {
        return useMondrianCubeDefinition;
    }


    public void setUseMondrianCubeDefinition(final boolean useMondrianCubeDefinition)
    {
        final boolean oldUseMondrianCubeDefinition = this.useMondrianCubeDefinition;
        this.useMondrianCubeDefinition = useMondrianCubeDefinition;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.USE_MONDRIAN_CUBE_DEFINITION);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setUseMondrianCubeDefinition(oldUseMondrianCubeDefinition);
                }


                public void redo()
                {
                    setUseMondrianCubeDefinition(useMondrianCubeDefinition);
                }
            });
            undo.endTransaction();
        }

        cachedTableModel = null;

        firePropertyChange(PropertyKeys.USE_MONDRIAN_CUBE_DEFINITION, oldUseMondrianCubeDefinition, useMondrianCubeDefinition);
    }


    @Nullable
    public String getXQueryDataFile()
    {
        return xQueryDataFile;
    }


    public void setXQueryDataFile(@Nullable final String xQueryDataFile)
    {
        final String oldXQueryDataFile = this.xQueryDataFile;
        this.xQueryDataFile = xQueryDataFile;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.XQUERY_DATA_FILE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setXQueryDataFile(oldXQueryDataFile);
                }


                public void redo()
                {
                    setXQueryDataFile(xQueryDataFile);
                }
            });
            undo.endTransaction();
        }

        cachedTableModel = null;

        firePropertyChange(PropertyKeys.XQUERY_DATA_FILE, oldXQueryDataFile, xQueryDataFile);
    }


    @Nullable
    public String getXmiDefinitionFile()
    {
        return xmiDefinitionFile;
    }


    public void setXmiDefinitionFile(@Nullable final String xmiDefinitionFile)
    {
        final String oldXmiDefinitionFile = this.xmiDefinitionFile;
        this.xmiDefinitionFile = xmiDefinitionFile;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.XMI_DEFINITION_FILE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setXmiDefinitionFile(oldXmiDefinitionFile);
                }


                public void redo()
                {
                    setXmiDefinitionFile(xmiDefinitionFile);
                }
            });
            undo.endTransaction();
        }

        cachedTableModel = null;

        firePropertyChange(PropertyKeys.XMI_DEFINITION_FILE, oldXmiDefinitionFile, xmiDefinitionFile);
    }


    public boolean canCreateReportOnServer()
    {
        return false;
    }


    public void createReportOnServer(@NotNull String jFreeReportDefinition)
    {
        throw new RuntimeException("Can not create the report on the server");
    }


    public boolean canFetchPreviewDataTableModel()
    {
        return true;
    }


    @NotNull
    public ArrayList<ColumnInfo> fetchColumnInfos(@NotNull ConnectionType connectionType, @Nullable JNDISource source, @Nullable String xQueryDataFile, @Nullable String xmiDefinitionFile, boolean useMondrianCubeDefinition, @Nullable String mondrianCubeDefinition, @NotNull String queryString) throws DataFetchException
    {
        TableModel tableModel = createTableModel(connectionType, source, xQueryDataFile, xmiDefinitionFile, useMondrianCubeDefinition, mondrianCubeDefinition, queryString, 1);

        ArrayList<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
        for (int i = 0; i < tableModel.getColumnCount(); i++)
        {
            columnInfos.add(new ColumnInfo("", tableModel.getColumnName(i), tableModel.getColumnClass(i)));
        }
        return columnInfos;
    }


    @NotNull
    private static TableModel createTableModel(@NotNull ConnectionType connectionType, @Nullable JNDISource selectedJNDIDataSource, @Nullable String xQueryDataFile, @Nullable String xmiDefinitionFile, boolean useMondrianCubeDefinition, @Nullable String mondrianCubeDefinitionFile, @NotNull String queryString, int maxRowsToProcess) throws DataFetchException
    {
        TableModel tableModel = null;

        if (connectionType == ConnectionType.XQuery)
        {
            try
            {
                tableModel = new XPathTableModel(new File(xQueryDataFile).toURI().toURL(), queryString, null, maxRowsToProcess);
            }
            catch (IOException e)
            {
                throw new DataFetchException("Could not read XML data file", e);
            }
            catch (XPathExpressionException e)
            {
                throw new DataFetchException("Invalid query", e);
            }
        }
        else if (connectionType == ConnectionType.JNDI)
        {
            if (useMondrianCubeDefinition)
            {
                try
                {
                    if (selectedJNDIDataSource == null)
                    {
                        throw new DataFetchException("You have to select a JNDI source");
                    }
                    tableModel = new MondrianTableModel(selectedJNDIDataSource, new File(mondrianCubeDefinitionFile).toURI().toURL(), queryString, maxRowsToProcess);
                }
                catch (MalformedURLException e)
                {
                    throw new DataFetchException("Could not read XML file", e);
                }
            }
            else
            {
                if (selectedJNDIDataSource == null)
                {
                    throw new DataFetchException("You have to select a JNDI source");
                }
                try
                {
                    tableModel = new JDBCTableModel(selectedJNDIDataSource, queryString, maxRowsToProcess);
                }
                catch (Exception e)
                {
                    throw new DataFetchException(e.getMessage(), e);
                }
            }
        }
        else if (connectionType == ConnectionType.MQL)
        {
            try
            {
                tableModel = new MQLTableModel(xmiDefinitionFile, queryString, maxRowsToProcess);
            }
            catch (Exception e)
            {
                throw new DataFetchException(e.getMessage(), e);
            }
        }
        return tableModel;
    }


    @NotNull
    public TableModel fetchPreviewDataTableModel() throws DataFetchException
    {
        try
        {
            long l1 = System.nanoTime();
            TableModel ctm = cachedTableModel;
            if (ctm == null)
            {
                if (!queries.isEmpty())
                {
                    ctm = createTableModel(connectionType, selectedJNDIDataSource, xQueryDataFile, xmiDefinitionFile, useMondrianCubeDefinition, mondrianCubeDefinitionFile, queries.get(0).getQuery(), 1000);
                    cachedTableModel = ctm;
                }
                else
                {
                    ctm = new DefaultTableModel();
                }
            }
            long l2 = System.nanoTime();
            if (LOG.isLoggable(Level.FINE))
                LOG.log(Level.FINE, "MultiDataSetReportElement.fetchPreviewDataTableModel " + (l2 - l1) / (1000. * 1000.) + " ms");
            return ctm;
        }
        catch (Throwable e)
        {
            throw new DataFetchException(e);
        }
    }


    public boolean canFetchRealDataTableModel()
    {
        return true;
    }


    @NotNull
    public TableModel fetchRealDataTableModel() throws DataFetchException
    {
        try
        {
            String query = queries.get(0).getQuery();
            return createTableModel(connectionType, selectedJNDIDataSource, xQueryDataFile, xmiDefinitionFile, useMondrianCubeDefinition, mondrianCubeDefinitionFile, query, Integer.MAX_VALUE);
        }
        catch (Throwable e)
        {
            throw new DataFetchException(e);
        }
    }


    @NotNull
    public String getShortSummary()
    {
        if (connectionType == ConnectionType.JNDI)
        {
            JNDISource jndiDataSource = selectedJNDIDataSource;
            if (useMondrianCubeDefinition)
            {
                if (jndiDataSource != null)
                {
                    return getQueryName() + " " + TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElement.Mondrian", mondrianCubeDefinitionFile, jndiDataSource.getJndiName());
                }
                else
                {
                    return getQueryName() + " "
                           + TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElement.Mondrian", mondrianCubeDefinitionFile, TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElement.Undefined"));
                }
            }
            else
            {
                if (jndiDataSource != null)
                {
                    return getQueryName() + " " + TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElement.JNDI", jndiDataSource.getJndiName());
                }
                else
                {
                    return getQueryName() + " " + TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElement.JNDI", TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElement.Undefined"));
                }
            }

        }
        else if (connectionType == ConnectionType.XQuery)
        {
            String xQueryDataFile = this.xQueryDataFile;
            if (xQueryDataFile != null)
            {
                return getQueryName() + " " + TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElement.XQuery", xQueryDataFile);
            }
            else
            {
                return getQueryName() + " " + TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElement.XQuery", "");
            }
        }
        else if (connectionType == ConnectionType.MQL)
        {
            String xmiDefinitionFile = this.xmiDefinitionFile;
            if (xmiDefinitionFile != null)
            {
                return getQueryName() + " " + TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElement.MQL", xmiDefinitionFile);
            }
            else
            {
                return getQueryName() + " " + TranslationManager.getInstance().getTranslation("R", "MultiDataSetReportElement.MQL", "");
            }
        }
        return getQueryName();
    }


    @NotNull
    public HashSet<String> getDefinedFields()
    {
        HashSet<String> definedFields = new HashSet<String>();
        for (ColumnInfo columnInfo : columnInfos)
        {
            definedFields.add(columnInfo.getColumnName());
        }
        return definedFields;
    }


    public boolean canConfigure()
    {
        return true;
    }


    public boolean showConfigurationComponent(@NotNull ReportDialog parent, boolean firsttime)
    {
        boolean ok = MultiDataSetReportElementConfigurator.showStaticFactoryDataSetReportElementConfigurator(parent, this);
        cachedTableModel = null;
        return ok;
    }


    @NotNull
    public JComponent getInfoComponent()
    {
        @NonNls
        FormLayout formLayout = new FormLayout("2dlu, pref, 4dlu, 0dlu:grow, 2dlu", "2dlu, pref, 4dlu, pref, 4dlu, fill:default:grow, 2dlu");
        JPanel infoPanel = new JPanel(formLayout);
        @NonNls
        CellConstraints cc = new CellConstraints();

        infoTable = new JTable(new ColumnInfoTableModel(columnInfos));
        infoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (ReportDialogConstants.DEFAULT_DATA_FACTORY.equals(getQueryName()))
        {
            initDragAndDrop(infoTable);
        }
        else
        {
            infoTable.setEnabled(false);
        }

        infoPanel.add(new JScrollPane(infoTable), cc.xyw(2, 6, 3, "fill, fill"));

        return infoPanel;
    }


    @NotNull
    public ArrayList<ColumnInfo> getColumnInfos()
    {
        return columnInfos;
    }


    public void setColumnInfos(@NotNull final ArrayList<ColumnInfo> columnInfos)
    {
        // noinspection ConstantConditions
        if (columnInfos == null)
        {
            throw new IllegalArgumentException("columnInfos must not be null");
        }

        final ArrayList<ColumnInfo> oldColumnInfos = this.columnInfos;
        this.columnInfos = columnInfos;

        infoTable.setModel(new ColumnInfoTableModel(columnInfos));

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.COLUMN_INFOS);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setColumnInfos(oldColumnInfos);
                }


                public void redo()
                {
                    setColumnInfos(columnInfos);
                }
            });
            undo.endTransaction();
        }
        firePropertyChange(PropertyKeys.COLUMN_INFOS, oldColumnInfos, columnInfos);
    }


    private void initDragAndDrop(@NotNull final JTable table)
    {
        @NonNls
        final DataFlavor dataFlavorLibraryItems = new DataFlavor("application/x-icore-reportelement;class=" + ReportElement.class.getName(), "ReportElement " + ReportDialogConstants.UNSELECTED);

        table.setTransferHandler(new TransferHandler()
        {
            @NotNull
            protected Transferable createTransferable(@NotNull JComponent c)
            {
                TextFieldReportElement textFieldReportElement = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
                int selectedRow = table.getSelectedRow();
                ColumnInfo columnInfo = columnInfos.get(selectedRow);
                textFieldReportElement.setFieldName(columnInfo.getColumnName());
                final ReportElement reportElement = textFieldReportElement;

                return new Transferable()
                {
                    @NotNull
                    public DataFlavor[] getTransferDataFlavors()
                    {
                        return new DataFlavor[]{dataFlavorLibraryItems};
                    }


                    public boolean isDataFlavorSupported(@NotNull DataFlavor flavor)
                    {
                        return dataFlavorLibraryItems.equals(flavor);
                    }


                    @NotNull
                    public Object getTransferData(@NotNull DataFlavor flavor) throws UnsupportedFlavorException
                    {
                        if (dataFlavorLibraryItems.equals(flavor))
                        {
                            return reportElement;
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
                return DnDConstants.ACTION_COPY;
            }
        });

        table.setDragEnabled(true);

    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.CONNECTION_TYPE, connectionType.toString());
        String xQueryDataFile = this.xQueryDataFile;
        if (xQueryDataFile != null)
        {
            xmlWriter.writeProperty(PropertyKeys.XQUERY_DATA_FILE, FileRelativator.getRelativePathFromFile(xmlContext, xQueryDataFile));
        }
        String xmiDefinitionFile = this.xmiDefinitionFile;
        if (xmiDefinitionFile != null)
        {
            xmlWriter.writeProperty(PropertyKeys.XMI_DEFINITION_FILE, FileRelativator.getRelativePathFromFile(xmlContext, xmiDefinitionFile));
        }
        xmlWriter.writeProperty(PropertyKeys.USE_MONDRIAN_CUBE_DEFINITION, Boolean.valueOf(useMondrianCubeDefinition).toString());
        xmlWriter.writeProperty(PropertyKeys.MONDRIAN_CUBE_DEFINITION_FILE, FileRelativator.getRelativePathFromFile(xmlContext, mondrianCubeDefinitionFile));

        for (Query query : queries)
        {
            xmlWriter.startElement(PropertyKeys.QUERY);
            query.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.QUERY);
        }

        for (ColumnInfo columnInfo : columnInfos)
        {
            xmlWriter.startElement(PropertyKeys.COLUMN_INFO);
            columnInfo.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.COLUMN_INFO);
        }

        JNDISource jndiDataSource = selectedJNDIDataSource;
        if (jndiDataSource != null)
        {
            xmlWriter.startElement(PropertyKeys.SELECTED_JNDI_DATA_SOURCE);
            jndiDataSource.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.SELECTED_JNDI_DATA_SOURCE);
        }
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        queries = new ArrayList<Query>();

        super.readObject(node, xmlContext);

        if (queries.isEmpty())
        {
            queries.add(new Query(ReportDialogConstants.DEFAULT_DATA_FACTORY, ""));
        }
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.CONNECTION_TYPE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            connectionType = ConnectionType.valueOf(XMLUtils.readProperty(PropertyKeys.CONNECTION_TYPE, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.XQUERY_DATA_FILE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            xQueryDataFile = FileRelativator.getAbsoluteFile(xmlContext, XMLUtils.readProperty(PropertyKeys.XQUERY_DATA_FILE, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.XMI_DEFINITION_FILE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            xmiDefinitionFile = FileRelativator.getAbsoluteFile(xmlContext, XMLUtils.readProperty(PropertyKeys.XMI_DEFINITION_FILE, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.USE_MONDRIAN_CUBE_DEFINITION.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            useMondrianCubeDefinition = Boolean.valueOf(XMLUtils.readProperty(PropertyKeys.USE_MONDRIAN_CUBE_DEFINITION, node)).booleanValue();
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.MONDRIAN_CUBE_DEFINITION_FILE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            mondrianCubeDefinitionFile = FileRelativator.getAbsoluteFile(xmlContext, XMLUtils.readProperty(PropertyKeys.MONDRIAN_CUBE_DEFINITION_FILE, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.QUERY_STRING.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            Query query = new Query(ReportDialogConstants.DEFAULT_DATA_FACTORY, XMLUtils.readProperty(PropertyKeys.QUERY_STRING, node));
            queries.add(query);
        }
        else if (PropertyKeys.QUERY.equals(node.getRawName()))
        {
            Query query = new Query("", "");
            query.readObject(node, xmlContext);
            queries.add(query);
        }
        else if (PropertyKeys.COLUMN_INFO.equals(node.getRawName()))
        {
            ColumnInfo columnInfo = new ColumnInfo("", "", Object.class);
            columnInfo.readObject(node, xmlContext);
            columnInfos.add(columnInfo);
        }
        else if (PropertyKeys.SELECTED_JNDI_DATA_SOURCE.equals(node.getRawName()))
        {
            JNDISource jndiSource = new JNDISource();
            jndiSource.readObject(node, xmlContext);
            selectedJNDIDataSource = jndiSource;
            if (!jndiSources.contains(selectedJNDIDataSource))
            {
                jndiSources.add(jndiSource);
            }
        }
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /* Object newParent = */
        reportVisitor.visit(parent, this);
    }

}
