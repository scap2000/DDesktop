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
import org.gjt.xpp.XmlPullParserException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;

/**
 * User: Martin
 * Date: 18.11.2005
 * Time: 15:38:15
 */
public class PageDefinition implements XMLExternalizable
{
    @NotNull
    private PageOrientation pageOrientation;

    @NotNull
    private DoubleDimension pageSize;

    private double topBorder;
    private double bottomBorder;
    private double leftBorder;
    private double rightBorder;


    public PageDefinition(@NotNull PageFormatPreset pageFormatPreset,
                          @NotNull PageOrientation pageOrientation,
                          double topBorder, double bottomBorder, double leftBorder, double rightBorder)
    {
        this(pageFormatPreset, pageOrientation, topBorder, bottomBorder, leftBorder, rightBorder, 0, 0);
    }


    public PageDefinition(@NotNull PageFormatPreset pageFormatPreset,
                          @NotNull PageOrientation pageOrientation,
                          double topBorder, double bottomBorder, double leftBorder, double rightBorder,
                          double width, double height)
    {
        //noinspection ConstantConditions
        if (pageFormatPreset == null)
        {
            throw new IllegalArgumentException("pageFormatPreset must not be null");
        }
        //noinspection ConstantConditions
        if (pageOrientation == null)
        {
            throw new IllegalArgumentException("pageOrientation must not be null");
        }

        this.pageOrientation = pageOrientation;
        if (pageFormatPreset == PageFormatPreset.CUSTOM)
        {
            pageSize = new DoubleDimension(width - (leftBorder + rightBorder), height - (topBorder + bottomBorder));
        }
        else
        {
            if (pageOrientation == PageOrientation.PORTRAIT)
            {
                pageSize = new DoubleDimension(pageFormatPreset.getWidth() - (leftBorder + rightBorder), pageFormatPreset.getHeight() - (topBorder + bottomBorder));
            }
            else
            {
                pageSize = new DoubleDimension(pageFormatPreset.getHeight() - (leftBorder + rightBorder), pageFormatPreset.getWidth() - (topBorder + bottomBorder));
            }
        }

        this.topBorder = topBorder;
        this.bottomBorder = bottomBorder;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }


    @NotNull
    public String getNiceName()
    {
        PageFormatPreset preset = PageFormatPreset.getPreset(this);
        return TranslationManager.getInstance().getTranslation("R", "PageDefinition.PageFormat", preset.toString(), Integer.valueOf(pageOrientation.ordinal()));
    }


    @NotNull
    public DoubleDimension getInnerPageSize()
    {
        return pageSize;
    }


    @NotNull
    public DoubleDimension getOuterPageSize()
    {
        return new DoubleDimension(pageSize.getWidth() + leftBorder + rightBorder, pageSize.getHeight() + topBorder + bottomBorder);
    }


    @NotNull
    public PageOrientation getPageOrientation()
    {
        return pageOrientation;
    }


    public double getTopBorder()
    {
        return topBorder;
    }


    public double getBottomBorder()
    {
        return bottomBorder;
    }


    public double getLeftBorder()
    {
        return leftBorder;
    }


    public double getRightBorder()
    {
        return rightBorder;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PageDefinition that = (PageDefinition) o;

        if (Double.compare(that.bottomBorder, bottomBorder) != 0) return false;
        if (Double.compare(that.leftBorder, leftBorder) != 0) return false;
        if (Double.compare(that.rightBorder, rightBorder) != 0) return false;
        if (Double.compare(that.topBorder, topBorder) != 0) return false;
        if (pageOrientation != that.pageOrientation) return false;
        return pageSize.equals(that.pageSize);

    }


    public int hashCode()
    {
        int result;
        long temp;
        result = pageOrientation.hashCode();
        result = 29 * result + pageSize.hashCode();
        temp = topBorder != +0.0d ? Double.doubleToLongBits(topBorder) : 0L;
        result = 29 * result + (int) (temp ^ (temp >>> 32));
        temp = bottomBorder != +0.0d ? Double.doubleToLongBits(bottomBorder) : 0L;
        result = 29 * result + (int) (temp ^ (temp >>> 32));
        temp = leftBorder != +0.0d ? Double.doubleToLongBits(leftBorder) : 0L;
        result = 29 * result + (int) (temp ^ (temp >>> 32));
        temp = rightBorder != +0.0d ? Double.doubleToLongBits(rightBorder) : 0L;
        result = 29 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @NotNull
    public String toString()
    {
        return "PageDefinition{" +
               "pageSize=" + pageSize +
               ", topBorder=" + topBorder +
               ", bottomBorder=" + bottomBorder +
               ", leftBorder=" + leftBorder +
               ", rightBorder=" + rightBorder +
               "}";
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute(PropertyKeys.PAGE_SIZE, ObjectConverterFactory.getInstance().getDoubleDimensionConverter().getString(new DoubleDimension(pageSize.getWidth() + leftBorder + rightBorder, pageSize.getHeight() + topBorder + bottomBorder)));
        xmlWriter.writeAttribute(PropertyKeys.TOP_BORDER, String.valueOf(topBorder));
        xmlWriter.writeAttribute(PropertyKeys.BOTTOM_BORDER, String.valueOf(bottomBorder));
        xmlWriter.writeAttribute(PropertyKeys.LEFT_BORDER, String.valueOf(leftBorder));
        xmlWriter.writeAttribute(PropertyKeys.RIGHT_BORDER, String.valueOf(rightBorder));
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException, XmlPullParserException
    {
        DoubleDimension outerPageSize = ObjectConverterFactory.getInstance().getDoubleDimensionConverter().getObject(node.getAttributeValueFromRawName(PropertyKeys.PAGE_SIZE));
        topBorder = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.TOP_BORDER));
        bottomBorder = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.BOTTOM_BORDER));
        leftBorder = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.LEFT_BORDER));
        rightBorder = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.RIGHT_BORDER));
        pageSize = new DoubleDimension(outerPageSize.getWidth() - (leftBorder + rightBorder), outerPageSize.getHeight() - (topBorder + bottomBorder));
    }
}
