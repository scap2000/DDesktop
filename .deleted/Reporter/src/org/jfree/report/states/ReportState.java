package org.jfree.report.states;

import org.jfree.report.DataRow;
import org.jfree.report.ReportDefinition;
import org.jfree.report.SubReport;
import org.jfree.report.states.datarow.DefaultFlowController;
import org.jfree.report.util.ReportProperties;

/**
 * Creation-Date: 03.07.2007, 13:18:11
 *
 * @author Thomas Morgner
 */
public interface ReportState extends Cloneable
{
  /**
   * A row number that is 'before' the first row.
   */
  public static final int BEFORE_FIRST_ROW = -1;
  /**
   * A group number that is 'before' the first group.
   */
  public static final int BEFORE_FIRST_GROUP = -1;
  /**
   * The first page.
   */
  public static final int BEFORE_FIRST_PAGE = 0;

  ReportProperties getReportProperties();

  int getCurrentSubReport();

  int getNumberOfRows();

  DataRow getDataRow();

  ReportDefinition getReport();

  int getCurrentDataItem();

  int getCurrentGroupIndex();

  Object getProperty(String key);

  Object getProperty(String key, Object def);

  void setProperty(String key, Object o);

  ReportProperties getProperties();

  boolean isPrepareRun();

  boolean isFinish();

  int getLevel();

  int getProgressLevel();
  int getProgressLevelCount();

  /**
   * Returns the unique event code for this report state type.
   *
   * @return the event code for this state type.
   */
  int getEventCode();

  public Object clone() throws CloneNotSupportedException;

  public DefaultFlowController getFlowController();

  public boolean isDeepTraversing();

  public int getCurrentPage();

  public void setCurrentPage (int page);

  public void setErrorHandler (ReportProcessingErrorHandler errorHandler);

  public ReportProcessingErrorHandler getErrorHandler ();

  LayoutProcess getLayoutProcess();

  public void firePageFinishedEvent();

  public void firePageStartedEvent(final int eventCode);

  public ReportState getParentSubReportState();

  public ReportStateKey getProcessKey();

  public SubReport[] getSubReports();

}
