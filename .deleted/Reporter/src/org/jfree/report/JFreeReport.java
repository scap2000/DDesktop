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
 * JFreeReport.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.function.Expression;
import org.jfree.report.function.StructureFunction;
import org.jfree.report.function.sys.CellFormatFunction;
import org.jfree.report.function.sys.SheetNameFunction;
import org.jfree.report.function.sys.StyleExpressionsEvaluator;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.ReportProperties;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Configuration;
import org.jfree.util.ExtendedConfiguration;

/**
 * A JFreeReport instance is used as report template to define the visual layout of a report and to collect all data
 * sources for the reporting. Possible data sources are the {@link TableModel}, {@link Expression}s or {@link
 * ReportProperties}. The report is made up of 'bands', which are used repeatedly as necessary to generate small
 * sections of the report.
 * <p/>
 * <h2>Accessing the bands and the elements:</h2>
 * <p/>
 * The different bands can be accessed using the main report definition (this class):
 * <p/>
 * <ul> <li>the report header and footer can be reached by using <code>getReportHeader()</code> and
 * <code>getReportFooter()</code>
 * <p/>
 * <li>the page header and page footer can be reached by using <code>getPageHeader()</code> and
 * <code>getPageFooter()</code>
 * <p/>
 * <li>the item band is reachable with <code>getItemBand()</code>
 * <p/>
 * <li>the no-data band is reachable with <code>getNoDataBand()</code>
 * <p/>
 * <li>the watermark band is reachable with <code>getWaterMark()</code> </ul>
 * <p/>
 * Groups can be queried using <code>getGroup(int groupLevel)</code>. The group header and footer are accessible through
 * the group object, so use <code>getGroup(int groupLevel).getGroupHeader()<code> and <code>getGroup(int
 * groupLevel).getGroupFooter()<code>.
 * <p/>
 * All report elements share the same stylesheet collection. Report elements cannot be shared between two different
 * report instances. Adding a report element to one band will remove it from the other one.
 * <p/>
 * For dynamic computation of content you can add {@link Expression}s and {@link org.jfree.report.function.Function}s to
 * the report.
 * <p/>
 * Creating a new instance of JFreeReport seems to lock down the JDK on some Windows Systems, where no printer driver is
 * installed. To prevent that behaviour on these systems, you can set the {@link ReportConfiguration} key
 * "org.jfree.report.NoPrinterAvailable" to "false" and JFreeReport will use a hardcoded default page format instead.
 * <p/>
 * A JFreeReport object always acts as Master-Report. The JFreeReport object defines the global report-configuration,
 * the report's datasource (through the DataFactory property) and the ResourceBundleFactory (for localization).
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class JFreeReport extends AbstractReportDefinition
{
  /**
   * Key for the 'report name' property.
   */
  public static final String NAME_PROPERTY = "report.name";

  /**
   * Key for the 'report date' property.
   */
  public static final String REPORT_DATE_PROPERTY = "report.date";

  /**
   * A reference to the currently used layout support implementation. This can be used to compute text sizes from within
   * functions.
   *
   * @deprecated The layout-support is no longer maintained. Layouting is now fully encapsulated and report functions
   *             should not try to tamper with that.
   */
  public static final String REPORT_LAYOUT_SUPPORT = "report.layout-support";

  /**
   * The page format for the report (determines the page size, and therefore the report width).
   */
  private PageDefinition pageDefinition;

  /**
     * The data factory is used to labelQuery data for the reporting.
     */
  private DataFactory dataFactory;
  /**
   * The collection of structural functions.
   */
  private ArrayList structureFunctions;

  /**
   * The report configuration.
   */
  private ModifiableConfiguration reportConfiguration;
  /**
   * The resource bundle factory is used when generating localized reports.
   */
  private ResourceBundleFactory resourceBundleFactory;
  /**
   * The resource manager is used to load the report resources.
   */
  private transient ResourceManager resourceManager;
  /**
   * The base resource represents the resource that was used to create this report.
   */
  private ResourceKey baseResource;
  /**
   * The content base is used to resolve relative URLs.
   */
  private ResourceKey contentBase;

  /**
   * The default constructor. Creates an empty but fully initialized report.
   */
  public JFreeReport()
  {
    this.reportConfiguration = new HierarchicalConfiguration
        (JFreeReportBoot.getInstance().getGlobalConfig());
    this.resourceBundleFactory = new DefaultResourceBundleFactory();

    setPageDefinition(null);

    final TableDataFactory dataFactory = new TableDataFactory();
    dataFactory.addTable("default", new DefaultTableModel());
    this.dataFactory = dataFactory;

    this.structureFunctions = new ArrayList();
    this.structureFunctions.add(new CellFormatFunction());
    this.structureFunctions.add(new SheetNameFunction());
    this.structureFunctions.add(new StyleExpressionsEvaluator());
  }

  /**
   * Returns the name of the report.
   * <p/>
   * You can reference the report name in your XML report template file using the key '<code>report.name</code>'.
   *
   * @return the name.
   */
  public String getName()
  {
    final Object name = getProperty(JFreeReport.NAME_PROPERTY);
    if (name == null)
    {
      return null;
    }
    return String.valueOf(name);
  }

  /**
   * Sets the name of the report.
   * <p/>
   * The report name is stored as a property (key {@link JFreeReport#NAME_PROPERTY}) inside the report properties.  If
   * you supply <code>null</code> as the name, the property is removed.
   *
   * @param name the name of the report.
   */
  public void setName(final String name)
  {
    setProperty(JFreeReport.NAME_PROPERTY, name);
  }


  /**
   * Returns the logical page definition for this report.
   *
   * @return the page definition.
   */
  public PageDefinition getPageDefinition()
  {
    return pageDefinition;
  }

  /**
   * Defines the logical page definition for this report. If no format is defined the system's default page format is
   * used.
   * <p/>
   * If there is no printer available and the JDK blocks during the printer discovery, you can set the {@link
   * ReportConfiguration} key "org.jfree.report.NoPrinterAvailable" to "false" and JFreeReport will use a hardcoded
   * default page format instead.
   *
   * @param format the default format (<code>null</code> permitted).
   */
  public void setPageDefinition(PageDefinition format)
  {
    if (format == null)
    {
      final ExtendedConfiguration config = JFreeReportBoot.getInstance().getExtendedConfig();
      if (config.getBoolProperty(JFreeReportCoreModule.NO_PRINTER_AVAILABLE_KEY))
      {
        format = new SimplePageDefinition(new PageFormat());
      }
      else
      {
        format = new SimplePageDefinition(PrinterJob.getPrinterJob().defaultPage());
      }
    }
    pageDefinition = format;
  }

  /**
     * Sets the data for the report.
     * <p/>
     * Reports are generated from a {@link TableModel}(as used by Swing's {@link javax.swing.JTable}). If you don't want
     * to give data to the report, use an empty {@link TableModel}instead of <code>null</code>.
     * <p/>
     * Using this method will override the defined data-factory and will set the tablemodel as default destTable in a
     * TableDataFactory.
     *
     * @param data the data for the report (<code>null</code> not permitted).
     * @deprecated use data factories instead.
     */
  public void setData(final TableModel data)
  {
    if (data == null)
    {
      throw new NullPointerException("JFreeReport.setData(...) : null not permitted.");
    }
    this.dataFactory = new TableDataFactory("default", data);
  }

  /**
     * Returns the current data for this report.
     *
     * @return the data in form of a destTable model.
     * @deprecated use data factories instead.
     */
  public TableModel getData()
  {
    if (dataFactory instanceof TableDataFactory)
    {
      // deprecation mode.
      final TableDataFactory tableDataFactory = (TableDataFactory) dataFactory;
      return tableDataFactory.queryData("default",null);
    }

    return null;
  }

  /**
   * Returns the data factory that has been assigned to this report. The data factory will never be null.
   *
   * @return the data factory.
   */
  public DataFactory getDataFactory()
  {
    return dataFactory;
  }

  /**
   * Sets the data factory for the report.
   *
   * @param dataFactory the data factory for the report, never null.
   */
  public void setDataFactory(final DataFactory dataFactory)
  {
    if (dataFactory == null)
    {
      throw new NullPointerException();
    }

    this.dataFactory = dataFactory;
  }

  /**
   * Clones the report.
   *
   * @return the clone.
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    final JFreeReport report = (JFreeReport) super.clone();
    report.pageDefinition = (PageDefinition) pageDefinition.clone();
    report.reportConfiguration = (ModifiableConfiguration) reportConfiguration.clone();

    report.structureFunctions = (ArrayList) structureFunctions.clone();
    report.structureFunctions.clear();

    for (int i = 0; i < structureFunctions.size(); i++)
    {
      final StructureFunction function =
          (StructureFunction) structureFunctions.get(i);
      report.structureFunctions.add(function.clone());
    }
    return report;
  }

  /**
   * Adds a structural function to the report. Structural functions perform content preparation and maintainance
   * operations before elements are layouted or printed.
   * <p/>
   * Structural function can live on their own processing level and are evaluated after the user expressions but before
   * the layout expressions have been evaluated.
   *
   * @param function the structure function.
   */
  public void addStructureFunction(final StructureFunction function)
  {
    if (function == null)
    {
      throw new NullPointerException();
    }
    structureFunctions.add(function);
  }

  /**
   * Returns the number of structural functions added to the report.
   *
   * @return the function count.
   */
  public int getStructureFunctionCount()
  {
    return structureFunctions.size();
  }

  /**
   * Returns the structure function at the given position.
   *
   * @param index the position of the function in the list.
   * @return the function, never null.
   * @throws IndexOutOfBoundsException if the index is invalid.
   */
  public StructureFunction getStructureFunction(final int index)
  {
    return (StructureFunction) structureFunctions.get(index);
  }

  /**
   * Removes the given function from the collection of structure functions. This removes only the first occurence of the
   * function, in case a function has been added twice.
   *
   * @param f the function to be removed.
   */
  public void removeStructureFunction(final StructureFunction f)
  {
    structureFunctions.remove(f);
  }

  /**
   * Returns a copy of all structure functions contained in the report. Modifying the function instances will not alter
   * the functions contained in the report.
   *
   * @return the functions.
   */
  public StructureFunction[] getStructureFunctions()
  {
    final StructureFunction[] structureFunctions =
        (StructureFunction[]) this.structureFunctions.toArray(new StructureFunction[this.structureFunctions.size()]);

    for (int i = 0; i < structureFunctions.length; i++)
    {
      final StructureFunction function = structureFunctions[i];
      structureFunctions[i] = (StructureFunction) function.getInstance();
    }
    return structureFunctions;
  }


  /**
   * Returns the report configuration.
   * <p/>
   * The report configuration is automatically set up when the report is first created, and uses the global JFreeReport
   * configuration as its parent.
   *
   * @return the report configuration.
   */
  public ModifiableConfiguration getReportConfiguration()
  {
    return reportConfiguration;
  }

  /**
   * Returns the report's configuration.
   *
   * @return the configuration.
   */
  public Configuration getConfiguration()
  {
    return reportConfiguration;
  }


  /**
   * Returns the resource manager that was responsible for loading the report. This method will return a default manager
   * if the report had been constructed otherwise.
   * <p/>
   * The resource manager of the report should be used for all resource loading activities during the report
   * processing.
   *
   * @return the resource manager, never null.
   */
  public ResourceManager getResourceManager()
  {
    if (resourceManager == null)
    {
      resourceManager = new ResourceManager();
      resourceManager.registerDefaults();
    }
    return resourceManager;
  }

  /**
   * Assigns a new resource manager or clears the current one. If no resource manager is set anymore, the next call to
   * 'getResourceManager' will recreate one.
   *
   * @param resourceManager the new resource manager or null.
   */
  public void setResourceManager(final ResourceManager resourceManager)
  {
    this.resourceManager = resourceManager;
  }

  /**
   * Returns the base resource for all resource loading activities. The base resource represents the file that was used
   * to load the report and can be null, if the report has not been loaded through a ResourceLoader.
   *
   * @return the base resource.
   */
  public ResourceKey getBaseResource()
  {
    return baseResource;
  }

  /**
   * Redefines the base resource key. The base resource represents the file that was used to load the report and can be
   * null, if the report has not been loaded through a ResourceLoader.
   *
   * @param baseResource the new base resource key or null.
   */
  public void setBaseResource(final ResourceKey baseResource)
  {
    this.baseResource = baseResource;
  }


  /**
   * Defines the content base for the report. The content base will be used to resolve relative URLs during the report
   * generation and resource loading. If there is no content base defined, it will be impossible to resolve relative
   * paths.
   *
   * @param key the content base or null.
   */
  public void setContentBase(final ResourceKey key)
  {
    this.contentBase = key;
  }

  /**
   * Returns the content base of this report. The content base is used to resolve relative URLs during the report
   * generation and resource loading. If there is no content base defined, it will be impossible to resolve relative
   * paths.
   *
   * @return the content base or null, if no content base is defined.
   */
  public ResourceKey getContentBase()
  {
    return contentBase;
  }


  /**
   * Returns the resource bundle factory for this report definition. The {@link ResourceBundleFactory} is used in
   * internationalized reports to create the resourcebundles holding the localized resources.
   *
   * @return the assigned resource bundle factory.
   */
  public ResourceBundleFactory getResourceBundleFactory()
  {
    return resourceBundleFactory;
  }

  /**
   * Redefines the resource bundle factory for the report.
   *
   * @param resourceBundleFactory the new resource bundle factory, never null.
   * @throws NullPointerException if the given ResourceBundleFactory is null.
   */
  public void setResourceBundleFactory(final ResourceBundleFactory resourceBundleFactory)
  {
    if (resourceBundleFactory == null)
    {
      throw new NullPointerException("ResourceBundleFactory must not be null");
    }
    this.resourceBundleFactory = resourceBundleFactory;
  }

  /**
   * Queries the current resource bundle factory for the resource bundle specified by the given key.
   *
   * @param identifier the base name of the resource bundle.
   * @return the resource bundle
   * @throws java.util.MissingResourceException if no resource bundle for the specified base name can be found
   * @deprecated Use the resource-bundle factory directly.
   */
  public ResourceBundle getResourceBundle(final String identifier)
  {
    return resourceBundleFactory.getResourceBundle(identifier);
  }
}
