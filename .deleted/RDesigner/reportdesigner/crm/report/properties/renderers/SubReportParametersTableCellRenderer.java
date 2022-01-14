package org.pentaho.reportdesigner.crm.report.properties.renderers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.SubReportParameter;
import org.pentaho.reportdesigner.crm.report.model.SubReportParameters;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 09:15:46
 */
public class SubReportParametersTableCellRenderer extends DefaultTableCellRenderer
{

    @NotNull
    public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setEnabled(table.isEnabled());
        try
        {
            if (value instanceof HashSet)
            {
                HashSet hashSet = (HashSet) value;
                if (hashSet.size() == 1)
                {
                    SubReportParameters subReportParameters = (SubReportParameters) hashSet.iterator().next();
                    if (subReportParameters != null)
                    {
                        String text = createText(subReportParameters);
                        label.setText(text);
                    }
                    else
                    {
                        label.setText(TranslationManager.getInstance().getTranslation("R", "Property.None"));
                        label.setEnabled(false);
                    }
                }
                else if (hashSet.size() > 1)
                {
                    label.setText(TranslationManager.getInstance().getTranslation("R", "Property.MultipleValues"));
                }
            }
        }
        catch (Throwable e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        return label;
    }


    @NotNull
    public static String createText(@Nullable SubReportParameters subReportParameters)
    {
        if (subReportParameters == null)
        {
            return "";
        }

        StringBuilder text = new StringBuilder(20);
        text.append(TranslationManager.getInstance().getTranslation("R", "SubReportParameters.ImportPrefix"));
        if (subReportParameters.isGlobalImport())
        {
            text.append("* ");
        }
        else
        {
            Collection<SubReportParameter> parameters = subReportParameters.getImportParameters().values();
            for (SubReportParameter parameter : parameters)
            {
                text.append(parameter.getKey()).append("->").append(parameter.getValue()).append(" ");
            }
        }

        text.append(", ");

        text.append(TranslationManager.getInstance().getTranslation("R", "SubReportParameters.ExportPrefix"));
        if (subReportParameters.isGlobalExport())
        {
            text.append("* ");
        }
        else
        {
            Collection<SubReportParameter> parameters = subReportParameters.getExportParameters().values();
            for (SubReportParameter parameter : parameters)
            {
                text.append(parameter.getKey()).append("->").append(parameter.getValue()).append(" ");
            }
        }
        return text.toString();
    }
}
