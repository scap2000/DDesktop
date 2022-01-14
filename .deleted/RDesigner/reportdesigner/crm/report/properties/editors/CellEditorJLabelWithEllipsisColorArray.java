package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 07:24:52
 */
public class CellEditorJLabelWithEllipsisColorArray extends CellEditorJLabelWithEllipsis<Color[]>
{
    public CellEditorJLabelWithEllipsisColorArray()
    {
    }


    @Nullable
    public String convertToText(@Nullable Color[] array)
    {
        if (array == null)
        {
            return null;
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

        return sb.toString();
    }
}
