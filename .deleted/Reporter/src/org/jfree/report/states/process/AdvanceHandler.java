package org.jfree.report.states.process;

import org.jfree.report.ReportProcessingException;

/**
 * This handler does the same as the ReportState.advance() method, but does not create a new state
 * object all the time.
 *
 * @author Thomas Morgner
 */
public interface AdvanceHandler
{
  public ProcessState advance(ProcessState state) throws ReportProcessingException;
  public ProcessState commit(ProcessState state) throws ReportProcessingException;
  public boolean isFinish();

  public int getEventCode();
}
