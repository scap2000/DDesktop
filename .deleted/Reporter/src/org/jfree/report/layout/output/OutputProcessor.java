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
 * OutputProcessor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import org.jfree.report.layout.model.LogicalPageBox;

/**
 * The output-processor receives the layouted content and is responsible for translating the received content into the
 * specific output format. The output processor also keeps track of the number of received pages and their physical
 * layout.
 *
 * @author Thomas Morgner
 */
public interface OutputProcessor
{
  public OutputProcessorMetaData getMetaData();

  /**
   * A call-back that passes a layouted pagebox to the output processor.
   *
   * @param pageBox
   */
  public void processContent(final LogicalPageBox pageBox) throws ContentProcessingException;
  public void processRecomputedContent(final LogicalPageBox pageBox) throws ContentProcessingException;

  /**
   * A call-back to indicate that the processing of the current process-run has been finished.
   */
  public void processingFinished();

  public int getPageCursor();

  public void setPageCursor(int pc);

  public int getLogicalPageCount();
  public LogicalPageKey getLogicalPage (int page);

  /**
   * Checks, whether the 'processingFinished' event had been received at least
   * once.
   *
   * @return
   */
  public boolean isPaginationFinished();
  public boolean isNeedAlignedPage();

  public int getPhysicalPageCount();
}
