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
import org.pentaho.reportdesigner.crm.report.lineal.LinealModel;
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
public class BandToplevelReportElement extends ReportElement
{
    private boolean pageBreakBefore;
    private boolean pageBreakAfter;

    private boolean showInLayoutGUI;

    private double visualHeight;

    @Nullable
    private LinealModel verticalLinealModel;

    @NotNull
    private BandToplevelType bandToplevelType;


    public BandToplevelReportElement()
    {
        setName("Band" + System.identityHashCode(this));

        showInLayoutGUI = true;

        visualHeight = 100;
        bandToplevelType = BandToplevelType.ITEM_BAND;
    }


    @NotNull
    public BandToplevelType getBandToplevelType()
    {
        return bandToplevelType;
    }


    public void setBandToplevelType(@NotNull BandToplevelType bandToplevelType)
    {
        this.bandToplevelType = bandToplevelType;
    }


    @Nullable
    public LinealModel getVerticalLinealModel()
    {
        LinealModel linealModel = verticalLinealModel;
        if (linealModel == null)
        {
            Report report = getReport();
            if (report != null)
            {
                verticalLinealModel = new LinealModel(report);
            }
        }
        else if (linealModel.getReport() == null)
        {
            Report report = getReport();
            linealModel.setReport(report);
        }
        return verticalLinealModel;
    }


    public double getVisualHeight()
    {
        return visualHeight;
    }


    public void setVisualHeight(final double visualHeight)
    {
        final double oldVisualHeight = this.visualHeight;
        this.visualHeight = visualHeight;

        firePropertyChange(PropertyKeys.VISUAL_HEIGHT, Double.valueOf(oldVisualHeight), Double.valueOf(visualHeight));
    }


    public boolean isPageBreakBefore()
    {
        return pageBreakBefore;
    }


    public void setPageBreakBefore(final boolean pageBreakBefore)
    {
        final boolean oldPageBreakBefore = this.pageBreakBefore;
        this.pageBreakBefore = pageBreakBefore;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.PAGE_BREAK_BEFORE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setPageBreakBefore(oldPageBreakBefore);
                }


                public void redo()
                {
                    setPageBreakBefore(pageBreakBefore);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.PAGE_BREAK_BEFORE, Boolean.valueOf(oldPageBreakBefore), Boolean.valueOf(pageBreakBefore));
    }


    public boolean isPageBreakAfter()
    {
        return pageBreakAfter;
    }


    public void setPageBreakAfter(final boolean pageBreakAfter)
    {
        final boolean oldPageBreakAfter = this.pageBreakAfter;
        this.pageBreakAfter = pageBreakAfter;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.PAGE_BREAK_AFTER);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setPageBreakAfter(oldPageBreakAfter);
                }


                public void redo()
                {
                    setPageBreakAfter(pageBreakAfter);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.PAGE_BREAK_AFTER, Boolean.valueOf(oldPageBreakAfter), Boolean.valueOf(pageBreakAfter));
    }


    public boolean isShowInLayoutGUI()
    {
        return showInLayoutGUI;
    }


    public void setShowInLayoutGUI(final boolean showInLayoutGUI)
    {
        final boolean oldShowInLayoutGUI = this.showInLayoutGUI;
        this.showInLayoutGUI = showInLayoutGUI;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.SHOW_IN_LAYOUT_GUI);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setShowInLayoutGUI(oldShowInLayoutGUI);
                }


                public void redo()
                {
                    setShowInLayoutGUI(showInLayoutGUI);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.SHOW_IN_LAYOUT_GUI, Boolean.valueOf(oldShowInLayoutGUI), Boolean.valueOf(showInLayoutGUI));
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

        xmlWriter.writeProperty(PropertyKeys.PAGE_BREAK_BEFORE, String.valueOf(pageBreakBefore));
        xmlWriter.writeProperty(PropertyKeys.PAGE_BREAK_AFTER, String.valueOf(pageBreakAfter));
        xmlWriter.writeProperty(PropertyKeys.SHOW_IN_LAYOUT_GUI, String.valueOf(showInLayoutGUI));
        xmlWriter.writeProperty(PropertyKeys.VISUAL_HEIGHT, String.valueOf(visualHeight));

        LinealModel linealModel = verticalLinealModel;
        if (linealModel != null)
        {
            xmlWriter.startElement(PropertyKeys.VERTICAL_LINEAL_MODEL);
            linealModel.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.VERTICAL_LINEAL_MODEL);
        }
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.PAGE_BREAK_BEFORE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            pageBreakBefore = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.PAGE_BREAK_BEFORE, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.PAGE_BREAK_AFTER.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            pageBreakAfter = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.PAGE_BREAK_AFTER, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.SHOW_IN_LAYOUT_GUI.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            showInLayoutGUI = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.SHOW_IN_LAYOUT_GUI, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.VISUAL_HEIGHT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            visualHeight = Double.parseDouble(XMLUtils.readProperty(PropertyKeys.VISUAL_HEIGHT, node));
        }
        else if (PropertyKeys.VERTICAL_LINEAL_MODEL.equals(node.getRawName()))
        {
            verticalLinealModel = new LinealModel(getReport());
            verticalLinealModel.readObject(node, xmlContext);
        }
    }
}
