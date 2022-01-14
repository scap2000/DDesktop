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
 * RTFOutputProcessorMetaData.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.rtf.helper;

import org.jfree.fonts.FontMappingUtility;
import org.jfree.fonts.itext.ITextFontStorage;
import org.jfree.report.layout.output.AbstractOutputProcessorMetaData;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.modules.output.support.itext.BaseFontModule;
import org.jfree.report.modules.output.table.base.AbstractTableOutputProcessor;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 09.05.2007, 14:39:13
 *
 * @author Thomas Morgner
 */
public class RTFOutputProcessorMetaData extends AbstractOutputProcessorMetaData
{
  public static final int PAGINATION_NONE = 0;
  public static final int PAGINATION_MANUAL = 1;
  public static final int PAGINATION_FULL = 2;
  
  public static final OutputProcessorFeature.BooleanOutputProcessorFeature IMAGES_ENABLED =
      new OutputProcessorFeature.BooleanOutputProcessorFeature("RTF.EnableImages");

  public RTFOutputProcessorMetaData(final Configuration configuration, final int paginationMode)
  {
    super(configuration, new ITextFontStorage(BaseFontModule.getFontRegistry()));
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.rtf.EnableImages")))
    {
      addFeature(IMAGES_ENABLED);
    }

    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.rtf.StrictLayout")))
    {
      addFeature(AbstractTableOutputProcessor.STRICT_LAYOUT);
    }
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.base.StrictLayout")))
    {
      addFeature(AbstractTableOutputProcessor.STRICT_LAYOUT);
    }

    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.base.UsePageBands")))
    {
      addFeature(OutputProcessorFeature.PAGE_SECTIONS);
    }
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.rtf.UsePageBands")))
    {
      addFeature(OutputProcessorFeature.PAGE_SECTIONS);
    }
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.base.TreatEllipseAsRectangle")))
    {
      addFeature(AbstractTableOutputProcessor.TREAT_ELLIPSE_AS_RECTANGLE);
    }
    if ("true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.rtf.TreatEllipseAsRectangle")))
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
    return "table/rtf";
  }

  public String getNormalizedFontFamilyName(final String name)
  {
    final String mappedName = super.getNormalizedFontFamilyName(name);
    if (FontMappingUtility.isSerif(mappedName))
    {
      return "Times";
    }
    if (FontMappingUtility.isSansSerif(mappedName))
    {
      return "Helvetica";
    }
    if (FontMappingUtility.isCourier(mappedName))
    {
      return "Courier";
    }
    if (FontMappingUtility.isSymbol(mappedName))
    {
      return "Symbol";
    }
    return mappedName;
  }

  public ITextFontStorage getITextFontStorage()
  {
    return (ITextFontStorage) getFontStorage();
  }
}
