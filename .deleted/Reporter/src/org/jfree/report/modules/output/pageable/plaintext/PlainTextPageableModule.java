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
 * PlainTextPageableModule.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.plaintext;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;
import org.jfree.fonts.encoding.EncodingRegistry;

/**
 * The module definition for the plain text pagable export module.
 *
 * @author Thomas Morgner
 */
public class PlainTextPageableModule extends AbstractModule
{
  /**
   * The configuration prefix for all properties.
   */
  public static final String CONFIGURATION_PREFIX =
          "org.jfree.report.modules.output.pageable.plaintext.";

  /**
   * A default value of the 'text encoding' property key.
   */
  public static final String ENCODING_DEFAULT = EncodingRegistry.getPlatformDefaultEncoding();

  /**
   * The property to define the encoding of the text.
   */
  public static final String ENCODING = CONFIGURATION_PREFIX + "Encoding";
  /**
   * The property to define the lines per inch of the text.
   */
  public static final String LINES_PER_INCH = CONFIGURATION_PREFIX + "LinesPerInch";
  /**
   * The property to define the characters per inch of the text.
   */
  public static final String CHARS_PER_INCH = CONFIGURATION_PREFIX + "CharsPerInch";

  /**
   * DefaultConstructor. Loads the module specification.
   *
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error occured.
   */
  public PlainTextPageableModule ()
          throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initalizes the module. This method is empty.
   *
   * @throws ModuleInitializeException if an error occured.
   * @see org.jfree.base.modules.Module#initialize(org.jfree.base.modules.SubSystem)
   */
  public void initialize (final SubSystem subSystem)
          throws ModuleInitializeException
  {
  }
}
