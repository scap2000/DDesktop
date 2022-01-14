package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.StyleExpression;
import org.pentaho.reportdesigner.crm.report.model.StyleExpressions;

import java.util.List;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 07:24:52
 */
public class CellEditorJLabelWithEllipsisStyleExpressions extends CellEditorJLabelWithEllipsis<StyleExpressions>
{

    public CellEditorJLabelWithEllipsisStyleExpressions()
    {
    }


    public void setValue(@Nullable StyleExpressions styleExpressions, boolean commit)
    {
        super.setValue(styleExpressions, commit);
    }


    @Nullable
    public String convertToText(@Nullable StyleExpressions styleExpressions)
    {
        if (styleExpressions != null)
        {
            List<StyleExpression> list = styleExpressions.getStyleExpressions();
            if (!list.isEmpty())
            {
                return list.get(0).toString();
            }
        }
        return null;
    }


}
