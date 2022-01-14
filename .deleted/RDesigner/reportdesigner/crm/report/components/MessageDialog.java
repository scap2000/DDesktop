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
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.components.docking.IconCreator;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.CharArrayWriter;
import java.io.PrintWriter;

/**
 * User: Martin
 * Date: 09.02.2006
 * Time: 13:17:10
 */
@SuppressWarnings({"HardCodedStringLiteral", "HardcodedLineSeparator"})
public class MessageDialog extends JDialog
{
    public static void main(@NotNull String[] args)
    {
        JFrame frame = new JFrame("testFrame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocation(400, 400);

        MessageDialog messageDialog = new MessageDialog(frame, "Title", null);

        try
        {
            //longMethoNameXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX();
            recursiveMethod(100);
        }
        catch (Throwable t)
        {
            messageDialog.setMessage("Message\nMessage\nMessage\n" +
                                     "Message\n" +
                                     "Message\n" +
                                     "Message\n" +
                                     "Message\n" +
                                     "Message\n" +
                                     "Message\n" +
                                     "Message\n" +
                                     "Message\n" +
                                     "Message");
            messageDialog.setDetailThrowable(t);
        }


        messageDialog.pack();

        frame.setVisible(true);
        messageDialog.setVisible(true);
    }


    @SuppressWarnings({"UnusedDeclaration"})
    private static void longMethoNameXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX()
    {
        throw new RuntimeException("Bla");
    }


    private static void recursiveMethod(int i)
    {
        i--;
        if (i > 0)
        {
            recursiveMethod(i);
        }
        else
        {
            throw new RuntimeException();
        }
    }


    public static void showExceptionDialog(@NotNull final Component comp, @NotNull final String title, @NotNull final String message, @NotNull final Throwable t)
    {
        //noinspection ConstantConditions
        if (comp == null)
        {
            throw new IllegalArgumentException("comp must not be null");
        }

        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                Window w = SwingUtilities.getWindowAncestor(comp);
                showExceptionDialog(w, title, message, t);
            }
        });
    }


    public static void showExceptionDialog(@Nullable final Window w, @NotNull final String title, @NotNull final String message, @NotNull final Throwable t)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                if (w == null)
                {
                    MessageDialog messageDialog = new MessageDialog(title, null);
                    messageDialog.setModal(false);
                    messageDialog.setMessage(message);
                    messageDialog.setDetailThrowable(t);
                    messageDialog.pack();
                    WindowUtils.setLocationRelativeTo(messageDialog, null);
                    messageDialog.setVisible(true);
                }
                else if (w instanceof Dialog)
                {
                    MessageDialog messageDialog = new MessageDialog((Dialog) w, title, null);
                    messageDialog.setMessage(message);
                    messageDialog.setDetailThrowable(t);
                    messageDialog.pack();
                    WindowUtils.setLocationRelativeTo(messageDialog, w);
                    messageDialog.setVisible(true);
                }
                else if (w instanceof Frame)
                {
                    MessageDialog messageDialog = new MessageDialog((Frame) w, title, null);
                    messageDialog.setMessage(message);
                    messageDialog.setDetailThrowable(t);
                    messageDialog.pack();
                    WindowUtils.setLocationRelativeTo(messageDialog, w);
                    messageDialog.setVisible(true);
                }
            }
        });
    }


    public static void showExceptionDialog(@NotNull final Window w, @NotNull final String title, @NotNull final String message, @NotNull final Throwable t, @NotNull final Runnable execAfter)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                //noinspection ConstantConditions
                if (w == null)
                {
                    MessageDialog messageDialog = new MessageDialog(title, execAfter);
                    messageDialog.setModal(false);
                    messageDialog.setMessage(message);
                    messageDialog.setDetailThrowable(t);
                    messageDialog.pack();
                    WindowUtils.setLocationRelativeTo(messageDialog, null);
                    messageDialog.setVisible(true);
                }
                else if (w instanceof Dialog)
                {
                    MessageDialog messageDialog = new MessageDialog((Dialog) w, title, execAfter);
                    messageDialog.setMessage(message);
                    messageDialog.setDetailThrowable(t);
                    messageDialog.pack();
                    WindowUtils.setLocationRelativeTo(messageDialog, w);
                    messageDialog.setVisible(true);
                }
                else if (w instanceof Frame)
                {
                    MessageDialog messageDialog = new MessageDialog((Frame) w, title, execAfter);
                    messageDialog.setMessage(message);
                    messageDialog.setDetailThrowable(t);
                    messageDialog.pack();
                    WindowUtils.setLocationRelativeTo(messageDialog, w);
                    messageDialog.setVisible(true);
                }
            }
        });
    }


    @NotNull
    private JToggleButton detailsButton;
    @NotNull
    private JTextArea detailTextArea;
    @NotNull
    private JTextArea messageTextArea;
    @NotNull
    private JPanel contentPane;
    @Nullable
    private Runnable execAfter;


    public MessageDialog(@NotNull String title, @Nullable Runnable execAfter)
    {
        super();
        this.execAfter = execAfter;
        setTitle(title);
        init();
    }


    public MessageDialog(@NotNull Frame owner, @NotNull String title, @Nullable Runnable execAfter)
    {
        super(owner, title, true);
        this.execAfter = execAfter;
        init();
    }


    public MessageDialog(@NotNull Dialog owner, @NotNull String title, @Nullable Runnable execAfter)
    {
        super(owner, title, true);
        this.execAfter = execAfter;
        init();
    }


    private void init()
    {
        setResizable(false);

        JPanel upperPanel = new JPanel(new BorderLayout());
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JPanel helperPanel = new JPanel(new BorderLayout(10, 0));
        Icon icon = UIManager.getIcon("OptionPane.errorIcon");//NON-NLS

        helperPanel.add(new JLabel(IconCreator.converIconToImageIcon(icon)), BorderLayout.WEST);
        messageTextArea = new JTextArea();
        UndoHelper.installUndoSupport(messageTextArea);
        TextComponentHelper.installDefaultPopupMenu(messageTextArea);

        messageTextArea.setEditable(false);
        messageTextArea.setRows(8);
        messageTextArea.setColumns(100);
        helperPanel.add(new JScrollPane(messageTextArea), BorderLayout.CENTER);

        upperPanel.add(helperPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton closeButton = ComponentFactory.createButton("R", "MessageDialog.Close");
        closeButton.setDefaultCapable(true);
        detailsButton = ComponentFactory.createToggleButton("R", "MessageDialog.Details");
        detailsButton.setEnabled(false);
        buttonPanel.add(detailsButton);
        buttonPanel.add(closeButton);

        JPanel helperPanel2 = new JPanel(new GridBagLayout());
        helperPanel2.add(buttonPanel);
        upperPanel.add(helperPanel2, BorderLayout.SOUTH);

        contentPane.add(upperPanel, BorderLayout.NORTH);

        getRootPane().setDefaultButton(closeButton);

        detailTextArea = new JTextArea();
        UndoHelper.installUndoSupport(detailTextArea);
        TextComponentHelper.installDefaultPopupMenu(detailTextArea);

        detailTextArea.setEditable(false);
        detailTextArea.setRows(20);

        final JScrollPane detailScrollPane = new JScrollPane(detailTextArea);
        final JPanel scrollPanePanel = new JPanel(new BorderLayout());
        scrollPanePanel.add(detailScrollPane, BorderLayout.CENTER);
        scrollPanePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        getContentPane().add(contentPane, BorderLayout.CENTER);

        closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                dispose();
                if (execAfter != null)
                {
                    execAfter.run();
                }
            }
        });

        detailsButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Point p = getLocation();
                Dimension s = getSize();

                if (detailsButton.isSelected())
                {
                    contentPane.add(scrollPanePanel, BorderLayout.CENTER);
                    setResizable(true);
                }
                else
                {
                    contentPane.remove(scrollPanePanel);
                    setResizable(false);
                }

                contentPane.revalidate();

                Dimension nS = getPreferredSize();

                int x = p.x + s.width / 2 - nS.width / 2;
                int y = p.y;

                if (x < 0)
                {
                    x = 0;
                }

                if (y < 0)
                {
                    y = 0;
                }

                setLocation(x, y);

                contentPane.repaint();

                pack();
            }
        });

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(@NotNull WindowEvent e)
            {
                dispose();
                if (execAfter != null)
                {
                    execAfter.run();
                }
            }
        });
    }


    public void setMessage(@NotNull String message)
    {
        messageTextArea.setText(message);
        messageTextArea.setCaretPosition(0);
    }


    public void setDetailMessage(@NotNull String detail)
    {
        detailTextArea.setText(detail);
        detailTextArea.setCaretPosition(0);
        detailsButton.setEnabled(true);
    }


    public void setDetailThrowable(@Nullable Throwable ex)
    {
        if (ex != null)
        {
            CharArrayWriter charArrayWriter = new CharArrayWriter();
            PrintWriter pw = null;
            try
            {
                //noinspection IOResourceOpenedButNotSafelyClosed
                pw = new PrintWriter(charArrayWriter);
                ex.printStackTrace(pw);

                setDetailMessage(charArrayWriter.toString());
            }
            finally
            {
                IOUtil.closeStream(pw);
            }
        }
    }


}
