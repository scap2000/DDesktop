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
 * ConverterParser.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.converter.parser;

import java.net.URL;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.JFreeReportXmlResourceFactory;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceException;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;

import org.xml.sax.ErrorHandler;

/**
 * The ConverterParser is a filtering proxy implementation that uses the mappings defined
 * in this package to modify the parsed attribute values on the fly into the new values.
 *
 * @author Thomas Morgner
 */
public class ConverterParser extends JFreeReportXmlResourceFactory
{
  private OperationErrorHandler errorHandler;

  public ConverterParser()
  {
    this.errorHandler = new OperationErrorHandler();
  }

  public OperationResult[] getErrors()
  {
    return errorHandler.getErrors();
  }

  protected ErrorHandler getErrorHandler()
  {
    return errorHandler;
  }

  public JFreeReport parse(final URL url, final URL context)
      throws ResourceException
  {
    final ResourceManager resourceManager = new ResourceManager();
    resourceManager.registerDefaults();

    final ResourceKey contextKey = resourceManager.createKey(context);
    final ResourceKey key = resourceManager.createKey(url);
    final Resource resource = resourceManager.create(key, contextKey, JFreeReport.class);
    return (JFreeReport) resource.getResource();

  }
}
