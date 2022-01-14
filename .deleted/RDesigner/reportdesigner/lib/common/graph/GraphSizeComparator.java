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

import java.util.Comparator;


public class GraphSizeComparator implements Comparator<Graph>
{

    private boolean ascending;


    public GraphSizeComparator(boolean ascending)
    {
        this.ascending = ascending;
    }


    public int compare(@NotNull Graph g1, @NotNull Graph g2)
    {
        int g1Size = g1.getNumberOfVertices();
        int g2Size = g2.getNumberOfVertices();
        if (g1Size < g2Size)
        {
            return ascending ? -1 : 1;
        }
        else if (g1Size > g2Size)
        {
            return ascending ? 1 : -1;
        }
        else
        {
            return 0;
        }
    }

}
