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
package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.i18n.BundleAlreadyExistsException;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * User: Martin
 * Date: 09.08.2006
 * Time: 21:33:59
 */
public class MnemonicTester
{
    public static void main(@NotNull String[] args) throws BundleAlreadyExistsException, IllegalAccessException, UnsupportedLookAndFeelException, InstantiationException, ClassNotFoundException
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridLayout(0, 1));

        TranslationManager.getInstance().addBundle("R", ResourceBundle.getBundle("res.Translations", Locale.ENGLISH));//NON-NLS
        TranslationManager.getInstance().addBundle(TranslationManager.COMMON_BUNDLE_PREFIX, ResourceBundle.getBundle("res.Translations", Locale.ENGLISH));//NON-NLS
        TranslationManager.getInstance().addSupportedLocale(Locale.GERMAN);
        TranslationManager.getInstance().addSupportedLocale(Locale.ENGLISH);

        for (int i = 1; i <= 12; i++)
        {
            String text = TranslationManager.getInstance().getTranslation("R", "MnemonicTest" + i + ".TestKey");
            JLabel label = new JLabel(text);
            int mnemonic = TranslationManager.getInstance().getMnemonic("R", "MnemonicTest" + i + ".TestKey");
            label.setDisplayedMnemonic(mnemonic);
            int index = TranslationManager.getInstance().getDisplayedMnemonicIndex("R", "MnemonicTest" + i + ".TestKey");
            label.setDisplayedMnemonicIndex(index);
            frame.getContentPane().add(label);
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 200, 200);
        frame.setVisible(true);
    }
}
