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

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"ALL"})
public class GraphTester
{

    public static void main(@NotNull String[] args)
    {
        Graph graph = new Graph();

        Vertex v1 = new DummyVertex("v1");
        Vertex v2 = new DummyVertex("v2");
        Vertex v3 = new DummyVertex("v3");
        Vertex v4 = new DummyVertex("v4");
        Vertex v5 = new DummyVertex("v5");
        Vertex v6 = new DummyVertex("v6");
        Vertex v7 = new DummyVertex("v7");

        Edge e1 = new Edge(v1, v3, 1);
        Edge e2 = new Edge(v2, v3, 1);
        Edge e3 = new Edge(v3, v4, 1);
        Edge e4 = new Edge(v3, v5, 1);
        Edge e5 = new Edge(v6, v7, 1);
        Edge e6 = new Edge(v2, v5, 1);

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        graph.addVertex(v7);

        graph.addEdge(e1);
        graph.addEdge(e2);
        graph.addEdge(e3);
        graph.addEdge(e4);
        graph.addEdge(e5);
        graph.addEdge(e6);

        System.out.println("connected vertices");//sout-ok
        DFS.FindAllVerticesDFS dfs = DFS.getFindAllVerticesDFSInstance();
        dfs.execute(graph, v3);
        List<Vertex> list = dfs.getVertices();
        for (Vertex aList : list)
        {
            Vertex v = (Vertex) aList;
            System.out.println("v = " + v);//sout-ok
        }

        System.out.println("cycle");//sout-ok
        DFS.FindCycleDFS fcDFS = DFS.getFindCycleDFSInstance();
        fcDFS.execute(graph, v3);
        List<Vertex> cycleList = fcDFS.getCycle();
        for (Vertex aCycleList : cycleList)
        {
            Vertex v = (Vertex) aCycleList;
            System.out.println("v = " + v);//sout-ok
        }


        LinkedList subGraphs = graph.divideIntoSubgraphList();
        for (Object subGraph1 : subGraphs)
        {
            Graph subGraph = (Graph) subGraph1;
            System.out.println("TopSort");//sout-ok
            LinkedList topSortedList = subGraph.topSortToSpanList();
            int level = 0;
            for (Object aTopSortedList : topSortedList)
            {
                System.out.print("level: " + level + " --> ");//ok
                LinkedList spanList = (LinkedList) aTopSortedList;
                for (Object aSpanList : spanList)
                {
                    Vertex vertex = (Vertex) aSpanList;
                    System.out.print(vertex + " ");//ok
                }
                System.out.println();
                level++;
            }
        }

    }

}
