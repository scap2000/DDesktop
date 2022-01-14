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
 * ReportConfigurationUtil.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.util;

import org.jfree.report.JFreeReportCoreModule;
import org.jfree.util.Configuration;

/**
 * An Utility class that simplifies using the report configuration.
 *
 * @author Thomas Morgner
 */
public class ReportConfigurationUtil
{
  /**
   * Hidden default constructor.
   */
  private ReportConfigurationUtil()
  {

  }

  /**
   * Checks, whether report processing should be aborted when an exception
   * occurs.
   *
   * @param config the configuration.
   * @return if strict error handling is enabled.
   */
  public static boolean isStrictErrorHandling (final Configuration config)
  {
    final String strictError = config.getConfigProperty(JFreeReportCoreModule.STRICT_ERROR_HANDLING_KEY);
    return "true".equals(strictError);
  }
}
