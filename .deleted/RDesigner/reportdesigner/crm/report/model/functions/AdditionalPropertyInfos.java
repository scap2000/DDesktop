package org.pentaho.reportdesigner.crm.report.model.functions;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;

import java.util.HashMap;

/**
 * User: Martin
 * Date: 26.01.2007
 * Time: 13:09:10
 */
public class AdditionalPropertyInfos
{
    public static void initExternalPropertyInfos(@NotNull HashMap<String, PropertyInfo> propertyInfos)
    {
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.antiAlias", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.backgroundColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.backgroundImage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.borderColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.categoricalItemLabelRotation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.categoricalLabelDateFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.categoricalLabelDecimalFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.categoricalLabelFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.categoryAxisLabel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.chartDirectory", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.chartFile", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.chartHeight", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.chartSectionOutline", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.chartUrlMask", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.chartWidth", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.dataSource", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 2));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.dependencyLevel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.drawLegendBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.horizontal", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.labelFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.labelRotation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.legendFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.legendLocation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.maxCategoryLabelWidthRatio", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.name", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.noDataMessage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.preserve", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.returnFileNameOnly", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.returnImageReference", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.seriesColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.session", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.showBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.showGridlines", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.showLegend", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.stacked", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.threeD", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.title", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 3));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.titleFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.useDrawable", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression.valueAxisLabel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS

        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.antiAlias", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.backgroundColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.backgroundImage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.borderColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.categoricalItemLabelRotation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.categoricalLabelDateFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.categoricalLabelDecimalFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.categoricalLabelFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.categoryAxisLabel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.chartDirectory", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.chartFile", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.chartHeight", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.chartSectionOutline", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.chartUrlMask", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.chartWidth", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.dataSource", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 2));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.dependencyLevel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.drawBarOutline", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.drawLegendBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.horizontal", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.labelFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.labelRotation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.legendFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.legendLocation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.maxBarWidth", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.maxCategoryLabelWidthRatio", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.name", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.noDataMessage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.preserve", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.returnFileNameOnly", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.returnImageReference", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.seriesColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.session", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.showBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.showGridlines", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.showLegend", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.stacked", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.stackedBarRenderPercentages", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.threeD", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.title", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.titleFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.useDrawable", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression.valueAxisLabel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS

        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.name", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.categoryColumn", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 2));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.valueColumn", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 3));//NON-NLS        
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.preserve", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, 4));//NON-NLS        
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.seriesName", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 5));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.seriesColumn", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 6));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.summaryOnly", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 7));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.resetGroup", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, 2));//NON-NLS 
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.group", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, 3));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.currentGroup", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, 4));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.ignoreColumn", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, 5));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.categoryStartColumn", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, 6));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.dependencyLevel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, 7));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction.generatedReport", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, 8));//NON-NLS

        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction.name", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction.valueColumn", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 2));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction.seriesColumn", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 3));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction.summaryOnly", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 4));//NON-NLS        
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction.resetGroup", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS        
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction.group", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction.currentGroup", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction.preserve", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, 4));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction.dependencyLevel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS

        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.antiAlias", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.backgroundColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.backgroundImage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.borderColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.categoricalItemLabelRotation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.categoricalLabelDateFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.categoricalLabelDecimalFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.categoricalLabelFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.categoryAxisLabel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.chartDirectory", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.chartFile", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.chartHeight", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.chartSectionOutline", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.chartUrlMask", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.chartWidth", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.dataSource", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.dependencyLevel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.drawLegendBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.horizontal", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.labelFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.labelRotation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.legendFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.legendLocation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.lineStyle", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.lineWidth", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.markersVisible", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.maxCategoryLabelWidthRatio", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.name", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.noDataMessage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.preserve", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.returnFileNameOnly", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.returnImageReference", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.session", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.seriesColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.showBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.showGridlines", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.showLegend", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.threeD", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.title", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.titleFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.useDrawable", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression.valueAxisLabel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS

        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.antiAlias", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.backgroundColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.backgroundImage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.borderColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.chartDirectory", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.chartFile", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.chartHeight", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.chartSectionOutline", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.chartUrlMask", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.chartWidth", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.dataSource", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 2));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.dependencyLevel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.drawLegendBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.labelFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.legendFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.legendLocation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.multipieByRow", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.multipieLabelFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.name", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.noDataMessage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.preserve", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.returnFileNameOnly", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.returnImageReference", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.session", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.seriesColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.showBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.showLegend", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.threeD", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.title", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.titleFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression.useDrawable", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS

        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.antiAlias", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.backgroundColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.backgroundImage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.borderColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.chartDirectory", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.chartFile", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.chartHeight", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.chartSectionOutline", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.chartUrlMask", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.chartWidth", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.dataSource", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 2));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.dependencyLevel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.drawLegendBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.explodePct", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.explodeSegment", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.ignoreNulls", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.ignoreZeros", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.labelFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.legendFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.legendLocation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.name", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.noDataMessage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.preserve", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.pieLabelFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.pieLegendLabelFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.returnFileNameOnly", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.returnImageReference", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.rotationClockwise", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.session", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.seriesColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.showBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.showLegend", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.threeD", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.title", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.titleFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression.useDrawable", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS

        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.antiAlias", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.backgroundColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.backgroundImage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.borderColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.chartDirectory", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.chartFile", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.chartHeight", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.chartSectionOutline", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.chartUrlMask", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.chartWidth", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.dataSource", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 2));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.dependencyLevel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.drawLegendBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.explodePct", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.explodeSegment", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.ignoreNulls", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.ignoreZeros", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.labelFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.legendFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.legendLocation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.name", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.preserve", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.noDataMessage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.pieLabelFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.pieLegendLabelFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.returnFileNameOnly", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.returnImageReference", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.rotationClockwise", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.session", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.seriesColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.showBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.showLegend", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.threeD", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.title", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.titleFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression.useDrawable", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS

        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.antiAlias", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.backgroundColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.backgroundImage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.borderColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.categoricalItemLabelRotation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.categoricalLabelDateFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.categoricalLabelDecimalFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.categoricalLabelFormat", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.categoryAxisLabel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.chartDirectory", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.chartFile", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.chartHeight", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.chartSectionOutline", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.chartUrlMask", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.chartWidth", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.dataSource", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 2));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.dependencyLevel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.drawLegendBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.horizontal", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.labelFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.labelRotation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.legendFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.legendLocation", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.maxCategoryLabelWidthRatio", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.name", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, 1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.noDataMessage", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.preserve", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.returnFileNameOnly", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.returnImageReference", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.session", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.seriesColor", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.showBorder", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.showGridlines", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.showLegend", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.threeD", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.title", new PropertyInfo(PropertyKeys.GROUP_REQUIRED, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.titleFont", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.useDrawable", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
        propertyInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions.valueAxisLabel", new PropertyInfo(PropertyKeys.GROUP_OPTIONAL, -1));//NON-NLS
    }


    @SuppressWarnings({"ZeroLengthArrayAllocation"})
    public static void initTreePathInfos(@NotNull HashMap<String, String> treePathInfos)
    {
        treePathInfos.put("org.jfree.report.function.AverageExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ColumnAverageExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ColumnDifferenceExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ColumnDivisionExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ColumnMaximumExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ColumnMinimumExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ColumnMultiplyExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ColumnSumExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.CompareFieldsExpression", "logic");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ConvertToDateExpression", "date");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ConvertToNumberExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.CountDistinctFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.CreateGroupAnchorsFunction", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.CreateHyperLinksFunction", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.DateCutExpression", "date");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ElementColorFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ElementTrafficLightFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ElementVisibilityFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ElementVisibilitySwitchFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.EventMonitorFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.GroupCountFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.HideElementByNameFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.HideElementIfDataAvailableExpression", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.HideNullValuesFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.HidePageBandForTableExportFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.IsEmptyExpression", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.IsNullExpression", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ItemAvgFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ItemColumnQuotientExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ItemCountFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ItemHideFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ItemMaxFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ItemMinFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ItemPercentageFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ItemSumFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.NegativeNumberPaintChangeFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.PageFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.PageItemCountFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.PageItemSumFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.PageOfPagesFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.PageTotalFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.PaintComponentFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.PaintDynamicComponentFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.PercentageExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ShowElementByNameFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.ShowElementIfDataAvailableExpression", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.TextFormatExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.TotalCalculationFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.TotalGroupCountFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.TotalGroupSumFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.TotalGroupSumQuotientFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.TotalGroupSumQuotientPercentFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.TotalItemCountFunction", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.TriggerPageFooterFunction", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.bool.AndExpression", "logic");//NON-NLS
        treePathInfos.put("org.jfree.report.function.bool.IsEmptyDataExpression", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.function.bool.OrExpression", "logic");//NON-NLS
        treePathInfos.put("org.jfree.report.function.date.CompareDateExpression", "date");//NON-NLS
        treePathInfos.put("org.jfree.report.function.date.DateExpression", "date");//NON-NLS
        treePathInfos.put("org.jfree.report.function.date.DateSpanExpression", "date");//NON-NLS
        treePathInfos.put("org.jfree.report.function.date.VariableDateExpression", "date");//NON-NLS
        treePathInfos.put("org.jfree.report.function.numeric.CompareNumberExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.numeric.IsNegativeExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.numeric.IsPositiveExpression", "math");//NON-NLS
        treePathInfos.put("org.jfree.report.function.strings.CapitalizeStringExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.strings.CompareStringExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.strings.MapIndirectExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.strings.MapStringExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.strings.ResourceBundleLookupExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.strings.ResourceMesssageFormatExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.strings.SubStringExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.strings.ToLowerCaseStringExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.strings.ToUpperCaseStringExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.strings.TokenizeStringExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.strings.URLEncodeExpression", "text");//NON-NLS
        treePathInfos.put("org.jfree.report.function.sys.IsExportTypeExpression", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.modules.misc.beanshell.BSHExpression", "misc");//NON-NLS
        treePathInfos.put("org.jfree.report.modules.misc.survey.SurveyScaleExpression", "misc");//NON-NLS
        treePathInfos.put("org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression", "chart");//NON-NLS
        treePathInfos.put("org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression", "chart");//NON-NLS
        treePathInfos.put("org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction", "chart");//NON-NLS
        treePathInfos.put("org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression", "chart");//NON-NLS
        treePathInfos.put("org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression", "chart");//NON-NLS
        treePathInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression", "chart");//NON-NLS
        treePathInfos.put("org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction", "chart");//NON-NLS
        treePathInfos.put("org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression", "chart");//NON-NLS
        treePathInfos.put("org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions", "chart");//NON-NLS
    }


    private AdditionalPropertyInfos()
    {
    }


}
