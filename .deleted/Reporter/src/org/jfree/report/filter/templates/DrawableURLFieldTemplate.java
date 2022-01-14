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
 * DrawableURLFieldTemplate.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter.templates;

import java.net.URL;

import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DrawableLoadFilter;
import org.jfree.report.filter.URLFilter;
import org.jfree.report.function.ExpressionRuntime;

/**
 * An image URL field template, which reads the image from an URL supplied from a destColumn
 * in the DataRow.
 *
 * @author Thomas Morgner
 */
public class DrawableURLFieldTemplate extends AbstractTemplate
{
  /**
   * An image load filter.
   */
  private DrawableLoadFilter imageLoadFilter;

  /**
   * A data row accessor.
   */
  private DataRowDataSource dataRowDataSource;

  /**
   * A URL filter.
   */
  private URLFilter urlFilter;

  /**
   * Creates a new template.
   */
  public DrawableURLFieldTemplate ()
  {
    dataRowDataSource = new DataRowDataSource();
    urlFilter = new URLFilter();
    urlFilter.setDataSource(dataRowDataSource);
    imageLoadFilter = new DrawableLoadFilter();
    imageLoadFilter.setDataSource(urlFilter);
  }

  /**
   * Returns the name of the field from the data-row that the template gets images from.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return dataRowDataSource.getDataSourceColumnName();
  }

  /**
   * Sets the field name.
   *
   * @param field the field name.
   */
  public void setField (final String field)
  {
    dataRowDataSource.setDataSourceColumnName(field);
  }

  /**
   * Returns the formula used to compute the value of the data source.
   *
   * @return the formula.
   */
  public String getFormula()
  {
    return dataRowDataSource.getFormula();
  }

  /**
   * Defines the formula used to compute the value of this data source.
   *
   * @param formula the formula for the data source.
   */
  public void setFormula(final String formula)
  {
    dataRowDataSource.setFormula(formula);
  }


  /**
   * Returns the base URL.
   *
   * @return The base URL.
   */
  public URL getBaseURL ()
  {
    return urlFilter.getBaseURL();
  }

  /**
   * Sets the base URL.
   *
   * @param baseURL the base URL.
   */
  public void setBaseURL (final URL baseURL)
  {
    urlFilter.setBaseURL(baseURL);
  }

  /**
   * Returns the current value for the data source.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return the value.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    return imageLoadFilter.getValue(runtime);
  }

  /**
   * Clones the template.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final DrawableURLFieldTemplate template = (DrawableURLFieldTemplate) super.clone();
    template.imageLoadFilter = (DrawableLoadFilter) imageLoadFilter.clone();
    template.urlFilter = (URLFilter) template.imageLoadFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.urlFilter.getDataSource();
    return template;
  }

  /**
   * Returns the datarow data source used in this template.
   *
   * @return the datarow data source.
   */
  protected DataRowDataSource getDataRowDataSource ()
  {
    return dataRowDataSource;
  }
}
