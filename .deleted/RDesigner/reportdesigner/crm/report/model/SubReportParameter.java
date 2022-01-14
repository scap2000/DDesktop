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
 * Date: 17.02.2007
 * Time: 15:44:59
 */
public class SubReportParameter implements XMLExternalizable
{
    @NotNull
    private String key;
    @NotNull
    private String value;


    public SubReportParameter(@NotNull String key, @NotNull String value)
    {
        //noinspection ConstantConditions
        if (key == null)
        {
            throw new IllegalArgumentException("key must not be null");
        }
        //noinspection ConstantConditions
        if (value == null)
        {
            throw new IllegalArgumentException("value must not be null");
        }
        this.key = key;
        this.value = value;
    }


    @NotNull
    public String getKey()
    {
        return key;
    }


    @NotNull
    public String getValue()
    {
        return value;
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute(PropertyKeys.KEY, key);
        xmlWriter.writeAttribute(PropertyKeys.VALUE, value);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        key = node.getAttributeValueFromRawName(PropertyKeys.KEY);
        value = node.getAttributeValueFromRawName(PropertyKeys.VALUE);
    }


    @NotNull
    public String toString()
    {
        return "SubReportParameter{" +
               "key='" + key + '\'' +
               ", value='" + value + '\'' +
               '}';
    }
}
