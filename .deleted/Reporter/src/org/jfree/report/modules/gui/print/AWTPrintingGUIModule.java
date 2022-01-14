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
 * AWTPrintingGUIModule.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.print;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;

/**
 * The module definition for the AWT printing export gui module. The module contains 2
 * export plugins, the page setup plugin and the printing plugin.
 *
 * @author Thomas Morgner
 */
public class AWTPrintingGUIModule extends AbstractModule
{
//  /**
//   * The printing export plugin preference key.
//   */
//  public static final String PRINT_ORDER_KEY =
//          "org.jfree.report.modules.gui.print.Order";
//  /**
//   * The printing export plugin enable key.
//   */
//  public static final String PRINT_ENABLE_KEY =
//          "org.jfree.report.modules.gui.print.Enable";
//  /**
//   * The pagesetup export plugin preference key.
//   */
//  private static final String PAGESETUP_ORDER_KEY =
//          "org.jfree.report.modules.gui.print.pagesetup.Order";
//  /**
//   * The pagesetup export plugin enable key.
//   */
//  private static final String PAGESETUP_ENABLE_KEY =
//          "org.jfree.report.modules.gui.print.pagesetup.Enable";
//  public static final String PRINT_SERVICE_KEY =
//          "org.jfree.report.modules.gui.print.PrintService";

  /**
   * DefaultConstructor. Loads the module specification.
   *
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error occured.
   */
  public AWTPrintingGUIModule ()
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
