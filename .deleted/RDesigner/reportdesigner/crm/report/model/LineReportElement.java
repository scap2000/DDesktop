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
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;
import org.pentaho.reportdesigner.lib.client.util.MathUtils;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 15:40:13
 */
public class LineReportElement extends ReportElement
{
    @NotNull
    private LineDirection direction;
    @NotNull
    private LineDefinition lineDefinition;


    public LineReportElement()
    {
        lineDefinition = new LineDefinition();
        direction = LineDirection.HORIZONTAL;

        setElementRepaintBorder(0);
    }


    @NotNull
    public LineDefinition getLineDefinition()
    {
        return lineDefinition;
    }


    public void setLineDefinition(@NotNull final LineDefinition lineDefinition)
    {
        //noinspection ConstantConditions
        if (lineDefinition == null)
        {
            throw new IllegalArgumentException("lineDefinition must not be null");
        }

        final LineDefinition oldLineDefinition = this.lineDefinition;
        this.lineDefinition = lineDefinition;
        setElementRepaintBorder(lineDefinition.getWidth() * MathUtils.SQRT_OF_2 / 2 + 1);

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.LINE_DEFINITION);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setLineDefinition(oldLineDefinition);
                }


                public void redo()
                {
                    setLineDefinition(lineDefinition);
                }
            });
            undo.endTransaction();
        }
        firePropertyChange(PropertyKeys.LINE_DEFINITION, oldLineDefinition, lineDefinition);
    }


    @NotNull
    public LineDirection getDirection()
    {
        return direction;
    }


    public void setDirection(@NotNull final LineDirection direction)
    {
        //noinspection ConstantConditions
        if (direction == null)
        {
            throw new IllegalArgumentException("direction must not be null");
        }

        final LineDirection oldDirection = this.direction;
        this.direction = direction;

        setMinimumSize(new DoubleDimension(getMinimumSize().getHeight(), getMinimumSize().getWidth()));

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.DIRECTION);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setDirection(oldDirection);
                }


                public void redo()
                {
                    setDirection(direction);
                }
            });
            undo.endTransaction();
        }
        firePropertyChange(PropertyKeys.DIRECTION, oldDirection, direction);
    }


    public void paint(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        Shape origClip = g2d.getClip();
        Color origColor = g2d.getColor();
        Composite origComposite = g2d.getComposite();
        Stroke origStroke = g2d.getStroke();

        Rectangle2D.Double rect = getRectangle();
        
        paintBackroundAndBorder(g2d);

        g2d.setColor(lineDefinition.getColor());
        //if (color != null)
        //{
        //    g2d.setColor(color);
        //}
        //else
        //{
        //    g2d.setColor(Color.BLACK);
        //}

        if (direction == LineDirection.HORIZONTAL)
        {
            g2d.setStroke(lineDefinition.getBasicStroke());
            Line2D.Double s = new Line2D.Double(rect.getX(), (rect.getY() + rect.getHeight() / 2), rect.getX() + rect.getWidth(), (rect.getY() + rect.getHeight() / 2));
            g2d.draw(s);
        }
        else
        {
            g2d.setStroke(lineDefinition.getBasicStroke());
            Line2D.Double s = new Line2D.Double((rect.getX() + rect.getWidth() / 2), rect.getY(), (rect.getX() + rect.getWidth() / 2), rect.getY() + rect.getHeight());
            g2d.draw(s);
        }

        g2d.setStroke(origStroke);
        g2d.setComposite(origComposite);
        g2d.setColor(origColor);
        g2d.setClip(origClip);
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        //xmlWriter.writeProperty(PropertyKeys.COLOR, ObjectConverterFactory.getInstance().getColorConverter().getString(color));
        //xmlWriter.writeProperty(PropertyKeys.LINE_WIDTH, String.valueOf(lineWidth));
        xmlWriter.writeProperty(PropertyKeys.DIRECTION, direction.toString());

        xmlWriter.startElement(PropertyKeys.LINE_DEFINITION);
        lineDefinition.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.LINE_DEFINITION);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.COLOR.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            //must be old->there will be no separate borderDefinition in the xml
            Color color = ObjectConverterFactory.getInstance().getColorConverter().getObject(XMLUtils.readProperty(PropertyKeys.COLOR, node));
            lineDefinition = new LineDefinition(color, lineDefinition.getWidth(), lineDefinition.getJoin(), lineDefinition.getCap(), lineDefinition.getMiterlimit(), lineDefinition.getDash(), lineDefinition.getDashPhase());
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.LINE_WIDTH.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            //must be old->there will be no separate borderDefinition in the xml
            double lineWidth = Double.parseDouble(XMLUtils.readProperty(PropertyKeys.LINE_WIDTH, node));
            lineDefinition = new LineDefinition(lineDefinition.getColor(), lineWidth, lineDefinition.getJoin(), lineDefinition.getCap(), lineDefinition.getMiterlimit(), lineDefinition.getDash(), lineDefinition.getDashPhase());
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.DIRECTION.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            direction = LineDirection.valueOf(XMLUtils.readProperty(PropertyKeys.DIRECTION, node));
        }
        else if (PropertyKeys.LINE_DEFINITION.equals(node.getRawName()))
        {
            lineDefinition = new LineDefinition();
            lineDefinition.readObject(node, xmlContext);
        }
    }

}
