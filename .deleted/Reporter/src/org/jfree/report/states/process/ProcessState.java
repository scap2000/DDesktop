package org.jfree.report.states.process;

import org.jfree.report.DataRow;
import org.jfree.report.Group;
import org.jfree.report.JFreeReport;
import org.jfree.report.ParameterMapping;
import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.SubReport;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ProcessingContext;
import org.jfree.report.states.CachingDataFactory;
import org.jfree.report.states.FunctionStorage;
import org.jfree.report.states.FunctionStorageKey;
import org.jfree.report.states.IgnoreEverythingReportErrorHandler;
import org.jfree.report.states.LayoutProcess;
import org.jfree.report.states.ReportDefinitionImpl;
import org.jfree.report.states.ReportProcessingErrorHandler;
import org.jfree.report.states.ReportState;
import org.jfree.report.states.ReportStateKey;
import org.jfree.report.states.datarow.DefaultFlowController;
import org.jfree.report.states.datarow.GlobalMasterRow;
import org.jfree.report.states.datarow.ReportDataRow;
import org.jfree.report.util.ReportProperties;

/**
 * Creation-Date: 03.07.2007, 12:56:46
 *
 * @author Thomas Morgner
 */
public class ProcessState implements ReportState
{
  public static final int ARTIFICIAL_EVENT_CODE = 0x80000000;

  private int currentGroupIndex;
  private ReportDefinitionImpl report;
  private ReportProperties reportProperties;

  private int currentSubReport;
  private SubReport[] subReports;
  private ProcessState parentState;
  private ProcessState parentSubReportState;
  private FunctionStorage functionStorage;

  private DefaultFlowController flowController;
  private LayoutProcess layoutProcess;
  private ReportStateKey processKey;
  private AdvanceHandler advanceHandler;
  private int currentPage;
  private ReportProcessingErrorHandler errorHandler;
  private int sequenceCounter;

  public ProcessState(final JFreeReport report,
                      final ProcessingContext processingContext,
                      final LayoutProcess layoutProcess)
      throws ReportProcessingException
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException("LayoutProcess must not be null.");
    }
    if (report == null)
    {
      throw new NullPointerException("Report must not be null");
    }
    if (processingContext == null)
    {
      throw new NullPointerException("ProcessingContext must not be null.");
    }

    this.errorHandler = IgnoreEverythingReportErrorHandler.INSTANCE;
    this.advanceHandler = BeginReportHandler.HANDLER;
    this.parentState = null;
    this.currentPage = BEFORE_FIRST_PAGE;
    this.currentSubReport = -1;
    this.currentGroupIndex = BEFORE_FIRST_GROUP;
    this.functionStorage = new FunctionStorage();
    this.processKey = new ReportStateKey(null, BEFORE_FIRST_ROW, 0, BEFORE_FIRST_GROUP, -1);
    this.reportProperties = report.getProperties();
    this.report = new ReportDefinitionImpl(report, report.getPageDefinition());

    final DefaultFlowController flowController =
        new DefaultFlowController(processingContext, report.getDataFactory(), this.report.getProperties());
    final DefaultFlowController qfc = flowController.performQuery(getReport().getQuery());
    this.flowController = qfc.activateExpressions(report.getExpressions().getExpressions(), false);

    this.report.getDataRowConnector().setDataRowBackend(this.flowController.getMasterRow().getGlobalView());
    this.layoutProcess = layoutProcess;
    this.processKey = createKey();
  }

  public ProcessState(final SubReport[] subReports,
                      final int subReportIndex,
                      final ProcessState parentState) throws ReportProcessingException
  {
    this.parentState = parentState;
    this.parentSubReportState = parentState;
    this.advanceHandler = BeginReportHandler.HANDLER;
    this.errorHandler = parentState.errorHandler;
    this.functionStorage = parentState.functionStorage;
    this.layoutProcess = parentState.layoutProcess;

    this.currentPage = BEFORE_FIRST_PAGE;
    this.currentGroupIndex = BEFORE_FIRST_GROUP;
    this.currentSubReport = subReportIndex;
    this.flowController = parentState.flowController;
    this.subReports = subReports;
    final SubReport report = subReports[subReportIndex];
    this.reportProperties = report.getProperties();
    this.reportProperties.setMasterProperties(parentState.getReportProperties());

    final ReportDefinition parentReport = parentState.getReport();
    this.report = new ReportDefinitionImpl(report, parentReport.getPageDefinition());
    this.sequenceCounter = parentState.getSequenceCounter() + 1;

    // And now initialize the sub-report.
    final ParameterMapping[] inputMappings = report.getInputMappings();
    final ParameterMapping[] exportMappings = report.getExportMappings();
    final DefaultFlowController qfc = parentState.flowController.performSubReportQuery
        (report.getQuery(), inputMappings, exportMappings);

    boolean preserve = true;
    Expression[] expressions = getFunctionStorage().restore(new FunctionStorageKey
        (parentSubReportState.getProcessKey(), subReportIndex));
    if (expressions == null)
    {
      // ok, it seems we have entered a new subreport ..
      // we use the expressions from the report itself ..
      expressions = report.getExpressions().getExpressions();
      preserve = false;
    }

    this.flowController = qfc.activateExpressions(expressions, preserve);
    this.report.getDataRowConnector().setDataRowBackend(this.flowController.getMasterRow().getGlobalView());
    this.processKey = createKey();
  }

  public ProcessState(final ProcessState parentState)
  {
    this.advanceHandler = parentState.advanceHandler;
    this.currentGroupIndex = parentState.currentGroupIndex;
    this.currentPage = parentState.currentPage;
    this.currentSubReport = parentState.currentSubReport;
    this.errorHandler = parentState.errorHandler;
    this.flowController = parentState.flowController;
    this.functionStorage = parentState.functionStorage;
    this.layoutProcess = parentState.layoutProcess;
    this.parentState = parentState;
    this.parentSubReportState = parentState.parentSubReportState;
    this.report = parentState.report;
    this.reportProperties = parentState.reportProperties;
    this.sequenceCounter = parentState.getSequenceCounter() + 1;
    this.processKey = createKey();
  }


  public ProcessState restart () throws ReportProcessingException
  {
    final ProcessState state = this.deriveForStorage();
    state.currentPage = BEFORE_FIRST_PAGE;
    state.currentSubReport = -1;
    state.currentGroupIndex = BEFORE_FIRST_GROUP;
    state.setAdvanceHandler(BeginReportHandler.HANDLER);

    final DefaultFlowController fc = state.getFlowController();
    final DefaultFlowController qfc = fc.performQuery(getReport().getQuery());
    final Expression[] expressions = getFunctionStorage().restore(new FunctionStorageKey(null, currentSubReport));
    final DefaultFlowController efc = qfc.activateExpressions(expressions, true);
    state.setFlowController(efc);
    state.sequenceCounter += 1;
    state.processKey = createKey();
    return state;
  }

  public ReportProcessingErrorHandler getErrorHandler()
  {
    return errorHandler;
  }

  public void setErrorHandler(final ReportProcessingErrorHandler errorHandler)
  {
    this.errorHandler = errorHandler;
  }

  public void setSequenceCounter(final int sequenceCounter)
  {
    this.sequenceCounter = sequenceCounter;
  }

  public int getSequenceCounter()
  {
    return sequenceCounter;
  }

  /**
   * This is a more expensive version of the ordinary derive. This method creates a separate copy of the layout-process
   * so that this operation is expensive in memory and CPU usage.
   *
   * @return
   */
  public ProcessState deriveForPagebreak()
  {
    try
    {
      final ProcessState processState = (ProcessState) clone();
      processState.sequenceCounter += 1;
      processState.flowController = flowController.derive();
      processState.report = (ReportDefinitionImpl) report.clone();
      processState.report.getDataRowConnector().setDataRowBackend(processState.getDataRow());
      processState.layoutProcess = layoutProcess.deriveForPagebreak();
      return processState;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Clone failed but I dont know why ..");
    }
  }

  public ProcessState deriveForAdvance()
  {
    try
    {
      final ProcessState processState = (ProcessState) clone();
      processState.sequenceCounter += 1;
      return processState;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Clone failed but I dont know why ..");
    }
  }

  public ProcessState deriveForStorage()
  {
    try
    {
      final ProcessState result = (ProcessState) clone();
      result.flowController = flowController.derive();
      result.report = (ReportDefinitionImpl) report.clone();
      result.report.getDataRowConnector().setDataRowBackend(result.getDataRow());
      result.layoutProcess = layoutProcess.deriveForStorage();
      return result;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Clone failed but I dont know why ..");
    }
  }

  public Object clone() throws CloneNotSupportedException
  {
    final ProcessState result = (ProcessState) super.clone();
    result.processKey = createKey();
    return result;
  }

  public AdvanceHandler getAdvanceHandler()
  {
    return advanceHandler;
  }

  private ReportStateKey createKey()
  {
    if (parentState != null)
    {
      return new ReportStateKey(parentState.createKey(),
          getCurrentDataItem(), getEventCode(), getCurrentGroupIndex(), getCurrentSubReport());
    }

    return new ReportStateKey(null, getCurrentDataItem(), getEventCode(), getCurrentGroupIndex(), getCurrentSubReport());
  }

  public void setAdvanceHandler(final AdvanceHandler advanceHandler)
  {
    if (advanceHandler == null)
    {
      throw new NullPointerException();
    }
    this.advanceHandler = advanceHandler;
  }

  public final ProcessState advance() throws ReportProcessingException
  {
    return advanceHandler.advance(this);
  }

  public final ProcessState commit () throws ReportProcessingException
  {
    return advanceHandler.commit(this);
  }

  public int getCurrentDataItem()
  {
    return this.flowController.getCursor();
  }

  public int getProgressLevel()
  {
    return flowController.getReportContext().getProgressLevel();
  }

  public int getProgressLevelCount()
  {
    return flowController.getReportContext().getProgressLevelCount();
  }

  public boolean isPrepareRun()
  {
    return flowController.getReportContext().isPrepareRun();
  }

  public int getLevel()
  {
    return flowController.getReportContext().getProcessingLevel();
  }

  public boolean isFinish()
  {
    return advanceHandler.isFinish();
  }

  public int getEventCode()
  {
    return advanceHandler.getEventCode();
  }

  public int getCurrentGroupIndex()
  {
    return currentGroupIndex;
  }

  public void setCurrentGroupIndex(final int currentGroupIndex)
  {
    this.currentGroupIndex = currentGroupIndex;
  }

  public ReportDefinition getReport()
  {
    return report;
  }

  private ReportDefinitionImpl getReportDefinition()
  {
    return report;
  }

  public void setReport(final ReportDefinitionImpl report)
  {
    this.report = report;
  }

  public ReportProperties getReportProperties()
  {
    return reportProperties;
  }

  public void setReportProperties(final ReportProperties reportProperties)
  {
    this.reportProperties = reportProperties;
  }

  public int getCurrentSubReport()
  {
    return currentSubReport;
  }

  public void setCurrentSubReport(final int currentSubReport)
  {
    this.currentSubReport = currentSubReport;
  }

  public ReportState getParentState()
  {
    return parentState;
  }

  public ReportState getParentSubReportState()
  {
    return parentSubReportState;
  }

  public FunctionStorage getFunctionStorage()
  {
    return functionStorage;
  }

  public void setFunctionStorage(final FunctionStorage functionStorage)
  {
    this.functionStorage = functionStorage;
  }

  public DefaultFlowController getFlowController()
  {
    return flowController;
  }

  public void setFlowController(final DefaultFlowController flowController)
  {
    if (flowController == null)
    {
      throw new NullPointerException();
    }
    this.flowController = flowController;
    this.report.getDataRowConnector().setDataRowBackend(flowController.getMasterRow().getGlobalView());
  }

  public LayoutProcess getLayoutProcess()
  {
    return layoutProcess;
  }

  public void setLayoutProcess(final LayoutProcess layoutProcess)
  {
    this.layoutProcess = layoutProcess;
  }

  public ReportStateKey getProcessKey()
  {
    return processKey;
  }

  public void setProcessKey(final ReportStateKey processKey)
  {
    this.processKey = processKey;
  }

  public DataRow getDataRow()
  {
    return flowController.getMasterRow().getGlobalView();
  }

  public int getNumberOfRows()
  {
    final GlobalMasterRow masterRow = flowController.getMasterRow();
    final ReportDataRow reportDataRow = masterRow.getReportDataRow();
    if (reportDataRow != null)
    {
      return reportDataRow.getReportData().getRowCount();
    }
    return 0;
  }

  /**
   * Fires a 'prepare' event.
   */
  public void firePrepareEvent()
  {
    if (flowController.getMasterRow().isPrepareEventListener() == false &&
        layoutProcess.isPrepareListener() == false)
    {
      return;
    }

    final ReportEvent event = new ReportEvent(this, (ReportEvent.PREPARE_EVENT | getEventCode()));
    flowController = flowController.fireReportEvent(event);
    getReportDefinition().getDataRowConnector().setDataRowBackend(getDataRow());
    layoutProcess.fireReportEvent(event);
  }

  /**
   * Fires a 'page-started' event.
   *
   * @param baseEvent the type of the base event which caused the page start to be triggered.
   */
  public void firePageStartedEvent(final int baseEvent)
  {
    final ReportEvent event = new ReportEvent(this, ReportEvent.PAGE_STARTED | baseEvent);
    flowController = flowController.fireReportEvent(event);
    getReportDefinition().getDataRowConnector().setDataRowBackend(getDataRow());
    layoutProcess.fireReportEvent(event);
  }

  /**
   * Fires a '<code>page-finished</code>' event.  The <code>pageFinished(...)</code> method is called for every report
   * function.
   */
  public void firePageFinishedEvent()
  {
    final ReportEvent event = new ReportEvent(this, ReportEvent.PAGE_FINISHED);
    flowController = flowController.fireReportEvent(event);
    getReportDefinition().getDataRowConnector().setDataRowBackend(getDataRow());
    layoutProcess.fireReportEvent(event);
  }

  protected void fireReportEvent ()
  {
    if ((advanceHandler.getEventCode() & ARTIFICIAL_EVENT_CODE) == ARTIFICIAL_EVENT_CODE)
    {
      throw new IllegalStateException("Cannot fire artificial events.");
    }

    final ReportEvent event = new ReportEvent(this, advanceHandler.getEventCode());
    flowController = flowController.fireReportEvent(event);
    getReportDefinition().getDataRowConnector().setDataRowBackend(getDataRow());
    layoutProcess.fireReportEvent(event);
  }

  /**
   * Returns the value of a property with the specified name.
   *
   * @param key the property name.
   * @return the property value.
   */
  public Object getProperty(final String key)
  {
    return reportProperties.get(key);
  }

  /**
   * Returns a property with the specified name.  If no property with the specified name is found, returns def.
   *
   * @param key the property name.
   * @param def the default value.
   * @return the property value.
   */
  public Object getProperty(final String key, final Object def)
  {
    return reportProperties.get(key, def);
  }

  /**
   * Sets a property.
   *
   * @param key the property name.
   * @param o   the property value.
   */
  public void setProperty(final String key, final Object o)
  {
    reportProperties.put(key, o);
  }

  /**
   * Returns the report properties.
   *
   * @return the report properties.
   */
  public ReportProperties getProperties()
  {
    return reportProperties;
  }

  /**
   * Returns true if this is the last item in the group, and false otherwise.
   *
   * @param g              the group that should be checked.
   * @param currentDataRow the current data row.
   * @param nextDataRow    the next data row, or null, if this is the last datarow.
   * @return A flag indicating whether or not the current item is the last in its group.
   */
  public static boolean isLastItemInGroup
      (final Group g,
       final GlobalMasterRow currentDataRow,
       final GlobalMasterRow nextDataRow)
  {
    // return true if this is the last row in the model.
    if (currentDataRow.isAdvanceable() == false || nextDataRow == null)
    {
      return true;
    }

    final DataRow nextView = nextDataRow.getGlobalView();

    // compare item and item+1, if any field differs, then item==last in group
    final String[] fieldsCached = g.getFieldsArray();
    for (int i = 0; i < fieldsCached.length; i++)
    {
      final String field = fieldsCached[i];
      final int column2 = nextView.findColumn(field);
      if (column2 == -1)
      {
        continue;
      }

      if (nextView.isChanged(column2))
      {
        return true;
      }
    }
    return false;
  }

  public boolean isDeepTraversing()
  {
    return parentState != null;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage(final int currentPage)
  {
    this.currentPage = currentPage;
  }

  public SubReport[] getSubReports()
  {
    return subReports;
  }

  public CachingDataFactory getDataFactory()
  {
    return flowController.getDataFactory();
  }
}
