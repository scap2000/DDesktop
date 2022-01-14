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
package org.pentaho.reportdesigner.crm.report.preview;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.TableDataFactory;
import org.jfree.report.layout.DefaultSizeCalculator;
import org.pentaho.reportdesigner.crm.report.IconLoader;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.jdbc.ReportDataTableModel;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionGadget;
import org.pentaho.reportdesigner.crm.report.inspections.InspectionResult;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport.JFreeReportVisitor;
import org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport.NullDataFactory;
import org.pentaho.reportdesigner.crm.report.util.JFreeReportBootingHelper;
import org.pentaho.reportdesigner.crm.report.zoom.ZoomModel;
import org.pentaho.reportdesigner.lib.client.components.docking.ToolWindow;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 04.02.2006
 * Time: 17:37:48
 */
public class PreviewComponent extends JPanel
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(PreviewComponent.class.getName());

    @NotNull
    private static final String DOWN_PAGE = "downPage";
    @NotNull
    private static final String LAST_PAGE = "lastPage";
    @NotNull
    private static final String UP_PAGE = "upPage";
    @NotNull
    private static final String FIRST_PAGE = "firstPage";

    @NotNull
    private JScrollPane scrollPane;

    @Nullable
    private ReportPane reportPane;
    @NotNull
    private JButton firstButton;
    @NotNull
    private JButton upButton;
    @NotNull
    private JButton downButton;

    @NotNull
    private JButton lastButton;
    @NotNull
    private JTextField pageTextField;


    public PreviewComponent()
    {
        setLayout(new BorderLayout());
        scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        scrollPane.setColumnHeaderView(Box.createVerticalStrut(15));
        scrollPane.setRowHeaderView(Box.createHorizontalStrut(20 + 15));

        firstButton = new JButton(IconLoader.getInstance().getPageFirstIcon());
        upButton = new JButton(IconLoader.getInstance().getPageUpIcon());
        downButton = new JButton(IconLoader.getInstance().getPageDownIcon());
        lastButton = new JButton(IconLoader.getInstance().getPageLastIcon());

        pageTextField = new JTextField(" ");
        pageTextField.setColumns(8);
        pageTextField.setHorizontalAlignment(JTextField.CENTER);

        pageTextField.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                try
                {
                    pageTextField.selectAll();
                    int p = Integer.parseInt(pageTextField.getText());
                    if (p <= 0)
                    {
                        p = 1;
                    }
                    if (p > reportPane.getNumberOfPages())
                    {
                        p = reportPane.getNumberOfPages();
                    }
                    reportPane.setPageNumber(p);
                }
                catch (NumberFormatException e1)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PreviewComponent.actionPerformed ", e1);
                }
            }
        });

        AbstractAction pageDownAction = new AbstractAction()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                downButton.doClick();
            }
        };
        AbstractAction pageLastAction = new AbstractAction()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                lastButton.doClick();
            }
        };
        AbstractAction pageUpAction = new AbstractAction()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                upButton.doClick();
            }
        };
        AbstractAction pageFirstAction = new AbstractAction()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                firstButton.doClick();
            }
        };

        setFocusable(true);
        addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                requestFocusInWindow();
            }
        });

        installActions(this, pageDownAction, pageLastAction, pageUpAction, pageFirstAction);
        installActions(pageTextField, pageDownAction, pageLastAction, pageUpAction, pageFirstAction);

        pageTextField.addFocusListener(new FocusListener()
        {
            public void focusGained(@NotNull FocusEvent e)
            {
                pageTextField.selectAll();
            }


            public void focusLost(@NotNull FocusEvent e)
            {
            }
        });

        downButton.setMargin(new Insets(1, 1, 1, 1));
        lastButton.setMargin(new Insets(1, 1, 1, 1));
        upButton.setMargin(new Insets(1, 1, 1, 1));
        firstButton.setMargin(new Insets(1, 1, 1, 1));

        firstButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (reportPane != null)
                {
                    reportPane.setPageNumber(1);
                }
            }
        });

        upButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                ReportPane pane = reportPane;
                if (pane != null)
                {
                    pane.setPageNumber(pane.getPageNumber() - 1);
                }
            }
        });

        downButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                ReportPane pane = reportPane;
                if (pane != null)
                {
                    int page = pane.getPageNumber() + 1;
                    pane.setPageNumber(page);
                }
            }
        });
        lastButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                ReportPane pane = reportPane;
                if (pane != null)
                {
                    pane.setPageNumber(pane.getNumberOfPages());
                }
            }
        });

        @NonNls
        FormLayout formLayout = new FormLayout("4dlu:grow, pref, 0dlu, pref, 0dlu, pref, 0dlu, pref, 0dlu, pref, 4dlu:grow", "0dlu, pref, 0dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();
        JPanel helperPanel = new JPanel(formLayout);
        helperPanel.add(firstButton, cc.xy(2, 2));
        helperPanel.add(upButton, cc.xy(4, 2));
        helperPanel.add(pageTextField, cc.xy(6, 2));
        helperPanel.add(downButton, cc.xy(8, 2));
        helperPanel.add(lastButton, cc.xy(10, 2));

        add(helperPanel, BorderLayout.SOUTH);
    }


    private void installActions(@NotNull JComponent component, @NotNull AbstractAction pageDownAction, @NotNull AbstractAction pageLastAction, @NotNull AbstractAction pageUpAction, @NotNull AbstractAction pageFirstAction)
    {
        component.getInputMap().put(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PreviewComponent.PageDown.Accelerator1")), DOWN_PAGE);
        component.getInputMap().put(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PreviewComponent.PageDown.Accelerator2")), DOWN_PAGE);
        component.getActionMap().put(DOWN_PAGE, pageDownAction);
        component.getInputMap().put(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PreviewComponent.PageLast.Accelerator1")), LAST_PAGE);
        component.getInputMap().put(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PreviewComponent.PageLast.Accelerator2")), LAST_PAGE);
        component.getActionMap().put(LAST_PAGE, pageLastAction);
        component.getInputMap().put(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PreviewComponent.PageUp.Accelerator1")), UP_PAGE);
        component.getInputMap().put(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PreviewComponent.PageUp.Accelerator2")), UP_PAGE);
        component.getActionMap().put(UP_PAGE, pageUpAction);
        component.getInputMap().put(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PreviewComponent.PageFirst.Accelerator1")), FIRST_PAGE);
        component.getInputMap().put(KeyStroke.getKeyStroke(TranslationManager.getInstance().getTranslation("R", "PreviewComponent.PageFirst.Accelerator2")), FIRST_PAGE);
        component.getActionMap().put(FIRST_PAGE, pageFirstAction);
    }


    public void updatePreview(@NotNull ReportDialog reportDialog)
    {
        InspectionGadget inspectionGadget = reportDialog.getInspectionGadget();
        ToolWindow inspectionsToolWindow = reportDialog.getInspectionsToolWindow();
        Report report = reportDialog.getReport();

        if (inspectionGadget != null && inspectionsToolWindow != null && report != null)
        {
            Set<InspectionResult> inspectionResultsAfterRun = inspectionGadget.getInspectionResultsAfterRun(report);
            for (InspectionResult inspectionResult : inspectionResultsAfterRun)
            {
                if (inspectionResult.getSeverity() == InspectionResult.Severity.ERROR)
                {
                    inspectionsToolWindow.setSizeState(ToolWindow.SizeState.NORMAL);
                    JOptionPane.showMessageDialog(reportDialog.getRootJComponent(),
                                                  TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.ReportDefinitionContainsErrors"),
                                                  TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                                  JOptionPane.ERROR_MESSAGE);
                    reportDialog.showDesignView();
                    return;
                }
            }

            try
            {
                showReport(reportDialog, report, new ReportDataTableModel(ColumnInfo.EMPTY_ARRAY, new ArrayList<ArrayList<Object>>()));
            }
            catch (ReportCreationException e1)
            {
                if (LOG.isLoggable(Level.WARNING)) LOG.log(Level.WARNING, "PreviewComponent.updatePreview ", e1);
                JOptionPane.showMessageDialog(reportDialog.getRootJComponent(),
                                              TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.CouldNotCreateReport", e1.getCause().getMessage()),
                                              TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                              JOptionPane.ERROR_MESSAGE);

                reportDialog.showDesignView();
                ReportElementSelectionModel selectionModel = reportDialog.getReportElementModel();
                if (selectionModel != null)
                {
                    selectionModel.setSelection(Arrays.asList(e1.getReportElement()));
                }

                scrollPane.setViewportView(new JLabel(TranslationManager.getInstance().getTranslation("R", "MessageDialog.Generic.Message", e1.getMessage())));
            }
            catch (Exception e1)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PreviewComponent.updatePreview ", e1);

                JOptionPane.showMessageDialog(reportDialog.getRootJComponent(),
                                              TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.CouldNotCreateReport", e1.getMessage()),
                                              TranslationManager.getInstance().getTranslation("R", "Error.Title"),
                                              JOptionPane.ERROR_MESSAGE);

                scrollPane.setViewportView(new JLabel(TranslationManager.getInstance().getTranslation("R", "CreateReportCommand.CouldNotCreateReport", e1.getMessage())));
            }

            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PreviewComponent.run ");
                    requestFocusInWindow();
                }
            });
        }
    }


    private void showReport(@NotNull final ReportDialog dialog, @NotNull Report report, @NotNull TableModel tableModel) throws ReportCreationException
    {
        long l1 = System.currentTimeMillis();

        JFreeReportVisitor reportVisitor = new JFreeReportVisitor();
        report.accept(null, reportVisitor);
        final JFreeReport jFreeReport = reportVisitor.getJFreeReport();

        jFreeReport.getReportConfiguration().setConfigProperty("org.jfree.report.NoPrinterAvailable", Boolean.TRUE.toString());
        jFreeReport.getReportConfiguration().setConfigProperty("org.jfree.report.layout.fontrenderer.UseMaxCharBounds", Boolean.valueOf(report.isUseMaxCharBounds()).toString());//NON-NLS


        try
        {
            Field field = DefaultSizeCalculator.class.getDeclaredField("cache");//NON-NLS
            field.setAccessible(true);
            Object object = field.get(null);
            if (object instanceof WeakHashMap)
            {
                WeakHashMap weakHashMap = (WeakHashMap) object;
                weakHashMap.clear();
            }
        }
        catch (Exception e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PreviewComponent.showReport ", e);
        }

        if (jFreeReport.getDataFactory() instanceof NullDataFactory)
        {
            jFreeReport.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, tableModel));
        }

        JFreeReportBootingHelper.boot(dialog);

        try
        {
            ReportPane pane = reportPane;
            if (pane == null)
            {
                pane = new ReportPane();
                reportPane = pane;

                pane.addPropertyChangeListener(new PropertyChangeListener()
                {
                    public void propertyChange(@NotNull PropertyChangeEvent evt)
                    {
                        updateButtons();
                    }
                });
            }

            pane.setReport(jFreeReport);

            scrollPane.setViewportView(pane);
            ZoomModel zoomModel = dialog.getZoomModel();
            if (zoomModel != null)
            {
                pane.setZoomFactor((float) (zoomModel.getZoomFactor() / 1000.));
            }
        }
        catch (ReportProcessingException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PreviewComponent.showReport ", e);
            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    scrollPane.setViewportView(new JLabel(TranslationManager.getInstance().getTranslation("R", "PreviewComponent.Error")));
                }
            });
        }

        long l2 = System.currentTimeMillis();
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PreviewComponent.showReport " + (l2 - l1) + " ms");
    }


    private void updateButtons()
    {
        ReportPane pane = reportPane;

        if (pane == null || !pane.isUpdated() || pane.getNumberOfPages() <= 0)
        {
            firstButton.setEnabled(false);
            upButton.setEnabled(false);
            downButton.setEnabled(false);
            lastButton.setEnabled(false);
            pageTextField.setEnabled(false);
            pageTextField.setText("");
        }
        else
        {
            firstButton.setEnabled(pane.getPageNumber() >= 2);
            upButton.setEnabled(pane.getPageNumber() >= 2);
            downButton.setEnabled(pane.getPageNumber() < pane.getNumberOfPages());
            lastButton.setEnabled(pane.getPageNumber() < pane.getNumberOfPages());
            pageTextField.setEnabled(true);
            resetPageTextField();
        }
    }


    private void resetPageTextField()
    {
        ReportPane pane = reportPane;

        if (pane != null)
        {
            pageTextField.setText(TranslationManager.getInstance().getTranslation("R", "PreviewComponent.PageTextField", Integer.valueOf(pane.getPageNumber()), Integer.valueOf(pane.getNumberOfPages())));
            if (pageTextField.hasFocus())
            {
                pageTextField.selectAll();
            }
        }
    }
}
