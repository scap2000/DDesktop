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
package org.pentaho.reportdesigner.crm.report;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.crm.report.lineal.LinealModel;
import org.pentaho.reportdesigner.crm.report.lineal.VerticalLabel;
import org.pentaho.reportdesigner.crm.report.lineal.VerticalLinealComponent;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.util.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 25.01.2006
 * Time: 18:17:48
 */
public class BandsComponent extends JComponent implements Scrollable
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(BandsComponent.class.getName());

    @NotNull
    private ArrayList<ReportBorderPanel> reportBorderPanels;
    @NotNull
    private ArrayList<GrabberComponent> grabberComponents;
    @NotNull
    private ArrayList<VerticalLabel> verticalLabels;
    @NotNull
    private ArrayList<VerticalLinealComponent> verticalLineals;

    @NotNull
    private BandsComponentHelper bandsComponentHelper;

    @NotNull
    private JPanel bottomHelperPanel;


    public BandsComponent()
    {
        setLayout(new BandsComponentLayoutManager(this));
        reportBorderPanels = new ArrayList<ReportBorderPanel>();
        grabberComponents = new ArrayList<GrabberComponent>();
        verticalLabels = new ArrayList<VerticalLabel>();
        verticalLineals = new ArrayList<VerticalLinealComponent>();

        bandsComponentHelper = new BandsComponentHelper(this);
        bottomHelperPanel = new JPanel();
        bottomHelperPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
    }


    public void updateReportBorderPanels(@NotNull ArrayList<ReportBorderPanel> reportBorderPanels)
    {
        clear();
        for (ReportBorderPanel reportBorderPanel : reportBorderPanels)
        {
            addReportBorderPanel(reportBorderPanel);
        }

        add(bottomHelperPanel);
    }


    private void clear()
    {
        reportBorderPanels.clear();
        grabberComponents.clear();
        verticalLineals.clear();
        verticalLabels.clear();
        bandsComponentHelper.removeAll();
        removeAll();
    }


    private void addReportBorderPanel(@NotNull final ReportBorderPanel reportBorderPanel)
    {
        add(reportBorderPanel);

        reportBorderPanels.add(reportBorderPanel);

        final LinealModel linealModel = reportBorderPanel.getReportPanel().getVerticalLinealModel();
        if (linealModel != null)
        {
            VerticalLinealComponent verticalLinealComponent = new VerticalLinealComponent(reportBorderPanel.getReportPanel().getReportDialog(),
                                                                                          reportBorderPanel.isShowTopBorder(),
                                                                                          reportBorderPanel.isShowBottomBorder(),
                                                                                          linealModel);

            verticalLinealComponent.setScaleFactor(reportBorderPanel.getReportPanel().getScaleFactor());

            GrabberComponent grabberComponent = new GrabberComponent();

            final int[] startY = new int[1];

            grabberComponent.addMouseListener(new MouseAdapter()
            {
                public void mousePressed(@NotNull MouseEvent e)
                {
                    Undo undo = reportBorderPanel.getReportPanel().getReportDialog().getUndo();
                    undo.startTransaction(PropertyKeys.VISUAL_HEIGHT);
                    startY[0] = e.getY();
                    bandsComponentHelper.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                }


                public void mouseReleased(@NotNull MouseEvent e)
                {
                    Undo undo = reportBorderPanel.getReportPanel().getReportDialog().getUndo();
                    undo.endTransaction();
                    bandsComponentHelper.setCursor(Cursor.getDefaultCursor());
                }
            });

            grabberComponent.addMouseMotionListener(new MouseMotionAdapter()
            {
                public void mouseDragged(@NotNull MouseEvent e)
                {
                    int diff = startY[0] - e.getY();
                    ReportPanel reportPanel = reportBorderPanel.getReportPanel();
                    double height = reportPanel.getBandToplevelReportElement().getVisualHeight() - diff / reportPanel.getScaleFactor();
                    reportPanel.getBandToplevelReportElement().setVisualHeight(Math.max(height, reportBorderPanel.isShowTopBorder() ? 0 : 5));
                }
            });

            grabberComponents.add(grabberComponent);
            verticalLineals.add(verticalLinealComponent);

            bandsComponentHelper.add(grabberComponent);
            bandsComponentHelper.add(verticalLinealComponent);

            String text = TranslationManager.getInstance().getTranslation("R", reportBorderPanel.getReportPanel().getBandToplevelReportElement().getBandToplevelType().getName());
            final VerticalLabel label = new VerticalLabel(text);
            reportBorderPanel.getReportPanel().addFocusListener(new FocusListener()
            {
                public void focusGained(@NotNull FocusEvent e)
                {
                    label.setFont(FontUtils.getDerivedFont(label.getFont(), Font.BOLD, label.getFont().getSize()));
                }


                public void focusLost(@NotNull FocusEvent e)
                {
                    if (!e.isTemporary())
                    {
                        label.setFont(FontUtils.getDerivedFont(label.getFont(), Font.PLAIN, label.getFont().getSize()));
                    }
                }
            });
            label.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(@NotNull MouseEvent e)
                {
                    if (e.getButton() == MouseEvent.BUTTON1)
                    {
                        reportBorderPanel.getReportPanel().requestFocusInWindow();
                    }
                }
            });
            label.setToolTipText(text);
            grabberComponent.setToolTipText(text);
            verticalLabels.add(label);
            bandsComponentHelper.add(label);

            revalidate();
            repaint();
        }
        else
        {
            if (LOG.isLoggable(Level.SEVERE)) LOG.log(Level.SEVERE, "BandsComponent.addReportBorderPanel lineal model is null");
        }
    }


    @NotNull
    public BandsComponentHelper getBandsComponentHelper()
    {
        return bandsComponentHelper;
    }


    @NotNull
    public ArrayList<ReportBorderPanel> getReportBorderPanels()
    {
        return reportBorderPanels;
    }


    @NotNull
    public ArrayList<GrabberComponent> getGrabberComponents()
    {
        return grabberComponents;
    }


    @NotNull
    public ArrayList<VerticalLabel> getVerticalLabels()
    {
        return verticalLabels;
    }


    @NotNull
    public ArrayList<VerticalLinealComponent> getVerticalLineals()
    {
        return verticalLineals;
    }


    @NotNull
    public JPanel getBottomHelperPanel()
    {
        return bottomHelperPanel;
    }


    public void setScaleFactor(double sf)
    {
        for (VerticalLinealComponent verticalLinealComponent : verticalLineals)
        {
            verticalLinealComponent.setScaleFactor(sf);
        }
    }


    @NotNull
    public Dimension getPreferredScrollableViewportSize()
    {
        return new Dimension(1, 1);
    }


    public int getScrollableUnitIncrement(@NotNull Rectangle visibleRect, int orientation, int direction)
    {
        return 20;
    }


    public int getScrollableBlockIncrement(@NotNull Rectangle visibleRect, int orientation, int direction)
    {
        return 100;
    }


    public boolean getScrollableTracksViewportWidth()
    {
        return false;
    }


    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }


}
