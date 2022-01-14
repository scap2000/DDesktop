/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * GlobalMasterRow.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.states.datarow;

import org.jfree.report.DataRow;
import org.jfree.report.ParameterDataRow;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.ProcessingContext;
import org.jfree.report.states.ReportState;

/**
 * Creation-Date: Dec 13, 2006, 2:52:23 PM
 *
 * @author Thomas Morgner
 */
public final class GlobalMasterRow
{
  private ReportDataRow reportDataRow;
  private ExpressionDataRow expressionDataRow;
  private ParameterDataRow parameterDataRow;
  private GlobalMasterRow parentRow;
  private FastGlobalView globalView;
  private ImportedVariablesDataRow importedDataRow;

  private GlobalMasterRow()
  {
  }

  public static GlobalMasterRow createReportRow(final ProcessingContext reportContext)
  {
    final GlobalMasterRow gmr = new GlobalMasterRow();
    gmr.globalView = new FastGlobalView();
    gmr.expressionDataRow = new ExpressionDataRow(gmr, reportContext);
    return gmr;
  }

  public static GlobalMasterRow createReportRow(final GlobalMasterRow parentRow,
                                                final ProcessingContext reportContext)
  {
    final GlobalMasterRow gmr = new GlobalMasterRow();
    gmr.globalView = new FastGlobalView();
    gmr.expressionDataRow = new ExpressionDataRow(gmr, reportContext);
    gmr.parentRow = parentRow;
    return gmr;
  }

  public ReportDataRow getReportDataRow()
  {
    return reportDataRow;
  }

  public void setReportDataRow(final ReportDataRow reportDataRow)
  {
    if (this.reportDataRow != null)
    {
      final int dataColCount = this.reportDataRow.getColumnCount();
      for (int i = dataColCount - 1; i >= 0; i--)
      {
        final String columnName = this.reportDataRow.getColumnName(i);
        if (columnName != null)
        {
          globalView.removeColumn(columnName);
        }
      }
    }

    this.reportDataRow = reportDataRow;

    // todo if the fucking thing is empty, dont try to query it.
    if (reportDataRow != null)
    {
      final boolean readable = reportDataRow.isReadable();
      final int dataColCount = reportDataRow.getColumnCount();
      for (int i = 0; i < dataColCount; i++)
      {
        final String columnName = reportDataRow.getColumnName(i);
        if (columnName != null)
        {
          if (readable)
          {
            final Object columnValue = reportDataRow.get(i);
            globalView.putField(columnName, columnValue, false);
          }
          else
          {
            globalView.putField(columnName, null, false);
          }
        }
      }
    }

  }

  public ExpressionDataRow getExpressionDataRow()
  {
    return expressionDataRow;
  }

  public void setExpressionDataRow(final ExpressionDataRow expressionDataRow)
  {
    this.expressionDataRow = expressionDataRow;
  }

  public ParameterDataRow getParameterDataRow()
  {
    return parameterDataRow;
  }

  public void setParameterDataRow(final ParameterDataRow parameterDataRow)
  {
    if (this.parameterDataRow != null)
    {
      final int parameterCount = this.parameterDataRow.getColumnCount();
      for (int i = parameterCount - 1; i >= 0; i--)
      {
        final String columnName = this.parameterDataRow.getColumnName(i);
        if (columnName != null)
        {
          globalView.removeColumn(columnName);
        }
      }
    }

    this.parameterDataRow = parameterDataRow;

    if (parameterDataRow != null)
    {
      final int parameterCount = parameterDataRow.getColumnCount();
      for (int i = 0; i < parameterCount; i++)
      {
        final String columnName = parameterDataRow.getColumnName(i);
        if (columnName != null)
        {
          final Object columnValue = parameterDataRow.get(i);
          globalView.putField(columnName, columnValue, false);
        }
      }
    }
  }

  public DataRow getGlobalView()
  {
    return globalView;
  }

  public void dataRowChanged(final MasterDataRowChangeEvent chEvent)
  {
    // rebuild the global view and tracks changes ..
    final int type = chEvent.getType();
    if (type == MasterDataRowChangeEvent.COLUMN_ADDED)
    {
      globalView.putField(chEvent.getColumnName(), chEvent.getColumnValue(), false);
    }
    else if (type == MasterDataRowChangeEvent.COLUMN_UPDATED)
    {
      globalView.putField(chEvent.getColumnName(), chEvent.getColumnValue(), true);
    }
    else if (type == MasterDataRowChangeEvent.COLUMN_REMOVED)
    {
      globalView.removeColumn(chEvent.getColumnName());
    }
  }

  /**
   * This updates the global view.
   */
  private void updateGlobalView()
  {
    if (parameterDataRow != null)
    {
      final int parameterCount = parameterDataRow.getColumnCount();
      for (int i = 0; i < parameterCount; i++)
      {
        final String columnName = parameterDataRow.getColumnName(i);
        if (columnName != null)
        {
          final Object columnValue = parameterDataRow.get(i);
          globalView.putField(columnName, columnValue, true);
        }
      }
    }

    if (reportDataRow != null)
    {
      final int dataColCount = reportDataRow.getColumnCount();
      final boolean readable = reportDataRow.isReadable();
      for (int i = 0; i < dataColCount; i++)
      {
        final String columnName = reportDataRow.getColumnName(i);
        if (columnName != null)
        {
          if (readable)
          {
            final Object columnValue = reportDataRow.get(i);
            globalView.putField(columnName, columnValue, true);
          }
          else
          {
            globalView.putField(columnName, null, true);
          }
        }
      }
    }
  }

  public boolean isAdvanceable()
  {
    if (reportDataRow != null)
    {
      return reportDataRow.isAdvanceable();
    }
    return false;
  }

  public GlobalMasterRow derive()
  {
    final GlobalMasterRow o = new GlobalMasterRow();
    o.globalView = globalView.derive();
    o.reportDataRow = reportDataRow;
    o.parameterDataRow = parameterDataRow;
    o.expressionDataRow = expressionDataRow.derive(o, false);
    if (parentRow != null)
    {
      o.parentRow = parentRow.derive();
    }
    o.importedDataRow = importedDataRow;
    return o;
  }

  public void setExportedDataRow(final ImportedVariablesDataRow dataRow)
  {
    if (importedDataRow != null)
    {
      final int parameterCount = importedDataRow.getColumnCount();
      for (int i = parameterCount - 1; i >= 0; i--)
      {
        final String columnName = importedDataRow.getColumnName(i);
        if (columnName != null)
        {
          globalView.removeColumn(columnName);
        }
      }
    }

    this.importedDataRow = dataRow;
    if (importedDataRow != null)
    {
      final int parameterCount = importedDataRow.getColumnCount();
      for (int i = 0; i < parameterCount; i++)
      {
        final String columnName = importedDataRow.getColumnName(i);
        if (columnName != null)
        {
          final Object columnValue = importedDataRow.get(i);
          globalView.putField(columnName, columnValue, false);
        }
      }
    }
  }

  public ImportedVariablesDataRow getExportedDataRow ()
  {
    return importedDataRow;
  }

  public GlobalMasterRow getParentDataRow()
  {
    return parentRow;
  }

  /**
   * This advances the cursor by one row and updates the flags.
   *
   * @return
   */
  public GlobalMasterRow advance()
  {
    return advance(false, null);
  }

  private GlobalMasterRow advance(final boolean deepTraversingOnly,
                                  final GlobalMasterRow subReportRow)
  {
    final GlobalMasterRow dataRow = new GlobalMasterRow();
    dataRow.globalView = globalView.advance();
    dataRow.parameterDataRow = parameterDataRow;

    if (deepTraversingOnly == false && reportDataRow != null)
    {
      dataRow.reportDataRow = reportDataRow.advance();
    }
    else
    {
      dataRow.reportDataRow = reportDataRow;
    }
    dataRow.updateGlobalView();
    if (expressionDataRow != null)
    {
      dataRow.expressionDataRow = expressionDataRow.derive(dataRow, true);
    }
    if (parentRow != null)
    {
      // the parent row should get a grip on our data as well - just for the
      // deep traversing fun and so on ..
      dataRow.parentRow = parentRow.advance(true, dataRow);
    }

    if (importedDataRow != null)
    {
      if (subReportRow != null)
      {
        dataRow.importedDataRow = importedDataRow.refresh(subReportRow);
        final int parameterCount = dataRow.importedDataRow.getColumnCount();
        for (int i = 0; i < parameterCount; i++)
        {
          final String columnName = dataRow.importedDataRow.getColumnName(i);
          if (columnName != null)
          {
            final Object columnValue = dataRow.importedDataRow.get(i);
            dataRow.globalView.putField(columnName, columnValue, true);
          }
        }
      }
    }
    return dataRow;
  }

  public void fireReportEvent(final ReportEvent event)
  {
    if (expressionDataRow != null)
    {
      expressionDataRow.fireReportEvent(event);
    }
    if (parentRow != null)
    {
      final ReportState parentState = event.getState().getParentSubReportState();
      final ReportEvent deepEvent;
      if (parentState == null)
      {
        deepEvent = event;
      }
      else
      {
        deepEvent = new ReportEvent
            (parentState, event.getState(), event.getType() | ReportEvent.DEEP_TRAVERSING_EVENT);
      }
      parentRow.fireReportEvent(deepEvent);

      if (parentRow.importedDataRow != null)
      {
        // This advance is just an refresh ...
        parentRow.importedDataRow = parentRow.importedDataRow.refresh(this);
        final int parameterCount = parentRow.importedDataRow.getColumnCount();
        for (int i = 0; i < parameterCount; i++)
        {
          final String columnName = parentRow.importedDataRow.getColumnName(i);
          if (columnName != null)
          {
            final Object columnValue = parentRow.importedDataRow.get(i);
            parentRow.globalView.putField(columnName, columnValue, true);
          }
        }
      }
    }
  }

  public boolean isPrepareEventListener()
  {
    if (parentRow != null)
    {
      if (parentRow.isPrepareEventListener())
      {
        return true;
      }
    }

    if (expressionDataRow == null)
    {
      return false;
    }
    return expressionDataRow.isPrepareEventListener();
  }
}
