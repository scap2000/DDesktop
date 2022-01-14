package org.pentaho.reportdesigner.crm.report.model;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;

/**
 * User: Martin
 * Date: 17.01.2007
 * Time: 18:32:28
 */
public class Formula implements XMLExternalizable
{
    @NotNull
    private String text;


    public Formula(@NotNull String text)
    {
        this.text = text;
    }


    @NotNull
    public String getText()
    {
        return text;
    }


    @NotNull
    public String toString()
    {
        return "Formula{" +
               "text='" + text + '\'' +
               '}';
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeProperty(PropertyKeys.TEXT, text);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        while (!node.isFinished())
        {
            Object childNodeList = node.readNextChild();
            if (childNodeList instanceof XmlPullNode)
            {
                XmlPullNode child = (XmlPullNode) childNodeList;
                if (XMLConstants.PROPERTY.equals(child.getRawName()) && PropertyKeys.TEXT.equals(child.getAttributeValueFromRawName(XMLConstants.NAME)))
                {
                    text = XMLUtils.readProperty(PropertyKeys.TEXT, child);
                }
            }
        }
    }
}
