package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;

/**
 * User: Martin
 * Date: 16.03.2007
 * Time: 18:49:10
 */
public class FieldDefinition
{
    @NotNull
    private ReportElement definingElement;
    @NotNull
    private String field;


    public FieldDefinition(@NotNull ReportElement definingElement, @NotNull String field)
    {
        //noinspection ConstantConditions
        if (definingElement == null)
        {
            throw new IllegalArgumentException("definingElement must not be null");
        }
        //noinspection ConstantConditions
        if (field == null)
        {
            throw new IllegalArgumentException("field must not be null");
        }
        this.definingElement = definingElement;
        this.field = field;
    }


    @NotNull
    public ReportElement getDefiningElement()
    {
        return definingElement;
    }


    @NotNull
    public String getField()
    {
        return field;
    }
}
