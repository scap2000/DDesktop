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
 * HtmlOutputProcessorMetaData.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html.helper;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.report.layout.output.AbstractOutputProcessorMetaData;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.modules.output.table.base.AbstractTableOutputProcessor;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 04.05.2007, 18:04:58
 *
 * @author Thomas Morgner
 */
public class HtmlOutputProcessorMetaData extends AbstractOutputProcessorMetaData
{
  public static final int PAGINATION_NONE = 0;
  public static final int PAGINATION_MANUAL = 1;
  public static final int PAGINATION_FULL = 2;

  private int paginationMode;

  public HtmlOutputProcessorMetaData(final Configuration configuration,
                                     final int paginationMode)
  {
    this(configuration, createFontStorage(), paginationMode);
  }

  private static FontStorage createFontStorage()
  {
    return new DefaultFontStorage(new AWTFontRegistry());
  }

  public HtmlOutputProcessorMetaData(final Configuration configuration,
                                     final FontStorage fontStorage,
                                     final int paginationMode)
  {
    super(configuration, fontStorage);
    addFeature(OutputProcessorFeature.SPACING_SUPPORTED);
    
    this.paginationMode = paginationMode;
    final String localStrict = configuration.getConfigProperty("org.jfree.report.modules.output.table.html.StrictLayout");
    if (localStrict != null)
    {
      if ("true".equals(localStrict))
      {
        addFeature(AbstractTableOutputProcessor.STRICT_LAYOUT);
      }
    }
    else
    {
      final String globalStrict = configuration.getConfigProperty("org.jfree.report.modules.output.table.base.StrictLayout");
      if ("true".equals(globalStrict))
      {
        addFeature(AbstractTableOutputProcessor.STRICT_LAYOUT);
      }
    }
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.base.UsePageBands")))
    {
      addFeature(OutputProcessorFeature.PAGE_SECTIONS);
    }
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.html.UsePageBands")))
    {
      addFeature(OutputProcessorFeature.PAGE_SECTIONS);
    }
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.base.TreatEllipseAsRectangle")))
    {
      addFeature(AbstractTableOutputProcessor.TREAT_ELLIPSE_AS_RECTANGLE);
    }
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.html.TreatEllipseAsRectangle")))
    {
      addFeature(AbstractTableOutputProcessor.TREAT_ELLIPSE_AS_RECTANGLE);
    }

    if (paginationMode == PAGINATION_FULL)
    {
      addFeature(OutputProcessorFeature.PAGEBREAKS);
    }
    else if (paginationMode == PAGINATION_MANUAL)
    {
      addFeature(OutputProcessorFeature.PAGEBREAKS);
      addFeature(OutputProcessorFeature.ITERATIVE_RENDERING);
      addFeature(OutputProcessorFeature.UNALIGNED_PAGEBANDS);
    }
    else
    {
      addFeature(OutputProcessorFeature.ITERATIVE_RENDERING);
      addFeature(OutputProcessorFeature.UNALIGNED_PAGEBANDS);
    }
  }

  public int getPaginationMode()
  {
    return paginationMode;
  }

  public String getExportDescriptor()
  {
    switch (paginationMode)
    {
      case PAGINATION_FULL:
        return "table/html+pagination";
      case PAGINATION_MANUAL:
        return "table/html+flow";
      default:
        return "table/html+stream";
    }
  }
}
