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
 * PageableReportProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.base;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.layout.output.AbstractReportProcessor;
import org.jfree.report.layout.output.DefaultOutputFunction;

/**
 * Creation-Date: 08.04.2007, 14:51:53
 *
 * @author Thomas Morgner
 */
public class PageableReportProcessor extends AbstractReportProcessor
{
  public PageableReportProcessor(final JFreeReport report,
                                 final PageableOutputProcessor outputProcessor) throws ReportProcessingException
  {
    super(report, outputProcessor);
  }

  /**
   * Returns the layout manager.  If the key is <code>null</code>, an instance of the <code>SimplePageLayouter</code>
   * class is returned.
   *
   * @return the page layouter.
   */
  protected DefaultOutputFunction createLayoutManager()
  {
    final DefaultOutputFunction pageableOutputFunction = new DefaultOutputFunction();
    pageableOutputFunction.setRenderer(new PageableRenderer(getOutputProcessor()));
    return pageableOutputFunction;
  }

}
