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
package org.pentaho.reportdesigner.crm.report.templateplugin;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.wizard.WizardData;

import javax.swing.*;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 27.02.2006
 * Time: 18:19:24
 */
public interface LayoutPlugin
{
    @NotNull
    String getLocalizedName();


    @NotNull
    ImageIcon getSampleImage();


    void addVisualReportElements(@NotNull Report report, @NotNull HashMap<String, WizardData> wizardDatas, @NotNull LayoutStyle layoutStyle) throws LayoutException;
}
