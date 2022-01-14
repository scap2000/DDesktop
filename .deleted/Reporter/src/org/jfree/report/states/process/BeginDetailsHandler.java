package org.jfree.report.states.process;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.RootLevelBand;
import org.jfree.report.event.ReportEvent;

/**
 * Creation-Date: 03.07.2007, 13:57:49
 *
 * @author Thomas Morgner
 */
public class BeginDetailsHandler implements AdvanceHandler
{
  public static final BeginDetailsHandler HANDLER = new BeginDetailsHandler();

  private BeginDetailsHandler()
  {
  }

  public int getEventCode()
  {
    return ReportEvent.ITEMS_STARTED;
  }

  public ProcessState advance(final ProcessState state) throws ReportProcessingException
  {
    final ProcessState next = state.deriveForAdvance();
    next.firePrepareEvent();
    // if there is no data in the report's data source, this now prints the No-Data-Band.
    next.fireReportEvent();
    return next;
  }


  public ProcessState commit(final ProcessState next) throws ReportProcessingException
  {
    if (next.getNumberOfRows() > 0)
    {
      next.setAdvanceHandler(ProcessDetailsHandler.HANDLER);
      return next;
    }

    next.setAdvanceHandler(EndDetailsHandler.HANDLER);

    final RootLevelBand rootLevelBand = next.getReport().getNoDataBand();
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
