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
import org.pentaho.reportdesigner.crm.report.model.ElementBorderDefinition;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;

/**
 * User: Martin
 * Date: 01.11.2005
 * Time: 10:23:10
 */
public class ElementBorderDefinitionChooser
{

    private ElementBorderDefinitionChooser()
    {
    }


    @NotNull
    public static ElementBorderDefinition showElementBorderDefinitionChooser(@NotNull JComponent parent, @NotNull String title, @NotNull ElementBorderDefinition origBorderDefinition)
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

        ElementBorderDefinitionPanel centerPanel = new ElementBorderDefinitionPanel(origBorderDefinition);

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.pack();
        GUIUtils.ensureMinimumDialogSize(centerPanelDialog, 250, 250);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);

        if (action[0])
        {
            return centerPanel.getCurrentBorderDefinition();
        }
        return origBorderDefinition;
    }


    private static class ElementBorderDefinitionPanel extends JPanel
    {
        @NotNull
        private ElementBorderDefinition currentBorderDefinition;

        @NotNull
        private BorderSideIcon top;
        @NotNull
        private BorderSideIcon bottom;
        @NotNull
        private BorderSideIcon left;
        @NotNull
        private BorderSideIcon right;

        @NotNull
        private BorderSideIcon breakSide;

        @NotNull
        private JButton topButton;
        @NotNull
        private JButton leftButton;
        @NotNull
        private JButton rightButton;
        @NotNull
        private JButton bottomButton;
        @NotNull
        private JButton breakButton;
        @NotNull
        private JButton topLeftButton;
        @NotNull
        private JButton topRightButton;
        @NotNull
        private JButton bottomLeftButton;
        @NotNull
        private JButton bottomRightButton;


        private ElementBorderDefinitionPanel(@NotNull ElementBorderDefinition origBorderDefinition)
        {
            currentBorderDefinition = origBorderDefinition;

            final JCheckBox sameBorderCheckBox = new JCheckBox(TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.SameBorderForAllSides"));

            top = new BorderSideIcon(currentBorderDefinition.getTopSide(), true);
            left = new BorderSideIcon(currentBorderDefinition.getLeftSide(), false);
            right = new BorderSideIcon(currentBorderDefinition.getRightSide(), false);
            bottom = new BorderSideIcon(currentBorderDefinition.getBottomSide(), true);

            breakSide = new BorderSideIcon(currentBorderDefinition.getBreakSide(), true);

            @NonNls
            FormLayout formLayout = new FormLayout("default, 4dlu, default, 4dlu, default",
                                                   "default, 4dlu, " +
                                                   "default, 4dlu, " +
                                                   "default, 4dlu, " +
                                                   "default, 20dlu," +
                                                   "default, 50dlu");

            setLayout(formLayout);
            @NonNls
            CellConstraints cc = new CellConstraints();

            add(sameBorderCheckBox, cc.xyw(1, 1, 5));

            topButton = new JButton(top);
            leftButton = new JButton(left);
            rightButton = new JButton(right);
            bottomButton = new JButton(bottom);
            breakButton = new JButton(breakSide);

            add(topButton, cc.xy(3, 3));
            add(leftButton, cc.xy(1, 5, "center, center"));
            add(rightButton, cc.xy(5, 5, "center, center"));
            add(bottomButton, cc.xy(3, 7));

            add(new JLabel(TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Break")), cc.xy(1, 9));
            add(breakButton, cc.xy(3, 9));

            topLeftButton = new JButton(new BorderEdgeIcon(currentBorderDefinition.getTopLeftEdge(), 1));
            topRightButton = new JButton(new BorderEdgeIcon(currentBorderDefinition.getTopRightEdge(), 2));
            bottomLeftButton = new JButton(new BorderEdgeIcon(currentBorderDefinition.getBottomLeftEdge(), 3));
            bottomRightButton = new JButton(new BorderEdgeIcon(currentBorderDefinition.getBottomRightEdge(), 4));

            add(topLeftButton, cc.xy(1, 3));
            add(topRightButton, cc.xy(5, 3));
            add(bottomLeftButton, cc.xy(1, 7));
            add(bottomRightButton, cc.xy(5, 7));

            topLeftButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    DoubleValueChooser.Pair<Double> pair = DoubleValueChooser.showValueChooser(ElementBorderDefinitionPanel.this,
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Edge"),
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Width"),
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Height"),
                                                                                               new DoubleValueChooser.Pair<Double>(Double.valueOf(currentBorderDefinition.getTopLeftEdge().getRadii().getWidth()), Double.valueOf(currentBorderDefinition.getTopLeftEdge().getRadii().getHeight())));

                    if (pair != null)
                    {
                        currentBorderDefinition = new ElementBorderDefinition(sameBorderCheckBox.isSelected(),
                                                                              currentBorderDefinition.getTopSide(),
                                                                              currentBorderDefinition.getBottomSide(),
                                                                              currentBorderDefinition.getLeftSide(),
                                                                              currentBorderDefinition.getRightSide(),
                                                                              currentBorderDefinition.getBreakSide(),
                                                                              new ElementBorderDefinition.Edge(new DoubleDimension(pair.getValue1().doubleValue(), pair.getValue2().doubleValue())),
                                                                              currentBorderDefinition.getTopRightEdge(),
                                                                              currentBorderDefinition.getBottomLeftEdge(),
                                                                              currentBorderDefinition.getBottomRightEdge());
                        updateComponents();
                    }
                }
            });

            topRightButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    DoubleValueChooser.Pair<Double> pair = DoubleValueChooser.showValueChooser(ElementBorderDefinitionPanel.this,
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Edge"),
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Width"),
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Height"),
                                                                                               new DoubleValueChooser.Pair<Double>(Double.valueOf(currentBorderDefinition.getTopRightEdge().getRadii().getWidth()), Double.valueOf(currentBorderDefinition.getTopRightEdge().getRadii().getHeight())));
                    if (pair != null)
                    {
                        currentBorderDefinition = new ElementBorderDefinition(sameBorderCheckBox.isSelected(),
                                                                              currentBorderDefinition.getTopSide(),
                                                                              currentBorderDefinition.getBottomSide(),
                                                                              currentBorderDefinition.getLeftSide(),
                                                                              currentBorderDefinition.getRightSide(),
                                                                              currentBorderDefinition.getBreakSide(),
                                                                              currentBorderDefinition.getTopLeftEdge(),
                                                                              new ElementBorderDefinition.Edge(new DoubleDimension(pair.getValue1().doubleValue(), pair.getValue2().doubleValue())),
                                                                              currentBorderDefinition.getBottomLeftEdge(),
                                                                              currentBorderDefinition.getBottomRightEdge());
                        updateComponents();
                    }
                }
            });

            bottomLeftButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    DoubleValueChooser.Pair<Double> pair = DoubleValueChooser.showValueChooser(ElementBorderDefinitionPanel.this,
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Edge"),
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Width"),
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Height"),
                                                                                               new DoubleValueChooser.Pair<Double>(Double.valueOf(currentBorderDefinition.getBottomLeftEdge().getRadii().getWidth()), Double.valueOf(currentBorderDefinition.getBottomLeftEdge().getRadii().getHeight())));

                    if (pair != null)
                    {
                        currentBorderDefinition = new ElementBorderDefinition(sameBorderCheckBox.isSelected(),
                                                                              currentBorderDefinition.getTopSide(),
                                                                              currentBorderDefinition.getBottomSide(),
                                                                              currentBorderDefinition.getLeftSide(),
                                                                              currentBorderDefinition.getRightSide(),
                                                                              currentBorderDefinition.getBreakSide(),
                                                                              currentBorderDefinition.getTopLeftEdge(),
                                                                              currentBorderDefinition.getTopRightEdge(),
                                                                              new ElementBorderDefinition.Edge(new DoubleDimension(pair.getValue1().doubleValue(), pair.getValue2().doubleValue())),
                                                                              currentBorderDefinition.getBottomRightEdge());
                        updateComponents();
                    }
                }
            });

            bottomRightButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    DoubleValueChooser.Pair<Double> pair = DoubleValueChooser.showValueChooser(ElementBorderDefinitionPanel.this,
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Edge"),
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Width"),
                                                                                               TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Height"),
                                                                                               new DoubleValueChooser.Pair<Double>(Double.valueOf(currentBorderDefinition.getBottomRightEdge().getRadii().getWidth()), Double.valueOf(currentBorderDefinition.getBottomRightEdge().getRadii().getHeight())));
                    if (pair != null)
                    {
                        currentBorderDefinition = new ElementBorderDefinition(sameBorderCheckBox.isSelected(),
                                                                              currentBorderDefinition.getTopSide(),
                                                                              currentBorderDefinition.getBottomSide(),
                                                                              currentBorderDefinition.getLeftSide(),
                                                                              currentBorderDefinition.getRightSide(),
                                                                              currentBorderDefinition.getBreakSide(),
                                                                              currentBorderDefinition.getTopLeftEdge(),
                                                                              currentBorderDefinition.getTopRightEdge(),
                                                                              currentBorderDefinition.getBottomLeftEdge(),
                                                                              new ElementBorderDefinition.Edge(new DoubleDimension(pair.getValue1().doubleValue(), pair.getValue2().doubleValue())));
                        updateComponents();
                    }
                }
            });

            sameBorderCheckBox.setSelected(currentBorderDefinition.isSameBorderForAllSides());
            sameBorderCheckBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    currentBorderDefinition = new ElementBorderDefinition(sameBorderCheckBox.isSelected(),
                                                                          currentBorderDefinition.getTopSide(),
                                                                          currentBorderDefinition.getBottomSide(),
                                                                          currentBorderDefinition.getLeftSide(),
                                                                          currentBorderDefinition.getRightSide(),
                                                                          currentBorderDefinition.getBreakSide(),
                                                                          currentBorderDefinition.getTopLeftEdge(),
                                                                          currentBorderDefinition.getTopRightEdge(),
                                                                          currentBorderDefinition.getBottomLeftEdge(),
                                                                          currentBorderDefinition.getBottomRightEdge());
                    updateComponents();
                }
            });

            topButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    ElementBorderDefinition.Side side = ElementBorderDefinitionSideChooser.showBorderDefinitionChooser(ElementBorderDefinitionPanel.this, TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Side"), currentBorderDefinition.getTopSide());

                    currentBorderDefinition = new ElementBorderDefinition(sameBorderCheckBox.isSelected(),
                                                                          side,
                                                                          currentBorderDefinition.getBottomSide(),
                                                                          currentBorderDefinition.getLeftSide(),
                                                                          currentBorderDefinition.getRightSide(),
                                                                          currentBorderDefinition.getBreakSide(),
                                                                          currentBorderDefinition.getTopLeftEdge(),
                                                                          currentBorderDefinition.getTopRightEdge(),
                                                                          currentBorderDefinition.getBottomLeftEdge(),
                                                                          currentBorderDefinition.getBottomRightEdge());
                    updateComponents();
                }
            });

            bottomButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    ElementBorderDefinition.Side side = ElementBorderDefinitionSideChooser.showBorderDefinitionChooser(ElementBorderDefinitionPanel.this, TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Side"), currentBorderDefinition.getBottomSide());

                    currentBorderDefinition = new ElementBorderDefinition(sameBorderCheckBox.isSelected(),
                                                                          currentBorderDefinition.getTopSide(),
                                                                          side,
                                                                          currentBorderDefinition.getLeftSide(),
                                                                          currentBorderDefinition.getRightSide(),
                                                                          currentBorderDefinition.getBreakSide(),
                                                                          currentBorderDefinition.getTopLeftEdge(),
                                                                          currentBorderDefinition.getTopRightEdge(),
                                                                          currentBorderDefinition.getBottomLeftEdge(),
                                                                          currentBorderDefinition.getBottomRightEdge());
                    updateComponents();
                }
            });

            leftButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    ElementBorderDefinition.Side side = ElementBorderDefinitionSideChooser.showBorderDefinitionChooser(ElementBorderDefinitionPanel.this, TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Side"), currentBorderDefinition.getLeftSide());

                    currentBorderDefinition = new ElementBorderDefinition(sameBorderCheckBox.isSelected(),
                                                                          currentBorderDefinition.getTopSide(),
                                                                          currentBorderDefinition.getBottomSide(),
                                                                          side,
                                                                          currentBorderDefinition.getRightSide(),
                                                                          currentBorderDefinition.getBreakSide(),
                                                                          currentBorderDefinition.getTopLeftEdge(),
                                                                          currentBorderDefinition.getTopRightEdge(),
                                                                          currentBorderDefinition.getBottomLeftEdge(),
                                                                          currentBorderDefinition.getBottomRightEdge());
                    updateComponents();
                }
            });

            rightButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    ElementBorderDefinition.Side side = ElementBorderDefinitionSideChooser.showBorderDefinitionChooser(ElementBorderDefinitionPanel.this, TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Side"), currentBorderDefinition.getRightSide());

                    currentBorderDefinition = new ElementBorderDefinition(sameBorderCheckBox.isSelected(),
                                                                          currentBorderDefinition.getTopSide(),
                                                                          currentBorderDefinition.getBottomSide(),
                                                                          currentBorderDefinition.getLeftSide(),
                                                                          side,
                                                                          currentBorderDefinition.getBreakSide(),
                                                                          currentBorderDefinition.getTopLeftEdge(),
                                                                          currentBorderDefinition.getTopRightEdge(),
                                                                          currentBorderDefinition.getBottomLeftEdge(),
                                                                          currentBorderDefinition.getBottomRightEdge());
                    updateComponents();
                }
            });

            breakButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    ElementBorderDefinition.Side side = ElementBorderDefinitionSideChooser.showBorderDefinitionChooser(ElementBorderDefinitionPanel.this, TranslationManager.getInstance().getTranslation("R", "ElementBorderDefinitionChooser.Side"), currentBorderDefinition.getBreakSide());

                    currentBorderDefinition = new ElementBorderDefinition(sameBorderCheckBox.isSelected(),
                                                                          currentBorderDefinition.getTopSide(),
                                                                          currentBorderDefinition.getBottomSide(),
                                                                          currentBorderDefinition.getLeftSide(),
                                                                          currentBorderDefinition.getRightSide(),
                                                                          side,
                                                                          currentBorderDefinition.getTopLeftEdge(),
                                                                          currentBorderDefinition.getTopRightEdge(),
                                                                          currentBorderDefinition.getBottomLeftEdge(),
                                                                          currentBorderDefinition.getBottomRightEdge());
                    updateComponents();
                }
            });

            updateComponents();
        }


        private void updateComponents()
        {
            if (currentBorderDefinition.isSameBorderForAllSides())
            {
                topButton.setEnabled(true);
                leftButton.setEnabled(false);
                rightButton.setEnabled(false);
                bottomButton.setEnabled(false);
                breakButton.setEnabled(true);

                topLeftButton.setEnabled(true);
                topRightButton.setEnabled(false);
                bottomLeftButton.setEnabled(false);
                bottomRightButton.setEnabled(false);

                topButton.setIcon(new BorderSideIcon(currentBorderDefinition.getTopSide(), true));
                leftButton.setIcon(new BorderSideIcon(currentBorderDefinition.getTopSide(), false));
                rightButton.setIcon(new BorderSideIcon(currentBorderDefinition.getTopSide(), false));
                bottomButton.setIcon(new BorderSideIcon(currentBorderDefinition.getTopSide(), true));
                breakButton.setIcon(new BorderSideIcon(currentBorderDefinition.getBreakSide(), true));

                topLeftButton.setIcon(new BorderEdgeIcon(currentBorderDefinition.getTopLeftEdge(), 1));
                topRightButton.setIcon(new BorderEdgeIcon(currentBorderDefinition.getTopLeftEdge(), 2));
                bottomLeftButton.setIcon(new BorderEdgeIcon(currentBorderDefinition.getTopLeftEdge(), 3));
                bottomRightButton.setIcon(new BorderEdgeIcon(currentBorderDefinition.getTopLeftEdge(), 4));
            }
            else
            {
                topButton.setEnabled(true);
                leftButton.setEnabled(true);
                rightButton.setEnabled(true);
                bottomButton.setEnabled(true);
                breakButton.setEnabled(true);

                topLeftButton.setEnabled(true);
                topRightButton.setEnabled(true);
                bottomLeftButton.setEnabled(true);
                bottomRightButton.setEnabled(true);

                topButton.setIcon(new BorderSideIcon(currentBorderDefinition.getTopSide(), true));
                leftButton.setIcon(new BorderSideIcon(currentBorderDefinition.getLeftSide(), false));
                rightButton.setIcon(new BorderSideIcon(currentBorderDefinition.getRightSide(), false));
                bottomButton.setIcon(new BorderSideIcon(currentBorderDefinition.getBottomSide(), true));
                breakButton.setIcon(new BorderSideIcon(currentBorderDefinition.getBreakSide(), true));

                topLeftButton.setIcon(new BorderEdgeIcon(currentBorderDefinition.getTopLeftEdge(), 1));
                topRightButton.setIcon(new BorderEdgeIcon(currentBorderDefinition.getTopRightEdge(), 2));
                bottomLeftButton.setIcon(new BorderEdgeIcon(currentBorderDefinition.getBottomLeftEdge(), 3));
                bottomRightButton.setIcon(new BorderEdgeIcon(currentBorderDefinition.getBottomRightEdge(), 4));
            }
        }


        @NotNull
        public ElementBorderDefinition getCurrentBorderDefinition()
        {
            return currentBorderDefinition;
        }
    }

    private static class BorderEdgeIcon implements Icon
    {
        @NotNull
        private ElementBorderDefinition.Edge side;
        private int edge;


        private BorderEdgeIcon(@NotNull ElementBorderDefinition.Edge side, int edge)
        {
            this.side = side;
            this.edge = edge;
        }


        public void setEdge(@NotNull ElementBorderDefinition.Edge side)
        {
            this.side = side;
        }


        public void paintIcon(@NotNull Component c, @NotNull Graphics g, int x, int y)
        {
            Graphics2D g2d = (Graphics2D) g;
            Arc2D.Double a;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.BLACK);
            if (edge == 1)
            {
                a = new Arc2D.Double(x + 10, y + 10, 20, 20, 90, 90, Arc2D.OPEN);
            }
            else if (edge == 2)
            {
                a = new Arc2D.Double(x - 10, y + 10, 20, 20, 0, 90, Arc2D.OPEN);
            }
            else if (edge == 3)
            {
                a = new Arc2D.Double(x + 10, y - 10, 20, 20, 180, 90, Arc2D.OPEN);
            }
            else
            {
                a = new Arc2D.Double(x - 10, y - 10, 20, 20, -90, 90, Arc2D.OPEN);
            }
            g2d.draw(a);
        }


        public int getIconWidth()
        {
            return 20;
        }


        public int getIconHeight()
        {
            return 20;
        }
    }

    private static class BorderSideIcon implements Icon
    {
        @NotNull
        private ElementBorderDefinition.Side side;
        private boolean horizontal;


        private BorderSideIcon(@NotNull ElementBorderDefinition.Side side, boolean horizontal)
        {
            this.side = side;
            this.horizontal = horizontal;
        }


        public void setSide(@NotNull ElementBorderDefinition.Side side)
        {
            this.side = side;
        }


        public void paintIcon(@NotNull Component c, @NotNull Graphics g, int x, int y)
        {
            Graphics2D g2d = (Graphics2D) g;
            if (side.getType() != ElementBorderDefinition.BorderType.NONE)
            {
                g2d.setStroke(side.getStroke());
                g2d.setColor(side.getColor());
                if (horizontal)
                {
                    g2d.drawLine(x, 10 + y, 100 + x, 10 + y);
                }
                else
                {
                    g2d.drawLine(10 + x, y, 10 + x, 100 + y);
                }
            }
        }


        public int getIconWidth()
        {
            return horizontal ? 100 : 20;
        }


        public int getIconHeight()
        {
            return horizontal ? 20 : 100;
        }
    }
}