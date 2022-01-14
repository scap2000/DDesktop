package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.configuration.ReportConfiguration;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 07:24:52
 */
public class CellEditorJLabelWithEllipsisReportConfiguration extends CellEditorJLabelWithEllipsis<ReportConfiguration>
{

    public CellEditorJLabelWithEllipsisReportConfiguration()
    {
    }


    @Nullable
    public String convertToText(@Nullable ReportConfiguration reportConfiguration)
    {
        if (reportConfiguration == null)
        {
            return null;
        }

        if (reportConfiguration.getConfigProperties().isEmpty())
        {
            return TranslationManager.getInstance().getTranslation("R", "Property.ReportConfigurationDefault");
        }
        else
        {
            return TranslationManager.getInstance().getTranslation("R", "Property.ReportConfigurationChanged");
        }
    }

}
