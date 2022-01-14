package org.jfree.report.states.process;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;

/**
 * Creation-Date: 03.07.2007, 13:05:16
 *
 * @author Thomas Morgner
 */
public class BeginReportHandler implements AdvanceHandler
{
  public static final AdvanceHandler HANDLER = new BeginReportHandler();

  private BeginReportHandler()
  {
  }

  public int getEventCode()
  {
    return ReportEvent.REPORT_INITIALIZED;
  }

  public boolean isFinish()
  {
    return false;
  }

  public ProcessState advance(final ProcessState state)
  {
    if (!state.getFlowController().getMasterRow().getExpressionDataRow().isValid())
    {
      throw new IllegalStateException("The expression data-row must be valid upon the start of the report processing.");
    }

    final ProcessState next = state.deriveForAdvance();
    next.fireReportEvent();
    return next;
  }


  public ProcessState commit(final ProcessState next) throws ReportProcessingException
  {
    next.setAdvanceHandler(ReportHeaderHandler.HANDLER);
    return next;
  }
}
