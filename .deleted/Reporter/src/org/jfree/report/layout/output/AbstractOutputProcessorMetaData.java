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
 * DefaultOutputProcessorMetaData.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import java.util.HashMap;
import java.util.HashSet;

import org.jfree.fonts.awt.AWTFontRegistry;
import org.jfree.fonts.registry.DefaultFontStorage;
import org.jfree.fonts.registry.FontContext;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.report.layout.text.LegacyFontMetrics;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.util.Configuration;
import org.jfree.util.ExtendedConfiguration;
import org.jfree.util.ExtendedConfigurationWrapper;
import org.jfree.util.Log;

/**
 * Creation-Date: 07.04.2007, 19:21:44
 *
 * @author Thomas Morgner
 */
public abstract class AbstractOutputProcessorMetaData implements OutputProcessorMetaData
{
  protected static class ReusableFontContext implements FontContext
  {
    private boolean antiAliased;
    private double fontSize;
    private boolean embedded;
    private String encoding;


    protected ReusableFontContext()
    {
    }

    public boolean isEmbedded()
    {
      return embedded;
    }

    public void setEmbedded(final boolean embedded)
    {
      this.embedded = embedded;
    }

    public String getEncoding()
    {
      return encoding;
    }

    public void setEncoding(final String encoding)
    {
      this.encoding = encoding;
    }

    public void setAntiAliased(final boolean antiAliased)
    {
      this.antiAliased = antiAliased;
    }

    public void setFontSize(final double fontSize)
    {
      this.fontSize = fontSize;
    }

    /**
     * This is controlled by the output target and the stylesheet. If the output target does not support aliasing, it
     * makes no sense to enable it and all such requests are ignored.
     *
     * @return
     */
    public boolean isAntiAliased()
    {
      return antiAliased;
    }

    /**
     * This is defined by the output target. This is not controlled by the stylesheet.
     *
     * @return
     */
    public boolean isFractionalMetrics()
    {
      return true;
    }

    /**
     * The requested font size. A font may have a fractional font size (ie. 8.5 point). The font size may be influenced
     * by the output target.
     *
     * @return the font size.
     */
    public double getFontSize()
    {
      return fontSize;
    }
  }

  private FontStorage fontStorage;
  private FontRegistry fontRegistry;
  private HashMap numericFeatures;
  private HashMap fontFamilies;
  private HashSet features;
  private Configuration configuration;
  private ReusableFontContext reusableFontContext;
  private HashMap fontMetricsCache;
  private FontMetricsKey lookupKey;

  protected AbstractOutputProcessorMetaData(final Configuration configuration)
  {
    this(configuration, new DefaultFontStorage(new AWTFontRegistry()));
  }


  protected AbstractOutputProcessorMetaData(final Configuration configuration,
                                         final FontStorage fontStorage)
  {
    this.configuration = configuration;
    this.fontRegistry = fontStorage.getFontRegistry();
    this.fontStorage = fontStorage;
    this.features = new HashSet();
    this.numericFeatures = new HashMap();
    this.reusableFontContext = new ReusableFontContext();
    this.fontMetricsCache = new HashMap();
    this.lookupKey = new FontMetricsKey();

    final ExtendedConfiguration extendedConfig = new ExtendedConfigurationWrapper(configuration);

    final double defaultFontSize = extendedConfig.getIntProperty("org.jfree.report.layout.defaults.FontSize", 12);

    fontFamilies = new HashMap();

    setNumericFeatureValue(OutputProcessorFeature.DEFAULT_FONT_SIZE, defaultFontSize);

    final double fontSmoothThreshold =
        extendedConfig.getIntProperty("org.jfree.report.layout.defaults.FontSmoothThreshold", 8);
    setNumericFeatureValue(OutputProcessorFeature.FONT_SMOOTH_THRESHOLD, fontSmoothThreshold);

    if (extendedConfig.getBoolProperty("org.jfree.report.layout.fontrenderer.UseMaxCharBounds", true) == false)
    {
      addFeature(OutputProcessorFeature.LEGACY_LINEHEIGHT_CALC);
    }
    if (extendedConfig.getBoolProperty("org.jfree.report.FixImageResolutionMapping", true))
    {
      addFeature(OutputProcessorFeature.IMAGE_RESOLUTION_MAPPING);
    }

    final double deviceResolution = extendedConfig.getIntProperty("org.jfree.report.layout.DeviceResolution", 72);
    setNumericFeatureValue(OutputProcessorFeature.DEVICE_RESOLUTION, deviceResolution);
    
    setFamilyMapping(null, "SansSerif");
  }

  public Configuration getConfiguration()
  {
    return configuration;
  }

  protected void setFamilyMapping(final String family, final String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    fontFamilies.put(family, name);
  }

  protected void addFeature(final OutputProcessorFeature.BooleanOutputProcessorFeature feature)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    this.features.add(feature);
  }

  protected void removeFeature(final OutputProcessorFeature.BooleanOutputProcessorFeature feature)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    this.features.remove(feature);
  }

  public boolean isFeatureSupported(final OutputProcessorFeature.BooleanOutputProcessorFeature feature)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    return this.features.contains(feature);
  }

  protected void setNumericFeatureValue(final OutputProcessorFeature.NumericOutputProcessorFeature feature,
                                        final double value)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    numericFeatures.put(feature, new Double(value));
  }

  public double getNumericFeatureValue(final OutputProcessorFeature.NumericOutputProcessorFeature feature)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    final Double d = (Double) numericFeatures.get(feature);
    if (d == null)
    {
      return 0;
    }
    return d.doubleValue();
  }

  public boolean isContentSupported(final Object content)
  {
    return true;
  }

  protected FontRegistry getFontRegistry()
  {
    return fontRegistry;
  }

  protected FontStorage getFontStorage()
  {
    return fontStorage;
  }

  public String getNormalizedFontFamilyName(final String name)
  {
    final String normalizedFontFamily = (String) fontFamilies.get(name);
    if (normalizedFontFamily == null)
    {
      return name;
    }
    return normalizedFontFamily;
  }

  private static class FontMetricsKey
  {
    private String fontFamily;
    private double fontSize;
    private boolean antiAliased;
    private boolean embedded;
    private String encoding;
    private boolean italics;
    private boolean bold;

    public FontMetricsKey()
    {
    }

    public FontMetricsKey(final FontMetricsKey derived)
    {
      this.fontFamily = derived.fontFamily;
      this.fontSize = derived.fontSize;
      this.antiAliased = derived.antiAliased;
      this.embedded = derived.embedded;
      this.encoding = derived.encoding;
      this.italics = derived.italics;
      this.bold = derived.bold;
    }

    public String getFontFamily()
    {
      return fontFamily;
    }

    public void setFontFamily(final String fontFamily)
    {
      this.fontFamily = fontFamily;
    }

    public double getFontSize()
    {
      return fontSize;
    }

    public void setFontSize(final double fontSize)
    {
      this.fontSize = fontSize;
    }

    public boolean isAntiAliased()
    {
      return antiAliased;
    }

    public void setAntiAliased(final boolean antiAliased)
    {
      this.antiAliased = antiAliased;
    }

    public boolean isEmbedded()
    {
      return embedded;
    }

    public void setEmbedded(final boolean embedded)
    {
      this.embedded = embedded;
    }

    public String getEncoding()
    {
      return encoding;
    }

    public void setEncoding(final String encoding)
    {
      this.encoding = encoding;
    }


    public boolean isItalics()
    {
      return italics;
    }

    public void setItalics(final boolean italics)
    {
      this.italics = italics;
    }

    public boolean isBold()
    {
      return bold;
    }

    public void setBold(final boolean bold)
    {
      this.bold = bold;
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

      final FontMetricsKey that = (FontMetricsKey) o;

      if (antiAliased != that.antiAliased)
      {
        return false;
      }
      if (embedded != that.embedded)
      {
        return false;
      }
      if (bold != that.bold)
      {
        return false;
      }
      if (italics != that.italics)
      {
        return false;
      }
      if (Double.compare(that.fontSize, fontSize) != 0)
      {
        return false;
      }
      if (encoding != null ? !encoding.equals(that.encoding) : that.encoding != null)
      {
        return false;
      }
      if (!fontFamily.equals(that.fontFamily))
      {
        return false;
      }

      return true;
    }

    public int hashCode()
    {
      int result = fontFamily.hashCode();
      final long temp = fontSize != +0.0d ? Double.doubleToLongBits(fontSize) : 0L;
      result = 29 * result + (int) (temp ^ (temp >>> 32));
      result = 29 * result + (antiAliased ? 1 : 0);
      result = 29 * result + (embedded ? 1 : 0);
      result = 29 * result + (italics ? 1 : 0);
      result = 29 * result + (bold ? 1 : 0);
      result = 29 * result + (encoding != null ? encoding.hashCode() : 0);
      return result;
    }
  }


  /**
   * Returns the font metrics for the font specified in the style sheet.
   * <p/>
   * <B>NOTE: This method will throw an <code>IllegalArgumentException</code> if the specified font family can not be
   * found and the default font family can not be found</B>
   *
   * @param styleSheet ths style sheet from which the font information will be extracted
   * @return FontMetrics for the specified font. If the font family can not be found, the FontMetrics for the default
   *         font family will be returned
   * @throws IllegalArgumentException indicated the font metrics could not be determined (this is thrown since methods
   *                                  depending upon this method can not handle a <code>null</code> return).
   */
  public FontMetrics getFontMetrics(final StyleSheet styleSheet) throws IllegalArgumentException
  {
    final String fontFamily = getNormalizedFontFamilyName((String) styleSheet.getStyleProperty(TextStyleKeys.FONT));
    if (fontFamily == null)
    {
      // If this case happens, the stylesheet is not implemented correctly. At that point,
      // we have to assume that the whole engine is no longer behaving valid and therefore we
      // abort early.
      final String errorMessage = "No font family specified.";
      Log.error(errorMessage);
      throw new IllegalArgumentException(errorMessage);
    }

    final double fontSize = styleSheet.getDoubleStyleProperty
        (TextStyleKeys.FONTSIZE, getNumericFeatureValue(OutputProcessorFeature.DEFAULT_FONT_SIZE));

    final boolean antiAliasing = RenderUtility.isFontSmooth(styleSheet, this);
    final String encoding = (String) styleSheet.getStyleProperty(TextStyleKeys.FONTENCODING);
    final boolean embedded =
        isFeatureSupported(OutputProcessorFeature.EMBED_ALL_FONTS) ||
        styleSheet.getBooleanStyleProperty(TextStyleKeys.EMBEDDED_FONT);
    final boolean bold = styleSheet.getBooleanStyleProperty(TextStyleKeys.BOLD, false);
    final boolean italics = styleSheet.getBooleanStyleProperty(TextStyleKeys.ITALIC, false);

    lookupKey.setAntiAliased(antiAliasing);
    lookupKey.setEncoding(encoding);
    lookupKey.setEmbedded(embedded);
    lookupKey.setFontFamily(fontFamily);
    lookupKey.setFontSize(fontSize);
    lookupKey.setBold(bold);
    lookupKey.setItalics(italics);

    final FontMetrics cached = (FontMetrics) fontMetricsCache.get(lookupKey);
    if (cached != null)
    {
      return cached;
    }

    final FontRegistry registry = getFontRegistry();
    FontFamily family = registry.getFontFamily(fontFamily);
    if (family == null)
    {
      Log.warn("Unable to lookup the font family: " + fontFamily);

      // Get the default font name
      final String fallBack = getNormalizedFontFamilyName(null);
      if (fallBack == null)
      {
        // If this case happens, the output-processor meta-data does not provide a sensible
        // fall-back value. As we cannot continue without a font, we fail here instead of
        // waiting for a NullPointer or other weird error later.
        final String errorMessage = "No default family defined, aborting.";
        Log.error(errorMessage);
        throw new IllegalArgumentException(errorMessage);
      }

      family = registry.getFontFamily(fallBack);
      if (family == null)
      {
        // If this case happens, the output-processor meta-data does not provide a sensible
        // fall-back value. As we cannot continue without a font, we fail here instead of
        // waiting for a NullPointer or other weird error later.
        final String errorMessage = "Default family is invalid. Aborting.";
        Log.error(errorMessage);
        throw new IllegalArgumentException(errorMessage);
      }
    }


    reusableFontContext.setAntiAliased(antiAliasing);
    reusableFontContext.setFontSize(fontSize);
    reusableFontContext.setEncoding(encoding);
    reusableFontContext.setEmbedded(embedded);

    final FontRecord record = family.getFontRecord(bold, italics);
    final FontMetrics fm = getFontStorage().getFontMetrics(record.getIdentifier(), reusableFontContext);
    if (fm == null)
    {
      // If this case happens, then the previous steps of mapping the font name into sensible
      // defaults failed. The font-system's font-registry is not in sync with the actual font-metrics
      // provider (which indicates that the LibFonts font-system implementation is invalid).
      throw new NullPointerException("FontMetrics returned from factory is null.");
    }

    if (isFeatureSupported(OutputProcessorFeature.LEGACY_LINEHEIGHT_CALC))
    {
      // Wrap the font metrics into the legacy-metrics ..
      final LegacyFontMetrics legacyFontMetrics = new LegacyFontMetrics(fm, fontSize);
      fontMetricsCache.put(new FontMetricsKey(lookupKey), legacyFontMetrics);
      return legacyFontMetrics;
    }

    fontMetricsCache.put(new FontMetricsKey(lookupKey), fm);
    return fm;
  }
}
