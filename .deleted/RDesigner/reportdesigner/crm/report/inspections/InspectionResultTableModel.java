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
package org.pentaho.reportdesigner.crm.report.inspections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Set;

/**
 * User: Martin
 * Date: 01.02.2006
 * Time: 19:12:32
 */
public class InspectionResultTableModel extends AbstractTableModel
{
    @NotNull
    private ArrayList<InspectionResult> inspectionResults;


    public InspectionResultTableModel()
    {
        inspectionResults = new ArrayList<InspectionResult>();
    }


    public void updateInspectionResults(@NotNull Set<InspectionResult> irs)
    {
        //remove resuts not in results anymore
        for (int i = inspectionResults.size() - 1; i >= 0; i--)
        {
            InspectionResult inspectionResult = inspectionResults.get(i);
            if (!irs.contains(inspectionResult))
            {
                inspectionResults.remove(i);
                fireTableRowsDeleted(i, i);
            }
            else
            {
                irs.remove(inspectionResult);
            }
        }

        if (!irs.isEmpty())
        {
            for (InspectionResult inspectionResult : irs)
            {
                insertInspectionResult(inspectionResult);
            }
        }

    }


    private void insertInspectionResult(@NotNull InspectionResult inspectionResult)
    {
        for (int i = 0; i < inspectionResults.size(); i++)
        {
            InspectionResult result = inspectionResults.get(i);
            if (result.getSeverity().compareTo(inspectionResult.getSeverity()) <= 0)
            {
                inspectionResults.add(i, inspectionResult);
                fireTableRowsInserted(i, i);
                return;
            }
        }

        int index = inspectionResults.size();
        inspectionResults.add(inspectionResult);
        fireTableRowsInserted(index, index);
    }


    @Nullable
    public String getColumnName(int column)
    {
        if (column == 0)
        {
            return " ";
        }
        else if (column == 1)
        {
            return TranslationManager.getInstance().getTranslation("R", "InspectionResult.Summary");
        }
        else if (column == 2)
        {
            return TranslationManager.getInstance().getTranslation("R", "InspectionResult.Description");
        }
        return null;
    }


    public int getRowCount()
    {
        return inspectionResults.size();
    }


    public int getColumnCount()
    {
        return 3;
    }


    @Nullable
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (columnIndex == 0)
        {
            return inspectionResults.get(rowIndex).getSeverity();
        }
        else if (columnIndex == 1)
        {
            return inspectionResults.get(rowIndex).getSummary();
        }
        else if (columnIndex == 2)
        {
            return inspectionResults.get(rowIndex).getDescription();
        }

        return null;
    }


    @NotNull
    public InspectionResult getInspectionResult(int row)
    {
        return inspectionResults.get(row);
    }
}
