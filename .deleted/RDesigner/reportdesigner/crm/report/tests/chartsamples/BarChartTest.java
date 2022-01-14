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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

/**
 * User: Martin
 * Date: 20.02.2006
 * Time: 08:05:58
 */
@SuppressWarnings({"ALL"})
public class BarChartTest
{
    public static void main(@NotNull String[] args)
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1., "row1", "column1");
        dataset.addValue(2., "row2", "column1");
        dataset.addValue(3., "row3", "column1");

        dataset.addValue(1.5, "row1", "column2");
        dataset.addValue(2.5, "row2", "column2");
        dataset.addValue(3.5, "row3", "column2");


        JFreeChart jFreeChart = new JFreeChart(new CategoryPlot(dataset, new CategoryAxis("Category"), new NumberAxis("Number Axis"), new BarRenderer3D()));
        ChartPanel chartPanel = new ChartPanel(jFreeChart);

        JFrame frame = new JFrame();
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 500, 400);
        frame.setVisible(true);
    }
}
