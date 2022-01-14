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

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 06.08.2006
 * Time: 15:57:09
 */
@SuppressWarnings({"ALL"})
public class LongLabelTest
{
    public static void main(@NotNull String[] args)
    {
        Toolkit.getDefaultToolkit().setDynamicLayout(true);

        JFrame frame = new JFrame();
        JLabel label = new JLabel("C:\\Daten\\07_Projekte\\PentahoReportDesigner\\src\\org\\pentaho\\reportdesigner\\crm\\report\\commands\\AddPropertiesDataSetCommand.java");
        //label.setHorizontalAlignment(JLabel.TRAILING);
        //label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 300, 100);

        frame.setVisible(true);
    }
}
