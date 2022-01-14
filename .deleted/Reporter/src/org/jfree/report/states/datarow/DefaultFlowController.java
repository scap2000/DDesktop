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
 * DefaultFlowController.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.states.datarow;

import org.jfree.report.DataFactory;
import org.jfree.report.ParameterDataRow;
import org.jfree.report.ParameterMapping;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ProcessingContext;
import org.jfree.report.states.CachingDataFactory;
import org.jfree.report.util.IntegerCache;
import org.jfree.report.util.ReportProperties;
import org.jfree.util.FastStack;

/**
 * Creation-Date: 20.02.2006, 15:30:21
 *
 * @author Thomas Morgner
 */
public final class DefaultFlowController
{
  private static class ReportDataContext
  {
    private boolean advanceRequested;

    protected ReportDataContext(final boolean advanceRequested)
    {
      this.advanceRequested = advanceRequested;
    }

    public boolean isAdvanceRequested()
    {
      return advanceRequested;
    }
  }

  private CachingDataFactory reportDataFactory;
  private GlobalMasterRow dataRow;
  private boolean advanceRequested;
  private ReportDataContext dataContext;
  private FastStack expressionsStack;
  private String exportDescriptor;
  private ProcessingContext reportContext;
  private ReportProperties parameters;

  public DefaultFlowController(final ProcessingContext reportContext,
                               final DataFactory dataFactory,
                               final ReportProperties parameters) throws ReportDataFactoryException
  {
    if (reportContext == null)
    {
      throw new NullPointerException();
    }
    if (dataFactory == null)
    {
      throw new NullPointerException();
    }
    if (parameters == null)
    {
      throw new NullPointerException();
    }

    this.reportContext = reportContext;
    this.exportDescriptor = reportContext.getExportDescriptor();
    this.reportDataFactory = new CachingDataFactory(dataFactory.derive());
    this.expressionsStack = new FastStack();
    this.advanceRequested = false;
    this.parameters = parameters;

    this.dataRow = GlobalMasterRow.createReportRow(reportContext);
    this.dataRow.setParameterDataRow(new ParameterDataRow(parameters));
  }

  private DefaultFlowController(final DefaultFlowController fc,
                                  final GlobalMasterRow dataRow)
  {
    this.reportContext = fc.reportContext;
    this.exportDescriptor = fc.exportDescriptor;
    this.reportDataFactory = fc.reportDataFactory;
    this.dataContext = fc.dataContext;
    this.expressionsStack = (FastStack) fc.expressionsStack.clone();
    this.advanceRequested = fc.advanceRequested;
    this.dataRow = dataRow;
    this.parameters = fc.parameters;
  }

  public DefaultFlowController derive ()
  {
    return new DefaultFlowController(this, dataRow.derive());
  }

  public DefaultFlowController performAdvance ()
  {
    if (dataRow.isAdvanceable() && advanceRequested == false)
    {
      final DefaultFlowController fc = new DefaultFlowController(this, dataRow);
      fc.advanceRequested = true;
      return fc;
    }
    return this;
  }

  public DefaultFlowController performCommit ()
  {
    if (isAdvanceRequested())
    {
      final DefaultFlowController fc = new DefaultFlowController(this, dataRow);
      fc.dataRow = dataRow.advance();
      fc.advanceRequested = false;
      return fc;
    }
    return this;
  }

  public GlobalMasterRow getMasterRow()
  {
    return dataRow;
  }

  public boolean isAdvanceRequested()
  {
    return advanceRequested;
  }

  /**
   * This should be called only once per report processing. A JFreeReport object
   * defines the global master report - all other reports are subreport
   * instances.
   * <p/>
   * The global master report receives its parameter set from the
   * Job-Definition, while subreports will read their parameters from the
   * current datarow state.
   *
   * @param query
   * @return
   * @throws ReportDataFactoryException
   */
  public DefaultFlowController performQuery(final String query)
      throws ReportDataFactoryException
  {
    final GlobalMasterRow outerRow = dataRow.derive();

    final GlobalMasterRow masterRow =
        GlobalMasterRow.createReportRow(outerRow, reportContext);
    masterRow.setParameterDataRow(new ParameterDataRow(parameters));
    masterRow.setReportDataRow(ReportDataRow.createDataRow
        (reportDataFactory, query, masterRow.getGlobalView()));

    final DefaultFlowController fc = new DefaultFlowController(this, masterRow);
    fc.dataContext = new ReportDataContext(advanceRequested);
    fc.dataRow = masterRow;
    return fc;
  }

  public DefaultFlowController performSubReportQuery(final String query,
                                              final ParameterMapping[] inputParameters,
                                              final ParameterMapping[] outputParameters)
      throws ReportDataFactoryException
  {
    final GlobalMasterRow parentDataRow = dataRow.derive();

    // create a view for the parameters of the report ...
    final GlobalMasterRow subReportDataRow = GlobalMasterRow.createReportRow(parentDataRow, reportContext);
    if (isGlobalImportOrExport(inputParameters))
    {
      subReportDataRow.setParameterDataRow(new ParameterDataRow(parentDataRow.getGlobalView()));
    }
    else
    {
      subReportDataRow.setParameterDataRow(new ParameterDataRow(inputParameters, parentDataRow.getGlobalView()));
    }

    // perform the query ...
    // add the resultset ...

    subReportDataRow.setReportDataRow(ReportDataRow.createDataRow(reportDataFactory, query, subReportDataRow.getGlobalView()));

    if (isGlobalImportOrExport(outputParameters))
    {
      parentDataRow.setExportedDataRow(new ImportedVariablesDataRow(subReportDataRow));
    }
    else
    {
      // check and rebuild the parameter mapping from the inner to the outer
      // context. Only deep-traversal expressions will be able to see these
      // values (unless they have been defined as local variables).
      parentDataRow.setExportedDataRow(new ImportedVariablesDataRow(subReportDataRow, outputParameters));
    }

    final DefaultFlowController fc = new DefaultFlowController(this, subReportDataRow);
    fc.dataContext = new ReportDataContext(advanceRequested);
    fc.dataRow = subReportDataRow;
    return fc;
  }


  /**
   * Checks, whether a global import is defined. A global import effectly
   * overrides all other imports.
   *
   * @return true, if there is a global import defined, false otherwise.
   */
  private boolean isGlobalImportOrExport(final ParameterMapping[] inputParameters)
  {
    for (int i = 0; i < inputParameters.length; i++)
    {
      final ParameterMapping inputParameter = inputParameters[i];
      if ("*".equals(inputParameter.getName()) &&
          "*".equals(inputParameter.getAlias()))
      {
        return true;
      }
    }
    return false;
  }

  public DefaultFlowController activateExpressions(final Expression[] expressions,
                                                   final boolean preserveState)
      throws ReportProcessingException
  {
    final GlobalMasterRow dataRow = this.dataRow.derive();
    final ExpressionDataRow edr = dataRow.getExpressionDataRow();
    edr.pushExpressions(expressions, preserveState);

    final DefaultFlowController fc = new DefaultFlowController(this, dataRow);
    final Integer exCount = IntegerCache.getInteger(expressions.length);
    fc.expressionsStack.push(exCount);
    return fc;
  }

  public DefaultFlowController deactivateExpressions()
  {
    final Integer counter = (Integer) this.expressionsStack.peek();
    final int counterRaw = counter.intValue();
    if (counterRaw == 0)
    {
      final DefaultFlowController fc = new DefaultFlowController(this, dataRow);
      fc.expressionsStack.pop();
      return fc;
    }

    final GlobalMasterRow dataRow = this.dataRow.derive();
    final ExpressionDataRow edr = dataRow.getExpressionDataRow();

    final DefaultFlowController fc = new DefaultFlowController(this, dataRow);
    fc.expressionsStack.pop();
    edr.popExpressions(counterRaw);
    return fc;
  }

  public DefaultFlowController performReturnFromQuery()
  {
    final ReportDataRow reportDataRow = dataRow.getReportDataRow();
    if (reportDataRow == null)
    {
      return this;
    }
    // We dont close the report data, as some previously saved states may
    // still reference it. (The caching report data factory takes care of
    // that later.)

    final GlobalMasterRow innerDr = dataRow.derive();
    innerDr.setReportDataRow(null);
    innerDr.setParameterDataRow(null);

    final DefaultFlowController fc = new DefaultFlowController(this, innerDr);
    final ReportDataContext context = fc.dataContext;
    fc.dataRow = dataRow.getParentDataRow();
    fc.dataRow = fc.dataRow.derive();
    fc.advanceRequested = context.isAdvanceRequested();
    return fc;
  }

  public DefaultFlowController performClearExportedParameters()
  {
    final ImportedVariablesDataRow exportedDataRow = dataRow.getExportedDataRow();
    if (exportedDataRow == null)
    {
      return this;
    }

    final DefaultFlowController derived = derive();
    derived.dataRow.setExportedDataRow(null);
    return derived;
  }

  public String getExportDescriptor()
  {
    return exportDescriptor;
  }

  public ProcessingContext getReportContext()
  {
    return reportContext;
  }

  public DefaultFlowController fireReportEvent(final ReportEvent event)
  {
    dataRow.fireReportEvent(event);
    return this;
  }

  public int getCursor()
  {
    if (dataRow.getReportDataRow() != null)
    {
      return dataRow.getReportDataRow().getCursor();
    }
    return 0;
  }


  public CachingDataFactory getDataFactory()
  {
    return reportDataFactory;
  }
}
