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
package org.pentaho.reportdesigner.crm.report.wizard.reportgeneratewizard;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.wizard.WizardDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 10:13:18
 */
public class ReportGenerateWizardFactory
{
    private ReportGenerateWizardFactory()
    {
    }


    @NotNull
    public static WizardDialog createWizardDialog(@NotNull ReportDialog reportDialog)
    {
        WizardPageConnectionSettings wizardPageConnectionSettings = new WizardPageConnectionSettings();
        WizardPageSQLQuery wizardPageSQLQuery = new WizardPageSQLQuery();
        WizardPageVisibleFields wizardPageVisibleFields = new WizardPageVisibleFields();
        WizardPageFieldExpressions wizardPageFieldExpressions = new WizardPageFieldExpressions();
        WizardPagePageSetup wizardPagePageSetup = new WizardPagePageSetup();

        wizardPageConnectionSettings.setNext(wizardPageSQLQuery);

        wizardPageSQLQuery.setPrevious(wizardPageConnectionSettings);
        wizardPageSQLQuery.setNext(wizardPageVisibleFields);

        wizardPageVisibleFields.setPrevious(wizardPageSQLQuery);
        wizardPageVisibleFields.setNext(wizardPageFieldExpressions);

        wizardPageFieldExpressions.setPrevious(wizardPageVisibleFields);
        wizardPageFieldExpressions.setNext(wizardPagePageSetup);

        wizardPagePageSetup.setPrevious(wizardPageFieldExpressions);


        WizardDialog wizardDialog = new WizardDialog(reportDialog,
                                                     TranslationManager.getInstance().getTranslation("R", "ReportGenerateWizard.Title"),
                                                     true,
                                                     "ReportGenerateWizard",
                                                     wizardPageConnectionSettings);

        return wizardDialog;
    }

}
