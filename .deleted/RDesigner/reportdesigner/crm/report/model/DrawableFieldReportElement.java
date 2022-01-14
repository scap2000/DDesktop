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
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 15:40:13
 */
public class DrawableFieldReportElement extends TextReportElement
{
    @NotNull
    private String fieldName;
    @NotNull
    private Formula formula;


    public DrawableFieldReportElement()
    {
        fieldName = "field" + FieldNameFactory.getInstance().getNextFreeNumber("field");//NON-NLS
        formula = new Formula("");
    }


    @NotNull
    public String getFieldName()
    {
        return fieldName;
    }


    public void setFieldName(@NotNull final String fieldName)
    {
        //noinspection ConstantConditions
        if (fieldName == null)
        {
            throw new IllegalArgumentException("fieldName must not be null");
        }

        final String oldFieldName = this.fieldName;
        this.fieldName = fieldName;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.FIELD_NAME);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setFieldName(oldFieldName);
                }


                public void redo()
                {
                    setFieldName(fieldName);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.FIELD_NAME, oldFieldName, fieldName);
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
        return fieldName;
    }


    public void paint(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        Shape origClip = g2d.getClip();
        Color origColor = g2d.getColor();
        Composite origComposite = g2d.getComposite();
        Stroke origStroke = g2d.getStroke();

        Rectangle2D.Double rect = getRectangle();

        g2d.clip(rect);
        paintBackroundAndBorder(g2d);

        g2d.setColor(new Color(240, 240, 240));
        g2d.fill(rect);

        g2d.setColor(new Color(230, 255, 230));
        g2d.fillOval((int) (rect.getX() + 0.1 * rect.getWidth()), (int) (rect.getY() + 0.1 * rect.getHeight()), (int) (0.5 * rect.getWidth()), (int) (0.7 * rect.getHeight()));

        g2d.setColor(new Color(230, 230, 255));
        g2d.fillPolygon(new int[]{(int) (rect.getX() + 0.4 * rect.getWidth()), (int) (rect.getX() + 0.675 * rect.getWidth()), (int) (rect.getX() + 0.95 * rect.getWidth())},
                        new int[]{(int) (rect.getY() + 0.7 * rect.getHeight()), (int) (rect.getY() + 0.1 * rect.getHeight()), (int) (rect.getY() + 0.7 * rect.getHeight())},
                        3);

        g2d.setColor(new Color(255, 230, 230));
        g2d.fillRect((int) (rect.getX() + 0.15 * rect.getWidth()), (int) (rect.getY() + 0.6 * rect.getHeight()), (int) (0.7 * rect.getWidth()), (int) (0.3 * rect.getHeight()));


        g2d.setStroke(origStroke);
        g2d.setComposite(origComposite);
        g2d.setColor(origColor);
        g2d.setClip(origClip);

        super.paint(graphicsContext, g2d);
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.FIELD_NAME, fieldName);
        xmlWriter.startElement(PropertyKeys.FORMULA);
        formula.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.FORMULA);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.FIELD_NAME.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            fieldName = XMLUtils.readProperty(PropertyKeys.FIELD_NAME, node);
        }
        else if (PropertyKeys.FORMULA.equals(node.getRawName()))
        {
            formula = new Formula("");
            formula.readObject(node, xmlContext);
        }
    }
}
