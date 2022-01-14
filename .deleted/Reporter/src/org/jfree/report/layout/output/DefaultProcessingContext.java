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
 * PageableProcessingContext.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import java.io.File;

import org.jfree.formula.DefaultFormulaContext;
import org.jfree.formula.FormulaContext;
import org.jfree.report.DefaultResourceBundleFactory;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.function.ProcessingContext;
import org.jfree.report.layout.DefaultLayoutSupport;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 08.04.2007, 15:46:44
 *
 * @author Thomas Morgner
 */
public class DefaultProcessingContext implements ProcessingContext
{
  private FormulaContext formulaContext;
  private boolean prepareRun;
  private LayoutSupport layoutSupport;
  private int processingLevel;
  private String exportDescriptor;
  private ResourceBundleFactory resourceBundleFactory;
  private Configuration configuration;
  private int progressLevelCount;
  private int progressLevel;
  private ResourceManager resourceManager;
  private ResourceKey contentBase;

  public DefaultProcessingContext() throws ResourceKeyCreationException
  {
    exportDescriptor = "undefined";
    layoutSupport = new DefaultLayoutSupport(true, true);
    resourceBundleFactory =  new DefaultResourceBundleFactory();
    configuration = JFreeReportBoot.getInstance().getGlobalConfig();
    resourceManager = new ResourceManager();
    resourceManager.registerDefaults();
    contentBase = resourceManager.createKey(new File ("."));
  }


  public DefaultProcessingContext(final String exportDescriptor,
                                  final LayoutSupport layoutSupport,
                                  final ResourceBundleFactory resourceBundleFactory,
                                  final Configuration configuration,
                                  final ResourceManager resourceManager,
                                  final ResourceKey contentBase)
  {
    this.contentBase = contentBase;
    this.resourceManager = resourceManager;
    this.formulaContext = new DefaultFormulaContext();
    this.exportDescriptor = exportDescriptor;
    this.resourceBundleFactory = resourceBundleFactory;
    this.configuration = configuration;
    this.layoutSupport = layoutSupport;
  }

  public ResourceManager getResourceManager()
  {
    return resourceManager;
  }

  public ResourceKey getContentBase()
  {
    return contentBase;
  }

  public int getProgressLevel()
  {
    return progressLevel;
  }

  public void setProgressLevel(final int progressLevel)
  {
    this.progressLevel = progressLevel;
  }

  public int getProgressLevelCount()
  {
    return progressLevelCount;
  }

  public void setProgressLevelCount(final int progressLevelCount)
  {
    this.progressLevelCount = progressLevelCount;
  }

  public void setProcessingLevel(final int processingLevel)
  {
    this.processingLevel = processingLevel;
  }

  public int getProcessingLevel()
  {
    return processingLevel;
  }

  public FormulaContext getFormulaContext()
  {
    return formulaContext;
  }

  public void setPrepareRun(final boolean prepareRun)
  {
    this.prepareRun = prepareRun;
  }

  public boolean isPrepareRun()
  {
    return prepareRun;
  }

  public String getExportDescriptor()
  {
    return exportDescriptor;
  }

  public LayoutSupport getLayoutSupport()
  {
    return layoutSupport;
  }

  public ResourceBundleFactory getResourceBundleFactory()
  {
    return resourceBundleFactory;
  }

  public Configuration getConfiguration()
  {
    return configuration;
  }
}
