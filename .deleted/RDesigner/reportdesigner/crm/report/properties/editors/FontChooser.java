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
package org.pentaho.reportdesigner.crm.report.properties.editors;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 01.11.2005
 * Time: 10:23:10
 */
public class FontChooser
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(FontChooser.class.getName());


    private FontChooser()
    {
    }


    @Nullable
    public static Font showFontChooser(@NotNull JComponent parent, @NotNull String title, @Nullable Font font)
    {
        final CenterPanelDialog centerPanelDialog;
        Window windowAncestor = SwingUtilities.getWindowAncestor(parent);

        if (windowAncestor instanceof Dialog)
        {
            centerPanelDialog = new CenterPanelDialog((Dialog) windowAncestor, title, true);
        }
        else
        {
            centerPanelDialog = new CenterPanelDialog((Frame) windowAncestor, title, true);
        }

        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, fill:default:grow, 4dlu, fill:default:grow, 4dlu, fill:default:grow, 0dlu",
                                               "0dlu, " +
                                               "pref, " +
                                               "4dlu, " +
                                               "fill:default:grow, " +
                                               "4dlu, " +
                                               "fill:40dlu, " +
                                               "0dlu");

        JPanel centerPanel = new JPanel(formLayout);
        @NonNls
        CellConstraints cc = new CellConstraints();

        DefaultListModel nameListModel = new DefaultListModel();
        String[] availableFontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String famName : availableFontFamilyNames)
        {
            nameListModel.addElement(famName);
        }
        final JList nameList = new JList(nameListModel);
        nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        centerPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "FontChooser.Name")), cc.xy(2, 2));
        centerPanel.add(new JScrollPane(nameList), cc.xy(2, 4));

        //noinspection AutoBoxing
        final DefaultComboBoxModel sizeComboBoxModel = new DefaultComboBoxModel(new Object[]{6, 8, 10, 12, 14, 16, 18, 20, 24, 28, 36});
        final JComboBox comboBox = new JComboBox(sizeComboBoxModel);
        comboBox.setEditable(true);

        centerPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "FontChooser.Size")), cc.xy(4, 2));
        centerPanel.add(comboBox, cc.xy(4, 4, "fill, top"));

        DefaultListModel styleListModel = new DefaultListModel();
        styleListModel.addElement(Integer.valueOf(Font.PLAIN));
        styleListModel.addElement(Integer.valueOf(Font.BOLD));
        styleListModel.addElement(Integer.valueOf(Font.ITALIC));
        styleListModel.addElement(Integer.valueOf(Font.ITALIC | Font.BOLD));
        final JList styleList = new JList(styleListModel);
        styleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        centerPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "FontChooser.Style")), cc.xy(6, 2));
        centerPanel.add(new JScrollPane(styleList), cc.xy(6, 4));

        final JLabel sampleLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "FontChooser.SampleText"));
        sampleLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        centerPanel.add(sampleLabel, cc.xyw(2, 6, 5));


        styleList.setCellRenderer(new FontChooser.StyleListCellRenderer());

        if (font != null)
        {
            sampleLabel.setFont(font);
            nameList.setSelectedValue(font.getFamily(), true);
            comboBox.setSelectedItem(Integer.valueOf(font.getSize()));

            styleList.setSelectedValue(Integer.valueOf(font.getStyle()), true);
        }

        nameList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(@NotNull ListSelectionEvent e)
            {
                Font f = new Font((String) nameList.getSelectedValue(), ((Integer) styleList.getSelectedValue()).intValue(), ((Integer) comboBox.getSelectedItem()).intValue());
                sampleLabel.setFont(f);
            }
        });

        styleList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(@NotNull ListSelectionEvent e)
            {
                Font f = new Font((String) nameList.getSelectedValue(), ((Integer) styleList.getSelectedValue()).intValue(), ((Integer) comboBox.getSelectedItem()).intValue());
                sampleLabel.setFont(f);
            }
        });

        comboBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                int size = 0;
                try
                {
                    size = ((Integer) comboBox.getSelectedItem()).intValue();
                }
                catch (Exception e1)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FontChooser.actionPerformed ", e1);
                    comboBox.setSelectedItem(Integer.valueOf(sampleLabel.getFont().getSize()));
                }

                Font f = new Font((String) nameList.getSelectedValue(), ((Integer) styleList.getSelectedValue()).intValue(), size);
                sampleLabel.setFont(f);
            }
        });

        final boolean[] action = new boolean[]{false};

        JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                action[0] = true;
                centerPanelDialog.dispose();
            }
        });

        JButton cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                action[0] = false;
                centerPanelDialog.dispose();
            }
        });

        centerPanelDialog.setButtons(okButton, cancelButton, okButton, cancelButton);

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.setSize(400, 400);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            return sampleLabel.getFont();
        }
        return font;
    }


    private static class StyleListCellRenderer extends DefaultListCellRenderer
    {
        @NotNull
        private HashMap<Integer, String> values;


        private StyleListCellRenderer()
        {
            values = new HashMap<Integer, String>();
            values.put(Integer.valueOf(Font.PLAIN), TranslationManager.getInstance().getTranslation("R", "FontChooser.Style.Plain"));
            values.put(Integer.valueOf(Font.BOLD), TranslationManager.getInstance().getTranslation("R", "FontChooser.Style.Bold"));
            values.put(Integer.valueOf(Font.ITALIC), TranslationManager.getInstance().getTranslation("R", "FontChooser.Style.Italic"));
            values.put(Integer.valueOf(Font.BOLD | Font.ITALIC), TranslationManager.getInstance().getTranslation("R", "FontChooser.Style.BoldItalic"));
        }


        @NotNull
        public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            //noinspection SuspiciousMethodCalls
            return super.getListCellRendererComponent(list, values.get(value), index, isSelected, cellHasFocus);
        }
    }

}
