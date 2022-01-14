package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Martin
 * Date: 13.02.2007
 * Time: 17:49:48
 */
@SuppressWarnings({"ALL"})
public class NullReturnTest
{

    @Nullable
    private String bla;


    public NullReturnTest(@Nullable String bla)
    {
        this.bla = bla;
    }


    @NotNull
    public String getBla()
    {
        try
        {
            Integer.parseInt("abc");
            return null;//Problem 1
        }
        catch (NumberFormatException e)
        {
            System.out.println("can be expected");
        }
        return bla;//Problem 2
    }
}
