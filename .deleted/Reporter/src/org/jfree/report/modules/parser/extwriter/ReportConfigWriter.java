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
 * ReportConfigWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.extwriter;

import java.awt.print.PageFormat;
import java.awt.print.Paper;

import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.Enumeration;

import org.jfree.report.AbstractReportDefinition;
import org.jfree.report.JFreeReport;
import org.jfree.report.PageDefinition;
import org.jfree.report.SimplePageDefinition;
import org.jfree.report.modules.parser.ext.ExtParserModule;
import org.jfree.report.util.PageFormatFactory;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * A report configuration writer.
 *
 * @author Thomas Morgner.
 */
public class ReportConfigWriter extends AbstractXMLDefinitionWriter
{
  protected static final String PAGE_DEFINITION_TAG = "page-definition";
  protected static final String SIMPLE_PAGE_DEFINITION_TAG = "simple-page-definition";
  protected static final String PAGE_TAG = "page";

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
  /**
   * Literal text for an XML attribute.
   */
  public static final String PAGEFORMAT_ATT = "pageformat";

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
   * A constant for the top border.
   */
  private static final int TOP_BORDER = 0;

  /**
   * A constant for the left border.
   */
  private static final int LEFT_BORDER = 1;

  /**
   * A constant for the bottom border.
   */
  private static final int BOTTOM_BORDER = 2;

  /**
   * A constant for the right border.
   */
  private static final int RIGHT_BORDER = 3;

  /**
   * A report configuration writer.
   *
   * @param reportWriter the report writer.
   * @param xmlWriter  the current indention level.
   */
  public ReportConfigWriter(final ReportWriterContext reportWriter,
                            final XmlWriter xmlWriter)
  {
    super(reportWriter, xmlWriter);
  }

  /**
   * Writes the report configuration element.
   *
   * @throws java.io.IOException if there is an I/O problem.
   */
  public void write()
      throws IOException, ReportWriterException
  {
    final XmlWriter xmlWriter = getXmlWriter();
    xmlWriter.writeTag(ExtParserModule.NAMESPACE, REPORT_CONFIG_TAG, XmlWriter.OPEN);

    final AbstractReportDefinition report = getReport();
    if (report instanceof JFreeReport)
    {
      final JFreeReport masterReport = (JFreeReport) report;
      final DataFactoryWriter writer = new DataFactoryWriter(getReportWriter(), getXmlWriter());
      writer.write();

      writePageDefinition();
      writeReportConfig(masterReport.getConfiguration());
    }

    xmlWriter.writeCloseTag();
  }

  private void writeReportConfig(final Configuration config)
      throws IOException
  {
    final XmlWriter writer = getXmlWriter();
    final Enumeration properties = config.getConfigProperties();

    if (properties.hasMoreElements())
    {
      writer.writeTag(ExtParserModule.NAMESPACE, CONFIGURATION_TAG, XmlWriter.OPEN);
      while (properties.hasMoreElements())
      {
        final String key = (String) properties.nextElement();
        final String value = config.getConfigProperty(key);
        if (value != null)
        {
          writer.writeTag(ExtParserModule.NAMESPACE, "property", "name", key, XmlWriter.OPEN);
          writer.writeText(XmlWriter.normalize(value, false));
          writer.writeCloseTag();
        }
      }
      writer.writeCloseTag();
    }

  }


  private void writePageDefinition() throws IOException
  {
    final XmlWriter xmlWriter = getXmlWriter();
    final PageDefinition pageDefinition = getReport().getPageDefinition();
    if (pageDefinition instanceof SimplePageDefinition)
    {
      final SimplePageDefinition spdef = (SimplePageDefinition) pageDefinition;
      final AttributeList attr = new AttributeList();
      attr.setAttribute(ExtParserModule.NAMESPACE, "width",
          String.valueOf(spdef.getPageCountHorizontal()));
      attr.setAttribute(ExtParserModule.NAMESPACE, "height",
          String.valueOf(spdef.getPageCountVertical()));
      xmlWriter.writeTag (ExtParserModule.NAMESPACE,
          SIMPLE_PAGE_DEFINITION_TAG, attr, XmlWriter.OPEN);

      final AttributeList attributes = buildPageFormatProperties(spdef.getPageFormat(0));
      xmlWriter.writeTag (ExtParserModule.NAMESPACE,
          PAGE_TAG, attributes, XmlWriter.CLOSE);
      xmlWriter.writeCloseTag();
    }
    else
    {
      xmlWriter.writeTag(ExtParserModule.NAMESPACE,
          PAGE_DEFINITION_TAG, XmlWriter.OPEN);

      final int max = pageDefinition.getPageCount();
      for (int i = 0; i < max; i++)
      {
        final PageFormat fmt = pageDefinition.getPageFormat(i);

        final AttributeList attributes = buildPageFormatProperties(fmt);
        xmlWriter.writeTag(ExtParserModule.NAMESPACE, PAGE_TAG,
            attributes, XmlWriter.CLOSE);
      }
      xmlWriter.writeCloseTag();
    }
  }

  /**
   * Compiles a collection of page format properties.
   *
   * @return The properties.
   */
  private AttributeList buildPageFormatProperties(final PageFormat fmt)
  {
    final AttributeList retval = new AttributeList();
    final int[] borders = getBorders(fmt.getPaper());

    if (fmt.getOrientation() == PageFormat.LANDSCAPE)
    {
      retval.setAttribute(ExtParserModule.NAMESPACE,
          ORIENTATION_ATT, ORIENTATION_LANDSCAPE_VAL);
      retval.setAttribute(ExtParserModule.NAMESPACE,
          TOPMARGIN_ATT, String.valueOf(borders[RIGHT_BORDER]));
      retval.setAttribute(ExtParserModule.NAMESPACE,
          LEFTMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
      retval.setAttribute(ExtParserModule.NAMESPACE,
          BOTTOMMARGIN_ATT, String.valueOf(borders[LEFT_BORDER]));
      retval.setAttribute(ExtParserModule.NAMESPACE,
          RIGHTMARGIN_ATT, String.valueOf(borders[BOTTOM_BORDER]));
    }
    else if (fmt.getOrientation() == PageFormat.PORTRAIT)
    {
      retval.setAttribute(ExtParserModule.NAMESPACE,
          ORIENTATION_ATT, ORIENTATION_PORTRAIT_VAL);
      retval.setAttribute(ExtParserModule.NAMESPACE,
          TOPMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
      retval.setAttribute(ExtParserModule.NAMESPACE,
          LEFTMARGIN_ATT, String.valueOf(borders[LEFT_BORDER]));
      retval.setAttribute(ExtParserModule.NAMESPACE,
          BOTTOMMARGIN_ATT, String.valueOf(borders[BOTTOM_BORDER]));
      retval.setAttribute(ExtParserModule.NAMESPACE,
          RIGHTMARGIN_ATT, String.valueOf(borders[RIGHT_BORDER]));
    }
    else
    {
      retval.setAttribute(ExtParserModule.NAMESPACE,
          ORIENTATION_ATT, ORIENTATION_REVERSE_LANDSCAPE_VAL);
      retval.setAttribute(ExtParserModule.NAMESPACE,
          TOPMARGIN_ATT, String.valueOf(borders[LEFT_BORDER]));
      retval.setAttribute(ExtParserModule.NAMESPACE,
          LEFTMARGIN_ATT, String.valueOf(borders[BOTTOM_BORDER]));
      retval.setAttribute(ExtParserModule.NAMESPACE,
          BOTTOMMARGIN_ATT, String.valueOf(borders[RIGHT_BORDER]));
      retval.setAttribute(ExtParserModule.NAMESPACE,
          RIGHTMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
    }

    final int w = (int) fmt.getPaper().getWidth();
    final int h = (int) fmt.getPaper().getHeight();

    final String pageDefinition = lookupPageDefinition(w, h);
    if (pageDefinition != null)
    {
      retval.setAttribute(ExtParserModule.NAMESPACE,
          PAGEFORMAT_ATT, pageDefinition);
    }
    else
    {
      retval.setAttribute(ExtParserModule.NAMESPACE,
          WIDTH_ATT, String.valueOf(w));
      retval.setAttribute(ExtParserModule.NAMESPACE,
          HEIGHT_ATT, String.valueOf(h));
    }
    return retval;
  }

  /**
   * Returns the borders for the given paper.
   *
   * @param p the paper.
   * @return The borders.
   */
  private int[] getBorders(final Paper p)
  {
    final int[] retval = new int[4];

    retval[TOP_BORDER] = (int) p.getImageableY();
    retval[LEFT_BORDER] = (int) p.getImageableX();
    retval[BOTTOM_BORDER] = (int) (p.getHeight() - (p.getImageableY() + p.getImageableHeight()));
    retval[RIGHT_BORDER] = (int) (p.getWidth() - (p.getImageableX() + p.getImageableWidth()));
    return retval;
  }

  /**
   * Finds the page definition from the {@link org.jfree.report.util.PageFormatFactory}
   * class that matches the specified width and height.
   *
   * @param w the width.
   * @param h the height.
   * @return The page definition name.
   */
  public String lookupPageDefinition(final int w, final int h)
  {
    try
    {
      final Field[] fields = PageFormatFactory.class.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        final Field f = fields[i];
        if (Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers()))
        {
          final Object o = f.get(PageFormatFactory.getInstance());
          if (o instanceof int[])
          {
            final int[] pageDef = (int[]) o;
            if (pageDef[0] == w && pageDef[1] == h)
            {
              return f.getName();
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      Log.info("Unable to translate the page size", e);
    }
    return null;
  }
}
