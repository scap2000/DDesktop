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
 * AbstractOutputFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import java.util.ArrayList;

import org.jfree.report.Band;
import org.jfree.report.Group;
import org.jfree.report.GroupFooter;
import org.jfree.report.GroupHeader;
import org.jfree.report.PageFooter;
import org.jfree.report.PageHeader;
import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.Watermark;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.PrepareEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.function.FunctionProcessingException;
import org.jfree.report.function.OutputFunction;
import org.jfree.report.layout.Renderer;
import org.jfree.report.states.ReportState;
import org.jfree.report.states.datarow.DefaultFlowController;
import org.jfree.report.states.datarow.GlobalMasterRow;
import org.jfree.report.states.datarow.ReportDataRow;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.util.Log;

/**
 * Creation-Date: 08.04.2007, 16:22:18
 *
 * @author Thomas Morgner
 */
public class DefaultOutputFunction extends AbstractFunction
    implements OutputFunction, PageEventListener, PrepareEventListener
{
  public static final String HANDLE_PENDING_GROUP_FOOTER_KEY =
      "org.jfree.report.modules.output.support.pagelayout.HandlePendingGroupFooter";

  private static final LayouterLevel[] EMPTY_LAYOUTER_LEVEL = new LayouterLevel[0];

  private ReportEvent currentEvent;
  private Renderer renderer;
  private boolean lastPagebreak;
  private DefaultLayoutPagebreakHandler pagebreakHandler;
  private boolean groupStartPending;

  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link #setName} before the function
   * is added to the report's function collection.
   */
  public DefaultOutputFunction()
  {
    pagebreakHandler = new DefaultLayoutPagebreakHandler();
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    return null;
  }


  public void prepareEvent(final ReportEvent event)
  {
  }

  public void reportInitialized(final ReportEvent event)
  {
    // there can be no pending page-start, we just have started ...
    
    // activating this state after the page has ended is invalid.
    setCurrentEvent(event);
    try
    {
      // activating this state after the page has ended is invalid.
      final ReportDefinition report = event.getReport();
      if (event.isDeepTraversing() == false)
      {
        renderer.startReport(report.getPageDefinition());

        final ReportState reportState = event.getState();
        final ExpressionRuntime runtime = getRuntime();
        reportState.setCurrentPage(1);
        reportState.firePageStartedEvent(reportState.getEventCode());
        // restore the current event, as the page-started event will clear it ..
        setRuntime(runtime);
        setCurrentEvent(event);
      }
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ReportInitialized failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that the report has started. Also invokes the start of the first page ... <P> Layout and draw
   * the report header after the PageStartEvent was fired.
   *
   * @param event the event.
   */
  public void reportStarted(final ReportEvent event)
  {

    // activating this state after the page has ended is invalid.
    setCurrentEvent(event);
    try
    {
      // activating this state after the page has ended is invalid.
      updateFooterArea(event);

      renderer.startSection(Renderer.TYPE_NORMALFLOW);
      final ReportDefinition report = event.getReport();
      print(getRuntime(), report.getReportHeader());
      renderer.endSection();
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ReportStarted failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that a group has started. <P> Prints the GroupHeader
   *
   * @param event Information about the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    groupStartPending = true;
    clearPendingPageStart(event.getState());
    groupStartPending = false;
    
    // activating this state after the page has ended is invalid.
    setCurrentEvent(event);
    try
    {
      updateFooterArea(event);

      final int gidx = event.getState().getCurrentGroupIndex();
      final Group g = event.getReport().getGroup(gidx);
      final Band b = g.getHeader();
      renderer.startGroup(false);
      renderer.startSection(Renderer.TYPE_NORMALFLOW);
      print(getRuntime(), b);
      renderer.endSection();
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("GroupStarted failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }


  /**
   * Receives notification that a group of item bands is about to be processed. <P> The next events will be
   * itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(final ReportEvent event)
  {
    clearPendingPageStart(event.getState());

    setCurrentEvent(event);
//    effectiveGroupIndex += 1;
//    Log.debug ("Items-Started: " + effectiveGroupIndex + " SR: " + event.isDeepTraversing() + " GI:" + event.getState().getCurrentGroupIndex());

    try
    {
      // activating this state after the page has ended is invalid.
      final int numberOfRows = event.getState().getNumberOfRows();
      if (numberOfRows == 0)
      {
        // ups, we have no data. Lets signal that ...
        try
        {
          updateFooterArea(event);
          renderer.startSection(Renderer.TYPE_NORMALFLOW);
          print(getRuntime(), event.getReport().getNoDataBand());
          renderer.endSection();
        }
        catch (FunctionProcessingException fe)
        {
          throw fe;
        }
        catch (Exception e)
        {
          throw new FunctionProcessingException("ItemsStarted failed", e);
        }
        // there will be no item-band printed.
      }
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that a row of data is being processed. <P> prints the ItemBand.
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    clearPendingPageStart(event.getState());

    setCurrentEvent(event);
    try
    {
      updateFooterArea(event);

      renderer.startSection(Renderer.TYPE_NORMALFLOW);
      print(getRuntime(), event.getReport().getItemBand());
      renderer.endSection();
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ItemsAdvanced failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that a group has finished. <P> Prints the GroupFooter.
   *
   * @param event Information about the event.
   */
  public void groupFinished(final ReportEvent event)
  {
    clearPendingPageStart(event.getState());

    setCurrentEvent(event);
    try
    {
      updateFooterArea(event);

      final int gidx = event.getState().getCurrentGroupIndex();
      final Group g = event.getReport().getGroup(gidx);
      final Band b = g.getFooter();

      renderer.startSection(Renderer.TYPE_NORMALFLOW);
      print(getRuntime(), b);
      renderer.endSection();
      renderer.endGroup();

//      effectiveGroupIndex -= 1;
//      Log.debug ("Group-Finished: " + effectiveGroupIndex + " SR: " + event.isDeepTraversing() + " GI:" + event.getState().getCurrentGroupIndex());
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("GroupFinished failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }


  /**
   * Receives notification that the report has finished. <P> Prints the ReportFooter and forces the last pagebreak.
   *
   * @param event Information about the event.
   */
  public void reportFinished(final ReportEvent event)
  {
    clearPendingPageStart(event.getState());

    setCurrentEvent(event);
    try
    {
      // a deep traversing event means, we are in a subreport ..

      // force that this last pagebreak ... (This is an indicator for the
      // pagefooter's print-on-last-page) This is highly unclean and may or
      // may not work ..
      final Band b = event.getReport().getReportFooter();
      renderer.startSection(Renderer.TYPE_NORMALFLOW);
      print(getRuntime(), b);
      renderer.endSection();

//      if (effectiveGroupIndex != -1)
//      {
//         throw new IllegalStateException("Assertation failed: Group index is invalid");
//      }
//      Log.debug ("Report-Finished: " + effectiveGroupIndex + " SR: " + event.isDeepTraversing() + " GI:" + event.getState().getCurrentGroupIndex());
      lastPagebreak = true;
      updateFooterArea(event);
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("ReportFinished failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  /**
   * Receives notification that report generation has completed, the report footer was printed, no more output is done.
   * This is a helper event to shut down the output service.
   *
   * @param event The event.
   */
  public void reportDone(final ReportEvent event)
  {
    if (event.isDeepTraversing() == false)
    {
      final ReportState state = event.getState();
      state.firePageFinishedEvent();
      renderer.endReport();
    }
  }

  protected static LayouterLevel[] collectSubReportStates(final ReportEvent event,
                                                          final ExpressionRuntime parent)
  {
    final ReportState state = event.getState();
    ReportState parentState = state.getParentSubReportState();
    if (parentState == null)
    {
      return EMPTY_LAYOUTER_LEVEL;
    }
    GlobalMasterRow dataRow = state.getFlowController().getMasterRow();
    dataRow = dataRow.getParentDataRow();

    final ArrayList stack = new ArrayList();
    while (parentState != null)
    {
      final ReportState realState = parentState;
      final DefaultFlowController flowController = realState.getFlowController();
      final GlobalMasterRow masterRow = flowController.getMasterRow();
      final ReportDataRow reportDataRow = masterRow.getReportDataRow();
      final LayoutExpressionRuntime runtime = new LayoutExpressionRuntime
          (dataRow.getGlobalView(), realState.getCurrentDataItem(),
              reportDataRow.getReportData(), parent.getProcessingContext());
      stack.add(new LayouterLevel
          (realState.getReport(), realState.getCurrentGroupIndex(), runtime));
      parentState = parentState.getParentSubReportState();
    }
    return (LayouterLevel[]) stack.toArray(new LayouterLevel[stack.size()]);
  }


  private boolean isPageHeaderPrinting(final Band b, final ReportEvent event)
  {
    final boolean displayOnFirstPage = b.getStyle().getBooleanStyleProperty(BandStyleKeys.DISPLAY_ON_FIRSTPAGE);
    if ((event.getState().getCurrentPage() == 1) && (displayOnFirstPage == false))
    {
      return false;
    }

    final boolean displayOnLastPage = b.getStyle().getBooleanStyleProperty(BandStyleKeys.DISPLAY_ON_LASTPAGE);
    if (isLastPagebreak() && (displayOnLastPage == false))
    {
      return false;
    }

    return true;
  }

  protected boolean isLastPagebreak()
  {
    return lastPagebreak;
  }


  /**
   * Receives notification that a page has started. <P> This prints the PageHeader. If this is the first page, the
   * header is not printed if the pageheader style-flag DISPLAY_ON_FIRSTPAGE is set to false. If this event is known to
   * be the last pageStarted event, the DISPLAY_ON_LASTPAGE is evaluated and the header is printed only if this flag is
   * set to TRUE.
   * <p/>
   * If there is an active repeating GroupHeader, print the last one. The GroupHeader is searched for the current group
   * and all parent groups, starting at the current group and ascending to the parents. The first goupheader that has
   * the StyleFlag REPEAT_HEADER set to TRUE is printed.
   * <p/>
   * The PageHeader and the repeating GroupHeader are spooled until the first real content is printed. This way, the
   * LogicalPage remains empty until an other band is printed.
   *
   * @param event Information about the event.
   */
  public void pageStarted(final ReportEvent event)
  {
    // activating this state after the page has ended is invalid.
    setCurrentEvent(event);
    try
    {
      updateHeaderArea(event);
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("PageStarted failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  protected void updateHeaderArea(final ReportEvent event)
      throws ReportProcessingException
  {
    final ReportDefinition report = event.getReport();
    LayouterLevel[] levels = null;
    final OutputProcessorMetaData metaData = renderer.getOutputProcessor().getMetaData();
    if (metaData.isFeatureSupported(OutputProcessorFeature.WATERMARK_SECTION))
    {
      renderer.startSection(Renderer.TYPE_WATERMARK);
      // a new page has started, so reset the cursor ...
      // Check the subreport for sticky watermarks ...
      levels = collectSubReportStates(event, getRuntime());

      for (int i = levels.length - 1; i >= 0; i -= 1)
      {
        final LayouterLevel level = levels[i];
        final ReportDefinition def = level.getReportDefinition();
        final Watermark watermark = def.getWatermark();
        if (watermark.isSticky() && isPageHeaderPrinting(watermark, event))
        {
          print(level.getRuntime(), watermark);
        }
      }

      // and finally print the watermark of the subreport itself ..
      final Band watermark = report.getWatermark();
      if (isPageHeaderPrinting(watermark, event))
      {
        print(getRuntime(), watermark);
      }
      renderer.endSection();
    }

    if (metaData.isFeatureSupported(OutputProcessorFeature.PAGE_SECTIONS))
    {
      renderer.startSection(Renderer.TYPE_HEADER);
      // after printing the watermark, we are still at the top of the page.

      if (levels == null)
      {
        levels = collectSubReportStates(event, getRuntime());
      }

      for (int i = levels.length - 1; i >= 0; i -= 1)
      {
        // This is propably wrong (or at least incomplete) in case a subreport uses header or footer which should
        // not be printed with the report-footer or header ..
        final LayouterLevel level = levels[i];
        final ReportDefinition def = level.getReportDefinition();
        final PageHeader header = def.getPageHeader();
        if (header.isSticky() && isPageHeaderPrinting(header, event))
        {
          print(level.getRuntime(), header);
        }
      }

      // and print the ordinary page header ..
      final Band b = report.getPageHeader();
      if (isPageHeaderPrinting(b, event))
      {
        print(getRuntime(), b);
      }

      /**
       * Repeating group header are only printed while ItemElements are
       * processed.
       *
       * Dive into the pending group to print the group header ...
       */

      for (int i = levels.length - 1; i >= 0; i -= 1)
      {
        final LayouterLevel level = levels[i];
        final ReportDefinition def = level.getReportDefinition();

        for (int gidx = 0; gidx < level.getGroupIndex(); gidx++)
        {
          final Group g = def.getGroup(gidx);
          final GroupHeader header = g.getHeader();
          if (header.isSticky() &&
              header.getStyle().getBooleanStyleProperty(BandStyleKeys.REPEAT_HEADER))
          {
            print(level.getRuntime(), header);
          }
        }
      }

      final int groupsPrinted;
      if (groupStartPending)
      {
        groupsPrinted = event.getState().getCurrentGroupIndex() - 1;
      }
      else
      {
        groupsPrinted = event.getState().getCurrentGroupIndex();
      }

      for (int gidx = 0; gidx <= groupsPrinted; gidx++)
      {
        final Group g = report.getGroup(gidx);
        final GroupHeader header = g.getHeader();
        if (header.getStyle().getBooleanStyleProperty(BandStyleKeys.REPEAT_HEADER))
        {
          print(getRuntime(), header);
        }
      }

      renderer.endSection();
    }
    // mark the current position to calculate the maxBand-Height
  }

  /**
   * Receives notification that a page has ended.
   * <p/>
   * This prints the PageFooter. If this is the first page, the footer is not printed if the pagefooter style-flag
   * DISPLAY_ON_FIRSTPAGE is set to false. If this event is known to be the last pageFinished event, the
   * DISPLAY_ON_LASTPAGE is evaluated and the footer is printed only if this flag is set to TRUE.
   * <p/>
   *
   * @param event the report event.
   */
  public void pageFinished(final ReportEvent event)
  {
    setCurrentEvent(event);
    try
    {
      updateFooterArea(event);
    }
    catch (FunctionProcessingException fe)
    {
      throw fe;
    }
    catch (Exception e)
    {
      throw new FunctionProcessingException("PageFinished failed", e);
    }
    finally
    {
      clearCurrentEvent();
    }
  }

  protected void updateFooterArea(final ReportEvent event)
      throws ReportProcessingException
  {
    final OutputProcessorMetaData metaData = renderer.getOutputProcessor().getMetaData();
    if (metaData.isFeatureSupported(OutputProcessorFeature.PAGE_SECTIONS) == false)
    {
      return;
    }

    renderer.startSection(Renderer.TYPE_FOOTER);

    final ReportDefinition report = event.getReport();
    /**
     * Repeating group header are only printed while ItemElements are
     * processed.
     */
    final int groupsPrinted = event.getState().getCurrentGroupIndex();
    for (int gidx = groupsPrinted; gidx >= 0; gidx -= 1)
    {
      final Group g = report.getGroup(gidx);
      final GroupFooter footer = g.getFooter();
      if (footer.getStyle().getBooleanStyleProperty(BandStyleKeys.REPEAT_HEADER))
      {
        print(getRuntime(), footer);
      }
    }

    final LayouterLevel[] levels = collectSubReportStates(event, getRuntime());
    for (int i = 0; i < levels.length; i++)
    {
      final LayouterLevel level = levels[i];
      final ReportDefinition def = level.getReportDefinition();
      for (int gidx = level.getGroupIndex(); gidx >= 0; gidx -= 1)
      {
        final Group g = def.getGroup(gidx);
        final GroupFooter footer = g.getFooter();
        final ElementStyleSheet groupFooterStyle = footer.getStyle();
        if (footer.isSticky() && groupFooterStyle.getBooleanStyleProperty(BandStyleKeys.REPEAT_HEADER, false))
        {
          print(level.getRuntime(), footer);
        }
      }
    }

    final Band pageFooter = report.getPageFooter();
    final ElementStyleSheet pageFooterStyle = pageFooter.getStyle();
    if (event.getState().getCurrentPage() == 1)
    {
      if (pageFooterStyle.getBooleanStyleProperty(BandStyleKeys.DISPLAY_ON_FIRSTPAGE) == true)
      {
        print(getRuntime(), pageFooter);
      }
    }
    else if (isLastPagebreak())
    {
      if (pageFooterStyle.getBooleanStyleProperty(BandStyleKeys.DISPLAY_ON_LASTPAGE, false) == true)
      {
        print(getRuntime(), pageFooter);
      }
    }
    else
    {
      print(getRuntime(), pageFooter);
    }

    for (int i = 0; i < levels.length; i++)
    {
      final LayouterLevel level = levels[i];
      final ReportDefinition def = level.getReportDefinition();
      final PageFooter b = def.getPageFooter();
      if (b.isSticky() == false)
      {
        continue;
      }

      final ElementStyleSheet style = b.getStyle();
      if (event.getState().getCurrentPage() == 1)
      {
        if (style.getBooleanStyleProperty(BandStyleKeys.DISPLAY_ON_FIRSTPAGE) == true)
        {
          print(level.getRuntime(), b);
        }
      }
      else if (isLastPagebreak())
      {
        if (style.getBooleanStyleProperty(BandStyleKeys.DISPLAY_ON_LASTPAGE) == true)
        {
          print(level.getRuntime(), b);
        }
      }
      else
      {
        print(level.getRuntime(), b);
      }
    }
    renderer.endSection();
  }

  /**
   * Returns the current report event.
   *
   * @return the event.
   */
  protected ReportEvent getCurrentEvent()
  {
    return currentEvent;
  }

  /**
   * Sets the current event (also updates the report reference).
   *
   * @param currentEvent event.
   */
  protected void setCurrentEvent(final ReportEvent currentEvent)
  {
    if (currentEvent == null)
    {
      throw new NullPointerException("Event must not be null.");
    }
    this.currentEvent = currentEvent;
    this.pagebreakHandler.setReportState(currentEvent.getState());
    this.renderer.setStateKey(currentEvent.getState().getProcessKey());
  }

  /**
   * Clears the current event.
   */
  protected void clearCurrentEvent()
  {
    if (currentEvent == null)
    {
      Log.error("Failed to clear current event; we don't have an event set!",
          new IllegalStateException("stacktrace generated:"));
    }
    this.currentEvent = null;
    this.pagebreakHandler.setReportState(null);
    this.renderer.setStateKey(null);
  }

  /**
   * Clones the function. <P> Be aware, this does not create a deep copy. If you have complex strucures contained in
   * objects, you have to override this function.
   *
   * @return a clone of this function.
   * @throws CloneNotSupportedException this should never happen.
   */
  public final Object clone() throws CloneNotSupportedException
  {
    final DefaultOutputFunction sl = (DefaultOutputFunction) super.clone();
    sl.currentEvent = null;
    return sl;
  }


  public Expression getInstance()
  {
    return deriveForStorage();
  }

  public OutputFunction deriveForStorage()
  {
    try
    {
      final DefaultOutputFunction sl = (DefaultOutputFunction) super.clone();
      sl.renderer = renderer.deriveForStorage();
      sl.currentEvent = null;
      sl.pagebreakHandler = (DefaultLayoutPagebreakHandler) pagebreakHandler.clone();
      sl.pagebreakHandler.setReportState(null);
      return sl;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException();
    }
  }

  public OutputFunction deriveForPagebreak()
  {
    try
    {
      final DefaultOutputFunction sl = (DefaultOutputFunction) super.clone();
      sl.renderer = renderer.deriveForPagebreak();
      sl.currentEvent = null;
      sl.pagebreakHandler = (DefaultLayoutPagebreakHandler) pagebreakHandler.clone();
      return sl;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException();
    }
  }

  public void setRenderer(final Renderer renderer)
  {
    this.renderer = renderer;
  }

  public Renderer getRenderer()
  {
    return renderer;
  }

  /**
   * Prints the given band at the current cursor position.
   *
   * @param dataRow the datarow for evaluating the band's value-expressions.
   * @param band    the band to be printed.
   */
  protected void print(final ExpressionRuntime dataRow, final Band band)
  {
    renderer.add(band, dataRow, getCurrentEvent().getState().getProcessKey());
  }

  private void clearPendingPageStart(final ReportState state)
  {
    pagebreakHandler.setReportState(state);
    try
    {
      renderer.clearPendingPageStart(pagebreakHandler);
    }
    finally
    {
      pagebreakHandler.setReportState(null);
    }
  }

}
