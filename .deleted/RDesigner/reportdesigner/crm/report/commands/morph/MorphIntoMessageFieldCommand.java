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
package org.pentaho.reportdesigner.crm.report.commands.morph;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 11:26:24
 */
public class MorphIntoMessageFieldCommand extends AbstractMorphIntoCommand
{
    public MorphIntoMessageFieldCommand()
    {
        super("MorphIntoMessageFieldCommand");
        getTemplatePresentation().setText(TranslationManager.getInstance().getTranslation("R", "MorphIntoMessageFieldCommand.Text"));
        getTemplatePresentation().setDescription(TranslationManager.getInstance().getTranslation("R", "MorphIntoMessageFieldCommand.Description"));
        getTemplatePresentation().setMnemonic(TranslationManager.getInstance().getMnemonic("R", "MorphIntoMessageFieldCommand.Text"));
        getTemplatePresentation().setDisplayedMnemonicIndex(TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "MorphIntoMessageFieldCommand.Text"));

        getTemplatePresentation().setAccelerator(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "MorphIntoMessageFieldCommand.Accelerator")));
    }


    @NotNull
    public Class getTargetClass()
    {
        return MessageFieldReportElement.class;
    }
}
