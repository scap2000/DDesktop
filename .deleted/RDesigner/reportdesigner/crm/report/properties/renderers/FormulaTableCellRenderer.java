package org.pentaho.reportdesigner.crm.report.properties.renderers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.Formula;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashSet;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 09:15:46
 */
public class FormulaTableCellRenderer extends DefaultTableCellRenderer
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
                    Formula formula = (Formula) hashSet.iterator().next();
                    if (formula != null)
                    {
                        label.setText(formula.getText());
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
}
