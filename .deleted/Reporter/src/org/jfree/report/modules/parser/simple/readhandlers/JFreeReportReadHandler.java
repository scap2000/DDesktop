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
 * JFreeReportReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import java.awt.print.PageFormat;
import java.awt.print.Paper;

import org.jfree.report.DataFactory;
import org.jfree.report.JFreeReport;
import org.jfree.report.SimplePageDefinition;
import org.jfree.report.modules.parser.base.DataFactoryReadHandler;
import org.jfree.report.modules.parser.base.DataFactoryReadHandlerFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.common.ConfigurationReadHandler;
import org.jfree.report.modules.parser.base.common.FunctionsReadHandler;
import org.jfree.report.modules.parser.base.common.IncludeReadHandler;
import org.jfree.report.modules.parser.base.common.ParserConfigurationReadHandler;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.util.Log;
import org.jfree.xmlns.common.ParserUtil;
import org.jfree.xmlns.parser.ParseException;
import org.jfree.xmlns.parser.RootXmlReadHandler;
import org.jfree.xmlns.parser.XmlReadHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * &lt;!ELEMENT report   (configuration?, reportheader?, reportfooter?, pageheader?,
 * pagefooter?, watermark?, groups?, items?, functions?)&gt;
 * &lt;!ATTLIST report
 *   width          CDATA           #IMPLIED
 *   height         CDATA           #IMPLIED
 *   name           CDATA           #IMPLIED
 *   pageformat     %pageFormats;   #IMPLIED
 *   orientation    (%orientations;) "portrait"
 *   leftmargin     CDATA           #IMPLIED
 *   rightmargin    CDATA           #IMPLIED
 *   topmargin      CDATA           #IMPLIED
 *   bottommargin   CDATA           #IMPLIED
 * &gt;
 * </pre>
 */
public class JFreeReportReadHandler extends AbstractPropertyXmlReadHandler
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

  private JFreeReport report;
  private DataFactoryReadHandler dataFactoryReadHandler;

  public JFreeReportReadHandler ()
  {
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @noinspection SuspiciousNameCombination
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException
  {
    final Object maybeReport = getRootHandler().getHelperObject
        (ReportParserUtil.HELPER_OBJ_REPORT_NAME);
    final JFreeReport report;
    if (maybeReport instanceof JFreeReport == false)
    {
      // replace it ..
      report = new JFreeReport();
    }
    else
    {
      report = (JFreeReport) maybeReport;
    }

    final RootXmlReadHandler parser = getRootHandler();
    if (ReportParserUtil.isIncluded(parser) == false)
    {
      final String query = attrs.getValue(getUri(), "query");
      if (query != null)
      {
        report.setQuery(query);
      }

      final String name = attrs.getValue(getUri(), NAME_ATT);
      if (name != null)
      {
        report.setName(name);
      }

      PageFormat format = report.getPageDefinition().getPageFormat(0);
      float defTopMargin = (float) format.getImageableY();
      float defBottomMargin = (float) (format.getHeight() - format.getImageableHeight()
              - format.getImageableY());
      float defLeftMargin = (float) format.getImageableX();
      float defRightMargin = (float) (format.getWidth() - format.getImageableWidth()
              - format.getImageableX());

      format = createPageFormat(format, attrs);

      defTopMargin = ParserUtil.parseFloat(attrs.getValue(getUri(), TOPMARGIN_ATT), defTopMargin);
      defBottomMargin = ParserUtil.parseFloat(attrs.getValue(getUri(), BOTTOMMARGIN_ATT), defBottomMargin);
      defLeftMargin = ParserUtil.parseFloat(attrs.getValue(getUri(), LEFTMARGIN_ATT), defLeftMargin);
      defRightMargin = ParserUtil.parseFloat(attrs.getValue(getUri(), RIGHTMARGIN_ATT), defRightMargin);

      final Paper p = format.getPaper();
      switch (format.getOrientation())
      {
        case PageFormat.PORTRAIT:
          PageFormatFactory.getInstance().setBorders(p, defTopMargin, defLeftMargin,
                  defBottomMargin, defRightMargin);
          break;
        case PageFormat.LANDSCAPE:
          // right, top, left, bottom
          PageFormatFactory.getInstance().setBorders(p, defRightMargin, defTopMargin,
                  defLeftMargin, defBottomMargin);
          break;
        case PageFormat.REVERSE_LANDSCAPE:
          PageFormatFactory.getInstance().setBorders(p, defLeftMargin, defBottomMargin,
                  defRightMargin, defTopMargin);
          break;
        default:
          throw new IllegalStateException("Unexpected paper orientation.");
      }

      final int pageSpan = ParserUtil.parseInt(attrs.getValue(getUri(), PAGESPAN_ATT), 1);

      format.setPaper(p);
      report.setPageDefinition(new SimplePageDefinition(format, pageSpan, 1));
    }
    getRootHandler().setHelperObject(ReportParserUtil.HELPER_OBJ_REPORT_NAME, report);
    this.report = report;
  }


  /**
   * Creates the pageFormat by using the given Attributes. If an PageFormat name is given,
   * the named PageFormat is used and the parameters width and height are ignored. If no
   * name is defined, height and width attributes are used to create the pageformat. The
   * attributes define the dimension of the PageFormat in points, where the printing
   * resolution is defined at 72 pixels per inch.
   *
   * @param format the page format.
   * @param atts   the element attributes.
   * @return the page format.
   *
   * @throws SAXException if there is an error parsing the report.
   */
  private PageFormat createPageFormat (final PageFormat format, final Attributes atts)
          throws SAXException
  {
    final String pageformatName = atts.getValue(getUri(), PAGEFORMAT_ATT);

    final int orientationVal;
    final String orientation = atts.getValue(getUri(), ORIENTATION_ATT);
    if (orientation == null)
    {
      orientationVal = PageFormat.PORTRAIT;
    }
    else if (orientation.equals(ORIENTATION_LANDSCAPE_VAL))
    {
      orientationVal = PageFormat.LANDSCAPE;
    }
    else if (orientation.equals(ORIENTATION_REVERSE_LANDSCAPE_VAL))
    {
      orientationVal = PageFormat.REVERSE_LANDSCAPE;
    }
    else if (orientation.equals(ORIENTATION_PORTRAIT_VAL))
    {
      orientationVal = PageFormat.PORTRAIT;
    }
    else
    {
      throw new ParseException("Orientation value in REPORT-Tag is invalid.",
              getRootHandler().getDocumentLocator());
    }
    if (pageformatName != null)
    {
      final Paper p = PageFormatFactory.getInstance().createPaper(pageformatName);
      if (p == null)
      {
        Log.warn("Unable to create the requested Paper. " + pageformatName);
        return format;
      }
      return PageFormatFactory.getInstance().createPageFormat(p, orientationVal);
    }

    if (atts.getValue(getUri(), WIDTH_ATT) != null &&
        atts.getValue(getUri(), HEIGHT_ATT) != null)
    {
      final int[] pageformatData = new int[2];
      pageformatData[0] = ParserUtil.parseInt(atts.getValue(getUri(), WIDTH_ATT), "No Width set", getLocator());
      pageformatData[1] = ParserUtil.parseInt(atts.getValue(getUri(), HEIGHT_ATT), "No Height set", getLocator());
      final Paper p = PageFormatFactory.getInstance().createPaper(pageformatData);
      if (p == null)
      {
        Log.warn("Unable to create the requested Paper. Paper={" + pageformatData[0] + ", "
                + pageformatData[1] + '}');
        return format;
      }
      return PageFormatFactory.getInstance().createPageFormat(p, orientationVal);
    }

    Log.info("Insufficient Data to create a pageformat: Returned default.");
    return format;
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

    final DataFactoryReadHandlerFactory factory = DataFactoryReadHandlerFactory.getInstance();
    final DataFactoryReadHandler handler = (DataFactoryReadHandler) factory.getHandler(uri, tagName);
    if (handler != null)
    {
      dataFactoryReadHandler = handler;
      return handler;
    }

    if (getUri().equals(uri) == false)
    {
      return null;
    }

    if ("configuration".equals(tagName))
    {
      return new ConfigurationReadHandler(report.getReportConfiguration());
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
    else
    {
      return null;
    }
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    if (dataFactoryReadHandler == null)
    {
      return;
    }
    final DataFactory dataFactory = dataFactoryReadHandler.getDataFactory();
    if (dataFactory != null)
    {
      report.setDataFactory(dataFactory);
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
}
