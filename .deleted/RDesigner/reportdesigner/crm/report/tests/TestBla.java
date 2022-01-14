package org.pentaho.reportdesigner.crm.report.tests;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * User: Martin
 * Date: 21.02.2007
 * Time: 21:15:23
 */
@SuppressWarnings({"ALL"})
public class TestBla
{
    public static void main(String[] args) throws IOException
    {
        Hugo bla = Hugo.ONE;

        bla.setVal(123);

        System.out.println("bla = " + bla.getVal());

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("test.txt"));
        objectOutputStream.writeObject(bla);
        objectOutputStream.close();
    }
}
