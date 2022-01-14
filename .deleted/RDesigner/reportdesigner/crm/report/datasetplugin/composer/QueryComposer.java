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
package org.pentaho.reportdesigner.crm.report.datasetplugin.composer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.common.graph.Edge;
import org.pentaho.reportdesigner.lib.common.graph.Graph;
import org.pentaho.reportdesigner.lib.common.graph.ShortestPath;
import org.pentaho.reportdesigner.lib.common.graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * User: Martin
 * Date: 06.03.2006
 * Time: 09:41:47
 */
public class QueryComposer
{
    private QueryComposer()
    {
    }


    @NotNull
    public static String getQuery(@NotNull JDBCGraph graph, @NotNull ArrayList<QueryComposerColumn> requestedColumns) throws NoAffectedTablesException, NoSuitableRootException
    {
        HashSet<JDBCVertex> affectedTables = getAffectedTableVertices(graph, requestedColumns);

        ArrayList<QueryComposerColumn> visibleColumns = new ArrayList<QueryComposerColumn>();
        for (QueryComposerColumn queryComposerColumn : requestedColumns)
        {
            if (queryComposerColumn.isVisible())
            {
                visibleColumns.add(queryComposerColumn);
            }
        }
        StringBuilder fieldQueryPart = new StringBuilder(100);
        for (int i = 0; i < visibleColumns.size(); i++)
        {
            QueryComposerColumn queryComposerColumn = visibleColumns.get(i);
            fieldQueryPart.append(queryComposerColumn.getColumnInfo().getTableName());
            fieldQueryPart.append(".");
            fieldQueryPart.append(queryComposerColumn.getColumnInfo().getColumnName());

            if (i < visibleColumns.size() - 1)
            {
                fieldQueryPart.append(", ");
            }
        }

        HashSet<JDBCEdge> edges = new HashSet<JDBCEdge>();
        for (JDBCVertex jdbcVertex : affectedTables)
        {
            Iterator<Edge> outIncidentEdges = graph.getOutIncidentEdges(jdbcVertex);
            while (outIncidentEdges.hasNext())
            {
                Edge edge = outIncidentEdges.next();
                if (edge instanceof JDBCEdge)
                {
                    JDBCEdge jdbcEdge = (JDBCEdge) edge;
                    Vertex opposite = graph.getOpposite(jdbcVertex, jdbcEdge);
                    if (opposite instanceof JDBCVertex)
                    {
                        JDBCVertex jv = (JDBCVertex) opposite;
                        if (affectedTables.contains(jv))
                        {
                            edges.add(jdbcEdge);
                        }
                    }
                }
            }
        }

        //System.out.println("All edges");
        //for (JDBCEdge edge : edges)
        //{
        //    System.out.println("edge = " + edge);
        //}
        //
        //System.out.println("Required edges");
        //for (JDBCEdge edge : edges)
        //{
        //    System.out.println("edge = " + edge);
        //}

        String fromQueryPart;
        //just one table?
        if (affectedTables.isEmpty())
        {
            throw new NoAffectedTablesException("no affected tables found");
        }
        else if (affectedTables.size() == 1)
        {
            fromQueryPart = affectedTables.iterator().next().getTableInfo().getTableName();
        }
        else
        {
            Graph subGraph = new Graph();

            HashMap<JDBCTableInfo, JDBCVertex> tableInfos = new HashMap<JDBCTableInfo, JDBCVertex>();

            for (JDBCEdge edge : edges)
            {
                JDBCTableInfo fromTableInfo = ((JDBCVertex) edge.getFromVertex()).getTableInfo();
                @Nullable
                JDBCVertex fromVertex = tableInfos.get(fromTableInfo);
                if (fromVertex == null)
                {
                    fromVertex = new JDBCVertex(fromTableInfo);
                    tableInfos.put(fromTableInfo, fromVertex);
                }

                JDBCTableInfo toTableInfo = ((JDBCVertex) edge.getToVertex()).getTableInfo();
                @Nullable
                JDBCVertex toVertex = tableInfos.get(toTableInfo);
                if (toVertex == null)
                {
                    toVertex = new JDBCVertex(toTableInfo);
                    tableInfos.put(toTableInfo, toVertex);
                }
                subGraph.addVertex(fromVertex);
                subGraph.addVertex(toVertex);
                JDBCEdge jdbcEdge = new JDBCEdge(fromVertex, toVertex, edge.getRelationInfo());
                subGraph.addEdge(jdbcEdge);
            }


            JDBCVertex root = (JDBCVertex) getRoot(subGraph);
            StringBuilder sb = new StringBuilder(100);
            HashSet<JDBCVertex> remainingVertices = new HashSet<JDBCVertex>();
            Iterator<Vertex> vertexIterator = subGraph.getVertexIterator();
            while (vertexIterator.hasNext())
            {
                Vertex vertex = vertexIterator.next();
                remainingVertices.add((JDBCVertex) vertex);
            }
            remainingVertices.remove(root);
            sb.append(root.getTableInfo().getTableName());
            sb.append("\n    ");
            appendJoins(sb, remainingVertices, subGraph, root);
            fromQueryPart = sb.toString();
        }

        StringBuilder whereQueryPart = new StringBuilder(100);
        boolean first = true;
        for (QueryComposerColumn queryComposerColumn : requestedColumns)
        {
            if (queryComposerColumn.getFilter() != null)
            {
                if (!first)
                {
                    whereQueryPart.append("AND ");//NON-NLS
                }
                whereQueryPart.append(queryComposerColumn.getColumnInfo().getTableName());
                whereQueryPart.append(".");
                whereQueryPart.append(queryComposerColumn.getColumnInfo().getColumnName());
                whereQueryPart.append(" ");
                whereQueryPart.append(queryComposerColumn.getFilter());
                whereQueryPart.append("\n    ");
                first = false;
            }
        }

        StringBuilder orderBy = new StringBuilder(100);
        first = true;
        for (QueryComposerColumn queryComposerColumn : requestedColumns)
        {
            if (queryComposerColumn.getOrderDirection() != null)
            {
                if (!first)
                {
                    orderBy.append(", ");
                }
                orderBy.append(queryComposerColumn.getColumnInfo().getTableName());
                orderBy.append(".");
                orderBy.append(queryComposerColumn.getColumnInfo().getColumnName());
                if (queryComposerColumn.getOrderDirection() == OrderDirection.DESCENDING)
                {
                    orderBy.append(" DESC");//NON-NLS
                }
                first = false;
            }
        }

        StringBuilder query = new StringBuilder(100);
        query.append("SELECT\n    ").append(fieldQueryPart).append("\nFROM\n    ").append(fromQueryPart);//NON-NLS
        if (whereQueryPart.length() > 0)
        {
            query.append("\nWHERE\n    ").append(whereQueryPart.toString());//NON-NLS
        }
        if (orderBy.length() > 0)
        {
            query.append("\nORDER BY\n    ").append(orderBy.toString());//NON-NLS
        }
        return query.toString();
    }


    private static void appendJoins(@NotNull StringBuilder sb, @NotNull HashSet<JDBCVertex> remainingVertices, @NotNull Graph graph, @NotNull JDBCVertex parent)
    {
        Iterator<Edge> outIncidentEdges = graph.getIncidentEdges(parent);
        while (outIncidentEdges.hasNext())
        {
            JDBCEdge edge = (JDBCEdge) outIncidentEdges.next();
            JDBCVertex opposite = (JDBCVertex) graph.getOpposite(parent, edge);

            if (remainingVertices.contains(opposite))
            {
                sb.append("INNER JOIN ");//NON-NLS
                sb.append(opposite.getTableInfo().getTableName());
                sb.append(" ON ");//NON-NLS
                sb.append(edge.getRelationInfo().getPrimaryKeyColumnTable()).append(".").append(edge.getRelationInfo().getPrimaryKeyColumnName());
                sb.append("=");
                sb.append(edge.getRelationInfo().getForeignKeyColumnTable()).append(".").append(edge.getRelationInfo().getForeignKeyColumnName());
                sb.append("\n    ");

                remainingVertices.remove(opposite);

                appendJoins(sb, remainingVertices, graph, opposite);
            }
        }
    }


    @NotNull
    private static Vertex getRoot(@NotNull Graph graph) throws NoSuitableRootException
    {
        Iterator<Vertex> vertexIterator = graph.getVertexIterator();
        while (vertexIterator.hasNext())
        {
            Vertex vertex = vertexIterator.next();
            if (vertex.getIncidenceContainer().getIncidentEdgeCount() == 2)
            {
                return vertex;
            }
        }
        throw new NoSuitableRootException("could not find a suitable root");
    }


    @NotNull
    private static HashSet<JDBCVertex> getAffectedTableVertices(@NotNull JDBCGraph graph, @NotNull ArrayList<QueryComposerColumn> requestedColumns)
    {
        HashSet<JDBCVertex> affectedTables = new HashSet<JDBCVertex>();
        for (QueryComposerColumn queryComposerColumn : requestedColumns)
        {
            JDBCVertex startVertex = graph.getVertexForTable(queryComposerColumn.getColumnInfo().getTableName());
            if (startVertex != null)
            {
                ShortestPath shortestPath = new ShortestPath(graph, startVertex);
                for (QueryComposerColumn composerColumn : requestedColumns)
                {
                    JDBCVertex endVertex = graph.getVertexForTable(composerColumn.getColumnInfo().getTableName());
                    if (endVertex != null)
                    {
                        List<Vertex> path = shortestPath.getPath(endVertex);
                        if (path != null)
                        {
                            for (Vertex vertex : path)
                            {
                                if (vertex instanceof JDBCVertex)
                                {
                                    JDBCVertex jdbcVertex = (JDBCVertex) vertex;
                                    affectedTables.add(jdbcVertex);
                                }
                            }
                        }
                    }
                }
            }
        }

        return affectedTables;
    }
}
