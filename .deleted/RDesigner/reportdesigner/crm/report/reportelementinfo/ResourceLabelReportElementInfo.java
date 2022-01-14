package org.pentaho.reportdesigner.crm.report.reportelementinfo;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.model.ResourceLabelReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 15:51:58
 */
public class ResourceLabelReportElementInfo extends ReportElementInfo
{
    public ResourceLabelReportElementInfo()
    {
        super(IconLoader.getInstance().getResourceLabelReportElementIcon(), TranslationManager.getInstance().getTranslation("R", "ReportElementInfo.ResourceLabel"));
    }


    @NotNull
    public ResourceLabelReportElement createReportElement()
    {
        ResourceLabelReportElement textFieldReportElement = new ResourceLabelReportElement();
        textFieldReportElement.setMinimumSize(new DoubleDimension(100, 16));
        textFieldReportElement.setRectangle(new Rectangle2D.Double(0, 0, 100, 16));
        return textFieldReportElement;
    }
}
