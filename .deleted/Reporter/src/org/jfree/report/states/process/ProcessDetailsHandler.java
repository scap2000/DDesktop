package org.jfree.report.states.process;

import org.jfree.report.Group;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.RootLevelBand;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.states.datarow.DefaultFlowController;

/**
 * Creation-Date: 03.07.2007, 13:57:49
 *
 * @author Thomas Morgner
 */
public class ProcessDetailsHandler implements AdvanceHandler
{
  public static final AdvanceHandler HANDLER = new ProcessDetailsHandler();

  private ProcessDetailsHandler()
  {
  }

  public int getEventCode()
  {
    return ReportEvent.ITEMS_ADVANCED;
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
    final RootLevelBand rootLevelBand = next.getReport().getItemBand();
    if (rootLevelBand.getSubReportCount() == 0)
    {
      final DefaultFlowController fc = next.getFlowController().performAdvance();
      final Group group = next.getReport().getGroup(next.getCurrentGroupIndex());
      final DefaultFlowController cfc = fc.performCommit();
      if (ProcessState.isLastItemInGroup(group, fc.getMasterRow(), cfc.getMasterRow()))
      {
        next.setFlowController(fc);
        next.setAdvanceHandler(EndDetailsHandler.HANDLER);
      }
      else
      {
        next.setFlowController(cfc);
      }
      return next;
    }

    // Delay the test on whether ths item is the last in the group until we've processed the subreports.
    next.setAdvanceHandler(JoinDetailsHandler.HANDLER);
    return new ProcessState(rootLevelBand.getSubReports(), 0, next);
  }

  public boolean isFinish()
  {
    return false;
  }
}
