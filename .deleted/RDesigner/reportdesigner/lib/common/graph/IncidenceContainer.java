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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class IncidenceContainer
{

    @NotNull
    private Set<Edge> inIncidentEdges;
    @NotNull
    private Set<Edge> outIncidentEdges;

    @NotNull
    private Set<Edge> incidentEdges;


    public IncidenceContainer()
    {
        inIncidentEdges = new HashSet<Edge>();
        outIncidentEdges = new HashSet<Edge>();

        incidentEdges = new HashSet<Edge>();
    }


    public void addOutIncidentEdge(@NotNull Edge e)
    {
        outIncidentEdges.add(e);
        e.setEdgeNumber(outIncidentEdges.size());
        incidentEdges.add(e);
    }


    public void removeOutIncidentEdge(@NotNull Edge e)
    {
        outIncidentEdges.remove(e);
        incidentEdges.remove(e);
    }


    public void addInIncidentEdge(@NotNull Edge e)
    {
        inIncidentEdges.add(e);
        e.setEdgeNumber(inIncidentEdges.size());
        incidentEdges.add(e);
    }


    public void removeInIncidentEdge(@NotNull Edge e)
    {
        inIncidentEdges.remove(e);
        incidentEdges.remove(e);
    }


    @NotNull
    public Iterator<Edge> getIncidentEdgeIterator()
    {
        return incidentEdges.iterator();
    }


    @NotNull
    public Iterator<Edge> getInIncidentEdgeIterator()
    {
        return inIncidentEdges.iterator();
    }


    @NotNull
    public Iterator<Edge> getOutIncidentEdgeIterator()
    {
        return outIncidentEdges.iterator();
    }


    @NotNull
    public Edge[] getIncidentEdges()
    {
        return incidentEdges.toArray(new Edge[incidentEdges.size()]);
    }


    public int getIncidentEdgeCount()
    {
        return incidentEdges.size();
    }


    public int getInIncidentEdgeCount()
    {
        return inIncidentEdges.size();
    }


    public int getOutIncidentEdgeCount()
    {
        return outIncidentEdges.size();
    }


    public boolean hasConnectionTo(@NotNull Vertex vertex)
    {
        for (Edge edge : outIncidentEdges)
        {
            //noinspection ObjectEquality
            if (edge.getToVertex() == vertex)
            {
                return true;
            }
        }
        return false;
    }
}
