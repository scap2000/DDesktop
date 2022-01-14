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
 * RootBandReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.readhandlers;

import java.util.ArrayList;

import org.jfree.report.AbstractRootLevelBand;
import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.SubReport;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.common.IncludeSubReportReadHandler;
import org.jfree.xmlns.parser.XmlReadHandler;

import org.xml.sax.SAXException;

/**
 * Creation-Date: Dec 18, 2006, 3:34:02 PM
 *
 * @author Thomas Morgner
 */
public class RootBandReadHandler extends BandReadHandler
{
  private ArrayList subReportHandlers;

  public RootBandReadHandler(final Band element)
  {
    super(element);
    this.subReportHandlers = new ArrayList();
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final PropertyAttributes atts)
      throws SAXException
  {
    if (isSameNamespace(uri))
    {
      if ("sub-report".equals(tagName))
      {
        final IncludeSubReportReadHandler subReportReadHandler =
            new IncludeSubReportReadHandler();
        subReportHandlers.add(subReportReadHandler);
        return subReportReadHandler;
      }
    }
    return super.getHandlerForChild(uri, tagName, atts);
  }


  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    super.doneParsing();
    final Element band = getElement();
    if (band instanceof AbstractRootLevelBand)
    {
      final AbstractRootLevelBand arlb = (AbstractRootLevelBand) band;
      for (int i = 0; i < subReportHandlers.size(); i++)
      {
        final IncludeSubReportReadHandler handler =
            (IncludeSubReportReadHandler) subReportHandlers.get(i);
        arlb.addSubReport((SubReport) handler.getObject());
      }
    }
  }
}
