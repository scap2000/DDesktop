package org.pentaho.reportdesigner.crm.report.model;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 09:00:29
 */
public class ResourceLabelReportElement extends TextReportElement
{
    @NotNull
    private String resourceBase;
    @NotNull
    private String resourceKey;
    @NotNull
    private String nullString;


    public ResourceLabelReportElement()
    {
        resourceBase = "";
        resourceKey = "resource" + FieldNameFactory.getInstance().getNextFreeNumber("resourceKey");//NON-NLS;
        nullString = "";
        setName("ResourceLabel@" + System.identityHashCode(this));
    }


    @NotNull
    public String getResourceBase()
    {
        return resourceBase;
    }


    public void setResourceBase(@NotNull final String resourceBase)
    {
        final String oldResourceBase = this.resourceBase;
        this.resourceBase = resourceBase;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.RESOURCE_BASE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setResourceBase(oldResourceBase);
                }


                public void redo()
                {
                    setResourceBase(resourceBase);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.RESOURCE_BASE, oldResourceBase, resourceBase);
    }


    @NotNull
    public String getResourceKey()
    {
        return resourceKey;
    }


    public void setResourceKey(@NotNull final String resourceKey)
    {
        final String oldResourceKey = this.resourceKey;
        this.resourceKey = resourceKey;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.RESOURCE_KEY);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setResourceKey(oldResourceKey);
                }


                public void redo()
                {
                    setResourceKey(resourceKey);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.RESOURCE_KEY, oldResourceKey, resourceKey);
    }


    @NotNull
    public String getNullString()
    {
        return nullString;
    }


    public void setNullString(@NotNull final String nullString)
    {
        //noinspection ConstantConditions
        if (nullString == null)
        {
            throw new IllegalArgumentException("nullString must not be null");
        }

        final String oldNullString = this.nullString;
        this.nullString = nullString;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.PAGE_BREAK_BEFORE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setNullString(oldNullString);
                }


                public void redo()
                {
                    setNullString(nullString);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.NULL_STRING, oldNullString, nullString);
    }


    @NotNull
    public String getPaintText()
    {
        return getResourceKey();
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.RESOURCE_BASE, resourceBase);
        xmlWriter.writeProperty(PropertyKeys.RESOURCE_KEY, resourceKey);
        xmlWriter.writeProperty(PropertyKeys.NULL_STRING, nullString);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.RESOURCE_BASE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            resourceBase = XMLUtils.readProperty(PropertyKeys.RESOURCE_BASE, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.RESOURCE_KEY.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            resourceKey = XMLUtils.readProperty(PropertyKeys.RESOURCE_KEY, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.NULL_STRING.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            nullString = XMLUtils.readProperty(PropertyKeys.NULL_STRING, node);
        }
    }
}
