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
 * ProcessingContext.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.function;

import org.jfree.formula.FormulaContext;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Configuration;

/**
 * The processing context hold information about the progress of the report processing and contains global properties
 * used during the report processing.
 *
 * @author Thomas Morgner
 */
public interface ProcessingContext
{
  /**
   * Returns the current progress level. The number itself has no meaning and is only used to measure the progress of
   * the report processing.
   *
   * @return the progress level.
   */
  public int getProgressLevel();

  /**
   * Returns the total number of different activities the report will process.
   *
   * @return the number of different progress levels.
   */
  public int getProgressLevelCount();

  /**
   * The processing-level is used for dependency tracking. A function that precomputes values should use this level
   * value to determine its current activity.
   *
   * @return the processing level.
   * @see Expression#getDependencyLevel()
   */
  public int getProcessingLevel();

  /**
   * Returns the formula context of this report process. The formula context is required to evaluate inline expression
   * with LibFormula.
   *
   * @return the current formula context.
   */
  public FormulaContext getFormulaContext();

  /**
   * Returns true, if the current processing run is a prepare-run. A prepare run does not generate content, but will be
   * needed to compute the layout. This flag can be used to possibly optimize the content computation. If in doubt on
   * how to interpret the flag, then please ignore this flag. The process may be slightly slower, but at least it will
   * work all the time.
   *
   * @return true, if this is a prepare-run, false if this is a content processing run.
   */
  public boolean isPrepareRun();

  /**
   * Returns the export descriptor from the output-target.
   * 
   * @return the export descriptor string.
   * @see org.jfree.report.layout.output.OutputProcessorMetaData#getExportDescriptor()
   */
  public String getExportDescriptor();

  /**
   * Returns the layout support. The layout support allows functions to do basic layouting.
   *
   * @return the layout support.
   * @deprecated the layouter works differently now. The layout-support returned here is a dummy implementation and
   * does no longer reflect the real layout computations. 
   */
  public LayoutSupport getLayoutSupport();

  /**
   * The resource-bundle factory encapsulates all locale specific resources and provides a system-independent way to
   * create Resource-Bundles. This returns the initial master-report's resource-bundle factory.
   *
   * @return the report's resource-bundle factory.
   */
  public ResourceBundleFactory getResourceBundleFactory();

  /**
   * Returns the content base of the initial master-report. The content-base resource-key can be used to
   * resolve relative paths.
   *
   * @return the initial content base.
   */
  public ResourceKey getContentBase();

  /**
   * Returns the initial master-report's resource manager. The resource manager can be used to load external resources
   * in a system-independent way.
   *
   * @return the master-report's resourcemanager.
   */
  public ResourceManager getResourceManager();

  /**
   * Returns the initial master-report's configuration. The initial configuration is used for all subreports.
   *
   * @return the global report configuration.
   */
  public Configuration getConfiguration();
}
