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
package org.pentaho.reportdesigner.crm.report.reportelementinfo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.JDBCDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertiesDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.sampledb.SampleDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.staticfactory.StaticFactoryDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.AnchorFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelType;
import org.pentaho.reportdesigner.crm.report.model.ChartReportElement;
import org.pentaho.reportdesigner.crm.report.model.DateFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.DrawableFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.EllipseReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageURLFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.LabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.LineReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.NumberFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.RectangleReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionsElement;
import org.pentaho.reportdesigner.crm.report.model.ReportGroup;
import org.pentaho.reportdesigner.crm.report.model.ReportGroups;
import org.pentaho.reportdesigner.crm.report.model.ResourceFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ResourceLabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.ResourceMessageReportElement;
import org.pentaho.reportdesigner.crm.report.model.StaticImageReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.model.SubReportDataElement;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;

import java.util.HashMap;

/**
 * User: Martin
 * Date: 09.01.2006
 * Time: 07:54:59
 */
public class ReportElementInfoFactory
{
    @NotNull
    private static final ReportElementInfoFactory instance = new ReportElementInfoFactory();


    @NotNull
    public static ReportElementInfoFactory getInstance()
    {
        return instance;
    }


    @NotNull
    private NullReportElementInfo nullReportElementInfo;

    @NotNull
    private ReportReportElementInfo reportReportElementInfo;

    @NotNull
    private BandToplevelReportElementInfo[] bandToplevelReportElementInfos = new BandToplevelReportElementInfo[BandToplevelType.values().length];

    @NotNull
    private BandReportElementInfo bandReportElementInfo;
    @NotNull
    private LabelReportElementInfo labelReportElementInfo;
    @NotNull
    private TextFieldReportElementInfo textFieldReportElementInfo;
    @NotNull
    private MessageFieldReportElementInfo messageFieldReportElementInfo;
    @NotNull
    private NumberFieldReportElementInfo numberFieldReportElementInfo;
    @NotNull
    private ResourceFieldReportElementInfo resourceFieldReportElementInfo;
    @NotNull
    private ResourceLabelReportElementInfo resourceLabelReportElementInfo;
    @NotNull
    private ResourceMessageReportElementInfo resourceMessageReportElementInfo;
    @NotNull
    private DateFieldReportElementInfo dateFieldReportElementInfo;

    @NotNull
    private ReportFunctionsElementInfo reportFunctionsElementInfo;

    @NotNull
    private ReportGroupElementInfo reportGroupElementInfo;
    @NotNull
    private ReportGroupsElementInfo reportGroupsElementInfo;

    @NotNull
    private LineReportElementInfo lineReportElementInfo;
    @NotNull
    private StaticImageReportElementInfo staticImageReportElementInfo;
    @NotNull
    private ImageFieldReportElementInfo imageFieldReportElementInfo;
    @NotNull
    private ImageURLFieldReportElementInfo imageURLFieldReportElementInfo;
    @NotNull
    private AnchorFieldReportElementInfo anchorFieldReportElementInfo;
    @NotNull
    private RectangleReportElementInfo rectangelReportElementInfo;
    @NotNull
    private EllipseReportElementInfo ellipseReportElementInfo;

    @NotNull
    private DataSetsReportElementInfo dataSetsReportElementInfo;

    @NotNull
    private PropertiesDataSetReportElementInfo propertiesDataSetReportElementInfo;
    @NotNull
    private JDBCDataSetReportElementInfo jdbcDataSetReportElementInfo;
    @NotNull
    private MultiDataSetReportElementInfo multiDataSetReportElementInfo;
    @NotNull
    private SubReportDataElementInfo subReportDataElementInfo;
    @NotNull
    private StaticFactoryDataSetReportElementInfo staticFactoryDataSetReportElementInfo;
    @NotNull
    private SampleDataSetReportElementInfo sampleDataSetReportElementInfo;

    @NotNull
    private DrawableFieldReportElementInfo drawableFieldReportElementInfo;
    @NotNull
    private ChartReportElementInfo chartReportElementInfo;

    @NotNull
    private SubReportElementInfo subReportElementInfo;

    @NotNull
    private HashMap<Class, ReportElementInfo> reportElementInfoMap;


    private ReportElementInfoFactory()
    {
        reportElementInfoMap = new HashMap<Class, ReportElementInfo>();

        nullReportElementInfo = new NullReportElementInfo();
        reportReportElementInfo = new ReportReportElementInfo();

        for (int i = 0; i < BandToplevelType.values().length; i++)
        {
            BandToplevelType bandToplevelType = BandToplevelType.values()[i];
            bandToplevelReportElementInfos[i] = new BandToplevelReportElementInfo(bandToplevelType);
        }

        bandToplevelReportElementInfos[BandToplevelType.ITEM_BAND.ordinal()] = new BandToplevelItemReportElementInfo(BandToplevelType.ITEM_BAND);

        bandToplevelReportElementInfos[BandToplevelType.GROUP_HEADER.ordinal()] = new BandToplevelGroupReportElementInfo(BandToplevelType.GROUP_HEADER);
        bandToplevelReportElementInfos[BandToplevelType.GROUP_FOOTER.ordinal()] = new BandToplevelGroupReportElementInfo(BandToplevelType.GROUP_FOOTER);

        bandToplevelReportElementInfos[BandToplevelType.PAGE_HEADER.ordinal()] = new BandToplevelPageReportElementInfo(BandToplevelType.PAGE_HEADER);
        bandToplevelReportElementInfos[BandToplevelType.PAGE_FOOTER.ordinal()] = new BandToplevelPageReportElementInfo(BandToplevelType.PAGE_FOOTER);

        bandReportElementInfo = new BandReportElementInfo();
        labelReportElementInfo = new LabelReportElementInfo();
        textFieldReportElementInfo = new TextFieldReportElementInfo();
        messageFieldReportElementInfo = new MessageFieldReportElementInfo();
        numberFieldReportElementInfo = new NumberFieldReportElementInfo();
        resourceFieldReportElementInfo = new ResourceFieldReportElementInfo();
        resourceLabelReportElementInfo = new ResourceLabelReportElementInfo();
        resourceMessageReportElementInfo = new ResourceMessageReportElementInfo();
        dateFieldReportElementInfo = new DateFieldReportElementInfo();

        reportFunctionsElementInfo = new ReportFunctionsElementInfo();

        reportGroupElementInfo = new ReportGroupElementInfo();
        reportGroupsElementInfo = new ReportGroupsElementInfo();

        lineReportElementInfo = new LineReportElementInfo();
        staticImageReportElementInfo = new StaticImageReportElementInfo();
        imageFieldReportElementInfo = new ImageFieldReportElementInfo();
        imageURLFieldReportElementInfo = new ImageURLFieldReportElementInfo();
        anchorFieldReportElementInfo = new AnchorFieldReportElementInfo();
        rectangelReportElementInfo = new RectangleReportElementInfo();
        ellipseReportElementInfo = new EllipseReportElementInfo();
        chartReportElementInfo = new ChartReportElementInfo();

        dataSetsReportElementInfo = new DataSetsReportElementInfo();

        propertiesDataSetReportElementInfo = new PropertiesDataSetReportElementInfo();
        jdbcDataSetReportElementInfo = new JDBCDataSetReportElementInfo();
        multiDataSetReportElementInfo = new MultiDataSetReportElementInfo();
        staticFactoryDataSetReportElementInfo = new StaticFactoryDataSetReportElementInfo();
        sampleDataSetReportElementInfo = new SampleDataSetReportElementInfo();
        subReportDataElementInfo = new SubReportDataElementInfo();

        drawableFieldReportElementInfo = new DrawableFieldReportElementInfo();

        subReportElementInfo = new SubReportElementInfo();

        register(ReportElement.class, nullReportElementInfo);
        register(Report.class, reportReportElementInfo);
        register(SubReport.class, subReportElementInfo);
        register(BandReportElement.class, bandReportElementInfo);
        register(LabelReportElement.class, labelReportElementInfo);
        register(TextFieldReportElement.class, textFieldReportElementInfo);
        register(MessageFieldReportElement.class, messageFieldReportElementInfo);
        register(NumberFieldReportElement.class, numberFieldReportElementInfo);
        register(ResourceFieldReportElement.class, resourceFieldReportElementInfo);
        register(ResourceLabelReportElement.class, resourceLabelReportElementInfo);
        register(ResourceMessageReportElement.class, resourceMessageReportElementInfo);
        register(DateFieldReportElement.class, dateFieldReportElementInfo);
        register(ReportFunctionsElement.class, reportFunctionsElementInfo);
        register(ReportFunctionElement.class, reportFunctionsElementInfo);
        register(ReportGroup.class, reportGroupElementInfo);
        register(ReportGroups.class, reportGroupsElementInfo);
        register(LineReportElement.class, lineReportElementInfo);
        register(StaticImageReportElement.class, staticImageReportElementInfo);
        register(ImageFieldReportElement.class, imageFieldReportElementInfo);
        register(ImageURLFieldReportElement.class, imageURLFieldReportElementInfo);
        register(AnchorFieldReportElement.class, anchorFieldReportElementInfo);
        register(RectangleReportElement.class, rectangelReportElementInfo);
        register(EllipseReportElement.class, ellipseReportElementInfo);
        register(DataSetsReportElement.class, dataSetsReportElementInfo);
        register(PropertiesDataSetReportElement.class, propertiesDataSetReportElementInfo);
        register(JDBCDataSetReportElement.class, jdbcDataSetReportElementInfo);
        register(MultiDataSetReportElement.class, multiDataSetReportElementInfo);
        register(SubReportDataElement.class, subReportDataElementInfo);
        register(StaticFactoryDataSetReportElement.class, staticFactoryDataSetReportElementInfo);
        register(SampleDataSetReportElement.class, sampleDataSetReportElementInfo);
        register(DrawableFieldReportElement.class, drawableFieldReportElementInfo);
        register(SubReportElement.class, subReportElementInfo);
        register(ChartReportElement.class, chartReportElementInfo);
    }


    @NotNull
    public ReportElementInfo getReportElementInfo(@NotNull ReportElement reportElement)
    {
        if (reportElement instanceof BandToplevelReportElement)
        {
            BandToplevelReportElement bandToplevelReportElement = (BandToplevelReportElement) reportElement;
            return bandToplevelReportElementInfos[bandToplevelReportElement.getBandToplevelType().ordinal()];
        }
        else
        {
            Class clazz = reportElement.getClass();
            @Nullable
            ReportElementInfo reportElementInfo;

            while (clazz != null)
            {
                reportElementInfo = reportElementInfoMap.get(clazz);
                if (reportElementInfo == null)
                {
                    clazz = clazz.getSuperclass();
                }
                else
                {
                    return reportElementInfo;
                }
            }

            reportElementInfo = nullReportElementInfo;

            return reportElementInfo;
        }
    }


    private void register(@NotNull Class reportElementClass, @NotNull ReportElementInfo reportElementInfo)
    {
        reportElementInfoMap.put(reportElementClass, reportElementInfo);
    }


    @NotNull
    public ReportFunctionsElementInfo getReportFunctionsElementInfo()
    {
        return reportFunctionsElementInfo;
    }


    @NotNull
    public ReportReportElementInfo getReportReportElementInfo()
    {
        return reportReportElementInfo;
    }


    @NotNull
    public BandToplevelReportElementInfo getBandToplevelReportElementInfo(@NotNull BandToplevelType bandToplevelType)
    {
        return bandToplevelReportElementInfos[bandToplevelType.ordinal()];
    }


    @NotNull
    public BandReportElementInfo getBandReportElementInfo()
    {
        return bandReportElementInfo;
    }


    @NotNull
    public LabelReportElementInfo getLabelReportElementInfo()
    {
        return labelReportElementInfo;
    }


    @NotNull
    public TextFieldReportElementInfo getTextFieldReportElementInfo()
    {
        return textFieldReportElementInfo;
    }


    @NotNull
    public MessageFieldReportElementInfo getMessageFieldReportElementInfo()
    {
        return messageFieldReportElementInfo;
    }


    @NotNull
    public ResourceFieldReportElementInfo getResourceFieldReportElementInfo()
    {
        return resourceFieldReportElementInfo;
    }


    @NotNull
    public ResourceLabelReportElementInfo getResourceLabelReportElementInfo()
    {
        return resourceLabelReportElementInfo;
    }


    @NotNull
    public ResourceMessageReportElementInfo getResourceMessageReportElementInfo()
    {
        return resourceMessageReportElementInfo;
    }


    @NotNull
    public DateFieldReportElementInfo getDateFieldReportElementInfo()
    {
        return dateFieldReportElementInfo;
    }


    @NotNull
    public ReportFunctionElementInfo getReportFunctionElementInfo(@NotNull String className)
    {
        return new ReportFunctionElementInfo(className);
    }


    @NotNull
    public ReportGroupElementInfo getReportGroupElementInfo()
    {
        return reportGroupElementInfo;
    }


    @NotNull
    public ReportGroupsElementInfo getReportGroupsElementInfo()
    {
        return reportGroupsElementInfo;
    }


    @NotNull
    public LineReportElementInfo getLineReportElementInfo()
    {
        return lineReportElementInfo;
    }


    @NotNull
    public StaticImageReportElementInfo getStaticImageReportElementInfo()
    {
        return staticImageReportElementInfo;
    }


    @NotNull
    public NumberFieldReportElementInfo getNumberFieldReportElementInfo()
    {
        return numberFieldReportElementInfo;
    }


    @NotNull
    public DataSetsReportElementInfo getDataSetsReportElementInfo()
    {
        return dataSetsReportElementInfo;
    }


    @NotNull
    public PropertiesDataSetReportElementInfo getPropertiesDataSetReportElementInfo()
    {
        return propertiesDataSetReportElementInfo;
    }


    @NotNull
    public JDBCDataSetReportElementInfo getJDBCDataSetReportElementInfo()
    {
        return jdbcDataSetReportElementInfo;
    }


    @NotNull
    public SampleDataSetReportElementInfo getSampleDataSetReportElementInfo()
    {
        return sampleDataSetReportElementInfo;
    }


    @NotNull
    public ReportElementInfo getRectangleReportElementInfo()
    {
        return rectangelReportElementInfo;
    }


    @NotNull
    public EllipseReportElementInfo getEllipseReportElementInfo()
    {
        return ellipseReportElementInfo;
    }


    @NotNull
    public ImageFieldReportElementInfo getImageFieldReportElementInfo()
    {
        return imageFieldReportElementInfo;
    }


    @NotNull
    public ImageURLFieldReportElementInfo getImageURLFieldReportElementInfo()
    {
        return imageURLFieldReportElementInfo;
    }


    @NotNull
    public AnchorFieldReportElementInfo getAnchorFieldReportElementInfo()
    {
        return anchorFieldReportElementInfo;
    }


    @NotNull
    public DrawableFieldReportElementInfo getDrawableFieldReportElementInfo()
    {
        return drawableFieldReportElementInfo;
    }


    @NotNull
    public SubReportElementInfo getSubReportElementInfo()
    {
        return subReportElementInfo;
    }


    @NotNull
    public ChartReportElementInfo getChartReportElementInfo()
    {
        return chartReportElementInfo;
    }


    @NotNull
    public SubReportDataElementInfo getSubReportDataElementInfo()
    {
        return subReportDataElementInfo;
    }
}
