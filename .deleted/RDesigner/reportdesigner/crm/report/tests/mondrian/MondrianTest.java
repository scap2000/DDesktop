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
package org.pentaho.reportdesigner.crm.report.tests.mondrian;

import java.util.List;

import mondrian.olap.Axis;
import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Member;
import mondrian.olap.Position;
import mondrian.olap.Query;
import mondrian.olap.Result;

import org.jetbrains.annotations.NotNull;

/**
 * User: Martin
 * Date: 22.07.2006
 * Time: 15:31:31
 */
@SuppressWarnings({"ALL"})
public class MondrianTest
{
    public static void main(@NotNull String[] args)
    {
        String mondrianFile = MondrianTest.class.getResource("SampleData.mondrian.xml").toString();
        System.out.println("mondrianFile = " + mondrianFile);
        String qs = "with member [Measures].[Variance Percent] as '([Measures].[Variance] / [Measures].[Budget])', format_string = IIf(((([Measures].[Variance] / [Measures].[Budget]) * 100.0) > 2.0), \"|#.00%|style='green'\", IIf(((([Measures].[Variance] / [Measures].[Budget]) * 100.0) < 0.0), \"|#.00%|style='red'\", \"#.00%\"))\n" +
                    "select Crossjoin({[Region].[All Regions].[Central], [Region].[All Regions].[Eastern], [Region].[All Regions].[Southern], [Region].[All Regions].[Western]}, {[Measures].[Actual], [Measures].[Budget]}) ON columns,\n" +
                    "  Hierarchize(Union({[Department].[All Departments]}, [Department].[All Departments].Children)) ON rows\n" +
                    "from [Quadrant Analysis]";
        Connection connection = DriverManager.getConnection("provider=mondrian; JdbcDrivers=org.hsqldb.jdbcDriver; Jdbc=jdbc:hsqldb:hsql://localhost/sampledata; Catalog=http://localhost:8080/pentaho/GetMondrianModel?model=samples/reporting/SampleData.mondrian.xml; user=pentaho_user; password=password", null, false);
        Query query = connection.parseQuery(qs);

        Result result = connection.execute(query);
        Axis[] axes = result.getAxes();
        List<Position> positions = axes[0].getPositions();
        for (int i = 0; i < positions.size(); i++)
        {
            Position position = positions.get(i);
            for (int j = 0; j < position.size(); j++)
            {
                Member member = position.get(j);
                System.out.println("member = " + member);
                String caption = member.getHierarchy().getCaption();
                System.out.println("caption = " + caption);

                String name = member.getHierarchy().getName();
                System.out.println("name = " + name);
            }
        }

        System.out.println("result = " + result);

    }
}
