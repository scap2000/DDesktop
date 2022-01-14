package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.FilePath;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 13:15:53
 */
public class CellEditorJTextFieldWithEllipsisFilePath extends CellEditorJTextFieldWithEllipsis<FilePath>
{
    public CellEditorJTextFieldWithEllipsisFilePath()
    {
    }


    @NotNull
    public String convertToText(@Nullable FilePath obj)
    {
        if (obj != null)
        {
            return obj.getPath();
        }
        return "";
    }


    @Nullable
    public FilePath convertFromText(@NotNull String text) throws IllegalArgumentException
    {
        if (text.trim().length() == 0)
        {
            return new FilePath("");
        }

        return new FilePath(text);
    }

}
