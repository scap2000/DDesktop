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
 * AbstractReportDefinition.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.io.Serializable;

import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionCollection;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.report.util.ReportProperties;

/**
 * The AbstractReportDefinition serves as base-implementation for both the SubReport and the global JFreeReport
 * instance. There's no point to subclass this class any further.
 * <p/>
 * ReportDefinitions define the labelQuery string to "default" by default, change this to reflect the accepted queries
 * in your data-source.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractReportDefinition
    implements Serializable, ReportDefinition
{
  /**
   * Storage for arbitrary properties that a user can assign to the report.
   */
  private ReportProperties properties;
  /**
   * Storage for the expressions in the report.
   */
  private ExpressionCollection expressions;
  /**
   * An ordered list of report groups (each group defines its own header and footer).
   */
  private GroupList groups;
  /**
   * The report header band (printed once at the start of the report).
   */
  private ReportHeader reportHeader;
  /**
   * The report footer band (printed once at the end of the report).
   */
  private ReportFooter reportFooter;
  /**
   * The page header band (printed at the start of every page).
   */
  private PageHeader pageHeader;
  /**
   * The page footer band (printed at the end of every page).
   */
  private PageFooter pageFooter;
  /**
   * The item band - used once for each row of data.
   */
  private ItemBand itemBand;
  /**
   * The watermark band.
   */
  private Watermark watermark;
  /**
   * The no-data band is printed, if the tablemodel is empty.
   */
  private NoDataBand noDataBand;
  /**
     * The name of the labelQuery that will be used to retrieve the data from the data-factory.
     */
  private String query;
  /**
   * The stylesheet collection used for this report.
   */
  private StyleSheetCollection styleSheetCollection;


  /**
   * Creates a new instance. This initializes all properties to their defaults - especially for subreports you have to
   * set sensible values before you can use them later.
   */
  protected AbstractReportDefinition()
  {
    this.query = "default";

    this.properties = new ReportProperties();
    this.styleSheetCollection = new StyleSheetCollection();

    this.groups = new GroupList();
    this.reportHeader = new ReportHeader();
    this.reportFooter = new ReportFooter();
    this.pageHeader = new PageHeader();
    this.pageFooter = new PageFooter();
    this.itemBand = new ItemBand();
    this.watermark = new Watermark();
    this.noDataBand = new NoDataBand();

    this.expressions = new ExpressionCollection();
    this.groups.setReportDefinition(this);
    this.reportHeader.setReportDefinition(this);
    this.reportFooter.setReportDefinition(this);
    this.pageHeader.setReportDefinition(this);
    this.pageFooter.setReportDefinition(this);
    this.itemBand.setReportDefinition(this);
    this.watermark.setReportDefinition(this);
  }

  /**
   * Adds a property to the report.
   * <p/>
   * If a property with the given name already exists, the property will be updated with the new value. If the supplied
   * value is <code>null</code>, the property will be removed.
   * <p/>
   * Developers are free to add any properties they want to a report, and then display those properties in the report.
   * For example, you might add a 'user.name' property, so that you can display the username in the header of a report.
   *
   * @param key   the key.
   * @param value the value.
   */
  public void setProperty(final String key, final Object value)
  {
    this.properties.put(key, value);
  }

  /**
   * Returns the report properties collection for this report.
   * <p/>
   * These properties are inherited to all ReportStates generated for this report.
   *
   * @return the report properties.
   */
  public ReportProperties getProperties()
  {
    return properties;
  }

  /**
   * Returns the value of the property with the specified key.
   *
   * @param key the key.
   * @return the property value.
   */
  public Object getProperty(final String key)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    return this.properties.get(key);
  }

  /**
   * Sets the report header.
   *
   * @param header the report header (<code>null</code> not permitted).
   */
  public void setReportHeader(final ReportHeader header)
  {
    if (header == null)
    {
      throw new NullPointerException("AbstractReportDefinition.setReportHeader(...) : null not permitted.");
    }
    this.reportHeader.setReportDefinition(null);
    this.reportHeader = header;
    this.reportHeader.setReportDefinition(this);
  }

  /**
   * Returns the report header.
   *
   * @return the report header (never <code>null</code>).
   */
  public ReportHeader getReportHeader()
  {
    return reportHeader;
  }

  /**
   * Sets the report footer.
   *
   * @param footer the report footer (<code>null</code> not permitted).
   */
  public void setReportFooter(final ReportFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException("AbstractReportDefinition.setReportFooter(...) : null not permitted.");
    }
    this.reportFooter.setReportDefinition(null);
    this.reportFooter = footer;
    this.reportFooter.setReportDefinition(this);
  }

  /**
   * Returns the page footer.
   *
   * @return the report footer (never <code>null</code>).
   */
  public ReportFooter getReportFooter()
  {
    return reportFooter;
  }

  /**
   * Sets the page header.
   *
   * @param header the page header (<code>null</code> not permitted).
   */
  public void setPageHeader(final PageHeader header)
  {
    if (header == null)
    {
      throw new NullPointerException("AbstractReportDefinition.setPageHeader(...) : null not permitted.");
    }
    this.pageHeader.setReportDefinition(null);
    this.pageHeader = header;
    this.pageHeader.setReportDefinition(this);
  }

  /**
   * Returns the page header.
   *
   * @return the page header (never <code>null</code>).
   */
  public PageHeader getPageHeader()
  {
    return pageHeader;
  }

  /**
   * Sets the page footer.
   *
   * @param footer the page footer (<code>null</code> not permitted).
   */
  public void setPageFooter(final PageFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException("AbstractReportDefinition.setPageFooter(...) : null not permitted.");
    }
    this.pageFooter.setReportDefinition(null);
    this.pageFooter = footer;
    this.pageFooter.setReportDefinition(this);
  }

  /**
   * Returns the page footer.
   *
   * @return the page footer (never <code>null</code>).
   */
  public PageFooter getPageFooter()
  {
    return pageFooter;
  }

  /**
   * Sets the watermark band for the report.
   *
   * @param band the new watermark band (<code>null</code> not permitted).
   */
  public void setWatermark(final Watermark band)
  {
    if (band == null)
    {
      throw new NullPointerException("AbstractReportDefinition.setWatermark(...) : null not permitted.");
    }
    this.watermark.setReportDefinition(null);
    this.watermark = band;
    this.watermark.setReportDefinition(this);
  }

  /**
   * Returns the report's watermark band.
   *
   * @return the watermark band (never <code>null</code>).
   */
  public NoDataBand getNoDataBand()
  {
    return this.noDataBand;
  }

  /**
   * Sets the watermark band for the report.
   *
   * @param band the new watermark band (<code>null</code> not permitted).
   */
  public void setNoDataBand(final NoDataBand band)
  {
    if (band == null)
    {
      throw new NullPointerException("AbstractReportDefinition.setNoDataBand(...) : null not permitted.");
    }
    this.noDataBand.setReportDefinition(null);
    this.noDataBand = band;
    this.noDataBand.setReportDefinition(this);
  }

  /**
   * Returns the report's watermark band.
   *
   * @return the watermark band (never <code>null</code>).
   */
  public Watermark getWatermark()
  {
    return this.watermark;
  }

  /**
   * Sets the item band for the report.
   *
   * @param band the new item band (<code>null</code> not permitted).
   */
  public void setItemBand(final ItemBand band)
  {
    if (band == null)
    {
      throw new NullPointerException("AbstractReportDefinition.setItemBand(...) : null not permitted.");
    }
    this.itemBand.setReportDefinition(null);
    this.itemBand = band;
    this.itemBand.setReportDefinition(this);
  }

  /**
   * Returns the report's item band.
   *
   * @return the item band (never <code>null</code>).
   */
  public ItemBand getItemBand()
  {
    return this.itemBand;
  }

  /**
   * Adds a group to the report.
   *
   * @param group the group.
   */
  public void addGroup(final Group group)
  {
    groups.add(group);
  }

  /**
   * Sets the groups for this report. If no list (null) or an empty list is given, an default group is created. This
   * default group contains no elements and starts at the first record of the data and ends on the last record.
   *
   * @param groupList the list of groups.
   */
  public void setGroups(final GroupList groupList)
  {
    if (groupList == null)
    {
      throw new NullPointerException("GroupList must not be null");
    }
    this.groups.setReportDefinition(null);
    this.groups = groupList;
    this.groups.setReportDefinition(this);
  }

  /**
   * Returns a clone of the list of groups for the report.
   *
   * @return the group list.
   */
  public GroupList getGroups()
  {
    return this.groups;
  }

  /**
   * Returns the number of groups in this report. <P> Every report has at least one group defined.
   *
   * @return the group count.
   */
  public int getGroupCount()
  {
    return groups.size();
  }

  /**
   * Returns the group at the specified index or null, if there is no such group.
   *
   * @param count the group index.
   * @return the requested group.
   * @throws IllegalArgumentException  if the count is negative.
   * @throws IndexOutOfBoundsException if the count is greater than the number of defined groups.
   */
  public Group getGroup(final int count)
  {
    if (count < 0)
    {
      throw new IllegalArgumentException("GroupCount must not be negative");
    }

    if (count >= groups.size())
    {
      throw new IndexOutOfBoundsException("No such group defined. " + count + " vs. " + groups.size());
    }

    return groups.get(count);
  }

  /**
   * Searches a group by its defined name. This method returns null, if the group was not found.
   *
   * @param name the name of the group.
   * @return the group or null if not found.
   * @see org.jfree.report.GroupList#getGroupByName
   */
  public Group getGroupByName(final String name)
  {
    return groups.getGroupByName(name);
  }

  /**
   * Adds a function to the report's collection of expressions.
   *
   * @param function the function.
   */
  public void addExpression(final Expression function)
  {
    expressions.add(function);
  }

  /**
     * Returns a new labelQuery or labelQuery-name that is used when retrieving the data from the data-factory.
     *
     * @return the labelQuery-string.
     */
  public String getQuery()
  {
    return query;
  }

  /**
     * Defines a new labelQuery or labelQuery-name that is used when retrieving the data from the data-factory.
     *
     * @param query the labelQuery-string.
     * @see DataFactory#queryData(String,DataRow)
     */
  public void setQuery(final String query)
  {
    if (query == null)
    {
      throw new NullPointerException("Query cannot be null.");
    }
    this.query = query;
  }

  /**
   * Returns the stylesheet collection of this report. The stylesheet collection is fixed for the report and all
   * elements of the report. When a band or group is added to the report it will get registered with this stylesheet
   * collection and cannot be used in an different report.
   *
   * @return the stylesheet collection of the report, never null.
   */
  public StyleSheetCollection getStyleSheetCollection()
  {
    return styleSheetCollection;
  }

  /**
   * Returns the current datarow assigned to this report definition. JFreeReport objects do not hold a working DataRow,
   * as the final contents of the data cannot be known, until the reporting has started.
   *
   * @return the default implementation for non-processed reports.
   */
  public DataRow getDataRow()
  {
    return new ParameterDataRow(properties);
  }

  /**
   * Returns the expressions for the report.
   *
   * @return the expressions.
   */
  public ExpressionCollection getExpressions()
  {
    return expressions;
  }

  /**
   * Sets the expressions for the report.
   *
   * @param expressions the expressions (<code>null</code> not permitted).
   */
  public void setExpressions(final ExpressionCollection expressions)
  {
    if (expressions == null)
    {
      throw new NullPointerException("AbstractReportDefinition.setExpressions(...) : null not permitted.");
    }
    this.expressions = expressions;
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
    final AbstractReportDefinition report = (AbstractReportDefinition) super.clone();
    report.groups = (GroupList) groups.clone();
    report.watermark = (Watermark) watermark.clone();
    report.itemBand = (ItemBand) itemBand.clone();
    report.pageFooter = (PageFooter) pageFooter.clone();
    report.pageHeader = (PageHeader) pageHeader.clone();
    report.reportFooter = (ReportFooter) reportFooter.clone();
    report.reportHeader = (ReportHeader) reportHeader.clone();
    report.noDataBand = (NoDataBand) noDataBand.clone();
    report.properties = (ReportProperties) properties.clone();
    report.expressions = (ExpressionCollection) expressions.clone();
    report.styleSheetCollection = (StyleSheetCollection) styleSheetCollection.clone();
    report.noDataBand.setReportDefinition(report);
    report.groups.setReportDefinition(report);
    report.reportHeader.setReportDefinition(report);
    report.reportFooter.setReportDefinition(report);
    report.pageHeader.setReportDefinition(report);
    report.pageFooter.setReportDefinition(report);
    report.itemBand.setReportDefinition(report);
    report.watermark.setReportDefinition(report);
    return report;
  }
}
