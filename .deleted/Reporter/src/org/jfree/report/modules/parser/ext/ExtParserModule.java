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
 * ExtParserModule.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;

/**
 * The module definition for the extended parser module.
 *
 * @author Thomas Morgner
 */
public class ExtParserModule extends AbstractModule
{
  public static final String NAMESPACE =
          "http://jfreereport.sourceforge.net/namespaces/reports/legacy/ext";

  /**
   * DefaultConstructor. Loads the module specification.
   *
   * @throws ModuleInitializeException if an error occured.
   */
  public ExtParserModule ()
          throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initalizes the module. This performs the external initialization and checks that an
   * JAXP1.1 parser is available.
   *
   * @param subSystem the subsystem for this module.
   * @throws ModuleInitializeException if an error occured.
   */
  public void initialize (final SubSystem subSystem)
          throws ModuleInitializeException
  {
    if (isClassLoadable("org.xml.sax.ext.LexicalHandler") == false)
    {
      throw new ModuleInitializeException("Unable to load JAXP-1.1 classes. " +
              "Check your classpath and XML parser configuration.");
    }

    performExternalInitialize(ExtParserModuleInit.class.getName());
  }


}
