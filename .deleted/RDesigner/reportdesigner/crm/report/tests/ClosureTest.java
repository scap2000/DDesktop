/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * User: Martin
 * Date: 20.08.2006
 * Time: 10:09:38
 */
@SuppressWarnings({"ALL"})
public class ClosureTest
{
    public static void main(String[] args) throws IOException
    {
        File file = new File("C:\\temp\\invoicePRD-2.xml");

        LineProcessor lineOut1 = new LineProcessor()
        {
            public void processLine(String line)
            {
                System.out.println("line = " + line);
            }
        };

        ClosureTest.readLines(file, lineOut1);

        ClosureTest.readLines(file, getSysoutProcessor());
        ClosureTest.readLines(file, getNullProcessor());

        /*
        //closure as in: http://blogs.sun.com/roller/resources/ahe/closures.pdf
        void lineOut2(String line)
        {
            System.out.println("line = " + line);
        }

        ClosureTest.readLines(file, lineOut2);
        */

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine())
        {
            System.out.println(sc.nextLine());
        }
        sc.close();
    }


    private static LineProcessor getSysoutProcessor()
    {
        return new LineProcessor()
        {
            public void processLine(String line)
            {
                System.out.println(line);
            }
        };
    }


    private static LineProcessor getNullProcessor()
    {
        return new LineProcessor()
        {
            public void processLine(String line)
            {
            }
        };
    }


    private static interface LineProcessor
    {
        void processLine(String line);
    }


    private static void readLines(File file, LineProcessor lp) throws IOException
    {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
            {
                lp.processLine(line);
            }
        }
        finally
        {
            if (br != null)
            {
                br.close();
            }
        }
    }
}
