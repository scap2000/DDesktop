package org.jfree.report.states.process;

import org.jfree.report.Group;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.states.datarow.DefaultFlowController;

/**
 * This delays the actual test on whether the current detail-group should be finished until the subreports have been
 * processed. The subreports can influence this test by declaring output-parameters.
 *
 * @author Thomas Morgner
 */
public class JoinDetailsHandler implements AdvanceHandler
{
  public static final AdvanceHandler HANDLER = new JoinDetailsHandler();

  private JoinDetailsHandler()
  {
  }

  public int getEventCode()
  {
    return ReportEvent.ITEMS_ADVANCED | ProcessState.ARTIFICIAL_EVENT_CODE;
  }

  public ProcessState advance(final ProcessState state) throws ReportProcessingException
  {
    return state.deriveForAdvance();
  }


  public ProcessState commit(final ProcessState next) throws ReportProcessingException
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
      next.setAdvanceHandler(ProcessDetailsHandler.HANDLER);
    }
    return next;
  }

  public boolean isFinish()
  {
    return false;
  }
}
