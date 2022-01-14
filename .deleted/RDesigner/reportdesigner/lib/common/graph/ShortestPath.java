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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Martin
 * Date: 05.03.2006
 * Time: 14:12:10
 */
public class ShortestPath
{
    @SuppressWarnings({"UseOfSystemOutOrSystemErr"})
    public static void main(@NotNull String[] args)
    {
        //Vertex a = new DummyVertex("a");
        //Vertex b = new DummyVertex("b");
        //Vertex c = new DummyVertex("c");
        //Vertex d = new DummyVertex("d");
        //
        //Edge e1 = new Edge(a, b, 4);
        //Edge e2 = new Edge(a, c, 2);
        //Edge e3 = new Edge(c, a, 2);
        //Edge e4 = new Edge(b, c, 3);
        //Edge e5 = new Edge(c, b, 1);
        //Edge e6 = new Edge(b, d, 1);
        //Edge e7 = new Edge(c, d, 5);
        //
        //Graph graph = new Graph();
        //graph.addVertex(a);
        //graph.addVertex(b);
        //graph.addVertex(c);
        //graph.addVertex(d);
        //
        //graph.addEdge(e1);
        //graph.addEdge(e2);
        //graph.addEdge(e3);
        //graph.addEdge(e4);
        //graph.addEdge(e5);
        //graph.addEdge(e6);
        //graph.addEdge(e7);

        Vertex a = new DummyVertex("a");//NON-NLS
        Vertex b = new DummyVertex("b");//NON-NLS
        Vertex c = new DummyVertex("c");//NON-NLS
        Vertex d = new DummyVertex("d");//NON-NLS

        Edge e1 = new Edge(a, b, 2);
        Edge e2 = new Edge(a, c, 1);
        Edge e3 = new Edge(b, d, 1);
        Edge e4 = new Edge(c, d, 1);

        Graph graph = new Graph();
        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);
        graph.addVertex(d);

        graph.addEdge(e1);
        graph.addEdge(e2);
        graph.addEdge(e3);
        graph.addEdge(e4);

        ShortestPath shortestPath = new ShortestPath(graph, a);
        HashMap<Vertex, Vertex> pre = shortestPath.getPre();
        System.out.println("pre = " + pre);//NON-NLS

        System.out.println("shortestPath.getPath() = " + shortestPath.getPath(d));//NON-NLS
        System.out.println("shortestPath.getPath() = " + shortestPath.getPath(c));//NON-NLS
        System.out.println("shortestPath.getPath() = " + shortestPath.getPath(b));//NON-NLS
        System.out.println("shortestPath.getPath() = " + shortestPath.getPath(a));//NON-NLS
    }


    @NotNull
    private Graph graph;
    @NotNull
    private Vertex start;

    @NotNull
    private HashMap<Vertex, Integer> d;//cost to reach each vertex
    @NotNull
    private HashMap<Vertex, Vertex> pre;//predecessor dor each vertex

    @NotNull
    private HashSet<Vertex> s;//settled
    @NotNull
    private HashSet<Vertex> q;//unsettled


    public ShortestPath(@NotNull Graph graph, @NotNull Vertex start)
    {
        this.graph = graph;
        this.start = start;

        d = new HashMap<Vertex, Integer>();
        pre = new HashMap<Vertex, Vertex>();

        s = new HashSet<Vertex>();

        q = new HashSet<Vertex>();
        Iterator<Vertex> vertexIterator = graph.getVertexIterator();
        while (vertexIterator.hasNext())
        {
            Vertex vertex = vertexIterator.next();
            q.add(vertex);
            d.put(vertex, Integer.valueOf(999999));
        }

        s.add(start);
        d.put(start, Integer.valueOf(0));


        while (!q.isEmpty())
        {
            Vertex u = extractMinimum(q);
            if (u != null)
            {
                s.add(u);
                relaxNeigbours(u);
            }
        }
    }


    private void relaxNeigbours(@NotNull Vertex u)
    {
        Iterator<Edge> outIncidentEdges = graph.getOutIncidentEdges(u);
        while (outIncidentEdges.hasNext())
        {
            Edge edge = outIncidentEdges.next();
            Vertex v = edge.getToVertex();
            if (!s.contains(v))
            {
                if (d.get(v).intValue() > d.get(u).intValue() + edge.getCost())
                {
                    d.put(v, Integer.valueOf(d.get(u).intValue() + edge.getCost()));
                    pre.put(v, u);
                    q.add(v);
                }
            }
        }
    }


    @Nullable
    private Vertex extractMinimum(@NotNull HashSet<Vertex> q)
    {
        Vertex smallest = null;
        int c = 0;
        for (Vertex vertex : q)
        {
            if (smallest == null)
            {
                smallest = vertex;
                c = d.get(vertex).intValue();
            }
            else
            {
                int c2 = d.get(vertex).intValue();

                if (c2 < c)
                {
                    smallest = vertex;
                    c = c2;
                }
            }
        }
        q.remove(smallest);
        return smallest;
    }


    @NotNull
    public HashMap<Vertex, Vertex> getPre()
    {
        return pre;
    }


    @Nullable
    public List<Vertex> getPath(@NotNull Vertex endVertex)
    {
        LinkedList<Vertex> path = new LinkedList<Vertex>();
        Vertex last = endVertex;
        path.addLast(last);
        //noinspection ObjectEquality
        while (last != start)
        {
            last = pre.get(last);
            if (last == null)
            {
                return null;//no path from start-->endVertex
            }
            path.addLast(last);
        }
        return path;
    }

}
