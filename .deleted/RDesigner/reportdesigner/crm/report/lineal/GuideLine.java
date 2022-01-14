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
package org.pentaho.reportdesigner.crm.report.lineal;

import org.gjt.xpp.XmlPullNode;
import org.gjt.xpp.XmlPullParserException;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;

/**
 * User: Martin
 * Date: 26.01.2006
 * Time: 10:44:44
 */
public class GuideLine implements XMLExternalizable
{
    private double position;
    private boolean active;


    public GuideLine(double position, boolean active)
    {
        this.position = position;
        this.active = active;
    }


    public double getPosition()
    {
        return position;
    }


    void setPosition(double position)
    {
        this.position = position;
    }


    public boolean isActive()
    {
        return active;
    }


    void setActive(boolean active)
    {
        this.active = active;
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute(PropertyKeys.POSITION, String.valueOf(position));
        xmlWriter.writeAttribute(PropertyKeys.ACTIVE, String.valueOf(active));
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException, XmlPullParserException
    {
        position = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.POSITION));
        active = Boolean.parseBoolean(node.getAttributeValueFromRawName(PropertyKeys.ACTIVE));
    }
}
