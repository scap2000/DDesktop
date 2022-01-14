/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * ExpressionDataRow.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.states.datarow;

import java.util.ArrayList;

import javax.swing.table.TableModel;

import org.jfree.report.DataRow;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.PrepareEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.function.Function;
import org.jfree.report.function.ProcessingContext;
import org.jfree.report.util.IntList;
import org.jfree.report.util.IntegerCache;
import org.jfree.report.util.LevelList;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * Creation-Date: Dec 13, 2006, 3:17:20 PM
 *
 * @author Thomas Morgner
 */
public final class ExpressionDataRow
{
  private static final Integer[] EMPTY_INTEGERARRAY = new Integer[0];
  private static final Expression[] EMPTY_EXPRESSIONS = new Expression[0];

  private static class DataRowRuntime implements ExpressionRuntime
  {
    private ExpressionDataRow expressionDataRow;

    protected DataRowRuntime(final ExpressionDataRow dataRow)
    {
      this.expressionDataRow = dataRow;
    }

    public DataRow getDataRow()
    {
      return expressionDataRow.masterRow.getGlobalView();
    }

    public Configuration getConfiguration()
    {
      return getProcessingContext().getConfiguration();
    }

    public ResourceBundleFactory getResourceBundleFactory()
    {
      return getProcessingContext().getResourceBundleFactory();
    }

    /**
     * Access to the tablemodel was granted using report properties, now direct.
     */
    public TableModel getData()
    {
      return expressionDataRow.masterRow.getReportDataRow().getReportData();
    }

    /**
     * Where are we in the current processing.
     */
    public int getCurrentRow()
    {
      return expressionDataRow.masterRow.getReportDataRow().getCursor();
    }

    /**
	 * The output descriptor is a simple string collections consisting of the following components:
	 * exportclass/type/subtype
	 * <p/>
	 * For example, the PDF export would be: pageable/pdf The StreamHTML export would return destTable/html/stream
	 *
	 * @return the export descriptor.
	 */
    public String getExportDescriptor()
    {
      return getProcessingContext().getExportDescriptor();
    }

    public ProcessingContext getProcessingContext()
    {
      return expressionDataRow.processingContext;
    }
  }


  private static class LevelStorage
  {
    private int levelNumber;
    private int[] activeExpressions;
    private int[] functions;
    private int[] pageEventListeners;
    private int[] prepareEventListeners;
    private int[] expressions;

    protected LevelStorage(final int levelNumber,
                        final int[] expressions,
                        final int[] activeExpressions,
                        final int[] functions,
                        final int[] pageEventListeners,
                        final int[] prepareEventListeners)
    {
      this.levelNumber = levelNumber;
      this.activeExpressions = activeExpressions;
      this.functions = functions;
      this.pageEventListeners = pageEventListeners;
      this.prepareEventListeners = prepareEventListeners;
      this.expressions = expressions;
    }

    public int getLevelNumber()
    {
      return levelNumber;
    }

    public int[] getFunctions()
    {
      return functions;
    }


    /**
     * @return Returns the activeExpressions.
     */
    public int[] getActiveExpressions()
    {
      return activeExpressions;
    }

    /**
     * @return Returns the expressions.
     */
    public int[] getExpressions()
    {
      return expressions;
    }

    /**
     * @return Returns the pageEventListeners.
     */
    public int[] getPageEventListeners()
    {
      return pageEventListeners;
    }

    /**
     * @return Returns the prepareEventListeners.
     */
    public int[] getPrepareEventListeners()
    {
      return prepareEventListeners;
    }
  }

  //private HashMap nameCache;
  private GlobalMasterRow masterRow;
  private ProcessingContext processingContext;
  private int length;
  private Expression[] expressions;
  private LevelStorage[] levelData;
  private MasterDataRowChangeEvent chEvent;
  private DataRowRuntime runtime;
  private ArrayList errorList;
  private static final Exception[] EMPTY_EXCEPTIONS = new Exception[0];
  private boolean prepareEventListener;

  public ExpressionDataRow(final GlobalMasterRow masterRow,
                           final ProcessingContext processingContext)
  {
    this.processingContext = processingContext;
    this.masterRow = masterRow;
    this.expressions = EMPTY_EXPRESSIONS;
    this.chEvent = new MasterDataRowChangeEvent
        (MasterDataRowChangeEvent.COLUMN_UPDATED, "", "");
    this.runtime = new DataRowRuntime(this);
    this.revalidate();
  }

  private ExpressionDataRow(final GlobalMasterRow masterRow,
                            final ExpressionDataRow previousRow,
                            final boolean updateGlobalView)
      throws CloneNotSupportedException
  {
    this.chEvent = new MasterDataRowChangeEvent
        (MasterDataRowChangeEvent.COLUMN_UPDATED, "", "");
    this.processingContext = previousRow.processingContext;
    this.masterRow = masterRow;
    this.expressions = new Expression[previousRow.expressions.length];
    this.length = previousRow.length;
    this.levelData = previousRow.levelData;
    this.runtime = new DataRowRuntime(this);

    for (int i = 0; i < length; i++)
    {
      final Expression expression = previousRow.expressions[i];
      if (expression == null)
      {
        Log.debug("Error: Expression is null...");
        throw new IllegalStateException();
      }

      if (expression instanceof Function)
      {
        expressions[i] = (Expression) expression.clone();
      }
      else
      {
        expressions[i] = expression;
      }

      if (updateGlobalView == false)
      {
        continue;
      }
      
      final String name = expression.getName();
      chEvent.setColumnName(name);
      Object value;

      final ExpressionRuntime oldRuntime = expression.getRuntime();
      try
      {
        expression.setRuntime(runtime);
        if (runtime.getProcessingContext().getProcessingLevel() <= expression.getDependencyLevel())
        {
          value = expression.getValue();
        }
        else
        {
          value = null;
        }
      }
      catch (Exception e)
      {
        if (Log.isDebugEnabled())
        {
          Log.warn("Failed to evaluate expression '" + name + '\'', e);
        }
        else
        {
          Log.warn("Failed to evaluate expression '" + name + '\'');
        }
        value = null;
      }
      finally
      {
        expression.setRuntime(oldRuntime);
      }
      chEvent.setColumnValue(value);
      masterRow.dataRowChanged(chEvent);
    }
  }

  /**
   * This adds the expression to the data-row and queries the expression for the first time.
   *
   * @param expressionSlot
   * @param preserveState
   * @throws ReportProcessingException
   */
  private void pushExpression(final Expression expressionSlot,
                              final boolean preserveState)
      throws ReportProcessingException
  {
    if (expressionSlot == null)
    {
      throw new NullPointerException();
    }

    ensureCapacity(length + 1);

    if (preserveState == false)
    {
      this.expressions[length] = expressionSlot.getInstance();
    }
    else
    {
      try
      {
        this.expressions[length] = (Expression) expressionSlot.clone();
      }
      catch (CloneNotSupportedException e)
      {
        throw new ReportProcessingException("Failed to clone the expression.");
      }
    }

    final String name = expressionSlot.getName();
    length += 1;

    // A manual advance to initialize the function.
    if (name != null)
    {
      final MasterDataRowChangeEvent chEvent = new MasterDataRowChangeEvent
          (MasterDataRowChangeEvent.COLUMN_ADDED, name, null);
      masterRow.dataRowChanged(chEvent);
    }
  }

  public synchronized void pushExpressions(final Expression[] expressionSlots,
                                           final boolean preserveState)
      throws ReportProcessingException
  {
    if (expressionSlots == null)
    {
      throw new NullPointerException();
    }

    ensureCapacity(length + expressionSlots.length);
    for (int i = 0; i < expressionSlots.length; i++)
    {
      final Expression expression = expressionSlots[i];
      if (expression == null)
      {
        continue;
      }
      pushExpression(expression, preserveState);
    }

    revalidate();
  }

  public synchronized void popExpressions(final int counter)
  {
    for (int i = 0; i < counter; i++)
    {
      popExpression();
    }

    revalidate();
  }

  private void popExpression()
  {
    if (length == 0)
    {
      return;
    }
    final Expression removedExpression = this.expressions[length - 1];
    final String originalName = removedExpression.getName();
    removedExpression.setRuntime(null);

    this.expressions[length - 1] = null;
    this.length -= 1;
    if (originalName != null)
    {
      if (removedExpression.isPreserve() == false)
      {
        final MasterDataRowChangeEvent chEvent = new MasterDataRowChangeEvent
            (MasterDataRowChangeEvent.COLUMN_REMOVED, originalName, null);
        masterRow.dataRowChanged(chEvent);
      }
    }
  }

  private void ensureCapacity(final int requestedSize)
  {
    final int capacity = this.expressions.length;
    if (capacity > requestedSize)
    {
      return;
    }
    final int newSize = Math.max(capacity * 2, requestedSize + 10);

    final Expression[] newExpressions = new Expression[newSize];
    System.arraycopy(expressions, 0, newExpressions, 0, length);

    this.expressions = newExpressions;
  }

  private void revalidate()
  {
    // recompute the level storage ..
    final LevelList levelList = new LevelList();
    for (int i = 0; i < length; i++)
    {
      final Expression expression = expressions[i];

      // The list maps the current position to the level ..
      levelList.add(IntegerCache.getInteger(i), expression.getDependencyLevel());
    }

    final Integer[] levels = levelList.getLevelsDescendingArray();
    this.levelData = new LevelStorage[levels.length];
    final int expressionsCount = levelList.size();

    final int capacity = Math.min(20, expressionsCount);
    final IntList expressionPositions = new IntList(capacity);
    final IntList activeExpressions = new IntList(capacity);
    final IntList functions = new IntList(capacity);
    final IntList pageEventListeners = new IntList(capacity);
    final IntList prepareEventListeners = new IntList(capacity);
    boolean prepareEventListener = false;

    for (int i = 0; i < levels.length; i++)
    {
      final int currentLevel = levels[i].intValue();
      final Integer[] data = (Integer[])
          levelList.getElementArrayForLevel(currentLevel, EMPTY_INTEGERARRAY);
      for (int x = 0; x < data.length; x++)
      {
        final Integer position = data[x];
        final Expression ex = this.expressions[position.intValue()];
        final int globalPosition = position.intValue();

        expressionPositions.add(globalPosition);
        activeExpressions.add(globalPosition);
        if (ex instanceof Function == false)
        {
          continue;
        }

        functions.add(globalPosition);
        if (ex instanceof PageEventListener)
        {
          pageEventListeners.add(globalPosition);
        }
        else if (ex instanceof PrepareEventListener)
        {
          prepareEventListener = true;
          prepareEventListeners.add(globalPosition);
        }
      }

      levelData[i] = new LevelStorage(currentLevel,
          expressionPositions.toArray(), activeExpressions.toArray(),
          functions.toArray(), pageEventListeners.toArray(),
          prepareEventListeners.toArray());

      expressionPositions.clear();
      activeExpressions.clear();
      functions.clear();
      pageEventListeners.clear();
      prepareEventListeners.clear();

      this.prepareEventListener = prepareEventListener;
    }
  }

  public int[] getLevels()
  {
    final int[] retval = new int[levelData.length];
    for (int i = 0; i < levelData.length; i++)
    {
      final LevelStorage storage = levelData[i];
      retval[i] = storage.getLevelNumber();
    }
    return retval;
  }

  /**
   * Returns the number of columns, expressions and functions and marked ReportProperties in the report.
   *
   * @return the item count.
   */
  public int getColumnCount()
  {
    return length;
  }

  public void fireReportEvent(final ReportEvent event)
  {
    if (event.isPrepareEvent())
    {
      firePrepareEvent(event);
    }
    else if ((event.getType() & ReportEvent.PAGE_STARTED) ==
        ReportEvent.PAGE_STARTED)
    {
      firePageStartedEvent(event);
    }
    else if ((event.getType() & ReportEvent.PAGE_FINISHED) ==
        ReportEvent.PAGE_FINISHED)
    {
      firePageFinishedEvent(event);
    }
    else if ((event.getType() & ReportEvent.ITEMS_ADVANCED) ==
        ReportEvent.ITEMS_ADVANCED)
    {
      fireItemsAdvancedEvent(event);
    }
    else if ((event.getType() & ReportEvent.ITEMS_FINISHED) ==
        ReportEvent.ITEMS_FINISHED)
    {
      fireItemsFinishedEvent(event);
    }
    else if ((event.getType() & ReportEvent.ITEMS_STARTED) ==
        ReportEvent.ITEMS_STARTED)
    {
      fireItemsStartedEvent(event);
    }
    else if ((event.getType() & ReportEvent.GROUP_FINISHED) ==
        ReportEvent.GROUP_FINISHED)
    {
      fireGroupFinishedEvent(event);
    }
    else if ((event.getType() & ReportEvent.GROUP_STARTED) ==
        ReportEvent.GROUP_STARTED)
    {
      fireGroupStartedEvent(event);
    }
    else if ((event.getType() & ReportEvent.REPORT_INITIALIZED) ==
        ReportEvent.REPORT_INITIALIZED)
    {
      fireReportInitializedEvent(event);
    }
    else if ((event.getType() & ReportEvent.REPORT_DONE) ==
        ReportEvent.REPORT_DONE)
    {
      fireReportDoneEvent(event);
    }
    else if ((event.getType() & ReportEvent.REPORT_FINISHED) ==
        ReportEvent.REPORT_FINISHED)
    {
      fireReportFinishedEvent(event);
    }
    else if ((event.getType() & ReportEvent.REPORT_STARTED) ==
        ReportEvent.REPORT_STARTED)
    {
      fireReportStartedEvent(event);
    }
    else
    {
      throw new IllegalArgumentException();
    }

    reactivateExpressions(event.isDeepTraversing());
  }

  private void reactivateExpressions(final boolean deepTraversing)
  {
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getActiveExpressions();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        expression.setRuntime(runtime);
        final String name = expression.getName();
        if (name != null)
        {
          chEvent.setColumnName(name);
          try
          {
            final Object value;
            if (runtime.getProcessingContext().getProcessingLevel() <= expression.getDependencyLevel())
            {
              value = expression.getValue();
            }
            else
            {
              value = null;
            }
            chEvent.setColumnValue(value);
          }
          catch(Exception e)
          {
            chEvent.setColumnValue(null);
            Log.info ("Evaluation of expression '" + name + "'failed.", e); 
          }
          masterRow.dataRowChanged(chEvent);
        }
        expression.setRuntime(null);
      }
    }
  }

  private void fireItemsAdvancedEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getFunctions();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final Function e = (Function) expression;
        try
        {
          e.itemsAdvanced(event);
          final String name = expression.getName();
          if (name != null)
          {
            chEvent.setColumnName(name);
            chEvent.setColumnValue(expression.getValue());
            masterRow.dataRowChanged(chEvent);
          }
        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire prepare event", ex);
          }
          else
          {
            Log.error("Failed to fire prepare event: " + ex);
          }
          addError(ex);
        }

        expression.setRuntime(oldRuntime);
      }
    }
  }

  private void fireItemsStartedEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getFunctions();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final Function e = (Function) expression;
        try
        {
          e.itemsStarted(event);
          final String name = expression.getName();
          if (name != null)
          {
            chEvent.setColumnName(name);
            chEvent.setColumnValue(expression.getValue());
            masterRow.dataRowChanged(chEvent);
          }
        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire prepare event", ex);
          }
          else
          {
            Log.error("Failed to fire prepare event: " + ex);
          }
          addError(ex);
        }

        expression.setRuntime(oldRuntime);
      }
    }
  }

  private void fireItemsFinishedEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getFunctions();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final Function e = (Function) expression;
        try
        {
          e.itemsFinished(event);
          final String name = expression.getName();
          if (name != null)
          {
            chEvent.setColumnName(name);
            chEvent.setColumnValue(expression.getValue());
            masterRow.dataRowChanged(chEvent);
          }
        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire prepare event", ex);
          }
          else
          {
            Log.error("Failed to fire prepare event: " + ex);
          }
          addError(ex);
        }

        expression.setRuntime(oldRuntime);
      }
    }
  }

  private void fireGroupStartedEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getFunctions();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final Function e = (Function) expression;
        try
        {
          e.groupStarted(event);
          final String name = expression.getName();
          if (name != null)
          {
            chEvent.setColumnName(name);
            chEvent.setColumnValue(expression.getValue());
            masterRow.dataRowChanged(chEvent);
          }
        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire group-started event", ex);
          }
          else
          {
            Log.error("Failed to fire group-started event: " + ex);
          }
          addError(ex);
        }

        expression.setRuntime(oldRuntime);
      }
    }
  }

  private void fireGroupFinishedEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getFunctions();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final Function e = (Function) expression;
        try
        {
          e.groupFinished(event);
          final String name = expression.getName();
          if (name != null)
          {
            chEvent.setColumnName(name);
            chEvent.setColumnValue(expression.getValue());
            masterRow.dataRowChanged(chEvent);
          }
        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire group-finished event", ex);
          }
          else
          {
            Log.error("Failed to fire group-finished event: " + ex);
          }
          addError(ex);
        }

        expression.setRuntime(oldRuntime);
      }
    }
  }

  private void fireReportStartedEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getFunctions();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final Function e = (Function) expression;
        try
        {
          e.reportStarted(event);
          final String name = expression.getName();
          if (name != null)
          {
            chEvent.setColumnName(name);
            chEvent.setColumnValue(expression.getValue());
            masterRow.dataRowChanged(chEvent);
          }
        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire report-started event", ex);
          }
          else
          {
            Log.error("Failed to fire report-started event: " + ex);
          }
          addError(ex);
        }

        expression.setRuntime(oldRuntime);
      }
    }
  }

  private void fireReportDoneEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getFunctions();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final Function e = (Function) expression;
        try
        {
          e.reportDone(event);
          final String name = expression.getName();
          if (name != null)
          {
            chEvent.setColumnName(name);
            chEvent.setColumnValue(expression.getValue());
            masterRow.dataRowChanged(chEvent);
          }
        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire report-done event", ex);
          }
          else
          {
            Log.error("Failed to fire report-done event: " + ex);
          }
          addError(ex);
        }

        expression.setRuntime(oldRuntime);
      }
    }
  }

  private void fireReportFinishedEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getFunctions();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final Function e = (Function) expression;
        try
        {
          e.reportFinished(event);
          final String name = expression.getName();
          if (name != null)
          {
            chEvent.setColumnName(name);
            chEvent.setColumnValue(expression.getValue());
            masterRow.dataRowChanged(chEvent);
          }
        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire report-finished event", ex);
          }
          else
          {
            Log.error("Failed to fire report-finished event: " + ex);
          }
          addError(ex);
        }

        expression.setRuntime(oldRuntime);
      }
    }
  }

  private void fireReportInitializedEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getFunctions();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final Function e = (Function) expression;
        try
        {
          e.reportInitialized(event);
          final String name = expression.getName();
          if (name != null)
          {
            chEvent.setColumnName(name);
            chEvent.setColumnValue(expression.getValue());
            masterRow.dataRowChanged(chEvent);
          }

        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire report-initialized event", ex);
          }
          else
          {
            Log.error("Failed to fire report-initialized event: " + ex);
          }
          addError(ex);
        }

        expression.setRuntime(oldRuntime);
      }
    }
  }

  private void firePageStartedEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getPageEventListeners();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final PageEventListener e = (PageEventListener) expression;
        try
        {
          e.pageStarted(event);
          final String name = expression.getName();
          if (name != null)
          {
            chEvent.setColumnName(name);
            chEvent.setColumnValue(expression.getValue());
            masterRow.dataRowChanged(chEvent);
          }

        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire page-started event", ex);
          }
          else
          {
            Log.error("Failed to fire page-started event: " + ex);
          }
          addError(ex);
        }

        expression.setRuntime(oldRuntime);
      }
    }
  }

  private void firePageFinishedEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getPageEventListeners();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final PageEventListener e = (PageEventListener) expression;
        try
        {
          e.pageFinished(event);
          final String name = expression.getName();
          if (name != null)
          {
            chEvent.setColumnName(name);
            chEvent.setColumnValue(expression.getValue());
            masterRow.dataRowChanged(chEvent);
          }

        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire page-finished event", ex);
          }
          else
          {
            Log.error("Failed to fire page-finished event: " + ex);
          }
          addError(ex);
        }

        expression.setRuntime(oldRuntime);
      }
    }
  }

  private void firePrepareEvent(final ReportEvent event)
  {
    final boolean deepTraversing = event.isDeepTraversing();
    final int activeLevel = processingContext.getProcessingLevel();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].getLevelNumber();
      if (level < activeLevel)
      {
        break;
      }

      final int[] listeners = levelData[levelIdx].getPrepareEventListeners();
      for (int l = 0; l < listeners.length; l++)
      {
        final Expression expression = expressions[listeners[l]];
        if (deepTraversing && expression.isDeepTraversing() == false)
        {
          continue;
        }

        final ExpressionRuntime oldRuntime = expression.getRuntime();
        expression.setRuntime(runtime);
        final PrepareEventListener e = (PrepareEventListener) expression;
        try
        {
          e.prepareEvent(event);
        }
        catch (Exception ex)
        {
          if (Log.isDebugEnabled())
          {
            Log.error("Failed to fire prepare event", ex);
          }
          else
          {
            Log.error("Failed to fire prepare event: " + ex);
          }
          addError(ex);
        }
        expression.setRuntime(oldRuntime);
      }
    }
  }

  public ExpressionDataRow derive(final GlobalMasterRow masterRow,
                                  final boolean update)
  {
    try
    {
      return new ExpressionDataRow(masterRow, this, update);
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Cannot clone? Cannot survive!");
    }
  }

  public boolean isErrorOccured()
  {
    if (errorList == null)
    {
      return false;
    }
    return errorList.isEmpty() == false;
  }

  public void clearErrors()
  {
    if (errorList == null)
    {
      return;
    }
    this.errorList.clear();
  }

  public Exception[] getErrors()
  {
    if (errorList == null)
    {
      return EMPTY_EXCEPTIONS;
    }
    return (Exception[]) errorList.toArray(new Exception[errorList.size()]);
  }

  private void addError(final Exception e)
  {
    if (errorList == null)
    {
      errorList = new ArrayList();
    }
    errorList.add(e);
  }

  public boolean isValid()
  {
    return levelData != null;
  }

  public Expression[] getExpressions()
  {
    return (Expression[]) expressions.clone();
  }

  public boolean isPrepareEventListener()
  {
    return prepareEventListener;
  }
}
