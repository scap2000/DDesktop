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
import org.pentaho.reportdesigner.lib.client.util.MathUtils;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * User: Martin
 * Date: 08.02.2006
 * Time: 11:07:45
 */
public class EllipseReportElement extends ReportElement
{
    private boolean drawBorder;

    @NotNull
    private BorderDefinition borderDefinition;

    @Nullable
    private Color color;
    private boolean fill;


    public EllipseReportElement()
    {
        borderDefinition = new BorderDefinition();
        drawBorder = false;
        fill = true;
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    @Nullable
    public Color getColor()
    {
        return color;
    }


    public void setColor(@Nullable final Color color)
    {
        final Color oldColor = this.color;
        this.color = color;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.COLOR);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setColor(oldColor);
                }


                public void redo()
                {
                    setColor(color);
                }
            });
            undo.endTransaction();
        }
        firePropertyChange(PropertyKeys.COLOR, oldColor, color);
    }


    @NotNull
    public BorderDefinition getBorderDefinition()
    {
        return borderDefinition;
    }


    public void setBorderDefinition(@NotNull final BorderDefinition borderDefinition)
    {
        //noinspection ConstantConditions
        if (borderDefinition == null)
        {
            throw new IllegalArgumentException("borderDefinition must not be null");
        }

        final BorderDefinition oldBorderDefinition = this.borderDefinition;
        this.borderDefinition = borderDefinition;
        setElementRepaintBorder(borderDefinition.getWidth() * MathUtils.SQRT_OF_2 / 2 + 1);

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.BORDER_DEFINITION);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setBorderDefinition(oldBorderDefinition);
                }


                public void redo()
                {
                    setBorderDefinition(borderDefinition);
                }
            });
            undo.endTransaction();
        }
        firePropertyChange(PropertyKeys.BORDER_DEFINITION, oldBorderDefinition, borderDefinition);
    }


    public boolean isFill()
    {
        return fill;
    }


    public void setFill(final boolean fill)
    {
        final boolean oldFill = this.fill;
        this.fill = fill;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.FILL);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setFill(oldFill);
                }


                public void redo()
                {
                    setFill(fill);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.FILL, Boolean.valueOf(oldFill), Boolean.valueOf(fill));
    }


    public boolean isDrawBorder()
    {
        return drawBorder;
    }


    public void setDrawBorder(final boolean drawBorder)
    {
        final boolean oldDrawBorder = this.drawBorder;
        this.drawBorder = drawBorder;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.DRAW_BORDER);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setDrawBorder(oldDrawBorder);
                }


                public void redo()
                {
                    setDrawBorder(drawBorder);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.DRAW_BORDER, Boolean.valueOf(oldDrawBorder), Boolean.valueOf(drawBorder));
    }


    public void paint(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        Shape origClip = g2d.getClip();
        Color origColor = g2d.getColor();
        Stroke origStroke = g2d.getStroke();
        Composite origComposite = g2d.getComposite();

        Rectangle2D.Double rect = getRectangle();

        paintBackroundAndBorder(g2d);

        Ellipse2D.Double ellipse;
        ellipse = new Ellipse2D.Double(rect.x, rect.y, rect.width, rect.height);

        Color bgColor = color;
        if (bgColor != null)
        {
            if (fill)
            {
                g2d.setColor(bgColor);
                g2d.fill(ellipse);
            }
            if (drawBorder)
            {
                g2d.setColor(borderDefinition.getColor());
                g2d.setStroke(borderDefinition.getBasicStroke());
                g2d.draw(ellipse);
            }
        }

        g2d.setComposite(origComposite);
        g2d.setStroke(origStroke);
        g2d.setColor(origColor);
        g2d.setClip(origClip);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        Color color = this.color;
        if (color != null)
        {
            xmlWriter.writeProperty(PropertyKeys.COLOR, ObjectConverterFactory.getInstance().getColorConverter().getString(color));
        }

        xmlWriter.writeProperty(PropertyKeys.DRAW_BORDER, String.valueOf(drawBorder));
        xmlWriter.writeProperty(PropertyKeys.FILL, String.valueOf(fill));

        xmlWriter.startElement(PropertyKeys.BORDER_DEFINITION);
        borderDefinition.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.BORDER_DEFINITION);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.COLOR.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            color = ObjectConverterFactory.getInstance().getColorConverter().getObject(XMLUtils.readProperty(PropertyKeys.COLOR, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.DRAW_BORDER.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            drawBorder = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.DRAW_BORDER, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.FILL.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            fill = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.FILL, node));
        }
        else if (PropertyKeys.BORDER_DEFINITION.equals(node.getRawName()))
        {
            borderDefinition = new BorderDefinition();
            borderDefinition.readObject(node, xmlContext);
        }
    }

}
