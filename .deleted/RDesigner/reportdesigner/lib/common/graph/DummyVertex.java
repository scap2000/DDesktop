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
package org.pentaho.reportdesigner.lib.common.graph;

import org.jetbrains.annotations.NotNull;


public class DummyVertex extends Vertex
{

    private int width = 1;
    private int height = 1;
    @NotNull
    private Object obj;


    public DummyVertex(@NotNull Object obj)
    {
        this.obj = obj;
    }


    public int getWidth()
    {
        return height;
    }


    public int getHeight()
    {
        return width;
    }


    public int getOrderID()
    {
        return 0;
    }


    @NotNull
    public String toString()
    {
        return obj.toString();
    }

}
