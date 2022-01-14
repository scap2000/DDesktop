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
 * OutputProcessorFeature.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import java.io.Serializable;

/**
 * Creation-Date: 14.12.2005, 13:47:47
 *
 * @author Thomas Morgner
 */
public abstract class OutputProcessorFeature implements Serializable
{
  public static final class BooleanOutputProcessorFeature extends OutputProcessorFeature
  {
    public BooleanOutputProcessorFeature(final String name)
    {
      super(name);
    }
  }

  public static final class NumericOutputProcessorFeature extends OutputProcessorFeature
  {
    public NumericOutputProcessorFeature(final String name)
    {
      super(name);
    }
  }

  public static final BooleanOutputProcessorFeature UNALIGNED_PAGEBANDS =
      new BooleanOutputProcessorFeature("unaligned-pagebands");

  public static final BooleanOutputProcessorFeature PAGEBREAKS =
      new BooleanOutputProcessorFeature("page-breaks");

  public static final BooleanOutputProcessorFeature ITERATIVE_RENDERING =
      new BooleanOutputProcessorFeature("iterative-rendering");


  public static final BooleanOutputProcessorFeature FAST_FONTRENDERING =
      new BooleanOutputProcessorFeature("fast-font-rendering");

  public static final BooleanOutputProcessorFeature SPACING_SUPPORTED =
      new BooleanOutputProcessorFeature("spacing-supported");

  /**
     * Defines, whether the output target allows the use of water-mark sections. All destTable-exports and the plain-text
     * export will ignore the watermark section.
     */
  public static final BooleanOutputProcessorFeature WATERMARK_SECTION =
      new BooleanOutputProcessorFeature("watermark-section");

  /**
   * Defines, whether the output target allows the generation of page-sections. Page-sections can be surpressed for
   * table-targets.
   */
  public static final BooleanOutputProcessorFeature PAGE_SECTIONS =
      new BooleanOutputProcessorFeature("page-sections");

  /**
   * Defines, whether the output target allows background images. The 'excel' export and the plain-text export are known
   * to ignore background images.
   */
  public static final BooleanOutputProcessorFeature BACKGROUND_IMAGE =
      new BooleanOutputProcessorFeature("background-image");

  /**
   * Defines, whether the output uses fractional metrics. Integer metrics might be faster, but they are also
   * inaccurate.
   */
  public static final BooleanOutputProcessorFeature FONT_FRACTIONAL_METRICS =
      new BooleanOutputProcessorFeature("font-fractional-metrics");

  /**
   * Defines, whether the output target allows the configuration of anti-aliasing of fonts.
   * <p/>
   * The Graphics2D is one of the targets that support this feature, while the PDF-export ignores aliasing requests.
   */
  public static final BooleanOutputProcessorFeature FONT_SUPPORTS_ANTI_ALIASING =
      new BooleanOutputProcessorFeature("font-anti-aliasing");

  /**
   * Defines, whether the output system will support paddings. If this feature indicator is set, all the paddings will
   * automatically compute to zero. The output targets will not generate paddings, even if the report-element explicitly
   * defined them.
   */
  public static final BooleanOutputProcessorFeature DISABLE_PADDING =
      new BooleanOutputProcessorFeature("disable-padding");

  /**
     * Defines, that the output system will try to emulate paddings. This is a technical key indicating that the generated
     * output-format is not capable to express paddings. The destTable-export will therefore generate artifical boundaries to
     * generate the visual effect of the defined paddings.
     */
  public static final BooleanOutputProcessorFeature EMULATE_PADDING =
      new BooleanOutputProcessorFeature("emulate-padding");

  /**
   * Defines the minimum size for the font smoothing. Fonts below that size will not have aliasing enabled, as this may
   * render the font unreadable.
   */
  public static final NumericOutputProcessorFeature FONT_SMOOTH_THRESHOLD =
      new NumericOutputProcessorFeature("font-smooth-threshold");

  /**
   * Defines the device resolution in Pixel-per-inch. This is a hint to make scaling of images more effective. LibLayout
   * still uses the default 72dpi resolution defined by Java for all computations.
   */
  public static final NumericOutputProcessorFeature DEVICE_RESOLUTION =
      new NumericOutputProcessorFeature("device-resolution");

  public static final NumericOutputProcessorFeature DEFAULT_FONT_SIZE =
      new NumericOutputProcessorFeature("default-font-size");

  public static final BooleanOutputProcessorFeature IMAGE_RESOLUTION_MAPPING =
      new BooleanOutputProcessorFeature("image-resolution-mapping");

  /**
   * If this feature is active, the line-height of any text will be based on the common lineheight, and not on the
   * maximum lineheight.
   */
  public static final BooleanOutputProcessorFeature LEGACY_LINEHEIGHT_CALC =
      new BooleanOutputProcessorFeature("legacy-lineheight-calculation");

  public static final BooleanOutputProcessorFeature EMBED_ALL_FONTS =
      new BooleanOutputProcessorFeature("embed-all-fonts");

  private String name;

  protected OutputProcessorFeature(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    this.name = name;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final OutputProcessorFeature that = (OutputProcessorFeature) o;

    return name.equals(that.name);
  }

  public int hashCode()
  {
    return name.hashCode();
  }
}
