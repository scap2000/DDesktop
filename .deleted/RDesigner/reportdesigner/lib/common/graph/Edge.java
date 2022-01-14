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
import org.jetbrains.annotations.Nullable;


public class Edge
{
    private int cost;
    private int edgeNumber;
    @NotNull
    private Vertex fromVertex;
    @NotNull
    private Vertex toVertex;


    public Edge(@NotNull Vertex fromVertex, @NotNull Vertex toVertex, int cost)
    {
        this.cost = cost;
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;

        attachToVertices();
    }


    public void attachToVertices()
    {
        fromVertex.getIncidenceContainer().addOutIncidentEdge(this);
        toVertex.getIncidenceContainer().addInIncidentEdge(this);
    }


    public void detachFromVertices()
    {
        fromVertex.getIncidenceContainer().removeOutIncidentEdge(this);
        toVertex.getIncidenceContainer().removeInIncidentEdge(this);
    }


    @NotNull
    public Vertex getFromVertex()
    {
        return fromVertex;
    }


    public void setFromVertex(@NotNull Vertex fromVertex)
    {
        this.fromVertex = fromVertex;
    }


    @NotNull
    public Vertex getToVertex()
    {
        return toVertex;
    }


    public void setToVertex(@NotNull Vertex toVertex)
    {
        this.toVertex = toVertex;
    }


    public int getCost()
    {
        return cost;
    }


    public boolean equals(@Nullable Object o)
    {
        if (!(o instanceof Edge))
            return false;

        Edge edge = (Edge) o;

        if (!fromVertex.equals(edge.fromVertex))
            return false;
        return toVertex.equals(edge.toVertex);

    }


    public int hashCode()
    {
        int result;
        result = fromVertex.hashCode();
        result = 29 * result + toVertex.hashCode();
        return result;
    }


    @NotNull
    public String toString()
    {
        //noinspection ObjectToString
        return fromVertex + " --> " + toVertex;
    }


    public int getEdgeNumber()
    {
        return edgeNumber;
    }


    public void setEdgeNumber(int edgeNumber)
    {
        this.edgeNumber = edgeNumber;
    }
}
