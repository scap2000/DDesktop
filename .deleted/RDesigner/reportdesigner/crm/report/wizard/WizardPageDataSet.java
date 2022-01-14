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
package org.pentaho.reportdesigner.crm.report.wizard;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataSetPlugin;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataSetPluginRegistry;
import org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard.WizardPagePageSetup;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 09:48:17
 */
public class WizardPageDataSet extends AbstractWizardPage
{
    @NotNull
    private JPanel centerPanel;
    @NotNull
    private JComboBox dataSetComboBox;
    @NotNull
    private WizardPagePageSetup wizardPagePageSetup;


    public WizardPageDataSet()
    {
        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, max(50dlu;pref), 4dlu, max(100dlu;pref), 4dlu, 10dlu:grow, 0dlu", "0dlu, pref, 4dlu, fill:pref:grow, 0dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();

        centerPanel = new JPanel(formLayout);

        LinkedHashSet<DataSetPlugin> dataSetPlugins = DataSetPluginRegistry.getInstance().getDataSetPlugins();

        for (Iterator<DataSetPlugin> iterator = dataSetPlugins.iterator(); iterator.hasNext();)
        {
            DataSetPlugin dataSetPlugin = iterator.next();
            if (!dataSetPlugin.isWizardable())
            {
                iterator.remove();
            }
        }

        dataSetComboBox = new JComboBox(dataSetPlugins.toArray(new DataSetPlugin[dataSetPlugins.size()]));
        dataSetComboBox.setRenderer(new DefaultListCellRenderer()
        {
            @NotNull
            public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                try
                {
                    if (value instanceof DataSetPlugin)
                    {
                        DataSetPlugin dataSetPlugin = (DataSetPlugin) value;
                        label.setText(dataSetPlugin.getLocalizedName());
                    }
                }
                catch (Throwable e)
                {
                    UncaughtExcpetionsModel.getInstance().addException(e);
                }
                return label;
            }
        });

        JLabel dataSetLabel = ComponentFactory.createLabel("R", "WizardPageDataSet.DataSetLabel", dataSetComboBox);

        centerPanel.add(dataSetLabel, cc.xy(2, 2));
        centerPanel.add(dataSetComboBox, cc.xy(4, 2));

        final JEditorPane descriptionTextArea = new JEditorPane();
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();

        htmlEditorKit.getStyleSheet().addRule("body { font-family: " + dataSetLabel.getFont().getFamily() + ", sans-serif; font-size: " + dataSetLabel.getFont().getSize() + "pt;}");//NON-NLS
        HTMLDocument htmlDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();
        descriptionTextArea.setEditorKit(htmlEditorKit);
        descriptionTextArea.setDocument(htmlDocument);
        descriptionTextArea.setContentType("text/html");//NON-NLS
        descriptionTextArea.setEditable(false);
        TextComponentHelper.installDefaultPopupMenu(descriptionTextArea);
        JScrollPane scrollPane = new JScrollPane(descriptionTextArea);
        scrollPane.setPreferredSize(new Dimension(100, 100));
        centerPanel.add(scrollPane, cc.xyw(4, 4, 3));

        DataSetPlugin selectedItem = (DataSetPlugin) dataSetComboBox.getSelectedItem();
        descriptionTextArea.setText(selectedItem.getLocalizedDescription());

        dataSetComboBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                DataSetPlugin selectedItem = (DataSetPlugin) dataSetComboBox.getSelectedItem();
                descriptionTextArea.setText(selectedItem.getLocalizedDescription());
            }
        });

        wizardPagePageSetup = new WizardPagePageSetup();
    }


    @NotNull
    public JComponent getCenterPanel()
    {
        return centerPanel;
    }


    public boolean canNext()
    {
        return true;
    }


    public boolean hasNext()
    {
        return true;
    }


    @Nullable
    public WizardPage getNext()
    {
        DataSetPlugin dataSetPlugin = (DataSetPlugin) dataSetComboBox.getSelectedItem();
        dataSetPlugin.initWizardPages();
        setNext(dataSetPlugin.getFirstWizardPage());
        AbstractWizardPage wizardPage = dataSetPlugin.getLastWizardPage();
        if (wizardPage != null)
        {
            wizardPage.setNext(wizardPagePageSetup);
        }
        wizardPagePageSetup.setPrevious(wizardPage);


        return super.getNext();
    }


    public boolean canPrevious()
    {
        return false;
    }


    public boolean canCancel()
    {
        return true;
    }


    public boolean canFinish()
    {
        return true;
    }


    @NotNull
    public WizardData[] getWizardDatas()
    {
        DataSetPlugin dataSetPlugin = (DataSetPlugin) dataSetComboBox.getSelectedItem();
        ArrayList<WizardData> wizardDatas = new ArrayList<WizardData>();
        wizardDatas.addAll(Arrays.asList(dataSetPlugin.getInitialWizardDatas()));
        wizardDatas.add(new WizardData(WizardData.DATA_SET_PLUGIN, dataSetComboBox.getSelectedItem()));
        return wizardDatas.toArray(new WizardData[wizardDatas.size()]);
    }


    @NotNull
    public String getTitle()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageDataSet.Title");
    }


    @NotNull
    public ImageIcon getIcon()
    {
        return IconLoader.getInstance().getWizardPageWelcomeIcon();
    }


    @NotNull
    public String getDescription()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPageDataSet.Description");
    }


    public void dispose()
    {
    }
}
