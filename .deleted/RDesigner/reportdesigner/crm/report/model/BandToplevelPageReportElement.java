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
public class BandToplevelPageReportElement extends BandToplevelReportElement
{
    private boolean displayOnFirstPage;
    private boolean displayOnLastPage;


    public BandToplevelPageReportElement()
    {
        setName("Band" + System.identityHashCode(this));
    }


    public boolean isDisplayOnFirstPage()
    {
        return displayOnFirstPage;
    }


    public void setDisplayOnFirstPage(final boolean displayOnFirstPage)
    {
        final boolean oldDisplayOnFirstPage = this.displayOnFirstPage;
        this.displayOnFirstPage = displayOnFirstPage;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.DISPLAY_ON_FIRST_PAGE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setDisplayOnFirstPage(oldDisplayOnFirstPage);
                }


                public void redo()
                {
                    setDisplayOnFirstPage(displayOnFirstPage);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.DISPLAY_ON_FIRST_PAGE, Boolean.valueOf(oldDisplayOnFirstPage), Boolean.valueOf(displayOnFirstPage));
    }


    public boolean isDisplayOnLastPage()
    {
        return displayOnLastPage;
    }


    public void setDisplayOnLastPage(final boolean displayOnLastPage)
    {
        final boolean oldDisplayOnLastPage = this.displayOnLastPage;
        this.displayOnLastPage = displayOnLastPage;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.DISPLAY_ON_LAST_PAGE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setDisplayOnLastPage(oldDisplayOnLastPage);
                }


                public void redo()
                {
                    setDisplayOnLastPage(displayOnLastPage);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.DISPLAY_ON_LAST_PAGE, Boolean.valueOf(oldDisplayOnLastPage), Boolean.valueOf(displayOnLastPage));
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

        xmlWriter.writeProperty(PropertyKeys.DISPLAY_ON_FIRST_PAGE, String.valueOf(displayOnFirstPage));
        xmlWriter.writeProperty(PropertyKeys.DISPLAY_ON_LAST_PAGE, String.valueOf(displayOnLastPage));
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.DISPLAY_ON_FIRST_PAGE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            displayOnFirstPage = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.DISPLAY_ON_FIRST_PAGE, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.DISPLAY_ON_LAST_PAGE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            displayOnLastPage = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.DISPLAY_ON_LAST_PAGE, node));
        }
    }
}
