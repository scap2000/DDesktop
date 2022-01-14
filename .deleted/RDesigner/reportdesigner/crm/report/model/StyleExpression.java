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
 * Date: 05.01.2007
 * Time: 08:04:59
 */
public class StyleExpression implements XMLExternalizable
{
    @NotNull
    private String styleKeyName;

    @NotNull
    private String expression;


    public StyleExpression(@NotNull String styleKeyName, @NotNull String expression)
    {
        //noinspection ConstantConditions
        if (styleKeyName == null)
        {
            throw new IllegalArgumentException("styleKeyName must not be null");
        }
        //noinspection ConstantConditions
        if (expression == null)
        {
            throw new IllegalArgumentException("expression must not be null");
        }

        this.expression = expression;
        this.styleKeyName = styleKeyName;
    }


    @NotNull
    public String getExpression()
    {
        return expression;
    }


    @NotNull
    public String getStyleKeyName()
    {
        return styleKeyName;
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute(PropertyKeys.STYLE_KEY_NAME, styleKeyName);
        xmlWriter.writeAttribute(PropertyKeys.EXPRESSION, expression);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        styleKeyName = node.getAttributeValueFromRawName(PropertyKeys.STYLE_KEY_NAME);
        expression = node.getAttributeValueFromRawName(PropertyKeys.EXPRESSION);
    }


    @NotNull
    public String toString()
    {
        return styleKeyName + ": " + expression;
    }
}
