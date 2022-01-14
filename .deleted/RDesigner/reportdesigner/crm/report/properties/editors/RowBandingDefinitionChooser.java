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
import org.pentaho.reportdesigner.crm.report.model.RowBandingDefinition;
import org.pentaho.reportdesigner.crm.report.util.GraphicUtils;
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
public class RowBandingDefinitionChooser
{

    private RowBandingDefinitionChooser()
    {
    }


    @NotNull
    public static RowBandingDefinition showBorderDefinitionChooser(@NotNull JComponent parent, @NotNull String title, @NotNull RowBandingDefinition origRowBandingDefinition)
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

        RowBandingDefinitionPanel centerPanel = new RowBandingDefinitionPanel(origRowBandingDefinition);

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.pack();
        GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 250, 400);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            return centerPanel.getCurrentRowBandingDefinition();
        }
        //return font;
        return origRowBandingDefinition;
    }


    private static class SamplePanel extends JPanel
    {
        @NotNull
        private RowBandingDefinition rowBandingDefinition;


        private SamplePanel(@NotNull RowBandingDefinition rowBandingDefinition)
        {
            this.rowBandingDefinition = rowBandingDefinition;
        }


        @NotNull
        public RowBandingDefinition getRowBandingDefinition()
        {
            return rowBandingDefinition;
        }


        public void setRowBandingDefinition(@NotNull RowBandingDefinition rowBandingDefinition)
        {
            this.rowBandingDefinition = rowBandingDefinition;

            repaint();
        }


        protected void paintComponent(@NotNull Graphics g)
        {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            Stroke origStroke = g2d.getStroke();
            Color origColor = g2d.getColor();
            Shape origClip = g2d.getClip();

            g2d.clip(new Rectangle(getInsets().left, getInsets().top, getWidth() - (getInsets().left + getInsets().right), getHeight() - (getInsets().top + getInsets().bottom)));
            g2d.setColor(rowBandingDefinition.getColor());
            int stripeHeight = 10;
            int stripes = (getHeight() - (getInsets().top + getInsets().bottom)) / stripeHeight - 1;

            for (int i = 0; i < stripes; i++)
            {
                if ((i / rowBandingDefinition.getSwitchItemCount()) % 2 == (rowBandingDefinition.isStartState() ? 0 : 1) && rowBandingDefinition.isEnabled())
                {
                    g2d.setColor(rowBandingDefinition.getColor());
                    g2d.fillRect(getInsets().left, i * stripeHeight + getInsets().top, getWidth() - (getInsets().left + getInsets().right), stripeHeight);
                }
                else
                {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(getInsets().left, i * stripeHeight + getInsets().top, getWidth() - (getInsets().left + getInsets().right), stripeHeight);
                }
                g2d.setColor(Color.GRAY);
                g2d.drawRect(getInsets().left, i * stripeHeight + getInsets().top, getWidth() - (getInsets().left + getInsets().right) - 1, stripeHeight);
            }

            g2d.setClip(origClip);
            g2d.setStroke(origStroke);
            g2d.setColor(origColor);
        }
    }

    private static class RowBandingDefinitionPanel extends JPanel
    {
        @NotNull
        private RowBandingDefinition rowBandingDefinition;

        @NotNull
        private JLabel enabledLabel;
        @NotNull
        private JCheckBox enabledCheckBox;
        @NotNull
        private JLabel colorLabel;
        @NotNull
        private JLabel colorIconLabel;
        @NotNull
        private Color color;
        @NotNull
        private JButton colorButton;
        @NotNull
        private JLabel initialStateLabel;
        @NotNull
        private JRadioButton initialStateColoredRadioButton;
        @NotNull
        private JRadioButton initialStateUncoloredRadioButton;
        @NotNull
        private JLabel switchItemCountLabel;
        @NotNull
        private JSpinner switchItemCountSpinner;


        private RowBandingDefinitionPanel(@NotNull RowBandingDefinition origRowBandingDefinition)
        {
            this.rowBandingDefinition = origRowBandingDefinition;

            @NonNls
            FormLayout formLayout = new FormLayout("0dlu, default:grow, 4dlu, fill:default, 4dlu, default, 0dlu",
                                                   "0dlu, " +
                                                   "pref, " +//enabled
                                                   "10dlu, " +
                                                   "pref, " +//color
                                                   "4dlu, " +
                                                   "pref, " +//initialStateradioButton
                                                   "4dlu, " +
                                                   "pref, " +//numberOfElementsSpinner
                                                   "10dlu, " +
                                                   "fill:20dlu:grow, " +//sample
                                                   "0dlu");

            setLayout(formLayout);
            @NonNls
            CellConstraints cc = new CellConstraints();

            enabledLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "RowBandingDefinitionChooser.EnabledLabel"));
            enabledCheckBox = new JCheckBox(TranslationManager.getInstance().getTranslation("R", "RowBandingDefinitionChooser.EnabledCheckBox"));
            enabledCheckBox.setSelected(rowBandingDefinition.isEnabled());

            colorLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "RowBandingDefinitionChooser.ColorLabel"));
            color = rowBandingDefinition.getColor();
            colorIconLabel = new JLabel(GraphicUtils.createColorImageIcon(Color.BLACK, color, 130, 20, true));
            colorButton = new JButton("...");
            colorButton.setMargin(new Insets(1, 1, 1, 1));

            initialStateLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "RowBandingDefinitionChooser.InitialStateLabel"));
            initialStateColoredRadioButton = new JRadioButton(TranslationManager.getInstance().getTranslation("R", "RowBandingDefinitionChooser.InitialStateColoredRadioButton"));
            initialStateUncoloredRadioButton = new JRadioButton(TranslationManager.getInstance().getTranslation("R", "RowBandingDefinitionChooser.InitialStateUncoloredRadioButton"));
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(initialStateColoredRadioButton);
            buttonGroup.add(initialStateUncoloredRadioButton);
            initialStateColoredRadioButton.setSelected(rowBandingDefinition.isStartState());
            initialStateUncoloredRadioButton.setSelected(!rowBandingDefinition.isStartState());

            switchItemCountLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "RowBandingDefinitionChooser.SwitchItemCountLabel"));
            switchItemCountSpinner = new JSpinner(new SpinnerNumberModel(rowBandingDefinition.getSwitchItemCount(), 1, Integer.MAX_VALUE, 1));

            final SamplePanel samplePanel = new SamplePanel(rowBandingDefinition);
            samplePanel.setBorder(new TitledBorder(TranslationManager.getInstance().getTranslation("R", "RowBandingDefinitionPanel.Sample.Title")));

            add(enabledLabel, cc.xy(2, 2));
            add(enabledCheckBox, cc.xy(4, 2));

            add(colorLabel, cc.xy(2, 4));
            add(colorIconLabel, cc.xy(4, 4));
            add(colorButton, cc.xy(6, 4));

            add(initialStateLabel, cc.xy(2, 6));
            JPanel helperPanel = new JPanel(new GridLayout(1, 0));
            helperPanel.add(initialStateColoredRadioButton);
            helperPanel.add(initialStateUncoloredRadioButton);
            add(helperPanel, cc.xy(4, 6));

            add(switchItemCountLabel, cc.xy(2, 8));
            add(switchItemCountSpinner, cc.xy(4, 8));

            add(samplePanel, cc.xyw(2, 10, 5));

            updateState();
            enabledCheckBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    updateState();
                    rowBandingDefinition = getCurrentRowBandingDefinition();
                    samplePanel.setRowBandingDefinition(rowBandingDefinition);
                }
            });


            colorButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    Color newColor = PaletteColorChooser.showDialog(RowBandingDefinitionChooser.RowBandingDefinitionPanel.this, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Color.Title"), color);
                    if (newColor != null)
                    {
                        color = newColor;
                        colorIconLabel.setIcon(GraphicUtils.createColorImageIcon(Color.BLACK, color, 130, 20, true));
                    }

                    rowBandingDefinition = getCurrentRowBandingDefinition();
                    samplePanel.setRowBandingDefinition(rowBandingDefinition);
                }
            });

            colorIconLabel.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(@NotNull MouseEvent e)
                {
                    Color newColor = PaletteColorChooser.showDialog(RowBandingDefinitionChooser.RowBandingDefinitionPanel.this, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Color.Title"), color);
                    if (newColor != null)
                    {
                        color = newColor;
                        colorIconLabel.setIcon(GraphicUtils.createColorImageIcon(Color.BLACK, color, 130, 20, true));
                    }

                    rowBandingDefinition = getCurrentRowBandingDefinition();
                    samplePanel.setRowBandingDefinition(rowBandingDefinition);
                }
            });

            initialStateColoredRadioButton.addChangeListener(new ChangeListener()
            {
                public void stateChanged(@NotNull ChangeEvent e)
                {
                    rowBandingDefinition = getCurrentRowBandingDefinition();
                    samplePanel.setRowBandingDefinition(rowBandingDefinition);
                }
            });

            initialStateUncoloredRadioButton.addChangeListener(new ChangeListener()
            {
                public void stateChanged(@NotNull ChangeEvent e)
                {
                    rowBandingDefinition = getCurrentRowBandingDefinition();
                    samplePanel.setRowBandingDefinition(rowBandingDefinition);
                }
            });

            switchItemCountSpinner.addChangeListener(new ChangeListener()
            {
                public void stateChanged(@NotNull ChangeEvent e)
                {
                    rowBandingDefinition = getCurrentRowBandingDefinition();
                    samplePanel.setRowBandingDefinition(rowBandingDefinition);
                }
            });

        }


        private void updateState()
        {
            colorLabel.setEnabled(enabledCheckBox.isSelected());
            colorIconLabel.setEnabled(enabledCheckBox.isSelected());
            colorButton.setEnabled(enabledCheckBox.isSelected());

            initialStateLabel.setEnabled(enabledCheckBox.isSelected());
            initialStateColoredRadioButton.setEnabled(enabledCheckBox.isSelected());
            initialStateUncoloredRadioButton.setEnabled(enabledCheckBox.isSelected());

            switchItemCountLabel.setEnabled(enabledCheckBox.isSelected());
            switchItemCountSpinner.setEnabled(enabledCheckBox.isSelected());
        }


        @NotNull
        private RowBandingDefinition getCurrentRowBandingDefinition()
        {
            int switchItemCount = ((Number) switchItemCountSpinner.getValue()).intValue();


            return new RowBandingDefinition(enabledCheckBox.isSelected(),
                                            color,
                                            initialStateColoredRadioButton.isSelected(),
                                            switchItemCount);
        }
    }
}
