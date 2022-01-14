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

import javax.swing.*;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 09:17:42
 */
public interface WizardPage
{

    @NotNull
    JComponent getCenterPanel();


    @Nullable
    WizardPage getPrevious();


    @Nullable
    WizardPage getNext();


    boolean hasNext();


    boolean canNext();


    boolean hasPrevious();


    boolean canPrevious();


    boolean canCancel();


    boolean canFinish();


    void addWizardPageListener(@NotNull WizardPageListener wizardPageListener);


    void removeWizardPageListener(@NotNull WizardPageListener wizardPageListener);


    @NotNull
    WizardData[] getWizardDatas();


    void setWizardDialog(@NotNull WizardDialog wizardDialog);


    @NotNull
    WizardDialog getWizardDialog();


    @NotNull
    String getTitle();


    @Nullable
    ImageIcon getIcon();


    @NotNull
    String getDescription();


    void dispose();
}
