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
package org.pentaho.reportdesigner.crm.report.tests.simple1;

import org.jetbrains.annotations.NotNull;
import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.parser.extwriter.ReportWriterException;
import org.jfree.report.style.FontDefinition;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * User: Martin
 * Date: 06.10.2005
 * Time: 15:55:11
 */
@SuppressWarnings({"ALL"})
public class MultilineLabelTest
{
    public static void main(@NotNull String[] args) throws IOException, ReportWriterException
    {
        JFreeReport report = new JFreeReport();

        report.getReportConfiguration().setConfigProperty("org.jfree.report.layout.fontrenderer.UseMaxCharBounds", "true");//NON-NLS
        report.setName("SimpleAbsolutePositions");

        report.getReportHeader().addElement(LabelElementFactory.createLabelElement("CustomerLabel",
                                                                                   new Rectangle2D.Double(0, 0, 100, 100),
                                                                                   Color.RED,
                                                                                   ElementAlignment.LEFT,
                                                                                   new FontDefinition("Arial", 12),
                                                                                   "Customer\nLabel"));
        JFreeReportBoot.getInstance().start();



        PreviewFrame preview = new PreviewFrame(report);
        preview.pack();
        preview.setVisible(true);
    }
}