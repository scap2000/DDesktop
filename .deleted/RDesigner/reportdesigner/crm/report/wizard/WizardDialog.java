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
package org.pentaho.reportdesigner.crm.report.wizard;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.components.ComponentFactory;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 09:26:50
 */
public class WizardDialog extends CenterPanelDialog implements WizardPageListener
{
    @NotNull
    private WizardPage currentPage;
    @NotNull
    private JComponent currentPageCenterPanel;

    @NotNull
    private JPanel centerPanel;

    @NotNull
    private JButton previousButton;
    @NotNull
    private JButton nextButton;
    @NotNull
    private JButton cancelButton;
    @NotNull
    private JButton finishButton;

    @NotNull
    private HashMap<String, WizardData> wizardDatas;

    private boolean ok;

    @NotNull
    private WizardHeaderComponent wizardHeaderComponent;
    @NotNull
    private ReportDialog reportDialog;


    public WizardDialog(@NotNull final ReportDialog reportDialog, @NotNull String title, boolean modal, @NotNull @NonNls final String key, @NotNull WizardPage firstPage)
    {
        super(reportDialog, title, modal);
        //noinspection ConstantConditions
        if (reportDialog == null)
        {
            throw new IllegalArgumentException("reportDialog must not be null");
        }
        this.reportDialog = reportDialog;
        //noinspection ConstantConditions
        if (firstPage == null)
        {
            throw new IllegalArgumentException("firstPage must not be null");
        }

        wizardDatas = new HashMap<String, WizardData>();

        currentPage = firstPage;

        @NonNls
        FormLayout formLayout = new FormLayout("4dlu, fill:pref:grow, 4dlu",
                                               "0dlu, " +
                                               "pref, " +
                                               "0dlu, " +
                                               "pref," +
                                               "4dlu, " +
                                               "fill:pref:grow, " +
                                               "4dlu");

        centerPanel = new JPanel(formLayout);
        setCenterPanel(centerPanel);

        wizardHeaderComponent = new WizardHeaderComponent();
        getContentPane().add(wizardHeaderComponent, BorderLayout.NORTH);

        currentPage.setWizardDialog(this);

        previousButton = ComponentFactory.createButton("R", "WizardDialog.Previous");
        nextButton = ComponentFactory.createButton("R", "WizardDialog.Next");

        cancelButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.cancel"));
        finishButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.finish"));

        JPanel buttonPanel = new JPanel();
        ButtonBarBuilder buttonBarBuilder = new ButtonBarBuilder(buttonPanel);
        buttonBarBuilder.addGlue();
        buttonBarBuilder.addGridded(cancelButton);
        buttonBarBuilder.addUnrelatedGap();
        buttonBarBuilder.addGriddedButtons(new JButton[]{previousButton, nextButton});
        buttonBarBuilder.addUnrelatedGap();
        buttonBarBuilder.addGridded(finishButton);
        setButtonPanel(nextButton, cancelButton, buttonPanel);

        previousButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                readData();

                WizardPage wizardPage = currentPage.getPrevious();
                if (wizardPage != null)
                {
                    uninstallCurrentPage();
                    currentPage = wizardPage;
                    installCurrentPage();
                }
            }
        });

        nextButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                readData();

                WizardPage next = currentPage.getNext();
                if (next != null)
                {
                    uninstallCurrentPage();
                    currentPage = next;
                    installCurrentPage();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                uninstallCurrentPage();
                ok = false;
                reportDialog.getWorkspaceSettings().storeDialogBounds(WizardDialog.this, key);
                dispose();
            }
        });

        finishButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                readData();
                uninstallCurrentPage();
                ok = true;
                reportDialog.getWorkspaceSettings().storeDialogBounds(WizardDialog.this, key);
                dispose();
            }
        });

        installCurrentPage();
    }


    @NotNull
    public ReportDialog getReportDialog()
    {
        return reportDialog;
    }


    private void readData()
    {
        WizardData[] wd = currentPage.getWizardDatas();
        for (WizardData wizardData : wd)
        {
            wizardDatas.put(wizardData.getKey(), wizardData);
        }
    }


    @NotNull
    public HashMap<String, WizardData> getWizardDatas()
    {
        return wizardDatas;
    }


    public boolean isOk()
    {
        return ok;
    }


    private void uninstallCurrentPage()
    {
        centerPanel.remove(currentPageCenterPanel);
        currentPage.removeWizardPageListener(this);
    }


    private void installCurrentPage()
    {
        CellConstraints cc = new CellConstraints();
        currentPage.setWizardDialog(this);
        currentPageCenterPanel = currentPage.getCenterPanel();
        this.centerPanel.add(currentPageCenterPanel, cc.xy(2, 6));
        currentPage.addWizardPageListener(this);
        stateChanged();

        this.centerPanel.revalidate();
        this.centerPanel.repaint();
    }


    public void stateChanged()
    {
        previousButton.setEnabled(currentPage.hasPrevious() && currentPage.canPrevious());
        nextButton.setEnabled(currentPage.hasNext() && currentPage.canNext());
        cancelButton.setEnabled(currentPage.canCancel());

        finishButton.setEnabled(canAllPagesFinish());

        if (currentPage.getIcon() != null)
        {
            wizardHeaderComponent.setIcon(currentPage.getIcon());
        }
        else
        {
            wizardHeaderComponent.setIcon(IconLoader.getInstance().getReportWizardIcon());
        }
        wizardHeaderComponent.setTitle(currentPage.getTitle());
        wizardHeaderComponent.setDescription(currentPage.getDescription());
    }


    private boolean canAllPagesFinish()
    {
        if (!currentPage.canFinish())
        {
            return false;
        }

        WizardPage previous = currentPage;
        while ((previous = previous.getPrevious()) != null)
        {
            if (!previous.canFinish())
            {
                return false;
            }
        }

        WizardPage next = currentPage;
        while ((next = next.getNext()) != null)
        {
            if (!next.canFinish())
            {
                return false;
            }
        }

        return true;
    }


    public void dispose()
    {
        super.dispose();

        currentPage.dispose();

        WizardPage previous = currentPage;
        while ((previous = previous.getPrevious()) != null)
        {
            previous.dispose();
        }

        WizardPage next = currentPage;
        while ((next = next.getNext()) != null)
        {
            next.dispose();
        }
    }
}
