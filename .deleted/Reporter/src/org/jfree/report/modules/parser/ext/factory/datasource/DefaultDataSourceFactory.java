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
 * DefaultDataSourceFactory.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.parser.ext.factory.datasource;

import org.jfree.report.filter.AnchorFilter;
import org.jfree.report.filter.ComponentDrawableFilter;
import org.jfree.report.filter.DataRowDataSource;
import org.jfree.report.filter.DateFormatFilter;
import org.jfree.report.filter.DateFormatParser;
import org.jfree.report.filter.DecimalFormatFilter;
import org.jfree.report.filter.DecimalFormatParser;
import org.jfree.report.filter.DrawableLoadFilter;
import org.jfree.report.filter.EmptyDataSource;
import org.jfree.report.filter.FormatFilter;
import org.jfree.report.filter.FormatParser;
import org.jfree.report.filter.ImageLoadFilter;
import org.jfree.report.filter.ImageRefFilter;
import org.jfree.report.filter.MessageFormatFilter;
import org.jfree.report.filter.NumberFormatFilter;
import org.jfree.report.filter.NumberFormatParser;
import org.jfree.report.filter.ResourceFileFilter;
import org.jfree.report.filter.ResourceMessageFormatFilter;
import org.jfree.report.filter.ShapeFilter;
import org.jfree.report.filter.SimpleDateFormatFilter;
import org.jfree.report.filter.SimpleDateFormatParser;
import org.jfree.report.filter.StaticDataSource;
import org.jfree.report.filter.StringFilter;
import org.jfree.report.filter.URLFilter;
import org.jfree.report.modules.parser.ext.factory.base.BeanObjectDescription;
import org.jfree.report.modules.parser.ext.factory.templates.DefaultTemplateCollection;

/**
 * A default implementation of the {@link DataSourceFactory} interface.
 *
 * @author Thomas Morgner
 */
public class DefaultDataSourceFactory extends AbstractDataSourceFactory
{
  /**
   * Creates a new factory.
   */
  public DefaultDataSourceFactory ()
  {
    registerDataSources("AnchorFilter", new BeanObjectDescription(AnchorFilter.class));
    registerDataSources("ComponentDrawableFilter",
            new BeanObjectDescription(ComponentDrawableFilter.class));
    registerDataSources("DataRowDataSource", new BeanObjectDescription(DataRowDataSource.class));
    registerDataSources("DateFormatFilter", new BeanObjectDescription(DateFormatFilter.class));
    registerDataSources("DateFormatParser", new BeanObjectDescription(DateFormatParser.class));
    registerDataSources("DecimalFormatFilter",
            new BeanObjectDescription(DecimalFormatFilter.class));
    registerDataSources("DecimalFormatParser",
            new BeanObjectDescription(DecimalFormatParser.class));
    registerDataSources("DrawableLoadFilter",
            new BeanObjectDescription(DrawableLoadFilter.class));
    registerDataSources("EmptyDataSource", new BeanObjectDescription(EmptyDataSource.class));
    registerDataSources("FormatFilter", new BeanObjectDescription(FormatFilter.class));
    registerDataSources("FormatParser", new BeanObjectDescription(FormatParser.class));
    registerDataSources("ImageLoadFilter", new BeanObjectDescription(ImageLoadFilter.class));
    registerDataSources("ImageRefFilter", new BeanObjectDescription(ImageRefFilter.class));
    registerDataSources("MessageFormatFilter", new BeanObjectDescription(MessageFormatFilter.class));
    registerDataSources("NumberFormatFilter", new BeanObjectDescription(NumberFormatFilter.class));
    registerDataSources("NumberFormatParser", new BeanObjectDescription(NumberFormatParser.class));
    registerDataSources("ResourceFileFilter", new BeanObjectDescription(ResourceFileFilter.class));
    registerDataSources("ResourceMessageFormatFilter", new BeanObjectDescription(ResourceMessageFormatFilter.class));
    registerDataSources("ShapeFilter", new BeanObjectDescription(ShapeFilter.class));
    registerDataSources("SimpleDateFormatFilter",
            new BeanObjectDescription(SimpleDateFormatFilter.class));
    registerDataSources("SimpleDateFormatParser",
            new BeanObjectDescription(SimpleDateFormatParser.class));
    registerDataSources("StaticDataSource", new BeanObjectDescription(StaticDataSource.class));
    registerDataSources("StringFilter", new BeanObjectDescription(StringFilter.class));
    registerDataSources("URLFilter", new URLFilterObjectDescription(URLFilter.class));

    final DefaultTemplateCollection templateCollection = new DefaultTemplateCollection();

    final String[] templateNames = templateCollection.getNames();
    for (int i = 0; i < templateNames.length; i++)
    {
      final String name = templateNames[i];
      registerDataSources(name, templateCollection.getTemplate(name));
    }
  }

}
