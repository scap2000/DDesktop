package org.pentaho.reportdesigner.crm.report.model.functions;

import org.jetbrains.annotations.NotNull;

/**
 * User: Martin
 * Date: 26.01.2007
 * Time: 12:38:37
 */
public class PropertyInfo
{
    @NotNull
    private String group;
    private int sortingID;


    public PropertyInfo(@NotNull String group, int sortingID)
    {
        this.group = group;
        this.sortingID = sortingID;
    }


    @NotNull
    public String getGroup()
    {
        return group;
    }


    public int getSortingID()
    {
        return sortingID;
    }
}
