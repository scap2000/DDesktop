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
 * SimpleParserModuleInit.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.simple;

import java.net.URL;

import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.ModuleInitializer;
import org.jfree.util.ObjectUtilities;
import org.jfree.xmlns.parser.ParserEntityResolver;

/**
 * Handles the initalisation of the simple parser module. This contains support for the
 * simple report definition format.
 *
 * @author Thomas Morgner
 */
public class SimpleParserModuleInit implements ModuleInitializer
{

  /**
   * the document element tag for the simple report format.
   */
  public static final String SIMPLE_REPORT_TAG = "report";

  /**
   * the Public ID for the simple version of JFreeReport XML definitions.
   */
  public static final String PUBLIC_ID_SIMPLE =
          "-//JFreeReport//DTD report definition//EN//simple/version 0.8.5";

  /**
   * the Public ID for the simple version of JFreeReport XML definitions (pre 0.8.5).
   */
  private static final String PUBLIC_ID_SIMPLE_084 =
          "-//JFreeReport//DTD report definition//EN//simple";
  public static final String SYSTEM_ID = "http://jfreereport.sourceforge.net/report-085.dtd";

  /**
   * DefaultConstructor. Does nothing.
   */
  public SimpleParserModuleInit ()
  {
  }

  /**
   * Initializes the simple parser and registers this handler with the parser base
   * module.
   *
   * @throws ModuleInitializeException if initializing the module failes.
   */
  public void performInit ()
          throws ModuleInitializeException
  {
    final ParserEntityResolver res = ParserEntityResolver.getDefaultResolver();

    final URL urlReportDTD = ObjectUtilities.getResource
            ("org/jfree/report/modules/parser/simple/resources/report-085.dtd",
                    SimpleParserModuleInit.class);

    res.setDTDLocation (PUBLIC_ID_SIMPLE, SYSTEM_ID, urlReportDTD);
    res.setDeprecatedDTDMessage(PUBLIC_ID_SIMPLE_084,
            "The given public identifier for the XML document is deprecated. " +
            "Please use the current document type declaration instead: \n" +
            "  <!DOCTYPE report PUBLIC \n" +
            "      \"-//JFreeReport//DTD report definition//EN//simple/version 0.8.5\"\n" +
            "      \"http://jfreereport.sourceforge.net/report-085.dtd\">");
  }
}
