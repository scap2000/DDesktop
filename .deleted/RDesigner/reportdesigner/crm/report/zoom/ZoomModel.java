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
package org.pentaho.reportdesigner.crm.report.zoom;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;

/**
 * User: Martin
 * Date: 03.02.2006
 * Time: 19:28:50
 */
public class ZoomModel
{
    @NotNull
    private LinkedHashSet<ZoomModelListener> zoomModelListeners;

    private int zoomFactor;//*1000


    public ZoomModel()
    {
        zoomModelListeners = new LinkedHashSet<ZoomModelListener>();
    }


    public int getZoomFactor()
    {
        return zoomFactor;
    }


    public void setZoomFactor(int zoomFactor)
    {
        int oldZoomFactor = this.zoomFactor;
        this.zoomFactor = zoomFactor;

        if (oldZoomFactor != zoomFactor)
        {
            notifyListeners(oldZoomFactor, zoomFactor);
        }
    }


    public void addZoomModelListener(@NotNull ZoomModelListener zoomModelListener)
    {
        zoomModelListeners.add(zoomModelListener);
    }


    public void removeZoomModelListener(@NotNull ZoomModelListener zoomModelListener)
    {
        zoomModelListeners.remove(zoomModelListener);
    }


    private void notifyListeners(int oldFactor, int newFactor)
    {
        LinkedHashSet<ZoomModelListener> set = new LinkedHashSet<ZoomModelListener>(zoomModelListeners);
        for (ZoomModelListener zoomModelListener : set)
        {
            zoomModelListener.zoomFactorChanged(oldFactor, newFactor);
        }
    }
}
