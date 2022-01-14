package org.jfree.report.states.process;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.RootLevelBand;
import org.jfree.report.event.ReportEvent;

/**
 * Creation-Date: 03.07.2007, 13:36:56
 *
 * @author Thomas Morgner
 */
public class ReportHeaderHandler implements AdvanceHandler
{
  public static final AdvanceHandler HANDLER = new ReportHeaderHandler();

  private ReportHeaderHandler()
  {
  }

  public int getEventCode()
  {
    return ReportEvent.REPORT_STARTED;
  }

  public ProcessState advance(final ProcessState state) throws ReportProcessingException
  {
    final ProcessState next = state.deriveForAdvance();
    next.firePrepareEvent();
    next.fireReportEvent();
    return next;
  }


  public ProcessState commit(final ProcessState next) throws ReportProcessingException
  {
    next.setAdvanceHandler(BeginGroupHandler.HANDLER);

    final RootLevelBand rootLevelBand = next.getReport().getReportHeader();
    if (rootLevelBand.getSubReportCount() == 0)
    {
      return next;
    }

    return new ProcessState(rootLevelBand.getSubReports(), 0, next);
  }

  public boolean isFinish()
  {
    return false;
  }
}
