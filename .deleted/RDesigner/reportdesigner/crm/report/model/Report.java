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
package org.pentaho.reportdesigner.crm.report.model;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.configuration.ReportConfiguration;
import org.pentaho.reportdesigner.crm.report.lineal.LinealModel;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 16:17:10
 */
public class Report extends ReportElement
{
    @SuppressWarnings({"InstanceVariableInitializationInspection"})
    @NotNull
    private BandToplevelReportElement reportHeaderBand;

    @SuppressWarnings({"InstanceVariableInitializationInspection"})
    @NotNull
    private BandToplevelReportElement reportFooterBand;

    @SuppressWarnings({"InstanceVariableInitializationInspection"})
    @NotNull
    private BandToplevelPageReportElement pageHeaderBand;
    @SuppressWarnings({"InstanceVariableInitializationInspection"})
    @NotNull
    private BandToplevelPageReportElement pageFooterBand;

    @SuppressWarnings({"InstanceVariableInitializationInspection"})
    @NotNull
    private ReportGroups reportGroups;

    @SuppressWarnings({"InstanceVariableInitializationInspection"})
    @NotNull
    private BandToplevelItemReportElement itemBand;

    @SuppressWarnings({"InstanceVariableInitializationInspection"})
    @NotNull
    private BandToplevelReportElement watermarkBand;

    @SuppressWarnings({"InstanceVariableInitializationInspection"})
    @NotNull
    private BandToplevelReportElement noDataBand;

    @NotNull
    private PageDefinition pageDefinition;

    @SuppressWarnings({"InstanceVariableInitializationInspection"})
    @NotNull
    private ReportFunctionsElement reportFunctionsElement;

    @SuppressWarnings({"InstanceVariableInitializationInspection"})
    @NotNull
    private DataSetsReportElement dataSetsReportElement;

    @NotNull
    private LinealModel horizontalLinealModel;

    @NotNull
    private Locale defaultLocale;

    @Nullable
    private URL resourceBundleClasspath;

    private boolean useMaxCharBounds;

    @NotNull
    private ReportConfiguration reportConfiguration;


    public Report(@NotNull BandToplevelReportElement reportHeaderBand,
                  @NotNull BandToplevelReportElement reportFooterBand,
                  @NotNull BandToplevelReportElement pageHeaderBand,
                  @NotNull BandToplevelReportElement pageFooterBand,
                  @NotNull ReportGroups reportGroups,
                  @NotNull BandToplevelReportElement itemBand,
                  @NotNull BandToplevelReportElement watermarkBand,
                  @NotNull BandToplevelReportElement noDataBand,
                  @NotNull PageDefinition pageDefinition,
                  @NotNull ReportFunctionsElement reportFunctionsElement)
    {
        //noinspection ConstantConditions
        if (reportHeaderBand == null)
        {
            throw new IllegalArgumentException("reportHeaderBand must not be null");
        }
        //noinspection ConstantConditions
        if (reportFooterBand == null)
        {
            throw new IllegalArgumentException("reportFooterBand must not be null");
        }
        //noinspection ConstantConditions
        if (pageHeaderBand == null)
        {
            throw new IllegalArgumentException("pageHeaderBand must not be null");
        }
        //noinspection ConstantConditions
        if (pageFooterBand == null)
        {
            throw new IllegalArgumentException("pageFooterBand must not be null");
        }
        //noinspection ConstantConditions
        if (reportGroups == null)
        {
            throw new IllegalArgumentException("reportGroups must not be null");
        }
        //noinspection ConstantConditions
        if (itemBand == null)
        {
            throw new IllegalArgumentException("itemBand must not be null");
        }
        //noinspection ConstantConditions
        if (watermarkBand == null)
        {
            throw new IllegalArgumentException("watermarkBand must not be null");
        }
        //noinspection ConstantConditions
        if (noDataBand == null)
        {
            throw new IllegalArgumentException("noDataBand must not be null");
        }
        //noinspection ConstantConditions
        if (pageDefinition == null)
        {
            throw new IllegalArgumentException("pageDefinition must not be null");
        }

        this.pageDefinition = pageDefinition;

        addChild(ReportElementInfoFactory.getInstance().getDataSetsReportElementInfo().createReportElement());
        addChild(reportFunctionsElement);

        addChild(pageHeaderBand);
        addChild(reportHeaderBand);
        addChild(itemBand);
        addChild(reportFooterBand);
        addChild(pageFooterBand);
        addChild(noDataBand);
        addChild(watermarkBand);

        addChild(reportGroups);

        horizontalLinealModel = new LinealModel(this);
        defaultLocale = Locale.getDefault();

        setName("Report");

        useMaxCharBounds = true;

        reportConfiguration = new ReportConfiguration();
    }


    public void addChild(@NotNull final ReportElement child)
    {
        if (child instanceof BandToplevelReportElement)
        {
            BandToplevelReportElement bandToplevelReportElement = (BandToplevelReportElement) child;
            if (bandToplevelReportElement.getBandToplevelType() == BandToplevelType.PAGE_HEADER)
            {
                uninstallElement(pageHeaderBand);
                pageHeaderBand = (BandToplevelPageReportElement) bandToplevelReportElement;
            }
            else if (bandToplevelReportElement.getBandToplevelType() == BandToplevelType.REPORT_HEADER)
            {
                uninstallElement(reportHeaderBand);
                reportHeaderBand = bandToplevelReportElement;
            }
            else if (bandToplevelReportElement.getBandToplevelType() == BandToplevelType.ITEM_BAND)
            {
                uninstallElement(itemBand);
                itemBand = (BandToplevelItemReportElement) bandToplevelReportElement;
            }
            else if (bandToplevelReportElement.getBandToplevelType() == BandToplevelType.REPORT_FOOTER)
            {
                uninstallElement(reportFooterBand);
                reportFooterBand = bandToplevelReportElement;
            }
            else if (bandToplevelReportElement.getBandToplevelType() == BandToplevelType.PAGE_FOOTER)
            {
                uninstallElement(pageFooterBand);
                pageFooterBand = (BandToplevelPageReportElement) bandToplevelReportElement;
            }
            else if (bandToplevelReportElement.getBandToplevelType() == BandToplevelType.WATERMARK)
            {
                uninstallElement(watermarkBand);
                watermarkBand = bandToplevelReportElement;
            }
            else if (bandToplevelReportElement.getBandToplevelType() == BandToplevelType.NO_DATA_BAND)
            {
                uninstallElement(noDataBand);
                noDataBand = bandToplevelReportElement;
            }
            else
            {
                throw new IllegalArgumentException("wrong type: " + child);
            }
        }
        else if (child instanceof ReportFunctionsElement)
        {
            uninstallElement(reportFunctionsElement);
            reportFunctionsElement = (ReportFunctionsElement) child;
        }
        else if (child instanceof DataSetsReportElement)
        {
            uninstallElement(dataSetsReportElement);
            dataSetsReportElement = (DataSetsReportElement) child;
        }
        else if (child instanceof ReportGroups)
        {
            uninstallElement(reportGroups);
            reportGroups = (ReportGroups) child;
        }
        else if (child instanceof SubReportDataElement)
        {
            //ok, subreport makes necessary adjustments
        }
        else
        {
            throw new IllegalArgumentException("wrong type: " + child);
        }
        super.addChild(child);
    }


    private void uninstallElement(@Nullable ReportElement reportElement)
    {
        if (reportElement != null)
        {
            //reportElement.setParent(null);
            removeChild(reportElement);
        }
    }


    @NotNull
    public Report getReport()
    {
        return this;
    }


    @NotNull
    public LinealModel getHorizontalLinealModel()
    {
        return horizontalLinealModel;
    }


    @NotNull
    public ReportFunctionsElement getReportFunctions()
    {
        return reportFunctionsElement;
    }


    @NotNull
    public BandToplevelReportElement getReportHeaderBand()
    {
        return reportHeaderBand;
    }


    @NotNull
    public BandToplevelReportElement getReportFooterBand()
    {
        return reportFooterBand;
    }


    @NotNull
    public BandToplevelPageReportElement getPageHeaderBand()
    {
        return pageHeaderBand;
    }


    @NotNull
    public BandToplevelPageReportElement getPageFooterBand()
    {
        return pageFooterBand;
    }


    @NotNull
    public ReportGroups getReportGroups()
    {
        return reportGroups;
    }


    @NotNull
    public BandToplevelItemReportElement getItemBand()
    {
        return itemBand;
    }


    @NotNull
    public BandToplevelReportElement getWatermarkBand()
    {
        return watermarkBand;
    }


    @NotNull
    public BandToplevelReportElement getNoDataBand()
    {
        return noDataBand;
    }


    @NotNull
    public PageDefinition getPageDefinition()
    {
        return pageDefinition;
    }


    public void setPageDefinition(@NotNull final PageDefinition pageDefinition)
    {
        //noinspection ConstantConditions
        if (pageDefinition == null)
        {
            throw new IllegalArgumentException("pageDefinition must not be null");
        }

        final PageDefinition oldPageDefinition = this.pageDefinition;
        this.pageDefinition = pageDefinition;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.PAGE_DEFINITION);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setPageDefinition(oldPageDefinition);
                }


                public void redo()
                {
                    setPageDefinition(pageDefinition);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.PAGE_DEFINITION, oldPageDefinition, pageDefinition);
    }


    @NotNull
    public DataSetsReportElement getDataSetsReportElement()
    {
        return dataSetsReportElement;
    }


    @NotNull
    public Locale getDefaultLocale()
    {
        return defaultLocale;
    }


    public void setDefaultLocale(@NotNull final Locale defaultLocale)
    {
        //noinspection ConstantConditions
        if (defaultLocale == null)
        {
            throw new IllegalArgumentException("defaultLocale must not be null");
        }

        final Locale oldDefaultLocale = this.defaultLocale;
        this.defaultLocale = defaultLocale;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.DEFAULT_LOCALE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setDefaultLocale(oldDefaultLocale);
                }


                public void redo()
                {
                    setDefaultLocale(defaultLocale);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.DEFAULT_LOCALE, oldDefaultLocale, defaultLocale);
    }


    @Nullable
    public URL getResourceBundleClasspath()
    {
        return resourceBundleClasspath;
    }


    public void setResourceBundleClasspath(@Nullable final URL resourceBundleClasspath)
    {
        final URL oldResourceBundleClasspath = this.resourceBundleClasspath;
        this.resourceBundleClasspath = resourceBundleClasspath;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.RESOURCE_BUNDLE_CLASSPATH);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setResourceBundleClasspath(oldResourceBundleClasspath);
                }


                public void redo()
                {
                    setResourceBundleClasspath(resourceBundleClasspath);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.RESOURCE_BUNDLE_CLASSPATH, oldResourceBundleClasspath, resourceBundleClasspath);
    }


    public boolean isUseMaxCharBounds()
    {
        return useMaxCharBounds;
    }


    public void setUseMaxCharBounds(final boolean useMaxCharBounds)
    {
        final boolean oldUseMaxCharBounds = this.useMaxCharBounds;
        this.useMaxCharBounds = useMaxCharBounds;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.USE_MAX_CHAR_BOUNDS);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setUseMaxCharBounds(oldUseMaxCharBounds);
                }


                public void redo()
                {
                    setUseMaxCharBounds(useMaxCharBounds);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.USE_MAX_CHAR_BOUNDS, oldUseMaxCharBounds, useMaxCharBounds);
    }


    @NotNull
    public ReportConfiguration getReportConfiguration()
    {
        return reportConfiguration;
    }


    public void setReportConfiguration(@NotNull final ReportConfiguration reportConfiguration)
    {
        //noinspection ConstantConditions
        if (reportConfiguration == null)
        {
            throw new IllegalArgumentException("reportConfiguration must not be null");
        }

        final ReportConfiguration oldReportConfiguration = this.reportConfiguration;
        this.reportConfiguration = reportConfiguration;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.REPORT_CONFIGURATION);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setReportConfiguration(oldReportConfiguration);
                }


                public void redo()
                {
                    setReportConfiguration(reportConfiguration);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.REPORT_CONFIGURATION, oldReportConfiguration, reportConfiguration);

    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        Object newParent = reportVisitor.visit(parent, this);

        Object oldContext = reportVisitor.getCurrentContext();
        reportVisitor.setCurrentContext(newParent);

        ArrayList<ReportElement> children = new ArrayList<ReportElement>(getChildren());
        for (ReportElement reportElement : children)
        {
            reportElement.accept(newParent, reportVisitor);
        }

        reportVisitor.setCurrentContext(oldContext);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.DEFAULT_LOCALE, ObjectConverterFactory.getInstance().getLocaleConverter().getString(defaultLocale));

        URL rbc = resourceBundleClasspath;
        if (rbc != null)
        {
            xmlWriter.writeProperty(PropertyKeys.RESOURCE_BUNDLE_CLASSPATH, ObjectConverterFactory.getInstance().getURLConverter(xmlContext).getString(rbc));
        }

        xmlWriter.startElement(PropertyKeys.HORIZONTAL_LINEAL_MODEL);
        horizontalLinealModel.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.HORIZONTAL_LINEAL_MODEL);

        xmlWriter.startElement(PropertyKeys.PAGE_DEFINITION);
        pageDefinition.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.PAGE_DEFINITION);

        xmlWriter.startElement(PropertyKeys.REPORT_CONFIGURATION);
        reportConfiguration.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.REPORT_CONFIGURATION);

        xmlWriter.writeProperty(PropertyKeys.USE_MAX_CHAR_BOUNDS, String.valueOf(useMaxCharBounds));
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode child, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, child, xmlContext);

        if (XMLConstants.PROPERTY.equals(child.getRawName()) && PropertyKeys.DEFAULT_LOCALE.equals(child.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            defaultLocale = ObjectConverterFactory.getInstance().getLocaleConverter().getObject(XMLUtils.readProperty(PropertyKeys.DEFAULT_LOCALE, child));
        }
        else if (XMLConstants.PROPERTY.equals(child.getRawName()) && PropertyKeys.RESOURCE_BUNDLE_CLASSPATH.equals(child.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            resourceBundleClasspath = ObjectConverterFactory.getInstance().getURLConverter(xmlContext).getObject(XMLUtils.readProperty(PropertyKeys.RESOURCE_BUNDLE_CLASSPATH, child));
        }
        else if (PropertyKeys.HORIZONTAL_LINEAL_MODEL.equals(child.getRawName()))
        {
            horizontalLinealModel = new LinealModel(getReport());
            horizontalLinealModel.readObject(child, xmlContext);
        }
        else if (PropertyKeys.PAGE_DEFINITION.equals(child.getRawName()))
        {
            pageDefinition = new PageDefinition(PageFormatPreset.A4, PageOrientation.PORTRAIT, 20, 20, 20, 20);
            pageDefinition.readObject(child, xmlContext);
        }
        else if (PropertyKeys.REPORT_CONFIGURATION.equals(child.getRawName()))
        {
            reportConfiguration = new ReportConfiguration();
            reportConfiguration.readObject(child, xmlContext);
        }
        else if (XMLConstants.PROPERTY.equals(child.getRawName()) && PropertyKeys.USE_MAX_CHAR_BOUNDS.equals(child.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            useMaxCharBounds = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.USE_MAX_CHAR_BOUNDS, child));
        }
    }
}
