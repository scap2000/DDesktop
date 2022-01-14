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


public abstract class Vertex
{

    @NotNull
    private IncidenceContainer incidenceContainer;


    public Vertex()
    {
        incidenceContainer = new IncidenceContainer();
    }


    @NotNull
    public IncidenceContainer getIncidenceContainer()
    {
        return incidenceContainer;
    }


    public void setIncidenceContainer(@NotNull IncidenceContainer incidenceContainer)
    {
        this.incidenceContainer = incidenceContainer;
    }


    public void detachEdges()
    {
        Edge[] edges = incidenceContainer.getIncidentEdges();
        for (Edge edge : edges)
        {
            edge.detachFromVertices();
        }
    }


    public boolean hasConnectionTo(@NotNull Vertex vertex)
    {
        return getIncidenceContainer().hasConnectionTo(vertex);
    }


}
