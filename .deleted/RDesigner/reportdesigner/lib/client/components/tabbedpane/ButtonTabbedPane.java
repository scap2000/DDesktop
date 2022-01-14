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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * User: Martin
 * Date: 12.03.2005
 * Time: 13:57:59
 */
public class ButtonTabbedPane extends JPanel
{

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

        ButtonTabbedPane buttonTabbedPane = new ButtonTabbedPane(true);
        final Category category1 = new Category<JComponent>("books", new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/book-icon.gif")), new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/book-icon.gif")), "Bücher", new JScrollPane(new JTree()));//NON-NLS
        final Category category2 = new Category<JComponent>("books1", new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/book-icon.gif")), new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/book-icon.gif")), "Bücher1", new JScrollPane(new JTree()));//NON-NLS
        Category category3 = new Category<JComponent>("books2", new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/book-icon.gif")), new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/book-icon.gif")), "Bücher2", new JScrollPane(new JTree()));//NON-NLS
        Category category4 = new Category<JComponent>("books3", new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/book-icon.gif")), new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/book-icon.gif")), "Bücher3", new JScrollPane(new JTree()));//NON-NLS
        Category category5 = new Category<JComponent>("dvd", new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/dvd-icon.gif")), new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/dvd-icon.gif")), "DVD", new JScrollPane(new JLabel("blub")));//NON-NLS

        JButton testButton = new JButton("Test");//NON-NLS
        testButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                category1.setTitle("Hugo");//NON-NLS
                category2.setIconBig(new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/dvd-icon.gif")));//NON-NLS
            }
        });
        Category category6 = new Category<JComponent>("music", new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/music-icon.gif")), new ImageIcon(ButtonTabbedPane.class.getResource("/res/icons/music-icon.gif")), "Musik", new JScrollPane(testButton));//NON-NLS

        buttonTabbedPane.addCard(category1);
        buttonTabbedPane.addCard(category2);
        buttonTabbedPane.addCard(category3);
        buttonTabbedPane.addCard(category4);
        buttonTabbedPane.addCard(category5);
        buttonTabbedPane.addCard(category6);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(buttonTabbedPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    @NotNull
    private ArrayList<Category> categories;
    @NotNull
    private HashMap<Object, JToggleButton> buttonMap;
    @NotNull
    private JPanel leftPanel;
    @NotNull
    private JScrollPane scrollPane;
    @NotNull
    private JPanel buttonPanel;
    @NotNull
    private JPanel buttonHelperPanel;
    @NotNull
    private JPanel cardPanel;

    @NotNull
    private CardLayout cardLayout;


    public ButtonTabbedPane(boolean border)
    {
        setLayout(new BorderLayout(0, 0));
        setOpaque(false);

        categories = new ArrayList<Category>();

        buttonMap = new HashMap<Object, JToggleButton>();
        buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.setOpaque(false);

        buttonHelperPanel = new ScrollablePanel(new BorderLayout());
        buttonHelperPanel.setOpaque(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        buttonHelperPanel.add(buttonPanel, BorderLayout.NORTH);

        JPanel helperPanel = new JPanel(new BorderLayout());
        scrollPane = new JScrollPane(buttonHelperPanel);
        if (!border)
        {
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
        }
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        helperPanel.add(leftPanel, BorderLayout.WEST);
        helperPanel.add(Box.createHorizontalStrut(5), BorderLayout.CENTER);
        add(helperPanel, BorderLayout.WEST);

        add(cardPanel, BorderLayout.CENTER);

        cardPanel.add(new JPanel(), "EMPTY_PANEL");//NON-NLS
    }


    public void addCard(@NotNull final Category buttonCategory)
    {
        if (buttonMap.containsKey(buttonCategory.getKey().toString()))
        {
            throw new IllegalArgumentException("a category with key='" + buttonCategory.getKey().toString() + "' is already registered");
        }

        categories.add(buttonCategory);
        cardPanel.add(buttonCategory.getMainComponent(), buttonCategory.getKey().toString());


        final HoverToggleButton toggleButton = new HoverToggleButton(buttonCategory.getTitle(), buttonCategory.getIconBig());
        toggleButton.setHorizontalTextPosition(JToggleButton.CENTER);
        toggleButton.setVerticalTextPosition(JToggleButton.BOTTOM);

        toggleButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                setSelectedComponent(buttonCategory.getKey());
            }
        });

        buttonCategory.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                if ("title".equals(evt.getPropertyName()))//NON-NLS
                {
                    toggleButton.setText(buttonCategory.getTitle());
                }
                else if ("iconBig".equals(evt.getPropertyName()))//NON-NLS
                {
                    toggleButton.setIcon(buttonCategory.getIconBig());
                }
                else if ("mainComponent".equals(evt.getPropertyName()))//NON-NLS
                {
                    cardPanel.remove((Component) evt.getOldValue());
                    cardPanel.add(buttonCategory.getMainComponent());

                    cardPanel.revalidate();
                    cardPanel.repaint();
                }
            }
        });

        buttonMap.put(buttonCategory.getKey(), toggleButton);

        JPanel helperPanel = new JPanel(new BorderLayout());
        helperPanel.setOpaque(false);
        helperPanel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        helperPanel.add(toggleButton, BorderLayout.CENTER);
        buttonPanel.add(helperPanel);
    }


    public void setSelectedComponent(@NotNull Object key)
    {
        Set<Object> keys = buttonMap.keySet();
        for (Object k : keys)
        {
            buttonMap.get(k).setSelected(false);
        }

        JToggleButton jToggleButton = buttonMap.get(key);
        jToggleButton.setSelected(true);
        jToggleButton.requestFocusInWindow();
        cardLayout.show(cardPanel, key.toString());
    }


    public void setSelectedIndex(int index)
    {
        setSelectedComponent(categories.get(index).getKey());
    }


    @Nullable
    public Category getSelectedCategory()
    {
        for (Category category : categories)
        {
            if (buttonMap.get(category.getKey()).isSelected())
            {
                return category;
            }
        }
        return null;
    }


    public int getSelectedIndex()
    {
        return categories.indexOf(getSelectedCategory());
    }


    @NotNull
    public JPanel getButtonPanel()
    {
        return leftPanel;
    }


    @Nullable
    public JComponent getSelectedComponent()
    {
        Category category = getSelectedCategory();
        if (category != null)
        {
            return category.getMainComponent();
        }
        return null;
    }


    public void setSelectionTabWidth(int width)
    {
        leftPanel.setPreferredSize(new Dimension(width, leftPanel.getPreferredSize().height));
    }


    private class ScrollablePanel extends JPanel implements Scrollable
    {
        private ScrollablePanel(@NotNull LayoutManager layout)
        {
            super(layout);
        }


        @NotNull
        public Dimension getPreferredScrollableViewportSize()
        {
            return buttonHelperPanel.getPreferredSize();
        }


        public int getScrollableUnitIncrement(@NotNull Rectangle visibleRect, int orientation, int direction)
        {
            return 20;
        }


        public int getScrollableBlockIncrement(@NotNull Rectangle visibleRect, int orientation, int direction)
        {
            return 20;
        }


        public boolean getScrollableTracksViewportWidth()
        {
            return true;
        }


        public boolean getScrollableTracksViewportHeight()
        {
            return false;
        }
    }
}
