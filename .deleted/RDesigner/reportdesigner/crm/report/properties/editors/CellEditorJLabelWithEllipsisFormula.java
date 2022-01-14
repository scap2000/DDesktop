package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.Formula;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 07:24:52
 */
public class CellEditorJLabelWithEllipsisFormula extends CellEditorJLabelWithEllipsis<Formula>
{

    public CellEditorJLabelWithEllipsisFormula()
    {
    }


    public void setValue(@Nullable Formula formula, boolean commit)
    {
        super.setValue(formula, commit);
    }


    @Nullable
    public String convertToText(@Nullable Formula formula)
    {
        if (formula != null)
        {
            return formula.getText();
        }
        return null;
    }


}
