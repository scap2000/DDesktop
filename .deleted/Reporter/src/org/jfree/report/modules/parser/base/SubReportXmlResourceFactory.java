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
 * SubReportXmlResourceFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.base;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.SubReport;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.util.Configuration;
import org.jfree.xmlns.parser.AbstractXmlResourceFactory;
import org.jfree.xmlns.parser.MultiplexRootElementHandler;

/**
 * Creation-Date: 08.04.2006, 14:27:36
 *
 * @author Thomas Morgner
 */
public class SubReportXmlResourceFactory
    extends AbstractXmlResourceFactory
{
  public SubReportXmlResourceFactory()
  {
  }

  protected Configuration getConfiguration()
  {
    return JFreeReportBoot.getInstance().getGlobalConfig();
  }

  public Class getFactoryType()
  {
    return SubReport.class;
  }

  protected Resource createResource(final ResourceKey targetKey,
                                    final MultiplexRootElementHandler handler,
                                    final Object createdProduct)
  {
    return new ReportResource
        (targetKey, handler.getDependencyCollector(), createdProduct);
  }


}
