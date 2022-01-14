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
package org.pentaho.reportdesigner.crm.report.datasetplugin.staticfactory;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.ParameterDataRow;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.modules.misc.datafactory.StaticDataFactory;
import org.jfree.report.util.ReportProperties;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.ColumnInfoTableModel;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataFetchException;
import org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertyInfo;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.TableModelDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverter;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 31.01.2006
 * Time: 10:30:43
 */
public class StaticFactoryDataSetReportElement extends TableModelDataSetReportElement
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(StaticFactoryDataSetReportElement.class.getName());

    @NotNull
    private String queryName;
    @NotNull
    private String className;
    @NotNull
    private String methodName;
    @NotNull
    private TreeSet<PropertyInfo> parameters;

    @NotNull
    private ArrayList<ColumnInfo> columnInfos;

    @Nullable
    private TableModel cachedTableModel;
    @Nullable
    private JTable infoTable;


    public StaticFactoryDataSetReportElement()
    {
        queryName = ReportDialogConstants.DEFAULT_DATA_FACTORY;
        className = "";
        methodName = "";
        parameters = new TreeSet<PropertyInfo>();
        columnInfos = new ArrayList<ColumnInfo>();
    }


    @NotNull
    public String getQueryName()
    {
        return queryName;
    }


    public void setQueryName(@NotNull final String queryName)
    {
        final String oldQueryName = this.queryName;
        this.queryName = queryName;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.QUERY_NAME);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setQueryName(oldQueryName);
                }


                public void redo()
                {
                    setQueryName(queryName);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.QUERY_NAME, oldQueryName, queryName);
    }


    @NotNull
    public TreeSet<PropertyInfo> getParameters()
    {
        return parameters;
    }


    public void setParameters(@NotNull final TreeSet<PropertyInfo> parameters)
    {
        final TreeSet<PropertyInfo> oldParameters = this.parameters;
        this.parameters = parameters;

        JTable table = infoTable;
        if (table != null)
        {
            table.setModel(new ParametersTableModel(new ArrayList<PropertyInfo>(parameters)));
        }

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.PARAMETERS);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setParameters(oldParameters);
                }


                public void redo()
                {
                    setParameters(parameters);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.PARAMETERS, oldParameters, parameters);
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
    public String getQuery()
    {
        return StaticFactoryDataSetReportElement.getQuery(className, methodName, parameters);
    }


    @NotNull
    public static String getQuery(@NotNull String className, @NotNull String methodName, @NotNull Collection<PropertyInfo> parameters)
    {
        StringBuilder sb = new StringBuilder(className).append("#").append(methodName);
        //if (!parameters.isEmpty())
        {
            sb.append("(");
        }
        boolean first = true;
        for (PropertyInfo propertyInfo : parameters)
        {
            if (!first)
            {
                sb.append(", ");
            }
            else
            {
                first = false;
            }
            sb.append(propertyInfo.getKey());
        }
        //if (!parameters.isEmpty())
        {
            sb.append(")");
        }
        return sb.toString();
    }


    @NotNull
    public static ReportProperties getReportProperties(@NotNull Collection<PropertyInfo> parameters)
    {
        ReportProperties reportProperties = new ReportProperties();
        for (PropertyInfo propertyInfo : parameters)
        {
            reportProperties.put(propertyInfo.getKey(), propertyInfo.getValue());
        }
        return reportProperties;
    }


    @NotNull
    public TableModel fetchPreviewDataTableModel() throws DataFetchException
    {
        try
        {
            long l1 = System.nanoTime();
            TableModel ctm = cachedTableModel;
            if (cachedTableModel == null)
            {
                StaticDataFactory staticDataFactory = new StaticDataFactory();
                ctm = staticDataFactory.queryData(getQuery(className, methodName, parameters), new ParameterDataRow(getReportProperties(parameters)));
                cachedTableModel = ctm;
            }
            long l2 = System.nanoTime();
            if (StaticFactoryDataSetReportElement.LOG.isLoggable(Level.FINE)) StaticFactoryDataSetReportElement.LOG.log(Level.FINE, "JDBCDataSetReportElement.fetchDataTableModel " + (l2 - l1) / (1000. * 1000.) + " ms");
            return ctm;
        }
        catch (Throwable e)
        {
            throw new DataFetchException(e);
        }
    }


    @NotNull
    public static TableModel fetchPreviewDataTableModel(@NotNull String className, @NotNull String methodName, @NotNull Collection<PropertyInfo> parameters) throws ReportDataFactoryException
    {
        StaticDataFactory staticDataFactory = new StaticDataFactory();
        return staticDataFactory.queryData(getQuery(className, methodName, parameters), new ParameterDataRow(getReportProperties(parameters)));
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
            return StaticFactoryDataSetReportElement.fetchPreviewDataTableModel(className, methodName, parameters);
        }
        catch (Throwable e)
        {
            throw new DataFetchException(e);
        }
    }


    @NotNull
    public String getClassName()
    {
        return className;
    }


    public void setClassName(@NotNull final String className)
    {
        //noinspection ConstantConditions
        if (className == null)
        {
            throw new IllegalArgumentException("className must not be null");
        }

        final String oldClassName = this.className;
        this.className = className;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.CLASS_NAME);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setClassName(oldClassName);
                }


                public void redo()
                {
                    setClassName(className);
                }
            });
            undo.endTransaction();
        }
        firePropertyChange(PropertyKeys.CLASS_NAME, oldClassName, className);
    }


    @NotNull
    public String getMethodName()
    {
        return methodName;
    }


    public void setMethodName(@NotNull final String methodName)
    {
        //noinspection ConstantConditions
        if (methodName == null)
        {
            throw new IllegalArgumentException("methodName must not be null");
        }

        final String oldMethodName = this.methodName;
        this.methodName = methodName;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.METHOD_NAME);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setMethodName(oldMethodName);
                }


                public void redo()
                {
                    setMethodName(methodName);
                }
            });
            undo.endTransaction();
        }
        firePropertyChange(PropertyKeys.METHOD_NAME, oldMethodName, methodName);
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

        //if (infoTable != null)
        //{
        //    infoTable.setModel(new HelperTableModel(columnInfos));
        //}

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


    @NotNull
    public String getShortSummary()
    {
        String niceName = className;
        try
        {
            Class clazz = Class.forName(className);
            if (clazz != null)
            {
                niceName = clazz.getSimpleName();
            }
        }
        catch (ClassNotFoundException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "StaticFactoryDataSetReportElement.getShortSummary ", e);
        }

        StringBuilder sb = new StringBuilder(niceName).append(".").append(methodName).append("(");
        boolean first = true;
        for (PropertyInfo propertyInfo : parameters)
        {
            if (!first)
            {
                sb.append(", ");
            }
            else
            {
                first = false;
            }
            sb.append(propertyInfo.getValue());
        }
        sb.append(")");
        return getQueryName() + " " + sb.toString();
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
        boolean ok = StaticFactoryDataSetReportElementConfigurator.showStaticFactoryDataSetReportElementConfigurator(parent, this);
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

        infoPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "StaticFactoryDataSetReportElement.ClassName")), cc.xy(2, 2));
        JLabel classLabel = new JLabel(className);
        classLabel.setToolTipText(className);
        infoPanel.add(classLabel, cc.xy(4, 2));

        infoPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "StaticFactoryDataSetReportElement.MethodName")), cc.xy(2, 4));
        JLabel methodLabel = new JLabel(methodName);
        methodLabel.setToolTipText(methodName);
        infoPanel.add(methodLabel, cc.xy(4, 4));

        JTable table = new JTable(new ColumnInfoTableModel(columnInfos));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        initDragAndDrop(table);
        infoTable = table;

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

        xmlWriter.writeProperty(PropertyKeys.QUERY_NAME, queryName);
        xmlWriter.writeProperty(PropertyKeys.CLASS_NAME, className);
        xmlWriter.writeProperty(PropertyKeys.METHOD_NAME, methodName);

        for (ColumnInfo columnInfo : columnInfos)
        {
            xmlWriter.startElement(PropertyKeys.COLUMN_INFO);
            columnInfo.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.COLUMN_INFO);
        }

        xmlWriter.startElement(PropertyKeys.PARAMETERS);
        for (PropertyInfo propertyInfo : parameters)
        {
            Object value = propertyInfo.getValue();
            if (value != null)
            {
                ObjectConverter converter = ObjectConverterFactory.getInstance().getConverter(value.getClass(), null, xmlContext);
                if (converter != null)
                {
                    xmlWriter.writeProperty(propertyInfo.getKey(), propertyInfo.getClazz().getName(), converter.getString(value));
                }
            }
        }
        xmlWriter.closeElement(PropertyKeys.PARAMETERS);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.QUERY_NAME.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            queryName = XMLUtils.readProperty(PropertyKeys.QUERY_NAME, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.CLASS_NAME.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            className = XMLUtils.readProperty(PropertyKeys.CLASS_NAME, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.METHOD_NAME.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            methodName = XMLUtils.readProperty(PropertyKeys.METHOD_NAME, node);
        }
        else if (PropertyKeys.PARAMETERS.equals(node.getRawName()))
        {
            while (!node.isFinished())
            {
                Object child = node.readNextChild();
                if (child instanceof XmlPullNode)
                {
                    XmlPullNode xmlPullNode = (XmlPullNode) child;
                    if (XMLConstants.PROPERTY.equals(xmlPullNode.getRawName()))
                    {
                        try
                        {
                            String name = xmlPullNode.getAttributeValueFromRawName(XMLConstants.NAME);
                            String type = xmlPullNode.getAttributeValueFromRawName(XMLConstants.TYPE);
                            while (!xmlPullNode.isFinished())
                            {
                                Object value = xmlPullNode.readNextChild();
                                if (value instanceof String)
                                {
                                    String s = (String) value;
                                    Class<?> clazz = Class.forName(type);
                                    ObjectConverter converter = ObjectConverterFactory.getInstance().getConverter(clazz, null, xmlContext);
                                    if (converter != null)
                                    {
                                        Object object = converter.getObject(s);
                                        PropertyInfo propertyInfo = new PropertyInfo(name, clazz, object);
                                        parameters.add(propertyInfo);
                                    }
                                }
                            }
                        }
                        catch (ClassNotFoundException e)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertiesDataSetReportElement.readElement ", e);
                        }
                    }
                }
            }
        }
        else if (PropertyKeys.COLUMN_INFO.equals(node.getRawName()))
        {
            ColumnInfo columnInfo = new ColumnInfo("", "", Object.class);
            columnInfo.readObject(node, xmlContext);
            columnInfos.add(columnInfo);
        }

    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }

}
