package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.model.ChartReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetReportElement;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 16.03.2007
 * Time: 18:48:47
 */
public class ReportFieldUtilities
{
    private ReportFieldUtilities()
    {
    }


    @NotNull
    public static HashSet<FieldDefinition> getFieldDefinitions(@NotNull ReportElement reportElement)
    {
        //noinspection ConstantConditions
        if (reportElement == null)
        {
            throw new IllegalArgumentException("reportElement must not be null");
        }

        HashSet<FieldDefinition> hashSet = new HashSet<FieldDefinition>();
        collectFieldDefinitions(reportElement, hashSet);
        return hashSet;
    }


    private static void collectFieldDefinitions(@NotNull ReportElement reportElement, @NotNull HashSet<FieldDefinition> fieldDefinitions)
    {
        if (reportElement instanceof DataSetReportElement)
        {
            DataSetReportElement dataSetReportElement = (DataSetReportElement) reportElement;
            HashSet<String> hashSet = dataSetReportElement.getDefinedFields();
            for (String fieldName : hashSet)
            {
                fieldDefinitions.add(new FieldDefinition(dataSetReportElement, fieldName));
            }
        }
        else if (reportElement instanceof ReportFunctionElement)
        {
            ReportFunctionElement functionElement = (ReportFunctionElement) reportElement;
            if (functionElement.getName().length() > 0)
            {
                fieldDefinitions.add(new FieldDefinition(functionElement, functionElement.getName()));
            }
        }
        else if (reportElement instanceof ChartReportElement)
        {
            ChartReportElement functionElement = (ChartReportElement) reportElement;
            collectFieldDefinitions(functionElement.getChartFunction(), fieldDefinitions);
            collectFieldDefinitions(functionElement.getDataCollectorFunction(), fieldDefinitions);
        }

        ArrayList<ReportElement> list = reportElement.getChildren();
        for (ReportElement element : list)
        {
            collectFieldDefinitions(element, fieldDefinitions);
        }
    }
}
