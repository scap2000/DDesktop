package org.pentaho.reportdesigner.crm.report.properties.renderers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.StyleExpression;
import org.pentaho.reportdesigner.crm.report.model.StyleExpressions;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashSet;
import java.util.List;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 09:15:46
 */
public class StyleExpressionsTableCellRenderer extends DefaultTableCellRenderer
{
    @NotNull
    public Component getTableCellRendererComponent(@NotNull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        try
        {
            if (value instanceof HashSet)
            {
                HashSet hashSet = (HashSet) value;
                if (hashSet.size() > 1)
                {
                    label.setText(TranslationManager.getInstance().getTranslation("R", "Property.MultipleValues"));
                }

                if (hashSet.size() == 1)
                {
                    StyleExpressions styleExpressions = (StyleExpressions) hashSet.iterator().next();
                    List<StyleExpression> list = styleExpressions.getStyleExpressions();
                    boolean empty = list.isEmpty();
                    if (empty)
                    {
                        label.setText(TranslationManager.getInstance().getTranslation("R", "StyleExpressionTableCellRenderer.NoExpressionsDefined"));
                    }
                    else
                    {
                        label.setText(list.get(0).toString());
                    }
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
