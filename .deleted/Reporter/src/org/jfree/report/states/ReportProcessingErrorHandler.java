package org.jfree.report.states;

/**
 * Creation-Date: 04.07.2007, 13:59:42
 *
 * @author Thomas Morgner
 */
public interface ReportProcessingErrorHandler
{
  public void handleError (Exception exception);
  public boolean isErrorOccured();

  public Exception[] getErrors();

  public void clearErrors();
}
