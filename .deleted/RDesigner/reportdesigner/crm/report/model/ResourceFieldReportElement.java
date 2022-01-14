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
 * Time: 09:52:34
 */
public class ResourceFieldReportElement extends TextFieldReportElement
{
    @NotNull
    private String resourceBase;


    public ResourceFieldReportElement()
    {
        resourceBase = "";
    }


    @NotNull
    public String getResourceBase()
    {
        return resourceBase;
    }


    public void setResourceBase(@NotNull final String resourceBase)
    {
        //noinspection ConstantConditions
        if (resourceBase == null)
        {
            throw new IllegalArgumentException("resourceBase must not be null");
        }

        final String oldResourceBase = this.resourceBase;
        this.resourceBase = resourceBase;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.RESOURCE_BASE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setResourceBase(oldResourceBase);
                }


                public void redo()
                {
                    setResourceBase(resourceBase);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.RESOURCE_BASE, oldResourceBase, resourceBase);
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.RESOURCE_BASE, resourceBase);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.RESOURCE_BASE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            resourceBase = XMLUtils.readProperty(PropertyKeys.RESOURCE_BASE, node);
        }
    }

}
