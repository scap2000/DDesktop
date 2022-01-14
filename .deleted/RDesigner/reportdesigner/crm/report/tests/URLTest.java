package org.pentaho.reportdesigner.crm.report.tests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * User: Martin
 * Date: 26.09.2007
 * Time: 16:35:26
 */
@SuppressWarnings({"ALL"})
public class URLTest
{
    public static void main(String[] args) throws IOException, URISyntaxException
    {
        URL url = new URL(new URL("file:/C:/../Daten/"), "todo.txt");
        File f = new File(url.toURI());
        System.out.println("f.getAbsolutePath() = " + f.getAbsolutePath());
    }
}
