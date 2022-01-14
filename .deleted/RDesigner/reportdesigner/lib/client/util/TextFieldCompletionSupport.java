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
package org.pentaho.reportdesigner.lib.client.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 11.02.2006
 * Time: 21:00:51
 */
public class TextFieldCompletionSupport
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(TextFieldCompletionSupport.class.getName());

    /**
     * Maximum number of entries shown in completion window.
     */
    private static final int MAX_COMPLETION_CHOICES = 10;

    //keys used to register actions
    @NotNull
    public static final String KEY_HIDE_COMPLETION_WINDOW = "hideCompletionWindow";
    @NotNull
    public static final String KEY_SELECTION_DOWN = "selectionDown";
    @NotNull
    public static final String KEY_SELECTION_UP = "selectionUp";
    @NotNull
    public static final String KEY_DELETE_SELECTED_CHOICE = "deleteSelectedChoice";
    @NotNull
    public static final String KEY_ACCEPT_COMPLETION_WITH_DEFAULT = "acceptCompletionWithDefault";
    @NotNull
    public static final String KEY_ACCEPT_COMPLETION = "acceptCompletion";


    private TextFieldCompletionSupport()
    {
    }


    /**
     * Adds auto completion support to a textfield. Note that the TreeSet will be modified.
     *
     * @param treeSet    the completions initially available
     * @param jTextField the textField to add the completion support
     */
    public static void initCompletionSupport(@NotNull final TreeSet<String> treeSet, @NotNull final JTextField jTextField)
    {
        final CompleteWindow completeWindow = new CompleteWindow(jTextField);

        jTextField.getActionMap().put(KEY_HIDE_COMPLETION_WINDOW, new AbstractAction()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                completeWindow.setVisible(false);
            }
        });

        jTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), KEY_HIDE_COMPLETION_WINDOW);

        //let selection cycle through list (visually upwards)
        jTextField.getActionMap().put(KEY_SELECTION_DOWN, new AbstractAction()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (completeWindow.getList().getSelectedIndex() < 0 || completeWindow.getList().getSelectedIndex() >= completeWindow.getCompletionListModel().getSize() - 1)
                {
                    completeWindow.getList().setSelectedIndex(0);
                    return;
                }
                completeWindow.getList().setSelectedIndex(completeWindow.getList().getSelectedIndex() + 1);
            }
        });

        jTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), KEY_SELECTION_DOWN);

        //let selection cycle through list (visually downwards)
        jTextField.getActionMap().put(KEY_SELECTION_UP, new AbstractAction()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (completeWindow.getList().getSelectedIndex() <= 0)
                {
                    completeWindow.getList().setSelectedIndex(completeWindow.getCompletionListModel().getSize() - 1);
                    return;
                }
                completeWindow.getList().setSelectedIndex(completeWindow.getList().getSelectedIndex() - 1);
            }
        });

        jTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), KEY_SELECTION_UP);

        final boolean[] ignoreInput = new boolean[]{false};//used to ignore actions when we are currently processing another action

        //action to delete the currently selected word from the list of possible completions
        jTextField.getActionMap().put(KEY_DELETE_SELECTED_CHOICE, new AbstractAction()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (completeWindow.getList().getSelectedIndex() != -1)
                {
                    String value = String.valueOf(completeWindow.getList().getSelectedValue());
                    treeSet.remove(value);
                    if (ignoreInput[0])
                    {
                        return;
                    }
                    updateWindow(treeSet, jTextField, completeWindow);
                }
            }
        });

        jTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.SHIFT_MASK), KEY_DELETE_SELECTED_CHOICE);

        Object key = jTextField.getInputMap().get(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        final Action enterAction = jTextField.getActionMap().get(key);

        //accepts the completion or triggers the action (if any) that was registered before
        jTextField.getActionMap().put(KEY_ACCEPT_COMPLETION_WITH_DEFAULT, new AbstractAction()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                int index = completeWindow.getList().getSelectedIndex();
                if (completeWindow.isVisible() && index != -1)
                {
                    completeWord(jTextField, completeWindow, ignoreInput);
                }
                else if (enterAction != null)
                {
                    completeWindow.setVisible(false);
                    enterAction.actionPerformed(e);
                }
            }
        });

        jTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), KEY_ACCEPT_COMPLETION_WITH_DEFAULT);
        //jTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), KEY_ACCEPT_COMPLETION);

        //accept the selected word in the list without doing any special processing
        jTextField.getActionMap().put(KEY_ACCEPT_COMPLETION, new AbstractAction()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                int index = completeWindow.getList().getSelectedIndex();
                if (completeWindow.isVisible() && index != -1)
                {
                    completeWord(jTextField, completeWindow, ignoreInput);
                }
            }
        });

        jTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_MASK), KEY_ACCEPT_COMPLETION);

        jTextField.addCaretListener(new CaretListener()
        {
            public void caretUpdate(@NotNull CaretEvent e)
            {
                if (ignoreInput[0])
                {
                    return;
                }
                updateWindow(treeSet, jTextField, completeWindow);
            }
        });

        //we remember each text that was entered in the textfield when we leave the textfield or when hit enter
        jTextField.addFocusListener(new FocusListener()
        {
            public void focusGained(@NotNull FocusEvent e)
            {
            }


            public void focusLost(@NotNull FocusEvent e)
            {
                String text = jTextField.getText();
                if (!"".equals(text))
                {
                    treeSet.add(text);
                }
                completeWindow.setVisible(false);
            }
        });

        jTextField.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                String text = jTextField.getText();
                if (!"".equals(text))
                {
                    treeSet.add(text);
                }
            }
        });

        //also complete the word if we click on an entry in the list
        completeWindow.getList().addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                completeWord(jTextField, completeWindow, ignoreInput);
            }
        });
    }


    /**
     * Completes the word we are currently editing with the word selected in the list.
     *
     * @param jTextField     the textfield
     * @param completeWindow the window/popup used to display the completion choices
     * @param ignoreInput    used as out-parameter to ignore other actions while we are modifying the caret position.
     */
    private static void completeWord(@NotNull JTextField jTextField, @NotNull CompleteWindow completeWindow, @NotNull boolean[] ignoreInput)
    {
        String text = jTextField.getText();
        String value = String.valueOf(completeWindow.getList().getSelectedValue());
        ignoreInput[0] = true;

        int caretPos = jTextField.getCaretPosition();

        String word;
        try
        {
            if (caretPos == 0)
            {
                completeWindow.setVisible(false);
                return;
            }

            int start = Utilities.getPreviousWord(jTextField, caretPos);
            word = text.substring(start, caretPos);
            String textToInsert = value.substring(word.length());
            jTextField.getDocument().insertString(caretPos, textToInsert, null);
        }
        catch (BadLocationException ex)
        {
            //exception is not useful for user in this case. Hopfully Hani does not see this...
        }

        completeWindow.setVisible(false);
        ignoreInput[0] = false;
    }


    /**
     * @param treeSet
     * @param jTextField
     * @param completeWindow
     */
    private static void updateWindow(@NotNull final TreeSet<String> treeSet, @NotNull final JTextField jTextField, @NotNull final CompleteWindow completeWindow)
    {
        if (!jTextField.isShowing())
        {
            return;
        }

        String word = getCurrentEditingWord(jTextField, completeWindow);

        if (word == null || "".equals(word))
        {
            completeWindow.setVisible(false);
            return;
        }

        SortedSet<String> strings = treeSet.tailSet(word);
        completeWindow.getCompletionListModel().clear();

        assembleCompletionChoices(strings, jTextField, word, completeWindow);
    }


    /**
     * Find the word we are currently editing (the word at the caret).
     *
     * @return the editing word
     */
    @Nullable
    private static String getCurrentEditingWord(@NotNull JTextField jTextField, @NotNull CompleteWindow completeWindow)
    {
        try
        {
            int caretPos = jTextField.getCaretPosition();
            if (caretPos == 0)
            {
                completeWindow.setVisible(false);
                return null;
            }
            int start = Utilities.getPreviousWord(jTextField, caretPos);
            String text = jTextField.getText();
            return text.substring(start, caretPos);
        }
        catch (BadLocationException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "TextFieldCompletionSupport.getCurrentEditingWord ", e);
            completeWindow.setVisible(false);
            return null;
        }
    }


    /**
     * Find the words suitable as a completion choice. This are usually the completionChoices starting with the part the user is editing.
     *
     * @param completionChoices the set containing all possible completion choices.
     * @param jTextField        the textfield
     * @param word              the word the user is currently editing
     * @param completeWindow    the window/popup to display the choices
     */
    private static void assembleCompletionChoices(@NotNull SortedSet<String> completionChoices, @NotNull JTextField jTextField, @NotNull String word, @NotNull CompleteWindow completeWindow)
    {
        if (!completionChoices.isEmpty())
        {
            Point p = jTextField.getLocationOnScreen();
            int x = p.x;
            int y = p.y + jTextField.getHeight();

            int count = 0;
            for (String s1 : completionChoices)
            {
                if (s1.startsWith(word) && !s1.equals(word))
                {
                    completeWindow.getCompletionListModel().addElement(s1);
                    count++;
                }
                if (count == MAX_COMPLETION_CHOICES)
                {
                    break;
                }
            }

            if (count > 0)
            {
                completeWindow.setLocation(x, y);
                if (!completeWindow.isVisible())
                {
                    completeWindow.setVisible(true);
                }
                completeWindow.updateWindowSize();
            }
            else
            {
                completeWindow.setVisible(false);
            }
        }
        else
        {
            completeWindow.setVisible(false);
        }
    }


    /**
     * A Popup to display the possible completion choices.
     */
    private static class CompleteWindow extends JPopupMenu
    {
        @NotNull
        private JList list;

        @NotNull
        private DefaultListModel completionListModel;


        private CompleteWindow(@NotNull final JTextField jTextField)
        {
            completionListModel = new DefaultListModel();
            list = new JList(completionListModel);

            JScrollPane scrollPane = new JScrollPane(list)
            {
                @NotNull
                public Dimension getPreferredSize()
                {
                    Dimension preferredSize = super.getPreferredSize();
                    return new Dimension(Math.max(jTextField.getWidth(), preferredSize.width), preferredSize.height);
                }
            };

            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            setFocusable(false);
            list.setFocusable(false);

            setInvoker(jTextField);
            setLayout(new BorderLayout());
            setBorder(null);

            add(scrollPane, BorderLayout.CENTER);
        }


        /**
         * Resizes the window to fit the lists content.
         */
        public void updateWindowSize()
        {
            getList().setVisibleRowCount(completionListModel.size());
            pack();
        }


        @NotNull
        public JList getList()
        {
            return list;
        }


        @NotNull
        public DefaultListModel getCompletionListModel()
        {
            return completionListModel;
        }
    }

}
