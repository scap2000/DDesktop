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
 * CachingReportGenerator.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.base;

import java.io.File;

import java.net.URL;

import org.jfree.report.JFreeReport;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceException;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;

/**
 * A caching report generator.
 *
 * @author Thomas Morgner
 * @deprecated The capabilities of this class have been merged with the
 *             ReportGenerator class.
 */
public class CachingReportGenerator
{
  private static CachingReportGenerator instance;

  public static synchronized CachingReportGenerator getInstance()
  {
    if (instance == null)
    {
      instance = new CachingReportGenerator();
    }
    return instance;
  }

  private ResourceManager resourceManager;

  private CachingReportGenerator()
  {
    resourceManager = new ResourceManager();
    resourceManager.registerDefaults();
  }

  public JFreeReport parseReport(final URL file, final URL context)
      throws ResourceException
  {
    if (file == null)
    {
      throw new NullPointerException("Report-Definition URL cannot be null.");
    }

    final ResourceKey key = resourceManager.createKey(file);
    final ResourceKey contextKey;
    if (context != null)
    {
      contextKey = resourceManager.createKey(context);
    }
    else
    {
      contextKey = null;
    }

    final Resource resource = resourceManager.create(key, contextKey, JFreeReport.class);
    return (JFreeReport) resource.getResource();

  }

  public JFreeReport parseReport(final URL file)
      throws ResourceException
  {
    return parseReport(file, file);
  }

  public JFreeReport parseReport(final File file)
      throws ResourceException
  {
    if (file == null)
    {
      throw new NullPointerException("Report-Definition URL cannot be null.");
    }

    final ResourceKey key = resourceManager.createKey(file);
    final Resource resource = resourceManager.create(key, key, JFreeReport.class);
    return (JFreeReport) resource.getResource();
  }
}
