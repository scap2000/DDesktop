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
 * StylesWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;

import java.util.ArrayList;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.Group;
import org.jfree.report.ReportDefinition;
import org.jfree.report.modules.parser.ext.ExtParserModule;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * A styles writer.
 *
 * @author Thomas Morgner.
 */
public class StylesWriter extends AbstractXMLDefinitionWriter
{

  /**
   * Storage for the styles.
   */
  private final ArrayList reportStyles;

  /**
   * Creates a new styles writer.
   *
   * @param reportWriter the report writer.
   * @param xmlWriter  the current indention level.
   */
  public StylesWriter (final ReportWriterContext reportWriter, final XmlWriter xmlWriter)
  {
    super(reportWriter, xmlWriter);
    reportStyles = new ArrayList();
  }

  /**
   * Writes the ihnerited styles to a character stream writer. This will collect all
   * inherited styles, ignoring all styles which are directly bound to an element or which
   * are global default stylesheets.
   *
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write ()
          throws IOException, ReportWriterException
  {
    final ElementStyleSheet[] styles = collectStyles();
    if (styles.length == 0)
    {
      return;
    }

    final XmlWriter xmlWriter = getXmlWriter();
    xmlWriter.writeTag(ExtParserModule.NAMESPACE, STYLES_TAG, XmlWriter.OPEN);
    for (int i = 0; i < styles.length; i++)
    {
      final ElementStyleSheet style = styles[i];
      xmlWriter.writeTag(ExtParserModule.NAMESPACE, STYLE_TAG,
          "name", style.getName(), XmlWriter.OPEN);

      final StyleWriter stW = new StyleWriter
              (getReportWriter(), style, xmlWriter);
      stW.write();

      xmlWriter.writeCloseTag();
    }

    xmlWriter.writeCloseTag();
  }

  /**
   * Collects styles from all the bands in the report. The returned styles are ordered so
   * that parent style sheets are contained before any child stylesheets in the array.
   *
   * @return The styles.
   */
  private ElementStyleSheet[] collectStyles ()
  {
    final ReportDefinition report = getReport();
    collectStylesFromBand(report.getReportHeader());
    collectStylesFromBand(report.getReportFooter());
    collectStylesFromBand(report.getPageHeader());
    collectStylesFromBand(report.getPageFooter());
    collectStylesFromBand(report.getItemBand());
    for (int i = 0; i < report.getGroupCount(); i++)
    {
      final Group g = report.getGroup(i);
      collectStylesFromBand(g.getHeader());
      collectStylesFromBand(g.getFooter());
    }

    return (ElementStyleSheet[])
            reportStyles.toArray(new ElementStyleSheet[reportStyles.size()]);
  }

  /**
   * Collects the styles from a band.
   *
   * @param band the band.
   */
  private void collectStylesFromBand (final Band band)
  {
    collectStylesFromElement(band);

    final Element[] elements = band.getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      if (elements[i] instanceof Band)
      {
        collectStylesFromBand((Band) elements[i]);
      }
      else
      {
        collectStylesFromElement(elements[i]);
      }
    }

  }

  /**
   * Collects the styles from an element.
   *
   * @param element the element.
   */
  private void collectStylesFromElement (final Element element)
  {
    final ElementStyleSheet elementSheet = element.getStyle();

    final ElementStyleSheet[] parents = elementSheet.getParents();
    for (int i = 0; i < parents.length; i++)
    {
      final ElementStyleSheet es = parents[i];
      addCollectableStyleSheet(es);
    }
  }

  /**
   * Adds a defined stylesheet to the styles collection. If the stylesheet is one of the
   * default stylesheets, then it is not collected.
   *
   * @param es the element style sheet.
   */
  private void addCollectableStyleSheet (final ElementStyleSheet es)
  {
    if (es.isGlobalDefault())
    {
      return;
    }

    final ElementStyleSheet[] parents = es.getParents();
    for (int i = 0; i < parents.length; i++)
    {
      final ElementStyleSheet parentsheet = parents[i];
      addCollectableStyleSheet(parentsheet);
    }

    if (reportStyles.contains(es) == false)
    {
      reportStyles.add(es);
    }
  }
}
