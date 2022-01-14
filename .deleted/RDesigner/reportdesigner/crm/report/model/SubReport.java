package org.pentaho.reportdesigner.crm.report.model;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;

/**
 * User: Martin
 * Date: 06.02.2007
 * Time: 17:29:22
 */
public class SubReport extends Report
{

    @NotNull
    private String query;

    @NotNull
    private SubReportParameters parameters;

    @NotNull
    private SubReportDataElement subReportDataElement;


    public SubReport(@NotNull BandToplevelReportElement reportHeaderBand, @NotNull BandToplevelReportElement reportFooterBand, @NotNull BandToplevelReportElement pageHeaderBand, @NotNull BandToplevelReportElement pageFooterBand, @NotNull ReportGroups reportGroups, @NotNull BandToplevelReportElement itemBand, @NotNull BandToplevelReportElement watermarkBand, @NotNull BandToplevelReportElement noDataBand, @NotNull PageDefinition pageDefinition, @NotNull ReportFunctionsElement reportFunctionsElement)
    {
        super(reportHeaderBand, reportFooterBand, pageHeaderBand, pageFooterBand, reportGroups, itemBand, watermarkBand, noDataBand, pageDefinition, reportFunctionsElement);

        subReportDataElement = new SubReportDataElement();
        insertChild(subReportDataElement, 0);
        removeChild(getDataSetsReportElement());

        parameters = new SubReportParameters();
        query = "";
    }


    public void addChild(@NotNull ReportElement child)
    {
        if (child instanceof SubReportDataElement)
        {
            removeChild(subReportDataElement);
            subReportDataElement = (SubReportDataElement) child;
            super.addChild(child);
        }
        else
        {
            super.addChild(child);
        }
    }


    @NotNull
    public SubReportDataElement getSubReportDataElement()
    {
        return subReportDataElement;
    }


    @NotNull
    public String getQuery()
    {
        return query;
    }


    public void setQuery(@NotNull final String query)
    {
        //noinspection ConstantConditions
        if (query == null)
        {
            throw new IllegalArgumentException("query must not be null");
        }

        final String oldQuery = this.query;
        this.query = query;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.QUERY);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setQuery(oldQuery);
                }


                public void redo()
                {
                    setQuery(query);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.QUERY, oldQuery, query);
    }


    @NotNull
    public SubReportParameters getParameters()
    {
        return parameters;
    }


    public void setParameters(@NotNull final SubReportParameters parameters)
    {
        //noinspection ConstantConditions
        if (parameters == null)
        {
            throw new IllegalArgumentException("parameters must not be null");
        }

        final SubReportParameters oldParameters = this.parameters;
        this.parameters = parameters;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.PARAMETERS);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setParameters(oldParameters);
                }


                public void redo()
                {
                    setParameters(parameters);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.PARAMETERS, oldParameters, parameters);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.QUERY, query);

        xmlWriter.startElement(PropertyKeys.PARAMETERS);
        parameters.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.PARAMETERS);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.QUERY.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            query = XMLUtils.readProperty(PropertyKeys.QUERY, node);
        }
        else if (PropertyKeys.PARAMETERS.equals(node.getRawName()))
        {
            parameters = new SubReportParameters();
            parameters.readObject(node, xmlContext);
        }
    }
}
