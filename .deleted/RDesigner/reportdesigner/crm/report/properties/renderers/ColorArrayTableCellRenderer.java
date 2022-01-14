package org.pentaho.reportdesigner.crm.report.properties.renderers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
public class ColorArrayTableCellRenderer extends DefaultTableCellRenderer
{
    @NotNull
    private static final Color[] EMPTY_COLOR_ARRAY = new Color[0];


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
                    Color[] array = (Color[]) hashSet.iterator().next();
                    if (array == null)
                    {
                        array = EMPTY_COLOR_ARRAY;
                    }

                    StringBuilder sb = new StringBuilder("[");
                    for (int i = 0; i < array.length; i++)
                    {
                        Color color = array[i];
                        sb.append(color.getRed()).append("; ").append(color.getGreen()).append("; ").append(color.getBlue());
                        if (i < array.length - 1)
                        {
                            sb.append(", ");
                        }
                    }
                    sb.append("]");
                    label.setText(sb.toString());
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
