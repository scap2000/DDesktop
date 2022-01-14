package org.pentaho.reportdesigner.crm.report.tests;

import java.io.Serializable;

/**
 * User: Martin
 * Date: 21.02.2007
 * Time: 21:12:24
 */
@SuppressWarnings({"ALL"})
public enum Bla implements Serializable
{
    SOMMER(1),
    WINTER(2),
    HERBST(3);

    private int val;


    Bla(int val)
    {
        this.val = val;
    }


    public int getVal()
    {
        return val;
    }


    public void setVal(int val)
    {
        this.val = val;
    }

}
