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
import org.pentaho.reportdesigner.crm.report.GraphicsContext;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.model.layoutmanager.NullReportLayoutManager;
import org.pentaho.reportdesigner.crm.report.model.layoutmanager.ReportLayoutManager;
import org.pentaho.reportdesigner.crm.report.model.layoutmanager.StackedReportLayoutManager;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 16:03:02
 */
public class BandReportElement extends ReportElement
{
    @NotNull
    private ReportLayoutManager.Type reportLayoutManagerType;
    @NotNull
    private ReportLayoutManager reportLayoutManager;

    private boolean showInLayoutGUI;


    public BandReportElement()
    {
        setName("Band" + System.identityHashCode(this));

        reportLayoutManagerType = ReportLayoutManager.Type.STACKED;
        reportLayoutManager = new StackedReportLayoutManager();

        showInLayoutGUI = true;
    }


    @NotNull
    public ReportLayoutManager.Type getReportLayoutManagerType()
    {
        return reportLayoutManagerType;
    }


    public void setReportLayoutManagerType(@NotNull final ReportLayoutManager.Type reportLayoutManagerType)
    {
        //noinspection ConstantConditions
        if (reportLayoutManagerType == null)
        {
            throw new IllegalArgumentException("reportLayoutManagerType must not be null");
        }

        final ReportLayoutManager.Type oldReportLayoutManagerType = this.reportLayoutManagerType;
        this.reportLayoutManagerType = reportLayoutManagerType;

        if (reportLayoutManagerType == ReportLayoutManager.Type.STACKED)
        {
            reportLayoutManager = new StackedReportLayoutManager();
        }
        else
        {
            reportLayoutManager = new NullReportLayoutManager();
        }

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.REPORT_LAYOUT_MANAGER_TYPE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setReportLayoutManagerType(oldReportLayoutManagerType);
                }


                public void redo()
                {
                    setReportLayoutManagerType(reportLayoutManagerType);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.REPORT_LAYOUT_MANAGER_TYPE, oldReportLayoutManagerType, reportLayoutManagerType);
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

        firePropertyChange(PropertyKeys.SHOW_IN_LAYOUT_GUI, oldShowInLayoutGUI, showInLayoutGUI);
    }


    @NotNull
    public ReportLayoutManager getReportLayoutManager()
    {
        return reportLayoutManager;
    }


    public void validate()
    {
        if (!invalid)
        {
            return;
        }

        //this validates all children (gives them a chance to calculate their layout)
        ArrayList<ReportElement> children = getChildren();
        for (ReportElement reportElement : children)
        {
            reportElement.validate();
        }

        //now we take this outcome and layout how we like the children again to reflect our preferences
        reportLayoutManager.layoutReportElement(this);//this adjusts only the minimumsize

        for (ReportElement reportElement : children)
        {
            reportElement.validate();
        }

        //convert position to absolute coordinate
        getOrigRectangle().setRect(getPosition().getX(), getPosition().getY(), getMinimumSize().getWidth(), getMinimumSize().getHeight());
        getRectangle().setRect(getPosition().getX(), getPosition().getY(), getMinimumSize().getWidth(), getMinimumSize().getHeight());
    }


    public void paint(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        if (!showInLayoutGUI)
        {
            return;
        }

        Shape origClip = g2d.getClip();
        Color origColor = g2d.getColor();
        Composite origComposite = g2d.getComposite();
        Stroke origStroke = g2d.getStroke();

        Rectangle2D.Double rect = getRectangle();

        g2d.setColor(new Color(220, 220, 220));
        g2d.draw(rect);

        g2d.clip(rect);
        paintBackroundAndBorder(g2d);

        g2d.setStroke(origStroke);
        g2d.setComposite(origComposite);
        g2d.setColor(origColor);
        g2d.setClip(origClip);

        //paint children
        ArrayList<ReportElement> children = getChildren();
        AffineTransform origTransform = g2d.getTransform();

        g2d.translate(getRectangle().x, getRectangle().y);
        for (ReportElement reportElement : children)
        {
            reportElement.paint(graphicsContext, g2d);
        }

        g2d.setTransform(origTransform);
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

        xmlWriter.writeProperty(PropertyKeys.REPORT_LAYOUT_MANAGER_TYPE, reportLayoutManagerType.toString());
        xmlWriter.writeProperty(PropertyKeys.SHOW_IN_LAYOUT_GUI, String.valueOf(showInLayoutGUI));
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.REPORT_LAYOUT_MANAGER_TYPE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            reportLayoutManagerType = ReportLayoutManager.Type.valueOf(XMLUtils.readProperty(PropertyKeys.REPORT_LAYOUT_MANAGER_TYPE, node));
            if (reportLayoutManagerType == ReportLayoutManager.Type.STACKED)
            {
                reportLayoutManager = new StackedReportLayoutManager();
            }
            else
            {
                reportLayoutManager = new NullReportLayoutManager();
            }
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.SHOW_IN_LAYOUT_GUI.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            showInLayoutGUI = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.SHOW_IN_LAYOUT_GUI, node));
        }
    }
}
