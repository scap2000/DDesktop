package org.pentaho.reportdesigner.crm.report.model;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Martin
 * Date: 02.06.2006
 * Time: 14:39:32
 */
public class StyleExpressions implements XMLExternalizable
{
    @NotNull
    private ArrayList<StyleExpression> styleExpressions;


    public StyleExpressions()
    {
        styleExpressions = new ArrayList<StyleExpression>();
    }


    public StyleExpressions(@NotNull StyleExpressions styleExpressions)
    {
        //noinspection ConstantConditions
        if (styleExpressions == null)
        {
            throw new IllegalArgumentException("styleExpressions must not be null");
        }

        this.styleExpressions = new ArrayList<StyleExpression>();
        this.styleExpressions.addAll(styleExpressions.styleExpressions);
    }


    @NotNull
    public StyleExpressions addStyleExpression(@NotNull StyleExpression styleExpression)
    {
        //noinspection ConstantConditions
        if (styleExpression == null)
        {
            throw new IllegalArgumentException("styleExpression must not be null");
        }

        StyleExpressions styleExpressions = new StyleExpressions();
        styleExpressions.styleExpressions.addAll(this.styleExpressions);
        styleExpressions.styleExpressions.add(styleExpression);
        return styleExpressions;
    }


    @NotNull
    public StyleExpressions removeStyleExpression(@NotNull StyleExpression styleExpression)
    {
        //noinspection ConstantConditions
        if (styleExpression == null)
        {
            throw new IllegalArgumentException("styleExpression must not be null");
        }

        StyleExpressions styleExpressions = new StyleExpressions();
        styleExpressions.styleExpressions.addAll(this.styleExpressions);
        styleExpressions.styleExpressions.remove(styleExpression);
        return styleExpressions;
    }


    @NotNull
    public List<StyleExpression> getStyleExpressions()
    {
        return Collections.unmodifiableList(styleExpressions);
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        for (StyleExpression styleExpression : styleExpressions)
        {
            xmlWriter.startElement(PropertyKeys.STYLE_EXPRESSION);
            styleExpression.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.STYLE_EXPRESSION);
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
                if (PropertyKeys.STYLE_EXPRESSION.equals(child.getRawName()))
                {
                    StyleExpression styleExpression = new StyleExpression("", "");
                    styleExpression.readObject(child, xmlContext);
                    styleExpressions.add(styleExpression);
                }
            }
        }
    }
}
