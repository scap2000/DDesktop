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
 * HtmlTableModule.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;

/**
 * The module definition for the Html destTable export module.
 *
 * @author Thomas Morgner
 */
public class HtmlTableModule extends AbstractModule
{
  /**
   * The configuration prefix when reading the configuration settings from the report
   * configuration.
   */
  public static final String CONFIGURATION_PREFIX =
          "org.jfree.report.modules.output.table.html";

  /**
   * The configuration key that defines whether to generate XHTML code.
   */
  public static final String GENERATE_XHTML = "GenerateXHTML";

  /**
   * the fileencoding for the main html file.
   */
  public static final String ENCODING = "Encoding";
  /**
   * a default value for the fileencoding of the main html file.
   */
  public static final String ENCODING_DEFAULT = "UTF-8";

  /**
   * The property key to define whether to build a html body fragment.
   */
  public static final String BODY_FRAGMENT = "BodyFragment";

  public static final String EMPTY_CELLS_USE_CSS = "EmptyCellsUseCSS";

  public static final String TABLE_ROW_BORDER_DEFINITION = "TableRowBorderDefinition";

  public static final String USE_DEVICE_INDEPENDENT_IMAGESIZES = "UseDeviceIndependentImageSize";
  public static final String PROPORTIONAL_COLUMN_WIDTHS = "ProportionalColumnWidths";
  public static final String COPY_EXTERNAL_IMAGES = "org.jfree.report.modules.output.table.html.CopyExternalImages";
  /**
   * DefaultConstructor. Loads the module specification.
   *
   * @throws ModuleInitializeException if an error occured.
   */
  public HtmlTableModule ()
          throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initializes the module. Use this method to perform all initial setup operations. This
   * method is called only once in a modules lifetime. If the initializing cannot be
   * completed, throw a ModuleInitializeException to indicate the error,. The module will
   * not be available to the system.
   *
   * @param subSystem the subSystem.
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error ocurred while initializing the module.
   */
  public void initialize (final SubSystem subSystem)
          throws ModuleInitializeException
  {
  }
}
