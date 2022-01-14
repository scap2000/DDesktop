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
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 16:03:02
 */
public class BandToplevelGroupReportElement extends BandToplevelReportElement
{
    private boolean repeat;


    public BandToplevelGroupReportElement()
    {
        setName("Band" + System.identityHashCode(this));
    }


    public boolean isRepeat()
    {
        return repeat;
    }


    public void setRepeat(final boolean repeat)
    {
        final boolean oldRepeat = this.repeat;
        this.repeat = repeat;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.REPEAT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setRepeat(oldRepeat);
                }


                public void redo()
                {
                    setRepeat(repeat);
                }
            });
            undo.endTransaction();
        }
        firePropertyChange(PropertyKeys.REPEAT, Boolean.valueOf(oldRepeat), Boolean.valueOf(repeat));
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        Object newParent = reportVisitor.visit(parent, this);

        ArrayList<ReportElement> children = new ArrayList<ReportElement>(getChildren());
        for (ReportElement reportElement : children)
        {
            reportElement.accept(newParent, reportVisitor);
        }
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.REPEAT, String.valueOf(repeat));
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.REPEAT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            repeat = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.REPEAT, node));
        }
    }
}
