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
public class JoinEndGroupHandler implements AdvanceHandler
{
  public static final AdvanceHandler HANDLER = new JoinEndGroupHandler();

  private JoinEndGroupHandler()
  {
  }

  public int getEventCode()
  {
    return ReportEvent.GROUP_FINISHED | ProcessState.ARTIFICIAL_EVENT_CODE;
  }


  public ProcessState advance(final ProcessState state) throws ReportProcessingException
  {
    return state.deriveForAdvance();
  }

  public ProcessState commit(final ProcessState next) throws ReportProcessingException
  {
    next.setCurrentGroupIndex(next.getCurrentGroupIndex() - 1);
    final DefaultFlowController fc = next.getFlowController();
    final boolean advanceRequested = fc.isAdvanceRequested();
    final boolean advanceable = fc.getMasterRow().isAdvanceable();
    if (isRootGroup(next))
    {
      // there is no parent group. So if there is more data, print the next header for this group,
      // else print the report-footer and finish the report processing.
      if (advanceRequested && advanceable)
      {
        final DefaultFlowController cfc = fc.performCommit();
        next.setFlowController(cfc);
        next.setAdvanceHandler(BeginGroupHandler.HANDLER);
        return next;
      }
      else
      {
        next.setAdvanceHandler(ReportFooterHandler.HANDLER);
        return next;
      }
    }

    if (advanceRequested == false || advanceable == false)
    {
      // This happens for empty - reports. Empty-Reports are never advanceable, therefore we can
      // reach an non-advance state where inner group-footers are printed.
      next.setAdvanceHandler(EndGroupHandler.HANDLER);
      return next;
    }

    // This group is not the outer-most group ..
    final Group group = next.getReport().getGroup(next.getCurrentGroupIndex());
    final DefaultFlowController cfc = fc.performCommit();
    if (ProcessState.isLastItemInGroup(group, fc.getMasterRow(), cfc.getMasterRow()))
    {
      // continue with an other EndGroup-State ...
      next.setAdvanceHandler(EndGroupHandler.HANDLER);
      return next;
    }
    else
    {
      // The parent group is not finished, so finalize the createRollbackInformation.
      // more data in parent group, print the next header
      next.setFlowController(cfc);
      next.setAdvanceHandler(BeginGroupHandler.HANDLER);
      return next;
    }
  }

  public boolean isFinish()
  {
    return false;
  }

  /**
   * Checks, whether there are more groups active.
   *
   * @return true if this is the last (outer-most) group.
   */
  private boolean isRootGroup (final ProcessState state)
  {
    return state.getCurrentGroupIndex() == ProcessState.BEFORE_FIRST_GROUP;
  }

}
