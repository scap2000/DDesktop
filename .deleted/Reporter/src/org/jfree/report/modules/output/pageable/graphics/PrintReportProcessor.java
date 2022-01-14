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
 * PrintReportProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.graphics;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;

import org.jfree.report.EmptyReportException;
import org.jfree.report.JFreeReport;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.layout.output.PageState;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.modules.output.pageable.graphics.internal.DrawablePrintable;
import org.jfree.report.modules.output.pageable.graphics.internal.GraphicsOutputProcessor;
import org.jfree.report.modules.output.pageable.graphics.internal.QueryPhysicalPageInterceptor;
import org.jfree.util.Log;

/**
 * Creation-Date: 09.04.2007, 13:28:33
 *
 * @author Thomas Morgner
 */
public class PrintReportProcessor extends PageableReportProcessor implements Pageable
{
  private Throwable error;

  public PrintReportProcessor(final JFreeReport report)
      throws ReportProcessingException
  {
    super(report, new GraphicsOutputProcessor(report.getConfiguration()));

  }

  protected GraphicsOutputProcessor getGraphicsProcessor()
  {
    return (GraphicsOutputProcessor) getOutputProcessor();
  }

  /**
   * Returns the number of pages in the set. To enable advanced printing
   * features, it is recommended that <code>Pageable</code> implementations
   * return the true number of pages rather than the UNKNOWN_NUMBER_OF_PAGES
   * constant.
   *
   * @return the number of pages in this <code>Pageable</code>.
   */
  public synchronized int getNumberOfPages()
  {
    if (isError())
    {
      return 0;
    }

    if (isPaginated() == false)
    {
      try
      {
        prepareReportProcessing();
        Log.debug ("After pagination, we have " + getGraphicsProcessor().getPhysicalPageCount() + " physical pages.");
      }
      catch (Exception e)
      {
        Log.debug("PrintReportProcessor: ", e);
        error = e;
        return 0;
      }
    }
    return getGraphicsProcessor().getPhysicalPageCount();
  }

  /**
   * Manually triggers the pagination. This method will block until the pagination is finished and will do nothing
   * if an error occured.
   *
   * @return true, if the pagination was successfull, false otherwise.
   */
  public boolean paginate()
  {
    if (isError())
    {
      return false;
    }

    if (isPaginated() == false)
    {
      try
      {
        prepareReportProcessing();
        return true;
      }
      catch (Exception e)
      {
        error = e;
        return false;
      }
    }
    return true;
  }


  /**
   * Returns the <code>PageFormat</code> of the page specified by
   * <code>pageIndex</code>.
   *
   * @param pageIndex the zero based index of the page whose <code>PageFormat</code>
   *                  is being requested
   * @return the <code>PageFormat</code> describing the size and orientation.
   * @throws IndexOutOfBoundsException if the <code>Pageable</code> does not
   *                                   contain the requested page.
   */
  public synchronized PageFormat getPageFormat(final int pageIndex)
      throws IndexOutOfBoundsException
  {
    if (isError())
    {
      return null;
    }

    if (isPaginated() == false)
    {
      try
      {
        prepareReportProcessing();
      }
      catch (Exception e)
      {
        error = e;
        return null;
      }
    }

    try
    {
      final PageDrawable pageDrawable = processPage(pageIndex);
      return pageDrawable.getPageFormat();
    }
    catch (Exception e)
    {
      throw new IllegalStateException("Unable to return a valid pageformat.");
    }
  }

  /**
   * Returns the <code>Printable</code> instance responsible for rendering the
   * page specified by <code>pageIndex</code>.
   *
   * @param pageIndex the zero based index of the page whose <code>Printable</code>
   *                  is being requested
   * @return the <code>Printable</code> that renders the page.
   * @throws IndexOutOfBoundsException if the <code>Pageable</code> does not
   *                                   contain the requested page.
   */
  public synchronized Printable getPrintable(final int pageIndex)
      throws IndexOutOfBoundsException
  {
    if (isError())
    {
      return null;
    }

    if (isPaginated() == false)
    {
      try
      {
        prepareReportProcessing();
      }
      catch (Exception e)
      {
        error = e;
        return null;
      }
    }

    try
    {
      final PageDrawable pageDrawable = processPage(pageIndex);
      return new DrawablePrintable(pageDrawable);
    }
    catch (Exception e)
    {
      Log.error ("Failed to return a valid pageable object: ", e);
      throw new IllegalStateException("Unable to return a valid pageformat.");
    }
  }

  /**
   * Returns the <code>PageDrawable</code> instance responsible for rendering the
   * page specified by <code>pageIndex</code>.
   *
   * @param pageIndex the zero based index of the page whose <code>Printable</code>
   *                  is being requested
   * @return the <code>PageDrawable</code> that renders the page.
   * @throws IndexOutOfBoundsException if the <code>Pageable</code> does not
   *                                   contain the requested page.
   */
  public PageDrawable getPageDrawable(final int pageIndex)
  {
    if (isError())
    {
      return null;
    }
    
    if (isPaginated() == false)
    {
      try
      {
        prepareReportProcessing();
      }
      catch (Exception e)
      {
        error = e;
        return null;
      }
    }

    try
    {
      return processPage(pageIndex);
    }
    catch (Exception e)
    {
      error = e;
      Log.debug("Failed to process the page", e);
      throw new IllegalStateException("Unable to return a valid pageformat.");
    }
  }

  /**
   * An internal method that returns the page-drawable for the given page.
   *
   * @param page the page number.
   * @return the pagedrawable for the given page.
   * @throws ReportDataFactoryException
   * @throws ReportProcessingException
   */
  protected PageDrawable processPage(final int page)
      throws ReportProcessingException
  {
    final GraphicsOutputProcessor outputProcessor = getGraphicsProcessor();
    try
    {
      // set up the scene. We can assume that the report has been paginated by now ..
      final PageState state = getPhysicalPageState(page);
      final QueryPhysicalPageInterceptor interceptor =
          new QueryPhysicalPageInterceptor(outputProcessor.getPhysicalPage(page));
      outputProcessor.setInterceptor(interceptor);
      processPage(state, true);
      return interceptor.getDrawable();
    }
    finally
    {
      outputProcessor.setInterceptor(null);
    }
  }

  /**
   * Checks, whether an error occured. The Exception itself can be queried using 'getErrorReason()'.
   *
   * @return true, if an error occured, false otherwise.
   */
  public boolean isError()
  {
    return error != null;
  }

  /**
   * This method throws an UnsupportedOperationException as printing is a passive process and cannot be started
   * here. To print the whole report, use this Pageable implementation and pass it to one of the JDKs printing
   * sub-systems.
   *
   * @throws ReportProcessingException
   * @throws EmptyReportException
   */
  public void processReport() throws ReportProcessingException
  {
    throw new UnsupportedOperationException("Printing is a passive process.");
  }

  /**
   * Returns the last exception that has been caught.
   *
   * @return the error reason.
   */
  public Throwable getErrorReason()
  {
    return error;
  }
}
