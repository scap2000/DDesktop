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
package org.pentaho.reportdesigner.crm.report.tests.styles;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.TextElement;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.parser.extwriter.ReportWriterException;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.ReportConfiguration;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * User: Martin
 * Date: 06.10.2005
 * Time: 15:55:11
 */
@SuppressWarnings({"ALL"})
public class PaddingTest
{
    public static void main(@NotNull String[] args) throws IOException, ReportWriterException
    {
        JFreeReport report = new JFreeReport();

        report.setName("BorderTest");

        TextElement label1 = LabelElementFactory.createLabelElement("Label1",
                                                                    new Rectangle2D.Double(0, 0, 200, 100),
                                                                    Color.RED,
                                                                    ElementAlignment.LEFT,
                                                                    new FontDefinition("Arial", 12),
                                                                    "Label1");

        report.getReportHeader().addElement(label1);

        label1.getStyle().setStyleProperty(ElementStyleKeys.BACKGROUND_COLOR, new Color(255, 127, 127, 120));

        TextElement label2 = LabelElementFactory.createLabelElement("Label2",
                                                                    new Rectangle2D.Double(0, 110, 200, 100),
                                                                    Color.RED,
                                                                    ElementAlignment.LEFT,
                                                                    new FontDefinition("Arial", 12),
                                                                    "Label2");

        report.getReportHeader().addElement(label2);

        label2.getStyle().setStyleProperty(ElementStyleKeys.BACKGROUND_COLOR, new Color(255, 127, 127, 120));
        label2.getStyle().setStyleProperty(ElementStyleKeys.PADDING_TOP, new Float(10));
        label2.getStyle().setStyleProperty(ElementStyleKeys.PADDING_LEFT, new Float(10));
        label2.getStyle().setStyleProperty(ElementStyleKeys.PADDING_RIGHT, new Float(10));
        label2.getStyle().setStyleProperty(ElementStyleKeys.PADDING_BOTTOM, new Float(10));


        TextElement label3 = LabelElementFactory.createLabelElement("Label3",
                                                                    new Rectangle2D.Double(210, 0, 200, 100),
                                                                    Color.RED,
                                                                    ElementAlignment.LEFT,
                                                                    new FontDefinition("Arial", 12),
                                                                    "Label3");

        report.getReportHeader().addElement(label3);

        label3.getStyle().setStyleProperty(ElementStyleKeys.BACKGROUND_COLOR, new Color(255, 127, 127, 120));
        label3.getStyle().setStyleProperty(ElementStyleKeys.PADDING_TOP, new Float(10));
        label3.getStyle().setStyleProperty(ElementStyleKeys.PADDING_LEFT, new Float(10));
        label3.getStyle().setStyleProperty(ElementStyleKeys.PADDING_RIGHT, new Float(10));
        label3.getStyle().setStyleProperty(ElementStyleKeys.PADDING_BOTTOM, new Float(10));


        final ReportConfiguration config = new ReportConfiguration(report.getReportConfiguration());

        JFreeReportBoot.getInstance().start();

        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setSize(600, 400);
        preview.setVisible(true);
    }
}