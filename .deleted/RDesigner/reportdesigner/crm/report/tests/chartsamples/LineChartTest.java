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
package org.pentaho.reportdesigner.crm.report.tests.chartsamples;

import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 20.02.2006
 * Time: 08:05:58
 */
@SuppressWarnings({"ALL"})
public class LineChartTest
{
    public static void main(@NotNull String[] args)
    {
        XYSeries series = new XYSeries("serie1", true);
        series.add(1, 1);
        series.add(2, 2);
        series.add(3, 0.5);
        series.add(4, 3);
        series.add(4.5, 1);
        series.add(5, 4);

        StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
        renderer.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        renderer.setDrawSeriesLineAsPath(true);
        JFreeChart jFreeChart = new JFreeChart(new XYPlot(new XYSeriesCollection(series), new NumberAxis("domain"), new NumberAxis("value"), renderer));
        ChartPanel chartPanel = new ChartPanel(jFreeChart);

        JFrame frame = new JFrame();
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 500, 400);
        frame.setVisible(true);
    }
}
