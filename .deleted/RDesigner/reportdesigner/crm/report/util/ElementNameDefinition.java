package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;

/**
 * User: Martin
 * Date: 16.03.2007
 * Time: 18:49:10
 */
public class ElementNameDefinition
{
    @NotNull
    private ReportElement definingElement;
    @NotNull
    private String name;


    public ElementNameDefinition(@NotNull ReportElement definingElement, @NotNull String name)
    {
        //noinspection ConstantConditions
        if (definingElement == null)
        {
            throw new IllegalArgumentException("definingElement must not be null");
        }
        //noinspection ConstantConditions
        if (name == null)
        {
            throw new IllegalArgumentException("name must not be null");
        }
        this.definingElement = definingElement;
        this.name = name;
    }


    @NotNull
    public ReportElement getDefiningElement()
    {
        return definingElement;
    }


    @NotNull
    public String getElementName()
    {
        return name;
    }
}
