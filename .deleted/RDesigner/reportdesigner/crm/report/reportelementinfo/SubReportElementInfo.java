package org.pentaho.reportdesigner.crm.report.reportelementinfo;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 15:51:58
 */
public class SubReportElementInfo extends ReportElementInfo
{
    @NotNull
    private Rectangle2D.Double rectangle;


    public SubReportElementInfo()
    {
        super(IconLoader.getInstance().getSubReportElementIcon(), TranslationManager.getInstance().getTranslation("R", "ReportElementInfo.SubReport"));
        rectangle = new Rectangle2D.Double(0, 0, 100, 20);
    }


    @NotNull
    public Rectangle2D.Double getRectangle()
    {
        return rectangle;
    }


    public void setRectangle(@NotNull Rectangle2D.Double rectangle)
    {
        this.rectangle = rectangle;
    }


    @NotNull
    public SubReportElement createReportElement()
    {
        SubReportElement subReportElement = new SubReportElement();
        subReportElement.setMinimumSize(new DoubleDimension(rectangle.getWidth(), rectangle.getHeight()));
        subReportElement.setRectangle(new Rectangle2D.Double(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight()));
        return subReportElement;
    }
}
