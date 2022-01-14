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
import org.pentaho.reportdesigner.crm.report.util.GraphicUtils;
import org.pentaho.reportdesigner.lib.client.util.MathUtils;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 25.05.2006
 * Time: 11:59:08
 */
public class BorderDefinition implements XMLExternalizable
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(BorderDefinition.class.getName());

    private enum DashType
    {
        @NotNull DOTTED,
        @NotNull DASHED,
        @NotNull DOT_DASH,
        @NotNull DOT_DOT_DASH,
        @NotNull SOLID
    }

    @NotNull
    private Color color;

    private double width;
    private int join;
    private int cap;
    private double miterlimit;
    @Nullable
    private double[] dash;
    private double dashPhase;

    @NotNull
    private BasicStroke basicStroke;


    public BorderDefinition()
    {
        this(1);
    }


    public BorderDefinition(double width)
    {
        this(Color.BLACK, width, BasicStroke.JOIN_MITER, BasicStroke.CAP_SQUARE, 10, null, 0);
    }


    public BorderDefinition(@NotNull BasicStroke basicStroke)
    {
        this(Color.BLACK, basicStroke);
    }


    public BorderDefinition(@NotNull Color color, @NotNull BasicStroke basicStroke)
    {
        //noinspection ConstantConditions
        if (color == null)
        {
            throw new IllegalArgumentException("color must not be null");
        }
        //noinspection ConstantConditions
        if (basicStroke == null)
        {
            throw new IllegalArgumentException("basicStroke must not be null");
        }

        this.color = color;
        this.width = basicStroke.getLineWidth();
        this.join = basicStroke.getLineJoin();
        this.cap = basicStroke.getEndCap();
        this.miterlimit = basicStroke.getMiterLimit();

        double[] dash = null;
        if (basicStroke.getDashArray() != null)
        {
            dash = new double[basicStroke.getDashArray().length];
            for (int i = 0; i < basicStroke.getDashArray().length; i++)
            {
                dash[i] = basicStroke.getDashArray()[i];
            }
        }

        this.dash = dash;
        this.dashPhase = basicStroke.getDashPhase();

        this.basicStroke = new BasicStroke((float) width, cap, join, (float) miterlimit, GraphicUtils.getArrayCopy(dash), (float) dashPhase);
    }


    public BorderDefinition(@NotNull Color borderColor, double borderWidth, int join, int cap, double miterlimit, @Nullable double[] dash, double dashPhase)
    {
        //noinspection ConstantConditions
        if (borderColor == null)
        {
            throw new IllegalArgumentException("borderColor must not be null");
        }

        this.color = borderColor;
        this.width = borderWidth;
        this.join = join;
        this.cap = cap;
        this.miterlimit = miterlimit;
        if (dash != null)
        {
            this.dash = dash.clone();
        }
        this.dashPhase = dashPhase;

        basicStroke = new BasicStroke((float) borderWidth, cap, join, (float) miterlimit, GraphicUtils.getArrayCopy(dash), (float) dashPhase);
    }


    @NotNull
    public BasicStroke getBasicStroke()
    {
        return basicStroke;
    }


    @NotNull
    public Color getColor()
    {
        return color;
    }


    public double getWidth()
    {
        return width;
    }


    public int getJoin()
    {
        return join;
    }


    public int getCap()
    {
        return cap;
    }


    public double getMiterlimit()
    {
        return miterlimit;
    }


    @Nullable
    public double[] getDash()
    {
        if (dash != null)
        {
            return dash.clone();
        }
        return null;
    }


    public double getDashPhase()
    {
        return dashPhase;
    }


    public boolean isSolid()
    {
        return dash == null;
    }


    public boolean isDotted()
    {
        double[] dash = this.dash;
        return dash != null && MathUtils.approxEquals(dash, getDotted(width));
    }


    public boolean isDashed()
    {
        double[] dash = this.dash;
        return dash != null && MathUtils.approxEquals(dash, getDashed(width));
    }


    public boolean isDotDash()
    {
        double[] dash = this.dash;
        return dash != null && MathUtils.approxEquals(dash, getDotDash(width));
    }


    public boolean isDotDotDash()
    {
        double[] dash = this.dash;
        return dash != null && MathUtils.approxEquals(dash, getDotDotDash(width));
    }


    @NotNull
    public static double[] getDotted(double lineWidth)
    {
        return new double[]{0, 2 * lineWidth};
    }


    @NotNull
    public static double[] getDashed(double lineWidth)
    {
        return new double[]{6 * lineWidth, 6 * lineWidth};
    }


    @NotNull
    public static double[] getDotDash(double lineWidth)
    {
        return new double[]{0, 2 * lineWidth, 6 * lineWidth, 2 * lineWidth};
    }


    @NotNull
    public static double[] getDotDotDash(double lineWidth)
    {
        return new double[]{0, 2 * lineWidth, 0, 2 * lineWidth, 6 * lineWidth, 2 * lineWidth};
    }


    @NotNull
    public BorderDefinition derive(double width)
    {
        return new BorderDefinition(color, width, join, cap, miterlimit, dash, dashPhase);
    }


    @NotNull
    public BorderDefinition derive(@NotNull Color color)
    {
        return new BorderDefinition(color, width, join, cap, miterlimit, dash, dashPhase);
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute(PropertyKeys.COLOR, ObjectConverterFactory.getInstance().getColorConverter().getString(color));
        xmlWriter.writeAttribute(PropertyKeys.WIDTH, String.valueOf(width));
        xmlWriter.writeAttribute(PropertyKeys.JOIN, String.valueOf(join));
        xmlWriter.writeAttribute(PropertyKeys.CAP, String.valueOf(cap));
        xmlWriter.writeAttribute(PropertyKeys.MITERLIMIT, String.valueOf(miterlimit));
        if (dash != null)
        {
            if (isDotted())
            {
                xmlWriter.writeAttribute(PropertyKeys.DASH, DashType.DOTTED.toString());
            }
            else if (isDashed())
            {
                xmlWriter.writeAttribute(PropertyKeys.DASH, DashType.DASHED.toString());
            }
            else if (isDotDash())
            {
                xmlWriter.writeAttribute(PropertyKeys.DASH, DashType.DOT_DASH.toString());
            }
            else if (isDotDotDash())
            {
                xmlWriter.writeAttribute(PropertyKeys.DASH, DashType.DOT_DOT_DASH.toString());
            }
        }
        //don't write anything for solid->default
        xmlWriter.writeAttribute(PropertyKeys.DASH_PHASE, String.valueOf(dashPhase));
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException
    {
        color = ObjectConverterFactory.getInstance().getColorConverter().getObject(node.getAttributeValueFromRawName(PropertyKeys.COLOR));
        width = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.WIDTH));
        join = Integer.parseInt(node.getAttributeValueFromRawName(PropertyKeys.JOIN));
        cap = Integer.parseInt(node.getAttributeValueFromRawName(PropertyKeys.CAP));
        miterlimit = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.MITERLIMIT));

        String dash = node.getAttributeValueFromRawName(PropertyKeys.DASH);
        if (dash != null)
        {
            try
            {
                DashType dashType = DashType.valueOf(dash);
                if (dashType == DashType.DOTTED)
                {
                    this.dash = getDotted(width);
                }
                else if (dashType == DashType.DASHED)
                {
                    this.dash = getDashed(width);
                }
                else if (dashType == DashType.DOT_DASH)
                {
                    this.dash = getDotDash(width);
                }
                else if (dashType == DashType.DOT_DOT_DASH)
                {
                    this.dash = getDotDotDash(width);
                }
            }
            catch (IllegalArgumentException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "BorderDefinition.readObject ", e);
                //fall back to default->solid->dashPhase=null
            }
        }
        dashPhase = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.DASH_PHASE));

        basicStroke = new BasicStroke((float) width, cap, join, (float) miterlimit, GraphicUtils.getArrayCopy(this.dash), (float) dashPhase);
    }
}
