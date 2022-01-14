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
 * TextOutputProcessorMetaData.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.pageable.plaintext;

import org.jfree.fonts.monospace.MonospaceFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.report.layout.output.AbstractOutputProcessorMetaData;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 13.05.2007, 13:10:59
 *
 * @author Thomas Morgner
 */
public class TextOutputProcessorMetaData extends AbstractOutputProcessorMetaData
{
  public static final OutputProcessorFeature.NumericOutputProcessorFeature CHAR_WIDTH =
      new OutputProcessorFeature.NumericOutputProcessorFeature ("txt.character-width-pt");
  public static final OutputProcessorFeature.NumericOutputProcessorFeature CHAR_HEIGHT =
      new OutputProcessorFeature.NumericOutputProcessorFeature ("txt.character-height-pt");

  public TextOutputProcessorMetaData(final Configuration configuration,
                                     final float lpi, final float cpi)
  {
    super(configuration, new DefaultFontStorage(new MonospaceFontRegistry(lpi, cpi)));
    setNumericFeatureValue(CHAR_WIDTH, 72.0 / cpi);
    setNumericFeatureValue(CHAR_HEIGHT, 72.0 / lpi);
    addFeature(OutputProcessorFeature.PAGE_SECTIONS);
    addFeature(OutputProcessorFeature.PAGEBREAKS);
  }

  public boolean isFeatureSupported(final OutputProcessorFeature.BooleanOutputProcessorFeature feature)
  {
    if (OutputProcessorFeature.LEGACY_LINEHEIGHT_CALC.equals(feature))
    {
      // This would mess up our beautiful text processing. We hardcode the value here so that no evil (or stupid)
      // user could ever override it.
      return false;
    }
    return super.isFeatureSupported(feature);
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
    return "pageable/text";
  }
}
