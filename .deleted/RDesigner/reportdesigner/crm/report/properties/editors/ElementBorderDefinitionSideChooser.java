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
import org.pentaho.reportdesigner.crm.report.components.PaletteColorChooser;
import org.pentaho.reportdesigner.crm.report.model.ElementBorderDefinition;
import org.pentaho.reportdesigner.crm.report.util.ColorIcon;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: Martin
 * Date: 01.11.2005
 * Time: 10:23:10
 */
public class ElementBorderDefinitionSideChooser
{

    private ElementBorderDefinitionSideChooser()
    {
    }


    @NotNull
    public static ElementBorderDefinition.Side showBorderDefinitionChooser(@NotNull JComponent parent, @NotNull String title, @NotNull ElementBorderDefinition.Side origBorderDefinition)
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

        BorderDefinitionPanel centerPanel = new BorderDefinitionPanel(origBorderDefinition);

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.pack();
        GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 250, 250);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            return centerPanel.getCurrentBorderDefinition();
        }
        //return font;
        return origBorderDefinition;
    }


    private static class SamplePanel extends JPanel
    {
        @NotNull
        private ElementBorderDefinition.Side borderDefinition;


        private SamplePanel(@NotNull ElementBorderDefinition.Side borderDefinition)
        {
            this.borderDefinition = borderDefinition;
        }


        @NotNull
        public ElementBorderDefinition.Side getBorderDefinition()
        {
            return borderDefinition;
        }


        public void setBorderDefinition(@NotNull ElementBorderDefinition.Side borderDefinition)
        {
            this.borderDefinition = borderDefinition;

            repaint();
        }


        protected void paintComponent(@NotNull Graphics g)
        {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            Stroke origStroke = g2d.getStroke();
            Color origColor = g2d.getColor();
            Shape origClip = g2d.getClip();

            if (borderDefinition.getType() != ElementBorderDefinition.BorderType.NONE)
            {
                g2d.setColor(borderDefinition.getColor());
                g2d.setStroke(borderDefinition.getStroke());

                g2d.clip(new Rectangle(getInsets().left + 1, getInsets().top + 1, getWidth() - (getInsets().left + getInsets().right + 2), getHeight() - (getInsets().top + getInsets().bottom + 2)));
                g2d.drawLine(getInsets().left + 1, getHeight() / 2, getWidth() - (getInsets().right + 1), getHeight() / 2);
            }

            g2d.setClip(origClip);
            g2d.setStroke(origStroke);
            g2d.setColor(origColor);
        }
    }

    private static class BorderDefinitionPanel extends JPanel
    {
        @NotNull
        private ElementBorderDefinition.Side borderDefinition;
        @NotNull
        private Color color;
        @NotNull
        private JButton colorButton;
        @NotNull
        private JSpinner widthSpinner;
        @NotNull
        private JComboBox dashComboBox;


        private BorderDefinitionPanel(@NotNull ElementBorderDefinition.Side origBorderDefinition)
        {
            this.borderDefinition = origBorderDefinition;

            @NonNls
            FormLayout formLayout = new FormLayout("0dlu, default:grow, 4dlu, fill:default, 4dlu, default, 0dlu",
                                                   "0dlu, " +
                                                   "pref, " +//color
                                                   "4dlu, " +
                                                   "pref, " +//lineWidth
                                                   "4dlu, " +
                                                   "pref, " +//dash
                                                   "4dlu, " +
                                                   "fill:40dlu, " +//sample
                                                   "0dlu:grow");

            setLayout(formLayout);
            @NonNls
            CellConstraints cc = new CellConstraints();

            JLabel colorLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "BorderDefinitionChooser.ColorLabel"));
            color = borderDefinition.getColor();
            final JLabel colorIconLabel = new JLabel(new ColorIcon(Color.BLACK, color, 120, 20, true));
            colorButton = new JButton("...");
            colorButton.setMargin(new Insets(1, 1, 1, 1));

            JLabel widthLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "BorderDefinitionChooser.WidthLabel"));
            widthSpinner = new JSpinner(new SpinnerNumberModel(borderDefinition.getWidth(), 0, 1000, 0.1));

            JLabel dashLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "BorderDefinitionChooser.DashLabel"));
            dashComboBox = new JComboBox(ElementBorderDefinition.BorderType.values());
            dashComboBox.setSelectedItem(origBorderDefinition.getType());

            final SamplePanel samplePanel = new SamplePanel(borderDefinition);
            samplePanel.setBorder(new TitledBorder(TranslationManager.getInstance().getTranslation("R", "BorderDefinitionPanel.Sample.Title")));

            add(colorLabel, cc.xy(2, 2));
            add(colorIconLabel, cc.xy(4, 2));
            add(colorButton, cc.xy(6, 2));

            add(widthLabel, cc.xy(2, 4));
            add(widthSpinner, cc.xy(4, 4));

            //add(capLabel, cc.xy(2, 6));
            //add(capBox, cc.xy(4, 6));

            //add(joinLabel, cc.xy(2, 8));
            //add(joinBox, cc.xy(4, 8));
            //
            //add(miterLabel, cc.xy(2, 10));
            //add(miterSpinner, cc.xy(4, 10));

            add(dashLabel, cc.xy(2, 6));
            add(dashComboBox, cc.xy(4, 6));

            add(samplePanel, cc.xyw(2, 8, 5));


            dashComboBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    borderDefinition = getCurrentBorderDefinition();
                    samplePanel.setBorderDefinition(borderDefinition);
                }
            });


            colorButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    Color newColor = PaletteColorChooser.showDialog(BorderDefinitionPanel.this, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Color.Title"), color);
                    if (newColor != null)
                    {
                        color = newColor;
                        colorIconLabel.setIcon(new ColorIcon(Color.BLACK, color, 120, 20, true));
                    }

                    borderDefinition = getCurrentBorderDefinition();
                    samplePanel.setBorderDefinition(borderDefinition);
                }
            });

            colorIconLabel.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(@NotNull MouseEvent e)
                {
                    Color newColor = PaletteColorChooser.showDialog(BorderDefinitionPanel.this, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Color.Title"), color);
                    if (newColor != null)
                    {
                        color = newColor;
                        colorIconLabel.setIcon(new ColorIcon(Color.BLACK, color, 120, 20, true));
                    }

                    borderDefinition = getCurrentBorderDefinition();
                    samplePanel.setBorderDefinition(borderDefinition);
                }
            });

            widthSpinner.addChangeListener(new ChangeListener()
            {
                public void stateChanged(@NotNull ChangeEvent e)
                {
                    borderDefinition = getCurrentBorderDefinition();
                    samplePanel.setBorderDefinition(borderDefinition);
                }
            });

        }


        @NotNull
        private ElementBorderDefinition.Side getCurrentBorderDefinition()
        {
            return new ElementBorderDefinition.Side(((Number) widthSpinner.getValue()).doubleValue(), (ElementBorderDefinition.BorderType) dashComboBox.getSelectedItem(), color);
        }
    }

}