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
 * AbstractElementFormatFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import org.jfree.report.Band;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;

/**
 * The AbstractElementFormatFunction provides a common base implementation for all functions that need to modify the
 * report definition or the style of an report element or band during the report processing.
 * <p/>
 * The Expression retrieves the next root-level band that will be printed and uses this band as parameter for the {@link
 * AbstractElementFormatFunction#processRootBand(org.jfree.report.Band)} method.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractElementFormatFunction extends AbstractFunction implements PageEventListener
{
  /**
   * The name of the element that should be formatted.
   */
  private String element;

  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link #setName} before the function
   * is added to the report's function collection.
   */
  protected AbstractElementFormatFunction()
  {
  }

  /**
   * Sets the element name. The name denotes an element or band within the root-band or the root-band itself. It is
   * possible to define multiple elements with the same name to apply the modification to all of these elements.
   *
   * @param name The element name.
   * @see org.jfree.report.function.FunctionUtilities#findAllElements(org.jfree.report.Band,String)
   */
  public void setElement(final String name)
  {
    this.element = name;
  }

  /**
   * Returns the element name.
   *
   * @return The element name.
   * @see #setElement(String)
   */
  public String getElement()
  {
    return element;
  }

  /**
   * Processes the No-Data-Band.
   *
   * @param event the report event.
   */
  public void itemsStarted(final ReportEvent event)
  {
    if (FunctionUtilities.isLayoutLevel(event) == false)
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = event.getReport().getNoDataBand();
    processRootBand(b);
  }

  /**
   * Processes the ItemBand.
   *
   * @param event the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    if (FunctionUtilities.isLayoutLevel(event) == false)
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = event.getReport().getItemBand();
    processRootBand(b);
  }

  /**
   * Processes the Report-Footer.
   *
   * @param event the event.
   */
  public void reportFinished(final ReportEvent event)
  {
    if (FunctionUtilities.isLayoutLevel(event) == false)
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = event.getReport().getReportFooter();
    processRootBand(b);
  }

  /**
   * Processes the Report-Header.
   *
   * @param event the event.
   */
  public void reportStarted(final ReportEvent event)
  {
    if (FunctionUtilities.isLayoutLevel(event) == false)
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = event.getReport().getReportHeader();
    processRootBand(b);
  }

  /**
   * Processes the group header of the current group.
   *
   * @param event the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    if (FunctionUtilities.isLayoutLevel(event) == false)
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = FunctionUtilities.getCurrentGroup(event).getHeader();
    processRootBand(b);
  }

  /**
   * Processes the group footer of the current group.
   *
   * @param event the event.
   */
  public void groupFinished(final ReportEvent event)
  {
    if (FunctionUtilities.isLayoutLevel(event) == false)
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = FunctionUtilities.getCurrentGroup(event).getFooter();
    processRootBand(b);
  }

  /**
   * Processes the page footer.
   *
   * @param event the event.
   */
  public void pageFinished(final ReportEvent event)
  {
    if (FunctionUtilities.isLayoutLevel(event) == false)
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = event.getReport().getPageFooter();
    processRootBand(b);
  }

  /**
   * Processes the page header.
   *
   * @param event the event.
   */
  public void pageStarted(final ReportEvent event)
  {
    if (FunctionUtilities.isLayoutLevel(event) == false)
    {
      // dont do anything if there is no printing done ...
      return;
    }
    final Band b = event.getReport().getPageHeader();
    processRootBand(b);

    final Band w = event.getReport().getWatermark();
    processRootBand(w);
  }

  /**
   * Processes the root band for the current event. This method must be implemented by all subclasses and contains all
   * code necessary to update the style or structure of the given band. The update must be deterministic, calls must
   * result in the same layout for all calls for a given report processing state.
   *
   * @param b the band.
   */
  protected abstract void processRootBand(Band b);

  /**
   * Format-Functions usually are not expected to return anything.
   *
   * @return null, as format functions do not compute values.
   */
  public Object getValue()
  {
    return null;
  }
}
