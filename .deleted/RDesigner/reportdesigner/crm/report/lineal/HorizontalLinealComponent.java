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
package org.pentaho.reportdesigner.crm.report.lineal;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.Unit;
import org.pentaho.reportdesigner.crm.report.model.PageDefinition;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.MathUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 26.01.2006
 * Time: 09:02:38
 */
public class HorizontalLinealComponent extends JComponent
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(HorizontalLinealComponent.class.getName());

    @NotNull
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0##");

    @NotNull
    public static final DecimalFormat DECIMAL_FORMAT_NUMBERS_ONE_DIGIT = new DecimalFormat("0.0");
    @NotNull
    public static final DecimalFormat DECIMAL_FORMAT_NUMBERS_INTEGER = new DecimalFormat("0");


    private double scaleFactor;
    @NotNull
    private ReportDialog reportDialog;
    @NotNull
    private LinealModel linealModel;

    @Nullable
    private GuideLine activeGuidLine;

    private boolean showLeftBorder = true;

    @Nullable
    private GuideLine draggedGuideLine;


    public HorizontalLinealComponent(@NotNull final ReportDialog reportDialog, @NotNull final LinealModel linealModel)
    {
        this.reportDialog = reportDialog;
        this.linealModel = linealModel;

        addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                double start = 0;
                Report report = linealModel.getReport();
                if (report != null)
                {
                    if (showLeftBorder)
                    {
                        start = report.getPageDefinition().getLeftBorder();
                    }
                    if (e.getButton() == MouseEvent.BUTTON1 && getActiveGuide(e, linealModel) == null)
                    {
                        linealModel.addGuidLine(new GuideLine(MathUtils.truncate((e.getX() / scaleFactor) - start, 0, report.getPageDefinition().getInnerPageSize().getWidth()), true));
                    }
                    else if (e.getButton() == MouseEvent.BUTTON2 && getActiveGuide(e, linealModel) == null)
                    {
                        linealModel.addGuidLine(new GuideLine(MathUtils.truncate((e.getX() / scaleFactor) - start, 0, report.getPageDefinition().getInnerPageSize().getWidth()), false));
                    }
                }
            }


            public void mousePressed(@NotNull MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    draggedGuideLine = getActiveGuide(e, linealModel);
                }
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                draggedGuideLine = null;
            }
        });

        linealModel.addLinealModelListener(new LinealModelListener()
        {
            public void guidLineAdded(@NotNull GuideLine guideLine)
            {
                activeGuidLine = null;
                repaint();
            }


            public void guidLineRemoved(@NotNull GuideLine guideLine)
            {
                activeGuidLine = null;
                repaint();
            }


            public void activationChanged(@NotNull GuideLine guideLine)
            {
                activeGuidLine = null;
                repaint();
            }


            public void positionChanged(@NotNull GuideLine guideLine, double oldPosition)
            {
                activeGuidLine = null;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionListener()
        {
            public void mouseDragged(@NotNull MouseEvent e)
            {
                GuideLine dragged = draggedGuideLine;
                if (dragged != null)
                {
                    double start = 0;
                    Report report = linealModel.getReport();
                    if (report != null)
                    {
                        if (showLeftBorder)
                        {
                            start = report.getPageDefinition().getLeftBorder();
                        }
                        linealModel.setPosition(dragged, MathUtils.truncate((e.getX() / scaleFactor) - start, 0, report.getPageDefinition().getInnerPageSize().getWidth()));
                    }
                }
            }


            public void mouseMoved(@NotNull MouseEvent e)
            {
                GuideLine ag = getActiveGuide(e, linealModel);
                //noinspection ObjectEquality
                if (activeGuidLine != ag)
                {
                    activeGuidLine = ag;
                    repaint();
                }
            }
        });


        addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(@NotNull MouseEvent e)
            {
                GuideLine ag = getActiveGuide(e, linealModel);
                //noinspection ObjectEquality
                if (activeGuidLine != ag)
                {
                    activeGuidLine = ag;
                    repaint();
                }
            }


            public void mouseExited(@NotNull MouseEvent e)
            {
                if (activeGuidLine != null)
                {
                    activeGuidLine = null;
                    repaint();
                }
            }


            public void mouseClicked(@NotNull MouseEvent e)
            {
                GuideLine ag = getActiveGuide(e, linealModel);
                //noinspection ObjectEquality
                if (activeGuidLine != ag)
                {
                    activeGuidLine = ag;
                    repaint();
                }
                popup(e);
            }


            public void mousePressed(@NotNull MouseEvent e)
            {
                GuideLine ag = getActiveGuide(e, linealModel);
                //noinspection ObjectEquality
                if (activeGuidLine != ag)
                {
                    activeGuidLine = ag;
                    repaint();
                }
                popup(e);
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                GuideLine ag = getActiveGuide(e, linealModel);
                //noinspection ObjectEquality
                if (activeGuidLine != ag)
                {

                    repaint();
                }
                popup(e);
            }


            private void popup(@NotNull final MouseEvent me)
            {
                if (me.isPopupTrigger())
                {
                    double start = 0;
                    final Report report = linealModel.getReport();
                    if (report != null)
                    {
                        if (showLeftBorder)
                        {
                            start = report.getPageDefinition().getLeftBorder();
                        }

                        LinkedHashSet<GuideLine> guideLines = linealModel.getGuideLines();
                        for (final GuideLine guideLine : guideLines)
                        {
                            int x = (int) ((guideLine.getPosition() + start) * scaleFactor);

                            if (x <= me.getX() + 2 && x >= me.getX() - 2)
                            {
                                JPopupMenu popupMenu = new JPopupMenu();

                                popupMenu.add(new AbstractAction(TranslationManager.getInstance().getTranslation("R", "LinealComponent.Properties"))
                                {
                                    public void actionPerformed(@NotNull ActionEvent e)
                                    {
                                        double unitValue = reportDialog.getApplicationSettings().getUnit().convertFromPoints(guideLine.getPosition());
                                        String initialSelectionValue = DECIMAL_FORMAT.format(unitValue);
                                        String s = JOptionPane.showInputDialog(reportDialog, TranslationManager.getInstance().getTranslation("R", "LinealComponent.Guide.Position"), initialSelectionValue);
                                        if (s != null)
                                        {
                                            try
                                            {
                                                double d = Double.parseDouble(s);
                                                double points = reportDialog.getApplicationSettings().getUnit().convertToPoints(d);
                                                linealModel.setPosition(guideLine, MathUtils.truncate(points, 0, report.getPageDefinition().getInnerPageSize().getWidth()));
                                            }
                                            catch (NumberFormatException e1)
                                            {
                                                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "HorizontalLinealComponent.actionPerformed ", e1);
                                                //ok
                                            }
                                        }
                                    }
                                });

                                if (guideLine.isActive())
                                {
                                    popupMenu.add(new AbstractAction(TranslationManager.getInstance().getTranslation("R", "LinealComponent.Deactivate"))
                                    {
                                        public void actionPerformed(@NotNull ActionEvent e)
                                        {
                                            linealModel.setActive(guideLine, false);
                                        }
                                    });
                                }
                                else
                                {
                                    popupMenu.add(new AbstractAction(TranslationManager.getInstance().getTranslation("R", "LinealComponent.Activate"))
                                    {
                                        public void actionPerformed(@NotNull ActionEvent e)
                                        {
                                            linealModel.setActive(guideLine, true);
                                        }
                                    });
                                }

                                popupMenu.add(new AbstractAction(TranslationManager.getInstance().getTranslation("R", "LinealComponent.Delete"))
                                {
                                    public void actionPerformed(@NotNull ActionEvent e)
                                    {
                                        linealModel.removeGuideLine(guideLine);
                                    }
                                });

                                popupMenu.show(HorizontalLinealComponent.this, me.getX(), me.getY());

                                break;
                            }
                        }
                    }
                }
            }
        });
    }


    @Nullable
    private GuideLine getActiveGuide(@NotNull MouseEvent e, @NotNull LinealModel linealModel)
    {
        Report report = linealModel.getReport();
        if (report != null)
        {
            for (GuideLine guideLine : linealModel.getGuideLines())
            {
                double start = 0;
                if (showLeftBorder)
                {
                    start = report.getPageDefinition().getLeftBorder();
                }
                int x = (int) ((guideLine.getPosition() + start) * scaleFactor);
                if (x <= e.getX() + 2 && x >= e.getX() - 2)
                {
                    double unitValue = reportDialog.getApplicationSettings().getUnit().convertFromPoints(guideLine.getPosition());
                    setToolTipText(DECIMAL_FORMAT.format(unitValue));
                    return guideLine;
                }
            }
        }

        setToolTipText(null);
        return null;
    }


    @NotNull
    public Dimension getPreferredSize()
    {
        Report report = linealModel.getReport();
        if (report != null)
        {
            PageDefinition pageDefinition = report.getPageDefinition();
            double d = pageDefinition.getLeftBorder() + pageDefinition.getInnerPageSize().getWidth() + pageDefinition.getRightBorder();
            return new Dimension((int) (d * scaleFactor), 15);
        }
        else
        {
            return new Dimension(0, 15);
        }
    }


    protected void paintComponent(@NotNull Graphics g)
    {
        Report report = linealModel.getReport();
        if (report != null)
        {
            double start = report.getPageDefinition().getLeftBorder();

            double end = report.getPageDefinition().getInnerPageSize().getWidth();
            end += report.getPageDefinition().getLeftBorder();
            end += report.getPageDefinition().getRightBorder();

            g.setColor(Color.WHITE);
            g.fillRect((int) (start * scaleFactor), 0, (int) ((end - start) * scaleFactor), getHeight());

            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(-1, -1, (int) (start * scaleFactor) + 1, getHeight() - 1 + 1);
            g.drawRect((int) (start * scaleFactor), -1, (int) ((end - start) * scaleFactor), getHeight() - 1 + 1);

            drawGuideLines(g, report);
            drawDots(g, start, end);
            drawNumbers(g, start, end);
        }
    }


    private void drawGuideLines(@NotNull Graphics g, @NotNull Report report)
    {
        LinkedHashSet<GuideLine> guideLines = linealModel.getGuideLines();
        double startOffset = 0;
        if (showLeftBorder)
        {
            startOffset = report.getPageDefinition().getLeftBorder();
        }
        int so = (int) (startOffset * scaleFactor);

        for (GuideLine guideLine : guideLines)
        {
            int x = (int) (guideLine.getPosition() * scaleFactor) + so;
            if (guideLine.isActive())
            {
                g.setColor(new Color(240, 170, 170));
            }
            else
            {
                g.setColor(new Color(205, 205, 205));
            }
            g.fillRect(x - 2, 1, 4, 13);
            if (guideLine.isActive())
            {
                g.setColor(new Color(231, 109, 109));
            }
            else
            {
                g.setColor(new Color(170, 170, 170));
            }
            g.drawRect(x - 2, 0, 4, 14);
        }

        g.setColor(Color.RED);
        GuideLine highlightGuideLine = activeGuidLine;
        if (draggedGuideLine != null)
        {
            highlightGuideLine = draggedGuideLine;
        }

        if (highlightGuideLine != null)
        {
            int x = (int) (highlightGuideLine.getPosition() * scaleFactor) + so;
            if (highlightGuideLine.isActive())
            {
                g.setColor(new Color(255, 128, 128));
            }
            else
            {
                g.setColor(new Color(170, 170, 170));
            }
            g.fillRect(x - 2, 1, 4, 13);
            if (highlightGuideLine.isActive())
            {
                g.setColor(new Color(255, 34, 34));
            }
            else
            {
                g.setColor(new Color(100, 100, 100));
            }
            g.drawRect(x - 2, 0, 4, 14);
        }
    }


    private void drawDots(@NotNull Graphics g, double start, double end)
    {
        g.setColor(Color.GRAY);

        Unit unit = reportDialog.getApplicationSettings().getUnit();
        double factorForUnitAndScale = NumberHelper.getFactorForUnitAndScale(unit, scaleFactor);

        double increment = unit.getDotsPerUnit() * factorForUnitAndScale;

        for (double i = start + increment / 2; i < end; i += increment)
        {
            int x = (int) (i * scaleFactor);
            g.drawLine(x, 7, x, 9);
        }
    }


    private void drawNumbers(@NotNull Graphics g, double start, double end)
    {
        Unit unit = reportDialog.getApplicationSettings().getUnit();
        double factorForUnitAndScale = NumberHelper.getFactorForUnitAndScale(unit, scaleFactor);

        double increment = unit.getDotsPerUnit() * factorForUnitAndScale;
        double numberIncrement = factorForUnitAndScale;

        DecimalFormat df = DECIMAL_FORMAT_NUMBERS_INTEGER;
        if (numberIncrement < 1)
        {
            df = DECIMAL_FORMAT_NUMBERS_ONE_DIGIT;
        }


        g.setColor(Color.BLACK);
        double number = 0;
        for (double i = start; i < end - increment / 2; i += increment)
        {
            int x = (int) (i * scaleFactor);

            if (number > 0)
            {
                String s = df.format(number);
                Rectangle2D sb = g.getFontMetrics().getStringBounds(s, g);
                g.drawString(s, (int) ((sb.getX() - sb.getWidth() / 2) + x), (int) -sb.getY());
            }
            number += numberIncrement;
        }
    }


    public void setScaleFactor(double sf)
    {
        scaleFactor = sf;
    }
}
