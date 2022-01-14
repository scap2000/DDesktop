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
import org.pentaho.reportdesigner.lib.common.graph.Edge;
import org.pentaho.reportdesigner.lib.common.graph.Vertex;

/**
 * User: Martin
 * Date: 06.03.2006
 * Time: 09:08:51
 */
public class JDBCEdge extends Edge
{
    private static final int COST = 1;

    @NotNull
    private JDBCRelationInfo relationInfo;


    public JDBCEdge(@NotNull Vertex fromVertex, @NotNull Vertex toVertex, @NotNull JDBCRelationInfo relationInfo)
    {
        super(fromVertex, toVertex, COST);
        this.relationInfo = relationInfo;
    }


    @NotNull
    public JDBCRelationInfo getRelationInfo()
    {
        return relationInfo;
    }
}
