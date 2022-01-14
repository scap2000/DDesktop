package org.pentaho.reportdesigner.crm.report.reportelementinfo;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.model.AnchorFieldReportElement;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;

import java.awt.geom.Rectangle2D;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 15:51:58
 */
public class AnchorFieldReportElementInfo extends ReportElementInfo
{

    public AnchorFieldReportElementInfo()
    {
        super(IconLoader.getInstance().getAnchorFieldReportElementIcon(), TranslationManager.getInstance().getTranslation("R", "ReportElementInfo.AnchorField"));
    }


    @NotNull
    public AnchorFieldReportElement createReportElement()
    {
        AnchorFieldReportElement anchorFieldReportElement = new AnchorFieldReportElement();
        anchorFieldReportElement.setMinimumSize(new DoubleDimension(100, 16));
        anchorFieldReportElement.setRectangle(new Rectangle2D.Double(0, 0, 100, 16));
        return anchorFieldReportElement;
    }
}
