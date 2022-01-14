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
import org.jfree.report.ParameterDataRow;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportInitialisationException;
import org.jfree.report.modules.misc.datafactory.StaticDataFactory;
import org.jfree.report.util.ReportProperties;

/**
 * User: Martin
 * Date: 28.06.2006
 * Time: 05:01:04
 */
@SuppressWarnings({"ALL"})
public class TestStaticDataFactory
{
    public static void main(@NotNull String[] args)
            throws ReportInitialisationException, ReportDataFactoryException
    {
        StaticDataFactory staticDataFactory = new StaticDataFactory();
        staticDataFactory.queryData("org.test.Klasse#methode()", new ParameterDataRow(new ReportProperties()));//java.lang.StringIndexOutOfBoundsException: String index out of range: -1
        staticDataFactory.queryData("org.test.Klasse#methode", new ParameterDataRow(new ReportProperties()));//org.jfree.report.ReportInitialisationException: Malformed query: org.test.Klasse#methode
    }
}
