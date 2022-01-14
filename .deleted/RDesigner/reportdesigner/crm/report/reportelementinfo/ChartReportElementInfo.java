package org.pentaho.reportdesigner.crm.report.reportelementinfo;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.model.ChartReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 15:51:58
 */
public class ChartReportElementInfo extends ReportElementInfo
{
    public ChartReportElementInfo()
    {
        super(IconLoader.getInstance().getChartReportElementIcon(), TranslationManager.getInstance().getTranslation("R", "ReportElementInfo.Chart"));
    }


    @NotNull
    public ChartReportElement createReportElement()
    {
        ChartReportElement chartReportElement = new ChartReportElement();
        chartReportElement.setMinimumSize(new DoubleDimension(250, 200));
        chartReportElement.setRectangle(new Rectangle2D.Double(0, 0, 250, 200));
        return chartReportElement;
    }
}
