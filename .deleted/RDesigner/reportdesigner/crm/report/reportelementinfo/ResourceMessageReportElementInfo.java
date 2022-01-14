package org.pentaho.reportdesigner.crm.report.reportelementinfo;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.model.ResourceMessageReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 15:51:58
 */
public class ResourceMessageReportElementInfo extends ReportElementInfo
{
    public ResourceMessageReportElementInfo()
    {
        super(IconLoader.getInstance().getResourceMessageReportElementIcon(), TranslationManager.getInstance().getTranslation("R", "ReportElementInfo.ResourceMessage"));
    }


    @NotNull
    public ResourceMessageReportElement createReportElement()
    {
        ResourceMessageReportElement textFieldReportElement = new ResourceMessageReportElement();
        textFieldReportElement.setMinimumSize(new DoubleDimension(100, 16));
        textFieldReportElement.setRectangle(new Rectangle2D.Double(0, 0, 100, 16));
        return textFieldReportElement;
    }
}
