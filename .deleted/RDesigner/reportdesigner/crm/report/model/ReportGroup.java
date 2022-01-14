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
import org.jetbrains.annotations.NonNls;
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
 * Date: 25.10.2005
 * Time: 10:09:46
 */
public class ReportGroup extends ReportElement
{
    @NotNull
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    @NotNull
    private BandToplevelGroupReportElement groupHeader;

    @NotNull
    private BandToplevelGroupReportElement groupFooter;
    @NotNull
    private String[] groupFields;


    public ReportGroup()
    {
        groupHeader = new BandToplevelGroupReportElement();
        groupHeader.setBandToplevelType(BandToplevelType.GROUP_HEADER);
        groupHeader.setShowInLayoutGUI(false);

        groupFooter = new BandToplevelGroupReportElement();
        groupFooter.setBandToplevelType(BandToplevelType.GROUP_FOOTER);
        groupFooter.setShowInLayoutGUI(false);

        super.addChild(groupHeader);
        super.addChild(groupFooter);

        groupFields = EMPTY_STRING_ARRAY;
    }


    public void addChild(@NotNull ReportElement child)
    {
        if (child instanceof BandToplevelGroupReportElement)
        {
            BandToplevelGroupReportElement bandToplevelGroupReportElement = (BandToplevelGroupReportElement) child;
            if (bandToplevelGroupReportElement.getBandToplevelType() == BandToplevelType.GROUP_HEADER)
            {
                ReportElement childToRemove = getChildren().get(0);
                super.removeChild(childToRemove);
                super.insertChild(bandToplevelGroupReportElement, 0);
                groupHeader = bandToplevelGroupReportElement;
            }
            else if (bandToplevelGroupReportElement.getBandToplevelType() == BandToplevelType.GROUP_FOOTER)
            {
                ReportElement childToRemove = getChildren().get(getChildren().size() - 1);
                super.removeChild(childToRemove);
                super.addChild(bandToplevelGroupReportElement);
                groupFooter = bandToplevelGroupReportElement;
            }
            else
            {
                throw new UnsupportedOperationException("can not add additional elements to group");
            }
        }
        else if (child instanceof ReportGroup && getChildren().size() == 2)
        {
            super.insertChild(child, 1);
        }
        else
        {
            throw new UnsupportedOperationException("can not add additional elements to group");
        }
    }


    public void removeChild(@NotNull ReportElement reportElement)
    {
        if (reportElement instanceof ReportGroup && getChildren().size() == 3)
        {
            super.removeChild(reportElement);
        }
        else
        {
            throw new UnsupportedOperationException("can not remove elements from group");
        }
    }


    @NotNull
    public BandToplevelGroupReportElement getGroupHeader()
    {
        return groupHeader;
    }


    @NotNull
    public BandToplevelGroupReportElement getGroupFooter()
    {
        return groupFooter;
    }


    @NotNull
    public String[] getGroupFields()
    {
        return groupFields;
    }


    public void setGroupFields(@NotNull @NonNls final String[] groupFields)
    {
        //noinspection ConstantConditions
        if (groupFields == null)
        {
            throw new IllegalArgumentException("groupFields must not be null");
        }

        final String[] oldGroupFields = this.groupFields;
        this.groupFields = groupFields;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.GROUP_FIELDS);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setGroupFields(oldGroupFields);
                }


                public void redo()
                {
                    setGroupFields(groupFields);
                }
            });
            undo.endTransaction();
        }


        firePropertyChange(PropertyKeys.GROUP_FIELDS, oldGroupFields, groupFields);
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

        if (groupFields.length > 0)
        {
            xmlWriter.startElement(XMLConstants.PROPERTY);
            xmlWriter.writeAttribute(XMLConstants.NAME, PropertyKeys.GROUP_FIELDS);
            xmlWriter.writeAttribute(XMLConstants.ARRAY, "true");
            for (int i = 0; i < groupFields.length; i++)
            {
                String groupField = groupFields[i];
                if (groupField != null)
                {
                    xmlWriter.writeProperty(String.valueOf(i), groupField);
                }
            }
            xmlWriter.closeElement(XMLConstants.PROPERTY);
        }
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.GROUP_FIELDS.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            if (node.getAttributeValueFromRawName(XMLConstants.ARRAY) != null)
            {
                ArrayList<String> al = new ArrayList<String>();
                int n = 0;
                while (!node.isFinished())
                {
                    Object childNodeList = node.readNextChild();
                    if (childNodeList instanceof XmlPullNode)
                    {
                        XmlPullNode child = (XmlPullNode) childNodeList;
                        if (XMLConstants.PROPERTY.equals(child.getRawName()))
                        {
                            al.add(XMLUtils.readProperty(String.valueOf(n), child));
                            n++;
                        }
                    }
                }
                groupFields = al.toArray(new String[al.size()]);
            }
        }
    }


}
