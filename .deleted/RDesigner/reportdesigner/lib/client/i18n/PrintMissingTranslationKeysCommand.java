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
package org.pentaho.reportdesigner.lib.client.i18n;


import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.commands.AbstractCommand;
import org.pentaho.reportdesigner.lib.client.commands.CommandEvent;

import java.util.Collection;


/**
 * User: Martin
 * Date: 02.04.2005
 * Time: 10:32:34
 */
public class PrintMissingTranslationKeysCommand extends AbstractCommand
{

    public PrintMissingTranslationKeysCommand()
    {
        super("PrintMissingTranslationKeysCommand");
        getTemplatePresentation().setText("Print Missing Translations");//NON-NLS
    }


    public void update(@NotNull CommandEvent event)
    {
        event.getPresentation().setEnabled(true);
    }


    @SuppressWarnings({"UseOfSystemOutOrSystemErr"})
    public void execute(@NotNull CommandEvent event)
    {
        StringBuilder stringBuffer = new StringBuilder(100);

        Collection<String> list = TranslationManager.getInstance().getMissingKeysList();
        for (String key : list)
        {
            stringBuffer.append(key);
            stringBuffer.append('\n');
        }

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Missing-Translations:");//NON-NLS
        System.out.println("---------------------");
        System.out.println(stringBuffer.toString());
        System.out.println("------------------------------------------------------------------------------------------");
    }
}
