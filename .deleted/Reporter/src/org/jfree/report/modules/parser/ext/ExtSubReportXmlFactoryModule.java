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
 * ExtSubReportXmlFactoryModule.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.modules.parser.ext.readhandlers.ExtSubReportReadHandler;
import org.jfree.xmlns.parser.XmlDocumentInfo;
import org.jfree.xmlns.parser.XmlFactoryModule;
import org.jfree.xmlns.parser.XmlReadHandler;

/**
 * Creation-Date: Dec 15, 2006, 10:01:09 PM
 *
 * @author Thomas Morgner
 */
public class ExtSubReportXmlFactoryModule implements XmlFactoryModule
{
  public ExtSubReportXmlFactoryModule()
  {
  }

  public int getDocumentSupport(final XmlDocumentInfo documentInfo)
  {
    final String rootNamespace = documentInfo.getRootElementNameSpace();
    if (rootNamespace != null && rootNamespace.length() > 0)
    {
      if (ExtParserModule.NAMESPACE.equals(rootNamespace) == false)
      {
        return NOT_RECOGNIZED;
      }
      else if ("report-definition".equals(documentInfo.getRootElement()))
      {
        return RECOGNIZED_BY_NAMESPACE;
      }
      else if ("sub-report".equals(documentInfo.getRootElement()))
      {
        return RECOGNIZED_BY_NAMESPACE;
      }
    }
    else if ("report-definition".equals(documentInfo.getRootElement()))
    {
      return RECOGNIZED_BY_TAGNAME;
    }
    else if ("sub-report".equals(documentInfo.getRootElement()))
    {
      return RECOGNIZED_BY_TAGNAME;
    }

    return NOT_RECOGNIZED;

  }

  public String getDefaultNamespace(final XmlDocumentInfo documentInfo)
  {
    return ExtParserModule.NAMESPACE;
  }

  public XmlReadHandler createReadHandler(final XmlDocumentInfo documentInfo)
  {
    return new ExtSubReportReadHandler();
  }
}
