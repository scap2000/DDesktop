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
package org.pentaho.reportdesigner.lib.client.components.tabbedpane;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.components.Category;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 12.03.2005
 * Time: 13:57:59
 */
public class ListTabbedPane extends JPanel
{
    @NotNull
    private static final String EMPTY_PANEL = "EMPTY_PANEL";


    public static void main(@NotNull String[] args)
    {
        UIManager.put("swing.boldMetal", Boolean.FALSE);//NON-NLS

        try
        {
            UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[0].getClassName());
        }
        catch (Throwable e)
        {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }


        JFrame frame = new JFrame("ButtonTabbedPane");//NON-NLS

        ListTabbedPane listTabbedPane = new ListTabbedPane(true);
        final Category category1 = new Category<JComponent>("books", new ImageIcon(ListTabbedPane.class.getResource("/res/icons/book-icon.gif")), new ImageIcon(ListTabbedPane.class.getResource("/res/icons/book-icon.gif")), "1. Bücher", new JScrollPane(new JTree()));//NON-NLS
        final Category category2 = new Category<JComponent>("books1", new ImageIcon(ListTabbedPane.class.getResource("/res/icons/book-icon.gif")), new ImageIcon(ListTabbedPane.class.getResource("/res/icons/book-icon.gif")), "2. Bücher1", new JScrollPane(new JTree()));//NON-NLS
        Category category3 = new Category<JComponent>("books2", new ImageIcon(ListTabbedPane.class.getResource("/res/icons/book-icon.gif")), new ImageIcon(ListTabbedPane.class.getResource("/res/icons/book-icon.gif")), "3. Bücher2", new JScrollPane(new JTree()));//NON-NLS
        Category category4 = new Category<JComponent>("books3", new ImageIcon(ListTabbedPane.class.getResource("/res/icons/book-icon.gif")), new ImageIcon(ListTabbedPane.class.getResource("/res/icons/book-icon.gif")), "4. Bücher3", new JScrollPane(new JTree()));//NON-NLS
        Category category5 = new Category<JComponent>("dvd", new ImageIcon(ListTabbedPane.class.getResource("/res/icons/dvd-icon.gif")), new ImageIcon(ListTabbedPane.class.getResource("/res/icons/dvd-icon.gif")), "5. DVD", new JScrollPane(new JLabel("blub")));//NON-NLS

        JButton testButton = new JButton("Test");//NON-NLS
        testButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                category1.setTitle("Hugo");//NON-NLS
                category2.setIconBig(new ImageIcon(ListTabbedPane.class.getResource("/res/icons/dvd-icon.gif")));//NON-NLS
            }
        });
        Category category6 = new Category<JComponent>("music", new ImageIcon(ListTabbedPane.class.getResource("/res/icons/music-icon.gif")), new ImageIcon(ListTabbedPane.class.getResource("/res/icons/music-icon.gif")), "6. Musik", new JScrollPane(testButton));//NON-NLS

        listTabbedPane.addCard(category1);
        listTabbedPane.addCard(category2);
        listTabbedPane.addCard(category3);
        listTabbedPane.addCard(category4);
        listTabbedPane.addCard(category5);
        listTabbedPane.addCard(category6);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(listTabbedPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    @NotNull
    private HashMap<Object, Category> hashMap;
    @NotNull
    private JList list;
    @NotNull
    private ButtonTabbedPaneListModel buttonTabbedPaneListModel;
    @NotNull
    private JPanel buttonPanel;
    @NotNull
    private JPanel cardPanel;

    @NotNull
    private CardLayout cardLayout;


    public ListTabbedPane(boolean border)
    {
        setLayout(new BorderLayout(5, 0));
        setOpaque(false);

        buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        hashMap = new HashMap<Object, Category>();

        buttonTabbedPaneListModel = new ButtonTabbedPaneListModel();
        list = new JList(buttonTabbedPaneListModel);
        list.setCellRenderer(new ListTabbedPaneListCellRenderer());
        list.setVisibleRowCount(4);

        list.setBackground(buttonPanel.getBackground());
        if (!border)
        {
            list.setBorder(BorderFactory.createEmptyBorder());
        }
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(@NotNull ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    showSelectedCard();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(list);
        if (!border)
        {
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
        }
        buttonPanel.add(scrollPane, BorderLayout.CENTER);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        add(buttonPanel, BorderLayout.WEST);

        add(cardPanel, BorderLayout.CENTER);

        cardPanel.add(new JPanel(), EMPTY_PANEL);
    }


    public void setSelectionTabWidth(int width)
    {
        list.setFixedCellWidth(width);
    }


    @NotNull
    public JPanel getButtonPanel()
    {
        return buttonPanel;
    }


    private void showSelectedCard()
    {
        Category category = (Category) list.getSelectedValue();
        if (category != null)
        {
            cardLayout.show(cardPanel, category.getKey().toString());
            cardPanel.revalidate();
            cardPanel.repaint();
        }
        else
        {
            cardLayout.show(cardPanel, EMPTY_PANEL);
            cardPanel.revalidate();
            cardPanel.repaint();
        }
    }


    public int getSelectedIndex()
    {
        return list.getSelectedIndex();
    }


    public void setSelectedIndex(int index)
    {
        list.setSelectedIndex(index);
        showSelectedCard();
    }


    public void addCard(@NotNull final Category category)
    {
        if (hashMap.containsKey(category.getKey()))
        {
            throw new IllegalArgumentException("a category with key='" + category.getKey() + "' is already registered");
        }

        buttonTabbedPaneListModel.addElement(category);

        cardPanel.add(category.getMainComponent(), category.getKey().toString());

        category.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                if ("title".equals(evt.getPropertyName()))//NON-NLS
                {
                    buttonTabbedPaneListModel.fireContentsChanged();
                }
                else if ("iconBig".equals(evt.getPropertyName()))//NON-NLS
                {
                    buttonTabbedPaneListModel.fireContentsChanged();
                }
                else if ("mainComponent".equals(evt.getPropertyName()))//NON-NLS
                {
                    cardPanel.remove((Component) evt.getOldValue());
                    cardPanel.add(category.getMainComponent(), category.getKey().toString());

                    cardPanel.revalidate();
                    cardPanel.repaint();
                }
            }
        });

        hashMap.put(category.getKey(), category);
    }


    @Nullable
    public Category getSelectedCategory()
    {
        if (getSelectedIndex() != -1)
        {
            return (Category) list.getSelectedValue();
        }
        return null;
    }


    @Nullable
    public JComponent getSelectedComponent()
    {
        if (getSelectedIndex() != -1)
        {
            return ((Category) list.getSelectedValue()).getMainComponent();
        }
        return null;
    }
}
