package org.pentaho.reportdesigner.crm.report.model;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.TableOrder;
import org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression;
import org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction;
import org.pentaho.reportdesigner.crm.report.GraphicsContext;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.model.functions.FunctionGenerator;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfo;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 15:40:13
 */
public class ChartReportElement extends ReportElement
{

    @NotNull
    private ChartType chartType;
    @NotNull
    private ReportFunctionElement dataCollectorFunction;
    @NotNull
    private ReportFunctionElement chartFunction;
    @NotNull
    private JFreeChart chart;


    public ChartReportElement()
    {
        setName("Chart" + FieldNameFactory.getInstance().getNextFreeNumber("Chart"));//NON-NLS

        chartType = ChartType.AREA;
        CategorySetCollectorFunction collectorFunction = new CategorySetCollectorFunction();
        String n = "DataCollector" + FieldNameFactory.getInstance().getNextFreeNumber("DataCollector");//NON-NLS
        collectorFunction.setName(n);
        dataCollectorFunction = ExpressionRegistry.getInstance().createWrapperInstance(collectorFunction);

        AreaChartExpression chartExpression = new AreaChartExpression();
        chartExpression.setName("ChartFunction" + FieldNameFactory.getInstance().getNextFreeNumber("ChartFunction"));//NON-NLS
        chartExpression.setDataSource(n);
        chartFunction = ExpressionRegistry.getInstance().createWrapperInstance(chartExpression);

        refreshSampleChart();
    }


    @NotNull
    public ChartType getChartType()
    {
        return chartType;
    }


    public void setChartType(@NotNull final ChartType chartType)
    {
        //noinspection ConstantConditions
        if (chartType == null)
        {
            throw new IllegalArgumentException("chartType must not be null");
        }

        final ChartType oldChartType = this.chartType;
        this.chartType = chartType;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.CHART_TYPE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setChartType(oldChartType);
                }


                public void redo()
                {
                    setChartType(chartType);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.CHART_TYPE, oldChartType, chartType);

        refreshSampleChart();
    }


    private void refreshSampleChart()
    {
        switch (chartType)
        {
            case AREA:
            {
                chart = ChartFactory.createAreaChart("Area Chart", "Category", "Value", createDataset(), PlotOrientation.VERTICAL, true, false, false);//NON-NLS
                break;
            }
            case BAR:
            {
                chart = ChartFactory.createBarChart("Bar Chart", "Category", "Value", createDataset(), PlotOrientation.VERTICAL, true, false, false);//NON-NLS
                break;
            }
            case LINE:
            {
                chart = ChartFactory.createLineChart("Line Chart", "Category", "Value", createDataset(), PlotOrientation.VERTICAL, true, false, false);//NON-NLS
                break;
            }
            case MULTI_PIE:
            {
                chart = ChartFactory.createMultiplePieChart("Multi Pie Chart", createDataset(), TableOrder.BY_COLUMN, true, false, false);//NON-NLS
                break;
            }
            case PIE:
            {
                chart = ChartFactory.createPieChart("Pie Chart", createPieDataset(), true, false, false);//NON-NLS
                break;
            }
            case RING:
            {
                chart = ChartFactory.createRingChart("Ring Chart", createPieDataset(), true, false, false);//NON-NLS
                break;
            }
            case WATERFALL:
            {
                chart = ChartFactory.createWaterfallChart("Bar Chart", "Category", "Value", createDataset(), PlotOrientation.HORIZONTAL, true, false, false);//NON-NLS
                break;
            }
        }
    }


    @NotNull
    private DefaultPieDataset createPieDataset()
    {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Part 1", 23);//NON-NLS
        dataset.setValue("Part 2", 35);//NON-NLS
        dataset.setValue("Part 3", 42);//NON-NLS
        return dataset;
    }


    @NotNull
    private CategoryDataset createDataset()
    {
        String series1 = "First";//NON-NLS
        String series2 = "Second";//NON-NLS
        String series3 = "Third";//NON-NLS
        String category1 = "Category 1";//NON-NLS
        String category2 = "Category 2";//NON-NLS
        String category3 = "Category 3";//NON-NLS
        String category4 = "Category 4";//NON-NLS
        String category5 = "Category 5";//NON-NLS
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1D, series1, category1);
        dataset.addValue(5D, series1, category2);
        dataset.addValue(4D, series1, category3);
        dataset.addValue(8D, series1, category4);
        dataset.addValue(7D, series1, category5);

        dataset.addValue(3D, series2, category1);
        dataset.addValue(4D, series2, category2);
        dataset.addValue(3D, series2, category3);
        dataset.addValue(5D, series2, category4);
        dataset.addValue(4D, series2, category5);

        dataset.addValue(1D, series3, category1);
        dataset.addValue(3D, series3, category2);
        dataset.addValue(2D, series3, category3);
        dataset.addValue(3D, series3, category4);
        dataset.addValue(2D, series3, category5);

        return dataset;
    }


    @NotNull
    public ReportFunctionElement getDataCollectorFunction()
    {
        return dataCollectorFunction;
    }


    public void setDataCollectorFunction(@NotNull final ReportFunctionElement dataCollectorFunction)
    {
        //noinspection ConstantConditions
        if (dataCollectorFunction == null)
        {
            throw new IllegalArgumentException("dataCollectorFunction must not be null");
        }

        final ReportFunctionElement oldDataCollectorFunction = this.dataCollectorFunction;
        this.dataCollectorFunction = dataCollectorFunction;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.DATA_COLLECTOR_FUNCTION);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setDataCollectorFunction(oldDataCollectorFunction);
                }


                public void redo()
                {
                    setDataCollectorFunction(dataCollectorFunction);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.DATA_COLLECTOR_FUNCTION, oldDataCollectorFunction, dataCollectorFunction);
    }


    @NotNull
    public ReportFunctionElement getChartFunction()
    {
        return chartFunction;
    }


    public void setChartFunction(@NotNull final ReportFunctionElement chartFunction)
    {
        //noinspection ConstantConditions
        if (chartFunction == null)
        {
            throw new IllegalArgumentException("chartFunction must not be null");
        }

        final ReportFunctionElement oldChartFunction = this.chartFunction;
        this.chartFunction = chartFunction;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.CHART_FUNCTION);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setChartFunction(oldChartFunction);
                }


                public void redo()
                {
                    setChartFunction(chartFunction);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.CHART_FUNCTION, oldChartFunction, chartFunction);
    }


    public void paint(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        Shape origClip = g2d.getClip();
        Color origColor = g2d.getColor();
        Composite origComposite = g2d.getComposite();
        Stroke origStroke = g2d.getStroke();

        Rectangle2D.Double rect = getRectangle();

        g2d.clip(rect);
        paintBackroundAndBorder(g2d);

        g2d.setColor(new Color(240, 240, 240));
        g2d.fill(rect);

        chart.draw(g2d, rect);

        g2d.setStroke(origStroke);
        g2d.setComposite(origComposite);
        g2d.setColor(origColor);
        g2d.setClip(origClip);
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.CHART_TYPE, chartType.name());

        xmlWriter.startElement(PropertyKeys.CHART_FUNCTION);
        String chartTypeName = chartFunction.getClass().getName();
        if (chartTypeName.startsWith(FunctionGenerator.PACKAGE_PREFIX))
        {
            chartTypeName = chartTypeName.substring(FunctionGenerator.PACKAGE_PREFIX.length());
        }
        xmlWriter.writeAttribute(PropertyKeys.TYPE, chartTypeName);
        chartFunction.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.CHART_FUNCTION);

        xmlWriter.startElement(PropertyKeys.DATA_COLLECTOR_FUNCTION);
        String dataTypeName = dataCollectorFunction.getClass().getName();
        if (dataTypeName.startsWith(FunctionGenerator.PACKAGE_PREFIX))
        {
            dataTypeName = dataTypeName.substring(FunctionGenerator.PACKAGE_PREFIX.length());
        }
        xmlWriter.writeAttribute(PropertyKeys.TYPE, dataTypeName);
        dataCollectorFunction.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.DATA_COLLECTOR_FUNCTION);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.CHART_TYPE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            chartType = ChartType.valueOf(XMLUtils.readProperty(PropertyKeys.CHART_TYPE, node));
            refreshSampleChart();
        }
        else if (PropertyKeys.CHART_FUNCTION.equals(node.getRawName()))
        {
            ReportElementInfo reportElementInfo = getReportElementInfo(expressions, node);
            if (reportElementInfo != null)
            {
                ReportElement reportElement = reportElementInfo.createReportElement();
                reportElement.readObject(node, xmlContext);
                chartFunction = (ReportFunctionElement) reportElement;
            }
        }
        else if (PropertyKeys.DATA_COLLECTOR_FUNCTION.equals(node.getRawName()))
        {
            ReportElementInfo reportElementInfo = getReportElementInfo(expressions, node);
            if (reportElementInfo != null)
            {
                ReportElement reportElement = reportElementInfo.createReportElement();
                reportElement.readObject(node, xmlContext);
                dataCollectorFunction = (ReportFunctionElement) reportElement;
            }
        }
    }
}
