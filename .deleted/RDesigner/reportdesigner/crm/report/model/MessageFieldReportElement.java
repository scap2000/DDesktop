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

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 08:55:24
 */
public class MessageFieldReportElement extends TextReportElement
{
    @NotNull
    private String formatString;
    @NotNull
    private String nullString;
    @NotNull
    private Formula formula;


    public MessageFieldReportElement()
    {
        String field = "field" + FieldNameFactory.getInstance().getNextFreeNumber("field");//NON-NLS
        formatString = field + ": $(" + field + ")";
        nullString = "";
        formula = new Formula("");
    }


    @NotNull
    public String getNullString()
    {
        return nullString;
    }


    public void setNullString(@NotNull final String nullString)
    {
        //noinspection ConstantConditions
        if (nullString == null)
        {
            throw new IllegalArgumentException("nullString must not be null");
        }

        final String oldNullString = this.nullString;
        this.nullString = nullString;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.PAGE_BREAK_BEFORE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setNullString(oldNullString);
                }


                public void redo()
                {
                    setNullString(nullString);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.NULL_STRING, oldNullString, nullString);
    }


    @NotNull
    public String getFormatString()
    {
        return formatString;
    }


    public void setFormatString(@NotNull final String formatString)
    {
        //noinspection ConstantConditions
        if (formatString == null)
        {
            throw new IllegalArgumentException("formatString must not be null");
        }

        final String oldFormatString = this.formatString;
        this.formatString = formatString;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.FORMAT_STRING);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setFormatString(oldFormatString);
                }


                public void redo()
                {
                    setFormatString(formatString);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.FORMAT_STRING, oldFormatString, formatString);
    }


    @NotNull
    public Formula getFormula()
    {
        return formula;
    }


    public void setFormula(@NotNull final Formula formula)
    {
        //noinspection ConstantConditions
        if (formula == null)
        {
            throw new IllegalArgumentException("formula must not be null");
        }

        final Formula oldFormula = this.formula;
        this.formula = formula;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.FORMULA);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setFormula(oldFormula);
                }


                public void redo()
                {
                    setFormula(formula);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.FORMULA, oldFormula, formula);
    }


    @NotNull
    public String getPaintText()
    {
        return getFormatString();
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.FORMAT_STRING, formatString);
        xmlWriter.writeProperty(PropertyKeys.NULL_STRING, nullString);

        xmlWriter.startElement(PropertyKeys.FORMULA);
        formula.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.FORMULA);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.FORMAT_STRING.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            formatString = XMLUtils.readProperty(PropertyKeys.FORMAT_STRING, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.NULL_STRING.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            nullString = XMLUtils.readProperty(PropertyKeys.NULL_STRING, node);
        }
        else if (PropertyKeys.FORMULA.equals(node.getRawName()))
        {
            formula = new Formula("");
            formula.readObject(node, xmlContext);
        }
    }
}
