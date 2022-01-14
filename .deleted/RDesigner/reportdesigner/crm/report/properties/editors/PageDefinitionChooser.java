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
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.crm.report.model.PageDefinition;
import org.pentaho.reportdesigner.crm.report.model.PageFormatPreset;
import org.pentaho.reportdesigner.crm.report.model.PageOrientation;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.FontUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * User: Martin
 * Date: 08.02.2006
 * Time: 13:48:23
 */
public class PageDefinitionChooser
{
    @NotNull
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.0###");


    private PageDefinitionChooser()
    {
    }


    @NotNull
    public static PageDefinition showPageDefinitionChooser(@NotNull final ReportDialog reportDialog, @NotNull JComponent parent, @NotNull String title, @NotNull PageDefinition pageDefinition)
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
        FormLayout formLayout = new FormLayout("0dlu:grow, default, 10dlu, default, 4dlu, max(40dlu;default), 4dlu, default, 4dlu, max(40dlu;default), 0dlu:grow",
                                               "0dlu, " +
                                               "fill:10dlu:grow, " +
                                               "4dlu, " +
                                               "default, " +
                                               "4dlu, " +
                                               "default, " +
                                               "10dlu, " +
                                               "default, " +
                                               "4dlu, " +
                                               "default, " +
                                               "0dlu");

        JPanel centerPanel = new JPanel(formLayout);
        @NonNls
        CellConstraints cc = new CellConstraints();

        final PagePanel pagePanel = new PagePanel(pageDefinition);
        centerPanel.add(pagePanel, cc.xyw(1, 2, 11));

        final JComboBox paperComboBox = new JComboBox(PageFormatPreset.values());
        centerPanel.add(paperComboBox, cc.xyw(2, 4, 9));


        centerPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "PageDefinitionChooser.Width", Integer.valueOf(reportDialog.getApplicationSettings().getUnit().ordinal()))), cc.xy(4, 6));
        final JTextField widthTextField = ComponentFactory.createTextField(true, true);
        centerPanel.add(widthTextField, cc.xy(6, 6));

        centerPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "PageDefinitionChooser.Height", Integer.valueOf(reportDialog.getApplicationSettings().getUnit().ordinal()))), cc.xy(8, 6));
        final JTextField heightTextField = ComponentFactory.createTextField(true, true);
        centerPanel.add(heightTextField, cc.xy(10, 6));


        final JRadioButton portraitRadioButton = new JRadioButton(TranslationManager.getInstance().getTranslation("R", "PageDefinitionChooser.Portrait"));
        centerPanel.add(portraitRadioButton, cc.xy(2, 8));

        centerPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "PageDefinitionChooser.Top", Integer.valueOf(reportDialog.getApplicationSettings().getUnit().ordinal()))), cc.xy(4, 8));
        final JTextField topTextField = ComponentFactory.createTextField(true, true);
        centerPanel.add(topTextField, cc.xy(6, 8));

        centerPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "PageDefinitionChooser.Bottom", Integer.valueOf(reportDialog.getApplicationSettings().getUnit().ordinal()))), cc.xy(8, 8));
        final JTextField bottomTextField = ComponentFactory.createTextField(true, true);
        centerPanel.add(bottomTextField, cc.xy(10, 8));

        final JRadioButton landscapeRadioButton = new JRadioButton(TranslationManager.getInstance().getTranslation("R", "PageDefinitionChooser.Landscape"));
        centerPanel.add(landscapeRadioButton, cc.xy(2, 10));

        centerPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "PageDefinitionChooser.Left", Integer.valueOf(reportDialog.getApplicationSettings().getUnit().ordinal()))), cc.xy(4, 10));
        final JTextField leftTextField = ComponentFactory.createTextField(true, true);
        centerPanel.add(leftTextField, cc.xy(6, 10));

        centerPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "PageDefinitionChooser.Right", Integer.valueOf(reportDialog.getApplicationSettings().getUnit().ordinal()))), cc.xy(8, 10));
        final JTextField rigthTextField = ComponentFactory.createTextField(true, true);
        centerPanel.add(rigthTextField, cc.xy(10, 10));

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(portraitRadioButton);
        buttonGroup.add(landscapeRadioButton);


        PageFormatPreset preset = PageFormatPreset.getPreset(pageDefinition);

        paperComboBox.setSelectedItem(preset);


        if (pageDefinition.getPageOrientation() == PageOrientation.LANDSCAPE)
        {
            landscapeRadioButton.setSelected(true);
        }
        else
        {
            portraitRadioButton.setSelected(true);
        }


        widthTextField.setText(decimalFormat.format(reportDialog.getApplicationSettings().getUnit().convertFromPoints(pageDefinition.getOuterPageSize().getWidth())));
        heightTextField.setText(decimalFormat.format(reportDialog.getApplicationSettings().getUnit().convertFromPoints(pageDefinition.getOuterPageSize().getHeight())));

        widthTextField.setEditable(preset == PageFormatPreset.CUSTOM);
        heightTextField.setEditable(preset == PageFormatPreset.CUSTOM);

        topTextField.setText(decimalFormat.format(reportDialog.getApplicationSettings().getUnit().convertFromPoints(pageDefinition.getTopBorder())));
        bottomTextField.setText(decimalFormat.format(reportDialog.getApplicationSettings().getUnit().convertFromPoints(pageDefinition.getBottomBorder())));
        leftTextField.setText(decimalFormat.format(reportDialog.getApplicationSettings().getUnit().convertFromPoints(pageDefinition.getLeftBorder())));
        rigthTextField.setText(decimalFormat.format(reportDialog.getApplicationSettings().getUnit().convertFromPoints(pageDefinition.getRightBorder())));

        ActionListener al = new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                PageFormatPreset preset = (PageFormatPreset) paperComboBox.getSelectedItem();
                widthTextField.setEditable(preset == PageFormatPreset.CUSTOM);
                heightTextField.setEditable(preset == PageFormatPreset.CUSTOM);

                PageDefinition pageDefinition = buildPageDefinition(reportDialog, paperComboBox, portraitRadioButton, widthTextField, heightTextField, topTextField, bottomTextField, leftTextField, rigthTextField);
                if (pageDefinition != null)
                {
                    pagePanel.setPageDefinition(pageDefinition);

                    widthTextField.setText(decimalFormat.format(pageDefinition.getOuterPageSize().getWidth()));
                    heightTextField.setText(decimalFormat.format(pageDefinition.getOuterPageSize().getHeight()));
                }
            }
        };
        portraitRadioButton.addActionListener(al);
        landscapeRadioButton.addActionListener(al);
        paperComboBox.addActionListener(al);

        DocumentListener dl = new DocumentListener()
        {
            public void insertUpdate(@NotNull DocumentEvent e)
            {
                PageDefinition pageDefinition = buildPageDefinition(reportDialog, paperComboBox, portraitRadioButton, widthTextField, heightTextField, topTextField, bottomTextField, leftTextField, rigthTextField);
                pagePanel.setPageDefinition(pageDefinition);
            }


            public void removeUpdate(@NotNull DocumentEvent e)
            {
                PageDefinition pageDefinition = buildPageDefinition(reportDialog, paperComboBox, portraitRadioButton, widthTextField, heightTextField, topTextField, bottomTextField, leftTextField, rigthTextField);
                pagePanel.setPageDefinition(pageDefinition);
            }


            public void changedUpdate(@NotNull DocumentEvent e)
            {
                PageDefinition pageDefinition = buildPageDefinition(reportDialog, paperComboBox, portraitRadioButton, widthTextField, heightTextField, topTextField, bottomTextField, leftTextField, rigthTextField);
                pagePanel.setPageDefinition(pageDefinition);
            }
        };
        widthTextField.getDocument().addDocumentListener(dl);
        heightTextField.getDocument().addDocumentListener(dl);
        topTextField.getDocument().addDocumentListener(dl);
        bottomTextField.getDocument().addDocumentListener(dl);
        leftTextField.getDocument().addDocumentListener(dl);
        rigthTextField.getDocument().addDocumentListener(dl);

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
        centerPanelDialog.setSize(500, 500);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            return pagePanel.getPageDefinition();
        }
        return pageDefinition;
    }


    @Nullable
    private static PageDefinition buildPageDefinition(@NotNull ReportDialog reportDialog,
                                                      @NotNull JComboBox paperComboBox,
                                                      @NotNull JRadioButton portraitRadioButton,
                                                      @NotNull JTextField widthTextField, @NotNull JTextField heightTextField,
                                                      @NotNull JTextField topTextField, @NotNull JTextField bottomTextField,
                                                      @NotNull JTextField leftTextField, @NotNull JTextField rightTextField)
    {
        PageFormatPreset pageFormatPreset;
        double width;
        double height;
        double topBorder;
        double bottomBorder;
        double leftBorder;
        double rightBorder;
        PageOrientation pageOrientation;
        try
        {
            pageFormatPreset = (PageFormatPreset) paperComboBox.getSelectedItem();
            width = reportDialog.getApplicationSettings().getUnit().convertToPoints(decimalFormat.parse(widthTextField.getText()).doubleValue());
            height = reportDialog.getApplicationSettings().getUnit().convertToPoints(decimalFormat.parse(heightTextField.getText()).doubleValue());
            topBorder = reportDialog.getApplicationSettings().getUnit().convertToPoints(decimalFormat.parse(topTextField.getText()).doubleValue());
            bottomBorder = reportDialog.getApplicationSettings().getUnit().convertToPoints(decimalFormat.parse(bottomTextField.getText()).doubleValue());
            leftBorder = reportDialog.getApplicationSettings().getUnit().convertToPoints(decimalFormat.parse(leftTextField.getText()).doubleValue());
            rightBorder = reportDialog.getApplicationSettings().getUnit().convertToPoints(decimalFormat.parse(rightTextField.getText()).doubleValue());
            pageOrientation = portraitRadioButton.isSelected() ? PageOrientation.PORTRAIT : PageOrientation.LANDSCAPE;
        }
        catch (NumberFormatException e)
        {
            //ok
            return null;
        }
        catch (ParseException e)
        {
            //ok
            return null;
        }

        if (topBorder < 0 || bottomBorder < 0 || leftBorder < 0 || rightBorder < 0)
        {
            return null;
        }

        PageDefinition pageDefinition;
        if (pageFormatPreset == PageFormatPreset.CUSTOM)
        {
            pageDefinition = new PageDefinition(pageFormatPreset, pageOrientation, topBorder, bottomBorder, leftBorder, rightBorder, width, height);
        }
        else
        {
            pageDefinition = new PageDefinition(pageFormatPreset, pageOrientation, topBorder, bottomBorder, leftBorder, rightBorder);
        }

        if (pageDefinition.getInnerPageSize().getWidth() <= 0)
        {
            return null;
        }
        else if (pageDefinition.getInnerPageSize().getHeight() <= 0)
        {
            return null;
        }
        return pageDefinition;
    }


    private static class PagePanel extends JPanel
    {

        @NotNull
        private PageDefinition pageDefinition;
        private boolean error;

        @NotNull
        private Font bigFont;


        private PagePanel(@NotNull PageDefinition pageDefinition)
        {
            this.pageDefinition = pageDefinition;

            bigFont = FontUtils.getDerivedFont(getFont(), Font.BOLD, 24);
        }


        @NotNull
        public PageDefinition getPageDefinition()
        {
            return pageDefinition;
        }


        public boolean isError()
        {
            return error;
        }


        public void setPageDefinition(@Nullable PageDefinition pageDefinition)
        {
            if (pageDefinition == null)
            {
                error = true;
            }
            else
            {
                this.pageDefinition = pageDefinition;
                error = false;
            }
            repaint();
        }


        protected void paintComponent(@NotNull Graphics g)
        {
            super.paintComponent(g);

            double wf = (getWidth() - 10) / (pageDefinition.getInnerPageSize().getWidth() + pageDefinition.getLeftBorder() + pageDefinition.getRightBorder());
            double hf = (getHeight() - 10) / (pageDefinition.getInnerPageSize().getHeight() + pageDefinition.getTopBorder() + pageDefinition.getBottomBorder());

            double scaleFactor;
            if (wf > hf)
            {
                scaleFactor = hf;
            }
            else
            {
                scaleFactor = wf;
            }

            double w = getWidth();
            double h = getHeight();

            g.setColor(new Color(240, 240, 240));
            double ow = (pageDefinition.getInnerPageSize().getWidth() + pageDefinition.getLeftBorder() + pageDefinition.getRightBorder()) * scaleFactor;
            double oh = (pageDefinition.getInnerPageSize().getHeight() + pageDefinition.getTopBorder() + pageDefinition.getBottomBorder()) * scaleFactor;

            int xStart = (int) (w - ow) / 2;
            int yStart = (int) (h - oh) / 2;

            g.fillRect(xStart, yStart, (int) ow, (int) oh);
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(xStart, yStart, (int) ow, (int) oh);


            double iw = (pageDefinition.getInnerPageSize().getWidth()) * scaleFactor;
            double ih = (pageDefinition.getInnerPageSize().getHeight()) * scaleFactor;
            g.setColor(Color.WHITE);
            g.fillRect(xStart + (int) (pageDefinition.getLeftBorder() * scaleFactor), yStart + (int) (pageDefinition.getTopBorder() * scaleFactor), (int) iw, (int) ih);
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(xStart + (int) (pageDefinition.getLeftBorder() * scaleFactor), yStart + (int) (pageDefinition.getTopBorder() * scaleFactor), (int) iw, (int) ih);

            PageFormatPreset preset = PageFormatPreset.getPreset(pageDefinition);
            Font origFont = g.getFont();
            g.setFont(bigFont);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(preset.toString(), g);
            g.drawString(preset.toString(), (int) ((getWidth() - stringBounds.getWidth()) / 2), (int) ((getHeight() - stringBounds.getHeight()) / 2 - stringBounds.getY()));
            g.setFont(origFont);

            if (error)
            {
                g.setColor(Color.RED);
                g.drawString(TranslationManager.getInstance().getTranslation("R", "PagePanel.InvalidPageDefinition"), xStart + 20, yStart + 20);
            }
        }
    }
}
