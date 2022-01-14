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
 * ExcelOutputProcessorMetaData.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.xls.helper;

import org.jfree.report.layout.output.AbstractOutputProcessorMetaData;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.modules.output.table.base.AbstractTableOutputProcessor;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 09.05.2007, 14:39:13
 *
 * @author Thomas Morgner
 */
public class ExcelOutputProcessorMetaData extends AbstractOutputProcessorMetaData
{
  public static final int PAGINATION_NONE = 0;
  public static final int PAGINATION_MANUAL = 1;
  public static final int PAGINATION_FULL = 2;
  
  public ExcelOutputProcessorMetaData(final Configuration configuration, final int paginationMode)
  {
    super(configuration);
    final String localStrict = configuration.getConfigProperty("org.jfree.report.modules.output.table.xls.StrictLayout");
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

    final String emulatePadding = configuration.getConfigProperty("org.jfree.report.modules.output.table.xls.EmulateCellPadding");
    if ("true".equals(emulatePadding) == false)
    {
      addFeature(OutputProcessorFeature.DISABLE_PADDING);
    }
    else
    {
      addFeature(OutputProcessorFeature.EMULATE_PADDING);
    }

    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.base.UsePageBands")))
    {
      addFeature(OutputProcessorFeature.PAGE_SECTIONS);
    }
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.xls.UsePageBands")))
    {
      addFeature(OutputProcessorFeature.PAGE_SECTIONS);
    }
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.base.TreatEllipseAsRectangle")))
    {
      addFeature(AbstractTableOutputProcessor.TREAT_ELLIPSE_AS_RECTANGLE);
    }
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.xls.TreatEllipseAsRectangle")))
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

    setNumericFeatureValue(OutputProcessorFeature.DEVICE_RESOLUTION, 72);
  }

  /**
   * The export descriptor is a string that describes the output characteristics. For libLayout outputs, it should start
   * with the output class (one of 'pageable', 'flow' or 'stream'), followed by '/liblayout/' and finally followed by
   * the output type (ie. PDF, Print, etc).
   *
   * @return the export descriptor.
   */
  public String getExportDescriptor()
  {
    return "table/excel";
  }
}
