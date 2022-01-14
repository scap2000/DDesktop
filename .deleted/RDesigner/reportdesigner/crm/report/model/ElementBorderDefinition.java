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
import org.pentaho.reportdesigner.crm.report.util.GraphicUtils;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.awt.*;
import java.io.IOException;

/**
 * User: Martin
 * Date: 25.05.2006
 * Time: 11:59:08
 */
public class ElementBorderDefinition implements XMLExternalizable
{

    public enum BorderType
    {
        @NotNull NONE,
        @NotNull HIDDEN,
        @NotNull DOTTED,
        @NotNull DASHED,
        @NotNull SOLID,
        @NotNull DOUBLE,
        @NotNull DOT_DASH,
        @NotNull DOT_DOT_DASH,
        @NotNull WAVE,
        @NotNull GROOVE,
        @NotNull RIDGE,
        @NotNull INSET,
        @NotNull OUTSET
    }

    public static class Side implements XMLExternalizable
    {
        private double width;
        @NotNull
        private BorderType type;
        @NotNull
        private Color color;


        public Side(double width, @NotNull BorderType type, @NotNull Color color)
        {
            this.width = width;
            this.type = type;
            this.color = color;
        }


        public double getWidth()
        {
            return width;
        }


        public double getPaintWidth()
        {
            return type == BorderType.NONE ? 0 : width;
        }


        @NotNull
        public BorderType getType()
        {
            return type;
        }


        @NotNull
        public Color getColor()
        {
            return color;
        }


        @NotNull
        public Stroke getStroke()
        {
            switch (type)
            {
                case NONE:
                    return new BasicStroke(0);
                case DASHED:
                    return new BasicStroke((float) getWidth(), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, GraphicUtils.getArrayCopy(BorderDefinition.getDashed(getWidth())), 0);
                case DOT_DASH:
                    return new BasicStroke((float) getWidth(), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, GraphicUtils.getArrayCopy(BorderDefinition.getDotDash(getWidth())), 0);
                case DOT_DOT_DASH:
                    return new BasicStroke((float) getWidth(), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, GraphicUtils.getArrayCopy(BorderDefinition.getDotDotDash(getWidth())), 0);
                case DOTTED:
                    return new BasicStroke((float) getWidth(), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, GraphicUtils.getArrayCopy(BorderDefinition.getDotted(getWidth())), 0);
                case DOUBLE:
                    return new BasicStroke((float) getWidth());
                case GROOVE:
                    return new BasicStroke((float) getWidth());
                case HIDDEN:
                    return new BasicStroke(0);
                case INSET:
                    return new BasicStroke((float) getWidth());
                case OUTSET:
                    return new BasicStroke((float) getWidth());
                case RIDGE:
                    return new BasicStroke((float) getWidth());
                case SOLID:
                    return new BasicStroke((float) getWidth());
                case WAVE:
                    return new BasicStroke((float) getWidth());
            }

            return new BasicStroke((float) getWidth());
        }


        public boolean equals(@Nullable Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Side side = (Side) o;

            if (Double.compare(side.width, width) != 0) return false;
            if (!color.equals(side.color)) return false;
            return type == side.type;
        }


        public int hashCode()
        {
            int result;
            long temp;
            temp = width != +0.0d ? Double.doubleToLongBits(width) : 0L;
            result = (int) (temp ^ (temp >>> 32));
            result = 31 * result + type.hashCode();
            result = 31 * result + color.hashCode();
            return result;
        }


        public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
        {
            xmlWriter.writeAttribute(PropertyKeys.WIDTH, String.valueOf(width));
            xmlWriter.writeAttribute(PropertyKeys.BORDER_TYPE, type.name());
            xmlWriter.writeAttribute(PropertyKeys.COLOR, ObjectConverterFactory.getInstance().getColorConverter().getString(color));
        }


        public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException
        {
            width = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.WIDTH));
            type = BorderType.valueOf(node.getAttributeValueFromRawName(PropertyKeys.BORDER_TYPE));
            color = ObjectConverterFactory.getInstance().getColorConverter().getObject(node.getAttributeValueFromRawName(PropertyKeys.COLOR));
        }
    }

    public static class Edge implements XMLExternalizable
    {
        @NotNull
        private DoubleDimension radii;


        public Edge(@NotNull DoubleDimension radii)
        {
            this.radii = radii;
        }


        @NotNull
        public DoubleDimension getRadii()
        {
            return radii;
        }


        public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
        {
            xmlWriter.writeAttribute(PropertyKeys.RADII, ObjectConverterFactory.getInstance().getDoubleDimensionConverter().getString(radii));
        }


        public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException
        {
            radii = ObjectConverterFactory.getInstance().getDoubleDimensionConverter().getObject(node.getAttributeValueFromRawName(PropertyKeys.RADII));
        }


        public boolean equals(@Nullable Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            return radii.equals(edge.radii);
        }


        public int hashCode()
        {
            return radii.hashCode();
        }
    }

    private boolean sameBorderForAllSides;

    @NotNull
    private Side topSide;
    @NotNull
    private Side bottomSide;
    @NotNull
    private Side leftSide;
    @NotNull
    private Side rightSide;

    @NotNull
    private Side breakSide;

    @NotNull
    private Edge topLeftEdge;
    @NotNull
    private Edge topRightEdge;
    @NotNull
    private Edge bottomLeftEdge;
    @NotNull
    private Edge bottomRightEdge;


    public ElementBorderDefinition()
    {
        sameBorderForAllSides = true;

        topSide = new Side(1, BorderType.NONE, Color.BLACK);
        bottomSide = new Side(1, BorderType.NONE, Color.BLACK);
        leftSide = new Side(1, BorderType.NONE, Color.BLACK);
        rightSide = new Side(1, BorderType.NONE, Color.BLACK);

        breakSide = new Side(0, BorderType.NONE, Color.BLACK);

        topLeftEdge = new Edge(new DoubleDimension());
        topRightEdge = new Edge(new DoubleDimension());
        bottomLeftEdge = new Edge(new DoubleDimension());
        bottomRightEdge = new Edge(new DoubleDimension());
    }


    public ElementBorderDefinition(boolean sameBorderForAllSides, @NotNull Side topSide, @NotNull Side bottomSide, @NotNull Side leftSide, @NotNull Side rightSide, @NotNull Side breakSide, @NotNull Edge topLeftEdge, @NotNull Edge topRightEdge, @NotNull Edge bottomLeftEdge, @NotNull Edge bottomRightEdge)
    {
        this.sameBorderForAllSides = sameBorderForAllSides;

        this.topSide = topSide;
        this.bottomSide = bottomSide;
        this.leftSide = leftSide;
        this.rightSide = rightSide;

        this.breakSide = breakSide;

        this.topLeftEdge = topLeftEdge;
        this.topRightEdge = topRightEdge;
        this.bottomLeftEdge = bottomLeftEdge;
        this.bottomRightEdge = bottomRightEdge;
    }


    public boolean isSameBorderForAllSides()
    {
        return sameBorderForAllSides;
    }


    @NotNull
    public Side getTopSide()
    {
        return topSide;
    }


    @NotNull
    public Side getBottomSide()
    {
        return bottomSide;
    }


    @NotNull
    public Side getLeftSide()
    {
        return leftSide;
    }


    @NotNull
    public Side getRightSide()
    {
        return rightSide;
    }


    @NotNull
    public Side getBreakSide()
    {
        return breakSide;
    }


    @NotNull
    public Edge getTopLeftEdge()
    {
        return topLeftEdge;
    }


    @NotNull
    public Edge getTopRightEdge()
    {
        return topRightEdge;
    }


    @NotNull
    public Edge getBottomLeftEdge()
    {
        return bottomLeftEdge;
    }


    @NotNull
    public Edge getBottomRightEdge()
    {
        return bottomRightEdge;
    }


    public boolean isDefault()
    {
        return equals(new ElementBorderDefinition());
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementBorderDefinition that = (ElementBorderDefinition) o;

        if (sameBorderForAllSides != that.sameBorderForAllSides) return false;
        if (!bottomLeftEdge.equals(that.bottomLeftEdge)) return false;
        if (!bottomRightEdge.equals(that.bottomRightEdge)) return false;
        if (!bottomSide.equals(that.bottomSide)) return false;
        if (!breakSide.equals(that.breakSide)) return false;
        if (!leftSide.equals(that.leftSide)) return false;
        if (!rightSide.equals(that.rightSide)) return false;
        if (!topLeftEdge.equals(that.topLeftEdge)) return false;
        if (!topRightEdge.equals(that.topRightEdge)) return false;
        return topSide.equals(that.topSide);
    }


    public int hashCode()
    {
        int result;
        result = (sameBorderForAllSides ? 1 : 0);
        result = 31 * result + topSide.hashCode();
        result = 31 * result + bottomSide.hashCode();
        result = 31 * result + leftSide.hashCode();
        result = 31 * result + rightSide.hashCode();
        result = 31 * result + breakSide.hashCode();
        result = 31 * result + topLeftEdge.hashCode();
        result = 31 * result + topRightEdge.hashCode();
        result = 31 * result + bottomLeftEdge.hashCode();
        result = 31 * result + bottomRightEdge.hashCode();
        return result;
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute(PropertyKeys.SAME_BORDER_FOR_ALL_SIDES, String.valueOf(sameBorderForAllSides));

        xmlWriter.startElement(PropertyKeys.BORDER_TOP);
        topSide.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.BORDER_TOP);

        xmlWriter.startElement(PropertyKeys.BORDER_BOTTOM);
        bottomSide.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.BORDER_BOTTOM);

        xmlWriter.startElement(PropertyKeys.BORDER_LEFT);
        leftSide.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.BORDER_LEFT);

        xmlWriter.startElement(PropertyKeys.BORDER_RIGHT);
        rightSide.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.BORDER_RIGHT);

        xmlWriter.startElement(PropertyKeys.BORDER_BREAK);
        breakSide.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.BORDER_BREAK);


        xmlWriter.startElement(PropertyKeys.TOP_LEFT_EDGE);
        topLeftEdge.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.TOP_LEFT_EDGE);

        xmlWriter.startElement(PropertyKeys.TOP_RIGHT_EDGE);
        topRightEdge.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.TOP_RIGHT_EDGE);

        xmlWriter.startElement(PropertyKeys.BOTTOM_LEFT_EDGE);
        bottomLeftEdge.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.BOTTOM_LEFT_EDGE);

        xmlWriter.startElement(PropertyKeys.BOTTOM_RIGHT_EDGE);
        bottomRightEdge.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.BOTTOM_RIGHT_EDGE);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException, XmlPullParserException
    {
        sameBorderForAllSides = Boolean.parseBoolean(node.getAttributeValueFromRawName(PropertyKeys.SAME_BORDER_FOR_ALL_SIDES));

        while (!node.isFinished())
        {
            Object childNodeList = node.readNextChild();
            if (childNodeList instanceof XmlPullNode)
            {
                XmlPullNode child = (XmlPullNode) childNodeList;
                if (PropertyKeys.BORDER_TOP.equals(child.getRawName()))
                {
                    topSide.readObject(child, xmlContext);
                }
                else if (PropertyKeys.BORDER_BOTTOM.equals(child.getRawName()))
                {
                    bottomSide.readObject(child, xmlContext);
                }
                else if (PropertyKeys.BORDER_LEFT.equals(child.getRawName()))
                {
                    leftSide.readObject(child, xmlContext);
                }
                else if (PropertyKeys.BORDER_RIGHT.equals(child.getRawName()))
                {
                    rightSide.readObject(child, xmlContext);
                }
                else if (PropertyKeys.BORDER_BREAK.equals(child.getRawName()))
                {
                    breakSide.readObject(child, xmlContext);
                }
                else if (PropertyKeys.TOP_LEFT_EDGE.equals(child.getRawName()))
                {
                    topLeftEdge.readObject(child, xmlContext);
                }
                else if (PropertyKeys.TOP_RIGHT_EDGE.equals(child.getRawName()))
                {
                    topRightEdge.readObject(child, xmlContext);
                }
                else if (PropertyKeys.BOTTOM_LEFT_EDGE.equals(child.getRawName()))
                {
                    bottomLeftEdge.readObject(child, xmlContext);
                }
                else if (PropertyKeys.BOTTOM_RIGHT_EDGE.equals(child.getRawName()))
                {
                    bottomRightEdge.readObject(child, xmlContext);
                }
            }
        }
    }
}