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

import org.gjt.xpp.XmlPullNode;
import org.gjt.xpp.XmlPullParserException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.UndoConstants;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;
import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 26.01.2006
 * Time: 10:44:19
 */
public class LinealModel implements XMLExternalizable
{
    @Nullable
    private Report report;
    @NotNull
    private LinkedHashSet<GuideLine> guideLines;

    @NotNull
    private LinkedHashSet<LinealModelListener> linealModelListeners;


    public LinealModel(@Nullable Report report)
    {
        this.report = report;
        guideLines = new LinkedHashSet<GuideLine>();
        linealModelListeners = new LinkedHashSet<LinealModelListener>();
    }


    public void setReport(@Nullable Report report)
    {
        this.report = report;
    }


    @Nullable
    public Report getReport()
    {
        return report;
    }


    @NotNull
    public LinkedHashSet<GuideLine> getGuideLines()
    {
        return guideLines;
    }


    public void setActive(@NotNull final GuideLine guideLine, final boolean active)
    {
        if (guideLine.isActive() != active)
        {
            final boolean oldActive = guideLine.isActive();

            Report report = this.report;

            if (report != null)
            {
                Undo undo = report.getUndo();
                if (undo != null && !undo.isInProgress())
                {
                    undo.startTransaction(UndoConstants.SET_ACTIVE);
                    undo.add(new UndoEntry()
                    {
                        public void undo()
                        {
                            setActive(guideLine, oldActive);
                        }


                        public void redo()
                        {
                            setActive(guideLine, active);
                        }
                    });
                    undo.endTransaction();
                }
            }

            guideLine.setActive(active);
            LinkedHashSet<LinealModelListener> lml = new LinkedHashSet<LinealModelListener>(linealModelListeners);
            for (LinealModelListener linealModelListener : lml)
            {
                linealModelListener.activationChanged(guideLine);
            }
        }
    }


    public void setPosition(@NotNull final GuideLine guideLine, final double position)
    {
        final double oldPosition = guideLine.getPosition();
        guideLine.setPosition(position);

        Report report = this.report;

        if (report != null)
        {
            Undo undo = report.getUndo();
            if (undo != null && !undo.isInProgress())
            {
                undo.startTransaction(UndoConstants.SET_POSITION);
                undo.add(new UndoEntry()
                {
                    public void undo()
                    {
                        setPosition(guideLine, oldPosition);
                    }


                    public void redo()
                    {
                        setPosition(guideLine, position);
                    }
                });
                undo.endTransaction();
            }
        }

        LinkedHashSet<LinealModelListener> lml = new LinkedHashSet<LinealModelListener>(linealModelListeners);
        for (LinealModelListener linealModelListener : lml)
        {
            linealModelListener.positionChanged(guideLine, oldPosition);
        }
    }


    public void removeGuideLine(@NotNull final GuideLine guideLine)
    {
        boolean removed = guideLines.remove(guideLine);
        if (removed)
        {
            Report report = this.report;
            if (report != null)
            {
                Undo undo = report.getUndo();
                if (undo != null && !undo.isInProgress())
                {
                    undo.startTransaction(UndoConstants.REMOVE_GUIDE_LINE);
                    undo.add(new UndoEntry()
                    {
                        public void undo()
                        {
                            addGuidLine(guideLine);
                        }


                        public void redo()
                        {
                            removeGuideLine(guideLine);
                        }
                    });
                    undo.endTransaction();
                }
            }

            LinkedHashSet<LinealModelListener> lml = new LinkedHashSet<LinealModelListener>(linealModelListeners);
            for (LinealModelListener linealModelListener : lml)
            {
                linealModelListener.guidLineRemoved(guideLine);
            }
        }
    }


    public void addGuidLine(@NotNull final GuideLine guideLine)
    {
        boolean added = guideLines.add(guideLine);

        if (added)
        {
            Report report = this.report;
            if (report != null)
            {
                Undo undo = report.getUndo();
                if (undo != null && !undo.isInProgress())
                {
                    undo.startTransaction(UndoConstants.ADD_GUIDE_LINE);
                    undo.add(new UndoEntry()
                    {
                        public void undo()
                        {
                            removeGuideLine(guideLine);
                        }


                        public void redo()
                        {
                            addGuidLine(guideLine);
                        }
                    });
                    undo.endTransaction();
                }
            }

            LinkedHashSet<LinealModelListener> lml = new LinkedHashSet<LinealModelListener>(linealModelListeners);
            for (LinealModelListener linealModelListener : lml)
            {
                linealModelListener.guidLineAdded(guideLine);
            }
        }
    }


    public void addLinealModelListener(@NotNull LinealModelListener linealModelListener)
    {
        linealModelListeners.add(linealModelListener);
    }


    public void removeLinealModelListener(@NotNull LinealModelListener linealModelListener)
    {
        linealModelListeners.remove(linealModelListener);
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        for (GuideLine guideLine : guideLines)
        {
            xmlWriter.startElement(PropertyKeys.GUIDE_LINE);
            guideLine.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(PropertyKeys.GUIDE_LINE);
        }
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException, XmlPullParserException
    {
        while (!node.isFinished())
        {
            Object childNodeList = node.readNextChild();
            if (childNodeList instanceof XmlPullNode)
            {
                XmlPullNode child = (XmlPullNode) childNodeList;
                if (PropertyKeys.GUIDE_LINE.equals(child.getRawName()))
                {
                    GuideLine guideLine = new GuideLine(0, false);
                    guideLine.readObject(child, xmlContext);
                    guideLines.add(guideLine);
                }
            }
        }
    }
}
