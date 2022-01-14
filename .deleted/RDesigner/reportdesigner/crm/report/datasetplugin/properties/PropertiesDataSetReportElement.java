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
package org.pentaho.reportdesigner.crm.report.datasetplugin.properties;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverter;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 31.01.2006
 * Time: 10:31:01
 */
public class PropertiesDataSetReportElement extends DataSetReportElement
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(PropertiesDataSetReportElement.class.getName());

    @NotNull
    private TreeSet<PropertyInfo> properties;
    @NotNull
    private PropertiesTable infoTable;


    public PropertiesDataSetReportElement()
    {
        properties = new TreeSet<PropertyInfo>();
    }


    @NotNull
    public String getQueryName()
    {
        return "";//not used only one properties datase is possible and is handled different to the other DataSetReportElements
    }


    public boolean canPreview()
    {
        return true;
    }


    public boolean canConfigure()
    {
        return true;
    }


    @NotNull
    public TreeSet<PropertyInfo> getProperties()
    {
        return properties;
    }


    public void setProperties(@NotNull final TreeSet<PropertyInfo> properties)
    {
        final TreeSet<PropertyInfo> oldProperties = this.properties;
        this.properties = properties;

        if (infoTable != null)
        {
            infoTable.setModel(new PropertiesTableModel(new ArrayList<PropertyInfo>(properties)));
        }

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.PROPERTIES);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setProperties(oldProperties);
                }


                public void redo()
                {
                    setProperties(properties);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.PROPERTIES, oldProperties, properties);
    }


    public boolean showConfigurationComponent(@NotNull final ReportDialog parent, boolean firsttime)
    {
        final CenterPanelDialog centerPanelDialog = new CenterPanelDialog(parent, TranslationManager.getInstance().getTranslation("R", "PropertiesDataSetConfigurator.Title"), true);

        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, fill:10dlu:grow, 4dlu, pref, 0dlu", "0dlu, pref, 4dlu, pref, 4dlu, pref:grow, 0dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();
        JPanel configPanel = new JPanel(formLayout);

        ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();
        propertyInfos.addAll(properties);
        final PropertiesTableModel tableModel = new PropertiesTableModel(propertyInfos);
        final PropertiesTable table = new PropertiesTable(tableModel, true);

        configPanel.add(new JScrollPane(table), cc.xywh(2, 2, 1, 5, "fill, fill"));
        JButton addButton = new JButton(TranslationManager.getInstance().getTranslation("R", "PropertiesDataSetConfigurator.ButtonAdd"));
        configPanel.add(addButton, cc.xy(4, 2));
        final JButton removeButton = new JButton(TranslationManager.getInstance().getTranslation("R", "PropertiesDataSetConfigurator.ButtonRemove"));
        configPanel.add(removeButton, cc.xy(4, 4));

        removeButton.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(@NotNull ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    removeButton.setEnabled(table.getSelectedRows().length > 0);
                }
            }
        });
        removeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                table.editingStopped(new ChangeEvent(this));
                tableModel.removeProperties(table.getSelectedRows());
            }
        });

        addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                table.editingStopped(new ChangeEvent(this));
                tableModel.addProperty(new PropertyInfo("", String.class, null));
            }
        });

        centerPanelDialog.setCenterPanel(configPanel);

        JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));

        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                table.editingStopped(new ChangeEvent(this));
                setProperties(new TreeSet<PropertyInfo>(tableModel.getProperties()));
                parent.getWorkspaceSettings().storeDialogBounds(centerPanelDialog, "PropertiesDataSetConfigurator");
                centerPanelDialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                parent.getWorkspaceSettings().storeDialogBounds(centerPanelDialog, "PropertiesDataSetConfigurator");
                centerPanelDialog.dispose();
            }
        });

        centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

        if (!parent.getWorkspaceSettings().restoreDialogBounds(centerPanelDialog, "PropertiesDataSetConfigurator"))
        {
            centerPanelDialog.pack();
            GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 400, 400);
        }
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        return true;
    }


    @NotNull
    public HashSet<String> getDefinedFields()
    {
        HashSet<String> definedFields = new HashSet<String>();
        for (PropertyInfo propertyInfo : properties)
        {
            definedFields.add(propertyInfo.getKey());
        }
        return definedFields;
    }


    @NotNull
    public JComponent getInfoComponent()
    {
        ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();
        propertyInfos.addAll(properties);
        infoTable = new PropertiesTable(new PropertiesTableModel(propertyInfos), false);
        return new JScrollPane(infoTable);
    }


    @NotNull
    public String getShortSummary()
    {
        return "";
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.startElement(PropertyKeys.PROPERTIES);
        for (PropertyInfo propertyInfo : properties)
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
            else
            {
                xmlWriter.writeProperty(propertyInfo.getKey(), propertyInfo.getClazz().getName(), "");
            }
        }
        xmlWriter.closeElement(PropertyKeys.PROPERTIES);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (PropertyKeys.PROPERTIES.equals(node.getRawName()))
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
                                if (value == null)
                                {
                                    value = "";
                                }
                                if (value instanceof String)
                                {
                                    String s = (String) value;
                                    Class<?> clazz = Class.forName(type);
                                    if ("".equals(s) && Boolean.class.equals(clazz))
                                    {
                                        PropertyInfo propertyInfo = new PropertyInfo(name, clazz, Boolean.FALSE);
                                        properties.add(propertyInfo);
                                    }
                                    else if ("".equals(s) && Integer.class.equals(clazz))
                                    {
                                        PropertyInfo propertyInfo = new PropertyInfo(name, clazz, null);
                                        properties.add(propertyInfo);
                                    }
                                    else if ("".equals(s) && Date.class.equals(clazz))
                                    {
                                        PropertyInfo propertyInfo = new PropertyInfo(name, clazz, null);
                                        properties.add(propertyInfo);
                                    }
                                    else if ("".equals(s) && Double.class.equals(clazz))
                                    {
                                        PropertyInfo propertyInfo = new PropertyInfo(name, clazz, null);
                                        properties.add(propertyInfo);
                                    }
                                    else
                                    {
                                        ObjectConverter converter = ObjectConverterFactory.getInstance().getConverter(clazz, null, xmlContext);
                                        if (converter != null)
                                        {
                                            Object object = converter.getObject(s);
                                            PropertyInfo propertyInfo = new PropertyInfo(name, clazz, object);
                                            properties.add(propertyInfo);
                                        }
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
    }

}
