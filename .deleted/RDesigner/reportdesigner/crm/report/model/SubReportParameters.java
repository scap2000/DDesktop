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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Martin
 * Date: 17.02.2007
 * Time: 15:19:26
 */
public class SubReportParameters implements XMLExternalizable
{
    private boolean globalImport;
    private boolean globalExport;
    @NotNull
    private HashMap<String, SubReportParameter> importParameters;
    @NotNull
    private HashMap<String, SubReportParameter> exportParameters;


    public SubReportParameters()
    {
        importParameters = new HashMap<String, SubReportParameter>();
        exportParameters = new HashMap<String, SubReportParameter>();
    }


    public SubReportParameters(boolean globalImport, @NotNull ArrayList<SubReportParameter> importParameterList, boolean globalExport, @NotNull ArrayList<SubReportParameter> exportParameterList)
    {
        //noinspection ConstantConditions
        if (importParameterList == null)
        {
            throw new IllegalArgumentException("importParameterList must not be null");
        }
        //noinspection ConstantConditions
        if (exportParameterList == null)
        {
            throw new IllegalArgumentException("exportParameterList must not be null");
        }

        this.globalExport = globalExport;
        this.globalImport = globalImport;
        importParameters = new HashMap<String, SubReportParameter>();
        for (SubReportParameter subReportParameter : importParameterList)
        {
            importParameters.put(subReportParameter.getKey(), subReportParameter);
        }
        exportParameters = new HashMap<String, SubReportParameter>();
        for (SubReportParameter subReportParameter : exportParameterList)
        {
            exportParameters.put(subReportParameter.getKey(), subReportParameter);
        }
    }


    private SubReportParameters(@NotNull HashMap<String, SubReportParameter> importParameters, @NotNull HashMap<String, SubReportParameter> exportParameters, boolean globalImport, boolean globalExport)
    {
        //noinspection ConstantConditions
        if (importParameters == null)
        {
            throw new IllegalArgumentException("importParameters must not be null");
        }
        //noinspection ConstantConditions
        if (exportParameters == null)
        {
            throw new IllegalArgumentException("exportParameters must not be null");
        }
        this.exportParameters = exportParameters;
        this.globalExport = globalExport;
        this.globalImport = globalImport;
        this.importParameters = importParameters;
    }


    @NotNull
    public HashMap<String, SubReportParameter> getExportParameters()
    {
        return new HashMap<String, SubReportParameter>(exportParameters);
    }


    @NotNull
    public SubReportParameters addExportParameters(@NotNull SubReportParameter parameter)
    {
        //noinspection ConstantConditions
        if (parameter == null)
        {
            throw new IllegalArgumentException("parameter must not be null");
        }
        HashMap<String, SubReportParameter> parameters = getImportParameters();
        parameters.put(parameter.getKey(), parameter);
        return new SubReportParameters(importParameters, parameters, globalImport, globalExport);
    }


    public boolean isGlobalExport()
    {
        return globalExport;
    }


    @NotNull
    public SubReportParameters setGlobalExport(boolean globalExport)
    {
        return new SubReportParameters(importParameters, exportParameters, globalImport, globalExport);
    }


    public boolean isGlobalImport()
    {
        return globalImport;
    }


    @NotNull
    public SubReportParameters setGlobalImport(boolean globalImport)
    {
        return new SubReportParameters(importParameters, exportParameters, globalImport, globalExport);
    }


    @NotNull
    public HashMap<String, SubReportParameter> getImportParameters()
    {
        return new HashMap<String, SubReportParameter>(importParameters);
    }


    @NotNull
    public SubReportParameters addImportParameter(@NotNull SubReportParameter parameter)
    {
        //noinspection ConstantConditions
        if (parameter == null)
        {
            throw new IllegalArgumentException("parameter must not be null");
        }
        HashMap<String, SubReportParameter> parameters = getImportParameters();
        parameters.put(parameter.getKey(), parameter);
        return new SubReportParameters(parameters, exportParameters, globalImport, globalExport);
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeProperty(PropertyKeys.GLOBAL_IMPORT, Boolean.toString(globalImport));
        xmlWriter.writeProperty(PropertyKeys.GLOBAL_EXPORT, Boolean.toString(globalExport));

        for (SubReportParameter styleExpression : importParameters.values())
        {
            xmlWriter.startElement(PropertyKeys.IMPORT_PARAMETER);
            styleExpression.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.IMPORT_PARAMETER);
        }

        for (SubReportParameter styleExpression : exportParameters.values())
        {
            xmlWriter.startElement(PropertyKeys.EXPORT_PARAMETER);
            styleExpression.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.EXPORT_PARAMETER);
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

                if (XMLConstants.PROPERTY.equals(child.getRawName()) && PropertyKeys.GLOBAL_IMPORT.equals(child.getAttributeValueFromRawName(XMLConstants.NAME)))
                {
                    globalImport = Boolean.valueOf(XMLUtils.readProperty(PropertyKeys.GLOBAL_IMPORT, child)).booleanValue();
                }
                else if (XMLConstants.PROPERTY.equals(child.getRawName()) && PropertyKeys.GLOBAL_EXPORT.equals(child.getAttributeValueFromRawName(XMLConstants.NAME)))
                {
                    globalExport = Boolean.valueOf(XMLUtils.readProperty(PropertyKeys.GLOBAL_EXPORT, child)).booleanValue();
                }
                else if (PropertyKeys.IMPORT_PARAMETER.equals(child.getRawName()))
                {
                    SubReportParameter subReportParameter = new SubReportParameter("", "");
                    subReportParameter.readObject(child, xmlContext);
                    importParameters.put(subReportParameter.getKey(), subReportParameter);
                }
                else if (PropertyKeys.EXPORT_PARAMETER.equals(child.getRawName()))
                {
                    SubReportParameter subReportParameter = new SubReportParameter("", "");
                    subReportParameter.readObject(child, xmlContext);
                    exportParameters.put(subReportParameter.getKey(), subReportParameter);
                }
            }
        }
    }
}
