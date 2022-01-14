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
 * RenderedContentNode.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import org.jfree.report.ImageContainer;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.ui.ExtendedDrawable;

/**
 * Creation-Date: 03.04.2007, 15:10:19
 *
 * @author Thomas Morgner
 */
public class RenderableReplacedContent extends RenderNode
{
  private transient Object contentCached;
  private Object content;
  private ResourceKey source;
  private boolean imageResolutionMapping;
  private boolean avoidPagebreaksInside;
  private long contentWidth;
  private long contentHeight;

  private RenderLength requestedWidth;
  private RenderLength requestedHeight;
  private RenderLength minimumWidth;
  private RenderLength minimumHeight;
  private RenderLength maximumWidth;
  private RenderLength maximumHeight;

  public RenderableReplacedContent(final StyleSheet styleSheet,
                                   final Object content,
                                   final ResourceKey source,
                                   final OutputProcessorMetaData metaData)
  {
    super(styleSheet);
    this.content = content;
    this.source = source;
    this.avoidPagebreaksInside = styleSheet.getBooleanStyleProperty(ElementStyleKeys.AVOID_PAGEBREAK_INSIDE);
    setMajorAxis(VERTICAL_AXIS);
    setMinorAxis(HORIZONTAL_AXIS);

    minimumWidth = RenderLength.createFromRaw(styleSheet.getDoubleStyleProperty(ElementStyleKeys.MIN_WIDTH, 0));
    minimumHeight = RenderLength.createFromRaw(styleSheet.getDoubleStyleProperty(ElementStyleKeys.MIN_HEIGHT, 0));
    maximumWidth = RenderLength.createFromRaw(styleSheet.getDoubleStyleProperty(ElementStyleKeys.MAX_WIDTH, Short.MAX_VALUE));
    maximumHeight = RenderLength.createFromRaw(styleSheet.getDoubleStyleProperty(ElementStyleKeys.MAX_HEIGHT, Short.MAX_VALUE));

    final Float prefWidth = (Float) styleSheet.getStyleProperty(ElementStyleKeys.WIDTH, null);
    if (prefWidth != null)
    {
      requestedWidth = RenderLength.createFromRaw(prefWidth.doubleValue());
    }
    else
    {
      requestedWidth = RenderLength.AUTO;
    }

    final Float prefHeight = (Float) styleSheet.getStyleProperty(ElementStyleKeys.HEIGHT, null);
    if (prefHeight != null)
    {
      requestedHeight = RenderLength.createFromRaw(prefHeight.doubleValue());
    }
    else
    {
      requestedHeight = RenderLength.AUTO;
    }

    if (content instanceof ImageContainer)
    {
      this.imageResolutionMapping = metaData.isFeatureSupported(OutputProcessorFeature.IMAGE_RESOLUTION_MAPPING);
      final double displayResolution = metaData.getNumericFeatureValue(OutputProcessorFeature.DEVICE_RESOLUTION);
      final double correctionFactorPxToPoint = 72.0 / displayResolution;

      final ImageContainer ir = (ImageContainer) content;
      final double scaleX = ir.getScaleX();
      final double scaleY = ir.getScaleY();
      if (imageResolutionMapping)
      {
        contentWidth = StrictGeomUtility.toInternalValue(ir.getImageWidth() * scaleX * correctionFactorPxToPoint);
        contentHeight = StrictGeomUtility.toInternalValue(ir.getImageHeight() * scaleY * correctionFactorPxToPoint);
      }
      else
      {
        contentWidth = StrictGeomUtility.toInternalValue(ir.getImageWidth() * scaleX);
        contentHeight = StrictGeomUtility.toInternalValue(ir.getImageHeight() * scaleY);
      }
    }
    else if (content instanceof ExtendedDrawable)
    {
      final ExtendedDrawable edr = (ExtendedDrawable) content;
      final Dimension preferredSize = edr.getPreferredSize();
      contentWidth = StrictGeomUtility.toInternalValue(preferredSize.getWidth());
      contentHeight = StrictGeomUtility.toInternalValue(preferredSize.getHeight());
    }
    else if (content instanceof Shape)
    {
      final Shape s = (Shape) content;
      final Rectangle2D bounds2D = s.getBounds2D();
      contentWidth = StrictGeomUtility.toInternalValue(bounds2D.getWidth());
      contentHeight = StrictGeomUtility.toInternalValue(bounds2D.getHeight());
    }

    //Log.debug (this);
  }

  public ResourceKey getSource()
  {
    return source;
  }

  public Object getRawObject()
  {
    return content;
  }

  public long getContentWidth()
  {
    return contentWidth;
  }

  public long getContentHeight()
  {
    return contentHeight;
  }

  public RenderLength getRequestedWidth()
  {
    return requestedWidth;
  }

  public RenderLength getRequestedHeight()
  {
    return requestedHeight;
  }

  public boolean isImageResolutionMapping()
  {
    return imageResolutionMapping;
  }

  public boolean isAvoidPagebreaksInside ()
  {
    return avoidPagebreaksInside;
  }

  public long computeWidth (final long bcw)
  {
    final long width = computeWidthInternal(bcw);
    return computeLength(minimumWidth.resolve(bcw), maximumWidth.resolve(bcw), width);
  }

  public long computeHeight (final long bcw, final long computedWidth)
  {
    final long height = computeHeightInternal(bcw, computedWidth);
    return computeLength(minimumHeight.resolve(bcw), maximumHeight.resolve(bcw), height);
  }

  private long computeLength(final long min, final long max, final long pref)
  {
    if (pref > max)
    {
      if (max < min)
      {
        return min;
      }
      return max;
    }

    if (pref < min)
    {
      if (max < min)
      {
        return max;
      }
      return min;
    }

    if (max < pref)
    {
      return max;
    }
    return pref;
  }

  private long computeWidthInternal(final long blockContextWidth)
  {
    if (RenderLength.AUTO.equals(getRequestedWidth()))
    {
      // if width is auto, and height is auto,
      if (RenderLength.AUTO.equals(getRequestedHeight()))
      {
        // use the intrinsic width ..
        return getContentWidth();
      }
      // if height is not auto, but the width is, then compute a width that
      // preserves the aspect ratio.
      else if (getContentHeight() > 0)
      {
        final long height = getRequestedHeight().resolve(blockContextWidth);
        return height * getComputedWidth() / getContentHeight();
      }
      else
      {
        return 0;
      }
    }
    else
    {
      // width is not auto.
      return getRequestedWidth().resolve(blockContextWidth);
    }
  }



  private long computeHeightInternal(final long blockContextWidth, final long computedWidth)
  {
    final RenderLength requestedHeight = getRequestedHeight();
    if (RenderLength.AUTO.equals(getRequestedWidth()))
    {
      // if width is auto, and height is auto,
      if (RenderLength.AUTO.equals(requestedHeight))
      {
        final long contentWidth = getContentWidth();
        if (contentWidth > 0)
        {
          // Intrinsic height must be computed to preserve the aspect ratio.
          return computedWidth * getContentHeight()/contentWidth;
        }

        // use the intrinsic height ..
        return getContentHeight();
      }
      // if height is not auto, then use the declared height.
      else
      {
        // A percentage is now relative to the intrinsinc size.
        // And yes, I'm aware that this is not what the standard says ..
        return requestedHeight.resolve(blockContextWidth);
      }
    }
    else
    {
      // width is not auto.
      // If the height is auto, we have to preserve the aspect ratio ..
      if (RenderLength.AUTO.equals(requestedHeight))
      {
        final long contentWidth = getContentWidth();
        if (contentWidth > 0)
        {
          // Requested height must be computed to preserve the aspect ratio.
          return computedWidth * getContentHeight()/contentWidth;
        }
        else
        {
          return 0;
        }
      }
      else
      {
        // height is something fixed ..
        return requestedHeight.resolve(blockContextWidth);
      }
    }
  }

  public Object getContentCached()
  {
    return contentCached;
  }

  public void setContentCached(final Object contentCached)
  {
    this.contentCached = contentCached;
  }

  /**
   * Computes a relative content-width relative to the current block-context width.
   * @return
   */
  public long getComputedContentWidth()
  {
    return computeWidth(computeBlockContextWidth(this));
  }

  private long computeBlockContextWidth(final RenderNode box)
  {
    final RenderBox parentBlockContext = box.getParent();
    if (parentBlockContext == null)
    {
      final LogicalPageBox logicalPage = box.getLogicalPage();
      if (logicalPage == null)
      {
        return 0;
      }
      return logicalPage.getPageWidth();
    }
    return parentBlockContext.getStaticBoxLayoutProperties().getBlockContextWidth();
  }

  public String toString()
  {
    return "RenderableReplacedContent{" +
//        "content=" + content +
        ", source=" + source +
        ", imageResolutionMapping=" + imageResolutionMapping +
        ", avoidPagebreaksInside=" + avoidPagebreaksInside +
        ", contentWidth=" + contentWidth +
        ", contentHeight=" + contentHeight +
        ", requestedWidth=" + requestedWidth +
        ", requestedHeight=" + requestedHeight +
        ", minimumWidth=" + minimumWidth +
        ", minimumHeight=" + minimumHeight +
        ", maximumWidth=" + maximumWidth +
        ", maximumHeight=" + maximumHeight +
        '}';
  }
}
