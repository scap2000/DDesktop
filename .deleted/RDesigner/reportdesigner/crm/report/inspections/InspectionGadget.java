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
package org.pentaho.reportdesigner.crm.report.inspections;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.BandElementModelAdapter;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.inspections.impl.ChartSeriesNamesAndValuesColumnDiffererentElementInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.FieldNotSetInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.FontHigherThanElementInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.FormulaErrorInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.FunctionNameTestInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.MultiDataSetReportElementValidationInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.MultipleFieldDefinitionInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.NoContentInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.OverlappingTextElementsInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.ResourceBaseNotSetInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.ResourceClasspathNotSetInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.SizeProblemInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.StaticImageWithURLProblemInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.SubReportElementTestInspection;
import org.pentaho.reportdesigner.crm.report.inspections.impl.UndefinedFieldInspection;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.properties.PropertyEditorPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 02.02.2006
 * Time: 08:55:58
 */
public class InspectionGadget extends JPanel
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(InspectionGadget.class.getName());

    @NotNull
    private ArrayList<Inspection> inspections;

    private boolean ignore;
    @NotNull
    private Timer timer;


    public InspectionGadget(@NotNull final ReportDialog reportDialog)
    {
        setLayout(new BorderLayout());

        final InspectionResultTable inspectionResultTable = new InspectionResultTable();
        JScrollPane scrollPane = new JScrollPane(inspectionResultTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        final ReportElementSelectionModel elementModel = reportDialog.getReportElementModel();

        if (elementModel != null)
        {
            inspectionResultTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(@NotNull ListSelectionEvent e)
                {
                    ignore = true;

                    if (!e.getValueIsAdjusting())
                    {
                        int[] rows = inspectionResultTable.getSelectedRows();
                        if (rows.length > 0)
                        {
                            HashSet<ReportElement> reportElements = new HashSet<ReportElement>();
                            HashSet<String> highlightProperties = new HashSet<String>();
                            for (int i : rows)
                            {
                                InspectionResult inspectionResult = inspectionResultTable.getInspectionResult(i);
                                for (LocationInfo locationInfo : inspectionResult.getLocationInfos())
                                {
                                    reportElements.add(locationInfo.getReportElement());
                                    if (locationInfo.getPropertyName() != null)
                                    {
                                        highlightProperties.add(locationInfo.getPropertyName());
                                    }
                                }
                            }

                            reportDialog.showDesignView();
                            elementModel.setSelection(reportElements);
                            if (highlightProperties.size() == 1)
                            {
                                PropertyEditorPanel panel = reportDialog.getPropertyEditorPanel();
                                if (panel != null)
                                {
                                    panel.selectProperty(highlightProperties.iterator().next(), true);
                                }
                            }
                        }
                    }

                    ignore = false;
                }
            });

            elementModel.addBandElementModelListener(new BandElementModelAdapter()
            {
                public void selectionChanged()
                {
                    if (!ignore)
                    {
                        inspectionResultTable.clearSelection();
                    }
                }
            });

            inspections = new ArrayList<Inspection>();

            installInspections();

            timer = new Timer(1000, new ActionListener()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    Report report = reportDialog.getReport();
                    if (report != null)
                    {
                        Set<InspectionResult> inspectionResults = runInspections(report);
                        inspectionResultTable.setInspectionResults(inspectionResults);
                    }
                }
            });

            timer.setCoalesce(true);
            timer.setRepeats(true);

            timer.start();
        }
    }


    private void installInspections()
    {
        inspections.add(new StaticImageWithURLProblemInspection());
        inspections.add(new OverlappingTextElementsInspection());
        inspections.add(new SizeProblemInspection());
        inspections.add(new FontHigherThanElementInspection());
        inspections.add(new NoContentInspection());
        inspections.add(new UndefinedFieldInspection());
        inspections.add(new FunctionNameTestInspection());
        inspections.add(new MultipleFieldDefinitionInspection());
        inspections.add(new FieldNotSetInspection());
        inspections.add(new ResourceClasspathNotSetInspection());
        inspections.add(new ResourceBaseNotSetInspection());
        inspections.add(new FormulaErrorInspection());
        inspections.add(new MultiDataSetReportElementValidationInspection());
        inspections.add(new ChartSeriesNamesAndValuesColumnDiffererentElementInspection());
        inspections.add(new SubReportElementTestInspection());
    }


    @NotNull
    private Set<InspectionResult> runInspections(@NotNull Report report)
    {
        long nanos1 = System.nanoTime();
        Set<InspectionResult> inspectionResults = new HashSet<InspectionResult>();

        for (Inspection inspection : inspections)
        {
            ArrayList<InspectionResult> ir = inspection.inspect(report);
            inspectionResults.addAll(ir);
        }
        long nanos2 = System.nanoTime();
        if ((nanos2 - nanos1) > 30 * 1000 * 1000)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "InspectorGadget.actionPerformed  duration = " + (nanos2 - nanos1) / (1000. * 1000.));
        }
        return inspectionResults;
    }


    @NotNull
    public ArrayList<Inspection> getInspections()
    {
        return inspections;
    }


    @NotNull
    public Set<InspectionResult> getInspectionResultsAfterRun(@NotNull Report report)
    {
        return runInspections(report);
    }


    public void dispose()
    {
        timer.stop();
    }
}
