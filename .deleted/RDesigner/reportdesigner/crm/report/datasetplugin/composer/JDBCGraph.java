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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.common.graph.Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 06.03.2006
 * Time: 08:21:39
 */
public class JDBCGraph extends Graph
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(JDBCGraph.class.getName());

    @NotNull
    private HashMap<String, JDBCVertex> jdbcVertices;

    @NotNull
    private ArrayList<JDBCEdge> reversedEdges;


    public JDBCGraph(@NotNull ArrayList<JDBCTableInfo> jdbcTableInfos, @NotNull ArrayList<JDBCRelationInfo> allImportedKeys)
    {
        jdbcVertices = new HashMap<String, JDBCVertex>();
        for (JDBCTableInfo jdbcTableInfo : jdbcTableInfos)
        {
            JDBCVertex value = new JDBCVertex(jdbcTableInfo);
            jdbcVertices.put(jdbcTableInfo.getTableName(), value);
            addVertex(value);
        }

        reversedEdges = new ArrayList<JDBCEdge>();

        for (JDBCRelationInfo jdbcRelationInfo : allImportedKeys)
        {
            @Nullable
            JDBCVertex vertexFrom = jdbcVertices.get(jdbcRelationInfo.getPrimaryKeyColumnTable());
            @Nullable
            JDBCVertex vertexTo = jdbcVertices.get(jdbcRelationInfo.getForeignKeyColumnTable());

            if (vertexFrom != null && vertexTo != null)
            {
                JDBCEdge edge = new JDBCEdge(vertexFrom, vertexTo, jdbcRelationInfo);
                addEdge(edge);

                JDBCEdge edgeReverse = new JDBCEdge(vertexTo, vertexFrom, jdbcRelationInfo);
                addEdge(edgeReverse);
                reversedEdges.add(edgeReverse);
            }
            else
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCGraph.JDBCGraph WARNING: vertex null?!");
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCGraph.JDBCGraph vertexFrom = " + vertexFrom);
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JDBCGraph.JDBCGraph vertexTo = " + vertexTo);
            }
        }
    }


    @NotNull
    public Collection<JDBCEdge> getReversedEdges()
    {
        return reversedEdges;
    }


    @Nullable
    public JDBCVertex getVertexForTable(@NotNull String tableName)
    {
        return jdbcVertices.get(tableName);
    }
}
