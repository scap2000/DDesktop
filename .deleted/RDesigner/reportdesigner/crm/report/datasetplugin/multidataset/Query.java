package org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset;

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
 * Date: 24.02.2007
 * Time: 12:44:59
 */
public class Query implements XMLExternalizable
{
    @NotNull
    private String queryName;
    @NotNull
    private String query;


    public Query(@NotNull String queryName, @NotNull String query)
    {
        //noinspection ConstantConditions
        if (queryName == null)
        {
            throw new IllegalArgumentException("queryName must not be null");
        }
        //noinspection ConstantConditions
        if (query == null)
        {
            throw new IllegalArgumentException("query must not be null");
        }

        this.queryName = queryName;
        this.query = query;
    }


    @NotNull
    public String getQueryName()
    {
        return queryName;
    }


    public void setQueryName(@NotNull String queryName)
    {
        //noinspection ConstantConditions
        if (queryName == null)
        {
            throw new IllegalArgumentException("queryName must not be null");
        }

        this.queryName = queryName;
    }


    @NotNull
    public String getQuery()
    {
        if (queryName == null)
        {
            throw new IllegalArgumentException("queryName must not be null");
        }

        return query;
    }


    public void setQuery(@NotNull String query)
    {
        this.query = query;
    }


    @NotNull
    public String toString()
    {
        return queryName.length() > 0 ? queryName : " ";
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute(PropertyKeys.QUERY_NAME, queryName);
        xmlWriter.writeProperty(PropertyKeys.QUERY, query);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        queryName = node.getAttributeValueFromRawName(PropertyKeys.QUERY_NAME);
        while (!node.isFinished())
        {
            Object childNodeList = node.readNextChild();
            if (childNodeList instanceof XmlPullNode)
            {
                XmlPullNode child = (XmlPullNode) childNodeList;
                if (XMLConstants.PROPERTY.equals(child.getRawName()) && PropertyKeys.QUERY.equals(child.getAttributeValueFromRawName(XMLConstants.NAME)))
                {
                    query = XMLUtils.readProperty(PropertyKeys.QUERY, child);
                }
            }
        }
    }
}
