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
package org.pentaho.reportdesigner.crm.report.model;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 09:47:48
 */
public class NumberFieldReportElement extends TextFieldReportElement
{
    @NotNull
    private NumberFormat format;
    @NotNull
    private String excelNumberFormat;


    public NumberFieldReportElement()
    {
        format = NumberFormat.getInstance();
        excelNumberFormat = "";
    }


    @NotNull
    public NumberFormat getFormat()
    {
        return format;
    }


    public void setFormat(@NotNull final NumberFormat format)
    {
        //noinspection ConstantConditions
        if (format == null)
        {
            throw new IllegalArgumentException("format must not be null");
        }

        final NumberFormat oldFormat = this.format;
        this.format = format;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.FORMAT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setFormat(oldFormat);
                }


                public void redo()
                {
                    setFormat(format);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.FORMAT, oldFormat, format);
    }


    @NotNull
    public String getExcelNumberFormat()
    {
        return excelNumberFormat;
    }


    public void setExcelNumberFormat(@NotNull final String excelNumberFormat)
    {
        //noinspection ConstantConditions
        if (excelNumberFormat == null)
        {
            throw new IllegalArgumentException("excelDateFormat must not be null");
        }

        final String oldExcelFormat = this.excelNumberFormat;
        this.excelNumberFormat = excelNumberFormat;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.EXCEL_NUMBER_FORMAT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setExcelNumberFormat(oldExcelFormat);
                }


                public void redo()
                {
                    setExcelNumberFormat(excelNumberFormat);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.EXCEL_NUMBER_FORMAT, oldExcelFormat, excelNumberFormat);
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.FORMAT, ((DecimalFormat) format).toPattern());
        xmlWriter.writeProperty(PropertyKeys.EXCEL_NUMBER_FORMAT, excelNumberFormat);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.FORMAT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            format = new DecimalFormat(XMLUtils.readProperty(PropertyKeys.FORMAT, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.EXCEL_NUMBER_FORMAT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            excelNumberFormat = XMLUtils.readProperty(PropertyKeys.EXCEL_NUMBER_FORMAT, node);
        }
    }
}
