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
package org.pentaho.reportdesigner.crm.report.model;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.GraphicsContext;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataSetPlugin;
import org.pentaho.reportdesigner.crm.report.datasetplugin.DataSetPluginRegistry;
import org.pentaho.reportdesigner.crm.report.datasetplugin.properties.PropertiesDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.model.functions.FunctionGenerator;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfo;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;
import org.pentaho.reportdesigner.lib.client.util.MathUtils;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 13.10.2005
 * Time: 12:19:15
 */
public abstract class ReportElement implements XMLExternalizable, Cloneable
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ReportElement.class.getName());

    @NotNull
    private PropertyChangeSupport propertyChangeSupport;

    @Nullable
    private ReportElement parent;
    @NotNull
    private ArrayList<ReportElement> children;

    @NotNull
    private Rectangle2D.Double origRectangle;
    @NotNull
    private Rectangle2D.Double rectangle;

    @NotNull
    private Point2D.Double position;

    @NotNull
    private DoubleDimension minimumSize;
    @NotNull
    private DoubleDimension preferredSize;
    @NotNull
    private DoubleDimension maximumSize;
    @NotNull
    private ElementPadding padding;
    @NotNull
    private ElementBorderDefinition elementBorder;

    @Nullable
    private Color background;

    private boolean dynamicContent;

    protected boolean invalid;

    @NotNull
    private String name;

    @NotNull
    private StyleExpressions styleExpressions;

    @Nullable
    private Undo undo;

    private double elementRepaintBorder;


    protected ReportElement()
    {
        propertyChangeSupport = new PropertyChangeSupport(this);

        children = new ArrayList<ReportElement>();

        origRectangle = new Rectangle2D.Double();
        rectangle = new Rectangle2D.Double();

        position = new Point2D.Double();
        minimumSize = new DoubleDimension();
        preferredSize = new DoubleDimension();
        maximumSize = new DoubleDimension();

        background = null;

        dynamicContent = false;

        invalid = true;

        //element properties
        this.name = "Element@" + System.identityHashCode(this);//NON-NLS

        styleExpressions = new StyleExpressions();

        padding = new ElementPadding();
        elementBorder = new ElementBorderDefinition();

        elementRepaintBorder = 0;
    }


    @Nullable
    public Undo getUndo()
    {
        return undo;
    }


    public void setUndo(@Nullable Undo undo)
    {
        this.undo = undo;

        for (ReportElement reportElement : children)
        {
            reportElement.setUndo(undo);
        }
    }


    @NotNull
    public String getName()
    {
        return name;
    }


    public void setName(@NonNls @NotNull final String name)
    {
        //noinspection ConstantConditions
        if (name == null)
        {
            throw new IllegalArgumentException("name must not be null");
        }

        final String oldName = this.name;
        this.name = name;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.NAME);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setName(oldName);
                }


                public void redo()
                {
                    setName(name);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.NAME, oldName, name);
    }


    @Nullable
    public ReportElement getParent()
    {
        return parent;
    }


    void setParent(@Nullable final ReportElement parent)
    {
        this.parent = parent;
    }


    @Nullable
    public Report getReport()
    {
        if (parent == null)
        {
            return null;
        }
        else
        {
            return parent.getReport();
        }
    }


    @NotNull
    public ArrayList<ReportElement> getChildren()
    {
        return children;
    }


    public boolean isDescendant(@NotNull ReportElement parent)
    {
        ReportElement p = this;
        while ((p = p.getParent()) != null)
        {
            //noinspection ObjectEquality
            if (p == parent)
            {
                return true;
            }
        }

        return false;
    }


    public void addChild(@NotNull final ReportElement child)
    {
        //noinspection ConstantConditions
        if (child == null)
        {
            throw new IllegalArgumentException("child must not be null");
        }

        Undo undo = getUndo();
        child.setUndo(undo);

        final ReportElement oldParent = child.getParent();
        if (oldParent != null)
        {
            oldParent.removeChild(child);
        }
        child.setParent(this);
        children.add(child);

        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(UndoConstants.ADD_CHILD);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    removeChild(child);
                    child.setParent(oldParent);
                }


                public void redo()
                {
                    addChild(child);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.CHILD_ADDED, null, child);
    }


    public void removeChild(@NotNull final ReportElement reportElement)
    {
        //noinspection ConstantConditions
        if (reportElement == null)
        {
            throw new IllegalArgumentException("reportElement must not be null");
        }

        final int index = children.indexOf(reportElement);
        boolean remomved = children.remove(reportElement);
        if (remomved)
        {
            reportElement.setParent(null);

            Undo undo = getUndo();
            if (undo != null && !undo.isInProgress())
            {
                undo.startTransaction(UndoConstants.REMOVE_CHILD);
                undo.add(new UndoEntry()
                {
                    public void undo()
                    {
                        insertChild(reportElement, index);
                    }


                    public void redo()
                    {
                        removeChild(reportElement);
                    }
                });
                undo.endTransaction();
            }

            firePropertyChange(PropertyKeys.CHILD_REMOVED, null, reportElement);
        }
    }


    public void insertChild(@NotNull final ReportElement child, final int index)
    {
        //noinspection ConstantConditions
        if (child == null)
        {
            throw new IllegalArgumentException("child must not be null");
        }

        Undo undo = getUndo();
        child.setUndo(undo);

        final ReportElement oldParent = child.getParent();
        child.setParent(this);
        children.add(index, child);

        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(UndoConstants.ADD_CHILD);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    removeChild(child);
                    child.setParent(oldParent);
                }


                public void redo()
                {
                    insertChild(child, index);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.CHILD_ADDED, null, child);
    }


    public void moveChildDown(@NotNull final ReportElement child)
    {
        int index = children.indexOf(child);
        if (index > 0)
        {
            Undo undo = getUndo();
            if (undo != null && !undo.isInProgress())
            {
                undo.startTransaction(UndoConstants.MOVE_CHILD_DOWN);
                undo.add(new UndoEntry()
                {
                    public void undo()
                    {
                        moveChildUp(child);
                    }


                    public void redo()
                    {
                        moveChildDown(child);
                    }
                });
                undo.endTransaction();
            }

            children.remove(child);
            children.add(index - 1, child);
            firePropertyChange(PropertyKeys.CHILD_MOVED, null, child);
        }
    }


    public void moveChildUp(@NotNull final ReportElement child)
    {
        int index = children.indexOf(child);
        if (index != -1 && index < children.size() - 1)
        {
            Undo undo = getUndo();
            if (undo != null && !undo.isInProgress())
            {
                undo.startTransaction(UndoConstants.MOVE_CHILD_UP);
                undo.add(new UndoEntry()
                {
                    public void undo()
                    {
                        moveChildDown(child);
                    }


                    public void redo()
                    {
                        moveChildUp(child);
                    }
                });
                undo.endTransaction();
            }

            children.remove(child);
            children.add(index + 1, child);
            firePropertyChange(PropertyKeys.CHILD_MOVED, null, child);
        }
    }


    @NotNull
    public StyleExpressions getStyleExpressions()
    {
        return styleExpressions;
    }


    public void setStyleExpressions(@NotNull final StyleExpressions styleExpressions)
    {
        //noinspection ConstantConditions
        if (styleExpressions == null)
        {
            throw new IllegalArgumentException("styleExpressions must not be null");
        }

        final StyleExpressions oldStyleExpressions = this.styleExpressions;
        this.styleExpressions = styleExpressions;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.STYLE_EXPRESSIONS);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setStyleExpressions(oldStyleExpressions);
                }


                public void redo()
                {
                    setStyleExpressions(styleExpressions);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.STYLE_EXPRESSIONS, oldStyleExpressions, styleExpressions);
        this.styleExpressions = styleExpressions;
    }


    @NotNull
    public Rectangle2D.Double getOrigRectangle()
    {
        return origRectangle;
    }


    public void setOrigRectangle(@NotNull final Rectangle2D.Double origRectangle)
    {
        //noinspection ConstantConditions
        if (origRectangle == null)
        {
            throw new IllegalArgumentException("origRectangle must not be null");
        }

        final Rectangle2D.Double oldOrigRectangle = this.origRectangle;
        this.origRectangle = origRectangle;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.ORIG_RECTANGLE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setOrigRectangle(oldOrigRectangle);
                }


                public void redo()
                {
                    setOrigRectangle(origRectangle);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.ORIG_RECTANGLE, oldOrigRectangle, origRectangle);
    }


    @NotNull
    public Rectangle2D.Double getRectangle()
    {
        return rectangle;
    }


    @NotNull
    public Rectangle2D.Double getContentRectangle()
    {
        Rectangle2D.Double rect = getRectangle();

        double x;
        double y;
        double w;
        double h;

        if (getElementBorder().isSameBorderForAllSides())
        {
            x = rect.x + getPadding().getLeft() + getElementBorder().getTopSide().getPaintWidth();
            y = rect.y + getPadding().getTop() + getElementBorder().getTopSide().getPaintWidth();
            w = Math.max(0, rect.width - getPadding().getLeft() - getPadding().getRight() - getElementBorder().getTopSide().getPaintWidth() - getElementBorder().getTopSide().getPaintWidth());
            h = Math.max(0, rect.height - getPadding().getTop() - getPadding().getBottom() - getElementBorder().getTopSide().getPaintWidth() - getElementBorder().getTopSide().getPaintWidth());
        }
        else
        {
            x = rect.x + getPadding().getLeft() + getElementBorder().getLeftSide().getPaintWidth();
            y = rect.y + getPadding().getTop() + getElementBorder().getTopSide().getPaintWidth();
            w = Math.max(0, rect.width - getPadding().getLeft() - getPadding().getRight() - getElementBorder().getLeftSide().getPaintWidth() - getElementBorder().getRightSide().getPaintWidth());
            h = Math.max(0, rect.height - getPadding().getTop() - getPadding().getBottom() - getElementBorder().getTopSide().getPaintWidth() - getElementBorder().getBottomSide().getPaintWidth());
        }

        return new Rectangle2D.Double(x, y, w, h);
    }


    @NotNull
    public Shape getBorderShape()
    {
        Rectangle2D.Double b = getBorderRectangle();

        double x = b.x;
        double y = b.y;
        double w = b.width;
        double h = b.height;

        double topLeftWidth = getElementBorder().getTopLeftEdge().getRadii().getWidth();
        double topLeftHeight = getElementBorder().getTopLeftEdge().getRadii().getHeight();

        double topRightWidth = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getWidth() : getElementBorder().getTopRightEdge().getRadii().getWidth();
        double topRightHeight = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getHeight() : getElementBorder().getTopRightEdge().getRadii().getHeight();

        double bottomRightWidth = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getWidth() : getElementBorder().getBottomRightEdge().getRadii().getWidth();
        double bottomRightHeight = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getHeight() : getElementBorder().getBottomRightEdge().getRadii().getHeight();

        double bottomLeftWidth = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getWidth() : getElementBorder().getBottomLeftEdge().getRadii().getWidth();
        double bottomLeftHeight = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getHeight() : getElementBorder().getBottomLeftEdge().getRadii().getHeight();

        GeneralPath generalPath = new GeneralPath();
        generalPath.moveTo((float) (x + topLeftWidth), (float) y);//1
        generalPath.lineTo((float) (x + w - topRightWidth), (float) y);//2
        generalPath.quadTo((float) (x + w), (float) y, (float) (x + w), (float) (y + topRightHeight));//3
        generalPath.lineTo((float) (x + w), (float) (y + h - bottomRightHeight));//4
        generalPath.quadTo((float) (x + w), (float) (y + h), (float) (x + w - bottomRightWidth), (float) (y + h));//5
        generalPath.lineTo((float) (x + bottomLeftWidth), (float) (y + h));//6
        generalPath.quadTo((float) x, (float) (y + h), (float) x, (float) (y + h - bottomLeftHeight));//7
        generalPath.lineTo((float) x, (float) (y + topLeftHeight));//8
        generalPath.quadTo((float) x, (float) y, (float) (x + topLeftWidth), (float) y);//1
        generalPath.closePath();

        return generalPath;
    }


    @NotNull
    public Shape getBorderTopShape()
    {
        Rectangle2D.Double b = getBorderRectangle();

        double x = b.x;
        double y = b.y;
        double w = b.width;
        //double h = b.height;

        double topLeftWidth = getElementBorder().getTopLeftEdge().getRadii().getWidth();
        double topLeftHeight = getElementBorder().getTopLeftEdge().getRadii().getHeight();

        double topRightWidth = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getWidth() : getElementBorder().getTopRightEdge().getRadii().getWidth();
        double topRightHeight = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getHeight() : getElementBorder().getTopRightEdge().getRadii().getHeight();

        GeneralPath generalPath = new GeneralPath();
        generalPath.moveTo((float) x, (float) (y + topLeftHeight));//8
        generalPath.quadTo((float) x, (float) y, (float) (x + topLeftWidth), (float) y);//1
        generalPath.lineTo((float) (x + w - topRightWidth), (float) y);//2
        generalPath.quadTo((float) (x + w), (float) y, (float) (x + w), (float) (y + topRightHeight));//3

        return generalPath;
    }


    @NotNull
    public Shape getBorderBottomShape()
    {
        Rectangle2D.Double b = getBorderRectangle();

        double x = b.x;
        double y = b.y;
        double w = b.width;
        double h = b.height;

        double bottomRightWidth = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getWidth() : getElementBorder().getBottomRightEdge().getRadii().getWidth();
        double bottomRightHeight = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getHeight() : getElementBorder().getBottomRightEdge().getRadii().getHeight();

        double bottomLeftWidth = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getWidth() : getElementBorder().getBottomLeftEdge().getRadii().getWidth();
        double bottomLeftHeight = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getHeight() : getElementBorder().getBottomLeftEdge().getRadii().getHeight();

        GeneralPath generalPath = new GeneralPath();
        generalPath.moveTo((float) (x + w), (float) (y + h - bottomRightHeight));//4
        generalPath.quadTo((float) (x + w), (float) (y + h), (float) (x + w - bottomRightWidth), (float) (y + h));//5
        generalPath.lineTo((float) (x + bottomLeftWidth), (float) (y + h));//6
        generalPath.quadTo((float) x, (float) (y + h), (float) x, (float) (y + h - bottomLeftHeight));//7

        return generalPath;
    }


    @NotNull
    public Shape getBorderLeftShape()
    {
        Rectangle2D.Double b = getBorderRectangle();

        double x = b.x;
        double y = b.y;
        //double w = b.width;
        double h = b.height;

        double topLeftWidth = getElementBorder().getTopLeftEdge().getRadii().getWidth();
        double topLeftHeight = getElementBorder().getTopLeftEdge().getRadii().getHeight();

        double bottomLeftWidth = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getWidth() : getElementBorder().getBottomLeftEdge().getRadii().getWidth();
        double bottomLeftHeight = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getHeight() : getElementBorder().getBottomLeftEdge().getRadii().getHeight();

        GeneralPath generalPath = new GeneralPath();
        generalPath.moveTo((float) (x + bottomLeftWidth), (float) (y + h));//6
        generalPath.quadTo((float) x, (float) (y + h), (float) x, (float) (y + h - bottomLeftHeight));//7
        generalPath.lineTo((float) x, (float) (y + topLeftHeight));//8
        generalPath.quadTo((float) x, (float) y, (float) (x + topLeftWidth), (float) y);//1

        return generalPath;
    }


    @NotNull
    public Shape getBorderRightShape()
    {
        Rectangle2D.Double b = getBorderRectangle();

        double x = b.x;
        double y = b.y;
        double w = b.width;
        double h = b.height;

        double topRightWidth = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getWidth() : getElementBorder().getTopRightEdge().getRadii().getWidth();
        double topRightHeight = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getHeight() : getElementBorder().getTopRightEdge().getRadii().getHeight();

        double bottomRightWidth = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getWidth() : getElementBorder().getBottomRightEdge().getRadii().getWidth();
        double bottomRightHeight = getElementBorder().isSameBorderForAllSides() ? getElementBorder().getTopLeftEdge().getRadii().getHeight() : getElementBorder().getBottomRightEdge().getRadii().getHeight();

        GeneralPath generalPath = new GeneralPath();
        generalPath.moveTo((float) (x + w - topRightWidth), (float) y);//2
        generalPath.quadTo((float) (x + w), (float) y, (float) (x + w), (float) (y + topRightHeight));//3
        generalPath.lineTo((float) (x + w), (float) (y + h - bottomRightHeight));//4
        generalPath.quadTo((float) (x + w), (float) (y + h), (float) (x + w - bottomRightWidth), (float) (y + h));//5

        return generalPath;
    }


    @NotNull
    private Rectangle2D.Double getBorderRectangle()
    {
        Rectangle2D.Double rect = getRectangle();

        double x;
        double y;
        double w;
        double h;

        if (getElementBorder().isSameBorderForAllSides())
        {
            x = rect.x + getElementBorder().getTopSide().getPaintWidth() / 2;
            y = rect.y + getElementBorder().getTopSide().getPaintWidth() / 2;
            w = Math.max(0, rect.width - getElementBorder().getTopSide().getPaintWidth() / 2 - getElementBorder().getTopSide().getPaintWidth() / 2);
            h = Math.max(0, rect.height - getElementBorder().getTopSide().getPaintWidth() / 2 - getElementBorder().getTopSide().getPaintWidth() / 2);
        }
        else
        {
            x = rect.x + +getElementBorder().getLeftSide().getPaintWidth() / 2;
            y = rect.y + getElementBorder().getTopSide().getPaintWidth() / 2;
            w = Math.max(0, rect.width - getElementBorder().getLeftSide().getPaintWidth() / 2 - getElementBorder().getRightSide().getPaintWidth() / 2);
            h = Math.max(0, rect.height - getElementBorder().getTopSide().getPaintWidth() / 2 - getElementBorder().getBottomSide().getPaintWidth() / 2);
        }
        return new Rectangle2D.Double(x, y, w, h);
    }


    public void setRectangle(@NotNull final Rectangle2D.Double rectangle)
    {
        //noinspection ConstantConditions
        if (rectangle == null)
        {
            throw new IllegalArgumentException("rectangle must not be null");
        }

        final Rectangle2D.Double oldRectangle = this.rectangle;
        this.rectangle = rectangle;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.RECTANGLE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setRectangle(oldRectangle);
                }


                public void redo()
                {
                    setRectangle(rectangle);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.RECTANGLE, oldRectangle, rectangle);
    }


    @NotNull
    public Point2D.Double getPosition()
    {
        return position;
    }


    public void setPosition(@NotNull final Point2D.Double position)
    {
        //noinspection ConstantConditions
        if (position == null)
        {
            throw new IllegalArgumentException("position must not be null");
        }

        final Point2D.Double oldPosition = this.position;
        this.position = position;

        if (position.equals(oldPosition))
        {
            return;
        }

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.POSITION);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setPosition(oldPosition);
                }


                public void redo()
                {
                    setPosition(position);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.POSITION, oldPosition, position);
    }


    @NotNull
    public DoubleDimension getMinimumSize()
    {
        return minimumSize;
    }


    public void setMinimumSize(@NotNull final DoubleDimension minimumSize)
    {
        final DoubleDimension oldMinimumSize = this.minimumSize;
        if (minimumSize.getWidth() < 0)
        {
            minimumSize.setSize(0, minimumSize.getHeight());
        }
        if (minimumSize.getHeight() < 0)
        {
            minimumSize.setSize(minimumSize.getWidth(), 0);
        }

        this.minimumSize = minimumSize;

        if (minimumSize.equals(oldMinimumSize))
        {
            return;
        }

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.MINIMUM_SIZE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setMinimumSize(oldMinimumSize);
                }


                public void redo()
                {
                    setMinimumSize(minimumSize);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.MINIMUM_SIZE, oldMinimumSize, minimumSize);
    }


    @NotNull
    public DoubleDimension getPreferredSize()
    {
        return preferredSize;
    }


    public void setPreferredSize(@NotNull final DoubleDimension preferredSize)
    {
        final DoubleDimension oldPreferredSize = this.preferredSize;
        this.preferredSize = preferredSize;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.PREFERRED_SIZE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setPreferredSize(oldPreferredSize);
                }


                public void redo()
                {
                    setPreferredSize(preferredSize);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.PREFERRED_SIZE, oldPreferredSize, preferredSize);
    }


    @NotNull
    public DoubleDimension getMaximumSize()
    {
        return maximumSize;
    }


    public void setMaximumSize(@NotNull final DoubleDimension maximumSize)
    {
        final DoubleDimension oldMaximumSize = this.maximumSize;
        this.maximumSize = maximumSize;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.MAXIMUM_SIZE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setMaximumSize(oldMaximumSize);
                }


                public void redo()
                {
                    setMaximumSize(maximumSize);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.MAXIMUM_SIZE, oldMaximumSize, maximumSize);
    }


    @Nullable
    public Color getBackground()
    {
        return background;
    }


    public void setBackground(@Nullable final Color background)
    {
        final Color oldBackground = this.background;
        this.background = background;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.BACKGROUND);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setBackground(oldBackground);
                }


                public void redo()
                {
                    setBackground(background);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.BACKGROUND, oldBackground, background);
    }


    @NotNull
    public ElementPadding getPadding()
    {
        return padding;
    }


    public void setPadding(@NotNull final ElementPadding padding)
    {
        final ElementPadding oldPadding = this.padding;
        this.padding = padding;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.PADDING);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setPadding(oldPadding);
                }


                public void redo()
                {
                    setPadding(padding);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.PADDING, oldPadding, padding);
    }


    @NotNull
    public ElementBorderDefinition getElementBorder()
    {
        return elementBorder;
    }


    public void setElementBorder(@NotNull final ElementBorderDefinition elementBorder)
    {
        final ElementBorderDefinition oldElementBorder = this.elementBorder;
        this.elementBorder = elementBorder;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.ELEMENT_BORDER);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setElementBorder(oldElementBorder);
                }


                public void redo()
                {
                    setElementBorder(elementBorder);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.ELEMENT_BORDER, oldElementBorder, elementBorder);
    }


    public boolean isDynamicContent()
    {
        return dynamicContent;
    }


    public void setDynamicContent(final boolean dynamicContent)
    {
        final boolean oldDynamicContent = this.dynamicContent;
        this.dynamicContent = dynamicContent;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.DYNAMIC_CONTENT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setDynamicContent(oldDynamicContent);
                }


                public void redo()
                {
                    setDynamicContent(dynamicContent);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.DYNAMIC_CONTENT, oldDynamicContent, dynamicContent);
    }


    public double getElementRepaintBorder()
    {
        return elementRepaintBorder;
    }


    public void setElementRepaintBorder(double elementRepaintBorder)
    {
        this.elementRepaintBorder = elementRepaintBorder;
    }


    public void invalidate()
    {
        invalid = true;
        if (parent != null)
        {
            parent.invalidate();
        }
    }


    public void validate()
    {
        if (!invalid)
        {
            return;
        }

        for (ReportElement reportElement : children)
        {
            reportElement.validate();
        }

        //convert position to absolute coordinate
        origRectangle.setRect(position.getX(), position.getY(), minimumSize.getWidth(), minimumSize.getHeight());
        rectangle.setRect(position.getX(), position.getY(), minimumSize.getWidth(), minimumSize.getHeight());

        for (ReportElement reportElement : children)
        {
            origRectangle = (Rectangle2D.Double) origRectangle.createUnion(reportElement.getOrigRectangle());
            rectangle = (Rectangle2D.Double) rectangle.createUnion(reportElement.getOrigRectangle());
        }

        //adjust minimum size to contain all children
        //scream if there are elements >100%

        //convert relative positions to absolute ones
    }


    @NotNull
    public String toString()
    {
        return getName();
    }


    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }


    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }


    @NotNull
    public PropertyChangeListener[] getPropertyChangeListeners()
    {
        return propertyChangeSupport.getPropertyChangeListeners();
    }


    public void addPropertyChangeListener(@NotNull String propertyName, @NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }


    public void removePropertyChangeListener(@NotNull String propertyName, @NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }


    public void firePropertyChange(@NotNull @NonNls String propertyName, @Nullable Object oldValue, @Nullable Object newValue)
    {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }


    public void firePropertyChange(@NotNull @NonNls String propertyName, int oldValue, int newValue)
    {
        propertyChangeSupport.firePropertyChange(propertyName, Integer.valueOf(oldValue), Integer.valueOf(newValue));
    }


    public void firePropertyChange(@NotNull @NonNls String propertyName, boolean oldValue, boolean newValue)
    {
        propertyChangeSupport.firePropertyChange(propertyName, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
    }


    public void firePropertyChange(@NotNull @NonNls String propertyName, double oldValue, double newValue)
    {
        propertyChangeSupport.firePropertyChange(propertyName, Double.valueOf(oldValue), Double.valueOf(newValue));
    }


    public void fireIndexedPropertyChange(@NotNull String propertyName, int index, @Nullable Object oldValue, @Nullable Object newValue)
    {
        propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }


    public void paint(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        for (ReportElement reportElement : children)
        {
            reportElement.paint(graphicsContext, g2d);
        }
    }


    public void paintBackroundAndBorder(@NotNull Graphics2D g2d)
    {
        Color bgColor = getBackground();
        if (bgColor != null)
        {
            g2d.setColor(bgColor);
            g2d.fill(getBorderShape());
        }

        if (getElementBorder().isSameBorderForAllSides())
        {
            if (getElementBorder().getTopSide().getType() != ElementBorderDefinition.BorderType.NONE)
            {
                g2d.setColor(getElementBorder().getTopSide().getColor());
                g2d.setStroke(getElementBorder().getTopSide().getStroke());
                g2d.draw(getBorderShape());
            }
        }
        else
        {
            if (getElementBorder().getTopSide().getType() != ElementBorderDefinition.BorderType.NONE)
            {
                g2d.setColor(getElementBorder().getTopSide().getColor());
                g2d.setStroke(getElementBorder().getTopSide().getStroke());
                g2d.draw(getBorderTopShape());
            }

            if (getElementBorder().getRightSide().getType() != ElementBorderDefinition.BorderType.NONE)
            {
                g2d.setColor(getElementBorder().getRightSide().getColor());
                g2d.setStroke(getElementBorder().getRightSide().getStroke());
                g2d.draw(getBorderRightShape());
            }

            if (getElementBorder().getBottomSide().getType() != ElementBorderDefinition.BorderType.NONE)
            {
                g2d.setColor(getElementBorder().getBottomSide().getColor());
                g2d.setStroke(getElementBorder().getBottomSide().getStroke());
                g2d.draw(getBorderBottomShape());
            }

            if (getElementBorder().getLeftSide().getType() != ElementBorderDefinition.BorderType.NONE)
            {
                g2d.setColor(getElementBorder().getLeftSide().getColor());
                g2d.setStroke(getElementBorder().getLeftSide().getStroke());
                g2d.draw(getBorderLeftShape());
            }
        }


    }


    @NotNull
    public ReportElement[] getPath()
    {
        ArrayList<ReportElement> path = new ArrayList<ReportElement>();
        path.add(this);

        ReportElement parent = this;
        while ((parent = parent.getParent()) != null)
        {
            path.add(0, parent);
        }

        return path.toArray(new ReportElement[path.size()]);
    }


    public void disposeCachedData()
    {
    }


    public abstract void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException;


    @NotNull
    public ReportElement clone() throws CloneNotSupportedException
    {
        ReportElement re = (ReportElement) super.clone();
        re.propertyChangeSupport = new PropertyChangeSupport(re);
        re.parent = null;
        re.children = new ArrayList<ReportElement>();
        for (int i = 0; i < children.size(); i++)
        {
            ReportElement reportElement = children.get(i);
            ReportElement child = reportElement.clone();
            re.children.add(child);
            child.setParent(re);
        }
        re.origRectangle = (Rectangle2D.Double) origRectangle.clone();
        re.rectangle = (Rectangle2D.Double) rectangle.clone();
        re.position = (Point2D.Double) position.clone();
        re.minimumSize = (DoubleDimension) minimumSize.clone();
        re.preferredSize = (DoubleDimension) preferredSize.clone();
        re.maximumSize = (DoubleDimension) maximumSize.clone();
        re.name = name;
        re.undo = undo;

        return re;
    }


    public final void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        externalizeAttributes(xmlWriter, xmlContext);
        externalizeElements(xmlWriter, xmlContext);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeProperty(XMLConstants.NAME, name);
        xmlWriter.writeProperty(PropertyKeys.POSITION, ObjectConverterFactory.getInstance().getPoint2DConverter().getString(position));

        if (!isDefault(minimumSize))
        {
            xmlWriter.writeProperty(PropertyKeys.MINIMUM_SIZE, ObjectConverterFactory.getInstance().getDoubleDimensionConverter().getString(minimumSize));
        }
        if (!isDefault(preferredSize))
        {
            xmlWriter.writeProperty(PropertyKeys.PREFERRED_SIZE, ObjectConverterFactory.getInstance().getDoubleDimensionConverter().getString(preferredSize));
        }
        if (!isDefault(maximumSize))
        {
            xmlWriter.writeProperty(PropertyKeys.MAXIMUM_SIZE, ObjectConverterFactory.getInstance().getDoubleDimensionConverter().getString(maximumSize));
        }
        Color bg = background;
        if (bg != null)
        {
            xmlWriter.writeProperty(PropertyKeys.BACKGROUND, ObjectConverterFactory.getInstance().getColorConverter().getString(bg));
        }
        if (dynamicContent)
        {
            xmlWriter.writeProperty(PropertyKeys.DYNAMIC_CONTENT, String.valueOf(dynamicContent));
        }

        if (!styleExpressions.getStyleExpressions().isEmpty())
        {
            xmlWriter.startElement(PropertyKeys.STYLE_EXPRESSIONS);
            styleExpressions.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.STYLE_EXPRESSIONS);
        }

        xmlWriter.startElement(PropertyKeys.PADDING);
        padding.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.PADDING);

        if (!elementBorder.isDefault())
        {
            xmlWriter.startElement(PropertyKeys.ELEMENT_BORDER);
            elementBorder.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.ELEMENT_BORDER);
        }

        for (ReportElement reportElement : children)
        {
            xmlWriter.startElement(PropertyKeys.CHILD);
            String name = reportElement.getClass().getName();
            if (name.startsWith(FunctionGenerator.PACKAGE_PREFIX))
            {
                name = name.substring(FunctionGenerator.PACKAGE_PREFIX.length());
            }
            xmlWriter.writeAttribute(PropertyKeys.TYPE, name);
            if (reportElement instanceof BandToplevelReportElement)
            {
                BandToplevelReportElement bandToplevelReportElement = (BandToplevelReportElement) reportElement;
                xmlWriter.writeAttribute(PropertyKeys.BAND_TOPLEVEL_TYPE, bandToplevelReportElement.getBandToplevelType().toString());
            }
            reportElement.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.CHILD);
        }
    }


    @SuppressWarnings({"EmptyMethod", "UnusedDeclaration"})
    protected final void externalizeAttributes(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext)
    {
    }


    private boolean isDefault(@NotNull DoubleDimension doubleDimension)
    {
        return MathUtils.approxEquals(doubleDimension.getWidth(), 0) && MathUtils.approxEquals(doubleDimension.getHeight(), 0);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        readAttributes(node);
        readElements(node, xmlContext);
    }


    private void readElements(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        ExpressionRegistry expressionRegistry = ExpressionRegistry.getInstance();

        while (!node.isFinished())
        {
            Object childNodeList = node.readNextChild();
            if (childNodeList instanceof XmlPullNode)
            {
                XmlPullNode child = (XmlPullNode) childNodeList;
                readElement(expressionRegistry, child, xmlContext);
            }
        }
    }


    @SuppressWarnings({"EmptyMethod", "UnusedDeclaration"})
    protected final void readAttributes(@NotNull XmlPullNode node)
    {
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        if (PropertyKeys.CHILD.equals(node.getRawName()))
        {
            ReportElementInfo reportElementInfo = getReportElementInfo(expressions, node);
            if (reportElementInfo != null)
            {
                ReportElement reportElement = reportElementInfo.createReportElement();
                reportElement.readObject(node, xmlContext);
                addChild(reportElement);
            }
            else
            {
                String className = node.getAttributeValueFromRawName(PropertyKeys.TYPE);
                LinkedHashSet<DataSetPlugin> dataSetPlugins = DataSetPluginRegistry.getInstance().getDataSetPlugins();
                boolean read = false;
                for (DataSetPlugin dataSetPlugin : dataSetPlugins)
                {
                    if (dataSetPlugin.canRead(className))
                    {
                        ReportElement reportElement = dataSetPlugin.createEmptyInstance(className);
                        reportElement.readObject(node, xmlContext);
                        addChild(reportElement);
                        read = true;
                        break;//ensures not to read from the sampleDB plugin and a second time by the JDBCDataSetPlugin
                    }
                }
                if (!read)
                {
                    //noinspection ThrowableInstanceNeverThrown
                    UncaughtExcpetionsModel.getInstance().addException(new Throwable("Could not read complete file, Plugin missing? Element = " + className));//NON-NLS
                }
            }
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.NAME.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            name = XMLUtils.readProperty(PropertyKeys.NAME, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.POSITION.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            position = ObjectConverterFactory.getInstance().getPoint2DConverter().getObject(XMLUtils.readProperty(PropertyKeys.POSITION, node));
            //layout managers generate garbage if these properties are not correct
            rectangle = new Rectangle2D.Double(position.x, position.y, minimumSize.getWidth(), minimumSize.getHeight());
            origRectangle = new Rectangle2D.Double(position.x, position.y, minimumSize.getWidth(), minimumSize.getHeight());
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.MINIMUM_SIZE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            minimumSize = ObjectConverterFactory.getInstance().getDoubleDimensionConverter().getObject(XMLUtils.readProperty(PropertyKeys.MINIMUM_SIZE, node));
            //layout managers generate garbage if these properties are not correct
            rectangle = new Rectangle2D.Double(position.x, position.y, minimumSize.getWidth(), minimumSize.getHeight());
            origRectangle = new Rectangle2D.Double(position.x, position.y, minimumSize.getWidth(), minimumSize.getHeight());
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.PREFERRED_SIZE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            preferredSize = ObjectConverterFactory.getInstance().getDoubleDimensionConverter().getObject(XMLUtils.readProperty(PropertyKeys.PREFERRED_SIZE, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.MAXIMUM_SIZE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            maximumSize = ObjectConverterFactory.getInstance().getDoubleDimensionConverter().getObject(XMLUtils.readProperty(PropertyKeys.MAXIMUM_SIZE, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.BACKGROUND.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            background = ObjectConverterFactory.getInstance().getColorConverter().getObject(XMLUtils.readProperty(PropertyKeys.BACKGROUND, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.DYNAMIC_CONTENT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            dynamicContent = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.DYNAMIC_CONTENT, node));
        }
        else if (PropertyKeys.STYLE_EXPRESSIONS.equals(node.getRawName()))
        {
            styleExpressions = new StyleExpressions();
            styleExpressions.readObject(node, xmlContext);
        }
        else if (PropertyKeys.PADDING.equals(node.getRawName()))
        {
            padding = new ElementPadding();
            padding.readObject(node, xmlContext);
        }
        else if (PropertyKeys.ELEMENT_BORDER.equals(node.getRawName()))
        {
            elementBorder = new ElementBorderDefinition();
            elementBorder.readObject(node, xmlContext);
        }
    }


    @Nullable
    protected ReportElementInfo getReportElementInfo(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node)
    {
        String className = node.getAttributeValueFromRawName(PropertyKeys.TYPE);

        try
        {
            Class clazz;
            //noinspection UnusedCatchParameter
            try
            {
                clazz = Class.forName(className);
            }
            catch (ClassNotFoundException e)
            {
                clazz = Class.forName(FunctionGenerator.PACKAGE_PREFIX + className);
            }

            if (className.equals(BandToplevelReportElement.class.getName()) ||
                className.equals(BandToplevelGroupReportElement.class.getName()) ||
                className.equals(BandToplevelPageReportElement.class.getName()) ||
                className.equals(BandToplevelItemReportElement.class.getName()))
            {
                String bandTopLevelType = node.getAttributeValueFromRawName(PropertyKeys.BAND_TOPLEVEL_TYPE);
                BandToplevelType bandToplevelType = BandToplevelType.valueOf(bandTopLevelType);
                return ReportElementInfoFactory.getInstance().getBandToplevelReportElementInfo(bandToplevelType);
            }
            else if (className.equals(LabelReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getLabelReportElementInfo();
            }
            else if (className.equals(DateFieldReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getDateFieldReportElementInfo();
            }
            else if (className.equals(TextFieldReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo();
            }
            else if (className.equals(MessageFieldReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getMessageFieldReportElementInfo();
            }
            else if (className.equals(NumberFieldReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getNumberFieldReportElementInfo();
            }
            else if (className.equals(ResourceFieldReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getResourceFieldReportElementInfo();
            }
            else if (className.equals(ResourceLabelReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getResourceLabelReportElementInfo();
            }
            else if (className.equals(ResourceMessageReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getResourceMessageReportElementInfo();
            }
            else if (className.equals(BandReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getBandReportElementInfo();
            }
            else if (className.equals(ReportGroups.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getReportGroupsElementInfo();
            }
            else if (className.equals(ReportGroup.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getReportGroupElementInfo();
            }
            else if (className.equals(ReportFunctionsElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getReportFunctionsElementInfo();
            }
            else if (className.equals(LineReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getLineReportElementInfo();
            }
            else if (className.equals(StaticImageReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getStaticImageReportElementInfo();
            }
            else if (className.equals(ImageFieldReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getImageFieldReportElementInfo();
            }
            else if (className.equals(ImageURLFieldReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getImageURLFieldReportElementInfo();
            }
            else if (className.equals(AnchorFieldReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getAnchorFieldReportElementInfo();
            }
            else if (className.equals(RectangleReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getRectangleReportElementInfo();
            }
            else if (className.equals(EllipseReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getEllipseReportElementInfo();
            }
            else if (className.equals(DataSetsReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getDataSetsReportElementInfo();
            }
            else if (className.equals(PropertiesDataSetReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getPropertiesDataSetReportElementInfo();
            }
            else if (className.equals(DrawableFieldReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getDrawableFieldReportElementInfo();
            }
            else if (className.equals(ChartReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getChartReportElementInfo();
            }
            else if (className.equals(SubReportElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getSubReportElementInfo();
            }
            else if (className.equals(SubReportDataElement.class.getName()))
            {
                return ReportElementInfoFactory.getInstance().getSubReportDataElementInfo();
            }
            else if (expressions.getJFreeReportExpressionToWrapperClassesMap().containsValue(clazz))
            {
                return ReportElementInfoFactory.getInstance().getReportFunctionElementInfo(clazz.getName());
            }
        }
        catch (ClassNotFoundException e)
        {
            //perhaps an old function -> try to map to new one
            int index = className.lastIndexOf('.');
            if (index != -1)
            {
                String simpleClassName = className.substring(index + 1);
                Class wrapperClass = expressions.getWrapperClassForOldFunction(simpleClassName);
                if (wrapperClass != null)
                {
                    return ReportElementInfoFactory.getInstance().getReportFunctionElementInfo(wrapperClass.getName());
                }
                else
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportElement.getReportElementInfo searched for an old function with name = " + simpleClassName + " but did not find anything useful");
                }
            }
        }

        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportElement.getReportElementInfo did not find type = " + className);
        return null;
    }


}
