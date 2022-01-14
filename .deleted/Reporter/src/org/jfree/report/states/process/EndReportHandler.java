package org.jfree.report.states.process;

import org.jfree.report.ReportProcessingException;
import org.jfree.report.event.ReportEvent;

/**
 * Creation-Date: 03.07.2007, 13:57:49
 *
 * @author Thomas Morgner
 */
public class EndReportHandler implements AdvanceHandler
{
  public static final AdvanceHandler HANDLER = new EndReportHandler();

  public EndReportHandler()
  {
  }

  public int getEventCode()
  {
    return ReportEvent.REPORT_DONE | ProcessState.ARTIFICIAL_EVENT_CODE;
  }

  public ProcessState advance(final ProcessState state) throws ReportProcessingException
  {
    throw new ReportProcessingException("Cannot advance beyond finish!");
  }


  public ProcessState commit(final ProcessState state) throws ReportProcessingException
  {
    throw new ReportProcessingException("Cannot advance beyond finish!");
  }

  public boolean isFinish()
  {
    return true;
  }
}
