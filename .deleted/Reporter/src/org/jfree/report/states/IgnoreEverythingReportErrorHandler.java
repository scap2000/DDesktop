package org.jfree.report.states;

/**
 * Creation-Date: 04.07.2007, 14:01:43
 *
 * @author Thomas Morgner
 */
public class IgnoreEverythingReportErrorHandler implements ReportProcessingErrorHandler
{
  public static final ReportProcessingErrorHandler INSTANCE = new IgnoreEverythingReportErrorHandler();

  private static final Exception[] EMPTY_EXCEPTION = new Exception[0];

  private IgnoreEverythingReportErrorHandler()
  {
  }

  public void handleError(final Exception exception)
  {

  }

  public boolean isErrorOccured()
  {
    return false;
  }

  public Exception[] getErrors()
  {
    return EMPTY_EXCEPTION;
  }

  public void clearErrors()
  {

  }
}
