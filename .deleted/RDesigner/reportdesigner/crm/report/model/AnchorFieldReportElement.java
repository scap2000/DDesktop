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
 * Time: 08:50:18
 */
public class AnchorFieldReportElement extends TextReportElement
{
    @NotNull
    private String fieldName;


    public AnchorFieldReportElement()
    {
        fieldName = "field" + FieldNameFactory.getInstance().getNextFreeNumber("field");//NON-NLS
    }


    @NotNull
    public String getFieldName()
    {
        return fieldName;
    }


    public void setFieldName(@NotNull final String fieldName)
    {
        //noinspection ConstantConditions
        if (fieldName == null)
        {
            throw new IllegalArgumentException("fieldName must not be null");
        }

        final String oldFieldName = this.fieldName;
        this.fieldName = fieldName;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.FIELD_NAME);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setFieldName(oldFieldName);
                }


                public void redo()
                {
                    setFieldName(fieldName);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.FIELD_NAME, oldFieldName, fieldName);
    }


    @NotNull
    public String getPaintText()
    {
        return fieldName;
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.FIELD_NAME, fieldName);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.FIELD_NAME.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            fieldName = XMLUtils.readProperty(PropertyKeys.FIELD_NAME, node);
        }
    }

}
