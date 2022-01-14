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
 * DrawableUtility.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.output;

import com.keypoint.PngEncoder;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.io.IOException;

import org.jfree.report.DefaultImageReference;
import org.jfree.report.ElementAlignment;
import org.jfree.report.ImageContainer;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.FontSmooth;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.util.ImageUtils;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.ui.Drawable;
import org.jfree.util.Log;
import org.jfree.util.WaitingImageObserver;

/**
 * Creation-Date: 12.05.2007, 15:58:43
 *
 * @author Thomas Morgner
 */
public class RenderUtility
{
  private RenderUtility()
  {
  }


  public static boolean isFontSmooth(final StyleSheet styleSheet,
                                     final OutputProcessorMetaData metaData)
  {
    final double fontSize = styleSheet.getDoubleStyleProperty
        (TextStyleKeys.FONTSIZE, metaData.getNumericFeatureValue(OutputProcessorFeature.DEFAULT_FONT_SIZE));

    final FontSmooth smoothing = (FontSmooth) styleSheet.getStyleProperty(TextStyleKeys.FONT_SMOOTH);
    final boolean antiAliasing;
    if (FontSmooth.NEVER.equals(smoothing))
    {
      antiAliasing = false;
    }
    else if (FontSmooth.AUTO.equals(smoothing) &&
             fontSize <= metaData.getNumericFeatureValue(OutputProcessorFeature.FONT_SMOOTH_THRESHOLD))
    {
      antiAliasing = false;
    }
    else
    {
      antiAliasing = true;
    }
    return antiAliasing;
  }

  /**
   * Encodes the given image as PNG, stores the image in the generated file and returns the name of the new image file.
   *
   * @param image the image to be encoded
   * @return the name of the image, never null.
   *
   * @throws IOException if an IO erro occured.
   */
  public static byte[] encodeImage(final Image image)
  {
    // quick caching ... use a weak list ...
    final WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();

    final PngEncoder encoder = new PngEncoder(image,
        PngEncoder.ENCODE_ALPHA, PngEncoder.FILTER_NONE, 5);
    return encoder.pngEncode();
  }

  public static Image scaleImage(final Image img,
                                        final int targetWidth,
                                        final int targetHeight,
                                        final Object hintValue,
                                        final boolean higherQuality)
  {
    final int type = BufferedImage.TYPE_INT_ARGB;

    Image ret = img;
    int w;
    int h;
    do
    {

      if (higherQuality)
      {
        final int imageWidth = ret.getWidth(null);
        final int imageHeight = ret.getHeight(null);
        if (imageWidth < targetWidth)
        {
          // This is a up-scale operation.
          w = Math.min(imageWidth << 1, targetWidth);
        }
        else if (imageWidth > targetWidth)
        {
          // downscale
          w = Math.max(imageWidth >> 1, targetWidth);
        }
        else
        {
          w = imageWidth;
        }

        if (imageHeight < targetHeight)
        {
          // This is a up-scale operation.
          h = Math.min(imageHeight << 1, targetHeight);
        }
        else if (imageHeight > targetHeight)
        {
          // downscale
          h = Math.max(imageHeight >> 1, targetHeight);
        }
        else
        {
          h = imageHeight;
        }
      }
      else
      {
        w = targetWidth;
        h = targetHeight;
      }

      final BufferedImage tmp = new BufferedImage(w, h, type);
      final Graphics2D g2 = tmp.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hintValue);
      // this one scales the image ..
      if (ret instanceof BufferedImage)
      {
        if (g2.drawImage(ret, 0, 0, w, h, null) == false)
        {
          Log.debug("Failed to scale the image. This should not happen.");
        }
      }
      else
      {
        final WaitingImageObserver obs = new WaitingImageObserver(ret);
        while (g2.drawImage(ret, 0, 0, w, h, null) == false)
        {
          obs.waitImageLoaded();
          if (obs.isError())
          {
            Log.warn("Error while loading the image during the rendering.");
            break;
          }
        }

      }
      g2.dispose();

      ret = tmp;
    }
    while (w != targetWidth || h != targetHeight);

    return ret;
  }

  public static ImageContainer createImageFromDrawable (final Drawable drawable,
                                                        final StrictBounds rect,
                                                        final RenderNode box,
                                                        final OutputProcessorMetaData metaData)
  {
    final int imageWidth = (int) StrictGeomUtility.toExternalValue(rect.getWidth());
    final int imageHeight = (int) StrictGeomUtility.toExternalValue(rect.getHeight());

    if (imageWidth == 0 && imageHeight == 0)
    {
      return null;
    }

    final double devResolution = metaData.getNumericFeatureValue(OutputProcessorFeature.DEVICE_RESOLUTION);
    final double scale;
    if (metaData.isFeatureSupported(OutputProcessorFeature.IMAGE_RESOLUTION_MAPPING) &&
        devResolution > 0)
    {
      scale = devResolution / 72.0;
    }
    else
    {
      scale = 1;
    }

    final Image image = ImageUtils.createTransparentImage
        ((int) (imageWidth * scale), (int) (imageHeight * scale));
    final Graphics2D g2 = (Graphics2D) image.getGraphics();
    g2.scale(scale, scale);
    // the clipping bounds are a sub-area of the whole drawable
    // we only want to print a certain area ...

    final StyleSheet style = box.getStyleSheet();
    final String fontName = (String) style.getStyleProperty(TextStyleKeys.FONT);
    final int fontSize = style.getIntStyleProperty(TextStyleKeys.FONTSIZE, 8);
    final boolean bold = style.getBooleanStyleProperty(TextStyleKeys.BOLD);
    final boolean italics = style.getBooleanStyleProperty(TextStyleKeys.ITALIC);
    if (bold && italics)
    {
      g2.setFont(new Font(fontName, Font.BOLD | Font.ITALIC, fontSize));
    }
    else if (bold)
    {
      g2.setFont(new Font(fontName, Font.BOLD, fontSize));
    }
    else if (italics)
    {
      g2.setFont(new Font(fontName, Font.ITALIC, fontSize));
    }
    else
    {
      g2.setFont(new Font(fontName, Font.PLAIN, fontSize));
    }

    g2.setStroke((Stroke) style.getStyleProperty(ElementStyleKeys.STROKE));
    final Paint extPaint = (Paint) style.getStyleProperty(ElementStyleKeys.EXTPAINT);
    if (extPaint != null)
    {
      g2.setPaint(extPaint);
    }
    else
    {
      g2.setPaint((Paint) style.getStyleProperty(ElementStyleKeys.PAINT));
    }

    drawable.draw(g2, new Rectangle2D.Double(0, 0, imageWidth, imageHeight));
    g2.dispose();

    try
    {
      return new DefaultImageReference(image);
    }
    catch (IOException e1)
    {
      Log.warn(
          "Unable to fully load a given image. (It should not happen here.)");
      return null;
    }
  }


  public static long computeHorizontalAlignment(final ElementAlignment alignment, final long width, final long imageWidth)
  {
    if (ElementAlignment.RIGHT.equals(alignment))
    {
      return Math.max(0, width - imageWidth);
    }
    if (ElementAlignment.CENTER.equals(alignment))
    {
      return Math.max(0, (width - imageWidth) / 2);
    }
    return 0;
  }

  public static long computeVerticalAlignment(final ElementAlignment alignment, final long height, final long imageHeight)
  {
    if (ElementAlignment.BOTTOM.equals(alignment))
    {
      return Math.max(0, height - imageHeight);
    }
    if (ElementAlignment.MIDDLE.equals(alignment))
    {
      return Math.max(0, (height - imageHeight) / 2);
    }
    return 0;
  }


}
