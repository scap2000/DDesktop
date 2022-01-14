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
package org.pentaho.reportdesigner.lib.client.components;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.factories.Borders;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: tgdschmm
 * Date: 20.07.2005
 * Time: 08:41:40
 */
public class CenterPanelDialog extends JDialog
{
    @NotNull
    private Runnable visibilityRunnable;

    public enum ButtonAlignment
    {
        @NotNull LEFT,
        @NotNull CENTER,
        @NotNull RIGHT
    }

    @NotNull
    private static final String ESCAPE = "ESCAPE";
    @NotNull
    private static final String CANCEL_DIALOG = "cancelDialog";

    @NotNull
    private JPanel contentPanel;
    @NotNull
    private JPanel centerPanel;
    @NotNull
    private JPanel buttonPanel;
    @NotNull
    private JComponent initialFocusedComponent;


    @NotNull
    public static CenterPanelDialog createDialog(@NotNull Component parent, @NotNull String title, boolean modal)
    {
        if (parent instanceof Frame)
        {
            Frame frame = (Frame) parent;
            return new CenterPanelDialog(frame, title, modal);
        }
        else if (parent instanceof Dialog)
        {
            Dialog dialog = (Dialog) parent;
            return new CenterPanelDialog(dialog, title, modal);
        }
        else
        {
            Window window = SwingUtilities.getWindowAncestor(parent);
            if (window instanceof Frame)
            {
                Frame frame = (Frame) window;
                return new CenterPanelDialog(frame, title, modal);
            }
            else
            {
                return new CenterPanelDialog((Dialog) window, title, modal);
            }
        }
    }


    /**
     * @deprecated use the constructors with a title argument
     */
    @Deprecated
    public CenterPanelDialog() throws HeadlessException
    {
        init();
    }


    /**
     * @deprecated use the constructors with a title argument
     */
    @Deprecated
    public CenterPanelDialog(@NotNull Frame owner) throws HeadlessException
    {
        super(owner);
        init();
    }


    /**
     * @deprecated use the constructors with a title argument
     */
    @Deprecated
    public CenterPanelDialog(@NotNull Frame owner, boolean modal) throws HeadlessException
    {
        super(owner, modal);
        init();
    }


    public CenterPanelDialog(@NotNull Frame owner, @NotNull String title) throws HeadlessException
    {
        super(owner, title);
        init();
    }


    public CenterPanelDialog(@NotNull Frame owner, @NotNull String title, boolean modal) throws HeadlessException
    {
        super(owner, title, modal);
        init();
    }


    public CenterPanelDialog(@NotNull Frame owner, @NotNull String title, boolean modal, @NotNull GraphicsConfiguration gc)
    {
        super(owner, title, modal, gc);
        init();
    }


    /**
     * @deprecated use the constructors with a title argument
     */
    @Deprecated
    public CenterPanelDialog(@NotNull Dialog owner) throws HeadlessException
    {
        super(owner);
        init();
    }


    /**
     * @deprecated use the constructors with a title argument
     */
    @Deprecated
    public CenterPanelDialog(@NotNull Dialog owner, boolean modal) throws HeadlessException
    {
        super(owner, modal);
        init();
    }


    public CenterPanelDialog(@NotNull Dialog owner, @NotNull String title) throws HeadlessException
    {
        super(owner, title);
        init();
    }


    public CenterPanelDialog(@NotNull Dialog owner, @NotNull String title, boolean modal) throws HeadlessException
    {
        super(owner, title, modal);
        init();
    }


    public CenterPanelDialog(@NotNull Dialog owner, @NotNull String title, boolean modal, @NotNull GraphicsConfiguration gc) throws HeadlessException
    {
        super(owner, title, modal, gc);
        init();
    }


    private void init()
    {
        contentPanel = new JPanel(new BorderLayout());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        contentPanel.setBorder(Borders.DIALOG_BORDER);

        addWindowListener(new WindowAdapter()
        {
            public void windowOpened(@NotNull WindowEvent e)
            {
                if (initialFocusedComponent != null)
                {
                    initialFocusedComponent.requestFocusInWindow();
                }
            }
        });
    }


    public void setDialogBorder(@NotNull Border border)
    {
        contentPanel.setBorder(border);
    }


    public void setCenterPanel(@NotNull JPanel centerPanel)
    {
        if (this.centerPanel != null)
        {
            contentPanel.remove(this.centerPanel);
        }

        this.centerPanel = centerPanel;
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    public void setButtonPanel(@NotNull JButton defaultButton, @NotNull final JButton cancelButton, @NotNull JPanel buttonPanel)
    {
        if (this.buttonPanel != null)
        {
            contentPanel.remove(this.buttonPanel);
        }

        this.buttonPanel = buttonPanel;
        buttonPanel.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(defaultButton);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(ESCAPE), CANCEL_DIALOG);
        getRootPane().getActionMap().put(CANCEL_DIALOG, new AbstractAction()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                cancelButton.doClick();
            }
        });

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(@NotNull WindowEvent e)
            {
                cancelButton.doClick();
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    public void setButtons(@NotNull JButton defaultButton, @NotNull JButton cancelButton, @NotNull JButton... buttons)
    {
        setButtons(defaultButton, cancelButton, ButtonAlignment.RIGHT, buttons);
    }


    public void setButtons(@NotNull JButton defaultButton, @NotNull JButton cancelButton, @NotNull ButtonAlignment buttonAlignment, @NotNull JButton... buttons)
    {
        JPanel buttonPanel = new JPanel();
        ButtonBarBuilder buttonBarBuilder = new ButtonBarBuilder(buttonPanel);
        if (buttonAlignment != ButtonAlignment.LEFT)
        {
            buttonBarBuilder.addGlue();
        }
        for (int i = 0; i < buttons.length; i++)
        {
            JButton button = buttons[i];
            if (button != null)
            {
                buttonBarBuilder.addGridded(button);
                if (i < buttons.length - 1)
                {
                    buttonBarBuilder.addRelatedGap();
                }
            }
            else
            {
                buttonBarBuilder.addUnrelatedGap();
            }
        }
        if (buttonAlignment != ButtonAlignment.RIGHT)
        {
            buttonBarBuilder.addGlue();
        }
        setButtonPanel(defaultButton, cancelButton, buttonBarBuilder.getPanel());
    }


    public void setInitialFocusedComponent(@NotNull JComponent initialFocusedComponent)
    {
        this.initialFocusedComponent = initialFocusedComponent;
    }


    public void executeAfterMakingVisible(@NotNull Runnable runnable)
    {
        this.visibilityRunnable = runnable;
    }


    public void setVisible(boolean b)
    {
        if (b && visibilityRunnable != null)
        {
            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    visibilityRunnable.run();
                }
            });
        }
        super.setVisible(b);
    }

}
