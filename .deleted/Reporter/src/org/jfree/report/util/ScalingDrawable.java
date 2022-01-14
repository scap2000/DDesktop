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
 * ScalingDrawable.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.util;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.jfree.report.ResourceBundleFactory;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.style.StyleSheet;
import org.jfree.ui.Drawable;
import org.jfree.util.Configuration;

/**
 * A drawable implementation that applies scaling to the wrapped up drawable object.
 *
 * @author Thomas Morgner
 */
public class ScalingDrawable implements ReportDrawable
{
  /**
   * The horizontal scale factor.
   */
  private float scaleX;
  /**
   * The vertical scale factor.
   */
  private float scaleY;
  /**
   * The drawable that should be drawn.
   */
  private Drawable drawable;

  /**
   * The resource-bundle factory used if the drawable is a {@link ReportDrawable}.
   */
  private ResourceBundleFactory resourceBundleFactory;
  /**
   * The layout-support used if the drawable is a {@link ReportDrawable}.
   */
  private LayoutSupport layoutSupport;
  /**
   * The report configuration used if the drawable is a {@link ReportDrawable}.
   */
  private Configuration configuration;
  /**
   * The stylesheet of the element containing the drawable that is used if the drawable is a {@link ReportDrawable}.
   */
  private StyleSheet styleSheet;

  /**
   * Default constructor. Initializes the scaling to 1.
   */
  public ScalingDrawable()
  {
    scaleX = 1;
    scaleY = 1;
  }

  /**
   * Returns the drawable contained in this wrapper or null, if there is no drawable given yet.
   *
   * @return the drawable.
   */
  public Drawable getDrawable()
  {
    return drawable;
  }

  /**
   * Defines the drawable contained in this wrapper or null, if there is no drawable given yet.
   *
   * @param drawable the drawable.
   */
  public void setDrawable(final Drawable drawable)
  {
    this.drawable = drawable;
  }

  /**
   * Returns the vertical scale factor.
   *
   * @return the scale factor.
   */
  public float getScaleY()
  {
    return scaleY;
  }

  /**
   * Defines the vertical scale factor.
   *
   * @param scaleY the scale factor.
   */
  public void setScaleY(final float scaleY)
  {
    this.scaleY = scaleY;
  }

  /**
   * Returns the horizontal scale factor.
   *
   * @return the scale factor.
   */
  public float getScaleX()
  {
    return scaleX;
  }

  /**
   * Defines the horizontal scale factor.
   *
   * @param scaleX the scale factor.
   */
  public void setScaleX(final float scaleX)
  {
    this.scaleX = scaleX;
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(final Graphics2D g2, final Rectangle2D area)
  {
    if (drawable == null)
    {
      return;
    }

    if (drawable instanceof ReportDrawable)
    {
      final ReportDrawable reportDrawable = (ReportDrawable) drawable;
      reportDrawable.setLayoutSupport(getLayoutSupport());
      reportDrawable.setConfiguration(getConfiguration());
      reportDrawable.setResourceBundleFactory(getResourceBundleFactory());
      reportDrawable.setStyleSheet(getStyleSheet());
    }

    final Graphics2D derived = (Graphics2D) g2.create();
    derived.scale(scaleX, scaleY);
    final Rectangle2D scaledArea = (Rectangle2D) area.clone();
    scaledArea.setRect(scaledArea.getX() * scaleX, scaledArea.getY() * scaleY,
            scaledArea.getWidth() * scaleX, scaledArea.getHeight() * scaleY);
    drawable.draw(derived, scaledArea);
    derived.dispose();
  }

  /**
   * Returns the stylesheet of the element containing this drawable.
   *
   * @return the element's stylesheet.
   */
  public StyleSheet getStyleSheet()
  {
    return styleSheet;
  }

  /**
   * Defines the stylesheet of the element containing this drawable.
   *
   * @param styleSheet the element's stylesheet.
   */
  public void setStyleSheet(final StyleSheet styleSheet)
  {
    this.styleSheet = styleSheet;
  }

  /**
   * Returns the resource-bundle factory used if the drawable is a {@link ReportDrawable}.
   *
   * @return the resource-bundle factory.
   */
  public ResourceBundleFactory getResourceBundleFactory()
  {
    return resourceBundleFactory;
  }

  /**
   * Defines the resource-bundle factory used if the drawable is a {@link ReportDrawable}.
   *
   * @param resourceBundleFactory the resource-bundle factory.
   */
  public void setResourceBundleFactory(final ResourceBundleFactory resourceBundleFactory)
  {
    this.resourceBundleFactory = resourceBundleFactory;
  }

  /**
   * Returns the layout-support used if the drawable is a {@link ReportDrawable}.
   *
   * @return the layout support.
   */
  public LayoutSupport getLayoutSupport()
  {
    return layoutSupport;
  }

  /**
   * Defines the layout-support used if the drawable is a {@link ReportDrawable}.
   *
   * @param layoutSupport the layout support.
   */
  public void setLayoutSupport(final LayoutSupport layoutSupport)
  {
    this.layoutSupport = layoutSupport;
  }

  /**
   * Returns the report configuration used if the drawable is a {@link ReportDrawable}.
   *
   * @return the report's configuration.
   */
  public Configuration getConfiguration()
  {
    return configuration;
  }

  /**
   * Defines the report configuration used if the drawable is a {@link ReportDrawable}.
   *
   * @param configuration the report's configuration.
   */
  public void setConfiguration(final Configuration configuration)
  {
    this.configuration = configuration;
  }
}
