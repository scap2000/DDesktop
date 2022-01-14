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
 * SimpleSubReportReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.simple.readhandlers;

import java.util.ArrayList;

import org.jfree.report.SubReport;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.base.SubReportReadHandler;
import org.jfree.report.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.common.FunctionsReadHandler;
import org.jfree.report.modules.parser.base.common.IncludeReadHandler;
import org.jfree.report.modules.parser.base.common.ParameterMappingReadHandler;
import org.jfree.report.modules.parser.base.common.ParserConfigurationReadHandler;
import org.jfree.util.Log;
import org.jfree.xmlns.parser.IgnoreAnyChildReadHandler;
import org.jfree.xmlns.parser.RootXmlReadHandler;
import org.jfree.xmlns.parser.XmlReadHandler;

import org.xml.sax.SAXException;

/**
 * Creation-Date: Dec 18, 2006, 1:09:51 PM
 *
 * @author Thomas Morgner
 */
public class SimpleSubReportReadHandler extends AbstractPropertyXmlReadHandler
  implements SubReportReadHandler
{
  /**
   * Literal text for an XML report element.
   */
  public static final String REPORT_TAG = "report";

  /**
   * Literal text for an XML attribute.
   */
  public static final String NAME_ATT = "name";

  /**
   * Literal text for an XML attribute.
   */
  public static final String PAGEFORMAT_ATT = "pageformat";

  /**
   * Literal text for an XML attribute.
   */
  public static final String PAGESPAN_ATT = "pagespan";

  /**
   * Literal text for an XML attribute.
   */
  public static final String UNIT_ATT = "unit";

  /**
   * Literal text for an XML attribute.
   */
  public static final String LEFTMARGIN_ATT = "leftmargin";

  /**
   * Literal text for an XML attribute.
   */
  public static final String RIGHTMARGIN_ATT = "rightmargin";

  /**
   * Literal text for an XML attribute.
   */
  public static final String TOPMARGIN_ATT = "topmargin";

  /**
   * Literal text for an XML attribute.
   */
  public static final String BOTTOMMARGIN_ATT = "bottommargin";

  /**
   * Literal text for an XML attribute.
   */
  public static final String WIDTH_ATT = "width";

  /**
   * Literal text for an XML attribute.
   */
  public static final String HEIGHT_ATT = "height";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ORIENTATION_ATT = "orientation";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ORIENTATION_PORTRAIT_VAL = "portrait";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ORIENTATION_LANDSCAPE_VAL = "landscape";

  /**
   * Literal text for an XML attribute.
   */
  public static final String ORIENTATION_REVERSE_LANDSCAPE_VAL = "reverse_landscape";

  private SubReport report;
  private ArrayList importParameters;
  private ArrayList exportParameters;
  private boolean disableRootTagWarning;

  public SimpleSubReportReadHandler ()
  {
    importParameters = new ArrayList();
    exportParameters = new ArrayList();
  }


  public void setDisableRootTagWarning(final boolean disableWarning)
  {
    disableRootTagWarning = disableWarning;
  }


  public boolean isDisableRootTagWarning()
  {
    return disableRootTagWarning;
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException
  {
    if ("sub-report".equals(getTagName()) && disableRootTagWarning == false)
    {
      Log.info ("Encountered a subreport with an <sub-report> root-element. As of version 0.8.9-rc1, " +
                "this tag has been deprecated and the common <report> tag should be used for both " +
                "standalone and sub-reports.");
    }

    final Object maybeReport = getRootHandler().getHelperObject
        (ReportParserUtil.HELPER_OBJ_REPORT_NAME);
    if (maybeReport instanceof SubReport)
    {
      report = (SubReport) maybeReport;
    }
    else
    {
      report = new SubReport();
    }

    final RootXmlReadHandler parser = getRootHandler();
    if (ReportParserUtil.isIncluded(parser) == false)
    {
      final String query = attrs.getValue(getUri(), "query");
      if (query != null)
      {
        report.setQuery(query);
      }
    }
    getRootHandler().setHelperObject(ReportParserUtil.HELPER_OBJ_REPORT_NAME, report);
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild (final String uri,
                                               final String tagName,
                                               final PropertyAttributes atts)
          throws SAXException
  {
    if (getUri().equals(uri) == false)
    {
      return null;
    }
    else if ("data-factory".equals(tagName))
    {
      return new IgnoreAnyChildReadHandler();
    }
    else if ("reportheader".equals(tagName))
    {
      return new ReportHeaderReadHandler(report.getReportHeader());
    }
    else if ("reportfooter".equals(tagName))
    {
      return new ReportFooterReadHandler(report.getReportFooter());
    }
    else if ("pageheader".equals(tagName))
    {
      return new PageBandReadHandler(report.getPageHeader());
    }
    else if ("pagefooter".equals(tagName))
    {
      return new PageBandReadHandler(report.getPageFooter());
    }
    else if ("watermark".equals(tagName))
    {
      return new WatermarkReadHandler(report.getWatermark());
    }
    else if ("no-data-band".equals(tagName))
    {
      return new RootLevelBandReadHandler(report.getNoDataBand());
    }
    else if ("groups".equals(tagName))
    {
      return new GroupsReadHandler(report.getGroups());
    }
    else if ("items".equals(tagName))
    {
      return new RootLevelBandReadHandler(report.getItemBand());
    }
    else if ("functions".equals(tagName))
    {
      return new FunctionsReadHandler(report);
    }
    else if ("include".equals(tagName))
    {
      return new IncludeReadHandler();
    }
    else if ("parser-config".equals(tagName))
    {
      return new ParserConfigurationReadHandler();
    }
    else if ("import-parameter".equals(tagName))
    {
      final ParameterMappingReadHandler handler = new ParameterMappingReadHandler();
      importParameters.add(handler);
      return handler;
    }
    else if ("export-parameter".equals(tagName))
    {
      final ParameterMappingReadHandler handler = new ParameterMappingReadHandler();
      exportParameters.add(handler);
      return handler;
    }
    else
    {
      return null;
    }
  }

  /**
   * Done parsing.
   *
   * @throws SAXException       if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    super.doneParsing();
    final SubReport report = getSubReport();
    for (int i = 0; i < importParameters.size(); i++)
    {
      final ParameterMappingReadHandler handler =
              (ParameterMappingReadHandler) importParameters.get(i);
      report.addInputParameter(handler.getName(), handler.getAlias());
    }
    for (int i = 0; i < exportParameters.size(); i++)
    {
      final ParameterMappingReadHandler handler =
              (ParameterMappingReadHandler) exportParameters.get(i);
      report.addExportParameter(handler.getAlias(), handler.getName());
    }
  }

  /**
   * Returns the object for this element.
   *
   * @return the object.
   */
  public Object getObject ()
  {
    return report;
  }

  public SubReport getSubReport()
  {
    return report;
  }
}
