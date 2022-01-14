package org.pentaho.reportdesigner.crm.report.model;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;

/**
 * User: Martin
 * Date: 02.08.2007
 * Time: 11:20:28
 */
public class ElementPadding implements XMLExternalizable
{
    private double top;
    private double bottom;
    private double left;
    private double right;


    public ElementPadding()
    {
    }


    public ElementPadding(double top, double bottom, double left, double right)
    {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }


    public double getTop()
    {
        return top;
    }


    public double getBottom()
    {
        return bottom;
    }


    public double getLeft()
    {
        return left;
    }


    public double getRight()
    {
        return right;
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute(PropertyKeys.TOP, String.valueOf(top));
        xmlWriter.writeAttribute(PropertyKeys.BOTTOM, String.valueOf(bottom));
        xmlWriter.writeAttribute(PropertyKeys.LEFT, String.valueOf(left));
        xmlWriter.writeAttribute(PropertyKeys.RIGHT, String.valueOf(right));
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException
    {
        top = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.TOP));
        bottom = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.BOTTOM));
        left = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.LEFT));
        right = Double.parseDouble(node.getAttributeValueFromRawName(PropertyKeys.RIGHT));
    }
}
