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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportProgressEvent;
import org.jfree.report.event.ReportProgressListener;
import org.jfree.report.modules.output.pageable.graphics.PageDrawable;
import org.jfree.report.modules.output.pageable.graphics.PrintReportProcessor;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;


@SuppressWarnings({"DuplicateStringLiteralInspection"})
public class ReportPane extends JComponent implements Scrollable
{
    @Nullable
    private PageDrawable pageDrawable;
    @Nullable
    private PrintReportProcessor printReportProcessor;

    private int pageNumber;
    private float zoomFactor;

    private double percentage;


    public ReportPane()
    {
    }


    public int getNumberOfPages()
    {
        if (printReportProcessor != null)
        {
            return printReportProcessor.getNumberOfPages();
        }

        return 0;
    }


    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;

        Thread t = new Thread()
        {
            public void run()
            {
                if (printReportProcessor != null)
                {
                    pageDrawable = printReportProcessor.getPageDrawable(ReportPane.this.pageNumber - 1);
                }
                refresh();
            }
        };
        t.start();
    }


    public int getPageNumber()
    {
        return pageNumber;
    }


    public void setReport(@NotNull JFreeReport jFreeReport) throws ReportProcessingException
    {
        pageDrawable = null;
        pageNumber = 1;

        printReportProcessor = new PrintReportProcessor(jFreeReport);
        final PrintReportProcessor prp = printReportProcessor;
        prp.addReportProgressListener(new ReportProgressListener()
        {
            public void reportProcessingStarted(@NotNull ReportProgressEvent event)
            {
                refresh();
            }


            public void reportProcessingUpdate(@NotNull ReportProgressEvent event)
            {
                percentage = event.getRow() / (double) event.getMaximumRow();
                refresh();
            }


            public void reportProcessingFinished(@NotNull ReportProgressEvent event)
            {
                refresh();
            }
        });

        Thread t = new Thread()
        {
            public void run()
            {
                pageDrawable = prp.getPageDrawable(ReportPane.this.pageNumber - 1);
                if (prp.getErrorReason() != null)
                {
                    UncaughtExcpetionsModel.getInstance().addException(prp.getErrorReason());
                }

                refresh();
            }
        };
        t.start();
    }


    private void refresh()
    {
        repaint();

        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                revalidate();
                repaint();

                if (pageDrawable != null)
                {
                    firePropertyChange("reportFinished", false, true);//NON-NLS
                }
            }
        });
    }


    public void setZoomFactor(float zoomFactor)
    {
        this.zoomFactor = zoomFactor;

        revalidate();
        repaint();
    }


    public boolean isUpdated()
    {
        return pageDrawable != null;
    }


    @NotNull
    public Dimension getPreferredSize()
    {
        if (pageDrawable == null)
        {
            return new Dimension(0, 0);
        }
        final Dimension preferredSize = pageDrawable.getPreferredSize();
        return new Dimension((int) ((preferredSize.width) * zoomFactor) + 20, (int) ((preferredSize.height) * zoomFactor) + 20);
    }


    protected void paintComponent(@NotNull Graphics g)
    {
        if (!isUpdated())
        {
            g.setColor(new Color(135, 211, 231));
            g.drawRect(0, 0, 100, 10);
            g.fillRect(0, 0, (int) (100 * percentage), 10);
        }

        if (pageDrawable != null)
        {
            Graphics2D g2 = (Graphics2D) g;

            final PageFormat pageFormat = pageDrawable.getPageFormat();
            final float outerW = (float) pageFormat.getWidth();
            final float outerH = (float) pageFormat.getHeight();

            g2.transform(AffineTransform.getScaleInstance(zoomFactor, zoomFactor));
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            Rectangle2D pageArea = new Rectangle2D.Float(0, 0, outerW, outerH);

            g2.setPaint(Color.white);
            g2.fill(pageArea);

            Graphics2D g22 = (Graphics2D) g2.create();
            Rectangle2D.Double r = new Rectangle2D.Double(0, 0, pageFormat.getImageableWidth(), pageFormat.getImageableHeight());
            if (pageDrawable != null)
            {
                pageDrawable.draw(g22, r);
            }
            g22.dispose();

            g2.setPaint(Color.LIGHT_GRAY);
            g2.draw(pageArea);
        }
    }


    @NotNull
    public Dimension getPreferredScrollableViewportSize()
    {
        return getPreferredSize();
    }


    public int getScrollableUnitIncrement(@NotNull Rectangle visibleRect, int orientation, int direction)
    {
        return 10;
    }


    public int getScrollableBlockIncrement(@NotNull Rectangle visibleRect, int orientation, int direction)
    {
        return 50;
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
