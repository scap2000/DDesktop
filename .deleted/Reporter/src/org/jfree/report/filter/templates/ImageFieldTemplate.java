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
 * ImageFieldTemplate.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter.templates;

import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.ImageLoadFilter;
import org.jfree.report.filter.ImageRefFilter;
import org.jfree.report.function.ExpressionRuntime;

/**
 * An image field template. The image content will be read from the datarow.
 *
 * @author Thomas Morgner
 */
public class ImageFieldTemplate extends AbstractTemplate
{
  /**
   * The data row reader.
   */
  private DataRowDataSource dataRowDataSource;

  /**
   * An image reference filter.
   */
  private ImageRefFilter imageRefFilter;

  /**
   * The filter that is used to load the image from a URL, file or Blob.
   */
  private ImageLoadFilter imageLoadFilter;

  /**
   * Creates a new image field template.
   */
  public ImageFieldTemplate ()
  {
    dataRowDataSource = new DataRowDataSource();
    imageLoadFilter = new ImageLoadFilter();
    imageLoadFilter.setDataSource(dataRowDataSource);
    imageRefFilter = new ImageRefFilter();
    imageRefFilter.setDataSource(imageLoadFilter);
  }

  /**
   * Returns the field name.
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
   * Returns the current value for the data source.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return the value.
   */
  public Object getValue (final ExpressionRuntime runtime)
  {
    return imageRefFilter.getValue(runtime);
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
    final ImageFieldTemplate template = (ImageFieldTemplate) super.clone();
    template.imageRefFilter = (ImageRefFilter) imageRefFilter.clone();
    template.imageLoadFilter = (ImageLoadFilter) imageRefFilter.getDataSource();
    template.dataRowDataSource = (DataRowDataSource) template.imageLoadFilter.getDataSource();
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
