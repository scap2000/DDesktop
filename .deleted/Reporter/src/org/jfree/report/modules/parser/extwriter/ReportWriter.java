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
 * ReportWriter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;
import java.io.Writer;

import java.net.URL;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.parser.ext.factory.base.ClassFactory;
import org.jfree.report.modules.parser.ext.factory.base.ClassFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceCollector;
import org.jfree.report.modules.parser.ext.factory.datasource.DataSourceFactory;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactory;
import org.jfree.report.modules.parser.ext.factory.elements.ElementFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactoryCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollection;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Configuration;
import org.jfree.xmlns.parser.AbstractXmlResourceFactory;
import org.jfree.xmlns.writer.DefaultTagDescription;
import org.jfree.xmlns.writer.TagDescription;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * A report writer.
 *
 * @author Thomas Morgner
 */
public class ReportWriter extends ReportWriterContext
{
  /**
   * A data-source collector.
   */
  private DataSourceCollector dataSourceCollector;

  /**
   * An element factory collector.
   */
  private ElementFactoryCollector elementFactoryCollector;

  /**
   * A class factory collector.
   */
  private ClassFactoryCollector classFactoryCollector;

  /**
   * A style-key factory collector.
   */
  private StyleKeyFactoryCollector styleKeyFactoryCollector;

  /**
   * A template collector.
   */
  private TemplateCollector templateCollector;

  /**
   * The encoding.
   */
  private String encoding;

  /**
   * The report writer configuration used during writing.
   */
  private Configuration configuration;

  /**
   * Builds a default configuration from a given report definition object.
   * <p/>
   * This will only create a valid definition, if the report has a valid content
   * base object set.
   *
   * @param report the report for which to create the writer configuration.
   * @return the generated configuration.
   */
  public static Configuration createDefaultConfiguration(final JFreeReport report)
  {
    final ModifiableConfiguration repConf =
        new HierarchicalConfiguration(report.getReportConfiguration());
    final ResourceKey contentBase = report.getContentBase();
    if (contentBase != null)
    {
      final ResourceManager resourceManager = report.getResourceManager();
      final URL value = resourceManager.toURL(contentBase);
      if (value != null)
      {
        repConf.setConfigProperty
            (AbstractXmlResourceFactory.CONTENTBASE_KEY, value.toExternalForm());
      }
    }

    return repConf;
  }

  public ReportWriter(final JFreeReport reportDefinition,
                      final String encoding)
  {
    this(reportDefinition, encoding,
        ReportWriter.createDefaultConfiguration(reportDefinition));
  }

  /**
   * Creates a new report writer for a report.
   *
   * @param report   the report.
   * @param encoding the encoding.
   * @param config   the write configuration.
   */
  public ReportWriter(final JFreeReport report,
                      final String encoding,
                      final Configuration config)
  {
    super(report);
    if (encoding == null)
    {
      throw new NullPointerException("Encoding is null.");
    }
    if (config == null)
    {
      throw new NullPointerException("Configuration is null.");
    }
//    if (config.getConfigProperty(AbstractXmlResourceFactory.CONTENTBASE_KEY) == null)
//    {
//      throw new IllegalStateException
//          ("This report writer configuration does not define a content base.");
//    }

    this.encoding = encoding;
    this.configuration = config;

    dataSourceCollector = new DataSourceCollector();
    elementFactoryCollector = new ElementFactoryCollector();
    classFactoryCollector = new ClassFactoryCollector();
    classFactoryCollector.addFactory(dataSourceCollector);
    styleKeyFactoryCollector = new StyleKeyFactoryCollector();
    templateCollector = new TemplateCollector();

    // configure all factories with the current report configuration ...
    dataSourceCollector.configure(configuration);
    classFactoryCollector.configure(configuration);
    templateCollector.configure(configuration);
  }

  /**
   * Returns the encoding.
   *
   * @return The encoding.
   */
  public String getEncoding()
  {
    return encoding;
  }

  /**
   * Adds a data-source factory.
   *
   * @param dsf the data-source factory.
   */
  public void addDataSourceFactory(final DataSourceFactory dsf)
  {
    dataSourceCollector.addFactory(dsf);
  }

  /**
   * Returns the data-source collector.
   *
   * @return The data-source collector.
   */
  public DataSourceCollector getDataSourceCollector()
  {
    return dataSourceCollector;
  }

  /**
   * Adds an element factory.
   *
   * @param ef the element factory.
   */
  public void addElementFactory(final ElementFactory ef)
  {
    elementFactoryCollector.addFactory(ef);
  }

  /**
   * Returns the element factory collector.
   *
   * @return The element factory collector.
   */
  public ElementFactoryCollector getElementFactoryCollector()
  {
    return elementFactoryCollector;
  }

  /**
   * Adds a class factory.
   *
   * @param cf the class factory.
   */
  public void addClassFactoryFactory(final ClassFactory cf)
  {
    classFactoryCollector.addFactory(cf);
  }

  /**
   * Returns the class factory collector.
   *
   * @return The class factory collector.
   */
  public ClassFactoryCollector getClassFactoryCollector()
  {
    return classFactoryCollector;
  }

  /**
   * Adds a style-key factory.
   *
   * @param skf the style-key factory.
   */
  public void addStyleKeyFactory(final StyleKeyFactory skf)
  {
    styleKeyFactoryCollector.addFactory(skf);
  }

  /**
   * Returns the style-key factory collector.
   *
   * @return The style-key factory collector.
   */
  public StyleKeyFactoryCollector getStyleKeyFactoryCollector()
  {
    return styleKeyFactoryCollector;
  }

  /**
   * Adds a template collection.
   *
   * @param collection the template collection.
   */
  public void addTemplateCollection(final TemplateCollection collection)
  {
    templateCollector.addTemplateCollection(collection);
  }

  /**
   * Returns the template collector.
   *
   * @return The template collector.
   */
  public TemplateCollector getTemplateCollector()
  {
    return templateCollector;
  }

  /**
   * Writes a report to a character stream writer.
   *
   * @param w the character stream writer.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write(final Writer w)
      throws IOException, ReportWriterException
  {
    final XmlWriter xmlWriter = new XmlWriter(w, createTagDescription());

    xmlWriter.writeXmlDeclaration(getEncoding());
    final ReportDefinitionWriter writer = new ReportDefinitionWriter(this, xmlWriter);
    writer.write();
  }

  private TagDescription createTagDescription()
  {
    final DefaultTagDescription defaultTagDescription = new DefaultTagDescription();
    defaultTagDescription.configure
        (JFreeReportBoot.getInstance().getGlobalConfig(),
            "org.jfree.report.modules.parser.extwriter.");
    return defaultTagDescription;
  }

  /**
   * Returns the configuration used to write the report.
   *
   * @return the writer configuration.
   */
  public Configuration getConfiguration()
  {
    return configuration;
  }
}
