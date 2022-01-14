package org.jfree.report.states.process;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;

/**
 * Creation-Date: 03.07.2007, 13:57:49
 *
 * @author Thomas Morgner
 */
public class EndDetailsHandler implements AdvanceHandler
{
  public static final AdvanceHandler HANDLER = new EndDetailsHandler();

  private EndDetailsHandler()
  {
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
    state.setAdvanceHandler(EndGroupHandler.HANDLER);
    return state;
  }

  public int getEventCode()
  {
    return ReportEvent.ITEMS_FINISHED;
  }

  public boolean isFinish()
  {
    return false;
  }
}
