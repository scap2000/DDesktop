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
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.commands.CommandKeys;
import org.pentaho.reportdesigner.crm.report.commands.SaveReportCommand;
import org.pentaho.reportdesigner.crm.report.lineal.GuideLine;
import org.pentaho.reportdesigner.crm.report.lineal.LinealModel;
import org.pentaho.reportdesigner.crm.report.model.AnchorFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.BandToplevelReportElement;
import org.pentaho.reportdesigner.crm.report.model.ChartReportElement;
import org.pentaho.reportdesigner.crm.report.model.DrawableFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.FilePath;
import org.pentaho.reportdesigner.crm.report.model.ImageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.ImageURLFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.LabelReportElement;
import org.pentaho.reportdesigner.crm.report.model.MessageFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.PageDefinition;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.ReportFactory;
import org.pentaho.reportdesigner.crm.report.model.StaticImageReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextFieldReportElement;
import org.pentaho.reportdesigner.crm.report.model.TextReportElement;
import org.pentaho.reportdesigner.crm.report.palette.ReportElementPalette;
import org.pentaho.reportdesigner.crm.report.properties.editors.ChartEditor;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.util.GraphicUtils;
import org.pentaho.reportdesigner.crm.report.util.ReportElementUtilities;
import org.pentaho.reportdesigner.crm.report.zoom.ZoomModel;
import org.pentaho.reportdesigner.lib.client.commands.CommandManager;
import org.pentaho.reportdesigner.lib.client.commands.DataContext;
import org.pentaho.reportdesigner.lib.client.commands.DataProvider;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;
import org.pentaho.reportdesigner.lib.client.util.MathUtils;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * User: Martin
 * Date: 10.10.2005
 * Time: 10:02:33
 */
public class ReportPanel extends JPanel implements PropertyChangeListener, DataProvider
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ReportPanel.class.getName());

    @NotNull
    public static final String CANCEL_EDIT = "cancelEdit";
    @NotNull
    public static final String ESCAPE = "ESCAPE";
    @NotNull
    public static final String ENTER = "ENTER";
    @NotNull
    public static final String APPROVE_EDIT = "approveEdit";
    @NotNull
    private static final String INSERT_NEWLINE = "insertNewline";
    @NotNull
    private static final String CTRL_ENTER = "ctrl ENTER";


    @NotNull
    private ReportDialog reportDialog;
    @NotNull
    private Report report;
    @NotNull
    private BandToplevelReportElement bandToplevelReportElement;
    @NotNull
    private ReportElementSelectionModel reportElementSelectionModel;

    @NotNull
    private SelectionHandler selectionHandler;

    private double scaleFactor = 1;

    @Nullable
    private ReportElement draggedReportElement;

    @Nullable
    private JScrollPane inlineEditingScrollPane;
    @Nullable
    private ReportElement inlineEditingReportElement;
    @Nullable
    private JTextArea inlineEditingTextArea;


    public ReportPanel(@NotNull ReportDialog reportDialog,
                       @NotNull Report report,
                       @NotNull BandToplevelReportElement bandToplevelReportElement,
                       @NotNull ReportElementSelectionModel reportElementSelectionModel)
    {
        setLayout(null);

        this.reportDialog = reportDialog;
        this.report = report;
        this.bandToplevelReportElement = bandToplevelReportElement;

        this.reportElementSelectionModel = reportElementSelectionModel;

        reportElementSelectionModel.refresh();

        selectionHandler = new SelectionHandler(this, reportElementSelectionModel);

        reportElementSelectionModel.addBandElementModelListener(new BandElementModelListener()
        {
            public void layoutChanged()
            {
                stopInlineEditing();

                repaint();
            }


            public void selectionChanged()
            {
                stopInlineEditing();

                CommandManager.dataProviderChanged();
                repaint();
            }
        });

        report.addPropertyChangeListener(this);
        attachPropertyChangeListener(report);

        initDragAndDrop();

        setFocusable(true);

        addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                requestFocusInWindow();
            }
        });

        selectionHandler.updateSubReportElements();
    }


    @Nullable
    public LinealModel getVerticalLinealModel()
    {
        return bandToplevelReportElement.getVerticalLinealModel();
    }


    @NotNull
    public LinealModel getHorizontalLinealModel()
    {
        return report.getHorizontalLinealModel();
    }


    @NotNull
    public SelectionHandler getSelectionHandler()
    {
        return selectionHandler;
    }


    @NotNull
    public ReportDialog getReportDialog()
    {
        return reportDialog;
    }


    @NotNull
    public Report getReport()
    {
        return report;
    }


    @NotNull
    public BandToplevelReportElement getBandToplevelReportElement()
    {
        return bandToplevelReportElement;
    }


    private void attachPropertyChangeListener(@NotNull ReportElement reportElement)
    {
        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement element : children)
        {
            element.addPropertyChangeListener(this);
            attachPropertyChangeListener(element);
        }
    }


    private void detachPropertyChangeListener(@NotNull ReportElement reportElement)
    {
        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement element : children)
        {
            element.removePropertyChangeListener(this);
            detachPropertyChangeListener(element);
        }
    }


    public double getScaleFactor()
    {
        return scaleFactor;
    }


    public void setScaleFactor(double scaleFactor)
    {
        stopInlineEditing();

        this.scaleFactor = scaleFactor;
        selectionHandler.updateSelectionRects();


        PageDefinition pageDefinition = report.getPageDefinition();
        int width = (int) ((pageDefinition.getInnerPageSize().getWidth()) * scaleFactor + 1);
        int height = (int) ((pageDefinition.getInnerPageSize().getHeight()) * scaleFactor + 1);

        setPreferredSize(new Dimension(width, height));

        repaint();
    }


    @NotNull
    public ReportElementSelectionModel getBandElementModel()
    {
        return reportElementSelectionModel;
    }


    @Nullable
    public ReportElement getDraggedReportElement()
    {
        return draggedReportElement;
    }


    public void setDraggedReportElement(@Nullable ReportElement draggedReportElement)
    {
        stopInlineEditing();

        if (draggedReportElement instanceof SubReportElement)
        {
            SubReportElement subReportElement = (SubReportElement) draggedReportElement;
            Report r = bandToplevelReportElement.getReport();
            if (r != null)
            {
                subReportElement.getRectangle().setRect(0, subReportElement.getRectangle().getY(), r.getPageDefinition().getInnerPageSize().getWidth(), 20);
                subReportElement.getMinimumSize().setSize(r.getPageDefinition().getInnerPageSize().getWidth(), 20);
            }

        }
        this.draggedReportElement = draggedReportElement;
    }


    protected void paintComponent(@NotNull Graphics g)
    {
        super.paintComponent(g);

        try
        {
            Shape origClip = g.getClip();

            GraphicsContext gc = new GraphicsContext(getScaleFactor());

            g.clipRect(0, 0, getWidth(), getHeight());
            paintBand(gc, (Graphics2D) g);

            g.setClip(origClip);
        }
        catch (Exception e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
    }


    private void paintBand(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        AffineTransform transform = g2d.getTransform();

        drawPageDefinition(g2d);

        if (!MathUtils.approxEquals(scaleFactor, 1))
        {
            g2d.transform(AffineTransform.getScaleInstance(scaleFactor, scaleFactor));
        }

        //Object antiAliasingHint = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        Object textAntiAliasingHint = g2d.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        Object fractionalMetrics = g2d.getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS);

        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        bandToplevelReportElement.paint(graphicsContext, g2d);

        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAliasingHint);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAntiAliasingHint);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics);

        g2d.setTransform(transform);

        selectionHandler.paintSelection(graphicsContext, g2d);

        paintDraggedReportElement(graphicsContext, g2d);
    }


    private void paintDraggedReportElement(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        ReportElement de = draggedReportElement;
        if (de != null)
        {
            Color origColor = g2d.getColor();
            g2d.setColor(Color.LIGHT_GRAY);
            Rectangle2D.Double rect = de.getRectangle();
            g2d.draw(GraphicUtils.getScaledRectangle(graphicsContext, rect));
            g2d.setColor(origColor);

            AffineTransform transform = g2d.getTransform();
            g2d.transform(AffineTransform.getScaleInstance(scaleFactor, scaleFactor));
            //Object antiAliasingHint = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            Object textAntiAliasingHint = g2d.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
            Object fractionalMetrics = g2d.getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS);
            //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            de.paint(graphicsContext, g2d);

            //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAliasingHint);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAntiAliasingHint);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics);
            g2d.setTransform(transform);
        }
    }


    private void drawPageDefinition(@NotNull Graphics2D g2d)
    {
        DoubleDimension pageDefinition = report.getPageDefinition().getInnerPageSize();

        Color origColor = g2d.getColor();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, (int) (scaleFactor * pageDefinition.getWidth()), (int) (scaleFactor * pageDefinition.getHeight()));

        if (bandToplevelReportElement.getBackground() != null)
        {
            g2d.setColor(bandToplevelReportElement.getBackground());
            g2d.fillRect(0, 0, (int) (scaleFactor * pageDefinition.getWidth()), (int) (scaleFactor * pageDefinition.getHeight()));
        }

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawRect(0, 0, (int) (scaleFactor * pageDefinition.getWidth()), (int) (scaleFactor * pageDefinition.getHeight()));

        //int spacing = 10;
        //boolean gridEnabled = false;
        //
        //if (scaleFactor < 0.1)
        //{
        //    gridEnabled = false;
        //}
        //if (scaleFactor < 0.5)
        //{
        //    spacing = 100;
        //}
        //else if (scaleFactor < 1)
        //{
        //    spacing = 20;
        //}
        //else if (scaleFactor > 3)
        //{
        //    spacing = 5;
        //}

        g2d.setColor(new Color(240, 170, 170));
        LinealModel verticalLinealModel = getVerticalLinealModel();
        if (verticalLinealModel != null)
        {
            LinkedHashSet<GuideLine> verticalGuideLines = verticalLinealModel.getGuideLines();
            for (GuideLine guideLine : verticalGuideLines)
            {
                if (guideLine.isActive())
                {
                    double v = guideLine.getPosition() * scaleFactor;
                    int y = (int) v;
                    g2d.drawLine(0, y, getWidth(), y);
                }
            }
        }

        LinkedHashSet<GuideLine> horizontalGuideLines = getHorizontalLinealModel().getGuideLines();
        for (GuideLine guideLine : horizontalGuideLines)
        {
            if (guideLine.isActive())
            {
                double v = guideLine.getPosition() * scaleFactor;
                int x = (int) v;
                g2d.drawLine(x, 0, x, getHeight());
            }
        }

        g2d.setColor(Color.LIGHT_GRAY);

        //if (gridEnabled)
        //{
        //    //int bla = (int) (spacing*scaleFactor);
        //    //double startY = clipBounds.y/bla*bla;
        //    //if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportPanel.drawPageDefinition startY = " + startY);
        //
        //    int count = 0;
        //
        //    int skipY = (int) (clipBounds.y / (spacing * scaleFactor));
        //    double startY = skipY * (spacing * scaleFactor);
        //    //if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportPanel.drawPageDefinition startY = " + startY);
        //
        //    double y = startY;
        //    //for (double y = startY; y < scaleFactor * pageDefinition.getHeight(); y += (spacing * scaleFactor))
        //    while (y < clipBounds.y + clipBounds.height && y < pageDefinition.getHeight() * scaleFactor)
        //    {
        //        int skipX = (int) (clipBounds.x / (spacing * scaleFactor));
        //        double startX = skipX * (spacing * scaleFactor);
        //        double x = startX;
        //
        //        while (x < clipBounds.x + clipBounds.width && x < pageDefinition.getWidth() * scaleFactor)
        //        {
        //            //g2d.drawLine((int) x, 0, (int) x, (int) (pageDefinition.getHeight() * scaleFactor));
        //            g2d.drawRect((int) x, (int) y, 0, 0);
        //            count++;
        //            //line.setLine(x, 0, x, pageDefinition.getHeight());
        //            //g2d.draw(line);
        //
        //            x += spacing * scaleFactor;
        //        }
        //
        //
        //        y += spacing * scaleFactor;
        //    }
        //
        //    //if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportPanel.drawPageDefinition count = " + count);
        //}

        g2d.setColor(origColor);
    }


    public void propertyChange(@NotNull PropertyChangeEvent evt)
    {
        if (PropertyKeys.CHILD_ADDED.equals(evt.getPropertyName()) || PropertyKeys.CHILD_REMOVED.equals(evt.getPropertyName()))
        {
            detachPropertyChangeListener(report);
            attachPropertyChangeListener(report);

            reportElementSelectionModel.refresh();
        }
        else if (PropertyKeys.VISUAL_HEIGHT.equals(evt.getPropertyName()))
        {
            revalidate();
        }

        repaint();
    }


    private void addReportElement(@NotNull Point2D.Double pos, @NotNull ReportElement reportElement, @Nullable String humanPresentableName)
    {
        //noinspection ConstantConditions
        if (pos == null)
        {
            throw new IllegalArgumentException("pos must not be null");
        }
        //noinspection ConstantConditions
        if (reportElement == null)
        {
            throw new IllegalArgumentException("reportElement must not be null");
        }

        reportElement.setPosition(pos);
        reportElement.setOrigRectangle(new Rectangle2D.Double(pos.x, pos.y, reportElement.getMinimumSize().getWidth(), reportElement.getMinimumSize().getHeight()));
        bandToplevelReportElement.addChild(reportElement);

        if (humanPresentableName == null || !humanPresentableName.contains(ReportDialogConstants.UNSELECTED))
        {
            reportElementSelectionModel.clearSelection();
            reportElementSelectionModel.setSelection(Arrays.asList(reportElement));
        }
    }


    private void repaintElementRectangle(@NotNull ReportElement elementInfo, double sf)
    {
        repaint((int) (elementInfo.getRectangle().x * sf) - 5, (int) (elementInfo.getRectangle().y * sf) - 5, (int) (elementInfo.getRectangle().width * sf) + 10, (int) (elementInfo.getRectangle().height * sf) + 10);
    }


    private void initDragAndDrop()
    {
        @NonNls
        final DataFlavor dataFlavor = new DataFlavor("application/x-icore-reportelement;class=" + ReportElement.class.getName(), "ReportElement");

        //noinspection ResultOfObjectAllocationIgnored
        new DropTarget(this, new DropTargetListener()
        {
            public void dragEnter(@NotNull DropTargetDragEvent dtde)
            {
            }


            public void dragOver(@NotNull DropTargetDragEvent dtde)
            {
                Transferable transferable = dtde.getTransferable();

                if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                }

                if (transferable.isDataFlavorSupported(dataFlavor))
                {
                    try
                    {
                        ReportElement transferData = (ReportElement) transferable.getTransferData(dataFlavor);

                        Point p = dtde.getLocation();
                        double sf = getScaleFactor();

                        repaintElementRectangle(transferData, sf);

                        transferData.getRectangle().x = p.getX() / sf;
                        transferData.getRectangle().y = p.getY() / sf;

                        setDraggedReportElement(transferData);
                        repaintElementRectangle(transferData, sf);

                        dtde.acceptDrag(DnDConstants.ACTION_COPY);
                    }
                    catch (Exception e)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportPanel.dragOver ", e);
                        setDraggedReportElement(null);
                        repaint();
                        dtde.rejectDrag();
                    }
                }
            }


            public void dropActionChanged(@NotNull DropTargetDragEvent dtde)
            {
            }


            public void dragExit(@NotNull DropTargetEvent dte)
            {
                setDraggedReportElement(null);
                repaint();
            }


            public void drop(@NotNull DropTargetDropEvent dtde)
            {
                Undo undo = reportDialog.getUndo();
                ReportElement dre = getDraggedReportElement();

                if (dtde.isDataFlavorSupported(dataFlavor) && dre != null)
                {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);

                    setDraggedReportElement(null);
                    Point2D.Double pos = new Point2D.Double(dre.getRectangle().x, dre.getRectangle().y);

                    undo.startTransaction(UndoConstants.CREATE);
                    addReportElement(pos, dre, dtde.getTransferable().getTransferDataFlavors()[0].getHumanPresentableName());
                    getSelectionHandler().updateSubReportElements();
                    undo.endTransaction();
                    repaint();
                    dtde.dropComplete(true);
                    ReportPanel.this.requestFocusInWindow();
                }
                else
                {
                    if (dtde.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                    {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        try
                        {
                            List<?> list = (List<?>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                            for (Object entry : list)
                            {
                                if (entry instanceof File)
                                {
                                    File file = (File) entry;
                                    if (StaticImageReportElement.isValidImageType(file.getName()))
                                    {
                                        StaticImageReportElement reportElement = ReportElementInfoFactory.getInstance().getStaticImageReportElementInfo().createReportElement();
                                        reportElement.setUrl(file.toURI().toURL());

                                        undo.startTransaction(UndoConstants.CREATE);
                                        addReportElement(new Point2D.Double(dtde.getLocation().x, dtde.getLocation().y), reportElement, dtde.getTransferable().getTransferDataFlavors()[0].getHumanPresentableName());
                                        getSelectionHandler().updateSubReportElements();
                                        undo.endTransaction();
                                        repaint();
                                        dtde.dropComplete(true);
                                        ReportPanel.this.requestFocusInWindow();

                                        return;
                                    }
                                }
                            }
                        }
                        catch (UnsupportedFlavorException e)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportPanel.drop ", e);
                        }
                        catch (IOException e)
                        {
                            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportPanel.drop ", e);
                        }
                    }
                    else
                    {
                        dtde.rejectDrop();
                    }
                }

                ReportElementPalette palette = reportDialog.getReportElementPalette();
                if (palette != null)
                {
                    palette.dropCompleted();
                }
            }
        });
    }


    @NotNull
    public DataContext getDataContext()
    {
        return new DataContext()
        {
            @Nullable
            public Object getData(@Nullable String key)
            {
                if (CommandKeys.KEY_SELECTED_ELEMENTS_ARRAY.equals(key))
                {
                    ArrayList<ReportElement> selectedElementInfos = reportElementSelectionModel.getSelectedElementInfos();
                    return selectedElementInfos.toArray(new ReportElement[selectedElementInfos.size()]);
                }
                else if (CommandKeys.KEY_ELEMENTS_ARRAY.equals(key))
                {
                    ArrayList<ReportElement> reportElements = new ArrayList<ReportElement>();
                    addChildrenToCollection(reportElements, bandToplevelReportElement);
                    return reportElements.toArray(new ReportElement[reportElements.size()]);
                }
                else if (CommandKeys.KEY_REPORT_PANEL.equals(key))
                {
                    return ReportPanel.this;
                }
                else if (CommandKeys.KEY_REPORT_ELEMENT_MODEL.equals(key))
                {
                    return ReportPanel.this.getBandElementModel();
                }

                return null;
            }
        };
    }


    private void addChildrenToCollection(@NotNull ArrayList<ReportElement> reportElements, @NotNull ReportElement reportElement)
    {
        ArrayList<ReportElement> children = reportElement.getChildren();
        for (ReportElement element : children)
        {
            reportElements.add(element);
            addChildrenToCollection(reportElements, element);
        }
    }


    @NotNull
    public String getPlace()
    {
        return "ReportPanel";
    }


    public void startInlineEditing(@NotNull final ReportElement reportElement)
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportPanel.startInlineEditing ");
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportPanel.startInlineEditing reportElement = " + reportElement);

        stopInlineEditing();

        if (reportElement instanceof SubReportElement)
        {
            SubReportElement subReportElement = (SubReportElement) reportElement;
            FilePath filePath = subReportElement.getFilePath();
            File f = new File(filePath.getPath());

            File mainReportFile = reportDialog.getCurrentReportFile();

            ReportDialog dialog = ReportDesignerUtils.getOpenReportDialog(f);
            if (dialog != null)
            {
                dialog.toFront();

                Report sr = dialog.getReport();
                if (!(subReportElement.getReport() instanceof SubReport) && mainReportFile != null && sr instanceof SubReport)
                {
                    SubReport element = (SubReport) sr;
                    element.getSubReportDataElement().setCurrentMainReport(mainReportFile);
                }
                return;
            }

            if (f.exists())
            {
                ReportDialog reportDialog = ReportDesignerWindows.getInstance().createNewReportDialog(null);
                ReportDesignerUtils.openReport(f, reportDialog, mainReportFile);
            }
            else
            {
                SubReport subReport = ReportFactory.createEmptySubReport();
                ReportDialog subReportDialog = ReportDesignerWindows.getInstance().createNewReportDialog(subReport);
                File subReportFile = SaveReportCommand.save(true, subReportDialog, false);
                if (subReportFile != null)
                {
                    subReportElement.setFilePath(new FilePath(subReportFile.getAbsolutePath()));
                }
                subReport.getSubReportDataElement().setCurrentMainReport(mainReportFile);
            }
        }

        if (reportElement instanceof TextReportElement)
        {
            String text;
            if (reportElement instanceof TextFieldReportElement)
            {
                TextFieldReportElement textFieldReportElement = (TextFieldReportElement) reportElement;
                text = textFieldReportElement.getFieldName();
            }
            else if (reportElement instanceof MessageFieldReportElement)
            {
                MessageFieldReportElement messageFieldReportElement = (MessageFieldReportElement) reportElement;
                text = messageFieldReportElement.getFormatString();
            }
            else if (reportElement instanceof LabelReportElement)
            {
                LabelReportElement labelReportElement = (LabelReportElement) reportElement;
                text = labelReportElement.getText();
            }
            else if (reportElement instanceof ImageFieldReportElement)
            {
                ImageFieldReportElement labelReportElement = (ImageFieldReportElement) reportElement;
                text = labelReportElement.getFieldName();
            }
            else if (reportElement instanceof ImageURLFieldReportElement)
            {
                ImageURLFieldReportElement labelReportElement = (ImageURLFieldReportElement) reportElement;
                text = labelReportElement.getFieldName();
            }
            else if (reportElement instanceof AnchorFieldReportElement)
            {
                AnchorFieldReportElement anchorFieldReportElement = (AnchorFieldReportElement) reportElement;
                text = anchorFieldReportElement.getFieldName();
            }
            else if (reportElement instanceof DrawableFieldReportElement)
            {
                DrawableFieldReportElement labelReportElement = (DrawableFieldReportElement) reportElement;
                text = labelReportElement.getFieldName();
            }
            else
            {
                return;
            }

            TextReportElement textReportElement = (TextReportElement) reportElement;

            final JTextArea textArea = new JTextArea(text);
            UndoHelper.installUndoSupport(textArea);
            TextComponentHelper.installDefaultPopupMenu(textArea);

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            final JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setCaretPosition(textArea.getDocument().getLength());

            textArea.setFont(textReportElement.getFont());
            textArea.setForeground(textReportElement.getForeground());
            Color background = textReportElement.getBackground();
            if (background == null)
            {
                background = Color.WHITE;
            }
            textArea.setBackground(background);
            textArea.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(scrollPane);

            Rectangle2D.Double rect = new Rectangle2D.Double();
            rect.setRect(textReportElement.getRectangle());
            ReportElementUtilities.convertRectangle(rect, textReportElement, null);
            Rectangle bounds = rect.getBounds();

            bounds.x++;
            bounds.y++;
            bounds.width--;
            bounds.height--;

            ZoomModel zoomModel = reportDialog.getZoomModel();
            int zoomFactor = 1;
            if (zoomModel != null)//almost guaranteed
            {
                zoomFactor = zoomModel.getZoomFactor();
            }
            double zf = zoomFactor / 1000.;

            bounds.x *= zf;
            bounds.y *= zf;
            bounds.width *= zf;
            bounds.height *= zf;

            scrollPane.setBounds(bounds);
            textArea.addFocusListener(new FocusAdapter()
            {
                public void focusLost(@NotNull FocusEvent e)
                {
                    remove(scrollPane);
                    revalidate();
                    repaint();
                }
            });

            textArea.getInputMap().put(KeyStroke.getKeyStroke(CTRL_ENTER), INSERT_NEWLINE);
            textArea.getActionMap().put(INSERT_NEWLINE, new AbstractAction()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    textArea.insert("\n", textArea.getCaretPosition());
                }
            });

            textArea.getInputMap().put(KeyStroke.getKeyStroke(ESCAPE), CANCEL_EDIT);
            textArea.getActionMap().put(CANCEL_EDIT, new AbstractAction()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    cancelInlineEditing();
                }
            });

            textArea.getInputMap().put(KeyStroke.getKeyStroke(ENTER), APPROVE_EDIT);
            textArea.getActionMap().put(APPROVE_EDIT, new AbstractAction()
            {
                public void actionPerformed(@NotNull ActionEvent e)
                {
                    stopInlineEditing();
                }
            });


            revalidate();
            repaint();

            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    textArea.requestFocusInWindow();
                }
            });

            inlineEditingReportElement = reportElement;
            inlineEditingTextArea = textArea;
            inlineEditingScrollPane = scrollPane;
        }
        else if (reportElement instanceof ChartReportElement)
        {
            ChartReportElement chartReportElement = (ChartReportElement) reportElement;
            ChartEditor.showChartEditor(reportDialog.getRootJComponent(), TranslationManager.getInstance().getTranslation("R", "ChartEditor.Title"), chartReportElement, reportDialog);
        }
    }


    public void cancelInlineEditing()
    {
        if (inlineEditingScrollPane != null)
        {
            remove(inlineEditingScrollPane);
            repaint();
        }

        inlineEditingReportElement = null;
        inlineEditingTextArea = null;
        inlineEditingScrollPane = null;
    }


    public void stopInlineEditing()
    {
        if (inlineEditingReportElement != null && inlineEditingTextArea != null && inlineEditingScrollPane != null)
        {
            if (inlineEditingReportElement instanceof TextFieldReportElement)
            {
                TextFieldReportElement textFieldReportElement = (TextFieldReportElement) inlineEditingReportElement;
                textFieldReportElement.setFieldName(inlineEditingTextArea.getText());
            }
            else if (inlineEditingReportElement instanceof MessageFieldReportElement)
            {
                MessageFieldReportElement messageFieldReportElement = (MessageFieldReportElement) inlineEditingReportElement;
                messageFieldReportElement.setFormatString(inlineEditingTextArea.getText());
            }
            else if (inlineEditingReportElement instanceof LabelReportElement)
            {
                LabelReportElement labelReportElement = (LabelReportElement) inlineEditingReportElement;
                labelReportElement.setText(inlineEditingTextArea.getText());
            }
            else if (inlineEditingReportElement instanceof ImageFieldReportElement)
            {
                ImageFieldReportElement labelReportElement = (ImageFieldReportElement) inlineEditingReportElement;
                labelReportElement.setFieldName(inlineEditingTextArea.getText());
            }
            else if (inlineEditingReportElement instanceof ImageURLFieldReportElement)
            {
                ImageURLFieldReportElement labelReportElement = (ImageURLFieldReportElement) inlineEditingReportElement;
                labelReportElement.setFieldName(inlineEditingTextArea.getText());
            }
            else if (inlineEditingReportElement instanceof AnchorFieldReportElement)
            {
                AnchorFieldReportElement labelReportElement = (AnchorFieldReportElement) inlineEditingReportElement;
                labelReportElement.setFieldName(inlineEditingTextArea.getText());
            }
            else if (inlineEditingReportElement instanceof DrawableFieldReportElement)
            {
                DrawableFieldReportElement labelReportElement = (DrawableFieldReportElement) inlineEditingReportElement;
                labelReportElement.setFieldName(inlineEditingTextArea.getText());
            }

            remove(inlineEditingScrollPane);
            revalidate();
            repaint();

            inlineEditingReportElement = null;
            inlineEditingTextArea = null;
            inlineEditingScrollPane = null;
        }
    }

}
