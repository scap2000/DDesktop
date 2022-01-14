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
import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.connection.ConnectionFactory;
import org.pentaho.reportdesigner.crm.report.connection.MetaDataService;
import org.pentaho.reportdesigner.crm.report.datasetplugin.ColumnInfoTableModel;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataFetchException;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.TableModelDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 31.01.2006
 * Time: 10:30:43
 */
public class JDBCDataSetReportElement extends TableModelDataSetReportElement
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(JDBCDataSetReportElement.class.getName());

    @NotNull
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private int maxPreviewRows;
    @NotNull
    private String[] jars;
    @NotNull
    private String userName;
    @NotNull
    private String password;
    @NotNull
    private String driverClass;
    @NotNull
    private String sqlQuery;
    @NotNull
    private String connectionString;

    @NotNull
    private ArrayList<ColumnInfo> columnInfos;

    @Nullable
    private TableModel cachedTableModel;
    @Nullable
    private JTable infoTable;


    public JDBCDataSetReportElement()
    {
        jars = EMPTY_STRING_ARRAY;
        userName = "";
        password = "";
        driverClass = "";
        sqlQuery = "";
        connectionString = "";

        columnInfos = new ArrayList<ColumnInfo>();
        maxPreviewRows = 100;
    }


    @NotNull
    public String getQueryName()
    {
        return ReportDialogConstants.DEFAULT_DATA_FACTORY;
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
    public TableModel fetchPreviewDataTableModel() throws DataFetchException
    {
        MetaDataService metaDataService = null;
        try
        {
            long l1 = System.nanoTime();
            TableModel cachedTM = cachedTableModel;
            if (cachedTM == null)
            {
                metaDataService = ConnectionFactory.getJDBCService(jars, driverClass, connectionString, userName, password);
                ArrayList<ArrayList<Object>> queryData = metaDataService.getQueryData(maxPreviewRows, sqlQuery);
                cachedTM = new ReportDataTableModel(metaDataService.getQueryMetaData(sqlQuery), queryData);
                cachedTableModel = cachedTM;
            }
            long l2 = System.nanoTime();
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCDataSetReportElement.fetchDataTableModel " + (l2 - l1) / (1000. * 1000.) + " ms");
            return cachedTM;
        }
        catch (Throwable e)
        {
            throw new DataFetchException(e);
        }
        finally
        {
            if (metaDataService != null)
            {
                metaDataService.dispose();
            }
        }
    }


    public boolean canFetchRealDataTableModel()
    {
        return true;
    }


    @NotNull
    public TableModel fetchRealDataTableModel() throws DataFetchException
    {
        MetaDataService metaDataService = null;
        try
        {
            metaDataService = ConnectionFactory.getJDBCService(jars, driverClass, connectionString, userName, password);
            ArrayList<ArrayList<Object>> queryData = metaDataService.getQueryData(Integer.MAX_VALUE, sqlQuery);
            return new ReportDataTableModel(metaDataService.getQueryMetaData(sqlQuery), queryData);
        }
        catch (Throwable e)
        {
            throw new DataFetchException(e);
        }
        finally
        {
            if (metaDataService != null)
            {
                metaDataService.dispose();
            }
        }
    }


    @NotNull
    public ArrayList<ColumnInfo> getColumnInfos()
    {
        return columnInfos;
    }


    public void setColumnInfos(@NotNull final ArrayList<ColumnInfo> columnInfos)
    {
        //noinspection ConstantConditions
        if (columnInfos == null)
        {
            throw new IllegalArgumentException("columnInfos must not be null");
        }

        final ArrayList<ColumnInfo> oldColumnInfos = this.columnInfos;
        this.columnInfos = columnInfos;

        JTable infoTable = this.infoTable;
        if (infoTable != null)
        {
            infoTable.setModel(new ColumnInfoTableModel(columnInfos));
        }

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
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
        }
        firePropertyChange(PropertyKeys.COLUMN_INFOS, oldColumnInfos, columnInfos);
    }


    @NotNull
    public String[] getJars()
    {
        return jars;
    }


    public void setJars(@NotNull final String[] jars)
    {
        //noinspection ConstantConditions
        if (jars == null)
        {
            throw new IllegalArgumentException("jar must not be null");
        }

        final String[] oldJars = this.jars;
        this.jars = jars;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setJars(oldJars);
                }


                public void redo()
                {
                    setJars(jars);
                }
            });
        }

        firePropertyChange(PropertyKeys.JARS, oldJars, jars);
    }


    public int getMaxPreviewRows()
    {
        return maxPreviewRows;
    }


    public void setMaxPreviewRows(final int maxPreviewRows)
    {
        final int oldMaxPreviewRows = this.maxPreviewRows;
        this.maxPreviewRows = maxPreviewRows;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setMaxPreviewRows(oldMaxPreviewRows);
                }


                public void redo()
                {
                    setMaxPreviewRows(maxPreviewRows);
                }
            });
        }

        firePropertyChange(PropertyKeys.MAX_PREVIEW_ROWS, Integer.valueOf(oldMaxPreviewRows), Integer.valueOf(maxPreviewRows));
    }


    @NotNull
    public String getUserName()
    {
        return userName;
    }


    public void setUserName(@NotNull final String userName)
    {
        //noinspection ConstantConditions
        if (userName == null)
        {
            throw new IllegalArgumentException("userName must not be null");
        }

        final String oldUserName = this.userName;
        this.userName = userName;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setUserName(oldUserName);
                }


                public void redo()
                {
                    setUserName(userName);
                }
            });
        }

        firePropertyChange(PropertyKeys.USER_NAME, oldUserName, userName);
    }


    @NotNull
    public String getPassword()
    {
        return password;
    }


    public void setPassword(@NotNull final String password)
    {
        //noinspection ConstantConditions
        if (password == null)
        {
            throw new IllegalArgumentException("password must not be null");
        }

        final String oldPassword = this.password;
        this.password = password;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setPassword(oldPassword);
                }


                public void redo()
                {
                    setPassword(password);
                }
            });
        }
        firePropertyChange(PropertyKeys.PASSWORD, oldPassword, password);
    }


    @NotNull
    public String getDriverClass()
    {
        return driverClass;
    }


    public void setDriverClass(@NotNull final String driverClass)
    {
        //noinspection ConstantConditions
        if (driverClass == null)
        {
            throw new IllegalArgumentException("driverClass must not be null");
        }

        final String oldDriverClass = this.driverClass;
        this.driverClass = driverClass;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setDriverClass(oldDriverClass);
                }


                public void redo()
                {
                    setDriverClass(driverClass);
                }
            });
        }

        firePropertyChange(PropertyKeys.DRIVER_CLASS, oldDriverClass, driverClass);
    }


    @NotNull
    public String getSqlQuery()
    {
        return sqlQuery;
    }


    public void setSqlQuery(@NotNull final String sqlQuery)
    {
        //noinspection ConstantConditions
        if (sqlQuery == null)
        {
            throw new IllegalArgumentException("sqlQuery must not be null");
        }

        final String oldSqlQuery = this.sqlQuery;
        this.sqlQuery = sqlQuery;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setSqlQuery(oldSqlQuery);
                }


                public void redo()
                {
                    setSqlQuery(sqlQuery);
                }
            });
        }
        firePropertyChange(PropertyKeys.SQL_QUERY, oldSqlQuery, sqlQuery);
    }


    @NotNull
    public String getConnectionString()
    {
        return connectionString;
    }


    public void setConnectionString(@NotNull final String connectionString)
    {
        //noinspection ConstantConditions
        if (connectionString == null)
        {
            throw new IllegalArgumentException("connectionString must not be null");
        }

        final String oldConnectionString = this.connectionString;
        this.connectionString = connectionString;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setConnectionString(oldConnectionString);
                }


                public void redo()
                {
                    setConnectionString(connectionString);
                }
            });
        }
        firePropertyChange(PropertyKeys.CONNECTION_STRING, oldConnectionString, connectionString);
    }


    @NotNull
    public String getShortSummary()
    {
        return getQueryName() + " " + connectionString;
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
        boolean ok = JDBCDataSetReportElementConfigurator.showJDBCDataSetReportElementConfigurator(parent, this, firsttime);
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

        infoPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "JDBCDataSetReportElement.Username")), cc.xy(2, 2));
        infoPanel.add(new JLabel(userName), cc.xy(4, 2));

        infoPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "JDBCDataSetReportElement.Connection")), cc.xy(2, 4));
        JLabel label = new JLabel(connectionString);
        label.setToolTipText(connectionString);
        infoPanel.add(label, cc.xy(4, 4));

        JTable infoTable;
        infoTable = new JTable(new ColumnInfoTableModel(columnInfos));
        infoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        initDragAndDrop(infoTable);
        this.infoTable = infoTable;

        infoPanel.add(new JScrollPane(infoTable), cc.xyw(2, 6, 3, "fill, fill"));

        return infoPanel;
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
        if (jars.length > 0)
        {
            xmlWriter.startElement(XMLConstants.PROPERTY);
            xmlWriter.writeAttribute(XMLConstants.NAME, PropertyKeys.JARS);
            xmlWriter.writeAttribute(XMLConstants.ARRAY, "true");
            for (int i = 0; i < jars.length; i++)
            {
                String jar = jars[i];
                if (jar != null)
                {
                    xmlWriter.writeProperty(String.valueOf(i), jar);
                }
            }
            xmlWriter.closeElement(XMLConstants.PROPERTY);
        }
        xmlWriter.writeProperty(PropertyKeys.USER_NAME, userName);
        xmlWriter.writeProperty(PropertyKeys.PASSWORD, password);
        xmlWriter.writeProperty(PropertyKeys.DRIVER_CLASS, driverClass);
        xmlWriter.writeProperty(PropertyKeys.SQL_QUERY, sqlQuery);
        xmlWriter.writeProperty(PropertyKeys.CONNECTION_STRING, connectionString);
        xmlWriter.writeProperty(PropertyKeys.MAX_PREVIEW_ROWS, String.valueOf(maxPreviewRows));

        for (ColumnInfo columnInfo : columnInfos)
        {
            xmlWriter.startElement(PropertyKeys.COLUMN_INFO);
            columnInfo.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.COLUMN_INFO);
        }
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.JARS.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            if (node.getAttributeValueFromRawName(XMLConstants.ARRAY) != null)
            {
                ArrayList<String> al = new ArrayList<String>();
                int n = 0;
                while (!node.isFinished())
                {
                    Object childNodeList = node.readNextChild();
                    if (childNodeList instanceof XmlPullNode)
                    {
                        XmlPullNode child = (XmlPullNode) childNodeList;
                        if (XMLConstants.PROPERTY.equals(child.getRawName()))
                        {
                            al.add(XMLUtils.readProperty(String.valueOf(n), child));
                            n++;
                        }
                    }
                }
                jars = al.toArray(new String[al.size()]);
            }
        }
        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.USER_NAME.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            userName = XMLUtils.readProperty(PropertyKeys.USER_NAME, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.PASSWORD.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            password = XMLUtils.readProperty(PropertyKeys.PASSWORD, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.DRIVER_CLASS.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            driverClass = XMLUtils.readProperty(PropertyKeys.DRIVER_CLASS, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.SQL_QUERY.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            sqlQuery = XMLUtils.readProperty(PropertyKeys.SQL_QUERY, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.CONNECTION_STRING.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            connectionString = XMLUtils.readProperty(PropertyKeys.CONNECTION_STRING, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.MAX_PREVIEW_ROWS.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            maxPreviewRows = Integer.parseInt(XMLUtils.readProperty(PropertyKeys.MAX_PREVIEW_ROWS, node));
        }
        else if (PropertyKeys.COLUMN_INFO.equals(node.getRawName()))
        {
            ColumnInfo columnInfo = new ColumnInfo("", "", Object.class);
            columnInfo.readObject(node, xmlContext);
            columnInfos.add(columnInfo);
        }
    }


}
