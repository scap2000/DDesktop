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
 * LabelTemplate.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.filter.templates;

import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.filter.StringFilter;
import org.jfree.report.function.ExpressionRuntime;

/**
 * A label template can be used to describe static text content.
 *
 * @author Thomas Morgner
 */
public class LabelTemplate extends AbstractTemplate
{
  /**
   * A static data source.
   */
  private StaticDataSource staticDataSource;

  /**
   * A string filter.
   */
  private StringFilter stringFilter;

  /**
   * Creates a new label template.
   */
  public LabelTemplate ()
  {
    staticDataSource = new StaticDataSource();
    stringFilter = new StringFilter();
    stringFilter.setDataSource(staticDataSource);
  }

  /**
   * Sets the text for the label.
   *
   * @param content the text.
   */
  public void setContent (final String content)
  {
    staticDataSource.setValue(content);
  }

  /**
   * Returns the text for the label.
   *
   * @return The text.
   */
  public String getContent ()
  {
    return (String) (staticDataSource.getValue(null));
  }

  /**
   * Returns the string that represents <code>null</code>.
   *
   * @return The string that represents <code>null</code>.
   */
  public String getNullValue ()
  {
    return stringFilter.getNullValue();
  }

  /**
   * Sets the string that represents <code>null</code>.
   *
   * @param nullValue the string.
   */
  public void setNullValue (final String nullValue)
  {
    stringFilter.setNullValue(nullValue);
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
    return stringFilter.getValue(runtime);
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
    final LabelTemplate template = (LabelTemplate) super.clone();
    template.stringFilter = (StringFilter) stringFilter.clone();
    template.staticDataSource = (StaticDataSource) template.stringFilter.getDataSource();
    return template;
  }

}
