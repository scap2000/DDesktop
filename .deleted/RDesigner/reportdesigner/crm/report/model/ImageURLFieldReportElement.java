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
public class ImageURLFieldReportElement extends TextReportElement
{
    @NotNull
    private String fieldName;
    private boolean keepAspect;
    @NotNull
    private Formula formula;


    public ImageURLFieldReportElement()
    {
        keepAspect = true;
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


    public boolean isKeepAspect()
    {
        return keepAspect;
    }


    public void setKeepAspect(final boolean keepAspect)
    {
        final boolean oldKeepAspect = this.keepAspect;
        this.keepAspect = keepAspect;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.KEEP_ASPECT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setKeepAspect(oldKeepAspect);
                }


                public void redo()
                {
                    setKeepAspect(keepAspect);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.KEEP_ASPECT, Boolean.valueOf(oldKeepAspect), Boolean.valueOf(keepAspect));
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


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.FIELD_NAME, fieldName);
        xmlWriter.writeProperty(PropertyKeys.KEEP_ASPECT, String.valueOf(keepAspect));

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
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.KEEP_ASPECT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            keepAspect = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.KEEP_ASPECT, node));
        }
        else if (PropertyKeys.FORMULA.equals(node.getRawName()))
        {
            formula = new Formula("");
            formula.readObject(node, xmlContext);
        }
    }

}