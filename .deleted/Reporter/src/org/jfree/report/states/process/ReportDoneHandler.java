package org.jfree.report.states.process;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.Expression;
import org.jfree.report.states.FunctionStorageKey;
import org.jfree.report.states.ReportState;
import org.jfree.report.states.ReportStateKey;
import org.jfree.report.states.datarow.DefaultFlowController;
import org.jfree.report.states.datarow.ExpressionDataRow;
import org.jfree.report.states.datarow.GlobalMasterRow;

/**
 * Creation-Date: 03.07.2007, 13:57:49
 *
 * @author Thomas Morgner
 */
public class ReportDoneHandler implements AdvanceHandler
{
  public static final AdvanceHandler HANDLER = new ReportDoneHandler();

  private ReportDoneHandler()
  {
  }

  public int getEventCode()
  {
    return ReportEvent.REPORT_DONE;
  }

  public ProcessState advance(final ProcessState state) throws ReportProcessingException
  {
    final ProcessState next = state.deriveForAdvance();
    next.firePrepareEvent();
    next.fireReportEvent();
    return next;
  }


  public ProcessState commit(final ProcessState state) throws ReportProcessingException
  {
    // better clone twice than to face the subtle errors that crawl out here..
    final ProcessState next = state.deriveForAdvance();
    final DefaultFlowController flowController = next.getFlowController();
    final GlobalMasterRow masterRow = flowController.getMasterRow();
    final ExpressionDataRow expressionDataRow = masterRow.getExpressionDataRow();
    final Expression[] expressions = expressionDataRow.getExpressions();

    if (next.isDeepTraversing())
    {
      next.setAdvanceHandler(EndSubReportHandler.HANDLER);
    }
    else
    {
      next.setAdvanceHandler(EndReportHandler.HANDLER);
    }
    final ReportStateKey parentStateKey;
    final ReportState parentState = next.getParentSubReportState();
    if (parentState == null)
    {
      parentStateKey = null;
    }
    else
    {
      parentStateKey = parentState.getProcessKey();
    }
    next.getFunctionStorage().store
        (new FunctionStorageKey(parentStateKey, next.getCurrentSubReport()),
            expressions, expressionDataRow.getColumnCount());

    final DefaultFlowController pfc = flowController.performClearExportedParameters();
    final DefaultFlowController efc = pfc.deactivateExpressions();
    final DefaultFlowController qfc = efc.performReturnFromQuery();
    next.setFlowController(qfc);
    return next;
  }

  public boolean isFinish()
  {
    return false;
  }
}
