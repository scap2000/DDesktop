package org.pentaho.reportdesigner.crm.report.configuration;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * User: Martin
 * Date: 17.03.2007
 * Time: 11:20:28
 */
public class ReportConfiguration implements XMLExternalizable
{
    @NotNull
    private HashMap<String, String> configProperties;


    public ReportConfiguration()
    {
        configProperties = new HashMap<String, String>();
    }


    public ReportConfiguration(@NotNull HashMap<String, String> configProperties)
    {
        //noinspection ConstantConditions
        if (configProperties == null)
        {
            throw new IllegalArgumentException("configProperties must not be null");
        }

        this.configProperties = configProperties;
    }


    @NotNull
    public HashMap<String, String> getConfigProperties()
    {
        return new HashMap<String, String>(configProperties);
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        Set<String> keys = configProperties.keySet();
        for (String key : keys)
        {
            String value = configProperties.get(key);
            xmlWriter.writeProperty(key, value);
        }
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        while (!node.isFinished())
        {
            Object childNodeList = node.readNextChild();
            if (childNodeList instanceof XmlPullNode)
            {
                XmlPullNode child = (XmlPullNode) childNodeList;
                if (XMLConstants.PROPERTY.equals(child.getRawName()))
                {
                    String key = child.getAttributeValueFromRawName(XMLConstants.NAME);
                    String value = XMLUtils.readProperty(key, child);

                    configProperties.put(key, value);
                }
            }
        }
    }
}
