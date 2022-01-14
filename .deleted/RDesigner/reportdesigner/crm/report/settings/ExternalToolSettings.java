/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.settings;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;

/**
 * User: Martin
 * Date: 28.05.2006
 * Time: 09:58:37
 */
public class ExternalToolSettings implements XMLExternalizable
{
    @NotNull
    private static final String USE_DEFAULT_PDFVIEWER = "useDefaultPDFViewer";
    @NotNull
    private static final String CUSTOM_PDFVIEWER_EXECUTABLE = "customPDFViewerExecutable";
    @NotNull
    private static final String CUSTOM_PDFVIEWER_PARAMETERS = "customPDFViewerParameters";
    @NotNull
    private static final String USE_DEFAULT_RTFVIEWER = "useDefaultRTFViewer";
    @NotNull
    private static final String CUSTOM_RTFVIEWER_EXECUTABLE = "customRTFViewerExecutable";
    @NotNull
    private static final String CUSTOM_RTFVIEWER_PARAMETERS = "customRTFViewerParameters";
    @NotNull
    private static final String USE_DEFAULT_XLSVIEWER = "useDefaultXLSViewer";
    @NotNull
    private static final String CUSTOM_XLSVIEWER_EXECUTABLE = "customXLSViewerExecutable";
    @NotNull
    private static final String CUSTOM_XLSVIEWER_PARAMETERS = "customXLSViewerParameters";
    @NotNull
    private static final String USE_DEFAULT_CSVVIEWER = "useDefaultCSVViewer";
    @NotNull
    private static final String CUSTOM_CSVVIEWER_EXECUTABLE = "customCSVViewerExecutable";
    @NotNull
    private static final String CUSTOM_CSVVIEWER_PARAMETERS = "customCSVViewerParameters";
    @NotNull
    private static final String USE_DEFAULT_XMLVIEWER = "useDefaultXMLViewer";
    @NotNull
    private static final String CUSTOM_XMLVIEWER_EXECUTABLE = "customXMLViewerExecutable";
    @NotNull
    private static final String CUSTOM_XMLVIEWER_PARAMETERS = "customXMLViewerParameters";


    private boolean useDefaultPDFViewer;
    @NotNull
    private String customPDFViewerExecutable;
    @NotNull
    private String customPDFViewerParameters;

    private boolean useDefaultRTFViewer;
    @NotNull
    private String customRTFViewerExecutable;
    @NotNull
    private String customRTFViewerParameters;

    private boolean useDefaultXLSViewer;
    @NotNull
    private String customXLSViewerExecutable;
    @NotNull
    private String customXLSViewerParameters;

    private boolean useDefaultCSVViewer;
    @NotNull
    private String customCSVViewerExecutable;
    @NotNull
    private String customCSVViewerParameters;

    private boolean useDefaultXMLViewer;
    @NotNull
    private String customXMLViewerExecutable;
    @NotNull
    private String customXMLViewerParameters;


    public ExternalToolSettings()
    {
        useDefaultPDFViewer = true;
        customPDFViewerExecutable = "";
        customPDFViewerParameters = "{0}";

        useDefaultRTFViewer = true;
        customRTFViewerExecutable = "";
        customRTFViewerParameters = "{0}";

        useDefaultXLSViewer = true;
        customXLSViewerExecutable = "";
        customXLSViewerParameters = "{0}";

        useDefaultCSVViewer = true;
        customCSVViewerExecutable = "";
        customCSVViewerParameters = "{0}";

        useDefaultXMLViewer = true;
        customXMLViewerExecutable = "";
        customXMLViewerParameters = "{0}";
    }


    public boolean isUseDefaultPDFViewer()
    {
        return useDefaultPDFViewer;
    }


    public void setUseDefaultPDFViewer(boolean useDefaultPDFViewer)
    {
        this.useDefaultPDFViewer = useDefaultPDFViewer;
    }


    @NotNull
    public String getCustomPDFViewerExecutable()
    {
        return customPDFViewerExecutable;
    }


    public void setCustomPDFViewerExecutable(@NotNull String customPDFViewerExecutable)
    {
        this.customPDFViewerExecutable = customPDFViewerExecutable;
    }


    @NotNull
    public String getCustomPDFViewerParameters()
    {
        return customPDFViewerParameters;
    }


    public void setCustomPDFViewerParameters(@NotNull String customPDFViewerParameters)
    {
        this.customPDFViewerParameters = customPDFViewerParameters;
    }


    public boolean isUseDefaultRTFViewer()
    {
        return useDefaultRTFViewer;
    }


    public void setUseDefaultRTFViewer(boolean useDefaultRTFViewer)
    {
        this.useDefaultRTFViewer = useDefaultRTFViewer;
    }


    @NotNull
    public String getCustomRTFViewerExecutable()
    {
        return customRTFViewerExecutable;
    }


    public void setCustomRTFViewerExecutable(@NotNull String customRTFViewerExecutable)
    {
        this.customRTFViewerExecutable = customRTFViewerExecutable;
    }


    @NotNull
    public String getCustomRTFViewerParameters()
    {
        return customRTFViewerParameters;
    }


    public void setCustomRTFViewerParameters(@NotNull String customRTFViewerParameters)
    {
        this.customRTFViewerParameters = customRTFViewerParameters;
    }


    public boolean isUseDefaultXLSViewer()
    {
        return useDefaultXLSViewer;
    }


    public void setUseDefaultXLSViewer(boolean useDefaultXLSViewer)
    {
        this.useDefaultXLSViewer = useDefaultXLSViewer;
    }


    @NotNull
    public String getCustomXLSViewerExecutable()
    {
        return customXLSViewerExecutable;
    }


    public void setCustomXLSViewerExecutable(@NotNull String customXLSViewerExecutable)
    {
        this.customXLSViewerExecutable = customXLSViewerExecutable;
    }


    @NotNull
    public String getCustomXLSViewerParameters()
    {
        return customXLSViewerParameters;
    }


    public void setCustomXLSViewerParameters(@NotNull String customXLSViewerParameters)
    {
        this.customXLSViewerParameters = customXLSViewerParameters;
    }


    public boolean isUseDefaultCSVViewer()
    {
        return useDefaultCSVViewer;
    }


    public void setUseDefaultCSVViewer(boolean useDefaultCSVViewer)
    {
        this.useDefaultCSVViewer = useDefaultCSVViewer;
    }


    @NotNull
    public String getCustomCSVViewerExecutable()
    {
        return customCSVViewerExecutable;
    }


    public void setCustomCSVViewerExecutable(@NotNull String customCSVViewerExecutable)
    {
        this.customCSVViewerExecutable = customCSVViewerExecutable;
    }


    @NotNull
    public String getCustomCSVViewerParameters()
    {
        return customCSVViewerParameters;
    }


    public void setCustomCSVViewerParameters(@NotNull String customCSVViewerParameters)
    {
        this.customCSVViewerParameters = customCSVViewerParameters;
    }


    public boolean isUseDefaultXMLViewer()
    {
        return useDefaultXMLViewer;
    }


    public void setUseDefaultXMLViewer(boolean useDefaultXMLViewer)
    {
        this.useDefaultXMLViewer = useDefaultXMLViewer;
    }


    @NotNull
    public String getCustomXMLViewerExecutable()
    {
        return customXMLViewerExecutable;
    }


    public void setCustomXMLViewerExecutable(@NotNull String customXMLViewerExecutable)
    {
        this.customXMLViewerExecutable = customXMLViewerExecutable;
    }


    @NotNull
    public String getCustomXMLViewerParameters()
    {
        return customXMLViewerParameters;
    }


    public void setCustomXMLViewerParameters(@NotNull String customXMLViewerParameters)
    {
        this.customXMLViewerParameters = customXMLViewerParameters;
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute("v", "1");

        xmlWriter.writeAttribute(USE_DEFAULT_PDFVIEWER, String.valueOf(useDefaultPDFViewer)).writeAttribute(CUSTOM_PDFVIEWER_EXECUTABLE, customPDFViewerExecutable).writeAttribute(CUSTOM_PDFVIEWER_PARAMETERS, customPDFViewerParameters);
        xmlWriter.writeAttribute(USE_DEFAULT_RTFVIEWER, String.valueOf(useDefaultPDFViewer)).writeAttribute(CUSTOM_RTFVIEWER_EXECUTABLE, customPDFViewerExecutable).writeAttribute(CUSTOM_RTFVIEWER_PARAMETERS, customPDFViewerParameters);
        xmlWriter.writeAttribute(USE_DEFAULT_XLSVIEWER, String.valueOf(useDefaultXLSViewer)).writeAttribute(CUSTOM_XLSVIEWER_EXECUTABLE, customXLSViewerExecutable).writeAttribute(CUSTOM_XLSVIEWER_PARAMETERS, customXLSViewerParameters);
        xmlWriter.writeAttribute(USE_DEFAULT_CSVVIEWER, String.valueOf(useDefaultCSVViewer)).writeAttribute(CUSTOM_CSVVIEWER_EXECUTABLE, customCSVViewerExecutable).writeAttribute(CUSTOM_CSVVIEWER_PARAMETERS, customCSVViewerParameters);
        xmlWriter.writeAttribute(USE_DEFAULT_XMLVIEWER, String.valueOf(useDefaultXMLViewer)).writeAttribute(CUSTOM_XMLVIEWER_EXECUTABLE, customXMLViewerExecutable).writeAttribute(CUSTOM_XMLVIEWER_PARAMETERS, customXMLViewerParameters);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext)
    {
        //PDF
        String useDefaultPDFViewerTemp = node.getAttributeValueFromRawName(USE_DEFAULT_PDFVIEWER);
        if (useDefaultPDFViewerTemp != null)
        {
            useDefaultPDFViewer = Boolean.parseBoolean(useDefaultPDFViewerTemp);
        }

        String customPDFViewerExecutableTemp = node.getAttributeValueFromRawName(CUSTOM_PDFVIEWER_EXECUTABLE);
        if (customPDFViewerExecutableTemp != null)
        {
            customPDFViewerExecutable = customPDFViewerExecutableTemp;
        }

        String customPDFViewerParametersTemp = node.getAttributeValueFromRawName(CUSTOM_PDFVIEWER_PARAMETERS);
        if (customPDFViewerParametersTemp != null)
        {
            customPDFViewerParameters = customPDFViewerParametersTemp;
        }

        //RTF
        String useDefaultRTFViewerTemp = node.getAttributeValueFromRawName(USE_DEFAULT_RTFVIEWER);
        if (useDefaultRTFViewerTemp != null)
        {
            useDefaultRTFViewer = Boolean.parseBoolean(useDefaultRTFViewerTemp);
        }

        String customRTFViewerExecutableTemp = node.getAttributeValueFromRawName(CUSTOM_RTFVIEWER_EXECUTABLE);
        if (customRTFViewerExecutableTemp != null)
        {
            customRTFViewerExecutable = customRTFViewerExecutableTemp;
        }

        String customRTFViewerParametersTemp = node.getAttributeValueFromRawName(CUSTOM_RTFVIEWER_PARAMETERS);
        if (customRTFViewerParametersTemp != null)
        {
            customRTFViewerParameters = customRTFViewerParametersTemp;
        }

        //XLS
        String useDefaultXLSViewerTemp = node.getAttributeValueFromRawName(USE_DEFAULT_XLSVIEWER);
        if (useDefaultXLSViewerTemp != null)
        {
            useDefaultXLSViewer = Boolean.parseBoolean(useDefaultXLSViewerTemp);
        }

        String customXLSViewerExecutableTemp = node.getAttributeValueFromRawName(CUSTOM_XLSVIEWER_EXECUTABLE);
        if (customXLSViewerExecutableTemp != null)
        {
            customXLSViewerExecutable = customXLSViewerExecutableTemp;
        }

        String customXLSViewerParametersTemp = node.getAttributeValueFromRawName(CUSTOM_XLSVIEWER_PARAMETERS);
        if (customXLSViewerParametersTemp != null)
        {
            customXLSViewerParameters = customXLSViewerParametersTemp;
        }

        //CSV
        String useDefaultCSVViewerTemp = node.getAttributeValueFromRawName(USE_DEFAULT_CSVVIEWER);
        if (useDefaultCSVViewerTemp != null)
        {
            useDefaultCSVViewer = Boolean.parseBoolean(useDefaultCSVViewerTemp);
        }

        String customCSVViewerExecutableTemp = node.getAttributeValueFromRawName(CUSTOM_CSVVIEWER_EXECUTABLE);
        if (customCSVViewerExecutableTemp != null)
        {
            customCSVViewerExecutable = customCSVViewerExecutableTemp;
        }

        String customCSVViewerParametersTemp = node.getAttributeValueFromRawName(CUSTOM_CSVVIEWER_PARAMETERS);
        if (customCSVViewerParametersTemp != null)
        {
            customCSVViewerParameters = customCSVViewerParametersTemp;
        }

        //XML
        String useDefaultXMLViewerTemp = node.getAttributeValueFromRawName(USE_DEFAULT_XMLVIEWER);
        if (useDefaultXMLViewerTemp != null)
        {
            useDefaultXMLViewer = Boolean.parseBoolean(useDefaultXMLViewerTemp);
        }

        String customXMLViewerExecutableTemp = node.getAttributeValueFromRawName(CUSTOM_XMLVIEWER_EXECUTABLE);
        if (customXMLViewerExecutableTemp != null)
        {
            customXMLViewerExecutable = customXMLViewerExecutableTemp;
        }

        String customXMLViewerParametersTemp = node.getAttributeValueFromRawName(CUSTOM_XMLVIEWER_PARAMETERS);
        if (customXMLViewerParametersTemp != null)
        {
            customXMLViewerParameters = customXMLViewerParametersTemp;
        }
    }
}
