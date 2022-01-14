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
 * PdfOutputProcessorMetaData.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.pdf.internal;

import org.jfree.fonts.FontMappingUtility;
import org.jfree.fonts.itext.ITextFontStorage;
import org.jfree.report.layout.output.AbstractOutputProcessorMetaData;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.util.Configuration;
import org.jfree.util.ExtendedConfiguration;
import org.jfree.util.ExtendedConfigurationWrapper;

/**
 * Creation-Date: 12.05.2007, 20:16:18
 *
 * @author Thomas Morgner
 */
public class PdfOutputProcessorMetaData extends AbstractOutputProcessorMetaData
{
  public PdfOutputProcessorMetaData(final Configuration configuration, final ITextFontStorage fontStorage)
  {
    super(configuration, fontStorage);
    setFamilyMapping(null, "Helvetica");
    addFeature(OutputProcessorFeature.FAST_FONTRENDERING);
    addFeature(OutputProcessorFeature.BACKGROUND_IMAGE);
    addFeature(OutputProcessorFeature.PAGE_SECTIONS);
    addFeature(OutputProcessorFeature.SPACING_SUPPORTED);
    addFeature(OutputProcessorFeature.PAGEBREAKS);
    
    if ("true".equals(configuration.getConfigProperty
        ("org.jfree.report.modules.output.pageable.pdf.WatermarkPrinted")))
    {
      addFeature(OutputProcessorFeature.WATERMARK_SECTION);
    }
    if ("true".equals(configuration.getConfigProperty
        ("org.jfree.report.modules.output.pageable.pdf.EmbedFonts")))
    {
      addFeature(OutputProcessorFeature.EMBED_ALL_FONTS);
    }

    final ExtendedConfiguration extendedConfig = new ExtendedConfigurationWrapper(configuration);
    final double deviceResolution = extendedConfig.getIntProperty
        ("org.jfree.report.modules.output.pageable.pdf.DeviceResolution", 0);
    if (deviceResolution > 0)
    {
      setNumericFeatureValue(OutputProcessorFeature.DEVICE_RESOLUTION, deviceResolution);
    }
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

  public String getExportDescriptor()
  {
    return "pageable/pdf";
  }

  public ITextFontStorage getITextFontStorage()
  {
    return (ITextFontStorage) getFontStorage();
  }
}
