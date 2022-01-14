package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 13:15:53
 */
public class CellEditorJTextFieldWithEllipsisElement extends CellEditorJTextFieldWithEllipsis<String>
{
    public CellEditorJTextFieldWithEllipsisElement()
    {
    }


    @NotNull
    public String convertToText(@Nullable String text)
    {
        if (text != null)
        {
            return text;
        }

        return "";
    }


    @NotNull
    public String convertFromText(@NotNull String text) throws IllegalArgumentException
    {
        return text;
    }

}
