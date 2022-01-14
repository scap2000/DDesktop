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
import java.util.LinkedList;
import java.util.List;


public abstract class DFS
{

    @NotNull
    public static FindAllVerticesDFS getFindAllVerticesDFSInstance()
    {
        return new FindAllVerticesDFS();
    }


    @NotNull
    public static FindCycleDFS getFindCycleDFSInstance()
    {
        return new FindCycleDFS();
    }


    @NotNull
    private Graph graph;

    @NotNull
    private HashSet<Vertex> markedVerts;
    @NotNull
    private HashSet<Edge> markedEdges;


    public DFS()
    {
        markedVerts = new HashSet<Vertex>();
        markedEdges = new HashSet<Edge>();
    }


    public void execute(@NotNull Graph g, @NotNull Vertex start)
    {
        graph = g;
    }


    @NotNull
    protected Object dfsVisit(@NotNull Vertex v)
    {
        initResult();
        startVisit(v);
        mark(v);

        for (Iterator inEdges = getIncidentEdges(v); inEdges.hasNext();)
        {
            Edge nextEdge = (Edge) inEdges.next();
            if (!isMarked(nextEdge))
            {
                mark(nextEdge);
                Vertex w = graph.getOpposite(v, nextEdge);

                if (!isMarked(w))
                {
                    mark(nextEdge);
                    traverseDiscovery(nextEdge, v);
                    if (!isDone())
                    {
                        dfsVisit(w);
                    }
                }
                else
                {
                    traverseBack(nextEdge, v);
                }
            }
        }
        finishVisit(v);
        return result();
    }


    @NotNull
    protected Iterator<Edge> getIncidentEdges(@NotNull Vertex v)
    {
        return graph.getIncidentEdges(v);
    }


    protected void mark(@NotNull Vertex v)
    {
        markedVerts.add(v);
    }


    protected void mark(@NotNull Edge e)
    {
        markedEdges.add(e);
    }


    protected boolean isMarked(@NotNull Vertex v)
    {
        return markedVerts.contains(v);
    }


    protected boolean isMarked(@NotNull Edge e)
    {
        return markedEdges.contains(e);
    }


    @SuppressWarnings({"EmptyMethod"})
    protected void initResult()
    {
    }


    protected void startVisit(@NotNull Vertex v)
    {
    }


    @SuppressWarnings({"EmptyMethod"})
    protected void traverseDiscovery(@NotNull Edge e, @NotNull Vertex v)
    {
    }


    protected void traverseBack(@NotNull Edge e, @NotNull Vertex v)
    {
    }


    protected boolean isDone()
    {
        return false;
    }


    protected void finishVisit(@NotNull Vertex v)
    {
    }


    @NotNull
    protected Object result()
    {
        return new Object();
    }


    @NotNull
    public Graph getGraph()
    {
        return graph;
    }


    //DFS specialization
    public static class FindAllVerticesDFS extends DFS
    {

        @NotNull
        private List<Vertex> vertices;


        public void execute(@NotNull Graph g, @NotNull Vertex start)
        {
            super.execute(g, start);
            vertices = new LinkedList<Vertex>();
            dfsVisit(start);
        }


        @NotNull
        public List<Vertex> getVertices()
        {
            return vertices;
        }


        @NotNull
        protected Iterator<Edge> getIncidentEdges(@NotNull Vertex v)
        {
            return getGraph().getIncidentEdges(v);
        }


        protected void startVisit(@NotNull Vertex v)
        {
            vertices.add(v);
        }
    }


    //DFS specialization
    public static class FindCycleDFS extends DFS
    {

        @NotNull
        private LinkedList<Vertex> path;
        private boolean done;
        @NotNull
        private Vertex cycleStart;

        @NotNull
        private LinkedList<Vertex> theCycle;


        public void execute(@NotNull Graph g, @NotNull Vertex start)
        {
            super.execute(g, start);
            path = new LinkedList<Vertex>();
            done = false;
            dfsVisit(start);
            theCycle = new LinkedList<Vertex>();
            for (Vertex vertex : path)
            {
                Vertex v = vertex;
                theCycle.addFirst(v);
                //noinspection ObjectEquality
                if (v == cycleStart)
                {
                    break;
                }
            }
        }


        @NotNull
        public LinkedList<Vertex> getCycle()
        {
            return theCycle;
        }


        @NotNull
        protected Iterator<Edge> getIncidentEdges(@NotNull Vertex v)
        {
            return getGraph().getOutIncidentEdges(v);
        }


        protected void startVisit(@NotNull Vertex v)
        {
            path.addFirst(v);
        }


        protected void finishVisit(@NotNull Vertex v)
        {
            if (!done)
            {
                path.remove(path.getFirst());
            }
        }


        protected void traverseBack(@NotNull Edge e, @NotNull Vertex v)
        {
            cycleStart = getGraph().getOpposite(v, e);
            done = true;
        }


        protected boolean isDone()
        {
            return done;
        }
    }

}
