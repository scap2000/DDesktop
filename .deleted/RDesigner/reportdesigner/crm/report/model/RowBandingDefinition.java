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
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.awt.*;
import java.io.IOException;

/**
 * User: Martin
 * Date: 02.06.2006
 * Time: 14:39:32
 */
public class RowBandingDefinition implements XMLExternalizable
{
    private boolean enabled;
    @NotNull
    private Color color;
    private boolean startState;//visible or not when starting
    private int switchItemCount;//number of elements until a switch occurs, see ElementVisibilitySwitchFunction


    public RowBandingDefinition()
    {
        this.enabled = false;
        this.color = Color.LIGHT_GRAY;
        this.startState = true;
        this.switchItemCount = 1;
    }


    public RowBandingDefinition(boolean enabled, @NotNull Color color, boolean startState, int switchItemCount)
    {
        //noinspection ConstantConditions
        if (color == null)
        {
            throw new IllegalArgumentException("color must not be null");
        }

        this.enabled = enabled;
        this.color = color;
        this.startState = startState;
        this.switchItemCount = switchItemCount;
    }


    public boolean isEnabled()
    {
        return enabled;
    }


    @NotNull
    public Color getColor()
    {
        return color;
    }


    public boolean isStartState()
    {
        return startState;
    }


    public int getSwitchItemCount()
    {
        return switchItemCount;
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute(PropertyKeys.ENABLED, String.valueOf(enabled));
        xmlWriter.writeAttribute(PropertyKeys.COLOR, ObjectConverterFactory.getInstance().getColorConverter().getString(color));
        xmlWriter.writeAttribute(PropertyKeys.START_STATE, String.valueOf(startState));
        xmlWriter.writeAttribute(PropertyKeys.SWITCH_ITEM_COUNT, String.valueOf(switchItemCount));
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        enabled = Boolean.parseBoolean(node.getAttributeValueFromRawName(PropertyKeys.ENABLED));
        color = ObjectConverterFactory.getInstance().getColorConverter().getObject(node.getAttributeValueFromRawName(PropertyKeys.COLOR));
        startState = Boolean.parseBoolean(node.getAttributeValueFromRawName(PropertyKeys.START_STATE));
        switchItemCount = Integer.parseInt(node.getAttributeValueFromRawName(PropertyKeys.SWITCH_ITEM_COUNT));
    }
}
