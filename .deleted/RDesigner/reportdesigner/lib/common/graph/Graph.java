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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Graph
{

    @NotNull
    private HashSet<Edge> edges;
    @NotNull
    private HashSet<Vertex> vertices;


    public Graph()
    {
        edges = new HashSet<Edge>();
        vertices = new HashSet<Vertex>();
    }


    public int getNumberOfVertices()
    {
        return vertices.size();
    }


    public int getNumberOfEdges()
    {
        return edges.size();
    }


    @NotNull
    public Iterator<Edge> getIncidentEdges(@NotNull Vertex v)
    {
        return v.getIncidenceContainer().getIncidentEdgeIterator();
    }


    @NotNull
    public Iterator<Edge> getInIncidentEdges(@NotNull Vertex v)
    {
        return v.getIncidenceContainer().getInIncidentEdgeIterator();
    }


    @NotNull
    public Iterator<Edge> getOutIncidentEdges(@NotNull Vertex v)
    {
        return v.getIncidenceContainer().getOutIncidentEdgeIterator();
    }


    @NotNull
    public Vertex getOpposite(@NotNull Vertex v, @NotNull Edge edge)
    {
        //noinspection ObjectEquality
        if (edge.getFromVertex() == v)
        {
            return edge.getToVertex();
        }
        else
        {
            return edge.getFromVertex();
        }
    }


    public void addVertex(@NotNull Vertex vertex)
    {
        vertices.add(vertex);
    }


    public void addEdge(@NotNull Edge edge)
    {
        edges.add(edge);
    }


    @NotNull
    public Iterator<Vertex> getVertexIterator()
    {
        return vertices.iterator();
    }


    @NotNull
    public LinkedList<Vertex> getCycles()
    {
        LinkedList<Vertex> remainingVertices = new LinkedList<Vertex>(vertices);
        LinkedList<Vertex> startVertices;
        while (!(startVertices = findStartVertices(remainingVertices)).isEmpty())
        {
            for (Vertex startVertice : startVertices)
            {
                Vertex vertex = startVertice;
                vertex.detachEdges();
            }
        }
        for (Edge edge1 : edges)
        {
            Edge edge = edge1;
            edge.attachToVertices();
        }

        return remainingVertices;
    }


    @NotNull
    public LinkedList<LinkedList<Vertex>> topSortToSpanList()
    {
        LinkedList<Vertex> remainingVertices = new LinkedList<Vertex>(vertices);
        LinkedList<LinkedList<Vertex>> topSortedList = new LinkedList<LinkedList<Vertex>>();
        LinkedList<Vertex> startVertices;
        while (!(startVertices = findStartVertices(remainingVertices)).isEmpty())
        {
            topSortedList.add(startVertices);
            for (Vertex startVertice : startVertices)
            {
                Vertex vertex = startVertice;
                vertex.detachEdges();
            }
        }

        for (Edge edge1 : edges)
        {
            Edge edge = edge1;
            edge.attachToVertices();
        }


        return topSortedList;
    }


    @NotNull
    private LinkedList<Vertex> findStartVertices(@NotNull LinkedList<Vertex> remainingVertices)
    {
        LinkedList<Vertex> startVerticesList = new LinkedList<Vertex>();
        for (Iterator iterator = remainingVertices.iterator(); iterator.hasNext();)
        {
            Vertex vertex = (Vertex) iterator.next();
            if (vertex.getIncidenceContainer().getOutIncidentEdgeCount() == 0)
            {
                startVerticesList.add(vertex);
                iterator.remove();
            }
        }
        return startVerticesList;
    }


    @NotNull
    public LinkedList<Graph> divideIntoSubgraphList()
    {
        Graph graphCopy = new Graph();
        HashSet<Edge> edgesCopy = new HashSet<Edge>(edges);
        HashSet<Vertex> verticesCopy = new HashSet<Vertex>(vertices);
        graphCopy.edges = edgesCopy;
        graphCopy.vertices = verticesCopy;
        return graphCopy.divide();
    }


    @NotNull
    private LinkedList<Graph> divide()
    {
        LinkedList<Graph> graphList = new LinkedList<Graph>();

        while (vertices.iterator().hasNext())
        {
            Graph subGraph = new Graph();
            DFS.FindAllVerticesDFS dfs = new DFS.FindAllVerticesDFS();
            dfs.execute(this, vertices.iterator().next());
            List<Vertex> verticesList = dfs.getVertices();
            for (Vertex aVerticesList : verticesList)
            {
                Vertex vertex = aVerticesList;
                subGraph.addVertex(vertex);
                for (Iterator edgeIterator = vertex.getIncidenceContainer().getIncidentEdgeIterator(); edgeIterator.hasNext();)
                {
                    Edge edge = (Edge) edgeIterator.next();
                    subGraph.addEdge(edge);
                }
                vertices.remove(vertex);
            }

            graphList.add(subGraph);
        }
        Collections.sort(graphList, new GraphSizeComparator(false));
        return graphList;
    }


}
