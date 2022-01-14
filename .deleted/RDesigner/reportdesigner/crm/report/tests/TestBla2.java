package org.pentaho.reportdesigner.crm.report.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * User: Martin
 * Date: 21.02.2007
 * Time: 21:15:23
 */
@SuppressWarnings({"ALL"})
public class TestBla2
{
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("test.txt"));
        Hugo o = (Hugo) objectInputStream.readObject();
        System.out.println(o.getVal());
    }
}
