package org.jfree.report.states.process;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.RootLevelBand;
import org.jfree.report.event.ReportEvent;

/**
 * Creation-Date: 03.07.2007, 13:57:49
 *
 * @author Thomas Morgner
 */
public class ReportFooterHandler implements AdvanceHandler
{
  public static final AdvanceHandler HANDLER = new ReportFooterHandler();

  private ReportFooterHandler()
  {
  }

  public int getEventCode()
  {
    return ReportEvent.REPORT_FINISHED;
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
    next.setAdvanceHandler(ReportDoneHandler.HANDLER);

    final RootLevelBand rootLevelBand = next.getReport().getReportFooter();
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
