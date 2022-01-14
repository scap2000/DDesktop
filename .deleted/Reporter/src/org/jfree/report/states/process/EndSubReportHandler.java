package org.jfree.report.states.process;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.SubReport;
import org.jfree.report.event.ReportEvent;

/**
 * Creation-Date: 04.07.2007, 19:00:19
 *
 * @author Thomas Morgner
 */
public class EndSubReportHandler implements AdvanceHandler
{
  public static final AdvanceHandler HANDLER = new EndSubReportHandler();

  private EndSubReportHandler()
  {
  }


  public ProcessState advance(final ProcessState state) throws ReportProcessingException
  {
    return state.deriveForAdvance();
  }

  public ProcessState commit(final ProcessState state) throws ReportProcessingException
  {
    final SubReport[] subReports = state.getSubReports();
    final int currentSubReport = state.getCurrentSubReport();
    if ((currentSubReport + 1) < subReports.length)
    {
      final ProcessState parentState = (ProcessState) state.getParentSubReportState();
      final ProcessState parentNext = parentState.deriveForAdvance();
      parentNext.setLayoutProcess(state.getLayoutProcess());
      parentNext.setFlowController(state.getFlowController());
      parentNext.setSequenceCounter(state.getSequenceCounter() + 1);

      return new ProcessState(subReports, currentSubReport + 1, parentNext);
    }
    else
    {
      // No more sub-reports, so join back with the parent ..
      final ProcessState parentState = (ProcessState) state.getParentSubReportState();
      final ProcessState parentNext = parentState.deriveForAdvance();
      parentNext.setLayoutProcess(state.getLayoutProcess());
      parentNext.setFlowController(state.getFlowController());
      parentNext.setSequenceCounter(state.getSequenceCounter() + 1);
      return parentNext;
    }
  }

  public boolean isFinish()
  {
    return false;
  }

  public int getEventCode()
  {
    return ReportEvent.REPORT_DONE | ProcessState.ARTIFICIAL_EVENT_CODE;
  }
}
