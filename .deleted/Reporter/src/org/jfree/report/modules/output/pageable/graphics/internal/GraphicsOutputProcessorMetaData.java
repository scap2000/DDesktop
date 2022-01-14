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
 * GraphicsOutputProcessorMetaData.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.graphics.internal;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.report.layout.output.AbstractOutputProcessorMetaData;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 02.01.2006, 19:57:08
 *
 * @author Thomas Morgner
 */
public class GraphicsOutputProcessorMetaData extends AbstractOutputProcessorMetaData
{
  public GraphicsOutputProcessorMetaData(final Configuration configuration)
  {
    this(configuration, new DefaultFontStorage(new AWTFontRegistry()));
  }

  public GraphicsOutputProcessorMetaData(final Configuration configuration,
                                         final FontStorage storage)
  {
    super(configuration, storage);
    addFeature(OutputProcessorFeature.FAST_FONTRENDERING);
    addFeature(OutputProcessorFeature.BACKGROUND_IMAGE);
    addFeature(OutputProcessorFeature.PAGE_SECTIONS);
    addFeature(OutputProcessorFeature.PAGEBREAKS);
    addFeature(OutputProcessorFeature.SPACING_SUPPORTED);

    if ("true".equals(configuration.getConfigProperty
        ("org.jfree.report.modules.output.pageable.graphics.WatermarkPrinted")))
    {
      addFeature(OutputProcessorFeature.WATERMARK_SECTION);
    }
  }

  public FontFamily getDefaultFontFamily()
  {
    return getFontRegistry().getFontFamily("SansSerif");
  }

  public String getExportDescriptor()
  {
    return "pageable/X-AWT-Graphics";
  }
}
