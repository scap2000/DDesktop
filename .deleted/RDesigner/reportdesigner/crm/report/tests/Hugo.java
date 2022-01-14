/**
 * User: Martin
 * Date: 21.02.2007
 * Time: 21:26:37
 */
package org.pentaho.reportdesigner.crm.report.tests;

import java.io.Serializable;

@SuppressWarnings({"ALL"})
public class Hugo implements Serializable
{
    public static final Hugo ONE = new Hugo("ONE", 1);//NON-NLS
    public static final Hugo TWO = new Hugo("TWO", 2);//NON-NLS
    public static final Hugo THREE = new Hugo("THREE", 3);//NON-NLS

    private final String myName; // for debug only

    private int val;


    private Hugo(String name, int val)
    {
        this.val = val;
        myName = name;
    }


    public String toString()
    {
        return myName;
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
