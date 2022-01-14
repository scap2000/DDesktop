package org.pentaho.reportdesigner.crm.report.properties.editors;

import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.SubReportParameters;
import org.pentaho.reportdesigner.crm.report.properties.renderers.SubReportParametersTableCellRenderer;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 07:24:52
 */
public class CellEditorJLabelWithEllipsisSubReportParameters extends CellEditorJLabelWithEllipsis<SubReportParameters>
{

    public CellEditorJLabelWithEllipsisSubReportParameters()
    {
    }


    public void setValue(@Nullable SubReportParameters formula, boolean commit)
    {
        super.setValue(formula, commit);
    }


    @Nullable
    public String convertToText(@Nullable SubReportParameters subReportParameters)
    {
        return SubReportParametersTableCellRenderer.createText(subReportParameters);
    }


}
