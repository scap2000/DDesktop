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
 * Time: 08:50:18
 */
public class LabelReportElement extends TextReportElement
{
    @NotNull
    private String text;


    public LabelReportElement()
    {
        text = "";
    }


    @NotNull
    public String getText()
    {
        return text;
    }


    public void setText(@NotNull final String text)
    {
        //noinspection ConstantConditions
        if (text == null)
        {
            throw new IllegalArgumentException("text must not be null");
        }

        final String oldText = this.text;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.TEXT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setText(oldText);
                }


                public void redo()
                {
                    setText(text);
                }
            });
            undo.endTransaction();
        }

        this.text = text;
        firePropertyChange(PropertyKeys.TEXT, oldText, text);

    }


    @NotNull
    public String getPaintText()
    {
        return text;
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);
        xmlWriter.writeProperty(PropertyKeys.TEXT, text);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.TEXT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            text = XMLUtils.readProperty(PropertyKeys.TEXT, node);
        }
    }

}
