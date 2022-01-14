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
package org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.model.PageDefinition;
import org.pentaho.reportdesigner.crm.report.model.PageFormatPreset;
import org.pentaho.reportdesigner.crm.report.model.PageOrientation;
import org.pentaho.reportdesigner.crm.report.properties.editors.PageDefinitionChooser;
import org.pentaho.reportdesigner.crm.report.templateplugin.LayoutPlugin;
import org.pentaho.reportdesigner.crm.report.templateplugin.LayoutPluginRegistry;
import org.pentaho.reportdesigner.crm.report.wizard.AbstractWizardPage;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 09:48:17
 */
public class WizardPagePageSetup extends AbstractWizardPage
{
    @NotNull
    private JPanel centerPanel;
    @NotNull
    private PageDefinition pageDefinition;
    @NotNull
    private LayoutPlugin layoutPlugin;


    public WizardPagePageSetup()
    {
    }


    @NotNull
    public JComponent getCenterPanel()
    {
        if (centerPanel == null)
        {
            layoutPlugin = LayoutPluginRegistry.getInstance().getLayoutPlugins().iterator().next();//at least one has to be available

            pageDefinition = new PageDefinition(PageFormatPreset.A4, PageOrientation.PORTRAIT, 20, 20, 20, 20);

            @NonNls
            FormLayout formLayout = new FormLayout("4dlu, default, 4dlu, max(80dlu;default), 4dlu, default, 4dlu:grow",
                                                   "4dlu, " +
                                                   "pref, " +
                                                   "4dlu, " +
                                                   "pref, " +
                                                   "4dlu, " +
                                                   "pref, " +
                                                   "4dlu:grow");
            centerPanel = new JPanel(formLayout);
            CellConstraints cc = new CellConstraints();

            final JTextField pageDefinitionTextField = new JTextField(pageDefinition.getNiceName());
            pageDefinitionTextField.setEditable(false);

            JLabel pageSetupLabel = ComponentFactory.createLabel("R", "WizardPagePageSetup.PaperLabel", pageDefinitionTextField);

            centerPanel.add(pageSetupLabel, cc.xy(2, 2));
            centerPanel.add(pageDefinitionTextField, cc.xy(4, 2));

            JButton pageSetupButton = ComponentFactory.createButton("R", "WizardPagePageSetup.PageSetupButton");
            pageSetupButton.setMargin(new Insets(1, 1, 1, 1));
            centerPanel.add(pageSetupButton, cc.xy(6, 2));

            pageSetupButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    pageDefinition = PageDefinitionChooser.showPageDefinitionChooser(getWizardDialog().getReportDialog(), getWizardDialog().getRootPane(), TranslationManager.getInstance().getTranslation("R", "PropertyEditor.PageDefinition.Title"), WizardPagePageSetup.this.pageDefinition);
                    pageDefinitionTextField.setText(pageDefinition.getNiceName());
                }
            });


            JLabel templateLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "WizardPagePageSetup.TemplateLabel"));
            centerPanel.add(templateLabel, cc.xy(2, 4));

            LinkedHashSet<LayoutPlugin> layoutPlugins = LayoutPluginRegistry.getInstance().getLayoutPlugins();
            final JComboBox templateComboBox = new JComboBox(layoutPlugins.toArray(new LayoutPlugin[layoutPlugins.size()]));

            templateComboBox.setSelectedItem(layoutPlugin);
            centerPanel.add(templateComboBox, cc.xy(4, 4));

            final JLabel templateSampleImageLabel = new JLabel(layoutPlugin.getSampleImage());
            templateSampleImageLabel.setBackground(Color.WHITE);
            templateSampleImageLabel.setOpaque(true);
            templateSampleImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            centerPanel.add(templateSampleImageLabel, cc.xy(4, 6));

            templateComboBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    layoutPlugin = (LayoutPlugin) templateComboBox.getSelectedItem();
                    templateSampleImageLabel.setIcon(layoutPlugin.getSampleImage());
                }
            });

            templateComboBox.setRenderer(new DefaultListCellRenderer()
            {
                @NotNull
                public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
                {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    try
                    {
                        if (value instanceof LayoutPlugin)
                        {
                            LayoutPlugin plugin = (LayoutPlugin) value;
                            label.setText(plugin.getLocalizedName());
                        }
                    }
                    catch (Throwable e)
                    {
                        UncaughtExcpetionsModel.getInstance().addException(e);
                    }
                    return label;
                }
            });
        }
        return centerPanel;
    }


    public boolean canNext()
    {
        return true;
    }


    public boolean canPrevious()
    {
        return true;
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
        return new WizardData[]{
                new WizardData(WizardData.PAGE_DEFINITION, pageDefinition),
                new WizardData(WizardData.LAYOUT_PLUGIN, layoutPlugin)
        };
    }


    @NotNull
    public String getTitle()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPagePageSetup.Title");
    }


    @NotNull
    public ImageIcon getIcon()
    {
        return IconLoader.getInstance().getWizardPagePageSetupIcon();
    }


    @NotNull
    public String getDescription()
    {
        return TranslationManager.getInstance().getTranslation("R", "WizardPagePageSetup.Description");
    }


    public void dispose()
    {
    }

}
