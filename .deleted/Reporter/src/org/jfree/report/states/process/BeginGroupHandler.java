package org.jfree.report.states.process;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.RootLevelBand;
import org.jfree.report.event.ReportEvent;

/**
 * Creation-Date: 03.07.2007, 13:57:49
 *
 * @author Thomas Morgner
 */
public class BeginGroupHandler implements AdvanceHandler
{
  public static final AdvanceHandler HANDLER = new BeginGroupHandler();

  private BeginGroupHandler()
  {
  }

  public int getEventCode()
  {
    return ReportEvent.GROUP_STARTED;
  }

  private boolean hasMoreGroups (final ProcessState state)
  {
    return state.getCurrentGroupIndex() < (state.getReport().getGroupCount() - 1);
  }

  public ProcessState advance(final ProcessState state) throws ReportProcessingException
  {
    final ProcessState next = state.deriveForAdvance();
    next.firePrepareEvent();
    next.setCurrentGroupIndex(next.getCurrentGroupIndex() + 1);
    next.fireReportEvent();
    return next;
  }

  public ProcessState commit(final ProcessState next) throws ReportProcessingException
  {
    if (hasMoreGroups(next) == false)
    {
      next.setAdvanceHandler(BeginDetailsHandler.HANDLER);
    }

    final RootLevelBand rootLevelBand = next.getReport().getGroup(next.getCurrentGroupIndex()).getHeader();
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
