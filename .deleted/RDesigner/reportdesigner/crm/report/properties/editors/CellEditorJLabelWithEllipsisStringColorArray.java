package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.Nullable;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 07:24:52
 */
public class CellEditorJLabelWithEllipsisStringColorArray extends CellEditorJLabelWithEllipsis<String[]>
{
    public CellEditorJLabelWithEllipsisStringColorArray()
    {
    }


    @Nullable
    public String convertToText(@Nullable String[] array)
    {
        if (array == null)
        {
            return null;
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++)
        {
            String color = array[i];
            sb.append(color);
            if (i < array.length - 1)
            {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }
}