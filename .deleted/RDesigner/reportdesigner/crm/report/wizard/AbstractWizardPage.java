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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 09:21:00
 */
public abstract class AbstractWizardPage implements WizardPage
{
    @NotNull
    private LinkedHashSet<WizardPageListener> wizardPageListeners;

    @Nullable
    private WizardPage previousWizardPage;
    @Nullable
    private WizardPage nextWizardPage;
    @NotNull
    private WizardDialog wizardDialog;


    public AbstractWizardPage()
    {
        wizardPageListeners = new LinkedHashSet<WizardPageListener>();
    }


    @NotNull
    public WizardDialog getWizardDialog()
    {
        return wizardDialog;
    }


    public void setWizardDialog(@NotNull WizardDialog wizardDialog)
    {
        this.wizardDialog = wizardDialog;
    }


    public void setPrevious(@Nullable WizardPage previousWizardPage)
    {
        this.previousWizardPage = previousWizardPage;
    }


    public void setNext(@Nullable WizardPage nextWizardPage)
    {
        this.nextWizardPage = nextWizardPage;
    }


    @Nullable
    public WizardPage getPrevious()
    {
        return previousWizardPage;
    }


    @Nullable
    public WizardPage getNext()
    {
        return nextWizardPage;
    }


    public boolean hasNext()
    {
        return nextWizardPage != null;
    }


    public boolean canNext()
    {
        return nextWizardPage != null;
    }


    public boolean hasPrevious()
    {
        return previousWizardPage != null;
    }


    public boolean canPrevious()
    {
        return previousWizardPage != null;
    }


    public boolean canCancel()
    {
        return true;
    }


    public boolean canFinish()
    {
        return true;
    }


    public void addWizardPageListener(@NotNull WizardPageListener wizardPageListener)
    {
        wizardPageListeners.add(wizardPageListener);
    }


    public void removeWizardPageListener(@NotNull WizardPageListener wizardPageListener)
    {
        wizardPageListeners.remove(wizardPageListener);
    }


    protected void fireWizardPageStateChanged()
    {
        LinkedHashSet<WizardPageListener> set = new LinkedHashSet<WizardPageListener>(wizardPageListeners);

        for (WizardPageListener wizardPageListener : set)
        {
            wizardPageListener.stateChanged();
        }
    }
}
