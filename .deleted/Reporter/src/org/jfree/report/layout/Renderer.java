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
 * Renderer.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

import org.jfree.report.Band;
import org.jfree.report.PageDefinition;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.LayoutPagebreakHandler;
import org.jfree.report.layout.output.OutputProcessor;

/**
 * Creation-Date: 08.04.2007, 16:35:29
 *
 * @author Thomas Morgner
 */
public interface Renderer extends Cloneable
{
  public static final int TYPE_NORMALFLOW = 0;
  public static final int TYPE_HEADER = 1;
  public static final int TYPE_FOOTER = 2;
  public static final int TYPE_COLUMNS = 3;
  public static final int TYPE_WATERMARK = 4;

  public OutputProcessor getOutputProcessor();

  public void startReport(final PageDefinition pageDefinition);

  public void startGroup(boolean keepTogether);

  public void startSection(int type);

  public void endSection();

  public void endGroup();

  public void endReport();

  public void add(Band band, ExpressionRuntime runtime, final Object stateKey);

  public boolean validatePages()
      throws ContentProcessingException;

  public boolean processPage (final LayoutPagebreakHandler handler,
                              final Object commitMarker,
                              final boolean performOutput) throws ContentProcessingException;

  public void processIncrementalUpdate(final boolean performOutput) throws ContentProcessingException;

  public int getPagebreaks();

  public boolean isOpen();

  public Object clone() throws CloneNotSupportedException;

  public Object getLastStateKey();

  public void addPagebreak (final Object stateKey);

  public boolean clearPendingPageStart(final LayoutPagebreakHandler layoutPagebreakHandler);

  public boolean isPageStartPending ();

  public Renderer deriveForStorage();

  public Renderer deriveForPagebreak();

  public boolean isValid();

  public void createRollbackInformation();
  public void applyRollbackInformation();

  public void rollback();

  public void setStateKey(Object stateKey);

  public void applyAutoCommit();
}
