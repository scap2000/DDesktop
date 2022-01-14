package org.pentaho.reportdesigner.crm.report.model;

import org.jetbrains.annotations.NotNull;

/**
 * User: Martin
 * Date: 20.03.2007
 * Time: 17:40:32
 */
public enum ChartType
{
    @NotNull AREA(true, false),
    @NotNull BAR(true, false),
    @NotNull LINE(true, false),
    @NotNull PIE(false, true),
    @NotNull RING(false, true),
    @NotNull MULTI_PIE(true, false),
    @NotNull WATERFALL(true, false);

    private boolean categorySet;
    private boolean pieSet;


    ChartType(boolean categorySet, boolean pieSet)
    {
        this.categorySet = categorySet;
        this.pieSet = pieSet;
    }


    public boolean isCategorySet()
    {
        return categorySet;
    }


    public boolean isPieSet()
    {
        return pieSet;
    }
}
