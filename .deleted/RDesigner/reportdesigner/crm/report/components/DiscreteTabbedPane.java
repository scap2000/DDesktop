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
package org.pentaho.reportdesigner.crm.report.components;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 04.02.2006
 * Time: 17:15:39
 */
public class DiscreteTabbedPane extends JComponent
{
    @NotNull
    private ArrayList<TabInfo> tabInfos;

    @NotNull
    private CardLayout cardLayout;
    @NotNull
    private JPanel cardPanel;
    @NotNull
    private JPanel buttonPanel;

    @NotNull
    private ButtonGroup buttonGroup;


    public DiscreteTabbedPane()
    {
        tabInfos = new ArrayList<TabInfo>();

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));

        buttonGroup = new ButtonGroup();

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }


    @NotNull
    public JToggleButton addTab(@NotNull final String name, @NotNull JComponent component)
    {
        final JToggleButton button = new JToggleButton(name);
        button.setFocusable(false);
        button.setMargin(new Insets(1, 1, 1, 1));
        TabInfo tabInfo = new TabInfo(name, button, component);
        tabInfos.add(tabInfo);

        buttonPanel.add(button);

        cardPanel.add(component, name);

        buttonGroup.add(button);

        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (button.isSelected())
                {
                    cardLayout.show(cardPanel, name);
                }
            }
        });

        return button;
    }


    public void addStrut(int width)
    {
        buttonPanel.add(Box.createHorizontalStrut(width));
    }


    @NotNull
    public JButton addButton(@NotNull String name)
    {
        JButton button = new JButton(name);
        button.setFocusable(false);
        button.setMargin(new Insets(1, 1, 1, 1));
        buttonPanel.add(button);

        return button;
    }


    public void showTab(@NotNull String key)
    {
        for (TabInfo tabInfo : tabInfos)
        {
            if (tabInfo.getName().equals(key))
            {
                tabInfo.getButton().doClick();
            }
        }
    }


    private static class TabInfo
    {
        @NotNull
        private String name;
        @NotNull
        private JToggleButton button;
        @NotNull
        private JComponent component;


        private TabInfo(@NotNull String name, @NotNull JToggleButton button, @NotNull JComponent component)
        {
            this.name = name;
            this.button = button;
            this.component = component;
        }


        @NotNull
        public String getName()
        {
            return name;
        }


        @NotNull
        public JToggleButton getButton()
        {
            return button;
        }


        @NotNull
        public JComponent getComponent()
        {
            return component;
        }
    }
}
