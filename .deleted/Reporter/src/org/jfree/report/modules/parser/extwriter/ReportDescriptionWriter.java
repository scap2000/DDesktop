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
 * ReportDescriptionWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.Group;
import org.jfree.report.RootLevelBand;
import org.jfree.report.SubReport;
import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.EmptyDataSource;
import org.jfree.report.filter.templates.Template;
import org.jfree.report.function.Expression;
import org.jfree.report.layout.BandLayoutManager;
import org.jfree.report.layout.StaticLayoutManager;
import org.jfree.report.modules.parser.ext.ExtParserModule;
import org.jfree.report.modules.parser.ext.factory.base.ClassFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.base.ObjectDescription;
import org.jfree.report.modules.parser.ext.factory.base.ObjectFactoryException;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateDescription;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * A report description writer.  The {@link ReportDefinitionWriter} class is
 * responsible for writing the complete XML report definition file, but it
 * delegates one large section (the report description) to this class.
 *
 * @author Thomas Morgner.
 */
public class ReportDescriptionWriter extends AbstractXMLDefinitionWriter
{
  /**
   * The 'band' tag.
   */
  public static final String BAND_TAG = "band";

  /**
   * The 'element' tag.
   */
  public static final String ELEMENT_TAG = "element";


  /**
   * The 'fields' tag name.
   */
  public static final String FIELDS_TAG = "fields";

  /**
   * The 'field' tag name.
   */
  public static final String FIELD_TAG = "field";

  /**
   * The 'group-header' tag name.
   */
  public static final String GROUP_HEADER_TAG = "group-header";

  /**
   * The 'group-footer' tag name.
   */
  public static final String GROUP_FOOTER_TAG = "group-footer";

  /**
   * The 'group' tag name.
   */
  public static final String GROUP_TAG = "group";

  /**
   * The 'groups' tag name.
   */
  public static final String GROUPS_TAG = "groups";

  /**
   * The 'watermark' tag name.
   */
  public static final String WATERMARK_TAG = "watermark";

  /**
   * The report description tag name.
   */
  public static final String REPORT_DESCRIPTION_TAG = "report-description";

  /**
   * The 'report-header' tag name.
   */
  public static final String REPORT_HEADER_TAG = "report-header";

  /**
   * The 'report-footer' tag name.
   */
  public static final String REPORT_FOOTER_TAG = "report-footer";

  /**
   * The 'page-header' tag name.
   */
  public static final String PAGE_HEADER_TAG = "page-header";

  /**
   * The 'page-footer' tag name.
   */
  public static final String PAGE_FOOTER_TAG = "page-footer";

  /**
   * The 'itemband' tag name.
   */
  public static final String ITEMBAND_TAG = "itemband";
  public static final String NO_DATA_BAND_TAG = "no-data-band";

  /**
   * Creates a new report description writer.
   *
   * @param reportWriter the report writer.
   * @param indent       the current indention level.
   */
  public ReportDescriptionWriter(final ReportWriterContext reportWriter,
                                 final XmlWriter indent)
  {
    super(reportWriter, indent);
  }

  /**
   * Writes a report description element to a character stream writer.
   *
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write()
      throws IOException, ReportWriterException
  {
    final XmlWriter writer = getXmlWriter();
    writer.writeTag(ExtParserModule.NAMESPACE, REPORT_DESCRIPTION_TAG, XmlWriter.OPEN);

    writeRootBand(REPORT_HEADER_TAG, getReport().getReportHeader());
    writeRootBand(REPORT_FOOTER_TAG, getReport().getReportFooter());
    writeRootBand(PAGE_HEADER_TAG, getReport().getPageHeader());
    writeRootBand(PAGE_FOOTER_TAG, getReport().getPageFooter());
    writeRootBand(WATERMARK_TAG, getReport().getWatermark());
    writeGroups();
    writeRootBand(ITEMBAND_TAG, getReport().getItemBand());
    writeRootBand(NO_DATA_BAND_TAG, getReport().getNoDataBand());

    writer.writeCloseTag();
  }

  private void writeSubReports(final RootLevelBand band)
      throws IOException, ReportWriterException
  {
    final int subReportCount = band.getSubReportCount();
    for (int i = 0; i < subReportCount; i++)
    {
      final SubReport sreport = band.getSubReport(i);
      final ReportWriterContext context =
          new ReportWriterContext(sreport, getReportWriter());
      final SubReportDefinitionWriter writer =
          new SubReportDefinitionWriter(context, getXmlWriter());
      writer.write();
    }
  }

  /**
   * Writes an element for a report band.
   *
   * @param tagName the tag name (for the band).
   * @param band    the band.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeBand(final String tagName,
                         final Band band)
      throws IOException, ReportWriterException
  {
    final XmlWriter writer = getXmlWriter();
    if (band.getName().startsWith(Band.ANONYMOUS_BAND_PREFIX) ||
        band.getName().startsWith(Element.ANONYMOUS_ELEMENT_PREFIX))
    {
      writer.writeTag(ExtParserModule.NAMESPACE, tagName, XmlWriter.OPEN);
    }
    else
    {
      writer.writeTag(ExtParserModule.NAMESPACE, tagName,
          "name", band.getName(), XmlWriter.OPEN);
    }

    writeStyleInfo(band);

    final Element[] list = band.getElementArray();
    for (int i = 0; i < list.length; i++)
    {
      if (list[i] instanceof Band)
      {
        final Band b = (Band) list[i];
        writeBand(BAND_TAG, b);
      }
      else
      {
        writeElement(list[i]);
      }
    }

    if (band instanceof RootLevelBand)
    {
      writeSubReports((RootLevelBand) band);
    }

    writer.writeCloseTag();
  }

  private void writeStyleInfo(final Element band)
      throws IOException, ReportWriterException
  {
    final XmlWriter writer = getXmlWriter();
    final ElementStyleSheet styleSheet = band.getStyle();
    if (isStyleSheetEmpty(styleSheet) == false)
    {
      writer.writeTag(ExtParserModule.NAMESPACE, STYLE_TAG, XmlWriter.OPEN);

      final StyleWriter styleWriter =
          new StyleWriter(getReportWriter(), band.getStyle(), writer);
      styleWriter.write();
      writer.writeCloseTag();
    }


    final Map styleExpressions = band.getStyleExpressions();
    final Iterator styleExpressionsIt = styleExpressions.entrySet().iterator();
    if (styleExpressionsIt.hasNext())
    {
      final FunctionsWriter fnWriter =
          new FunctionsWriter(getReportWriter(), writer);
      while (styleExpressionsIt.hasNext())
      {
        final Map.Entry entry = (Map.Entry) styleExpressionsIt.next();
        final StyleKey key = (StyleKey) entry.getKey();
        final Expression ex = (Expression) entry.getValue();
        fnWriter.writeStyleExpression(ex, key);
      }
    }
  }

  /**
   * Checks, whether the given stylesheet is empty and does not inherit values
   * from modifiable or userdefined parents.
   *
   * @param es the element stylesheet to test
   * @return true, if the sheet is empty, false otherwise.
   */
  private boolean isStyleSheetEmpty(final ElementStyleSheet es)
  {
    if (es.getParents().length > 0)
    {
      return false;
    }
    final Iterator definedPropertyNames = es.getDefinedPropertyNames();
    if (definedPropertyNames.hasNext() == false)
    {
      return true;
    }
    final StyleKey name = (StyleKey) definedPropertyNames.next();
    if (BandLayoutManager.LAYOUTMANAGER.equals(name) == false)
    {
      return false;
    }

    final Object styleProperty =
        es.getStyleProperty(BandLayoutManager.LAYOUTMANAGER);
    if (styleProperty instanceof StaticLayoutManager || styleProperty == null)
    {
      if (definedPropertyNames.hasNext() == false)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Writes an element to a character stream writer.
   *
   * @param element the element.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeElement(final Element element)
      throws IOException, ReportWriterException
  {
    final AttributeList attList = new AttributeList();
    if (element.getName().startsWith(Element.ANONYMOUS_ELEMENT_PREFIX) == false)
    {
      attList.setAttribute(ExtParserModule.NAMESPACE, "name", element.getName());
    }
    attList.setAttribute(ExtParserModule.NAMESPACE, "type", element.getContentType());

    final XmlWriter writer = getXmlWriter();
    writer.writeTag(ExtParserModule.NAMESPACE, ELEMENT_TAG, attList, XmlWriter.OPEN);

    writeStyleInfo(element);
    writeDataSourceForElement(element);

    writer.writeCloseTag();
  }

  /**
   * Writes the datasource- or template-tag for an given element.
   *
   * @param element the element, which should be written.
   * @throws ReportWriterException if there is a problem writing the report
   * @throws IOException           if there is an IO error.
   */
  protected void writeDataSourceForElement(final Element element)
      throws ReportWriterException, IOException
  {
    if ((element.getDataSource() instanceof EmptyDataSource))
    {
      return;
    }

    if (element.getDataSource() instanceof Template == false)
    {
      writeDataSource(element.getDataSource());
      return;
    }

    final TemplateCollector tc = getReportWriter().getTemplateCollector();
    final Template template = (Template) element.getDataSource();

    // the template description of the element template will get the
    // template name as its name.
    final TemplateDescription templateDescription =
        tc.getDescription(template);

    if (templateDescription == null)
    {
      throw new ReportWriterException("Unknown template type: " + template);
    }

    // create the parent description before the template description is filled.
    final TemplateDescription parentTemplate = (TemplateDescription) templateDescription.getInstance();

    try
    {
      templateDescription.setParameterFromObject(template);
    }
    catch (ObjectFactoryException ofe)
    {
      throw new ReportWriterException("Error while preparing the template", ofe);
    }

    final TemplateWriter templateWriter = new TemplateWriter
        (getReportWriter(), getXmlWriter(), templateDescription, parentTemplate);
    templateWriter.write();
  }

  /**
   * Writes a data source to a character stream writer.
   *
   * @param datasource the datasource.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeDataSource(final DataSource datasource)
      throws IOException, ReportWriterException
  {
    final ReportWriterContext reportWriter = getReportWriter();
    final ClassFactoryCollector classFactoryCollector =
        reportWriter.getClassFactoryCollector();
    ObjectDescription od =
        classFactoryCollector.getDescriptionForClass(datasource.getClass());
    if (od == null)
    {
      od = classFactoryCollector.
          getSuperClassObjectDescription(datasource.getClass(), null);
    }

    if (od == null)
    {
      throw new ReportWriterException("Unable to resolve DataSource: " + datasource.getClass());
    }

    final DataSourceCollector dataSourceCollector =
        reportWriter.getDataSourceCollector();
    final String dsname = dataSourceCollector.getDataSourceName(od);
    if (dsname == null)
    {
      throw new ReportWriterException("No name for DataSource " + datasource);
    }

    final XmlWriter writer = getXmlWriter();
    writer.writeTag(ExtParserModule.NAMESPACE,
        DATASOURCE_TAG, "type", dsname, XmlWriter.OPEN);

    final DataSourceWriter dsWriter =
        new DataSourceWriter(reportWriter, datasource, od, writer);
    dsWriter.write();

    writer.writeCloseTag();
  }

  /**
   * Writes groups to a character stream writer.
   *
9   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeGroups()
      throws IOException, ReportWriterException
  {
    final XmlWriter writer = getXmlWriter();
    writer.writeTag(ExtParserModule.NAMESPACE, GROUPS_TAG, XmlWriter.OPEN);

    //logComment = true;
    final int groupSize = getReport().getGroupCount();
    for (int i = 0; i < groupSize; i++)
    {
      final Group g = getReport().getGroup(i);
      writer.writeTag(ExtParserModule.NAMESPACE, GROUP_TAG,
          "name", g.getName(), XmlWriter.OPEN);

      final List fields = g.getFields();
      if (fields.isEmpty() == false)
      {
        writer.writeTag(ExtParserModule.NAMESPACE, FIELDS_TAG, XmlWriter.OPEN);

        for (int f = 0; f < fields.size(); f++)
        {
          final String field = (String) fields.get(f);
          writer.writeTag(ExtParserModule.NAMESPACE, FIELD_TAG, XmlWriter.OPEN);
          writer.writeText(XmlWriter.normalize(field, false));
          writer.writeCloseTag();
        }
        writer.writeCloseTag();
      }
      else
      {
        writer.writeTag(ExtParserModule.NAMESPACE, FIELDS_TAG, XmlWriter.CLOSE);
      }

      writeRootBand(GROUP_HEADER_TAG, g.getHeader());
      writeRootBand(GROUP_FOOTER_TAG, g.getFooter());

      writer.writeCloseTag();
    }

    writer.writeCloseTag();
  }

  private void writeRootBand(final String tag,
                             final Band band)
      throws IOException, ReportWriterException
  {
    if (isEmptyRootBand(band))
    {
      return;
    }
    writeBand(tag, band);
  }

  private boolean isEmptyRootBand (final Band band)
  {
    if (band.getName().startsWith(Band.ANONYMOUS_BAND_PREFIX) == false)
    {
      return false;
    }
    if (band.getName().startsWith(Band.ANONYMOUS_ELEMENT_PREFIX) == false)
    {
      return false;
    }
    if (band.getElementCount() != 0)
    {
      return false;
    }
    if (band instanceof RootLevelBand)
    {
      final RootLevelBand rlb = (RootLevelBand) band;
      if (rlb.getSubReportCount() > 0)
      {
        return false;
      }
    }
    final ElementStyleSheet styleSheet = band.getStyle();
    if (isStyleSheetEmpty(styleSheet))
    {
      return true;
    }
    return false;
  }

}
