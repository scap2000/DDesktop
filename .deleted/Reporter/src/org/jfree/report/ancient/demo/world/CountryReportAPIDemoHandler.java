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
 * CountryReportAPIDemoHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.ancient.demo.world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.net.URL;

import javax.swing.JComponent;

import org.jfree.report.ElementAlignment;
import org.jfree.report.Group;
import org.jfree.report.GroupFooter;
import org.jfree.report.GroupHeader;
import org.jfree.report.GroupList;
import org.jfree.report.ItemBand;
import org.jfree.report.JFreeReport;
import org.jfree.report.PageFooter;
import org.jfree.report.PageHeader;
import org.jfree.report.ReportFooter;
import org.jfree.report.ReportHeader;
import org.jfree.report.TableDataFactory;
import org.jfree.report.demo.util.AbstractDemoHandler;
import org.jfree.report.elementfactory.DateFieldElementFactory;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.NumberFieldElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.function.ElementVisibilitySwitchFunction;
import org.jfree.report.function.ExpressionCollection;
import org.jfree.report.function.ItemSumFunction;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.FontDefinition;
import org.jfree.ui.FloatDimension;
import org.jfree.util.ObjectUtilities;

/**
 * This creates a report similar to the one defined by report1.xml.
 *
 * @author Thomas Morgner
 */
public class CountryReportAPIDemoHandler extends AbstractDemoHandler
{
  private CountryDataTableModel data;


  /**
   * Default constructor.
   */
  public CountryReportAPIDemoHandler ()
  {
    data = new CountryDataTableModel();
  }

  public String getDemoName()
  {
    return "Country Report Demo (API)";
  }

  public URL getDemoDescriptionSource()
  {
    return ObjectUtilities.getResourceRelative("country-report-api.html", CountryReportAPIDemoHandler.class);
  }

  public JComponent getPresentationComponent()
  {
    return createDefaultTable(data);
  }

  /**
   * Creates the page header.
   *
   * @return the page header.
   */
  private PageHeader createPageHeader ()
  {
    final PageHeader header = new PageHeader();
    header.setName("Page-header");
    header.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, new Float(18));
    header.getStyle().setFontDefinitionProperty(new FontDefinition("Serif", 10));
    header.setDisplayOnFirstPage(true);
    header.setDisplayOnLastPage(false);

    // is by default true, but it is defined in the xml template, so I add it here too.
    header.addElement(StaticShapeElementFactory.createRectangleShapeElement(null, Color.decode("#AFAFAF"), null,
            new Rectangle2D.Float(0, 0, -100, -100),
            false, true));
    final DateFieldElementFactory factory = new DateFieldElementFactory();
    factory.setName("Date");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new FloatDimension(-100, 14));
    factory.setHorizontalAlignment(ElementAlignment.RIGHT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("<null>");
    factory.setFormatString("d-MMM-yyyy");
    factory.setFieldname("report.date");
    header.addElement(factory.createElement());

    header.addElement(StaticShapeElementFactory.createHorizontalLine("line1", Color.decode("#CFCFCF"),
            new BasicStroke(2), 16));
    return header;
  }

  /**
   * Creates a page footer.
   *
   * @return The page footer.
   */
  private PageFooter createPageFooter ()
  {
    final PageFooter pageFooter = new PageFooter();
    pageFooter.setName("Page-Footer");
    pageFooter.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, new Float(30));
    pageFooter.getStyle().setFontDefinitionProperty(new FontDefinition("Dialog", 10));

    pageFooter.addElement(StaticShapeElementFactory.createRectangleShapeElement
            (null, Color.black, null, new Rectangle2D.Float(0, 0, -100, -100), true, false));

    final LabelElementFactory factory = new LabelElementFactory();
    factory.setName("Page-Footer-Label");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new FloatDimension(-100, 0));
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.TOP);
    factory.setText("Some Text for the page footer");
    factory.setDynamicHeight(Boolean.TRUE);
    pageFooter.addElement(factory.createElement());
    return pageFooter;
  }

  /**
   * Creates the report footer.
   *
   * @return the report footer.
   */
  private ReportFooter createReportFooter ()
  {
    final ReportFooter footer = new ReportFooter();
    footer.setName("Report-Footer");
    footer.getStyle().setStyleProperty (ElementStyleKeys.MIN_HEIGHT, new Float(48));
    footer.getStyle().setFontDefinitionProperty
            (new FontDefinition("Serif", 16, true, false, false, false));

    final LabelElementFactory factory = new LabelElementFactory();
    factory.setName("Report-Footer-Label");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new FloatDimension(-100, 24));
    factory.setHorizontalAlignment(ElementAlignment.CENTER);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setText("*** END OF REPORT ***");
    footer.addElement(factory.createElement());
    return footer;
  }

  /**
   * Creates the report header.
   *
   * @return the report header.
   */
  private ReportHeader createReportHeader ()
  {
    final ReportHeader header = new ReportHeader();
    header.setName("Report-Header");
    header.getStyle().setStyleProperty (ElementStyleKeys.MIN_HEIGHT, new Float(48));
    header.getStyle().setFontDefinitionProperty
            (new FontDefinition("Serif", 20, true, false, false, false));

    final LabelElementFactory factory = new LabelElementFactory();
    factory.setName("Report-Header-Label");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new FloatDimension(-100, 24));
    factory.setHorizontalAlignment(ElementAlignment.CENTER);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setText("LIST OF CONTINENTS BY COUNTRY");
    header.addElement(factory.createElement());
    return header;
  }


  /**
   * Creates the itemBand.
   *
   * @return the item band.
   */
  private ItemBand createItemBand ()
  {
    final ItemBand items = new ItemBand();
    items.setName("Items");
    items.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, new Float(10));
    items.getStyle().setFontDefinitionProperty
            (new FontDefinition("Monospaced", 10));


    items.addElement(StaticShapeElementFactory.createRectangleShapeElement
            ("background", Color.decode("#DFDFDF"), new BasicStroke(0),
                    new Rectangle2D.Float(0, 0, -100, -100), false, true));
    items.addElement(StaticShapeElementFactory.createHorizontalLine
            ("top", Color.decode("#DFDFDF"), new BasicStroke(0.1f), 0));
    items.addElement(StaticShapeElementFactory.createHorizontalLine
            ("bottom", Color.decode("#DFDFDF"), new BasicStroke(0.1f), 10));

    TextFieldElementFactory factory = new TextFieldElementFactory();
    factory.setName("Country Element");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new FloatDimension(176, 10));
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("<null>");
    factory.setFieldname("Country");
    items.addElement(factory.createElement());

    factory = new TextFieldElementFactory();
    factory.setName("Code Element");
    factory.setAbsolutePosition(new Point2D.Float(180, 0));
    factory.setMinimumSize(new FloatDimension(76, 10));
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("<null>");
    factory.setFieldname("ISO Code");
    items.addElement(factory.createElement());

    final NumberFieldElementFactory nfactory = new NumberFieldElementFactory();
    nfactory.setName("Population Element");
    nfactory.setAbsolutePosition(new Point2D.Float(260, 0));
    nfactory.setMinimumSize(new FloatDimension(76, 10));
    nfactory.setHorizontalAlignment(ElementAlignment.LEFT);
    nfactory.setVerticalAlignment(ElementAlignment.MIDDLE);
    nfactory.setNullString("<null>");
    nfactory.setFieldname("Population");
    nfactory.setFormatString("#,##0");
    items.addElement(nfactory.createElement());
    return items;
  }

  /**
   * Creates the function collection. The xml definition for this construct:
   * <p/>
   * <pre>
   * <functions>
   * <function name="sum" class="org.jfree.report.function.ItemSumFunction">
   * <properties>
   * <property name="field">Population</property>
   * <property name="group">Continent Group</property>
   * </properties>
   * </function>
   * <function name="backgroundTrigger"
   * class="org.jfree.report.function.ElementVisibilitySwitchFunction">
   * <properties>
   * <property name="element">background</property>
   * </properties>
   * </function>
   * </functions>
   * </pre>
   *
   * @return the functions.
   */
  private ExpressionCollection createFunctions ()
  {
    final ExpressionCollection functions = new ExpressionCollection();

    final ItemSumFunction sum = new ItemSumFunction();
    sum.setName("sum");
    sum.setField("Population");
    sum.setGroup("Continent Group");
    functions.add(sum);

    final ElementVisibilitySwitchFunction backgroundTrigger = new ElementVisibilitySwitchFunction();
    backgroundTrigger.setName("backgroundTrigger");
    backgroundTrigger.setElement("background");
    functions.add(backgroundTrigger);
    return functions;
  }

  /**
   * <pre>
   * <groups>
   * <p/>
   * ... create the groups and add them to the list ...
   * <p/>
   * </groups>
   * </pre>
   *
   * @return the groups.
   */
  private GroupList createGroups ()
  {
    final GroupList list = new GroupList();
    list.add(createContinentGroup());
    return list;
  }

  /**
     * <pre>
     * <group name="Continent Group">
     * <groupheader height="18" fontname="Monospaced" fontstyle="bold" fontsize="9"
     * pagebreak="false">
     * <label name="Label 5" minX="0" minY="1" width="76" height="9" alignment="left">CONTINENT:</label>
     * <string-field name="Continent Element" minX="96" minY="1" width="76" height="9"
     * alignment="left"
     * fieldname="Continent"/>
     * <line name="line1" x1="0" y1="12" x2="0" y2="12" weight="0.5"/>
     * </groupheader>
     * <groupfooter height="18" fontname="Monospaced" fontstyle="bold" fontsize="9">
     * <label name="Label 6" minX="0" minY="0" width="450" height="12" alignment="left"
     * baseline="10">Population:</label>
     * <number-function minX="260" minY="0" width="76" height="12" alignment="right"
     * baseline="10"
     * format="#,##0" function="sum"/>
     * </groupfooter>
     * <fields>
     * <field>Continent</field>
     * </fields>
     * </group>
     * </pre>
     *
     * @return the continent group.
     */
  private Group createContinentGroup ()
  {
    final Group continentGroup = new Group();
    continentGroup.setName("Continent Group");
    continentGroup.addField("Continent");

    final GroupHeader header = new GroupHeader();
    header.setName("Continent-Group-Header");
    header.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, new Float(18));
    header.getStyle().setFontDefinitionProperty
            (new FontDefinition("Monospaced", 9, true, false, false, false));

    LabelElementFactory factory = new LabelElementFactory();
    factory.setName("Continent-Group-Header-Label");
    factory.setAbsolutePosition(new Point2D.Float(0, 1));
    factory.setMinimumSize(new FloatDimension(76, 9));
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setText("CONTINENT:");
    header.addElement(factory.createElement());

    final TextFieldElementFactory tfactory = new TextFieldElementFactory();
    tfactory.setName("Continent-Group-Header Continent Element");
    tfactory.setAbsolutePosition(new Point2D.Float(96, 1));
    tfactory.setMinimumSize(new FloatDimension(76, 9));
    tfactory.setHorizontalAlignment(ElementAlignment.LEFT);
    tfactory.setVerticalAlignment(ElementAlignment.MIDDLE);
    tfactory.setNullString("<null>");
    tfactory.setFieldname("Continent");
    header.addElement(tfactory.createElement());

    header.addElement(StaticShapeElementFactory.createHorizontalLine
            ("Continent-Group-Header line", null, new BasicStroke(0.5f), 12));
    continentGroup.setHeader(header);

    final GroupFooter footer = new GroupFooter();
    footer.setName("Continent-Group-Footer");
    footer.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, new Float(18));
    footer.getStyle().setFontDefinitionProperty
            (new FontDefinition("Monospaced", 9, true, false, false, false));

    factory = new LabelElementFactory();
    factory.setName("Continent-Group-Footer Label");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new FloatDimension(100, 12));
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setText("Population:");
    footer.addElement(factory.createElement());

    final NumberFieldElementFactory nfactory = new NumberFieldElementFactory();
    nfactory.setName("Continent-Group-Footer Sum");
    nfactory.setAbsolutePosition(new Point2D.Float(260, 0));
    nfactory.setMinimumSize(new FloatDimension(76, 12));
    nfactory.setHorizontalAlignment(ElementAlignment.LEFT);
    nfactory.setVerticalAlignment(ElementAlignment.MIDDLE);
    nfactory.setNullString("<null>");
    nfactory.setFieldname("sum");
    nfactory.setFormatString("#,##0");
    footer.addElement(nfactory.createElement());
    continentGroup.setFooter(footer);
    return continentGroup;
  }

  /**
   * Creates the report.
   *
   * @return the constructed report.
   */
  public JFreeReport createReport ()
  {
    final JFreeReport report = new JFreeReport();
    report.setName("Sample Report 1");
    report.setReportFooter(createReportFooter());
    report.setReportHeader(createReportHeader());
    report.setPageFooter(createPageFooter());
    report.setPageHeader(createPageHeader());
    report.setGroups(createGroups());
    report.setItemBand(createItemBand());
    report.setExpressions(createFunctions());
    report.getReportConfiguration().setConfigProperty
            ("org.jfree.report.modules.output.pageable.pdf.Encoding", "Identity-H");
    report.setDataFactory(new TableDataFactory
        ("default", data));
    return report;
  }
}
