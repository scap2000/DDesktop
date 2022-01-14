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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.util.GUIUtils;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 09.02.2006
 * Time: 10:04:01
 */
public class ProgressDialog extends CenterPanelDialog
{

    @NotNull
    private String task;
    @NotNull
    private JLabel taskLabel;


    @NotNull
    public static ProgressDialog createProgressDialog(@NotNull Component parent, @NotNull String title, @NotNull String task)
    {
        ProgressDialog progressDialog;

        if (parent instanceof Dialog)
        {
            Dialog dialog = (Dialog) parent;
            progressDialog = new ProgressDialog(dialog, title, task);
        }
        else if (parent instanceof Frame)
        {
            Frame frame = (Frame) parent;
            progressDialog = new ProgressDialog(frame, title, task);
        }
        else
        {
            Window windowAncestor = SwingUtilities.getWindowAncestor(parent);
            if (windowAncestor instanceof Dialog)
            {
                Dialog dialog = (Dialog) windowAncestor;
                progressDialog = new ProgressDialog(dialog, title, task);
            }
            else
            {
                Frame frame = (Frame) windowAncestor;
                progressDialog = new ProgressDialog(frame, title, task);
            }
        }

        progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        progressDialog.pack();
        GUIUtils.ensureMinimumDialogWidth(progressDialog, 300);
        WindowUtils.setLocationRelativeTo(progressDialog, parent);
        return progressDialog;
    }


    private ProgressDialog(@NotNull Frame owner, @NotNull String title, @NotNull String task)
    {
        super(owner, title, true);
        this.task = task;
        init();
    }


    private ProgressDialog(@NotNull Dialog owner, @NotNull String title, @NotNull String task)
    {
        super(owner, title, true);
        this.task = task;
        init();
    }


    public void setTask(@NotNull String task)
    {
        this.task = task;
        taskLabel.setText(task);
    }


    private void init()
    {
        @NonNls
        FormLayout formLayout = new FormLayout("0dlu, fill:100dlu:grow, 0dlu", "0dlu:grow, pref, 4dlu, pref, 0dlu:grow");
        @NonNls
        CellConstraints cc = new CellConstraints();
        JPanel centerPanel = new JPanel(formLayout);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(true);

        centerPanel.add(progressBar, cc.xy(2, 2));
        taskLabel = new JLabel(task);
        centerPanel.add(taskLabel, cc.xy(2, 4));

        setCenterPanel(centerPanel);
        setButtonPanel(new JButton(), new JButton(), new JPanel());
    }
}
